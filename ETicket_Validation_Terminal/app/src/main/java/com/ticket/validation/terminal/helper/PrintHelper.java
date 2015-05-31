package com.ticket.validation.terminal.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.ticket.validation.terminal.util.DeviceTypeUtil;

/**
 * Created by dengshengjin on 15/5/26.
 */
public class PrintHelper {
    private static PrintHelper mPrintHelper;
    private PrintStrategy mPrintStrategy;
    private Context mContext;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private PrintHelper(Context context) {
        mContext = context;
        if (DeviceTypeUtil.isHaoDeXin()) {
            mPrintStrategy = new PrintHaoDeXinStrategy(context);
        } else if (DeviceTypeUtil.isWizarpos()) {
//            mPrintStrategy = new PrintWizarposStrategy(context);
        }else if(DeviceTypeUtil.isGuZhiLian()){
            mPrintStrategy=new PrintZkcPc700Strategy(context);
        }
    }

    public static PrintHelper getInstance(Context context) {
        if (mPrintHelper == null) {
            mPrintHelper = new PrintHelper(context);
        }
        return mPrintHelper;
    }

    public void startPrintViaChar(final String printStr, final PrintStrategy.PrintCallback printCallback) {
        if (mPrintStrategy != null) {
            mPrintStrategy.startPrintViaChar(printStr, printCallback);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (printCallback != null) {
                        printCallback.onFinishPrint();
                    }
                    return;
                }
            });
        }
    }


}
