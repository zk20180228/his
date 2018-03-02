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
<title>修改流程定义页面</title>
</head>
<body>
	<form id="demoForm" method="post" action="updateProcess.action" class="form-horizontal">
		<div class="container-fluid">
			<input id="processDefinitionId" type="hidden" name="id" value="${id}">
		    <div style="margin-top:5px;margin-bottom:5px;">
		    	<label class="control-label" style="">XML编辑</label>
			</div>
			<div>
				<textarea name="processXml" rows="20" class="form-control">${xml}</textarea>
			</div>
			<div class="input-group" style="margin-top:5px;margin-bottom:5px;">
				<button id="submitButton" type="submit" class="btn">保存</button>
				<button type="button" onclick="window.close();" class="btn">关闭</button>
		    </div>
		</div>
	</form>
</body>
</html>