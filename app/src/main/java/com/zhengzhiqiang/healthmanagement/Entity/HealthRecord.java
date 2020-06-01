package com.zhengzhiqiang.healthmanagement.Entity;

public class HealthRecord {
	
	private int Id;
	private String habit;
	private String FamilyHistory;
	private String illnessHistory;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getHabit() {
		return habit;
	}
	public void setHabit(String habit) {
		this.habit = habit;
	}
	public String getFamilyHistory() {
		return FamilyHistory;
	}
	public void setFamilyHistory(String familyHistory) {
		FamilyHistory = familyHistory;
	}
	public String getIllnessHistory() {
		return illnessHistory;
	}
	public void setIllnessHistory(String illnessHistory) {
		this.illnessHistory = illnessHistory;
	}
	
	
}
