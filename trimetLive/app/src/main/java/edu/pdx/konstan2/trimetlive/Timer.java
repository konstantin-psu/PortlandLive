package edu.pdx.konstan2.trimetlive;

import android.os.Handler;

/**
 * Created by konstantin on 7/23/15.
 */
public class Timer {
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            afficher();
        }
    };

    /** Called when the activity is first created. */

    public Timer() {
        runnable.run();
    }

    public void afficher()
    {
        handler.postDelayed(runnable, 1000);
    }
}
