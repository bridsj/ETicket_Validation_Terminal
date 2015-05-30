//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wizarpos.barcode.scanner;

public class ScannerResult {
    private String result;
    private String resultFormat;
    private byte[] barcodeBitmap;

    public ScannerResult() {
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String var1) {
        this.result = var1;
    }

    public String getResultFormat() {
        return this.resultFormat;
    }

    public void setResultFormat(String var1) {
        this.resultFormat = var1;
    }

    public byte[] getBarcodeBitmap() {
        return this.barcodeBitmap;
    }

    public void setBarcodeBitmap(byte[] var1) {
        this.barcodeBitmap = var1;
    }
}
