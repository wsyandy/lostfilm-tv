package com.github.alexesprit.lostfilm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.github.alexesprit.lostfilm.R;
import com.github.alexesprit.lostfilm.item.EpisodeItem;

import java.util.ArrayList;

public class EpisodeItemAdapter extends BaseAdapter {
    private ArrayList<EpisodeItem> episodes;
    private LayoutInflater inflater;

    public EpisodeItemAdapter(Context context, ArrayList<EpisodeItem> episodes) {
        this.episodes = episodes;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return episodes.size();
    }

    @Override
    public EpisodeItem getItem(int i) {
        return episodes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (null == view) {
            view = inflater.inflate(R.layout.episode_item, null);
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
        private TextView numberView;

        private ViewHolder(View view) {
            nameView = (TextView)view.findViewById(R.id.episode_item_name);
            numberView = (TextView)view.findViewById(R.id.episode_item_number);
        }

        private void populateFrom(EpisodeItem item) {
            nameView.setText(item.name);
            numberView.setText(item.number);
        }
    }
}
