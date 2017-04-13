package com.hasom.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hasom.data.GraphData;
import com.hasom.service.GraphService;
import com.hasom.service.MonitoringService;
import com.hasom.util.ServletUtil;
import com.hasom.util.StringUtil;

/* 2017.03.16~ 남연우.
 * 측정 데이터 그래프 조회 컨트롤러
 * 이건 ModelAndView로 해보자 (난 void/String이 편하긴 하던데;;)
 * 파라메터도 VO로 받자 (막 받으니 너무 길더라ㅠㅠ)
 */
@Controller
public class GraphController {
    Logger log = Logger.getLogger(this.getClass());
	@Resource(name="monitoringService")
	private MonitoringService mService;
	@Resource(name="graphService")
	private GraphService service;
	
	
	// VO 유효성 검사
	private GraphData checkVO(GraphData data) {
		GraphData result = data;
		try {
			// 자연수 아니면 0
			result.setNowGroup( StringUtil.isNaturalNum(0 , data.getNowGroup()) + "");
			// 값 없으면 오늘
			result.setStartDay( ServletUtil.getDay( data.getStartDay() , 0) );
			// 값 없으면 오늘+1
			result.setEndDay( ServletUtil.getDay( data.getEndDay() , 1) );
			// 값 없으면 0시
			result.setStartTime( ServletUtil.getTime( data.getStartTime(), 0) + "");
			result.setEndTime( ServletUtil.getTime( data.getEndTime(), 0) + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}//VO 유효성 검사 메서드
	
	/*
	 * 그래프 화면 요청
	 */
	@RequestMapping("/Graph/DataGraph.hs")
	public ModelAndView DataGraph(HttpSession session, GraphData data, ModelAndView mv) {
		
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		// 2) 나머지는 VO 사용
		data = this.checkVO(data); //전체 유효성검사
		int nowGroup = Integer.parseInt(data.getNowGroup()); 
		// 3) nowSensor (gs_no)
		// 다시 모델로 보낼 ArrayList 
		//	★ 처음부터 VO로 보낼껄ㅠㅠ + 중간의 설계 변경 
		//		시간이 없어서, 일단 그냥 중복으로 보냄ㅠㅠ
		ArrayList<String> nowSensor_List = new ArrayList<String>();
		String[] gs_no_s_display = data.getNowSensor();
		// 받은 값이 있다면,
		if (gs_no_s_display != null) {		
			// 받은 배열에서 gs_no 와 s_display를 분리 기억
			for (int i=0 ; i<gs_no_s_display.length ; i++) {
				nowSensor_List.add(gs_no_s_display[i]);
			}
		}		

		//서비스
		//1) 선택 화면에 필요한 내용
		int g_no = 0;
		ArrayList g_names	= null;
		ArrayList s_displays= null;
		ArrayList f_names	= null;
		try {
			//	(1) 그룹 목록 ==> 그룹 탭
			// 그 아이디[u_id]가 관리하는 그룹 번호(g_no[]) 목록 확인 (Group_user)
			ArrayList g_nos = mService.getG_nos(u_id);
			// (해당 사용자의) 모든 g_no[]에 대한 g_name[] 확인 ==> 탭 이름 결정
			//	(나중에) 이름 길이에 신경써야 함 (ex. 5글자 이상 ... 처리)
			g_names = mService.getG_names(u_id);
			// 그 그룹 갯수[g_count] 저장
			int g_count = g_names.size();
			// g_count < 0 이면, 담당 그룹이 없다는 페이지 리턴
			if (g_count < 0) {
				mv.setViewName( "/Monitoring/NoGroup" );
				return mv;
			}
			// nowGroup > g_count 이면, nowGroup = 0 
			if (nowGroup > g_count) {
				nowGroup = 0 ;
			}			
			// g_no = g_no[nowGroup] 에 대해서 다음 실행
			g_no = (Integer) g_nos.get(nowGroup);

			//	(3) 해당 그룹의 모든 센서 설치 위치 목록
			s_displays = mService.getS_displays(g_no);
			
			//	(4) 해당 그룹의 모든 측정요소 목록 (중복제외)
			f_names = mService.getF_names(g_no);
		}
		catch (Exception ex) {
			log.info(">> 그룹 정보조회 실패");	
			String errMsg = "그룹 정보를 조회할 수 없습니다.";
			mv.addObject("ERRMSG", errMsg);
			// ★★★★ (나중에) 에러 안내 페이지 만들자
		}		
		
		//모델 보내기 1
		mv.addObject("DATA", data);
		mv.addObject("G_NAMES", g_names);			
		mv.addObject("F_NAMES", f_names);	
		mv.addObject("NOWSENSOR", nowSensor_List);
		mv.addObject("S_DISPLAYS", s_displays);		
		// 공용Header 때문에 , NOWGROUP을 따로 한번더 보냄ㅠㅠ
		mv.addObject("NOWGROUP", data.getNowGroup());
		
		//2) nowFactor가 유효한지 검사 (없거나 유효하지 않으면, 조회 전임)
		if ( data.getNowFactor() == null || data.getNowFactor().length() <= 0 
				|| f_names.indexOf(data.getNowFactor().toLowerCase()) >= 0 
				|| f_names.indexOf(data.getNowFactor().toUpperCase()) >=0 ) {
			//A. 유효하지 않음 → 여기서 return
			mv.setViewName("Graph/DataGraph");
			return mv;
		}	//B. 유효함 → 3)으로 진행
		
		//3) 그래프 그리는데 필요한 내용 서비스 받음
		try{	
			HashMap listMap = service.getListMap(data, g_no);
			mv.addObject("GRAPHDATA", listMap);
		} catch (Exception e) {
			// (나중에) 에러 페이지로 연결시키기
			e.printStackTrace();
		}
		
		// 뷰 설정
		mv.setViewName("Graph/DataGraph");
		return mv;
	}
	
	
}//class
