package com.zhengzhiqiang.healthmanagement.Entity;

public class TipOff {

    private long stickerId;
    private String Reason;
    private long tipOffPhone;

    public long getStickerId() {
        return stickerId;
    }

    public void setStickerId(long stickerId) {
        this.stickerId = stickerId;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public long getTipOffPhone() {
        return tipOffPhone;
    }

    public void setTipOffPhone(long tipOffPhone) {
        this.tipOffPhone = tipOffPhone;
    }
}
