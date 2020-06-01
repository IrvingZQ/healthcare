package com.zhengzhiqiang.healthmanagement.CommonActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhengzhiqiang.healthmanagement.Entity.Sticker;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;
import com.zhengzhiqiang.healthmanagement.Utils.PhotoUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PostStickerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvBack;
    private TextView tvPost;
    private EditText etContent;
    private ImageView ivPhoto;

    //选择照片的uri
    private Uri headImageUri;
    //相册选取图片
    private final int CODE_PICK_PHOTO = 4;
    //拍照
    private final int CODE_TAKE_PHOTO = 5;
    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private AlertDialog picDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_post_sticker);
        initEvent();
    }

    //获取到相册里的照片后显示出来
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_TAKE_PHOTO){
            ivPhoto.setImageURI(headImageUri);
        }else if (data != null && requestCode == CODE_PICK_PHOTO) {
            headImageUri = data.getData();
            ivPhoto.setImageURI(headImageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_TAKE_PHOTO:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCamera();
                } else {
                    Toast.makeText(PostStickerActivity.this, "相机权限禁用了。请务必开启相机权", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initEvent(){
        tvBack = findViewById(R.id.post_sticker_tv_back);
        tvBack.setOnClickListener(this);

        tvPost= findViewById(R.id.post_sticker_tv_confirm);
        tvPost.setOnClickListener(this);

        etContent = findViewById(R.id.post_sticker_et_content);

        ivPhoto = findViewById(R.id.post_sticker_iv_picture);
        ivPhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.post_sticker_tv_back:
                finishAndRemoveTask();
                break;
            case R.id.post_sticker_iv_picture:
                choosePhoto();
                break;
            case R.id.post_sticker_tv_confirm:
                if (etContent.getText().toString().trim().equals("")&&headImageUri == null){
                    return;
                }
                postSticker();
                break;
        }

    }

    private void showCamera() {
        //有权限，直接拍照
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues values = new ContentValues();
            headImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, headImageUri);
            startActivityForResult(intent, CODE_TAKE_PHOTO);
        }
    }

    private void choosePhoto(){
        View picView = LayoutInflater.from(PostStickerActivity.this).inflate(R.layout.pic_alert_dialog, null);
        btn_take_photo = picView.findViewById(R.id.btn_take_photo);
        btn_pick_photo = picView.findViewById(R.id.btn_pick_photo);
        btn_cancel = picView.findViewById(R.id.btn_cancel);
        //添加按钮监听
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picDialog.cancel();
            }
        });
        btn_pick_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picDialog.cancel();
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, CODE_PICK_PHOTO);
            }
        });
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picDialog.cancel();
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(PostStickerActivity.this, Manifest.permission.CAMERA);
                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(PostStickerActivity.this,new String[]{Manifest.permission.CAMERA},CODE_TAKE_PHOTO);
                        return;
                    }else{
                        //有权限，直接拍照
                        showCamera();
                    }
                } else {
                    showCamera();
                }

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(PostStickerActivity.this)
                .setView(picView);
        picDialog = builder.create();
        picDialog.show();
        Window window = picDialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void postSticker(){
        Sticker sticker = new Sticker();

        sticker.setPhone(Long.valueOf(MyApplication.phone));

        Calendar calendar = Calendar.getInstance();
        Timestamp date = new Timestamp(calendar.getTimeInMillis());
        sticker.setDate(date);

        sticker.setContent(etContent.getText().toString().trim());

        if(headImageUri != null){
            String photoPath = PhotoUtils.getRealFilePath(PostStickerActivity.this,headImageUri);
            String photoString = PhotoUtils.bitmapToString(photoPath);
            sticker.setPhoto(photoString);
        }

        HttpUtils.postSticker(sticker, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PostStickerActivity.this,"网络出错了",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(PostStickerActivity.this,"发表成功！",Toast.LENGTH_SHORT).show();
                            finishAndRemoveTask();
                        }
                    }
                });

            }
        });
    }
}
