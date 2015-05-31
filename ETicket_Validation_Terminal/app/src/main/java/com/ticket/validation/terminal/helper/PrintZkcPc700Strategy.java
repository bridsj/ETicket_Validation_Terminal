package com.ticket.validation.terminal.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;

import com.zkc.helper.printer.PrintService;
import com.zkc.helper.printer.PrinterClass;
import com.zkc.pc700.helper.PrinterClassSerialPort;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dengshengjin on 15/5/27.
 */
public class PrintZkcPc700Strategy implements PrintStrategy {


    private Context mContext;
    private ExecutorService mExecutorService;
    private boolean mIsPrinting;
    private PowerManager.WakeLock mWakeLock;
    private PrinterClassSerialPort mPrinterClassSerialPort;
    private Handler mHandler;

    public PrintZkcPc700Strategy(Context context) {
        super();
        mContext = context;
        mExecutorService = Executors.newSingleThreadExecutor();
        mPrinterClassSerialPort = new PrinterClassSerialPort(callbackHandler);

        mWakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wake_lock");
        mHandler = new Handler(Looper.getMainLooper());
        mIsPrinting = false;
    }

    @Override
    public void startPrintViaChar(final String printStr, final PrintCallback printCallback) {
        if (mIsPrinting) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (printCallback != null) {
                        printCallback.onErrorPrint();
                    }
                }
            });
            return;
        }
        mIsPrinting = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (printCallback != null) {
                    printCallback.onStartPrint();
                }
            }
        });

        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mWakeLock.acquire();
                    mPrinterClassSerialPort.open(mContext);
                    //开始打印
                    startPrintAsync(printCallback, printStr);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (printCallback != null) {
                                printCallback.onFinishPrint();
                            }
                        }
                    });
                } catch (Throwable t) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (printCallback != null) {
                                printCallback.onFailPrint();
                            }
                        }
                    });

                } finally {
                    try {
                        mWakeLock.release();
                        mPrinterClassSerialPort.close(mContext);
                    } catch (Throwable t) {

                    }
                    mIsPrinting = false;
                }

            }
        });
    }

    private void startPrintAsync(final PrintCallback printCallback, final String printStr) throws Throwable {
        SystemClock.sleep(500);
        mPrinterClassSerialPort.printText(printStr);
        mPrinterClassSerialPort.printText("\n\n\n\n");
        SystemClock.sleep(500);
    }

    private Handler callbackHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PrinterClass.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    if (readBuf[0] == 0x13) {
                        PrintService.isFUll = true;
                    } else if (readBuf[0] == 0x11) {
                        PrintService.isFUll = false;
                    } else {
                        String readMessage = new String(readBuf, 0, msg.arg1);
                        if (readMessage.contains("800"))// 80mm paper
                        {
                            PrintService.imageWidth = 72;
                        } else if (readMessage.contains("580"))// 58mm paper
                        {
                            PrintService.imageWidth = 48;
                        } else {

                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
