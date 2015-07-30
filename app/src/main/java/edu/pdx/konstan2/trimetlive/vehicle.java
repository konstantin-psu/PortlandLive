package edu.pdx.konstan2.trimetlive;

/**
 * Created by konstantin on 7/5/15.
 */
import org.json.simple.*;

public class vehicle {
    public Long vehicleID             ;//  Identifies the vehicle.
    public String type                  ;//  Identifies the type of vehile. Can be "bus" or "rail".
    public Long blockID               ;//  Identifies the block number of the vehicle.
    public Long bearing               ;//  Bearing of the vehicle if available. 0 is north, 180 is south.
    public Long serviceDate           ;//  Midnight of the service day the vehicle is performing service for.
    public Long locationInScheduleDay ;//  Number of seconds since midnight from the scheduleDate that the vehicle is positioned at along its schedule.
    public Long time                  ;//  Time this position was initially recorded.
    public Long expires               ;//  Time this vehicle's entry should be discarded if no new position information is received from the vehicle.
    public Double longitude             ;//  Longitude of the vehicle.
    public Double latitude              ;//  Latitude of the vehicle.
    public Long routeNumber           ;//  Route number the vehicle is servicing.
    public Long direction             ;//  Direction of the route the vehicle is servicing.
    public String tripID                ;//  TripID the vehicle is servicing.
    public Boolean newTrip               ;//  Will be true when the trip the vehicle is serving is new and was not part of the published schedule. When true the value vehicle[@                                                                           tripID                                                                                  ] will not be found in GTFS.
    public Long delay                 ;//  Delay of the vehilce along its schedule. Negative is late. Positive is early.
    public Long messageCode           ;//  Identifier for the over head sign message.
    public String signMessage           ;//  Vehicle's over head sign text message.
    public String signMessageLong       ;//  Vehicle's full over head sign text message.
    public Long nextLocID             ;//  Location ID (or stopID) of the next stop this vehicle is scheduled to serve.
    public Long nextStopSeq           ;//  Stop sequence for the next stop this vehicle is scheduled to serve. Some trips serve the same stop twice.
    public Long lastLocID             ;//  Location ID (or stopID) of the previous stop this vehicle was scheduled to serve.
    public Long lastStopSeq           ;//  Stop sequence for the next stop this vehicle is scheduled to serve. Some trips serve the same stop twice.
    public String garage                ;//  Identifies the garage the vehicle originates from.
    public Long extrablockID          ;//  Integer value identifies a new unscheduled block the vehicle is servicing. This will not be in the published GTFS schedule.
    public Boolean offRoute              ;//  Placeholder for future. Set to true if the vehicle reported that it has gone off route.
    public Boolean inCongestion          ;//  (Experimental)   Set to true if vehicle is reporting its not moving while in traffic. Bus only.
    public Long loadPercentage        ;//  (Experimental)     Vehicles (bus only) can report when load thresholds have been crossed. Currently there are three possible thresholds, 0%, 70% and 90%. These thresholds may change so values 0 to 100 may be possible.

    public vehicle (JSONObject v) {
        vehicleID             = (Long) v.get("vehicleID");
        type                  = (String) v.get("type");
        blockID               = (Long) v.get("blockID");
        bearing               = (Long) v.get("bearing");
        serviceDate           = (Long) v.get("serviceDate");
        locationInScheduleDay = (Long) v.get("locationInScheduleDay");
        time                  = (Long) v.get("time");
        expires               = (Long) v.get("expires");
        longitude             = (Double) v.get("longitude");
        latitude              = (Double) v.get("latitude");
        routeNumber           = (Long) v.get("routeNumber");
        direction             = (Long) v.get("direction");
        tripID                = (String) v.get("tripID");
        newTrip               = (Boolean) v.get("newTrip");
        delay                 = (Long) v.get("delay");
        messageCode           = (Long) v.get("messageCode");
        signMessage           = (String) v.get("signMessage");
        signMessageLong       = (String) v.get("signMessageLong");
        nextLocID             = (Long) v.get("nextLocID");
        nextStopSeq           = (Long) v.get("nextStopSeq");
        lastLocID             = (Long) v.get("lastLocID");
        lastStopSeq           = (Long) v.get("lastStopSeq");
        garage                = (String) v.get("garage");
        extrablockID          = (Long) v.get("extrablockID");
        offRoute              = (Boolean) v.get("offRoute");
        inCongestion          = (Boolean) v.get("inCongestion");
        loadPercentage        = (Long) v.get("loadPercentage");
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

