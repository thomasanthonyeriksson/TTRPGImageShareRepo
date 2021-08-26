package com.thomas.ttrpgimageshare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.apache.http.params.HttpParams;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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

    static public class ImageUploader extends AsyncTask<Void, Void, Void>{
        Context inContext;
        String inRoom;
        String inPath;

        ImageUploader(Context context, String room, String path) {
            inContext = context;
            inRoom = room;
            inPath = path;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream(4096);
            byte[] data;
            try {
                InputStream input = new FileInputStream(inPath);
                int byteReads;
                while ((byteReads = input.read()) != -1) {
                    byteOutStream.write(byteReads);
                }
                data = byteOutStream.toByteArray();
                byteOutStream.close();
                input.close();
            } catch (IOException exception) {
                exception.printStackTrace();
                return null;
            }

            String encodedData = Base64.encodeToString(byteOutStream.toByteArray(), Base64.DEFAULT);

            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://thomaseriksson.infinityfreeapp.com/DownloadPicture.php");

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setReadTimeout(30 * 1000);
                connection.setConnectTimeout(30 * 1000);
                connection.setChunkedStreamingMode(0);
                connection.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("room", inRoom)
                        .appendQueryParameter("imageType", inPath.substring(inPath.lastIndexOf(".") + 1))
                        .appendQueryParameter("image", encodedData);
                String query = builder.build().getEncodedQuery();

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                Log.d("HostActivity", "ResponseCode: " + Integer.toString(connection.getResponseCode()));

            } catch ( Exception ex ) {
                ex.printStackTrace();

            } finally {
                if ( connection != null ) connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Toast.makeText(inContext,"Image uploaded",Toast.LENGTH_SHORT).show();
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
                    new ImageUploader(view.getContext(), "Room_Z4HG", imgFile.getPath()).execute();
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
