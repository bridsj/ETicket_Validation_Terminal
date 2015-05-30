//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wizarpos.barcode.scanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback2;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.google.zxing.client.android.PlanarYUVLuminanceSource;
import com.wizarpos.barcode.decode.BarcodeDecoder;
import com.wizarpos.barcode.decode.DecodeResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ScannerRelativeLayout extends RelativeLayout {
    private static final String TAG = "ScannerRelativeLayout";
    private Context context;
    private ViewfinderResultPointCallback resultPointCallback;
    private SurfaceHolder mSurfaceHolder;
    private CameraConfigurationManager manager;
    private CameraManager mCameraManager;
    private BarcodeDecoder mBarcodeDecoder;
    private ScannerRelativeLayout.InnerSurfaceHolderCallback mInnerSurfaceHolderCallback;
    private static final float BEEP_VOLUME = 0.8F;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private SurfaceView mSurfaceViewPreview;
    private IScanEvent mIScanSuccessListener;
    private AssetManager assetManager;
    private View rightFunction;
    private Camera mCamera;
    private boolean cameraIsRelease = true;
    private boolean cameraStopPreview = true;
    private boolean StopBypause = true;
    private String encodeFormat = null;
    private int isOn = 0;
    long start = 0L;

    public String getEncodeFormat() {
        return this.encodeFormat;
    }

    public void setEncodeFormat(String var1) {
        this.encodeFormat = var1;
    }

    public ScannerRelativeLayout(Context var1) {
        super(var1);
        this.initialization(var1);
    }

    public ScannerRelativeLayout(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.initialization(var1);
    }

    public ScannerRelativeLayout(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.initialization(var1);
    }

    public boolean onTouchEvent(MotionEvent var1) {
        switch (var1.getAction()) {
            case 0:
            case 1:
            case 2:
            default:
                return super.onTouchEvent(var1);
        }
    }

    public void setFrontFacingCamera(boolean var1) {
        SharedPreferences var2 = PreferenceManager.getDefaultSharedPreferences(this.context);
        Editor var3 = var2.edit();
        if (var1) {
            var3.putInt("preferences_camera_faceing", 1);
        } else {
            var3.putInt("preferences_camera_faceing", 0);
        }

        var3.commit();
    }

    private View prepare() {
        RelativeLayout var1 = new RelativeLayout(this.context);
        LayoutParams var2 = new LayoutParams(-2, -1);
        var2.addRule(9);
        var2.alignWithParent = true;
        LayoutParams var3 = new LayoutParams(-2, -2);
        var1.setLayoutParams(var2);
        var1.setBackgroundColor(2500134);
        return var1;
    }

    public void openLED() {
        if (!this.cameraIsRelease && this.mCamera != null) {
            Parameters var1 = this.mCamera.getParameters();
            var1.setFlashMode("torch");
            this.mCamera.setParameters(var1);
            this.mCamera.startPreview();
            Log.v("", "open");
        }

    }

    public void closeLED() {
        if (!this.cameraIsRelease && this.mCamera != null) {
            Parameters var1 = this.mCamera.getParameters();
            var1.setFlashMode("off");
            this.mCamera.setParameters(var1);
            Log.v("", "close");
        }

    }

    private void initialization(Context var1) {
        Log.v("ScannerRelativeLayout", "initialization");
        this.context = var1;
        this.mSurfaceViewPreview = new ScannerSurfaceView(var1);
        this.addView(this.mSurfaceViewPreview, new LayoutParams(-1, -1));
        this.rightFunction = this.prepare();
        this.addView(this.rightFunction);
        this.setWillNotDraw(false);
        this.audioManager = (AudioManager) var1.getSystemService("audio");
        this.resultPointCallback = new ViewfinderResultPointCallback(null);
        this.mBarcodeDecoder = new BarcodeDecoder((String) null, this.resultPointCallback);
        this.mSurfaceHolder = this.getSurfaceHolder();
        this.mInnerSurfaceHolderCallback = new ScannerRelativeLayout.InnerSurfaceHolderCallback();
        this.manager = new CameraConfigurationManager(var1);
        this.mCameraManager = new CameraManager(var1, new ScannerRelativeLayout.InnerCameraPreviewCallback(this.manager), this.manager);
        this.assetManager = var1.getAssets();
        this.mediaPlayer = this.buildMediaPlayer(var1);
    }

    public synchronized void onResume() {
        Log.v("ScannerRelativeLayout", "onResume");
        this.StopBypause = false;
        this.mSurfaceHolder.setType(3);
        this.mSurfaceHolder.addCallback(this.mInnerSurfaceHolderCallback);
    }

    public synchronized void onPause() {
        this.StopBypause = true;
        Log.v("ScannerRelativeLayout", "onPause");
        if (!this.cameraIsRelease) {
            this.setBoolInXml();
        }

        this.mCameraManager.resetCamera();
        if (null != this.mSurfaceHolder) {
            this.mSurfaceHolder.removeCallback(this.mInnerSurfaceHolderCallback);
        }

    }

    private void setBoolInXml() {
        try {
            if (this.mCamera != null) {
                String var1 = this.mCamera.getParameters().getFlashMode();
                if (var1 != null) {
                    SharedPreferences var2 = PreferenceManager.getDefaultSharedPreferences(this.context);
                    Editor var3 = var2.edit();
                    if (!var1.equals("on") && !var1.equals("torch")) {
                        if (var1.equals("off")) {
                            var3.putBoolean("preferences_front_light", false);
                        }
                    } else {
                        var3.putBoolean("preferences_front_light", true);
                    }

                    var3.commit();
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public synchronized void stopScan() {
        if (!this.cameraIsRelease) {
            Log.v("ScannerRelativeLayout", "stopScan");
            this.setBoolInXml();
            this.mCameraManager.resetCamera();
            if (null != this.mSurfaceHolder) {
                this.mSurfaceHolder.removeCallback(this.mInnerSurfaceHolderCallback);
            }

            this.cameraIsRelease = true;
        }

        this.cameraStopPreview = true;
    }

    public boolean isCameraStopPreview() {
        return this.cameraStopPreview;
    }

    public synchronized void startScan() {
        if (this.cameraIsRelease && !this.StopBypause) {
            Log.v("ScannerRelativeLayout", "startScan");
            if (this.mSurfaceHolder != null) {
                Log.v("ScannerRelativeLayout", "init camera");
                this.mCamera = this.mCameraManager.initCamera(this.mSurfaceHolder);
                if (this.mCamera != null) {
                    String var1 = this.mCamera.getParameters().getFlashMode();
                    if (var1 != null) {

                    }
                }
            }

            this.cameraIsRelease = false;
        }

        this.cameraStopPreview = false;
    }

    public void setScanSuccessListener(IScanEvent var1) {
        this.mIScanSuccessListener = var1;
    }


    public SurfaceHolder getSurfaceHolder() {
        return this.mSurfaceViewPreview.getHolder();
    }

    protected void onDraw(Canvas var1) {
        super.onDraw(var1);
    }

    private void decode(byte[] var1, int var2, int var3) {
        Rect var4 = new Rect(0, 0, var2, var3);
        boolean var5 = false;
        PlanarYUVLuminanceSource var6 = new PlanarYUVLuminanceSource(var1, var2, var3, var4.left, var4.top, var4.width(), var4.height(), var5);
        Bitmap var7 = null;
        DecodeResult var8 = null;
        if (var6 != null) {
            var8 = this.mBarcodeDecoder.decode(var6);
            long var9 = System.currentTimeMillis();
            this.start = var9;
        }

        if (null == var8) {
            if (!this.cameraStopPreview) {
                this.invalidate();
                this.mCameraManager.requestPreviewFrame();
            }
        } else {
            var7 = var6.renderCroppedGreyscaleBitmap();
            if (this.mediaPlayer != null) {
                this.mediaPlayer.start();
            } else {
                this.audioManager.playSoundEffect(0, 0.8F);
            }

            Log.d("ScannerRelativeLayout", "find a barcode");
            ByteArrayOutputStream var12 = new ByteArrayOutputStream();
            var7.compress(CompressFormat.JPEG, 50, var12);
            byte[] var10 = var12.toByteArray();
            ScannerResult var11 = new ScannerResult();
            var11.setResult(var8.getText());
            var11.setResultFormat(var8.getBarcodeFormat());
            var11.setBarcodeBitmap(var10);
            if (this.mIScanSuccessListener != null) {
                this.mIScanSuccessListener.scanCompleted(var11);
                if (!this.cameraStopPreview) {
                    this.invalidate();
                    this.mCameraManager.requestPreviewFrame();
                }
            } else {
                this.stopScan();
            }
        }

    }

    private MediaPlayer buildMediaPlayer(Context var1) {
        MediaPlayer var2 = new MediaPlayer();
        var2.setAudioStreamType(3);
        var2.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer var1) {
                var1.seekTo(0);
            }
        });

        try {
            AssetFileDescriptor var3 = this.assetManager.openFd("beep.ogg");
            var2.setDataSource(var3.getFileDescriptor(), var3.getStartOffset(), var3.getLength());
            var3.close();
            var2.setVolume(0.8F, 0.8F);
            var2.prepare();
        } catch (IOException var4) {
            var2 = null;
        }

        return var2;
    }

    private class InnerCameraPreviewCallback implements PreviewCallback {
        public InnerCameraPreviewCallback(CameraConfigurationManager var2) {
        }

        public void onPreviewFrame(byte[] var1, Camera var2) {
            if (var2 != null) {
                Parameters var3 = var2.getParameters();
                Size var4 = var3.getPreviewSize();
                ScannerRelativeLayout.this.decode(var1, var4.width, var4.height);
            }

        }
    }

    public CameraManager getCameraManager(){
        return mCameraManager;
    }

    private class InnerSurfaceHolderCallback implements Callback2 {
        private InnerSurfaceHolderCallback() {
        }

        public void surfaceCreated(SurfaceHolder var1) {
            if (!ScannerRelativeLayout.this.cameraIsRelease) {
                ScannerRelativeLayout.this.mCamera = ScannerRelativeLayout.this.mCameraManager.initCamera(var1);
                if (null == ScannerRelativeLayout.this.mCamera) {
                    ;
                }
            }

        }

        public void surfaceChanged(SurfaceHolder var1, int var2, int var3, int var4) {
        }

        public void surfaceDestroyed(SurfaceHolder var1) {
        }

        public void surfaceRedrawNeeded(SurfaceHolder var1) {
        }
    }
}
