package com.github.alexesprit.lostfilm;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import com.github.alexesprit.lostfilm.loader.NewsLoader;

import java.util.ArrayList;


public final class MainActivity extends Activity {
    private NewsLoader loader;
    private ListView newsView;
    private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);
        setTitle(R.string.news_title);

        newsView = (ListView)findViewById(R.id.news_list);
        newsView.setAdapter(new NewsItemAdapter(this));
        newsView.setEmptyView(findViewById(R.id.empty_view));
        newsView.setOnScrollListener(listener);

        loader = new NewsLoader();
        updateView();
    }

    private void loadNextData() {
        loader.setNextPage();
        updateView();
    }

    private void updateView() {
        new Thread() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setLoadingProgress(true);
                    }
                });
                final ArrayList<NewsItem> items = loader.loadNews();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (items != null) {
                            NewsItemAdapter adapter = (NewsItemAdapter)newsView.getAdapter();
                            adapter.extend(items);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.unable_to_load, Toast.LENGTH_SHORT).show();
                        }
                        setLoadingProgress(false);
                    }
                });
            }
        }.start();
    }

    private void setLoadingProgress(boolean show) {
        setProgressBarIndeterminateVisibility(show);
    }
}