package com.zhengzhiqiang.healthmanagement.CommonActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etAccount;
    private EditText etPassword;
    private RadioGroup rgIdentity;
    private Button Btn_login;
    private TextView TV_forgetPassword;
    private TextView TV_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();
        //获取默认账号为上一次登录的账号
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String account = sharedPreferences.getString("userId", null);
        if (account != null) {
            etAccount.setText(account);
        }
    }

    private void initView() {
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        rgIdentity = findViewById(R.id.login_rg_identity);
        Btn_login = findViewById(R.id.btn_login);
        Btn_login.setOnClickListener(this);
        TV_forgetPassword = findViewById(R.id.tv_forget_password);
        TV_forgetPassword.setOnClickListener(this);
        TV_register = findViewById(R.id.tv_register);
        TV_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if(rgIdentity.getCheckedRadioButtonId() == R.id.identity_rb_patient){
                    MyApplication.IDENTITY_TYPE = MyApplication.IDENTITY_TYPE_PATIENT;
                }else{
                    MyApplication.IDENTITY_TYPE = MyApplication.IDENTITY_TYPE_DOCTOR;
                }
                String phone = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                Log.d("LoginActivity","MyApplication.IDENTITY_TYPE:"+MyApplication.IDENTITY_TYPE);
                HttpUtils.login(phone, password,new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("LoginActivity","result"+result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200 && result.equals("1")) {
                                    SharedPreferences.Editor sharedPreferences = getPreferences(MODE_PRIVATE).edit();
                                    sharedPreferences.putString("userId", phone);
                                    sharedPreferences.apply();
                                    MyApplication.phone = phone;
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    finishAndRemoveTask();
                                    startActivity(intent);
                                }else if(response.code() == 200 && result.equals("0")){
                                    Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LoginActivity.this, "请求出错！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
                break;
            case R.id.tv_register:
                finishAndRemoveTask();
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            default:
                break;
        }
    }
}
