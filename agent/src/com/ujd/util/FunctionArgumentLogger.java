package com.ujd.util;

import java.util.Calendar;

import com.google.gson.Gson;
import com.ujd.sharedutils.config.Config;
import com.ujd.sharedutils.datainterface.PersistentDatabaseConnection;
import com.ujd.sharedutils.model.FunctionParameters;

public class FunctionArgumentLogger {
	private static FunctionArgumentLogger instance = null;
	private static Config agentConfig;
	
	private Gson gson;
	private PersistentDatabaseConnection database;
	
	private FunctionArgumentLogger(){
		this.gson = new Gson();
		this.database = new PersistentDatabaseConnection(agentConfig.getDatabaseUrl(), 
															agentConfig.getDatabaseUsername(), 
															agentConfig.getDatabasePassword());
	}
	
	private String getSimpleClassName(String name){
		return name.substring(name.lastIndexOf(".")+1);
	}
	
	private <T> void logParameters(String programName, long threadId, String className, String methodName, Object[] args, Object returned){
		try {
			FunctionParameters functionParameters = new FunctionParameters();
			functionParameters.setProgramName(programName);
			functionParameters.setThreadId(threadId);
			functionParameters.setClassName(getSimpleClassName(className));
			functionParameters.setMethodName(methodName);
			functionParameters.setParameterValues(this.gson.toJson(new ArgumentObject(args, returned)));
			functionParameters.setTimestamp(Calendar.getInstance().getTimeInMillis());
		
			this.database.insertFunctionParameters(functionParameters);
			
		} catch (Throwable e){}
	}
	
	private static FunctionArgumentLogger getInstance(){
		if (instance == null){
			instance = new FunctionArgumentLogger();
		}
		return instance;
	}
	
	public static synchronized void log(String programName, long threadId, String className, String methodName, Object[] args, Object returned){
		getInstance().logParameters(programName, threadId, className, methodName, args, returned);
	}
	
	public static synchronized void setConfig(Config config){
		agentConfig = config;
	}
	
	private static class ArgumentObject {
		@SuppressWarnings("unused")
		private Object[] args;
		@SuppressWarnings("unused")
		private Object returned;
		
		public ArgumentObject(Object[] args, Object returned){
			this.args = args;
			this.returned = returned;
		}
	}
}
