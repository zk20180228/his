<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<title>统计大类面板</title>
</head>
<body>
<div class="easyui-panel"  id="panelEast"  data-options="title:'统计大类',iconCls:'icon-form'">
<div class="easyui-panel" data-options="region:'west',split:false" style="width:45%;height:100%">
			统计类型:<input id="statType" class="easyui-combobox"><br/>
			<input id="check1" type="checkbox" name="checkAll" value='0'>全选<br/>
			<input id="check2" type="checkbox" name="checkAll" value='1'>反选<br/>
</div>
<div class="easyui-panel" data-options="region:'center',split:false" style="width:55%;height:100%">
	<table id="grid2" style="width: 100%" data-options="fit:true"></table>
</div>
</div>
</body>
<script type="text/javascript">
$(function(){
	$('#statType').combobox({
		
	});
	
});

</script>
</html>