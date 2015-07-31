package edu.pdx.konstan2.trimetlive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class testActivity extends FragmentActivity implements OnMapReadyCallback, MasterTask {
    GoogleMap mMap;
    Handler handler = new Handler();
    String locId;
    String route;
    ArrivalsFactory arrivalsFactory;
    VehiclesLocationFactory vehiclesLocationFactory;
    ArrayList<String> routes;

    final ArrayList itemsSelected;

    private CheckBox checkBox;
    private TextView txtCheckBox, txtRadio;
    private RadioButton rb1, rb2, rb3;
    private Spinner spnMusketeers;
    private HashMap<String, String> routesReslver;
    private htmlRequestor req;

    Runnable runnable = new Runnable() {
        public void run() {
            afficher();
        }
    };

    public testActivity() {
        itemsSelected = new ArrayList();

    }

    public void mapToString(Map<String, Arrival> arr) {
        String result = new String();
        Iterator it = arr.entrySet().iterator();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout displayPlace = (LinearLayout) findViewById(R.id.arrivals_insert_point);
        View custom = inflater.inflate(R.layout.insertable, null);
        LinearLayout insertPoint = (LinearLayout) custom.findViewById(R.id.insert_point);
        routes = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
//            TextView tv = (TextView) custom.findViewById(R.id.text);
            TextView tv = new TextView(this);
            Arrival arrival = (Arrival) pair.getValue();
            tv.setText(((Arrival) pair.getValue()).asString());
            insertPoint.addView(tv);
//            if (itemsSelected.contains(arrival.route)) {
//                mMap.
//            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        displayPlace.addView(custom);
    }
    public void run(String methodName) {
        if (methodName.equals(ArrivalsFactory.command)) {
            mapToString(arrivalsFactory.arrivalsmap);
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
        setContentView(R.layout.activity_test);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        routesReslver = new HashMap<>();
        String message = intent.getStringExtra("stopId");
        String [] routes = intent.getStringExtra("routes").split("[#]");
        String [] routeIDs = intent.getStringExtra("routesID").split("[+]");
        String [] routeIdsOnly = intent.getStringExtra("routesIdsOnly").split("[#]");
        for (String s: routes) {
            String[] j = s.split("[+]");
            routesReslver.put(j[1], j[0]);
        }
        locId = message;
        req = new htmlRequestor();
        arrivalsFactory = new ArrivalsFactory(this);
        vehiclesLocationFactory = new VehiclesLocationFactory(this);

        arrivalsFactory.getArrivalsAt(locId.split(" "));
        vehiclesLocationFactory.getVehiclesOnRoutes(routeIdsOnly);
        req.execute(arrivalsFactory, vehiclesLocationFactory);
        createMulticheckboxDialog(routeIDs);
//        checkBox = (CheckBox) findViewById(R.id.cbxBox1);
//        txtCheckBox = (TextView) findViewById(R.id.txtCheckBox);
//        txtRadio = (TextView) findViewById(R.id.txtRadio);
//        rb1 = (RadioButton) findViewById(R.id.RB1);
//        rb2 = (RadioButton) findViewById(R.id.RB2);
//        rb3 = (RadioButton) findViewById(R.id.RB3);
//        spnMusketeers = (Spinner) findViewById(R.id.spnMusketeers);
//        // React to events from the CheckBox
//        checkBox.setOnClickListener(new CheckBox.OnClickListener() {
//            public void onClick(View v){
//                if (checkBox.isChecked()) {
//                    txtCheckBox.setText("CheckBox: Box is checked");
//                }
//                else
//                {
//                    txtCheckBox.setText("CheckBox: Not checked");
//                }
//            }
//        });
//        // React to events from the RadioGroup
//        rb1.setOnClickListener(new RadioGroup.OnClickListener() {
//            public void onClick(View v){
//                txtRadio.setText("Radio: Button 1 picked");
//            }
//        });
//        rb2.setOnClickListener(new RadioGroup.OnClickListener() {
//            public void onClick(View v){
//                txtRadio.setText("Radio: Button 2 picked");
//            }
//        });
//        rb3.setOnClickListener(new RadioGroup.OnClickListener() {
//            public void onClick(View v){
//                txtRadio.setText("Radio: Button 3 picked");
//            }
//        });
//        // Set up the Spinner entries
//        List<String> lsMusketeers = new ArrayList<String>();
//        lsMusketeers.add("Athos");
//        lsMusketeers.add("Porthos");
//        lsMusketeers.add("Aramis");
//        ArrayAdapter<String> aspnMusketeers =
//                new ArrayAdapter<String>(this, android.R.layout.select_dialog_multichoice,
//                        lsMusketeers);
//        aspnMusketeers.setDropDownViewResource
//                (android.R.layout.simple_spinner_dropdown_item);
//        spnMusketeers.setAdapter(aspnMusketeers);
//        // Set up a callback for the spinner
//        spnMusketeers.setOnItemSelectedListener(
//                new AdapterView.OnItemSelectedListener() {
//                    public void onNothingSelected(AdapterView<?> arg0) {
//                    }
//
//                    public void onItemSelected(AdapterView<?> parent, View v,
//                                               int position, long id) {
//                        // Code that does something when the Spinner value changes
//                    }
//                });
//        runnable.run();
    }

    private void createMulticheckboxDialog (String [] items) {
        final Dialog dialog;
        final ArrayList savedState = new ArrayList();

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
        dialog.show();
        Button btn = (Button) findViewById(R.id.button_dialog);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }
//                final ArrayList<String> selColors = new ArrayList<String>();
//                try {
//                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(getApplicationContext());
//                    final String[] Colors = {"Red", "Green", "Blue", "Orange", "Pink"};
//                    final boolean[] _selections = {true, true, true, true, true, true, true};
//                    dialog1.setTitle("Colors List");
//                    dialog1.setMessage("Select Your Favoriate Color");
//                    dialog1.setMultiChoiceItems(Colors, _selections, new DialogInterface.OnMultiChoiceClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                            if (isChecked) {
//                                selColors.add(Colors[which]);
//                                Toast.makeText(getApplicationContext(), "You have selected " + Colors[which], Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                    dialog1.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                            Toast.makeText(getApplicationContext(), "SAVED", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    dialog1.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                            Toast.makeText(getApplicationContext(), "CANCELED", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    dialog1.show();
//                } catch (Exception ex) {
//                    TextView tv = (TextView) findViewById(R.id.textView1);
//                    tv.setText(ex.toString());
//                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
//                }
//            }

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
                .zoom(18)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }
}
