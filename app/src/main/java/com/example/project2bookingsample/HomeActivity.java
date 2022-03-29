package com.example.project2bookingsample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.project2bookingsample.databinding.ActivityHomeBinding;

import java.io.IOException;
import java.net.URL;

//public class HomeActivity extends AppCompatActivity {

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomeBinding binding;
    private GoogleMap mMap;
    private TextView mSelectedCenter;
    private int mCenterID;

    public void OnClickShowSummary(View view) {
        Intent intent = new Intent(this, FutureBooking.class);
        // pass along the userid
        intent.putExtra("userid", getIntent().getStringExtra("userid"));
        startActivity(intent);
    }

    public enum RecCenter {
        LYON_CENTER(0, "LYON CENTER",34.02458465159024, -118.2883445513555),
        VILLAGE_CENTER(1, "VILLAGE CENTER",34.0250306490167, -118.28560868454927),
        HSC_CENTER(2, "HSC CENTER",34.06589845255248, -118.19668270228046);

        public final int value;
        public final String name;
        public final double lat;
        public final double longitude;
        public final LatLng latLng;

        RecCenter(int value, String name, double lat, double longitude) {
            this.value = value;
            this.name = name;
            this.lat = lat;
            this.longitude = longitude;
            this.latLng = new LatLng(lat, longitude);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView selectedCenter = (TextView) findViewById(R.id.selectedCenter);
        mSelectedCenter = selectedCenter;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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

    public void OnClickMakeBooking(View view) {
        Intent intent = new Intent(this, BookingActivity.class);
        // pass along the userid
        intent.putExtra("userid", getIntent().getStringExtra("userid"));
        intent.putExtra("centerid", mCenterID);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //TODO: HomeActivity or MapsFragment
        googleMap.addMarker(new MarkerOptions().position(HomeActivity.RecCenter.LYON_CENTER.latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("UCS Lyon Center").snippet("Hours: M-F 10AM - 4PM"));
        googleMap.addMarker(new MarkerOptions().position(HomeActivity.RecCenter.VILLAGE_CENTER.latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Village Fitness Center").snippet("Hours: M-F 10AM - 4PM"));
        googleMap.addMarker(new MarkerOptions().position(HomeActivity.RecCenter.HSC_CENTER.latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("HSC Center").snippet("Hours: M-F 10AM - 4PM"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HomeActivity.RecCenter.LYON_CENTER.latLng, 10));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if(marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                mSelectedCenter.setText(marker.getTitle());
                if(marker.getTitle().equals("UCS Lyon Center")){
                    mCenterID = 0;
                } else if(marker.getTitle().equals("Village Fitness Center")){
                    mCenterID = 1;
                } else if(marker.getTitle().equals("HSC Center")){
                    mCenterID = 2;
                }
                return true;
            }
        });
    }

}