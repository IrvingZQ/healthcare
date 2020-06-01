package com.zhengzhiqiang.healthmanagement.Utils;

import android.app.Application;
import android.graphics.Paint;

import com.zhengzhiqiang.healthmanagement.Entity.Doctor;
import com.zhengzhiqiang.healthmanagement.Entity.Patient;

public class MyApplication extends Application {

    public  static  boolean IDENTITY_TYPE =false;
    public  static final  boolean IDENTITY_TYPE_PATIENT =false;
    public  static final  boolean IDENTITY_TYPE_DOCTOR =true;
    public  static final  String NEWS_TYPE_EVENT ="健康资讯";
    public  static final  String NEWS_TYPE_KNOWLEDGE ="健康知识";
    public  static final  int ASSESSMENT_TYPE_BODY =0;
    public  static final  int ASSESSMENT_TYPE_PSYCHOLOGY =1;
//    public static final String Server_Url = "http://192.168.43.38:8080/healthcare/";
//    public static final String Images_Url = "http://192.168.43.38:8080/images/";
    public static final String Server_Url = "http://119.23.172.131:8080/healthcare/";
    public static final String Images_Url = "http://119.23.172.131:8080/images/";
    public static String phone;
    public static Patient patient;
    public static Doctor doctor;
}
