package com.ticket.validation.terminal.parse;

import com.ticket.validation.terminal.model.LoginModel;
import com.ticket.validation.terminal.model.UserModel;

import org.json.JSONObject;

/**
 * Created by dengshengjin on 15/5/23.
 */
public class UserParse {
    public static LoginModel parseLogin(JSONObject jObj) {
        try {
            if (jObj == null) {
                return null;
            }
            int status = jObj.optInt("status");
            if (status == 1) {
                LoginModel model = new LoginModel();
                model.mUser = jObj.optString("info");
                model.mSession = jObj.optString("data");
                return model;
            }
        } catch (Throwable t) {

        }
        return null;
    }

    public static boolean parseSession(JSONObject jObj) {
        try {
            if (jObj == null) {
                return false;
            }
            int status = jObj.optInt("status");
            if (status == 1) {
                return true;
            }
        } catch (Throwable t) {

        }
        return false;
    }

    public static UserModel parseUser(JSONObject jObj) {
        try {
            if (jObj == null) {
                return null;
            }
            int status = jObj.optInt("status");
            if (status == 1) {
                if (jObj.has("data")) {
                    UserModel model = new UserModel();
                    JSONObject dataObj = jObj.optJSONObject("data");
                    model.mStatus = 1;
                    model.mInfo = jObj.optString("info");
                    model.mUrl = dataObj.optString("url");
                    model.mUser = dataObj.optString("user");
                    model.mPassword = dataObj.optString("password");
                    return model;
                }
            }
        } catch (Throwable t) {

        }
        return null;
    }
}
