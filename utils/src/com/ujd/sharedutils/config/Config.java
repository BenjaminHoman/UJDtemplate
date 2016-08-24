package com.ujd.sharedutils.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private static final String DATABASE_URL_KEY = "databaseUrl";
	private static final String DATABASE_USERNAME_KEY = "databaseUsername";
	private static final String DATABASE_PASSWORD_KEY = "databaseUsername";
	private static final String DEBUG_CLASS_PATTERN_KEY = "debugClassPattern";
	
	private String databaseUrl;
	private String databaseUsername;
	private String databasePassword;
	private String debugClassPattern;

	public Config(String filename){
		Properties properties = new Properties();
		try {
			InputStream input = new FileInputStream(filename);
			properties.load(input);
			input.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.databaseUrl = properties.getProperty(DATABASE_URL_KEY);
		this.databaseUsername = properties.getProperty(DATABASE_USERNAME_KEY);
		this.databasePassword = properties.getProperty(DATABASE_PASSWORD_KEY);
		this.debugClassPattern = properties.getProperty(DEBUG_CLASS_PATTERN_KEY);
	}
	
	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public String getDatabaseUsername() {
		return databaseUsername;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public String getDebugClassPattern() {
		return debugClassPattern;
	}
}
