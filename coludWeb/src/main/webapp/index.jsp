<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>하솜 정보기술</title>


</head>
<body>
<h1>인덱스 파일<br></h1>

<%
	// 바로 로그인 화면으로 redirect
	 response.sendRedirect("./Login/LoginForm.hs");
	// RequestDispatcher dispatcher = request.getRequestDispatcher("./Login/LoginForm.hs");
	// dispatcher.forward(request,response);
%>

<ul>
	<li><a href="./Login/LoginForm.hs">로그인</a></li>

</ul>

</body>
</html>