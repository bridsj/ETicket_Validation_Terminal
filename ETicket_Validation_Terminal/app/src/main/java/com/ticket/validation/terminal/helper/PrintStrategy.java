package com.ticket.validation.terminal.helper;

/**
 * Created by dengshengjin on 15/5/27.
 */
//策略
public interface PrintStrategy {
    void startPrintViaChar(final String printStr, final PrintCallback printCallback);

    interface PrintCallback {

        void onErrorPrint();

        void onStartPrint();

        void onFinishPrint();

        void onFailPrint();
    }
}
