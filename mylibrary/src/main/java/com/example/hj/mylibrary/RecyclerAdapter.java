package com.example.hj.mylibrary;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.VH> {
    List<String> list=new ArrayList<>();
    Context context;
    OnItemClickListener listener;
    public RecyclerAdapter(List<String> list, Context context){
        this.list=list;
        this.context=context;
    }
    static  class  VH extends RecyclerView.ViewHolder{
        ImageView image;
        public VH(View v){
           super(v);
           image=v.findViewById(R.id.imageView_hj_pickphoto);
        }
    }
    @Override
    public VH onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_hj_pickphoto,null,true);
        VH vh=new VH(view);
        return vh;
    }
    @Override
    public void onBindViewHolder(VH vh, int i) {
        final RecyclerView.ViewHolder holder=vh;
        final int position=i;
            if(i==list.size()){
                    vh.image.setImageResource(R.drawable.ic_add_photo_hj_takephoto);
                    vh.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           listener.click(holder,position);
                        }
                    });
                if(i==ImagePath.maxPictureNum){
                    vh.image.setVisibility(View.GONE);
                }

            }else{
                Glide.with(context).load(list.get(position)).into(((VH) holder).image);
                vh.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context,PhotoPreviewActivity.class);
                        intent.putExtra("path",list.get(position));
                        context.startActivity(intent);
                    }
                });

            }

    }
    @Override
    public int getItemCount() {
        return list.size()+1;
    }
    public  void setOnItemClickListener(OnItemClickListener l){
        listener=l;
    }
public interface  OnItemClickListener{
        void click(RecyclerView.ViewHolder holder, int position);
}
}
