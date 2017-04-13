package com.hasom.data;

import java.util.ArrayList;

import com.hasom.util.PageUtil;

/* 2017.03.23. 남연우.
 * UserList 검색을 위한 데이터 +a (좀 찝찝?)
 */
public class SearchData {
	// 사용자 정보 또는 상세보기 대상 id
	private String u_id;
	// 검색 범위 및 검색어
	private String scope;
	private String keyword;
	// 페이지 정보
	private String nowPage;
	private PageUtil pInfo;
	// 조회한 사용자 목록
	private ArrayList userDataList;
	// 사용자 1명의 상세정보
	private UserData userData;
	// 소속 회사의 모든 그룹 정보
	private ArrayList allGroups;
	
	
	//getter&setter
	public ArrayList getUserDataList() {
		return userDataList;
	}
	public void setUserDataList(ArrayList userDataList) {
		this.userDataList = userDataList;
	}
	public UserData getUserData() {
		return userData;
	}
	public void setUserData(UserData userData) {
		this.userData = userData;
	}
	public ArrayList getAllGroups() {
		return allGroups;
	}
	public void setAllGroups(ArrayList allGroups) {
		this.allGroups = allGroups;
	}
	public PageUtil getpInfo() {
		return pInfo;
	}
	public void setpInfo(PageUtil pInfo) {
		this.pInfo = pInfo;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getNowPage() {
		return nowPage;
	}
	public void setNowPage(String nowPage) {
		this.nowPage = nowPage;
	}
	public String getU_id() {
		return u_id;
	}
	public void setU_id(String u_id) {
		this.u_id = u_id;
	}

	

	
}//class
