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



<!-- 날짜입력폼 위한 라이브러리 -->
<link rel="stylesheet" href="http://code.jquery.com/ui/1.8.18/themes/base/jquery-ui.css" type="text/css" media="all" />
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
<!-- jQuery -->
<script src="http://code.jquery.com/ui/1.8.18/jquery-ui.min.js" type="text/javascript"></script>
<!-- Bootstrap Core JavaScript -->
<script src="../resources/bootstrap/sb-admin-2/vendor/bootstrap/js/bootstrap.min.js"></script>
<!-- Bootstrap Core CSS -->
<link href="../resources/bootstrap/sb-admin-2/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- MetisMenu CSS -->
<link href="../resources/bootstrap/sb-admin-2/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">
<!-- Custom CSS -->
<link href="../resources/bootstrap/sb-admin-2/dist/css/sb-admin-2.css" rel="stylesheet">
<!-- Custom Fonts -->
<link href="../resources/bootstrap/sb-admin-2/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">





<script>
$(function() {
  var dates = $( "#from, #to " ).datepicker({
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
		maxDate:'+30d',
	  onSelect: function( selectedDate ) {
			var option = this.id == "from" ? "minDate" : "maxDate",
			instance = $( this ).data( "datepicker" ),
			date = $.datepicker.parseDate(
			instance.settings.dateFormat ||
			$.datepicker._defaults.dateFormat,
			selectedDate, instance.settings );
			dates.not( this ).datepicker( "option", option, date );
  	}
  });
});
</script>
<p>조회기간: <input type="text" id="from"> ~ <input type="text" id="to"></p>

<!-- 
<c:forEach var="data" items="${LIST}" varStatus="st">
	<p>
		<fmt:formatDate value="${data}" type="both" pattern="yyyy-MM-dd HH:mm"/>
	</p>
</c:forEach>
 -->

			<div class="well col-sm-12">
				<div class="col-sm-1">
					<label>센서</label>
				</div>
				<form action="../test/test.hs">
					<label class="checkbox-inline">
						<input type="checkbox" name="nowSensor" value="4">입구 
					</label>
				
					<label class="checkbox-inline">
						<input type="checkbox" name="nowSensor" value="5">중간 
					</label>
				
					<label class="checkbox-inline">
						<input type="checkbox" name="nowSensor" value="6">출구 
					</label>
					<button>submit</button>
				</form>
				
			</div>




