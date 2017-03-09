package com.hasom.util;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/*	160927
 * 	관리자 검사 인터셉터
 * 	localhost:8080/lost/Admin/* 로 들어오는 요청 대상
 * 	세션의 'KIND' 값이 '0'이어야 통과
 */
public class IsAdminCheck extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle (HttpServletRequest req, HttpServletResponse resp, Object handle) throws Exception {
		
		System.out.println(">> 관리자/담당자 검사 인터셉터 실행");
		
		HttpSession session = req.getSession();
		String kind = String.valueOf( session.getAttribute("TYPE") );
		
//		// 아작스 명령 제외
//		String reqUri = req.getRequestURI();
//		System.out.println(reqUri);
//		if(reqUri.equals("/lost/Admin/getSCate.lost")){
//			System.out.println(">> 아작스라서 통과!");
//			return true;
//		}
		
		
		// 그 외 요청들
		if (kind == null || !kind.equals( "A" ) || !kind.equals( "M" )) {
			// 들어온 요청 파악하기
			String uri = req.getRequestURI();
			req.setAttribute("toGo", uri); 	//안해도 포워드라 자동으로 간다.  .. 아닌가 -ㅅ-;;
			RequestDispatcher rd = req.getRequestDispatcher("../Login/LoginForm.hs");
			rd.forward(req, resp);
			return false;
		}
		else {
			return true;			
		}
		
	}
	
	
}
