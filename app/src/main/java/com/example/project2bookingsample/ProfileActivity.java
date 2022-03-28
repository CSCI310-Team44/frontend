package com.example.project2bookingsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

    }
    public void OnClickBackToHome(View view){
        Intent intent = new Intent(this, BookingActivity.class);
        // always passing the username as a means of user authentication
        intent.putExtra("username", "9966389234");
        startActivity(intent);
    }
}