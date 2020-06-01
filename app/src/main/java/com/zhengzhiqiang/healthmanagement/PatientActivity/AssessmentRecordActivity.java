package com.zhengzhiqiang.healthmanagement.PatientActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zhengzhiqiang.healthmanagement.Adapter.AssessmentRecordRecyclerAdapter;
import com.zhengzhiqiang.healthmanagement.Entity.AssessmentRecord;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AssessmentRecordActivity extends AppCompatActivity {

    private TextView tvBack;
    private RecyclerView recyclerView;
    private AssessmentRecordRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_assessment_record);

        tvBack = findViewById(R.id.assessment_record_tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView(){
        HttpUtils.getAssessmentRecords(MyApplication.phone, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AssessmentRecordActivity.this,"网络出错了！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String recordJson = response.body().string();
                Log.d("AssessmentRecord","recordJson:"+recordJson);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() !=200){
                            Toast.makeText(AssessmentRecordActivity.this,"获取数据失败！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Gson gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd HH:mm")
                                .create();
                        List<AssessmentRecord> assessmentRecordList = gson.fromJson(recordJson,new TypeToken<List<AssessmentRecord>>(){}.getType());
                        adapter = new AssessmentRecordRecyclerAdapter(assessmentRecordList);
                        recyclerView = findViewById(R.id.assessment_record_item_recycler);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AssessmentRecordActivity.this));
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
    }
}
