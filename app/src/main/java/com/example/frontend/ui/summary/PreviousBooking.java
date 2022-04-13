package com.example.frontend.ui.summary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.FakeSingleton;
import com.example.frontend.web.http.HTTPRequestSyncRest;
import com.example.frontend.ui.home.HomeActivity;
import com.example.frontend.web.sse.notifier.Notifier;
import com.example.frontend.R;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class PreviousBooking extends AppCompatActivity {
    Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previousbooking);
        ListView listView = findViewById(R.id.previousBookingList);

        String message;
        //todo 拿currentuserid
        long userId = (long) 0;
        try {
            userId = (long) Integer.parseInt(FakeSingleton.getUserid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            //todo:put it in all activities (except login)
            Notifier notifier = new Notifier(
                    userId,
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
                                () -> new AlertDialog.Builder(PreviousBooking.this)
                                        .setTitle("New Space Available!")
                                        .setMessage("There is a new spot released at " + info[0] + " with a starting time of " + info[1] + ". Move quick or it will be occupied!")
                                        .setNegativeButton("Close", (dialog, which) -> {
                                        }).show()
                        );
                    }
            );
            notifier.start();
            String vacancyFmt = "http://10.0.2.2:8080/api/summary/previous?userid=%d";

            @SuppressLint("DefaultLocale") URL vacancyURL =
                    new URL(String.format(vacancyFmt, userId));

            HTTPRequestSyncRest httpRequest = new HTTPRequestSyncRest();
            httpRequest.setUrl(vacancyURL);
            httpRequest.setRequestMethod("GET");
            httpRequest.sendAndAwaitResponse();
            message = httpRequest.getResponseContent();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        String[] csv;
        if (message.equals("")) {
            csv = new String[0];
        } else {
            csv = message.split(",");
        }

        ArrayList<PreviousBookingInfo> bookingLst = new ArrayList<>();
        for (int i = 0; i < csv.length; i += 2) {
            bookingLst.add(new PreviousBookingInfo(csv[i + 1], csv[i]));
        }

        ArrayAdapter<PreviousBookingInfo> arrayAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookingLst);

        listView.setAdapter(arrayAdapter);

        btn1 = findViewById(R.id.futureBooking);
        btn2 = findViewById(R.id.back);
        btn1.setOnClickListener(v -> {
            Intent intent = new Intent(PreviousBooking.this, FutureBooking.class);
            startActivity(intent);
            finish();
        });
        //todo associate with frontend page
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviousBooking.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
