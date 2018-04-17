package org.com.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainApp {

    public static void main(String[] args) {
        // Integer.toString(2);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.err.println(sdf.format(Calendar.getInstance().getTime()));
            Date date = Calendar.getInstance().getTime();
            date.setTime(Long.parseLong("1497007693859"));
            System.err.println(date);
            // 1497004049016

            String src = "2017-09-09 11:12:13.0";
            System.err.println(src.substring(0, 10));

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
