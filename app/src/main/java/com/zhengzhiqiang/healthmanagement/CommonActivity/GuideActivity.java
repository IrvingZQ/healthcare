package com.zhengzhiqiang.healthmanagement.CommonActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhengzhiqiang.healthmanagement.Adapter.MyGuidePageAdapter;
import com.zhengzhiqiang.healthmanagement.R;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {

    private ViewPager VP_GuidePager;
    private PagerAdapter guideAdapter;
    private static int[] imgs={R.drawable.day_of_month_background,R.drawable.day_of_month_background,R.drawable.day_of_month_background,R.drawable.day_of_month_background};
    private ArrayList<ImageView> imageViews;
    private ImageView[] dotViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        initImages();
        initDots();
        VP_GuidePager = findViewById(R.id.vp_guide);
        guideAdapter = new MyGuidePageAdapter(imageViews);
        VP_GuidePager.setAdapter(guideAdapter);
        VP_GuidePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i=0;i<imgs.length;i++){
                    if (position==i){
                        dotViews[i].setSelected(true);
                    }else {
                        dotViews[i].setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initImages(){
        ViewPager.LayoutParams params=new ViewPager.LayoutParams();
        imageViews = new ArrayList<>();
        for (int i= 0;i<imgs.length;i++){
            ImageView iv=new ImageView(this);
            iv.setImageResource(imgs[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setLayoutParams(params);
            imageViews.add(iv);
            if(i==imgs.length-1){

                iv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    }
                });
            }
        }

    }

    private void initDots(){
        LinearLayout layout = findViewById(R.id.dot_Layout);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(20,20);
        params.setMargins(10,0,10,0);
        dotViews = new ImageView[imgs.length];
        for (int i=0;i<imageViews.size();i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(R.drawable.dot_selector);
            iv.setLayoutParams(params);
            if (i == 0) {
                iv.setSelected(true);
            } else {
                iv.setSelected(false);
            }
            dotViews[i] = iv;
            layout.addView(iv);
        }
    }
}
