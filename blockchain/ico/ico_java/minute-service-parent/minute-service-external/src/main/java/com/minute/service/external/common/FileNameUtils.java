package com.minute.service.external.common;

import com.common.tookit.date.DateTimeUtils;

import java.util.Date;

public class FileNameUtils {

    /**
     * generate unique file name according to time
     * @return
     */
    public static String generateUniqueFileName() {
        //TODO implements unique
        Date date = new Date();

        String now = DateTimeUtils.formatYYYYMMDDHHMMSS_NOSPACE(date);

        return now;
    }

}
