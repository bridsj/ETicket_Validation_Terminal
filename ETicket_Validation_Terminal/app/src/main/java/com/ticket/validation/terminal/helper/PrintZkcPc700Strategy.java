package com.ticket.validation.terminal.helper;

import android.content.Context;

import com.zkc.helper.PrinterClassSerialPort;

/**
 * Created by dengshengjin on 15/5/27.
 */
public class PrintZkcPc700Strategy implements PrintStrategy {
    private Context mContext;
    private PrinterClassSerialPort mPrinterClass;

    public PrintZkcPc700Strategy(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void startPrintViaChar(String printStr, PrintCallback printCallback) {

    }
}
