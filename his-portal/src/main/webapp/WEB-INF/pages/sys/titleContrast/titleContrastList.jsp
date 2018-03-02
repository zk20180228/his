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
	<div data-options="region:'west',title:'职称类型信息',split:true" style="width:15%;">
		<ul id="tDt"></ul>  
	</div>   
	<div data-options="region:'center',title:'职称列表'">
		<div class="easyui-layout" data-options="fit:true" > 
			<div data-options="region:'north'" style="height:42px;width:100%;border-top:0;border-left:0;border-right:0;">
				<table style="width:100%;padding: 0px 5px 0px 5px;height: 40px;">
					<tr>
						<td nowrap="nowrap"  >查询条件：
							<input  id="codes" data-options="prompt:'代码、名称、拼音、五笔码、自定义码'"  class="easyui-textbox"  style="width: 200px"/>
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							<a href="javascript:reset();" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
						</td>
					</tr>
				</table>
			</div> 
			<div data-options="region:'center',border: false" style="width:100%;">
				<table id="list" class="easyui-datagrid" data-options="url:'${pageContext.request.contextPath}/baseinfo/titleContrast/queryTitleContrastList.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true" text-align: center">id</th>
							<th data-options="field:'belongTypeName',align:'center'"  style="width:10%" >职称类型</th>
							<th data-options="field:'titleCode',align:'center'"  style="width:5%" >职称代码</th>
							<th data-options="field:'titleName',align:'center'"  style="width:10%">职称名称</th>
							<th data-options="field:'titleLevel',align:'center',formatter:formatLevel" style="width:10% " >职称等级</th>
							<th data-options="field:'titlePinyin',align:'center'"  style="width:10%">拼音码</th>
							<th data-options="field:'titleWb',align:'center'"  style="width:10% ">五笔码</th>
							<th data-options="field:'titleInputCode',align:'center'"  style="width:10% ">自定义码</th>
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
var winH=$("body").height();
var departmentMap= "";//科室
$(function(){
	//添加datagrid事件及分页
	$('#list').datagrid({
		pagination:true,
   		pageSize:20,
   		pageList:[10,20,30,40,50],
   		onBeforeLoad:function (param) {
			$('#list').datagrid('clearChecked');
			$('#list').datagrid('clearSelections');
        },
		onLoadSuccess: function (data) {//默认选中
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
				}
        },onDblClickRow: function (rowIndex, rowData) {//双击查看
				if(rowData.id!=null&&rowData.id!=''){
					AddOrShowEast('查看',"<%=basePath %>baseinfo/titleContrast/viewTitleContrast.action?id="+rowData.id);
			   	}
			}    
		});
	bindEnterEvent('codes',searchFrom,'easyui');
});
	
$('#tDt').tree({    
	url:"<%=basePath %>baseinfo/titleContrast/treeTitleContrast.action",
	formatter:function(node){//统计节点总数
		var s=node.text;
		return s;
	},onClick:function(node){
		//选择根节点的话  清空所有数据
		$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
		if(node==null||node.id=='root'){
			$('#list').datagrid('loadData',{ total: 0, rows: [] });
		}else{
			$('#list').datagrid({
				url:'<%=basePath %>baseinfo/titleContrast/queryTitleContrastList.action',
				queryParams: {treeId : node.attributes.code,treeName:node.text}
			});
		}
		$('#divLayout').layout('remove','east');
		
	}
}); 
//条件查询
function searchFrom(){
	var queryName =  $.trim($('#codes').textbox('getValue'));
	var node = $('#tDt').tree('getSelected');
	var treeId='';
	if(node!=null&&node.id!='root'){
		treeId=node.attributes.code;
	}
	$('#divLayout').layout('remove','east');
    $('#list').datagrid('load', {
    	queryName: queryName,
    	treeId : treeId
	});
}	
//添加
function add(){
	var node=$('#tDt').tree('getSelected');
	//选择根节点的话  清空所有数据
	if(node==null||node.id=="root"){
		$.messager.alert('提示','请选择一项职务类型信息再进行添加');
		close_alert();
		return;
	}
	AddOrShowEast('EditForm',"<%=basePath %>baseinfo/titleContrast/addTitleContrast.action",'post'
			   ,{"treeId":node.id,"treeName":node.text});
}
//修改
function edit(){
		var rows = $('#list').datagrid('getSelected');
		if(rows==null){
			$.messager.alert('提示','请选择要修改的信息');
			close_alert();
			return;
		}
		var sid=getIdUtil("#list");
		 AddOrShowEast('EditForm','<%=basePath %>baseinfo/titleContrast/editTitleContrast.action','post'
				   ,{"id":sid});
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
				$.messager.progress({text:'删除中，请稍后...',modal:true});
				$.ajax({
					url: '<%=basePath %>baseinfo/titleContrast/delTitleContrast.action?ids='+ids,
					type:'post',
					success: function(data) {
						$.messager.progress('close');
						if("success"==data.resCode){
							$.messager.alert('提示',data.resMsg);
							$('#tDt').tree("reload");
							$('#list').datagrid('reload');
						}else{
							$.messager.alert('提示',data.resMsg);
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
	$('#divLayout').layout('remove','east');
	$('#tDt').tree('reload'); 
}
var levelMap={ '1':'一级','2':'二级','3':'三级','4':'四级'};
//渲染科室
function formatLevel(value,row,index){
	if(value!=null&&value!=''){
		return levelMap[value];
	}else{
		return value;
	}
}

</script>
</body>
</html>