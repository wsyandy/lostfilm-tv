package com.github.alexesprit.lostfilm.loader;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import com.github.alexesprit.lostfilm.Util;

import java.util.HashMap;

public class ImageLoader  {
    private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Object data[] = (Object[])msg.obj;
            ImageView view = (ImageView)data[0];
            Bitmap bitmap = (Bitmap)data[1];
            view.setImageBitmap(bitmap);
        }
    };

    public void loadImage(final String url, final ImageView view) {
        new Thread() {
            @Override
            public void run() {
                Bitmap bmp;
                if (cache.containsKey(url)) {
                    bmp = cache.get(url);
                } else {
                    bmp = Util.getBitmapByURL(url);
                    cache.put(url, bmp);
                }

                handler.sendMessage(Message.obtain(handler, 0, new Object[]{view, bmp}));
            }
        }.start();
    }
}
