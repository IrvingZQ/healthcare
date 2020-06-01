package com.zhengzhiqiang.healthmanagement.Entity;

import java.sql.Timestamp;

public class IllnessHistory {

	private int phrId;
	private Timestamp date ;
	private String doctorName;
	private String condition;
	private String treatment;
	public int getPhrId() {
		return phrId;
	}
	public void setPhrId(int phrId) {
		this.phrId = phrId;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getTreatment() {
		return treatment;
	}
	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}
	
	
	
}
