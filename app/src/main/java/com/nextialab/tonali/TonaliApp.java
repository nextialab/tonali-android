package com.nextialab.tonali;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.nextialab.tonali.support.Persistence;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Nelson on 8/14/2016.
 */
public class TonaliApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        Persistence.instance().setContext(this);
    }

}
