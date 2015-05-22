package com.ticket.validation.terminal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.ticket.validation.terminal.fragment.BaseFragment;
import com.ticket.validation.terminal.fragment.FragmentValidationElectronic;
import com.ticket.validation.terminal.fragment.FragmentValidationIDCard;
import com.ticket.validation.terminal.fragment.FragmentValidationQrCodeForWizarpos;
import com.ticket.validation.terminal.fragment.FragmentValidationTicketMenu;

/**
 * Created by dengshengjin on 15/5/17.
 * 验票
 */
public class ValidationTicketActivity extends BaseActivity {
    private FrameLayout mMenuFrame, mContentFrame;
    private FragmentValidationElectronic mFragmentValidationElectronic;
    private FragmentValidationTicketMenu mFragmentValidationTicketMenu;
    private FragmentValidationIDCard mFragmentValidationIDCard;
    private BaseFragment mFragmentValidationQrCode;
    private final static String GOOGLE = "Google";
    private final static String WIZARPOS = "Wizarpos";
    private String type = WIZARPOS;

    @Override
    protected void initData() {

    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_validation_ticket);
        mMenuFrame = (FrameLayout) findViewById(R.id.menu_frame);
        mContentFrame = (FrameLayout) findViewById(R.id.content_frame);
        mFragmentValidationTicketMenu = FragmentValidationTicketMenu.newInstance();
        initMenuFrame(mFragmentValidationTicketMenu);
        if (mFragmentValidationElectronic == null) {
            mFragmentValidationElectronic = FragmentValidationElectronic.newInstance();
        }
        initContentFrame(mFragmentValidationElectronic);
    }

    @Override
    protected void initWidgetsActions() {
        mFragmentValidationTicketMenu.setOnMenuClickListener(new FragmentValidationTicketMenu.OnMenuClickListener() {
            @Override
            public void onClick(String Tag) {
                if (Tag.equals(FragmentValidationTicketMenu.MenuType.ELECTRONIC.name().toString())) {
                    if (mFragmentValidationElectronic == null) {
                        mFragmentValidationElectronic = FragmentValidationElectronic.newInstance();
                    }
                    initContentFrame(mFragmentValidationElectronic);
                } else if (Tag.equals(FragmentValidationTicketMenu.MenuType.QRCODE.name().toString())) {
                    createQrFragment();
                } else {
                    if (mFragmentValidationIDCard == null) {
                        mFragmentValidationIDCard = FragmentValidationIDCard.newInstance();
                    }
                    initContentFrame(mFragmentValidationIDCard);
                }
            }
        });
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

    private void createQrFragment() {
        if (type.equals(WIZARPOS)) {
            if (mFragmentValidationQrCode == null) {
                mFragmentValidationQrCode = FragmentValidationQrCodeForWizarpos.newInstance();
            }
            initContentFrame(mFragmentValidationQrCode);
        } else if (type.equals(GOOGLE)) {
            if (mFragmentValidationQrCode == null) {
//                mFragmentValidationQrCode = FragmentValidationQrCodeForGoogle.newInstance();
            }
            initContentFrame(mFragmentValidationQrCode);
        }
    }
}
