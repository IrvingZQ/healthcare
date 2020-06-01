package com.zhengzhiqiang.healthmanagement.CommonActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zhengzhiqiang.healthmanagement.CustomView.ImageLoadingDialog;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

public class ImageShowerActivity extends Activity {

    private String imagePath;
    private ImageView ivLargeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_shower);
        imagePath = getIntent().getStringExtra("imagePath");
        showImage();
    }

    private void showImage(){

        final ImageLoadingDialog dialog = new ImageLoadingDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ivLargeImage = findViewById(R.id.iv_show_large_image);
        Picasso.with(ImageShowerActivity.this)
                .load(MyApplication.Images_Url+imagePath)
                .error(R.drawable.head_image)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(ivLargeImage);
        dialog.cancel();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        finish();
        return true;
    }
}
