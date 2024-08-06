package com.example.gutfoodwit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class ListHeadViewAdapter extends BaseAdapter {
    private Context mActivity;

    public ListHeadViewAdapter(Context context) {
        mActivity = context;
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(mActivity);
        textView.setText(String.valueOf(position));
        textView.setTextSize(20);
        return textView;
    }
}
