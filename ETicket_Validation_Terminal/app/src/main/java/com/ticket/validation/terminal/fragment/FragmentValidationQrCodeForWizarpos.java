package com.ticket.validation.terminal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ticket.validation.terminal.R;
import com.wizarpos.barcode.scanner.IScanEvent;
import com.wizarpos.barcode.scanner.ScannerRelativeLayout;
import com.wizarpos.barcode.scanner.ScannerResult;

/**
 * Created by dengshengjin on 15/5/21.
 */
public class FragmentValidationQrCodeForWizarpos extends BaseFragment {
    private View mContentView;
    private ScannerRelativeLayout mScanner;
    private IScanEvent mIScanSuccessListener;

    public static FragmentValidationQrCodeForWizarpos newInstance() {
        FragmentValidationQrCodeForWizarpos f = new FragmentValidationQrCodeForWizarpos();
        return f;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_validation_ticket_qr_code_for_wizarpos, container, false);
        mScanner = (ScannerRelativeLayout) mContentView.findViewById(R.id.scanner_box);
        mIScanSuccessListener = new ScanSuccesListener();

        mScanner.setFrontFacingCamera(true);

        mScanner.setEncodeFormat("CODE_128");
        mScanner.setScanSuccessListener(mIScanSuccessListener);
        mScanner.startScan();
        return mContentView;
    }

    @Override
    public void onResume() {
        mScanner.onResume();
        super.onResume();
        mScanner.setScanSuccessListener(mIScanSuccessListener);
        mScanner.startScan();
        new Thread() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mScanner.startScan();
            }
        }.start();
    }

    @Override
    public void onPause() {
        mScanner.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScanner.stopScan();
    }

    @Override
    protected void initWidgetActions() {

    }

    private class ScanSuccesListener extends IScanEvent {

        @Override
        public void scanCompleted(ScannerResult scannerResult) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
