//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wizarpos.barcode.scanner;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import java.util.ArrayList;

public final class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    public static final String KEY_DECODE_1D = "preferences_decode_1D";
    public static final String KEY_DECODE_QR = "preferences_decode_QR";
    public static final String KEY_DECODE_DATA_MATRIX = "preferences_decode_Data_Matrix";
    public static final String KEY_CUSTOM_PRODUCT_SEARCH = "preferences_custom_product_search";
    public static final String KEY_REVERSE_IMAGE = "preferences_reverse_image";
    public static final String KEY_PLAY_BEEP = "preferences_play_beep";
    public static final String KEY_VIBRATE = "preferences_vibrate";
    public static final String KEY_COPY_TO_CLIPBOARD = "preferences_copy_to_clipboard";
    public static final String KEY_FRONT_LIGHT = "preferences_front_light";
    public static final String KEY_BULK_MODE = "preferences_bulk_mode";
    public static final String KEY_REMEMBER_DUPLICATES = "preferences_remember_duplicates";
    public static final String KEY_SUPPLEMENTAL = "preferences_supplemental";
    public static final String KEY_SEARCH_COUNTRY = "preferences_search_country";
    public static final String KEY_CAMERA_FACING = "preferences_camera_faceing";
    public static final String KEY_HELP_VERSION_SHOWN = "preferences_help_version_shown";
    private CheckBoxPreference decode1D;
    private CheckBoxPreference decodeQR;
    private CheckBoxPreference decodeDataMatrix;

    public PreferencesActivity() {
    }

    protected void onCreate(Bundle var1) {
        super.onCreate(var1);
        PreferenceScreen var2 = this.getPreferenceScreen();
        var2.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        this.decode1D = (CheckBoxPreference)var2.findPreference("preferences_decode_1D");
        this.decodeQR = (CheckBoxPreference)var2.findPreference("preferences_decode_QR");
        this.decodeDataMatrix = (CheckBoxPreference)var2.findPreference("preferences_decode_Data_Matrix");
        this.disableLastCheckedPref();
    }

    public void onSharedPreferenceChanged(SharedPreferences var1, String var2) {
        this.disableLastCheckedPref();
    }

    private void disableLastCheckedPref() {
        ArrayList var1 = new ArrayList(3);
        if(this.decode1D.isChecked()) {
            var1.add(this.decode1D);
        }

        if(this.decodeQR.isChecked()) {
            var1.add(this.decodeQR);
        }

        if(this.decodeDataMatrix.isChecked()) {
            var1.add(this.decodeDataMatrix);
        }

        boolean var2 = var1.size() < 2;
        CheckBoxPreference[] var3 = new CheckBoxPreference[]{this.decode1D, this.decodeQR, this.decodeDataMatrix};
        CheckBoxPreference[] var4 = var3;
        int var5 = var3.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            CheckBoxPreference var7 = var4[var6];
            var7.setEnabled(!var2 || !var1.contains(var7));
        }

    }
}
