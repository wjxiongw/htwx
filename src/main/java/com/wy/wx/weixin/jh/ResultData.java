package com.wy.wx.weixin.jh;

import java.util.List;

public class ResultData {
	
	private String error_code;
	private String reason;
	private String result;
	private List<DataInfo> dataInfo;
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public List<DataInfo> getDataInfo() {
		return dataInfo;
	}
	public void setDataInfo(List<DataInfo> dataInfo) {
		this.dataInfo = dataInfo;
	}
	

	
	
}
