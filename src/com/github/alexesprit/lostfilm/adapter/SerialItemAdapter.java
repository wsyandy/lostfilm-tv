package com.github.alexesprit.lostfilm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.github.alexesprit.lostfilm.R;
import com.github.alexesprit.lostfilm.Util;
import com.github.alexesprit.lostfilm.item.SerialItem;

import java.util.ArrayList;

public class SerialItemAdapter extends BaseAdapter {
    private ArrayList<SerialItem> series;
    private LayoutInflater inflater;

    public SerialItemAdapter(Context context, ArrayList<SerialItem> series) {
        this.inflater = LayoutInflater.from(context);
        this.series = series;
    }

    @Override
    public int getCount() {
        return series.size();
    }

    @Override
    public SerialItem getItem(int i) {
        return series.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (null == view) {
            view = inflater.inflate(R.layout.serial_item, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder)view.getTag();
        }
        vh.populateFrom(getItem(i));
        return view;
    }

    private class ViewHolder {
        private TextView nameView;

        private ViewHolder(View view) {
            nameView = (TextView)view.findViewById(R.id.serial_item_name);
        }

        private void populateFrom(SerialItem item) {
            int color = Util.getStringColor(item.name);
            nameView.setText(item.name);
            nameView.setTextColor(color);
        }
    }
}
