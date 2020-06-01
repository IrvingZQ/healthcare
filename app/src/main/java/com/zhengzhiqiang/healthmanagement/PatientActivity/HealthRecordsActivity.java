package com.zhengzhiqiang.healthmanagement.PatientActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.zhengzhiqiang.healthmanagement.Entity.HealthRecord;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HealthRecordsActivity extends AppCompatActivity {

    private TextView tvBack;
    private TextView tvEditRecords;
    private TextView tvHabit;
    private TextView tvFamilyHistory;
    private TextView tvIllnessHistory;
    private final int CODE_EDIT_HEALTH_RECORD = 7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_health_records);

        initEvent();
        refreshData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_EDIT_HEALTH_RECORD)
            refreshData();
    }

    private void initEvent(){

        tvBack = findViewById(R.id.health_record_tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });

        tvEditRecords = findViewById(R.id.health_record_tv_edit);
        tvEditRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthRecordsActivity.this,EditHealthRecordsActivity.class);
                startActivityForResult(intent,CODE_EDIT_HEALTH_RECORD);
            }
        });

        tvHabit = findViewById(R.id.health_records_tv_habit);
        tvFamilyHistory = findViewById(R.id.health_records_tv_family_history);
        tvIllnessHistory = findViewById(R.id.health_records_tv_illness_history);

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
                Log.d("HealthRecord","getHealthRecord:"+healthRecordJson);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200&& healthRecordJson!=null){
                            HealthRecord healthRecord = HttpUtils.gson.fromJson(healthRecordJson,HealthRecord.class);
                            if (healthRecord.getHabit() != null)
                            tvHabit.setText(healthRecord.getHabit());
                            if (healthRecord.getFamilyHistory() !=null)
                            tvFamilyHistory.setText(healthRecord.getFamilyHistory());
                            if (healthRecord.getIllnessHistory() !=null)
                            tvIllnessHistory.setText(healthRecord.getIllnessHistory());
                        }
                    }
                });
            }
        });
    }
}
