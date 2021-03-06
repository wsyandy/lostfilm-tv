package com.github.alexesprit.lostfilm.loader;

import android.util.Log;
import com.github.alexesprit.lostfilm.Util;
import com.github.alexesprit.lostfilm.item.NewEpisodeItem;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NewEpisodeListLoader {
    private static final String NEWS_URL = "http://www.lostfilm.tv/browse.php";
    /*
     * 1 group: serial name
     * 2 group: data (DATA_PATTERN)
     */
    private static final String NEWS_PATTERN = "<span style=\"font-family:arial;font-size:14px;color:#000000\">(.+?)</span>(.+?)</b>\n\t\t</span>";
    /*
     * 1 group: description local URL
     * 2 group: poster local URL
     * 3 group: episode name
     * 4 group: release date
     */
    private static final String DATA_PATTERN = "<a href=\"(.+?)\"><img src=\"(.+?)\".+?<span class=\"torrent_title\"><b>(.+?)</b>.+?Дата: <b>(.+?) ";
    private int page = 1;

    public void setNextPage() {
        // unlimited!1
        ++page;
    }

    public ArrayList<NewEpisodeItem> loadNews() {
        long t0, t1;
        t0 = System.currentTimeMillis();
        String content = Util.getURLContent(getNewsURL());
        t1 = System.currentTimeMillis();
        Log.i("LostFilm", "[news] load time: " + (t1 - t0) + " sec.");
        if (null != content) {
            t0 = System.currentTimeMillis();
            ArrayList<NewEpisodeItem> items = new ArrayList<NewEpisodeItem>();
            Pattern p = Pattern.compile(NEWS_PATTERN, Pattern.DOTALL);
            Pattern data = Pattern.compile(DATA_PATTERN, Pattern.DOTALL);
            Matcher m = p.matcher(content);
            while (m.find()) {
                Matcher mdata = data.matcher(m.group(2));
                while (mdata.find()) {
                    NewEpisodeItem item = new NewEpisodeItem();
                    item.name = m.group(1);
                    item.date = mdata.group(4);
                    item.episode = mdata.group(3);
                    item.previewURL = Util.getFullURL(mdata.group(2));
                    item.descURL = Util.getFullURL(mdata.group(1));
                    items.add(item);
                }
            }
            t1 = System.currentTimeMillis();
            Log.i("LostFilm", "[news] parse time: " + (t1 - t0) + " sec.");
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
}
