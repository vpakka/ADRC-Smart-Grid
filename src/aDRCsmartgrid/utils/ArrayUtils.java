/**
 * 
 */
package aDRCsmartgrid.utils;

/**
 * @author VijayPakka
 *
 */

import java.util.Arrays;
import aDRCsmartgrid.base.Consts;
import aDRCsmartgrid.base.Consts.ADRC_TYPE;

public class ArrayUtils {

	public static double[] initializeArraytoZeroes(double[] doubleArray){
		double [] ArrayofZeroes = new double[doubleArray.length];	
		for (int i = 0; i < doubleArray.length; i++)
			ArrayofZeroes[i] = 0.0;
		return ArrayofZeroes;
	}
	
	public static boolean isContainNegative(double[] doubleArray) {
		boolean negativeFound=false;
		int i=0;
		while (i<doubleArray.length && !negativeFound) {
			if (doubleArray[i] <0)
				negativeFound = true;
			else 	i++;
		}
		return negativeFound;
	}
	
	public static double sum(double[] doubleArray)	{
		double sum = 0;
		for (int i = 0; i < doubleArray.length; i++)
			sum = sum + doubleArray[i];
		return sum;
	}
	
	public static double weightedSum(double[] doubleArray, double[] wgtdoubleArray)	{
		double wgtsum = 0;
		for (int i = 0; i < doubleArray.length; i++)
			wgtsum = wgtsum + wgtdoubleArray[i]*doubleArray[i];
		return wgtsum;
	}
	
	public static double[] add(double[]... doubleArrays) {
		double[] resArray = new double[doubleArrays[0].length];
		for ( double[] array : doubleArrays)	{
			for (int i = 0; i < doubleArrays[0].length; i++)		
				resArray[i] = resArray[i] + array[i];
		}
		return resArray;
	}


	public static double maxVal( double[] doubleArray) {
		double maxVal = Double.MIN_VALUE;

		for(int i = 0; i<doubleArray.length; i++) {
			if (doubleArray[i] > maxVal) 
				maxVal = doubleArray[i];			
		}
		return maxVal;
	}


	public static double minVal( double[] doubleArray)	{
		double minVal = Double.MAX_VALUE;
		
		for(int i = 0; i<doubleArray.length; i++) {
			if (doubleArray[i] < minVal) 
				minVal = doubleArray[i];			
		}
		return minVal;
	}

	public static int indexOfMax(double[] doubleArray)	{
		int maxIndex = 0;
		double maxVal = Double.MIN_VALUE;
		for(int i = 0; i < doubleArray.length; i++) {	
			if (doubleArray[i] > maxVal) {
				maxVal = doubleArray[i];
				maxIndex = i;
			}			
		}
		return maxIndex;
	}

	public static int indexOfMin(double[] doubleArray)	{
		int minIndex = 0;
		double minVal = Double.MAX_VALUE;
		for(int i = 0; i < doubleArray.length; i++) {	
			if (doubleArray[i] < minVal) {
				minVal = doubleArray[i];
				minIndex = i;
			}			
		}
		return minIndex;
	}


	public static int indexOfGivenVal( double[] doubleArray, double val) {
		int index = -1;
		for(int i = 0; i < doubleArray.length; i++) {
			if (doubleArray[i] == val) 
				index = i;		
		}
		return index;
	}

	public static double avg(double[] doubleArray) {
		double avg=0d;
		if (doubleArray.length !=0) {
			double sum = sum(doubleArray);
			avg = sum/(double)doubleArray.length;
		}
		return avg;
	}
	
	
	public static double weightedAvg(double[] doubleArray, double[] wgtdoubleArray) {
		double wgtavg=0d;
		if (doubleArray.length !=0) {
			double wgtsum = weightedSum(doubleArray, wgtdoubleArray);
			wgtavg = wgtsum/sum(wgtdoubleArray);
		}
		return wgtavg;
	}
	
	public static double Alt_weightedAvg(double[] doubleArray, double[] wgtdoubleArray, double[] wgtdoubleArray2) {
		double wgtavg=0d;
		if (doubleArray.length !=0) {
			double wgtsum = weightedSum(doubleArray, wgtdoubleArray);
			wgtavg = wgtsum/sum(negate(wgtdoubleArray2));						// Pd = negative summation of all demand profiles at SP = 0,1,2,.......
		}
		return wgtavg;
	}
	
	//public static double compareArraysForTolerance(double [] array1, double [] array2){
		
	//}

	public static double[] multiplyBy(double[] doubleArray, double multiplier)	{
		double[] resultArray = new double [doubleArray.length];

		for (int i = 0; i < doubleArray.length; i++) {
			resultArray[i] =  doubleArray[i] * multiplier;
		}
		return resultArray;
	}

	public static double[] convertStringArrayToDoubleArray(String[] stringArray) {
		if (stringArray == null) 
			return null;
		double doubleArray[] = new double[stringArray.length];
		for (int i = 0; i < stringArray.length; i++) 
			doubleArray[i] = Double.parseDouble(stringArray[i]);
		return doubleArray;
	}
	
	public static int[] convertStringArrayToIntegerArray(String[] stringArray) {
		if (stringArray == null)
			return null;
		int integerArray[] = new int[stringArray.length];
		for (int i = 0; i < stringArray.length; i++)
			integerArray[i] = Integer.parseInt(stringArray[i]);
		return integerArray;
	}
	
	public static ADRC_TYPE[] convertStringArrayToSGSTYPEArray(String[] stringArray) {
		if (stringArray == null) 
			return null;
		
		ADRC_TYPE sgsType_Array[] = new ADRC_TYPE[stringArray.length];
		for (int i = 0; i < stringArray.length; i++) {
            switch (stringArray[i]){
            case "DOM_PROSUMER":
            	sgsType_Array[i] = ADRC_TYPE.DOM_PROSUMER;
            	break;
            case "IND_PROSUMER":
            	sgsType_Array[i] = ADRC_TYPE.IND_PROSUMER;
            	break;
            case "PV_DER":
            	sgsType_Array[i] = ADRC_TYPE.PV_DER;
            	break;
            case "WIND_DER":
            	sgsType_Array[i] = ADRC_TYPE.WIND_DER;
            	break;
            case "CHP_DER":
            	sgsType_Array[i] = ADRC_TYPE.CHP_DER;
            	break;
            case "BIOMASS_DER":
            	sgsType_Array[i] = ADRC_TYPE.BIOMASS_DER;
            	break;
            case "DIESEL_DER":
            	sgsType_Array[i] = ADRC_TYPE.DIESEL_DER;
            	break;
            case "BATTERY_DER":
            	sgsType_Array[i] = ADRC_TYPE.BATTERY_DER;
            	break;
            }
		}
		return sgsType_Array;
	}
	
	public static double[] negate(double[] sourceArray)	{
		double[] negativeArray = new double[sourceArray.length];
		for (int i = 0; i < sourceArray.length; i++)
			negativeArray[i] = -sourceArray[i];
		return negativeArray;
	}
	
	/**
	 * This utility function returns a copy of the specific column of a passed 2D double array
	 * as a one dimensional double array.
	 *  
	 * @param  twoDfloatArray a 2D double array whose column is supposed to be fetched and return
	 * @param  colNb the number of column whose values are supposed to be returned as array  	 
	 * @return an array (double) containing a copy of the passed column number and 2D array 
	 */
	public static double[] colCopy(double[][] twoDdoubleArray, int colNb) {
		double[] aCol = new double[twoDdoubleArray.length];
		for (int i = 0; i < twoDdoubleArray.length; i++) {
			aCol[i]=twoDdoubleArray[i][colNb];
		}
		return aCol;
	}
	
	
	public static double[] avgOfCols2DDoubleArray(double[][] twoDDoubleArray) {	
		double[] avgArr = new double[twoDDoubleArray[0].length];
		for (int col = 0; col < twoDDoubleArray[0].length; col++) 
			avgArr[col] = avg(colCopy(twoDDoubleArray, col));
	
		return avgArr;
	}
	
	public static boolean testToleranceOfArraysBetnIterations(double [] array1, double [] array2){
		boolean withinTolerance = false;
		int count = 0; int count1 = 0;
		
		for (int i = 0; i < array1.length; i++){
			if ((Math.abs(array2[i])<=(Math.abs(array1[i])*(1+Consts.TOLERANCE)))&(Math.abs(array2[i])>=(Math.abs(array1[i])*(1-Consts.TOLERANCE)))){
				count++;
			}
			if ((Math.abs(array1[i])<=0.005)&(Math.abs(array1[i])>=0.0))
				count1++;
		}
		
		if(count==array1.length)
			withinTolerance = true;
		else if(count1>=1)
			withinTolerance = true;
		
		return withinTolerance;
	}
	
	public static boolean testToleranceOfDoublesBetnIterations(double y1, double y2){
		boolean withinTolerance = false;
		
		if ((y2<=(y1*(1+Consts.TOLERANCE)))&(y2>=(y1*(1-Consts.TOLERANCE)))){
			withinTolerance = true;
		}
			
		return withinTolerance;
	}
	
	public static boolean testToleranceOfBalanceBetweenDemAndGen(double [] array1, double [] array2){
		boolean withinTolerance = false;
		double totalDem = 0.0;
		double totalGen = 0.0;
		
		for (int i = 0; i < array1.length; i++){
			totalDem += array1[i];
			totalGen += array2[i];
		}
		
		totalDem *= -1;
		
		if((totalDem<=totalGen*(1+Consts.TOLERANCE))&(totalDem>=totalGen*(1-Consts.TOLERANCE))){
			withinTolerance = true;
		}
		
		return withinTolerance;
	}
	
	public static boolean testTolerance(double[][] twoDArray1, double[][] twoDArray2, int sp){
		boolean tolerance = false;
		
		tolerance = testToleranceOfArraysBetnIterations(colCopy(twoDArray1, sp),colCopy(twoDArray2, sp));
		//tolerance  = testToleranceOfBalanceBetweenDemAndGen(colCopy(twoDArray1, sp),colCopy(twoDArray2, sp));
		
		return tolerance;
	}
	
	public static boolean testToleranceBetnSPs(double[][] twoDArray1, int sp){
		boolean tolerance = false;
		
		tolerance = testToleranceOfArraysBetnIterations(colCopy(twoDArray1, sp-1),colCopy(twoDArray1, sp));
		//tolerance  = testToleranceOfBalanceBetweenDemAndGen(colCopy(twoDArray1, sp),colCopy(twoDArray2, sp));
		
		return tolerance;
	}
	
	public static double[] weightedAvgOfCols2DDoubleArray(double[][] twoDDoubleArray, double[][] WgttwoDDoubleArray) {	
		double[] avgArr = new double[twoDDoubleArray[0].length];
		for (int col = 0; col < twoDDoubleArray[0].length; col++) 
			avgArr[col] = weightedAvg(colCopy(twoDDoubleArray, col),colCopy(WgttwoDDoubleArray, col));
	
		return avgArr;
	}
	
	
	public static double[] Alt_weightedAvgOfCols2DDoubleArray(double[][] twoDDoubleArray, double[][] WgttwoDDoubleArray, double[][] WgttwoDDoubleArray2) {	
		double[] avgArr = new double[twoDDoubleArray[0].length];
		for (int col = 0; col < twoDDoubleArray[0].length; col++) 
			avgArr[col] = Alt_weightedAvg(colCopy(twoDDoubleArray, col),colCopy(WgttwoDDoubleArray, col),colCopy(WgttwoDDoubleArray2, col));
	
		return avgArr;
	}
	
	public static double[] EquilibriumTTimesCols2DDoubleArray(double[][] twoDDoubleArray, double[][] twoDDoubleArray2, double[][] demtwoDDoubleArray) {	
		double[] eqTTArr = new double[twoDDoubleArray[0].length];
		//eqTTArr[0]=0.0;   // as of now its 0.0, because we are not considering transition from SP=48 to SP=0. We are assuming just one day's worth of data.
		
		eqTTArr[0] = eqTTimes(colCopy(twoDDoubleArray, 0),colCopy(twoDDoubleArray2, 0),colCopy(demtwoDDoubleArray, 0),colCopy(demtwoDDoubleArray, 47));
		
		for (int col = 1; col < twoDDoubleArray[0].length; col++)   // 'col' starts from 1 because our current time period for calculating EqTTs are in SP=1............. 
			eqTTArr[col] = eqTTimes(colCopy(twoDDoubleArray, col),colCopy(twoDDoubleArray2, col),colCopy(demtwoDDoubleArray, col),colCopy(demtwoDDoubleArray, col-1));   // ramprates, current demand, previous demand.
	
		return eqTTArr;
	}
	
	public static double eqTTimes(double[] doubleArray, double[] doubleArray2, double[] currDemtwoDDoubleArray, double[] prevDemtwoDDoubleArray) {
		double totalDemand_current=0d;
		double totalDemand_previous=0d;
		double totalRampUpRates=0d;
		double totalRampDownRates=0d;
		double eqTT = 0d;
		
		if ((doubleArray.length !=0)&&(doubleArray2.length !=0)) {
			totalDemand_current = sum(negate(currDemtwoDDoubleArray));						// Pd = negative summation of all demand profiles at SP = 0,1,2,.......
			totalDemand_previous = sum(negate(prevDemtwoDDoubleArray));
			totalRampUpRates = sum(doubleArray);
			totalRampDownRates = sum(doubleArray2);
			if(totalDemand_current>=totalDemand_previous)
				eqTT = (totalDemand_current-totalDemand_previous)/(totalRampUpRates);
			else
				eqTT = (totalDemand_previous-totalDemand_current)/(totalRampDownRates);
		}
		return eqTT;
	}
	
	public static double[] sumOfCols2DDoubleArray(double[][] twoDDoubleArray) {	
		double[] sumArr = new double[twoDDoubleArray[0].length];
		for (int col = 0; col < twoDDoubleArray[0].length; col++) 
			sumArr[col] = sum(colCopy(twoDDoubleArray, col));
		
		return sumArr;
	}
	

	/**
	 * This utility function returns an array copy containing of passed row index of a given 2D array
	 *  
	 * @param twoDdoubleArray the original 2D double array from which a row will be copied and return as an array
	 * @param row a row index indicating the row which needs to be taken out (copied and returned)
	 * @return an array (double) containing a copy of its needed row (index passed as argument)
	 */
	public static double[] rowCopy(double[][] twoDdoubleArray, int row) {
		double[] rowCopyArr = new double[twoDdoubleArray[0].length];
		for (int col = 0; col < twoDdoubleArray[0].length; col++) {
			rowCopyArr[col]=twoDdoubleArray[row][col];
		}
		return rowCopyArr;
	}
	
	
	/**
	 * This utility function builds an printable string of the elements (values) of a given 2D double array
	 * and returns it. The string could be used in any standard print/output function (such as println)
	 * 
	 * @param   a 2D doubleArray to be printed 	 
	 * @return a ready-to-be-printed string of array elements/values 
	 */
	public static String toString(double[][] twoDdoubleArray) {	
		String output = ""; 
		for (int row = 0; row < twoDdoubleArray.length; row++) {
			double [] aRowArray = rowCopy(twoDdoubleArray, row);
			output += "r"+row+Arrays.toString(aRowArray);
			output +="\n";
		}
		return output;
	}
	


	
}
