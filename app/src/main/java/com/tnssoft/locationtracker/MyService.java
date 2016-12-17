package com.tnssoft.locationtracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.squareup.otto.Bus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    public static Handler handler = new Handler();
    public static final Bus BUS = new Bus();

    public static boolean isRunning = false;
    private Timer timer;






    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;

        BUS.register(this);
        timer = new Timer();       // location.
        UpdateWithNewLocation();
        Log.v("Service ", "ON start command is start");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }

        isRunning = false;

        BUS.unregister(this);
    }



    private void UpdateWithNewLocation() {



        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                System.out.println(dateFormat.format(cal.getTime()));
                final String getDate = dateFormat.format(cal.getTime());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        postevent(getDate);
                    }
                });

            }
        }, 0, 10000);
    }


    public void postevent(String s ){
        BUS.post(s);
    }


}