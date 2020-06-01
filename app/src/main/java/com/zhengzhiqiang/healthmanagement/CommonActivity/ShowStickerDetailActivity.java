package com.zhengzhiqiang.healthmanagement.CommonActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zhengzhiqiang.healthmanagement.Adapter.CommentRecyclerAdapter;
import com.zhengzhiqiang.healthmanagement.CustomView.RoundImageView;
import com.zhengzhiqiang.healthmanagement.Entity.Comment;
import com.zhengzhiqiang.healthmanagement.Entity.Sticker;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.DateUtil;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;
import com.zhengzhiqiang.healthmanagement.Utils.PhotoUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShowStickerDetailActivity extends AppCompatActivity {

    private RecyclerView commentRecyclerView;
    private CommentRecyclerAdapter commentRecyclerAdapter;
    private TextView tvBack;
    private ConstraintLayout layoutDetailArea;
    private RoundImageView rivHeadImage;
    private TextView tvNickName;
    private TextView tvDate;
    private TextView tvContent;
    private ImageView ivThumbUp;
    private ImageView ivTipOff;
    private ImageView iconThumbUpCount;
    private ImageView iconCommentCount;
    private ImageView ivPhoto;
    private TextView tvCommentCount;
    private TextView tvThumbUpCount;
    private TextView tvToComment;

    private Sticker sticker;
    private AlertDialog commentDialog;
    private boolean isThumbUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_sticker_detail);
        tvBack = findViewById(R.id.sticker_detail_tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });
        initView();
        refreshCommentRecyclerView();
    }

    private void initView() {

        sticker = (Sticker) getIntent().getSerializableExtra("sticker");
        //Log.d("ShowStickerDetail", sticker.getHeadImage());

        layoutDetailArea = findViewById(R.id.sticker_detail_area);
        layoutDetailArea.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(String.valueOf(sticker.getPhone()).equals(MyApplication.phone)){
                    AlertDialog alertDialog = new AlertDialog.Builder(ShowStickerDetailActivity.this)
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    HttpUtils.deleteSticker(sticker.getId(), new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(ShowStickerDetailActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
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
                                                        Toast.makeText(ShowStickerDetailActivity.this,"帖子已删除",Toast.LENGTH_SHORT).show();
                                                        finishAndRemoveTask();
                                                    }else{
                                                        Toast.makeText(ShowStickerDetailActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create();
                    alertDialog.show();
                }
                return false;
            }
        });

        rivHeadImage = findViewById(R.id.sticker_detail_riv_head_image);
        Picasso.with(ShowStickerDetailActivity.this)
                .load(MyApplication.Images_Url + sticker.getHeadImage())
                .error(R.drawable.head_image)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(rivHeadImage);
        rivHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoUtils.showLargePhoto(ShowStickerDetailActivity.this,sticker.getHeadImage());
            }
        });


        tvNickName = findViewById(R.id.sticker_detail_tv_publisher);
        if (sticker.getNickName() != null) {
            tvNickName.setText(sticker.getNickName());
        } else {
            tvNickName.setText("普通用户");
        }
        tvDate = findViewById(R.id.sticker_detail_tv_date);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(sticker.getDate());
        tvDate.setText("发布于" + date);
        tvContent = findViewById(R.id.sticker_detail_tv_content);
        tvContent.setText(sticker.getContent());
        ivPhoto = findViewById(R.id.sticker_detail_iv_photo);
        if (sticker.getPhoto() != null) {
            ivPhoto.setVisibility(View.VISIBLE);
            Picasso.with(ShowStickerDetailActivity.this)
                    .load(MyApplication.Images_Url + sticker.getPhoto())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(ivPhoto);
        } else {
            ivPhoto.setVisibility(View.GONE);
        }
        tvCommentCount = findViewById(R.id.sticker_detail_comments_count);
        tvCommentCount.setText("评论:" + sticker.getCommentCount());
        tvThumbUpCount = findViewById(R.id.sticker_detail_tv_thumb_up_count);
        tvThumbUpCount.setText("点赞:" + sticker.getThumbUpCount());

        tvToComment = findViewById(R.id.sticker_detail_tv_to_comment);
        tvToComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWriteCommentDialog();
            }
        });

        ivThumbUp = findViewById(R.id.sticker_detail_iv_thumb_up);
        ivThumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeThumbUpStatus();
            }
        });
        checkThumbStatus();

        ivTipOff = findViewById(R.id.sticker_detail_iv_tip_off);
        ivTipOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowStickerDetailActivity.this, TipOffActivity.class);
                intent.putExtra("sticker", sticker);
                startActivity(intent);
            }
        });

        //点赞数图标事件，点击切换显示点赞用户列表
        iconThumbUpCount = findViewById(R.id.sticker_detail_icon_thumb_up);
        iconThumbUpCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //评论数图标事件，点击切换显示评论用户列表
        iconCommentCount = findViewById(R.id.sticker_detail_icon_comment);
        iconCommentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        commentRecyclerView = findViewById(R.id.recycler_sticker_comments);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(ShowStickerDetailActivity.this));
        commentRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
                int childCount = parent.getChildCount();
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                for (int i = 0; i < childCount; i++) {
                    View view = parent.getChildAt(i);
                    int top = view.getBottom() - 2;
                    int right = displayMetrics.widthPixels;
                    int bottom = view.getBottom() + 2;
                    c.drawRect(0, top, right, bottom, paint);
                }
            }
        });
    }

    private void refreshCommentRecyclerView() {
        HttpUtils.getCommentsById(sticker.getId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ShowStickerDetail", "网络出错了");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String commentJson = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            List<Comment> commentList = HttpUtils.gson.fromJson(commentJson, new TypeToken<List<Comment>>() {
                            }.getType());
                            if (commentList.size() == 0)
                                return;
                            commentRecyclerAdapter = new CommentRecyclerAdapter(commentList, ShowStickerDetailActivity.this);
                            commentRecyclerAdapter.setOnItemHeadImageClickListener(new CommentRecyclerAdapter.OnItemHeadImageClickListener() {
                                @Override
                                public void onItemHeadImageClick(String imagePath) {
                                    PhotoUtils.showLargePhoto(ShowStickerDetailActivity.this,imagePath);
                                }
                            });
                            commentRecyclerView.setAdapter(commentRecyclerAdapter);
                        }
                    }
                });
            }
        });
    }

    private void showWriteCommentDialog() {
        View dialogLayout = LayoutInflater.from(ShowStickerDetailActivity.this).inflate(R.layout.write_comment_dialog, null, false);
        commentDialog = new AlertDialog.Builder(ShowStickerDetailActivity.this)
                .setView(dialogLayout)
                .setCancelable(false)
                .create();
        EditText etWriteComment = dialogLayout.findViewById(R.id.comment_dialog_et_content);
        Button btnCancel = dialogLayout.findViewById(R.id.comment_dialog_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentDialog.cancel();
            }
        });
        Button btnPublish = dialogLayout.findViewById(R.id.comment_dialog_btn_publish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = etWriteComment.getText().toString().trim();
                if (!commentContent.equals("")) {
                    Comment comment = new Comment();
                    comment.setContent(commentContent);
                    comment.setCommentPhone(Long.valueOf(MyApplication.phone));
                    comment.setDate(DateUtil.getCurrentDate());
                    comment.setStickerId(sticker.getId());
                    HttpUtils.postComment(comment, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ShowStickerDetailActivity.this, "网络出错了", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (response.code() == 200 && result.equals("1")) {
                                        Toast.makeText(ShowStickerDetailActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                        commentDialog.cancel();
                                        refreshCommentRecyclerView();
                                        sticker.setCommentCount(sticker.getCommentCount() + 1);
                                        tvCommentCount.setText("评论：" + sticker.getCommentCount());
                                        //刷新评论
                                    } else {
                                        Toast.makeText(ShowStickerDetailActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

        commentDialog = new AlertDialog.Builder(ShowStickerDetailActivity.this)
                .setView(dialogLayout)
                .setCancelable(false)
                .create();
        commentDialog.show();
        Window window = commentDialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        //window.setBackgroundDrawableResource(android.R.color.transparent);
    }


    private void changeThumbUpStatus() {
        HttpUtils.changeThumbUpStatus(sticker.getId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ShowStickerDetail", "网络出错");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 && result.equals("1")) {
                            if (isThumbUp == false) {
                                ivThumbUp.setImageDrawable(getDrawable(R.mipmap.thumb_up_true));
                                isThumbUp = true;
                                sticker.setThumbUpCount(sticker.getThumbUpCount() + 1);
                                tvThumbUpCount.setText("点赞:" + String.valueOf(sticker.getThumbUpCount()));
                            } else {
                                ivThumbUp.setImageDrawable(getDrawable(R.mipmap.thumb_up_false));
                                isThumbUp = false;
                                sticker.setThumbUpCount(sticker.getThumbUpCount() - 1);
                                tvThumbUpCount.setText("点赞:" + String.valueOf(sticker.getThumbUpCount()));
                            }
                        }
                    }
                });
            }
        });
    }

    private void checkThumbStatus() {
        HttpUtils.checkThumbUpStatus(sticker.getId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ShowStickerDetail", "checkThumbStatus网络出错");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            if (result.equals("1")) {
                                ivThumbUp.setImageDrawable(getDrawable(R.mipmap.thumb_up_true));
                                isThumbUp = true;
                            } else {
                                ivThumbUp.setImageDrawable(getDrawable(R.mipmap.thumb_up_false));
                                isThumbUp = false;
                            }
                        }
                    }
                });
            }
        });
    }
}
