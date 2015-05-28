package com.ticket.validation.terminal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ticket.validation.terminal.adapter.ValidationResultAdapter;
import com.ticket.validation.terminal.db.CacheDBUtil;
import com.ticket.validation.terminal.helper.PrintHelper;
import com.ticket.validation.terminal.helper.PrintStrategy;
import com.ticket.validation.terminal.model.ErrorModel;
import com.ticket.validation.terminal.model.GoodsModel;
import com.ticket.validation.terminal.model.PrintModel;
import com.ticket.validation.terminal.parse.GoodsParse;
import com.ticket.validation.terminal.restful.ApiConstants;
import com.ticket.validation.terminal.restful.RestfulRequest;
import com.ticket.validation.terminal.restful.VerifyReqRestAdapter;
import com.ticket.validation.terminal.util.KeyCodeUtil;
import com.ticket.validation.terminal.util.LoginInterceporUtil;
import com.ticket.validation.terminal.util.ToastUtil;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dengshengjin on 15/5/23.
 */
public class ValidationResultActivity extends BaseUserActivity {
    private ViewGroup mBackBox;
    private ListView mListView;
    private ValidationResultAdapter mAdapter;
    private Handler mHandler;
    private TextView mEmptyView;
    public final static String MODELS = "MODELS";
    private List<GoodsModel> goodsList;
    private Executor mExecutor = Executors.newCachedThreadPool();

    private TextView mNumText;
    private TextView mDeleteImg, mAddImg;
    private EditText mInputText;
    private TextView mNameText, mIdCardText, mValidityText, mMarkText;
    private GoodsModel mGoodsModel;

    private ViewGroup mVerifyBox;
    private ProgressBar mProgressBar;

    private RestfulRequest mRestfulRequest;

    @Override
    protected void initData() {
        mAdapter = new ValidationResultAdapter(getContext());
        goodsList = (List<GoodsModel>) getIntent().getSerializableExtra(MODELS);
        if (goodsList == null) {
            goodsList = new LinkedList<>();
        }
        reqCount = 0;
        mRestfulRequest = VerifyReqRestAdapter.getInstance(getApplicationContext(), ApiConstants.API_BASE_URL).create(RestfulRequest.class);
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_validation_result);
        super.initWidgets();
        mListView = (ListView) findViewById(R.id.list_view);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mBackBox = (ViewGroup) findViewById(R.id.back_box);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyView);

        mNumText = (TextView) findViewById(R.id.num_text);
        mNumText.setText("0");
        mDeleteImg = (TextView) findViewById(R.id.delete_img);
        mAddImg = (TextView) findViewById(R.id.add_img);
        mInputText = (EditText) findViewById(R.id.input_text);
        mNameText = (TextView) findViewById(R.id.name_text);
        mIdCardText = (TextView) findViewById(R.id.id_card_text);
        mValidityText = (TextView) findViewById(R.id.validity_text);
        mMarkText = (TextView) findViewById(R.id.mark_text);

        mVerifyBox = (ViewGroup) findViewById(R.id.verify_box);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void initWidgetsActions() {
        mAdapter.setOnItemClickListener(new ValidationResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GoodsModel goodsModel) {
                mGoodsModel = goodsModel;

                mNumText.setText(goodsModel.mCount + "");
                mInputText.setText("0");
                mNameText.setText(goodsModel.mUserName);
                mIdCardText.setText(goodsModel.mIdCard);
                mValidityText.setText(goodsModel.mEndTime);
                mMarkText.setText(goodsModel.mOrderCommments);
            }
        });
        mDeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoodsModel == null) {
                    ToastUtil.showToast(getApplicationContext(), R.string.validation_result_error);
                    return;
                }
                int num = 0;
                try {
                    num = Integer.parseInt(mInputText.getText().toString());
                } catch (Throwable t) {

                }
                num = num - 1;
                if (num < 0) {
                    num = 0;
                }
                mInputText.setText(num + "");
            }
        });
        mAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoodsModel == null) {
                    ToastUtil.showToast(getApplicationContext(), R.string.validation_result_error);
                    return;
                }
                int num = 0;
                try {
                    num = Integer.parseInt(mInputText.getText().toString());
                } catch (Throwable t) {

                }
                num = num + 1;
                if (num > mGoodsModel.mCount) {
                    num = mGoodsModel.mCount;
                }
                mInputText.setText(num + "");
            }
        });
        mInputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mInputText.getWindowToken(), 0);
            }
        });
        mVerifyBox.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
                                                  return;
                                              }
                                              if (mGoodsModel == null) {
                                                  ToastUtil.showToast(getApplicationContext(), R.string.validation_result_error);
                                                  return;
                                              }
                                              if (mProgressBar.getVisibility() == View.VISIBLE) {
                                                  return;
                                              }


                                              int num = 0;
                                              try {
                                                  num = Integer.parseInt(mInputText.getText().toString());
                                              } catch (Throwable t) {

                                              }
                                              if (num == 0) {
                                                  ToastUtil.showToast(getApplicationContext(), R.string.verify_input_fail);
                                                  return;
                                              } else if (num > mGoodsModel.mCount) {
                                                  ToastUtil.showToast(getApplicationContext(), R.string.verify_input_fail);
                                                  return;
                                              }

                                              reqCount = 0;
                                              mProgressBar.setVisibility(View.VISIBLE);
                                              loadData(System.currentTimeMillis() + "", num);
                                          }
                                      }

        );
        mBackBox.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                        }
                                    }

        );
        mExecutor.execute(new Runnable() {
                              @Override
                              public void run() {

                                  getHandler().post(new Runnable() {
                                      @Override
                                      public void run() {
                                          mAdapter.notifyDataSetChanged(goodsList);
                                      }
                                  });

                              }
                          }

        );
    }

    private int reqCount;
    private int maxCount = 2;

    private void loadData(final String outId, final int num) {
        mRestfulRequest.exchangefunc(mGoodsModel.mExchageFunc,
                outId,
                mGoodsModel.mSoldGoodsId,
                num,
                CacheDBUtil.getSessionId(getApplicationContext()), 0, new Callback<JSONObject>() {
                    @Override
                    public void success(final JSONObject jsonObject, Response response) {
                        mExecutor.execute(new Runnable() {
                                              @Override
                                              public void run() {
                                                  final Object object = GoodsParse.parseVerify(getApplicationContext(), jsonObject);
                                                  if (isFinishing()) {
                                                      return;
                                                  }
                                                  getHandler().post(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                          if (object == null) {
                                                              if (reqCount < maxCount) {
                                                                  reqCount = reqCount + 1;
                                                                  loadData(outId, num);
                                                              } else {
                                                                  mProgressBar.setVisibility(View.GONE);
                                                                  ToastUtil.showToast(getApplicationContext(), R.string.verify_fail);
                                                              }
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
                                                                          ToastUtil.showToast(getApplicationContext(), String.format(getString(R.string.verify_succ), num));
                                                                          mProgressBar.setVisibility(View.GONE);
                                                                          mAdapter.verifySucc(mGoodsModel, num);
                                                                      }

                                                                      @Override
                                                                      public void onFailPrint() {
                                                                          ToastUtil.showToast(getApplicationContext(), R.string.print_fail);
                                                                          mAdapter.verifySucc(mGoodsModel, num);
                                                                          mProgressBar.setVisibility(View.GONE);
                                                                      }
                                                                  });
                                                                  return;
                                                              } else if (object instanceof ErrorModel) {
                                                                  mProgressBar.setVisibility(View.GONE);
                                                                  ToastUtil.showToast(getApplicationContext(), ((ErrorModel) object).mInfo);
                                                                  return;
                                                              } else {
                                                                  if (reqCount < maxCount) {
                                                                      reqCount = reqCount + 1;
                                                                      loadData(outId, num);
                                                                  } else {
                                                                      mProgressBar.setVisibility(View.GONE);
                                                                      ToastUtil.showToast(getApplicationContext(), R.string.verify_fail);
                                                                  }
                                                              }
                                                          }
                                                      }
                                                  });

                                              }
                                          }

                        );

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (reqCount < maxCount) {
                            reqCount = reqCount + 1;
                            loadData(outId, num);
                        } else {
                            mProgressBar.setVisibility(View.GONE);
                            ToastUtil.showToast(getApplicationContext(), R.string.verify_fail);
                        }
                    }
                }

        );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String key = KeyCodeUtil.getKeyCode(keyCode, true);
        if (!TextUtils.isEmpty(key)) {
            if (key.equals("del")) {
                onDelEvent();
            } else {
                onAddEvent(key);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onDelEvent() {
        if (mGoodsModel == null) {
            return;
        }
        Editable editable = mInputText.getText();
        int start = mInputText.getSelectionStart();
        if (editable != null && editable.length() > 0) {
            if (start > 0) {
                editable.delete(start - 1, start);
            }
        } else if (editable.length() == 0) {
            mInputText.setText("");
            mInputText.setSelection(0);
        }
    }

    private void onAddEvent(String key) {
        if (mGoodsModel == null) {
            return;
        }
        String text = mInputText.getText().toString();
        if (text.equals("0")) {
            mInputText.setText(key);
        } else {
            mInputText.setText(text + key);
            mInputText.setSelection(text.length() + 1);
        }

    }
}
