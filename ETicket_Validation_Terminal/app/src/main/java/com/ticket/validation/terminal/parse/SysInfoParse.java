package com.ticket.validation.terminal.parse;

import com.ticket.validation.terminal.model.SysInfoModel;

import org.json.JSONObject;

/**
 * Created by dengshengjin on 15/6/4.
 */
public class SysInfoParse {

    public static SysInfoModel parse(String jsonStr) {
        try {
            JSONObject jObj = new JSONObject(jsonStr);
            if (jObj == null) {
                return null;
            }
            int status = jObj.optInt("status");
            if (status == 1) {
                SysInfoModel model = new SysInfoModel();
                model.mInfo = jObj.optString("info");
                model.mData = jObj.optString("data");
                return model;
            }
        } catch (Throwable t) {

        }
        return null;
    }
}
