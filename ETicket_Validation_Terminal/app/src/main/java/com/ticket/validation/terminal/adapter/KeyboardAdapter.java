package com.ticket.validation.terminal.adapter;

import android.content.Context;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ticket.validation.terminal.R;
import com.ticket.validation.terminal.util.DoubleClickUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * @author deng.shengjin
 * @version create_time:2014-3-11_下午3:42:37
 * @Description 导航adapter
 */
public class KeyboardAdapter extends BaseAdapter {
    @SuppressWarnings("unused")
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> strs;
    private DoubleClickUtil mSmartStartDoubleClickUtil;

    public KeyboardAdapter(Context context) {
        super();
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        init();
        initData();
    }

    private void init() {
        strs = new LinkedList<>();
        strs.add("1");
        strs.add("2");
        strs.add("3");
        strs.add("del");

        strs.add("4");
        strs.add("5");
        strs.add("6");
        strs.add("0");

        strs.add("7");
        strs.add("8");
        strs.add("9");
        strs.add("X");
    }

    private void initData() {
        mSmartStartDoubleClickUtil = new DoubleClickUtil(mContext, new DoubleClickUtil.ClickEvent() {

            @Override
            public void performClickEvent(View v, PointF eventPoint) {
            }
        });
        mSmartStartDoubleClickUtil.setOnLongClickEvent(new DoubleClickUtil.ClickEvent() {

            @Override
            public void performClickEvent(View v, PointF eventPoint) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onLongClick(v.getTag().toString());
                }
            }
        });
    }

    @Override
    public int getCount() {
        if (strs == null) {
            return 0;
        }
        return strs.size();
    }

    @Override
    public Object getItem(int position) {
        return strs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_keyboard_item, parent, false);
            holder = new ViewHolder();
            initHolder(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder == null) {
            convertView = mInflater.inflate(R.layout.activity_keyboard_item, parent, false);
            holder = new ViewHolder();
            initHolder(holder, convertView);
            convertView.setTag(holder);
        }
        final String str = strs.get(position);
        updateHolder(holder, str);
        if (str.equals("del")) {
            holder.keyboardBox.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onTouch(event, str);
                    }
                    if (mSmartStartDoubleClickUtil != null) {
                        mSmartStartDoubleClickUtil.onEvent(v, event);
                    }
                    return false;
                }
            });
        } else {
            holder.keyboardBox.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }
        holder.keyboardBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(str);
                }
            }
        });
        return convertView;
    }

    private void initHolder(ViewHolder holder, View view) {
        holder.keyboardText = (TextView) view.findViewById(R.id.keyboard_item_text);
        holder.keyboardImg = (ImageView) view.findViewById(R.id.keyboard_item_img);
        holder.keyboardBox = (RelativeLayout) view.findViewById(R.id.keyboard_item_box);
    }

    private void updateHolder(ViewHolder holder, String str) {


        if (str.equals("del")) {
            holder.keyboardImg.setVisibility(View.VISIBLE);
            holder.keyboardText.setText("");
        } else {
            holder.keyboardImg.setVisibility(View.GONE);
            holder.keyboardText.setText(str);
        }

    }

    final class ViewHolder {
        TextView keyboardText;
        ImageView keyboardImg;
        RelativeLayout keyboardBox;

    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String text);

        void onLongClick(String text);

        void onTouch(MotionEvent event, String text);
    }

}
