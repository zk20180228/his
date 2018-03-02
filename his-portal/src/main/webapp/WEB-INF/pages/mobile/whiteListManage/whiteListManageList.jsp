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
<title>-白名单（短信）管理-</title>
	<style type="text/css">
		.window .panel-header .panel-tool a{
			background-color: red;	
		}
		#panelEast{
			border:0
		}
	</style>
</head>
<body >
	<div id="divLayout" class="easyui-layout" data-options="fit:true"style="width:100%;height:100%">
		 <div data-options="region:'north'" style="height: 45px;">
				<form id="search" method="post">
					<table style="width:100%;border-bottom:1px solid #95b8e7;padding:6px;" class="changeskinBottom">
						<tr>
							<td  >
								<input class="easyui-textbox" id="queryName" data-options="prompt:'手机类别回车查询'" style="width: 150px;"/>
								<a href="javascript:void(0)" onclick="searchFrom()"class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
				</form>
		</div>
		<div data-options="region:'center'" style="height: 89%;border-top:0">
				<table id="list" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
					 <thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'id',hidden:true" ></th>
							<th data-options="field:'mobileCategory',width:250">手机类别</th>
							<th data-options="field:'mobileRemark',width:350">备注</th>
							<th data-options="field:'createTime',width:170">创建时间</th>
							</tr>
					</thead> 
				</table>				
		</div>
	</div>
	<div id="group"></div>
	<div id="toolbarId">
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		<a href="javascript:void(0)" onclick="moveBlack()" class="easyui-linkbutton" data-options="iconCls:'icon-change',plain:true">移动至黑名单</a>
		<a href="javascript:void(0)" onclick="initData()" class="easyui-linkbutton" data-options="iconCls:'icon-init',plain:true">白名单初始化</a>
	</div>
</body>
<script type="text/javascript">
	$(function(){
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url:'<%=basePath%>mosys/whiteListManage/findWhiteManage.action',
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
	
	/**
	 * 查询
	*/
	function searchFrom() {
		var queryName = $('#queryName').textbox('getValue');
		$('#list').datagrid('load',{mobileCategory:queryName});
	}
	
	
	/**
	 * 重置
	 * @author zxl
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function clears(){
		$('#queryName').textbox('setValue','');
		searchFrom();
		//closeDialog();
	}
	/**
	 * 删除
	*/
	function del() {
		var rows = $('#list').datagrid('getChecked');
		if(rows.length > 0){
			$.messager.confirm('提示','您确认要删除吗？',function(r){    
	  		    if (r){ 
	  		    	var ids = '';
	  				for(var i = 0; i < rows.length; i++){
	  					if(ids != ''){
	  						ids += ','+rows[i].id;
	  					}else{
	  						ids += rows[i].id;
	  					}
	  				}
	  				$.ajax({
	  					url:"<%=basePath%>mosys/whiteListManage/delWhiteManage.action",
	  					async:false,
	  					cache:false,
	  					data:{'ids':ids},
	  					type:"POST",
	  					success:function(data){
	  						$.messager.alert("提示",data.resMsg);
	  						if(data.resCode=="0"){
	  							reload();
	  						}
	  						
	  					}
	  				});
	  		    }
			})
		}else{
			$.messager.alert('提示','请勾选要删除的信息！');	
			close_alert();
		}
	}
	
	
	//添加
	function add(){
   		Adddilog("添加",'<%=basePath%>mosys/whiteListManage/addWhiteManage.action?');
	}
	//修改	
	function edit(){
		var row = $('#list').datagrid('getSelected');
		  if(row != null){
			  Adddilog("编辑",'<%=basePath%>mosys/whiteListManage/editWhiteManage.action?id='+ row.id);
   		  }else{
	   			$.messager.alert('提示','请点中要修改的信息！');	
				close_alert();
   		  }
	}

	/**
	 * 刷新
	*/
	function reload() {
		$('#list').datagrid('reload');
		//closeDialog();
		
	}
	//加载dialog
	function Adddilog(title, url) {
		$('#group').dialog({    
		    title: title,    
		    width: '400px',    
		    height:'165px',      
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
	}
	//打开dialog
	function openDialog() {
		$('#group').dialog('open'); 
	}
	//关闭dialog
	function closeDialog() {
		$('#group').dialog('close');  
	}
	
	function initData() {
		$.messager.confirm('提示','初始化前需要清空白名单数据，您确认要初始化数据吗？',function(r){    
  		    if (r){ 
  				$.ajax({
  					url:"<%=basePath%>mosys/whiteListManage/initData.action",
  					async:false,
  					cache:false,
  					type:"POST",
  					success:function(data){
  						$.messager.alert("提示",data.resMsg);
  						if(data.resCode=="0"){
  							clears();
  						}
  						
  					}
  				});
  		    }
		})
	}
	/**
	 * 移至黑名单
	*/
	function moveBlack() {
		var rows = $('#list').datagrid('getChecked');
		if(rows.length > 0){
			$.messager.confirm('提示','您确认要移至黑名单吗？',function(r){    
	  		    if (r){  
	  		    	var ids = '';
	  				for(var i = 0; i < rows.length; i++){
	  					if(ids != ''){
	  						ids += ','+rows[i].id;
	  					}else{
	  						ids += rows[i].id;
	  					}
	  				}
	  				$.ajax({
	  					url:"<%=basePath%>mosys/whiteListManage/moveBlack.action",
	  					async:false,
	  					cache:false,
	  					data:{'ids':ids},
	  					type:"POST",
	  					success:function(data){
	  						$.messager.alert("提示",data.resMsg);
	  						if(data.resCode=="0"){
	  							reload();
	  						}
	  					}
	  				});
	  		    }
			});
		}else{
			$.messager.alert('提示','请勾选要移动的信息！');	
			close_alert();
		}
		
	
	}
	
</script>
<style  type="text/css">
  .window .panel-header .panel-tool .panel-tool-close .panel-tool-close{
  	  	background-color: red;
  	}
 </style>
</html>
