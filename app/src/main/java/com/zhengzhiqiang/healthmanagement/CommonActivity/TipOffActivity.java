package com.zhengzhiqiang.healthmanagement.CommonActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengzhiqiang.healthmanagement.Entity.Sticker;
import com.zhengzhiqiang.healthmanagement.Entity.TipOff;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TipOffActivity extends AppCompatActivity {

    private ImageView ivCancel;
    private TextView tvNickName;
    private TextView tvContent;
    private EditText etReason;
    private Button btnPost;
    private Sticker sticker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tip_off);
        sticker = (Sticker) getIntent().getSerializableExtra("sticker");
        initEvent();

    }

    private void initEvent(){

        ivCancel = findViewById(R.id.tip_off_iv_cancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });

        tvNickName = findViewById(R.id.tip_off_tv_nick_name);
        tvNickName.setText("举报"+sticker.getNickName()+"的帖子:");

        tvContent = findViewById(R.id.tip_off_tv_sticker_content);
        tvContent.setText(sticker.getContent());

        etReason = findViewById(R.id.tip_off_et_reason);

        btnPost = findViewById(R.id.tip_off_btn_post);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etReason.getText().toString().trim().equals("")){
                    Toast.makeText(TipOffActivity.this,"请说明举报的原因！",Toast.LENGTH_SHORT).show();
                    return;
                }

                TipOff tipOff = new TipOff();
                tipOff.setStickerId(sticker.getId());
                tipOff.setReason(etReason.getText().toString().trim());
                tipOff.setTipOffPhone(Long.valueOf(MyApplication.phone));

                HttpUtils.postTipOff(tipOff, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TipOffActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(TipOffActivity.this,"已举报",Toast.LENGTH_SHORT).show();
                                    finishAndRemoveTask();
                                }else {
                                    Toast.makeText(TipOffActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
