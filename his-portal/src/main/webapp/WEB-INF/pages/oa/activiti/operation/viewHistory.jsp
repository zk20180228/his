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
<title>未结流程详情</title>
<style type="text/css">
	body,html {
		width: 100%;
		height: 100%;
		overflow:"hidden"; 
	}
	#processGraphMask {
		display: none;
	}
	#processGraphWrapper img{
		/*width: 100%;*/
	}
</style>
</head>
<body>
<section id="m-main">
<div class="panel panel-default">
  <table id="demoGrid" class="table table-hover">
    <thead>
      <tr>
	    <th style="width: 15%"  name="name">环节</th>
        <th style="width: 16%" name="startTime">开始时间</th>
        <th style="width: 16%" name="endTime">结束时间</th>
        <th style="width: 20%" name="assignee">负责人</th>
        <th style="width: 18%" name="lastModifierName">审批人</th>
        <th style="width: 15%" >状态</th>
	  </tr>
    </thead>

    <c:forEach items="${humanTasks}" var="item">
    <tbody>
      <tr>
	    
	    <td>${item.name}</td>
	    <td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	    <td><fmt:formatDate value="${item.completeTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	    <td>
		  ${item.assigneeName}
		  <c:if test="${not empty item.owner && item.assignee != item.owner}">
<%-- 		  <b>(原执行人:<tags:user userId="${item.owner}"/>)</b> --%>
		  </c:if>
		</td>
	    <td>${item.lastModifierName}</td>
		<td>${item.action}</td>
      </tr>
	  <c:forEach items="${item.children}" var="child">
      <tr>
	    <td class="active text-muted">&nbsp;*&nbsp;${child.name}</td>
	    <td class="active text-muted"><fmt:formatDate value="${child.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	    <td class="active text-muted"><fmt:formatDate value="${child.completeTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	    <td class="active text-muted">
		  <tags:user userId="${child.assignee}"/>
		  <c:if test="${not empty child.owner && child.assignee != child.owner}">
<%-- 		  <b>(原执行人:<tags:user userId="${child.owner}"/>)</b> --%>
		  </c:if>
		</td>
		<td class="active text-muted">
		  ${item.lastModifierName}
		</td>
	    <td class="active text-muted">
		  <c:if test="${child.catalog == 'communicate'}">
		    沟通
		  </c:if>
		  <c:if test="${child.catalog == 'copy'}">
		    抄送
		  </c:if>
		  <c:if test="${child.catalog == 'vote'}">
		    加签
		  </c:if>
		  (${child.action})
		</td>
	    <td class="active text-muted">
		  ${item.lastModifierName}
		</td>
      </tr>
	    <c:forEach items="${child.children}" var="third">
      <tr>
	    <td class="active text-muted">&nbsp;**&nbsp;${third.name}</td>
	    <td class="active text-muted"><fmt:formatDate value="${third.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	    <td class="active text-muted"><fmt:formatDate value="${third.completeTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	    <td class="active text-muted">
		  <tags:user userId="${third.assignee}"/>
		  <c:if test="${not empty third.owner && third.assignee != third.owner}">
<%-- 		  <b>(原执行人:<tags:user userId="${third.owner}"/>)</b> --%>
		  </c:if>
		</td>
		<td class="active text-muted">
		  ${item.lastModifierName}
		</td>
	    <td class="active text-muted">
		  <c:if test="${third.catalog == 'vote'}">
		    加签
		  </c:if>
		  (${third.action})
		</td>
      </tr>
	    </c:forEach>
	  </c:forEach>
    </tbody>
    </c:forEach>
  </table>
      </div>
      <div class="panel panel-default">
        <div class="panel-heading">
		<div class="panel-body">
        <div id="processGraphWrapper" class="content">

		  <img id = "processGraphWrapperImg" src="activiti/operation/graphHistoryProcessInstance.action?id=${processInstanceId}">

		  <div id="processGraphMask" style="position:absolute;">
		    <c:forEach items="${nodeVos}" var="item">
		    <div style="position:absolute;left:${item.x}px;top:${item.y}px;width:${item.width}px;height:${item.height}px;" data-container="body" data-trigger="hover" data-toggle="popover" data-placement="bottom" data-html="true" data-content="<table><tr><td>节点类型:</td><td>${item.type}</td></tr><tr><td>节点名称:</td><td>${item.name}</td></tr><tr></table>"></div>
			</c:forEach>
		  </div>

		</div>
		</div>
      </div>



    </section>
</body>
</html>
<script type="text/javascript">
	$("#processGraphWrapperImg").on("load",function(){
		var bodyW = $("body").width()
		var imgW = $("#processGraphWrapperImg").width()
		if(bodyW < imgW ){
			$("#processGraphWrapperImg").css("width","100%")
		}
	})
</script>