<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 날짜형식 지정 태그 라이브러리 -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>하솜 정보기술 : 모니터링 페이지</title>
<meta charset=UTF-8>
<meta name="viewport" content="width=device-width, initial-scale=1">
	
<!-- Bootstrap Core CSS -->
<link href="../resources/bootstrap/sb-admin-2/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- jQuery 
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<!-- Bootstrap Core JavaScript -->
<script src="../resources/bootstrap/sb-admin-2/vendor/bootstrap/js/bootstrap.min.js"></script>
<!-- MetisMenu CSS -->
<link href="../resources/bootstrap/sb-admin-2/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">
<!-- Custom CSS -->
<link href="../resources/bootstrap/sb-admin-2/dist/css/sb-admin-2.css" rel="stylesheet">
<!-- Custom Fonts -->
<link href="../resources/bootstrap/sb-admin-2/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<style>
	.floating-box {
	    display: inline-block;
	    width: 150px;
	    margin: 5px;
	}
	.panel-body {
	  padding: 3px;
	}
	.panel-heading {
	  padding: 3px;
	}
	.panel-footer {
	  padding: 3px;
	}
	.well {
	  min-height: 20px;
	  padding: 19px;
	  margin-bottom: 20px;
	  background-color: #ffffff;
	  border: 1px solid #c3c3c3;
	  border-radius: 3px;
	  -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .05);
	          box-shadow: inset 0 1px 1px rgba(0, 0, 0, .05);
	}
	.container {
	  padding-right: 15px;
	  padding-left: 15px;
	  margin : auto;
	  width : 95%;
	}
</style>
<script>
	$(document).ready(function(){
		// Header의 현재 메뉴 표시
		$("li[id='MonitorList']").addClass("active");	
		
	});
	
	// 새로고침 스크립트 → Ajax 처리
	function pagestart(sec) {
		if (sec > 0) {
			window.setTimeout("pagereload()", sec*1000);
		}
	}
	function pagereload() {
		//location.reload(); //새로고침으로 처리하는 경우
		//		var currentLocation = window.location;
		//		$("#sensorArea").load(currentLocation + ' #sensorArea);
		
		$.ajax({
			url		: "../Monitoring/UpdateData.hs",
			type	: "POST" ,   // GET이면, IE11에서 400에러 발생
			//data	: "nowGroup=${NOWGROUP}&temp=" + new Date() ,
			data : {'nowGroup':${NOWGROUP},'temp':new Date()} , //POST는 Object로 전달가능
			dataType: "html" ,
			success	: function(data){
				$("#sensorArea").html(data);
				pagestart("${G_M_PERIOD}");
			} ,
			error	: function(request,status,error){
		    alert("값 업데이트 과정에서 에러\n" +"code:"+request.status+"\n"
		    		    +"message:"+request.responseText+"\n"+"error:"+error);
			}
		});//ajax
	}
	
	// 어딘가로 보내는 함수 form은 헤더에 있다
	function move(url){
		$("#gogo").attr("action" , url);
		$("#gogo").submit();
	}
	// 탭 이동 함수
	function moveTab(url , targetGroup){
		$("#targetGroup").val(targetGroup);
		$("#gogo").attr("action" , url);
		$("#gogo").submit();
	}	
	
</script>
</head>

<body onLoad="pagereload()"> <!-- 센서값 부분 Ajax로 부르기 -->

	<header>
		<%@ include file= "../Common/Header.jsp" %>
	</header>
	
	<div class="container" >
		<!-- Nav tabs -->
		<ul class="nav nav-tabs nav-pills nav-justified">
			<!-- 해당 사용자가 담당하는 그룹 선택 탭  -->
			<c:forEach items="${G_NAMES}" var="name" varStatus="st">
				<c:if test="${st.index eq NOWGROUP}">
					<li class="active"><a href="#">${name}</a></li>
				</c:if>
				<c:if test="${st.index ne NOWGROUP}">
					<li><a href="#" onclick="moveTab('../Monitoring/MonitorList.hs' , ${st.index})">${name}</a></li> <!-- ★★★★★ (나중에) post 방식으로 바꾸자 -->
				</c:if>
			</c:forEach>
		</ul>
		
		<!-- Tab panes -->
		<div id="sensorArea" class="tab-content well">
			<!-- Ajax로 센서 데이터 들어올 영역 -->
		</div> <!-- /.tab-content well-->
	</div> <!-- /.container -->

    <!-- Metis Menu Plugin JavaScript -->
    <script src="../resources/bootstrap/sb-admin-2/vendor/metisMenu/metisMenu.min.js"></script>
    <!-- Custom Theme JavaScript -->
    <script src="../resources/bootstrap/sb-admin-2/dist/js/sb-admin-2.js"></script>

</body>
</html>