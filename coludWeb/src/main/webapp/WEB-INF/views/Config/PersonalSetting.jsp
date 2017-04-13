<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 날짜형식 지정 태그 라이브러리 -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- 문자열 치환을 위한 태그 라이브러리 -->
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title>하솜 정보기술 : 개인정보 설정</title>
<meta charset=UTF-8>
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
	$(function() {		
		//초기 세팅
		// 0) 처리결과 메시지 있으면 보여줌
		if ('${MSG}'!= null && '${MSG}'.length>0 ) {
			alert('${MSG}');
		}
		// 1) sms,mail 수신여부 라디오버튼 선택
		$("#u_tel_on[value=${DATA.u_tel_on}]").attr("checked",true);
		$("#u_mail_on[value=${DATA.u_mail_on}]").attr("checked",true);
		// 2) 보고 주기 선택
		$("#u_report").val("${DATA.u_report}").prop("selected", true);
		
	});

	// 어딘가로 보내는 함수 form은 헤더에 있다
	function move(url){
		$("#gogo").attr("action" , url);
		$("#gogo").submit();
	}	
	
	// 개인정보 수정 요청
	function modifyInfo() {
		//무결성 검사
		if ( $("#pwd1").val() != $("#pwd2").val() ) {
			alert("변경하려는 비밀번호와 재확인 값이 일치하지 않습니다.");
			return;
		}
		else{
			// 나중에:  비밀번호 정규식 검사 추가★★★★
			//					나머지 항목들도!!!!!
			
		}
		if ( $("#u_tel").val() == null || $("#u_tel").val().length==0 ) {
			alert("전화번호를 입력해주세요.");
			return;
		}
		if ( $("#u_mail").val() == null || $("#u_mail").val().length==0 ) {
			alert("이메일 주소를 입력해주세요.");
			return;
		}		
		else if ( $("#u_mail").val().indexOf("@") <= 0 ) {
			alert("이메일 양식이 올바르지 않습니다.");
			return;
		}				
		//비밀번호 검사 (Ajax)
		var pw = prompt("비밀번호를 입력해주세요");
		checkPassword(pw);
		// 이하 작업은 checkPassword(pw) 에서 진행
	}
	
	function checkPassword(pw) {
		$.ajax({
			url		:	"../Config/PasswordChk.hs", 
			type	:	"POST",
			data	: {'id': '${sessionScope.ID}' , 'pw': pw , 'temp': new Date() },
			dataType	:	"text",
			success	: function(data) {
				var u_type = data.trim();
				if (u_type != "X"){
					// 임시 : u_report용 select disabled 취소하기
					$("#u_report").attr("disabled" , false);
					//폼 전송
					$("#sfrm").submit();					
				}
				else {
					alert("비밀번호가 틀립니다.");
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
	
	<!-- 본문 영역 -->
	<div class="container" align="center">
		<form method="post" id="sfrm" action="../Config/PersonalSettingProc.hs">
		<input type="hidden" name="u_id" value="${DATA.u_id}">		
		<div class="well col-sm-8 col-sm-offset-2" align="left" >
			<h3 class="page-header" align="center">개인 정보 조회</h3>
			
			<div class="form-group col-sm-12"  style="padding-top: 30px">
				<label class="col-sm-3">이름 (소속) :</label>
				<label class="col-sm-9">${DATA.u_name} (${DATA.c_name})</label>
			</div>
			<div class="form-group col-sm-12">
				<label class="col-sm-3">직위 (상태) :</label>
				<label class="col-sm-9">${DATA.strType} (${DATA.strState})</label>
			</div>
			<div class="form-group col-sm-12">
				<label class="col-sm-3">담당 그룹 : </label>
				<label class="col-sm-9">
					<c:forEach items="${DATA.g_no_g_names}" var="data" varStatus="st">
						${data.g_name}
						<c:if test="${!st.last}"> , </c:if>
					</c:forEach>		
				</label>
			</div>
			
			<div class="form-group col-sm-12" style="padding-top:20px;">
				<label class="col-sm-3">아이디 : </label>
				<label class="col-sm-9">${DATA.u_id}</label>
			</div>
			
			<div class="form-group col-sm-12">
				<label class="col-sm-3">비밀번호 변경 : </label>
				<div class="col-sm-4">
					<input type="password" class="form-control" id="pwd1" placeholder="변경할 비밀번호">
				</div>
				<div class="col-sm-4">
					<input type="password" class="form-control" id="pwd2" name="new_pw" placeholder="변경 비밀번호 재확인">
				</div>
				<div class="col-sm-1"></div>
			</div>
			
			<div class="form-group col-sm-12">
				<label class="col-sm-3">전화번호 : </label>
				<div class="col-sm-8">
					<input type="tel" class="form-control" id="u_tel" name="u_tel" value="${DATA.u_tel}">
				</div>
				<div class="col-sm-1"></div>
			</div>	
					
			<div class="form-group col-sm-12">
				<label class="col-sm-3">이메일 : </label>
				<div class="col-sm-8">
					<input type="email" class="form-control" id="u_mail" name="u_mail" value="${DATA.u_mail}">
				</div>
				<div class="col-sm-1"></div>
			</div>		
			
			<div class="form-group col-sm-12" style="padding-top:20px;">
				<label class="col-sm-3">SMS 알람 여부 : </label>
				<div class="col-sm-9">
					<label class="radio-inline">
						<input type="radio" id="u_tel_on" name="u_tel_on" value="Y">ON
					</label>
					<label class="radio-inline">
						<input type="radio" id="u_tel_on" name="u_tel_on" value="N">OFF
					</label>
				</div>
			</div>
			
			<div class="form-group col-sm-12">
				<label class="col-sm-3">email 알람 여부 : </label>
				<div class="col-sm-9">
					<label class="radio-inline">
						<input type="radio" id="u_mail_on" name="u_mail_on" value="Y">ON
					</label>
					<label class="radio-inline">
						<input type="radio" id="u_mail_on" name="u_mail_on" value="N">OFF
					</label>
				</div>
			</div>			

			<div class="form-group col-sm-12">
				<label class="col-sm-3">자동 보고 빈도 : </label>
				<div class="col-sm-8">
					<select class="form-control" id="u_report" name="u_report" disabled="disabled">
						<option value="X">사용하지 않음 (개발중)</option>
						<option value="M">월간보고 받음 (개발중)</option>
						<option value="W">주간보고 받음 (개발중)</option>
						<option value="D">일간보고 받음 (개발중)</option>
					</select>
				</div>
				<div class="col-sm-1"></div>
			</div>
			
			<!-- 수정, 확인 버튼 -->
			<div class="col-sm-12"><br></div>
			<div class="col-sm-12" align="center"  style="padding-bottom: 30px">
				<button type="button" class="btn btn-default" onclick="modifyInfo();">수정하기</button>
	      <button type="reset" class="btn btn-default">초기화</button>		
			</div>
			
		</div> <!-- well -->
		</form>	
	</div> <!-- /.container -->

    <!-- Metis Menu Plugin JavaScript -->
    <script src="../resources/bootstrap/sb-admin-2/vendor/metisMenu/metisMenu.min.js"></script>
    <!-- Custom Theme JavaScript -->
    <script src="../resources/bootstrap/sb-admin-2/dist/js/sb-admin-2.js"></script>

</body>
</html>