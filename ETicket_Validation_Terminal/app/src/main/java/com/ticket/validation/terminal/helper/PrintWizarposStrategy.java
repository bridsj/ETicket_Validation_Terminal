package com.ticket.validation.terminal.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;

import com.wizarpos.apidemo.jniinterface.PrinterInterface;
import com.wizarpos.printer.PrinterCommand;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dengshengjin on 15/5/30.
 */
public class PrintWizarposStrategy implements PrintStrategy {
    private ExecutorService mExecutorService;
    private Context mContext;
    private Handler mHandler;
    private boolean mIsPrinting;
    private PowerManager.WakeLock mWakeLock;

    public PrintWizarposStrategy(Context context) {
        super();
        mContext = context;
        mExecutorService = Executors.newSingleThreadExecutor();
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
                    if (PrinterInterface.PrinterOpen() < 0) {
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
                    PrinterInterface.PrinterBegin();
                    startPrintAsync(printCallback, printStr);
                    PrinterInterface.PrinterEnd();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (printCallback != null) {
                                printCallback.onFinishPrint();
                            }
                        }
                    });
                } catch (Throwable t) {
                    if (printCallback != null) {
                        printCallback.onFailPrint();
                    }
                } finally {
                    mWakeLock.release();
                    PrinterInterface.PrinterClose();
                    mIsPrinting = false;
                }

            }
        });
    }

    private void startPrintAsync(final PrintCallback printCallback, String printStr) throws Throwable {
        PrinterInterface.PrinterWrite(
                PrinterCommand.getCmdEscDN(1),
                PrinterCommand.getCmdEscDN(1).length);
        SystemClock.sleep(150);
        byte[] contentbytes = printStr.getBytes("GB2312");
        PrinterInterface.PrinterWrite(contentbytes, contentbytes.length);
        SystemClock.sleep(150);
        PrinterInterface.PrinterWrite(
                PrinterCommand.getCmdEscDN(4),
                PrinterCommand.getCmdEscDN(4).length);
    }
}
