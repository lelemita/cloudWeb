package com.hasom.controller;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hasom.dao.MemberDAO;
import com.hasom.service.MemberService;

/* 2016.12.27. 남연우.
 * 로그인 및 회원 기능 컨트롤러
 */
@Controller
public class MemberController {
	Logger log = Logger.getLogger(this.getClass());
	@Resource(name="memberService")
	private MemberService memberService;
	@Autowired
	private MemberService service;

	@RequestMapping("/")
	public String test() {
		return "index";
	}
	
	/* 161227
	 * 로그인 폼 요청
	 */
	@RequestMapping("/Login/LoginForm.hs")
	public void loginForm(HttpServletRequest req, Model model) {
		// 원래 하려던 요청
		String uri = (String) req.getAttribute("toGo");
		// 로그인 안내 메시지
		String loginMsg = (String) req.getAttribute("loginMsg");
		//System.out.println("★★★ loginMsgl : " + loginMsg);
		
		model.addAttribute("LOGINMSG", loginMsg);
	}
	
	/* 
	 * 로그인 처리
	 */
	@RequestMapping("/Login/LoginProc.hs")
	public String loginProc(HttpSession session, HttpServletRequest req, Model model) {
		String id = req.getParameter("u_id");
		String pw = req.getParameter("u_pw");
		HashMap map = null;
		try {
			map = service.loginProc(id, pw);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		if( (Boolean) map.get("ISMEMBER") ) {
			//세션에 사용자 정보 저장
			session.setAttribute("ID", id);
			session.setAttribute("NAME", map.get("u_name"));
			session.setAttribute("TYPE", map.get("u_type"));
			session.setAttribute("C_NAME", map.get("c_name"));
			session.setAttribute("C_NO", map.get("c_no"));
			// 회원이면, 모니터링 페이지로 이동
			return "redirect:../Monitoring/MonitorList.hs";
		}
		else {
			//회원 아니면, 다시 로그인 페이지로 이동
			return "redirect:../Login/LoginForm.hs";
		}
	}
	
	/* 2017.02.08.남연우.
	 * 로그아웃 처리
	 */
	@RequestMapping("/Login/Logout.hs")
	public String logOutProc(HttpSession session, HttpServletRequest req, Model model) {

		session.removeAttribute("ID");
		session.removeAttribute("NAME");
		session.removeAttribute("TYPE");
		return "redirect:../Login/LoginForm.hs";
		
	}	
	
	
	
	
	
	
	
	
	
	
}//class
