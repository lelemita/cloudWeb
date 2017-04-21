<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>통신 장애 이벤트 대응방법 설정</title>
<!-- jQuery -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js" type="text/javascript"></script>
<!-- Bootstrap Core JavaScript -->
<script src="../resources/bootstrap/sb-admin-2/vendor/bootstrap/js/bootstrap.min.js"></script>
<!-- Bootstrap Core CSS -->
<link href="../resources/bootstrap/sb-admin-2/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- 직접 작성 -->
<link href="../resources/css/common.css?ver=10" rel="stylesheet" type="text/css">
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
	
	$(document).ready(function(){
		// nowFactor 반영
		$(":radio[name=nowFactor][value=${NOWFACTOR}]").attr("checked",true);		

		// 요소 선택
		$('input[name=nowFactor]').change(function() {
			$("#sfrm").attr("action" , '../Manager/EventJobForm.hs');		
			$("#sfrm").submit();
		});//change()
	});
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
			<input type="hidden" name="nowGroup"	value="${NOWGROUP}">
			<input type="hidden" name="fileName" id="fileName">
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
			<div class="col-sm-12">
				<p style="font-weight: bold">1. 통신장애 발생시 대응방법</p>
			</div>
			<div class="col-sm-12">
				<table  class="table table-hover text-center table-bordered table-striped"
								style="margin-left:3%; margin-right:100em;">
					<tr>
						<th class="text-center">순번</th>
						<th class="text-center">수단</th>
						<th class="text-center">대응와ㅐ아ㅓ마ㅣㅓㅇㄹ방법</th>
						<th class="text-center">수신대상</th>
					</tr>
					<c:forEach items="${DISCON}" var="job" varStatus="st">
					<tr>
						<td>${st.count}</td>
						<td>${job.j_class}</td>
						<td>${job.j_name}</td>
						<td>${job.strTarget}</td>
					</tr>
					</c:forEach>
				</table>
			</div>
		</article>
	</section>		

</body>
</html>