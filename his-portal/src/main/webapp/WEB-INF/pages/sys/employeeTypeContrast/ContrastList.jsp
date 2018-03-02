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
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
.panel-header,.panel-body{
	border-top:0
}
</style>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true" >   
	<div data-options="region:'west',title:'人员类型分类信息',split:true" style="width:15%;">
		<ul id="tDt"></ul>  
	</div>   
	<div data-options="region:'center',title:'对照列表'">
		<div class="easyui-layout" data-options="fit:true" > 
			<div data-options="region:'north'" style="height:42px;width:100%;border-top:0;border-left:0;border-right:0;">
				<table style="width:100%;padding: 0px 5px 0px 5px;height: 40px;">
					<tr>
						<td nowrap="nowrap"  >查询条件：
							<input  id="codes" data-options="prompt:'代码,名称'"  class="easyui-textbox"  style="width: 200px"/>
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							<a href="javascript:reset();" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
						</td>
					</tr>
				</table>
			</div> 
			<div data-options="region:'center',border: false" style="width:100%;">
				<table id="list" class="easyui-datagrid" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true" text-align: center">id</th>
							<th data-options="field:'empTypeName'"  style="width:18%">类型分类名称</th>
							<th data-options="field:'empTypeCode'" style="width:18% " >类型分类代码</th>
							<th data-options="field:'inputCode'"  style="width:18%">类型分类自定义码</th>
							<th data-options="field:'name'"  style="width:18% ">类型名称</th>
							<th data-options="field:'code'"  style="width:18% ">类型代码</th>
						</tr>
					</thead>
				</table>
			</div>
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
</div>
<script type="text/javascript">
$(function(){
	$('#list').datagrid({
		pagination: true,
		pageSize: 20,
		url: '<%=basePath %>baseinfo/employeeTypeContrast/queryContrast.action',
		pageList: [20,30,50,100],
		onDblClickRow: function (rowIndex, rowData) {//双击查看
			AddOrShowEast('EditForm','<%=basePath %>baseinfo/employeeTypeContrast/viewContrast.action?id='+rowData.id);
		},
		onBeforeLoad:function(){
			//翻页时清空前页的选中项
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
	bindEnterEvent('codes',searchFrom,'easyui');
});
	
$('#tDt').tree({	
	url:"<%=basePath %>baseinfo/employeeTypeContrast/typeTree.action",
	onClick:function(node){
		$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
		if(node==null||node.id=='root'){
			$('#list').datagrid('load',{empType : ''});
		}else{
			$('#list').datagrid('load',{empType : node.id});
		}
		
	}
}); 
//条件查询
function searchFrom(){
	var codes =  $.trim($('#codes').textbox('getValue'));
	var node = $('#tDt').tree('getSelected');
	if(node!=null&&node.id!='root'){
		node=node.attributes.code;
	}
	$('#list').datagrid('load', {
		queryName: codes,
		empType : node
	});
}	
//添加
function add(){
	var node=$('#tDt').tree('getSelected');
	var empType = '';
	//选择根节点的话  清空所有数据
	if(node && node.id != 'root'){
		empType = node.id;
	}
	AddOrShowEast('EditForm',"<%=basePath %>baseinfo/employeeTypeContrast/addContrast.action",'post',{"empType":empType});
}
//修改
function edit(){
	var row = $('#list').datagrid('getSelected');
	if(row == null || row.length == 0){
		$.messager.alert('提示','请选择要修改的信息');
		close_alert();
		return;
	}else{
		AddOrShowEast('EditForm','<%=basePath %>baseinfo/employeeTypeContrast/editContrast.action','post',{"id":row.id});
	}
}
function del(){
	//选中要删除的行
	var rows = $('#list').datagrid('getChecked');
	if (rows.length > 0) {//选中几行的话触发事件
	 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
			if (res){
				var ids = '';
				for(var i = 0; i < rows.length; i++){
					if(ids != ''){
						ids = ids + ',';
					}
					ids = ids + rows[i].id;
				};
				$.messager.progress({text:'删除中，请稍后...',modal:true});
				$.ajax({
					url: '<%=basePath %>baseinfo/employeeTypeContrast/delContrasts.action?ids='+ids,
					type:'post',
					success: function(data) {
						var res = data;
						$.messager.progress('close');
						$.messager.alert('提示',res.resMsg);
						if(res.resCode == 'success'){
							$('#list').datagrid('reload');
						}
					}
				});
			}
		});
	}
}

//跳转
function AddOrShowEast(title, url,method,params) {
	if(!method){
		method="get";
	}
	if(!params){
		params={};
	}
	var eastpanel=$('#panelEast'); //获取右侧收缩面板
	if(eastpanel.length>0){ //判断右侧收缩面板是否存在
		//重新装载右侧面板
		$('#divLayout').layout('panel','east').panel({
				href:url,
				method:method,
				queryParams:params
		});
	}else{//打开新面板
		$('#divLayout').layout('add', {
			region : 'east',
			width : 580,
			split : true,
			href : url,
			method:method,
			queryParams:params,
			closable : true,
		});
	}
}
//重置
function reset(){
	$("#codes").textbox("setValue","");
	$('#list').datagrid('load',{});
}
</script>
</body>
</html>