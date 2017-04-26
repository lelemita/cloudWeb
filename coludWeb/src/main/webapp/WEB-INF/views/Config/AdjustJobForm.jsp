<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>하솜 정보기술 : 잡 ${WORKTYPE} 페이지</title>
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
<link href="../resources/css/common.css?ver=10" rel="stylesheet" type="text/css">

<script>
$(document).ready(function(){
	//복제는 원본이름 뒤에 복제라고 붙임
	if("${WORKTYPE}"=="복제") {
		$("#j_name").val( $("#j_name").val()+"_복제" );
	}
	
	// 수신대상 체크박스 선택 
	var j_target = "${DATA.j_target}";
	if(j_target.indexOf("U")>=0) {
		$("#target_U").attr("checked",true);
	}
	if(j_target.indexOf("M")>=0) {
		$("#target_M").attr("checked",true);
	}
	if(j_target.indexOf("A")>=0) {
		$("#target_A").attr("checked",true);
	}

	// 발송방법 선택
	$("#j_class").val("${DATA.j_class}").prop("selected",true);	
	
});



//잡 수정,복제, 신규등록하기
function adjustJobProc( workType ){
	var msgArea = document.getElementById("msg");
	msgArea.innerHTML = "";
	
	//선택된 j_class 확인
	var select = document.getElementById("j_class");
	var j_class = select.options[select.selectedIndex].value;
	
	// 무결성 검사
	if( $("#j_name").val().length == 0 ) {
		msgArea.innerHTML = "Job 이름을 입력해주세요."; return;
	}
	if( $("#j_text").val().length == 0 ) {
		msgArea.innerHTML = "내용을 입력해주세요."; return;
	}else if( $("#j_text").val().length > 500 ){
		msgArea.innerHTML = "내용이 너무 깁니다."; return;
	}else if( j_class=='SMS' && $("#j_text").val().length > 100 ){
		alert("내용이 너무 긴 경우 문자에 전부 표시 되지 않을 수 있습니다.")
	}
	
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
				<h2 align="center">Job ${WORKTYPE}</h2>
				<hr style="border-width:3px; padding-bottom: 10px;">
			</div>
		
			<form method="post" id="sfrm">
				<input type="hidden" name="nowGroup"	value="${NOWGROUP}">
				<input type="hidden" name="g_no" value="${G_NO}">
				<input type="hidden" name="workType" value="${WORKTYPE}">
				<input type="hidden" name="j_no" value="${DATA.j_no}">
			<!-- 잡 이름 -->
			<div class="form-group col-sm-12 col-sm-offset-1">
				<div class="col-sm-2" style="font-weight: bold;">Job 이름</div>
				<div class="col-sm-5">
					<input type="text" class="form-control" id="j_name" name="j_name" value="${DATA.j_name}" maxlength="30" placeholder="Job 이름"/>
				</div>
			</div>

			<!-- 전송방법 -->
			<div class="form-group col-sm-12 col-sm-offset-1">
				<div class="col-sm-2" style="font-weight: bold;">전송방법</div>
				<div class="col-sm-5">
					<select class="form-control text-center" name="j_class" id="j_class" style="text-align: center;">
						<c:forEach items="${JOBTYPES}" var="type">
							<option value="${type}"> ${type}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<!-- 수신대상 -->
			<div class="form-group col-sm-12 col-sm-offset-1">
				<div class="col-sm-2" style="font-weight: bold;">수신대상</div>
				<div class="col-sm-9">
					<input type="checkbox" name="targets" id='target_U' value='U' > 일반 &nbsp;&nbsp;&nbsp;  
					<input type="checkbox" name="targets" id='target_M' value='M'> 관리자 &nbsp;&nbsp;&nbsp; 
					<input type="checkbox" name="targets" id='target_A' value='A'> 담당자 &nbsp;&nbsp;&nbsp; 
				</div>
			</div>			
			
			<!-- 발송 내용 -->
			<div class="form-group col-sm-12 col-sm-offset-1">
				<div class="col-sm-2" style="font-weight: bold;">내용</div>
				<div class="col-sm-8">
					<textarea id="j_text" name="j_text" class="form-control" rows="5" placeholder="발송할 문구를 입럭하세요.">${DATA.j_textLF}</textarea>
				</div>
			</div>				
			
			<!-- 안내 문구 -->
			<div class="form-group col-sm-12 col-sm-offset-1">
				<div class="col-sm-2" style="font-weight: bold;">※ 안내</div>
				<div class="col-sm-10" style="font-weight: bold; color:red; padding-bottom: 10px">
					다음 단어는 해당 의미의 값으로 치환됩니다.
				</div>
				<div class="col-sm-4 col-sm-offset-2" style="line-height: 1.6em;">
					#GROUP &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : 그룹 이름<br>
					#LOCATION	: 센서위치(이름)<br>
					#FACTOR &nbsp;&nbsp;&nbsp; : 측정요소<br>
				</div>
				<div class="col-sm-4" style="line-height: 1.6em;">
					#VALUE &nbsp;&nbsp;&nbsp;&nbsp; : 현재 측정 값<br>
					#COUNT &nbsp;&nbsp; : 재알림 회수<br>
				</div>				
			</div>
		</form>
		
		<!-- 각종 버튼 -->
		<div class="col-sm-12"></div>
		<div class="col-sm-12" align="center"  >
			<button type="button" class="btn btn-primary"	onclick="adjustJobProc('${WORKTYPE}')">
				<c:if test="${WORKTYPE eq '수정'}">수정</c:if>
				<c:if test="${WORKTYPE ne '수정'}">신규등록</c:if>			
			</button>			
			<!-- 취소버튼 → 부모창 새로고치고, 현재창 닫기 -->
			<button type="button" class="btn btn-primary"	onclick="opener.parent.location.reload();window.close();">취소</button>
		</div>			
		
		<!-- 에러 메시지 출력 공간 -->
		<div class="col-sm-12 text-center" id="msg" style="padding: 20px; color: red; font-weight: bold;"></div>
			
			
		</article>
	</section>

</body>
</html>