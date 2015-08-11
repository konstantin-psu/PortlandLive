/*
 * Copyright (c) 2015. Konstantin Macarenco
 *
 * [This program is licensed under the GPL version 3 or later.]
 *
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 *
 */

package edu.pdx.konstan2.trimetlive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by konstantin on 8/1/15.
 *
 * Main class responsible for fetching storing and operating Routes information.
 * Implements AsyncJob, since required communication with the Trimet API
 *
 */
public class RoutesFactory implements AsyncJob {
    private String url;
    private String response;
    public HashMap<Long, Route> routesMap = new HashMap<>();
    private ArrayList<Route> routes =  new ArrayList<>();
    private RoutesUrlStringBuilder routesRequest = new RoutesUrlStringBuilder();
    private MasterTask master;
    public static final String COMMAND = "addRoutes";

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
        new responseParserFactory().parseRoutesXML(response, routesMap, true);
        Map<Long, Route> treeMap = new TreeMap<>(routesMap);
        Iterator it =  treeMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Route r =(Route) pair.getValue();
            routes.add(r);
        }
        master.run(COMMAND);

    }

    public void getRoutes(String [] routes, Boolean includeStops) {
        url = routesRequest.request(routes, includeStops);
    }

    public void getRoutes(String [] routes) {
        url = routesRequest.request(routes);
    }
    public void getAllRoutes() {
        url = routesRequest.request();
    }

    public void getAllRoutesWithStops() {
        url = routesRequest.request(false);
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

    public boolean containsRoute(Route cr) {
        return routesMap.containsKey(cr.route);
    }

    public Long getRouteIdById(int selectedItemId) {
        return routes.get(selectedItemId).route;
    }

    public int size() {
        return routes.size();
    }

    public Route get(int i) {
        return routes.get(i);
    }

    public Iterator iterator() {
        return routes.iterator();
    }
}
