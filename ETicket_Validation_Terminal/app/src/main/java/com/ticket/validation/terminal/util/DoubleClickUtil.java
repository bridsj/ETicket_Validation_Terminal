package com.ticket.validation.terminal.util;

import android.content.Context;

public class DoubleClickUtil extends MutilClickUtil {

    public DoubleClickUtil(Context context, ClickEvent event) {
        super(context, event, 2);
    }

}
