package com.jvm.debugger.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Config {
	private static final String DATABASE_USERNAME = "databaseUsername";
	private static final String DATABASE_PASSWORD = "databasePassword";
	private static final String DATABASE_URL = "databaseUrl";
	private static final String DEBUG_CLASS_PATTERNS = "debugClassPatterns";
	private static final String EXTRA_CLASS_PATHS = "extraClassPaths";
	private static final String PRINT_DEBUG_STATUS = "printDebugStatus";
	
	private String databaseUsername;
	private String databasePassword;
	private String databaseUrl;
	private List<Pattern> debugClassPatterns;
	private List<String> extraClassPaths;
	private boolean printDebugStatus;
	
	public Config(String filename){
		this.printDebugStatus = true;
		this.debugClassPatterns = new ArrayList<>();
		this.extraClassPaths = new ArrayList<>();
		try {
			String config = new String(Files.readAllBytes(Paths.get(filename)));
			
			JsonObject obj = new JsonParser().parse(config).getAsJsonObject();
			if (obj.has(DATABASE_USERNAME)){
				this.databaseUsername = obj.get(DATABASE_USERNAME).getAsString();
			}
			if (obj.has(DATABASE_PASSWORD)){
				this.databasePassword = obj.get(DATABASE_PASSWORD).getAsString();
			}
			if (obj.has(DATABASE_URL)){
				this.databaseUrl = obj.get(DATABASE_URL).getAsString();
			}
			if (obj.has(PRINT_DEBUG_STATUS)){
				this.printDebugStatus = obj.get(PRINT_DEBUG_STATUS).getAsBoolean();
			}
			if (obj.has(EXTRA_CLASS_PATHS)){
				JsonArray arr = obj.get(EXTRA_CLASS_PATHS).getAsJsonArray();
				for (int i = 0; i < arr.size(); i++){
					this.extraClassPaths.add(arr.get(i).getAsString());
				}
			}
			if (obj.has(DEBUG_CLASS_PATTERNS)){
				JsonArray arr = obj.get(DEBUG_CLASS_PATTERNS).getAsJsonArray();
				for (int i = 0; i < arr.size(); i++){
					this.debugClassPatterns.add(Pattern.compile(arr.get(i).getAsString()));
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getDatabaseUsername() {
		return databaseUsername;
	}
	
	public String getDatabasePassword() {
		return databasePassword;
	}
	
	public String getDatabaseUrl() {
		return databaseUrl;
	}
	
	public List<Pattern> getDebugClassPatterns() {
		return debugClassPatterns;
	}
	
	public List<String> getExtraClassPaths() {
		return extraClassPaths;
	}
	
	public boolean shouldPrintDebugStatus(){
		return this.printDebugStatus;
	}
}
