package com.ticket.validation.terminal.parse;

import com.ticket.validation.terminal.model.ErrorModel;
import com.ticket.validation.terminal.model.GoodsModel;

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
                    JSONObject listObj = dataObj.optJSONObject("goodslist");
                    if (listObj != null) {
                        JSONArray goodsArr = listObj.optJSONArray("goods");
                        if (goodsArr != null && goodsArr.length() > 0) {
                            List<GoodsModel> goodsList = new LinkedList<>();
                            int len = goodsArr.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject goodsObj = goodsArr.optJSONObject(i);
                                GoodsModel goodsModel = new GoodsModel();
                                goodsModel.mGoodsName = goodsObj.optString("goodsname");
                                goodsModel.mEndTime = goodsObj.optString("endtime");
                                goodsModel.mActTime = goodsObj.optString("acttime");
                                goodsModel.mStartTime = goodsObj.optString("starttime");
                                goodsModel.mPiececode = goodsObj.optString("piececode");
                                goodsModel.mInfo = goodsObj.optString("info");
                                goodsModel.mStorageStatus = goodsObj.optString("storagestatus");
                                goodsModel.mUserName = goodsObj.optString("username");
                                goodsModel.mIdCard = goodsObj.optString("idcard");
                                goodsModel.mOrderCommments = goodsObj.optString("ordercomments");
                                goodsModel.mSoldGoodsId = goodsObj.optString("soldgoodsid");
                                goodsModel.mExchageFunc = goodsObj.optString("exchangefunc");
                                goodsModel.mStatus = goodsObj.optInt("status");
                                JSONObject countObj = goodsObj.optJSONObject("count");
                                goodsModel.mCount = countObj.optInt("count1");
                                goodsList.add(goodsModel);
                            }
                            return goodsList;

                        }
                    }
                }
            }
        } catch (Throwable t) {

        }
        return null;
    }


}