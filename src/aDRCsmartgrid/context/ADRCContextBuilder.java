/**
 * 
 */
package aDRCsmartgrid.context;

/**
 * @author VijayPakka
 *
 */

import java.util.LinkedHashMap;
import java.util.WeakHashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkFactory;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.graph.Network;
import aDRCsmartgrid.base.Consts.ADRC_TYPE;
import aDRCsmartgrid.data.ADRCParams;
import aDRCsmartgrid.agents.Utility;
import aDRCsmartgrid.agents.BatteryDER;
import aDRCsmartgrid.agents.BiomassDER;
import aDRCsmartgrid.agents.CHPDER;
import aDRCsmartgrid.agents.DieselDER;
import aDRCsmartgrid.agents.DomPro;
import aDRCsmartgrid.agents.IndPro;
import aDRCsmartgrid.agents.Prosumer;
import aDRCsmartgrid.agents.Prosumer_Factory;
import aDRCsmartgrid.agents.PVDER;
import aDRCsmartgrid.agents.WindDER;
import aDRCsmartgrid.base.Consts;
import aDRCsmartgrid.utils.ArrayUtils;
import io.CSVReader;



public class ADRCContextBuilder implements ContextBuilder<Object> {

	private ADRCContext adrcMainContext;
	protected int totalNumOfProsumers;
	protected double totalCapacity;
	protected double totalDemand;
	private Parameters params; // parameters for the model run environment

	private void readParams() {
		// get the parameters from the current run environment
		this.params = RunEnvironment.getInstance().getParameters();
		this.adrcMainContext.setRandomSeedValue((Integer)params.getValue("randomSeed"));
		int ticksPerDay = (Integer) this.params.getValue("ticksPerDay");
		this.adrcMainContext.setNbOfTickPerDay(ticksPerDay);
		totalCapacity = (Double)params.getValue("totalCapacity");
		totalDemand = (Double)params.getValue("totalDemand");

		Date startDate;
		try
		{
			startDate = (new SimpleDateFormat("dd/MM/yyyy")).parse((String) this.params.getValue("startDate"));
		}
		catch (ParseException e1)
		{
			// TODO Auto-generated catch block
			System.err.println("ADRCContextBuilder: The start date parameter is in a format which cannot be parsed by this program");
			System.err.println("ADRCContextBuilder: The data will be set to 01/01/2000 by default");
			startDate = new Date(2000, 1, 1);
			e1.printStackTrace();
		}
		this.adrcMainContext.calendar = new GregorianCalendar();
		this.adrcMainContext.calendar.setTime(startDate);
	}  


	private Utility populateContext(WeakHashMap<String, double[]>mapOfProsumerType2BaseProfiles, LinkedHashMap<Integer, ADRCParams>mapOFProIDtoProsumerParams){
		Utility utility = createAndAddUtility(this.adrcMainContext);
		createAndAddProsumers(this.adrcMainContext, utility, mapOfProsumerType2BaseProfiles, mapOFProIDtoProsumerParams);
		return utility;
	}

	private void buildNetworkOfProsumers(Utility utility){
		boolean directed = true;
		NetworkFactory networkFactory = NetworkFactoryFinder.createNetworkFactory(null);
		Network proNet = networkFactory.createNetwork("ProsumerNetwork", this.adrcMainContext, directed);
		for (Prosumer proAgent:(Iterable<Prosumer>) (this.adrcMainContext.getObjects(Prosumer.class))){
			proNet.addEdge(utility, proAgent);
		}
		this.adrcMainContext.setNetworkOfProsumers(proNet);
	}

	private void createAndAddProsumers(ADRCContext mainContext, Utility utility, WeakHashMap<String, double []> mapOfProsumerType2BaseProfiles, LinkedHashMap<Integer, ADRCParams>mapOFProIDtoProsumerParams) {
		Prosumer_Factory proFactory = new Prosumer_Factory(mainContext, utility);

		double [] baseProfile_PV_DER = mapOfProsumerType2BaseProfiles.get("PV_DER");
		double [] baseProfile_WIND_DER = mapOfProsumerType2BaseProfiles.get("WIND_DER");
		double [] baseProfile_DIESEL_DER = mapOfProsumerType2BaseProfiles.get("DIESEL_DER");
		double [] baseProfile_BIOMASS_DER = mapOfProsumerType2BaseProfiles.get("BIOMASS_DER");
		double [] baseProfile_CHP_DER = mapOfProsumerType2BaseProfiles.get("CHP_DER");
		double [] baseProfile_BATTERY_DER = mapOfProsumerType2BaseProfiles.get("BATTERY_DER");
		double [] baseProfile_DOM_PROSUMER = mapOfProsumerType2BaseProfiles.get("DOM_PROSUMER");
		double [] baseProfile_IND_PROSUMER = mapOfProsumerType2BaseProfiles.get("IND_PROSUMER");


		ADRCParams adrcParamData = null;

		int num_Prosumers = mapOFProIDtoProsumerParams.size();
		mainContext.setnum_prosumers(num_Prosumers);

		int num_PV_DER = 0;
		int num_WIND_DER = 0;
		int num_DIESEL_DER = 0;
		int num_BIOMASS_DER = 0;
		int num_CHP_DER = 0;
		int num_BATTERY_DER = 0;
		int num_DOM_PROSUMER = 0;
		int num_IND_PROSUMER = 0;
		Prosumer prosumer;


		for (int i = 1; i<= num_Prosumers; i++){
			ADRCParams adrc_param = mapOFProIDtoProsumerParams.get(i);

			switch (adrc_param.getADRC_Type()) {
			case PV_DER:
				PVDER pvAgent = proFactory.createPVDER(ADRC_TYPE.PV_DER, baseProfile_PV_DER, adrc_param);
				mainContext.add(pvAgent);
				num_PV_DER++;
				break;
			case WIND_DER: 
				WindDER windAgent = proFactory.createWindDER(ADRC_TYPE.WIND_DER, baseProfile_WIND_DER, adrc_param);
				mainContext.add(windAgent);
				num_WIND_DER++;
				break;
			case DIESEL_DER: 
				DieselDER dieselAgent = proFactory.createDieselDER(ADRC_TYPE.DIESEL_DER, baseProfile_DIESEL_DER, adrc_param);
				mainContext.add(dieselAgent);
				num_DIESEL_DER++;
				break;
			case BIOMASS_DER: 
				BiomassDER biomassAgent = proFactory.createBiomassDER(ADRC_TYPE.BIOMASS_DER, baseProfile_BIOMASS_DER, adrc_param);
				mainContext.add(biomassAgent);
				num_BIOMASS_DER++;
				break;
			case CHP_DER: 
				CHPDER chpAgent = proFactory.createCHPDER(ADRC_TYPE.CHP_DER, baseProfile_CHP_DER, adrc_param);
				mainContext.add(chpAgent);
				num_CHP_DER++;
				break;	
			case BATTERY_DER: 
				BatteryDER batteryAgent = proFactory.createBatteryDER(ADRC_TYPE.BATTERY_DER, baseProfile_BATTERY_DER, adrc_param);
				mainContext.add(batteryAgent);
				num_BATTERY_DER++;
				break;
			case DOM_PROSUMER: 
				DomPro domproAgent = proFactory.createDomPro(ADRC_TYPE.DOM_PROSUMER, baseProfile_DOM_PROSUMER, adrc_param);
				mainContext.add(domproAgent);
				num_DOM_PROSUMER++;
				break;
			case IND_PROSUMER: 
				IndPro indproAgent = proFactory.createIndPro(ADRC_TYPE.IND_PROSUMER, baseProfile_IND_PROSUMER, adrc_param);
				mainContext.add(indproAgent);
				num_IND_PROSUMER++;
				break;
			}
		}

	}

	private Utility createAndAddUtility(ADRCContext mainContext) {
		Utility utility = new Utility(mainContext);
		mainContext.add(utility);
		return utility;
	}

	private WeakHashMap readADRCBaseProfiles(){

		String currDir = System.getProperty("user.dir"); 					//this is supposed to be the Eclipse project working space
		String pathToDataFiles = currDir+Consts.DATA_FILES_FOLDER_NAME;
		File parentDataFilesDir= new File(pathToDataFiles);
		File ADRCBaseProfiles_File = new File(parentDataFilesDir,Consts.BASE_PROFILES_FILENAME);

		WeakHashMap<String, double[]> mapOfProsumerType2BaseProfiles = new WeakHashMap<String, double[]> ();

		try {
			CSVReader baseProfileCSVReader = new CSVReader(ADRCBaseProfiles_File);
			System.out.println("baseProfileCSVReader created");
			baseProfileCSVReader.parseByColumn();

			mapOfProsumerType2BaseProfiles.put("PV_DER", ArrayUtils.convertStringArrayToDoubleArray(baseProfileCSVReader.getColumn("PV_DER")));
			mapOfProsumerType2BaseProfiles.put("WIND_DER", ArrayUtils.convertStringArrayToDoubleArray(baseProfileCSVReader.getColumn("WIND_DER")));
			mapOfProsumerType2BaseProfiles.put("DIESEL_DER", ArrayUtils.convertStringArrayToDoubleArray(baseProfileCSVReader.getColumn("DIESEL_DER")));
			mapOfProsumerType2BaseProfiles.put("BIOMASS_DER", ArrayUtils.convertStringArrayToDoubleArray(baseProfileCSVReader.getColumn("BIOMASS_DER")));
			mapOfProsumerType2BaseProfiles.put("CHP_DER", ArrayUtils.convertStringArrayToDoubleArray(baseProfileCSVReader.getColumn("CHP_DER")));
			mapOfProsumerType2BaseProfiles.put("BATTERY_DER", ArrayUtils.convertStringArrayToDoubleArray(baseProfileCSVReader.getColumn("BATTERY_DER")));
			mapOfProsumerType2BaseProfiles.put("DOM_PROSUMER", ArrayUtils.convertStringArrayToDoubleArray(baseProfileCSVReader.getColumn("DOM_PROSUMER")));
			mapOfProsumerType2BaseProfiles.put("IND_PROSUMER", ArrayUtils.convertStringArrayToDoubleArray(baseProfileCSVReader.getColumn("IND_PROSUMER")));
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + ADRCBaseProfiles_File.getAbsolutePath());
			e.printStackTrace();
			RunEnvironment.getInstance().endRun();
		}

		return mapOfProsumerType2BaseProfiles;
	}

	private LinkedHashMap readADRCParameterData(){
		String currDir = System.getProperty("user.dir"); 					//this is supposed to be the Eclipse project working space
		String pathToDataFiles = currDir+Consts.DATA_FILES_FOLDER_NAME;
		File parentDataFilesDir= new File(pathToDataFiles);
		File ADRCParameters_File = new File(parentDataFilesDir,Consts.ADRC_PARAMETERS_FILENAME);

		LinkedHashMap<String, String[]> mapOfParamsToData = new LinkedHashMap<String, String[]>();

		try{
			CSVReader ADRCParamsDataReader = new CSVReader(ADRCParameters_File);
			ADRCParamsDataReader.parseByColumn();			
			mapOfParamsToData.put("prosumerID", ADRCParamsDataReader.getColumn("PROSUMER_ID"));
			mapOfParamsToData.put("name", ADRCParamsDataReader.getColumn("Name"));
			mapOfParamsToData.put("type", ADRCParamsDataReader.getColumn("Type"));
			mapOfParamsToData.put("nomPower", ADRCParamsDataReader.getColumn("NomPower"));
			mapOfParamsToData.put("maxGen", ADRCParamsDataReader.getColumn("MaxGen"));
			mapOfParamsToData.put("minGen", ADRCParamsDataReader.getColumn("MinGen"));
			mapOfParamsToData.put("maxDem", ADRCParamsDataReader.getColumn("MaxDem"));
			mapOfParamsToData.put("minDem", ADRCParamsDataReader.getColumn("MinDem"));

		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + ADRCParameters_File.getAbsolutePath());
			e.printStackTrace();
			RunEnvironment.getInstance().endRun();
		}
		return mapOfParamsToData;
	}

	private LinkedHashMap<Integer, ADRCParams> initializeADRCParams(LinkedHashMap<String, String[]> mapOfADRCParamstoData){

		ArrayList<ADRCParams> list_adrcparams = new ArrayList<ADRCParams>();
		LinkedHashMap<Integer, ADRCParams> mapOfprosumerID2prosumerParams= new LinkedHashMap<Integer, ADRCParams>();

		int[] PROSUMERID = ArrayUtils.convertStringArrayToIntegerArray(Arrays.copyOf(mapOfADRCParamstoData.get("ProsumerID"), mapOfADRCParamstoData.get("ProsumerID").length));
		String[] name = Arrays.copyOf(mapOfADRCParamstoData.get("name"), mapOfADRCParamstoData.get("name").length);
		ADRC_TYPE[] type = ArrayUtils.convertStringArrayToSGSTYPEArray(Arrays.copyOf(mapOfADRCParamstoData.get("type"), mapOfADRCParamstoData.get("type").length));
		double[] nompower = ArrayUtils.convertStringArrayToDoubleArray(Arrays.copyOf(mapOfADRCParamstoData.get("nomPower"), mapOfADRCParamstoData.get("nomPower").length));
		double[] maxgen = ArrayUtils.convertStringArrayToDoubleArray(Arrays.copyOf(mapOfADRCParamstoData.get("maxGen"), mapOfADRCParamstoData.get("maxGen").length));
		double[] mingen = ArrayUtils.convertStringArrayToDoubleArray(Arrays.copyOf(mapOfADRCParamstoData.get("minGen"), mapOfADRCParamstoData.get("minGen").length));
		double[] maxdem = ArrayUtils.convertStringArrayToDoubleArray(Arrays.copyOf(mapOfADRCParamstoData.get("maxDem"), mapOfADRCParamstoData.get("maxDem").length));
		double[] mindem = ArrayUtils.convertStringArrayToDoubleArray(Arrays.copyOf(mapOfADRCParamstoData.get("minDem"), mapOfADRCParamstoData.get("minDem").length));

		for (int i = 0; i < PROSUMERID.length; i++){
			list_adrcparams.add(new ADRCParams(PROSUMERID[i], name[i], type[i], nompower[i], maxgen[i], mingen[i], maxdem[i], mindem[i]));
		}

		for (int i = 0; i < PROSUMERID.length; i++){
			mapOfprosumerID2prosumerParams.put(PROSUMERID[i], list_adrcparams.get(i));
		}

		return mapOfprosumerID2prosumerParams;
	}

	@Override
	public ADRCContext build(Context<Object> context) {

		Utility utility;
		this.adrcMainContext = new ADRCContext(context);

		WeakHashMap<String, double[]> mapOfProsumerType2BaseProfiles;
		LinkedHashMap<String, String[]> mapOfProsumerParamstoData;
		LinkedHashMap<Integer, ADRCParams> mapOFProIDtoProsumerParams;

		readParams();
		mapOfProsumerType2BaseProfiles = readADRCBaseProfiles();;
		mapOfProsumerParamstoData = readADRCParameterData();
		mapOFProIDtoProsumerParams = initializeADRCParams(mapOfProsumerParamstoData);
		utility = populateContext(mapOfProsumerType2BaseProfiles, mapOFProIDtoProsumerParams);

		buildNetworkOfProsumers(utility);
		return this.adrcMainContext;
	}

}
