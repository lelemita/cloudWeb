<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>하솜 정보기술 : 센서 설정</title>
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
<link href="../resources/css/common.css?ver=11" rel="stylesheet" type="text/css">
<script src="../resources/js/config/sensorSetting.js?ver=8"></script>

<script>
$(document).ready(function(){
	// 1) 선택된 요소 표시
	if('${NOWFACTOR}'.length !== 0){
		$(":radio[name=nowFactor][value=${NOWFACTOR}]").attr("checked",true);		
	}	
	// 요소 선택
	$('input[name=nowFactor]').change(function() {
		$("#sfrm").attr("action" , '../Manager/SensorSetting.hs');		
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
	
	<div class="container" >
		<!-- Nav tabs -->
		<ul class="nav nav-tabs nav-pills nav-justified">
			<!-- 해당 사용자가 담당하는 그룹 선택 탭  -->
			<c:forEach items="${G_NAMES}" var="name" varStatus="st">
				<c:if test="${st.index eq NOWGROUP}">
					<li class="active"><a href="#">${name}</a></li>
				</c:if>
				<c:if test="${st.index ne NOWGROUP}">
					<li><a href="#" onclick="moveTab('../Manager/SensorSetting.hs' , ${st.index})">${name}</a></li> 
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
				<c:forEach items="${F_NAMES}" var="f_info" varStatus="st">
					<label class="checkbox-inline"> <!-- radio-inline 보다 이게 낫네;; -->			
							<input type="radio" name="nowFactor" value="${f_info.f_table_name}">${f_info.f_name}
					</label>
				</c:forEach>
			</div><!-- 요소 선택 well -->		
			
			<!-- 보낼 변경 내용 저장할 영역 -->
			<div id='box'></div>
		</form>		

		<!-- 센서 현황 -->
		<c:if test="${SENSORINFOS ne null}">
		<div class="col-sm-12">
			<table  class="table table-hover text-center">
			<tr>
				<th class="text-center">번호</th>
				<th class="text-center">센서 위치</th>
				<th class="text-center">상한</th>
				<th class="text-center">상한복구</th>
				<th class="text-center">하한</th>
				<th class="text-center">하한복구</th>
				<th class="text-center">감지지연(초)</th>
				<th class="text-center">재알람(초)</th>
				<th class="text-center">보정값</th>
			</tr>
			<c:forEach items="${SENSORINFOS}" var="sensor" varStatus="st">
			<tr height="45" valign="middle">
				<td>${st.count}</td>
				<td><input type="text" class="formInTable" id='${sensor.l_no}-s_display' value='${sensor.s_display}' style="width:200px;"></td>
				<td><input type="text" class="formInTable" id='${sensor.l_no}-l_high' value='${sensor.l_high}'></td>
				<td><input type="text" class="formInTable" id='${sensor.l_no}-l_highlow' value='${sensor.l_highlow}'></td>
				<td><input type="text" class="formInTable" id='${sensor.l_no}-l_low' value='${sensor.l_low}'></td>
				<td><input type="text" class="formInTable" id='${sensor.l_no}-l_lowhigh' value='${sensor.l_lowhigh}'></td>
				<td><input type="text" class="formInTable" id='${sensor.l_no}-l_delay' value='${sensor.l_delay}'></td>
				<td><input type="text" class="formInTable" id='${sensor.l_no}-l_re_alarm' value='${sensor.l_re_alarm}'></td>
				<td><input type="text" class="formInTable" id='${sensor.l_no}-l_adjust' value='${sensor.l_adjust}'></td>
			</tr>	
			</c:forEach>
			
			</table>
		</div><!-- 테이블영역 -->
		
		<!-- 수정, 확인 버튼 -->
		<div class="col-sm-12"></div>
		<div class="col-sm-12" align="center"  >
	     <button type="button" class="btn btn-primary" onclick="location.reload();">원래대로</button>		
	     <button type="button" class="btn btn-primary" onclick="location.href='../Manager/SensorSetting.hs'">취소</button>
			<button type="button" id="saveBtn" class="btn btn-danger" onclick="saveValues();" disabled="disabled">저장하기</button>
		</div>
			
		<!-- 에러 메시지 출력 공간 -->
		<div class="col-sm-12 text-center" id="msg" style="padding: 20px; color: red; font-weight: bold;"></div>
		
		</c:if><!-- 센서 현황 well -->		
				
		</div> <!-- /.tab-content -->
		
	</div>
</body>
</html>