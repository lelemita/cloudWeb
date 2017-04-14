<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8>
<title>하솜 정보기술 : 사용자 목록</title>
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
		//document.write( '${DATA.userDataList[1].u_name}' );	
	});
	// 어딘가로 보내는 함수 - form은 헤더에 있다
	function move(url){
		$("#gogo").attr("action" , url)
		$("#gogo").submit();	
	}
	
	// 페이지 이동 버튼 클릭 이벤트
	function goPage (nowPage) {
		$("[name=nowPage]").val(nowPage);
		$("#sfrm").attr("action" , "../Admin/UserList.hs");
		$("#sfrm").submit();
	}
	
	// 사용자 정보 상세보기 이벤트
	function showDetail(u_id) {
		$("[name=u_id]").val(u_id);
		$("#sfrm").attr("action" , "../Admin/UserView.hs");
		$("#sfrm").submit();
	}
	
	// sfrm 보내는 이벤트
	function sendSfrm(url) {
		$("#sfrm").attr("action" , url);
		$("#sfrm").submit();
	}
	
</script>
</head>
<body>
	<!-- 상세보기 요청용 폼 -->
	<form method="post" id="sfrm" action="../Admin/UserView.hs">
		<!-- 상세정보 조회할 id 기억할 자리 -->
		<input type="hidden" name="u_id">
		<!-- 목록 복귀를 위한 릴레이 데이터 -->
		<input type="hidden" name="scope" value="${DATA.scope}">
		<input type="hidden" name="keyword" value="${DATA.keyword}">
		<input type="hidden" name="nowPage" value="${DATA.nowPage}">
	</form>

	<header>
		<%@ include file= "../Common/Header.jsp" %>
	</header>

	<div class="container" >
		<div class="well col-sm-10 col-sm-offset-1">
			<h3 class="page-header" align="center">사용자 조회</h3>
			<!-- 조회 결과 -->
			<table  class="table table-striped table-bordered  table-hover text-center">
				<tr>
					<th class="text-center">번호</th>
					<th class="text-center">권한</th>
					<th class="text-center">이름</th>
					<th class="text-center">ID</th>
					<th class="text-center">상태</th>											
				</tr>
				
				<c:forEach items="${DATA.userDataList}" var="data" varStatus="st">
					<tr onclick="showDetail('${data.u_id}');">
						<td>${st.count}</td>
						<td>${data.strType}</td>
						<td>${data.u_name}</td>
						<td>${data.u_id}</td>
						<td>${data.strState}</td>
					</tr>
				</c:forEach>							
			</table>	
			

			<!-- 페이지 이동 기능 : [이전] [1][2][3][4][5] [다음] 	-->
			<div class="col-sm-10 text-center">
				<ul class="pagination">
					<c:if test="${DATA.pInfo.startPage ne 1}">
						<li><a href="#" onclick="goPage(${DATA.pInfo.startPage-1});">이전</a></li> 
					</c:if>
					<c:forEach var="page" begin="${DATA.pInfo.startPage}" end="${DATA.pInfo.endPage}">
						<c:if test="${page eq DATA.nowPage}">
							<li class="active" id="page_${page}"><a href="#" onclick="goPage(${page});">${page}</a></li>					
						</c:if>
						<c:if test="${page ne DATA.nowPage}">
							<li id="page_${page}"><a href="#" onclick="goPage(${page});">${page}</a></li>					
						</c:if>					
					</c:forEach>
					<c:if test="${DATA.pInfo.endPage ne DATA.pInfo.totalPage}">
						<li><a href="#" onclick="goPage(${DATA.pInfo.endPage+1})">다음</a></li>
					</c:if>	
				</ul>
			</div>		
				
			<!-- 사용자 등록 버튼 -->
			<div class="col-sm-2">
				<button class="btn btn-default" onclick="sendSfrm('../Admin/UserRegistry.hs');">사용자 등록</button>				
			</div>
			
		</div><!-- well -->
	</div><!-- container -->

	<!-- 검색 기능 : 나중에 추가-->



</body>
</html>