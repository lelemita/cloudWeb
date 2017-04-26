<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>이벤트 대응방법 설정</title>
<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<!-- Bootstrap Core JavaScript -->
<script src="../resources/bootstrap/sb-admin-2/vendor/bootstrap/js/bootstrap.min.js"></script>
<!-- Bootstrap Core CSS -->
<link href="../resources/bootstrap/sb-admin-2/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
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
	
//--- 특화--	
	
$(document).ready(function(){
		
	// nowFactor 반영
	$(":radio[name=nowFactor][value=${NOWFACTOR}]").attr("checked",true);		

	// 요소 선택
	$('input[name=nowFactor]').change(function() {
		$("#sfrm").attr("action" , '../Manager/EventJobForm.hs');		
		$("#sfrm").submit();
	});//change()
	
	// 행 선택하기
	$("table:first tr td:first-child").click(function(){
		$(this).toggleClass("choosed");
	});
	
		
});

// 전체 선택 버튼
function chooseAll() {
	var tds = document.getElementsByName("sel");
	for(var i=0 ; i<tds.length ; i++) {
		$(tds[i]).addClass("choosed");
	}
}
// 선택 수정 버튼
function modify() {
	var chooses = document.getElementsByClassName("choosed");
	//무결성 검사
	if(chooses.length <= 0){
		alert("센서를 선택해주세요.");return;
	}
	// 값 저장
	var result = "";
	var s_displays = "";
	for(var i=0 ; i<chooses.length ; i++) {
		result += chooses[i].id + ",";
		s_displays += $(chooses[i]).next().text() + " , ";
	}
	$("#l_nos").val(result);
	$("#s_displays").val(s_displays);
	// 전송
	$("#sfrm").attr("target" , '_blank');		
	$("#sfrm").attr("action" , '../Manager/LimitEventJobForm.hs');		
	$("#sfrm").submit();	
	$("#sfrm").attr("target" , '_self');		
}

</script>


</head>
<body>
	<header>
		<%@ include file= "../Common/Header.jsp" %>
	</header>

	<!-- 외부 js에서 접근을 못해서.. -->
	<input type="hidden" value="${ID}" id="myid">
	<input type="hidden" value="${DATA.popMsg}" id="pop">	
	
	<section class="container" >
		<!-- Nav tabs -->
		<ul class="nav nav-tabs nav-pills nav-justified">
			<!-- 해당 사용자가 담당하는 그룹 선택 탭  -->
			<c:forEach items="${G_NAMES}" var="name" varStatus="st">
				<c:if test="${st.index eq NOWGROUP}">
					<li class="active"><a href="#">${name}</a></li>
				</c:if>
				<c:if test="${st.index ne NOWGROUP}">
					<li><a href="#" onclick="moveTab('../Manager/EventJobForm.hs' , ${st.index})">${name}</a></li> 
				</c:if>
			</c:forEach>
		</ul>
		
		<!-- Tab panes -->
		<div class="tab-content well col-sm-12">
		<!-- 전송용 폼 -->
		<form method="post" id="sfrm">
			<!-- hidden 요소 들 -->
			<input type="hidden" name="l_nos" id="l_nos">
			<input type="hidden" name="s_displays" id="s_displays">
			<input type="hidden" name="g_no"	value="${G_NO}">
			<input type="hidden" name="nowGroup"	value="${NOWGROUP}">
			<input type="hidden" name="g_name"		value="${G_NAMES[NOWGROUP]}"> 		
			
			<!-- 요소 선택 well -->
			<div class="well col-sm-12">
				<div class="col-sm-1">
					<label>요소</label>
				</div>
					<label class="checkbox-inline">			
							<input type="radio" name="nowFactor" value="CON">통신
					</label>
				<c:forEach items="${F_NAMES}" var="f_info" varStatus="st">
					<label class="checkbox-inline"> <!-- radio-inline 보다 이게 낫네;; -->			
							<input type="radio" name="nowFactor" value="${f_info.f_table_name}">${f_info.f_name}
					</label>
				</c:forEach>
			</div><!-- 요소 선택 well -->		
		</form>
		</div><!-- Tab panes -->
		
	
		<article class="well col-sm-12">	
		
			<!-- 각종 버튼들 -->
			<div class="col-sm-12" align="center" style="margin-bottom:20px; ">
				<button class="btn btn-success" onclick="chooseAll();">전체 선택</button>
				<button class="btn btn-primary" onclick="modify();">선택 수정</button>
			</div>		
		
			<!-- 현황 테이블 -->
			<table  class="table table-hover table-bordered table-striped  text-center "
							style="margin:auto; width:95%;">
				<tr>
					<th class="text-center" style="width:70px;">선택</th>
					<th class="text-center" style="width:200px;">센서위치</th>
					<th class="text-center">대응방법</th>
				</tr>
				<tr>
				
				<c:forEach items="${MAPLIST}" var="map" varStatus="st">
				<tr>
					<td name="sel" id="${map.l_no}" style="font-weight: bold;">${st.count}</td>
					<td style="font-weight: bold;">${map.s_display}</td>
					<td>
						<table class="table table-bordered table-striped">
							<tr>
								<td style="width:20%;">상한일탈</td>
								<td style="text-align: left; padding-left: 30px"">
									<c:forEach items="${map.event_jobs.high}" var="job" varStatus="st2">
										${job.j_name}
										<c:if test="${not st2.last}"> - </c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td>상한복구</td>
								<td style="text-align: left; padding-left: 30px"">
									<c:forEach items="${map.event_jobs.highlow}" var="job" varStatus="st2">
										${job.j_name}
										<c:if test="${not st2.last}"> - </c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td>하한복구</td>
								<td style="text-align: left; padding-left: 30px"">
									<c:forEach items="${map.event_jobs.lowhigh}" var="job" varStatus="st2">
										${job.j_name}
										<c:if test="${not st2.last}"> - </c:if>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td>하한일탈</td>
								<td style="text-align: left; padding-left: 30px">
									<c:forEach items="${map.event_jobs.low}" var="job" varStatus="st2">
										${job.j_name}
										<c:if test="${not st2.last}"> - </c:if>
									</c:forEach>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				</c:forEach>
			</table>

		</article>		
		
	</section>		

</body>
</html>