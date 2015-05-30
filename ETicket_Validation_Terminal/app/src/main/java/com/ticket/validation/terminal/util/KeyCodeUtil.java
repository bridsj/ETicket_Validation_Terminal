package com.ticket.validation.terminal.util;

import android.view.KeyEvent;

/**
 * Created by dengshengjin on 15/5/24.
 */
public class KeyCodeUtil {

    public static boolean isEnterKeyCode(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            return true;
        }
        return false;
    }

    public static String getKeyCode(int keyCode, KeyEvent keyEvent, boolean isOnlyNum) {
        if (keyCode == KeyEvent.KEYCODE_0) {
            return "0";
        } else if (keyCode == KeyEvent.KEYCODE_1) {
            return "1";
        } else if (keyCode == KeyEvent.KEYCODE_2) {
            return "2";
        } else if (keyCode == KeyEvent.KEYCODE_3) {
            return "3";
        } else if (keyCode == KeyEvent.KEYCODE_4) {
            return "4";
        } else if (keyCode == KeyEvent.KEYCODE_5) {
            return "5";
        } else if (keyCode == KeyEvent.KEYCODE_6) {
            return "6";
        } else if (keyCode == KeyEvent.KEYCODE_7) {
            return "7";
        } else if (keyCode == KeyEvent.KEYCODE_8) {
            return "8";
        } else if (keyCode == KeyEvent.KEYCODE_9) {
            return "9";
        } else if (keyCode == KeyEvent.KEYCODE_DEL) {
            return "del";
        } else if (!isOnlyNum && keyCode == KeyEvent.KEYCODE_X) {
            return "X";
        }
        return null;
    }
}
