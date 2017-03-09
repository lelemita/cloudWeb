package com.hasom.data;

/* 2017.02.13.남연우.
 * 표조회를 위한 데이터 빈
 */
public class TableData {

	private int year;
	private int month;
	private int day;
	private int hour;
	private int gr;		// 분/unitTime
	private int value;	// 평균 값
	
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getGr() {
		return gr;
	}
	public void setGr(int gr) {
		this.gr = gr;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	
		
}//class
