package com.example.mytablistview;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ${chenzhikai} on 2018/5/7.
 */

public abstract class BaseMenuAdapter {

    public abstract int getConut();

    public abstract View getTabView(int position, ViewGroup parent);

    public abstract View getMenuView(int positon ,ViewGroup parent);

    public void menuOpen(View tabView) {
        TextView tabTv = (TextView) tabView;
        tabTv.setTextColor(Color.RED);
    }

    public void close(View tabView) {
        TextView tabTv = (TextView) tabView;
        tabTv.setTextColor(Color.BLACK);
    }
}
