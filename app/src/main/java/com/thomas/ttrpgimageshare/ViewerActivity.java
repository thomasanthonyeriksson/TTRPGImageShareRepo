package com.thomas.ttrpgimageshare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ViewerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        if (getSupportActionBar() != null);
            getSupportActionBar().hide();
    }
}
