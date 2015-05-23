package com.ticket.validation.terminal.parse;

import com.ticket.validation.terminal.model.ReportModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dengshengjin on 15/5/23.
 */
public class ReportParse {
    public static List<ReportModel> parseLogin(JSONObject jObj) {
        try {
            if (jObj == null) {
                return null;
            }
            int status = jObj.optInt("status");
            if (status == 1) {
                JSONArray arr = jObj.optJSONArray("data");
                if (arr != null) {
                    if (arr.length() > 0) {
                        List<ReportModel> list = new LinkedList<>();
                        for (int i = 0, len = arr.length(); i < len; i++) {
                            JSONObject obj = arr.optJSONObject(i);
                            Iterator<String> keys = obj.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                String value = obj.optString(key);
                                ReportModel model = new ReportModel();
                                model.title = key;
                                model.num = value;
                                list.add(model);
                            }

                        }
                        return list;
                    } else {
                        return new LinkedList<>();
                    }
                }
            } else {
            }
        } catch (Throwable t) {

        }
        return null;
    }

}
