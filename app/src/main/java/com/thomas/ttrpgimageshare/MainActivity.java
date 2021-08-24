package com.thomas.ttrpgimageshare;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 940424;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        View.OnClickListener createRoomClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(MainActivity.this, HostActivity.class);
                startActivity(startIntent);
            }

        };
        Button createRoomButton = findViewById(R.id.buttonCreate);
        createRoomButton.setOnClickListener(createRoomClickListener);

        View.OnClickListener joinRoomClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(MainActivity.this, ViewerActivity.class);
                startActivity(startIntent);
            }

        };
        Button joinRoomButton = findViewById(R.id.buttonJoin);
        joinRoomButton.setOnClickListener(joinRoomClickListener);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }
    }
}