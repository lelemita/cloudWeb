package com.hasom.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.common.dao.AbstractDAO;

@Repository("eventLogDAO")
public class EventLogDAO extends AbstractDAO{

	// 테이블 목록 수 구하기
	public int getTotalCount(String tableName, String startTime, String endTime , String gs_code_arr, String kind) throws Exception{
		// tableName에서 g_no 구하기 ( ex. 'evt_1' )
		String[] arr = tableName.split("_");
		int g_no = Integer.parseInt(arr[1]);
		
		HashMap temp = new HashMap();
		temp.put("g_no", g_no);
		temp.put("tableName" , tableName);
		temp.put("startTime", startTime);
		temp.put("endTime", endTime);
		temp.put("gs_code_arr", gs_code_arr);
		temp.put("kind", kind);
		return (Integer) selectOne("eventLog.getTotalCount" ,temp);
	}
	// 테이블 전체 목록 구하기
	public ArrayList getTotalList(String tableName, String startTime, String endTime , String gs_code_arr, String kind) throws Exception {
		// tableName에서 g_no 구하기 ( ex. 'evt_1' )
		String[] arr = tableName.split("_");
		int g_no = Integer.parseInt(arr[1]);
		
		HashMap temp = new HashMap();
		temp.put("g_no", g_no);
		temp.put("tableName" , tableName);		
		temp.put("startTime", startTime);
		temp.put("endTime", endTime);
		temp.put("gs_code_arr", gs_code_arr);
		temp.put("kind", kind);		
		return (ArrayList) selectList("eventLog.getTotalList" ,temp);
	}
	// (rawData) 테이블 전체 목록 구하기

	
	
}//class
