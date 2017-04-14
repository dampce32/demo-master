<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>
	<title>第一个JSF程序</title>
</head>
<body>
	<f:view>
		<h:form>
			<h3>请输入您的名称</h3>
           	名称: <h:inputText value="#{user.name}" />
			<h:commandButton value="送出" action="login" />
		</h:form>
	</f:view>
</body>
</html>
