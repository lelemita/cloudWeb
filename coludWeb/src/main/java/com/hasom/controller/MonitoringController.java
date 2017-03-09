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
import org.springframework.web.bind.annotation.RequestParam;
import com.hasom.service.MonitoringService;
import com.hasom.util.PageUtil;
import com.hasom.util.ServletUtil;
import com.hasom.util.StringUtil;


/* 2016.12.29~ 남연우.
 * 모니터링, 표조회 기능 컨트롤러
 */
@Controller
public class MonitoringController {
    Logger log = Logger.getLogger(this.getClass());
	@Resource(name="monitoringService")
	private MonitoringService service;

/* 도구들 → ServletUtil로 옮김	
	// 문자열 ==> 숫자 변환, 자연수 아니면 지정한 숫자를 반환하는 함수
	public int StringUtil.isNaturalNum (int defaultNum , String strNum) {
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
	}//StringUtil.isNaturalNum()

	// yyyy-MM-dd  파라메터 이름으로 해당 날짜 찾기 (값 없으면 현재 날짜)
	public String ServletUtil.getDay(HttpServletRequest req, String dayParameter) throws Exception{
		int year, month, date;
		String strDay	= req.getParameter( dayParameter);
		Calendar cal = Calendar.getInstance();	// 오늘날짜, 현재시각으로 생성됨
		if (strDay==null || strDay.length() < 0) {
			// 값 없으면 오늘 (0~24)
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;
			date = cal.get(Calendar.DATE);
			if (dayParameter.indexOf("end")>=0) {
				// 값 없음 → 당일 0시 ~ 다음날 0시 조회
				date = date +1;
			}
			strDay = year + "-" + StringUtil.prependZero(month,2) + "-" + date ;
		}		
		return strDay;
	}	
	
	// HH:00:00 파라메터 이름으로 해당 시각 찾기 (값 없으면 0)
	public int getTime (HttpServletRequest req, String timeParameter) throws Exception{
		// 값 없으면 0
		int hour = 0;
		String strTime = req.getParameter(timeParameter);
		if (strTime != null && strTime.length() > 0) {
			hour = Integer.parseInt(strTime);
		}
		return hour;
	}
	
	// "2017-02-01" , "HH:00:00" 양식 문자열로 받은 객체를 켈린더로 반환함
	public Calendar ServletUtil.getCalendar(String date , int hour) {
		String[] strDate = date.split("-");
		Calendar cal = Calendar.getInstance();
		int[] dates = new int[3];
		for (int i=0 ; i<3 ; i++) {
			dates[i] = Integer.parseInt(strDate[i]);
		}
		cal.set(dates[0], dates[1]-1, dates[2], hour, 0 , 0); // Calendar의 달은  0~11
		return cal;
	}
*/

	
	/* 2017.02.08.남연우
	 * 모니터링 화면 업데이트 Ajax 요청
	 */
	@RequestMapping("/Monitoring/UpdateData.hs")	
	public String UpdateData(@RequestParam(value="nowGroup", required=true) String strG_no, HttpSession session, HttpServletRequest req, Model model) {
		int g_no = Integer.parseInt(strG_no);
		ArrayList list =  new ArrayList();
		try {
			// 모니터링 할 데이터 목록 가져오기
			list = service.getData(g_no);
			// ArrayList를 모델에 전달 ●
			model.addAttribute("LIST", list);
		}
		catch (Exception ex) {
			log.info(">> 측정 정보 조회 실패");
			ex.printStackTrace();
		}
		return "/Monitoring/UpdateData";
	}
	
	/*
	 * 모니터링 화면 요청
	 */
	@RequestMapping("/Monitoring/MonitorList.hs")
	public String MonitorList(HttpSession session, HttpServletRequest req, Model model) {
		// 세션에서 ID 받음 // 검사는 인터셉터에서★★★(나중에)★★★
		String u_id = (String) session.getAttribute("ID");
		// 요청에서 nowGroup 받음, 유효성 검사 (자연수 아니면 0 )
		String strG = (String) req.getParameter("nowGroup");
		int nowGroup = StringUtil.isNaturalNum(0 , strG);
		
		// ( 이하 과정은 각각 서비스에서)
		int g_no = 0;
		int g_m_period = 0;
		ArrayList g_names = null;
		try {
			// 그 아이디[u_id]가 관리하는 그룹 번호(g_no[]) 목록 확인 (Group_user)
			ArrayList g_nos = service.getG_nos(u_id);
			
			// (해당 사용자의) 모든 g_no[]에 대한 g_name[] 확인 ==> 탭 이름 결정
			//	(나중에) 이름 길이에 신경써야 함 (ex. 5글자 이상 ... 처리)
			g_names = service.getG_names(u_id);
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
			// g_no ==> g_m_period  확인 ==> 모델에 전달 ●  ==> 통신 지연 확인에 사용
			g_m_period = service.getG_m_period(g_no);

			// 모델에 전달 ●			
			model.addAttribute("G_NAMES", g_names);			
			model.addAttribute("NOWGROUP", nowGroup);				
			model.addAttribute("G_M_PERIOD", g_m_period);
		}
		catch (Exception ex) {
			log.info(">> 그룹 정보조회 실패");	
			String errMsg = "그룹 정보를 조회할 수 없습니다.";
			model.addAttribute("ERRMSG", errMsg);
			// ★★★★ (나중에) 에러 안내 페이지 만들자
		}
		
		ArrayList list =  new ArrayList();
		try {
			// 모니터링 할 데이터 목록 가져오기
			list = service.getData(g_no);
			// ArrayList를 모델에 전달 ●
			model.addAttribute("LIST", list);
		}
		catch (Exception ex) {
			log.info(">> 측정 정보 조회 실패");
			ex.printStackTrace();
		}

		return "/Monitoring/MonitorList";

	}//monitorList
	
	
	
	/* 2017.01.04.남연우.  : 데이터 조회 (표) 화면 요청
	 * 2017.02.07.남연우.  : 제작 재개 - 너무 오랜만이라 기억이ㅠㅠ
	 */
	@RequestMapping("/Monitoring/DataList.hs")
	public String DataList(HttpSession session, HttpServletRequest req, Model model) {
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		// 2) nowGroup	→ 유효성 검사 (자연수 아니면 0 )
		int nowGroup = StringUtil.isNaturalNum(0 , req.getParameter("nowGroup"));	
		
//		// 3) nowSensor (gs_no)	→ 유효성 검사 (자연수 아니면 -1 )
//		int gs_no = StringUtil.isNaturalNum(-1 , req.getParameter("nowSensor"));	
		String[] gs_no_s_display = req.getParameterValues("nowSensor");
		// 선택된 gs_no들을 기억할 배열
		ArrayList<Integer> gs_no_array = new ArrayList<Integer>();
		// 선택된 (gs_no , s_display)를 기억할 맵
		HashMap s_display_map = new HashMap();
		// 다시 모델로 보낼 ArrayList
		ArrayList<String> nowSensor_List = new ArrayList<String>();
		// 받은 값이 있다면,
		if (gs_no_s_display != null) {		
			// 받은 배열에서 gs_no 와 s_display를 분리 기억
			for (int i=0 ; i<gs_no_s_display.length ; i++) {
				nowSensor_List.add(gs_no_s_display[i]);
				String[] arr = gs_no_s_display[i].split("_");
				int gs_no = StringUtil.isNaturalNum(-1, arr[0].trim());
				gs_no_array.add(gs_no);
				s_display_map.put(gs_no, arr[1].trim() );
			}
		}

//		// 4) nowFactor (f_no)	→ 유효성 검사 (자연수 아니면 0 )
//		String f_table_name = req.getParameter("nowFactor");
		String[] f_table_name_array = req.getParameterValues("nowFactor");
		// 다시 모델로 보낼 ArrayList
		ArrayList<String> nowFactor_List = new ArrayList<String>();
		// 받은 값이 있다면,
		if (f_table_name_array != null) {		
			// 받은 배열에서 gs_no 와 s_display를 분리 기억
			for (int i=0 ; i<f_table_name_array.length ; i++) {
				nowFactor_List.add(f_table_name_array[i]);
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
		// 8) unitTime : 조회 간격 0 → all : raw Data 간격으로 보여줌
		int unitTime = StringUtil.isNaturalNum(0 , req.getParameter("unitTime"));
		
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
			ArrayList g_nos = service.getG_nos(u_id);
			// (해당 사용자의) 모든 g_no[]에 대한 g_name[] 확인 ==> 탭 이름 결정
			//	(나중에) 이름 길이에 신경써야 함 (ex. 5글자 이상 ... 처리)
			g_names = service.getG_names(u_id);
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
			g_cl_period = service.getG_cl_period(g_no);
			
			//	(3) 해당 그룹의 모든 센서 설치 위치 목록
			s_displays = service.getS_displays(g_no);
			
			//	(4) 해당 그룹의 모든 측정요소 목록 (중복제외)
			f_names = service.getF_names(g_no);
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
		model.addAttribute("NOWFACTOR", nowFactor_List);
		model.addAttribute("STARTDAY", startDay);
		model.addAttribute("STARTTIME", startTime);
		model.addAttribute("ENDDAY", endDay);
		model.addAttribute("ENDTIME", endTime);
		model.addAttribute("UNITTIME", unitTime);
		if ( gs_no_array.size() <= 0 ) {
			// 아직 데이터를 조회하기 전임
			return "/Monitoring/DataList";
		}
		
		//2-1) 조회 간격 == 0 인 경우 (rawData), 표 구성에 필요한 내용 (선택 이후 nowSensor>=0)
		if (unitTime == 0) {
			try{
				//  이 경우 선택된 gs_no는 하나임
				int gs_no = (Integer) gs_no_array.get(0);
				
				//1. 총 목록수 구하기
				//	동일 센서라면 모든 요소의 데이터 수가 같으므로, 첫번째 요소 기준으로 목록 수를 구한다.
				String tableName1 = "G" + g_no + "_" + f_table_name_array[0] + "_" + gs_no;
				int totalCount = service.getTotalCount(tableName1, startDay+" "+startTime, endDay+" "+endTime);
				//2. 페이지 정보 구하기
				PageUtil pInfo = new PageUtil(nowPage, totalCount, 10 ,10 );				

				// 각 센서+요소의 이름을 기억할 맵
				HashMap listNames = new HashMap();
				
				// 각 요소별 데이터 리스트를 저장할 리스트
				ArrayList listList = new ArrayList();
				int listCount = 0;	// 총 리스트의 개수
				for (String f_table_name : f_table_name_array ) {
					String tableName = "G" + g_no + "_" + f_table_name + "_" + gs_no;

					//3. 목록 구하기
					ArrayList list = service.GetTotalList(pInfo, tableName, startDay+" "+startTime, endDay+" "+endTime);
					listNames.put(listCount, s_display_map.get(gs_no) + f_table_name );
				
					if (listCount == 0) {
						// 첫 데이터 (시간의 기준) 리스트는 바로 보냄
						model.addAttribute("LIST_0" , list);
					}
					else {
						// 나머지 리스트는 리스트에 넣어서 보냄
						listList.add(list);						
					}
					listCount++;	
				}
				
				// 총 리스트의 개수 만큼 숫자 배열을 만듦 (→ JSP에서 목록 꺼내는데 사용)
				ArrayList<Integer> listNums = new ArrayList<Integer>();
				for(int i=0 ; i<listCount ; i++) {
					listNums.add(i);
				}
				
				//4. 모델에 전달
				model.addAttribute("LISTLIST", listList);
				model.addAttribute("LISTCOUNT", listNums );
				model.addAttribute("LISTNAMES", listNames);
				model.addAttribute("PINFO", pInfo);				
				
				
//				//1. 총 목록수 구하기
//				String tableName = "G" + g_no + "_" + f_table_name + "_" + gs_no;
//				int totalCount = service.getTotalCount(tableName, startDay+" "+startTime, endDay+" "+endTime);
//				//2. 페이지 정보 구하기
//				PageUtil pInfo = new PageUtil(nowPage, totalCount, 10 ,10 );				
//				//3. 목록 구하기
//				ArrayList list = service.GetTotalList(pInfo, tableName, startDay+" "+startTime, endDay+" "+endTime);
//				//4. 모델에 전달하기
//				model.addAttribute("LIST", list);
//				model.addAttribute("PINFO", pInfo);
			} catch (Exception e) {
				// (나중에) 에러 페이지로 연결시키기
				e.printStackTrace();
			}
			//모델 보내기
			return "/Monitoring/DataList";
		}//if (unitTime == 0)
		
		
		
		
		//2-2) 조회 간격 != 0 인 경우, 표 구성에 필요한 내용 (선택 이후 nowSensor>=0)
		try {
			//	1. (조회 기간/unitTime) 으로 총 목록과 그 개수 구함
			Calendar startCal	= ServletUtil.getCalendar(startDay , startTime); //Calendar의 달은 0~11
			Calendar endCal		= ServletUtil.getCalendar(endDay , endTime);
			ArrayList<Date> dateList = new ArrayList<Date>();
			//		★ 조회 순서 바꾸려면 이 아래를 반대로 (1/2)
			startCal.add(Calendar.MINUTE, -unitTime);
			while ( endCal.after(startCal) ) {
				dateList.add(endCal.getTime()); //왠지 Calendar를 JSP에서 못 받음
				endCal.add(Calendar.MINUTE, -unitTime);
			}			
			dateList.add(startCal.getTime()); 
			
			// 총 목록 수
			int totalCount = dateList.size();
			//	2.해당 페이지의 조회 기간 구함
			//	 1) 페이지 정보 구하기
			PageUtil pInfo = new PageUtil(nowPage, totalCount, 10 ,10 );
			//	 2) 시작과 마지막 인덱스 구하기
			int	startIndex	= (pInfo.getNowPage()-1) * pInfo.getPageList();
			int	endIndex	= startIndex + pInfo.getPageList() - 1;
			// 		마지막 페이지에서 꽉 채우지 못한 경우 대비
			endIndex = (endIndex < totalCount)? endIndex : totalCount-1;
			//	 3) 해당 페이지 조회 기간 (★조회 순서 바꾸고 싶으면 여기를 반대로 2/2)
			Date end	= dateList.get(startIndex);
			Date start	= dateList.get(endIndex);
			//	 4) dateList에서 해당 기간 만큼만 가짐
			List<Date> dateList_sub = dateList.subList(startIndex, endIndex);
	
			//	3.해당 페이지의 조회기간 만큼 각센서의 데이터를 조회
			// 		각 센서+요소의 이름을 기억할 맵
			HashMap listNames = new HashMap();
			// 		각 센서+요소별 데이터 리스트를 저장할 리스트
			ArrayList listList = new ArrayList();
			
			int listCount = 0;
			for (int gs_no : gs_no_array) {
				for (String f_table_name : f_table_name_array ) {
					String tableName = "G" + g_no + "_" + f_table_name + "_" + gs_no;
					ArrayList list = service.GetTotalList(tableName, StringUtil.dateForm(start, "yyyy-MM-dd HH:mm") , StringUtil.dateForm(end, "yyyy-MM-dd HH:mm") , unitTime);
					
					// 센서+요소 이름을 맵에 저장
					listNames.put(listCount, s_display_map.get(gs_no) + f_table_name );
					// 각 데이터 리스트를 리스트에 넣어서 보냄
					listList.add(list);						
					listCount++;
				}
			}
			// 총 리스트의 개수 만큼 숫자 배열을 만듦 (→ JSP에서 목록 꺼내는데 사용)			
			ArrayList<Integer> listNums = new ArrayList<Integer>();
			for(int i=0 ; i<listCount ; i++) {
				listNums.add(i);
			}
			
			//4. 모델에 전달
			model.addAttribute("LISTLIST", listList);			
			model.addAttribute("LISTCOUNT", listNums );
			model.addAttribute("LISTNAMES", listNames);
			model.addAttribute("DATELIST", dateList_sub);
			model.addAttribute("PINFO", pInfo);
		} catch (Exception e) {
			// (나중에) 에러 페이지로 연결시키기
			e.printStackTrace();
		}
		//모델 보내기
		return "/Monitoring/DataList";
		
	}//DataList()
	
	
	
	
	/* 2017.02.17.남연우.
	 *  데이터 저장 (csv) 
	 */
	@RequestMapping("/Monitoring/DataSave.hs")
	public String DataSave(HttpSession session, HttpServletRequest req, Model model) {
		//파라메터 받기
		//0) fileName : 너무나 취약한 방법, 나중에 수정하자
		String fileName = req.getParameter("fileName");
		if ( StringUtil.isNull(fileName) ) {
			fileName = "DataList.csv";
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
		
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		// 2) nowGroup	→ 유효성 검사 (자연수 아니면 0 )
		int nowGroup = StringUtil.isNaturalNum(0 , req.getParameter("nowGroup"));	
		// 3) nowSensor (gs_no)	→ 유효성 검사 (자연수 아니면 -1 )
		String[] gs_no_s_display = req.getParameterValues("nowSensor");
		// 선택된 gs_no들을 기억할 배열
		ArrayList<Integer> gs_no_array = new ArrayList<Integer>();
		// 선택된 (gs_no , s_display)를 기억할 맵
		HashMap s_display_map = new HashMap();
		// 다시 모델로 보낼 ArrayList
		ArrayList<String> nowSensor_List = new ArrayList<String>();
		// 받은 값이 있다면,
		if (gs_no_s_display != null) {		
			// 받은 배열에서 gs_no 와 s_display를 분리 기억
			for (int i=0 ; i<gs_no_s_display.length ; i++) {
				nowSensor_List.add(gs_no_s_display[i]);
				String[] arr = gs_no_s_display[i].split("_");
				int gs_no = StringUtil.isNaturalNum(-1, arr[0].trim());
				gs_no_array.add(gs_no);
				s_display_map.put(gs_no, arr[1].trim() );
			}
		}

		// 4) nowFactor (f_no)	→ 유효성 검사 (자연수 아니면 0 )
		String[] f_table_name_array = req.getParameterValues("nowFactor");
		// 다시 모델로 보낼 ArrayList
		ArrayList<String> nowFactor_List = new ArrayList<String>();
		// 받은 값이 있다면,
		if (f_table_name_array != null) {		
			// 받은 배열에서 gs_no 와 s_display를 분리 기억
			for (int i=0 ; i<f_table_name_array.length ; i++) {
				nowFactor_List.add(f_table_name_array[i]);
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
		// 8) unitTime : 조회 간격 0 → all : raw Data 간격으로 보여줌
		int unitTime = StringUtil.isNaturalNum(0 , req.getParameter("unitTime"));
		
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
			ArrayList g_nos = service.getG_nos(u_id);
			// (해당 사용자의) 모든 g_no[]에 대한 g_name[] 확인 ==> 탭 이름 결정
			//	(나중에) 이름 길이에 신경써야 함 (ex. 5글자 이상 ... 처리)
			g_names = service.getG_names(u_id);
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
			g_cl_period = service.getG_cl_period(g_no);
			//	(3) 해당 그룹의 모든 센서 설치 위치 목록
			s_displays = service.getS_displays(g_no);
			//	(4) 해당 그룹의 모든 측정요소 목록 (중복제외)
			f_names = service.getF_names(g_no);
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
		model.addAttribute("NOWFACTOR", nowFactor_List);
		model.addAttribute("STARTDAY", startDay);
		model.addAttribute("STARTTIME", startTime);
		model.addAttribute("ENDDAY", endDay);
		model.addAttribute("ENDTIME", endTime);
		model.addAttribute("UNITTIME", unitTime);
		if ( gs_no_array.size() <= 0 ) {
			// 아직 데이터를 조회하기 전임
			return "/Monitoring/DataSave";
		}
		
		//2-1) 조회 간격 == 0 인 경우 (rawData), 표 구성에 필요한 내용 (선택 이후 nowSensor>=0)
		if (unitTime == 0) {
			try{
				//  이 경우 선택된 gs_no는 하나임
				int gs_no = (Integer) gs_no_array.get(0);
				
				//1. 총 목록수 구하기
				//	동일 센서라면 모든 요소의 데이터 수가 같으므로, 첫번째 요소 기준으로 목록 수를 구한다.
				String tableName1 = "G" + g_no + "_" + f_table_name_array[0] + "_" + gs_no;
				//int totalCount = service.getTotalCount(tableName1, startDay+" "+startTime, endDay+" "+endTime);
				//2. 페이지 정보 구하기
				//PageUtil pInfo = new PageUtil(nowPage, totalCount, 10 ,10 );				

				// 각 센서+요소의 이름을 기억할 맵
				HashMap listNames = new HashMap();
				
				// 각 요소별 데이터 리스트를 저장할 리스트
				ArrayList listList = new ArrayList();
				int listCount = 0;	// 총 리스트의 개수
				for (String f_table_name : f_table_name_array ) {
					String tableName = "G" + g_no + "_" + f_table_name + "_" + gs_no;

					//3. 목록 구하기
					ArrayList list = service.GetTotalList(tableName, startDay+" "+startTime, endDay+" "+endTime);
					listNames.put(listCount, s_display_map.get(gs_no) + f_table_name );
				
					if (listCount == 0) {
						// 첫 데이터 (시간의 기준) 리스트는 바로 보냄
						model.addAttribute("LIST_0" , list);
					}
					else {
						// 나머지 리스트는 리스트에 넣어서 보냄
						listList.add(list);						
					}
					listCount++;	
				}
				
				// 총 리스트의 개수 만큼 숫자 배열을 만듦 (→ JSP에서 목록 꺼내는데 사용)
				ArrayList<Integer> listNums = new ArrayList<Integer>();
				for(int i=0 ; i<listCount ; i++) {
					listNums.add(i);
				}
				
				//4. 모델에 전달
				model.addAttribute("LISTLIST", listList);
				model.addAttribute("LISTCOUNT", listNums );
				model.addAttribute("LISTNAMES", listNames);
				// 페이지 정보 : 사용할 건 아니고, 릴레이 시키기 위해 전달
				model.addAttribute("PINFO", req.getParameter("pinfo"));				

			} catch (Exception e) {
				// (나중에) 에러 페이지로 연결시키기
				e.printStackTrace();
			}
			//모델 보내기
			return "/Monitoring/DataSave";
		}//if (unitTime == 0)
		
		//2-2) 조회 간격 != 0 인 경우, 표 구성에 필요한 내용 (선택 이후 nowSensor>=0)
		try {
			//	1. (조회 기간/unitTime) 으로 총 목록과 그 개수 구함
			Calendar startCal	= ServletUtil.getCalendar(startDay , startTime); //Calendar의 달은 0~11
			Calendar endCal		= ServletUtil.getCalendar(endDay , endTime);
			ArrayList<Date> dateList = new ArrayList<Date>();
			//		★ 조회 순서 바꾸려면 이 아래를 반대로 (1/2)
			startCal.add(Calendar.MINUTE, -unitTime);
			while ( endCal.after(startCal) ) {
				dateList.add(endCal.getTime()); //왠지 Calendar를 JSP에서 못 받음
				endCal.add(Calendar.MINUTE, -unitTime);
			}			
			dateList.add(startCal.getTime()); 
			
			// 총 목록 수
			//int totalCount = dateList.size();
			//	2.해당 페이지의 조회 기간 구함
			//	 1) 페이지 정보 구하기
			//PageUtil pInfo = new PageUtil(nowPage, totalCount, 10 ,10 );
			//	 2) 시작과 마지막 인덱스 구하기
			int	startIndex	= 0;
			int	endIndex	= dateList.size() - 1;
			// 		마지막 페이지에서 꽉 채우지 못한 경우 대비
			//endIndex = (endIndex < totalCount)? endIndex : totalCount-1;
			//	 3) 해당 페이지 조회 기간 (★조회 순서 바꾸고 싶으면 여기를 반대로 2/2)
			Date end	= dateList.get(startIndex);
			Date start	= dateList.get(endIndex);
			//	 4) dateList에서 해당 기간 만큼만 가짐
			List<Date> dateList_sub = dateList.subList(startIndex, endIndex);
	
			//	3.해당 페이지의 조회기간 만큼 각센서의 데이터를 조회
			// 		각 센서+요소의 이름을 기억할 맵
			HashMap listNames = new HashMap();
			// 		각 센서+요소별 데이터 리스트를 저장할 리스트
			ArrayList listList = new ArrayList();
			
			int listCount = 0;
			for (int gs_no : gs_no_array) {
				for (String f_table_name : f_table_name_array ) {
					String tableName = "G" + g_no + "_" + f_table_name + "_" + gs_no;
					ArrayList list = service.GetTotalList(tableName, StringUtil.dateForm(start, "yyyy-MM-dd HH:mm") , StringUtil.dateForm(end, "yyyy-MM-dd HH:mm") , unitTime);
					
					// 센서+요소 이름을 맵에 저장
					listNames.put(listCount, s_display_map.get(gs_no) + f_table_name );
					// 각 데이터 리스트를 리스트에 넣어서 보냄
					listList.add(list);						
					listCount++;
				}
			}
			// 총 리스트의 개수 만큼 숫자 배열을 만듦 (→ JSP에서 목록 꺼내는데 사용)			
			ArrayList<Integer> listNums = new ArrayList<Integer>();
			for(int i=0 ; i<listCount ; i++) {
				listNums.add(i);
			}
			
			//4. 모델에 전달
			model.addAttribute("LISTLIST", listList);			
			model.addAttribute("LISTCOUNT", listNums );
			model.addAttribute("LISTNAMES", listNames);
			model.addAttribute("DATELIST", dateList_sub);
			// 페이지 정보 : 사용할 건 아니고, 릴레이 시키기 위해 전달
			model.addAttribute("PINFO", req.getParameter("pinfo"));		
		} catch (Exception e) {
			// (나중에) 에러 페이지로 연결시키기
			e.printStackTrace();
		}
		//모델 보내기
		return "/Monitoring/DataSave";
		
	}//DataSave()	
}//class
