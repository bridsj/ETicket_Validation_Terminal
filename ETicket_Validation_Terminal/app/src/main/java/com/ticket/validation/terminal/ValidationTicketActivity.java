package com.ticket.validation.terminal;

import android.graphics.PixelFormat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.ticket.validation.terminal.fragment.BaseFragment;
import com.ticket.validation.terminal.fragment.FragmentValidationElectronic;
import com.ticket.validation.terminal.fragment.FragmentValidationIDCard;
import com.ticket.validation.terminal.fragment.FragmentValidationTicketMenu;
import com.ticket.validation.terminal.fragment.qrcode.FragmentValidationQrCodeForGoogle;
import com.ticket.validation.terminal.util.DeviceTypeUtil;

/**
 * Created by dengshengjin on 15/5/17.
 * 验票
 */
public class ValidationTicketActivity extends BaseUserActivity {
    private FrameLayout mMenuFrame, mContentFrame;
    private FragmentValidationElectronic mFragmentValidationElectronic;
    private FragmentValidationTicketMenu mFragmentValidationTicketMenu;
    private FragmentValidationIDCard mFragmentValidationIDCard;
    private BaseFragment mFragmentValidationQrCode;

    private String currFragment = "";
    private boolean mIsOpenLight;

    @Override
    protected void initData() {
        mIsOpenLight = true;
    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_validation_ticket);
        super.initWidgets();
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mMenuFrame = (FrameLayout) findViewById(R.id.menu_frame);
        mContentFrame = (FrameLayout) findViewById(R.id.content_frame);
        mFragmentValidationTicketMenu = FragmentValidationTicketMenu.newInstance();
        popBackStack();
        initMenuFrame(mFragmentValidationTicketMenu);
        mFragmentValidationElectronic = FragmentValidationElectronic.newInstance();
        initContentFrame(mFragmentValidationElectronic);
        currFragment = "ValidationElectronic";
    }

    @Override
    protected void initWidgetsActions() {
        mFragmentValidationTicketMenu.setOnMenuClickListener(new FragmentValidationTicketMenu.OnMenuClickListener() {
            @Override
            public void onClick(String Tag) {
                if (Tag.equals(FragmentValidationTicketMenu.MenuType.ELECTRONIC.name().toString())) {
                    mFragmentValidationElectronic = FragmentValidationElectronic.newInstance();
                    initContentFrame(mFragmentValidationElectronic);
                    currFragment = "ValidationElectronic";
                } else if (Tag.equals(FragmentValidationTicketMenu.MenuType.QRCODE.name().toString())) {
                    createQrFragment();
                    currFragment = "ValidationQrCode";
                } else {
                    mFragmentValidationIDCard = FragmentValidationIDCard.newInstance();
                    initContentFrame(mFragmentValidationIDCard);
                    currFragment = "ValidationIDCard";
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
        if (DeviceTypeUtil.isHaoDeXin()) {
            initValidationForGoogle();
        } else if (DeviceTypeUtil.isWizarpos()) {
//            mFragmentValidationQrCode = FragmentValidationQrCodeForWizarpos.newInstance();
//            ((FragmentValidationQrCodeForWizarpos) mFragmentValidationQrCode).setOnLightClickListener(new FragmentValidationQrCodeForWizarpos.OnLightClickListener() {
//                @Override
//                public void onChangeLight(boolean isOpenLight) {
//                    mIsOpenLight = isOpenLight;
//                }
//            });
//            initContentFrame(mFragmentValidationQrCode);
        } else {
           initValidationForGoogle();
        }
    }

    private void initValidationForGoogle(){
        mFragmentValidationQrCode = FragmentValidationQrCodeForGoogle.newInstance();
            ((FragmentValidationQrCodeForGoogle) mFragmentValidationQrCode).setOnLightClickListener(new FragmentValidationQrCodeForGoogle.OnLightClickListener() {
                @Override
                public void onChangeLight(boolean isOpenLight) {
                    mIsOpenLight = isOpenLight;
                }
            });
            initContentFrame(mFragmentValidationQrCode);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (currFragment.equals("ValidationElectronic")) {
            if (mFragmentValidationElectronic != null) {
                mFragmentValidationElectronic.dispatchKeyEvent(event);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (currFragment.equals("ValidationElectronic")) {
            if (mFragmentValidationElectronic != null) {
                mFragmentValidationElectronic.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean isOpenLight() {
        return mIsOpenLight;
    }

    private void popBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        int backStackCount = manager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            int backStackId = manager.getBackStackEntryAt(i).getId();
            manager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
