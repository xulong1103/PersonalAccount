package com.system.personalaccount.util;

/**
 * Created by mine on 2016/7/8.
 * 日期转换
 */
public class ChangeDay {

    /** 一年的第几周 **/
    private static int week_In_Year;

    /**
     * 转换天
     * @param date 当前日期
     * @param type 转换类型
     * @param name 上一天或者下一天
     */
    public static void changeDays(String date[], String type, String name) {
        String[] nowDate = new String[3];
        /** 上一天 **/
        if (name.equals("yesterday")) {
            /** 是否当前日期为今天 **/
            if (type.equals("now")) {
                /** 是否为某月的第一天 **/
                if (!date[2].equals("01")) {
                    /** 日期减一，若日期为个位数，前面补0 **/
                    if (String.valueOf(Integer.parseInt(date[2]) - 1).length() == 1) {
                        date[2] = "0" + String.valueOf(Integer.parseInt(date[2]) - 1);
                    } else {
                        date[2] = String.valueOf(Integer.parseInt(date[2]) - 1);
                    }
                }
                /** 某月1日的上一天 **/
                else {
                    /** 是否是1月 **/
                    if (date[1].equals("01")) {
                        /** 月01天为01,转换上一年的最后一天 **/
                        date[0] = String.valueOf(Integer.parseInt(date[0]) - 1);
                        date[1] = "12";
                        date[2] = "31";
                    }
                    /** 不为一月 **/
                    else {
                        /** 月不为01天为01，若日为个位数，前面补0 **/
                        if (String.valueOf(Integer.parseInt(date[1]) - 1).length() == 1) {
                            date[1] = "0" + String.valueOf(Integer.parseInt(date[1]) - 1);
                        } else {
                            date[1] = String.valueOf(Integer.parseInt(date[1]) - 1);
                        }
                        /** 取得对应天数 **/
                        GetDays getDays = new GetDays(Integer.parseInt(date[0]), Integer.parseInt(date[1]));
                        date[2] = String.valueOf(getDays.getDay());
                    }
                }
            }
            /** 当前日期不为今天 **/
            else if (type.equals("")) {
                /** 是否为某月的第一天 **/
                if (!date[2].equals("01")) {
                    /** 日期减一，若日为个位数，前面补0 **/
                    if (String.valueOf(Integer.parseInt(date[2]) - 1).length() == 1) {
                        date[2] = "0" + String.valueOf(Integer.parseInt(date[2]) - 1);
                    } else {
                        date[2] = String.valueOf(Integer.parseInt(date[2]) - 1);
                    }
                }
                /** 某月的第一天 **/
                else {
                    if (date[1].equals("01")) {
                        /** 月01天为01 **/
                        date[0] = String.valueOf(Integer.parseInt(date[0]) - 1);
                        date[1] = "12";
                        date[2] = "31";
                    } else {
                        /** 月不为01 天为01 **/
                        if (String.valueOf(Integer.parseInt(date[1]) - 1).length() == 1) {
                            date[1] = "0" + String.valueOf(Integer.parseInt(date[1]) - 1);
                        } else {
                            date[1] = String.valueOf(Integer.parseInt(date[1]) - 1);
                        }
                        /** 获取对应天数 **/
                        GetDays getDays = new GetDays(Integer.parseInt(date[0]), Integer.parseInt(date[1]));
                        date[2] = String.valueOf(getDays.getDay());
                    }
                }
            }
        }
        /** 下一天 **/
        else if (name.equals("morning")) {
            /** 当前日期是否为今天 **/
            if (type.equals("now")) {
                /** 获取对应天数 **/
                GetDays getDays = new GetDays(Integer.parseInt(date[0]), Integer.parseInt(date[1]));
                int days = getDays.getDay();
                /** 判断是否是这一月的最后一天 **/
                if (!date[2].equals(String.valueOf(days))) {
                    if (String.valueOf(Integer.parseInt(date[2]) + 1).length() == 1) {
                        date[2] = "0" + String.valueOf(Integer.parseInt(date[2]) + 1);
                    } else {
                        date[2] = String.valueOf(Integer.parseInt(date[2]) + 1);
                    }
                } else {
                    if (date[1].equals("12")) {
                        /** 月01天为01 **/
                        date[0] = String.valueOf(Integer.parseInt(date[0]) + 1);
                        date[1] = "01";
                        date[2] = "01";
                    } else {
                        /** 月不为01天为01 **/
                        if (String.valueOf(Integer.parseInt(date[1]) + 1).length() == 1) {
                            date[1] = "0" + String.valueOf(Integer.parseInt(date[1]) + 1);
                        } else {
                            date[1] = String.valueOf(Integer.parseInt(date[1]) + 1);
                        }
                        date[2] = "01";
                    }
                }
            }
            /** 不是今天 **/
            else if (type.equals("")) {
                /** 获取天数 **/
                GetDays getDays = new GetDays(Integer.parseInt(date[0]), Integer.parseInt(date[1]));
                int days = getDays.getDay();
                /** 判断是否是最后一天 **/
                if (!date[2].equals(String.valueOf(days))) {
                    if (String.valueOf(Integer.parseInt(date[2]) + 1).length() == 1) {
                        date[2] = "0" + String.valueOf(Integer.parseInt(date[2]) + 1);
                    } else {
                        date[2] = String.valueOf(Integer.parseInt(date[2]) + 1);
                    }
                } else {
                    if (date[1].equals("12")) {
                        /** 月01天为01 **/
                        date[0] = String.valueOf(Integer.parseInt(date[0]) + 1);
                        date[1] = "01";
                        date[2] = "01";
                    } else {
                        /** 月不为01天为01 **/
                        if (String.valueOf(Integer.parseInt(date[1]) + 1).length() == 1) {
                            date[1] = "0" + String.valueOf(Integer.parseInt(date[1]) + 1);
                        } else {
                            date[1] = String.valueOf(Integer.parseInt(date[1]) + 1);
                        }
                        date[2] = "01";
                    }
                }

            }
        }
    }

    /**
     * 周的转换
     * @param week 当前周
     * @param date 当前日期
     * @param type 是否为本周
     * @param name 上一周、下一周
     * @return
     */
    public static int changeWeekOrMonthYear(int week, String[] date, String type, String name) {
        week_In_Year = week;
        /** 下一周 **/
        if (name.equals("nextWeek")) {
            /** 如果当前日期是本周 **/
            if (type.equals("now")) {
                /** 每年53周，小于53周就增加 **/
                if (week_In_Year != 53) {
                    week_In_Year = week_In_Year + 1;
                } else {
                    week_In_Year = 1;
                    date[0] = String.valueOf(Integer.parseInt(date[0]) + 1);
                }
            }
            /** 不是本周 **/
            else {
                /** 每年53周 **/
                if (week_In_Year != 53) {
                    week_In_Year = week_In_Year + 1;
                } else {
                    week_In_Year = 1;
                    date[0] = String.valueOf(Integer.parseInt(date[0]) + 1);
                }
            }
        }
        /** 上一周 **/
        else if (name.equals("lastWeek")) {
            /** 判断当前日期是否是本周 **/
            if (type.equals("now")) {
                /** 判断是否是第一周 **/
                if (week_In_Year != 1) {
                    week_In_Year = week_In_Year - 1;
                } else {
                    week_In_Year = 53;
                    date[0] = String.valueOf(Integer.parseInt(date[0]) - 1);
                }
            }
            /** 不是本周 **/
            else if (type.equals("")) {
                /** 判断是否是第一周 **/
                if (week_In_Year != 1) {
                    week_In_Year = week_In_Year - 1;
                } else {
                    week_In_Year = 53;
                    date[0] = String.valueOf(Integer.parseInt(date[0]) - 1);
                }
            }
        }
        return week_In_Year;
    }

    /**
     * 转换月份
     * @param date 当前日期
     * @param type 是否是本月
     * @param name 上一月、下一月
     */
    public static void changeMonth(String[] date, String type, String name) {
        /** 上一月 **/
        if (name.equals("lastMonth")) {
            /** 是否是本月 **/
            if (type.equals("now")) {
                /** 是否为1月 **/
                if (!date[1].equals("01")) {
                    /** 月份减一，若是个位数前面补0 **/
                    if (String.valueOf(Integer.parseInt(date[1]) - 1).length() == 1) {
                        date[1] = "0" + String.valueOf(Integer.parseInt(date[1]) - 1);
                    } else {
                        date[1] = String.valueOf(Integer.parseInt(date[1]) - 1);
                    }
                } else {
                    date[0] = String.valueOf(Integer.parseInt(date[0]) - 1);
                    date[1] = "12";
                }
            }
            /** 不是本月 **/
            else {
                /** 是否为1月 **/
                if (!date[1].equals("01")) {
                    /** 月份减一，若是个位数前面补0 **/
                    if (String.valueOf(Integer.parseInt(date[1]) - 1).length() == 1) {
                        date[1] = "0" + String.valueOf(Integer.parseInt(date[1]) - 1);
                    } else {
                        date[1] = String.valueOf(Integer.parseInt(date[1]) - 1);
                    }
                } else {
                    date[0] = String.valueOf(Integer.parseInt(date[0]) - 1);
                    date[1] = "12";
                }
            }
        }
        /** 下一月 **/
        else if(name.equals("nextMonth")){
            /** 是否是本月 **/
            if (type.equals("now")) {
                /** 是否是12月 **/
                if (!date[1].equals("12")) {
                    /** 月份加一，若是个位数前面补0 **/
                    if (String.valueOf(Integer.parseInt(date[1]) + 1).length() == 1) {
                        date[1] = "0" + String.valueOf(Integer.parseInt(date[1]) + 1);
                    } else {
                        date[1] = String.valueOf(Integer.parseInt(date[1]) + 1);
                    }
                } else {
                    date[0] = String.valueOf(Integer.parseInt(date[0]) + 1);
                    date[1] = "01";
                }
            }
            /** 不是本月 **/
            else {
                /** 是否是12月 **/
                if (!date[1].equals("12")) {
                    /** 月份加一，若是个位数前面补0 **/
                    if (String.valueOf(Integer.parseInt(date[1]) + 1).length() == 1) {
                        date[1] = "0" + String.valueOf(Integer.parseInt(date[1]) + 1);
                    } else {
                        date[1] = String.valueOf(Integer.parseInt(date[1]) + 1);
                    }
                } else {
                    date[0] = String.valueOf(Integer.parseInt(date[0]) + 1);
                    date[1] = "01";
                }
            }
        }
    }

    /**
     * 转换年份
     * @param date 当前日期
     * @param type 是否是本年
     * @param name 上一年、下一年
     */
    public static void changeYear(String[] date,String type,String name){
        /** 上一年 **/
        if(name.equals("lastYear")) {
            /** 是否是本年 **/
            if (type.equals("本年")) {
                date[0] = String.valueOf(Integer.parseInt(date[0]) - 1);
            } else {
                date[0] = String.valueOf(Integer.parseInt(date[0]) - 1);
            }
        }
        /** 下一年 **/
        else if(name.equals("nextYear")){
            /** 是否是本年 **/
            if (type.equals("本年")) {
                date[0] = String.valueOf(Integer.parseInt(date[0]) + 1);
            } else {
                date[0] = String.valueOf(Integer.parseInt(date[0]) + 1);
            }
        }
    }

}
