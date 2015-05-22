package com.ticket.validation.terminal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ticket.validation.terminal.R;

/**
 * Created by dengshengjin on 15/5/21.
 */
public class FragmentValidationIDCard extends BaseFragment {
    private View mContentView;
    public static FragmentValidationIDCard newInstance() {
        FragmentValidationIDCard f = new FragmentValidationIDCard();
        return f;
    }
    @Override
    protected void initData() {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_validation_ticket_id_card, container, false);
        return mContentView;
    }

    @Override
    protected void initWidgetActions() {

    }
}
