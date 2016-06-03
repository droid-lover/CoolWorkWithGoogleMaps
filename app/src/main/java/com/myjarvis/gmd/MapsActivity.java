package com.myjarvis.gmd;
/*this project is the property of jarvis
   Sachin*/

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng currentLoc;

    List<Address> mAddress;

    Geocoder myGeocoder;

    String myCityName, mySubLocality = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double longitude, latitude;
        int fillColor = 0x80BBDEFB;
        myGeocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (Utils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    LocationManager lm = (LocationManager) getSystemService(Context
                            .LOCATION_SERVICE);
                    Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    currentLoc = new LatLng(latitude, longitude);
                    Log.d("sachin'slatlong", " is :" + currentLoc);
                    mAddress = myGeocoder.getFromLocation(latitude, longitude, 1);

                    myCityName = mAddress.get(0).getLocality();
                    mySubLocality = mAddress.get(0).getSubLocality();

                    mMap.addMarker(new MarkerOptions().position(currentLoc).title(mySubLocality));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(currentLoc)
                            .zoom(15)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(latitude, longitude))
                            .radius(500)
                            .strokeColor(Color.RED)
                            .fillColor(fillColor));
                    circle.setCenter(currentLoc);


                    Toast.makeText(MapsActivity.this, "adress Is:" + myCityName + mySubLocality, Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }
            } else if (Build.VERSION.SDK_INT < 23) {
                mMap.setMyLocationEnabled(true);
                LocationManager lm = (LocationManager) getSystemService(Context
                        .LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                Log.d("sachin'slatlong", " is :" + currentLoc);
                currentLoc = new LatLng(latitude, longitude);

                mMap.addMarker(new MarkerOptions().position(currentLoc).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentLoc)
                        .zoom(15)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(latitude, longitude))
                        .radius(400)
                        .strokeColor(Color.TRANSPARENT)
                        .fillColor(fillColor));
                circle.setCenter(currentLoc);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                float currentZoom = mMap.getCameraPosition().zoom;
//
//                Log.d("currentZoom", ":" + currentZoom);
//
//
//                if (currentZoom > 17) {
//                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                }
//            }
//        });
//

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                float currentZoom = mMap.getCameraPosition().zoom;

                Log.d("currentZoom", ":" + currentZoom);


                if (currentZoom > 16) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                if (currentZoom < 15) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
                        mMap.addMarker(new MarkerOptions().position(currentLoc)
                                .title("Device location"));
                        Log.d("sachin'slatlong", " is :" + currentLoc);
                        return;
                    }

                } else {

                    Toast.makeText(this, "Permission Denied, You cannot access this component.", Toast.LENGTH_LONG).show();
                    // Add a marker in Sydney and move the camera
                    LatLng iisc = new LatLng(13.0219, 77.5671);
                    mMap.addMarker(new MarkerOptions().position(iisc).title("Marker at IISC bangalore"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(iisc));


                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(iisc)
                            .zoom(15)
                            .build();


//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                float currentZoom = mMap.getCameraPosition().zoom;
//
//                Log.d("currentZoom", ":" + currentZoom);
//
//
//                if (currentZoom > 17) {
//                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                }
//            }
//        });
//

                    mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                        @Override
                        public void onCameraChange(CameraPosition cameraPosition) {
                            float currentZoom = mMap.getCameraPosition().zoom;

                            Log.d("currentZoom", ":" + currentZoom);


                            if (currentZoom > 16) {
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            }
                        }
                    });
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                break;
        }
    }
}
