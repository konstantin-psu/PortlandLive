package edu.pdx.konstan2.trimetlive;

/**
 * Created by kmacarenco on 7/21/15.
 */
public interface AsyncJob {
    public String url();
    public void setResponse(String response);
    public void execute();

}
