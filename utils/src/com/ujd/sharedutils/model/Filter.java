package com.ujd.sharedutils.model;

public class Filter extends FunctionParameters {
	private Long fromTimestamp;
	private Long toTimestamp;
	
	public Filter(){
		this.fromTimestamp = null;
		this.toTimestamp = null;
	}

	public Long getFromTimestamp() {
		return fromTimestamp;
	}

	public void setFromTimestamp(Long fromTimestamp) {
		this.fromTimestamp = fromTimestamp;
	}

	public Long getToTimestamp() {
		return toTimestamp;
	}

	public void setToTimestamp(Long toTimestamp) {
		this.toTimestamp = toTimestamp;
	}
}
