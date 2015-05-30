package com.ticket.validation.terminal.util;

import android.os.Build;

/**
 * Created by dengshengjin on 15/5/30.
 */
public class DeviceTypeUtil {
    public static boolean isHaoDeXin() {
        if ("hdx".equals(Build.MODEL)) {
            return true;
        }
        return false;
    }

}
