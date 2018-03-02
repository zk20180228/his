<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>个人常用语管理</title>
<script type="text/javascript">
var menuAlias="${menuAlias}";
var account = "${account}";
console.log(account)
$(function(){
	dateFrom();
});
//查询
function searchFrom(){
	dateFrom();
}
function dateFrom(){
	$('#list').datagrid({
		rownumbers: true,
		pageSize: "20",
		fit:true,
		singleSelect:true,
		checkOnSelect:false,
		selectOnCheck:false,
		pageList: [20, 30, 50],
		pagination: false,
		method: "post",
		url: '<%=basePath%>oa/commonLg/findMyCommon.action',
		toolbar:'#toolbarId',
	});
}
		
		
		function add() {
			var code=$("#codes").val();
			 AddOrShowEast('EditForm','<%=basePath%>oa/commonLg/addCommon.action','post'
					   ,{"codes":code});
		}

		function edit() {
			if (getIdUtil("#list") != null) {
				console.log(getIdUtil("#list"));
				AddOrShowEast('EditForm', '<%=basePath%>oa/commonLg/editCommon.action?id='+ getIdUtil("#list"));
			}
		}

		function del() {
			//选中要删除的行
			var rows = $('#list').datagrid('getChecked');
			if (rows.length > 0) {//选中几行的话触发事件	                        
				$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
					if (res) {
						var ids = '';
						for (var i = 0; i < rows.length; i++) {
							if (ids != '') {
								ids += ',';
							}
							ids += rows[i].id;
						}
						$.ajax({
							url : "<%=basePath%>oa/commonLg/delCommon.action?ids="+ ids,
							type : 'post',
							success : function(data) {
													$.messager.progress('close');
													if (data.resCode == 'success') {
														$('#list').datagrid('reload');
													}
												}
											});
								}
							});
		} else {
			$.messager.alert('提示信息', '请选择要删除的信息！');
			setTimeout(function() {
				$(".messager-body").window('close');
			}, 3500);
		}
		dateFrom();
	}

	function reload() {
		//实现刷新栏目中的数据
		dateFrom();
	}


	//按回车键提交表单！
	$('#searchTab').find('input').on('keyup', function(event) {
		if (event.keyCode == 13) {
			searchFrom();
		}
	});
	//列表查询重置
	function searchReload() {
		$('#name').textbox('setValue', '');
		$('#code').textbox('setValue', '');
		searchFrom();
	} 
	/**
	 * 动态添加LayOut
	 * @author  liujl
	 * @param title 标签名称
	 * @param url 跳转路径
	 * @date 2015-05-21
	 * @modifiedTime 2015-6-18
	 * @modifier liujl
	 * @version 1.0
	 */
	function AddOrShowEast(title, url, method, params) {
		if (!method) {
			method = "get";
		}
		if (!params) {
			params = {};
		}
		var eastpanel = $('#panelEast'); //获取右侧收缩面板
		if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
			//重新装载右侧面板
			$('#divLayout').layout('panel', 'east').panel({
				href : url,
				method : method,
				queryParams : params,
			});
		} else {//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : '30%',
				split : true,
				href : url,
				method : method,
				queryParams : params,
				closable : true
			});
		}
	}
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout"
		data-options="fit:true,border:false">
		<div data-options="region:'center',border:false" style="width: 100%;">
			<input type="hidden" id="codes">
			<table id="list" data-options="pagination:true,fit:true" >
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true"></th>
						<th data-options="field:'id',hidden:true">id</th>
						<th data-options="field:'tableName',width:'20%'" align="center">表单名称</th>
<!-- 						<th data-options="field:'userName',width:'7%'" align="center">人员名称</th> -->
						<th data-options="field:'common',width:'20%'" align="center">常用语</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>


	<div id="toolbarId">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
			<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
			<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</div>

</body>
</html>