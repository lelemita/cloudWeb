<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>하솜 정보기술 : 사용자 상세정보</title>
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

<style>
	.floating-box {
	    display: inline-block;
	    width: 150px;
	    margin: 5px;
	}
	.panel-body {
	  padding: 3px;
	}
	.panel-heading {
	  padding: 3px;
	}
	.panel-footer {
	  padding: 3px;
	}
	.well {
	  min-height: 20px;
	  padding: 19px;
	  margin-bottom: 20px;
	  background-color: #ffffff;
	  border: 1px solid #c3c3c3;
	  border-radius: 3px;
	  -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .05);
	          box-shadow: inset 0 1px 1px rgba(0, 0, 0, .05);
	}
	.container {
	  padding-right: 15px;
	  padding-left: 15px;
	  margin : auto;
	  width : 95%;
	}
</style>
<script>
	$(document).ready(function(){
		//pop 메시지
		if("${DATA.popMsg}".length!=0) {
			alert("${DATA.popMsg}");
		}else {
			alert("pop 없음");
		}
		
		//초기 세팅
		// 1) u_type, u_state
		$("#u_type").val("${DATA.userData.u_type}").prop("selected",true);
		$("#u_state").val("${DATA.userData.u_state}").prop("selected",true);
		// 2) 담당 그룹
		<c:forEach items="${DATA.userData.g_no_g_names}" var="gr">
			$("#gr_${gr.g_no}").attr("checked" , true);
		</c:forEach>
		
		//document.write( '${DATA.userDataList[1].u_name}' );	
	});
	// 어딘가로 보내는 함수 - form은 헤더에 있다
	function move(url){
		$("#gogo").attr("action" , url)
		$("#gogo").submit();	
	}
	
	// 목록가기 이벤트
	function submitForm(url) {
		$("#sfrm").attr("action" , url);
		$("#sfrm").submit();
	}
	
	// 수정 / 삭제하기 이벤트
	function modify(url) {
		//비밀번호 검사 (Ajax)
		var pw = prompt("비밀번호를 입력해주세요");
		if(pw.length==0) { return; }
		checkPassword(pw , url);
		// 이하 작업은 checkPassword(pw) 에서 진행
	}
	function checkPassword(pw , url) {
		$.ajax({
			url		:	"../Config/PasswordChk.hs", 
			type	:	"POST",
			data	: {'id': '${sessionScope.ID}' , 'pw': pw , 'temp': new Date() },
			dataType	:	"text",
			success	: function(data) {
				var u_type = data.trim();
				if (u_type == "A"){
					//폼 전송
					$("#sfrm").attr("action" , url);
					$("#sfrm").submit();				
				}
				else {
					alert("비밀번호가 틀리거나, 담당자가 아닙니다.");
				}
			}, 
			error	:	 function(request,status,error){
		    alert("비밀번호 확인 과정에서 에러\n" +"code:"+request.status+"\n"
		    		    +"message:"+request.responseText+"\n"+"error:"+error);
			}
		});//ajax
	}	

</script>
</head>
<body>
	<header>
		<%@ include file= "../Common/Header.jsp" %>
	</header>

	<div class="container" >
		<div class="well col-sm-10 col-sm-offset-1">
			<h3 class="page-header" align="center" >${DATA.userData.c_name} 사원 정보 : ${DATA.userData.u_name}</h3>
			<!-- 조회 결과 -->

		
			<div class="col-sm-12" style="padding-top: 20px"><!-- 한줄로 표시 -->		
				<div class="form-group col-sm-6">
					<label class="col-sm-6">이름 :</label>
					<label class="col-sm-6">${DATA.userData.u_name}</label>
				</div>		
				
				<div class="form-group col-sm-6" ">
					<label class="col-sm-6">아이디 : </label>
					<label class="col-sm-6">${DATA.userData.u_id}</label>
				</div>
			</div><!-- 줄바꿈 -->
				
			
			<div class="col-sm-12"><!-- 한줄로 표시 -->	
				<div class="form-group col-sm-6">
					<label class="col-sm-6">전화번호 : </label>
					<div class="col-sm-6">${DATA.userData.u_tel}</div>
				</div>	
						
				<div class="form-group col-sm-6">
					<label class="col-sm-6">이메일 : </label>
					<div class="col-sm-6">${DATA.userData.u_mail}</div>
				</div>		
			</div><!-- 줄바꿈 -->
				
			<div class="col-sm-12"><!-- 한줄로 표시 -->	
				<div class="form-group col-sm-6" ">
					<label class="col-sm-6">SMS 알람 여부 : </label>
					<div class="col-sm-6">
						<c:if test="${DATA.userData.u_mail_on eq 'Y'}">ON</c:if>
						<c:if test="${DATA.userData.u_mail_on eq 'N'}">OFF</c:if>
					</div>
				</div>
				
				<div class="form-group col-sm-6">
					<label class="col-sm-6">email 알람 여부 : </label>
					<div class="col-sm-6">
						<c:if test="${DATA.userData.u_mail_on eq 'Y'}">ON</c:if>
						<c:if test="${DATA.userData.u_mail_on eq 'N'}">OFF</c:if>
					</div>
				</div>			
			</div><!-- 줄바꿈 -->
				
			<div class="col-sm-12"><!-- 한줄로 표시 -->
				<div class="form-group col-sm-6">
					<label class="col-sm-6">자동 보고 빈도 : </label>
					<div class="col-sm-6">
						<c:if test="${DATA.userData.u_report eq 'X'}">사용하지 않음</c:if>
						<c:if test="${DATA.userData.u_report eq 'M'}">월간보고 받음</c:if>
						<c:if test="${DATA.userData.u_report eq 'W'}">주간보고 받음</c:if>
						<c:if test="${DATA.userData.u_report eq 'D'}">일간보고 받음</c:if>
					</div>
				</div>
				
				<div class="form-group col-sm-6">
				</div>
			</div><!-- 줄바꿈 -->	

			<!-- 가로선 -->	
			<hr align="center" color="#eee" size="1px" width="90%">


		<!-- 수정 / 목록복귀 요청용 폼 -->
		<form method="post" id="sfrm">
			<!-- 상세정보 주인 id -->
			<input type="hidden" name="u_id" value="${DATA.userData.u_id}">
			<!-- 목록 복귀를 위한 릴레이 데이터 -->
			<input type="hidden" name="scope" value="${DATA.scope}">
			<input type="hidden" name="keyword" value="${DATA.keyword}">
			<input type="hidden" name="nowPage" value="${DATA.nowPage}">

			<div class="col-sm-12" style="padding-top: 20px"><!-- 한줄로 표시 -->			
				<div class="form-group col-sm-6">
					<label class="col-sm-4">직위 :</label>
					<div class="col-sm-6">
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
					<div class="col-sm-6">
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
				<button type="button" class="btn btn-default" onclick="modify('../Admin/UserModiProc.hs');">수정하기</button>
	      <button type="button" class="btn btn-default" onclick="location.reload();">초기화</button>		
	      <button type="button" class="btn btn-default" onclick="modify('../Admin/UserDelProc.hs');">삭제하기</button>
	      <button type="button" class="btn btn-default" onclick="submitForm('../Admin/UserList.hs');">목록보기</button>
			</div>
			
			
		</form>

		</div><!-- well -->
	</div><!-- container -->

</body>
</html>