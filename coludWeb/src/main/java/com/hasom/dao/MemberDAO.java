package com.hasom.dao;

import java.util.HashMap;
import org.springframework.stereotype.Repository;
import com.common.dao.AbstractDAO;
/*
 *	2016.12.28.남연우.
 	http://addio3305.tistory.com/72 여기를 참조한 방식
 	AbstractDAO를 상속 받아서 사용한다.
 	저 분은, 
 	sqlSession의 쿼리 실행 함수들을 따로 만들어 두었다.
 	로그를 기록하기 위해서 인듯..
 	그래서 DAO들에서는 
 	sqlSession 대신 
 	AbstractDAO의 함수들을 사용하면 된다.
 	(ex. sqlSession.selectOne(~) ==> this.selectOne(~) )	
 */
@Repository("memberDAO")
public class MemberDAO  extends AbstractDAO{

	
	/*
	 * 로그인 처리 질의 실행 메서드
	 */
	public HashMap loginProc (HashMap map) throws Exception {
		return (HashMap) selectOne("member.loginProc", map);
	}
	

}//class