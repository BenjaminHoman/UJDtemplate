package com.ujd.sharedutils.datainterface;

import java.util.List;

import com.ujd.sharedutils.model.Filter;
import com.ujd.sharedutils.model.FunctionParameters;

public class PersistentDatabaseConnection extends DatabaseConnection {

	public PersistentDatabaseConnection(String url, String username, String password) {
		super(url, username, password);
		initConnection();
	}
	
	@Override
	public void insertFunctionParameters(FunctionParameters functionParameters){
		insert(functionParameters);
	}
	
	@Override
	public List<FunctionParameters> getFunctionParameters(Filter filter){
		return select(filter);
	}
	
	@Override
	public void clearFunctionParameters(){
		clear();
	}
	
	public void close(){
		closeConnection();
	}
}
