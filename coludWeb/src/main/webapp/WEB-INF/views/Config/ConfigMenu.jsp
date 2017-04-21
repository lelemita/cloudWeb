<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>
	<c:if test="${sessionScope.TYPE eq 'M'}">관리자 메뉴</c:if>
	<c:if test="${sessionScope.TYPE eq 'A'}">담당자 메뉴</c:if>
</title>
<!-- jQuery -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js" type="text/javascript"></script>
<!-- Bootstrap Core JavaScript -->
<script src="../resources/bootstrap/sb-admin-2/vendor/bootstrap/js/bootstrap.min.js"></script>
<!-- Bootstrap Core CSS -->
<link href="../resources/bootstrap/sb-admin-2/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- 직접 작성 -->
<script src="../resources/js/config/sensorSetting.js?ver=1"></script>

</head>
<body>
	<header>
		<%@ include file= "../Common/Header.jsp" %>
	</header>
	
	<!-- 좌측 세로 공간 -->
	<div class="col-sm-2 sidenav" >
	</div>
	<div class="col-sm-1"></div>
		
	<!-- 중앙공간 -->
	<article class="col-sm-6 text-left">
		<div class="col-sm-12" style="height: 100px;" ><!-- 여백 --></div>
		<div class="col-sm-6">
			<button type="button" class="btn btn-success btn-block" style="height: 200px;" onclick="location.href='../Manager/SensorSetting.hs'"><h2>센서<br>설정</h2></button>
		</div>
		<div class="col-sm-6">
			<c:if test="${sessionScope.TYPE eq 'A'}">
			<button type="button"class="btn btn-danger btn-block"  style="height: 200px;" onclick="location.href='../Admin/UserList.hs'"><h2>사용자<br>조회</h2></button>
			</c:if>
		</div>

		<div class="col-sm-12" style="height: 20px;" ><!-- 여백 --></div>
		<div class="col-sm-6">
			<button type="button"class="btn btn-warning btn-block"  style="height: 200px;" onclick="location.href='../Manager/EventJobForm.hs'"><h2>이벤트<br>대응Job 조회</h2></button>
		</div>
		<div class="col-sm-6">
			<button type="button" class="btn btn-info btn-block" style="height: 200px;" onclick="location.href='../Manager/JobSetting.hs'"><h2>대응 Job<br>목록</h2></button>
		</div>
	</article>

</body>
</html>