package com.zhengzhiqiang.healthmanagement.PatientActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhengzhiqiang.healthmanagement.Entity.News;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

public class ShowSingleNewsActivity extends AppCompatActivity {

    private TextView tvBack;
    private TextView tvType;
    private TextView tvTitle;
    private ImageView ivPicture;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_single_news);
        initView();
    }

    private void initView(){
        Intent intent = getIntent();
        News news = (News) intent.getSerializableExtra("news");
        tvBack = findViewById(R.id.news_detail_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvType = findViewById(R.id.news_detail_tv_type);
        tvType.setText(news.getType());

        tvTitle = findViewById(R.id.news_detail_tv_title);
        tvTitle.setText(news.getTitle());

        ivPicture = findViewById(R.id.mews_detail_iv_picture);
        Picasso.with(this)
                .load(MyApplication.Images_Url+news.getPicture())
                .into(ivPicture);

        tvContent = findViewById(R.id.news_detail_tv_content);
        tvContent.setText(news.getContent());

    }
}
