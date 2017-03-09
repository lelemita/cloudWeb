package com.hasom.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hasom.dao.MemberDAO;
import com.hasom.dao.SampleDAO;

@Service("memberService")
public class MemberService  implements SampleService{
	Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="memberDAO")
	private MemberDAO dao;
    @Resource(name="sampleDAO")
    private SampleDAO sampleDAO;
     
    @Override
    public List<Map<String, Object>> selectBoardList(Map<String, Object> map) throws Exception {
        return sampleDAO.selectBoardList(map);
    }
    
	/* 121227
	 * 로그인 처리 서비스
	 */
	public HashMap loginProc(String id, String pw) throws Exception {

		HashMap map = new HashMap();
		map.put("id", id);
		map.put("pw", pw);
		System.out.println(id + " : " + pw);
		HashMap result = dao.loginProc(map);

		// 결과 검사
		boolean isMember = false;
		if ( result == null ) {
			System.out.println(">> 회원 아님");
			result = new HashMap();
		}
		else {
			isMember = true;
			result.put("u_id", id);
			System.out.println(">> 회원이다 : " + result.get("u_name"));
		}
		result.put("ISMEMBER", isMember);
		
		System.out.println("서비스 끝");
	
		return result;
	}

}//class
