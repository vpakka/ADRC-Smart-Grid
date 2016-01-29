package aDRCsmartgrid.agents;

import aDRCsmartgrid.base.Consts.ADRC_TYPE;
import aDRCsmartgrid.context.ADRCContext;
import aDRCsmartgrid.data.ADRCParams;

/**
 * @author VijayPakka
 *
 */

public class Prosumer_Factory {

	private ADRCContext mainContext;
	private Utility utility;

	public WindDER createWindDER(ADRC_TYPE type, double [] baseProfile, ADRCParams adrcParam){
		WindDER windAgent = null;
		windAgent = new WindDER(type, mainContext, utility, baseProfile, adrcParam);
		return windAgent;
	}
	
	public PVDER createPVDER(ADRC_TYPE type, double [] baseProfile, ADRCParams adrcParam){
		PVDER pvAgent = null;
		pvAgent = new PVDER(type, mainContext, utility, baseProfile, adrcParam);
		return pvAgent;
	}
	
	public CHPDER createCHPDER(ADRC_TYPE type, double [] baseProfile, ADRCParams adrcParam){
		CHPDER chpAgent = null;
		chpAgent = new CHPDER(type, mainContext, utility, baseProfile, adrcParam);
		return chpAgent;
	}
	
	public DieselDER createDieselDER(ADRC_TYPE type, double [] baseProfile, ADRCParams adrcParam){
		DieselDER dieselAgent = null;
		dieselAgent = new DieselDER(type, mainContext, utility, baseProfile, adrcParam);
		return dieselAgent;
	}
	
	public BiomassDER createBiomassDER(ADRC_TYPE type, double [] baseProfile, ADRCParams adrcParam){
		BiomassDER biomassAgent = null;
		biomassAgent = new BiomassDER(type, mainContext, utility, baseProfile, adrcParam);
		return biomassAgent;
	}
	
	public BatteryDER createBatteryDER(ADRC_TYPE type, double [] baseProfile, ADRCParams adrcParam){
		BatteryDER batteryAgent = null;
		batteryAgent = new BatteryDER(type, mainContext, utility, baseProfile, adrcParam);
		return batteryAgent;
	}
	
	public IndPro createIndPro(ADRC_TYPE type, double [] baseProfile, ADRCParams adrcParam){
		IndPro indproAgent = null;
		indproAgent = new IndPro(type, mainContext, utility, baseProfile, adrcParam);
		return indproAgent;
	}
	
	public DomPro createDomPro(ADRC_TYPE type, double [] baseProfile, ADRCParams adrcParam){
		DomPro domproAgent = null;
		domproAgent = new DomPro(type, mainContext, utility, baseProfile, adrcParam);
		return domproAgent;
	}
	
	public Prosumer_Factory(ADRCContext context) {
		this.mainContext = context;
	}
	
	public Prosumer_Factory(ADRCContext context, Utility utility){
		this.mainContext = context;
		this.utility = utility;
	}
	
	
}
