<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
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
	<link rel="stylesheet" href="<%=basePath%>themes/easyui/default/easyui.css" />
	<script src="<%=basePath%>javascript/js/jquery-1.7.2.js" type="text/javascript"></script>
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
<body class="easyui-layout" style="background-color:#fff;">
		<div class="platform-main">
				<div style="position:relative;top:134px;">
					<div class="logo"></div>
				</div>
				<div style="position:relative;top:204px;">
					<div class="name"></div>
				</div>
				<div class="platform">
					<a href="login.jsp" class="his-link"><div class="his"></div></a>
				  	<a href="#" class="pacs-link"><div class="pacs"></div></a>
					<a href="#" class="lis-link"><div class="lis"></div></a>
					<a href="#" class="erecord-link"><div class="erecord"></div></a>
					<a href="#" class="shoushu-link"><div class="shoushu"></div></a>
					<a href="#" class="yao-link"><div class="yao"></div></a>
				</div>
		</div>
		<div class="platform-bottom">
				<div class="desc"></div>
		</div> 
</body>
</html>