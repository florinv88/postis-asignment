package com.fnkcode.postis.utils;

import io.micrometer.common.lang.Nullable;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public final class DateUtils {
    private DateUtils(){}

    private static final String DDMMYYYY = "dd-MM-yyyy";

    public static Date parseDateDDMMYYYY(String source){
        return getDate(source, DDMMYYYY);
    }

    @Nullable
    private static Date getDate(String source, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if(StringUtils.isEmpty(source)){
            return null;
        }
        try {
            return sdf.parse(source);
        } catch (ParseException e) {
            log.warn("Not a date in yyyyMMdd format: {}", source);
            return null;
        }
    }
}
