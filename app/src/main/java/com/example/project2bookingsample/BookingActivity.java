package com.example.project2bookingsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.example.project2bookingsample.databinding.ActivityBookingBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BookingActivity extends AppCompatActivity {

    private ActivityBookingBinding binding;

    /**
     * The recreation center that the user is currently searching.
     * Passed down by previous UI activity.
     */
    private RecCenter currentRecCenter = RecCenter.LYON_CENTER;

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
    };

    /**
     * The date that the user is currently searching. Defaults to today.
     */
    Calendar toDate = Calendar.getInstance();
    int offsetDate = 0;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Stores all booking buttons.
     */
    List<Button> bookingButtons = new ArrayList<>();

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

        if(getIntent().getIntExtra("centerid", 0) == 0){
            currentRecCenter = RecCenter.LYON_CENTER;
        } else if(getIntent().getIntExtra("centerid", 0) == 1){
            currentRecCenter = RecCenter.VILLAGE_CENTER;
        } else if(getIntent().getIntExtra("centerid", 0) == 2){
            currentRecCenter = RecCenter.HSC_CENTER;
        }
        setTitle(currentRecCenter.name);

        binding.date.setText(getDateString());

        bookingButtons.add(binding.time1);
        bookingButtons.add(binding.time2);
        bookingButtons.add(binding.time3);
        bookingButtons.add(binding.time4);
        bookingButtons.add(binding.time5);
        bookingButtons.add(binding.time6);
        bookingButtons.add(binding.time7);
        bookingButtons.add(binding.time8);
        bookingButtons.add(binding.time9);
        bookingButtons.add(binding.time10);
        bookingButtons.add(binding.time11);
        bookingButtons.add(binding.time12);
        bookingButtons.add(binding.time13);
        bookingButtons.add(binding.time14);
        bookingButtons.add(binding.time15);
        bookingButtons.add(binding.time16);

        onRefreshDate();

        binding.book.setOnClickListener(el -> {
            String message = "default";
            int status = 999;

            try {
                // Server request

                /*
                Solves network bogus: "Failed to connect to /127.0.0.1:8080"

                https://stackoverflow.com/questions/5495534/
                java-net-connectexception-localhost-127-0-0-18080-connection-refused
                 */
                URL bookingUrl = new URL("http://10.0.2.2:8080/booking/");
                HttpURLConnection con = (HttpURLConnection) bookingUrl.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in =
                        new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                message = content.toString();

                in.close();

                con.disconnect();

                System.out.println(con.getResponseCode());
            }
            catch (Exception e) {
                e.printStackTrace();
                message = "ERR";
                // Ignore
            }

            binding.book.setText(message);
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
            String vacancyFmt = "http://10.0.2.2:8080/api/booking/vacancy?center=%d&date=%s";
            URL vacancyURL =
                    new URL(String.format(vacancyFmt, currentRecCenter.value, getDateString()));

            HTTPRequestSyncRest httpRequest = new HTTPRequestSyncRest();
            httpRequest.setUrl(vacancyURL);
            httpRequest.setRequestMethod("GET");
            httpRequest.sendAndAwaitResponse();

            message = httpRequest.getResponseContent();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        String[] csv;
        if(message.equals("")) {
             csv = new String[0];
        }
        else {
            csv = message.split(",");
        }

        // Update button text
        int csvIdx = 0;
        String buttonTextFmt = "%s\n\nLeft:\n%s";
        for(Button button : bookingButtons) {
            if(csvIdx < csv.length) {
                button.setText(
                        String.format(buttonTextFmt, csv[csvIdx], csv[csvIdx + 1])
                );
                button.setVisibility(View.VISIBLE);
            }
            else {
                button.setVisibility(View.INVISIBLE);
            }
            csvIdx += 2;
        }
    }
}