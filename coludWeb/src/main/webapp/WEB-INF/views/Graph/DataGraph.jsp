<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 날짜형식 지정 태그 라이브러리 -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- 문자열 치환을 위한 태그 라이브러리 -->
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title>하솜 정보기술 : 그래프 조회</title>
<meta charset=UTF-8>
<meta name="viewport" content="width=device-width, initial-scale=1">
	
<!-- jQuery : 이 이상의 버전을 사용하면 날짜가 제대로 보이지 않는다-->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js" type="text/javascript"></script>
<!-- 날짜입력폼 위한 라이브러리 ※출처: http://zetawiki.com/wiki/JQuery_UI_날짜선택기_datepicker -->
<!-- 이하의 버전을 사용하면 : 좁은 화면에서 메뉴 단축이 팝업되지 않는다. -->
<link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" type="text/css" media="all" />
<script src="http://code.jquery.com/ui/1.12.1/jquery-ui.min.js" type="text/javascript"></script>

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
<!-- flot -->
<script src="../resources/bootstrap/sb-admin-2/vendor/flot/jquery.flot.js"></script>
<script src="../resources/bootstrap/sb-admin-2/vendor/flot/jquery.flot.time.js"></script>
<script src="../resources/bootstrap/sb-admin-2/vendor/flot/jquery.flot.selection.js"></script>
<!-- 직접 작성 -->
<link href="../resources/css/common.css?ver=0.1" rel="stylesheet" type="text/css">

<script>
$(function() {
		//초기 세팅
		// 0) Header의 현재 메뉴 표시
		$("li[id='GraphData']").addClass("active");
		
		// 1) 선택된 요소 표시
		$(":radio[value=${DATA.nowFactor}]").attr("checked",true);	

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
		$("#startDay").val( "${DATA.startDay}" );
		$("#endDay").val( "${DATA.endDay}" );
		
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
		$("#hour1").val("${DATA.startTime}").prop("selected", true);
		$("#hour2").val("${DATA.endTime}").prop("selected", true);
		
		// 그래프 그리기
		showGraph();
});

	
	// 숫자 앞에 지정한 자리수만큼 0으로 채우기
	function prependZero(num, len) {
	    while(num.toString().length < len) {
	        num = "0" + num;
	    }
	    return num;
	}
	
	// 어딘가로 보내는 함수 - form은 헤더에 있다
	function move(url){
		$("#gogo").attr("action" , url)
		$("#gogo").submit();	
	}
	// 탭 이동 함수
	function moveTab(url , targetGroup){
		$("#targetGroup").val(targetGroup);
		$("#gogo").attr("action" , url);
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
		else if( $(":checkbox[name='nowSensor']:checked").length > 6 ){
			alert("센서는 6개까지 선택 가능합니다.");
			return false;
		}		
		
		if( ! $(":radio[name='nowFactor']:checked").is(":checked") ) {
			alert("조회 할 요소를 선택하세요");
			return false;
		}
		$("#sfrm").submit();
	}	
	
	// 그래프 그리는 메서드
	function showGraph() {
		// hashmap 양식의 파라메터를 받아서 Object객체로 변환
		var datatext = '${GRAPHDATA}'.replace(/=/g , ":");
		var temp = eval("("+ datatext +")");
		// hashmap은 순서가 섞인다ㅠ → 일단 알파벳 순으로 정렬..
		var datasets = [];
		Object.keys(temp)
      		.sort()
      		.forEach(function(v, i) {
          		//console.log(v,temp[v]);      			
          		datasets[i] = temp[v];
       		});
		
		// 시간 x축 표현하는 함수
		function weekendAreas(axes) {
			var markings = [],
				d = new Date(axes.xaxis.min);

			// go to the first Saturday
			d.setUTCDate(d.getUTCDate() - ((d.getUTCDay() + 1) % 7))
			d.setUTCSeconds(0);
			d.setUTCMinutes(0);
			d.setUTCHours(0);

			var i = d.getTime();
			// when we don't set yaxis, the rectangle automatically
			// extends to infinity upwards and downwards

			do {
				markings.push({ xaxis: { from: i, to: i + 2 * 24 * 60 * 60 * 1000 } });
				i += 7 * 24 * 60 * 60 * 1000;
			} while (i < axes.xaxis.max);

			return markings;
		}		
		
		
	// 그래프 그리기
	var plot = $.plot("#placeholder", datasets, {
		series: {
			lines: {
				show: true
			},
			points: {
				show: false
			}
		},
		xaxis: {
			mode: "time",
			timezone: "browser",
			tickLength: 5
		},
		selection: {
			mode: "x"
		},		
		grid: {
			hoverable: true,
			clickable: true,
			markings: weekendAreas
		}
	});

	$("<div id='tooltip'></div>").css({
		position: "absolute",
		display: "none",
		border: "1px solid #fdd",
		padding: "2px",
		"background-color": "#fee",
		opacity: 0.80
	}).appendTo("body");

	$("#placeholder").bind("plothover", function (event, pos, item) {

		if ($("#enablePosition:checked").length > 0) {
			var str = "(" + pos.x.toFixed(2) + ", " + pos.y.toFixed(2) + ")";
			$("#hoverdata").text(str);
		}

		// 툴팁 표시
		if (item) {
			var x = item.datapoint[0].toFixed(2),
					y = item.datapoint[1].toFixed(2);

			var dateX = new Date(x*1);
			var timeX = (dateX.getMonth()+1) + "/" + dateX.getDate() + " " + dateX.getHours() + ":" + dateX.getMinutes();
 			
			$("#tooltip").html(item.series.label +":" + y + "<br>(" + timeX + ")" )
				.css({top: item.pageY+5, left: item.pageX+5})
				.fadeIn(200);
		} else {
			$("#tooltip").hide();
		}

	});

	$("#placeholder").bind("plotclick", function (event, pos, item) {
		if (item) {
			$("#clickdata").text(" - click point " + item.dataIndex + " in " + item.series.label);
			plot.highlight(item.series, item.datapoint);
		}
	});
		
	}//showGraph()
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
				<c:if test="${st.index eq DATA.nowGroup}">
					<li class="active"><a href="#">${name}</a></li>
				</c:if>
				<c:if test="${st.index ne DATA.nowGroup}">
					<li><a href="#" onclick="moveTab('../Graph/DataGraph.hs' , ${st.index})">${name}</a></li> 
				</c:if>
			</c:forEach>
		</ul>
		
		<!-- 조회 조건 입력 패널 -->
		<!-- Tab panes -->
		<div class="tab-content well">

		<form method="post" id="sfrm" action="../Graph/DataGraph.hs">
			<!-- hidden 요소 들 -->
			<input type="hidden" name="nowGroup"	value="${DATA.nowGroup}">
		
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
					<label class="checkbox-inline"> <!-- radio-inline 보다 이게 낫네;; -->			
							<input type="radio" name="nowFactor" value="${f_info.f_table_name}">${f_info.f_name}
					</label>
				</c:forEach>
			</div>		
	
			<!-- 조회 버튼  -->
			<div align="center">
			<input type="button" class="btn btn-primary" value=" 조회 " onclick="search();">
			</div>

		</form>
		</div> <!-- /.tab-content -->

		<!-- 조회결과		 -->		
		<!-- 대충구현 : 나중에, 데이터가 없으면, 데이터 없다고 말하도록 수정하자 -->
		<c:if test="${GRAPHDATA eq '-1'}">
			<p align="center">- 해당 데이터가 없습니다. -</p>
		</c:if>

		<c:if test="${GRAPHDATA ne null && GRAPHDATA ne '-1'}"><div>
			<div class="well col-sm-12" align="center">
				<div id="placeholder" class="demo-placeholder" style="width:100%; height:400px"></div>
			</div>
		
			<!-- 미구현 : 축약 선택하는 부분
			<div class="demo-container">
				<div id="overview" class="demo-placeholder" style="float:left; width:90%; height:150px"></div>
			</div>		
			 -->

			
		</div></c:if><!-- 조회결과 유무 -->
	</div> <!-- /.container -->

    <!-- Metis Menu Plugin JavaScript -->
    <script src="../resources/bootstrap/sb-admin-2/vendor/metisMenu/metisMenu.min.js"></script>
    <!-- Custom Theme JavaScript -->
    <script src="../resources/bootstrap/sb-admin-2/dist/js/sb-admin-2.js"></script>

</body>
</html>