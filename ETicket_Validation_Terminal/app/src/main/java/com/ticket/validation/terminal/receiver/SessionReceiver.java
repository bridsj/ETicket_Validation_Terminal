package com.ticket.validation.terminal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ticket.validation.terminal.db.CacheDBUtil;
import com.ticket.validation.terminal.helper.SessionHelper;
import com.ticket.validation.terminal.restful.ApiConstants;
import com.ticket.validation.terminal.restful.ReqRestAdapter;
import com.ticket.validation.terminal.restful.RestfulRequest;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dengshengjin on 15/5/24.
 */
public class SessionReceiver extends BroadcastReceiver {
    public static final String SESSION_RECEIVER = "com.ticket.validation.terminal.receiver.SessionReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {
        RestfulRequest mRestfulRequest = ReqRestAdapter.getInstance(context, ApiConstants.API_BASE_URL).create(RestfulRequest.class);
        String session = CacheDBUtil.getSessionId(context);
        mRestfulRequest.activate(session, new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response) {
                SessionHelper.getInstance(context).openNextSession();
            }

            @Override
            public void failure(RetrofitError error) {
                SessionHelper.getInstance(context).openNextSession();
            }
        });
    }
}
