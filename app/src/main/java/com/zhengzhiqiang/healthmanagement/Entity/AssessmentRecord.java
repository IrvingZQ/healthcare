package com.zhengzhiqiang.healthmanagement.Entity;

import java.sql.Date;
import java.sql.Timestamp;

public class AssessmentRecord {

    private long phone;
    private int type;
    private Timestamp date;
    private String  result;
    public long getPhone() {
        return phone;
    }
    public void setPhone(long phone) {
        this.phone = phone;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }



}
