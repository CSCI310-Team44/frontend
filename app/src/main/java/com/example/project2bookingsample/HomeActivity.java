package com.example.project2bookingsample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.project2bookingsample.databinding.ActivityHomeBinding;

import java.io.IOException;
import java.net.URL;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        TextView fullNameTextView = (TextView) findViewById(R.id.backAndName);
//        fullNameTextView.setText("Back, "+userFullName);
//
//        try {
//            String vacancyFmt = "http://10.0.2.2:8080/api/booking/vacancy?center=%d&date=%s";
//            URL vacancyURL =
//                    new URL(String.format(vacancyFmt, currentRecCenter.value, getDateString()));
//
//            HTTPRequestSyncRest httpRequest = new HTTPRequestSyncRest();
//            httpRequest.setUrl(vacancyURL);
//            httpRequest.setRequestMethod("GET");
//            httpRequest.sendAndAwaitResponse();
//
//            message = httpRequest.getResponseContent();
//        }
//        catch (IOException ioe) {
//            ioe.printStackTrace();
//            return;
//        }
//
//        setSupportActionBar(binding.toolbar);

//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void OnClickViewProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        // pass along the userid
        intent.putExtra("userid", getIntent().getStringExtra("userid"));
        startActivity(intent);
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}