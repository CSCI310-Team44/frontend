package com.example.frontend.ui.summary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.FakeSingleton;
import com.example.frontend.web.http.HTTPRequestSyncRest;
import com.example.frontend.ui.home.HomeActivity;
import com.example.frontend.R;
import com.example.frontend.web.sse.notifier.Notifier;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class FutureBooking extends AppCompatActivity {
    Button previousBooking, back;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.futurebooking);
        listView = findViewById(R.id.futureBookingList);
        //拿数据
        String message;
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
                                () -> new AlertDialog.Builder(FutureBooking.this)
                                        .setTitle("New Space Available!")
                                        .setMessage("There is a new spot released at " + info[0] + " with a starting time of " + info[1] + ". Move quick or it will be occupied!")
                                        .setNegativeButton("Close", (dialog, which) -> {
                                        }).show()
                        );
                    }
            );
            notifier.start();
            String future = "http://10.0.2.2:8080/api/summary/future?userid=%d";
            @SuppressLint("DefaultLocale") URL vacancyURL =
                    new URL(String.format(future, userId));
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

        ArrayList<FutureBookingInfo> bookingLst = new ArrayList<>();
        for (int i = 0; i < csv.length; i += 3) {
            bookingLst.add(new FutureBookingInfo(csv[i + 1], csv[i], csv[i + 2]));
        }

        ArrayAdapter<FutureBookingInfo> arrayAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookingLst);

        listView.setAdapter(arrayAdapter);

        Long finalUserId = userId; // effective final
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Object o = listView.getItemAtPosition(position);
            FutureBookingInfo bookingInfo = (FutureBookingInfo) o;
            AlertDialog.Builder builder = new AlertDialog.Builder(FutureBooking.this);
            builder
                    .setTitle("Cancel Booking")
                    .setMessage("Are you sure you want to cancel your booking at " + bookingInfo.getRecCenter(bookingInfo.getRecCenterId()) + " with a starting time of " + bookingInfo.getTimeslot() + " ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        String msg;
                        try {
                            String vacancyFmt = "http://10.0.2.2:8080/api/summary/delete?userid=%d&center=%s&datetime=%s";
                            @SuppressLint("DefaultLocale") URL vacancyURL =
                                    new URL(String.format(vacancyFmt, finalUserId, bookingInfo.getRecCenterId(), bookingInfo.getTimeslot()));
                            HTTPRequestSyncRest httpRequest = new HTTPRequestSyncRest();
                            httpRequest.setUrl(vacancyURL);
                            httpRequest.setRequestMethod("GET");
                            httpRequest.sendAndAwaitResponse();
                            msg = httpRequest.getResponseContent();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                            return;
                        }
                        if (msg.equals("Success")) {
                            new AlertDialog.Builder(FutureBooking.this)
                                    .setTitle("Cancel Successful!")
                                    .setMessage("You have successfully cancelled your booking at " + bookingInfo.getRecCenter(bookingInfo.getRecCenterId()) + " with a starting time of " + bookingInfo.getTimeslot() + " .")
                                    .setNegativeButton("Close", (dialog12, which12) -> {
                                        Intent intent = new Intent(FutureBooking.this, FutureBooking.class);
                                        startActivity(intent);
                                    }).show();
                        } else {
                            new AlertDialog.Builder(FutureBooking.this)
                                    .setTitle("Cancel Failed.")
                                    .setMessage("There is an error cancelling your booking at " + bookingInfo.getRecCenter(bookingInfo.getRecCenterId()) + " with a starting time of " + bookingInfo.getTimeslot() + " Please try again!")
                                    .setNegativeButton("Close", (dialog1, which1) -> {
                                        Intent intent = new Intent(FutureBooking.this, FutureBooking.class);
                                        startActivity(intent);
                                    }).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        previousBooking = findViewById(R.id.previousBooking);
        back = findViewById(R.id.back);
        long finalUserId2 = userId;
        previousBooking.setOnClickListener(v -> {
            Intent intent = new Intent(FutureBooking.this, PreviousBooking.class);
            startActivity(intent);
            intent.putExtra("userid", String.valueOf(finalUserId2));
            finish();
        });


        Long finalUserId1 = userId;
        back.setOnClickListener(v -> {
            Intent intent = new Intent(FutureBooking.this, HomeActivity.class);
            startActivity(intent);
            intent.putExtra("userid", String.valueOf(finalUserId1));
            finish();
        });
    }
}





