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
<title>护理信息维护</title>
<script type="text/javascript">
/**
 *加载页面加载
 */

$(function(){
	//护理列表
	$('#list').datagrid({
		rownumbers: true,
		pagination: true,
		pageSize: 20,
		url:'<%=basePath%>emrs/maintenance/queryMaintenanceList.action',
		pageList: [20,30,50,100],
		onBeforeLoad: function (param) {
			var nurInpatientNo = parent.window.getInpatientNo();
			if((param.nurInpatientNo == undefined) && parent.window.getInpatientNo() != ''){
				param.nurInpatientNo = parent.window.getInpatientNo();
			}
			$('#list').datagrid('clearChecked');
			$('#list').datagrid('clearSelections');
			if(nurInpatientNo == null || nurInpatientNo ==''){
				return false;
			}
		},
		onDblClickRow: function (rowIndex, rowData) {//双击查看
		var nurInpatientNo = parent.window.getInpatientNo();
			if(nurInpatientNo==null||nurInpatientNo==""){
				$.messager.alert('提示','请先选择患者，在进行操作');
				setTimeout(function(){$(".messager-body").window('close')},1500);
				return;
			}
			window.location.href="<%=basePath%>emrs/maintenance/toMaintenanceViewForInpatient.action?menuAlias=${menuAlias}&maintenance.nurInpatientNo="+nurInpatientNo+"&maintenance.dates="+rowData.nurMeasurEtime;
		},
		onLoadSuccess:function(row, data){
			//分页工具栏作用提示
			var pager = $(this).datagrid('getPager');
			var aArr = $(pager).find('a');
			var iArr = $(pager).find('input');
			$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
			for(var i=0;i<aArr.length;i++){
				$(aArr[i]).tooltip({
					content:toolArr[i],
					hideDelay:1
				});
				$(aArr[i]).tooltip('hide');
			}}
	});
});
	function pickedFunc(){
		var dates = $('#dates').val();
		var nurInpatientNo = parent.window.getInpatientNo();
		$('#list').datagrid('load', {
			"nurInpatientNo" : nurInpatientNo,
			"dates" : dates
		});
	}
	

</script>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="title:'护理信息列表'"  fit=true style="width: 100%;height: 100%;">
	<div data-options="region:'north'" style="height: 30px">
		<table style="width:100%;border:1px none #95b8e7;padding: 3px 0px 5px 5px;">
			<tr>
				<td style="font-size:14px" >日期：
				<input type="text" id="dates" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:pickedFunc})" class="Wdate" style="width: 150px"/>
				</td>
			</tr>
		</table>   
	</div>
	<div data-options="region:'center'"style="width: 100%;height: 100%;">
		<input id="mid" type="hidden">
		<table id="list" style="height:100%;width:100%;" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
			<thead>
				<tr>
					<th data-options="field:'id',checkbox:true" text-align: center">id</th>
					<th data-options="field:'nurName'"  style="width:16%" >患者姓名</th>
					<th data-options="field:'nurPatid'"  style="width:16%" >患者病历号</th>
					<th data-options="field:'nurInpatientNo'"  style="width:16%" >患者住院流水号</th>
					<th data-options="field:'nurMeasurEtime'"  style="width:16%" >测量时间</th>
					<th data-options="field:'nurOpDay'"  style="width:16%">手术天数</th>
					<th data-options="field:'nurInDay'" style="width:16% " >住院天数</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
</body>
</html>