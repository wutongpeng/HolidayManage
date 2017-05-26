package com.vito.holiday.util;

public class StringFunction {
	
	public static String fillBeforeValue(String value){
		if(value.length() == 1){
			value = "0"+value;
		}
		return value;
	}

}
