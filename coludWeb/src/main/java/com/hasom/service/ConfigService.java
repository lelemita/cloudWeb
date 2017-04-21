package com.hasom.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hasom.dao.ConfigDAO;
import com.hasom.data.JobData;
import com.hasom.data.SearchData;
import com.hasom.data.UserData;
import com.hasom.util.PageUtil;
import com.hasom.util.StringUtil;

@Service("configService")
public class ConfigService {
    // 조회할 잡 리스트를 구분하기 위한 필드 (e_no)
	public static final int DISCONTACT = -1;
	public static final int HIGH		= -1001;
	public static final int LOW			= -1002;
	public static final int CONTACT 	= -DISCONTACT;
	public static final int HIGHLOW		= -HIGH;
	public static final int LOWHIGH		= -LOW;
    
    
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

	/* 20170413 남연우
	 * 담당자 : 사용자 등록 폼 요청 필요 정보 : 담당 그룹 목록
	 */
	public SearchData prepareUserRegist(SearchData data, String u_id) {
		try {
			data.setAllGroups(dao.IDtoCompanyGroup(u_id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	// id 중복 검사
	public String IDChk(String id) {
		String result = "";
		try {
			String temp = dao.IDchk(id);
			if (temp!=null && temp.length()>0) {
				result=temp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//사용자 등록 작업
	public boolean userRegistryProc(UserData userData) {
		boolean isSuccess = true;
		try{
			dao.userRegistryProc(userData);
		}catch (Exception ex) {
			isSuccess = false;
			ex.printStackTrace();
		}
		return isSuccess;
	}

	/*
	 * 담당자 : 신규 회원 담당 그룹 등록
	 */
	public void newUserGroup (UserData data) {
		for(String strG_no : data.getCheckedGroups()) {
			try {
				dao.insertNewUserGroups(data.getU_id() , Integer.parseInt(strG_no));					
			}catch (Exception ex) {
				ex.printStackTrace();
				continue;
			}
		}//for
	}

	// 그룹, 요소 → 센서 정보 조회
	public ArrayList getSensorInfos(int g_no, String nowFactor) {
		try {
			HashMap paramap = new HashMap();
			paramap.put("g_no", g_no);
			paramap.put("f_table_name", nowFactor);
			return dao.getSensorInfo(paramap);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}//method

	// 설정 변경 사항 저장
	public boolean saveChangeSettings(String[] changes) {
	try{
		//	필요 정보
		for(String ch : changes) {			
			// 각 변경사항 저장 7-l_highlow$29.5
			String[] temp	= ch.split("\\$");
			String[] target	= temp[0].split("-");
			String str_l_no	= target[0];
			int 	l_no = Integer.parseInt(str_l_no);
			String	field	= target[1];
			String	value	= temp[1];
			HashMap paramap = new HashMap();
			paramap.put("l_no" , l_no);
			paramap.put("field" , field);
		
			// s_display 인 경우
			if(field.equals("s_display")){
				paramap.put("value", value);
				dao.change_s_display(paramap);
			}
			// l_delay, l_re_alarm 인 경우
			else if(field.equals("l_delay") || field.equals("l_re_alarm")){
				paramap.put("value", Integer.parseInt(value));
				dao.changeSettings(paramap);
			}
			// 그 외
			else {
				paramap.put("value", value);
				dao.changeSettings(paramap);
			}
		}//for
		return true;
	}catch(Exception ex) {
		ex.printStackTrace();
		return false;
	}
	}//함수

	//해당 그룹 + 표준 잡 정보 서비스
	public ArrayList<JobData> getJobList(int g_no) {
		try {
			return dao.getJobList(g_no);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList();
		}
	}

	//해당 j_no 삭제 서비스
	public boolean deleteJob(int j_no) {
		boolean isSuccess = true;
		try {
			dao.deleteJob(j_no);
		}catch (Exception ex){
			isSuccess = false;
			ex.printStackTrace();
		}
		return isSuccess;
	}

	// 수정, 복제인 경우, 해당 잡의 내용을 찾아서 보냄
	public JobData getJobData(int j_no) {
		try {
			return dao.getJobData(j_no);
		} catch (Exception e) {
			e.printStackTrace();
			return new JobData();
		}
	}

	// 전송방법 종류 조회
	public ArrayList<String> getJobTypeList() {
		try {
			return dao.getJobTypeList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	// 잡 등록, 수정 서비스
	public boolean adjustJob(String workType , JobData data) {
		try{
			if(workType.equals("수정")){
				dao.updateJob(data);				
			}else{
				dao.insertJob(data);
			}
			return true;
		}catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// 해당 그룹의 통신 장애, 복구에 대한 대응 잡 내용 조회
	public ArrayList<JobData> getEventJobList(int e_no, int g_no) {
		ArrayList<JobData> jobList = new ArrayList<JobData>();
		try {
			HashMap<String, Integer> paramap = new HashMap<String, Integer>();
			paramap.put("e_no", e_no);
			paramap.put("no", g_no);	
			String j_nos = dao.getJ_nos(paramap);
			String[] j_noArray = j_nos.split(",");
			for(String j_no : j_noArray){
				jobList.add( dao.getJobData(Integer.parseInt(j_no.trim())) );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobList;
	}
	
	
	
}//class
