package com.hasom.data;

public class JobData {

	private int j_no     ;
	private int g_no     ;
	private String j_name   ;
	private String j_text   ;
	private String j_target ;
	private String j_class  ;
	// 뷰에서 수신대상 체크박스값 받아오기 위한 배열
	private String[] targets;
	
	// 편집
	public String getStrTarget(){
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

	
}
