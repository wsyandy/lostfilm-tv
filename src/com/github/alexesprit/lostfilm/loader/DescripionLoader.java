package com.github.alexesprit.lostfilm.loader;

import android.util.Log;
import com.github.alexesprit.lostfilm.Util;
import com.github.alexesprit.lostfilm.item.EpisodeItem;
import com.github.alexesprit.lostfilm.item.SerialDescription;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DescripionLoader {
    private static final String DESC_PATTERN = "<h2 style=\"color: #999999;font-size: 14px;font-weight: bold\">(.+?)\"</h2></div>.+?<span>(.+?)</span>";
    private static final String POSTER_PATTERN = "<img src=\"(.+?)\" alt=\"\" title=\"\" align=\"center\" vspace=\"0\" hspace=\"0\" width=\"480\" height=\"270\" />";
    private static final String EPISODES_PATTERN = "</span>, <span>(.+?)</span></td>.+?<span style=\"color:#4b4b4b\">(.+?)</span><br />";
    private boolean isLoaded = false;

    public SerialDescription getDescription(String url) {
        long t0, t1;
        t0 = System.currentTimeMillis();
        String content = Util.getURLContent(url);
        t1 = System.currentTimeMillis();
        Log.i("LostFilm", "[desc] load time: " + (t1 - t0) + " sec.");
        if (null != content) {
            t0 = System.currentTimeMillis();

            SerialDescription desc = null;
            Matcher mdesc = Pattern.compile(DESC_PATTERN, Pattern.DOTALL).matcher(content);
            if (mdesc.find()) {
                desc = new SerialDescription();
                desc.desc = Util.removeTags(mdesc.group(2));

                Matcher mposter = Pattern.compile(POSTER_PATTERN).matcher(content);
                if (mposter.find()) {
                    String posterURL = Util.getFullURL(mposter.group(1));
                    desc.poster = Util.getBitmapByURL(posterURL);
                }

                ArrayList<EpisodeItem> episodes = new ArrayList<EpisodeItem>();
                Matcher m = Pattern.compile(EPISODES_PATTERN, Pattern.DOTALL).matcher(content);
                while (m.find()) {
                    String name = m.group(2);
                    if (isEpisodeName(name)) {
                        String number = m.group(1);
                        EpisodeItem episode = new EpisodeItem();
                        episode.name = name;
                        episode.number = number;
                        episodes.add(episode);
                    }
                }
                desc.episodes = episodes;
            }
            t1 = System.currentTimeMillis();
            Log.i("LostFilm", "[desc] parse time: " + (t1 - t0) + " sec.");
            return desc;
        }
        return null;
    }

    private boolean isEpisodeName(String name) {
        return !name.contains("сезон полностью");
    }
}
