package aDRCsmartgrid.agents;

import java.text.DecimalFormat;

import cern.jet.random.Normal;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import aDRCsmartgrid.agents.Utility;
import aDRCsmartgrid.base.Consts.ADRC_TYPE;
import aDRCsmartgrid.context.ADRCContext;
import aDRCsmartgrid.data.ADRCParams;
import aDRCsmartgrid.utils.ArrayUtils;
import aDRCsmartgrid.base.Consts;

public abstract class Prosumer {

	public ADRCContext mainContext;

	protected ADRC_TYPE type;

	protected String name;
	protected int id;

	protected double nomPower;
	protected double maxGen;
	protected double minGen;
	protected double maxDem;
	protected double minDem;

	int sp = 0;								// Settlement Period

	protected double [] baseGenProfile;		// suggested or previous day's planned schedule for the next day
	protected double [] baseDemProfile;		// forecast or expected demand for the next day 
	protected double [] genSchedule; 			// generation schedule determined by the algorithm
	protected double [] baseDemSchedule;		// original demand schedule before DR by AGG_DEM 
	protected double [] demSchedule;			// demand schedule after committing to demand response	
	protected double [] DRSchedule;			// demand response schedule determined by the algorithm
	protected double [] prevSPGenSchedule;


	DecimalFormat df = new DecimalFormat("#.##");

	public int getID(){
		return id;
	}

	public ADRC_TYPE getType(){
		return type;
	}

	public double [] getbaseGenProfile(){
		return this.baseGenProfile;
	}

	public double [] getbaseDemProfile(){
		return this.baseDemProfile;
	}

	public double [] getgenSchedule(){
		return this.genSchedule;
	}

	public double [] getDRSchedule(){
		return this.DRSchedule;
	}

	public double [] getdemSchedule(){
		return this.demSchedule;
	}

	protected void setGenSchedules4FirstTime(double [] baseGenSchedules){
		for (int i = 0; i < Consts.T_PER_DAY; i++)
			this.genSchedule[i] = baseGenSchedules[i];
	}

	protected void setDemSchedules4FirstTime(double [] baseDemSchedules){
		for (int i = 0; i < Consts.T_PER_DAY; i++){
			this.demSchedule[i] = baseDemSchedules[i];
			this.baseDemSchedule[i] = baseDemSchedules[i];
		}
	}
	
	public double [] getPrevSPGenSchedule(){
		return this.prevSPGenSchedule;
	}

	protected double[] calculateStochasticPN(double[] baseProfile, double mFactor){
		double [] randomPN = new double[Consts.T_PER_DAY];
		double avg = ArrayUtils.avg(baseProfile);
		double sd = avg * mFactor;
		for (int i=0; i<randomPN.length; i++) {
			Normal normalDist = RandomHelper.createNormal(baseProfile[i], sd);
			randomPN[i] = normalDist.nextDouble();
		}
		return randomPN;			
	}

	protected double[] calculatePotentialDemandReduction(double[] baseProfile, double eFactor){
		double [] potentialDR = new double[Consts.T_PER_DAY];
		for (int i=0; i<potentialDR.length; i++){
			potentialDR[i] = baseProfile[i] * (-eFactor);
		}
		
		return potentialDR;
	}
	
	protected double [] initializebaseGenProfileForGens(double [] baseProfile){

		double[] initializedArray = baseProfile; 

		switch (this.type) {
		case PV_DER: 
			initializedArray = calculateStochasticPN(baseProfile, 0.0);
			break;
		case WIND_DER: 
			initializedArray = calculateStochasticPN(baseProfile, 0.2);
			break;
		case DIESEL_DER:
			initializedArray = calculateStochasticPN(baseProfile, 0.1);
			break;
		case BIOMASS_DER: 
			initializedArray = calculateStochasticPN(baseProfile, 0.05);
			break;
		case CHP_DER: 
			initializedArray = calculateStochasticPN(baseProfile, 0.0);
			break;
		case BATTERY_DER: 
			initializedArray = calculateStochasticPN(baseProfile, 0.0);
			break;
		}

		return initializedArray;

	}

	protected double [] initializebaseGenProfileForDems(double [] baseProfile){

		double[] initializedArray = baseProfile; 

		switch (this.type) {
		case BATTERY_DER: 
			initializedArray = ArrayUtils.initializeArraytoZeroes(baseProfile);
			break;
		case DOM_PROSUMER: 
			initializedArray = calculatePotentialDemandReduction(baseProfile, 0.1);					//ArrayUtils.initializeArraytoZeroes(baseProfile);
			break;
		case IND_PROSUMER: 
			initializedArray = calculatePotentialDemandReduction(baseProfile, 0.1);					//ArrayUtils.initializeArraytoZeroes(baseProfile);
			break;
		}

		return initializedArray;

	}

	protected double [] initializebaseDemProfileForDems(double [] baseProfile){

		double[] initializedArray = baseProfile;

		switch (this.type) {
		case BATTERY_DER: 
			initializedArray = calculateStochasticPN(baseProfile, 0.0);
			break;
		case DOM_PROSUMER: 
			initializedArray = calculateStochasticPN(baseProfile, 0.2);
			break;
		case IND_PROSUMER: 
			initializedArray = calculateStochasticPN(baseProfile, 0.2);
			break;			
		}

		return initializedArray;
	}

	protected double [] initializebaseDemProfileForGens(double [] baseProfile){

		double[] initializedArray = baseProfile;

		switch (this.type) {
		case PV_DER: 
			initializedArray= ArrayUtils.initializeArraytoZeroes(baseProfile);
			break;
		case WIND_DER: 
			initializedArray= ArrayUtils.initializeArraytoZeroes(baseProfile);
			break;
		case DIESEL_DER: 
			initializedArray = ArrayUtils.initializeArraytoZeroes(baseProfile);
			break;
		case BIOMASS_DER: 
			initializedArray = ArrayUtils.initializeArraytoZeroes(baseProfile);
			break;
		case CHP_DER: 
			initializedArray = ArrayUtils.initializeArraytoZeroes(baseProfile);
			break;
		case BATTERY_DER: 
			initializedArray = ArrayUtils.initializeArraytoZeroes(baseProfile);
			break;
		}

		return initializedArray;
	}


	@ScheduledMethod(start = 0, interval = 1, priority = Consts.PRIORITY_SECOND)
	public void step(){

	}


	public Prosumer(ADRC_TYPE type, ADRCContext context, Utility utility, double [] baseProfile, ADRCParams adrcParam){

		this.mainContext = context;

		this.id = adrcParam.getADRC_ID();
		this.type = type;
		this.name = adrcParam.getADRC_name();
		this.nomPower = adrcParam.getADRC_NomPower();
		this.maxGen = adrcParam.getADRC_MaxGen();
		this.minGen = adrcParam.getADRC_MinGen();
		this.maxDem = adrcParam.getADRC_MaxDem();
		this.minDem = adrcParam.getADRC_MinDem();

		baseGenProfile = new double[Consts.T_PER_DAY];
		baseDemProfile = new double[Consts.T_PER_DAY];
		genSchedule = new double[Consts.T_PER_DAY];
		baseDemSchedule = new double[Consts.T_PER_DAY];
		demSchedule = new double[Consts.T_PER_DAY];

		if(ArrayUtils.isContainNegative(baseProfile)){
			System.arraycopy(baseProfile, 0, baseDemProfile, 0, baseDemProfile.length);
			this.baseDemProfile = initializebaseDemProfileForDems(baseDemProfile);
			this.baseGenProfile = initializebaseGenProfileForDems(baseDemProfile);			
		}
		else{ 
			System.arraycopy(baseProfile, 0, baseGenProfile, 0, baseGenProfile.length);
			this.baseGenProfile = initializebaseGenProfileForGens(baseGenProfile);
			this.baseDemProfile = initializebaseDemProfileForGens(baseGenProfile);
		}

		this.setGenSchedules4FirstTime(this.baseGenProfile);
		this.setDemSchedules4FirstTime(this.baseDemProfile);

	}

}
