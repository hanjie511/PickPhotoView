package com.example.hj.mylibrary;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
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
    ArrayList<String> list=new ArrayList<>();
    Context context;
    OnItemClickListener listener;
    private int maxPictureNum;
    private Activity activity;
    private int REQUEST_CODE_PREVIEW_PICTURE;
    public RecyclerAdapter(ArrayList<String> list, Context context, int maxPictureNum, Activity activity,int requestCode){
        this.list=list;
        this.context=context;
        this.maxPictureNum=maxPictureNum;
        this.activity=activity;
        this.REQUEST_CODE_PREVIEW_PICTURE=requestCode;
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
                    vh.image.setImageResource(R.drawable.ic_add_pic);
                    vh.image.setScaleType(ImageView.ScaleType.CENTER);
                    vh.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                listener.click(holder,position);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                if(i==maxPictureNum){
                    vh.image.setVisibility(View.GONE);
                }

            }else{
                Glide.with(context).load(list.get(position)).into(((VH) holder).image);
                vh.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context,PhotoPreviewActivity.class);
                        intent.putExtra("path",list.get(position));
                        intent.putStringArrayListExtra("pictureList",list);
                        intent.putExtra("requestCode",REQUEST_CODE_PREVIEW_PICTURE);
                        activity.startActivityForResult(intent,REQUEST_CODE_PREVIEW_PICTURE);
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
        void click(RecyclerView.ViewHolder holder, int position) throws Exception;
}
}
