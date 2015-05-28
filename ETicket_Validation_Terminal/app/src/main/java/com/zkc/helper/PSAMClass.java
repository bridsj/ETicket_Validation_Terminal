package com.zkc.helper;

import android.util.Log;

public class PSAMClass extends SerialPortHelper {
	private static final String TAG = "PSAMClass";
	String choosed_serial = "/dev/ttySAC0";
	int choosed_buad = 9600;

	byte[] data = new byte[0];
	byte[] temp = new byte[1024];
	int tempindex = 0;

	public int cardPosition = 1;

	byte[] cmd_reset_1 = { (byte) 0xaa, (byte) 0x01, (byte) 0x01, (byte) 0x55 };
	byte[] cmd_reset_2 = { (byte) 0xaa, (byte) 0x01, (byte) 0x03, (byte) 0x55 };

	public boolean Open() {
		try {
			if (OpenSerialPort(choosed_serial, choosed_buad)) {
				return true;
			}
		} catch (SecurityException e) {
			Log.e(TAG, e.getMessage());
		}
		return false;
	}

	public boolean Close() {
		if (CloseSerialPort()) {
			return true;
		}
		return false;
	}

	public byte[] PSAM_Reset() {
		byte[] recData = new byte[0];
		byte[] btSend = null;
		if (cardPosition == 1) {
			btSend = cmd_reset_1;
		} else if (cardPosition == 2) {
			btSend = cmd_reset_2;
		}
		Write(btSend);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Write(btSend);

		int timeIndex = 0;
		while (timeIndex++ < 500) {
			if (data.length != 0) {
				recData = new byte[data.length - 3];
				System.arraycopy(data, 2, recData, 0, recData.length);
				break;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		
		data = new byte[0];
		return recData;
	}

	public byte[] PSAM_SendCommand(byte[] btCommand) {
		byte[] recData = new byte[0];
		byte[] btSend = new byte[btCommand.length + 4];
		btSend[0] = (byte) 0xaa;
		btSend[1] = (byte) (btCommand.length + 1);
		if (cardPosition == 1) {
			btSend[2] = 2;
		} else if (cardPosition == 2) {
			btSend[2] = 4;
		}
		System.arraycopy(btCommand, 0, btSend, 3, btCommand.length);
		btSend[btSend.length - 1] = (byte) 0x55;

		Write(btSend);

		int timeIndex = 0;
		while (timeIndex++ < 500) {
			if (data.length != 0) {
				recData = new byte[data.length - 3];
				System.arraycopy(data, 2, recData, 0, recData.length);
				break;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		data = new byte[0];
		return recData;
	}

	@Override
	protected void onDataReceived(byte[] buffer, int size) {
		byte[] tempdata = new byte[size];
		System.arraycopy(buffer, 0, tempdata, 0, size);

		if (tempdata.length > 3 && tempdata[0] == (byte) 0xAA) {
			if (tempdata[tempdata.length - 1] == (byte) 0x55) {
				data=new byte[tempdata.length];
				System.arraycopy(tempdata, 0, data, 0, data.length);
			} else {
				temp = new byte[1024];
				System.arraycopy(tempdata, 0, temp, 0, tempdata.length);
				tempindex = tempdata.length;
			}
		} else if (temp[0] == (byte) 0xAA) {
			System.arraycopy(tempdata, 0, temp, tempindex, tempdata.length);
			if (temp[temp[1] + 2] == (byte) 0x55) {
				data = new byte[temp[1] + 3];
				System.arraycopy(temp, 0, data, 0, data.length);
			}
		} else {
			String str = byte2HexStr(tempdata, tempdata.length);
			Log.i(TAG, str);
		}

		String str = byte2HexStr(data, data.length);
		Log.i(TAG, "onDataReceived:" + str);
	}

	public static String byte2HexStr(byte[] b, int lenth) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < lenth; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
			sb.append(" ");
		}
		return sb.toString().toUpperCase().trim();
	}
}
