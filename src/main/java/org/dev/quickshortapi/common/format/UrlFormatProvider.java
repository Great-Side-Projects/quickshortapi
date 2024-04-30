package org.dev.quickshortapi.common.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UrlFormatProvider implements IUrlFormat {

    private final String DATE_FORMAT_PATTERN = "E, yyyy-MM-dd HH:mm:ss";
    @Override
    public String getDateFormatedToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        String strDate = dateFormat.format(date);
        return strDate;
    }
}
