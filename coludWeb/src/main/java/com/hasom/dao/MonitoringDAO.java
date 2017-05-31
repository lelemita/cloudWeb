package com.hasom.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.common.dao.AbstractDAO;

@Repository("monitoringDAO")
public class MonitoringDAO extends AbstractDAO{

	// 그 아이디[u_id]가 관리하는 그룹 번호(g_no[]) 목록 확인 (Group_user)
	public ArrayList getG_nos(String u_id)  throws Exception{
		return (ArrayList) selectList("monitoring.getG_nos" , u_id);
	}
	
	// 모든 g_no[]에 대한 g_name[] 확인
	public ArrayList getG_names(String u_id)  throws Exception{
		return (ArrayList) selectList("monitoring.getG_names" , u_id);
	}

	public int getG_cl_period(int g_no)  throws Exception{
		return (Integer) selectOne("monitoring.getG_cl_period" , g_no);
	}
	
	public int getG_m_period(int g_no)  throws Exception{
		return (Integer) selectOne("monitoring.getG_m_period" , g_no);
	}
	public int getG_m_off(int g_no)  throws Exception{
		return (Integer) selectOne("monitoring.getG_m_off" , g_no);
	}

	// 해당 그룹(g_no)에 설치된 센서(gs_no[]) 목록 확인 (Group_sensor)
	public ArrayList getGs_nos(int g_no) throws Exception {
		return (ArrayList) selectList("monitoring.getGs_nos" , g_no);
	}
	
	
	// (모니터링) gs_no[i] ==> s_display 확인
	public String getS_display(int gs_no)  throws Exception{
		return (String) selectOne("monitoring.getS_display" , gs_no);
	}

	// gs_no ==> 각 센서 요소(sf_no[]) 목록 확인 (Group_sensor + Sensor_Factor [s_no조인] )
	public ArrayList getSf_nos(int gs_no)  throws Exception{
		return (ArrayList) selectList("monitoring.getSf_nos" , gs_no);
	}

	// gs_no & sf_no ==> l_table_name , high/low
	public HashMap getFromLimitTable(HashMap paraMap)  throws Exception{
		return (HashMap) selectOne("monitoring.getFromLimitTable" , paraMap);
	}
	// gs_no & sf_no ==> l_no , high/low
	public HashMap getFromLimitTable1(HashMap paraMap)  throws Exception{
		return (HashMap) selectOne("monitoring.getFromLimitTable1" , paraMap);
	}
	
	// sf_no ==> f_unit[i] 확인
	public String getF_unit(int sf_no)  throws Exception{
		return (String) selectOne("monitoring.getF_unit", sf_no);
	}

	// g_no, gs_no, table_name ==> value[i], date[i] 저장
	public Date getLastDate(HashMap table_name)  throws Exception{
		return (Date) selectOne("monitoring.getLastDate", table_name);
	}
	public String getLastValue(HashMap map)  throws Exception{
		return (String) selectOne("monitoring.getLastValue", map);
	}
	public String getLastValue2(HashMap map)  throws Exception{
		return (String) selectOne("monitoring.getLastValue2", map);
	}

	//	해당 그룹의 모든 센서 설치 위치 목록
	public ArrayList getS_displays(int g_no) throws Exception {
		return (ArrayList) selectList("monitoring.getS_displays", g_no);
	}
	
	//	해당 그룹의 모든 측정요소 목록 (중복제외)
	public ArrayList getF_names(int g_no)throws Exception {
		return (ArrayList) selectList("monitoring.getF_names", g_no);
	}
	
	// (rawData) 테이블 목록 수 구하기
	public int getTotalCount(String table_name, String startTime, String endTime ) throws Exception{
		HashMap temp = new HashMap();
		temp.put("table_name" , table_name);
		temp.put("startTime", startTime);
		temp.put("endTime", endTime);
		return (Integer) selectOne("monitoring.getTotalCount" ,temp);
	}
	// (rawData) 테이블 전체 목록 구하기
	public ArrayList getTotalList(String table_name, String startTime, String endTime ) throws Exception {
		HashMap temp = new HashMap();
		temp.put("table_name" , table_name);		
		temp.put("startTime", startTime);
		temp.put("endTime", endTime);
		return (ArrayList) selectList("monitoring.getTotalList" ,temp);
	}
	
	// (단위시간 반영) 테이블 목록 수 구하기
	public int getTotalCount (String table_name, String startTime, String endTime, String unitTime ) throws Exception{
		HashMap temp = new HashMap();
		temp.put("tableName" , table_name);		
		temp.put("startTime", startTime);
		temp.put("endTime", endTime);
		temp.put("unitTime", unitTime);
		return (Integer) selectOne("monitoring.getTotalCount_UnitTime" ,temp);
	}
	// (단위시간 반영) 테이블 전체 목록 구하기
	public ArrayList getTotalList (String table_name, String startTime, String endTime, int unitTime ) throws Exception {
		HashMap temp = new HashMap();
		temp.put("tableName" , table_name);		
		temp.put("startTime", startTime);
		temp.put("endTime", endTime);
		temp.put("unitTime", unitTime);
		return (ArrayList) selectList("monitoring.getTotalList_UnitTime" ,temp);
	}	
	
}//class
