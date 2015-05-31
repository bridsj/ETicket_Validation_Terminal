package com.ticket.validation.terminal.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;

import com.synjones.sdt.IDCard;
import com.synjones.sdt.SerialPort;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dengshengjin on 15/5/31.
 */
public class IDCardZKCStrategy implements IDCardStrategy {
    private SerialPort mSerialPort;
    private ExecutorService mExecutorService;
    private Handler mHandler;
    private PowerManager.WakeLock mWakeLock;
    private boolean mIsPrinting;

    public IDCardZKCStrategy(Context context) {
        try {
            mSerialPort = getSerialPort();
        } catch (Throwable t) {
        }
        mExecutorService = Executors.newSingleThreadExecutor();
        mHandler = new Handler(Looper.getMainLooper());
        mWakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wake_lock");
        mIsPrinting = false;
    }

    @Override
    public void startRecognition(final RecognitionCallback callback) {
        if (mIsPrinting) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onErrorIDCard();
                    }
                }
            });
            return;
        }
        mIsPrinting = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onStartIDCard();
                }
            }
        });

        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mWakeLock.acquire();
                    //开始打印
                    final IDCard idcard = mSerialPort.getIDCard();
                    if (idcard == null) {
                        throw new Exception();
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinishIDCard(idcard.getIDCardNo());
                            }
                        }
                    });
                } catch (Throwable t) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFailIDCard();
                            }
                        }
                    });
                } finally {
                    mWakeLock.release();
                    closePort();
                    mIsPrinting = false;
                }

            }
        });
    }

    private SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            mSerialPort = new SerialPort(new File("/dev/ttySAC1"), 115200, 0);
            mSerialPort.setMaxRFByte((byte) 0x50);
        }

        return mSerialPort;
    }

    private void closePort() {
        try {
            if (mSerialPort != null) {
                mSerialPort.close();
                mSerialPort = null;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }
}
