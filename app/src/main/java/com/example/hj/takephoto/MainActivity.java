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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PickPhotoView.PhotoListChangedListener {
    ArrayList<Uri> list;
    Context context;
    private RecyclerView recyclerview_main;
    Uri imageUri;
    private PickPhotoView pickPhotoView;
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
        recyclerview_main=findViewById(R.id.recyclerview_main);
        pickPhotoView.setMaxPhotoNumber(1);
        pickPhotoView.setPhotoListChangedListener(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(MainActivity.this,3);
        adapter=new RecyclerAdapter(pathList,MainActivity.this);
        recyclerview_main.setLayoutManager(gridLayoutManager);
        recyclerview_main.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pickPhotoView.handleOnActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        pickPhotoView.handleRequestPermissionResult(requestCode,permissions,grantResults);
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

    @Override
    public void getPhotoList(final List<String> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GridLayoutManager gridLayoutManager=new GridLayoutManager(MainActivity.this,3);
                adapter=new RecyclerAdapter(list,MainActivity.this);
                recyclerview_main.setLayoutManager(gridLayoutManager);
                recyclerview_main.setAdapter(adapter);
            }
        });
    }
}
