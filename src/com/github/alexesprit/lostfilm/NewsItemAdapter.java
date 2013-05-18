package com.github.alexesprit.lostfilm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.alexesprit.lostfilm.loader.ImageLoader;

import java.util.ArrayList;

public final class NewsItemAdapter extends BaseAdapter {
    private ArrayList<NewsItem> items = new ArrayList<NewsItem>();
    private ImageLoader loader = new ImageLoader();
    private LayoutInflater inflater;

    public NewsItemAdapter(Context context) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public NewsItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        NewsItem item = getItem(i);
        if (null == view) {
            view = inflater.inflate(R.layout.item, null);
            vh = new ViewHolder();
            vh.nameView = (TextView)view.findViewById(R.id.item_name);
            vh.dateView = (TextView)view.findViewById(R.id.item_date);
            vh.episodeView = (TextView)view.findViewById(R.id.item_episode);
            vh.preView = (ImageView)view.findViewById(R.id.item_preview);
            view.setTag(vh);
        } else {
            vh = (ViewHolder)view.getTag();
        }
        int color = Util.getStringColor(item.name);
        vh.nameView.setText(item.name);
        vh.nameView.setTextColor(color);
        vh.dateView.setText(item.date);
        vh.dateView.setTextColor(color);
        vh.episodeView.setText(item.episode);
        /*if (vh.preView.getDrawable() == null) {
            loader.loadImage(item.previewURL, vh.preView);
        }*/
        return view;
    }

    public void extend(ArrayList<NewsItem> newItems) {
        items.addAll(newItems);
    }

    private class ViewHolder {
        private TextView nameView;
        private TextView episodeView;
        private TextView dateView;
        private ImageView preView;
    }
}
