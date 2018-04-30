package com.mschloapps.simplespeed;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static int is_metric = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                updateData(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  }, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        } else {
            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        ToggleButton toggle = findViewById(R.id.switch1);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    is_metric = 1;
                } else {
                    // The toggle is disabled
                    is_metric = 0;
                }
            }
        });

    }

    @Override
    protected void onPause() {
        locationManager.removeUpdates(locationListener);
        super.onPause();
    }

    @Override
    protected void onStop() {
        locationManager.removeUpdates(locationListener);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        locationManager.removeUpdates(locationListener);
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  }, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        super.onResume();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    }
                } else {
                    locationManager.removeUpdates(locationListener);
                }
                return;
            }
        }
    }

    public void updateData(Location loc) {
        double dir;
        String str;
        TextView direction = findViewById(R.id.textView);
        TextView speed = findViewById(R.id.textView2);
        TextView units = findViewById(R.id.textView4);
        ToggleButton met_btn = findViewById(R.id.switch1);
        DecimalFormat fmt1 = new DecimalFormat("##");
        dir = loc.getBearing();
        if ((dir <= 22.5 || dir > 337.5) && loc.getSpeed() > 0.0) {
            str = "N";
        } else if ((dir > 22.5 && dir <= 67.5) && loc.getSpeed() > 0.0) {
            str = "NE";
        } else if ((dir > 67.5 && dir <= 112.5) && loc.getSpeed() > 0.0) {
            str = "E";
        } else if ((dir > 112.5 && dir <= 157.5) && loc.getSpeed() > 0.0) {
            str = "SE";
        } else if ((dir > 157.5 && dir <= 202.5) && loc.getSpeed() > 0.0) {
            str = "S";
        } else if ((dir > 202.5 && dir <= 247.5) && loc.getSpeed() > 0.0) {
            str = "SW";
        } else if ((dir > 247.5 && dir <= 292.5) && loc.getSpeed() > 0.0) {
            str = "W";
        } else if ((dir > 292.5 && dir <= 337.5) && loc.getSpeed() > 0.0) {
            str = "NW";
        } else {
            str = "";
        }
        direction.setText(str);
        if (is_metric == 1) {
            speed.setText(String.valueOf(fmt1.format(loc.getSpeed() * 3.6)));
            units.setText(getString(R.string.kph_name));
        } else {
            speed.setText(String.valueOf(fmt1.format(loc.getSpeed() * 2.23694)));
            units.setText(getString(R.string.mph_name));
        }

    }

}
