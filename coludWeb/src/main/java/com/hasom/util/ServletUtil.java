package com.hasom.util;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

/*	160928
 * 	Request 관련 유틸
 */
public class ServletUtil {

	/*	
	 *  request ==> int nowPage 구하기 (기본: 1)
	 */
	static public int getNowPage (HttpServletRequest req) {
		String strNowPage = req.getParameter("nowPage");
		if ( StringUtil.isNull(strNowPage) ) {
			strNowPage = "1";
		}
		int nowPage = 1;
		try { 
			nowPage = Integer.parseInt(strNowPage);
		} catch (Exception ex) {}
		return nowPage;
	}

	// yyyy-MM-dd  파라메터 이름으로 해당 날짜 찾기 (값 없으면 현재 날짜)
	static public String getDay(HttpServletRequest req, String dayParameter) throws Exception{
		int year, month, date;
		String strDay	= req.getParameter( dayParameter);
		Calendar cal = Calendar.getInstance();	// 오늘날짜, 현재시각으로 생성됨
		if (strDay==null || strDay.length() < 0) {
			// 값 없으면 오늘 (0~24)
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;
			date = cal.get(Calendar.DATE);
			if (dayParameter.indexOf("end")>=0) {
				// 값 없음 → 당일 0시 ~ 다음날 0시 조회
				date = date +1;
			}
			strDay = year + "-" + StringUtil.prependZero(month,2) + "-" + date ;
		}		
		return strDay;
	}		

	// HH:00:00 파라메터 이름으로 해당 시각 찾기 (값 없으면 0)
	static public int getTime (HttpServletRequest req, String timeParameter) throws Exception{
		// 값 없으면 0
		int hour = 0;
		String strTime = req.getParameter(timeParameter);
		if (strTime != null && strTime.length() > 0) {
			hour = Integer.parseInt(strTime);
		}
		return hour;
	}
	
	// yyyy-MM-dd  파라메터로 해당 날짜 찾기 (값 없으면 현재 날짜+지정일)
	static public String getDay(String strDay, int addDay) throws Exception{
		int year, month, date;
		Calendar cal = Calendar.getInstance();	// 오늘날짜, 현재시각으로 생성됨
		if (strDay==null || strDay.length() < 0) {
			// 값 없으면 오늘 (0~24)
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;
			date = cal.get(Calendar.DATE) + addDay;
			strDay = year + "-" + StringUtil.prependZero(month,2) + "-" + date ;
		}		
		return strDay;
	}		

	// HH:00:00 파라메터로 해당 시각 찾기 (값 없으면 지정 시각)
	static public int getTime (String strTime, int defaultTime) throws Exception{
		// 값 없고, default가 유효하지 않으면 0
		int hour = 0;
		if (strTime != null && strTime.length() > 0) {
			hour = Integer.parseInt(strTime);
		}else if(defaultTime >= 0 && defaultTime <23 ){
			hour = defaultTime;
		}
		return hour;
	}	
	
	
	// "2017-02-01" , "HH:00:00" 양식 문자열로 받은 객체를 켈린더로 반환함
	static public Calendar getCalendar(String date , int hour) {
		String[] strDate = date.split("-");
		Calendar cal = Calendar.getInstance();
		int[] dates = new int[3];
		for (int i=0 ; i<3 ; i++) {
			dates[i] = Integer.parseInt(strDate[i]);
		}
		cal.set(dates[0], dates[1]-1, dates[2], hour, 0 , 0); // Calendar의 달은  0~11
		return cal;
	}
	
}// 클래스
