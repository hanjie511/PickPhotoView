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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChooseImageActivity extends AppCompatActivity {
    String bucketName = "";
    Context context;
    ChooseImageAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Map<String,String>>list=new ArrayList<Map<String,String>>();
    Button confirm_btn;
    private  ArrayList<Uri> bucketList=new ArrayList<>();
    private  ArrayList<String> bucketPathList=new ArrayList<>();
    private int maxPictureNum=9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image_hj_pickphoto);
        context = ChooseImageActivity.this;
        Intent intent = getIntent();
        if (intent.getStringExtra("bucketName") != null) {
            bucketName = intent.getStringExtra("bucketName");
        }
        maxPictureNum=intent.getIntExtra("maxPictureNum",9);
        bucketPathList.clear();
        bucketList.clear();
        recyclerView = findViewById(R.id.chooseImage_recycler_hj_pickphoto);
        confirm_btn=findViewById(R.id.confirm_btn_hj_pickphoto);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        getPictureList(bucketName);
        adapter=new ChooseImageAdapter(list,confirm_btn,context,maxPictureNum);
        recyclerView.setAdapter(adapter);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for(Uri str:adapter.getBucketList()){
                                    String path=copyFile(str);
                                    bucketPathList.add(path);
                            }
                 //           bucketListChangedListener.getBucket(bucketPathList);
                        }
                    }).start();
                }else{
                    for(String str:adapter.getBucketPathList()){
                            bucketPathList.add(str);
                    }
               //     bucketListChangedListener.getBucket(bucketPathList);
                }
                finish();
            }
        });
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
                list.add(map);
            }
            cursor_bucket.close();
        }
    }
    public void back(View view){
        Intent intent=new Intent(ChooseImageActivity.this,ImageBucketActivity.class);
        startActivity(intent);
        bucketList.clear();
        bucketPathList.clear();
        finish();
    }
//    protected static void setBucketListChangedListener(BucketListChangedListener bucketListChangedListener){
//        ChooseImageActivity.bucketListChangedListener=bucketListChangedListener;
//    }
//    protected interface BucketListChangedListener{
//        void getBucket(List<String> list);
//    }
}