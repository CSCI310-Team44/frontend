package com.example.project2bookingsample;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.project2bookingsample.databinding.ActivityHomeBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//public class HomeActivity extends AppCompatActivity {

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomeBinding binding;
    private GoogleMap mMap;
    private TextView mSelectedCenter;
    private int mCenterID;
    private String userId;

    public void OnClickShowSummary(View view) {
        Intent intent = new Intent(this, FutureBooking.class);
        // pass along the userid
        intent.putExtra("userid", getIntent().getStringExtra("userid"));
        startActivity(intent);
    }

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
        userId = FakeSingleton.getUserid();
        long longId = (long) 0;
        try {
            longId = (long) Integer.parseInt(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        com.example.project2bookingsample.Notifier notifier = new com.example.project2bookingsample.Notifier(
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
                            () -> new AlertDialog.Builder(HomeActivity.this)
                                    .setTitle("New Space Available!")
                                    .setMessage("There is a new spot released at " + info[0] + " with a starting time of " + info[1] + ". Move quick or it will be occupied!")
                                    .setNegativeButton("Close", (dialog, which) -> {
                                    }).show()
                    );
                }
        );
        notifier.start();
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
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                mSelectedCenter.setText(marker.getTitle());
                if (marker.getTitle().equals("UCS Lyon Center")) {
                    mCenterID = 0;
                } else if (marker.getTitle().equals("Village Fitness Center")) {
                    mCenterID = 1;
                } else if (marker.getTitle().equals("HSC Center")) {
                    mCenterID = 2;
                }
                return true;
            }
        });
    }

    public enum RecCenter {
        LYON_CENTER(0, "LYON CENTER", 34.02458465159024, -118.2883445513555),
        VILLAGE_CENTER(1, "VILLAGE CENTER", 34.0250306490167, -118.28560868454927),
        HSC_CENTER(2, "HSC CENTER", 34.06589845255248, -118.19668270228046);

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
    }

}