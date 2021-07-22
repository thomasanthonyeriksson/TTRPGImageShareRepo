package com.thomas.ttrpgimageshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

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
    }
}