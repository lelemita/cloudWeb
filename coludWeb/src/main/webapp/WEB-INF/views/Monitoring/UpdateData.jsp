<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 날짜형식 지정 태그 라이브러리 -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
		<!-- 센서 수만큼 패널 생성 (default class) -->
			<c:forEach items="${LIST}" var="data" varStatus="st">
				<div class="floating-box text-center">
					
					<!-- 패널 상태에 따라 패널 class(색)을 다르게 설정 : 정상-녹색 / 일탈-적색 / 통신장애-그대로(panel-default) -->
					<!-- 통신장애 파악을 위해 현재시간 파악 -->
					<jsp:useBean id="now" class="java.util.Date" />
					<fmt:formatDate var="nowTime"	value="${now}"			pattern="yyyyMMddHHmmss" />
					<fmt:formatDate var="lastTime"	value="${data.date}"	pattern="yyyyMMddHHmmss" />		
					<fmt:parseNumber var="gapTime" value="${(now.time - data.date.time) / (1000) }" integerOnly="true" />
		
					<c:choose>						
						<c:when test="${G_M_OFF lt gapTime}" >
							<!-- 통신 장애 -->
							<div class="panel panel-default">
						</c:when>
						<c:otherwise>
							<!-- 통신상태 정상 → 측정값 검사 -->
							<c:set var="isNormal" value="true" />
							<c:forEach items="${data.values}" var="value" varStatus="st">
								<c:if test="${value+0 lt data.l_lows[st.index]+0 or value+0 gt data.l_highs[st.index]+0}">
									<c:set var="isNormal" value="false" />
								</c:if>
							</c:forEach>
							<c:choose>
							    <c:when test="${isNormal eq true}">
							    	<div class="panel panel-green">
							    </c:when>
							    <c:otherwise>
							    	<div class="panel panel-red">
							    </c:otherwise>
						    </c:choose>    
					    </c:otherwise>
				    </c:choose>
				    
				    	<!-- 센서 위치 표시 -->
				        <div class="panel-heading">
				            ${data.s_display}
				        </div>
				        
				        <!-- 측정값 표시 -->
				        <div class="panel-body">
							<!-- 각 패널 속에 요소 수만큼 정보표시 -->
				        	<c:forEach items="${data.values}" var="value" varStatus="st">
								<c:choose>
									<c:when test="${ (value+0 lt data.l_lows[st.index]+0) or (value+0 gt data.l_highs[st.index]+0)}">
									<!-- 문자열을 숫자로 바꾸기 위해 +0 을 해줌 -->
								    	<h4><font color="red">${value} ${data.f_units[st.index]}</font></h4>
								    </c:when>
								    <c:otherwise>
								    	<h4>${value} ${data.f_units[st.index]}</h4>
								    </c:otherwise>
							    </c:choose> 
							</c:forEach>
						</div>
						
						<!-- 측정시간 표시 -->
						<div class="panel-footer">
							<small><fmt:formatDate value="${data.date}" pattern="MM/dd   HH:mm"/></small>
						</div>
						
				    </div> <!-- 센서 한칸 -->
				</div>	<!-- ./floating-box text-center -->
			</c:forEach>		
