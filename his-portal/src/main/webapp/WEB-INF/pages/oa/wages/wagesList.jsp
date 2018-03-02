<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>工资管理</title>
<%@ include file="/common/metas.jsp"%>
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<style type="text/css">
		.panel-body,.panel{
			overflow:visible;
		}
</style>
<script type="text/javascript">
	$(function(){
		$('#tDt').tree({
			 url:'<%=basePath%>oa/Wages/queryColumns.action',
			 onClick:function(node){
				 var noid=node.id; 
				 if(noid=='empolyee'){
				    var url="<%=basePath%>oa/Wages/toEmployee.action";
					if(url != undefined){
						if($("#tabs").tabs("exists","员工自助查询")){
							$("#tabs").tabs("select","员工自助查询");
						}else{
							var content ='<iframe scrolling="auto"  frameborder="0"  src="'+url+'"  style="width:100%;height:100%;"></iframe>'
							window.$("#tabs").tabs("add",{
								title:"员工自助查询",
								content:content,
								closable:true
							});
						}
					 }
				 }else if(noid=='personnel'){
					 var url="<%=basePath%>oa/Wages/wagesManageInfo.action";
					 if(url != undefined){
						if($("#tabs").tabs("exists","人事工资管理")){
							$("#tabs").tabs("select","人事工资管理");
						}else{
							window.$("#tabs").tabs("add",{
								title:"人事工资管理",
								content:'<iframe scrolling="auto"  frameborder="0"  src="'+url+'"  style="width:100%;height:100%;"></iframe>',
								closable:true
							});
						}
					 }	
			 	 }
						
		 	}	
		});
	});
	
</script>
</head>
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" fit="true">
		 <div data-options="region:'west',title:'工资管理'" style="width:200px;">
		 	 <div id="treeDiv" data-options="region:'center',border:false" style="">
		    	<ul id="tDt">数据加载中...</ul>
		    </div>  
		 </div>  
		 <div data-options="region:'center'">
			 <div id="tabs" class="easyui-tabs" style="width: 100%; height: 100%;" data-options="border:false,tabHeight:'24',fit:true">
			 </div>	 
		 </div>  
	</div>
</body>
</html>
