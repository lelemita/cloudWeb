package com.hasom.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hasom.service.EventLogService;
import com.hasom.service.MonitoringService;
import com.hasom.util.PageUtil;
import com.hasom.util.ServletUtil;
import com.hasom.util.StringUtil;

/* 2017.02.16. 남연우.
 * 이벤트 로그 조회 컨트롤러
 */
@Controller
public class EventLogController {
    Logger log = Logger.getLogger(this.getClass());
	@Resource(name="monitoringService")
	private MonitoringService mService;
	@Resource(name="eventLogService")
	private EventLogService service;


	/*
	 * 테스트 요청
	 */
	@RequestMapping("/test/test.hs")
	public void Test(HttpSession session, HttpServletRequest req, Model model) {
//		Calendar startCal	= ServletUtil.getCalendar("2016-01-28" , 0); //Calendar의 달은 0~11
//		Calendar endCal		= ServletUtil.getCalendar("2016-02-02" , 0);
//		ArrayList<Date> cals = new ArrayList<Date>(); 
//		while ( startCal.before(endCal) ) {
//			cals.add(startCal.getTime()); //왠지 Calendar를 JSP에서 못 받음
//			startCal.add(Calendar.MINUTE, 30);
//		}
//		model.addAttribute("LIST", cals);
		
		
		String[] nowSensors = req.getParameterValues("nowSensor");
		if (nowSensors != null ) {
			for (String s_no : nowSensors) {
				System.out.println(">>> " + s_no);
			}
		}
		
	}//test.hs	
	

	
	/* 2017.02.16.남연우.
	 * 이벤트 로그 조회 요청
	 */
	@RequestMapping("/EventLog/EventList.hs")
	public String EventList(HttpSession session, HttpServletRequest req, Model model) {
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		// 2) nowGroup	→ 유효성 검사 (자연수 아니면 0 )
		int nowGroup = StringUtil.isNaturalNum(0 , req.getParameter("nowGroup"));	
		
		// 3) nowSensor (gs_code)	배열→ 유효성 검사
		String[] gs_code = req.getParameterValues("nowSensor");
		// 선택된 gs_no들을 기억할 배열
		//ArrayList<Integer> gs_no_array = new ArrayList<Integer>();
		// 선택된 (gs_no , s_display)를 기억할 맵
		//HashMap s_display_map = new HashMap();
		// 다시 모델로 보낼 ArrayList
		ArrayList<String> nowSensor_List = new ArrayList<String>();
		// 받은 값이 있다면,
		if (gs_code != null) {		
			// 받은 배열에서 gs_no 와 s_display를 분리 기억
			for (int i=0 ; i<gs_code.length ; i++) {
				nowSensor_List.add(gs_code[i]);
			}
		}	
		// 6) nowPage	→ 유효성 검사 (자연수 아니면 1 )
		int nowPage = StringUtil.isNaturalNum(1 , req.getParameter("nowPage"));		
		// 7) start/end + Day/Time → 검색 기간 (값 없으면 오늘 0시~24시)
		String startDay = null;
		String endDay	 = null;
		int startTime = 0, endTime = 0;
		try {
			startTime	= ServletUtil.getTime(req, "startTime");
			endTime		= ServletUtil.getTime(req, "endTime");
			startDay	= ServletUtil.getDay(req, "startDay");
			endDay		= ServletUtil.getDay(req, "endDay");			
		} catch (Exception e1) {
			// 나중에 처리
			e1.printStackTrace();
		}
		//	8) kind : 조회할 알람 종류
		String kind = req.getParameter("kind");
		if(kind==null || kind.length()==0) kind="V";
		
		//서비스
		//1) 선택 화면에 필요한 내용
		int g_no = 0;
		int g_cl_period = 0;
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
				return "/Monitoring/NoGroup";
			}
			// nowGroup > g_count 이면, nowGroup = 0 
			if (nowGroup > g_count) {
				nowGroup = 0 ;
			}			
			// g_no = g_no[nowGroup] 에 대해서 다음 실행
			g_no = (Integer) g_nos.get(nowGroup);
			
			//	(2) g_no ==> g_cl_period  확인	==> (나중에) 조회 간격 선택에 사용
			g_cl_period = mService.getG_cl_period(g_no);
			
			//	(3) 해당 그룹의 모든 센서 설치 위치 목록
			s_displays = mService.getS_displays(g_no);
			
			//	(4) 해당 그룹의 모든 측정요소 목록 (중복제외)
			f_names = mService.getF_names(g_no);
		}
		catch (Exception ex) {
			log.info(">> 그룹 정보조회 실패");	
			String errMsg = "그룹 정보를 조회할 수 없습니다.";
			model.addAttribute("ERRMSG", errMsg);
			// ★★★★ (나중에) 에러 안내 페이지 만들자
		}		
		
		//모델 보내기 1
		//	...그냥 VO로 한번에 할걸...ㅠㅠ
		model.addAttribute("G_NAMES", g_names);						
		model.addAttribute("G_CL_PERIOD", g_cl_period);	
		model.addAttribute("S_DISPLAYS", s_displays);	
		model.addAttribute("F_NAMES", f_names);	
		model.addAttribute("NOWPAGE", nowPage);		
		model.addAttribute("NOWGROUP", nowGroup);
		model.addAttribute("NOWSENSOR", nowSensor_List);
		model.addAttribute("STARTDAY", startDay);
		model.addAttribute("STARTTIME", startTime);
		model.addAttribute("ENDDAY", endDay);
		model.addAttribute("ENDTIME", endTime);
		model.addAttribute("KIND", kind);
		if (  gs_code == null || gs_code.length <= 0 ) {
			// 아직 데이터를 조회하기 전임
			return "/EventLog/EventList";
		}
		

		try{
			// 쿼리에 넣기 위해 배열을 한 줄의 문자열로 바꿈
			//	.. 더 깔끔한 방법을 몰라서..ㅠㅠ
			StringBuffer buff = new StringBuffer();
			buff.append(gs_code[0]);
			for (int i=1 ; i<gs_code.length ; i++) {
				buff.append( "," + gs_code[i]);
			}
			String gs_code_arr = buff.toString();
			
			//1. 총 목록수 구하기
			String tableName = "evt_" + g_no;
			int totalCount = service.getTotalCount(tableName, startDay+" "+startTime, endDay+" "+endTime , gs_code_arr, kind);
			//2. 페이지 정보 구하기
			PageUtil pInfo = new PageUtil(nowPage, totalCount, 10 ,10 );				
			//3. 목록 구하기
			ArrayList list = service.GetTotalList(pInfo, tableName, startDay+" "+startTime, endDay+" "+endTime , gs_code_arr, kind);
			//4. 모델에 전달하기
			model.addAttribute("LIST", list);
			model.addAttribute("PINFO", pInfo);
			
		} catch (Exception e) {
			// (나중에) 에러 페이지로 연결시키기
			e.printStackTrace();
		}
		
		//모델 보내기
		return "/EventLog/EventList";
	
		
	}//EventList()
	
	
	/* 2017.03.15.남연우. (보름만이다;;)
	 *  이벤트 데이터 저장 (csv) 
	 */
	@RequestMapping("/EventLog/EventSave.hs")
	public String EventSave(HttpSession session, HttpServletRequest req, Model model) {
		//파라메터 받기
		//0) fileName : 너무나 취약한 방법, 나중에 수정하자
		String fileName = req.getParameter("fileName");
		if ( StringUtil.isNull(fileName) ) {
			fileName = "EventList.csv";
		}
		if( fileName.indexOf('.') < 0) {
			// 확장자 없음
			fileName += ".csv";
		}
		else{
			// 확장자 있음
			int dotIdx = fileName.lastIndexOf('.');
			String end = fileName.substring(dotIdx);
			if( !end.equals("csv")) {
				// 확장자가 csv 가 아님
				String temp = fileName.substring(0, dotIdx);
				fileName = temp + ".csv";
			}
		}
		model.addAttribute("FILENAME", fileName);
		
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		// 2) nowGroup	→ 유효성 검사 (자연수 아니면 0 )
		int nowGroup = StringUtil.isNaturalNum(0 , req.getParameter("nowGroup"));	
		
		// 3) nowSensor (gs_code)	배열→ 유효성 검사
		String[] gs_code = req.getParameterValues("nowSensor");
		// 선택된 gs_no들을 기억할 배열
		//ArrayList<Integer> gs_no_array = new ArrayList<Integer>();
		// 선택된 (gs_no , s_display)를 기억할 맵
		//HashMap s_display_map = new HashMap();
		// 다시 모델로 보낼 ArrayList
		ArrayList<String> nowSensor_List = new ArrayList<String>();
		// 받은 값이 있다면,
		if (gs_code != null) {		
			// 받은 배열에서 gs_no 와 s_display를 분리 기억
			for (int i=0 ; i<gs_code.length ; i++) {
				nowSensor_List.add(gs_code[i]);
			}
		}	
		// 6) nowPage	→ 유효성 검사 (자연수 아니면 1 )
		int nowPage = StringUtil.isNaturalNum(1 , req.getParameter("nowPage"));		
		// 7) start/end + Day/Time → 검색 기간 (값 없으면 오늘 0시~24시)
		String startDay = null;
		String endDay	 = null;
		int startTime = 0, endTime = 0;
		try {
			startTime	= ServletUtil.getTime(req, "startTime");
			endTime		= ServletUtil.getTime(req, "endTime");
			startDay	= ServletUtil.getDay(req, "startDay");
			endDay		= ServletUtil.getDay(req, "endDay");			
		} catch (Exception e1) {
			// 나중에 처리
			e1.printStackTrace();
		}
		//	8) kind : 조회할 알람 종류
		String kind = req.getParameter("kind");
		if(kind==null || kind.length()==0) kind="V";
		
		//서비스
		//1) 선택 화면에 필요한 내용
		int g_no = 0;
		int g_cl_period = 0;
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
				return "/Monitoring/NoGroup";
			}
			// nowGroup > g_count 이면, nowGroup = 0 
			if (nowGroup > g_count) {
				nowGroup = 0 ;
			}			
			// g_no = g_no[nowGroup] 에 대해서 다음 실행
			g_no = (Integer) g_nos.get(nowGroup);
			
			//	(2) g_no ==> g_cl_period  확인	==> (나중에) 조회 간격 선택에 사용
			g_cl_period = mService.getG_cl_period(g_no);
			
			//	(3) 해당 그룹의 모든 센서 설치 위치 목록
			s_displays = mService.getS_displays(g_no);
			
			//	(4) 해당 그룹의 모든 측정요소 목록 (중복제외)
			f_names = mService.getF_names(g_no);
		}
		catch (Exception ex) {
			log.info(">> 그룹 정보조회 실패");	
			String errMsg = "그룹 정보를 조회할 수 없습니다.";
			model.addAttribute("ERRMSG", errMsg);
			// ★★★★ (나중에) 에러 안내 페이지 만들자
		}		
		
		//모델 보내기 1
		//	...그냥 VO로 한번에 할걸...ㅠㅠ
		model.addAttribute("G_NAMES", g_names);						
		model.addAttribute("G_CL_PERIOD", g_cl_period);	
		model.addAttribute("S_DISPLAYS", s_displays);	
		model.addAttribute("F_NAMES", f_names);	
		model.addAttribute("NOWPAGE", nowPage);		
		model.addAttribute("NOWGROUP", nowGroup);
		model.addAttribute("NOWSENSOR", nowSensor_List);
		model.addAttribute("STARTDAY", startDay);
		model.addAttribute("STARTTIME", startTime);
		model.addAttribute("ENDDAY", endDay);
		model.addAttribute("ENDTIME", endTime);
		model.addAttribute("KIND", kind);
		if (  gs_code == null || gs_code.length <= 0 ) {
			// 아직 데이터를 조회하기 전임
			return "/EventLog/EventSave";
		}
		
		try{
			// 쿼리에 넣기 위해 배열을 한 줄의 문자열로 바꿈
			//	.. 더 깔끔한 방법을 몰라서..ㅠㅠ
			StringBuffer buff = new StringBuffer();
			buff.append(gs_code[0]);
			for (int i=1 ; i<gs_code.length ; i++) {
				buff.append( "," + gs_code[i]);
			}
			String gs_code_arr = buff.toString();
			
			//1. 총 목록수 구하기
			String tableName = "evt_" + g_no;
			
			// ★ csv 저장 할 때는 한 페이지로 모든 데이터를 보여줌
			//int totalCount = service.getTotalCount(tableName, startDay+" "+startTime, endDay+" "+endTime , gs_code_arr, kind);
			//2. 페이지 정보 구하기
			//PageUtil pInfo = new PageUtil(nowPage, totalCount, 10 ,10 );				
			//3. (★ 페이지 없이 전부) 목록 구하기
			ArrayList list = service.GetTotalList(tableName, startDay+" "+startTime, endDay+" "+endTime , gs_code_arr, kind);
			//4. 모델에 전달하기
			model.addAttribute("LIST", list);
			//model.addAttribute("PINFO", pInfo);
			// ★ 페이지 정보 : 사용할 건 아니고, 릴레이 시키기 위해 전달
			model.addAttribute("PINFO", req.getParameter("pinfo"));		
			
		} catch (Exception e) {
			// (나중에) 에러 페이지로 연결시키기
			e.printStackTrace();
		}
		
		//모델 보내기
		return "/EventLog/EventSave";
	
	}//EventSave()
	
	/* 
	 *  이벤트 로그 인쇄 :EventSave 와 뷰 말고 같다. 
	 */
	@RequestMapping("/EventLog/EventPrint.hs")
	public String EventPrint(HttpSession session, HttpServletRequest req, Model model) {
		//파라메터 받기
		//0) fileName : 너무나 취약한 방법, 나중에 수정하자
		String fileName = req.getParameter("fileName");
		if ( StringUtil.isNull(fileName) ) {
			fileName = "EventList.csv";
		}
		if( fileName.indexOf('.') < 0) {
			// 확장자 없음
			fileName += ".csv";
		}
		else{
			// 확장자 있음
			int dotIdx = fileName.lastIndexOf('.');
			String end = fileName.substring(dotIdx);
			if( !end.equals("csv")) {
				// 확장자가 csv 가 아님
				String temp = fileName.substring(0, dotIdx);
				fileName = temp + ".csv";
			}
		}
		model.addAttribute("FILENAME", fileName);
		
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		// 2) nowGroup	→ 유효성 검사 (자연수 아니면 0 )
		int nowGroup = StringUtil.isNaturalNum(0 , req.getParameter("nowGroup"));	
		
		// 3) nowSensor (gs_code)	배열→ 유효성 검사
		String[] gs_code = req.getParameterValues("nowSensor");
		// 선택된 gs_no들을 기억할 배열
		//ArrayList<Integer> gs_no_array = new ArrayList<Integer>();
		// 선택된 (gs_no , s_display)를 기억할 맵
		//HashMap s_display_map = new HashMap();
		// 다시 모델로 보낼 ArrayList
		ArrayList<String> nowSensor_List = new ArrayList<String>();
		// 받은 값이 있다면,
		if (gs_code != null) {		
			// 받은 배열에서 gs_no 와 s_display를 분리 기억
			for (int i=0 ; i<gs_code.length ; i++) {
				nowSensor_List.add(gs_code[i]);
			}
		}	
		// 6) nowPage	→ 유효성 검사 (자연수 아니면 1 )
		int nowPage = StringUtil.isNaturalNum(1 , req.getParameter("nowPage"));		
		// 7) start/end + Day/Time → 검색 기간 (값 없으면 오늘 0시~24시)
		String startDay = null;
		String endDay	 = null;
		int startTime = 0, endTime = 0;
		try {
			startTime	= ServletUtil.getTime(req, "startTime");
			endTime		= ServletUtil.getTime(req, "endTime");
			startDay	= ServletUtil.getDay(req, "startDay");
			endDay		= ServletUtil.getDay(req, "endDay");			
		} catch (Exception e1) {
			// 나중에 처리
			e1.printStackTrace();
		}
		//	8) kind : 조회할 알람 종류
		String kind = req.getParameter("kind");
		if(kind==null || kind.length()==0) kind="V";
		
		//서비스
		//1) 선택 화면에 필요한 내용
		int g_no = 0;
		int g_cl_period = 0;
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
				return "/Monitoring/NoGroup";
			}
			// nowGroup > g_count 이면, nowGroup = 0 
			if (nowGroup > g_count) {
				nowGroup = 0 ;
			}			
			// g_no = g_no[nowGroup] 에 대해서 다음 실행
			g_no = (Integer) g_nos.get(nowGroup);
			
			//	(2) g_no ==> g_cl_period  확인	==> (나중에) 조회 간격 선택에 사용
			g_cl_period = mService.getG_cl_period(g_no);
			
			//	(3) 해당 그룹의 모든 센서 설치 위치 목록
			s_displays = mService.getS_displays(g_no);
			
			//	(4) 해당 그룹의 모든 측정요소 목록 (중복제외)
			f_names = mService.getF_names(g_no);
		}
		catch (Exception ex) {
			log.info(">> 그룹 정보조회 실패");	
			String errMsg = "그룹 정보를 조회할 수 없습니다.";
			model.addAttribute("ERRMSG", errMsg);
			// ★★★★ (나중에) 에러 안내 페이지 만들자
		}		
		
		//모델 보내기 1
		//	...그냥 VO로 한번에 할걸...ㅠㅠ
		model.addAttribute("G_NAMES", g_names);						
		model.addAttribute("G_CL_PERIOD", g_cl_period);	
		model.addAttribute("S_DISPLAYS", s_displays);	
		model.addAttribute("F_NAMES", f_names);	
		model.addAttribute("NOWPAGE", nowPage);		
		model.addAttribute("NOWGROUP", nowGroup);
		model.addAttribute("NOWSENSOR", nowSensor_List);
		model.addAttribute("STARTDAY", startDay);
		model.addAttribute("STARTTIME", startTime);
		model.addAttribute("ENDDAY", endDay);
		model.addAttribute("ENDTIME", endTime);
		model.addAttribute("KIND", kind);
		if (  gs_code == null || gs_code.length <= 0 ) {
			// 아직 데이터를 조회하기 전임
			return "/EventLog/EventPrint";
		}
		
		try{
			// 쿼리에 넣기 위해 배열을 한 줄의 문자열로 바꿈
			//	.. 더 깔끔한 방법을 몰라서..ㅠㅠ
			StringBuffer buff = new StringBuffer();
			buff.append(gs_code[0]);
			for (int i=1 ; i<gs_code.length ; i++) {
				buff.append( "," + gs_code[i]);
			}
			String gs_code_arr = buff.toString();
			
			//1. 총 목록수 구하기
			String tableName = "evt_" + g_no;
			
			// ★ csv 저장 할 때는 한 페이지로 모든 데이터를 보여줌
			//int totalCount = service.getTotalCount(tableName, startDay+" "+startTime, endDay+" "+endTime , gs_code_arr, kind);
			//2. 페이지 정보 구하기
			//PageUtil pInfo = new PageUtil(nowPage, totalCount, 10 ,10 );				
			//3. (★ 페이지 없이 전부) 목록 구하기
			ArrayList list = service.GetTotalList(tableName, startDay+" "+startTime, endDay+" "+endTime , gs_code_arr, kind);
			//4. 모델에 전달하기
			model.addAttribute("LIST", list);
			//model.addAttribute("PINFO", pInfo);
			// ★ 페이지 정보 : 사용할 건 아니고, 릴레이 시키기 위해 전달
			model.addAttribute("PINFO", req.getParameter("pinfo"));		
			
		} catch (Exception e) {
			// (나중에) 에러 페이지로 연결시키기
			e.printStackTrace();
		}
		
		//모델 보내기
		return "/EventLog/EventPrint";
	
	}//EventPrint()	
	
}//class
