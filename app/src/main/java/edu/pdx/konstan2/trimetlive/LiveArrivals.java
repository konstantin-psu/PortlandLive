package edu.pdx.konstan2.trimetlive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class LiveArrivals extends FragmentActivity implements OnMapReadyCallback, MasterTask {
    GoogleMap mMap;
    String locId;
    ArrivalsFactory arrivalsFactory;
    VehiclesLocationFactory vehiclesLocationFactory;

    final ArrayList itemsSelected;

    private HashMap<String, String> routesReslver;
    private HtmlRequestor req;


    public LiveArrivals() {
        itemsSelected = new ArrayList();

    }

    public void addArrivals(Map<String, Arrival> arr) {
        ArrivalsViewBuilder arrivalsViewBuilder = new ArrivalsViewBuilder();
        arrivalsViewBuilder.buildView(arrivalsFactory, this);

    }
    public void run(String methodName) {
        if (methodName.equals(ArrivalsFactory.command)) {
            addArrivals(arrivalsFactory.arrivalsmap);
        } else if (methodName.equals(VehiclesLocationFactory.command)) {
            addVehiclesLocations();
        }
    }

    private void addVehiclesLocations() {
        Iterator it = vehiclesLocationFactory.vehiclesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Vehicle v = (Vehicle) pair.getValue();
            mMap.addMarker(new MarkerOptions().position(new LatLng(v.latitude, v.longitude)));
        }
    }

    public void afficher()
    {
        arrivalsFactory = new ArrivalsFactory(this);
        arrivalsFactory.getArrivalsAt(locId.split(" "));
//        handler.postDelayed(runnable, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_arrivals);
        MapFragment mapFragment = (MapFragment) getFragmentManager() .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        routesReslver = new HashMap<>();

        Stop myStop = new Stop(intent.getStringExtra("stopJsonEncoded"));

        for (Route r: myStop.routes) {
            routesReslver.put(r.description, r.route.toString());
        }
        locId = myStop.locID.toString();
        req = new HtmlRequestor();
        arrivalsFactory = new ArrivalsFactory(this);
        vehiclesLocationFactory = new VehiclesLocationFactory(this);

        arrivalsFactory.getArrivalsAt(locId.split(" "));
        vehiclesLocationFactory.getVehiclesOnRoutes(myStop.routesIDs());
        req.execute(arrivalsFactory, vehiclesLocationFactory);
        createMulticheckboxDialog(myStop);
    }

    private void createMulticheckboxDialog (Stop thisStop) {
        final Dialog dialog;

        String [] items = thisStop.listRoutes();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("List arrivals for routes: ");
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            itemsSelected.add(selectedItemId);
                        } else if (itemsSelected.contains(selectedItemId)) {
                            itemsSelected.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                })
                .setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                    }
                });
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                    }
//                });
        dialog = builder.create();
//        dialog.show();
        Button btn = (Button) findViewById(R.id.button_dialog);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callTest(View view) {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.activity_test, null);
//
//        for (int i = 0; i < 1; i++) {
//            View custom = inflater.inflate(R.layout.insertable, null);
//            TextView tv = (TextView) custom.findViewById(R.id.text);
//            tv.setText("Custom View " + i);
//            parent.addView(custom);
//        }
//        setContentView(parent);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true);
        mMap = map;
        centerMapOnMyLocation();
        mMap.setInfoWindowAdapter(
                new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker arg0) {
                        LatLng stopPosition = arg0.getPosition();
                        Vehicle vehicle = vehiclesLocationFactory.vehiclesMap.get(stopPosition);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                        VehicleInfoWindowBuilder vehicleInfoWindowBuilder =new VehicleInfoWindowBuilder();
                        return  vehicleInfoWindowBuilder.buildView(vehicle, inflater);
                    }
                }
        );

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isVisible() && !marker.isInfoWindowShown()) {
                    marker.showInfoWindow();
                }
                return true;
            }
        });
    }
    private void centerMapOnMyLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
//        location = mMap.getMyLocation();
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13)); CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                .zoom(10)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }
}
