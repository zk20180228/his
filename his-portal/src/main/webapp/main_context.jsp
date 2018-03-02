<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>郑州大学第一附属医院-综合信息应用平台</title>

<%-- <link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/index.css?v=1.0"/> --%>
<jsp:include page="/common/head_content.jsp"></jsp:include>
<%-- <link id="easyuiTheme" rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/themes/easyui/${sessionScope.themes}/public.css?v=1.0" ></link> --%>
<%-- <link id="changeFontSize" rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/themes/font/${sessionScope.fontSize}.css?v=1.0" ></link> --%>
<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Bootstrapv3.3.7/css/bootstrap.css" ></link> --%>
<style type="text/css">
	*{
	 box-sizing: border-box;
	}
	body{
		background-color: #EFF4F5;
	}
	a:HOVER{
		text-decoration: none;
	}
	.main{
		padding-left: 26px;
		padding-right: 26px;
		margin-top: 30px
	}
	li{
		list-style: none;
	}
	ul{
		padding: 0;
		margin: 0;
	}
	.main>div{
		padding-left: 12px;
		padding-right: 12px;
	}
	.main .header {
		margin-bottom: 1px;
		background-color: #ffffff;
		height: 66px;
	}
	
	.main .notice .header{
		border-top: 5px solid #45BF89;
	}
	.main .news .header{
		border-top: 5px solid #F0A753;
	}
	.main .hot .header{
		border-top: 5px solid #61A8E9;
	}
	.main .video .header{
		border-top: 5px solid #F96868;
	}
	.main .header .more a {
		padding:9px 12px;
		border-radius:5px;
		overflow: hidden;
		font-size: 14px;
		color: #ffffff;
	}
	.main .notice .header .more a{
		background-color:#45BF89; 
	}
	.main .news .header .more a{
		background-color:#F0A753; 
	}
	.main .hot .header .more a{
		background-color:#61A8E9; 
	}
	.main .video .header .more a{
		background-color:#F96868; 
	}
	
	
	.main>div{
		margin-bottom: 24px
	}
	.main .header .head-title {
		margin-left: 33px;
		font-size: 16px;
		font-family: "微软雅黑";
		float: left;
		line-height: 60px;
	}
	.main .header .more{
		float: right;
		line-height: 60px;
		margin-right: 32px;
	}
	.main .content{
		height: 260px;
		background-color: #ffffff;
		padding-top: 10px;
	}
	.main .content li{
		height: 40px;
		line-height: 40px;
		
	}
	.main .content li a {
		color: #74848E;		
		display: block;
		height: 100%;
		height: 100%;
		position: relative;
	}
	.main .content li a .left {
		float:left;
		width: 100%;
		padding-left: 33px;
		padding-right:150px;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;	
	}
	.main .content li a .right{
		position:absolute;
		right: 35px;
		width: 115px;
		text-align: right;
	}
	.main .shadow{
		box-shadow :#cccccc 2px 7px 20px 0px;
	}
	
</style>
</head>
<body style="margin: 0px;padding: 0px;">
			<div class="main container-fluid">
<%-- 				<div class="setting">
					<div class="header">
						<div class="head-img"></div>
						<div class="head-title"><span>个人定制快捷功能</span></div>
						<div style="float: right; margin:4px 5px 0 0;">
							<a href="javascript:open('<%=basePath%>sys/shortcut/shortcutList.action?menuAlias=DZKJGN','个人定制快捷功能');"><img src="${pageContext.request.contextPath}/themes/easyui/icons/2012080404391.png" /></a>
						</div>
					</div>
					<div class="shortcut">
						<ul>
								<c:forEach var="info" items="${menuList}" begin="0" end="5" varStatus="obj">
										<li><a  href="javascript:open('<%=basePath%>${info.menufunction.mfAction }?menuAlias=${info.alias}','${info.name}');"><c:out value="${info.name}"/></a></li>
								</c:forEach>
						</ul>
					</div>
				</div> --%>

					<div class="notice col-md-6 col-sm-12">
					<div class = "shadow">
						<div class="header">
							<div class="head-title"><span>通知公告</span></div>
							<div class = "more">
								<a href="javascript:open('<%=basePath%>sys/noticeManage/viewList.action?info.infoPubflag=1','通知公告信息');" class="main-more">
								<span>更多</span></a>
							</div>
						</div>
						<div class="content">
							<ul>
								<c:choose>
									<c:when test="${noticeList.size() > 6 }">
										<c:forEach var="info" items="${noticeList}" begin="0" end="5" varStatus="obj">
											<li <c:if test="${obj.count%2 == 0}">
											  class="even"
											</c:if>>
												<a href="javascript:open('<%=basePath%>sys/noticeManage/contentView.action?info.id=${info.id}','信息浏览');">
													<c:choose>
														 <c:when test="${info.infoTitle.length() > 45}">
														 	<c:out value="${info.infoTitle.substring(0, 45)}..."/>
														 </c:when>
														 <c:otherwise>
														 	<c:out value="${info.infoTitle}"/>
														 </c:otherwise>
													</c:choose>
													<span><fmt:formatDate value='${info.infoPubtime}' pattern='yyyy-MM-dd' /></span>
												</a>
											</li>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<c:forEach var="info" items="${noticeList}" begin="0" end="6" varStatus="obj">
											<li <c:if test="${obj.count%2 == 0}">
											  class="even"
											</c:if>>
												<a href="javascript:open('<%=basePath%>sys/noticeManage/contentView.action?info.id=${info.id}','信息浏览');">
													<span class = "left"><c:choose>
														 <c:when test="${info.infoTitle.length() > 45}">
														 	<c:out value="${info.infoTitle.substring(0, 45)}..."/>
														 </c:when>
														 <c:otherwise>
														 	<c:out value="${info.infoTitle}"/>
														 </c:otherwise>
													</c:choose></span>
													<span class = "right"><fmt:formatDate value='${info.infoPubtime}' pattern='yyyy-MM-dd' /></span>
												</a>
											</li>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</ul>
						</div>
					</div>
					</div>
					<div class="news col-md-6 col-sm-12">
					<div class = "shadow">
						<div class="header">
							<div class="head-title"><span>院内新闻</span></div>
							<div class = "more">
											<a href="javascript:open('<%=basePath%>sys/noticeManage/viewList.action?info.infoPubflag=1&info.infoType=3','新闻信息');" class="main-more">
											<span>更多</span></a>
							</div>
						</div>
						<div class="content">
							<ul>
								<c:choose>
									<c:when test="${newsList.size() > 6 }">
										<c:forEach var="info" items="${newsList}" begin="0" end="5" varStatus="obj">
											<li <c:if test="${obj.count%2 == 0}">
											  class="even"
											</c:if>>
												<a href="javascript:open('<%=basePath%>sys/noticeManage/contentView.action?info.id=${info.id}','信息浏览');">
													<span class = "left"><c:out value="${info.infoTitle}"/></span>
													<span class = "right"><fmt:formatDate value='${info.infoPubtime}' pattern='yyyy-MM-dd' /></span>
												</a>
											</li>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<c:forEach var="info" items="${newsList}" begin="0" end="6" varStatus="obj">
											<li <c:if test="${obj.count%2 == 0}">
											  class="even"
											</c:if>>
												<a href="javascript:open('<%=basePath%>sys/noticeManage/contentView.action?info.id=${info.id}','信息浏览');">
													<span class = "left"><c:out value="${info.infoTitle}"/></span>
													<span class = "right"><fmt:formatDate value='${info.infoPubtime}' pattern='yyyy-MM-dd' /></span>
												</a>
											</li>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							
							</ul>
						</div>
						</div>
					</div>
					<div class="hot col-md-6 col-sm-12">
					<div class = "shadow">
						<div class="header">
							<div class="head-title"><span>医疗前沿</span></div>
							<div class = "more">
											<a href="javascript:open('<%=basePath%>sys/noticeManage/viewList.action?info.infoPubflag=1&info.infoType=4','医疗前沿');" class="main-more">
											<span>更多</span></a>
							</div>
						</div>
						<div class="content">
							<ul>
								<c:choose>
									<c:when test="${medicalList.size() > 6 }">
										<c:forEach var="info" items="${medicalList}" begin="0" end="5" varStatus="obj">
											<li <c:if test="${obj.count%2 == 0}">
											  class="even"
											</c:if>>
												<a href="javascript:open('<%=basePath%>sys/noticeManage/contentView.action?info.id=${info.id}','信息浏览');">
													<span class = "left"><c:out value="${info.infoTitle}"/></span>
													<span class = "right"><fmt:formatDate value='${info.infoPubtime}' pattern='yyyy-MM-dd' /></span>
												</a>
											</li>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<c:forEach var="info" items="${medicalList}" begin="0" end="6" varStatus="obj">
											<li <c:if test="${obj.count%2 == 0}">
											  class="even"
											</c:if>>
												<a href="javascript:open('<%=basePath%>sys/noticeManage/contentView.action?info.id=${info.id}','信息浏览');">
													<span class = "left"><c:out value="${info.infoTitle}"/></span>
													<span class = "right"><fmt:formatDate value='${info.infoPubtime}' pattern='yyyy-MM-dd' /></span>
												</a>
											</li>
										</c:forEach>
									</c:otherwise>
								</c:choose>
								
							</ul>
						</div>
						</div>
					</div>
					<div class="video col-md-6 col-sm-12">
					<div class = "shadow">
						<div class="header">
							<div class="head-title"><span>信息提醒</span></div>
							<div class = "more">
								<a href="javascript:open('<%=basePath%>sys/noticeManage/viewList.action?info.infoPubflag=1&info.infoType=5','信息提醒');" class="main-more">
								<span>更多</span></a>
							</div>
						</div>
						<div class="content">
							<ul>
								<c:choose>
									<c:when test="${nursing.size() > 6 }">
										<c:forEach var="info" items="${nursing}" begin="0" end="5" varStatus="obj">
											<li <c:if test="${obj.count%2 == 0}">
											  class="even"
											</c:if>>
												<a href="javascript:open('<%=basePath%>sys/noticeManage/contentView.action?info.id=${info.id}','信息浏览');">
													<span class = "left"><c:out value="${info.infoTitle}"/></span>
													<span class = "right"><fmt:formatDate value='${info.infoPubtime}' pattern='yyyy-MM-dd' /></span>
												</a>
											</li>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<c:forEach var="info" items="${nursing}" begin="0" end="6" varStatus="obj">
											<li <c:if test="${obj.count%2 == 0}">
											  class="even"
											</c:if>>
												<a href="javascript:open('<%=basePath%>sys/noticeManage/contentView.action?info.id=${info.id}','信息浏览');">
													<span class = "left"><c:out value="${info.infoTitle}"/></span>
													<span class = "right"><fmt:formatDate value='${info.infoPubtime}' pattern='yyyy-MM-dd' /></span>
												</a>
											</li>
										</c:forEach>
									</c:otherwise>
								</c:choose>
								
							</ul>
						</div>
					</div>
					</div>
			</div>
	
<script type="text/javascript">

function open(url,title){
	parent.closableTab.addTab({
		'id':'IndexneesInfo',
		'name':title,
		'url':url,
		'closable':true,
		"newsInfo":true,
});
	
// 	if (window.parent.$('#tabs').tabs('exists',title)){
// 		window.parent.$('#tabs').tabs('select', title);//选中并刷新
// 		var currTab = window.parent.$('#tabs').tabs('getSelected');
// 		if (url != undefined) {
// 			window.parent.$('#tabs').tabs('update', {
// 				tab : currTab,
// 				options : {
// 					content : createFrame(url)
// 				}
// 			});
// 		}
// 	} else {
// // 		var conopentent = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
// 		window.parent.$('#tabs').tabs('add',{
// 			title : title,
// 			content:createFrame(url),
// 			closable:true
// 		});
// // 		window.parent.tabClose();
// 	}
}

</script>
</body>
</html>
