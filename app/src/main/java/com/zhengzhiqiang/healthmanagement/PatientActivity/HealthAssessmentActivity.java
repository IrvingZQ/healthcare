package com.zhengzhiqiang.healthmanagement.PatientActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhengzhiqiang.healthmanagement.Adapter.AssessmentRecyclerAdapter;
import com.zhengzhiqiang.healthmanagement.Entity.QuestionSet;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HealthAssessmentActivity extends AppCompatActivity {

    private RecyclerView assessmentRecyclerView;
    private AssessmentRecyclerAdapter assessmentRecyclerAdapter;
    private TextView tvBack;
    private TextView tvGoRecords;
    private TextView tvBodyQuestionSet;
    private TextView tvPsychologyQuestionSet;
    private List<QuestionSet> bodyQuestionSets;
    private List<QuestionSet> psychologyQuestionSets;

    private final int TYPE_BODY = 0;
    private final int TYPE_PSYCHOLOGY = 1;
    private int currentType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_health_assessment);
        init();
    }

    private void init(){

        tvBack = findViewById(R.id.assessment_tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });
        tvGoRecords = findViewById(R.id.assessment_tv_go_records);
        tvGoRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HealthAssessmentActivity.this,AssessmentRecordActivity.class));
            }
        });

        assessmentRecyclerView = findViewById(R.id.recycler_assessment);
        addLayoutAnimation(assessmentRecyclerView);
        assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(HealthAssessmentActivity.this));
        tvBodyQuestionSet = findViewById(R.id.body_tv_assessment);
        tvPsychologyQuestionSet = findViewById(R.id.psychology_tv_assessment);

        refreshRecyclerView(TYPE_BODY);

        assessmentRecyclerView.setAdapter(assessmentRecyclerAdapter);
        tvBodyQuestionSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBodyQuestionSet.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvPsychologyQuestionSet.setTextColor(getResources().getColor(R.color.text));
                if (bodyQuestionSets == null){
                    refreshRecyclerView(TYPE_BODY);
                }else if(currentType == TYPE_PSYCHOLOGY){
                    assessmentRecyclerAdapter = new AssessmentRecyclerAdapter(bodyQuestionSets);
                    assessmentRecyclerAdapter.setOnItemClickListener(new AssessmentRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(QuestionSet questionSet) {
                            showAlertDialog(questionSet);
                        }
                    });
                    assessmentRecyclerView.setAdapter(assessmentRecyclerAdapter);
                }
                currentType = TYPE_BODY;
            }
        });

        tvPsychologyQuestionSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPsychologyQuestionSet.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvBodyQuestionSet.setTextColor(getResources().getColor(R.color.text));
                if (psychologyQuestionSets == null){
                   refreshRecyclerView(TYPE_PSYCHOLOGY);
                } else if (currentType == TYPE_BODY) {
                    assessmentRecyclerAdapter = new AssessmentRecyclerAdapter(psychologyQuestionSets);
                    assessmentRecyclerAdapter.setOnItemClickListener(new AssessmentRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(QuestionSet questionSet) {
                            showAlertDialog(questionSet);
                        }
                    });
                    assessmentRecyclerView.setAdapter(assessmentRecyclerAdapter);
                }
                currentType = TYPE_PSYCHOLOGY;
            }
        });
    }

    private void refreshRecyclerView(int type){
        HttpUtils.getHealthTestSets(type, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(HealthAssessmentActivity.this,"网络出错了",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String questionSetJson = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 && !questionSetJson.equals("")){
                            Gson gson = new Gson();
                            List<QuestionSet> questionSets = gson.fromJson(questionSetJson,new TypeToken<List<QuestionSet>>(){}.getType());
                            if (type == TYPE_BODY){
                                bodyQuestionSets = questionSets;
                                assessmentRecyclerAdapter = new AssessmentRecyclerAdapter(bodyQuestionSets);
                            }else{
                                psychologyQuestionSets = questionSets;
                                assessmentRecyclerAdapter = new AssessmentRecyclerAdapter(psychologyQuestionSets);
                            }
                            assessmentRecyclerAdapter.setOnItemClickListener(new AssessmentRecyclerAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(QuestionSet questionSet) {
                                    showAlertDialog(questionSet);
                                }
                            });
                            assessmentRecyclerView.setAdapter(assessmentRecyclerAdapter);
                        }
                    }
                });
            }
        });
    }

    private void showAlertDialog(QuestionSet questionSet){
        AlertDialog.Builder builder = new AlertDialog.Builder(HealthAssessmentActivity.this);
        builder.setCancelable(false);
        builder.setMessage("是否开始测试"+questionSet.getName());
        builder.setPositiveButton("开始测试", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(HealthAssessmentActivity.this, AssessmentTestActivity.class);
                intent.putExtra("questionSet",questionSet);
                startActivity(intent);

                dialog.cancel();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void addLayoutAnimation(ViewGroup viewGroup){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.push_bottom_in);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
        layoutAnimationController.setDelay(0.5f);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        viewGroup.setLayoutAnimation(layoutAnimationController);
    }
}
