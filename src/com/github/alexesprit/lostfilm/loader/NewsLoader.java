package com.github.alexesprit.lostfilm.loader;

import android.util.Log;
import com.github.alexesprit.lostfilm.NewsItem;
import com.github.alexesprit.lostfilm.Util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NewsLoader {
    private static final String LOSTFILM_URL = "http://www.lostfilm.tv";
    private static final String NEWS_URL = "http://www.lostfilm.tv/browse.php";
    // 1 group: name
    // 2 group: data
    private static final String NEWS_PATTERN = "<span style=\"font-family:arial;font-size:14px;color:#000000\">(.+?)</span>(.+?)</b>\n\t\t</span>";
    // 1 group: TV series URL
    // 2 group: episode name
    // 3 group: release date
    private static final String DATA_PATTERN = "<a href=\"(.+?)\"><img src=\"(.+?)\".+?<span class=\"torrent_title\"><b>(.+?)</b>.+?Дата: <b>(.+?)</b><br />";
    private int page = 1;

    public void setNextPage() {
        // unlimited!1
        ++page;
    }

    public ArrayList<NewsItem> loadNews() {
        String content = Util.getURLContent(getNewsURL());
        if (null != content) {
            long t0 = System.currentTimeMillis();
            ArrayList<NewsItem> items = new ArrayList<NewsItem>();
            Pattern p = Pattern.compile(NEWS_PATTERN, Pattern.DOTALL);
            Pattern data = Pattern.compile(DATA_PATTERN, Pattern.DOTALL);
            Matcher m = p.matcher(content);
            while (m.find()) {
                Matcher mdata = data.matcher(m.group(2));
                while (mdata.find()) {
                    NewsItem item = new NewsItem();
                    item.name = m.group(1);
                    item.date = mdata.group(4);
                    item.episode = mdata.group(3);
                    item.previewURL = getFullURL(mdata.group(2));
                    Log.i("LostFilm", getFullURL(mdata.group(2)));
                    Log.i("LostFilm", getFullURL(mdata.group(1)));
                    items.add(item);
                }
            }
            long t1 = System.currentTimeMillis();
            Log.i("LostFilm", "Load time: " + (t1 - t0) + " sec.");
            return items;
        }
        return null;
    }

    private String getNewsURL() {
        // 1st page: o=0
        // 2nd page: o=15
        // ...
        int param = (page - 1) * 15;
        return String.format("%s?o=%d", NEWS_URL, param);
    }

    private String getFullURL(String localURL) {
        return LOSTFILM_URL + localURL;
    }
}
