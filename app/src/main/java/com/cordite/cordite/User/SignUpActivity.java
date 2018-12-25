package com.cordite.cordite.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.cordite.cordite.HomeActivity;
import com.cordite.cordite.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);

        startActivity(intent);
    }
}
