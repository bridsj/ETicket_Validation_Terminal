package com.ticket.validation.terminal.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ticket.validation.terminal.R;
import com.zuiapps.suite.utils.device.DeviceUtil;

/**
 * @author deng.shengjin
 * @version create_time:2014-3-17_下午5:51:17
 * @Description toast工具
 */
public class ToastUtil {

    public static void showToast(Context context, int textRes) {
        showToast(context, context.getString(textRes), Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int textRes, int toastDuration) {
        showToast(context, context.getString(textRes), toastDuration);
    }

    public static void showToast(Context context, String text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String text, int duration) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
        TextView textView = (TextView) view.findViewById(R.id.toast_text);
        textView.setText(text);
        Toast toast = new Toast(context);
        if (DeviceUtil.hasSmartBar()) {
            toast.setGravity(Gravity.BOTTOM, 0, 100);
        } else {
            toast.setGravity(Gravity.BOTTOM, 0, 100);
        }

        if (duration < 0) {
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            toast.setDuration(duration);
        }
        toast.setView(view);
        toast.show();

    }

}
