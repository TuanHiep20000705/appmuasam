package com.mth.example.banhangapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mth.example.banhangapp.R;

public class NullActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_null);
        Intent intent = new Intent(NullActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}