package com.ticket.validation.terminal.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author deng.shengjin
 * @version create_time:2014-3-11_上午11:52:33
 * @Description 父类
 */
public abstract class BaseFragment extends Fragment {
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = getActivity().getApplicationContext();
		initData();
		View view = initViews(inflater, container, savedInstanceState);
		initWidgetActions();
		return view;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	protected Context getApplicationContext() {
		return context;
	}

	protected abstract void initData();

	protected abstract View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	protected abstract void initWidgetActions();

}
