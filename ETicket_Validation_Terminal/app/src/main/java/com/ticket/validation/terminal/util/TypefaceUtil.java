package com.ticket.validation.terminal.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class TypefaceUtil {

    private static final Map<String, Typeface> cache = new HashMap<String, Typeface>();

    private static Typeface get(Context c, String path) {
        synchronized (cache) {
            if (!cache.containsKey(path)) {
                Typeface t = Typeface.createFromAsset(c.getAssets(), path);
                cache.put(path, t);
            }
            return cache.get(path);
        }
    }

    public static void init(Context context) {

    }

    public static Typeface getTypefaceLT(Context context) {
        return get(context, "fonts/fzlt.TTF");
    }


}
