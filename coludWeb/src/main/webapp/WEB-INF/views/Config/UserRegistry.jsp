<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>하솜 정보기술 : 사용자 등록</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- jQuery -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js" type="text/javascript"></script>
<!-- Bootstrap Core JavaScript -->
<script src="../resources/bootstrap/sb-admin-2/vendor/bootstrap/js/bootstrap.min.js"></script>
<!-- Bootstrap Core CSS -->
<link href="../resources/bootstrap/sb-admin-2/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- sb-admin-2 : MetisMenu CSS -->
<link href="../resources/bootstrap/sb-admin-2/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">
<!-- sb-admin-2 : Custom CSS -->
<link href="../resources/bootstrap/sb-admin-2/dist/css/sb-admin-2.css" rel="stylesheet">
<!--sb-admin-2 :  Custom Fonts -->
<link href="../resources/bootstrap/sb-admin-2/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<!-- 직접 작성 -->
<link href="../resources/css/common.css?ver=2" rel="stylesheet" type="text/css">
<script src="../resources/js/config/userRegistry.js?ver=3"></script>

</head>
<body>
	<header>
		<%@ include file= "../Common/Header.jsp" %>
	</header>

	<!-- 외부 js에서 접근을 못해서.. -->
	<input type="hidden" value="${ID}" id="myid">
	<input type="hidden" value="${DATA.popMsg}" id="pop">
	
	<div class="container" >
		<div class="well col-sm-10 col-sm-offset-1">
			<h3 class="page-header" align="center" >${C_NAME} 사용자 등록</h3>
			
			<!-- 등록 / 목록복귀 요청용 폼 -->
			<form method="post" id="sfrm">			
				<input type="hidden" id="u_id" name="u_id">
				<input type="hidden" name="c_no" value="${C_NO}">
				<!-- 목록 복귀를 위한 릴레이 데이터 -->
				<input type="hidden" name="scope" value="${DATA.scope}">
				<input type="hidden" name="keyword" value="${DATA.keyword}">
				<input type="hidden" name="nowPage" value="${DATA.nowPage}">
						
				<div class="col-sm-12" style="padding-top: 20px"><!-- 한줄로 표시 -->							
					<div class="form-group col-sm-6">
						<label class="col-sm-4">아이디 : </label>
						<div class="col-sm-7">
							<input type="text" class="form-control" id="temp_id" placeholder="신규 ID"/>
						</div>
					</div>
					
					<div class="form-group col-sm-6" id="msg"><!-- id검사결과 자리 --></div>	
				</div><!-- 줄바꿈 -->
			
				<div class="col-sm-12"><!-- 한줄로 표시 -->												
					<div class="form-group col-sm-6">
						<label class="col-sm-4">초기 비밀번호 :</label>
						<div class="col-sm-7">
							<input type="text" class="form-control" id="u_pw" name="u_pw" placeholder="생략시 ID 동일"/>
						</div>
					</div>	
					
					<div class="form-group col-sm-6"></div>						
				</div><!-- 줄바꿈 -->
	
			
				<div class="col-sm-12" style="padding-top: 20px"><!-- 한줄로 표시 -->		
					<div class="form-group col-sm-6">
						<label class="col-sm-4">이름 :</label>
						<div class="col-sm-7">
							<input type="text" class="form-control" id="u_name" name="u_name" placeholder="이름을 입력해주세요"/>
						</div>
					</div>		
					
					<div class="col-sm-6"></div>
				</div><!-- 줄바꿈 -->
				
				<div class="col-sm-12"><!-- 한줄로 표시 -->	
					<div class="form-group col-sm-6">
						<label class="col-sm-4">전화번호 : </label>
						<div class="col-sm-7">
							<input type="tel" class="form-control" id="u_tel" name="u_tel" placeholder="0101234567"/>
							<!-- 숫자만 있도록 검사하기★★★★★★ -->
						</div>
					</div>	
							
					<div class="form-group col-sm-6">
						<label class="col-sm-4">이메일 : </label>
						<div class="col-sm-7">
							<input type="email" class="form-control" id="u_mail" name="u_mail" placeholder="id@mail.com"/>
						</div>
					</div>		
				</div><!-- 줄바꿈 -->
					
				<div class="col-sm-12"><!-- 한줄로 표시 -->	
					<div class="form-group col-sm-6" ">
						<label class="col-sm-5">SMS 알람 여부 : </label>
						<div class="col-sm-6">
							<label class="radio-inline">
								<input type="radio" id="u_tel_on" name="u_tel_on" value="Y">ON   
							</label>
							<label class="radio-inline">
								<input type="radio" id="u_tel_on" name="u_tel_on" value="N" checked>OFF
							</label>
						</div>
					</div>
					
					<div class="form-group col-sm-6">
						<label class="col-sm-5">email 알람 여부 : </label>
						<div class="col-sm-6">
							<label class="radio-inline">
								<input type="radio" id="u_mail_on" name="u_mail_on" value="Y">ON   
							</label>
							<label class="radio-inline">
								<input type="radio" id="u_mail_on" name="u_mail_on" value="N" checked>OFF
							</label>
						</div>
					</div>			
				</div><!-- 줄바꿈 -->
					
				<div class="col-sm-12"><!-- 한줄로 표시 -->
					<div class="form-group col-sm-6">
						<label class="col-sm-4">자동 보고 빈도 : </label>
						<div class="col-sm-7">
							<select class="form-control" id="u_report" name="u_report" >
								<option value="X">사용하지 않음 (개발중)</option>
								<option value="M">월간보고 받음 (개발중)</option>
								<option value="W">주간보고 받음 (개발중)</option>
								<option value="D">일간보고 받음 (개발중)</option>
							</select>
					</div>
					
					<div class="form-group col-sm-6">
					</div>
				</div>
			</div><!-- 줄바꿈 -->	
			
			<!-- 가로선 -->	
			<hr align="center" color="#eee" size="1px" width="90%"/>


			<div class="col-sm-12" style="padding-top: 20px"><!-- 한줄로 표시 -->			
				<div class="form-group col-sm-6">
					<label class="col-sm-4">직위 :</label>
					<div class="col-sm-7">
						<select class="form-control" id="u_type" name="u_type">
							<option value="U">일반</option>
							<option value="M">관리자</option>
							<option value="A">담당자</option>
						</select>
					</div>
					<div class="col-sm-2"></div>
				</div>
				
				<div class="form-group col-sm-6">
					<label class="col-sm-4">상태 :</label>
					<div class="col-sm-7">
						<select class="form-control" id="u_state" name="u_state">
							<option value="Y">정상</option>
							<option value="N">차단</option>
							<option value="X" disabled="disabled">탈퇴</option>
						</select>
					</div>
					<div class="col-sm-2"></div>
				</div>
			</div><!-- 줄바꿈 -->		
	
			<div class="col-sm-12"><!-- 한줄로 표시 -->					
				<div class="form-group col-sm-12">
					<label class="col-sm-2">담당 그룹 : </label>
					<div class="col-sm-10" >
						<c:forEach items="${DATA.allGroups}" var="gr" varStatus="st">
							<input type="checkbox" name="checkedGroups" id='gr_${gr.g_no}' value='${gr.g_no}'> ${gr.g_name} &nbsp;&nbsp;&nbsp;  
						</c:forEach>		
					</div>
				</div>
			</div><!-- 줄바꿈 -->				
			
			<!-- 수정, 확인 버튼 -->
			<div class="col-sm-12"></div>
			<div class="col-sm-12" align="center"  style="padding-bottom: 30px">
				<button type="button" class="btn btn-default" onclick="register('../Admin/UserRegistryProc.hs');">등록하기</button>
	      <button type="button" class="btn btn-default" onclick="location.reload();">초기화</button>		
	      <button type="button" class="btn btn-default" onclick="submitForm('../Admin/UserList.hs');">목록보기</button>
			</div>
			
			
		</form>

		</div><!-- well -->
	</div><!-- container -->

</body>
</html>