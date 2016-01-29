/**
 * 
 */
package aDRCsmartgrid.agents;

/**
 * @author VijayPakka
 *
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.WeakHashMap;

import repast.simphony.engine.schedule.ScheduledMethod;
import aDRCsmartgrid.agents.Prosumer;
import aDRCsmartgrid.base.Consts;
import aDRCsmartgrid.context.ADRCContext;
import aDRCsmartgrid.utils.ArrayUtils;

public class Utility {

	private ADRCContext mainContext;
	private int sp=0;
	private int dayCount=0;
	
	private ArrayList<Prosumer> list_Prosumer;
	private double [] totalBaseGenProfile;
	private double [] totalBaseDemProfile;
	private double [] totalIMBAL;
	private double [] totalGenSchedule;
	private double [] totalDemSchedule;
	
	private WeakHashMap <Integer, double[]> map_Prosumer2GenProfile;
	private WeakHashMap <Integer, double[]> map_Prosumer2DemProfile;
	private WeakHashMap <Integer, double[]> map_Prosumer2BaseDemProfile;
	private WeakHashMap <Integer, double[]> map_Prosumer2BaseGenProfile;
	
	public int getDayCount(){
		return this.dayCount;
	}
	
	public int get_dayCount(){
		return this.dayCount;
	}
	
	public double[] get_totalBaseDemProfile(){
		return this.totalBaseDemProfile;
	}
	
	public double[] get_totalDemProfile(){
		return this.totalDemSchedule;
	}
	
	private WeakHashMap<Integer, double[]> fetchbaseGenProfiles(ArrayList<Prosumer> listOfProsumers) {
        WeakHashMap<Integer, double[]> mapOfProfiles = new WeakHashMap<Integer, double[]>();
		for (Prosumer pro : listOfProsumers){	
			mapOfProfiles.put(pro.getID(), pro.getbaseGenProfile());
		}
		return mapOfProfiles;
	}
	
	private WeakHashMap<Integer, double[]> fetchPrevSPGenProfiles(ArrayList<Prosumer> listOfProsumers) {
        WeakHashMap<Integer, double[]> mapOfProfiles = new WeakHashMap<Integer, double[]>();
		for (Prosumer pro : listOfProsumers){	
			mapOfProfiles.put(pro.getID(), pro.getPrevSPGenSchedule());
		}
		return mapOfProfiles;
	}
	
	private WeakHashMap<Integer, double[]> fetchbaseDemProfiles(ArrayList<Prosumer> listOfProsumers) {
        WeakHashMap<Integer, double[]> mapOfProfiles = new WeakHashMap<Integer, double[]>();
		for (Prosumer pro : listOfProsumers){	
			mapOfProfiles.put(pro.getID(), pro.getbaseDemProfile());
		}
		return mapOfProfiles;
	}
	
	private WeakHashMap<Integer, double[]> fetchGenSchedules(ArrayList<Prosumer> listOfProsumers) {
        WeakHashMap<Integer, double[]> mapOfProfiles = new WeakHashMap<Integer, double[]>();
		for (Prosumer pro : listOfProsumers){	
			mapOfProfiles.put(pro.getID(), pro.getgenSchedule());
		}
		return mapOfProfiles;
	}
	
	private WeakHashMap<Integer, double[]> fetchDemSchedules(ArrayList<Prosumer> listOfProsumers) {
        WeakHashMap<Integer, double[]> mapOfProfiles = new WeakHashMap<Integer, double[]>();
		for (Prosumer pro : listOfProsumers){	
			mapOfProfiles.put(pro.getID(), pro.getdemSchedule());
		}
		return mapOfProfiles;
	}
	
	private double [] calculateTotalBaseDemandProfiles(WeakHashMap<Integer, double[]> mapOfProID2DemProfile){
		double[] totalBaseDemandProfile = new double[Consts.T_PER_DAY];
		
		WeakHashMap<Integer, double[]> mapOfDemProfiles = mapOfProID2DemProfile;
		
		double [][] demProfile2DArray = new double[this.list_Prosumer.size()][Consts.T_PER_DAY];
		int k=0;
		
		for (double [] dp : mapOfDemProfiles.values()){
			demProfile2DArray[k] = dp;
			k++;			
		}
		
		totalBaseDemandProfile = ArrayUtils.sumOfCols2DDoubleArray(demProfile2DArray);
		return totalBaseDemandProfile;
	}
	
	private boolean calculateTolerance_DemGenBalance(WeakHashMap<Integer, double[]> mapOfProID2DemProfile, WeakHashMap<Integer, double[]> mapOfProID2GenProfile, int sp){
		boolean tolerance = false;
		
		WeakHashMap<Integer, double[]> mapOfDemProfiles = mapOfProID2DemProfile;
		WeakHashMap<Integer, double[]> mapOfGenProfiles = mapOfProID2GenProfile;
				
		double [][] demProfile2DArray = new double[this.list_Prosumer.size()][Consts.T_PER_DAY];
		double [][] genProfile2DArray = new double[this.list_Prosumer.size()][Consts.T_PER_DAY];
		
		int i=0, j=0;
		
		for (double [] dp : mapOfDemProfiles.values()){
			demProfile2DArray[i] = dp;
			i++;			
		}
		
		for (double [] gp : mapOfGenProfiles.values()){
			genProfile2DArray[j] = gp;
			j++;			
		}
		
		tolerance = ArrayUtils.testTolerance(demProfile2DArray, genProfile2DArray, sp);
		
		return tolerance;		
	}
	
	@ScheduledMethod(start = 0, interval = 1, priority = Consts.PRIORITY_FIRST)
	public void step(){
				
		if (mainContext.getTickCount()==0){
			this.list_Prosumer = this.mainContext.getListOfProsumers();
			this.mainContext.setnum_prosumers(this.list_Prosumer.size()-1);
			this.map_Prosumer2GenProfile = fetchbaseGenProfiles(list_Prosumer);
			this.map_Prosumer2DemProfile = fetchbaseDemProfiles(list_Prosumer);
			this.map_Prosumer2BaseDemProfile = fetchbaseDemProfiles(list_Prosumer);
			this.totalBaseDemProfile = calculateTotalBaseDemandProfiles(map_Prosumer2BaseDemProfile);
		}
		else{
			this.map_Prosumer2GenProfile = fetchGenSchedules(list_Prosumer);
			this.map_Prosumer2DemProfile = fetchDemSchedules(list_Prosumer);
		}
		
		if (mainContext.getTickCount()!=0){
			this.totalDemSchedule = calculateTotalBaseDemandProfiles(map_Prosumer2DemProfile);
		}
			
		if (this.mainContext.getSP()==48){
			dayCount++;
			this.mainContext.setSP(0);			
		}
		System.out.println("\nSettlement Period:    "+this.mainContext.getSP());
	
	}
	
	public Utility(ADRCContext context) {
		this.mainContext = context;
		
		totalBaseGenProfile = new double[Consts.T_PER_DAY];
		totalBaseDemProfile = new double[Consts.T_PER_DAY];
		totalIMBAL = new double[Consts.T_PER_DAY];
		totalGenSchedule = new double[Consts.T_PER_DAY];
		totalDemSchedule = new double[Consts.T_PER_DAY];
	}
	
}
