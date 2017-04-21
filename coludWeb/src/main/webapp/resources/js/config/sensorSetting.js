

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


// 목록가기 이벤트
function submitForm(url) {
	$("#sfrm").attr("action" , url);
	$("#sfrm").submit();
}




// 센서 설정 전용
$(document).ready(function(){
	// 설정 값 변경
	$(".formInTable").change(function() {
		$(this).addClass("changedValue");			
		$(this).removeClass("inputError");
		document.getElementById("msg").innerHTML="";
		
		// 무결성 검사 	: 걸리면 inputError 클래스 추가, 에러메시지 표시, 저장하기 버튼 비활성화
		//	1) null 금지
		if($(this).val().length==0) {
			$(this).addClass("inputError");
			$("#saveBtn").attr("disabled" , "disabled");
			document.getElementById("msg").innerHTML="모든 값은 필수 입니다.";
			return;
		}
		
		//	2) 정규식 : s_display 외에는 숫자와 - . 만 가능 / s_display에는$불가능
		if(this.id.indexOf("s_display")>=0) {
			// 센서 위치(s_display) 인 경우
			if(this.value.indexOf("$")>=0) {
				$(this).addClass("inputError");
				$("#saveBtn").attr("disabled" , "disabled");
				document.getElementById("msg").innerHTML="'$'마크는 사용할 수 없습니다.";
				return;
			}
		}else{
			// 그외의 경우
			var regExp = /^[-]?\d{1,8}(\.?\d*)$/;
			if(!regExp.test(this.value)) {
				$(this).addClass("inputError");
				$("#saveBtn").attr("disabled" , "disabled");
				document.getElementById("msg").innerHTML="잘못된 입력입니다.";
				return;
			}
		}
		
		//	3) 복구 값 검사 : 복구값이 임계값보다 작거나 크도록		
		//	4) 임계 값 검사 : 임계값이 복구값보다 작거나 크도록		
		if(this.id.indexOf("l_highlow")>=0) {
			//상한복구값이 변경된 경우
			var high = document.getElementById(this.id.replace("low","")).value;
			if(high - this.value <= 0 ) {
				$(this).addClass("inputError");	
				$("#saveBtn").attr("disabled" , "disabled");
				document.getElementById("msg").innerHTML="상한복구값은 상한값보다 낮아야 합니다.";
				return;
			}
		}else if(this.id.indexOf("l_lowhigh")>=0) {
			//하한복구값
			var low = document.getElementById(this.id.replace("high","")).value;
			if(low - this.value >= 0 ) {
				$(this).addClass("inputError");	
				$("#saveBtn").attr("disabled" , "disabled");
				document.getElementById("msg").innerHTML="하한복구값은 하한값보다 높아야 합니다.";
				return;
			}			
		}else if(this.id.indexOf("l_high")>=0) {
			//상한값이 변경된 경우 : 상한복구, 하한값과 비교
			$(this).addClass("changedValue");
			var msg = "";
			// 1) 하한값과 비교
			var low = $("#"+this.id.replace("high","low"));
			low.removeClass("inputError");
			if(low.val() - this.value >= 0){
				low.addClass("inputError");
				$("#saveBtn").attr("disabled" , "disabled");
				msg += "상한값이 하한값보다 높아야 합니다.<br>"
			}
			// 2) 상한복구값과 비교
			var highlow = $("#"+this.id.replace("high","highlow"));
			highlow.removeClass("inputError");		
			if(highlow.val() - this.value >= 0 ) {
				highlow.addClass("inputError");	
				$("#saveBtn").attr("disabled" , "disabled");
				msg += "상한복구값이 상한값보다 낮아야 합니다.<br>";
			}
			document.getElementById("msg").innerHTML=msg;
			if(msg.length !== 0) { return; }
		}else if(this.id.indexOf("l_low")>=0) {
			//하한값이 변경된 경우
			$(this).addClass("changedValue");	
			var msg = "";
			// 1) 상한값과 비교		
			var high = $("#"+this.id.replace("low","high"));
			high.removeClass("inputError");
			if(high.val() - this.value <= 0){
				high.addClass("inputError");
				$("#saveBtn").attr("disabled" , "disabled");
				msg += "상한값이 하한값보다 높아야 합니다.<br>"
			}			
			// 2) 하한복구값과 비교
			var lowhigh = $("#"+this.id.replace("low","lowhigh"));
			lowhigh.removeClass("inputError");
			if( lowhigh.val() - this.value <= 0 ) {
				lowhigh.addClass("inputError");
				$("#saveBtn").attr("disabled" , "disabled");
				msg += "하한복구값이 하한값보다 높아야 합니다.<br>";
			}
			document.getElementById("msg").innerHTML=msg;
			if(msg.length !== 0) { return; }
		}		
				
		// 저장하기 버튼 활성화 
		var errs = document.getElementsByClassName('inputError');
		if( errs.length == 0 ){	
			document.getElementById("saveBtn").disabled = false;
		}else if($("#msg").val().length == 0) {
			document.getElementById("msg").innerHTML="붉은색 항목을 점검해 주세요.";
		}
	});
});

// 저장하기 버튼 이벤트
function saveValues() {
	// 무결성 검사
	var errs = document.getElementsByClassName('inputError');
	if( errs.length != 0 ){
		document.getElementById("msg").innerHTML="붉은색 항목을 점검해 주세요.";
		return;
	}
	
	//담당자 비밀번호 검사 (Ajax)
	var pw = prompt("비밀번호를 입력해주세요");	
	if(pw.length==0) { return; }
	/*  if(checkPassword(pw) == false ){ return; }
	 	: ajax는 결과가 언제올지 몰라서, return 받아서 판단할 수가 없다..ㅠㅠ
	 		→ 이후 과정은 ajax 함수 안에서 처리하자..
	*/
	checkPassword(pw);
}

//비밀번호 검사
function checkPassword(pw) {
	$.ajax({
		url		:	"../Config/PasswordChk.hs", 
		type	:	"POST",
		data	: {'id': $('#myid').val() , 'pw': pw , 'temp': new Date() },
		dataType	:	"text",
		success	: function(data) {
			var u_type = data.trim();
			if (u_type == "A" || u_type == 'M'){
				// 변경된 값들 각각 form 만들어서 집어넣기. name 주의
				var changes = document.getElementsByClassName('changedValue');
				var result = "";
				for(var i=0; i<changes.length ; i++) {
					var temp = changes[i].id + "$" + changes[i].value; //.. 이게 최선은 아닐꺼 같은데.
					result += "<input type='hidden' name='changes' value='" + temp + "'>";
				}
					
				// form에 넣기
				document.getElementById("box").innerHTML = result;
				// 전송
				$("#sfrm").attr("action" , "../Manager/SensorSettingProc.hs");
				$("#sfrm").submit();
			}
			else {
				alert("비밀번호가 틀리거나, 담당자가 아닙니다.");
				return false;
			}
		}, 
		error	:	 function(request,status,error){
	    alert("비밀번호 확인 과정에서 에러\n" +"code:"+request.status+"\n"
	    		    +"message:"+request.responseText+"\n"+"error:"+error);
		}
	});//ajax
}	


