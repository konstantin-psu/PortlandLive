package edu.pdx.konstan2.trimetlive;

/**
 * Created by kmacarenco on 7/7/15.
 *
 * Takes trimet respond as input, and parses it to JSONObject.
 */

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
    public void parseStopsXML(String response, HashMap<LatLng, Stop> sMap) {
        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            InputSource is = new InputSource(new StringReader(response));

            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("location");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    NodeList dirList = eElement.getElementsByTagName("route");
                    for (int count = 0; count < dirList.getLength(); count++) {
                        Node node1 = dirList.item(count);
                        if (node1.getNodeType() == node1.ELEMENT_NODE) {
                            Element dir = (Element) node1;
                            System.out.print("dir id : ");
                            System.out.println(dir.getAttribute("dir"));
                            System.out.print("dir desc: ");
                            System.out.println(dir.getAttribute("desc"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public void parseArrivalsXML(String response, HashMap<String, Arrival> sMap) {
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
