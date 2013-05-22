package com.github.alexesprit.lostfilm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public final class Util {
    private static final String LOSTFILM_URL = "http://www.lostfilm.tv";
    private static int colors[] = new int[]{
            0xFF8EE5EE, 0xFF54FF9F,
            0xFFC0FF3E, 0xFFF0E68C,
            0xFFFFd700, 0xFFFFA500,
            0xFFFFC0cb, 0xFFFF8C69,
            0xFFFF0000, 0xFFAB82FF,
            0xFFE066FF
    };

    private Util() {
    }

    public static String getURLContent(String url) {
        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            return client.execute(new HttpGet(url), responseHandler);
        } catch (IOException ignored) {
        }
        return null;
    }

    public static int getStringColor(String string) {
        int hash = Math.abs(string.hashCode());
        return colors[hash % colors.length];
    }

    public static Bitmap getBitmapByURL(String url) {
        try {
            InputStream in = new java.net.URL(url).openStream();
            return BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("LostFilm", url);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("LostFilm", "getBitmapByURL: IOException");
        }
        return null;
    }

    public static String removeTags(String text) {
        return android.text.Html.fromHtml(text).toString();
    }

    public static String getFullURL(String localURL) {
        return LOSTFILM_URL + localURL;
    }
}
