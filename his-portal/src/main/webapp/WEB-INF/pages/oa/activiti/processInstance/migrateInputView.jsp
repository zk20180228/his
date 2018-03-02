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
<title>流程实例迁移视图</title>
</head>
<body>
<form id="demoForm" method="post" action="activiti/processInstance/migrateSave.action" class="form-horizontal">
  <input id="demo_id" type="hidden" name="id" value="${id}">
  <div class="control-group" align="center">
    <label class="control-label">流程定义</label>
	<div class="controls" >
	  <select name="processDefinitionId">
	    <c:forEach items="${result}" var="item">
	    <option value="${item.id}">${item.id}</option>
		</c:forEach>
	  </select>
    </div>
  </div>
  <div class="control-group" align="center" style="padding-top: 10px">
    <div class="controls">
      <button id="submitButton" type="button" onclick="submitFrom()" class="btn">保存</button>
	  &nbsp;
      <button type="button" onclick="closeDialogs()" class="btn">关闭</button>
    </div>
  </div>
</form>
<script type="text/javascript">

	function submitFrom() {
		$('#demoForm').form('submit', {
			url : "<c:url value='/activiti/processInstance/migrateSave.action'/>",
			success : function(data) {
				data = eval("("+data+")");
				if (data.resMsg == 'success') {
	 				window.opener.location.reload();
// 					$('#demoGrid').datagrid('reload');
					$.messager.alert('提示',"保存成功",'info',function(){
						closeDialogs();
					});
				} else {
					$.messager.alert('提示',data.resCode,'info',function(){
						closeDialogs();
					});
				}
			},
			error : function(data) {
				$.messager.alert('提示',"保存失败",'info',function(){
					closeDialogs();
				});
			}
		});

	}
	function closeDialogs(){
		window.close();
	}
</script>
</body>
</html>