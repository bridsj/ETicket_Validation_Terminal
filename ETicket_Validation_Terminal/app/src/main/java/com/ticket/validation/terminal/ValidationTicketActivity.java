package com.ticket.validation.terminal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.ticket.validation.terminal.fragment.FragmentValidationElectronic;
import com.ticket.validation.terminal.fragment.FragmentValidationTicketMenu;

/**
 * Created by dengshengjin on 15/5/17.
 * 验票
 */
public class ValidationTicketActivity extends BaseActivity {
    private FrameLayout mMenuFrame, mContentFrame;

    @Override
    protected void initData() {

    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_validation_ticket);
        mMenuFrame = (FrameLayout) findViewById(R.id.menu_frame);
        mContentFrame = (FrameLayout) findViewById(R.id.content_frame);
        initMenuFrame(FragmentValidationTicketMenu.newInstance());
        initContentFrame(FragmentValidationElectronic.newInstance());
    }

    @Override
    protected void initWidgetsActions() {

    }

    private void initMenuFrame(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.menu_frame, fragment);
        ft.commit();
    }

    private void initContentFrame(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }
}
