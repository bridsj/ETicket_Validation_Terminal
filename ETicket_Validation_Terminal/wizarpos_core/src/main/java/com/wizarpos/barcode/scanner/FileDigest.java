//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wizarpos.barcode.scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class FileDigest {
    public FileDigest() {
    }

    public static void copyFile(InputStream var0, String var1, String var2) {
        try {
            int var3 = 0;
            boolean var4 = false;
            File var5 = new File(var1);
            File var6 = new File(var1 + var2);
            if(!var5.exists()) {
                var5.mkdir();
            }

            if(!var6.exists()) {
                RandomAccessFile var7 = new RandomAccessFile(var6, "rws");
                byte[] var8 = new byte[1444];

                int var11;
                while((var11 = var0.read(var8)) != -1) {
                    var3 += var11;
                    var7.write(var8, 0, var11);
                }

                var0.close();
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    public void copyFolder(String var1, String var2) {
        try {
            (new File(var2)).mkdirs();
            File var3 = new File(var1);
            String[] var4 = var3.list();
            File var5 = null;

            for(int var6 = 0; var6 < var4.length; ++var6) {
                if(var1.endsWith(File.separator)) {
                    var5 = new File(var1 + var4[var6]);
                } else {
                    var5 = new File(var1 + File.separator + var4[var6]);
                }

                if(var5.isFile()) {
                    FileInputStream var7 = new FileInputStream(var5);
                    FileOutputStream var8 = new FileOutputStream(var2 + "/" + var5.getName().toString());
                    byte[] var9 = new byte[5120];

                    int var10;
                    while((var10 = var7.read(var9)) != -1) {
                        var8.write(var9, 0, var10);
                    }

                    var8.flush();
                    var8.close();
                    var7.close();
                }

                if(var5.isDirectory()) {
                    this.copyFolder(var1 + "/" + var4[var6], var2 + "/" + var4[var6]);
                }
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }

    }
}
