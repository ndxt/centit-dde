package com.centit.dde.domain;

public class Task {
	private String id;
	private String name;
	private String description;
	private String scheduleStr;
	private String code;
	private String codeType;
	private String version;
	private String runStatus;
	// 任务状态 0,停止 1運行
	private Integer status;
	private CodeInfo codeInfo = new CodeInfo();
	// 2:cron
	private Integer type=2;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getScheduleStr() {
		return scheduleStr;
	}
	public void setScheduleStr(String scheduleStr) {
		this.scheduleStr = scheduleStr;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getRunStatus() {
		return runStatus;
	}
	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public CodeInfo codeInfo() {
		return codeInfo;
	}
}
