package com.ticket.validation.terminal.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ticket.validation.terminal.util.TypefaceUtil;

public class TVTextView extends TextView {

    public TVTextView(Context context) {
        super(context);
        init();
    }

    public TVTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TVTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setTypeface(TypefaceUtil.getTypefaceLT(getContext()));
    }

}
