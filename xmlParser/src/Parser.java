import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kmacarenco on 7/14/15.
 */
public class Parser {

    public static String u = "https://developer.trimet.org/ws/V1/routeConfig/route/70/dir/true/stops/true/appid/EEC7240AC3168C424AC5A98E1";


    public static void main(String argv[]) {

        try {
            File inputFile = new File("input.txt");
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            InputSource is = new InputSource(new StringReader(request(u)));

            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            System.out.print("Root element: ");
            System.out.println(doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("route");
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :");
                System.out.println(nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.print("route desc: ");
                    System.out.println(eElement.getAttribute("desc"));
                    NodeList dirList = eElement.getElementsByTagName("dir");
                    System.out.println(eElement.getAttribute("dir"));
                    for (int count = 0; count < dirList.getLength(); count++) {
                        Node node1 = dirList.item(count);
                        if (node1.getNodeType() == node1.ELEMENT_NODE) {
                            Element dir = (Element) node1;
                            System.out.print("dir id : ");
                            System.out.println(dir.getAttribute("dir"));
                            System.out.print("dir desc: ");
                            System.out.println(dir.getAttribute("desc"));

                            NodeList stopList = eElement.getElementsByTagName("stop");
                            for (int count1 = 0; count1 < stopList.getLength(); count1++) {
                                Node node2 = stopList.item(count1);
                                if (node2.getNodeType() == node2.ELEMENT_NODE) {
                                    Element stop = (Element) node2;
                                    System.out.println(stop.getTextContent());
                                    System.out.print("stop id : ");
                                    System.out.println(stop.getAttribute("locid"));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String request(String urlString) {
        try {
            URL url= new URL(urlString);
            URL yahoo = url;


            HttpURLConnection yc = (HttpURLConnection) yahoo.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(yc.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }

    }
}
