<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/baseframe/header/common/frameHead.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
	<head>
		<title>郑州大学第一附属医院-综合信息应用平台</title>
	</head>
	<body style="margin: 0px;padding: 0px;overflow: hidden;">
		<div class="header-main">
			<!-- 头部 -->
			<div class="main-top bg">
				<div class="logo"></div>
			</div>
			<div class="btnBox">
				<a id="fullscreen" title="全屏"><i class="honryicon icon-enlarge"></i></a>
				<!--<a class="newsInfobtn" href="javascript:newsInfoClick();">
				<i class="honryicon icon-icon"></i>
				<span id="newsInfoitem">10</span>
			</a>-->
				<a href="javascript:accountNum();" title="用户信息"><i class="honryicon icon-gerenzhongxin"></i></a>
				<a href="javascript:zhuti();" title="主题"><i class="honryicon icon-zhutiB"></i></a>
				<a href="javascript:changeRoleDept();" title="切换角色"><i class="honryicon icon-loop"></i></a>
				<a href="userLoginAction!loginout" class="middleElement" title="退出"><i class="honryicon icon-tuichu"></i></a>
<!-- 				<a href="javascript:restlogin();" title="其他系统入口"><i class="honryicon icon-tuichu"></i></a> -->
			</div>
			<!-- 菜单栏 -->
			<div class="chnav darkBg" style="width:100%;">
				<div id="navDiv" style="display: none;"></div>
			</div>
		</div>
		<!-- 内容体 -->
		<div id="p" style="width:100%;position: fixed;top:114px;bottom:36px;" class="mainTabds">
			<div id="tabs" class="easyui-tabs" data-options="border:false,fit:true">
				<div id="homeDiv" title="Home"></div>
				<input type='hidden' id='recordPath' value='' class="" />
			</div>
		</div>
		<!-- 鼠标右击菜单 -->
		<div id="mm" class="easyui-menu cs-tab-menu" style="width: 100px;display: none;">
			<div id="mm-tabupdate">刷新</div>
			<div class="menu-sep"></div>
			<div id="mm-tabclose">关闭</div>
			<div id="mm-tabcloseother">关闭其他</div>
			<div id="mm-tabcloseall">关闭全部</div>
		</div>
		<!-- 尾部 -->

		<div class="bg index_footer">
			<ul class="clearfix fontM">
				<li>操作人：${sessionUser.name }</li>
				<li>登录科室：${loginDepartment.deptName }</li>
				<li>当前角色：${loginRole.name }</li>
				<!--<li>在线：<span id="contimes"></span>
					<input type="hidden" id="startLoginTime" value="${loginStartTime}">
				</li>-->
			</ul>
		</div>
		<div id="selectroledept"></div>
		<div id = "restlogin"></div>
	</body>
</html>
<script type="text/javascript">
$(function(){
	window.localStorage.setItem("hisFontSize", '${sessionScope.fontSize}') //字体大小缓存
	window.localStorage.setItem("hisTheme", '${sessionScope.themes}') //主题缓存
})

</script>