package com.hasom.util;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/*	160908
 * 	이 클래스는 인터셉터 처리를 위한 클래스 이다.
 * 	인터셉터는,스프링이 컨트롤러를 실행하기 전에 인터셉터해서 다른 함수를 먼저 실행하는 것이다.
 * 
 * 	인터셉터 함수는 이미 만들어져 있다 : preHandle() ★★★
 * 	그 함수가 존재하는 상위 클래스가 바로   HandlerInterceptorAdapter 이다.
 */
public class IsLoginCheck extends HandlerInterceptorAdapter {
	// 인터셉터한 후 실행할 함수가 이미 정해져 있다.
	@Override
	public boolean preHandle 
	(HttpServletRequest req, HttpServletResponse resp, Object handle) throws Exception {
		// 이 함수가 true를 반환하면, 컨트롤러가 제대로 실행되고
		// 이 함수가 false를 반환하면, 컨트롤러가 실행되지 못한다.
		
		System.out.println(">> 멤버검사 인터셉터 실행");
		// 이후 부터는 각자의 목적에 맞는 작업을 해주면 된다.
		HttpSession session = req.getSession();
		String id = (String) session.getAttribute("ID");
		if (!StringUtil.isNull(id)) {
			// 로그인 했으면
			return true;
		}
		else {
			// 컨트롤러가 실행하지 못한다 ==> 뷰를 호출하지 못한다 ==> 뷰가 없다는 에러가 발생한다.★
			// ★ 뷰를 강제로 만들어 주자
			//resp.sendRedirect("../Member/LoginForm.lost"); 	// ==> 로그인으로 보내기
			
			// 들어온 요청 파악하기
			String uri = req.getRequestURI();
			//	System.out.println(">> 인터셉터 toGo : " + uri);	// 점검용
			
			req.setAttribute("toGo", uri); 	//안해도 포워드라 자동으로 간다.  .. 아닌가 -ㅅ-;;
			RequestDispatcher rd = req.getRequestDispatcher("../Login/LoginForm.hs");
			rd.forward(req, resp);
			return false;
		}
	}//함수

}//클래스
