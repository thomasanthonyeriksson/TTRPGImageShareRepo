package com.thomas.ttrpgimageshare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FilenameFilter;

public class HostActivity extends AppCompatActivity {
    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 940424;
    private static String mRoomName = null;
    private static String[] mImageFileNames = null;
    private static File mAppImageDirectory = null;
    private static SelectGridAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        TextView textView = findViewById(R.id.hostText);
        mRoomName = textView.getText().toString();
        mAppImageDirectory = getApplicationContext().getDir(mRoomName, Context.MODE_PRIVATE);
        mImageFileNames = mAppImageDirectory.list();

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d("HostActivity","Inside onActivityResult");
                        mImageFileNames = mAppImageDirectory.list();
                        mAdapter.notifyDataSetChanged();
                    }
                });


        View.OnClickListener addButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HostActivity.this, SelectActivity.class);
                someActivityResultLauncher.launch(startIntent);
            }
        };


        Button addButton = findViewById(R.id.buttonAdd);
        addButton.setOnClickListener(addButtonClickListener);

        RecyclerView rvImages = (RecyclerView) findViewById(R.id.selectGrid);
        mAdapter = new SelectGridAdapter();
        rvImages.setAdapter(mAdapter);
        rvImages.setLayoutManager(new GridLayoutManager(this, 3));

    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;

        public ImageViewHolder(View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    public static class SelectGridAdapter extends RecyclerView.Adapter<ImageViewHolder> {

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_grid_item, parent, false);

            return new ImageViewHolder(view);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            File imgFile = new File(mAppImageDirectory, mImageFileNames[holder.getAdapterPosition()]);
            Bitmap bmImg = BitmapFactory.decodeFile(imgFile.toString());
            holder.imageView.setImageBitmap(bmImg);
            View.OnClickListener imageViewClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("SelectActivity", "onClick:" + mImageFileNames[holder.getAdapterPosition()]);
                }
            };
            holder.imageView.setOnClickListener(imageViewClickListener);
        }

        @Override
        public int getItemCount() {
            return mImageFileNames.length;
        }

    }
}
