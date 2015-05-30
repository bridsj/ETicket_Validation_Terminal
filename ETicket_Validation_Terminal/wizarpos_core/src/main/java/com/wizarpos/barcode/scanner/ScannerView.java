//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wizarpos.barcode.scanner;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

public class ScannerView extends RelativeLayout {
    private SurfaceView mSurfaceViewPreview;
    private TransparentView transparentView;

    public ScannerView(Context var1) {
        super(var1);
        this.mSurfaceViewPreview = new MySurfaceView(var1);
        this.addView(this.mSurfaceViewPreview, new LayoutParams(-1, -1));
        this.transparentView = new TransparentView(var1);
        this.addView(this.transparentView);
        this.setWillNotDraw(false);
    }

    public TransparentView getTransparentView() {
        return this.transparentView;
    }

    public void setTransparentView(TransparentView var1) {
        this.transparentView = var1;
    }

    public SurfaceHolder getSurfaceHolder() {
        return this.mSurfaceViewPreview.getHolder();
    }

    protected void onDraw(Canvas var1) {
        super.onDraw(var1);
        this.bringChildToFront(this.transparentView);
        this.transparentView.invalidate();
    }
}
