package com.zhengzhiqiang.healthmanagement.CommonActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterAccount;
    private EditText etRegisterName;
    private EditText etRegisterPassword;
    private EditText etConfirmPassword;
    private RadioGroup rgIdentity;
    private Button Btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        etRegisterAccount = findViewById(R.id.et_register_account);
        etRegisterName = findViewById(R.id.et_register_name);
        etRegisterPassword = findViewById(R.id.et_register_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        rgIdentity = findViewById(R.id.register_rb_identity);
        rgIdentity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.register_rb_doctor)
                    MyApplication.IDENTITY_TYPE = MyApplication.IDENTITY_TYPE_DOCTOR;
                else
                    MyApplication.IDENTITY_TYPE = MyApplication.IDENTITY_TYPE_PATIENT;
            }
        });
        Btn_register = findViewById(R.id.btn_register);
        Btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = etRegisterAccount.getText().toString().trim();
                String name = etRegisterName.getText().toString().trim();
                String registerPassword = etRegisterPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                if (phone.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                            .setCancelable(false)
                            .setTitle("账号错误")
                            .setMessage("账号不可为空！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    etRegisterPassword.setText("");
                                    etRegisterPassword.requestFocus();
                                }
                            }).create();
                    alertDialog.show();
                } else if (name.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                            .setCancelable(false)
                            .setTitle("错误")
                            .setMessage("姓名不能为空！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    etRegisterPassword.requestFocus();
                                }
                            }).create();
                    alertDialog.show();
                } else if (registerPassword.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                            .setCancelable(false)
                            .setTitle("密码错误")
                            .setMessage("密码不可为空！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    etRegisterPassword.setText("");
                                    etRegisterPassword.requestFocus();
                                }
                            }).create();
                    alertDialog.show();
                } else {
                    if (!registerPassword.equals(confirmPassword)) {
                        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
                                .setCancelable(false)
                                .setTitle("密码错误")
                                .setMessage("确认密码不一致！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        etConfirmPassword.setText("");
                                        etConfirmPassword.requestFocus();
                                    }
                                }).create();
                        alertDialog.show();
                    } else {
                        HttpUtils.register(phone, registerPassword, name, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(RegisterActivity.this, "服务器出错！", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                String result = response.body().string();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (response.code() == 200 && result.equals("1")) {
                                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                            MyApplication.phone = phone;
                                            SharedPreferences.Editor sharedPreferences = getPreferences(MODE_PRIVATE).edit();
                                            sharedPreferences.putString("userId", phone);
                                            sharedPreferences.apply();
                                            finishAndRemoveTask();
                                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
    }
}
