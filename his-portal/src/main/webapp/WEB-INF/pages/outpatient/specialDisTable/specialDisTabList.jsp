<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>特殊项目与配药台</title>
<%@ include file="/common/metas.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<style type="text/css">
	.tableCss{
			border-collapse: collapse;
			border-spacing: 0;
			border-left: 1px solid #95b8e7;
			border-top: 1px solid #95b8e7;
	}
	.tableLabel{
		text-align: right;
		width:100px;
	}
	.tableCss td{
		border-right: 1px solid #95b8e7;
		border-bottom: 1px solid #95b8e7;
		padding: 5px 15px;
		word-break: keep-all;
		white-space:nowrap;
	}
	#divLayout .panel-header{
		border-top:0
	}
</style>
</head>
<body>
	<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center'" data-options="fit:true" style="border-top:0">
			<table id="list" class="easyui-datagrid">   
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true"></th>
						<th data-options="field:'deptid',formatter:fundeptMap" style="width:10%" >所属药房</th>
						<th data-options="field:'code',formatter:fundosageMap" style="width:10%">配药台名称</th>
						<th data-options="field:'itemType',formatter:funitemTypeMap" style="width:10% ">类别</th>
						<th data-options="field:'itemName'" style="width:15%">项目名称</th>
						<th data-options="field:'mark'" style="width:55%">备注</th>
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
		<a href="javascript:listReload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div> 
</body>
<script type="text/javascript">

var d_datagridObj=null;	//所选类别
var itemTypeMap='';	/* 特定级别的map */
var deptMap='';		//药房Map
var dosageMap='';		//配药台

<%-- 初始化List--%>
$(function(){
	$.ajax({
        url:'<%=basePath %>drug/specialDispensingtable/romanceDept.action',
        type:'post',
        async:false,
        success:function(data){
            deptMap = data;
        }
    });
	
	$.ajax({
	     url:'<%=basePath %>drug/specialDispensingtable/romanceDosage.action',
	     type:'post',
	     async:false,
	     success:function(data){
	    	 dosageMap = data;
	     }
	 });
	
	$('#list').datagrid({
		url:'<%=basePath%>drug/specialDispensingtable/querySpecialDisTabList.action?menuAlias=${menuAlias}',
		selectOnCheck:false,
		rownumbers:true,
		idField: 'id',
		toolbar:'#toolbarId',
		pagination:true,
		pageSize:20,
		fit:true,
		fixed:false,
		fitColumns:false,
		singleSelect:true,
		checkOnSelect:false,
		pageNumber: 1,
		border:false,
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
	
});

//渲染药房信息
function fundeptMap(value){
    for(var i = 0; i < deptMap.length; i++){
        if(value == deptMap[i].deptCode){
            return deptMap[i].deptName;
        }
    }
}
 
 
/**获取配药台map**/
function fundosageMap(value){
	for(var i = 0; i < dosageMap.length; i++){
		if(value == dosageMap[i].id){
		return dosageMap[i].name;
		}
	}
}

<%--***  特定级别数组     ***--%>
function getitemTypeArray(){
	return [{id:'1',value: '药品'},{id:'2',value: '专科'},{id:'3',value: '结算类别'},{id:'4',value: '特定收费窗口'},{id:'5',value: '挂号级别'}];
}

<%--***  获取特定级别的Map   ***--%>
function getitemTypeMap(){
	var itemTypeMap = new Map();
	var itemTypeArray = getitemTypeArray();
	for(var i=0;i<itemTypeArray.length;i++){
		itemTypeMap.put(itemTypeArray[i].id,itemTypeArray[i].value);
	}
	return itemTypeMap;
}

<%--*** 渲染特定级别的Map   ***--%>
function funitemTypeMap(value){
	var itemTypeMap = getitemTypeMap();
	if(value!=null&&value!=''){
		return itemTypeMap.get(value);
	}
}

<%--***   表格操作区  Begin   ****************************************************************************************************--%>

/*** 添加 ***/
function add(){
	if($('#dd')){
		$('#dd').remove();
	}
	if($('#dilogtree')){
		$('#dilogtree').remove();
	}
	AddOrShowEast("添加","<%=basePath%>drug/specialDispensingtable/eastView.action");		
}


function edit(){
	if($('#dd')){
		$('#dd').remove();
	}
	if($('#dilogtree')){
		$('#dilogtree').remove();
	}
	var row = $('#list').datagrid('getSelected'); //获取当前选中行                        
    if(row){
    	AddOrShowEast('修改',"<%=basePath%>drug/specialDispensingtable/editView.action?ids="+row.id);
	}else{
		$.messager.alert('提示','请选择一条要修改的的数据！');
		setTimeout(function(){$(".messager-body").window('close')},1500);
	}
}


function del(){
	var obj=$('#list').datagrid('getChecked');     
	if(obj.length>0){
		var ids ='';
		$.each(obj,function(i,n){
			if(ids != ''){
				ids += ',';
			}
			ids += n.id;
		});
		var j = obj.length;
		$.messager.confirm('确认对话框', '您想要删除'+j+'条记录吗？', function(r){
			if(r){
				$.ajax({
					url:'<%=basePath%>drug/specialDispensingtable/delStoTerminalSpe.action',
					type:'post',
					data:{'ids':ids},
					success:function(data){
							listReload();
						$.messager.alert('提示','删除成功'); 
					}
				});
			}
		});
	}else{
		$.messager.alert('提示','请勾选要删除的数据');    
		setTimeout(function(){$(".messager-body").window('close')},1500);
	}
}

//判断是否添加LayOut
function AddOrShowEast(title, url) {
	var eastpanel=$('#panelEast'); //获取右侧收缩面板
	if(eastpanel.length>0){ //判断右侧收缩面板是否存在
		$('#divLayout').layout('remove','east');
		addPanel(title, url);
	}else{//打开新面板
		addPanel(title, url);
	}
}
//添加LayOut
function addPanel(title, url){
	$('#divLayout').layout('add', {
		title:title,
		region:'east',
		width:500,
		split:true,
		href:url,
		closable:true,
		collapsible : false
	});
}

//关闭编辑窗口
function closeLayout(){
	$('#divLayout').layout('remove','east');
}

function listReload(){
	//实现刷新栏目中的数据
	$('#list').datagrid('reload');   
}
</script>
</html>	