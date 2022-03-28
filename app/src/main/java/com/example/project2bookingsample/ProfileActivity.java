package com.example.project2bookingsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.project2bookingsample.data.model.LoggedInUser;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // TODO: complete this section once the backend is ready.
        String firstName = "Yuxing";
        String lastName = "Zhou";
        String netID = "9953222222";
        Integer pastAppointments = 23;


        TextView fullNameTextView = (TextView) findViewById(R.id.fullName);
        fullNameTextView.setText("Name: "+firstName+" "+lastName);

        TextView netIDView = (TextView) findViewById(R.id.netID);
        netIDView.setText("NetID: "+netID);

        TextView completedAppointments = (TextView) findViewById(R.id.completedAppointments);
        completedAppointments.setText("You have completed "+pastAppointments+" appointments over the last year. Fight On!");

    }
    public void OnClickBackToHome(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        // always passing the username as a means of user authentication
        intent.putExtra("username", "Lala");
        startActivity(intent);
    }
}