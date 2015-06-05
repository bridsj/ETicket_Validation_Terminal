package com.ticket.validation.terminal.helper;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;

import com.zkc.helper.printer.PrintService;
import com.zkc.helper.printer.PrinterClass;
import com.zkc.pc700.helper.PrinterClassSerialPort;

import java.io.IOException;
import java.io.RandomAccessFile;
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
                    mPrinterClassSerialPort = new PrinterClassSerialPort(callbackHandler);
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
                } catch (final Throwable t) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (printCallback != null) {
                                printCallback.onFailPrint("");
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
        SystemClock.sleep(200);
        mPrinterClassSerialPort.printText(printStr);
        mPrinterClassSerialPort.printText("\n\n\n\n\n\n\n");
        int rowNum = 0;
        try {
            rowNum = printStr.split("\n").length + 7;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (rowNum < 5) {
            rowNum = 5;
        }

        SystemClock.sleep(rowNum * 170);
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

    static public String getCpuType() {
        String strInfo = getCpuString();
        String strType = null;

        if (strInfo.contains("ARMv5")) {
            strType = "armv5";
        } else if (strInfo.contains("ARMv6")) {
            strType = "armv6";
        } else if (strInfo.contains("ARMv7")) {
            strType = "armv7";
        } else if (strInfo.contains("Intel")) {
            strType = "x86";
        } else {
            strType = "unknown";
            return strType;
        }

        if (strInfo.contains("neon")) {
            strType += "_neon";
        } else if (strInfo.contains("vfpv3")) {
            strType += "_vfpv3";
        } else if (strInfo.contains(" vfp")) {
            strType += "_vfp";
        } else {
            strType += "_none";
        }

        return strType;
    }

    static public String getCpuString() {
        if (Build.CPU_ABI.equalsIgnoreCase("x86")) {
            return "Intel";
        }

        String strInfo = "";
        RandomAccessFile reader = null;
        try {
            byte[] bs = new byte[1024];
            reader = new RandomAccessFile("/proc/cpuinfo", "r");
            reader.read(bs);
            String ret = new String(bs);
            int index = ret.indexOf(0);
            if (index != -1) {
                strInfo = ret.substring(0, index);
            } else {
                strInfo = ret;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return strInfo;
    }
}
