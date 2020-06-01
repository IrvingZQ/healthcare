package com.zhengzhiqiang.healthmanagement.PatientActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengzhiqiang.healthmanagement.Entity.HealthRecord;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditHealthRecordsActivity extends AppCompatActivity {


    private TextView tvCancel;
    private TextView tvSave;
    private EditText etHabit;
    private EditText etFamilyHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_health_records);
        initView();
    }

    private void initView(){
        tvCancel = findViewById(R.id.edit_health_records_tv_back);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });

        tvSave = findViewById(R.id.edit_health_records_tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdit();
            }
        });
        etHabit = findViewById(R.id.edit_health_records_et_habit);
        etFamilyHistory = findViewById(R.id.edit_health_records_et_family_history);
        refreshData();
    }

    private void saveEdit(){
        String habit = etHabit.getText().toString().trim();
        String familyHistory = etFamilyHistory.getText().toString().trim();
        HealthRecord healthRecord = new HealthRecord();
        healthRecord.setHabit(habit);
        healthRecord.setFamilyHistory(familyHistory);
        HttpUtils.updateHealthRecord(healthRecord, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("EditHealthRecord","updateHealthRecord失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result =response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 && result.equals("1")){
                            Toast.makeText(EditHealthRecordsActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                            finishAndRemoveTask();
                        }else {
                            Log.d("EditHealthRecord","updateHealthRecord失败1");
                        }
                    }
                });
            }
        });
    }

    private void refreshData(){
        HttpUtils.getHealthRecord(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HealthRecord","getHealthRecord出错");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String healthRecordJson = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200&&!healthRecordJson.equals("")){
                            HealthRecord healthRecord = HttpUtils.gson.fromJson(healthRecordJson,HealthRecord.class);
                            etHabit.setText(healthRecord.getHabit());
                            etFamilyHistory.setText(healthRecord.getFamilyHistory());
                        }
                    }
                });
            }
        });
    }

}
