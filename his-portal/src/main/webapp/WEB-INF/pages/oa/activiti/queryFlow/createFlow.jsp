<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>我的事务</title>
</head>
<body>
	<div id="divLayout" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'center'" style="width: 100%;height: 100%">
	    	<table id="xinjianshiwu"></table>
	    </div>
	</div>
</body>
</html>