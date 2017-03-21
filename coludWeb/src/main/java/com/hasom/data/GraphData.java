package com.hasom.data;

import com.hasom.util.ServletUtil;
import com.hasom.util.StringUtil;

/* 170316 남연우
 * 그래프 관련 데이터 빈
 */
public class GraphData {

	//현재 선택 그룹
	private String nowGroup;
	//현재 선택 요소
	private String nowFactor;
	private String[] nowSensor;
	//조회기간
	private String startDay;
	private String endDay;
	private String startTime;
	private String endTime;
	//그래프 활성화된 센서 번호 목록
	private String checkedSensor;
	
	
	//getters & Setters
	public String getNowGroup() {
		return nowGroup;
	}
	public String[] getNowSensor() {
		return nowSensor;
	}
	public void setNowSensor(String[] nowSensor) {
		this.nowSensor = nowSensor;
	}
	public void setNowGroup(String nowGroup) {
		this.nowGroup = nowGroup;
	}
	public String getNowFactor() {
		return nowFactor;
	}
	public void setNowFactor(String nowFactor) {
		this.nowFactor = nowFactor;
	}
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCheckedSensor() {
		return checkedSensor;
	}
	public void setCheckedSensor(String checkedSensor) {
		this.checkedSensor = checkedSensor;
	}	
	
}//class
