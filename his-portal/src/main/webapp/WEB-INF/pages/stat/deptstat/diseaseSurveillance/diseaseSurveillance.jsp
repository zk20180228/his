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
<script type="text/javascript">
//导出列表
function exportList() {
	var startTime = $('#startTime').val();
	var endTime = $('#endTime').val();
	var rows = $('#tableList').datagrid('getRows');
	if(rows==null||rows==""){
		$.messager.alert("提示", "列表无数据,无法导出！");
		return;
	}
	$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
		if (res) {
			$('#saveForm').form('submit', {
				url :"<%=basePath%>statistics/DetailedDaily/expDetailedDailylist.action",
				queryParams:{stime:beginDate,etime:endDate},
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(data) {
					$.messager.alert("操作提示", "导出成功！", "success");
				},
				error : function(data) {
					$.messager.alert("操作提示", "导出失败！", "error");
				}
			});
		}
	});
}
//报表打印
function exportPDF() {
	var rowsCount=$('#tableList').datagrid('getRows')
	if(''!=rowsCount&&null!=rowsCount){
	var startTime = $('#startTime').val();
	var endTime = $('#endTime').val();
	window.open ("<c:url value='queryDetailedDailylistPDF.action?stime='/>"+beginDate+"&etime="+endDate,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}else{
		$.messager.alert('提示','列表无数据,无法打印！');
	}	
}
//重置按钮
function clear(){
	$('#startTime').val("${startTime}");
	$('#endTime').val("${endTime}");
	$('#diseaseName').textbox('setValue',"");
	$("#tableList").datagrid('loadData', { total: 0, rows: [] });
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
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
	<div class="easyui-layout" data-options="fit:true" style="margin-left:auto;margin-right:auto;">
		<div data-options="region:'north',border:false" style="padding:5px 5px 0px 5px;width:100%;height:95px;"  >
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false" style="width:100%;">
					<table style="width: 100%" cellspacing="0" cellpadding="0">
						<tr>
							<td class="chargeBillistSize">出院日期:
								<input id="startTime"  name="startTime" class="Wdate" type="text" value="${startTime }" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								至
								<input id="endTime"  name="endTime"  class="Wdate" type="text" value="${endTime }" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								&nbsp;重点疾病名称：<input id="diseaseName" class='easyui-textbox'/>
								<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="clear()" iconCls="reset">重置</a>
								<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportList()" iconCls="icon-down">导出</a>
								<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportPDF()" iconCls="icon-printer">打印</a>
							</td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center',border:false" align="center" style="padding:5px">
					<font size="6" class="outpatienttit">住院重点疾病监测汇总</font>
				</div>
				
			</div>
		</div>
		<div data-options="region:'center',border:false" style="width: 100%;margin-left:auto;margin-right:auto;">
			<table id="tableList" class="easyui-datagrid" style="width:100%;height:95%;" data-options="fitColumns:true,singleSelect:true,fit:true,border:false" >
				<thead>
					<tr>
						<th data-options="field:'id',width:'5%',align:'center'">序号</th>
						<th data-options="field:'diseasesName',width:'15%'" align="center" halign="center">重点病种名称</th>
						<th data-options="field:'count',width:'7%'"align="center" halign="center">总例数</th>
						<th data-options="field:'death',width:'7%'" align="center" halign="center">死亡例数</th>
						<th data-options="field:'mortality',width:'7%'" align="center" halign="center">死亡率</th>
						<th data-options="field:'twoWeek',width:'10%'" align="center" halign="center">两周内再入院例数</th>
						<th data-options="field:'oneWeek',width:'10%'" align="center" halign="center">一周内再入院例数</th>
						<th data-options="field:'averageDay',width:'10%'" align="center" halign="center">平均住院日</th>
						<th data-options="field:'averageFee',width:'10%'" align="center" halign="center">平均住院费用</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>