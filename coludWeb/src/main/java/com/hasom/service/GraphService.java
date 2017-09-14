package com.hasom.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hasom.dao.GraphDAO;
import com.hasom.dao.MonitoringDAO;
import com.hasom.data.GraphData;
import com.hasom.util.StringUtil;

@Service("graphService")
public class GraphService {
    Logger log = Logger.getLogger(this.getClass());
    @Resource(name="monitoringDAO")
    private MonitoringDAO mDao;
    @Resource(name="graphDAO")
    private GraphDAO dao;
    
    // 그래프 그리는데 필요한 정보 서비스
	public HashMap getListMap(GraphData data, int g_no ) throws Exception {
		//1. 모든 센서의 정보 저장할 hashmap 생성 (listMap)
		HashMap listMap = new HashMap();
		//2. g_no , nowFactor(f_table_name) → gs_no / s_display List 얻음
		//	★ 해당 요소를 포함한 센서에 대한 gs_no 만 포함해야 함.
		//ArrayList<HashMap> gs_infos = dao.getGs_infos(g_no, data.getNowFactor());
		
		//2. nowSensor(ex. 4_입구) → gs_no(4) / s_display(입구) List 얻음
		String[] gs_no_s_display = data.getNowSensor();
		// 필요한 내용
		ArrayList<HashMap> gs_infos = new ArrayList<HashMap>();
		// 받은 값이 있다면,
		if (gs_no_s_display != null) {		
			// 받은 배열에서 gs_no 와 s_display를 분리 기억
			for (int i=0 ; i<gs_no_s_display.length ; i++) {
				String[] arr = gs_no_s_display[i].split("_");
				int gs_no = StringUtil.isNaturalNum(-1, arr[0].trim());
				// 선택한 요소를 측정하는 센서인지 확인 20170907
				if( !dao.checkSensor(gs_no , data.getNowFactor()) ){
					continue;
				}
				// 선택된 (gs_no , s_display)를 기억할 임시 맵
				HashMap temp = new HashMap();
				temp.put("gs_no", gs_no);
				temp.put("s_display", arr[1].trim());
				gs_infos.add(temp);
			}//for
			// 적합한 센서가 없으면 조회 안함
			if( gs_infos.isEmpty() ) {
				return null;
			}
		}//if		
		
		
		//3. g_no , f_table_name , gs_noList → 조회할 센서마다 다음을 실행
		for(HashMap map : gs_infos) {
			//3-1) 센서별 hashmap 생성 (temp)
			HashMap tempMap = new HashMap();
			//3-2)  temp에 'label'을 키로 s_display 값 저장 (범례에 들어갈 이름)
			tempMap.put("label", "\""+map.get("s_display")+"\"");
			
			//3-3) 조회기간에 해당하는 데이터를 "[UNIX_TIMESTAMP(측정일시) , 측정값]" 형태로 List에 저장 ex) [ [0,0] , [2,1] , [1,3] ]
			String tableName = "g" + g_no + "_" + data.getNowFactor() + "_" + map.get("gs_no");
			ArrayList<String> dataList = dao.getDataList(tableName , data.getStartDay()+" "+data.getStartTime() , data.getEndDay()+" "+data.getEndTime());
			
			//3-4) temp에 해당 정보 저장	temp.add( "data" , dataList );
			tempMap.put("data", dataList);
			
			//3-5) listMap에 temp를 저장	listMap.add( "센서이름-s_display" , temp );
			listMap.put( "\""+map.get("s_display")+"\"" , tempMap );
		}
		
		//5. listMap 반환
		return listMap;
	}

	//	해당 그룹의 모든 센서 설치 위치 목록
	public ArrayList getS_displays_wo_di(int g_no) throws Exception {
		return dao.getS_displays(g_no);
	}

	//	해당 그룹의 모든 측정요소 목록 (중복제외)
	public ArrayList getF_names_wo_di(int g_no)   throws Exception {
		return dao.getF_names_wo_di(g_no);
	}
  	
	
}//class
