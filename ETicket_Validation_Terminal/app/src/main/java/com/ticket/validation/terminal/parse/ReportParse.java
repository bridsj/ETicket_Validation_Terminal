package com.ticket.validation.terminal.parse;

import android.content.Context;

import com.ticket.validation.terminal.R;
import com.ticket.validation.terminal.model.ErrorModel;
import com.ticket.validation.terminal.model.ReportModel;
import com.ticket.validation.terminal.model.ReportPrintModel;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dengshengjin on 15/5/23.
 */
public class ReportParse {
    public static Object parse(Context mContext, JSONObject jObj) {
        try {
            if (jObj == null) {
                return null;
            }
            int status = jObj.optInt("status");
            if (status == 1) {
                JSONObject dataObj = jObj.optJSONObject("data");
                if (dataObj != null) {
                    ReportPrintModel reportPrintModel = new ReportPrintModel();
                    reportPrintModel.mPrintCount = dataObj.optInt("print_count");
                    String time = dataObj.optString("nowtime");
                    List<ReportModel> list = new LinkedList<>();
                    String printStr = "";
                    for (int i = 0; i < reportPrintModel.mPrintCount; i++) {
                        JSONObject contentObj = dataObj.optJSONObject("print_content" + (i + 1));
                        if (contentObj != null) {
                            Iterator<String> keys = contentObj.keys();
                            printStr += createPrintStr(mContext, i, reportPrintModel.mPrintCount, time, contentObj);
                            while (keys.hasNext()) {
                                String key = keys.next();
                                String value = contentObj.optString(key);
                                ReportModel model = new ReportModel();
                                model.title = key;
                                model.num = value;
                                list.add(model);
                            }
                        }
                    }

                    reportPrintModel.mReportList = list;
                    reportPrintModel.mPrintStr = printStr;
                    return reportPrintModel;
                }
            } else {
                ErrorModel errorModel = new ErrorModel();
                errorModel.mStatus = status;
                errorModel.mInfo = jObj.optString("info");
                return errorModel;
            }
        } catch (Throwable t) {

        }
        return null;
    }

    public static String createPrintStr(Context mContext, int page, int count, String time, JSONObject contentObj) {
        if (count < 0 || count >= 10) {//满足10页以内才打印
            return "";
        }
        String str = "";
        if (page > 0) {
            str += "\n\n\n\n";
        }
        String contentStr = "";
        Iterator<String> keys = contentObj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = contentObj.optString(key);
            contentStr = contentStr + (key + ":" + value + "\n");
        }

        str = str + String.format(mContext.getString(R.string.daily_print_str), (page + 1), count, time, contentStr);
        return str;
    }


}
