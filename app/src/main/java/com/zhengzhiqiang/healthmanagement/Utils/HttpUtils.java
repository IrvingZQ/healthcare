package com.zhengzhiqiang.healthmanagement.Utils;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhengzhiqiang.healthmanagement.Entity.AssessmentRecord;
import com.zhengzhiqiang.healthmanagement.Entity.Comment;
import com.zhengzhiqiang.healthmanagement.Entity.Doctor;
import com.zhengzhiqiang.healthmanagement.Entity.HealthRecord;
import com.zhengzhiqiang.healthmanagement.Entity.IllnessHistory;
import com.zhengzhiqiang.healthmanagement.Entity.Patient;
import com.zhengzhiqiang.healthmanagement.Entity.RegisterForm;
import com.zhengzhiqiang.healthmanagement.Entity.Sticker;
import com.zhengzhiqiang.healthmanagement.Entity.TipOff;

import java.sql.Timestamp;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.zhengzhiqiang.healthmanagement.Utils.MyApplication.IDENTITY_TYPE;
import static com.zhengzhiqiang.healthmanagement.Utils.MyApplication.IDENTITY_TYPE_PATIENT;
import static com.zhengzhiqiang.healthmanagement.Utils.MyApplication.doctor;
import static com.zhengzhiqiang.healthmanagement.Utils.MyApplication.patient;
import static com.zhengzhiqiang.healthmanagement.Utils.MyApplication.phone;

public class HttpUtils {

    private static OkHttpClient okHttpClient = new OkHttpClient();
    public static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    /**
     * 登录请求
     */
    public static void  login(String phone, String password,Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("phone",phone)
                .add("password",password)
                .build();

        String url = "user/login";
        Request request = new Request.Builder()
                .post(requestBody)
                .url(MyApplication.Server_Url+url)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 注册请求
     */
    public static void register(String phone, String password,String name,Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("phone",phone)
                .add("password",password)
                .add("name",name)
                .build();

        String url = "user/register";

        Request request = new Request.Builder()
                .post(requestBody)
                .url(MyApplication.Server_Url+url)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 注册请求
     */
    public static void changePassword( String oldPWD,String newPWD,Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("phone",phone)
                .add("oldPassword",oldPWD)
                .add("newPassword",newPWD)
                .add("identity",String.valueOf(IDENTITY_TYPE))
                .build();

        String url = "user/changePassword";
        Request request = new Request.Builder()
                .post(requestBody)
                .url(MyApplication.Server_Url+url)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getUserInformation(boolean identity,Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("phone",MyApplication.phone)
                .build();

        String url = "patient/getPatient";
        if (identity){
            url = "doctor/getDoctor";
        }

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void updatePatientInformation(Patient patient, Callback callback){
        String patientJson = gson.toJson(patient);
        Log.d("EditPersonal","patientJson:"+patientJson);
        RequestBody requestBody = new FormBody.Builder()
                .add("patient",patientJson)
                .build();

        String url = "patient/postPatient";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void updateDoctor(Doctor doctor, Callback callback){
        String doctorJson = gson.toJson(doctor);
        RequestBody requestBody = new FormBody.Builder()
                .add("doctor",doctorJson)
                .build();

        String url = "doctor/postDoctor";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getNews( String type, Callback callback) {

        RequestBody requestBody = new FormBody.Builder()
                .add("type", type)
                .build();
        String url = "user/getNews";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url + url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getDoctors(String hospital,String department,Callback callback) {

        RequestBody requestBody = new FormBody.Builder()
                .add("hospital", hospital)
                .add("department", department)
                .build();

        String url = "user/getDoctors";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url + url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getHospitals(Callback callback) {

        String url = "user/getHospitals";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url + url)
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getDepartments( String hospital,Callback callback) {

        RequestBody requestBody = new FormBody.Builder()
                .add("hospital", hospital)
                .build();

        String url = "user/getDepartments";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url + url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void postRegisterForm(RegisterForm registerForm, Callback callback){

        String registerFormString = gson.toJson(registerForm);

        RequestBody requestBody = new FormBody.Builder()
                .add("registerForm",registerFormString)
                .build();

        String url = "patient/postRegisterForm";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getPatientRegisterForms(Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("phone",MyApplication.phone)
                .build();

        String url = "patient/getRegisterForms";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getRegisterFormsByDoctor(Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("phone",MyApplication.phone)
                .build();

        String url = "doctor/getRegisterFormsByDoctor";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void deleteRegisterForm(long patientPhone, long doctorPhone, Timestamp date, Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("patientPhone",String.valueOf(patientPhone))
                .add("doctorPhone",String.valueOf(doctorPhone))
                .add("date",String.valueOf(date))
                .build();

        String url = "patient/deleteRegisterForm";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getHealthTestSets(int type, Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("type", String.valueOf(type))
                .build();

        String url = "patient/getHealthQuestionSets";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void postAssessmentRecord(AssessmentRecord assessmentRecord, Callback callback){

        String assessmentRecordJson = gson.toJson(assessmentRecord);
        RequestBody requestBody = new FormBody.Builder()
                .add("assessmentRecord",assessmentRecordJson)
                .build();

        String url = "patient/postAssessmentRecord";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getAssessmentRecords(String phone, Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("phone",phone)
                .build();

        String url = "patient/getAssessmentRecords";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void postSticker(Sticker sticker, Callback callback){

        String stickerJson = gson.toJson(sticker);
        RequestBody requestBody = new FormBody.Builder()
                .add("sticker",stickerJson)
                .build();

        String url = "sticker/postSticker";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void deleteSticker(long stickerId, Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("stickerId",String.valueOf(stickerId))
                .build();

        String url = "sticker/deleteSticker";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getStickers(Callback callback){

//        RequestBody requestBody = new FormBody.Builder()
//                .add("phone",phone)
//                .build();

        String url = "sticker/getStickers";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void postComment(Comment comment, Callback callback){

        String commentJson = gson.toJson(comment);
        RequestBody requestBody = new FormBody.Builder()
                .add("comment",commentJson)
                .build();

        String url = "sticker/postComment";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getCommentsById(long stickerId, Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("stickerId", String.valueOf(stickerId))
                .build();

        String url = "sticker/getCommentsById";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }


    public static void checkThumbUpStatus(long stickerId, Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("stickerId", String.valueOf(stickerId))
                .add("phone",MyApplication.phone)
                .build();

        String url = "sticker/checkThumbUpStatus";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void changeThumbUpStatus(long stickerId, Callback callback){

        RequestBody requestBody = new FormBody.Builder()
                .add("stickerId", String.valueOf(stickerId))
                .add("phone",MyApplication.phone)
                .build();

        String url = "sticker/changeThumbUpStatus";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void postTipOff(TipOff tipOff, Callback callback){

        String tipOffString = gson.toJson(tipOff);

        RequestBody requestBody = new FormBody.Builder()
                .add("tipOff",tipOffString)
                .build();

        String url = "sticker/postTipOff";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getHealthRecord(Callback callback){
        int phrId = patient.getPhrId();

        RequestBody requestBody = new FormBody.Builder()
                .add("phrId", String.valueOf(phrId))
                .build();

        String url = "healthRecord/getHealthRecord";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void updateHealthRecord(HealthRecord healthRecord,Callback callback){
        int phrId = patient.getPhrId();

        healthRecord.setId(phrId);

        String healthRecordJson = gson.toJson(healthRecord);

        RequestBody requestBody = new FormBody.Builder()
                .add("healthRecord", healthRecordJson)
                .build();

        String url = "healthRecord/updateHealthRecord";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void insertIllnessHistory(IllnessHistory illnessHistory,long patientPhone,long doctorPhone, Callback callback){

        String illnessHistoryJson = gson.toJson(illnessHistory);

        RequestBody requestBody = new FormBody.Builder()
                .add("illnessHistory",illnessHistoryJson)
                .add("patientPhone", String.valueOf(patientPhone))
                .add("doctorPhone", String.valueOf(doctorPhone))
                .build();

        String url = "healthRecord/insertIllnessHistory";

        Request request = new Request.Builder()
                .url(MyApplication.Server_Url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
