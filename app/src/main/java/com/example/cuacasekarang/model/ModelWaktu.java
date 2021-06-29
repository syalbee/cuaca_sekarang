package com.example.cuacasekarang.model;


import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ModelWaktu {    public String getDateNow(String format) {
    Calendar cal = Calendar.getInstance();
    final Date date = cal.getTime();

    DateFormat dateFormat = new SimpleDateFormat(format);

    String formattedDate = dateFormat.format(date);

    Log.d("date", String.valueOf(formattedDate));
    return formattedDate;
}

    public Integer getTimeNow(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);

        String formattedDate = dateFormat.format(new Date());

        int hourNow = Integer.valueOf(formattedDate);
        Log.d("date", String.valueOf(formattedDate));
        return hourNow;
    }
}
