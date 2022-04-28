package com.example.project2bookingsample.ui.booking;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2bookingsample.FakeSingleton;
import com.example.project2bookingsample.web.http.HTTPRequestSyncRest;
import com.example.project2bookingsample.ui.home.HomeActivity;
import com.example.project2bookingsample.R;
import com.example.project2bookingsample.databinding.ActivityBookingBinding;
import com.example.project2bookingsample.web.sse.notifier.Notifier;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingActivity extends AppCompatActivity {

    /**
     * The recreation center that the user is currently searching.
     * Passed down by previous UI activity.
     */
    String[] csv;
    /**
     * The date that the user is currently searching. Defaults to today.
     */
    Calendar toDate = Calendar.getInstance();
    int offsetDate = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Stores all booking buttons.
     */
    List<Button> timeButtons = new ArrayList<>();
    List<String> timestr = new ArrayList<>();
    List<Integer> numVacant = new ArrayList<>();
    Button currentTimeButton;

    private ActivityBookingBinding binding;
    private RecCenter currentRecCenter = RecCenter.LYON_CENTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // Enables binding
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
        Solves "android.os.NetworkOnMainThreadException"

        https://stackoverflow.com/questions/22395417/
        error-strictmodeandroidblockguardpolicy-onnetwork
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (getIntent().getIntExtra("centerid", 0) == 0) {
            currentRecCenter = RecCenter.LYON_CENTER;
        } else if (getIntent().getIntExtra("centerid", 0) == 1) {
            currentRecCenter = RecCenter.VILLAGE_CENTER;
        } else if (getIntent().getIntExtra("centerid", 0) == 2) {
            currentRecCenter = RecCenter.HSC_CENTER;
        }
        setTitle(currentRecCenter.name);

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
                            () -> new AlertDialog.Builder(BookingActivity.this)
                                    .setTitle("New Space Available!")
                                    .setMessage("There is a new spot released at " + info[0] + " with a starting time of " + info[1] + ". Move quick or it will be occupied!")
                                    .setNegativeButton("Close", (dialog, which) -> {
                                    }).show()
                    );
                }
        );
        notifier.start();

        binding.date.setText(getDateString());

        timeButtons.add(binding.time1);
        timeButtons.add(binding.time2);
        timeButtons.add(binding.time3);
        timeButtons.add(binding.time4);
        timeButtons.add(binding.time5);
        timeButtons.add(binding.time6);
        timeButtons.add(binding.time7);
        timeButtons.add(binding.time8);
        timeButtons.add(binding.time9);
        timeButtons.add(binding.time10);
        timeButtons.add(binding.time11);
        timeButtons.add(binding.time12);
        timeButtons.add(binding.time13);
        timeButtons.add(binding.time14);
        timeButtons.add(binding.time15);
        timeButtons.add(binding.time16);

        onRefreshDate();

        /**
         * bookingButtons onClick
         */
        for (int buttonIdx = 0; buttonIdx < timeButtons.size(); buttonIdx++) {
            Button button = timeButtons.get(buttonIdx);
            final int fButtonIdx = buttonIdx;
            button.setOnClickListener(el -> {
                // Unselect
                if (currentTimeButton != null) {
                    if (currentTimeButton != button) {
                        currentTimeButton.setBackgroundColor(Color.parseColor("#991B1E"));
                        binding.book.setText("Book");
                    }
                }

                // Select color
                currentTimeButton = button;
                currentTimeButton.getDrawingCacheBackgroundColor();
                currentTimeButton.setBackgroundColor(Color.RED);

                // Change book text
                if (numVacant.get(fButtonIdx) == 0) {
                    binding.book.setText("Remind me");
                }
            });
        }

        binding.book.setOnClickListener(el -> {
            if (currentTimeButton == null) {
                return;
            }

            String message;
            String substring = "";
            try {
                long id = (long) 0;
                try {
                    id = (long) Integer.parseInt(FakeSingleton.getUserid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String vacancyFmt = "http://10.0.2.2:8080/api/booking/book?userid=%d&center=%s&datetime=%s";
                String text = (String) currentTimeButton.getText();
                substring = getDateString() + " " + text.substring(0, 5);
                @SuppressLint("DefaultLocale") URL vacancyURL =
                        new URL(String.format(vacancyFmt, id, String.valueOf(currentRecCenter.value), substring));
                HTTPRequestSyncRest httpRequest = new HTTPRequestSyncRest();
                httpRequest.setUrl(vacancyURL);
                httpRequest.setRequestMethod("GET");
                httpRequest.sendAndAwaitResponse();
                System.out.println();
                message = httpRequest.getResponseContent();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            }
            String centerName;
            if (currentRecCenter.value == 0) {
                centerName = "Lyon Center";
            } else if (currentRecCenter.value == 1) {
                centerName = "Village Center";
            } else {
                centerName = "HSC Center";
            }
            if (message.equals("success")) {
                new AlertDialog.Builder(BookingActivity.this)
                        .setTitle("Booking Successful!")
                        .setMessage("You have successfully made your booking at " + centerName + " with a starting time of " + substring + " .")
                        .setNegativeButton("Close", (dialog12, which12) -> {
                        }).show();
                currentTimeButton.setBackgroundColor(Color.parseColor("#991B1E"));
                currentTimeButton = null;
            }
            else if(message.equals("fail")){
                new AlertDialog.Builder(BookingActivity.this)
                        .setTitle("Booking Failed!")
                        .setMessage("You are not allowed to book the same time slot twice.")
                        .setNegativeButton("Close", (dialog12, which12) -> {
                        }).show();
                currentTimeButton.setBackgroundColor(Color.parseColor("#991B1E"));
                currentTimeButton = null;
            }
            else if(message.equals("fail1")){
                new AlertDialog.Builder(BookingActivity.this)
                        .setTitle("Booking Failed!")
                        .setMessage("You are not allowed to book a past time slot.")
                        .setNegativeButton("Close", (dialog12, which12) -> {
                        }).show();
                currentTimeButton.setBackgroundColor(Color.parseColor("#991B1E"));
                currentTimeButton = null;
            }
            binding.book.setText("Book");
            onRefreshDate();
        });

        /**
         * YTD: yesterday, TMR: tomorrow.
         * Buttons onClick changes the date.
         */
        binding.ytd.setOnClickListener(el -> {
            toDate.add(Calendar.DATE, -1);
            binding.date.setText(getDateString());
            onRefreshDate();
        });

        binding.tmr.setOnClickListener(el -> {
            toDate.add(Calendar.DATE, 1);
            binding.date.setText(getDateString());
            onRefreshDate();
        });

        Button back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(BookingActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Returns calendar date and offset as a string.
     */
    String getDateString() {
        return sdf.format(toDate.getTime());
    }

    /**
     * Fetches and returns new vacancy information from the server
     * based on center and date.
     */
    void onRefreshDate() {
        String message = "";

        try {
            /*
            Solves network bogus: "Failed to connect to /127.0.0.1:8080"

            https://stackoverflow.com/questions/5495534/
            java-net-connectexception-localhost-127-0-0-18080-connection-refused
             */
            String vacancyFmt = "http://10.0.2.2:8080/api/booking/vacancy?center=%d&datetime=%s";
            URL vacancyURL =
                    new URL(String.format(vacancyFmt, currentRecCenter.value, getDateString()));

            HTTPRequestSyncRest httpRequest = new HTTPRequestSyncRest();
            httpRequest.setUrl(vacancyURL);
            httpRequest.setRequestMethod("GET");
            httpRequest.sendAndAwaitResponse();

            message = httpRequest.getResponseContent();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }


        if (message.equals("")) {
            csv = new String[0];
        } else {
            csv = message.split(",");
        }

        // Update button text
        int csvIdx = 0;
        timestr = new ArrayList<>();
        numVacant = new ArrayList<>();
        while(csvIdx < csv.length) {
            timestr.add(csv[csvIdx++]);
            numVacant.add(Integer.parseInt(csv[csvIdx++]));
        }

        String buttonTextFmt = "%s\n\nLeft:\n%s";
        for (int buttonIdx = 0; buttonIdx < timeButtons.size(); buttonIdx++) {
            Button button = timeButtons.get(buttonIdx);
            if (buttonIdx < numVacant.size()) {
                button.setText(
                        String.format(buttonTextFmt, timestr.get(buttonIdx), numVacant.get(buttonIdx))
                );
                button.setVisibility(View.VISIBLE);
            } else {
                button.setVisibility(View.INVISIBLE);
            }
        }
    }

    public enum RecCenter {
        LYON_CENTER(0, "LYON CENTER"),
        VILLAGE_CENTER(1, "VILLAGE CENTER"),
        HSC_CENTER(2, "HSC CENTER");

        public final int value;
        public final String name;

        RecCenter(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }
}