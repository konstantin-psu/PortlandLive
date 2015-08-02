package edu.pdx.konstan2.trimetlive;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by konstantin on 8/1/15.
 */
public class RoutesFactory implements AsyncJob {
    String url;
    String response;
    HashMap<Long, Route> routesMap = new HashMap<Long, Route>();
    ArrayList<Route> routes =  new ArrayList<>();
    RoutesBuilder routesRequest = new RoutesBuilder();
    MasterTask master;
    public static final String command = "addRoutes";

    public RoutesFactory(MasterTask master) {
        this.master = master;
    }

    public String url() {
        return  url;
    }

    public void setResponse(String resp) {
        response = resp;
    }
    public void execute() {
        new responseParserFactory().parseRoutesXML(response, routesMap);
        Map<Long, Route> treeMap = new TreeMap<>(routesMap);
        Iterator it =  treeMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Route r =(Route) pair.getValue();
            routes.add(r);
        }
        master.run(command);

    }

    public void getRoutes(String [] routes) {
        url = routesRequest.request(routes);
    }
    public void getAllRoutes() {
        url = routesRequest.request();
    }

    public String[] stringArray() {
        ArrayList<String> returnBuilder = new ArrayList<String>();
        Iterator it =  routes.iterator();
        while(it.hasNext()) {
            Route r =(Route) it.next();
            returnBuilder.add(r.description);
        }
        return returnBuilder.toArray(new String[returnBuilder.size()]);
    }
}
