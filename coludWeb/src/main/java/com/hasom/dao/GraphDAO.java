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
	
	
}//class
