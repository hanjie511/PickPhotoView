package com.example.hj.mylibrary;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageBucketActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private Context context;
    private BucketRecyclerAdapter adapter;
    private ArrayList<Map<String,String>> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_bucket_hj_pickphoto);
        Toolbar toolbar=findViewById(R.id.bucket_toolbar_hj_pickphoto);
        setSupportActionBar(toolbar);
        recycler=findViewById(R.id.bucketRecyclerView_hj_pickphoto);
        context=ImageBucketActivity.this;
        GridLayoutManager grid=new GridLayoutManager(context,2);
        getPictureAlbum();
        adapter=new BucketRecyclerAdapter(list,context);
        recycler.setLayoutManager(grid);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new BucketRecyclerAdapter.OnItemClickListener() {
            @Override
            public void click(int position) {
//                Toast.makeText(context,""+ImagePath.bucket_list.get(position).get("bucket_name"),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ImageBucketActivity.this,ChooseImageActivity.class);
                intent.putExtra("bucketName",list.get(position).get("bucket_name"));
                startActivity(intent);
                finish();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void getPictureAlbum(){
     //   Cursor cursor_bucket=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new String[]{MediaStore.Images.Media.DATA,MediaStore.Images.Media.BUCKET_DISPLAY_NAME,"COUNT ("+MediaStore.Images.Media.BUCKET_DISPLAY_NAME+") as num"},"1=1) group by("+MediaStore.Images.Media.BUCKET_DISPLAY_NAME+"",null,null);
        Cursor cursor_bucket=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new String[]{MediaStore.Images.Media._ID,MediaStore.Images.Media.DATA,MediaStore.Images.Media.BUCKET_DISPLAY_NAME},null,null,null);
        if(cursor_bucket!=null){
            list.clear();
            while(cursor_bucket.moveToNext()){
                Map<String,String> map=new HashMap<>();
                map.put("bucket_data",cursor_bucket.getString(cursor_bucket.getColumnIndex(MediaStore.Images.Media.DATA)));
                map.put("bucket_id",cursor_bucket.getString(cursor_bucket.getColumnIndex(MediaStore.Images.Media._ID)));
                map.put("bucket_name",cursor_bucket.getString(cursor_bucket.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
                list.add(map);
            }
            cursor_bucket.close();
            setList(list);
        }else{
            Toast.makeText(context,"该设备没有照片",Toast.LENGTH_SHORT).show();
        }
    }
    private void setList(List<Map<String,String>> list){
        Set<String> nameSet=new HashSet<>();
        for(Map m:list){
            nameSet.add(""+m.get("bucket_name"));
        }
        String [] nameArray=new String[nameSet.size()];
        String [] dataArray=new String[nameSet.size()];
        String [] idArray=new String[nameSet.size()];
        int [] numArray=new int[nameSet.size()];
        Iterator<Map<String,String>> mIter=list.iterator();
        Iterator<String> nameIter=nameSet.iterator();
        String data="";
        String name="";
        String id="";
        int index=0;
        while(nameIter.hasNext()){
            String str=nameIter.next();
            int count=0;
            int i=0;
            for(Map m:list){
                if(str.equals(m.get("bucket_name").toString())){
                    count++;
                    data=""+m.get("bucket_data");
                    name=""+m.get("bucket_name");
                    id=""+m.get("bucket_id");
                }
            }
            nameArray[index]=name;
            dataArray[index]=data;
            numArray[index]=count;
            idArray[index]=id;
            index++;
        }
        list.clear();
        Map<String,String> rm=null;
        Uri uri=null;
        for(int i=0;i<nameArray.length;i++){
            rm=new HashMap<>();
            uri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,Long.parseLong(idArray[i]));
            rm.put("bucket_name",nameArray[i]);
            rm.put("bucket_data",dataArray[i]);
            rm.put("bucket_num",""+numArray[i]);
            rm.put("bucket_uri",""+uri);
            rm.put("bucket_id",""+idArray[i]);
            list.add(rm);
        }
    }
}
