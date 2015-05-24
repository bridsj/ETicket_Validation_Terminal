package com.ticket.validation.terminal.util;

import android.content.Context;
import android.text.TextUtils;

import com.ticket.validation.terminal.R;
import com.ticket.validation.terminal.db.CacheDBUtil;

/**
 * Created by dengshengjin on 15/5/24.
 */
public class LoginInterceporUtil {
    public static boolean pauseRedirect(Context mContext) {
        if (TextUtils.isEmpty(CacheDBUtil.getSessionId(mContext))) {
            ToastUtil.showToast(mContext, R.string.un_login);
            return true;
        }
        return false;
    }
}
