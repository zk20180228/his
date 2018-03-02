<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <title>郑州大学第一附属医院-综合集成平台</title> 
    <base href="<%=basePath%>">
	<link id="easyuiTheme" rel="stylesheet" type="text/css" href="themes/easyui/<c:out value="${cookie.easyuiThemeName.value}" default="default"/>/easyui.css" ></link>
	<script type="text/javascript" src="javascript/js/jquery.min.js"></script>
	<script>
		$(function(){
			$('.his-link').mouseover(function(){
				$(".his").css('background',"url('<%=basePath%>themes/easyui/default/images/his_platform_.png') no-repeat")
			});
			$('.his-link').mouseout(function(){
				$(".his").css('background',"url('<%=basePath%>themes/easyui/default/images/his_platform.png') no-repeat")
			});
		});
	</script>
</head>
<body class="easyui-layout" style="background-color:#fff;overflow:hidden;">
	<div class="platform-main">
		<div style="position:relative;top:100px;">
			<div class="logo"></div>
		</div>
		<div style="position:relative;top:170px;">
			<div class="name"></div>
		</div>
		<div class="platform">
			<a href="<%=basePath%>mainAction!getMenu" class="his-link"><div class="his"></div></a>
		  	<a  class="pacs-link"><div class="pacs"></div></a>
			<a  class="lis-link"><div class="lis"></div></a>
			<a  class="erecord-link"><div class="erecord"></div></a>
			<a  class="shoushu-link"><div class="shoushu"></div></a>
			<a  class="yao-link"><div class="yao"></div></a>
		</div>
	</div>
	<div class="platform-bottom">
			<div class="desc"></div>
	</div> 
</body>
</html>