package com.ticket.validation.terminal.db;

import android.content.Context;

import com.zuiapps.suite.utils.db.SharePrefHelper;

/**
 * Created by dengshengjin on 15/5/16.
 */
public class CacheDBUtil {
    public static final String APP_SESSION_ID = "APP_SESSION_ID";
    public static final String APP_USER_NAME = "APP_USER_NAME";
    public static final String APP_URL = "APP_URL";
    public static final String APP_NAME = "APP_NAME";

    public static void saveSessionId(Context mContext, String jsonStr) {
        SharePrefHelper.getInstance(mContext).setPref(APP_SESSION_ID, jsonStr);
    }

    public static String getSessionId(Context mContext) {
        return SharePrefHelper.getInstance(mContext).getPref(APP_SESSION_ID, "");
    }

    public static void saveUserName(Context mContext, String jsonStr) {
        SharePrefHelper.getInstance(mContext).setPref(APP_USER_NAME, jsonStr);
    }

    public static String getUserName(Context mContext) {
        return SharePrefHelper.getInstance(mContext).getPref(APP_USER_NAME, "");
    }

    public static void saveAppUrl(Context mContext, String jsonStr) {
        SharePrefHelper.getInstance(mContext).setPref(APP_URL, jsonStr);
    }

    public static String getAppUrl(Context mContext) {
        return SharePrefHelper.getInstance(mContext).getPref(APP_URL, "");
    }

    public static void saveName(Context mContext, String jsonStr) {
        SharePrefHelper.getInstance(mContext).setPref(APP_NAME, jsonStr);
    }

    public static String getName(Context mContext) {
        return SharePrefHelper.getInstance(mContext).getPref(APP_NAME, "");
    }
}
