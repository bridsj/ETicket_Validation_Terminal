package com.zkc.helper;

import android.util.Log;

public class RFIDClass extends SerialPortHelper {
	private static final String TAG = "RFIDClass";
	String choosed_serial = "/dev/ttySAC1";
	int choosed_buad = 9600;
	byte[] data = new byte[0];

	byte[] temp = new byte[1024];
	int tempindex = 0;

	byte[] cmd_init = { (byte) 0xAA, (byte) 0x01, (byte) 0x01, (byte) 0x55 };
	byte[] cmd_init_2 = new byte[] { (byte) 0xAA, (byte) 0x01, (byte) 0x10,
			(byte) 0x55 };
	byte[] cmd_Request = { (byte) 0xAA, (byte) 0x02, (byte) 0x06, (byte) 0x00,
			(byte) 0x55 };

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

	public byte RFID_Init() {
		Write(cmd_init);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			Log.e(TAG, e.getMessage());
		}
		int timeIndex = 0;
		while (timeIndex++ < 500) {
			if ((data.length != 0) && data.length == 4) {
				if (data[2] == 0) {
					break;
				} else {
					return -1;
				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		data = new byte[0];
		Write(cmd_init_2);
		timeIndex = 0;
		while (timeIndex++ < 500) {
			if ((data.length != 0) && data.length == 4) {
				if (data[2] == 0) {
					break;
				} else {
					return -1;
				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		data = new byte[0];
		return 0;
	}

	public byte[] RFID_SearchCard() {
		byte[] cardtype = new byte[0];
		Write(cmd_Request);

		int timeIndex = 0;
		while (timeIndex++ < 500) {
			if (data.length != 0) {
				cardtype = new byte[10];
				for (int i = 0; i < data.length - 3; i++) {
					cardtype[i] = data[i + 2];
				}
				break;
			} else {

			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return cardtype;
	}

	int certificateIndex = 0;

	public byte RFID_Certificate(byte[] KeyData, byte Key, byte Sector) {
		byte[] cmd = new byte[20];
		cmd[0] = (byte) 0xaa;
		cmd[1] = (byte) 0x09;
		cmd[2] = (byte) 0x07;
		for (int i = 3, j = 0; j < 6; i++, j++) {
			cmd[i] = KeyData[j];
		}
		cmd[9] = Key;
		cmd[10] = Sector;
		cmd[11] = (byte) 0x55;
		Write(cmd);
		int timeIndex = 0;
		while (timeIndex++ < 500) {
			if ((data.length != 0) && data.length == 4) {
				if (data[2] == 0) {
					break;
				} else {
					return -1;
				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		certificateIndex = 0;
		data = new byte[0];
		return 0;
	}

	public byte RFID_Write(byte block, byte[] WriteDate) {
		byte[] cmd = new byte[64];
		cmd[0] = (byte) 0xaa;
		cmd[1] = (byte) 0x12;
		cmd[2] = (byte) 0x09;
		cmd[3] = (byte) block;
		for (int i = 0, j = 4; i < 16; i++, j++) {
			if (i < WriteDate.length)
				cmd[j] = (byte) WriteDate[i];
			else {
				cmd[j] = 0;
			}
		}
		cmd[20] = (byte) 0x55;
		Write(cmd);
		int timeIndex = 0;
		while (timeIndex++ < 500) {
			if ((data.length != 0) && data.length == 4) {
				if (data[2] == 0) {
					break;
				} else {
					return -1;
				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		data = new byte[0];
		return 0;
	}

	public byte[] RFID_Read(byte block) {
		byte[] RecvData = new byte[0];

		byte[] cmd = { (byte) 0xaa, (byte) 0x02, (byte) 0x08, (byte) block,
				(byte) 0x55 };
		Write(cmd);
		int timeIndex = 0;
		while (timeIndex++ < 500) {
			if (data.length == 20) {
				RecvData = new byte[16];
				for (int i = 0; i < RecvData.length; i++) {
					RecvData[i] = data[i + 3];
				}
				break;
			} else {

			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		data = new byte[0];
		return RecvData;
	}

	public String getCardType(byte[] g_ucTempbuf) {
		String CardType = "Unkonwn";
		if (g_ucTempbuf.length != 0) {
			if (g_ucTempbuf[0] == 0x44 && g_ucTempbuf[1] == 0) {

				CardType = "Mifare_UltraLight";
			} else if (g_ucTempbuf[0] == 0x4 && g_ucTempbuf[1] == 0) {

				CardType = "Mifare_One(S50)";
			} else if (g_ucTempbuf[0] == 0x2 && g_ucTempbuf[1] == 0) {

				CardType = "Mifare_One(S70)";

			} else if (g_ucTempbuf[0] == 0x8 && g_ucTempbuf[1] == 0) {

				CardType = "Mifare_Pro";
			} else if (g_ucTempbuf[0] == 0x44 && g_ucTempbuf[1] == 3) {

				CardType = "Mifare_DESFire";
			} else {
				CardType = "Unkonwn";
			}
		}
		return CardType;
	}

	public String getCardNumber(byte[] g_ucTempbuf) {
		byte[] cardid = new byte[4];
		for (int i = 0; i < cardid.length; i++) {
			cardid[i] = g_ucTempbuf[i + 2];
		}
		String str = byte2HexStr(cardid, cardid.length);
		return str;
	}

	@Override
	protected void onDataReceived(byte[] buffer, int size) {
		byte[] tempdata = new byte[size];
		System.arraycopy(buffer, 0, tempdata, 0, size);

		if (tempdata.length > 3 && tempdata[0] == (byte) 0xAA) {
			if (tempdata[tempdata.length - 1] == (byte) 0x55) {
				data = tempdata;
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
		Log.i(TAG, str);
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

	/**
	 * byte������ȡint��ֵ��������������(��λ��ǰ����λ�ں�)��˳��
	 * 
	 * @param ary
	 *            byte����
	 * @param offset
	 *            ������ĵ�offsetλ��ʼ
	 * @return int��ֵ
	 */
	public static int bytesToInt(byte[] ary, int offset) {
		int value;
		value = (int) ((ary[offset] & 0xFF) | ((ary[offset + 1] << 8) & 0xFF00)
				| ((ary[offset + 2] << 16) & 0xFF0000) | ((ary[offset + 3] << 24) & 0xFF000000));
		return value;
	}
}
