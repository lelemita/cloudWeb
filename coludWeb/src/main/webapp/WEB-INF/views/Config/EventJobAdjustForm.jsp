<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>하솜 정보기술 : 대응방법 수정</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
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
<link href="../resources/css/common.css?ver=0.1" rel="stylesheet" type="text/css">

<script>
$(document).ready(function(){
	//초기값 설정
	$("#j_class").val("${DATA.j_class}").prop("selected" , true);
	$("#j_name").val("${DATA.j_no}").prop("selected" , true);
	
	//j_class 변경 → 가능한 j_name 목록 변경
	$("#j_class").change(function(){
		var j_class = $("#j_class").val();
		var result = "<option value='-1'>-선택해주세요-</option>";
		<c:forEach items="${JOBLIST}" var="job" >
			if('${job.j_class}'==j_class){
				result += "<option value='${job.j_no}'>${job.j_name}</option>";				
			}
		</c:forEach>		
		document.getElementById("j_name").innerHTML=result;
		document.getElementById("j_text").innerHTML="";
		document.getElementById("j_target").innerHTML="";
	});
	
	//j_name 변경 → j_text, j_target 불러오기
	$("#j_name").change(function(){
		var j_no = $("#j_name").val();
		<c:forEach items="${JOBLIST}" var="job" >
			if('${job.j_no}'==j_no){				
				document.getElementById("j_text").innerHTML='${job.j_text}';
				document.getElementById("j_target").innerHTML='${job.strTarget}';
			}
		</c:forEach>		
	});	
	
});

// 추가, 수정, 삭제 버튼 이벤트
function adjust(workType){
	//무결성검사
	if(workType!='삭제' && $("#j_name").val()==-1){
		alert( workType + "할 Job을 선택해주세요.");
		return;
	}
	
	$("#workType").val(workType);
	//담당자 비밀번호 검사 (Ajax)
	var pw = prompt("비밀번호를 입력해주세요");	
	if(pw==null || pw.length==0 || !checkPassword(pw)){
		return;
	}
	
	// 요청
	$("#sfrm").attr("action" , "../Manager/EventJobAdjustProc.hs");
	$("#sfrm").submit();
}	
	
	
	
	
	
	
	



//잡 수정,복제, 신규등록하기
function adjustJobProc( workType ){
	//선택된 j_class 확인
	var select = document.getElementById("j_class");
	var j_class = select.options[select.selectedIndex].value;
	
	// 무결성 검사
	
	//담당자 비밀번호 검사 (Ajax)
	var pw = prompt("비밀번호를 입력해주세요");	
	if(pw.length==0 || !checkPassword(pw)){
		return;
	}
	
	// 요청
	window.opener.name = "parentPage";
	$("#sfrm").attr("target" , "parentPage");
	$("#sfrm").attr("action" , "../Manager/AdjustJobProc.hs");
	$("#sfrm").submit();
	window.close();
}

//비밀번호 검사
function checkPassword(pw) {
	var result = false;
	var request = $.ajax({
		url		:	"../Config/PasswordChk.hs", 
		async : false,	// deprecated.. 대안은?
		type	:	"POST",
		data	: {'id': $('#myid').val() , 'pw': pw , 'temp': new Date() },
		dataType	:	"text",
		success	: function(data) {
			var u_type = data.trim();
			if (u_type == "A" || u_type == 'M'){
				result = true;
			}
			else {
				alert("비밀번호가 틀리거나, 담당자가 아닙니다.");
			}
		}, 
		error	:	 function(request,status,error){
	    alert("비밀번호 확인 과정에서 에러\n" +"code:"+request.status+"\n"
	    		    +"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
	return result;
}	


</script>

</head>
<body>
	<!-- 외부 js에서 접근을 못해서.. -->
	<input type="hidden" value="${ID}" id="myid">

	<section class="container" >
	<div class="col-sm-12" style="height:50px"><!-- 여백 --></div>
	
		<article class="well col-sm-10 col-sm-offset-1">
			<div class="col-sm-12">
				<h2 align="center">
					<c:if test="${DATA.j_no eq -1}">대응방법 추가</c:if>
					<c:if test="${DATA.j_no ne -1}">대응방법 수정</c:if>
				</h2>
				<hr style="border-width:3px; padding-bottom: 10px;">
			</div>
		
			<form method="post" id="sfrm">
				<input type="hidden" name="g_no" value="${DATA.g_no}">
				<input type="hidden" name="e_no" value="${DATA.e_no}">
				<input type="hidden" name="j_no" value="${DATA.j_no}">
				<input type="hidden" name="order" value="${DATA.order}">
				<input type="hidden" id="workType" name="workType">

				
			<!-- 좌측 -->
			<div class="col-sm-6 form-group text-center">
				<!-- 기본 정보 -->
				<label class="col-sm-5">그룹 : </label>
				<label class="col-sm-7">${DATA.g_name}</label>
				<label class="col-sm-5">에러 종류 : </label>
				<label class="col-sm-7">${DATA.strE_no}</label>
				<label class="col-sm-5">순번 : </label>
				<label class="col-sm-7">${DATA.order + 1}</label>
				
				<!-- 수단목록 -->
				<label class="col-sm-5">수단 : </label>
				<div class="col-sm-7">
					<select class="form-control" id="j_class" style="width:200px; margin:auto;">
						<option>-선택해주세요-</option>
						<c:forEach items="${JOBTYPES}" var="j_class" >
							<option value="${j_class}">${j_class}</option>
						</c:forEach>
					</select>
				</div>
				
				<!-- 잡목록 -->
				<label class="col-sm-5">Job : </label>
				<div class="col-sm-7">
					<select class="form-control" name="newj_no" id="j_name" style="width:200px; margin:auto;">
						<option value='-1'>-선택해주세요-</option>
						<c:forEach items="${JOBLIST}" var="job" >
							<!-- j_class 일치하는 것만 보여줌 -->
							<c:if test="${DATA.j_class eq job.j_class}">
								<option value="${job.j_no}">${job.j_name}</option>
							</c:if>
						</c:forEach>
					</select>
				</div>
								
			</div><!-- 좌측 -->
		</form>
			
		<!-- 우측 -->
		<div class="col-sm-6 form-group">
			<label class="com-sm-12">내용</label>
			<div id="j_text" class="com-sm-12">${DATA.j_text}</div>
			<br>
			
			<label class="com-sm-12">수신 대상</label>
			<div id="j_target" class="com-sm-12">${DATA.strTarget}</div>	
		</div><!-- 우측 -->
		
		<div class="col-lg-12"> <!-- 여백 --> <br></div>
		
		<!-- 각종 버튼 -->
		<div class="col-sm-12" align="center"  >
			<c:if test="${DATA.j_no eq -1}">
				<button type="button" class="btn btn-danger" onclick="adjust('추가');">추가</button>	
			</c:if>
			<c:if test="${DATA.j_no ne -1}">	
				<button type="button" class="btn btn-danger" onclick="adjust('삭제');">사용안함</button>		
				<button type="button" class="btn btn-danger" onclick="adjust('변경');">변경</button>		
			</c:if>
			<!-- 취소버튼 → 부모창 새로고치고, 현재창 닫기 -->
			<button type="button" class="btn btn-primary"	onclick="opener.parent.location.reload();window.close();">취소</button>
		</div>			
					
		</article>
	</section>

</body>
</html>