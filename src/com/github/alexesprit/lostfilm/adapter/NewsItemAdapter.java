package com.github.alexesprit.lostfilm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.alexesprit.lostfilm.R;
import com.github.alexesprit.lostfilm.Util;
import com.github.alexesprit.lostfilm.item.NewsItem;
import com.github.alexesprit.lostfilm.loader.LazyImageLoader;

import java.util.ArrayList;

public final class NewsItemAdapter extends BaseAdapter {
    private ArrayList<NewsItem> items = new ArrayList<NewsItem>();
    private LazyImageLoader loader = new LazyImageLoader();
    private LayoutInflater inflater;

    public NewsItemAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
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
        if (null == view) {
            view = inflater.inflate(R.layout.news_item, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder)view.getTag();
        }
        vh.populateFrom(getItem(i));
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

        private ViewHolder(View view) {
            nameView = (TextView)view.findViewById(R.id.news_item_name);
            dateView = (TextView)view.findViewById(R.id.news_item_date);
            episodeView = (TextView)view.findViewById(R.id.news_item_episode);
            preView = (ImageView)view.findViewById(R.id.news_item_preview);
        }

        private void populateFrom(NewsItem item) {
            int color = Util.getStringColor(item.name);
            nameView.setText(item.name);
            nameView.setTextColor(color);
            dateView.setText(item.date);
            dateView.setTextColor(color);
            episodeView.setText(item.episode);
            preView.setVisibility(View.GONE);
            loader.loadImage(item.previewURL, preView);
        }
    }
}
