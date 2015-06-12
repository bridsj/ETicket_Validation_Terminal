package com.ticket.validation.terminal.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.ticket.validation.terminal.db.CacheDBUtil;
import com.ticket.validation.terminal.receiver.SessionReceiver;
import com.ticket.validation.terminal.restful.ApiConstants;
import com.ticket.validation.terminal.restful.ReqRestAdapter;
import com.ticket.validation.terminal.restful.RestfulRequest;

import retrofit.Callback;

/**
 * Created by dengshengjin on 15/5/23.
 */
public class SessionHelper {
    private static SessionHelper mSessionHelper;
    private RestfulRequest mRestfulRequest;
    private Context mContext;

    private SessionHelper(Context context) {
        mContext = context;
        mRestfulRequest = ReqRestAdapter.getInstance(context, ApiConstants.API_BASE_URL).create(RestfulRequest.class);
    }

    public static SessionHelper getInstance(Context context) {
        if (mSessionHelper == null) {
            mSessionHelper = new SessionHelper(context);
        }
        return mSessionHelper;
    }

    public void loginUser(Callback callback) {
        mRestfulRequest.login(ApiConstants.USER_NAME, ApiConstants.PASSWORD, callback);
    }

    public void updateConfigJson(Callback callback) {
        String session = CacheDBUtil.getSessionId(mContext);
        mRestfulRequest.updateConfigJson(session, callback);
    }


    public void sendSession() {
        Intent intent = new Intent(SessionReceiver.SESSION_RECEIVER);
        intent.setPackage(mContext.getPackageName());
        mContext.sendBroadcast(intent);
    }

    private final int ALARM = 5;

    public void openNextSession() {
        long now = SystemClock.elapsedRealtime();
        long next = now + (1000 * 60 * ALARM - now % 1000);//1分钟第一次更新
        Intent intent = new Intent(SessionReceiver.SESSION_RECEIVER);
        intent.setPackage(mContext.getPackageName());
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, next, PendingIntent.getBroadcast(mContext, 0, intent, 0));

    }

    public void closeSession() {
        Intent intent = new Intent(SessionReceiver.SESSION_RECEIVER);
        intent.setPackage(mContext.getPackageName());
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getBroadcast(mContext, 0, intent, 0));
    }

}
