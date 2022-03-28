package com.example.project2bookingsample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.project2bookingsample.data.model.LoggedInUser;

import java.net.URL;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get the user's full name by user id
        String userFullName = "User";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            String nameFmt = "http://10.0.2.2:8080/api/user/name?userid=%d";
            // giving the username as an integer (long) as the API requires
            @SuppressLint("DefaultLocale") URL nameURL =
                    new URL(String.format(nameFmt, Integer.parseInt(getIntent().getStringExtra("userid"))));

            HTTPRequestSyncRest httpRequest = new HTTPRequestSyncRest();
            httpRequest.setUrl(nameURL);
            Log.d("DEBUGDISPLAY", nameURL.toString());
            httpRequest.setRequestMethod("GET");
            httpRequest.sendAndAwaitResponse();
            String message = httpRequest.getResponseContent();
            if(message.equals("User Not Found")){
                throw new Exception("User Not Found");
            }else{
                userFullName = message;
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        TextView fullNameTextView = (TextView) findViewById(R.id.fullName);
        fullNameTextView.setText("Name: "+userFullName);

        TextView netIDView = (TextView) findViewById(R.id.netID);
        netIDView.setText("NetID: "+getIntent().getStringExtra("userid"));

    }
    public void OnClickBackToHome(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        // always passing the username as a means of user authentication
        intent.putExtra("userid", getIntent().getStringExtra("userid"));
        startActivity(intent);
    }
}