package com.hasom.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.hasom.dao.MonitoringDAO;
import com.hasom.data.SensorData;
import com.hasom.util.PageUtil;

@Service("monitoringService")
public class MonitoringService {
    Logger log = Logger.getLogger(this.getClass());
    @Resource(name="monitoringDAO")
    private MonitoringDAO dao;
    
	// 그 아이디[u_id]가 관리하는 그룹 번호(g_no[]) 목록 확인
	public ArrayList getG_nos(String u_id)  throws Exception{
		return dao.getG_nos(u_id);
	}

	// 해당 사용자가 관리하는 모든 g_no[]에 대한 g_name[] 확인 
	//	(나중에) 이름 길이에 신경써야 함 (ex. 5글자 이상 ... 처리)
	public ArrayList getG_names(String u_id)  throws Exception{
		return dao.getG_names(u_id);
	}

	// g_no ==> g_m_period  확인
	public int getG_m_period(int g_no)  throws Exception{
		return dao.getG_m_period(g_no);
	}
	public int getG_m_off(int g_no)  throws Exception{
		return dao.getG_m_off(g_no);
	}
	// g_no ==> g_cl_period  확인
	public int getG_cl_period(int g_no)  throws Exception{
		return dao.getG_cl_period(g_no);
	}
	
	// 해당 그룹 정보 표시를 위해 필요한 데이터빈 목록 얻기
	// (...내가 왜 이렇게 했지..ㅠㅠㅠ)
	public ArrayList getData(int g_no)  throws Exception{
		// 해당 그룹(g_no)에 설치된 센서(gs_no[]) 목록 확인 (Group_sensor)
		ArrayList	gs_nos = dao.getGs_nos(g_no);
		// VO 저장용 ArrayList 생성
		ArrayList	result = new ArrayList();
		// 각 센서(gs_no)마다 다음 과정 반복
		for (int i=0 ; i<gs_nos.size() ; i++) {
			int gs_no = (Integer)gs_nos.get(i);
			// gs_no[i] ==> s_display 확인
			String s_display = dao.getS_display(gs_no);
			
			// 각 센서 요소(sf_no[]) 목록 확인 (Group_sensor + Sensor_Factor [s_no조인] )
			ArrayList sf_nos = dao.getSf_nos(gs_no);
			// 센서별 요소 수 확인 [f_count]
			int f_count = sf_nos.size();
			// f_unit[f_count] , value[f_count], date[f_count] 생성 (ArrayList로 할까? ==> 응, 그래야 해..)
			ArrayList f_units	= new ArrayList();
			ArrayList values	= new ArrayList(); 
			ArrayList l_highs	= new ArrayList();
			ArrayList l_lows	= new ArrayList(); 
			ArrayList l_nos	= new ArrayList(); 
			Date	date = new Date();
			// 각 (sf_no)마다 다음 과정 반복
			for(int j=0; j<f_count ; j++) {
				int sf_no = (Integer) sf_nos.get(j);
				String table_name = "monitor_" + g_no;
				//1. gs_no & sf_no 로 limit_table에서 정보 가져오기 
				//	1) ==> l_no
				//	2) ==> l_high / l_low
				HashMap paraMap = new HashMap();
				paraMap.put("gs_no", gs_no);
				paraMap.put("sf_no", sf_no);
				HashMap resultMap = dao.getFromLimitTable1(paraMap);
				l_highs.add(resultMap.get("l_high"));
				l_lows.add(resultMap.get("l_low"));
				l_nos.add(resultMap.get("l_no"));
				
				//2. sf_no ==> f_unit[i] 확인
				f_units.add( j, dao.getF_unit(sf_no) );
				
				//3. g_no, gs_no, table_name ==> value[i], date[i] 확인
				HashMap paraMap2 = new HashMap();
				paraMap2.put("table_name", table_name);
				paraMap2.put("l_no", l_nos.get(j));
				if (j==0) {	date = dao.getLastDate(paraMap2); } // 센서당 한번만 하면 됨
				values.add( j, dao.getLastValue(paraMap2) );
		
				
			}//for(sf_no)
			
			// (gs_no), s_display , value[], f_unit[] ==> VO에 저장 
			SensorData sData = new SensorData();
			sData.setS_display(s_display);
			sData.setF_units(f_units);
			sData.setValues(values);
			sData.setL_highs(l_highs);
			sData.setL_lows(l_lows);
			sData.setDate(date);
			sData.setGs_no(gs_no);
			//VO를 ArrayList에 저장 (입력한 순서 보장)
			result.add(sData);
			
		}//for(gs_no)
		
		return result;
	}
    
	//	해당 그룹의 모든 센서 설치 위치 목록
	public ArrayList getS_displays(int g_no)   throws Exception {
		return dao.getS_displays(g_no);
	}
	
	//	해당 그룹의 모든 측정요소 목록 (중복제외)
	public ArrayList getF_names(int g_no)   throws Exception {
		return dao.getF_names(g_no);
	}
	
	// 데이터 조회용 목록 수 구하기1 : raw data
	public int getTotalCount(String tableName, String startTime, String endTime ) throws Exception {
		return dao.getTotalCount(tableName, startTime, endTime);
	}
	// 데이터 조회용 목록 수 구하기2 : unitTime 기준 분류
	public int getTotalCount(String tableName, String startTime, String endTime, String unitTime ) throws Exception {
		return dao.getTotalCount(tableName, startTime, endTime);
	}
	
	
	/* 170207
	 * (페이지 수에 맞는) 목록 구하기1 : raw data
	 */
	public ArrayList GetTotalList (PageUtil pInfo, String tableName, String startTime, String endTime )throws Exception {
		//전체 목록을 구한다.
		ArrayList list = dao.getTotalList(tableName, startTime, endTime);
		//이 중에 현재 페이지에 해당하는 목록만 꺼내야 한다.
		ArrayList	resultList = new ArrayList();
		//	1) 이번 페이지에서 필요한 시작, 마지막 페이지를 알아낸다.
		int	start	= (pInfo.getNowPage()-1) * pInfo.getPageList();
		int	end		= start + pInfo.getPageList() - 1;
		for (int i=start ; i<=end && i<pInfo.getTotalCount(); i++) {
			// 마지막 페이지인 경우에는 5개가 안 될 수 있으므로 체크해 두어야 한다.
			resultList.add(list.get(i));
		}
		return resultList;
	}
	/* 170213
	 * (페이지 수에 맞는) 목록 구하기2 : unitTime 기준 분류
	 */
	public ArrayList GetTotalList (String tableName, String startTime, String endTime, int unitTime )throws Exception {
		//전체 목록을 구한다.
		ArrayList list = dao.getTotalList(tableName, startTime, endTime, unitTime);
		return list;
	}
	/* 170217
	 * (페이지 없이) rawData 목록 구하기3 : csv 저장용
	 */
	public ArrayList GetTotalList(String tableName, String startTime, String endTime) throws Exception {
		ArrayList list = dao.getTotalList(tableName, startTime, endTime);
		return list;
	}	
	
	
	
}//class
