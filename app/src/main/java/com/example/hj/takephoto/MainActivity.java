package com.example.hj.takephoto;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.hj.mylibrary.PickPhotoView;
import com.example.hj.mylibrary.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<Uri> list;
    Context context;
    private RecyclerView recyclerview_main;
    Uri imageUri;
    private PickPhotoView pickPhotoView;
    private PickPhotoView pickPhotoView1;
    RecyclerAdapter adapter;
    String path="";
    private static final int TAKE_PHOTO=1;
    private Toolbar toolbar;
    private List<String> pathList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        context=MainActivity.this;
        pickPhotoView=findViewById(R.id.pickPhotoView);
        pickPhotoView.setMaxPhotoNumber(1);
        pickPhotoView.setRecyclerViewColumn(1);
        pickPhotoView.setREQUEST_CODE_CAMERA(1);
        pickPhotoView.setREQUEST_CODE_CHOOSE_PICTURE(2);
        pickPhotoView.setREQUEST_CODE_PREVIEW_PICTURE(3);
        pickPhotoView.setREQUEST_CODE_READ_EXTERNAL_STORAGE(4);
        pickPhotoView.setFileProviderAuthority("com.example.hj.takephoto.fileprovider");
        pickPhotoView.setPhotoListChangedListener(new PickPhotoView.PhotoListChangedListener() {
            @Override
            public void getPhotoList(List<String> pathList) {
                System.out.println("pathList.size:"+pathList.size());
            }
        });
        pickPhotoView.initView(MainActivity.this);
        pickPhotoView1=findViewById(R.id.pickPhotoView1);
        pickPhotoView1.setMaxPhotoNumber(1);
        pickPhotoView.setRecyclerViewColumn(1);
        pickPhotoView1.setREQUEST_CODE_CAMERA(5);
        pickPhotoView1.setREQUEST_CODE_CHOOSE_PICTURE(6);
        pickPhotoView1.setREQUEST_CODE_PREVIEW_PICTURE(7);
        pickPhotoView1.setREQUEST_CODE_READ_EXTERNAL_STORAGE(8);
        pickPhotoView1.setFileProviderAuthority("com.example.hj.takephoto.fileprovider");
        pickPhotoView1.setPhotoListChangedListener(new PickPhotoView.PhotoListChangedListener() {
            @Override
            public void getPhotoList(List<String> pathList) {
                System.out.println("pathList.size1:"+pathList.size());
            }
        });
        pickPhotoView1.initView(MainActivity.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pickPhotoView.handleOnActivityResult(requestCode,resultCode,data);
        pickPhotoView1.handleOnActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        pickPhotoView.handleRequestPermissionResult(requestCode,permissions,grantResults);
        pickPhotoView1.handleRequestPermissionResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

        }
    }

    @Override
    protected void onResume() {
//        adapter.notifyDataSetChanged();
        super.onResume();
    }

}
