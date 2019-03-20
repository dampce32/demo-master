<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.commons.codec.binary.Base64"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String src = "350123198912121234";
String encode = Base64.encodeBase64String(src.getBytes());

String enStr = "MzUwMTIzMTk4OTEyMTIxMjM0";
byte[] decodeBtyes = Base64.decodeBase64(encode);
String decode = new String(decodeBtyes);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="js/jquery.base64.js"></script>
</head>
<script type="text/javascript">
	$(function(){
		$("#encodeBtn").click(function(){
			var input = $("#srcStr").val();
			var encode=$.base64.btoa(input);
		 	$("#encodeStr").val(encode);
		})
		$("#decodeBtn").click(function(){
			var input = $("#encodeStr").val();
			var decode= $.base64.atob(input);
		 	$("#decodeStr").val(decode);
		})
	})
	
</script>
<body>
	源字符串：<input type="text" id="srcStr" value="<%=decode%>"> <button id ="encodeBtn">加密</button>
	加密字符串：<input type="text" id="encodeStr" value="<%=encode%>"> <button id ="decodeBtn">解密</button>
	解密后字符串：<input type="text" id="decodeStr"> 
	
</body>
</html>