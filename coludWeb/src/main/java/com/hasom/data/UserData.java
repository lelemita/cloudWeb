package com.hasom.data;

import java.util.ArrayList;

/* 2017.03.21.남연우.
 * 담당자의 사용자 등록,조회 등을 할 때 사용하는 VO 
 */
public class UserData {
	
	//사용자 정보
	private String u_id     ;
	private String u_pw     ;
	private String c_no   	;
	private String c_name   ;
	private String u_type   ;
	private String u_state  ;
	private String u_name   ;
	private String u_tel    ;
	private String u_mail   ;
	private String u_tel_on ;
	private String u_mail_on;
	private String u_report ;
	// 암호 변경하는 경우
	private String new_pw;
	// 담당 그룹 목록
	private ArrayList g_no_g_names;
	// 담당 그룹 체크박스 선택 정보
	private String[] checkedGroups;
	
	
	// type, state 내용을 반환하는 메서드
	public String getStrType() {
		String strType = "정의되지 않음";
		if(u_type != null) {
			if(u_type.equals("U")) {
				strType="일반";
			}
			else if(u_type.equals("M")) {
				strType="관리자";
			}
			else if(u_type.equals("A")) {
				strType="담당자";
			}
			else if(u_type.equals("H")) {
				strType="하솜직원";
			}
		}
		return strType;
	}
	public String getStrState() {
		String strState = "정의되지 않음";
		if(u_state != null) {
			if(u_state.equals("Y")) {
				strState="정상";
			}
			else if(u_state.equals("N")) {
				strState="차단";
			}
			else if(u_state.equals("X")) {
				strState="탈퇴";
				// 사실 이 거는 나오면 안된다.
			}
		}
		return strState;
	}	
	
	//getter & setter
	public String getU_id() {
		return u_id;
	}
	public void setU_id(String u_id) {
		this.u_id = u_id;
	}
	public String getU_pw() {
		return u_pw;
	}
	public void setU_pw(String u_pw) {
		this.u_pw = u_pw;
	}
	public String getU_type() {
		return u_type;
	}
	public void setU_type(String u_type) {
		this.u_type = u_type;
	}
	public String getU_state() {
		return u_state;
	}
	public void setU_state(String u_state) {
		this.u_state = u_state;
	}
	public String getU_name() {
		return u_name;
	}
	public void setU_name(String u_name) {
		this.u_name = u_name;
	}
	public String getU_tel() {
		return u_tel;
	}
	public void setU_tel(String u_tel) {
		this.u_tel = u_tel;
	}
	public String getU_mail() {
		return u_mail;
	}
	public void setU_mail(String u_mail) {
		this.u_mail = u_mail;
	}
	public String getU_tel_on() {
		return u_tel_on;
	}
	public void setU_tel_on(String u_tel_on) {
		this.u_tel_on = u_tel_on;
	}
	public String getU_mail_on() {
		return u_mail_on;
	}
	public void setU_mail_on(String u_mail_on) {
		this.u_mail_on = u_mail_on;
	}
	public String getU_report() {
		return u_report;
	}
	public void setU_report(String u_report) {
		this.u_report = u_report;
	}
	public ArrayList getG_no_g_names() {
		return g_no_g_names;
	}
	public void setG_no_g_names(ArrayList g_no_g_names) {
		this.g_no_g_names = g_no_g_names;
	}
	public String getNew_pw() {
		return new_pw;
	}
	public void setNew_pw(String new_pw) {
		this.new_pw = new_pw;
	}
	public String[] getCheckedGroups() {
		return checkedGroups;
	}
	public void setCheckedGroups(String[] checkedGroups) {
		this.checkedGroups = checkedGroups;
	}
	
	public String getC_no() {
		return c_no;
	}
	public void setC_no(String c_no) {
		this.c_no = c_no;
	}	
	public String getC_name() {
		return c_name;
	}
	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	
}//class
