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
<title>列表显示</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px; padding: 0px">
		<div id="cc" class="easyui-layout" data-options="fit:true">   
		    <div data-options="region:'center'">
		    	<table id="list11" class="easyui-datagrid" style="width:100%;height:100%;"   
			        data-options="fitColumns:true,singleSelect:true,border:false">   
			    	<thead>   
				        <tr>   
				            <th data-options="field:'NAME',width:100,rowspan:2">名称</th>
				            <th data-options="field:'date',width:100,colspan:3">年度</th>
				            <th data-options="field:'COUNT1',width:100,rowspan:2">数量</th>
				            <th data-options="field:'DEPTCODE',width:100,rowspan:2">科室</th>
				            <th data-options="field:'COUNT2',width:100,rowspan:2">数量</th>
				        </tr>
				        <tr>   
				            <th data-options="field:'date1',width:100">2014</th>
				            <th data-options="field:'date2',width:100">2015</th>
				            <th data-options="field:'date3',width:100">2016</th>
				        </tr>
				    </thead>   
				</table>
		    </div>   
		</div>
		<script type="text/javascript">
			$(function(){
				$('#list11').datagrid({
					url:'<%=basePath%>statistics/bi/statisticalSetting/querylistshow.action',
					onLoadSuccess: function (data) {//默认选中
						$('#list11').datagrid("autoMergeCells", ['NAME','date1','date2','date3','COUNT1']);
					}
				});
			})
			/**
			 * 合并单元格
			 */
			$.extend($.fn.datagrid.methods, {
				autoMergeCells: function (jq, fields) {
					return jq.each(function () {
						var target = $(this);
						if (!fields) {
							fields = target.datagrid("getColumnFields");
						}
						var rows = target.datagrid("getRows");
						var i = 0,
						j = 0,
						temp = {};
						for (i; i < rows.length; i++) {
							var row = rows[i];
							j = 0;
							for (j; j < fields.length; j++) {
								var field = fields[j];
								var tf = temp[field];
								if (!tf) {
									tf = temp[field] = {};
									tf[row[field]] = [i];
								} else {
									var tfv = tf[row[field]];
									if (tfv) {
										tfv.push(i);
									} else {
										tfv = tf[row[field]] = [i];
									}
								}
							}
						}
						$.each(temp, function (field, colunm) {
							$.each(colunm, function () {
							var group = this;
								if (group.length > 1) {
									var before,
									after,
									megerIndex = group[0];
									for (var i = 0; i < group.length; i++) {
										before = group[i];
										after = group[i + 1];
										if (after && (after - before) == 1) {
										    continue;
										}
										var rowspan = before - megerIndex + 1;
										if (rowspan > 1) {
											target.datagrid('mergeCells', {
												index: megerIndex,
												field: field,
												rowspan: rowspan
											});
										}
										if (after && (after - before) != 1) {
										    megerIndex = after;
										}
									}
								}
							});
						});
					});
				}
			});
		</script>
</body>
</html>