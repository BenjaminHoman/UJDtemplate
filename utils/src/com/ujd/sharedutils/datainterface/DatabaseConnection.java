package com.ujd.sharedutils.datainterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ujd.sharedutils.model.Filter;
import com.ujd.sharedutils.model.FunctionParameters;

public class DatabaseConnection {
	
	private static final String TABLE_NAME = "functionparameters";
	private static final String ID = "id";
	private static final String PROGRAM_NAME = "programname";
	private static final String THREAD_ID = "threadid";
	private static final String CLASS_NAME = "classname";
	private static final String METHOD_NAME = "methodname";
	private static final String PARAMETER_VALUES = "parametervalues";
	private static final String EXECUTE_TIME = "executetime";
	
	private static final String INSERT_STATEMENT_TEMPLATE = "INSERT INTO \""+TABLE_NAME+"\" "
			+ "("+PROGRAM_NAME+","+THREAD_ID+","+CLASS_NAME+","+METHOD_NAME+","+PARAMETER_VALUES+","+EXECUTE_TIME+") "
			+ "VALUES(?,?,?,?,?,?);";
	
	protected Connection connection = null;
	private String url;
	private String username;
	private String password;
	
	public DatabaseConnection(String url, String username, String password){
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public void insertFunctionParameters(FunctionParameters functionParameters){
		if (initConnection()){
			insert(functionParameters);
		}
		closeConnection();
	}
	
	public List<FunctionParameters> getFunctionParameters(Filter filter){
		if (initConnection()){
			return select(filter);
		}
		closeConnection();
		return new ArrayList<FunctionParameters>();
	}
	
	public void clearFunctionParameters(){
		if (initConnection()){
			clear();
		}
		closeConnection();
	}
	
	protected void clear(){
		PreparedStatement ps = null;
		try {
			ps = this.connection.prepareStatement("DELETE FROM \""+TABLE_NAME+"\";");
			ps.executeUpdate();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	protected boolean insert(FunctionParameters functionParameters){
		PreparedStatement ps = null;
		try {
			ps = this.connection.prepareStatement(INSERT_STATEMENT_TEMPLATE);
			
			ps.setString(1, functionParameters.getProgramName());
			ps.setLong(2, functionParameters.getThreadId());
			ps.setString(3, functionParameters.getClassName());
			ps.setString(4, functionParameters.getMethodName());
			ps.setString(5, functionParameters.getParameterValues());
			ps.setLong(6, functionParameters.getTimestamp());
			
			ps.executeUpdate();
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close(ps);
		return false;
	}
	
	protected List<FunctionParameters> select(Filter filter){
		List<FunctionParameters> functionParameters = new ArrayList<FunctionParameters>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.connection.prepareStatement(convertFilterToSql(filter));
			
			int index = 1;
			if (filter.getId() != null){
				ps.setInt(index++, filter.getId());
			}
			if (filter.getProgramName() != null){
				ps.setString(index++, filter.getProgramName());
			}
			if (filter.getThreadId() != null){
				ps.setLong(index++, filter.getThreadId());
			}
			if (filter.getClassName() != null){
				ps.setString(index++, filter.getClassName());
			}
			if (filter.getMethodName() != null){
				ps.setString(index++, filter.getMethodName());
			}
			if (filter.getFromTimestamp() != null){
				ps.setLong(index++, filter.getFromTimestamp());
			}
			if (filter.getToTimestamp() != null){
				ps.setLong(index++, filter.getToTimestamp());
			}
			rs = ps.executeQuery();
			while (rs.next()){
				FunctionParameters fp = new FunctionParameters();
				fp.setId(rs.getInt(1));
				fp.setProgramName(rs.getString(2));
				fp.setThreadId(rs.getLong(3));
				fp.setClassName(rs.getString(4));
				fp.setMethodName(rs.getString(5));
				fp.setParameterValues(rs.getString(6));
				fp.setTimestamp(rs.getLong(7));
				
				functionParameters.add(fp);
			}
			
			return functionParameters;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close(rs);
		close(ps);
		return functionParameters;
	}
	
	private String convertFilterToSql(Filter filter){
		String sql = "SELECT * FROM \""+TABLE_NAME+"\"";
		String condition = "";
		boolean hasCondition = false;
		
		if (filter.getId() != null){
			condition += ID + "=? ";
			hasCondition = true;
		}
		if (filter.getProgramName() != null){
			condition += ((hasCondition) ? " AND " : "") + PROGRAM_NAME + "=? ";
			hasCondition = true;
		}
		if (filter.getThreadId() != null){
			condition += ((hasCondition) ? " AND " : "") + THREAD_ID + "=? ";
			hasCondition = true;
		}
		if (filter.getClassName() != null){
			condition += ((hasCondition) ? " AND " : "") + CLASS_NAME + "=? ";
			hasCondition = true;
		}
		if (filter.getMethodName() != null){
			condition += ((hasCondition) ? " AND " : "") + METHOD_NAME + "=? ";
			hasCondition = true;
		}
		if (filter.getFromTimestamp() != null){
			condition += ((hasCondition) ? " AND " : "") + EXECUTE_TIME + " > ? ";
			hasCondition = true;
		}
		if (filter.getToTimestamp() != null){
			condition += ((hasCondition) ? " AND " : "") + EXECUTE_TIME + " < ? ";
		}
		return sql + ((condition.length() > 0) ? " WHERE " : "") + condition + " ORDER BY "+ID+";";
	}
	
	protected boolean initConnection(){
		if (loadDriver()){
			try {
				this.connection = DriverManager.getConnection(this.url, this.username, this.password);
				return true;
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		closeConnection();
		return false;
	}
	
	private void close(AutoCloseable closable){
		if (closable != null){
			try {
				closable.close();
			} catch (Exception e) {}
		}
	}
	
	protected void closeConnection(){
		if (this.connection != null){
			try {
				this.connection.close();
			} catch (SQLException e) {}
		}
		this.connection = null;
	}
	
	private boolean loadDriver(){
		try {
			Class.forName("org.postgresql.Driver");
			return true;
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}
