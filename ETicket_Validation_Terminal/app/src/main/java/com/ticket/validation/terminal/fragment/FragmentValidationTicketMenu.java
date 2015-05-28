package com.ticket.validation.terminal.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ticket.validation.terminal.R;
import com.ticket.validation.terminal.db.CacheDBUtil;
import com.ticket.validation.terminal.helper.PrintHelper;
import com.ticket.validation.terminal.helper.PrintStrategy;
import com.ticket.validation.terminal.model.ErrorModel;
import com.ticket.validation.terminal.model.PrintModel;
import com.ticket.validation.terminal.parse.GoodsParse;
import com.ticket.validation.terminal.restful.ApiConstants;
import com.ticket.validation.terminal.restful.ReqRestAdapter;
import com.ticket.validation.terminal.restful.RestfulRequest;
import com.ticket.validation.terminal.util.LoginInterceporUtil;
import com.ticket.validation.terminal.util.ToastUtil;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dengshengjin on 15/5/17.
 */
public class FragmentValidationTicketMenu extends BaseFragment {
    private ViewGroup mElectronicBox, mQrCodeBox, mIdCardBox;
    private TextView mElectronicText, mQrCodeText, mIdCardText;
    private ViewGroup mElectronicBoxBox, mQrCodeBoxBox, mIdCardBoxBox;
    private ViewGroup mRePrintBox, mBackBox;
    private RestfulRequest mRestfulRequest;
    private ProgressBar mProgressBar;
    private ExecutorService mExecutors = Executors.newSingleThreadExecutor();
    private Handler mHandler = new Handler();

    public enum MenuType {
        ELECTRONIC, QRCODE, IDCARD
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    public static FragmentValidationTicketMenu newInstance() {
        FragmentValidationTicketMenu f = new FragmentValidationTicketMenu();
        return f;
    }

    @Override
    protected void initData() {
        mRestfulRequest = ReqRestAdapter.getInstance(getApplicationContext(), ApiConstants.API_BASE_URL).create(RestfulRequest.class);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_validation_ticket_menu, container, false);
        mElectronicBox = (ViewGroup) view.findViewById(R.id.electronic_box);
        mQrCodeBox = (ViewGroup) view.findViewById(R.id.qr_code_box);
        mIdCardBox = (ViewGroup) view.findViewById(R.id.id_card_box);

        mElectronicText = (TextView) view.findViewById(R.id.electronic_text);
        mQrCodeText = (TextView) view.findViewById(R.id.qr_code_text);
        mIdCardText = (TextView) view.findViewById(R.id.id_card_text);

        mElectronicBoxBox = (ViewGroup) view.findViewById(R.id.electronic_box_box);
        mQrCodeBoxBox = (ViewGroup) view.findViewById(R.id.qr_code_box_box);
        mIdCardBoxBox = (ViewGroup) view.findViewById(R.id.id_card_box_box);

        mRePrintBox = (ViewGroup) view.findViewById(R.id.re_print_box);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mBackBox = (ViewGroup) view.findViewById(R.id.back_box);
        mElectronicBox.setTag(MenuType.ELECTRONIC.name().toString());
        mQrCodeBox.setTag(MenuType.QRCODE.name().toString());
        mIdCardBox.setTag(MenuType.IDCARD.name().toString());
        return view;
    }

    @Override
    protected void initWidgetActions() {
        mBackBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mElectronicBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedItem(v);
                if (mOnMenuClickListener != null) {
                    mOnMenuClickListener.onClick((String) v.getTag());
                }
            }
        });
        mElectronicBoxBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mElectronicBox.performClick();
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
        mQrCodeBoxBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQrCodeBox.performClick();
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
        mIdCardBoxBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIdCardBox.performClick();
            }
        });
        mRePrintBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
                    return;
                }
                if (mProgressBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                mRestfulRequest.reprint(CacheDBUtil.getSessionId(getApplicationContext()), new Callback<JSONObject>() {

                    @Override
                    public void success(final JSONObject jsonObject, Response response) {
                        mExecutors.execute(new Runnable() {
                            @Override
                            public void run() {
                                final Object object = GoodsParse.parseRePrint(getApplicationContext(), jsonObject);
                                if (getActivity() == null || getActivity().isFinishing()) {
                                    return;
                                }
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (object == null) {
                                            mProgressBar.setVisibility(View.GONE);
                                            ToastUtil.showToast(getApplicationContext(), R.string.verify_fail);
                                        } else {
                                            if (object instanceof PrintModel) {
                                                final PrintModel model = (PrintModel) object;
                                                PrintHelper.getInstance(getApplicationContext()).startPrintViaChar(model.mPrintStr, new PrintStrategy.PrintCallback() {
                                                    @Override
                                                    public void onErrorPrint() {
                                                        ToastUtil.showToast(getApplicationContext(), R.string.printing);
                                                    }

                                                    @Override
                                                    public void onStartPrint() {
                                                    }

                                                    @Override
                                                    public void onFinishPrint() {
                                                        mProgressBar.setVisibility(View.GONE);
                                                    }

                                                    @Override
                                                    public void onFailPrint() {
                                                        ToastUtil.showToast(getApplicationContext(), R.string.print_fail);
                                                        mProgressBar.setVisibility(View.GONE);
                                                    }
                                                });
                                                return;
                                            } else if (object instanceof ErrorModel) {
                                                mProgressBar.setVisibility(View.GONE);
                                                ToastUtil.showToast(getApplicationContext(), ((ErrorModel) object).mInfo);
                                                return;
                                            } else {
                                                mProgressBar.setVisibility(View.GONE);
                                                ToastUtil.showToast(getApplicationContext(), R.string.verify_fail);
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ToastUtil.showToast(getApplicationContext(), R.string.re_print_fail);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        onSelectedItem(mElectronicBox);
    }

    private void onSelectedItem(View view) {
        if (view == mElectronicBox) {
            mElectronicBox.setSelected(true);
            mQrCodeBox.setSelected(false);
            mIdCardBox.setSelected(false);

            mElectronicText.setSelected(true);
            mQrCodeText.setSelected(false);
            mIdCardText.setSelected(false);
        } else if (view == mQrCodeBox) {
            mElectronicBox.setSelected(false);
            mQrCodeBox.setSelected(true);
            mIdCardBox.setSelected(false);

            mElectronicText.setSelected(false);
            mQrCodeText.setSelected(true);
            mIdCardText.setSelected(false);
        } else {
            mElectronicBox.setSelected(false);
            mQrCodeBox.setSelected(false);
            mIdCardBox.setSelected(true);

            mElectronicText.setSelected(false);
            mQrCodeText.setSelected(false);
            mIdCardText.setSelected(true);
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
