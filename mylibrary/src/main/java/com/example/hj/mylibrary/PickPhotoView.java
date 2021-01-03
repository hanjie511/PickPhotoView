package com.example.hj.mylibrary;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PickPhotoView extends LinearLayout{
    private RecyclerView recycler_hj_pickphoto;
    private Context context;
    private Activity activity;
    private RecyclerAdapter adapter;
    private ArrayList<Uri> list;
    public  int REQUEST_CODE_CAMERA = -1;
    public  int REQUEST_CODE_CHOOSE_PICTURE =-1;
    public  int REQUEST_CODE_PREVIEW_PICTURE =-1;
    public  int REQUEST_CODE_READ_EXTERNAL_STORAGE =-1;
    private PhotoListChangedListener photoListChangedListener;
    public List<Uri> photoList = new ArrayList<>();
    public ArrayList<String> photoList1 = new ArrayList<>();
    public List<Uri> bucketList1 = new ArrayList<>();
    private int maxPhotoNum=9;

    public int getRecyclerViewColumn() {
        return recyclerViewColumn;
    }

    public void setRecyclerViewColumn(int recyclerViewColumn) {
        this.recyclerViewColumn = recyclerViewColumn;
    }

    private int recyclerViewColumn=3;
    public PickPhotoView(Context context){
        super(context);
        this.context = context;
        this.activity = (Activity) context;
     //   initView(context, activity);
    }
    public int getREQUEST_CODE_CAMERA() {
        return REQUEST_CODE_CAMERA;
    }

    public void setREQUEST_CODE_CAMERA(int REQUEST_CODE_CAMERA) {
        this.REQUEST_CODE_CAMERA = REQUEST_CODE_CAMERA;
    }

    public int getREQUEST_CODE_CHOOSE_PICTURE() {
        return REQUEST_CODE_CHOOSE_PICTURE;
    }

    public void setREQUEST_CODE_CHOOSE_PICTURE(int REQUEST_CODE_CHOOSE_PICTURE) {
        this.REQUEST_CODE_CHOOSE_PICTURE = REQUEST_CODE_CHOOSE_PICTURE;
    }

    public int getREQUEST_CODE_PREVIEW_PICTURE() {
        return REQUEST_CODE_PREVIEW_PICTURE;
    }

    public void setREQUEST_CODE_PREVIEW_PICTURE(int REQUEST_CODE_PREVIEW_PICTURE) {
        this.REQUEST_CODE_PREVIEW_PICTURE = REQUEST_CODE_PREVIEW_PICTURE;
    }

    public int getREQUEST_CODE_READ_EXTERNAL_STORAGE() {
        return REQUEST_CODE_READ_EXTERNAL_STORAGE;
    }

    public void setREQUEST_CODE_READ_EXTERNAL_STORAGE(int REQUEST_CODE_READ_EXTERNAL_STORAGE) {
        this.REQUEST_CODE_READ_EXTERNAL_STORAGE = REQUEST_CODE_READ_EXTERNAL_STORAGE;
    }
    public void setMaxPhotoNumber(int number){
        this.maxPhotoNum=number;
    }
    public int getMaxPhotoNum(){
        return  this.maxPhotoNum;
    }
    public PickPhotoView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        this.activity = (Activity) context;
     //   initView(context, activity);
    }

    public void initView(final Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.activity_main_hj_pickphoto, null, false);
        recycler_hj_pickphoto = view.findViewById(R.id.recycler_hj_pickphoto);
        if(recyclerViewColumn>3){
            try {
                throw new Exception("recyclerViewColumn not over 3");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        GridLayoutManager layout = new GridLayoutManager(context, recyclerViewColumn);
        recycler_hj_pickphoto.setLayoutManager(layout);
        photoList1.clear();
        adapter = new RecyclerAdapter(photoList1, context,maxPhotoNum,activity,REQUEST_CODE_PREVIEW_PICTURE);
        recycler_hj_pickphoto.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void click(RecyclerView.ViewHolder holder, int position) throws Exception {
                if(REQUEST_CODE_CAMERA==-1||REQUEST_CODE_CHOOSE_PICTURE==-1||REQUEST_CODE_PREVIEW_PICTURE==-1||REQUEST_CODE_READ_EXTERNAL_STORAGE==-1){
                    throw new Exception("REQUEST_CODE not init Exception");
                }
                PopupMenu menu = new PopupMenu(context, holder.itemView, Gravity.BOTTOM);
                final PopupMenu menu1 = menu;
                menu.getMenuInflater().inflate(R.menu.pop_menu, menu.getMenu());
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.goPhoto_hj_pickphoto) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                photo();
                            } else {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                            }
                        } else if (itemId == R.id.goPicture_hj_pickphoto) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                bucketList1.clear();
                                Intent intent = new Intent(context, ImageBucketActivity.class);
                                intent.putExtra("maxPictureNum",maxPhotoNum);
                                intent.putExtra("requestCode",REQUEST_CODE_CHOOSE_PICTURE);
                                activity.startActivityForResult(intent,REQUEST_CODE_CHOOSE_PICTURE);
                            } else {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
                            }
                        } else if (itemId == R.id.cancel_delete_hj_pickphoto) {
                            menu1.dismiss();
                        }
                        return true;
                    }
                });

            }
        });
        this.addView(view);
    }

    public void setPhotoListChangedListener(PhotoListChangedListener photoListChangedListener) {
        this.photoListChangedListener = photoListChangedListener;
    }

    public void handleRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE_CAMERA){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                photo();
            } else {
                Toast.makeText(context, "未授权", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode==REQUEST_CODE_READ_EXTERNAL_STORAGE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bucketList1.clear();
                Intent intent = new Intent(context, ImageBucketActivity.class);
                intent.putExtra("maxPictureNum",maxPhotoNum);
                activity.startActivityForResult(intent,REQUEST_CODE_CHOOSE_PICTURE);
            } else {
                Toast.makeText(context, "未授权", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == activity.RESULT_OK) {
            if(photoList1.size()<maxPhotoNum){
                if(currentPhotoPath==null){
                    return;
                }
                galleryAddPic();
                photoList.add(contentUri);
                photoList1.add(currentPhotoPath);
                photoListChangedListener.getPhotoList(photoList1);
                adapter = new RecyclerAdapter(photoList1, context,maxPhotoNum,activity,REQUEST_CODE_PREVIEW_PICTURE);
                recycler_hj_pickphoto.setAdapter(adapter);
                adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void click(RecyclerView.ViewHolder holder, int position) throws Exception {
                        if(REQUEST_CODE_CAMERA==-1||REQUEST_CODE_CHOOSE_PICTURE==-1||REQUEST_CODE_PREVIEW_PICTURE==-1||REQUEST_CODE_READ_EXTERNAL_STORAGE==-1){
                            throw new Exception("REQUEST_CODE not init Exception");
                        }
                        PopupMenu menu = new PopupMenu(context, holder.itemView, Gravity.BOTTOM);
                        final PopupMenu menu1 = menu;
                        menu.getMenuInflater().inflate(R.menu.pop_menu, menu.getMenu());
                        menu.show();
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                int itemId = menuItem.getItemId();
                                if (itemId == R.id.goPhoto_hj_pickphoto) {
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                        photo();
                                    } else {
                                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                                    }
                                } else if (itemId == R.id.goPicture_hj_pickphoto) {
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                        bucketList1.clear();
                                        Intent intent = new Intent(context, ImageBucketActivity.class);
                                        intent.putExtra("maxPictureNum",maxPhotoNum);
                                        intent.putExtra("requestCode",REQUEST_CODE_CHOOSE_PICTURE);
                                        activity.startActivityForResult(intent,REQUEST_CODE_CHOOSE_PICTURE);
                                    } else {
                                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
                                    }
                                } else if (itemId == R.id.cancel_delete_hj_pickphoto) {
                                    menu1.dismiss();
                                }
                                return true;
                            }
                        });

                    }
                });
            }
        }else if(requestCode==REQUEST_CODE_CHOOSE_PICTURE){//选择相册图片的回调
            String [] array=data.getStringArrayExtra("chooseImagePicture");
            for (String str : array) {
                if (photoList1.size() < maxPhotoNum) {
                    photoList1.add(str);
                }
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new RecyclerAdapter(photoList1, context,maxPhotoNum,activity,REQUEST_CODE_PREVIEW_PICTURE);
                    recycler_hj_pickphoto.setAdapter(adapter);
                }
            });
            photoListChangedListener.getPhotoList(photoList1);
        }else if(requestCode==REQUEST_CODE_PREVIEW_PICTURE){
            ArrayList<String> list=data.getStringArrayListExtra("previewPictureList");
            photoList1.clear();
            for(String str:list){
                photoList1.add(str);
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new RecyclerAdapter(photoList1, context,maxPhotoNum,activity,REQUEST_CODE_PREVIEW_PICTURE);
                    recycler_hj_pickphoto.setAdapter(adapter);
                    photoListChangedListener.getPhotoList(photoList1);
                }
            });
        }
    }

    String currentPhotoPath;
    Uri contentUri;
    private File createImageFile() throws IOException {
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
        return image;
    }

    private void galleryAddPic() {
        File f = new File(currentPhotoPath);
        scanFile(f);
    }
    //保存图片
    public  void scanFile(File file){
        String mimeType = getMimeType(file);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            String fileName = file.getName();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME,fileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if(uri == null){
                Toast.makeText(context, "图片保存失败", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                OutputStream out = contentResolver.openOutputStream(uri);
                FileInputStream fis = new FileInputStream(file);
                FileUtils.copy(fis,out);
                fis.close();
                out.close();
                Toast.makeText(context, "图片保存成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(currentPhotoPath);
            contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        }
    }


    public static String getMimeType(File file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(file.getName());
        return type;
    }
    public void photo() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        "com.example.hj.mylibrary.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
            }
        }
    }
    public interface PhotoListChangedListener {
        void getPhotoList(List<String> pathList);
    }
}
