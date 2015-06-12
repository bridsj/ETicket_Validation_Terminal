package com.ticket.validation.terminal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zuiapps.suite.utils.app.AppUtil;

/**
 * Created by dengshengjin on 15/6/12.
 * 开机自启动
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent restartIntent = AppUtil.getMainIntent(context, context.getPackageName());
            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(restartIntent);
        }
    }

}