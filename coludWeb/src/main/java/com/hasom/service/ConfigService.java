package com.hasom.service;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hasom.dao.ConfigDAO;
import com.hasom.data.SearchData;
import com.hasom.data.UserData;
import com.hasom.util.PageUtil;
import com.hasom.util.StringUtil;

@Service("configService")
public class ConfigService {
    Logger log = Logger.getLogger(this.getClass());
    @Resource(name="configDAO")
    private ConfigDAO dao;
    
    /*
     * 개인정보 조회 서비스
     */
    public UserData PersonalSetting (String id) throws Exception {
    	// 개인 정보 조회
    	UserData data = dao.PersonalSetting(id);
    	// 담당 그룹 정보 map의 리스트 
    	data.setG_no_g_names(dao.IDtoGroupList(id));
    	return data;
    }

    /*
     *  Ajax : 비밀번호 확인 (u_type 반환)
     */
	public String PasswordChk(String id, String pw){
		// 맞으면 해당회원의 u_type 반환 / 아니면 'X' 반환
		String u_type = "X";
		try {
			String temp = dao.PasswordChk(id, pw);
			if (temp != null) {
				u_type = temp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return u_type;
	}
    
	/*
	 * 개인정보 변경 처리 서비스
	 */
    public void PersonalSettingProc(UserData data) {
    	if (data.getNew_pw()!=null && data.getNew_pw().length()>0) {
    		// 비밀번호를 변경하는 경우
    		dao.newPassword(data);
    	}
    	// 나머지 수정
    	dao.PersonalSettingProc(data);
    }

    /*
     * 담당자 : 검색 내용 반영하여, 소속 회사의 사원 목록 조회하기
     */
	public SearchData getUserList(SearchData data) {
		// 반환할 DataBean (=VO)
		SearchData resultData = data;
		try {
			//검색키워드 반영하여, 본인의 소속 회사의 모든 사용자 목록 & 페이지 정보 (탈퇴 제외) 서비스
			// 1) 총 목록수 구하기
			int totalCount = dao.getUserListCount(data);
			// 2) 페이지 정보 구하기
			int nowPage = StringUtil.isNaturalNum(1, data.getNowPage());
			PageUtil pInfo = new PageUtil(nowPage, totalCount, 10,10 );
			// 3) 전체 목록 구하기
			ArrayList allList = dao.getUserList(data);
			// 4) 현재 페이지에 해당하는 목록만 잘라내기
			ArrayList userList = new ArrayList();
			int	start	= (pInfo.getNowPage()-1) * pInfo.getPageList();
			int	end		= start + pInfo.getPageList() - 1;
			for (int i=start ; i<=end && i<pInfo.getTotalCount(); i++) {
				// 마지막 페이지인 경우에는 10개가 안 될 수 있으므로 체크해 두어야 한다.
				userList.add(allList.get(i));
			}
			// VO에 넣기
			resultData.setpInfo(pInfo);
			resultData.setUserDataList(userList);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultData;
	}

	/*
	 * 담당자 : 직원 상세정보 조회하기
	 */	
	public SearchData getUserDetail(SearchData data) {
		// 해당 직원의 아이디
		String target_id = data.getU_id();
		try {
			// 해당 직원의 정보 조회
			UserData userData = dao.PersonalSetting(target_id);
			// 해당 직원의 담당 그룹 목록 조회
			ArrayList targetGroups = dao.IDtoGroupList(target_id);
			userData.setG_no_g_names(targetGroups);
			data.setUserData(userData);
			
			// 해당 회사의 모든 그룹 목록 조회
			ArrayList groupsInCompany = dao.IDtoCompanyGroup(target_id);
			data.setAllGroups(groupsInCompany);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/*
	 * 담당자 : 회원 정보 수정
	 */
	public void modifyUserData(UserData data) {
		try {
			//u_type, u_state 수정
			dao.modifyUserData(data);
			//담당 그룹 정보 수정
			//★★★ 좀 번잡한 듯.. delete 쓰는 것도 뭔가 찝찝하고,
			//		기회가 있다면, DB 구조를 개선해 보는 것도 좋을 듯 하다.
			// 1) group_user table에서 해당 아이디의 row를 모두 삭제
			dao.deleteUserGroup(data.getU_id());
			// 2) group_user table에 입력받은 g_no - u_id 입력
			for(String strG_no : data.getCheckedGroups()) {
				try {
					dao.insertNewUserGroups(data.getU_id() , Integer.parseInt(strG_no));					
				}catch (Exception ex) {
					ex.printStackTrace();
					continue;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	/*
	 * 담당자 : 직원 상세정보 조회 페이지에서 회원 삭제
	 */
	public void delUserData(UserData data) {
		try{
			// 탈퇴 u_type="X" 로 변경하기
			data.setU_state("X");
			dao.modifyUserData(data);
			// group_user 테이블에서 해당 사용자 삭제하기
			dao.deleteUserGroup(data.getU_id());
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
}//class
