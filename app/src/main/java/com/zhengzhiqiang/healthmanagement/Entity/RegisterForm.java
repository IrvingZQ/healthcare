package com.zhengzhiqiang.healthmanagement.Entity;

import java.sql.Date;
import java.sql.Timestamp;

//挂号单
public class RegisterForm {

    private long  patientPhone;
    private long doctorPhone;
    private Timestamp date;
    private double fee;
    private String hospital;
    private String department;
    private String doctorName;
    private String patientName;
    private String patientId;
    private int phrId;

    public int getPhrId() {
        return phrId;
    }

    public void setPhrId(int phrId) {
        this.phrId = phrId;
    }

    public long getPatientPhone() {
        return patientPhone;
    }
    public void setPatientPhone(long patientPhone) {
        this.patientPhone = patientPhone;
    }
    public long getDoctorPhone() {
        return doctorPhone;
    }
    public void setDoctorPhone(long doctorPhone) {
        this.doctorPhone = doctorPhone;
    }
    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }
    public double getFee() {
        return fee;
    }
    public void setFee(double fee) {
        this.fee = fee;
    }
    public String getHospital() {
        return hospital;
    }
    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }


}
