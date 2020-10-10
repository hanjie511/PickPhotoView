package com.example.hj.mylibrary;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BucketRecyclerAdapter extends RecyclerView.Adapter<BucketRecyclerAdapter.VH> {
    ArrayList<Map<String,String>> list;
    Context context;
    private OnItemClickListener listener;
    String bucketName="";
    static class VH extends  RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        TextView number;
        public VH(View view){
            super(view);
            image=view.findViewById(R.id.bucket_imageView_hj_pickphoto);
            title=view.findViewById(R.id.bucket_title_hj_pickphoto);
            number=view.findViewById(R.id.bucket_number_hj_pickphoto);
        }
    }
    public BucketRecyclerAdapter(ArrayList<Map<String,String>> list, Context context){
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_recycler_item,parent,false);
        VH vh=new VH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        Map<String,String> map=list.get(position);
        MediaStore.Images.Media media=new MediaStore.Images.Media();
       Glide.with(context).load(map.get("bucket_uri")).into(holder.image);
       // Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.parseLong(map.get("bucket_id")));
      //  Glide.with(context).load(uri).into(holder.image);
        holder.title.setText(map.get("bucket_name"));
        holder.number.setText(map.get("bucket_num"));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.click(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setOnItemClickListener(OnItemClickListener listener){this.listener=listener;}
    public interface OnItemClickListener{
        void click(int position);
    }
}
