package com.example.project2bookingsample.ui.profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2bookingsample.FakeSingleton;
import com.example.project2bookingsample.R;
import com.example.project2bookingsample.ui.home.HomeActivity;
import com.example.project2bookingsample.web.http.HTTPRequestSyncRest;
import com.example.project2bookingsample.web.sse.notifier.Notifier;

import java.io.InputStream;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get the user's full name by user id
        String userFullName = "User";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String userId = FakeSingleton.getUserid();
        long longId = (long) 0;
        try {
            longId = (long) Integer.parseInt(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Notifier notifier = new Notifier(
                longId,
                0,
                str -> {
                    String[] info = str.replace("data:", "").replace("T", " ").split(",");
                    if (Integer.parseInt(info[0]) == 0) {
                        info[0] = "Lyon Center";
                    } else if (Integer.parseInt(info[0]) == 1) {
                        info[0] = "Village Center";
                    } else {
                        info[0] = "HSC Center";
                    }
                    new Handler(Looper.getMainLooper()).post(
                            () -> new AlertDialog.Builder(ProfileActivity.this)
                                    .setTitle("New Space Available!")
                                    .setMessage("There is a new spot released at " + info[0] + " with a starting time of " + info[1] + ". Move quick or it will be occupied!")
                                    .setNegativeButton("Close", (dialog, which) -> {
                                    }).show()
                    );
                }
        );
        notifier.start();

        try {
            String nameFmt = "http://10.0.2.2:8080/api/user/name?userid=%d";
            // giving the username as an integer (long) as the API requires
            @SuppressLint("DefaultLocale") URL nameURL =
                    new URL(String.format(nameFmt, Integer.parseInt(FakeSingleton.getUserid())));
            HTTPRequestSyncRest httpRequest = new HTTPRequestSyncRest();
            httpRequest.setUrl(nameURL);
            Log.d("DEBUGDISPLAY", nameURL.toString());
            httpRequest.setRequestMethod("GET");
            httpRequest.sendAndAwaitResponse();
            String message = httpRequest.getResponseContent();
            if (message.equals("User Not Found")) {
                throw new Exception("User Not Found");
            } else {
                userFullName = message;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView fullNameTextView = (TextView) findViewById(R.id.fullName);
        fullNameTextView.setText("Name: " + userFullName);


        ImageView img = (ImageView) findViewById(R.id.imageView);
        try {
            URL url = new URL("http://10.0.2.2:8080/api/user/profilepic?userid="+ FakeSingleton.getUserid());
            InputStream inputStream = url.openConnection().getInputStream();
            System.out.println("input");
            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            System.out.println(bmp);
            img.setImageBitmap(bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView netIDView = (TextView) findViewById(R.id.netID);
        netIDView.setText("NetID: " + userId);

    }

    public void OnClickBackToHome(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        // always passing the username as a means of user authentication
        intent.putExtra("userid", getIntent().getStringExtra("userid"));
        startActivity(intent);
    }
}