package com.github.alexesprit.lostfilm.fragment;

import android.app.Activity;
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
import com.github.alexesprit.lostfilm.R;
import com.github.alexesprit.lostfilm.activity.DescriptionActivity;
import com.github.alexesprit.lostfilm.adapter.SerialItemAdapter;
import com.github.alexesprit.lostfilm.item.SerialItem;
import com.github.alexesprit.lostfilm.loader.SerialsListLoader;

import java.util.ArrayList;

public class SerialsListFragment extends SherlockFragment {
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
        return inflater.inflate(R.layout.serials_list_view, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        serialsList = (ListView)getActivity().findViewById(R.id.serials_list);
        serialsList.setEmptyView(getActivity().findViewById(R.id.serials_empty_view));
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

    private class SerialsListLoadTask extends AsyncTask<Void, Void, ArrayList<SerialItem>> {
        private SerialsListLoader loader = new SerialsListLoader();

        @Override
        protected ArrayList<SerialItem> doInBackground(Void... voids) {
            return loader.getSerialsList();
        }

        @Override
        protected void onPostExecute(ArrayList<SerialItem> items) {
            Activity activity = getActivity();
            if (items != null) {
                ListView view = (ListView)activity.findViewById(R.id.serials_list);
                view.setAdapter(new SerialItemAdapter(activity, items));
            } else {
                Toast.makeText(activity, R.string.unable_to_load, Toast.LENGTH_SHORT).show();
            }
            activity.findViewById(R.id.loading_layout).setVisibility(View.GONE);
        }
    }
}
