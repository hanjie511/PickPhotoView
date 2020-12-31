package com.example.hj.mylibrary;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChooseImageAdapter extends RecyclerView.Adapter<ChooseImageAdapter.VH> {
    Button confirm_btn;
    ArrayList<Map<String, String>> list;
    Context context;
    int picture_num=0;
    static class VH extends RecyclerView.ViewHolder {
        ImageView imageView;
        CheckBox checkBox;

        public VH(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView_imageRecycler_hj_pickphoto);
            checkBox = view.findViewById(R.id.checkbox_imageRecycler_hj_pickphoto);
        }
    }

    public ChooseImageAdapter(ArrayList<Map<String, String>> list, Button btn,Context context) {
        this.list = list;
        confirm_btn = btn;
        this.context=context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler_item_hj_pickphoto, parent, false);
        VH vh = new VH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
       final  Map<String, String> map = list.get(position);
        Glide.with(context).load(map.get("uri")).into(holder.imageView);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if(picture_num<ImagePath.maxPictureNum){
                        holder.imageView.setAlpha(0.5f);
                        if(ImagePath.pathList.size()<ImagePath.maxPictureNum){
                            ImagePath.bucketList.add(Uri.parse(map.get("uri")));
                            ImagePath.bucketPathList.add(map.get("data"));
                        }
                        picture_num=picture_num+1;
                        confirm_btn.setText("("+picture_num+"/"+ImagePath.maxPictureNum+")完成");
                        confirm_btn.setVisibility(View.VISIBLE);
                    }else{
                        compoundButton.setChecked(false);
                        Toast.makeText(context, "您只能选取"+ImagePath.maxPictureNum+"张图片", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    holder.imageView.setAlpha(1.0f);
                    if(picture_num>0){
                        picture_num=picture_num-1;
                        ImagePath.bucketList.remove(list.get(position).get("uri"));
                        ImagePath.bucketPathList.remove(list.get(position).get("data"));
                        confirm_btn.setText("("+picture_num+"/"+ImagePath.maxPictureNum+")完成");
                    }
                    if(picture_num==0){
                        confirm_btn.setText("完成");
                        confirm_btn.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
