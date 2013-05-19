package com.github.alexesprit.lostfilm.loader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import com.github.alexesprit.lostfilm.Util;

import java.util.HashMap;

public class LazyImageLoader {
    private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();

    public void loadImage(final String url, final ImageView view) {
        if (cache.containsKey(url)) {
            Bitmap bmp = cache.get(url);
            view.setImageBitmap(bmp);
            view.setVisibility(View.VISIBLE);
        } else {
            new ImageLoader(url, view).execute();
        }
    }

    private class ImageLoader extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        private ImageView view;

        private ImageLoader(String url, ImageView view) {
            this.url = url;
            this.view = view;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            return Util.getBitmapByURL(url);
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            if (null != bmp) {
                cache.put(url, bmp);
                view.setImageBitmap(bmp);
                view.setVisibility(View.VISIBLE);
            }
        }
    }
}
