package com.jvm.debugger.model;

public class Logging {
	private Long threadId;
	private String className;
	private String methodName;
	private String values;
	private Long timestamp;
	
	public Logging(){
		this.threadId = null;
		this.className = null;
		this.methodName = null;
		this.values = null;
		this.timestamp = null;
	}
	
	public Logging(long threadId, 
								String className, 
								String methodName,
								String values, 
								long timestamp) {
		
		this.threadId = threadId;
		this.className = className;
		this.methodName = methodName;
		this.values = values;
		this.timestamp = timestamp;
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

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
