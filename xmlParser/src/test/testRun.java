package test;

/**
 * Created by kmacarenco on 7/14/15.
 */
public class testRun {
    public static void main(String argv[]) {
        ArrivalsBuilder t = new ArrivalsBuilder();
        String [] test = new String [2];
        test[0] = "8381";
        test[1] = "8382";
        System.out.println(t.request(test));

        RoutesBuilder r = new RoutesBuilder();
        System.out.println(r.request());
    }
}
