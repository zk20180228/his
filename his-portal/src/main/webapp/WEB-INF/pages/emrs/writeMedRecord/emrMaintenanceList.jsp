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
			var nurInpatientNo = parent.window.getPaMeid();
			if((param.nurInpatientNo == undefined) && parent.window.getPaMeid() != ''){
				param.nurInpatientNo = parent.window.getPaMeid();
			}
			$('#list').datagrid('clearChecked');
			$('#list').datagrid('clearSelections');
			if(nurInpatientNo == null || nurInpatientNo ==''){
				return false;
			}
		},
        onDblClickRow: function (rowIndex, rowData) {//双击查看
        	var nurInpatientNo = parent.window.getPaMeid();
        	if(nurInpatientNo==null||nurInpatientNo==""){
        		$.messager.alert('提示','请先选择患者，在进行操作');
        		setTimeout(function(){$(".messager-body").window('close')},3500);
        		return;
        	}
        	window.location.href="<%=basePath%>emrs/maintenance/toQueryView.action?menuAlias=${menuAlias}&maintenance.nurInpatientNo="+nurInpatientNo+"&maintenance.dates="+rowData.nurMeasurEtime;
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

/**
 *添加跳转按钮
 */
function add(){
	var nurInpatientNo = parent.window.getPaMeid();
	if(nurInpatientNo == null||nurInpatientNo == ""){
		$.messager.alert('提示','请先选择患者，在进行操作');
		setTimeout(function(){$(".messager-body").window('close')},3500);
		return;
	}
	var dates = $('#dates').val();
	if(dates == null||dates == ""){
		$.messager.alert('提示','请先选择日期！');
		setTimeout(function(){$(".messager-body").window('close')},3500);
		return;
	}
	var rows = $('#list').datagrid('getRows');
	if(rows.length > 0){
		$.messager.alert('提示','当天已有记录信息，不能进行添加操作。如想操作可选择修改');
		setTimeout(function(){$(".messager-body").window('close')},3500);
	}else{
		window.location.href="<%=basePath%>emrs/maintenance/toAddView.action?menuAlias=${menuAlias}&maintenance.nurInpatientNo="+nurInpatientNo+"&maintenance.dates="+dates;
	}
}

/**
 *修改跳转按钮
 */
function edit(){
	var nurInpatientNo = parent.window.getPaMeid();
	var row = $('#list').datagrid('getSelected');
	if(nurInpatientNo == null||nurInpatientNo == ""){
		$.messager.alert('提示','请先选择患者，在进行操作');
		setTimeout(function(){$(".messager-body").window('close')},3500);
		return;
	}
	if(row == null || row == ""){
		$.messager.alert('提示','请先选择要修改的记录！');
		setTimeout(function(){$(".messager-body").window('close')},3500);
		return;
	}
	window.location.href="<%=basePath%>emrs/maintenance/toEditView.action?menuAlias=${menuAlias}&maintenance.nurInpatientNo="+row.nurInpatientNo+"&maintenance.dates="+row.nurMeasurEtime;
}

/**
 *删除
 */
function del(){
	var nurInpatientNo = parent.window.getPaMeid();
	if(nurInpatientNo == null || nurInpatientNo == ""){
		$.messager.alert('提示','请先选择患者，在进行操作');
		setTimeout(function(){$(".messager-body").window('close')},3500);
		return;
	}
	//选中要删除的行
	var rows = $('#list').datagrid('getChecked');
	if (rows.length > 0) {//选中几行的话触发事件	                        
	 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
			if (res){
				var ids = '';
				for(var i=0; i<rows.length; i++){
					if(ids!=''){
						ids += ',';
					}
					ids += rows[i].nurMeasurEtime;
				};
				$.messager.progress({text:'保存中，请稍后...',modal:true});
				$.ajax({
					url: '<%=basePath %>emrs/maintenance/remove.action',
					type:'post',
					data:{"maintenance.dates":ids,"maintenance.nurInpatientNo":nurInpatientNo},
					success: function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','删除成功');
						$('#list').datagrid('reload');
						rows.length = 0;
					}
				});
			}
		});
	}
}
function pickedFunc(){
	var dates = $('#dates').val();
	var nurInpatientNo = parent.window.getPaMeid();
	$('#list').datagrid('load', {
		"nurInpatientNo" : nurInpatientNo,
		"dates" : dates
	});
}
</script>
</head>
<body>
<div id="divLayout" class="easyui-layout,title:'护理信息列表'" fit=true style="width: 100%;height: 100%;">
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
		<table id="list" style="height:100%;width:100%;" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
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
<div id="toolbarId">
	<shiro:hasPermission name="${menuAlias}:function:add">
	<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:edit">
	<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:delete">
	<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
</div>
</body>
</html>