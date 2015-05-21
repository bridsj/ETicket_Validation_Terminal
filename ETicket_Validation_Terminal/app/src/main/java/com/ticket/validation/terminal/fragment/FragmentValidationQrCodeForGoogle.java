//package com.ticket.validation.terminal.fragment;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.DecodeHintType;
//import com.google.zxing.Result;
//import com.google.zxing.client.android.BeepManager;
//import com.google.zxing.client.android.CaptureActivityHandler;
//import com.google.zxing.client.android.InactivityTimer;
//import com.google.zxing.client.android.ViewfinderView;
//import com.google.zxing.client.android.camera.CameraManager;
//import com.ticket.validation.terminal.R;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Map;
//
///**
// * Created by dengshengjin on 15/5/17.
// */
//public class FragmentValidationQrCodeForGoogle extends BaseFragment implements SurfaceHolder.Callback {
//    private CaptureActivityHandler handler;
//    private CameraManager cameraManager;
//    private Collection<BarcodeFormat> decodeFormats;
//    private Map<DecodeHintType, ?> decodeHints;
//    private boolean hasSurface;
//    private InactivityTimer inactivityTimer;
//    private ViewfinderView viewfinderView;
//    private String characterSet;
//    private View mContentView;
//    private BeepManager beepManager;
//
//    public static FragmentValidationQrCodeForGoogle newInstance() {
//        FragmentValidationQrCodeForGoogle f = new FragmentValidationQrCodeForGoogle();
//        return f;
//    }
//
//    @Override
//    protected void initData() {
//        hasSurface = false;
//        inactivityTimer = new InactivityTimer(getActivity());
//        beepManager = new BeepManager(getActivity());
//    }
//
//    @Override
//    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mContentView = inflater.inflate(R.layout.fragment_validation_ticket_qr_code_for_google, container, false);
//        return mContentView;
//    }
//
//    @Override
//    protected void initWidgetActions() {
//
//    }
//
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        if (holder == null) {
//            Log.e("", "*** WARNING *** surfaceCreated() gave us a null surface!");
//        }
//        if (!hasSurface) {
//            hasSurface = true;
//            initCamera(holder);
//        }
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        hasSurface = false;
//    }
//
//    private void initCamera(SurfaceHolder surfaceHolder) {
//        if (surfaceHolder == null) {
//            throw new IllegalStateException("No SurfaceHolder provided");
//        }
//        if (cameraManager.isOpen()) {
//            Log.w("", "initCamera() while already open -- late SurfaceView callback?");
//            return;
//        }
//        try {
//            cameraManager.openDriver(surfaceHolder);
//            // Creating the handler starts the preview, which can also throw a RuntimeException.
//            if (handler == null) {
//                handler = new CaptureActivityHandler(getActivity(), decodeFormats, decodeHints, characterSet, cameraManager, new CaptureActivityHandler.CaptureHandlerListener() {
//                    @Override
//                    public void drawViewfinder() {
//                        viewfinderView.drawViewfinder();
//                    }
//
//                    @Override
//                    public ViewfinderView getViewfinderView() {
//                        return viewfinderView;
//                    }
//
//                    @Override
//                    public void handleCaptureDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
//                        handleDecode(rawResult, barcode, scaleFactor);
//                    }
//
//                    @Override
//                    public Handler getHandler() {
//                        return handler;
//                    }
//
//                    @Override
//                    public CameraManager getCameraManager() {
//                        return cameraManager;
//                    }
//                });
//            }
//
//
//        } catch (IOException ioe) {
//        } catch (RuntimeException e) {
//            // Barcode Scanner has seen crashes in the wild of this variety:
//            // java.?lang.?RuntimeException: Fail to connect to camera service
//        }
//    }
//
//    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
//        inactivityTimer.onActivity();
//        handler.sendEmptyMessageDelayed(R.id.restart_preview, 500);
//        beepManager.playBeepSoundAndVibrate();
//        Toast.makeText(getApplicationContext(), "验证成功" + rawResult.getText(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.e("", "onResume" + hasSurface + ",initCamera");
//        cameraManager = new CameraManager(getActivity());
//        viewfinderView = (ViewfinderView) mContentView.findViewById(R.id.viewfinder_view);
//
//        viewfinderView.setCameraManager(cameraManager);
//        handler = null;
//        SurfaceView surfaceView = (SurfaceView) getActivity().findViewById(R.id.preview_view);
//        surfaceView.setVisibility(View.VISIBLE);
//        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        if (hasSurface) {
//            initCamera(surfaceHolder);
//        } else {
//            surfaceHolder.addCallback(this);
////            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        }
//        beepManager.updatePrefs();
//        inactivityTimer.onResume();
//        characterSet = null;
//        decodeFormats = null;
//    }
//
//    private static final String[] ZXING_URLS = {"http://zxing.appspot.com/scan", "zxing://scan/"};
//
//    private static boolean isZXingURL(String dataString) {
//        if (dataString == null) {
//            return false;
//        }
//        for (String url : ZXING_URLS) {
//            if (dataString.startsWith(url)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void onPause() {
//        if (handler != null) {
//            handler.quitSynchronously();
//            handler = null;
//        }
//        inactivityTimer.onPause();
//        beepManager.close();
//        cameraManager.closeDriver();
//        if (!hasSurface) {
//            SurfaceView surfaceView = (SurfaceView) getActivity().findViewById(R.id.preview_view);
//            surfaceView.setVisibility(View.GONE);
//            SurfaceHolder surfaceHolder = surfaceView.getHolder();
//            surfaceHolder.removeCallback(this);
//        }
//        super.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        inactivityTimer.shutdown();
//        super.onDestroy();
//    }
//}
