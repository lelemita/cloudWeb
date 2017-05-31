package com.hasom.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Error")
public class CommonErrorController {

	//private static final Logger logger = Logger.getLogger(this.getClass());
    private Logger logger = Logger.getLogger(this.getClass());
    
	private void pageErrorLog (HttpServletRequest req) {
		logger.info(">> Status_code		: " + req.getAttribute("javax.servlet.error.status_code"));
		logger.info(">> Exception_type	: " + req.getAttribute("javax.servlet.error.exception_type"));
		logger.info(">> Message			: " + req.getAttribute("javax.servlet.error.message"));
		logger.info(">> Request_URI		: " + req.getAttribute("javax.servlet.error.request_URI	"));
		logger.info(">> Exception		: " + req.getAttribute("javax.servlet.error.exception"));
		logger.info(">> Servlet_name	: " + req.getAttribute("javax.servlet.error.servlet_name"));	
	}
	
	@RequestMapping(value="/Throwable")
	public String throwable (HttpServletRequest req, Model model) {
		logger.info("throwable");
		pageErrorLog(req);
		model.addAttribute("msg", "예외가 던져졌습니다.");
		return "Error/ErrorPage";
	}
	
	@RequestMapping(value="/Exception")
	public String exception (HttpServletRequest req, Model model) {
		logger.info("exception");
		pageErrorLog(req);
		model.addAttribute("msg", "예외가 발생했습니다.");
		return "Error/ErrorPage";
	}
	
	@RequestMapping(value="/400")
	public String pageError400 (HttpServletRequest req, Model model) {
		logger.info("page error code 400");
		pageErrorLog(req);
		model.addAttribute("msg", "잘못된 요청입니다 (400)");
		return "Error/ErrorPage";
	}
	@RequestMapping(value="/403")
	public String pageError403 (HttpServletRequest req, Model model) {
		logger.info("page error code 403");
		pageErrorLog(req);
		model.addAttribute("msg", "금지된 접근 입니다 (403)");
		return "Error/ErrorPage";
	}
	@RequestMapping(value="/404")
	public String pageError404 (HttpServletRequest req, Model model) {
		logger.info("page error code 404");
		pageErrorLog(req);
		model.addAttribute("msg", "요청하신 페이지가 없습니다 (404)");
		return "Error/ErrorPage";
	}
	@RequestMapping(value="/405")
	public String pageError405 (HttpServletRequest req, Model model) {
		logger.info("page error code 405");
		pageErrorLog(req);
		model.addAttribute("msg", "요청된 메소드가 허용되자 않습니다(405)");
		return "Error/ErrorPage";
	}
	@RequestMapping(value="/500")
	public String pageError500 (HttpServletRequest req, Model model) {
		logger.info("page error code 500");
		pageErrorLog(req);
		model.addAttribute("msg", "서버에 오류가 발생했습니다ㅠㅠ (500)");
		return "Error/ErrorPage";
	}
	@RequestMapping(value="/503")
	public String pageError503 (HttpServletRequest req, Model model) {
		logger.info("page error code 503");
		pageErrorLog(req);
		model.addAttribute("msg", "서비스를 사용할 수 없습니다 (503)");
		return "Error/ErrorPage";
	}
	

	
}//클래스
