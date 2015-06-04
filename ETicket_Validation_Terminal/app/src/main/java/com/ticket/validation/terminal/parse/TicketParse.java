package com.ticket.validation.terminal.parse;

import com.ticket.validation.terminal.model.ErrorModel;
import com.ticket.validation.terminal.model.TicketModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dengshengjin on 15/5/24.
 */
public class TicketParse {
    public static Object parse(JSONObject jObj) {
        try {
            if (jObj == null) {
                return null;
            }
            int status = jObj.optInt("status");
            if (status == 0) {
                ErrorModel errorModel = new ErrorModel();
                errorModel.mStatus = status;
                errorModel.mInfo = jObj.optString("info");
                return errorModel;
            } else {
                JSONObject dataObj = jObj.optJSONObject("data");
                if (dataObj != null) {
                    JSONArray goodsArr = dataObj.optJSONArray("goodslist");
                    List<TicketModel> goodsList = new LinkedList<>();
                    if (goodsArr != null && goodsArr.length() > 0) {
                        int len = goodsArr.length();
                        for (int i = 0; i < len; i++) {
                            JSONObject goodsObj = goodsArr.optJSONObject(i);
                            TicketModel ticketModel = new TicketModel();
                            ticketModel.mGoodsName = goodsObj.optString("goodsname");
                            ticketModel.mExchangeId = goodsObj.optString("exchangoruserID");
                            ticketModel.mCreationDate = goodsObj.optString("CREATION_DATE");
                            ticketModel.mExchangeCount = goodsObj.optString("exchangecount");
                            goodsList.add(ticketModel);
                        }
                        return goodsList;

                    }
                }
            }
        } catch (Throwable t) {

        }
        return null;
    }


}
