<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@page contentType="text/html;charset=Big5"%>
<html>
<head>
	<title>第一个JSF程序</title>
</head>
<body>
	<f:view>
		<h:outputText value="#{user.name}" /> 您好！
        <h3>欢迎使用 JavaServer Faces！</h3>
	</f:view>
</body>
</html>
