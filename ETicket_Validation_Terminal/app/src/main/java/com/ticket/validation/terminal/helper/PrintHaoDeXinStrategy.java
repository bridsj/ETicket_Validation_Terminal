package com.ticket.validation.terminal.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;

import com.hdx.lib.printer.SerialPrinter;
import com.hdx.lib.serial.SerialParam;
import com.hdx.lib.serial.SerialPortOperaion;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hdx.HdxUtil;

/**
 * Created by dengshengjin on 15/5/27.
 */
//具体策略
public class PrintHaoDeXinStrategy implements PrintStrategy {
    private Context mContext;
    private ExecutorService mExecutorService;
    private boolean mIsPrinting;
    private PowerManager.WakeLock mWakeLock;
    private SerialPrinter mSerialPrinter;
    private Handler mHandler;

    public PrintHaoDeXinStrategy(Context context) {
        super();
        mContext = context;
        mExecutorService = Executors.newSingleThreadExecutor();
        mSerialPrinter = SerialPrinter.GetSerialPrinter();
        HdxUtil.SwitchSerialFunction(HdxUtil.SERIAL_FUNCTION_PRINTER);
        try {
            mSerialPrinter.OpenPrinter(new SerialParam(115200, "/dev/ttyS1", 0), new SerialDataHandler());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
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
                    HdxUtil.SetPrinterPower(1);
                    SystemClock.sleep(200);
                    //开始打印
                    startPrintAsync(printCallback, printStr);
                    SystemClock.sleep(2000);
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
                                printCallback.onFailPrint("");
                            }
                        }
                    });
                } finally {
                    mWakeLock.release();
                    HdxUtil.SetPrinterPower(0);
                    mIsPrinting = false;
                }

            }
        });
    }

    //mSerialPrinter.setLineSpace(50);//行间距
//    mSerialPrinter.sendLineFeed();//换行
//    mSerialPrinter.enlargeFontSize(1, 1); 正常字体打印
    //mSerialPrinter.enlargeFontSize(1, 2); 倍高打印
//    mSerialPrinter.enlargeFontSize(2, 1); 倍宽打印
    // mSerialPrinter.printString(arr.get(1)); 打印文字
    //mSerialPrinter.walkPaper(100); 继续走50点行
    private void startPrintAsync(final PrintCallback printCallback, final String printStr) throws Throwable {
        mSerialPrinter.printString(printStr);
        mSerialPrinter.walkPaper(80);// 测试结束往下走纸25点行 */
    }

    private class SerialDataHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SerialPortOperaion.SERIAL_RECEIVED_DATA_MSG:
                    SerialPortOperaion.SerialReadData data = (SerialPortOperaion.SerialReadData) msg.obj;
                    StringBuilder sb = new StringBuilder();
                    for (int x = 0; x < data.size; x++) {
                        sb.append(String.format("%02x", data.data[x]));
                    }
//                    ToastUtil.showToast(mContext, "data=" + sb);
            }
        }
    }
}
