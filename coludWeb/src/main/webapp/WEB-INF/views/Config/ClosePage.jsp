<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>하솜 정보기술</title>
</head>

<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script>
$(document).ready(function(){
	
	if("${MSG}".length >0){
		alert("${MSG}");
	}
	
	window.opener.parent.location.reload();
	window.close();
	
});
</script>

<body>


</body>
</html>