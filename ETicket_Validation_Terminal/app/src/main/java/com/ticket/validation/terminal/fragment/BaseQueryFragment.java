package com.ticket.validation.terminal.fragment;

import android.os.Handler;
import android.os.Looper;

import com.ticket.validation.terminal.db.CacheDBUtil;
import com.ticket.validation.terminal.model.ErrorModel;
import com.ticket.validation.terminal.model.GoodsModel;
import com.ticket.validation.terminal.parse.GoodsParse;
import com.ticket.validation.terminal.restful.ReqRestAdapter;
import com.ticket.validation.terminal.restful.RestfulRequest;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dengshengjin on 15/5/24.
 */
public abstract class BaseQueryFragment extends BaseFragment {
    protected RestfulRequest mRestfulRequest;
    protected ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    protected Handler mHandler = new Handler();

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.myLooper());
        }
        return mHandler;
    }

    @Override
    protected void initData() {
        mRestfulRequest = ReqRestAdapter.getInstance(getApplicationContext()).create(RestfulRequest.class);
    }

    protected void queryData(String credenceno, final RestfulCallback callback) {
        mRestfulRequest.queryOrderInfo(credenceno, CacheDBUtil.getSessionId(getApplicationContext()), new Callback<JSONObject>() {
            @Override
            public void success(final JSONObject jsonObject, Response response) {
                mExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        final Object object = GoodsParse.parse(jsonObject);
                        if (getActivity() == null || getActivity().isFinishing()) {
                            return;
                        }
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (object == null) {
                                    if (callback != null) {
                                        callback.failureViaServer(null);
                                    }
                                } else {
                                    if (object instanceof ErrorModel) {
                                        if (callback != null) {
                                            callback.failureViaServer((ErrorModel) object);
                                        }
                                    } else if (object instanceof LinkedList) {
                                        if (callback != null) {
                                            callback.success((LinkedList) object);
                                        }
                                    } else {
                                        if (callback != null) {
                                            callback.failureViaServer(null);
                                        }
                                    }
                                }
                            }
                        });

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                if (callback != null) {
                    callback.failureViaLocal();
                }
            }
        });
    }

    public interface RestfulCallback {
        void success(LinkedList<GoodsModel> list);

        void failureViaLocal();

        void failureViaServer(ErrorModel errorModel);

    }
}
