/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etranzact.drs.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 *
 * @author Akachukwu
 */
public class DateUtility {

    public String getDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String today = sdf.format(new Date());
        return today;
    }

    public String getDate(Date d) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String today = sdf.format(d);
        return today;
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

    public String dateTimeStamp() {
        Date d = new Date();
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
            String sdate = yr + "" + mn + "" + dd + "" + hh + "" + mm + "" + ss;
            //System.out.println(d + " >>>>>>>>>>>>>>>>> " + sdate);
            return sdate;
        } else {
            return null;
        }
    }

    public Date convertStringToDate(String strDate) {
        Date theDate = null;
        SimpleDateFormat sdf = null;
        if (strDate.contains("-")) {
            sdf = new SimpleDateFormat("dd-MM-yyyy");
        } else {
            sdf = new SimpleDateFormat("dd/MM/yyyy");
        }
        try {

            theDate = sdf.parse(strDate);


        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(theDate);
        return theDate;
    }

    public Date convertStringToDateyyyymmdd(String strDate) {
        Date theDate = null;
        System.out.println(strDate + " strDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {

            theDate = sdf.parse(strDate);


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(theDate + " theDate");
        return theDate;
    }

    public Date calculateDueDate(Date d, String id, String type) {
        if (d != null) {
            int channelID = -1;
            try {
                channelID = Integer.parseInt(id);
            } catch (Exception s) {
            }
            int daySpace = 0;
            if (channelID == 4) {
                daySpace = 3;
            } else if (channelID == 1 || channelID == 2 || channelID == 3) {
                daySpace = 1;
            }
            if (type != null && type.equals("T")) {
                daySpace = 3;
            } else if (type != null && type.equals("P"))//This has exeedec 120 dyas
            {
                daySpace = 1;
            }

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(d);
            GregorianCalendar oldDate = new GregorianCalendar();
            oldDate.setTime(d);
            GregorianCalendar oldDatef = new GregorianCalendar();
            oldDatef = oldDate;
            GregorianCalendar oldDatesv = new GregorianCalendar();
            oldDatesv.setTime(d);
            gc.add(GregorianCalendar.DATE, daySpace);
            int workingDays = 0;
            while (oldDate.before(gc)) {
                int day = oldDate.get(gc.DAY_OF_WEEK);
                System.out.println(day);
                if ((day == gc.SATURDAY)) {
                    workingDays = workingDays + 1;
                }

                oldDate.add(gc.DATE, 1);
            }
            gc.add(gc.DATE, workingDays);

            
            oldDate = oldDatef;
            int workingDays2 = 0;
            while (oldDate.before(gc)) {
                int day = oldDate.get(gc.DAY_OF_WEEK);
                System.out.println(day);
                if ((day == gc.SUNDAY)) {
                    workingDays2 = workingDays2 + 1;
                }

                oldDate.add(gc.DATE, 1);
            }
            gc.add(gc.DATE, workingDays2);

            return gc.getTime();
        }
        return null;
    }

    public Date calculateDueDate(Date d) {
        if (d != null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(d);
            gc.add(gc.DATE, 7);
            return gc.getTime();
        }
        return null;
    }

    public Date getTomorrow() {
        Date d = new Date();
        if (d != null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(d);
            gc.add(gc.DATE, 1);
            return gc.getTime();
        }
        return null;
    }

    public Date getTomorrow(Date d) {
        //Date d = new Date();
        if (d != null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(d);
            gc.add(gc.DATE, 1);
            return gc.getTime();
        }
        return null;
    }

//    public String getTimeStamp() {
//        java.util.Date date = new Date();
//        GregorianCalendar gc = new GregorianCalendar();
//        gc.setTime(date);
//        String yr = gc.get(gc.YEAR) + "";
//        String mn = (gc.get(gc.MONTH) + 1) + "";
//        String dd = gc.get(gc.DATE) + "";
//        int hhInt = gc.get(gc.HOUR);
//        String mm = gc.get(gc.MINUTE) + "";
//        String ss = gc.get(gc.SECOND) + "";
//        int am_pm = gc.get(gc.AM_PM);
//        if (am_pm > 0)//pm
//        {
//            hhInt = 12 + hhInt;
//        }
//        String hh = hhInt + "";
//        String sdate = yr + "" + mn + "" + dd + "" + hh + "" + mm + "" + ss;//yyyymmddhhmmss
//
//    }
    public long getDateDiff(Date date1, Date date2) {
        if (date1 == null) {
            date1 = new Date();
        }
        if (date2 == null) {
            date2 = new Date();
        }
        long daydiff = (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);

        return daydiff;
    }

    public boolean checkexpiration(Date date1, Date date2, int durration) {
        return this.getDateDiff(date1, date2) > durration;
    }

    public static void main(String k[]) {
        DateUtility ut = new DateUtility();

        System.out.println("Due Date " + ut.calculateDueDate(ut.convertStringToDateyyyymmdd("2012-01-31 09:34:14"), "1", "P"));
//        try {
//            //System.out.println(ut.formatDate(new Date()));
//
//            Random rn = new Random();
//            System.out.println(rn.nextInt(9));
//        } catch (Exception s) {
//        }
    }
}
