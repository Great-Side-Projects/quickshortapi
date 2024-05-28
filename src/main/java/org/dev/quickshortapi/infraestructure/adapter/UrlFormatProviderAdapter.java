package org.dev.quickshortapi.infraestructure.adapter;

import org.apache.commons.lang3.StringUtils;
import org.dev.quickshortapi.application.port.format.IUrlFormatProviderPort;
import org.springframework.stereotype.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class UrlFormatProviderAdapter implements IUrlFormatProviderPort {

    private static final String DATE_FORMAT_PATTERN = "E, yyyy-MM-dd HH:mm:ss";
    @Override
    public String getDateFormatedToString(Date date) {
        if (date == null)
            return StringUtils.EMPTY;

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        return dateFormat.format(date);
    }
}
