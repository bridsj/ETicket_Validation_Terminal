//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wizarpos.barcode.scanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import com.google.zxing.ResultPoint;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransparentView extends View {
    private List<ResultPoint> possibleResultPoints = new ArrayList(5);
    private List<ResultPoint> lastPossibleResultPoints = null;
    private Context context;
    private int screenWidth;
    private int screenHeight;

    public TransparentView(Context var1) {
        super(var1);
        this.context = var1;
    }

    protected void onDraw(Canvas var1) {
        super.onDraw(var1);
        Paint var2 = new Paint();
        var2.setColor(-65536);
        int var3 = var1.getWidth();
        int var4 = var1.getHeight();
        var1.drawRect(0.0F, (float)(var4 / 2 - 2), (float)var3, (float)(var4 / 2 + 2), var2);
        var2.setColor(-256);
        List var5 = this.possibleResultPoints;
        List var6 = this.lastPossibleResultPoints;
        Iterator var8;
        ResultPoint var9;
        if(var5.isEmpty()) {
            this.lastPossibleResultPoints = null;
        } else {
            this.possibleResultPoints = new ArrayList(5);
            this.lastPossibleResultPoints = var5;
            synchronized(var5) {
                var8 = var5.iterator();

                while(var8.hasNext()) {
                    var9 = (ResultPoint)var8.next();
                    var1.drawCircle(var9.getX(), var9.getY(), 5.0F, var2);
                }
            }
        }

        if(var6 != null) {
            synchronized(var6) {
                var8 = var6.iterator();

                while(var8.hasNext()) {
                    var9 = (ResultPoint)var8.next();
                    var1.drawCircle(var9.getX(), var9.getY(), 4.0F, var2);
                }
            }
        }

    }

    public void addPossibleResultPoint(ResultPoint var1) {
        List var2 = this.possibleResultPoints;
        synchronized(this.possibleResultPoints) {
            this.possibleResultPoints.add(var1);
            int var3 = this.possibleResultPoints.size();
            if(var3 > 10) {
                this.possibleResultPoints.subList(0, var3 - 2).clear();
            }

        }
    }
}
