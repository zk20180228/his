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
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="width:100%; height: 40px">
		<form id="search" method="post">
			<table style="width:100%;border:none;padding: 5px 0px 5px 0px" data-options="border:false">
				<tr>
					<td style="width:190px;"><input id="sName" name="menu.name" class="easyui-textbox" data-options="prompt:'栏目名,别名,拼音,五笔,自定义码'" style="width:220px;"/></td>
					<td>
					<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</shiro:hasPermission>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center'" style="width: 100%">
		<table id="list" style="width:100%;" 
			data-options="idField:'id',border:false,fit:true,animate:false,striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true,height:40"></th>
						<th data-options="field:'id',hidden:true">主键</th>
						<th data-options="field:'name',width:'15%'">名称</th>
						<th data-options="field:'alias',width:'10%'">别名</th>
						<th data-options="field:'type',width:'10%',formatter:fun">类型</th>
						<th data-options="field:'parameter',width:'10%'">参数</th>
						<th data-options="field:'description',width:'10%'">说明</th>
					</tr>
				</thead>
			</table>
	</div>
</div>
	<div id="menuWin"></div>
	<div id="toolbarId">
	<shiro:hasPermission name="${menuAlias}:function:add">
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:delete">
		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<style type="text/css">
		.window .panel-header .panel-tool a{
  	  		background-color: red;	
		}
	</style>
	<script type="text/javascript">
	var parentId="${parentId}";
	//初始化页面
	var indexorder="";
	$(function(){
		$('#list').datagrid({
			url: "<%=basePath%>sys/shortcut/queryShortcutMenufunction.action?menuAlias=${menuAlias}",
			rownumbers: true,
			pagination: true,
			pageSize: 10,
			pageList: [10,20,30,50,80,100],
			onBeforeLoad: function(row,param){
				$('#list').datagrid('uncheckAll');
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
				}
			},
			onDblClickRow: function (row) {//加载之前获得数据权限类型
				if(getIdUtil('#list').length!=0){
			   	    AddOrShowEast("查看","<%=basePath%>sys/shortcut/viewMenu.action?id="+getIdUtil('#list'),'35%');
			   	}
			},
			onClickRow: function(row){
				var eastpanel=$('#panelEast'); //获取右侧收缩面板
				if(eastpanel.length>0){ //判断右侧收缩面板是否存在
					$('#divLayout').layout('remove','east');
				}
			}
		});
		bindEnterEvent('sName',searchFrom,'easyui');//回车键查询	
	});
	
	//查询
	function searchFrom(){
		var name = $('#sName').textbox('getValue');
	    $('#list').datagrid('load',{
			name:name
		});
	}	
	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function clears(){
		$('#sName').textbox('setValue','');
		searchFrom();
	}
	//添加
   	function add(){
   		Adddilog('栏目信息',"<%=basePath%>sys/shortcut/addMenu.action",'40%','90%');
   	}
	
	//删除
   	function del(){
   		var rows = $('#list').datagrid('getChecked');
    	if (rows.length > 0) {//选中几行的话触发事件	  
    		$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
					if (res){
						var ids = '';
						for(var i=0; i<rows.length; i++){
							if(ids!=''){
								ids += ',';
							}
							ids += rows[i].id;
						};
						$.messager.progress({text:'保存中，请稍后...',modal:true});
		 				$.ajax({
		 					url: "<%=basePath%>sys/shortcut/delShortMenu.action?delShortid="+ids,
		 					type:'post',
		 					success: function() {
		 						$.messager.progress('close');
		 						$.messager.alert("提示",'删除成功');
		 						$('#list').datagrid('reload');
		 						//删除后清空list已选中的项
		 						$('#list').datagrid('uncheckAll');
		 					}
		 				});
					}
		    });
    	}else{
    		$.messager.alert("提示","请选择要删除的信息！");
    		close_alert();
    	}
    	
   	}
	
	//刷新
	function reload(){
		$('#list').datagrid('reload');
	}
	
	//动态添加LayOut
	function AddOrShowEast(title, url,width,method,params) {
		if(!method){
			method="get";
		}
		if(!params){
			params={};
		}
		var eastpanel=$('#panelEast'); //获取右侧收缩面板
		if(eastpanel.length>0){ //判断右侧收缩面板是否存在
			$('#divLayout').layout('remove','east');
			$('#divLayout').layout('add', {
				region : 'east',
				width : width,
				split : true,
				href : url,
				method:method,
				queryParams:params,
				closable : true
			});
		}else{//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : width,
				split : true,
				href : url,
				method:method,
				queryParams:params,
				closable : true
			});
		}
	}
			
    //加载模式窗口
	function Adddilog(title, url, width, height) {
		$('#menuWin').dialog({    
		    title: title,    
		    width: width,    
		    height: height,    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
	}
    
	//打开模式窗口
	function openDialog() {
		$('#roleWin').dialog('open'); 
	}
	
	//关闭模式窗口
	function closeDialog() {
		$('#roleWin').dialog('close');  
	}
	
	//关闭模式窗口
	function fun(value,row,index){
	if(value == 1){
		return '系统栏目';
	}else if(value == 2){
		return '一般栏目';
	}
}
	</script>
	</body>
</html>