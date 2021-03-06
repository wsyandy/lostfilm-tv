package com.github.alexesprit.lostfilm.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.github.alexesprit.lostfilm.R;
import com.github.alexesprit.lostfilm.adapter.EpisodeItemAdapter;
import com.github.alexesprit.lostfilm.item.SerialDescription;
import com.github.alexesprit.lostfilm.loader.DescripionLoader;


public class DescriptionActivity extends SherlockActivity {
    private ListView episodesList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.serial_description_view);

        Intent intent = getIntent();
        if (null != intent) {
            episodesList = (ListView)findViewById(R.id.serial_description_episodes_list);
            episodesList.setEmptyView(findViewById(R.id.serial_description_empty));
            episodesList.setHeaderDividersEnabled(false);

            String url = intent.getStringExtra("url");
            String name = intent.getStringExtra("name");
            setTitle(name);
            loadDescription(url);
        }
    }

    private void loadDescription(String url) {
        new DescLoadTask().execute(url);
    }

    private void updateView(SerialDescription desc) {
        View headerView = getLayoutInflater().inflate(R.layout.serial_description_header, null);

        TextView descView = (TextView)headerView.findViewById(R.id.serial_description_text);
        descView.setText(desc.desc);

        ImageView posterView = (ImageView)headerView.findViewById(R.id.serial_description_poster);
        posterView.setImageBitmap(desc.poster);

        episodesList.addHeaderView(headerView);
        episodesList.setAdapter(new EpisodeItemAdapter(this, desc.episodes));
    }

    private void setLoadingProgress(boolean show) {
        setSupportProgressBarIndeterminateVisibility(show);
    }

    private class DescLoadTask extends AsyncTask<String, Void, SerialDescription> {
        private DescripionLoader loader = new DescripionLoader();

        @Override
        protected void onPreExecute() {
            setLoadingProgress(true);
        }

        @Override
        protected SerialDescription doInBackground(String... url) {
            return loader.getDescription(url[0]);
        }

        @Override
        protected void onPostExecute(SerialDescription desc) {
            if (null != desc) {
                updateView(desc);
            } else {
                Toast.makeText(DescriptionActivity.this, R.string.unable_to_load, Toast.LENGTH_SHORT).show();
            }
            setLoadingProgress(false);
        }
    }
}