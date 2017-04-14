$(document).ready(function(){
	//pop 메시지
	if($("#pop").val().length!=0) {
		alert($("#pop").val());
	}
	
	// ajax 아이디 중복 검사
	$("#temp_id").change(function() {
		$("#temp_id").removeClass("inputError");
		$("#u_id").val("");	
		
		var temp_id = $("#temp_id").val();
		if(temp_id.length==0) {
			$("#msg").html("<font color='red'> ID는 필수 항목 입니다.</font>");
			$("#temp_id").addClass("inputError");
			return;
		}
		// 정규식 검사 (숫자, 영문자)
		var reg_ID = /^[a-z0-9_]{4,20}$/;
		if( ! reg_ID.test( $("#temp_id").val() ) ) {
			$("#u_id").val("");
			$("#msg").html("<font color='red'> ID는 영,숫자 4~20문자 입니다.</font>");
			$("#temp_id").addClass("inputError");
			return;
		}
		
		$.ajax({
			url : "../Admin/IDcheck.hs" ,
			type : "get" ,
			data : "id=" + temp_id + "&temp=" + new Date(),
			dataType: "text",
			success : function(data) {
				if(data.trim().length!=0) {
					$("#u_id").val("");			
					$("#msg").html("<font color='red'> " + data.trim() + " : 사용중인 ID 입니다.</font>");
					$("#temp_id").addClass("inputError");
				} else {
					$("#u_id").val(temp_id);
					$("#msg").html("<font color='blue'> " + temp_id + " : 사용 가능한 ID 입니다.</font>");
					$("#temp_id").removeClass("inputError");
				}
			},
			error	:	 function(request,status,error){
			    alert("ID 중복 검사 과정에서 에러\n" +"code:"+request.status+"\n"
			    		    +"message:"+request.responseText+"\n"+"error:"+error);
			}			
		});//ajax()
	});//change()
});

// 어딘가로 보내는 함수 - form은 헤더에 있다
function move(url){
	$("#gogo").attr("action" , url)
	$("#gogo").submit();	
}

// 목록가기 이벤트
function submitForm(url) {
	$("#sfrm").attr("action" , url);
	$("#sfrm").submit();
}

// 등록하기 버튼 이벤트
function register(url) {
	//무결성 검사
	if( $("#u_id").val().length == 0 ) {
		alert("유효한 ID를 입력해주세요."); return;
	}
	
	var chkmsg = "";
	$("#u_name").removeClass("inputError");
	$("#u_tel").removeClass("inputError");
	$("#u_mail").removeClass("inputError");
	if( $("#u_name").val().length == 0 ) {
		chkmsg += " 이름,";
		$("#u_name").addClass("inputError");
	}
	$("#u_tel").val( rmvMark($("#u_tel").val()) );
	if( $("#u_tel").val().length == 0 ) {
		chkmsg += " 전화번호,";
		$("#u_tel").addClass("inputError");
	}
	if(!mailChk($("#u_mail").val())) {
		chkmsg += " 이메일"
		$("#u_mail").addClass("inputError");
	}
	if(chkmsg.length!=0){
		alert("점검이 필요한 항목 : " + chkmsg); return;
	}
	
	// 비밀번호가 없으면, 아이디의 값으로 입력
	if( $("#u_pw").val().length == 0 ) {
		$("#u_pw").val( $("#u_id").val() );
	}		
	
	
	//담당자 비밀번호 검사 (Ajax)
	var pw = prompt("비밀번호를 입력해주세요");
	if(pw.length==0) { return; }
	checkPassword(pw , url);
	// 이하 작업은 checkPassword(pw) 에서 진행
}
function checkPassword(pw , url) {
	$.ajax({
		url		:	"../Config/PasswordChk.hs", 
		type	:	"POST",
		data	: {'id': $('#myid').val() , 'pw': pw , 'temp': new Date() },
		dataType	:	"text",
		success	: function(data) {
			var u_type = data.trim();
			if (u_type == "A"){
				//폼 전송
				$("#sfrm").attr("action" , url);
				$("#sfrm").submit();				
			}
			else {
				alert("비밀번호가 틀리거나, 담당자가 아닙니다.");
			}
		}, 
		error	:	 function(request,status,error){
	    alert("비밀번호 확인 과정에서 에러\n" +"code:"+request.status+"\n"
	    		    +"message:"+request.responseText+"\n"+"error:"+error);
		}
	});//ajax
}	


//문자 및 특수문자 검증 제거 (전화번호 용)
function rmvMark(str){  
	 var regExp = /[a-zA-Z\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi
	 if(regExp.test(str)){
		 //특수문자 제거
		 str = str.replace(regExp, "")
	 }
	 return str;
}

//이메일 양식 검증
function mailChk(str){
	var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
	if(regExp.test(str)) {
		return true;
	} else {
		return false;
	}
}
