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
<title>配置任务负责人视图</title>
</head>
<body>
 <section id="m-main" class="col-md-12" style="padding-top:65px;">

      <div class="panel panel-default">
        <div class="panel-heading">
		  编辑
		</div>

		<div class="panel-body">

<form id="demoForm" method="post" action="confirmStartProcess.action" class="form-horizontal">
  <input id="demo_bpmProcessId" type="hidden" name="id" value="${bpmProcessId}">
  <input id="demo_businessKey" type="hidden" name="businessKey" value="${businessKey}">
  <input id="demo_status" type="hidden" name="status" value="taskConf">
  <c:if test="${taskDefinitions != null}">
  <table class="table table-border">
    <thead>
	  <tr>
	    <td>任务</td>
	    <td>负责人</td>
	  </tr>
	</thead>
	<tbody>
  <c:forEach items="${taskDefinitions}" var="item">
      <tr>
	    <td><input type="hidden" name="taskDefinitionKeys" value="${item.key}">${item.name}</td>
	    <td>
		    <div class="input-group userPicker" style="width: 175px;">
			  <input id="_task_name_key" type="hidden" name="value" class="input-medium" value="${item.assignee}">
			  <input type="text" name="taskAssignees" style="width: 175px;background-color:white;" value="<tags:user userId='${item.assignee}'/>" class="form-control" readonly>
			  <div class="input-group-addon"><i class="glyphicon glyphicon-user"></i></div>
		    </div>

		</td>
	  </tr>
  </c:forEach>
    </tbody>
  </table>
  </c:if>
  <div class="control-group">
    <div class="controls">
      <button id="submitButton" type="submit" class="btn btn-default">保存</button>
	  &nbsp;
      <button type="button" onclick="history.back();" class="btn btn-link">返回</button>
    </div>
  </div>
</form>
        </div>
      </div>

      </section>

</body>
</html>