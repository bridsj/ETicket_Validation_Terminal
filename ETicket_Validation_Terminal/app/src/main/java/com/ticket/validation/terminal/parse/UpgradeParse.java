package com.ticket.validation.terminal.parse;

import com.ticket.validation.terminal.model.ErrorModel;
import com.ticket.validation.terminal.model.UpgradeModel;

import org.json.JSONObject;

/**
 * Created by dengshengjin on 15/6/5.
 */
public class UpgradeParse {
    public static Object parse(JSONObject jObj) {
        try {
            if (jObj == null) {
                return null;
            }
            int status = jObj.optInt("status");
            if (status == 1) {
                UpgradeModel model = new UpgradeModel();
                model.mInfo = jObj.optString("info");
                model.mData = jObj.optString("data");
                return model;
            } else {
                ErrorModel errorModel = new ErrorModel();
                errorModel.mInfo = jObj.optString("info");
                return errorModel;
            }
        } catch (Throwable t) {

        }
        return null;
    }
}