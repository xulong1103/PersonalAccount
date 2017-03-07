package com.system.personalaccount.util;

/**
 * Created by mine on 2016/7/7.
 * 获取某一年某一月份的天数
 */
public class GetDays {
    private static int days;

    public GetDays(int year, int month) {
        int dayInMonth = 0;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            if (month == 0) {
                dayInMonth = 0;
            } else if (month == 1) {
                dayInMonth = 31;
            } else if (month == 2) {
                dayInMonth = 29;
            } else if (month == 3) {
                dayInMonth = 31;
            } else if (month == 4) {
                dayInMonth = 30;
            } else if (month == 5) {
                dayInMonth = 31;
            } else if (month == 6) {
                dayInMonth = 30;
            } else if (month == 7) {
                dayInMonth = 31;
            } else if (month == 8) {
                dayInMonth = 31;
            } else if (month == 9) {
                dayInMonth = 30;
            } else if (month == 10) {
                dayInMonth = 31;
            } else if (month == 11) {
                dayInMonth = 30;
            } else if (month == 12) {
                dayInMonth = 31;
            }

        } else {
            if (month == 0) {
                dayInMonth = 0;
            } else if (month == 1) {
                dayInMonth = 31;
            } else if (month == 2) {
                dayInMonth = 28;
            } else if (month == 3) {
                dayInMonth = 31;
            } else if (month == 4) {
                dayInMonth = 30;
            } else if (month == 5) {
                dayInMonth = 31;
            } else if (month == 6) {
                dayInMonth = 30;
            } else if (month == 7) {
                dayInMonth = 31;
            } else if (month == 8) {
                dayInMonth = 31;
            } else if (month == 9) {
                dayInMonth = 30;
            } else if (month == 10) {
                dayInMonth = 31;
            } else if (month == 11) {
                dayInMonth = 30;
            } else if (month == 12) {
                dayInMonth = 31;
            }
        }
        days = dayInMonth;
    }

    public int getDay() {
        return days;
    }
}
