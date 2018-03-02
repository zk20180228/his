<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<base src="<%=basePath%>">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/baseframe/css/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/themes/system/css/public.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/javascript/js/uploadify/uploadify.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/baseframe/css/font/easyuiFont.css">
<link id = "themelink" rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/baseframe/css/themes/theme/theme${sessionScope.themes}.css"/>
<link id = "fontlink" rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/baseframe/css/themes/font/fontSize${sessionScope.fontSize}.css"/>


<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/jquery.edatagrid.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/jquery.json-2.3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/publicUtils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/hisUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/public.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/validator.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/json2.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/keydown.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/97DP/WdatePicker.js"></script>
