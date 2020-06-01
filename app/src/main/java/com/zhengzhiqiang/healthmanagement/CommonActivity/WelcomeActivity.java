package com.zhengzhiqiang.healthmanagement.CommonActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import com.zhengzhiqiang.healthmanagement.CommonActivity.GuideActivity;
import com.zhengzhiqiang.healthmanagement.CommonActivity.LoginActivity;
import com.zhengzhiqiang.healthmanagement.R;

public class WelcomeActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            SharedPreferences sharedPreferences=getSharedPreferences("isFirstTime",0);
            Boolean isFirstTime=sharedPreferences.getBoolean("isFirst",true);
            if (isFirstTime) {
                Intent intent = new Intent(this, GuideActivity.class);
                startActivity(intent);

            }else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
            finish();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirst",false);
            editor.apply();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
