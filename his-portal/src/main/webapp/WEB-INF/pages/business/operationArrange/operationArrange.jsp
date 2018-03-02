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
<title>手术安排统计</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">

var empList="";//科室集合
$(function(){
	
	//加载数据表格
	$("#operaCostListData").datagrid({
		idField:'id',
		border:true,
		checkOnSelect:true,
		selectOnCheck:false,
		singleSelect:true,
		fitColumns:false,
		pagination:true,
		queryParams:{startTime:null,endTime:null,operaState:null,execDept:null},
		url:'<%=basePath %>business/operationCost/queryOperationCost.action'
		
	});
	
	$.ajax({
		url:"<%=basePath %>business/operationCost/getDeptMap.action",
		type:'post',
		success: function(Data) {
			empList =Data;
		}
	});
	
	
})	
	//科室empList
	function fundepName(value){
		var emp = "";
		if(value!=null&&value!=undefined){
			for(var i=0;i<empList.length;i++){
				if(value==empList[i].id){
					emp = empList[i].name;
					break;
				}
			}	
		}
		return emp;
	}
	
	function search(){
		var startTime= $('#startTime').textbox('getVlaue');
		var endTime= $('#endTime').textbox('getVlaue');
		var inpatientNo= $('#patientNo').textbox('getVlaue');
		var execDept= $('#execDept').combobox('getVlaue');
		$("#operaCostListData").datagrid('reload',{startTime:startTime,endTime:endTime,inpatientNo:inpatientNo,execDept:execDept});
	}
</script>
</head>
	<body style="margin: 0px;padding: 0px;">
		<div id="cc" class="easyui-layout" data-options="fit:true"> 
			<div id="p" data-options="region:'north',border:true" style="width:100%;height:15%;">
				<div id="toolbarId" style="padding:5px 5px 5px 5px;">
					<a href="javascript:void(0)" onclick="search()" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">查询</a>
					<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">打印</a>
					<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">导出</a>
				</div>
				<div style="padding:5px 5px 5px 5px;">
					开始时间：<input class="easyui-datebox" id="startTime" name="startTime"   style="width:200px" />
					结束时间：<input class="easyui-datebox" id="endTime" name="endTime"   style="width:200px" />
					手术状态：<input class="easyui-combobox" id="operaState" name="operaState"   style="width:200px" />
					执行科室：<input class="easyui-combobox" id="execDept" name="execDept"   style="width:200px" />
				</div>
			</div>
			<div data-options="region:'center',border:false" style="width: 100%;height: 85%;">
				统计日期：<input class="easyui-textbox" id="startTime2" style="width:200px" />至<input class="easyui-datebox" id="endTime2"  style="width:200px" />
				科室：<input class="easyui-textbox" id="execDept2" style="width:200px" />
				<table id="operaArrangeListData"  class="easyui-datagrid" style="padding:5px 5px 5px 5px;">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'inpatientNo',width:'15%',halign:'center'">患者所在科室</th>
							<th data-options="field:'name',width:'15%',halign:'center'">手术开始时间</th>
							<th data-options="field:'execDept',width:'15%',halign:'center',formatter:fundepName">住院号</th>
							<th data-options="field:'totCost',width:'15%',halign:'center'">床号</th>
							<th data-options="field:'feeDate',width:'15%',halign:'center'">姓名</th>
							<th data-options="field:'feeDate',width:'15%',halign:'center'">性别</th>
							<th data-options="field:'feeDate',width:'15%',halign:'center'">年龄</th>
							<th data-options="field:'feeDate',width:'15%',halign:'center'">手术名称</th>
							<th data-options="field:'feeDate',width:'15%',halign:'center'">手术间</th>
							<th data-options="field:'feeDate',width:'15%',halign:'center'">施手术者</th>
							<th data-options="field:'feeDate',width:'15%',halign:'center'">助手</th>
							<th data-options="field:'feeDate',width:'15%',halign:'center'">巡回护士</th>
							<th data-options="field:'feeDate',width:'15%',halign:'center'">洗手护士</th>
							<th data-options="field:'feeDate',width:'15%',halign:'center'">麻醉方法</th>
							<th data-options="field:'feeDate',width:'15%',halign:'center'">麻醉医生</th>
							<th data-options="field:'feeDate',width:'15%',halign:'center'">麻醉助手</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</body>
</html>