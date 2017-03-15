<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 날짜형식 지정 태그 라이브러리 -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>하솜 정보기술 : 데이터 조회</title>
<meta charset=UTF-8>
<meta name="viewport" content="width=device-width, initial-scale=1">
	
<!-- jQuery -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js" type="text/javascript"></script>
<!-- 날짜입력폼 위한 라이브러리 ※출처: http://zetawiki.com/wiki/JQuery_UI_날짜선택기_datepicker -->
<link rel="stylesheet" href="http://code.jquery.com/ui/1.8.18/themes/base/jquery-ui.css" type="text/css" media="all" />
<script src="http://code.jquery.com/ui/1.8.18/jquery-ui.min.js" type="text/javascript"></script>
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
$(function() {

		//초기 세팅
		// 0) Header의 현재 메뉴 표시
		$("li[id='DataList']").addClass("active");
		
		
		//여기서 잘라둠
		// 1) 선택된 센서 - 요소 표시
		//  하도 안되서, 센서 생성 부분에서 더럽게 처리함.ㅠㅠㅠ 나중에 수정하자..


		// 2) 날짜선택 폼
	  var dates = $( "#startDay, #endDay " ).datepicker({
		  prevText: '이전 달',
		  nextText: '다음 달',
		  monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		  monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		  dayNames: ['일','월','화','수','목','금','토'],
		  dayNamesShort: ['일','월','화','수','목','금','토'],
		  dayNamesMin: ['일','월','화','수','목','금','토'],
		  dateFormat: 'yy-mm-dd',
		  showMonthAfterYear: true,
		  yearSuffix: '년',
			maxDate: '+1d' , //'+30d', : //30일 후까지만 선택가능
		  onSelect: function( selectedDate ) {
				var option = this.id == "startDay" ? "minDate" : "maxDate",
				instance = $( this ).data( "datepicker" ),
				date = $.datepicker.parseDate(
				instance.settings.dateFormat ||
				$.datepicker._defaults.dateFormat,
				selectedDate, instance.settings );
				dates.not( this ).datepicker( "option", option, date );
	  	}
	  });
		var now 	= new Date();
		var year 	= now.getFullYear();
		var month = prependZero(now.getMonth()+1 , 2);
		var day 	= prependZero(now.getDate() , 2);
		$("#startDay").val( "${STARTDAY}" );
		$("#endDay").val( "${ENDDAY}" );
		
		//시간 선택박스 채우기
		var hourSel1 = document.getElementById("hour1");
		var hourSel2 = document.getElementById("hour2");	
		for (var i=0; i<24 ; i++) {
			var op = new Option();
			var op2 = new Option();
			op2.value	= op.value = i;
			op2.text 	= op.text	 = prependZero(i, 2)+"시";
			hourSel1.options.add(op);
			hourSel2.options.add(op2);			
		}
		// 시간 초기 값
		$("#hour1").val("${STARTTIME}").prop("selected", true);
		$("#hour2").val("${ENDTIME}").prop("selected", true);
		$("#unitTime").val("${UNITTIME}").prop("selected", true);
});

	
	// 숫자 앞에 지정한 자리수만큼 0으로 채우기
	function prependZero(num, len) {
	    while(num.toString().length < len) {
	        num = "0" + num;
	    }
	    return num;
	}
	
	// 어딘가로 보내는 함수 - form은 헤더에 있다
	function move(url , targetGroup){
		$("#targetGroup").val(targetGroup);
		$("#gogo").attr("action" , url)
		$("#gogo").submit();	
	}
	
	// 페이지 이동 버튼 클릭 이벤트
	function goPage (nowPage) {
		$("[name=nowPage]").val(nowPage);
		$("#sfrm").attr("action" , "../Monitoring/DataList.hs");
		$("#sfrm").submit();
	}
	
	// 조회 버튼 이벤트
	function search() {
		// 무결성 검사
		if( ! $(":checkbox[name='nowSensor']:checked").is(":checked") ) {
			alert("조회 할 센서를 선택하세요");
			return false;
		}
		else if( $(":checkbox[name='nowSensor']:checked").length > 1 && $("#unitTime option:selected").val() == 0 ) {
			alert("조회간격 all을 선택하신 경우, 한 센서만 조회 가능합니다.")
			return false;
		}
		else if( $(":checkbox[name='nowSensor']:checked").length * $(":checkbox[name='nowFactor']:checked").length >= 10 ){
			alert("(센서의 수) X (요소의 수) < 10 까지만 선택 가능합니다.");
			return false;
		}
		
		if( ! $(":checkbox[name='nowFactor']:checked").is(":checked") ) {
			alert("조회 할 요소를 선택하세요");
			return false;
		}
		$("#sfrm").submit();
	}
	
	// 조회 결과 저장 이벤트
	function save() {
		if (${PINFO.totalCount > 3000}) {
			alert("결과가 3000개 이상입니다.\n조회 기간을 줄이거나,\n조회 간격을 늘려주세요.");
			return false;
		}
		var fileName = window.prompt("저장할 파일 이름을 적어주세요" , "DataList");
		
		if (fileName == null) { // 취소 버튼 누름
			return false;
		}
		$("#fileName").val(fileName);
		$("#sfrm").attr("target","_self");		
		$("#sfrm").attr("action","../Monitoring/DataSave.hs")
		$("#sfrm").submit();
	}

	// 조회 결과 인쇄 이벤트
	function print() {
		if (${PINFO.totalCount > 3000}) {
			alert("결과가 3000개 이상입니다.\n조회 기간을 줄이거나,\n조회 간격을 늘려주세요.");
			return false;
		}
	
		$("#sfrm").attr("target","_blank");
		$("#sfrm").attr("action","../Monitoring/DataPrint.hs")
		$("#sfrm").submit();
		// 원래 값으로 복구
		$("#sfrm").attr("target","_self");
		$("#sfrm").attr("action","../Monitoring/DataList.hs")		
	}		
	
</script>
</head>

<body> 
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
					<li><a href="#" onclick="move('../Monitoring/DataList.hs' , ${st.index})">${name}</a></li> 
				</c:if>
			</c:forEach>
		</ul>
		
		<!-- 조회 조건 입력 패널 -->
		<!-- Tab panes -->
		<div class="tab-content well">

		<form method="post" id="sfrm" action="../Monitoring/DataList.hs">
			<!-- hidden 요소 들 -->
			<input type="hidden" name="nowGroup"	value="${NOWGROUP}">
			<input type="hidden" name="nowPage"		value="1">		
			<input type="hidden" name="fileName" id="fileName">
		
			<!-- 기간 설정 well -->
			<div class="well col-sm-12">
				<!-- 시작날짜,시각 -->
				<div class="col-sm-1"  align="left">
					<label>기간</label>
				</div>
				<div class="col-sm-8">

					<div class="col-sm-3">
						<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
							<input type="text" name="startDay" id="startDay" class="form-control" placeholder="시작 날짜">
						</div>
					</div>
					<div class="col-sm-2 ">
						<select name="startTime" id="hour1" class="form-control" >
						</select>
					</div>
					<!-- 마지막 날짜,시각 -->
					<div class="col-sm-1" align="center">
						<label>~</label>
					</div>
					<div class="col-sm-3">
						<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
							<input type="text" name="endDay" id="endDay" class="form-control" placeholder="마지막 날짜">
						</div>
					</div>
					<div class="col-sm-2 ">
						<select name="endTime" id="hour2" class="form-control" ></select>
					</div>

	     	</div><!-- col-sm-8 -->
	     	
	     	<!-- 조회 단위 시간 (분) -->
	     	<div class="col-sm-3">
					<div class="col-sm-5" align="right">
						<label>조회 간격</label>
					</div>
		     	<div class="col-sm-7">
						<select name="unitTime" id="unitTime" class="form-control" >
							<option value=0>all</option>
							<option value=1> 1분</option>
							<option value=5> 5분</option>
							<option value=10>10분</option>
							<option value=15>15분</option>
							<option value=30>30분</option>
						</select>
					</div>
	   		</div>
			</div><!-- 기간 설정 well -->

			<!-- 센서 선택 well -->
			<div class="well col-sm-12">
				<div class="col-sm-1">
					<label>센서</label>
				</div>
				<c:forEach items="${S_DISPLAYS}" var="gs_info" varStatus="st">
					
					<!-- 엄청 지저분한 체크박스 유지방법 -->
					<c:set var="name" value="${gs_info.gs_no}_${gs_info.s_display}"/>
					<c:if test="${NOWSENSOR.size() ne 0}">
						<c:forEach items="${NOWSENSOR}" var="chk">
							<c:if test="${chk eq name}">
								<c:set var="chkNum" value="${st.index}"/>
							</c:if>
						</c:forEach>
						<c:if test="${st.index eq chkNum}">
							<label class="checkbox-inline">
								<input type="checkbox" name="nowSensor" value="${name}" checked="checked">${gs_info.s_display}
							</label>
						</c:if>
						<c:if test="${st.index ne chkNum}">
							<label class="checkbox-inline">
								<input type="checkbox" name="nowSensor" value="${name}">${gs_info.s_display}
							</label>
						</c:if>
						
					</c:if>
					<c:if test="${NOWSENSOR.size() eq 0}">
						<label class="checkbox-inline">
							<input type="checkbox" name="nowSensor" value="${gs_info.gs_no}_${gs_info.s_display}">${gs_info.s_display}
						</label>	
					</c:if>				
					
					<!-- 여기서는 이렇게만 하고, <script>에서 처리하고 싶었는데 잘 안되는 중.. (나중에 수정하자) ★★★
					<label class="checkbox-inline">
						<input type="checkbox" name="nowSensor" value="${gs_info.gs_no}_${gs_info.s_display}">${gs_info.s_display}
					</label>
		 			-->
		 			
				</c:forEach>
			</div>
			
			<!-- 요소 선택 well -->
			<div class="well col-sm-12">
				<div class="col-sm-1">
					<label>요소</label>
				</div>
				<c:forEach items="${F_NAMES}" var="f_info" varStatus="st">
					<label class="checkbox-inline">
					
					<!-- 엄청 지저분한 체크박스 유지방법 -->
						<c:set var="name" value="${f_info.f_table_name}"/>
						<c:if test="${NOWFACTOR.size() ne 0}">
							<c:forEach items="${NOWFACTOR}" var="chk">
								<c:if test="${chk eq name}">
									<c:set var="chkNum" value="${st.index}"/>
								</c:if>
							</c:forEach>
							<c:if test="${st.index eq chkNum}">
								<input type="checkbox" name="nowFactor" value="${name}" checked="checked">${f_info.f_name}
							</c:if>
							<c:if test="${st.index ne chkNum}">
								<input type="checkbox" name="nowFactor" value="${name}">${f_info.f_name}	
							</c:if>
							
						</c:if>
						<c:if test="${NOWFACTOR.size() eq 0}">
							<input type="checkbox" name="nowFactor" value="${f_info.f_table_name}">${f_info.f_name}	
						</c:if>	
					
					
						<!-- 여기서는 이렇게만 하고, <script>에서 처리하고 싶었는데 잘 안되는 중.. (나중에 수정하자) ★★★ 
							<input type="checkbox" name="nowFactor" value="${f_info.f_table_name}">${f_info.f_name}
						 -->					
					</label>
				</c:forEach>
			</div>		
	
			<!-- 조회 버튼  -->
			<div align="center">
			<input type="button" class="btn btn-primary" value=" 조회 " onclick="search();">
			<!-- 조회 후, 결과 저장(csv) 요청 버튼 -->
			<c:if test="${LISTCOUNT ne null && LIST_0.size() ne 0}">
				<input type="button" class="btn btn-primary" value="결과 저장" id="csvExporter" onclick="save();">
				<input type="button" class="btn btn-primary" value="결과 인쇄" id="csvExporter" onclick="print();">
			</c:if>
			</div>

		</form>
		</div> <!-- /.tab-content -->

		<!-- 조회결과		 -->		
		<c:if test="${LISTCOUNT ne null}"><div>
		
			<!-- 조회결과 1-1 : raw Data -->
			<c:if test="${UNITTIME eq 0}">
				<!-- 목록 보기 -->
				<!-- 1-1. 데이터가 없는 경우 -->
				<c:if test="${LIST_0.size() eq 0}">
					<div align="center">- 해당 기간의 데이터가 없습니다. -</div>
				</c:if>
				<!-- 1-2. 데이터가 있는 경우 -->
				<c:if test="${LIST_0.size() ne 0}">
					<table  class="table table-hover text-center">
						<tr>
							<th class="text-center">번호</th>
							<th class="text-center">측정시각</th>
							<c:forEach items="${LISTCOUNT}" var="num">
								<th class="text-center">
									${LISTNAMES.get(num)}
								</th>			
							</c:forEach>
						</tr>
						
						<c:forEach items="${LIST_0}" var="data" varStatus="st">
							<tr>
								<td>${data.no}</td>
								<td><fmt:formatDate value="${data.date}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>${data.value}</td>

								<!-- 각 요소 별로 반복 -->
								<c:forEach items="${LISTLIST}" var="list">
									<td>
										${list[st.index].value}
									</td>
								</c:forEach>

							</tr>
						</c:forEach>							
					</table>
				</c:if><!-- data 유무 검사 -->
			</c:if><!-- 조회 결과 1-1 -->

			<!-- 조회결과 1-2 : 조회 간격 적용 Data -->
			<c:if test="${UNITTIME ne 0}">
				<!-- 목록 보기 -->
				<table  class="table table-hover text-center">
					<tr>
						<th class="text-center">측정일</th>
						<th class="text-center">측정시각</th>
						<c:forEach items="${LISTCOUNT}" var="num">
							<th class="text-center">
								${LISTNAMES.get(num)}
							</th>			
						</c:forEach>
					</tr>
				
					<c:forEach items="${DATELIST}" var="date">
					<tr>
						<td><fmt:formatDate value="${date}" type="date" pattern="yyyy-MM-dd"/></td>
						<td><fmt:formatDate value="${date}" type="time" pattern="HH:mm"/></td>
	
						<!-- 각 요소 별로, 해당 시각 데이터 찾음 -->
						<c:forEach items="${LISTLIST}" var="list">
							<td>
							
								<!-- data와 시간이 동일한 경우의 value만 보여줌 -->
								<c:forEach items="${list}" var="data" varStatus="st">
									<c:if test="${data.hour eq date.hours && data.gr*UNITTIME eq date.minutes}">
										${data.value/10}								
									</c:if>
								</c:forEach>
								
							</td>
						</c:forEach>						
						
					</tr>
					</c:forEach>	
				</table>
			</c:if><!-- 조회 결과 1-2 -->
			
			<!-- 페이지 이동 기능 : [이전] [1][2][3][4][5] [다음] 	-->
			<div  class="col-sm-12 text-center">
				<ul class="pagination">
					<c:if test="${PINFO.startPage ne 1}">
						<li><a href="#" onclick="goPage(${PINFO.startPage-1});">이전</a></li> 
					</c:if>
					<c:forEach var="page" begin="${PINFO.startPage}" end="${PINFO.endPage}">
						<c:if test="${page eq NOWPAGE}">
							<li class="active" id="page_${page}"><a href="#" onclick="goPage(${page});">${page}</a></li>					
						</c:if>
						<c:if test="${page ne NOWPAGE}">
							<li id="page_${page}"><a href="#" onclick="goPage(${page});">${page}</a></li>
						</c:if>					
					</c:forEach>
					<c:if test="${PINFO.endPage ne PINFO.totalPage}">
						<li><a href="#" onclick="goPage(${PINFO.endPage+1})">다음</a></li>
					</c:if>	
				</ul>
			</div>	
			
		</div></c:if><!-- 조회결과 유무 -->
	</div> <!-- /.container -->

    <!-- Metis Menu Plugin JavaScript -->
    <script src="../resources/bootstrap/sb-admin-2/vendor/metisMenu/metisMenu.min.js"></script>
    <!-- Custom Theme JavaScript -->
    <script src="../resources/bootstrap/sb-admin-2/dist/js/sb-admin-2.js"></script>

</body>
</html>