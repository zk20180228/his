<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>药品供货公司</title>
	<%@ include file="/common/metas.jsp"%>
	
<body>
	<script type="text/javascript">
	var addAndEdit;
	//加载页面
	$(function() {
		var id = "${id}"; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				if (getbachIdUtil('#list', 0, 0).length != 0) {
					addAndEdit = 1;
					closeLayout();
					AddOrShowEast('EditForm', '<%=basePath%>drug/supply/viewDrugSupplycompany.action?Id=' + getbachIdUtil('#list', 1, 0));
				}
			},onLoadSuccess:function(row, data){
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
		
		//回车查询
        bindEnterEvent('name',searchFrom,'easyui');//查询条件
	});
	
	function add(){
		closeLayout();
		AddOrShowEast('EditForm', '<%=basePath%>drug/supply/addSupply.action');
	}
	
	function edit(){
		closeLayout();
		var row = $('#list').datagrid('getSelected'); //获取当前选中行     
        if(row){
        	AddOrShowEast('EditForm','<%=basePath%>drug/supply/editSupply.action?id='+row.Id);
		}
	}
	
	
	
	function del1(){
		var rows = $('#list').datagrid('getChecked');
        if (rows.length > 0) {//选中几行的话触发事件	                        
		 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
				if (res){
					$.messager.progress({text:'删除中，请稍后...',modal:true});	// 显示进度条
					var ids = '';
					for(var i=0; i<rows.length; i++){
						if(ids!=''){
							ids += ',';
						}
						ids += rows[i].Id;
					};
					$.ajax({
						url: '<%=basePath %>drug/supply/deleteSupply.action?ids='+ids,
						type:'post',
						success: function(data) {
							$.messager.progress('close');
							$.messager.alert('提示',data.resMsg);
							if(data.resCode=='success'){
								$('#list').datagrid('reload');
							}
						}
					});
				}
           });
       }else{
    	   $.messager.alert('操作提示',"请选择要删除的信息！");
    	   setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
       }
}

	
	function reload(){
		//实现刷新栏目中的数据
		$('#list').datagrid('reload');
	}
	
	//查询
	function searchFrom() {
		var name = $.trim($('#name').val());
		$('#list').datagrid('load', {
			companyName : name,
		});
	}
	//获取数据表格选中行的ID checked=0否则是获取勾选行的ID ，获取多个带有拼接''的ID str=0，否则不带有''，
	function getbachIdUtil(tableID, str, checked) {
		var row;
		if (checked == 0) {
			row = $(tableID).datagrid("getSelections");
		} else {
			row = $(tableID).datagrid("getChecked");
		}
		var dgID = "";
		if (row.length < 1) {
			$.messager.alert("操作提示", "请选择一条记录！", "warning");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
		var i = 0;
		for (i; i < row.length; i++) {
			if (str == 0) {
				dgID += "\'" + row[i].Id + "\'";
			} else {
				dgID += row[i].Id;
			}
			if (i < row.length - 1) {
				dgID += ',';
			} else {
				break;
			}
		}
		return dgID;
	}
	//删除选中table row 
	function del() {
		var rows = $('#list').datagrid("getChecked");
		var copyRows = [];
		for ( var j = 0; j < rows.length; j++) {
			copyRows.push(rows[j]);
		}
		for ( var i = 0; i < copyRows.length; i++) {
			var index = $('#list').datagrid('getRowIndex', copyRows[i]);
			$('#list').datagrid('deleteRow', index);
		}
	}
	/**
	 * 动态添加标签页
	 * @author  sunshuo
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-05-21
	 * @version 1.0
	 */
	function AddOrShowEast(title, url) {
		$('#divLayout').layout('add', {
			region : 'east',
			width : 580,
			split : true,
			border : false,
			href : url,
			closable : true
		});
	}
	//关闭Layout
	function closeLayout() {
		$('#divLayout').layout('remove', 'east');
	}
	
	// 列表查询重置
	function searchReload() {
		$('#name').textbox('setValue','');
		searchFrom();
	}
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" fit=true>
			<div data-options="region:'north',split:false,border:false,iconCls:'icon-search'" style="height: 40px;">
				<table cellspacing="0" cellpadding="0" border="0" style="padding: 7px 0px 5px 0px">
					<tr >
						<td>
							&nbsp;查询条件：<input class="easyui-textbox" name="name" id="name"  onkeydown="KeyDown()" style="width:180px"/>
						</td>
						<td>
						<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</shiro:hasPermission>
						<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',split:false,border:false" style=" width: 100%;">
				<input type="hidden" value="${id }" id="id"></input>
				<table id="list"  style="height: 557px" data-options="fit:true,url:'${pageContext.request.contextPath}/drug/supply/querySupplycompany.action?menuAlias=${menuAlias}',method:'post',selectOnCheck:false,rownumbers:true,idField: 'Id',striped:true,border:true,singleSelect:true,checkOnSelect:true,pagination:true,pageSize:20,pageList:[20,40,60,80,100],showRefresh:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th field="ck" checkbox="true"></th>
							<th data-options="field:'companyName', width :'10%'">
								名称
							</th>
							<th data-options="field:'companyPinyin', width : '10%'">
								拼音码
							</th>
							<th data-options="field:'companyWb', width : '10%'">
								五笔码
							</th>
							<th data-options="field:'companyInputcode', width : '10%'">
								自定义码
							</th>
							<th data-options="field:'companyLink', width : '10%'">
								联系方式
							</th>
							<th data-options="field:'companyBank', width : '10%'">
								开户银行
							</th>
							<th data-options="field:'companyAccount', width : '10%'">
								开户账号
							</th>
							<th data-options="field:'companyAddress', width : '10%'">
								地址
							</th>
							<th data-options="field:'companyGsp', width : '10%'">
								GSP
							</th>
							<th data-options="field:'companyRemark', width : '10%'">
								备注
							</th>
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
		<a href="javascript:void(0)" onclick="del1()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
</body>
</html>