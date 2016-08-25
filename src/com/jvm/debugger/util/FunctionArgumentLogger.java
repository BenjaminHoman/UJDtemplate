package com.jvm.debugger.util;

import java.util.Calendar;

import com.google.gson.Gson;
import com.jvm.debugger.model.Logging;

public class FunctionArgumentLogger {
	private static FunctionArgumentLogger instance = null;
	private static Config config;
	
	private Gson gson;
	private PersistentDatabaseConnection database;
	
	private FunctionArgumentLogger(){
		this.gson = new Gson();
		this.database = new PersistentDatabaseConnection(config.getDatabaseUrl(), 
															config.getDatabaseUsername(), 
															config.getDatabasePassword());
	}
	
	private void logArgsObject(long threadId, String className, String methodName, Object[] args){
		log(threadId, className, methodName, this.gson.toJson(args));
	}
	
	private void log(long threadId, String className, String methodName, String value){
		try {
			Logging functionParameters = new Logging(threadId,
													className,
													methodName,
													value, 
													Calendar.getInstance().getTimeInMillis());
			
			this.database.insertFunctionParameters(functionParameters);
			
		} catch (Throwable e){}
	}
	
	private static FunctionArgumentLogger getInstance(){
		if (instance == null){
			instance = new FunctionArgumentLogger();
		}
		return instance;
	}
	
	public static synchronized void logArgs(long threadId, String className, String methodName, Object[] args){
		getInstance().logArgsObject(threadId, className, methodName, args);
	}
	
	public static synchronized void setConfig(Config c){
		config = c;
	}
	
	private static class ArgumentObject {
		@SuppressWarnings("unused")
		private Object[] args;
		
		public ArgumentObject(Object[] args){
			this.args = args;
		}
	}
	
	private static class ReturnObject {
		@SuppressWarnings("unused")
		private Object returned;
		
		public ReturnObject(Object returned){
			this.returned = returned;
		}
	}
}
