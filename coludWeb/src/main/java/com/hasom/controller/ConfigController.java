package com.hasom.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.hasom.data.SearchData;
import com.hasom.data.UserData;
import com.hasom.service.ConfigService;
import com.hasom.util.StringUtil;

/* A.D.2017.03.21.~ 남연우.
 * 환경 설정용 컨트롤러
 */
@Controller
public class ConfigController {
    Logger log = Logger.getLogger(this.getClass());
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
	public String SensorSetting (HttpSession session, Model model) {
		//파라메터 받기
		// 1) (세션) ID 		// 검사는 인터셉터에서 한다
		String u_id = (String) session.getAttribute("ID");
		
		
		return "/Config/SensorSetting";
	}	
}//class
