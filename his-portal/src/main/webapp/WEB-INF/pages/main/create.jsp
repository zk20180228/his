<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<title>在HTML中放置Flash动画图片</title>
</head>
<body topmargin="0" leftmargin="0" rightmargin="0" marginheight="0">
<center>
<br><br>
创建中......
<br><br>
<img src="<%=basePath%>themes/system/images/msg/creating.gif">
</center>
</body>
</html>
