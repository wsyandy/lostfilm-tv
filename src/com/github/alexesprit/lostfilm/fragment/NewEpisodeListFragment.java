package com.github.alexesprit.lostfilm.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.github.alexesprit.lostfilm.activity.DescriptionActivity;
import com.github.alexesprit.lostfilm.R;
import com.github.alexesprit.lostfilm.adapter.NewEpisodeItemAdapter;
import com.github.alexesprit.lostfilm.item.NewEpisodeItem;
import com.github.alexesprit.lostfilm.loader.NewEpisodeListLoader;

import java.util.ArrayList;

public class NewEpisodeListFragment extends SherlockFragment {
    private NewEpisodeListLoader loader;
    private ListView newsView;
    private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        private int prevTotalCount = 0;

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisible, int visibleCount, int totalCount) {
            boolean loadMore = ((firstVisible + visibleCount) >= totalCount) && totalCount != prevTotalCount;
            if (loadMore) {
                prevTotalCount = totalCount;
                loadNextData();
            }
        }
    };
    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Adapter adapter = adapterView.getAdapter();
            showDescription((NewEpisodeItem)adapter.getItem(i));
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_episode_list_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        newsView = (ListView)activity.findViewById(R.id.new_episode_list);
        newsView.setAdapter(new NewEpisodeItemAdapter(activity));
        newsView.setEmptyView(activity.findViewById(R.id.new_episode_empty_list));
        newsView.setOnScrollListener(scrollListener);
        newsView.setOnItemClickListener(clickListener);

        loader = new NewEpisodeListLoader();
        updateView();
    }

    private void showDescription(NewEpisodeItem item) {
        Intent intent = new Intent(getActivity(), DescriptionActivity.class);
        intent.putExtra("url", item.descURL);
        intent.putExtra("name", item.name);
        startActivity(intent);
    }

    private void loadNextData() {
        loader.setNextPage();
        updateView();
    }

    private void updateView() {
        new NewsLoadTask().execute();
    }

    private void setLoadingProgress(boolean show) {
        SherlockFragmentActivity activity = getSherlockActivity();
        activity.setSupportProgressBarIndeterminateVisibility(show);
    }

    private class NewsLoadTask extends AsyncTask<Void, Void, ArrayList<NewEpisodeItem>> {
        @Override
        protected void onPreExecute() {
            setLoadingProgress(true);
        }

        @Override
        protected ArrayList<NewEpisodeItem> doInBackground(Void... voids) {
            return loader.loadNews();
        }

        @Override
        protected void onPostExecute(ArrayList<NewEpisodeItem> items) {
            if (isVisible()) {
                if (items != null) {
                    NewEpisodeItemAdapter adapter = (NewEpisodeItemAdapter)newsView.getAdapter();
                    adapter.extend(items);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), R.string.unable_to_load, Toast.LENGTH_SHORT).show();
                }
                setLoadingProgress(false);
            }
        }
    }
}
