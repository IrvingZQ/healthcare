package com.zhengzhiqiang.healthmanagement.Adapter;

import android.content.Context;
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
import com.zhengzhiqiang.healthmanagement.Entity.Sticker;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.DateUtil;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

import java.sql.Date;
import java.util.List;

public class CommunityRecyclerAdapter extends RecyclerView.Adapter<CommunityRecyclerAdapter.MyViewHolder> {

    private List<Sticker> list;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public CommunityRecyclerAdapter(List<Sticker> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityRecyclerAdapter.MyViewHolder holder, final int position) {
        Sticker sticker = list.get(position);
        holder.tvPublisher.setText(String.valueOf(sticker.getNickName()));
        holder.tvDate.setText("发表于 "+DateUtil.FormatDate(new Date(sticker.getDate().getTime()),"yyyy-MM-dd HH:mm"));
        holder.tvContent.setText(sticker.getContent());
        holder.tvCommentCount.setText(String.valueOf(sticker.getCommentCount()));
        holder.tvThumbUpCount.setText(String.valueOf(sticker.getThumbUpCount()));
        if (sticker.getPhoto()!=null){
            holder.ivPhoto.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(MyApplication.Images_Url+sticker.getPhoto())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.ivPhoto);
        }

            Picasso.with(context)
                    .load(MyApplication.Images_Url+sticker.getHeadImage())
                    .error(R.drawable.head_image)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.ivHeadImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvPublisher;
        TextView tvDate;
        TextView tvContent;
        RoundImageView ivHeadImage;
        ImageView ivPhoto;
        TextView tvCommentCount;
        TextView tvThumbUpCount;

        public MyViewHolder(View itemView){
            super(itemView);

            tvPublisher = itemView.findViewById(R.id.sticker_tv_publisher);
            tvDate = itemView.findViewById(R.id.sticker_tv_date);
            tvContent = itemView.findViewById(R.id.sticker_tv_content);
            tvCommentCount = itemView.findViewById(R.id.sticker_tv_comment_count);
            tvThumbUpCount = itemView.findViewById(R.id.sticker_tv_thumb_up_count);
            ivPhoto = itemView.findViewById(R.id.sticker_iv_picture);
            ivHeadImage = itemView.findViewById(R.id.sticker_riv_head_image);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
