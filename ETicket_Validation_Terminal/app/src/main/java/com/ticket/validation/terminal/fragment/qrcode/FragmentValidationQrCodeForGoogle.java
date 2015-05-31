//package com.ticket.validation.terminal.fragment.qrcode;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
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
//import com.ticket.validation.terminal.ValidationResultActivity;
//import com.ticket.validation.terminal.ValidationTicketActivity;
//import com.ticket.validation.terminal.fragment.BaseQueryFragment;
//import com.ticket.validation.terminal.model.ErrorModel;
//import com.ticket.validation.terminal.model.GoodsModel;
//import com.ticket.validation.terminal.util.LoginInterceporUtil;
//import com.ticket.validation.terminal.util.ToastUtil;
//import com.zuiapps.suite.utils.device.DoubleKeyUtils;
//import com.zuiapps.suite.utils.log.LogUtil;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.LinkedList;
//import java.util.Map;
//
///**
// * Created by dengshengjin on 15/5/17.
// */
//public class FragmentValidationQrCodeForGoogle extends BaseQueryFragment implements SurfaceHolder.Callback {
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
//    private ViewGroup mVerifyBox;
//    private TextView mStatusText;
//    private boolean mIsOnPause;
//    private TextView mQrLightText;
//    private boolean mIsOpenLight;
//
//    public static FragmentValidationQrCodeForGoogle newInstance() {
//        FragmentValidationQrCodeForGoogle f = new FragmentValidationQrCodeForGoogle();
//        return f;
//    }
//
//    @Override
//    protected void initData() {
//        super.initData();
//        hasSurface = false;
//        mIsOpenLight = false;
//        inactivityTimer = new InactivityTimer(getActivity());
//        beepManager = new BeepManager(getActivity());
//    }
//
//    @Override
//    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mContentView = inflater.inflate(R.layout.fragment_validation_ticket_qr_code_for_google, container, false);
//
//        mVerifyBox = (ViewGroup) mContentView.findViewById(R.id.scanner_box);
//        mVerifyBox.setVisibility(View.INVISIBLE);
//        mStatusText = (TextView) mContentView.findViewById(R.id.status_text);
//        mQrLightText = (TextView) mContentView.findViewById(R.id.qr_light_text);
//        mStatusText.setText("");
//        return mContentView;
//    }
//
//    @Override
//    protected void initWidgetActions() {
//
//        mQrLightText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mIsOpenLight) {
//                    mIsOpenLight = false;
//                    cameraManager.setTorch(false);
//                    if (mOnLightClickListener != null) {
//                        mOnLightClickListener.onChangeLight(false);
//                    }
//                } else {
//                    mIsOpenLight = true;
//                    cameraManager.setTorch(true);
//                    if (mOnLightClickListener != null) {
//                        mOnLightClickListener.onChangeLight(true);
//                    }
//                }
//
//            }
//        });
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
//        onHandleDecodeSucc(rawResult.getText());
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mIsOnPause = false;
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
//            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        }
//
//        beepManager.updatePrefs();
//        inactivityTimer.onResume();
//        characterSet = null;
//        decodeFormats = null;
//        cameraManager.setManualFramingRect(getResources().getDimensionPixelOffset(R.dimen.view_finder_width), getResources().getDimensionPixelOffset(R.dimen.view_finder_height));
//        if (getActivity() != null && getActivity() instanceof ValidationTicketActivity) {
//            mIsOpenLight = ((ValidationTicketActivity) getActivity()).isOpenLight();
//        }
//        LogUtil.e("mIsOpenLight=" + mIsOpenLight);
//        getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mIsOpenLight) {
//                    cameraManager.setTorch(true);
//                } else {
//                    cameraManager.setTorch(false);
//                }
//            }
//        }, 500);
//
//        setFrontFacingCamera(true);
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
//        try {
//            if (handler != null) {
//                handler.quitSynchronously();
//                handler = null;
//            }
//            mIsOnPause = true;
//            inactivityTimer.onPause();
//            beepManager.close();
//            cameraManager.closeDriver();
//            if (!hasSurface) {
//                SurfaceView surfaceView = (SurfaceView) getActivity().findViewById(R.id.preview_view);
//                surfaceView.setVisibility(View.GONE);
//                SurfaceHolder surfaceHolder = surfaceView.getHolder();
//                surfaceHolder.removeCallback(this);
//            }
//        } catch (Throwable t) {
//
//        } finally {
//            super.onPause();
//        }
//
//    }
//
//    @Override
//    public void onDestroy() {
//        inactivityTimer.shutdown();
//        super.onDestroy();
//    }
//
//    private void onHandleDecodeSucc(String result) {
//        if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
//            return;
//        }
//        if (mIsOnPause) {
//            return;
//        }
//        if (DoubleKeyUtils.isFastDoubleClick(1000)) {
//            return;
//        }
//        if (mVerifyBox.getVisibility() == View.VISIBLE) {
//            return;
//        }
//        if (getActivity() == null || getActivity().isFinishing()) {
//            return;
//        }
//        inactivityTimer.onActivity();
//        handler.sendEmptyMessageDelayed(R.id.restart_preview, 500);
//        beepManager.playBeepSoundAndVibrate();
//        mVerifyBox.setVisibility(View.VISIBLE);
//        mStatusText.setText(String.format(getString(R.string.validation_qr_code_result), result));
//        queryData(result, new BaseQueryFragment.RestfulCallback() {
//            @Override
//            public void success(LinkedList<GoodsModel> list) {
//                mVerifyBox.setVisibility(View.INVISIBLE);
//                if (list.isEmpty()) {
//                    ToastUtil.showToast(getApplicationContext(), R.string.loading_empty_data);
//                    return;
//                }
//                Intent intent = new Intent(getActivity(), ValidationResultActivity.class);
//                intent.putExtra(ValidationResultActivity.MODELS, list);
//                startActivity(intent);
//            }
//
//            @Override
//            public void failureViaLocal() {
//                mVerifyBox.setVisibility(View.INVISIBLE);
//                ToastUtil.showToast(getApplicationContext(), R.string.loading_fail2);
//            }
//
//            @Override
//            public void failureViaServer(ErrorModel errorModel) {
//                mVerifyBox.setVisibility(View.INVISIBLE);
//                if (errorModel == null) {
//                    ToastUtil.showToast(getApplicationContext(), R.string.loading_fail);
//                    return;
//                }
//                ToastUtil.showToast(getApplicationContext(), errorModel.mInfo);
//            }
//        });
//
//    }
//
//    private OnLightClickListener mOnLightClickListener;
//
//    public void setOnLightClickListener(OnLightClickListener mOnLightClickListener) {
//        this.mOnLightClickListener = mOnLightClickListener;
//    }
//
//    public interface OnLightClickListener {
//        void onChangeLight(boolean isOpenLight);
//    }
//
//
//}
