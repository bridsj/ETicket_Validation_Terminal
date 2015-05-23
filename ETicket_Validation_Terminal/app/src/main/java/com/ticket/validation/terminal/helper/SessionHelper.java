package com.ticket.validation.terminal.helper;

import android.content.Context;

import com.ticket.validation.terminal.db.CacheDBUtil;
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

}
