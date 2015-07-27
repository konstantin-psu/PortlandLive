package edu.pdx.konstan2.trimetlive;

/**
 * Created by kmacarenco on 7/7/15.
 *
 * Takes trimet respond as input, and parses it to JSONObject.
 */

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class responseParserFactory {
    public void parse(String response, HashMap<Long, vehicle> vMap) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jobj = (JSONObject) parser.parse(response.toString());
            JSONObject v = (JSONObject) jobj.get("resultSet");
            JSONArray arr = (JSONArray) v.get("vehicle");
            Iterator<JSONObject> iter = arr.iterator();
            while(iter.hasNext()) {
                vehicle t = new vehicle(iter.next());
                vMap.put(t.vehicleID, t);
            }
        } catch (Exception e) {
        }
    }
    public void parseStops(String response, HashMap<LatLng, Stop> sMap) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jobj = (JSONObject) parser.parse(response.toString());
            JSONObject v = (JSONObject) jobj.get("resultSet");
            JSONArray arr = (JSONArray) v.get("location");
            Iterator<JSONObject> iter = arr.iterator();
            while(iter.hasNext()) {
                Stop t = new Stop(iter.next());
                sMap.put(new LatLng(t.latitude, t.longitude), t);
            }
        } catch (Exception e) {
        }
    }
    public void parseArrivals(String response, HashMap<String, Arrival> sMap) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jobj = (JSONObject) parser.parse(response.toString());
            JSONObject v = (JSONObject) jobj.get("resultSet");
            JSONArray arr = (JSONArray) v.get("arrival");
            Iterator<JSONObject> iter = arr.iterator();
            while(iter.hasNext()) {
                Arrival t = new Arrival(iter.next());
                sMap.put(t.id, t);
            }
        } catch (Exception e) {
            Log.d("exception", e.toString());
        }
    }
}
