package com.hasom.util;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/*
 * 담당자 권한 검사 인터셉터
 */
public class IsAdminCheck extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle (HttpServletRequest req, HttpServletResponse resp, Object handle) throws Exception {
		
		System.out.println(">> 담당자 권한 검사 인터셉터 실행");
		
		HttpSession session = req.getSession();
		String id = (String) session.getAttribute("ID");
		String kind = String.valueOf( session.getAttribute("TYPE") );
			
		if (kind == null || !kind.equals( "A" ) ) {
			// 들어온 요청 파악하기
			String uri = req.getRequestURI();
			//안해도 포워드라 자동으로 가지만, 요청 내용이 다르다.
			req.setAttribute("toGo", uri); 	
			req.setAttribute("loginMsg", "담당자 로그인이 필요한 요청입니다.");
			RequestDispatcher rd = req.getRequestDispatcher("../Login/LoginForm.hs");			
			rd.forward(req, resp);
			return false;
		}
		else {
			return true;			
		}
		
	}//preHandle()
	
	
}//class
