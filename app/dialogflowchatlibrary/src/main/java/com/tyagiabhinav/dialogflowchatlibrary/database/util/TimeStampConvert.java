package com.tyagiabhinav.dialogflowchatlibrary.database.util;

import androidx.room.TypeConverter;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeStampConvert {

    private static DateFormat dt = new SimpleDateFormat("HH:mm");


    @TypeConverter
    public static String ToTimestamp(Time value) {
        TimeZone timeZone = TimeZone.getTimeZone("CET");
        dt.setTimeZone(timeZone);

        if (value == null) return null;
        else {
            String result = dt.format(value);
            return result;
        }
    }


}
