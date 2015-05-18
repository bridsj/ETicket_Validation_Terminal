package com.ticket.validation.terminal.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivityHandler;
import com.google.zxing.client.android.InactivityTimer;
import com.google.zxing.client.android.ViewfinderView;
import com.google.zxing.client.android.camera.CameraManager;
import com.ticket.validation.terminal.R;

import java.util.Collection;
import java.util.Map;

/**
 * Created by dengshengjin on 15/5/17.
 */
public class FragmentValidationQrCode extends BaseFragment implements SurfaceHolder.Callback {
    private CaptureActivityHandler handler;
    private CameraManager cameraManager;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private ViewfinderView viewfinderView;
    private String characterSet;
    private View mContentView;
    public static FragmentValidationQrCode newInstance() {
        FragmentValidationQrCode f = new FragmentValidationQrCode();
        return f;
    }
    @Override
    protected void initData() {
        hasSurface = false;
        inactivityTimer = new InactivityTimer(getActivity());
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_validation_ticket_qr_code, container, false);
        return mContentView;
    }

    @Override
    protected void initWidgetActions() {

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("","onResume surfaceCreated"+hasSurface+",initCamera");
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w("", "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            if (handler == null) {
                handler = new CaptureActivityHandler(getActivity(), decodeFormats, decodeHints, characterSet, cameraManager, new CaptureActivityHandler.OnCaptureCallback() {
                    @Override
                    public void drawViewfinder() {
                        viewfinderView.drawViewfinder();
                    }

                    @Override
                    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
                        inactivityTimer.onActivity();
                        handler.sendEmptyMessageDelayed(R.id.restart_preview, 500);
                        Toast.makeText(getApplicationContext(),"handleDecode",Toast.LENGTH_SHORT).show();
                        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(200);
                    }

                    @Override
                    public ViewfinderView getViewfinderView() {
                        return viewfinderView;
                    }
                }, new CaptureActivityHandler.OnCaptureListener() {
                    @Override
                    public Handler getHandler() {
                        return handler;
                    }

                    @Override
                    public CameraManager getCameraManager() {
                        return cameraManager;
                    }
                });
            }
        } catch (Throwable t) {
            Log.e("", "onResume initCamera hhhhh "+t.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("","onResume"+hasSurface+",initCamera");
        cameraManager = new CameraManager(getActivity());
        viewfinderView = (ViewfinderView) mContentView.findViewById(R.id.viewfinder_view);

        viewfinderView.setCameraManager(cameraManager);
        handler = null;
        SurfaceView surfaceView = (SurfaceView) mContentView.findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
//            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        characterSet = null;
        decodeFormats = null;
    }

    @Override
    public void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) mContentView.findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();

            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }
}
