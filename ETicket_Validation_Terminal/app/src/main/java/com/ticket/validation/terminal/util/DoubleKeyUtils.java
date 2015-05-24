package com.ticket.validation.terminal.util;

/**
* @author deng.shengjin
* @version create_time:2014-5-29_下午5:23:54
* @Description 防止点击
*/
public class DoubleKeyUtils {

	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		return isFastDoubleClick(800);
	}

	public static boolean isFastDoubleClick(int duration) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < duration) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
