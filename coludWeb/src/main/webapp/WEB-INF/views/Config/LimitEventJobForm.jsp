<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>임계값 일탈 이벤트 대응방법 설정</title>
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
	//선택 폼 초기 값은 첫번째 센서의 현황으로 세팅
	var	jobs = new Array();
	jobs[0] = document.getElementsByName("highjob");
	jobs[1] = document.getElementsByName("highlowjob");
	jobs[2] = document.getElementsByName("lowhighjob");
	jobs[3] = document.getElementsByName("lowjob");
	// 이것도 반복문 쓰고 싶은데..ㅠㅠ
	<c:forEach items="${NOWJOBS.high}" var="job" varStatus="st">
		$(jobs[0][${st.index}]).val("${job.j_no}").prop("selected", true);
	</c:forEach>
	<c:forEach items="${NOWJOBS.highlow}" var="job" varStatus="st">
		$(jobs[1][${st.index}]).val("${job.j_no}").prop("selected", true);
	</c:forEach>
	<c:forEach items="${NOWJOBS.lowhigh}" var="job" varStatus="st">
		$(jobs[2][${st.index}]).val("${job.j_no}").prop("selected", true);
	</c:forEach>
	<c:forEach items="${NOWJOBS.low}" var="job" varStatus="st">
		$(jobs[3][${st.index}]).val("${job.j_no}").prop("selected", true);
	</c:forEach>
	
});

// 변경적용 버튼 이벤트
function adjust() {
	//비밀번호 검사
	var pw = prompt("비밀번호를 입력해주세요");	
	if(pw==null || pw.length==0 || !checkPassword(pw)){
		return;
	}
	//요청
	$("#sfrm").attr("action" , "../Manager/LimitEventJobProc.hs");
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


</script>

</head>
<body>

	<!-- 외부 js에서 접근을 못해서.. -->
	<input type="hidden" value="${ID}" id="myid">

	<!-- 본문 -->
	<section class="container" >
	<div class="col-sm-12" style="height:50px"><!-- 상단여백 --></div>
	
		<!-- 메인공간 -->
		<article class="well col-sm-10 col-sm-offset-1">
			<!-- 제목 -->
			<div class="col-sm-12">
				<h2 align="center">일탈 이벤트 대응방법 수정: ${G_NAME}</h2>
				<hr style="border-width:3px; padding-bottom: 10px;">
			</div>
		
			<!-- 기본 정보 -->
			<div class="col-sm-12" style="padding-bottom: 20px;">
				<label class="col-sm-10 col-sm-offset-1">선택한 요소 : 
					<c:if test="${NOWFACTOR eq 'TMP'}">온도</c:if>
					<c:if test="${NOWFACTOR eq 'HUM'}">습도</c:if>
				</label>
				<label class="col-sm-10 col-sm-offset-1">선택한 센서 : ${S_DISPLAYS}</label>
			</div>
		
			<!-- 선택 폼 : 초기 값은 첫번째 센서의 현황 -->
			<div class="col-sm-10 col-sm-offset-1">
				<!-- 전송용 폼 -->
				<form method="post" id="sfrm">
					<input type="hidden" name="l_nos" value="${L_NOS}">

					<table class="table table-bordered table-striped text-center">
						<!-- 이거 좀 자동으로 하고 싶은데 -ㅅ-;; -->
						<!-- 상한일탈 -->
						<tr>
							<td style="width:20%; vertical-align: middle;">상한일탈</td>
							<td style="text-align: left; padding-left: 30px"">
								<c:forEach items="${NOWJOBS.high}" var="job" varStatus="st">
									<div class="col-sm-4">
										<select class="form-control" name="highjob">
											<option value="-1">${st.count}번 사용안함</option>
											<c:forEach items="${JOBLIST}" var="j">
												<option value="${j.j_no}">${j.j_name}</option>
											</c:forEach>
										</select>		
									</div>
								</c:forEach>
								<!-- 추가 -->
								<div class="col-sm-4">
									<select class="form-control" name="highjob">
										<option value="-1">-${NOWJOBS.high.size()+1}번째 잡 추가-</option>
										<c:forEach items="${JOBLIST}" var="j">
											<option value="${j.j_no}">${j.j_name}</option>
										</c:forEach>
									</select>		
								</div>
							</td>
						</tr>
						<!-- 상한복구 -->
						<tr>
							<td style="width:20%; vertical-align: middle;">상한복구</td>
							<td style="text-align: left; padding-left: 30px"">
								<c:forEach items="${NOWJOBS.highlow}" var="job" varStatus="st">
									<div class="col-sm-4">
										<select class="form-control" name="highlowjob">
											<option value="-1">${st.count}번 사용안함</option>
											<c:forEach items="${JOBLIST}" var="j">
												<option value="${j.j_no}">${j.j_name}</option>
											</c:forEach>
										</select>		
									</div>
								</c:forEach>
								<!-- 추가 -->
								<div class="col-sm-4">
									<select class="form-control" name="highlowjob">
										<option value="-1">-${NOWJOBS.highlow.size()+1}번째 잡 추가-</option>
										<c:forEach items="${JOBLIST}" var="j">
											<option value="${j.j_no}">${j.j_name}</option>
										</c:forEach>
									</select>		
								</div>
							</td>
						</tr>
						<!-- 하한복구 -->
						<tr>
							<td style="width:20%; vertical-align: middle;">하한복구</td>
							<td style="text-align: left; padding-left: 30px"">
								<c:forEach items="${NOWJOBS.lowhigh}" var="job" varStatus="st">
									<div class="col-sm-4">
										<select class="form-control" name="lowhighjob">
											<option value="-1">${st.count}번 사용안함</option>
											<c:forEach items="${JOBLIST}" var="j">
												<option value="${j.j_no}">${j.j_name}</option>
											</c:forEach>
										</select>		
									</div>
								</c:forEach>
								<!-- 추가 -->
								<div class="col-sm-4">
									<select class="form-control" name="lowhighjob">
										<option value="-1">-${NOWJOBS.lowhigh.size()+1}번째 잡 추가-</option>
										<c:forEach items="${JOBLIST}" var="j">
											<option value="${j.j_no}">${j.j_name}</option>
										</c:forEach>
									</select>		
								</div>
							</td>
						</tr>
						<!-- 하한일탈 -->
						<tr>
							<td style="width:20%; vertical-align: middle;">하한일탈</td>
							<td style="text-align: left; padding-left: 30px"">
								<c:forEach items="${NOWJOBS.low}" var="job" varStatus="st">
									<div class="col-sm-4">
										<select class="form-control" name="lowjob">
											<option value="-1">${st.count}번 사용안함</option>
											<c:forEach items="${JOBLIST}" var="j">
												<option value="${j.j_no}">${j.j_name}</option>
											</c:forEach>
										</select>		
									</div>
								</c:forEach>
								<!-- 추가 -->
								<div class="col-sm-4">
									<select class="form-control" name="lowjob">
										<option value="-1">-${NOWJOBS.low.size()+1}번째 잡 추가-</option>
										<c:forEach items="${JOBLIST}" var="j">
											<option value="${j.j_no}">${j.j_name}</option>
										</c:forEach>
									</select>		
								</div>
							</td>
						</tr>					
						
					</table>	
				</form>
				
						
			</div>

			<!-- 각종 버튼 -->
			<div class="col-sm-12" align="center"  >
				<button type="button" class="btn btn-danger" onclick="adjust();">변경 적용</button>		
				<!-- 취소버튼 → 부모창 새로고치고, 현재창 닫기 -->
				<button type="button" class="btn btn-primary"	onclick="opener.parent.location.reload();window.close();">취소</button>
			</div>		

		</article>
	</section>

</body>
</html>