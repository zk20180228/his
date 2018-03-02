﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<title>出入院情况年度人数统计</title>
</head>
<body style="margin: 0px;padding: 0px;">
	<div id="cc" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center',collapsible:false" id="main" title="出入院情况年度人数统计" style="height:100%;width: 100%;">
			<table id="listopkshz"  class="easyui-datagrid" data-options="rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,
				url:' ${pageContext.request.contextPath}/common/datagrid_data5.json',method:'get',singleSelect:true,fitColumns:true,fixed:true,fit:true">
				<thead>
					<tr>
						<th style="width:10%" data-options="rowspan:2,field:'a'" align="center">科室</th>
						<th data-options="colspan:3" align="center">入院人次情况</th>
						<th data-options="colspan:3" align="center">出院人次情况</th>
					</tr>
					<tr>
						<th style="width: 10%;" data-options="field:'b'" align="center">入院人次</th>
						<th style="width: 10%;" data-options="field:'c'" align="center">比上月增加</th>
						<th style="width: 10%;" data-options="field:'d'" align="center">增幅(%)</th>
						<th style="width: 10%;" data-options="field:'e'" align="center">出院人次</th>
						<th style="width: 10%;" data-options="field:'f'" align="center">比上月增加</th>
						<th style="width: 10%;" data-options="field:'g'" align="center">增幅(%)</th>
					</tr>
					
				</thead>
			</table>
		</div>
	</div>
    <script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script> 
	<script type="text/javascript">
	</script>
</body>
</html>