import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;


public class Main {

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
                System.out.println(iter.next().toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString()) ;
        }
    }
}
