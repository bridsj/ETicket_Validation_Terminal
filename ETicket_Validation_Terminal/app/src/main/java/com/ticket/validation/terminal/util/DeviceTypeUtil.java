package com.ticket.validation.terminal.util;

import android.os.Build;
import android.text.TextUtils;

/**
 * Created by dengshengjin on 15/5/30.
 */
public class DeviceTypeUtil {
    public static boolean isHaoDeXin() {
        if (!TextUtils.isEmpty(Build.MODEL) && Build.MODEL.toLowerCase().contains("hdx")) {
            return true;
        }
        return false;
    }

    public static boolean isGuZhiLian() {
        if (!TextUtils.isEmpty(Build.MODEL) && Build.MODEL.toLowerCase().contains("x210")) {
            return true;
        }
        return false;
    }

    public static boolean isWizarpos(){
        if (!TextUtils.isEmpty(Build.MODEL) && Build.MODEL.toLowerCase().contains("wizarpos")) {
            return true;
        }
        return false;
    }
}
