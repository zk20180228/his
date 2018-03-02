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
<title>发起流程视图</title>
</head>
<body>

<section id="m-main" class="col-md-12" style="padding-top:65px;">

      <div class="panel panel-default">
        <div class="panel-heading">
		  确认发起流程
		</div>

		<div class="panel-body">
<form id="demoForm" method="post" action="startProcessInstance.action" class="form-horizontal">
  <input id="demo_id" type="hidden" name="id" value="${bpmProcessId}">
  <input type="hidden" name="businessKey" value="${businessKey}">
  <div class="control-group">
    <div class="controls">
      <button id="submitButton" type="button" class="btn btn-default" onclick="startProcessInstance()">发起流程</button>
	  &nbsp;
      <button type="button" onclick="history.back();" class="btn btn-link">返回</button>
    </div>
  </div>
</form>

		</div>
	  </div>

    </section>
</body>
<script type="text/javascript">
function startProcessInstance(){
	var bpmProcessId=$('#demo_id').val();
	$.ajax({
		url:'<%=basePath%>activiti/operation/startProcessInstance.action',
		type:'post',
		dataType:'json',
		data:{'id':bpmProcessId},
		success:function(value){
			if(value.resMsg=='success'){
				console.log(value.resCode);
// 				var humanTaskId=value.humanTaskId;
<%-- 				var uri='<%=basePath%>activiti/humanTask/viewTaskForm.action?humanTaskId='+humanTaskId; --%>
// 				replaceNav(uri,'请假表单');
			}
		}
	});
}
</script>
</html>