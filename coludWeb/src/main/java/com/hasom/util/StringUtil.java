package com.hasom.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * 문자에 관련된 공통 기능을 만들어 사용할 클래스
 */
public class StringUtil {

	/*
	 *	1) 문자열의 존재여부를 알려주는 메소드
	 */
	public static boolean isNull(String data){
		boolean		result = false;
		if(data==null||data.length()==0){
			return true;
		}
		return result;
	}

	/*
	 *	2) \r\n ==> <br> 변환 메소드
	 */
	static public String toBR(String data) {
		if (isNull(data)) {
			return null;
		}
		else {
			data = data.replaceAll("\r\n", "<br>");
			return data;
		}
	}
	static public String forLine(String data, String target) {
		if (isNull(data)) {
			return null;
		}
		else {
			data = data.replaceAll("\r\n", target);
			return data;
		}
	}	
	
	
	/*
	 *	3) 한 문자열이 특정 문자열 을 포함하고 있는지 검사하는 메서드 
	 */
	static public boolean isInclude (String mother, String daughter) {
		return mother.contains(daughter);
	}
	
	/*
	 *	4) 날짜양식 지정 "yyyy-MM-dd"
	 */
	static public String dateForm (Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date.getTime());
	}
	static public String dateForm (Date date, String format) {
		SimpleDateFormat format2 = new SimpleDateFormat(format);
		return format2.format(date.getTime());
	}
	
	/*
	 *	5) 문자열이 너무 길면 자르고 ... 붙이기
	 */
	static public String splitString (String string, int count) {
		// original	: 자를 문자열
		// count	: 자를 기준 글자수
		if ( string != null) {
			if( string.length() > count ) {
				string = string.substring(0, count) + "...";
			}
		}
		return string;
	}
	
	/*
	 *  6) 숫자 앞에 지정한 자리수만큼 0으로 채우기
	 */
	static public String prependZero(int num, int len) {
		String strNum = num + "";
	    while(strNum.length() < len) {
	        strNum = "0" + strNum;
	    }
	    return strNum;
	}
	
	// 문자열 ==> 숫자 변환, 자연수 아니면 지정한 숫자를 반환하는 함수
	static public int isNaturalNum (int defaultNum , String strNum) {
		int result = defaultNum;
		try {
			result = Integer.parseInt(strNum);
			if (result < 0) {
				// 음수 ==> 자연수가 아님 ==> 지정한 숫자 반환
				result = defaultNum;		
			}
		} catch (Exception ex) {	
			// 숫자 형식이 아님 → 그대로 defaultNum 반환	
		}	
		return result;
	}//isNaturalNum()	
	
	
	static public final int forInteger = 1000;
	static public final int forString = 1002;
	// 특정 구분자 문자열 원하는 양식 ArrayList로 구분
	static public ArrayList makeList(String data, String token, int dataType) {
		ArrayList result = new ArrayList();
		String[] array = data.split(token);
		for(String temp : array) {
			if(dataType == forInteger){
				result.add( (Integer.parseInt(temp.trim())) );
			}else{
				result.add(temp.trim());
			}
		}
		return result;
	}
	
}//클래스
