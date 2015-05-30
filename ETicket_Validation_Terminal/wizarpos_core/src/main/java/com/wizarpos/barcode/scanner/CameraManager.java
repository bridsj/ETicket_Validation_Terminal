//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wizarpos.barcode.scanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;

public final class CameraManager {
    private boolean previewing = false;
    private PreviewCallback mPreviewCallback = null;
    private Camera mCamera = null;
    private Context mContext = null;
    private CameraConfigurationManager manager;

    private static final int MIN_FRAME_WIDTH = 240;
    private static final int MIN_FRAME_HEIGHT = 240;
    private static final int MAX_FRAME_WIDTH = 1200; // = 5/8 * 1920
    private static final int MAX_FRAME_HEIGHT = 675; // = 5/8 * 1080
    private boolean initialized;
    private int requestedFramingRectWidth;
    private int requestedFramingRectHeight;

    private Rect framingRect;
    private Rect framingRectInPreview;

    public CameraManager(Context var1, PreviewCallback var2, CameraConfigurationManager var3) {
        this.mContext = var1;
        this.mPreviewCallback = var2;
        this.manager = var3;
    }

    public Camera initCamera(SurfaceHolder var1) {
        try {
            if (!initialized) {
                initialized = true;
                if (requestedFramingRectWidth > 0 && requestedFramingRectHeight > 0) {
                    setManualFramingRect(requestedFramingRectWidth, requestedFramingRectHeight);
                    requestedFramingRectWidth = 0;
                    requestedFramingRectHeight = 0;
                }
            }
            if (null == this.mCamera) {
                CameraInfo var2 = new CameraInfo();
                int var3 = Camera.getNumberOfCameras();
                SharedPreferences var4 = PreferenceManager.getDefaultSharedPreferences(this.mContext);
                int var5 = var4.getInt("preferences_camera_faceing", 1);

                for (int var6 = 0; var6 < var3; ++var6) {
                    Camera.getCameraInfo(var6, var2);
                    if (var2.facing == var5) {
                        try {
                            this.mCamera = Camera.open(var6);
                        } catch (RuntimeException var8) {
                            var8.printStackTrace();
                        }
                    }
                }
            }

            if (null != this.mCamera) {
                this.mCamera.setPreviewDisplay(var1);
                this.manager.initFromCameraParameters(this.mCamera);
                this.manager.setDesiredCameraParameters(this.mCamera);
                this.mCamera.startPreview();
                this.previewing = true;
                this.requestPreviewFrame();
            }
        } catch (Exception var9) {
            if (null != this.mCamera) {
                this.mCamera.setPreviewCallback((PreviewCallback) null);
                this.mCamera.release();
                this.previewing = false;
                this.mCamera = null;
            }

            var9.printStackTrace();
        }

        return this.mCamera;
    }

    public void requestPreviewFrame() {
        if (this.mCamera != null && this.previewing) {
            this.mCamera.setOneShotPreviewCallback(this.mPreviewCallback);
        }

    }

    public void resetCamera() {
        if (this.mCamera != null) {
            this.mCamera.stopPreview();
            this.previewing = false;
            this.mCamera.setPreviewCallback((PreviewCallback) null);
            this.mCamera.release();
            this.mCamera = null;
        }
        framingRect = null;
        framingRectInPreview = null;

    }

    public synchronized Rect getFramingRect() {
        if (framingRect == null) {
            if (mCamera == null) {
                return null;
            }
            Point screenResolution = manager.getScreenResolution();
            if (screenResolution == null) {
                // Called early, before init even finished
                return null;
            }

            int width = findDesiredDimensionInRange(screenResolution.x, MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
            int height = findDesiredDimensionInRange(screenResolution.y, MIN_FRAME_HEIGHT, MAX_FRAME_HEIGHT);

            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = (screenResolution.y - height) / 2;
            framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
            Log.d("", "Calculated framing rect: " + framingRect);
        }
        return framingRect;
    }

    public synchronized void setManualFramingRect(int width, int height) {
        if (initialized) {
            Point screenResolution = manager.getScreenResolution();
            if (width > screenResolution.x) {
                width = screenResolution.x;
            }
            if (height > screenResolution.y) {
                height = screenResolution.y;
            }
            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = (screenResolution.y - height) / 2;
            framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
            Log.d("", "Calculated manual framing rect: " + framingRect);
            framingRectInPreview = null;
        }else{
            requestedFramingRectWidth = width;
            requestedFramingRectHeight = height;
        }
    }

    public synchronized Rect getFramingRectInPreview() {
        if (framingRectInPreview == null) {
            Rect framingRect = getFramingRect();
            if (framingRect == null) {
                return null;
            }
            Rect rect = new Rect(framingRect);
            Point cameraResolution = manager.getCameraResolution();
            Point screenResolution = manager.getScreenResolution();
            if (cameraResolution == null || screenResolution == null) {
                // Called early, before init even finished
                return null;
            }
            rect.left = rect.left * cameraResolution.x / screenResolution.x;
            rect.right = rect.right * cameraResolution.x / screenResolution.x;
            rect.top = rect.top * cameraResolution.y / screenResolution.y;
            rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
            framingRectInPreview = rect;
        }
        return framingRectInPreview;
    }

    private static int findDesiredDimensionInRange(int resolution, int hardMin, int hardMax) {
        int dim = 4 * resolution / 8; // Target 5/8 of each dimension
        if (dim < hardMin) {
            return hardMin;
        }
        if (dim > hardMax) {
            return hardMax;
        }
        return dim;
    }
}
