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
<title>医嘱类型维护</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
$(function(){//加载列表
	var id='${id}'; //存储数据ID
	$('#list').datagrid({
		rownumbers: true,
		pagination: true,
		pageSize: 20,
		pageList: [20,30,50,100],
        onDblClickRow: function (rowIndex, rowData) {//双击查看
	        	AddOrShowEast('EditForm','<%=basePath %>baseinfo/kindInfo/viewKindInfo.action?kindInfo.id='+rowData.id);
		},
		onBeforeLoad:function(){
			$('#list').datagrid('clearChecked');
			$('#list').datagrid('clearSelections');
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
	
	bindEnterEvent('queryName',searchFrom,'easyui');
});

function decmpsStateformatter(value,row,index){		
		if(value==1){
			return '长期医嘱';
		}
		else {		
			return '临时医嘱';
		}	
}
function yornformatter(value,row,index){		
	if(value==1){
		return '是';
	}
	else {		
		return '否';
	}	
}

function fitExtentformatter(value,row,index){		
	if(value==1){
		return '门诊';
	}if(value==2){
		return '住院';
	}
	if(value==3){
		return '全院';
	}	
}


//添加
function add(){
	AddOrShowEast('EditForm',"<%=basePath %>baseinfo/kindInfo/addKindInfo.action");
}
//修改
function edit(){
	if(getIdUtil("#list") != null){
		AddOrShowEast('EditForm',"<%=basePath %>baseinfo/kindInfo/editKindInfo.action?kindInfo.id="+getIdUtil("#list"));
	}
}
//跳转
function AddOrShowEast(title, url) {
	var eastpanel=$('#panelEast'); //获取右侧收缩面板
	if(eastpanel.length>0){ //判断右侧收缩面板是否存在
		//重新装载右侧面板
   		$('#divLayout').layout('panel','east').panel({
               href:url
        });
	}else{//打开新面板
		$('#divLayout').layout('add', {
			region : 'east',
			width : '35%',
			split : true,
			href : url,
			closable : true,
			border : false
		});
	}
}
function del(){
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
					ids += rows[i].id;
				};
				$.ajax({
					url: '<%=basePath %>baseinfo/kindInfo/delKindInfo.action?ids='+ids,
					success: function() {
						$.messager.alert('提示','删除成功');
						$('#list').datagrid('reload');
					}
				});
			}
         });
 }else{
	 $.messager.alert('提示',"请选择要删除的信息！");
 }
}

function reload(){
//实现刷新栏目中的数据
 $('#list').datagrid('reload');
}

//条件查询
function searchFrom(){
	var queryName =$.trim($('#queryName').textbox('getValue'));
    $('#list').datagrid('load',{
    	'kindInfo.typeName':queryName
	});
}
//药品列表查询重置
function searchReload() {
	$('#queryName').textbox('setValue','');
	searchFrom();
}
</script>
</head>
<body style="margin: 0px; padding: 0px">
	<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',border:false">
			<table  style="height: 35px">
				<tr>
					<td>
						<input id="queryName" class="easyui-textbox" data-options="prompt:'名称'" />
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-search">查询</a>
						<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
				</tr>
			</table>
		</div>	
		<div data-options="region:'center',border:true">
			<table id="list" style="width:100%;" 
			data-options="url:'<%=basePath %>baseinfo/kindInfo/queryKindInfo.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',title:'查询列表',fit:true">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'typeName'" width="9%">医嘱类别名称</th>
						<th data-options="field:'fitExtent',formatter:fitExtentformatter" width="9%">适用范围</th>
						<th data-options="field:'decmpsState',formatter:decmpsStateformatter" width="9%">医嘱状态</th>
						<th data-options="field:'chargeState',formatter:yornformatter" width="9%">是否计费</th>
						<th data-options="field:'needDrug',formatter:yornformatter" width="9%">药房是否配药</th>
						<th data-options="field:'prnExelist',formatter:yornformatter" width="9%">是否打印执行单</th>
						<th data-options="field:'needConfirm',formatter:yornformatter" width="9%">是否需要确认</th>
						<th data-options="field:'totqtyFlag',formatter:yornformatter" width="9%">是否能开总量</th>
						<th data-options="field:'prnMorlist',formatter:yornformatter" width="9%">是否打印医嘱单</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
 	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add"> 
 			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" 
 				data-options="iconCls:'icon-add',plain:true">添加</a> 
		</shiro:hasPermission> 
		<shiro:hasPermission name="${menuAlias}:function:edit"> 
 			<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" 
 				data-options="iconCls:'icon-edit',plain:true">修改</a> 
 		</shiro:hasPermission> 
 		<shiro:hasPermission name="${menuAlias}:function:delete">	
 			<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" 
 				data-options="iconCls:'icon-remove',plain:true">删除</a> 
 		</shiro:hasPermission> 
 		<a href="javascript:void(0)" onclick="reload()"class="easyui-linkbutton"
			data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
</body>
</html>