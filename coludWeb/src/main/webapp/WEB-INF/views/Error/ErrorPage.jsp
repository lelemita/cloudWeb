<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html style="height:100%">
<head>
<meta charset=UTF-8>
<title>에러가 발생했습니다.</title>
<!-- 부트스트랩 기본 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!-- 자바스크립트 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>

</script>
</head>

<body style="height:100%">


	<div style="background-color:black; color:#f1f1f1; height:20%">
		<div style="height:30%; padding-left:30%;"></div>
		<div class="text-left" style="padding-left:25%;">
			<h1><c:out value="${msg}"/></h1>
		</div>
	</div>
	<div class="jumbotron text-left" style="height:60%; padding-left:25%; margin:0">
		<h2>에러가 발생했습니다..</h2>
		<h3 style="line-height:150%;">
			nam@hasom.com 으로<br>
			이 에러가 발생한 상황을 알려주시면,<br>
			조치하도록 하겠습니다.<br></h3>
		<br><br>
		<input type="button" class="btn btn-lg btn-warning" onclick="location.href='http://210.180.224.10:8080/cloud/'" value="홈으로 갑니다">
	</div>

	<div style="background-color:black; color:#f1f1f1; height:20%"></div>

</body>
</html>