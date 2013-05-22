package com.github.alexesprit.lostfilm.loader;

import com.github.alexesprit.lostfilm.Util;
import com.github.alexesprit.lostfilm.item.SerialItem;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerialsListLoader {
    private static final String SERIAL_PATTERN = "<a href=\"(.+?)\" class=\"bb_a\">(.+?)<br><span>";

    public ArrayList<SerialItem> getSerialsList() {
        String content = Util.getURLContent("http://www.lostfilm.tv/");
        if (null != content) {
            ArrayList<SerialItem> serials = new ArrayList<SerialItem>();
            Matcher m = Pattern.compile(SERIAL_PATTERN).matcher(content);
            while (m.find()) {
                SerialItem item = new SerialItem();
                item.name = m.group(2);
                item.descURL = Util.getFullURL(m.group(1));
                serials.add(item);
            }
            return serials;
        }
        return null;
    }
}
