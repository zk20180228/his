<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>${info.infoTitle}</title>
<style type="text/css">
	*{
		box-sizing: border-box;
	}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	var filenames = '${filenames}';
	var fileurls = '${fileurls}';
	$(function() {
		if (filenames != null && filenames != "") {
			var filename = filenames.split("#");
			var fileurl = fileurls.split("#");
			for (var i = 0; i < fileurl.length; i++) {
				var html = '<a href="'+fileurl[i]+'" download="'+filename[i]+'">'
						+ filename[i] + '</a></br>';
				$('#attach').append(html);
			}
		}
	});
	
</script>
</head>
<body style="overflow:hidden; padding: 10px 50px 130px 50px; background: #ffffff;">

    <div  style="text-align: center; ">
		<h1 style="line-height: 50px">${info.infoTitle }</h1>
	    <span style="line-height: 30px" align="center">发布时间:${info.time }&nbsp;&nbsp;发布人:${info.pubuserName }</span>
	</div>
	<div  class="borderColor" style="background:#F0F5FB;overflow-x: auto; overflow-y: auto;border-top: 3px solid;width: 100%;height: 100%">
		<div style="padding: 20px;">${content }</div>
		<div style="padding-left: 20px" id="attach"></div>
	</div>
</body>
</html>