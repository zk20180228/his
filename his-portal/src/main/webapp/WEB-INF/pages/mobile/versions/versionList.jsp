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
<title>- 版本升级界面 -</title>
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
							版本号：<input class="easyui-numberbox" data-options="prompt:'请输入版本号'"  id="queryNum"/>
							&nbsp;版本名称：<input class="easyui-textbox" data-options="prompt:'请输入版本名称'" id="queryName"/>
							&nbsp;是否强制更新：<input class="easyui-combobox" id="queryFlg" value="7" />
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
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
						<th data-options="field:'id',checkbox:true,hidden:true" ></th>
						<th data-options="field:'apkNewestVnum',align:'center',width:'5%'">最新版本号</th>
						<th data-options="field:'apkVersionName',align:'center',width:'14%'">版本名称</th>
						<th data-options="field:'forceUpdateFlg',formatter:formatFlg,align:'center',width:'5%'">是否强制更新</th>
						<th data-options="field:'apkClearCache',formatter:formatCache,align:'center',width:'5%'">是否清理缓存</th>
						<th data-options="field:'apkDownloadAddr',align:'center',width:'27%'">apk下载地址</th>
						<th data-options="field:'apkDownloadQRAddr',align:'center',width:'27%'">二维码</th>
						<th data-options="field:'apkDownloadFixedAddr',align:'center',width:'14%'">二维码下载地址</th>
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
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
</body>
<script type="text/javascript">
	$(function(){
		//类型
		$('#queryFlg').combobox({
			valueField : 'id',	
			textField : 'value',
			editable : false,
			data : [{id : 7, value : '全部'},{id : 1, value : '否'},{id : 2, value : '是'}]
		});
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url: '<%=basePath %>mosys/updateVersion/findVersionList.action',
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onDblClickRow: function (rowIndex, rowData) {//双击查看
				AddOrShowEast('查看',"<%=basePath %>mosys/updateVersion/toViewVersion.action?id="+rowData.id);
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
		
		bindEnterEvent('queryNum',searchFrom,'easyui');
		bindEnterEvent('queryName',searchFrom,'easyui');
		bindEnterEvent('queryFlg',searchFrom,'easyui');
	});
	
	/**
	 * 查询
	*/
	function searchFrom() {
		var queryNum = $('#queryNum').numberbox('getValue');
		var queryName = $('#queryName').textbox('getValue');
		var queryFlg = $('#queryFlg').combobox('getValue');
		$('#list').datagrid('load',{queryNum:queryNum,queryName:queryName,queryFlg:queryFlg});
	}
	
	/**
	 * 重置
	*/
	function clearw(){
		$('#queryNum').numberbox('clear');
		$('#queryName').textbox('clear');
		$('#queryFlg').combobox('setValue',7);
		searchFrom();
	}
	
	/**
	 * 格式化是否强制更新
	*/
	function formatFlg(value){
		if(value==2){
			return '是';
		}else if(value==1){
			return '否';
		}else{
			return '';
		}
	}
	/**
	 * 格式化是否更新缓存
	*/
	function formatCache(value){
		if(value==2){
			return '是';
		}else if(value==1){
			return '否';
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
						url:"<%=basePath%>mosys/updateVersion/delVersions.action",
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
		   AddOrShowEast('添加',"<%=basePath %>mosys/updateVersion/toAddVersion.action");
	}
	//修改	
	function edit(){
		var row = $('#list').datagrid('getSelected');
		  if(row != null){
               AddOrShowEast('编辑',"<%=basePath %>mosys/updateVersion/toEditVersion.action?id=" + row.id);
   		  }else{
	   			$.messager.alert('提示','请点中要修改的版本信息！');	
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
