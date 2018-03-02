<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>${info.infoTitle}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body style="overflow-x:auto;overflow-y:auto;">
<div style="padding: 20px;">
${content }
</div>
</body>
</html>