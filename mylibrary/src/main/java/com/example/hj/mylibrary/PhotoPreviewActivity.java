package com.example.hj.mylibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhotoPreviewActivity extends AppCompatActivity {
    private String path="";
    private ViewPager viewPager_hj_takePhoto;
    private  int currentPosition=0;
    private Toolbar toolbar;
    private ImageView delete_photoPreview_hj_takePhoto;
    private ImageView croup_photoPreview_hj_takePhoto;
    private MyPagerAdapter myPageAdapter;
    private ArrayList<String> pictureList=new ArrayList<>();
    private int REQUEST_CODE_PREVIEW_PICTURE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        toolbar=findViewById(R.id.toolbar_photoPreview_hj_takePhoto);
        setSupportActionBar(toolbar);
        receiveData();
        initView();
    }
    private void receiveData(){
        Intent intent=getIntent();
        pictureList=intent.getStringArrayListExtra("pictureList");
        REQUEST_CODE_PREVIEW_PICTURE=intent.getIntExtra("requestCode",-1);
    }
    String destiPath="";
    private void initView(){
        viewPager_hj_takePhoto=findViewById(R.id.viewPager_hj_takePhoto);
        viewPager_hj_takePhoto.setAdapter(new MyPagerAdapter());
        delete_photoPreview_hj_takePhoto=findViewById(R.id.delete_photoPreview_hj_takePhoto);
        croup_photoPreview_hj_takePhoto=findViewById(R.id.croup_photoPreview_hj_takePhoto);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putStringArrayListExtra("previewPictureList",pictureList);
                setResult(REQUEST_CODE_PREVIEW_PICTURE,intent);
                finish();
            }
        });
        delete_photoPreview_hj_takePhoto=findViewById(R.id.delete_photoPreview_hj_takePhoto);
        croup_photoPreview_hj_takePhoto=findViewById(R.id.croup_photoPreview_hj_takePhoto);
        delete_photoPreview_hj_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pictureList.size()>1){
                    pictureList.remove(currentPosition);
                    viewPager_hj_takePhoto.setAdapter(new MyPagerAdapter());
                    currentPosition=0;
                }else{
                    pictureList.remove(currentPosition);
                    Intent intent=new Intent();
                    intent.putStringArrayListExtra("previewPictureList",pictureList);
                    setResult(REQUEST_CODE_PREVIEW_PICTURE,intent);
                    finish();
                }
            }
        });
        croup_photoPreview_hj_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri sourceUri = Uri.fromFile(new File(pictureList.get(currentPosition)));
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File image = File.createTempFile(
                            imageFileName,  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                    );
                    destiPath = image.getAbsolutePath();
                    Uri destinationUri = Uri.fromFile(image);
                    //初始化，第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
                    UCrop.of(sourceUri, destinationUri)
                    .withAspectRatio(16, 9)
                            .withMaxResultSize(1080, 1920)
                            .start(PhotoPreviewActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        viewPager_hj_takePhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putStringArrayListExtra("previewPictureList",pictureList);
        setResult(REQUEST_CODE_PREVIEW_PICTURE,intent);
        finish();
    }

    class MyPagerAdapter extends PagerAdapter {
        MyPagerAdapter(){
        }
        @Override
        public int getCount() {
            return pictureList.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            PhotoView photo=new PhotoView(PhotoPreviewActivity.this);
            Bitmap bitmap = BitmapFactory.decodeFile(pictureList.get(position));
            photo.setImageBitmap(bitmap);
            container.addView(photo);
            return photo;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewGroup viewPager = ((ViewGroup) container);
            int count = viewPager.getChildCount();
            for (int i = 0; i < count; i++) {
                View childView = viewPager.getChildAt(i);
                if (childView == object) {
                    viewPager.removeView(childView);
                    break;
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            pictureList.set(currentPosition,destiPath);
            viewPager_hj_takePhoto.setAdapter(new MyPagerAdapter());
            currentPosition=0;
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }
}