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
<title>流程上传视图</title>
</head>
<body>
	<form id="demoForm" method="post" action="uploadProcess.action" class="form-horizontal" enctype="multipart/form-data">
		<div class="container-fluid">
			<input id="processDefinitionId" type="hidden" name="id" value="${id}">
		    <div class="pull-left pull-center" style="margin-top:5px;margin-bottom:5px;">
		    	<label class="control-label">流程定义</label>
			</div>
			<div>
				<input type="file" name="file">
			</div>
			<div class="input-group pull-center" style="margin-top:5px;margin-bottom:5px;">
				<button id="submitButton" type="submit" class="btn">保存</button>
				<button type="button" onclick="window.close();" class="btn">关闭</button>
		    </div>
		</div>
	</form>
</body>
</html>