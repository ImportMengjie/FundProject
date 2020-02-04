package com.mengjie.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mengjie.bean.FundListBean;
import com.paging.listview.PagingBaseAdapter;

public class PagingFundListAdapter extends PagingBaseAdapter<FundListBean.FundBean> {
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public FundListBean.FundBean getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        FundListBean.FundBean fund = getItem(position);

        if (convertView != null) {
            textView = (TextView) convertView;
        } else {
            textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
        }
        textView.setText(fund.name);
        return textView;
    }
}
