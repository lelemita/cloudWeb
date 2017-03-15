package com.hasom.service;

import java.util.ArrayList;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.hasom.dao.EventLogDAO;
import com.hasom.util.PageUtil;

/*	2017.02.16. 남연우.
 * 이벤트 로그 조회를 위한 서비스
 */
@Service("eventLogService")
public class EventLogService {
    Logger log = Logger.getLogger(this.getClass());
    @Resource(name="eventLogDAO")
    private EventLogDAO dao;   

	// 목록 수 구하기
	public int getTotalCount(String tableName, String startTime, String endTime , String gs_code_arr, String kind)  throws Exception {
		return dao.getTotalCount(tableName, startTime, endTime, gs_code_arr, kind) ;
	}
	
	// 페이지 수에 맞는 전체 목록 구하기
	public ArrayList GetTotalList (PageUtil pInfo, String tableName, String startTime, String endTime , String gs_code_arr, String kind) throws Exception {
		//전체 목록을 구한다.
		ArrayList list = dao.getTotalList(tableName, startTime, endTime, gs_code_arr, kind) ;
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
	
	/* 170315
	 * (페이지 없이) rawData 목록 구하기 : csv 저장용
	 */
	public ArrayList GetTotalList(String tableName, String startTime, String endTime, String gs_code_arr, String kind) throws Exception{
		ArrayList list = dao.getTotalList(tableName, startTime, endTime, gs_code_arr, kind);
		return list;
	}


	
	
}//class
