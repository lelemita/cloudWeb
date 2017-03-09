package com.common.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
/*
	http://addio3305.tistory.com/72 여기를 참조한 방식
	AbstractDAO를 상속 받아서 사용한다.
	저 분은, 
	sqlSession의 쿼리 실행 함수들을 따로 만들어 두었다.
	로그를 기록하기 위해서 인듯..
	그래서 DAO들에서는 
	sqlSession 대신 
	AbstractDAO의 함수들을 사용하면 된다.
	(ex. sqlSession.selectOne(~) ==> this.selectOne(~) )ogger
	
*/
public class AbstractDAO {
	protected Log log = LogFactory.getLog(AbstractDAO.class);
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	protected void printQueryId(String queryId) {
		if(log.isDebugEnabled()){
			log.debug("\t QueryId  \t:  " + queryId);
		}
	}
	
	public Object insert(String queryId, Object params){
		printQueryId(queryId);
		return sqlSession.insert(queryId, params);
	}
	
	public Object update(String queryId, Object params){
		printQueryId(queryId);
		return sqlSession.update(queryId, params);
	}
	
	public Object delete(String queryId, Object params){
		printQueryId(queryId);
		return sqlSession.delete(queryId, params);
	}
	
	public Object selectOne(String queryId) throws Exception{
		printQueryId(queryId);
		return sqlSession.selectOne(queryId);
	}
	
	public Object selectOne(String queryId, Object params) throws Exception{
		printQueryId(queryId);
		return sqlSession.selectOne(queryId, params);
	}
	
	@SuppressWarnings("rawtypes")
	public List selectList(String queryId) throws Exception{
		printQueryId(queryId);
		return sqlSession.selectList(queryId);
	}
	
	@SuppressWarnings("rawtypes")
	public List selectList(String queryId, Object params) throws Exception {
		printQueryId(queryId);
		return sqlSession.selectList(queryId,params);
	}
}
