<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 날짜형식 지정 태그 라이브러리 -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>하솜 정보기술 : 이벤트 발생 목록</title>
<meta charset=UTF-8>
<meta name="viewport" content="width=device-width, initial-scale=1">
	
<!-- jQuery -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js" type="text/javascript"></script>
<!-- Bootstrap Core JavaScript -->
<script src="../resources/bootstrap/sb-admin-2/vendor/bootstrap/js/bootstrap.min.js"></script>
<!-- Bootstrap Core CSS -->
<link href="../resources/bootstrap/sb-admin-2/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<script>
$(document).ready(function(){
		
	//초기 세팅
	// 2) 날짜선택 폼
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
	
	//인쇄 실행
	window.print();	

});
	
	// 숫자 앞에 지정한 자리수만큼 0으로 채우기
	function prependZero(num, len) {
	    while(num.toString().length < len) {
	        num = "0" + num;
	    }
	    return num;
	}

	// CSV 파일 저장하기
  function exportTableToCSV($table, filename) {
      var $headers = $table.find('tr:has(th)')
          ,$rows = $table.find('tr:has(td)')

          // Temporary delimiter characters unlikely to be typed by keyboard
          // This is to avoid accidentally splitting the actual contents
          ,tmpColDelim = String.fromCharCode(11) // vertical tab character
          ,tmpRowDelim = String.fromCharCode(0) // null character

          // actual delimiter characters for CSV format
          ,colDelim = '","'
          ,rowDelim = '"\r\n"';

          // Grab text from table into CSV formatted string
          var csv = '"';
          csv += formatRows( $headers.map(grabRow));
          csv += rowDelim;
          csv += formatRows( $rows.map(grabRow)) + '"';

          // Data URI
          var csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(csv);
          
      // For IE (tested 10+)
      if (window.navigator.msSaveOrOpenBlob) {
          var blob = new Blob([decodeURIComponent(encodeURI(csv))], {
              type: "text/csv;charset=utf-8;"
          });
          navigator.msSaveBlob(blob, filename);
      } else {
	    var hiddenElement = document.createElement('a');
	    hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csv);
	    hiddenElement.target = '_blank';
	    hiddenElement.download = filename;
	    hiddenElement.click();	
      }

      // Format the output so it has the appropriate delimiters
      function formatRows(rows){
          return rows.get().join(tmpRowDelim)
              .split(tmpRowDelim).join(rowDelim)
              .split(tmpColDelim).join(colDelim);
      }
      // Grab and format a row from the table
      function grabRow(i,row){
           
          var $row = $(row);
          //for some reason $cols = $row.find('td') || $row.find('th') won't work...
          var $cols = $row.find('td'); 
          if(!$cols.length) $cols = $row.find('th');  

          return $cols.map(grabCol)
                      .get().join(tmpColDelim);
      }
      // Grab and format a column from the table 
      function grabCol(j,col){
          var $col = $(col),
              $text = $col.text();

          return $text.replace('"', '""'); // escape double quotes
      }
  }//exportTableToCSV($table, filename)
</script>
</head>

<body> 

	<div class="container" >
		
		<!-- 조회 조건 입력 패널 -->
		<!-- Tab panes -->
		<div class="tab-content well" hidden="true">

		<form method="post" id="sfrm" action="../EventLog/EventList.hs">
			<!-- 기간 설정 well -->
			<div class="well col-sm-12">
				<!-- 시작날짜,시각 -->
				<div class="col-sm-1"  align="left">
					<label>기간</label>
				</div>
				<div class="col-sm-11">

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

	     	</div><!-- col-sm-11 -->
			</div><!-- 기간 설정 well -->

			<!-- 센서 선택 well -->
			<!-- ★★★ 데이터 조회 때와 센서 체크박스의 value가 다름에 유의할 것 -->
			
			<div class="well col-sm-12">
				<div class="col-sm-1">
					<label>센서</label>
				</div>
				<c:forEach items="${S_DISPLAYS}" var="gs_info" varStatus="st">
					<!-- 생각해보니 굳이 gs_code를 안 써도 될 지도?
					<c:set var="value" value="${gs_info.gs_code}"/>
					 -->
					<c:set var="value" value="${st.count}"/>
					
					<!-- 엄청 지저분한 체크박스 유지방법 -->
					<c:if test="${NOWSENSOR.size() ne 0}">
						<c:forEach items="${NOWSENSOR}" var="chk">
							<c:if test="${chk eq value}">
								<c:set var="chkNum" value="${st.index}"/>
							</c:if>
						</c:forEach>
						<c:if test="${st.index eq chkNum}">
							<label class="checkbox-inline">
								<input type="checkbox" name="nowSensor" value="${value}" checked="checked">${gs_info.s_display}
							</label>
						</c:if>
						<c:if test="${st.index ne chkNum}">
							<label class="checkbox-inline">
								<input type="checkbox" name="nowSensor" value="${value}">${gs_info.s_display}
							</label>
						</c:if>
						
					</c:if>
					<c:if test="${NOWSENSOR.size() eq 0}">
						<label class="checkbox-inline">
							<input type="checkbox" name="nowSensor" value="${value}">${gs_info.s_display}
						</label>	
					</c:if>						 			
				</c:forEach>
			</div>
			
			<!-- 알람종류 선택 well -->
			<div class="well col-sm-12">
				<div class="col-sm-1">
					<label>종류</label>
				</div>
				<label class="checkbox-inline">
					<input type="checkbox" id="valueAlarm" class="alarmChk" value="V">일탈알람
				</label>
				<label class="checkbox-inline">
					<input type="checkbox" id="contactAlarm" class="alarmChk" value="C">통신장애
				</label>
			</div>		
	
			<!-- 조회 버튼  -->
			<input type="button" class="btn btn-primary center-block " value=" 조회 " onclick="search();">

			<!-- hidden 요소 들 -->
			<input type="hidden" name="nowGroup" value="${NOWGROUP}">
			<input type="hidden" name="nowPage" value="1">
			<input type="hidden" name="kind" id="kind" value="${KIND}">
		</form>
		</div> <!-- /.tab-content -->

		<!-- 조회결과		 -->		
		<c:if test="${LIST ne null}"><div>
		
			<!-- 목록 보기 -->
			<!-- 1-1. 데이터가 없는 경우 -->
			<c:if test="${LIST.size() eq 0}">
				<div align="center">- 해당 기간의 데이터가 없습니다. -</div>
			</c:if>
			<!-- 1-2. 데이터가 있는 경우 -->
			<c:if test="${LIST.size() ne 0}">
				<table id="table" class="table table-hover text-center">
					<tr>
						<th class="text-center">번호</th>
						<th class="text-center">발생시각</th>
						<th class="text-center">센서</th>
						<th class="text-center">요소</th>
						<th class="text-center">값</th>
						<th class="text-center">내용</th>												
					</tr>
					
					<c:forEach items="${LIST}" var="data" varStatus="st">
						<tr>
							<td>${st.count}</td>
							<td><fmt:formatDate value="${data.evt_date}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>${data.s_display}</td>
							<td>${data.f_name}</td>
							<td>${data.e_value}</td>
							<td>${data.evt_msg}</td>
						</tr>
					</c:forEach>							
				</table>
			</c:if><!-- data 유무 검사 -->
			
		</div></c:if><!-- 조회결과 유무 -->
	</div> <!-- /.container -->

</body>
</html>