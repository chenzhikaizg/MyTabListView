package com.example.mytablistview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ${chenzhikai} on 2018/5/7.
 */

public class MyMenuAdapter extends BaseMenuAdapter {

    private String[] mItems = {"类型","品牌","价格","更多"};
    private Context mContext;

    public MyMenuAdapter(Context context){
        this.mContext = context;
    }
    @Override
    public int getConut() {
        return mItems.length;
    }

    @Override
    public View getTabView(int position, ViewGroup parent) {
        TextView  textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.adapter_tab_view, parent, false);
        textView.setText(mItems[position]);
        textView.setTextColor(Color.BLACK);

        return textView;
    }

    @Override
    public View getMenuView(int positon, ViewGroup parent) {
        TextView  menuTextView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.adapter_menu_content_view, parent, false);
        menuTextView.setText(mItems[positon]);
        return menuTextView;
    }
}
