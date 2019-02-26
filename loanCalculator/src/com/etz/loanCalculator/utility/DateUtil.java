package com.etz.loanCalculator.utility;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {

    public static Date addDate(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);

        return cal.getTime();
    }

    public static Date getYesterday() {
        Date date1 = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        cal.add(5, -1);

        return cal.getTime();
    }

    public static int getCurrentMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (cal.get(2) + 1);
    }

    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return (cal.get(2) + 1);
    }

    public static int getCurrentYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(1);
    }

    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(1);
    }

    public static int getCurrentDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(5);
    }

    public static int getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(5);
    }

    public static boolean compareDate(Date date) {
        boolean result = false;
        if (date != null) {
            System.out.println("Registration date ::::: " + date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(1, 1);
            Date date2 = cal.getTime();

            System.out.println("i year after registration is ::::: " + date2);

            if (date2.after(new Date())) {
                System.out.println("Still make payments.......");
                result = true;
            } else {
                System.out.println("no more payments.......");
            }
        }

        return result;
    }

    public static boolean compareMonth(Date date, int period) {
        boolean result = false;
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(2, period);
            Date date2 = cal.getTime();

            if (date2.after(new Date())) {
                result = true;
            }

        }

        return result;
    }

    public static String formatDate(String date, Integer type) throws Exception {
        String result = "";
        String tmpDate = "";
        String tmpTime = "";
        int t = type.intValue();

        if ((date == null) || (date.trim().equals(""))) {
            if (t == 1) {
                result = formatDate("MMM dd, yyyy", "");
            } else {
                result = formatDate("", "");
            }
        } else {
            if (t == 1) {
                tmpDate = date;
            } else {
                tmpDate = date.substring(0, 10);
                tmpTime = date.substring(11, date.length());
            }

            String[] datearr = tmpDate.split("-");
            String month = "";

            switch (Integer.parseInt(datearr[1])) {
                case 1:
                    month = "Jan";
                    break;
                case 2:
                    month = "Feb";
                    break;
                case 3:
                    month = "Mar";
                    break;
                case 4:
                    month = "Apr";
                    break;
                case 5:
                    month = "May";
                    break;
                case 6:
                    month = "Jun";
                    break;
                case 7:
                    month = "Jul";
                    break;
                case 8:
                    month = "Aug";
                    break;
                case 9:
                    month = "Sep";
                    break;
                case 10:
                    month = "Oct";
                    break;
                case 11:
                    month = "Nov";
                    break;
                case 12:
                    month = "Dec";
            }

            result = month + " " + datearr[2] + ", " + datearr[0] + " " + tmpTime;
        }

        return result.trim();
    }

    public static String formatDate(String pattern, String date) throws Exception {
        DateFormat df = DateFormat.getDateInstance(1, Locale.ENGLISH);
        Date dt = null;

        if ((date == null) || (date.trim().equals(""))) {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            dt = cal.getTime();
        } else {
            df.parse(date);
        }

        SimpleDateFormat sd = new SimpleDateFormat();

        String tmpPattern = "MMM dd, yyyy hh:mm aaa";
        if (pattern.trim().equals("")) {
            pattern = tmpPattern;
        }

        sd.applyPattern(pattern);
        return sd.format(dt);
    }

    public static String formatDate(String date) throws Exception {
        DateFormat df = DateFormat.getDateInstance(1, Locale.ENGLISH);
        Date dt = df.parse(date);

        SimpleDateFormat sd = new SimpleDateFormat();

        String tmpPattern = "yyyy-MM-dd hh:mm:ss";
        sd.applyPattern(tmpPattern);
        return sd.format(dt);
    }

    public static String formatDate(String pattern, Date dDate) throws Exception {
        SimpleDateFormat sd = new SimpleDateFormat();

        String tmpPattern = "MMM dd, yyyy hh:mm aaa";
        if (pattern.trim().equals("")) {
            pattern = tmpPattern;
        }

        sd.applyPattern(pattern);
        return sd.format(dDate);
    }

    public static String formatNumber(Number number) {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMaximumFractionDigits(2);

        return format.format(number);
    }
    
    
    public String formatDate(Date d) {
        if (d != null) {
            java.util.Date date = d;
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            String yr = gc.get(gc.YEAR) + "";
            String mn = (gc.get(gc.MONTH) + 1) + "";
            String dd = gc.get(gc.DATE) + "";
            int hhInt = gc.get(gc.HOUR);
            String mm = gc.get(gc.MINUTE) + "";
            String ss = gc.get(gc.SECOND) + "";
            int am_pm = gc.get(gc.AM_PM);
            if (am_pm > 0)//pm
            {
                hhInt = 12 + hhInt;
            }
            String hh = hhInt + "";
            //SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-mm-dd HH:mm:ss" );//'2011-36-11 10:36:36'
            String sdate = yr + "-" + mn + "-" + dd + " " + hh + ":" + mm + ":" + ss;
            //System.out.println(d + " >>>>>>>>>>>>>>>>> " + sdate);
            return sdate;
        } else {
            return null;
        }
    }

    

    public static long findDateDifference(Date date1, Date date2, int type) {
        long result = 0;
        long diff = date1.getTime() - date2.getTime();
        
        switch (type) {
            case 1 ://for days
                result = diff / (1000*60*60*24);
                break;
            case 2 : // for hours
                result = diff / (1000*60*60);
                 break;
            case 3 : // for minutes
                result = diff / (1000*60);
                 break;
            case 4 : // for minutes
                result = diff / (1000);
                 break;
        }

//System.out.println(" date1 " + date1 + " date 2 " + date2 + " is " + result + " hours difference");
        return result;
    }

    public static String generateUniqueReference() {
        String uniqueId = "01"+new java.text.SimpleDateFormat("ssSSS").format(new java.util.Date())
                          +new java.text.SimpleDateFormat("SSSss").format(new java.util.Date())
                          +new java.text.SimpleDateFormat("MMSSSss").format(new java.util.Date())
                          +(""+java.lang.Math.random()).substring(2);

        return uniqueId;
    }

    public static void main(String[] args) throws Exception {
        int year = getCurrentYear();
        System.out.println(year);
      /*Date date = getYesterday();
       Date date2 = new Date();
       System.out.println(" days " + findDateDifference(date2, date, 1));
       System.out.println(" hours " + findDateDifference(date2, date, 2));
       System.out.println(" mins " + findDateDifference(date2, date, 3));

       int year = getCurrentYear();
        System.out.println(year);
       String month = "" + getCurrentMonth();
       String mth = month.length() == 2 ? month : "0"+month;
       System.out.println(mth);

       String day = "" + getCurrentDay();
       String dy = day.length() == 2 ? day : "0"+day;
       System.out.println(" day >>>> " + dy);
       
       Date date1 = addDate(new Date(), -3);
System.out.println(date1);
       String dth = formatDate("yyyyMMdd", date1);
       System.out.println(dth);
       System.out.println(formatDate("MMM dd yyyy hh:mm a, zzzz '('Z')'", new Date()));
       String date = formatDate("yyyyMMdd", new Date());
       System.out.println(date);
       System.out.println(date.substring(0, 2));
       System.out.println(date.substring(2, 4));
       System.out.println(date.substring(4, 6));
       System.out.println(date.substring(6, 8));

       NumberFormat numberFormat = NumberFormat.getInstance();
       numberFormat.setMaximumFractionDigits(2);       

       Random random = new Random();
       for (int i = 0; i < 10; i++) {
           double value = random.nextDouble();
           value = value * 10;
           value = Double.valueOf(numberFormat.format(value));
           System.out.println(value);
       }

       String dater = formatDate("", new Date());
       System.out.println(dater);

       double money = 100550000.75;
       NumberFormat formatter = new DecimalFormat("#, ###, ##0.00");
       System.out.println(formatter.format(money));*/
    }
}
