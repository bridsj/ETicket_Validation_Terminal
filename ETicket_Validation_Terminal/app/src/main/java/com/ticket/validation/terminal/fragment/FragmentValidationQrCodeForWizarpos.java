//package com.ticket.validation.terminal.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.ticket.validation.terminal.R;
//import com.ticket.validation.terminal.ValidationResultActivity;
//import com.ticket.validation.terminal.model.ErrorModel;
//import com.ticket.validation.terminal.model.GoodsModel;
//import com.ticket.validation.terminal.util.LoginInterceporUtil;
//import com.ticket.validation.terminal.util.ToastUtil;
//import com.wizarpos.barcode.scanner.IScanEvent;
//import com.wizarpos.barcode.scanner.ScannerRelativeLayout;
//import com.wizarpos.barcode.scanner.ScannerResult;
//import com.zuiapps.suite.utils.device.DoubleKeyUtils;
//
//import java.util.LinkedList;
//
///**
// * Created by dengshengjin on 15/5/21.
// */
//public class FragmentValidationQrCodeForWizarpos extends BaseQueryFragment {
//    private View mContentView;
//    private ScannerRelativeLayout mScanner;
//    private IScanEvent mIScanSuccessListener;
//
//    private ViewGroup mVerifyBox;
//    private TextView mStatusText;
//    private String test = "124569874566325541";
//    private boolean mIsOnPause;
//
//    public static FragmentValidationQrCodeForWizarpos newInstance() {
//        FragmentValidationQrCodeForWizarpos f = new FragmentValidationQrCodeForWizarpos();
//        return f;
//    }
//
//    @Override
//    protected void initData() {
//        super.initData();
//    }
//
//    @Override
//    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mContentView = inflater.inflate(R.layout.fragment_validation_ticket_qr_code_for_wizarpos, container, false);
//        mScanner = (ScannerRelativeLayout) mContentView.findViewById(R.id.scanner_box);
//        mIScanSuccessListener = new ScanSuccesListener();
//
//        mScanner.setFrontFacingCamera(true);
//
//        mScanner.setEncodeFormat("CODE_128");
//        mScanner.setScanSuccessListener(mIScanSuccessListener);
//        mScanner.startScan();
//
//        mVerifyBox = (ViewGroup) mContentView.findViewById(R.id.verify_box);
//        mVerifyBox.setVisibility(View.GONE);
//        mStatusText = (TextView) mContentView.findViewById(R.id.status_text);
//        mStatusText.setText("");
//        return mContentView;
//    }
//
//    @Override
//    public void onResume() {
//        mIsOnPause = false;
//        mScanner.onResume();
//        super.onResume();
//        mScanner.setScanSuccessListener(mIScanSuccessListener);
//        mScanner.startScan();
//        getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mScanner.startScan();
//            }
//        }, 1000);
//    }
//
//    @Override
//    public void onPause() {
//        mIsOnPause = true;
//        mScanner.onPause();
//        super.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mScanner.stopScan();
//    }
//
//    @Override
//    protected void initWidgetActions() {
//
//    }
//
//    private class ScanSuccesListener extends IScanEvent {
//
//        @Override
//        public void scanCompleted(ScannerResult scannerResult) {
//            if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
//                return;
//            }
//            if (mIsOnPause) {
//                return;
//            }
//            if (DoubleKeyUtils.isFastDoubleClick(1000)) {
//                return;
//            }
//            if (mVerifyBox.getVisibility() == View.VISIBLE) {
//                return;
//            }
//            if (getActivity() == null || getActivity().isFinishing()) {
//                return;
//            }
//            mVerifyBox.setVisibility(View.VISIBLE);
//            mStatusText.setText(String.format(getString(R.string.validation_qr_code_result), scannerResult.getResult()));
//
//            queryData(test, new BaseQueryFragment.RestfulCallback() {
//                @Override
//                public void success(LinkedList<GoodsModel> list) {
//                    mVerifyBox.setVisibility(View.GONE);
//                    if (list.isEmpty()) {
//                        ToastUtil.showToast(getApplicationContext(), R.string.loading_empty_data);
//                        return;
//                    }
//                    Intent intent = new Intent(getActivity(), ValidationResultActivity.class);
//                    intent.putExtra(ValidationResultActivity.MODELS, list);
//                    startActivity(intent);
//                }
//
//                @Override
//                public void failureViaLocal() {
//                    mVerifyBox.setVisibility(View.GONE);
//                    ToastUtil.showToast(getApplicationContext(), R.string.loading_fail2);
//                }
//
//                @Override
//                public void failureViaServer(ErrorModel errorModel) {
//                    mVerifyBox.setVisibility(View.GONE);
//                    if (errorModel == null) {
//                        ToastUtil.showToast(getApplicationContext(), R.string.loading_fail);
//                        return;
//                    }
//                    ToastUtil.showToast(getApplicationContext(), errorModel.mInfo);
//                }
//            });
//
//        }
//    }
//}
