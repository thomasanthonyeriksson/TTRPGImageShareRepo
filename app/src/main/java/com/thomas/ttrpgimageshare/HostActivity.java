package com.thomas.ttrpgimageshare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FilenameFilter;

public class HostActivity extends AppCompatActivity {
    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 940424;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        if (getSupportActionBar() != null);
            getSupportActionBar().hide();
        View.OnClickListener addButtonClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("HostActivity", "onClick: Test");
                //String folderPath = "/storage/emulated/0/Download/";
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant that should be quite unique

                    return;
                }
                File folderFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                FilenameFilter filter = new FilenameFilter() {
                    @Override
                    public boolean accept(File f, String name) {
                        return name.endsWith(".png") || name.endsWith(".PNG") || name.endsWith(".JPG") ||
                                name.endsWith(".jpg") || name.endsWith(".jpeg");
                    }
                };

                String[] imageFileNames = folderFile.list(filter);
                for (int i = 0; i < imageFileNames.length; i++) {
                    Log.d("HostActivity", "Filename:"+imageFileNames[i]);
                }
            };
        };
        Button addButton = findViewById(R.id.buttonAdd);
        addButton.setOnClickListener(addButtonClickListener);

        RecyclerView rvImages = (RecyclerView) findViewById(R.id.selectGrid);
        SelectGridAdapter adapter = new SelectGridAdapter();
        rvImages.setAdapter(adapter);
        rvImages.setLayoutManager(new GridLayoutManager(this,3));

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
            if ( position % 2 == 0 )
                holder.imageView.setBackgroundColor(Color.BLACK);
            else
                holder.imageView.setBackgroundColor(Color.WHITE);
        }

        @Override
        public int getItemCount() {
            return 11;
        }
    }
}
