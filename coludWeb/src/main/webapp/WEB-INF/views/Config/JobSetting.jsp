<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>하솜 정보기술 : 대응방법 등록</title>
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
//어딘가로 보내는 함수 - form은 헤더에 있다
function move(url){
	$("#gogo").attr("action" , url)
	$("#gogo").submit();	
}
// 탭 이동 함수
function moveTab(url , targetGroup){
	$("#targetGroup").val(targetGroup);
	$("#gogo").attr("action" , url);
	$("#gogo").submit();
}	
// 목록가기 이벤트
function submitForm(url) {
	$("#sfrm").attr("action" , url);
	$("#sfrm").submit();
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


// 잡 삭제하기
function deleteJob(){
	var msgArea = document.getElementById("msg");
	msgArea.innerHTML = "";
	// 잡이 선택되어 있는지 검사
	var chooseTr = document.getElementsByClassName("choosed");
	if(chooseTr.length!=1){
		msgArea.innerHTML="삭제할 Job을 1개 선택해주세요.";
		return;
	}
	// 표준 잡은 삭제 불가능
	var nos = chooseTr[0].id.split("-"); // 양식 : g_no-j_no
	if(nos[0].length==0 || nos[0]=="0") {
		msgArea.innerHTML="표준 잡은 삭제할 수 없습니다.";
		return;
	}
	//담당자 비밀번호 검사 (Ajax)
	var pw = prompt("비밀번호를 입력해주세요");	
	if(pw.length==0 || !checkPassword(pw)){
		return;
	}
	// 폼에 j_no 저장
	$("#j_no").val(nos[1]);
	// 요청
	submitForm("../Manager/DeleteJob.hs");
}


//잡 수정,복제 신규등록하기
function adjustJob( workType ){
	var msgArea = document.getElementById("msg");
	msgArea.innerHTML = "";
	
	if(workType!='신규등록'){
		//수정,복제할 잡이 선택되어 있는지 검사
		var chooseTr = document.getElementsByClassName("choosed");
		if(chooseTr.length!=1){
			msgArea.innerHTML= workType + "할 Job을 1개 선택해주세요.";
			return;
		}
		
		var nos = chooseTr[0].id.split("-"); // 양식 : g_no-j_no
		// 표준 잡은 수정 불가능
		if(workType=='수정'){
			if(nos[0].length==0 || nos[0]=="0") {
				msgArea.innerHTML="표준 잡은 수정할 수 없습니다.";
				return;
			}
		}
		// 폼에 j_no 저장
		$("#j_no").val(nos[1]);
	
	}//if 신규등록외
	
	//담당자 비밀번호 검사 (Ajax)
	var pw = prompt("비밀번호를 입력해주세요");	
	if(pw.length==0 || !checkPassword(pw)){
		return;
	}
	
	// 요청
	$("#workType").val(workType);
	$("#sfrm").attr("target", "_blank");
	submitForm("../Manager/AdjustJobForm.hs");
	$("#sfrm").attr("target", "_self");
}




$(document).ready(function(){
	
	// 행 선택하기
	$("tr").click(function(){
		var trs = document.getElementsByTagName("tr");
		for(i=0;i<trs.length;i++){
			if(trs[i]==this){
				$(this).toggleClass("choosed");				
			}else {
				$(trs[i]).removeClass("choosed");				
			}
		}//for
	});
	
});
</script>

</head>


<body>
	<header>
		<%@ include file= "../Common/Header.jsp" %>
	</header>

	<!-- 전송용 폼 -->
	<form method="post" id="sfrm">
		<!-- hidden 요소 들 -->
		<input type="hidden" name="nowGroup"	value="${NOWGROUP}">
		<input type="hidden" name="g_no" value="${G_NO}">
		<input type="hidden" name="j_no" id="j_no" value="-1">
		<input type="hidden" name="workType" id="workType">
	</form>
	<!-- 외부 js에서 접근을 못해서.. -->
	<input type="hidden" value="${ID}" id="myid">
	<input type="hidden" value="${DATA.popMsg}" id="pop">	

	<!-- 본문 -->
	<section class="container" >
		<!-- Nav tabs -->
		<ul class="nav nav-tabs nav-pills nav-justified">
			<!-- 해당 사용자가 담당하는 그룹 선택 탭  -->
			<c:forEach items="${G_NAMES}" var="name" varStatus="st">
				<c:if test="${st.index eq NOWGROUP}">
					<li class="active"><a href="#">${name}</a></li>
				</c:if>
				<c:if test="${st.index ne NOWGROUP}">
					<li><a href="#" onclick="moveTab('../Manager/JobSetting.hs' , ${st.index})">${name}</a></li> 
				</c:if>
			</c:forEach>
		</ul>
		
		<!-- Tab panes -->
		<article class="tab-content well col-sm-12">
		
		<!-- 잡 현황 -->
		<c:if test="${JOBLIST ne null}">
		<div class="col-sm-12">
			<table  class="table table-hover text-center table-bordered table-striped">
			<tr>
				<th class="text-center">번호</th>
				<th class="text-center">잡 이름</th>
				<th class="text-center">발송수단</th>
				<th class="text-center">수신대상</th>
				<th class="text-center">발송내용</th>
			</tr>
			<c:forEach items="${JOBLIST}" var="job" varStatus="st">
			<tr height="45" valign="middle" id="${job.g_no}-${job.j_no}">
				<td>${st.count}</td>
				<td>${job.j_name}</td>
				<td>${job.strJ_class}</td>
				<td>${job.strTarget}</td>
				<td style="text-align:left; padding-left:30px;">${job.j_text}</td>
			</tr>	
			</c:forEach>
			
			</table>
		</div><!-- 테이블영역 -->
		
		<!-- 수정, 확인 버튼 -->
		<div class="col-sm-12"></div>
		<div class="col-sm-12" align="center"  >
			<button type="button" class="btn btn-primary"	onclick="adjustJob('수정');">수정</button>
			<button type="button" class="btn btn-danger"	onclick="deleteJob();">삭제</button>
			<button type="button" class="btn btn-success"	onclick="adjustJob('복제');">복제</button>
			<button type="button" class="btn btn-success"	onclick="adjustJob('신규등록');">신규</button>
		</div>
			
		<!-- 에러 메시지 출력 공간 -->
		<div class="col-sm-12 text-center" id="msg" style="padding: 20px; color: red; font-weight: bold;"></div>
		
		</c:if><!-- 잡 현황 well -->				
		
		</article><!-- Tab panes -->
		
	</section><!-- container -->

</body>
</html>