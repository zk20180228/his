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
<title>流程列表视图</title>
</head>
<body>
	<section id="m-main">
		<c:forEach items="${bpmCategories}" var="bpmCategory">
			<div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<i class="glyphicon glyphicon-list"></i>
							${bpmCategory.name}
						</h3>
					</div>
					<div class="panel-body">
						<c:forEach items="${bpmCategory.bpmProcesses}" var="bpmProcess">
							<div class="col-md-2">
								<div class="caption">
									<h3>${bpmProcess.name}&nbsp;</h3>
									<p>${bpmProcess.descn}&nbsp;</p>
									<div class="btn-group" style="margin-bottom:10px;">
										<a class="btn btn-default btn-sm" href="../operation/viewStartForm.action?id=${bpmProcess.id}"><i class="glyphicon glyphicon-play"></i> 发起</a>
										<a class="btn btn-default btn-sm" href="javascript:void(0)" onclick="graphProcessDefinition('${bpmProcess.id}')" target="_blank"><i class="glyphicon glyphicon-picture"></i> 图形</a>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</c:forEach>
	</section>
	<script type="text/javascript">
	function graphProcessDefinition(id){
		attWindow(id,'<%=basePath%>activiti/center/graphProcessDefinition.action?v='+Math.random());
	}
	
	//以post方式打开窗口
	function attWindow(id,url){
		var id = id;
		var url = url;
		var name = '查看';
		var width = 1350;
		var height = 700;
		var top = (window.screen.availHeight-30-height)/2;
		var left = (window.screen.availWidth-10-width)/2;
		if($("#winOpenFrom").length<=0){  
			var form = "<form id='winOpenFrom' action='"+url+"' method='post' target='"+name+"'>" +
					"<input type='hidden' id='winOpenFromInpId' name='id'/></form>";
			$("body").append(form);
		} 
		$('#winOpenFromInpId').val(id);
		openWindow('about:blank',name,width,height,top,left);
		$('#winOpenFrom').prop('action',url);
		$("#winOpenFrom").submit();
	}
	
	//打开窗口
	function openWindow(url,name,width,height,top,left){
		window.open(url, name, 'height=' + height + ',innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no'); 
	}
	</script>
</body>
</html> 