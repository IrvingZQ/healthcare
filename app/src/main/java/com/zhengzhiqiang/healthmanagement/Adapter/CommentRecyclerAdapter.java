package com.zhengzhiqiang.healthmanagement.Adapter;

import android.content.Context;
import android.system.StructStatVfs;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zhengzhiqiang.healthmanagement.CustomView.RoundImageView;
import com.zhengzhiqiang.healthmanagement.Entity.Comment;
import com.zhengzhiqiang.healthmanagement.Entity.Sticker;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.DateUtil;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

import java.sql.Date;
import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentViewHolder> {

    private List<Comment> list;
    private Context context;
    private OnItemHeadImageClickListener onItemHeadImageClickListener;

    public CommentRecyclerAdapter(List<Comment> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public OnItemHeadImageClickListener getOnItemHeadImageClickListener() {
        return onItemHeadImageClickListener;
    }

    public void setOnItemHeadImageClickListener(OnItemHeadImageClickListener onItemHeadImageClickListener) {
        this.onItemHeadImageClickListener = onItemHeadImageClickListener;
    }

    @NonNull
    @Override
    public CommentRecyclerAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.CommentViewHolder holder, int position) {
        Comment comment = list.get(position);
        Picasso.with(context)
                .load(MyApplication.Images_Url+comment.getHeadImage())
                .placeholder(R.drawable.head_image)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.ivHeadImage);
        holder.ivHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemHeadImageClickListener.onItemHeadImageClick(comment.getHeadImage());
            }
        });
        Log.d("ShowStickerDetail","comment.getHeadImage():"+comment.getHeadImage());
        holder.tvName.setText(comment.getNickName());
        holder.tvContent.setText(comment.getContent());
        holder.tvDate.setText(DateUtil.FormatDate(new Date(comment.getDate().getTime()),"yyyy-MM-dd HH:mm"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder{
        RoundImageView ivHeadImage;
        TextView tvName;
        TextView tvContent;
        TextView tvDate;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHeadImage = itemView.findViewById(R.id.comments_riv_item_head_image);
            tvName = itemView.findViewById(R.id.comments_riv_item_name);
            tvContent = itemView.findViewById(R.id.comments_riv_item_content);
            tvDate = itemView.findViewById(R.id.comments_riv_item_date);
        }
    }

    public interface OnItemHeadImageClickListener{
        void onItemHeadImageClick(String imagePath);
    }
}
