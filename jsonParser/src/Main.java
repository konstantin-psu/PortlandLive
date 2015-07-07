import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;


public class Main {

    public static HashMap<Integer, vehicle> vs = new HashMap<Integer, vehicle>();
    public static void main(String[] args) {
        try {
            URL url= new URL("http://developer.trimet.org/ws/v2/vehicles?appID=EEC7240AC3168C424AC5A98E1");
            URL yahoo = url;
            String res = new String();


            HttpURLConnection yc = (HttpURLConnection) yahoo.openConnection();

//                yc.setRequestProperty("appID", appId);

//                return Integer.toString(yc.getResponseCode());
//                return yc.toString();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(yc.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONParser parser = new JSONParser();
            JSONObject jobj = (JSONObject) parser.parse(response.toString());
            JSONObject v = (JSONObject) jobj.get("resultSet");
            JSONArray arr = (JSONArray) v.get("vehicle");
//            System.out.println(v.toString());
            Iterator<JSONObject> iter = arr.iterator();
            while(iter.hasNext()) {
                vehicle t = new vehicle(iter.next());
                vs.put(t.vehicleID, t);
                System.out.println(iter.next().toString());

            }
        } catch (Exception e) {
            System.out.println(e.toString()) ;
        }
    }
}

class vehicle {
    public int vehicleID             ;//  Identifies the vehicle.
    public String type                  ;//  Identifies the type of vehile. Can be "bus" or "rail".
    public int blockID               ;//  Identifies the block number of the vehicle.
    public int bearing               ;//  Bearing of the vehicle if available. 0 is north, 180 is south.
    public int serviceDate           ;//  Midnight of the service day the vehicle is performing service for.
    public int locationInScheduleDay ;//  Number of seconds since midnight from the scheduleDate that the vehicle is positioned at along its schedule.
    public int time                  ;//  Time this position was initially recorded.
    public int expires               ;//  Time this vehicle's entry should be discarded if no new position information is received from the vehicle.
    public int longitude             ;//  Longitude of the vehicle.
    public int latitude              ;//  Latitude of the vehicle.
    public int routeNumber           ;//  Route number the vehicle is servicing.
    public String direction             ;//  Direction of the route the vehicle is servicing.
    public String tripID                ;//  TripID the vehicle is servicing.
    public Boolean newTrip               ;//  Will be true when the trip the vehicle is serving is new and was not part of the published schedule. When true the value vehicle[@                                                                           tripID                                                                                  ] will not be found in GTFS.
    public int delay                 ;//  Delay of the vehilce along its schedule. Negative is late. Positive is early.
    public int messageCode           ;//  Identifier for the over head sign message.
    public String signMessage           ;//  Vehicle's over head sign text message.
    public String signMessageLong       ;//  Vehicle's full over head sign text message.
    public int nextLocID             ;//  Location ID (or stopID) of the next stop this vehicle is scheduled to serve.
    public int nextStopSeq           ;//  Stop sequence for the next stop this vehicle is scheduled to serve. Some trips serve the same stop twice.
    public int lastLocID             ;//  Location ID (or stopID) of the previous stop this vehicle was scheduled to serve.
    public int lastStopSeq           ;//  Stop sequence for the next stop this vehicle is scheduled to serve. Some trips serve the same stop twice.
    public String garage                ;//  Identifies the garage the vehicle originates from.
    public int extrablockID          ;//  Integer value identifies a new unscheduled block the vehicle is servicing. This will not be in the published GTFS schedule.
    public Boolean offRoute              ;//  Placeholder for future. Set to true if the vehicle reported that it has gone off route.
    public Boolean inCongestion          ;//  (Experimental)   Set to true if vehicle is reporting its not moving while in traffic. Bus only.
    public int loadPercentage        ;//  (Experimental)     Vehicles (bus only) can report when load thresholds have been crossed. Currently there are three possible thresholds, 0%, 70% and 90%. These thresholds may change so values 0 to 100 may be possible.

    public vehicle (JSONObject v) {
        vehicleID             = (Integer) v.get("vehicleID");
        type                  = (String) v.get("type");
        blockID               = (Integer) v.get("blockID");
        bearing               = (Integer) v.get("bearing");
        serviceDate           = (Integer) v.get("serviceDate");
        locationInScheduleDay = (Integer) v.get("locationInScheduleDay");
        time                  = (Integer) v.get("time");
        expires               = (Integer) v.get("expires");
        longitude             = (Integer) v.get("longitude");
        latitude              = (Integer) v.get("latitude");
        routeNumber           = (Integer) v.get("routeNumber");
        direction             = (String) v.get("direction");
        tripID                = (String) v.get("tripID");
        newTrip               = (Boolean) v.get("newTrip");
        delay                 = (Integer) v.get("delay");
        messageCode           = (Integer) v.get("messageCode");
        signMessage           = (String) v.get("signMessage");
        signMessageLong       = (String) v.get("signMessageLong");
        nextLocID             = (Integer) v.get("nextLocID");
        nextStopSeq           = (Integer) v.get("nextStopSeq");
        lastLocID             = (Integer) v.get("lastLocID");
        lastStopSeq           = (Integer) v.get("lastStopSeq");
        garage                = (String) v.get("garage");
        extrablockID          = (Integer) v.get("extrablockID");
        offRoute              = (Boolean) v.get("offRoute");
        inCongestion          = (Boolean) v.get("inCongestion");
        loadPercentage        = (Integer) v.get("loadPercentage");
    }

    public String toString() {
        return
                "vehicleID            " + vehicleID
                + "type                 " + type
                + "blockID              " + blockID
                + "bearing              " + bearing
                + "serviceDate          " + serviceDate
                + "locationInScheduleDay" + locationInScheduleDay
                + "time                 " + time
                + "expires              " + expires
                + "longitude            " + longitude
                + "latitude             " + latitude
                + "routeNumber          " + routeNumber
                + "direction            " + direction
                + "tripID               " + tripID
                + "newTrip              " + newTrip
                + "delay                " + delay
                + "messageCode          " + messageCode
                + "signMessage          " + signMessage
                + "signMessageLong      " + signMessageLong
                + "nextLocID            " + nextLocID
                + "nextStopSeq          " + nextStopSeq
                + "lastLocID            " + lastLocID
                + "lastStopSeq          " + lastStopSeq
                + "garage               " + garage
                + "extrablockID         " + extrablockID
                + "offRoute             " + offRoute
                + "inCongestion         " + inCongestion
                + "loadPercentage       " + loadPercentage;
    }

}
