package com.vito.holiday.util;

import java.util.Calendar;

/**
 * @Description: 日历数据对象
 * @author wutp 2017年5月15日
 * @version 1.0
 */
public class CalendarDO {
	
	private int year = 2017;
	private int month = 5;

	public CalendarDO() {
		super();
	}

	public CalendarDO(int year, int month) {
		super();
		this.year = year;
		this.month = month;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMonth() {
		return month;
	}

	/**
	 * @Description: 获得日历
	 * @auther: wutp 2017年5月15日
	 * @return
	 * @return String[]
	 */
	public String[] getCalendar() {
		String days[] = new String[42];
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int day = 0;
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			day = 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			day = 30;
		}
		if (month == 2) {
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
				day = 29;
			} else {
				day = 28;
			}
		}
		for (int i = week, n = 1; i < week + day; i++) {
			days[i] = String.valueOf(n);
			n++;
		}
		return days;
	}
	
}
