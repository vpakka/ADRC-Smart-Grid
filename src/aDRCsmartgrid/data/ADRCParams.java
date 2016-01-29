/**
 * 
 */
package aDRCsmartgrid.data;

import aDRCsmartgrid.base.Consts.ADRC_TYPE;

/**
 * @author VijayPakka
 *
 */
public class ADRCParams {

	private int ADRC_ID;
	private String Name;
	private ADRC_TYPE Type;
	private double NomPower;
	private double MaxGen;
	private double MinGen;
	private double MaxDem;
	private double MinDem;
 
	public int getADRC_ID(){
		return this.ADRC_ID;
	}
	
	public String getADRC_name(){
		return this.Name;
	}
	
	public ADRC_TYPE getADRC_Type(){
		return this.Type;
	}
	
	public double getADRC_NomPower(){
		return this.NomPower;
	}
	
	public double getADRC_MaxGen(){
		return this.MaxGen;
	}
	
	public double getADRC_MinGen(){
		return this.MinGen;
	}
	
	public double getADRC_MaxDem(){
		return this.MaxDem;
	}
	
	public double getADRC_MinDem(){
		return this.MinDem;
	}
	
	public ADRCParams(int id, String name, ADRC_TYPE type, double nompower, double maxgen, double mingen, double maxdem, double mindem){
		this.ADRC_ID = id;
		this.Name = name;
		this.Type = type;
		this.NomPower = nompower;
		this.MaxGen = maxgen;
		this.MinGen = mingen;
		this.MaxDem = maxdem;
		this.MinDem = mindem;
	}
}

