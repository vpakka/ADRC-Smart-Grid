package aDRCsmartgrid.agents;

import aDRCsmartgrid.base.Consts.ADRC_TYPE;
import aDRCsmartgrid.context.ADRCContext;
import aDRCsmartgrid.data.ADRCParams;

public class BatteryDER extends Prosumer{
	
	public BatteryDER(ADRC_TYPE type, ADRCContext context, Utility utility, double[] baseProfile, ADRCParams adrcParam) {
		super(type, context, utility, baseProfile, adrcParam);
		// TODO Auto-generated constructor stub
	}

}
