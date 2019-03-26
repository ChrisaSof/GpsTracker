package com.example.chrissa.gpstracker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    /*We take care of Run Time Permission in order for our application
    to run on devices running Marshmallow or higher*/

    //final static int PERMISSION_ALL = 1;
    //final static String[] PERMISSIONS = [android.Manifest.permission.ACCESS_COARSE_LOCATION,
    //        android.Manifest.permission.ACCESS_FINE_LOCATION];

    private GoogleMap mMap;

    /*We add a new global Marker and MarkerOptions variable
    * as we want to update the position of the same marker
    * not create a new marker every time our position is changing */
    MarkerOptions mo;
    Marker marker;

    /*Edw mallon ginetai klisi ths requestLocation pou zhtaei thn
    * current Location -- kapoio lathos sti diatypwsi */
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mo = new MarkerOptions().position(new LatLng(0,0)).title("Current Location");
        /*if (Build.VERSION.SDK_INT >=23 && !isPermissionGranted()){
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else */
        requestLocation();
        /*if (!isLocationEnabled())
            showAlert(1);*/
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
        marker = mMap.addMarker(mo);

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    //is called whenever new location data is received by the device
    @Override
    public void onLocationChanged(Location location) {
    LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
    marker.setPosition(myCoordinates);
    mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    /* This method will be responsible for setting the criteria of the location requests
    that you will make. Criteria defines the accuracy level required and the power level required.
    According to your criteria the Android system automatically chooses GPS or Network for getting
    your location so that you can focus on what you want rather that choosing the tool that you will
     be using.*/
    private void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider, 10000, 10, this);
    }

    /*isLocationEnabled checks if the location information can be given to apps
    i.e if apps are allowed to get the location information or not.*/
    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(locationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);
    }
    /*isPermissionGranted method checks specifically for your app if your app is allowed to
    access location services or not. If not allowed it requests the permissions to the user.*/
    private boolean isPermissionGranted(){
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.v("my log", "Permission is granted");
            return true;
        } else {
            Log.v("my log", "Permission is not granted");
            return false;
        }
    }

    /*showAlert. It shows a dialog box if the location services in your device is turned off,
    i.e if apps are not allowed to access you current location data. If the user choose to give
    the permissions then he/she is directly taken to the location setting options.*/
    /*private void showAlert(final int status) {
        String message, title, btnText;
        if (status == 1) {
            message = "Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                    "use this app";
            title = "Enable Location";
            btnText = "Location Settings";
        } else {
            message = "Please allow this app to access location!";
            title = "Permission access";
            btnText = "Grant";
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (status == 1) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        } else
                            requestPermissions(PERMISSIONS, PERMISSION_ALL);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }*/
}
