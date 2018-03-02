<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<jsp:include page="/baseframe/header/easyui/easyuiBase.jsp"></jsp:include>
<link id = "themelink" rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/baseframe/css/themes/theme/theme3.css"/>
<link id = "fontlink" rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/baseframe/css/themes/font/fontSize2.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/baseframe/css/font/easyuiFont.css">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	<title>郑州大学第一附属医院-综合信息应用平台</title>
	<link rel="stylesheet" href="<%=basePath%>themes/manage/css/login.css" />
	<link rel="stylesheet" href="<%=basePath%>themes/manage/css/alertwindow.css" />
	<!--[if lte IE 8]><script src="<%=basePath%>javascript/js/selectivizr.js" type="text/javascript" charset="utf-8"></script><![endif]-->
	<!--[if lt IE 9]><script src="<%=basePath%>javascript/js/html5shiv-printshiv.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<%-- 	<script src="<%=basePath%>javascript/js/jquery-1.7.2.js" type="text/javascript"></script> --%>
	<script src="<%=basePath%>javascript/js/jquery.cookie.js" type="text/javascript"></script>
	<script src="<%=basePath%>javascript/js/alertwindow.js" type="text/javascript"></script>
	<jsp:include page="/baseframe/header/bootstrap/bootstrapBase.jsp"></jsp:include>
	<jsp:include page="/baseframe/header/common/honryFont.jsp"></jsp:include>
	<style type="text/css">
		table.honry-table td {
			border-right: 1px solid #a7d9d4;
			border-bottom: 1px solid #a7d9d4
		}
		
		#auto_div {
			display: none;
			width: 284px;
			border: 1px #74c0f9 solid;
			background: #FFF;
			position: absolute;
			color: #323232;
		}
		
		.platform-main .logo {
			margin: 0 auto;
			width: 800px;
			height: 110px;
			margin-top: 8%;
			background: url('images/platform_logo.png') no-repeat;
			clear: both;
			background: url('themes/manage/css/images/platform_logo.png') no-repeat;
		}
		
		.platform-main .platform-bottom {
			width: 100%;
			height: 39px;
		}
		
		.alert-box {
			width: 400px;
			height: 200px;
			position: fixed;
			top: 50%;
			left: 50%;
			margin-left: -200px;
			margin-top: -100px;
			z-index: 100;
			line-height: 30px;
			padding: 0 10px;
			background-color: rgba(255, 255, 255, .6);
			filter: progid:DXImageTransform.Microsoft.Gradient(startColorstr=#aaffffff,endColorstr=#aaffffff);
		}
		
		.alert-title {
			line-height: 55px;
			margin-bottom: 10px;
			text-indent: 20px;
		}
		
		.alert-content {
			line-height: 30px;
		}
		table.honry-table td .datagrid-header-row td{
			border-top:0px;
			border-left:0px;
		}
		.datagrid-btable{
			border-collapse: collapse;
		}
		.datagrid-btable tr td:first-child{
			border-left:0;
		}
		.submitInput {
			line-height: 48px;
			height: 48px;
			font-family: "微软雅黑";
			font-size: 17px;
			padding: 0;
			margin-bottom: 22px;
			text-indent: 18px;
			background-color: #EAEAEA;
			border: 1px solid #d1d1d1;
			border-radius: 4px;
			display: block;
		}
		.submitBtn {
			width: 360px;
			line-height: 48px;
			font-family: "微软雅黑";
			font-size: 17px;
			padding: 0;
			text-align: center;
			border-radius: 4px;
			display: block;
			background-color: #017571;
			color: #FFFFFF;
		}
		#codebtn {
			text-align: center;
			width: 130px;
			margin-left: 10px;
			font-size: 16px;
			color: #FFFFFF;
			font-family: "微软雅黑";
			line-height: 48px;
			height: 48px;
			background-color: #017571;
			margin-bottom: 10px;
			display: block;
		}
		.subClear {
			color: #017570;
			background-color: #FFFFFF;
			position: absolute;
		    top: -16px;
		    right: -16px;
		    width: 32px;
		    height: 32px;
		    line-height: 32px;
		    text-align: center;
		    border-radius: 16px;
		}
		.subClear i {
			font-size: 23px;
		}
		.infoTitleBox {
			width: 100%;
			height: 0;
			line-height: 14px;
			font-size: 14px;
			overflow: hidden;
			transition: all 0.3s;
			float: left;
		}
		#infoTitleImg {
			width: 14px;
			height: 14px;
		    margin: 0;
		    margin-right:6px;
    		padding: 0;
    		vertical-align: bottom;
    		float: left;
    		display: block;
		}
		#authcode {
			margin-bottom: 10px;
		}
		.infoTitle {
			margin-bottom: 24px;
		}
		#infoTitleInof {
			color: 7d9d9a;
		}
	</style>
	<script src="<%=basePath%>javascript/js/login.js?v1.2" type="text/javascript"></script>
</head>

<body>
	<div class="main">
		<div class="main-mid" id="main-mid">
			<div class="logo" id="index-logo"><img src="<%=basePath%>themes/manage/css/images/platform_logo.png" title="logo" alt="logo" /></div>
			<div class="content" id="index-content">
				<div class="content_left" id="content_left"><img id="index-left-img" src="<%=basePath%>themes/manage/css/images/login_left.png" /></div>
				<div class="content_right">
					<div class="form" id="from">
						<div class="title">
							<div class="titlebg">
								<h2 id="h2Title">综合信息应用平台（HIAS）</h2>
								<h3>HOSPITAL INFORMATION APPLICATION SYSTEM</h3>
							</div>
						</div>
						<div class="login">
							<ul>
								<li class="username addshadow">
									<span>账号</span>
									<input type="text" style="display:none" name="txtAccount" />
									<!-- 防止谷歌自动填充用户名密码 -->
									<input type="text" id="txtAccount" placeholder="请输入用户名">
								</li>
								<li class="password addshadow">
									<span>密码</span>
									<input type="password" id="txtPassword" name="txtPassword" placeholder="请输入密码">
								</li>
								<li class="role addshadow slide">
									<span>角色</span>
									<input id="selectRole" readonly="readonly" type="text" name="" />
									<i></i>
									<div id="roleDiv" style="display: none;overflow: auto;">
									</div>
								</li>
								<li class="dept addshadow slide">
									<span>部门</span>
									<input id="selectDept" readonly="readonly" type="text" name="" />
									<i></i>
									<div id="deptDiv" style="display: none;overflow: auto;">
									</div>
								</li>
								<li class="remember">
									<input id="checkId" type="checkbox" name="rememberme">
									<span style="line-height: 20px !important;">记住我</span>
								</li>
								<li class="loginButton"><input type="submit" id="submitBtn" name="" value="登录" /></li>
						</div>
					</div>
				</div>
			</div>
			<div class="bottom_text" id="index-bottom_text"><img src="<%=basePath%>themes/manage/css/images/platform_bottom_desc.png" alt="技术支持：北京中科弘睿科技有限公司" />
			</div>
		</div>
	</div>
	<!-- 模态框 -->
	<form>
		<div class="modal fade" id="ManageInfoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="width: 480px;height: 290px;">
				<div class="modal-content">
					<div class="modal-body" style="padding: 47px 58px;">
						<input id="mobile" class="form-control submitInput"  placeholder="手机号为空" style="width: 360px;" />
						<div class="clearfix">
							<input id="authcode" class="form-control submitInput"  placeholder="输入验证码" style="width: 220px;float: left;background-color: #fff" />
							<a id="codebtn" href="javascript:createAuthCode();void(0)" style="width: 130px;float: left;border-radius: 4px;" >获取验证码</a>
						</div>
						<div class="clearfix infoTitle">
							<div class = "infoTitleBox">
								<img id  = "infoTitleImg" src="<%=basePath%>themes/manage/css/images/shibai.png" />
								<span id = "infoTitleInof"></span>	
							</div>
						</div>
						<a id="winBtn" href="javascript:checkAuthCode();void(0)" class="submitBtn" style="width: 360px;">确定</a>
						<a class="subClear" data-dismiss="modal"><i class="honryicon icon-shanchu"></i></a>
					</div>
				</div>
			</div>
		</div>
	 </form>
</body>

</html>