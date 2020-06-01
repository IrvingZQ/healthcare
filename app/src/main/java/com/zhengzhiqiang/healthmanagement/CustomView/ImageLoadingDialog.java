package com.zhengzhiqiang.healthmanagement.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.zhengzhiqiang.healthmanagement.R;

public class ImageLoadingDialog extends Dialog {
    public ImageLoadingDialog(@NonNull Context context) {
        super(context, R.style.ImageLoadingDialogStyle);
    }

    public ImageLoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_image_loading);
    }

}
