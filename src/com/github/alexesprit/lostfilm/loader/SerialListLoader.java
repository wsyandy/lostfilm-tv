package com.github.alexesprit.lostfilm.loader;

import android.util.Log;
import com.github.alexesprit.lostfilm.Util;
import com.github.alexesprit.lostfilm.item.SerialItem;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerialListLoader {
    private static final String SERIAL_PATTERN = "<a href=\"(.+?)\" class=\"bb_a\">(.+?)<br><span>";

    public ArrayList<SerialItem> getSerialsList() {
        long t0, t1;
        t0 = System.currentTimeMillis();
        String content = Util.getURLContent("http://www.lostfilm.tv/");
        t1 = System.currentTimeMillis();
        Log.i("LostFilm", "[serials] load time: " + (t1 - t0) + " sec.");
        if (null != content) {
            t0 = System.currentTimeMillis();
            ArrayList<SerialItem> serials = new ArrayList<SerialItem>();
            Matcher m = Pattern.compile(SERIAL_PATTERN).matcher(content);
            while (m.find()) {
                SerialItem item = new SerialItem();
                item.name = m.group(2);
                item.descURL = Util.getFullURL(m.group(1));
                serials.add(item);
            }
            t1 = System.currentTimeMillis();
            Log.i("LostFilm", "[serials] parse time: " + (t1 - t0) + " sec.");
            return serials;
        }
        return null;
    }
}
