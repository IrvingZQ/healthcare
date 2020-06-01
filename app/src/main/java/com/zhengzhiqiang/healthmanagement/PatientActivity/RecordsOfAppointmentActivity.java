package com.zhengzhiqiang.healthmanagement.PatientActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zhengzhiqiang.healthmanagement.Adapter.AppointmentRecyclerAdapter;
import com.zhengzhiqiang.healthmanagement.Entity.RegisterForm;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecordsOfAppointmentActivity extends AppCompatActivity {

    private RecyclerView appointRecyclerView;
    private AppointmentRecyclerAdapter appointmentRecyclerAdapter;
    private TextView tvBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_records_of_appointment);

        tvBack = findViewById(R.id.record_tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });
        refreshRecyclerView();
    }

    private void refreshRecyclerView(){
        HttpUtils.getPatientRegisterForms(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RecordsOfAppointmentActivity.this,"网络出错了！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String registerJson = response.body().string();
                Log.d("Record","registerJson:"+registerJson);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 && !registerJson.equals("")){
                            Gson gson = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd HH:mm")
                                    .create();
                            List<RegisterForm> registerFormList = gson.fromJson(registerJson,new TypeToken<List<RegisterForm>>(){}.getType());
                            appointmentRecyclerAdapter = new AppointmentRecyclerAdapter(registerFormList);
                            appointmentRecyclerAdapter.setOnItemClickListener(new AppointmentRecyclerAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    AlertDialog builder = new AlertDialog.Builder(RecordsOfAppointmentActivity.this)
                                            .setMessage("您要取消此次预约吗")
                                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    RegisterForm registerForm = registerFormList.get(position);
                                                    HttpUtils.deleteRegisterForm(registerForm.getPatientPhone(),registerForm.getDoctorPhone(),registerForm.getDate(),new Callback() {
                                                        @Override
                                                        public void onFailure(Call call, IOException e) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(RecordsOfAppointmentActivity.this,"网络出错！",Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onResponse(Call call, Response response) throws IOException {
                                                            String result = response.body().string();
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    if (response.code() == 200 && result.equals("1")){
                                                                        Toast.makeText(RecordsOfAppointmentActivity.this,"取消成功！",Toast.LENGTH_SHORT).show();
                                                                        refreshRecyclerView();
                                                                    }else{
                                                                        Toast.makeText(RecordsOfAppointmentActivity.this,"取消失败！",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            }).create();
                                    builder.show();


                                }
                            });
                            appointRecyclerView = findViewById(R.id.recycler_appointment_records);
                            appointRecyclerView.setLayoutManager(new LinearLayoutManager(RecordsOfAppointmentActivity.this));
                            appointRecyclerView.setAdapter(appointmentRecyclerAdapter);
                        }else{
                            Toast.makeText(RecordsOfAppointmentActivity.this,"请求数据出错了！",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

    }
}
