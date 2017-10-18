package aroundwise.nepi.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by mihai on 12/09/16.
 */
public class LocationUtil implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static LocationUtil locationUtil;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private LocationRequest locationRequest;

    protected LocationUtil(Context context) {
        buildGoogleApiClient(context);
    }

    public static LocationUtil getLocationUtil(Context context) {
        if (locationUtil == null)
            locationUtil = new LocationUtil(context);
        return locationUtil;
    }

    public synchronized void buildGoogleApiClient(Context context) {

        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(1000 * 60);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Log.e(LocationUtil.class.getSimpleName(), "Connection suspendet");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
       // Log.e(LocationUtil.class.getSimpleName(), connectionResult.getErrorMessage());
    }

    public void connect() {
        googleApiClient.connect();

    }

    public void disconnect() {
        googleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
    }

    public static boolean checkPermissions(Context context) {
        String coarse = Manifest.permission.ACCESS_COARSE_LOCATION;
        String fine = Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(context, coarse) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, coarse) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{coarse, fine}, Constants.PERMISSION_LOCATION);
            return false;
        }
        return true;
    }

    public Location getLastLocation() {
        return lastLocation;
    }
}
