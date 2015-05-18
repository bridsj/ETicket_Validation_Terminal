package com.google.zxing.client.android;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Dsj
 * @version create time:2013-12-16_下午3:06:59
 * @Description TODO
 */
public class CacheServiceHelper {
	private static final String IS_PORTRAIT = "isPortrait";
	private static final String DNS_PREFS_FILE = "dns_qrcode.prefs";

	@SuppressWarnings("deprecation")
	public static void saveLandscape(Context context, boolean isLandscape) {
		SharedPreferences prefs = context.getSharedPreferences(DNS_PREFS_FILE, Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(IS_PORTRAIT, isLandscape);
		editor.commit();
	}

	@SuppressWarnings("deprecation")
	public static boolean isLandscape(Context context) {// true,为横屏,false为竖
		SharedPreferences prefs = context.getSharedPreferences(DNS_PREFS_FILE, Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		return prefs.getBoolean(IS_PORTRAIT, false);
	}
}
