<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>- 设备管理界面 -</title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9,EmulateIE8,6"/>
<script src="<%=basePath%>javascript/js/jquery-1.7.2.js" type="text/javascript"></script>
<%@ include file="/common/metas.jsp"%>
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
							<td  style="width: 300px;">
							<input class="easyui-textbox" id="queryName" data-options="prompt:'用户账户、设备码、手机号回车查询'" style="width: 170px;"/>
							&nbsp;是否挂失：<input class="easyui-combobox" id="queryIsLost" value="7" />
							&nbsp; <input type="radio" id="queryWhite" name="whiteOrBlack" onclick="boxmazui()" >白名单</input>
               					   <input type="radio" id="queryBlack" name="whiteOrBlack" onclick="boxmazui()" >黑名单</input>
               					   <input type="hidden" id="whiteOrBlack"  ></input>
							&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="clearw()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
				</form>
		</div>
		<div data-options="region:'center'" style="height: 89%;border-top:0">
			<table id="list" fit="true" data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'id',hidden:true" ></th>
						<th data-options="field:'user_account',align:'center',width:'15%'">用户账户</th>
						<th data-options="field:'machine_code',align:'center',width:'15%'">设备码</th>
						<th data-options="field:'machine_mobile',align:'center',width:'15%'">手机号</th>
						<th data-options="field:'is_lost',formatter:formatFlg,align:'center',width:'10%'">是否挂失</th>
						<th data-options="field:'is_white',formatter:formatFlg,align:'center',width:'10%'">是否白名单</th>
						<th data-options="field:'is_black',formatter:formatFlg,align:'center',width:'10%'">是否黑名单</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="temWins">
	</div>
	<div id="toolbarId">
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		<a href="javascript:void(0)" onclick="lossData()" class="easyui-linkbutton" data-options="iconCls:'icon-02',plain:true">挂失</a>
		<a href="javascript:void(0)" onclick="activateData()" class="easyui-linkbutton" data-options="iconCls:'icon-2012080412263',plain:true">激活</a>
		<a href="javascript:void(0)" onclick="moveWhite()" class="easyui-linkbutton" data-options="iconCls:'icon-change',plain:true">移至白名单</a>
		<a href="javascript:void(0)" onclick="moveBlack()" class="easyui-linkbutton" data-options="iconCls:'icon-change',plain:true">移至黑名单</a>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
</body>
<script type="text/javascript">
var addr="${addr}"
	$(function(){
		//类型
		$('#queryIsLost').combobox({
			valueField : 'id',	
			textField : 'value',
			editable : false,
			data : [{id : 7, value : '全部'},{id : 1, value : '否'},{id : 2, value : '是'}]
		});
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url: '<%=basePath %>mosys/machineManage/findMachineManageList.action',
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onDblClickRow: function (rowIndex, rowData) {//双击查看
				AddOrShowEast('查看',"<%=basePath %>mosys/machineManage/toViewMachine.action?id="+rowData.id);
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
		bindEnterEvent('queryIsLost',searchFrom,'easyui');
	});
	
	/**
	 * 查询
	*/
	function searchFrom() {
		if ($('#queryWhite').is(':checked')) {
			$('#whiteOrBlack').val(1);
		}
		if ($('#queryBlack').is(':checked')) {
			$('#whiteOrBlack').val(2);
		}
		var whiteOrBlack=$('#whiteOrBlack').val();
		var queryName = $('#queryName').textbox('getValue');
		var queryIsLost = $('#queryIsLost').combobox('getValue');
		$('#list').datagrid('load',{whiteOrBlack:whiteOrBlack,queryName:queryName,queryIsLost:queryIsLost});
	}
	
	
	function boxmazui(){
		searchFrom();
	}
	/**
	 * 重置
	*/
	function clearw(){
		$('#queryWhite').attr("checked",false); 
		$('#queryBlack').attr("checked",false); 
		$('#whiteOrBlack').val("");
		$('#queryName').textbox('clear');
		$('#queryIsLost').combobox('setValue',7);
		searchFrom();
	}
	
	/**
	 * 格式化
	*/
	function formatFlg(value){
		if(value==2){
			return '√';
		}else if(value==1){
			return '×';
		}else{
			return '';
		}
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
						url:"<%=basePath%>mosys/machineManage/delMachines.action",
						async:false,
						cache:false,
						data:{'ids':ids},
						type:"POST",
						success:function(data){
							$.messager.alert("提示","删除成功!");
							reload();
						}
					});
	  		    }
			})
		}else{
			$.messager.alert('提示','请勾选要删除的信息！');	
			close_alert();
		}
	}
	
	
	/**
	 * 挂失
	*/
	function lossData() {
		var rows = $('#list').datagrid('getChecked');
		if(rows.length > 0){
			if(rows.length==1){
				if(rows[0].is_lost==2){
					$.messager.alert('提示','该条信息已是挂失状态！');	
					close_alert();
					return;
				}
			}
		}else{
			$.messager.alert('提示','请勾选要挂失的信息！');	
			close_alert();
		}
		$.messager.confirm('提示','您确认要挂失吗？',function(r){    
  		    if (r){ 
  		    	var ids = '';
  		    	var machines='';
  				for(var i = 0; i < rows.length; i++){
  					if(ids != ''){
  						ids += ','+rows[i].id;
  					}else{
  						ids += rows[i].id;
  					}
  				}
  				for(var i = 0; i < rows.length; i++){
  					if(machines != ''){
  						machines += ';'+rows[i].user_account+"_"+rows[i].machine_code;
  					}else{
  						machines += +rows[i].user_account+"_"+rows[i].machine_code;
  					}
  				}
  				$.ajax({
  					url:"<%=basePath%>mosys/machineManage/lossMachines.action",
  					async:false,
  					cache:false,
  					data:{'ids':ids,'userAndMach':machines},
  					type:"POST",
  					success:function(data){
  						$.messager.alert("提示","挂失成功!");
  						reload();
  					}
  				});
  		    }
		});
	}
	
	/**
	 * 激活
	*/
	function activateData() {
		var rows = $('#list').datagrid('getChecked');
		if(rows.length > 0){
			if(rows.length==1){
				if(rows[0].is_lost==1){
					$.messager.alert('提示','该条信息已是激活状态！');	
					close_alert();
					return;
				}
			}
		}else{
			$.messager.alert('提示','请勾选要激活的信息！');	
			close_alert();
		}
		
		$.messager.confirm('提示','您确认要激活吗？',function(r){    
  		    if (r){  
  		    	var ids = '';
  		    	var machines='';
  				for(var i = 0; i < rows.length; i++){
  					if(ids != ''){
  						ids += ','+rows[i].id;
  					}else{
  						ids += rows[i].id;
  					}
  				}
  				for(var i = 0; i < rows.length; i++){
  					if(machines != ''){
  						machines += ';'+rows[i].user_account+"_"+rows[i].machine_code;
  					}else{
  						machines += +rows[i].user_account+"_"+rows[i].machine_code;
  					}
  				}
  				$.ajax({
  					url:"<%=basePath%>mosys/machineManage/activateMachines.action",
  					async:false,
  					cache:false,
  					data:{'ids':ids,'userAndMach':machines},
  					type:"POST",
  					success:function(data){
  						$.messager.alert("提示","激活成功!");
  						reload();
  					}
  				});
  		    }
		});
	}
	
	//添加
		function add(){
		   AddOrShowEast('添加',"<%=basePath %>mosys/machineManage/toAddMachine.action");
	}
	//修改	
	function edit(){
		var row = $('#list').datagrid('getSelected');
		  if(row != null){
               AddOrShowEast('编辑',"<%=basePath %>mosys/machineManage/toEditMachine.action?id=" + row.id);
   		  }else{
	   			$.messager.alert('提示','请点中要修改信息！');	
				close_alert();
   		  }
	}
	

	/**
	 * 刷新
	*/
	function reload() {
		$('#divLayout').layout('remove','east');
		$('#list').datagrid('reload');
	}
	/**
	 * 动态添加标签页
	 * @author  zxl
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-05-21
	 * @version 1.0
	 */
	function AddOrShowEast(title, url) {
		var eastpanel = $('#panelEast'); //获取右侧收缩面板
		if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
			//重新装载右侧面板
			$('#divLayout').layout('panel', 'east').panel({
				href : url
			});
		} else {//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : 580,
				title:title,
				split : true,
				maxHeight:820,
				href : url
				
			});
		}
	}
	
	/**
	 * 移至白名单
	*/
	function moveWhite() {
		var rows = $('#list').datagrid('getChecked');
		if(rows.length > 0){
			if(rows.length==1){
				if(rows[0].is_white==2){
					$.messager.alert('提示','该账户已在白名单！');	
					close_alert();
					return;
				}
			}
			for(var i = 0; i < rows.length; i++){
				if(rows[i].is_lost==2){
					$.messager.alert('提示','您所选择账户有已经挂失的，请先激活再移动！');	
					close_alert();
					return;
				}
			}
		}else{
			$.messager.alert('提示','请勾选要移动的信息！');	
			close_alert();
		}
		
		$.messager.confirm('提示','您确认要移至白名单吗？',function(r){    
  		    if (r){  
  		    	var ids = '';
  				for(var i = 0; i < rows.length; i++){
  					if(rows[i].is_lost==2){
  						$.messager.alert('提示','您所选择账户有已经挂失的，请先激活再移动！');	
  						close_alert();
  						return;
  					}
  					
  					if(ids != ''){
  						ids += ','+rows[i].id;
  					}else{
  						ids += rows[i].id;
  					}
  				}
  				var userAndMach = '';
  				for(var i = 0; i < rows.length; i++){
  					if(userAndMach != ''){
  						userAndMach += ';'+rows[i].user_account+"_"+rows[i].machine_code;
  					}else{
  						userAndMach += rows[i].user_account+"_"+rows[i].machine_code;
  					}
  				}
  				$.ajax({
  					url:"<%=basePath%>mosys/machineManage/moveToWhite.action",
  					async:false,
  					cache:false,
  					data:{'ids':ids,'userAndMach':userAndMach},
  					type:"POST",
  					success:function(data){
  						$.messager.alert("提示","移动成功!");
  						reload();
  					}
  				});
  		    }
		});
	}
	
	/**
	 * 移至黑名单
	*/
	function moveBlack() {
		var rows = $('#list').datagrid('getChecked');
		if(rows.length > 0){
			if(rows.length==1){
				if(rows[0].is_black==2){
					$.messager.alert('提示','该账户已在黑名单！');	
					close_alert();
					return;
				}
			}
		}else{
			$.messager.alert('提示','请勾选要移动的信息！');	
			close_alert();
		}
		
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
  				var userAndMach = '';
  				for(var i = 0; i < rows.length; i++){
  					if(userAndMach != ''){
  						userAndMach += ';'+rows[i].user_account+"_"+rows[i].machine_code;
  					}else{
  						userAndMach += rows[i].user_account+"_"+rows[i].machine_code;
  					}
  				}
  				$.ajax({
  					url:"<%=basePath%>mosys/machineManage/moveToBlack.action",
  					async:false,
  					cache:false,
  					data:{'ids':ids,'userAndMach':userAndMach},
  					type:"POST",
  					success:function(data){
  						$.messager.alert("提示","移动成功!");
  						reload();
  					}
  				});
  		    }
		});
	}
	
	/* 
	* 关闭界面
	*/
	function closeLayout(flag){
		$('#temWins').layout('remove','east');
		if(flag == 'edit'){
			$('#list').datagrid('reload');
		}
	}
	
</script>
</html>
