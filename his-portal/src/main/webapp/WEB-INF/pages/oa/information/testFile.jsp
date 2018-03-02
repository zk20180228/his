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
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/javascript/js/webuploader/dist/webuploader.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/webuploader/dist/webuploader.js"></script>
<script type="text/javascript">
	$(function(){
		$("#uploadify").uploadify({
	        'uploader': '${pageContext.request.contextPath}/javascript/js/uploadify/uploadify.swf',
	        'script': '<%=basePath%>oa/information/upload.action',
	        'cancelImg': 'JS/cancel.png',
	        'folder': 'UploadFile',
	        'queueID': 'fileQueue',
	        'auto': true,
	        'multi': false
	    });
	});
</script>
</head>
<body>
<div id="fileQueue"></div>
<input type="file" name="uploadify" id="uploadify" />
<p>
    <a onclick="uploadfile()">上传</a>| 
    <a href="javascript:$('#uploadify').uploadifyClearQueue()">取消上传</a>
</p>
<script type="text/javascript">
function uploadfile(){
	$('#uploadify').uploadify('upload');
}
</script>
</body>
</html>