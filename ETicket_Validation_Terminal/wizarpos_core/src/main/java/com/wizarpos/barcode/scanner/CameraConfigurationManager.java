//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wizarpos.barcode.scanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.util.Collection;
import java.util.Iterator;

public final class CameraConfigurationManager {
    private static final String TAG = "CameraConfiguration";
    private static final int MIN_PREVIEW_PIXELS = 76800;
    private static final int MAX_PREVIEW_PIXELS = 480000;
    private final Context context;
    private Point screenResolution;
    private Point cameraResolution;

    CameraConfigurationManager(Context var1) {
        this.context = var1;
    }

    public void initFromCameraParameters(Camera var1) {
        Parameters var2 = var1.getParameters();
        WindowManager var3 = (WindowManager)this.context.getSystemService("window");
        Display var4 = var3.getDefaultDisplay();
        int var5 = var4.getWidth();
        int var6 = var4.getHeight();
        if(var5 < var6) {
            Log.i("CameraConfiguration", "Display reports portrait orientation; assuming this is incorrect");
            int var7 = var5;
            var5 = var6;
            var6 = var7;
        }

        this.screenResolution = new Point(var5, var6);
        Log.i("CameraConfiguration", "Screen resolution: " + this.screenResolution);
        this.cameraResolution = findBestPreviewSizeValue(var2, this.screenResolution, false);
        Log.i("CameraConfiguration", "Camera resolution: " + this.cameraResolution);
    }

    public void setCameraDisplayOrientation(int var1, Camera var2) {
        CameraInfo var3 = new CameraInfo();
        Camera.getCameraInfo(var1, var3);
        WindowManager var4 = (WindowManager)this.context.getSystemService("window");
        Display var5 = var4.getDefaultDisplay();
        int var6 = var5.getRotation();
        short var7 = 0;
        switch(var6) {
            case 0:
                var7 = 0;
                break;
            case 1:
                var7 = 90;
                break;
            case 2:
                var7 = 180;
                break;
            case 3:
                var7 = 270;
        }

        int var8;
        if(var3.facing == 1) {
            var8 = (var3.orientation + var7) % 360;
            var8 = (360 - var8) % 360;
        } else {
            var8 = (var3.orientation - var7 + 360) % 360;
        }

        var2.setDisplayOrientation(var8);
    }

    public void setDesiredCameraParameters(Camera var1) {
        Parameters var2 = var1.getParameters();
        if(var2 == null) {
            Log.w("CameraConfiguration", "Device error: no camera parameters are available. Proceeding without configuration.");
        } else {
            SharedPreferences var3 = PreferenceManager.getDefaultSharedPreferences(this.context);
            initializeTorch(var2, var3);
            String var4 = findSettableValue(var2.getSupportedFocusModes(), new String[]{"auto", "macro"});
            if(var4 != null) {
                var2.setFocusMode(var4);
            }

            var2.setPreviewSize(this.cameraResolution.x, this.cameraResolution.y);
            Log.w("CameraConfiguration", "parameters.setPreviewSize x=" + this.cameraResolution.x + " y=" + this.cameraResolution.y);
            this.setCameraDisplayOrientation(0, var1);
            var1.setParameters(var2);
        }
    }

    Point getCameraResolution() {
        return this.cameraResolution;
    }

    Point getScreenResolution() {
        return this.screenResolution;
    }

    void setTorch(Camera var1, boolean var2) {
        Parameters var3 = var1.getParameters();
        doSetTorch(var3, var2);
        var1.setParameters(var3);
        SharedPreferences var4 = PreferenceManager.getDefaultSharedPreferences(this.context);
        boolean var5 = var4.getBoolean("preferences_front_light", false);
        if(var5 != var2) {
            Editor var6 = var4.edit();
            var6.putBoolean("preferences_front_light", var2);
            var6.commit();
        }

    }

    private static void initializeTorch(Parameters var0, SharedPreferences var1) {
        boolean var2 = var1.getBoolean("preferences_front_light", true);
        doSetTorch(var0, var2);
    }

    private static void doSetTorch(Parameters var0, boolean var1) {
        String var2;
        if(var1) {
            var2 = findSettableValue(var0.getSupportedFlashModes(), new String[]{"torch", "on"});
        } else {
            var2 = findSettableValue(var0.getSupportedFlashModes(), new String[]{"off"});
        }

        if(var2 != null) {
            var0.setFlashMode(var2);
        }

    }

    private static Point findBestPreviewSizeValue(Parameters var0, Point var1, boolean var2) {
        Point var3 = null;
        int var4 = 2147483647;
        Iterator var5 = var0.getSupportedPreviewSizes().iterator();

        while(var5.hasNext()) {
            Size var6 = (Size)var5.next();
            Log.i("CameraConfiguration", "Camera supportedPreviewSize: " + var6.width + "x" + var6.height);
            int var7 = var6.height * var6.width;
            if(var7 >= 76800 && var7 <= 480000) {
                int var8 = var2?var6.height:var6.width;
                int var9 = var2?var6.width:var6.height;
                int var10 = Math.abs(var1.x * var9 - var8 * var1.y);
                if(var10 == 0) {
                    var3 = new Point(var8, var9);
                    break;
                }

                if(var10 < var4) {
                    var3 = new Point(var8, var9);
                    var4 = var10;
                }
            }
        }

        if(var3 == null) {
            Size var11 = var0.getPreviewSize();
            var3 = new Point(var11.width, var11.height);
        }

        return var3;
    }

    private static String findSettableValue(Collection<String> var0, String... var1) {
        Log.i("CameraConfiguration", "Supported values: " + var0);
        String var2 = null;
        if(var0 != null) {
            String[] var3 = var1;
            int var4 = var1.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String var6 = var3[var5];
                if(var0.contains(var6)) {
                    var2 = var6;
                    break;
                }
            }
        }

        Log.i("CameraConfiguration", "Settable value: " + var2);
        return var2;
    }
}
