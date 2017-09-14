package com.hasom.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.common.dao.AbstractDAO;

@Repository("graphDAO")
public class GraphDAO extends AbstractDAO{

	// g_no , nowFactor(f_table_name) → gs_noList 얻음
	public ArrayList getGs_infos(int g_no, String f_table_name) throws Exception {
		HashMap paramap = new HashMap();
		paramap.put("g_no", g_no);
		paramap.put("f_table_name", f_table_name);
		return (ArrayList) selectList("graph.getGs_infos" , paramap);
	}

	//조회기간에 해당하는 데이터를 "[UNIX_TIMESTAMP(측정일시) , 측정값]" 형태로 List에 저장 ex) [ [0,0] , [2,1] , [1,3] ]
	public ArrayList<String> getDataList(String tableName , String startTime, String endTime) throws Exception {
		HashMap paramap = new HashMap();
		paramap.put("tableName", tableName);
		paramap.put("startTime", startTime);
		paramap.put("endTime", endTime);
		return (ArrayList<String>) selectList("graph.getDataList" , paramap);
	}

	
	//	해당 그룹의 모든 센서 설치 위치 목록
	public ArrayList getS_displays(int g_no) throws Exception {
		return (ArrayList) selectList("monitoring.getS_displays_wo_di", g_no);
	}

		//	해당 그룹의 모든 측정요소 목록 (중복제외)
	public ArrayList getF_names_wo_di(int g_no)throws Exception {
		return (ArrayList) selectList("monitoring.getF_names_wo_di", g_no);
	}

	// 선택한 요소를 측정하는 센서인지 확인 20170907
	public boolean checkSensor(int gs_no, String f_table_name) throws Exception {
		HashMap paramap = new HashMap();
		paramap.put("gs_no", gs_no);
		paramap.put("f_table_name", f_table_name.toUpperCase() );
		return (Integer)selectOne("graph.checkSensor" , paramap) == 1 ? true : false;
	}
}//class
