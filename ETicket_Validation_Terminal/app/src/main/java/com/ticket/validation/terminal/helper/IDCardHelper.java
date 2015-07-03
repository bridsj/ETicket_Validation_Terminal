package com.ticket.validation.terminal.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.ticket.validation.terminal.util.DeviceTypeUtil;

/**
 * Created by dengshengjin on 15/5/31.
 */
public class IDCardHelper {
    private static IDCardHelper mIDCardHelper;
    private IDCardStrategy mIDCardStrategy;
    private Context mContext;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private IDCardHelper(Context context) {
        mContext = context;
        if (DeviceTypeUtil.isGuZhiLian()) {
//            mIDCardStrategy = new IDCardZKCStrategy(context);
        } else if (DeviceTypeUtil.isHaoDeXin()) {
            mIDCardStrategy = new IDCardHaoDeXinStrategy(context);
        }
    }

    public static IDCardHelper getInstance(Context context) {
        if (mIDCardHelper == null) {
            mIDCardHelper = new IDCardHelper(context);
        }
        return mIDCardHelper;
    }

    public void startRecognition(final IDCardStrategy.RecognitionCallback callback) {
        if (mIDCardStrategy != null) {
            mIDCardStrategy.startRecognition(callback);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onFailIDCard();
                    }
                    return;
                }
            });
        }
    }
}
