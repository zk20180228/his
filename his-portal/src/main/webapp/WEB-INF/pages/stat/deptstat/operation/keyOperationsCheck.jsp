<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>住院重点手术监测汇总</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">

function findData(){
	var start=$("#login").val();
	var end=$("#end").val();
	$("#startTime").text(start);
	$("#endTime").text(end);
}

function iReportInvoiceNo(){
	var data = $('#list').datagrid('getData');
	if (data.total==0) {
		$.messager.alert("友情提示", "列表无数据，无法打印");
		setTimeout(function(){
			$(".messager-body").window('close');
		},2000);
		return;
	}
}

//导出列表
function exportList() {
	var data=$("#list").datagrid('getData');
	if(data.total==0){
		$.messager.alert("友情提示", "列表无数据，无法导出");
		setTimeout(function(){
			$(".messager-body").window('close');
		},2000);
		return;
	}
}
/**
 * 重置
 * @author huzhenguo
 * @date 2017-03-17
 * @version 1.0
 */
function clears(){
	$("#tab").form('clear');
	$(".Input").text("");
	$("#list").datagrid('loadData',{total:0,rows:[]}); 
	$("#startTime").text("");
	$("#endTime").text("");
}
</script>
<style type="text/css">
	.tableCss{
		border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:100%;
	}
	.tableCss .TDlabel{
		text-align: right;
		font-size:14px;
		width:200px;
	}
	.tableCss td{
		border: 1px solid #95b8e7;
		padding: 5px 5px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.easyuiInput{
		width:200px;
	}
	.Input{
		width:200px;
	}
</style>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north',border:false" style="height:42px;width:100%">
    	<table id="tab" style="width: 100%;padding: 5px 5px 5px 5px;">
			<tr>
				<td>
					出院日期：
					<input id="login"  class="Wdate" type="text" value="${start }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:150px;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/> 
					&nbsp;至&nbsp;
					<input id="end"  class="Wdate" type="text" value="${end }"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:150px;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					&nbsp;
					重点手术名称：<input id="name"   class="easyui-textbox"  />&nbsp;
					<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="findData()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:print">
						<a href="javascript:void(0)" onclick="iReportInvoiceNo()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:export">
					<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
    </div>
    <div data-options="region:'center',border:false" style="width:100%">
    	<div class="easyui-layout" data-options="fit:true">
    		<div data-options="region:'north',border:false" style=";width:100%;height: 70px;overflow-y: hidden">
    			<table style="width:100%;padding: 5px 5px 5px 0px;border: 0px;">
		    		<tr>
		    			<td align="center"><font size="6" class="outpatienttit">住院重点手术监测汇总</font></td>
		    		</tr>
		    		<tr>
		    			<td >&nbsp;出院日期：<span style="width: 350px;font-size: 18" id="startTime"></span>&nbsp;&nbsp;&nbsp;至
		    					&nbsp;<span style="width: 150px;font-size: 18" id="endTime"></span>
		    			</td>
		    		</tr>
		    	</table>
    		</div>
    		<div data-options="region:'center',border:false" style="width:100%">
    			<table class="easyui-datagrid"  id="list"  data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">   
				    <thead>   
				        <tr> 
				            <th data-options="field:'drugFlag',halign:'center',align:'right'" style="width: 5%">序号</th>   
				            <th data-options="field:'feeName',halign:'center',align:'right'" style="width: 25%">重点手术名称</th>
				            <th data-options="field:'itemName',halign:'center',align:'right'" style="width: 9%">总例数</th>
				            <th data-options="field:'unitPrice',align:'right',halign:'center'" style="width: 9%">死亡例数</th>
				            <th data-options="field:'qty',align:'right',halign:'center'" style="width: 9%">死亡率</th>
				            <th data-options="field:'money',align:'right',halign:'center'," style="width: 9%">两周内再入院例数</th>
				            <th data-options="field:'money',align:'right',halign:'center'," style="width: 9%">一月内再入院例数</th>
				            <th data-options="field:'money',align:'right',halign:'center'," style="width: 9%">平均住院日</th>
				            <th data-options="field:'money',align:'right',halign:'center'," style="width: 9%">平均住院费用</th>
				        </tr>   
				    </thead>   
				</table>
    		</div>
    	</div>
    </div>   
</div>  
</body>
</html>