package com.seed.sbp.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String dateToString(Date date) {
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        return sdf.format(date);
    }

    public static Date stringToDate(String strDate) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        return sdf.parse(strDate);
    }

}
