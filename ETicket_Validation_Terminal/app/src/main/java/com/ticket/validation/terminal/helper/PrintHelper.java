package com.ticket.validation.terminal.helper;

import android.content.Context;

import com.ticket.validation.terminal.constant.Constants;

/**
 * Created by dengshengjin on 15/5/26.
 */
public class PrintHelper {
    private static PrintHelper mPrintHelper;
    private PrintStrategy mPrintStrategy;

    private PrintHelper(Context context) {
        if (Constants.type.equals(Constants.HAO_DE_XIN)) {
            mPrintStrategy = new PrintHaoDeXinStrategy(context);
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
        }
    }


}
