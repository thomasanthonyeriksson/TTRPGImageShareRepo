package com.thomas.ttrpgimageshare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SelectActivity extends AppCompatActivity {

    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 940424;
    private static String[] mImageFileNames = null;
    private static File mFileFolder = null;
    public static void copyFile(Context context, String inputFile) {
        InputStream in = null;
        OutputStream out = null;
        try {
            //create output directory if it doesn't exist
            File dir = context.getDir("Room_Z4HG", Context.MODE_PRIVATE);
            in = new FileInputStream(mFileFolder.getPath()+ File.separator + inputFile);
            out = new FileOutputStream(dir.getPath() + File.separator + inputFile);
            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            // write the output file
            out.flush();
            out.close();
        }
        catch (IOException fnfe1) {
            Log.e("SelectActivity", fnfe1.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }

        mFileFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".png") || name.endsWith(".PNG") || name.endsWith(".JPG") ||
                        name.endsWith(".jpg") || name.endsWith(".jpeg");
            }
        };

        mImageFileNames = mFileFolder.list(filter);

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
            File imgFile = new File(mFileFolder, mImageFileNames[position]);
            Bitmap bmImg = BitmapFactory.decodeFile(imgFile.toString());
            holder.imageView.setImageBitmap(bmImg);
            View.OnClickListener imageViewClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("SelectActivity", "onClick:"+mImageFileNames[position]);
                    copyFile(view.getContext(), mImageFileNames[position]);
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
