package com.hasom.data;

import java.util.Date;

/* 2017.02.16. 남연우.
 * 이벤트 로그 조회를 위한 데이터 빈
 */
public class EventData {

	private int evt_no;
	private Date evt_date;
	private String s_display;
	private String f_name;
	private String e_value;
	private String evt_msg;
	public int getEvt_no() {
		return evt_no;
	}
	public void setEvt_no(int evt_no) {
		this.evt_no = evt_no;
	}
	public Date getEvt_date() {
		return evt_date;
	}
	public void setEvt_date(Date evt_date) {
		this.evt_date = evt_date;
	}
	public String getS_display() {
		return s_display;
	}
	public void setS_display(String s_display) {
		this.s_display = s_display;
	}
	public String getF_name() {
		return f_name;
	}
	public void setF_name(String f_name) {
		this.f_name = f_name;
	}
	public String getE_value() {
		return e_value;
	}
	public void setE_value(String e_value) {
		this.e_value = e_value;
	}
	public String getEvt_msg() {
		return evt_msg;
	}
	public void setEvt_mg(String evt_msg) {
		this.evt_msg = evt_msg;
	}
	
	
	
}//class
