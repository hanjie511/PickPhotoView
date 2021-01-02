package com.example.hj.mylibrary;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

public class
ImageBucketActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private Context context;
    private BucketRecyclerAdapter adapter;
    private ArrayList<Map<String,String>> list=new ArrayList<>();
    private int maxPictureNum=9;
    ChooseImageAdapter chooseImageAdapter;
    private TextView bucket_title_hj_pickphoto;
    ArrayList<Map<String,String>> chooseImageList=new ArrayList<Map<String,String>>();
    Button confirm_btn;
    private  ArrayList<Uri> bucketList=new ArrayList<>();
    private  ArrayList<String> bucketPathList=new ArrayList<>();
    private int chooseFlag=0;//0表示选择相册1表示选择照片
    private int REQUEST_CODE_CHOOSE_PICTURE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_bucket_hj_pickphoto);
        receiveData();
        recycler=findViewById(R.id.bucketRecyclerView_hj_pickphoto);
        context=ImageBucketActivity.this;
        bucket_title_hj_pickphoto=findViewById(R.id.bucket_title_hj_pickphoto);
        confirm_btn=findViewById(R.id.confirm_btn_hj_pickphoto);
        initChooseBucket();
    }
    private void receiveData(){
        Intent intent=getIntent();
        maxPictureNum=intent.getIntExtra("maxPictureNum",9);
        REQUEST_CODE_CHOOSE_PICTURE=intent.getIntExtra("requestCode",-1);
    }
    private void initChooseBucket(){
        bucket_title_hj_pickphoto.setText("相册列表");
        confirm_btn.setText("完成");
        confirm_btn.setVisibility(View.INVISIBLE);
        GridLayoutManager grid=new GridLayoutManager(context,2);
        getPictureAlbum();
        adapter=new BucketRecyclerAdapter(list,context);
        recycler.setLayoutManager(grid);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new BucketRecyclerAdapter.OnItemClickListener() {
            @Override
            public void click(int position) {
//                Intent intent=new Intent(ImageBucketActivity.this,ChooseImageActivity.class);
//                intent.putExtra("bucketName",list.get(position).get("bucket_name"));
//                intent.putExtra("maxPictureNum",maxPictureNum);
//                startActivity(intent);
//                finish();
                chooseFlag=1;
                initChooseImage(list.get(position).get("bucket_name"),maxPictureNum);
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
    private void initChooseImage(String bucketName,int maxPictureNum){
        bucketPathList.clear();
        bucketList.clear();
        confirm_btn.setText("完成");
        confirm_btn.setVisibility(View.INVISIBLE);
        bucket_title_hj_pickphoto.setText("请选择照片");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        recycler.setLayoutManager(gridLayoutManager);
        getPictureList(bucketName);
        chooseImageAdapter=new ChooseImageAdapter(chooseImageList,confirm_btn,context,maxPictureNum);
        recycler.setAdapter(chooseImageAdapter);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for(Uri str:chooseImageAdapter.getBucketList()){
                                String path=copyFile(str);
                                bucketPathList.add(path);
                            }
                            Intent intent=new Intent();
                            String [] array=new String[bucketPathList.size()];
                            int index=0;
                            for(String str:bucketPathList){
                                array[index]=str;
                                index++;
                            }
                            System.out.println("index:"+index);
                            intent.putExtra("chooseImagePicture",array);
                            intent.putExtra("name","hanjie");
                            ImageBucketActivity.this.setResult(REQUEST_CODE_CHOOSE_PICTURE,intent);
                            ImageBucketActivity.this.finish();
                        }
                    }).start();
                }else{
                    for(String str:chooseImageAdapter.getBucketPathList()){
                        bucketPathList.add(str);
                    }
                    Intent intent=new Intent();
                    String [] array=new String[bucketPathList.size()];
                    int index=0;
                    for(String str:bucketPathList){
                        array[index]=str;
                        index++;
                    }
                    System.out.println("index:"+index);
                    intent.putExtra("chooseImagePicture",array);
                    intent.putExtra("name","hanjie");
                    ImageBucketActivity.this.setResult(REQUEST_CODE_CHOOSE_PICTURE,intent);
                    ImageBucketActivity.this.finish();
                }
            }
        });
    }

    public void back(View view){
        bucketPathList.clear();
        bucketList.clear();
        if(chooseFlag==1){//选择照片
            chooseFlag=0;
            initChooseBucket();
        }else if(chooseFlag==0){//选择相册
            bucketPathList.clear();
            Intent intent=new Intent();
            String [] array=new String[bucketPathList.size()];
            int index=0;
            for(String str:bucketPathList){
                array[index]=str;
                index++;
            }
            intent.putExtra("chooseImagePicture",array);
            ImageBucketActivity.this.setResult(REQUEST_CODE_CHOOSE_PICTURE,intent);
            ImageBucketActivity.this.finish();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        bucketPathList.clear();
        Intent intent=new Intent();
        String [] array=new String[bucketPathList.size()];
        int index=0;
        for(String str:bucketPathList){
            array[index]=str;
            index++;
        }
        intent.putExtra("chooseImagePicture",array);
        ImageBucketActivity.this.setResult(REQUEST_CODE_CHOOSE_PICTURE,intent);
        ImageBucketActivity.this.finish();
        finish();
    }

    private void getPictureList(String bucketName) {
        Cursor cursor_bucket = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID}, MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "='" + bucketName + "'", null, null);
        if (cursor_bucket != null) {
            list.clear();
            Map<String, String> map=null;
            String id="";
            while (cursor_bucket.moveToNext()) {
                map = new HashMap<>();
                id=cursor_bucket.getString(cursor_bucket.getColumnIndex(MediaStore.Images.Media._ID));
                map.put("id", id);
                map.put("data",cursor_bucket.getString(cursor_bucket.getColumnIndex(MediaStore.Images.Media.DATA)));
                map.put("uri", ""+ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,Long.parseLong(id)));
                chooseImageList.add(map);
            }
            cursor_bucket.close();
        }
    }
    @SuppressLint("NewApi")
    private String copyFile(Uri uri){
        String currentPhotoPath="";
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = image.getAbsolutePath();
            InputStream ipt = getContentResolver().openInputStream(uri);
            FileOutputStream opt= new FileOutputStream(image);
            FileUtils.copy(ipt,opt);
            ipt.close();
            opt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentPhotoPath;
    }
}
