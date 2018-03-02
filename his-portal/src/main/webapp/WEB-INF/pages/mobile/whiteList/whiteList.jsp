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
<script src="<%=basePath%>easyui/jquery-1.7.2.js" type="text/javascript"></script>
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
							用户账户：<input class="easyui-textbox" id="queryName" data-options="prompt:'输入用户账户回车查询'" style="width: 170px;"/>
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
						<th data-options="field:'userName',align:'center',width:'15%'">用户姓名</th>
						<th data-options="field:'userSex',formatter:formatSex,align:'center',width:'7%'">性别</th>
						<th data-options="field:'deptName',align:'center',width:'20%'">科室名称</th>
						<th data-options="field:'machine_code',align:'center',width:'15%'">设备码</th>
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
		<a href="javascript:void(0)" onclick="moveBlack()" class="easyui-linkbutton" data-options="iconCls:'icon-change',plain:true">移动至黑名单</a>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
</body>
<script type="text/javascript">
var addr="${addr}"
var sexList = '';
	$(function(){
		//性别渲染
		$.ajax({
			url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
			data:{"type":"sex"},
			type:'post',
			success: function(data) {
				sexList = data;
			}
		});
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url: '<%=basePath %>mosys/whiteList/findWhiteLists.action',
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onDblClickRow: function (rowIndex, rowData) {//双击查看
				AddOrShowEast('查看',"<%=basePath %>mosys/whiteList/toViewWhite.action?id="+rowData.id);
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
		$('#list').datagrid('load',{queryName:queryName});
	}
	
	//格式化性别
	function formatSex(value){
		if(!value){
			return '';
		}
		for(var i = 0; i < sexList.length; i++){
			if(value == sexList[i].encode){
				return sexList[i].name;
			}
		}
	}
	/**
	 * 重置
	*/
	function clearw(){
		$('#queryName').textbox('clear');
		searchFrom();
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
						url:"<%=basePath%>mosys/whiteList/delWhiteLists.action",
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
	
	
	
	//添加
		function add(){
		   AddOrShowEast('添加',"<%=basePath %>mosys/whiteList/toAddWhite.action");
	}
	//修改	
	function edit(){
		var row = $('#list').datagrid('getSelected');
		  if(row != null){
               AddOrShowEast('编辑',"<%=basePath %>mosys/whiteList/toEditWhite.action?id=" + row.id);
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
	  					url:"<%=basePath%>mosys/whiteList/moveBlack.action",
	  					async:false,
	  					cache:false,
	  					data:{'ids':ids},
	  					type:"POST",
	  					success:function(data){
	  						$.messager.alert("提示","移动成功!");
	  						reload();
	  					}
	  				});
	  		    }
			});
		}else{
			$.messager.alert('提示','请勾选要移动的信息！');	
			close_alert();
		}
		
	
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
