package edu.pdx.konstan2.trimetlive;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


public class ArrivalsActivity extends ActionBarActivity implements AsyncJob {

    String url;
    String response;
    TextView tw;
    public String url() {
        return  url;
    }

    public void setResponse(String resp) {
        response = resp;
    }
    public void execute() {
        tw.setText(response);
        hideSoftKeyboard(ArrivalsActivity.this);

    }
    public void getArrivalsForId(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(10);
        textView.setText(message);
        ArrivalsBuilder arrivals = new ArrivalsBuilder();

        TextView tw = (TextView) findViewById(R.id.displayArrivalsView);
        tw.setTextSize(20);
        url = arrivals.request(message.split(","));

        responseParserFactory parser = new responseParserFactory();
        htmlRequestor req = new htmlRequestor();
        req.execute(this);

//        tw.setText(url);


//        setContentView(textView);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrivals);
        tw = (TextView) findViewById(R.id.displayArrivalsView);
        tw.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_arrivals, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
