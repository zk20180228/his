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
</head>
<body>
<script type="text/javascript">
	var num="${num}";
	var row="${row}";
	$(function(){
		$('#time').datebox({
		    onSelect: function(date){
		    	var year=date.getFullYear();
		    	var day=date.getDate();
		    	var month=+date.getMonth()+1;
		    	var f=year+"/"+formate(month)+"/"+formate(day);
		    	$('#listsearch').datagrid('updateRow',{
		    		index:num,
		    		row:{
		    			relation:row.relation,
		    			tiaojian:row.tiaojian,
		    			caozuo:row.caozuo,
		    			zhi:f
		    		}
		    	});
		    }
		});
	});
	function formate(d){
		return d>9?d:'0'+d;
	}
</script>
<div style="padding:10px 10px 10px 10px">
	<input id="time" class="easyui-datebox" />
</div>
</body>
</html>