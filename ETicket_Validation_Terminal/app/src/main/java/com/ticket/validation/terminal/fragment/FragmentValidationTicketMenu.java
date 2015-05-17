package com.ticket.validation.terminal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ticket.validation.terminal.R;

/**
 * Created by dengshengjin on 15/5/17.
 */
public class FragmentValidationTicketMenu extends BaseFragment {
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
        return view;
    }

    @Override
    protected void initWidgetActions() {

    }
}
