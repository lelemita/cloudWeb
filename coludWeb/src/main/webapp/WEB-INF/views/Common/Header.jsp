<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 날짜형식 지정 태그 라이브러리 -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<meta charset=UTF-8>
<meta name="viewport" content="width=device-width, initial-scale=1">



	<!-- Navigation -->
	<nav class="navbar navbar-inverse">
	  <div class="container-fluid">
	    <div class="navbar-header">
	      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>                        
	      </button>
	      <a class="navbar-brand" href="../">하솜정보기술 &nbsp;&nbsp;&nbsp;</a>
	    </div>
	    <div class="collapse navbar-collapse" id="myNavbar">
	      <ul class="nav navbar-nav">
		      <li id="MonitorList"><a href="#" onclick="move('../Monitoring/MonitorList.hs')">실시간 현황</a></li>
		      <li id="DataList"><a href="#" onclick="move('../Monitoring/DataList.hs' )">데이터 조회</a></li>
		      <li id="EventList"><a href="#" onclick="move('../EventLog/EventList.hs')">이벤트 목록</a></li>
		      <li id="GraphData"><a href="#" onclick="move('../Graph/DataGraph.hs' )">그래프 조회</a></li>
		      <li id="MakeReport"><a href="#">보고서 작성</a></li>
		      <!-- 관리자 로그인 -->
		      <c:if test="${sessionScope.TYPE eq 'M'}">
		      	<li id="SensorSetting"><a href="#" onclick="move('../Manager/SensorSetting.hs' )">센서 설정</a></li>
		      </c:if>
		      <!-- 담당자 로그인 -->
		      <c:if test="${sessionScope.TYPE eq 'A'}">
		      	<li id="SensorSetting"><a href="#" onclick="move('../Manager/SensorSetting.hs' )">센서 설정</a></li>
		      	<li id="UserList"><a href="../Admin/UserList.hs">사용자 조회</a></li>
		      </c:if>		      
		      
		      
	      </ul>
	      <ul class="nav navbar-nav navbar-right">
		      <li><a href="../Config/PersonalSetting.hs"><span class="glyphicon glyphicon-user"></span> ${NAME}</a></li>
		      <li><a href="../Login/Logout.hs"><span class="glyphicon glyphicon-log-out"></span> 로그아웃</a></li>
	      </ul>
	    </div>
	  </div>
	</nav>
	
	<!-- 어딘가로 보내는 용 폼 -->
	<form method="post" id="gogo">
		<!-- 릴레이용 데이터 -->
		<input type="hidden" name="nowGroup" id="targetGroup" value="${NOWGROUP}">
	</form>	
	
	
