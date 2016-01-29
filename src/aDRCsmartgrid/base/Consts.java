/**
 * 
 */
package aDRCsmartgrid.base;

/**
 * @author VijayPakka
 *
 */
public final class Consts {

	/***********************************
	 * Model constants
	 **********************************/

	public static String ADRC_LOGGER_NAME = "ADRCLogger"; // use for debugging, default is false

	/*-----------------
	 * Model control 
	 *-----------------*/	
	public static boolean DEBUG = true;  
	public static boolean VERBOSE = true; 
	public static String FILE_CHART_FORMAT_EXT = ".png";

	//datFiles directory is supposed to be found under the current directory which
	// would be normally where this project resides in Eclipse working space   
	public static String DATA_FILES_FOLDER_NAME = "\\dataFiles"; //data files are us

	public static String BASE_PROFILES_FILENAME = "BaseProfiles.csv";  		//DEM and GEN profiles
	public static String ADRC_PARAMETERS_FILENAME = "ADRCParameters.csv";  	//Parameters of Prosumers

	/*---------------------
	 * Units of Measurements
	 *----------------------*/	

	public static final int HOURS_PER_DAY = 24;
	public static final int DAYS_PER_WEEK = 7;
	public static final int MINUTES_PER_DAY = 1440;
	public static final int DAYS_PER_YEAR = 365;
	public static int SECONDS_PER_DAY = 86400;
	public static final int SECONDS_PER_HALF_HOUR = 1800;


	public static int T_PER_DAY = 48; //time/tick (interval) per day	
	public static int SUMMER=180;

	public static int MONDAY=0;
	public static int TUESDAY=1;
	public static int WENDSDAY=2;
	public static int THURSDAY=3;
	public static int FRIDAY=4;
	public static int SATURDAY=5;
	public static int SUNDAY=6;


	/***********************************
	 * Prosumer constants
	 **********************************/

	public static enum ADRC_TYPE {
		DOM_PROSUMER, IND_PROSUMER, PV_DER, WIND_DER, BIOMASS_DER, CHP_DER, DIESEL_DER, BATTERY_DER
	}

	public static double Ts_StepSize = 1.0; 

	/*------------------------
	 * Scheduling Priorities  
	 *-------------------------*/
	public static final double PRIORITY_FIRST  = 500;  // SGSUnit
	public static final double PRIORITY_SECOND = 400;  // CCUnit


	public static final double TOLERANCE = 0.005;  // for reaching steady state
	
}
