package com.ticket.validation.terminal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ticket.validation.terminal.R;
import com.ticket.validation.terminal.model.TicketModel;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dengshengjin on 15/4/20.
 */
public class TicketAdapter extends BaseAdapter {
    private Context mContext;
    private List<TicketModel> mList;

    public TicketAdapter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.activity_query_result_item, null);
            holder.mTitleText = (TextView) convertView.findViewById(R.id.title);
            holder.mDescText = (TextView) convertView.findViewById(R.id.desc);
            holder.mNumText = (TextView) convertView.findViewById(R.id.num);
            holder.mUserText = (TextView) convertView.findViewById(R.id.userId);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TicketModel model = mList.get(position);
        holder.mTitleText.setText(model.mGoodsName);
        holder.mNumText.setText(model.mExchangeCount + "");
        holder.mDescText.setText(getFormatTime(model.mCreationDate));
        holder.mUserText.setText("X");
        return convertView;
    }

    public String getFormatTime(String time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String timeStr = simpleDateFormat.format(time);
            return timeStr;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return "";
    }

    final class ViewHolder {
        public TextView mTitleText;
        public TextView mDescText;
        public TextView mNumText;
        public TextView mUserText;
    }

    public void notifyDataSetChanged(TicketModel model) {
        if (mList == null) {
            mList = new LinkedList<>();
        }
        if (model != null) {
            mList.add(model);
        }
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<TicketModel> list) {
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

    public List<TicketModel> getList() {
        return mList;
    }
}
