package org.dev.quickshortapi.application.port.in.format;

import org.apache.commons.lang3.StringUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UrlFormatProvider implements IUrlFormat {

    private static final String DATE_FORMAT_PATTERN = "E, yyyy-MM-dd HH:mm:ss";
    @Override
    public String getDateFormatedToString(Date date) {
        if (date == null)
            return StringUtils.EMPTY;

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        return dateFormat.format(date);
    }
}
