package com.ujd.sharedutils.model;

public class FunctionParameters {
	private Integer id;
	private String programName;
	private Long threadId;
	private String className;
	private String methodName;
	private String parameterValues;
	private Long timestamp;
	
	public FunctionParameters(){
		this.id = null;
		this.programName = null;
		this.threadId = null;
		this.className = null;
		this.methodName = null;
		this.parameterValues = null;
		this.timestamp = null;
	}
	
	public FunctionParameters(Integer id, 
								String programName, 
								long threadId, 
								String className, 
								String methodName,
								String parameterValues, 
								long timestamp) {
		this.id = id;
		this.programName = programName;
		this.threadId = threadId;
		this.className = className;
		this.methodName = methodName;
		this.parameterValues = parameterValues;
		this.timestamp = timestamp;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Long getThreadId() {
		return threadId;
	}

	public void setThreadId(Long threadId) {
		this.threadId = threadId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParameterValues() {
		return parameterValues;
	}

	public void setParameterValues(String parameterValues) {
		this.parameterValues = parameterValues;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
