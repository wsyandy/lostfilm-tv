package com.github.alexesprit.lostfilm.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.github.alexesprit.lostfilm.R;
import com.github.alexesprit.lostfilm.activity.DescriptionActivity;
import com.github.alexesprit.lostfilm.adapter.SerialItemAdapter;
import com.github.alexesprit.lostfilm.item.SerialItem;
import com.github.alexesprit.lostfilm.loader.SerialListLoader;

import java.util.ArrayList;

public class SerialListFragment extends SherlockFragment {
    private ListView serialsList;
    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Adapter adapter = adapterView.getAdapter();
            showDescription((SerialItem)adapter.getItem(i));
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.serial_list_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        serialsList = (ListView)getActivity().findViewById(R.id.serials_list);
        serialsList.setEmptyView(getActivity().findViewById(R.id.serials_empty_list));
        serialsList.setOnItemClickListener(clickListener);
        updateView();
    }

    private void updateView() {
        new SerialsListLoadTask().execute();
    }

    private void showDescription(SerialItem item) {
        Intent intent = new Intent(getActivity(), DescriptionActivity.class);
        intent.putExtra("url", item.descURL);
        intent.putExtra("name", item.name);
        startActivity(intent);
    }

    private void setLoadingProgress(boolean show) {
        SherlockFragmentActivity activity = getSherlockActivity();
        activity.setSupportProgressBarIndeterminateVisibility(show);
    }

    private class SerialsListLoadTask extends AsyncTask<Void, Void, ArrayList<SerialItem>> {
        private SerialListLoader loader = new SerialListLoader();

        @Override
        protected void onPreExecute() {
            setLoadingProgress(true);
        }

        @Override
        protected ArrayList<SerialItem> doInBackground(Void... voids) {
            return loader.getSerialsList();
        }

        @Override
        protected void onPostExecute(ArrayList<SerialItem> items) {
            if (isVisible()) {
                SherlockFragmentActivity activity = getSherlockActivity();
                if (items != null) {
                    serialsList.setAdapter(new SerialItemAdapter(activity, items));
                } else {
                    Toast.makeText(activity, R.string.unable_to_load, Toast.LENGTH_SHORT).show();
                }
                setLoadingProgress(false);
            }
        }
    }
}
