package com.tyagiabhinav.dialogflowchatlibrary.database.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.time.LocalTime;

import java.sql.Time;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TimestampConverter {

    private static DateFormat df = new SimpleDateFormat("dd MMM");
    private static DateTimeFormatter dt = DateTimeFormatter.ofPattern("HH:mm");
    //private static DateFormat dt = new SimpleDateFormat("HH:mm");


    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                TimeZone timeZone = TimeZone.getTimeZone("CET");
                df.setTimeZone(timeZone);
                Date date = df.parse(value);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }


    @TypeConverter
    public static String dateToTimestamp(Date value) {
        TimeZone timeZone = TimeZone.getTimeZone("CET");
        df.setTimeZone(timeZone);

        if (value == null) return null;
        else {
            String result = df.format(value);
            return result;
        }
    }

    @TypeConverter
    public static LocalTime ToTimestamp(String value) {
        //DateFormat dt = new SimpleDateFormat("HH:mm");
        //TimeZone timeZone = TimeZone.getTimeZone("CET");
        //dt.setTimeZone(timeZone);

        if (value == null) return null;
        else {
            LocalTime result = LocalTime.parse(value);
            return result;
        }
    }

    @TypeConverter
    public static String TimeToTimestamp(LocalTime value) {
        //DateFormat dt = new SimpleDateFormat("HH:mm");
        //TimeZone timeZone = TimeZone.getTimeZone("CET");
        //dt.setTimeZone(timeZone);

        if (value == null) return null;
        else {
            String result = dt.format(value);
            return result;
        }
    }

//    @TypeConverter
//    public static Bitmap viewToImage(View view){
//        if(view == null) return null;
//
//        else{
//            Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(returnedBitmap);
//            Drawable bgDrawable = view.getBackground();
//            bgDrawable.draw(canvas);
//
//            return returnedBitmap;
//        }
//    }

    @TypeConverter
    public static byte[] imageToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    @TypeConverter
    public static Bitmap byteToBitmap(byte[] byteArray){
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }

}


