package com.hasom.controller;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.hasom.data.JobData;
import com.hasom.data.SearchData;
import com.hasom.data.UserData;
import com.hasom.service.ConfigService;
import com.hasom.service.MonitoringService;
import com.hasom.util.StringUtil;

/* A.D.2017.03.21.~ 남연우.
 * 환경 설정용 컨트롤러
 */
@Controller
public class ConfigController {
    Logger log = Logger.getLogger(this.getClass());
	@Resource(name="monitoringService")
	private MonitoringService mService;
	@Resource(name="configService")
	private ConfigService service;
	
	//파라메터로 받은 SearchData 유효성 검사
	private SearchData checkSearchData(SearchData data) {
		//nowPage : 자연수 아니면 1 
		StringUtil.isNaturalNum(1, data.getNowPage());
		//scope : 검색범위
		if( StringUtil.isNull(data.getScope()) ){
			// 나중에는 선택 가능한 검색범위인지도 검사할 것
			data.setScope("u_id"); //기본값 : u_id (PK)
		}
		//keyword : 검색어
		if(  StringUtil.isNull(data.getKeyword())  ) {
			data.setKeyword("");
		} else {
			//혹시 모르는 공백 제거
			data.setKeyword( data.getKeyword().trim() );
		}
		return data;
	}//checkSearchData(SearchData data)	
	
	
	/*
	 *  개인정보 조회 페이지 요청
	 */
	@RequestMapping("/Config/PersonalSetting.hs")
	public String PersonalSetting (HttpServletRequest req, HttpSession session, Model model) {
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		try {
			UserData data = service.PersonalSetting(u_id);		
			model.addAttribute("DATA", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Config/PersonalSetting";
	}
	
	/*
	 * Ajax : 비밀번호 확인 요청
	 */
	@RequestMapping("/Config/PasswordChk.hs")
	public String PasswordChk (HttpServletRequest req, Model model) {
		String id = req.getParameter("id");
		String pw = req.getParameter("pw");			
		// 비밀번호 확인 서비스
		// 맞으면 해당회원의 u_type 반환 / 아니면 'X' 반환
		model.addAttribute("u_type" , service.PasswordChk(id, pw));
		return "Config/PasswordChk";
	}
	
	/*
	 *  개인정보 변경 처리 요청
	 */
	@RequestMapping("/Config/PersonalSettingProc.hs")
	public String PersonalSettingProc (HttpSession session, Model model, UserData data, RedirectAttributes redirectAttributes) {
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		try {
			//변경사항 저장하는 서비스
			service.PersonalSettingProc(data);	
			// 처리 결과 : 이렇게 보내면 리다이렉트 후의 뷰까지 전달됨
			redirectAttributes.addFlashAttribute("MSG", "수정완료");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:../Config/PersonalSetting.hs";
	}	
	

	/*
	 * 담당자 : 사용자 목록 보기
	 */
	@RequestMapping("/Admin/UserList.hs")
	public String Userlist(HttpSession session, Model model, SearchData data) {
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		// 2) 그외 유효성 검사 : scope, keyword, nowPage
		data = checkSearchData(data);
		data.setU_id(u_id);
		
		//검색키워드 반영하여, 본인의 소속 회사의 모든 사용자 목록 & 페이지 정보 (탈퇴 제외) 서비스
		data = service.getUserList(data);
		
		//모델에 전달 및 뷰 호출
		model.addAttribute("DATA", data);
		return "/Config/UserList";
	}
	
	
	/*
	 * 담당자 : 직원 상세정보 조회하기
	 */
	@RequestMapping("/Admin/UserView.hs")
	public String UserView(HttpSession session, Model model, SearchData data) {
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		// 2) 그외 유효성 검사 : scope, keyword, nowPage
		data = checkSearchData(data);

		//해당 직원의 상세 정보 조회
		data = service.getUserDetail(data);
		
		//모델에 전달 및 뷰 호출
		model.addAttribute("DATA", data);
		return "/Config/UserView";
	}	
	
	/*
	 * 담당자 : 직원 상세정보 조회 페이지에서 권한 수정
	 */
	@RequestMapping("/Admin/UserModiProc.hs")
	public String UserModiProc(HttpServletRequest req, HttpSession session, SearchData data, UserData userData) {
		//파라메터 받기
		//  유효성 검사 : scope, keyword, nowPage
		data = checkSearchData(data);
		
		//서비스 : 회원 정보 수정		
		service.modifyUserData(userData);
		
		//모델에 전달 및 뷰 호출	
		// ★★★★ 나중에 Post 방식으로 바꾸자ㅠㅠㅠ
		return "redirect:../Admin/UserView.hs?u_id=" + data.getU_id()
					+ "&scope="		+ data.getScope()           
					+ "&keyword="	+ data.getKeyword()         
					+ "&nowPage="	+ data.getNowPage() ;      
		
	}		
	
	/*
	 * 담당자 : 직원 상세정보 조회 페이지에서 회원 삭제
	 */
	@RequestMapping("/Admin/UserDelProc.hs")
	public String UserDelProc(HttpServletRequest req, HttpSession session, SearchData data, UserData userData) {
		//파라메터 받기
		//  유효성 검사 : scope, keyword, nowPage
		data = checkSearchData(data);
		
		//서비스 : 회원 정보 수정		
		service.delUserData(userData);
		
		//모델에 전달 및 뷰 호출	
		// ★★★★ 나중에 Post 방식으로 바꾸자ㅠㅠㅠ
		return "redirect:../Admin/UserList.hs?scope="		+ data.getScope()           
					+ "&keyword="	+ data.getKeyword()         
					+ "&nowPage="	+ data.getNowPage() ;      
		
	}		
	
	/* 20170413 남연우
	 * 담당자 : 신규 사용자 등록 폼 요청
	 */
	@RequestMapping("/Admin/UserRegistry.hs")
	public String UserRegistry(HttpSession session, Model model, SearchData data) {
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		// 2) 그외 유효성 검사 : scope, keyword, nowPage
		data = checkSearchData(data);	
		
		//필요한 정보 서비스 :  담당 그룹 목록
		data = service.prepareUserRegist(data, u_id);
		
		//모델에 전달 및 뷰 호출 (릴레이)
		model.addAttribute("DATA", data);
		return "/Config/UserRegistry";
	}
	
	/*
	 * Ajax : 아이디 중복 검사 요청
	 */
	@RequestMapping("/Admin/IDcheck.hs")
	public String IDChk (HttpServletRequest req, Model model) {
		String id = req.getParameter("id");
		// 비밀번호 확인 서비스
		// 맞으면 해당회원의 u_type 반환 / 아니면 'X' 반환
		String temp = service.IDChk(id);

		model.addAttribute("u_type" , temp);
		return "Config/PasswordChk";
	}	
	
	/* 20170414 남연우
	 * 담당자 : 신규 사용자 등록 작업 요청
	 */
	@RequestMapping("/Admin/UserRegistryProc.hs")
	public String UserRegistryProc (HttpSession session, Model model, SearchData data, UserData userData) {
		// 결과 메시지
		String popMsg = "";
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		// 2) 그외 유효성 검사 : scope, keyword, nowPage
		data = checkSearchData(data);	
		// 3) 나중에 상세보기로 가기 위해서 id 정보 전달
		data.setU_id(userData.getU_id());
		
		//사용자 등록
		boolean isSuccess = service.userRegistryProc(userData);
		
		//담당 그룹 등록
		if( !isSuccess ) {
			popMsg = "Fail to register new user";
			return "redirect:../Admin/UserRegistry.hs?popMsg="	+ popMsg	;
		}
		
		if( userData.getCheckedGroups()!=null && userData.getCheckedGroups().length != 0 ){			
			service.newUserGroup(userData);	
		}
		
		//popMsg = "신규 사용자 등록 완료"; // 한글깨짐 → 나중에 해결
		//모델에 전달 및 뷰 호출	
		// ★★★★ 나중에 Post 방식으로 바꾸자ㅠㅠㅠ
		return "redirect:../Admin/UserView.hs?u_id=" + data.getU_id()
					+ "&scope="		+ data.getScope()           
					+ "&keyword="	+ data.getKeyword()         
					+ "&nowPage="	+ data.getNowPage()
					+ "&popMsg="	+ popMsg	; 
	}
		
	/*
	 * 관리자 : 센서 설정 하기
	 */
	@RequestMapping("/Manager/SensorSetting.hs")
	public ModelAndView SensorSetting (HttpServletRequest req, HttpSession session, ModelAndView mv) {
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		String strNowGr = req.getParameter("nowGroup");
		int nowGroup = 0;
		try {
			nowGroup = Integer.parseInt(strNowGr); 
		} catch (Exception ex) {
			// 값이 없거나 숫자 양식이 아님 → 그대로 0				
		}
		String nowFactor = req.getParameter("nowFactor");
		if (nowFactor==null) { nowFactor = ""; }
		
		//서비스
		//1) 선택 화면에 필요한 내용
		int g_no = 0;
		ArrayList g_names	= null;
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
			// (2) g_no = g_no[nowGroup] 에 대해서 다음 실행
			g_no = (Integer) g_nos.get(nowGroup);

			// (3) 해당 그룹의 모든 측정요소 목록 (중복제외)
			f_names = mService.getF_names(g_no);
			
			// (4) 해당그룹, 해당 요소의 센서들에 대한 정보 서비스
			if(nowFactor.length()!=0) {
				ArrayList sensorInfos = service.getSensorInfos(g_no, nowFactor);				
				mv.addObject("SENSORINFOS" , sensorInfos);
			}
			
		}
		catch (Exception ex) {
			log.info(">> 그룹 정보조회 실패");	
			String errMsg = "그룹 정보를 조회할 수 없습니다.";
			mv.addObject("ERRMSG", errMsg);
			// ★★★★ (나중에) 에러 안내 페이지 만들자
		}		
		
		//모델 보내기
		mv.addObject("G_NAMES", g_names);			
		mv.addObject("F_NAMES", f_names);		
		mv.addObject("NOWGROUP", nowGroup);	
		mv.addObject("NOWFACTOR", nowFactor);

		mv.setViewName("/Config/SensorSetting");
		return mv;
	}	
	
	/*
	 *  센서 설정 변경내용 반영하기
	 */
	@RequestMapping("/Manager/SensorSettingProc.hs")
	public String SensorSetting (HttpServletRequest req, HttpSession session, Model model) {
		//파라메터 받기
		// id
		String u_id = (String) session.getAttribute("ID");
		// nowGroup
		String strNowGr = req.getParameter("nowGroup");
		int nowGroup = 0;
		try { nowGroup = Integer.parseInt(strNowGr); 
		} catch (Exception ex) { /* 값이 없거나 숫자 양식이 아님 → 그대로 0*/}
		// nowFactor
		String nowFactor = req.getParameter("nowFactor");
		if (nowFactor==null) { nowFactor = ""; }
		// changes : 변경사항 리스트
		String[] changes = req.getParameterValues("changes");

		//서비스 : 각 변경사항 저장
		boolean isSuccess = service.saveChangeSettings(changes);
		
		//모델 보내기
		// 미구현 : model.addAttribute("isSuccess", isSuccess);
		model.addAttribute("nowGroup", nowGroup);	
		model.addAttribute("nowFactor", nowFactor);
				
		return "redirect: ../Manager/SensorSetting.hs";
	}
	
	/*
	 * 관리자/담당자 메뉴 페이지 호출
	 */
	@RequestMapping("/Manager/ConfigMenu.hs")
	public String ConfigMenu() {
		return "/Config/ConfigMenu";
	}
	
	/*
	 * job 관리 페이지 요청
	 */
	@RequestMapping("/Manager/JobSetting.hs")
	public String JobSetting(HttpServletRequest req, HttpSession session, Model model) {
		//파라메터 받기
		// id
		String u_id = (String) session.getAttribute("ID");
		// nowGroup
		String strNowGr = req.getParameter("nowGroup");
		int nowGroup = 0;
		try { nowGroup = Integer.parseInt(strNowGr); 
		} catch (Exception ex) { /* 값이 없거나 숫자 양식이 아님 → 그대로 0*/}

		//서비스
		//1) 선택 화면에 필요한 내용
		int g_no = 0;
		ArrayList g_names	= null;
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
			// (2) g_no = g_no[nowGroup] 에 대해서 다음 실행
			g_no = (Integer) g_nos.get(nowGroup);
		}
		catch (Exception ex) {
			log.info(">> 그룹 정보조회 실패");	
			String errMsg = "그룹 정보를 조회할 수 없습니다.";
			model.addAttribute("ERRMSG", errMsg);
			// ★★★★ (나중에) 에러 안내 페이지 만들자
		}	
		//2) 해당 그룹 + 표준 잡 정보 목록 서비스
		ArrayList<JobData> jobList = service.getJobList(g_no);

		//모델 보내기
		model.addAttribute("G_NAMES", g_names);				
		model.addAttribute("NOWGROUP", nowGroup);
		model.addAttribute("G_NO", g_no);
		model.addAttribute("JOBLIST", jobList);
		return "/Config/JobSetting";
	}
	
	/*
	 * 잡 삭제하기
	 */
	@RequestMapping("/Manager/DeleteJob.hs")
	public ModelAndView DeleteJob( HttpServletRequest req, HttpSession session,
			@RequestParam(value="nowGroup", defaultValue="0")	int nowGroup , 
			@RequestParam(value="j_no", required=true)			int j_no		) {
		
		//해당 j_no 삭제 서비스
		boolean isSuccess = service.deleteJob(j_no);
		//뷰 부르기
		RedirectView rv = new RedirectView("../Manager/JobSetting.hs");
		rv.setExposeModelAttributes(true);	// false로 하면, 주소줄에 속성을 감춘다는데, 아예 전달이 안된다..ㅠ
		ModelAndView mv = new ModelAndView(rv);
		mv.addObject("nowGroup", nowGroup);
		return mv;
	}

	/*
	 * 잡 수정, 복제, 신규등록 폼 요청
	 */
	@RequestMapping("/Manager/AdjustJobForm.hs")
	public String AdjustJobForm( Model model,
			@RequestParam(value="g_no", required=true)			int g_no ,
			@RequestParam(value="nowGroup",	defaultValue="0")	int nowGroup ,
			@RequestParam(value="workType",	defaultValue="")	String workType ,
			@RequestParam(value="j_no",		defaultValue="-1", required=false)	int j_no		) {
		
		//서비스
		// 전송방법 종류 조회
		ArrayList<String> typeList = service.getJobTypeList();
		
		// 수정, 복제인 경우, 해당 잡의 내용을 찾아서 보냄
		JobData data = new JobData();
		if(workType.indexOf("신규")<0){
			data = service.getJobData(j_no);			
		}
		
		//뷰 부르기
		model.addAttribute("NOWGROUP", nowGroup);
		model.addAttribute("G_NO", g_no);
		model.addAttribute("WORKTYPE", workType);
		model.addAttribute("JOBTYPES" , typeList);
		model.addAttribute("DATA", data);
		return "/Config/AdjustJobForm";
	}		
	
	
	/*
	 * 잡 수정, 복제, 신규등록 작업
	 */
	@RequestMapping("/Manager/AdjustJobProc.hs")
	public ModelAndView AdjustJobProc ( JobData data,
			@RequestParam(value="nowGroup",	defaultValue="0")	int nowGroup ,
			@RequestParam(value="workType", required=true)	String workType ) {
		
		boolean isSuccess = false;
		// 잡 등록, 수정 서비스
		isSuccess=service.adjustJob(workType,data);
			
		//뷰 부르기
		RedirectView rv = new RedirectView("../Manager/JobSetting.hs");
		rv.setExposeModelAttributes(true);	// false로 하면, 주소줄에 속성을 감춘다는데, 아예 전달이 안된다..ㅠ
		ModelAndView mv = new ModelAndView(rv);
		mv.addObject("nowGroup", nowGroup);
		mv.addObject("msg", isSuccess? workType+" 처리완료":workType+" 과정에서 에러"); //나중에 쓰자 (미구현)
		return mv;
	}	
		
	/*
	 * 관리자, 담당자 : 이벤트 별, Job 설정 현황 폼 요청
	 */
	@RequestMapping("/Manager/EventJobForm.hs")
	public String EventJobForm( HttpSession session, Model model,
			@RequestParam(value="nowGroup",	defaultValue="0")		int nowGroup ,
			@RequestParam(value="nowFactor", defaultValue="CON")	String nowFactor ) {
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		//서비스
		//1) 선택 화면에 필요한 내용
		int g_no = 0;
		ArrayList g_names	= null;
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
			// (2) g_no = g_no[nowGroup] 에 대해서 다음 실행
			g_no = (Integer) g_nos.get(nowGroup);
			// (3) 해당 그룹의 모든 측정요소 목록 (중복제외)
			f_names = mService.getF_names(g_no);
		}
		catch (Exception ex) {
			log.info(">> 그룹 정보조회 실패");	
			String errMsg = "그룹 정보를 조회할 수 없습니다.";
			model.addAttribute("ERRMSG", errMsg);
			// ★★★★ (나중에) 에러 안내 페이지 만들자
		}		

		//모델 보내기1
		model.addAttribute("NOWGROUP", nowGroup);	
		model.addAttribute("NOWFACTOR", nowFactor);
		model.addAttribute("G_NAMES", g_names);			
		model.addAttribute("F_NAMES", f_names);		
		model.addAttribute("G_NO", g_no);
		
		//통신 장애에 대한 설정인 경우
		if(nowFactor.equals("CON")){
			// 해당 그룹의 통신 장애, 복구에 대한 대응 잡 내용 조회
			ArrayList<JobData> disList = service.getEventJobList(ConfigService.DISCONTACT , g_no);
			ArrayList<JobData> conList = service.getEventJobList(ConfigService.CONTACT , g_no);
			model.addAttribute("DISCON" , disList);
			model.addAttribute("CONACT", conList);
			return "/Config/EventJobForm_Contact";
		}
		//요소에 대한 설정인 경우
		else {
			// 해당그룹, 요소의 모든 l_no 목록조회
			// 각 l_no 마다 다음 반복
				//	(나중에) 센서별로 발생 가능한 이벤트를 DB에 저장하자★★★★★
				// 상한일탈이벤트 대응 잡 목록 확인
				// 상한복구이벤트 대응 잡 목록 확인
				// 하한일탈이벤트 대응 잡 목록 확인
				// 하한복구이벤트 대응 잡 목록 확인
		
			return "/Config/EventJobForm_Limit";			
		}
	}//method
	
	
	
	
}//class
