package com.hasom.data;

public class JobData {
	// jobTable 구성요소
	private int j_no     ;
	private int g_no     ;
	private String j_name   ;
	private String j_text   ;
	private String j_target ;
	private String j_class  ;
	// 뷰에서 수신대상 체크박스값 받아오기 위한 배열
	private String[] targets;
	// 기타 필요 요소
	private String g_name ;
	private int e_no      ;
	private int order     ;
	private String workType;
	private String j_nos;
	private int no      ;
	private int newj_no;
	
	
	// 편집
	// textarea용 <br> → \r\n
	public String getJ_textLF() {
		return (j_text==null)? "" : j_text.replaceAll("<br>", "\r\n");
	}
	
	// e_no 의미
	public String getStrE_no() {
		switch(e_no){
		case -1 : return "통신장애";
		case 1	: return "통신복구";
		case -1001	: return "상한일탈";
		case 1001	: return "상한복구";
		case -1002	: return "하한일탈";
		case 1002	: return "하한복구";
		default		: return "정의되지 않은 이벤트";
		}
	}
	
	// j_class 의미
	public String getStrJ_class() {
		if(j_class==null) { 
			return ""; 
		}else if (j_class.equals("KAKAO_A")) {
			return "알림톡";
		}else if (j_class.equals("KAKAO_F")) {
			return "친구톡";
		}else {
			return j_class;
		}
	}
	
	// j_target 의미
	public String getStrTarget(){
		if(j_target==null) { return "";}
		StringBuffer result = new StringBuffer();
		int isAll = 0;
		if(j_target.indexOf("U")>=0){
			result.append("일반<br>");
			isAll++;
		}
		if(j_target.indexOf("M")>=0){
			result.append("관리자<br>");
			isAll++;
		}
		if(j_target.indexOf("A")>=0){
			result.append("담당자<br>");
			isAll++;
		}
		if(isAll>=3) {
			return "모두";
		}
		return result.toString();
	}
	
	// 뷰에서 받은 checkBox 결과를 문장으로 만드는 메서드
	public String getJ_targetInput(){
		StringBuffer buff = new StringBuffer();
		int lastIndex = targets.length-1;
		for(int i=0;i<lastIndex;i++) {
			buff.append("\'" + targets[i] + "\'|");
		}
		buff.append("\'" + targets[lastIndex] + "\'");
		return buff.toString();
	}
	
	
	// getter & setter
	public int getJ_no() {
		return j_no;
	}
	public void setJ_no(int j_no) {
		this.j_no = j_no;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	public int getG_no() {
		return g_no;
	}
	public void setG_no(int g_no) {
		this.g_no = g_no;
	}
	public String getJ_name() {
		return j_name;
	}
	public void setJ_name(String j_name) {
		this.j_name = j_name;
	}
	public String getJ_text() {
		return j_text;
	}
	public void setJ_text(String j_text) {
		this.j_text = j_text;
	}
	public String getJ_target() {
		return j_target;
	}
	public void setJ_target(String j_target) {
		this.j_target = j_target;
	}
	public String getJ_class() {
		return j_class;
	}
	public void setJ_class(String j_class) {
		this.j_class = j_class;
	}
	public String[] getTargets() {
		return targets;
	}
	public void setTargets(String[] targets) {
		this.targets = targets;
	}
	public String getG_name() {
		return g_name;
	}
	public void setG_name(String g_name) {
		this.g_name = g_name;
	}
	public int getE_no() {
		return e_no;
	}
	public void setE_no(int e_no) {
		this.e_no = e_no;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getJ_nos() {
		return j_nos;
	}
	public void setJ_nos(String j_nos) {
		this.j_nos = j_nos;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getNewj_no() {
		return newj_no;
	}
	public void setNewj_no(int newj_no) {
		this.newj_no = newj_no;
	}

	
}//class
