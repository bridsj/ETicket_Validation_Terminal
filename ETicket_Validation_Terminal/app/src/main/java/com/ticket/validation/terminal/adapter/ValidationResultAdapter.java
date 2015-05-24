package com.ticket.validation.terminal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ticket.validation.terminal.R;
import com.ticket.validation.terminal.model.GoodsModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dengshengjin on 15/4/20.
 */
public class ValidationResultAdapter extends BaseAdapter {
    private Context mContext;
    private List<GoodsModel> mList;

    public ValidationResultAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.validation_result_item, null);
            holder.mTitleText = (TextView) convertView.findViewById(R.id.title);
            holder.mBox = (LinearLayout) convertView.findViewById(R.id.item_box);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GoodsModel model = mList.get(position);
        if (getCount() == 1) {
            model.mIsSelected = true;
            holder.mTitleText.setSelected(true);
            holder.mBox.setBackgroundResource(R.color.black_10_alpha);
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(model);
            }
        } else {
            if (model.mIsSelected) {
                holder.mTitleText.setSelected(true);
                holder.mBox.setBackgroundResource(R.color.black_10_alpha);
            } else {
                holder.mTitleText.setSelected(false);
                holder.mBox.setBackgroundResource(R.color.transparent);
            }
        }
        holder.mTitleText.setText(model.mGoodsName);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.mIsSelected) {
                    return;
                }
                updateDataAndViews(model);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(model);
                }
            }
        });
        return convertView;
    }

    final class ViewHolder {
        public TextView mTitleText;
        public LinearLayout mBox;
    }

    public void notifyDataSetChanged(GoodsModel model) {
        if (mList == null) {
            mList = new LinkedList<>();
        }
        if (model != null) {
            mList.add(model);
        }
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<GoodsModel> list) {
        if (mList == null) {
            mList = new LinkedList<>();
        }

        if (list != null) {
            list.addAll(mList);
        }
        mList = list;
        super.notifyDataSetChanged();
    }


    public List<GoodsModel> getList() {
        return mList;
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(GoodsModel goodsModel);
    }

    private void updateDataAndViews(GoodsModel goodsModel) {
        for (int i = 0, len = mList.size(); i < len; i++) {
            if (goodsModel == mList.get(i)) {
                mList.get(i).mIsSelected = true;
            } else {
                mList.get(i).mIsSelected = false;
            }
            notifyDataSetChanged();
        }
    }

    public void verifySucc(GoodsModel goodsModel, int num) {
        for (int i = 0, len = mList.size(); i < len; i++) {
            if (goodsModel == mList.get(i)) {
                mList.get(i).mCount = mList.get(i).mCount - num;
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(goodsModel);
                }
            }
        }
    }

}
