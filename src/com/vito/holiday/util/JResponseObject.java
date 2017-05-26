package com.vito.holiday.util;

public class JResponseObject {

	public int ErrorCode;
	public Object ResponseObject;
	public String ErrorString;
	
	public JResponseObject(Object responseObject,int errorCode,  String errorString) {
		super();
		ErrorCode = errorCode;
		ResponseObject = responseObject;
		ErrorString = errorString;
	}
}
