package com.zhengzhiqiang.healthmanagement.PatientActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhengzhiqiang.healthmanagement.Adapter.QuestionRecyclerAdapter;
import com.zhengzhiqiang.healthmanagement.Entity.AssessmentRecord;
import com.zhengzhiqiang.healthmanagement.Entity.QuestionItem;
import com.zhengzhiqiang.healthmanagement.Entity.QuestionSet;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AssessmentTestActivity extends AppCompatActivity {

    private RecyclerView questionRecyclerView;
    private QuestionRecyclerAdapter questionRecyclerAdapter;

    private TextView tvBack;
    private TextView tvQuestionSetName;
    private Button btnCommit;

    private String[] options;
    private QuestionSet questionSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_assessment_test);
        init();
    }

    private void init(){

        questionSet = (QuestionSet) getIntent().getSerializableExtra("questionSet");
        Gson gson = new Gson();
        List<QuestionItem> questionItemList = gson.fromJson(questionSet.getContent(),new TypeToken<List<QuestionItem>>(){}.getType());
        options = new String[questionItemList.size()];
        tvBack = findViewById(R.id.assessment_test_tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });

        tvQuestionSetName = findViewById(R.id.assessment_test_set_name);
        tvQuestionSetName.setText(questionSet.getName());

        questionRecyclerView = findViewById(R.id.assessment_test_recycler_question);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionRecyclerAdapter = new QuestionRecyclerAdapter(questionItemList);
        questionRecyclerAdapter.setOnItemClickListener(new QuestionRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position,String selectedOption) {
                options[position] = selectedOption;
            }
        });
        questionRecyclerView.setAdapter(questionRecyclerAdapter);

        btnCommit = findViewById(R.id.assessment_test_btn_commit);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < options.length; i++) {
                    if (options[i] == null) {
                        Toast.makeText(AssessmentTestActivity.this, "还有题没做完哦！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    postTestResult(questionItemList);
                }
            }
        });
    }


    private void postTestResult(List<QuestionItem> questionItemList){
        QuestionSet finishQuestionSet = questionSet;
        List<QuestionItem> finishQuestionList=new ArrayList<>(questionItemList.size());
        for (int i=0;i<questionItemList.size();i++) {
            QuestionItem questionItem = new QuestionItem();
            questionItem.setQuestionContent(questionItemList.get(i).getQuestionContent());
            questionItem.setOptionOne(options[i]);
            finishQuestionList.add(questionItem);
        }
        Gson gson = new Gson();
        String finishQuestionListJson = gson.toJson(finishQuestionList);
        finishQuestionSet.setContent(finishQuestionListJson);
        String testResult = gson.toJson(finishQuestionSet);
        AssessmentRecord assessmentRecord =new AssessmentRecord();
        assessmentRecord.setPhone(MyApplication.patient.getPhone());
        assessmentRecord.setType(finishQuestionSet.getType());
        assessmentRecord.setResult(testResult);
        Calendar calendar= Calendar.getInstance();
        assessmentRecord.setDate(new Timestamp(calendar.getTimeInMillis()));

        HttpUtils.postAssessmentRecord(assessmentRecord, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AssessmentTestActivity.this,"网路走丢了！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() != 200 || !result.equals("1")){
                            Toast.makeText(AssessmentTestActivity.this,"提交失败！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(AssessmentTestActivity.this,"提交成功！",Toast.LENGTH_SHORT).show();
                        finishAndRemoveTask();
                        startActivity(new Intent(AssessmentTestActivity.this,AssessmentRecordActivity.class));
                    }
                });
            }
        });



    }
}
