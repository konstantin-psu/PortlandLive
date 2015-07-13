package edu.pdx.konstan2.trimetlive;

/**
 * Created by kmacarenco on 7/7/15.
 *
 * Takes trimet respond as input, and parses it to JSONObject.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class jsonParser {
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
    public void parseStops(String response, HashMap<Long, Stop> sMap) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jobj = (JSONObject) parser.parse(response.toString());
            JSONObject v = (JSONObject) jobj.get("resultSet");
            JSONArray arr = (JSONArray) v.get("location");
            Iterator<JSONObject> iter = arr.iterator();
            while(iter.hasNext()) {
                Stop t = new Stop(iter.next());
                sMap.put(t.locID, t);
            }
        } catch (Exception e) {
        }
    }
}
