package com.zhengzhiqiang.healthmanagement.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zhengzhiqiang.healthmanagement.CommonActivity.MainActivity;
import com.zhengzhiqiang.healthmanagement.Entity.News;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

import java.util.List;

public class KnowledgeRecyclerAdapter extends RecyclerView.Adapter<KnowledgeRecyclerAdapter.MyViewHolder> {

    private List<News> list;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public KnowledgeRecyclerAdapter(List<News> list,Context context) {
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

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_health_knowledge,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KnowledgeRecyclerAdapter.MyViewHolder holder, final int position) {
        News news = list.get(position);
        holder.tvTitle.setText(news.getTitle());
        holder.tvContent.setText(news.getContent());
        Picasso.with(context)
                .load(MyApplication.Images_Url+news.getPicture())
                .into(holder.ivPicture);
        //holder.ivPicture.setBackground(news.getPicture());
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

        TextView tvTitle;
        TextView tvContent;
        ImageView ivPicture;

        public MyViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_knowledge);
            tvContent = itemView.findViewById(R.id.tv_content_knowledge);
            ivPicture = itemView.findViewById(R.id.knowledge_iv_picture);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
