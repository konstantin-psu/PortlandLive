package edu.pdx.konstan2.PortlandLive;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by konstantin on 8/8/15.
 *
 *
 * Class description:
 *  This class is created to hold functionality common for all activities that use Google Maps
 *
 *
 */
public class MapsCommon extends FragmentActivity {

    protected GoogleMap mMap; // Might be null if Google Play services APK is not available.

    protected void goToDefaultLocation() {

        LatLng fab = new LatLng(45.509867, -122.680594); // Defaults to FAB (fourth avenue building location)

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(fab)                // Sets the center of the map to location user
                .zoom(18)                   // Sets the zoom
                .bearing(0)                 // Sets the orientation of the camera to east
                .tilt(0)                    // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }


    protected void centerMapOnMyLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(18)                   // Sets the zoom
                    .bearing(0)                 // Sets the orientation of the camera to east
                    .tilt(0)                    // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        }
    }
}
