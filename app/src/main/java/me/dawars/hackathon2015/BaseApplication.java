package me.dawars.hackathon2015;

import android.app.Application;

/**
 * Created by dawars on 2015.10.03..
 */
public class BaseApplication extends Application {

    public BlueComms myBlueComms;

    @Override
    public void onCreate() {
        super.onCreate();

        myBlueComms = new BlueComms();
    }
}
