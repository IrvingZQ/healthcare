
package com.zhengzhiqiang.healthmanagement.CommonActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextView tvCancel;
    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_change_password);
        initEvent();
    }

    private void initEvent(){
        tvCancel = findViewById(R.id.change_password_tv_back);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });
        etOldPassword = findViewById(R.id.change_password_et_old_password);
        etNewPassword = findViewById(R.id.change_password_et_new_password);
        etConfirmPassword = findViewById(R.id.change_password_et_confirm_password);
        btnPost = findViewById(R.id.change_password_patient_btn_post);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPwd = etOldPassword.getText().toString().trim();
                String newPwd = etNewPassword.getText().toString().trim();
                String confirmPwd = etConfirmPassword.getText().toString().trim();
                Log.d("ChangePassword","确认修改密码:"+oldPwd+";"+newPwd+";"+confirmPwd);
                if (oldPwd.equals("") || newPwd.equals("") || confirmPwd.equals("")){
                    Toast.makeText(ChangePasswordActivity.this,"请填写完整！",Toast.LENGTH_SHORT).show();
                }else{
                    if (!newPwd.equals(confirmPwd)){
                        Toast.makeText(ChangePasswordActivity.this,"两次输入新密码不一致！",Toast.LENGTH_SHORT).show();
                    }else{
                        HttpUtils.changePassword(oldPwd, newPwd, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ChangePasswordActivity.this,"网络出错！",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String result = response.body().string();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (response.code() == 200 && result.equals("1")){
                                            Toast.makeText(ChangePasswordActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else{
                                            Toast.makeText(ChangePasswordActivity.this,"旧密码错误，修改失败！",Toast.LENGTH_SHORT).show();
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
