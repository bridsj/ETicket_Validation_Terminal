package com.ticket.validation.terminal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ticket.validation.terminal.R;
import com.ticket.validation.terminal.model.ReportModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dengshengjin on 15/4/20.
 */
public class ReportAdapter extends BaseAdapter {
    private Context mContext;
    private List<ReportModel> mList;

    public ReportAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 100;
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
            convertView = View.inflate(mContext, R.layout.activity_report_item, null);
            holder.mTitleText = (TextView) convertView.findViewById(R.id.title);
            holder.mDescText = (TextView) convertView.findViewById(R.id.desc);
            holder.mNumText = (TextView) convertView.findViewById(R.id.num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        ReportModel model = mList.get(position);
//        holder.mTitleText.setText(model.title);
//        holder.mDescText.setText(model.desc);
//        holder.mNumText.setText(model.num);
        return convertView;
    }

    final class ViewHolder {
        public TextView mTitleText;
        public TextView mDescText;
        public TextView mNumText;
    }

    public void notifyDataSetChanged(ReportModel model) {
        if (mList == null) {
            mList = new LinkedList<>();
        }
        if (model != null) {
            mList.add(model);
        }
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<ReportModel> list) {
        if (mList == null) {
            mList = new LinkedList<>();
        }

        if (list != null) {
            list.addAll(mList);
        }
        mList = list;
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public List<ReportModel> getList() {
        return mList;
    }
}
