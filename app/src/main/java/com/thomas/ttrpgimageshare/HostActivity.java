package com.thomas.ttrpgimageshare;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
                                name.endsWith(".jpg");
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
    }
}
