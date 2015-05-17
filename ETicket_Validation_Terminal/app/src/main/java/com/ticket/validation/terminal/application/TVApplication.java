package com.ticket.validation.terminal.application;

import android.app.Application;

import com.ticket.validation.terminal.util.TypefaceUtil;

/**
 * Created by dengshengjin on 15/5/15.
 */
public class TVApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.init(getApplicationContext());
    }
}
