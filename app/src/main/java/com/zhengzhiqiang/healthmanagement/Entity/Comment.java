package com.zhengzhiqiang.healthmanagement.Entity;

import android.graphics.drawable.Drawable;
import android.widget.ScrollView;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Comment {

    private long stickerId;
    private String headImage;
    private String nickName;
    //评论人账号
    private long commentPhone;
    private String content;
    private Timestamp date;

    public String  getContent() {
        return content;
    }

    public void setContent(String  content) {
        this.content = content;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public long getCommentPhone() {
        return commentPhone;
    }

    public void setCommentPhone(long commentPhone) {
        this.commentPhone = commentPhone;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getStickerId() {
        return stickerId;
    }

    public void setStickerId(long stickerId) {
        this.stickerId = stickerId;
    }
}
