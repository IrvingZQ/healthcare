package com.zhengzhiqiang.healthmanagement.PatientActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhengzhiqiang.healthmanagement.Adapter.HospitalRecyclerAdapter;
import com.zhengzhiqiang.healthmanagement.Entity.Hospital;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OnlineInquiryFirstActivity extends AppCompatActivity {

    private HospitalRecyclerAdapter hospitalRecyclerAdapter;
    private RecyclerView hospitalRecyclerView;
    private TextView tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_online_inquiry_first);

        tvBack = findViewById(R.id.online_inquiry_tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });

        initRecycler();
    }

    private void initRecycler(){

        HttpUtils.getHospitals(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OnlineInquiryFirstActivity.this,"网络出错了！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String hospitalJson = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 && !hospitalJson.equals("")) {
                            Gson gson = new Gson();
                            List<Hospital> hospitals = gson.fromJson(hospitalJson, new TypeToken<List<Hospital>>() {}.getType());
                            showRecyclerView(hospitals);
                        }else{
                            Toast.makeText(OnlineInquiryFirstActivity.this,"请求数据失败了！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void showRecyclerView(List<Hospital> hospitals){
        hospitalRecyclerAdapter= new HospitalRecyclerAdapter(hospitals);
        hospitalRecyclerAdapter.setOnItemClickListener(new HospitalRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Hospital hospital = hospitals.get(position);
                Intent intent = new Intent(OnlineInquiryFirstActivity.this,OnlineInquirySecondActivity.class);
                intent.putExtra("hospital",hospital);
                startActivity(intent);

            }
        });
        hospitalRecyclerView = findViewById(R.id.recycler_online_inquiry_hospital);
        hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addLayoutAnimation(hospitalRecyclerView);
        hospitalRecyclerView.setAdapter(hospitalRecyclerAdapter);
        hospitalRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
                int childCount = parent.getChildCount();
                DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                for (int i=0;i<childCount;i++){
                    View view = parent.getChildAt(i);
                    int top = view.getBottom()-2;
                    int right =displayMetrics.widthPixels;
                    int bottom=view.getBottom()+2;
                    c.drawRect(0,top,right,bottom,paint);
                }
            }
        });
    }

    private void addLayoutAnimation(ViewGroup viewGroup){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.push_bottom_in);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
        layoutAnimationController.setDelay(0.5f);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        viewGroup.setLayoutAnimation(layoutAnimationController);
    }
}
