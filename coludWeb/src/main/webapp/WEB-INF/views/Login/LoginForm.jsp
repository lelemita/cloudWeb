<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>로그인 : 하솜 정보기술 클라우드 시스템</title>
	<!-- Bootstrap Core CSS -->
    <link href="../resources/bootstrap/sb-admin-2/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <!-- MetisMenu CSS -->
    <link href="../resources/bootstrap/sb-admin-2/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="../resources/bootstrap/sb-admin-2/dist/css/sb-admin-2.css" rel="stylesheet">
    <!-- Morris Charts CSS -->
    <link href="../resources/bootstrap/sb-admin-2/vendor/morrisjs/morris.css" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="../resources/bootstrap/sb-admin-2/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	
</head>
<body>

    <div class="container">
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">초기 ID와 암호는 담당자에게 문의하세요</h3>
                    </div>
                    <div class="panel-body">
                        <form method="post" id="lfrm" action="../Login/LoginProc.hs">
                            <fieldset>
                                <div class="form-group">
                                    <input class="form-control" placeholder="아이디" id="u_id" name="u_id" type="text" autofocus>
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="비밀번호" id="u_pw" name="u_pw" type="password" value="">
                                </div>
                               
                                <!-- Change this to a button or input when using this as a form -->
                                <input type="button" onclick="login()" class="btn btn-lg btn-success btn-block" value="Login">
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap Core JavaScript -->
    <script src="../resources/bootstrap/sb-admin-2/vendor/bootstrap/js/bootstrap.min.js"></script>
    <!-- jQuery -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
    <script>
	 // LoginForm 에서 로그인 할때
	    function login() {
	    	var id = $("#u_id").val();
	    	var pw = $("#u_pw").val();
	    	if ( id.length == 0 ) {
	    		alert("아이디를 입력해주세요");
	    		return;
	    	} 
	    	if ( pw.length == 0 ) {
	    		alert("비밀번호를 입력해주세요");
	    		return;
	    	} 
	    	$("#lfrm").submit();
	    }
    </script>
</body>
</html>