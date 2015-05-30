//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wizarpos.barcode.scanner;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

public class ViewfinderResultPointCallback implements ResultPointCallback {
    private TransparentView viewfinderView;

    ViewfinderResultPointCallback(TransparentView var1) {
        this.viewfinderView = var1;
    }

    public void foundPossibleResultPoint(ResultPoint var1) {
        if(viewfinderView!=null) {
            this.viewfinderView.addPossibleResultPoint(var1);
        }
    }
}
