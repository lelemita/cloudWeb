package com.hasom.data;

import java.util.ArrayList;
import java.util.Date;

/* 2016.12.29.남연우
 * 센서 하나를 보여줄 때 필요한 데이터
 *  ...VO 만들어두고, Service에서 일일히 파싱함...ㅠㅠ
 *  SQL에서 resultType으로 그냥 지정하면 되는데...
 *  (물론, 지금 상태로 바로 그렇게 하면 안됨.)
 */
public class SensorData {

	// 센서 번호 
	private int	gs_no;
	// 센서 위치 표시
	private String 		s_display;
	// 소속 요소 별 단위, 값, 측정시간, 상/하한값
	private	ArrayList	f_units;
	private ArrayList	values;
	private Date		date;
	private	ArrayList	l_highs;
	private ArrayList	l_lows;	
	
	public ArrayList getL_highs() {
		return l_highs;
	}
	public void setL_highs(ArrayList l_highs) {
		this.l_highs = l_highs;
	}
	public ArrayList getL_lows() {
		return l_lows;
	}
	public void setL_lows(ArrayList l_lows) {
		this.l_lows = l_lows;
	}
	public String getS_display() {
		return s_display;
	}
	public void setS_display(String s_display) {
		this.s_display = s_display;
	}
	public int getGs_no() {
		return gs_no;
	}
	public void setGs_no(int gs_no) {
		this.gs_no = gs_no;
	}
	public ArrayList getF_units() {
		return f_units;
	}
	public void setF_units(ArrayList f_units) {
		this.f_units = f_units;
	}
	public ArrayList getValues() {
		return values;
	}
	public void setValues(ArrayList values) {
		this.values = values;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	
	
}//class
