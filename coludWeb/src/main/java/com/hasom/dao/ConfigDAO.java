package com.hasom.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.common.dao.AbstractDAO;
import com.hasom.data.JobData;
import com.hasom.data.SearchData;
import com.hasom.data.UserData;
/*
 *	2017.03.21.남연우.
 *	환경설정용 DAO	
 */
@Repository("configDAO")
public class ConfigDAO  extends AbstractDAO{

	/*
	 * 개인정보 조회 요청
	 */
	public UserData PersonalSetting (String id) throws Exception {
		return (UserData) selectOne("member.PersonalSetting", id);
	}
	
	/*
	 * 아이디로 담당 그룹 목록 조회하기
	 */
	public ArrayList IDtoGroupList (String id) throws Exception {
		return (ArrayList) selectList("member.IDtoGroupList" , id);
	}

	// Ajax: 비밀번호 확인
	public String PasswordChk(String id, String pw) throws Exception {
		HashMap paramap = new HashMap();
		paramap.put("id", id);
		paramap.put("pw", pw);
		return (String) selectOne("member.PasswordChk" , paramap);
	}
	
	// 개인정보 수정 요청
	public void PersonalSettingProc (UserData data) {
		this.update("member.PersonalSettingProc", data);
	}
	// 비밀번호 수정
	public void newPassword(UserData data) {
		update("member.newPassword", data);
	}

	//담당자 : 검색 내용 반영하여, 소속 회사의 사원 목록수 구하기
	public Integer getUserListCount(SearchData data) throws Exception {
		return (Integer) selectOne("member.getUserListCount" , data);
	}
	//담당자 : 검색 내용 반영하여, 소속 회사의 사원 목록 조회하기
	public ArrayList getUserList(SearchData data) throws Exception {
		return (ArrayList) selectList("member.getUserList" , data);
	}
	// 소속 회사의 모든 그룹 목록 조회
	public ArrayList IDtoCompanyGroup(String id) throws Exception {
		return (ArrayList) selectList("member.getCompanyGroups" , id);
	}
	// 담당자 : 직원 권한 수정
	public void modifyUserData(UserData data) throws Exception {
		update("member.modifyUserData" , data);
	}
	// group_user table에서 해당 아이디의 row를 모두 삭제
	public void deleteUserGroup(String u_id) {
		delete("member.deleteUserGroup" , u_id);
	}
	// group_user table에 입력받은 g_no - u_id 입력
	public void insertNewUserGroups(String u_id, int g_no) {
		HashMap paramap = new HashMap();
		paramap.put("g_no", g_no);		
		paramap.put("u_id", u_id);
		insert("member.insertNewUserGroups" , paramap);
	}
	
	// 아이디 중복 검사
	public String IDchk(String id) throws Exception {
		return (String) selectOne("member.IDchk" , id);
	}

	// 사용자 등록 작업
	public void userRegistryProc(UserData userData) {
		insert("member.userRegistryProc" , userData);		
	}

	public ArrayList getSensorInfo(HashMap paramap) throws Exception {
		return (ArrayList) selectList("config.SensorInfos" , paramap);
	}

	// 설정 변경 사항 저장 : s_display
	public void change_s_display(HashMap paramap) {
		update("config.saveChange_s_display" , paramap);
	}

	// 설정 변경 사항 저장
	public void changeSettings(HashMap paramap) {
		update("config.saveChange" , paramap);
	}
	
	//해당 그룹 + 표준 잡 정보 서비스
	public ArrayList<JobData> getJobList(int g_no) throws Exception {
		return (ArrayList<JobData>) selectList("config.getJobList" , g_no);
	}

	//해당 j_no 삭제 
	public void deleteJob(int j_no) {
		this.delete("config.deleteJob", j_no);
	}

	// 수정, 복제인 경우, 해당 잡의 내용을 찾아서 보냄
	public JobData getJobData(int j_no) throws Exception {
		return (JobData) selectOne("config.getJobData" , j_no);
	}

	// 전송방법 종류 조회
	public ArrayList<String> getJobTypeList() throws Exception {
		return (ArrayList<String>) selectList("config.getJobTypeList");
	}

	// 잡 등록 서비스
	public void insertJob(JobData data) throws Exception {
		insert("config.insertJob" , data);			
	}
	// 잡 수정 서비스
	public void updateJob(JobData data) throws Exception {
		update("config.updateJob", data);
	}
	// 해당 그룹의 통신 장애, 복구에 대한 대응 잡 내용 조회
	public ArrayList<JobData> getEventJobList(HashMap paramap) throws Exception {
		return (ArrayList<JobData>)selectList("config.getEventJobList",paramap);
	}
	// e_no , (g_no or l_no) → j_nos 구하기
	public String getJ_nos(HashMap paramap) throws Exception {
		return (String)selectOne("config.getJ_nos", paramap);
	}
}//class