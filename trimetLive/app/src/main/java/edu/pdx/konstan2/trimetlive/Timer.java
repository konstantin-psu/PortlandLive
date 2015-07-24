package edu.pdx.konstan2.trimetlive;

import android.os.Handler;

/**
 * Created by konstantin on 7/23/15.
 */
public class Timer {
    Handler executer = new Handler();
}

class timerTask implements Runnable {
    Handler myHandler;
    public timerTask(Handler executer) {
        myHandler = executer;
    }
    public void run() {
       myHandler.postDelayed(this, 100);
    }
}
