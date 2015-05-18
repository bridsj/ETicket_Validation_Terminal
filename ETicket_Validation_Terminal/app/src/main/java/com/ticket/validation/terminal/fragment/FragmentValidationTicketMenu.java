package com.ticket.validation.terminal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ticket.validation.terminal.R;

/**
 * Created by dengshengjin on 15/5/17.
 */
public class FragmentValidationTicketMenu extends BaseFragment {
    private LinearLayout mElectronicBox, mQrCodeBox, mIdCardBox;

    public enum MenuType {
        ELECTRONIC, QRCODE, IDCARD
    }

    public static FragmentValidationTicketMenu newInstance() {
        FragmentValidationTicketMenu f = new FragmentValidationTicketMenu();
        return f;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_validation_ticket_menu, container, false);
        mElectronicBox = (LinearLayout) view.findViewById(R.id.electronic_box);
        mQrCodeBox = (LinearLayout) view.findViewById(R.id.qr_code_box);
        mIdCardBox = (LinearLayout) view.findViewById(R.id.id_card_box);
        mElectronicBox.setTag(MenuType.ELECTRONIC.name().toString());
        mQrCodeBox.setTag(MenuType.QRCODE.name().toString());
        mIdCardBox.setTag(MenuType.IDCARD.name().toString());
        return view;
    }

    @Override
    protected void initWidgetActions() {
        mElectronicBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedItem(v);
                if (mOnMenuClickListener != null) {
                    mOnMenuClickListener.onClick((String) v.getTag());
                }
            }
        });
        mQrCodeBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedItem(v);
                if (mOnMenuClickListener != null) {
                    mOnMenuClickListener.onClick((String) v.getTag());
                }
            }
        });
        mIdCardBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedItem(v);
                if (mOnMenuClickListener != null) {
                    mOnMenuClickListener.onClick((String) v.getTag());
                }
            }
        });
        onSelectedItem(mElectronicBox);
    }

    private void onSelectedItem(View view) {
        if (view == mElectronicBox) {
            mElectronicBox.setSelected(true);
            mQrCodeBox.setSelected(false);
            mIdCardBox.setSelected(false);
        } else if (view == mQrCodeBox) {
            mElectronicBox.setSelected(false);
            mQrCodeBox.setSelected(true);
            mIdCardBox.setSelected(false);
        } else {
            mElectronicBox.setSelected(false);
            mQrCodeBox.setSelected(false);
            mIdCardBox.setSelected(true);
        }
    }

    private OnMenuClickListener mOnMenuClickListener;

    public void setOnMenuClickListener(OnMenuClickListener mOnMenuClickListener) {
        this.mOnMenuClickListener = mOnMenuClickListener;
    }

    public interface OnMenuClickListener {
        void onClick(String Tag);
    }
}
