<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>电子病历病历借阅归还</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
		//加载页面
		$(function(){
			//添加datagrid事件及分页
			$('#list').datagrid({
				pagination:true,
				pageSize:20,
				pageList:[20,30,50,80,100],
				queryParams : {'state' : '1'},
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
		function add(){
			var rows = $('#list').datagrid('getChecked');
			if (rows.length > 0) {//选中几行的话触发事件							
				$.messager.confirm('确认', '确定对选中的档案归还吗?', function(res){
					if (res){
						var ids = '';
						for(var i = 0; i < rows.length; i++){
							if(ids != ''){
								ids += ',' + rows[i].id;
							}else{
								ids = rows[i].id;
							}
						}
						var infoJson = JSON.stringify(rows);
						$.messager.progress({text:'保存中，请稍后...',modal:true});
						$.ajax({
							url: '<%=basePath %>emrs/emrRecInOut/saveReturnList.action',
							type:'post',
							data:{'ids' : ids},
							success: function() {
								$.messager.progress('close');
								$.messager.alert('提示','保存成功！');
								reload();
								rows.length = 0;
							}
						});
					}
				});
			}else{
				$.messager.alert('提示','请勾选要保存的记录！');
				setTimeout(function(){$(".messager-body").window('close')},3500);
			}
		}
		//刷新
		function reload(){
			//实现刷新栏目中的数据
			$('#list').datagrid('unselectAll');
			$('#list').datagrid('reload');
		}
		
	</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;">
		<div data-options="region:'center',border:false" style="height: 100%">
			<table id="list" fit="true" data-options="url:'<%=basePath %>emrs/emrRecInOut/emrRecInOutList.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:false,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'inoutRecid',width:'15%'">档案编号</th>
						<th data-options="field:'cardId',width:'15%'">住院流水号</th>
						<th data-options="field:'patientName',width:'10%'">患者姓名</th>
						<th data-options="field:'appperson',width:'10%'">借阅申请人</th>
						<th data-options="field:'inoutAppdate',width:'10%'">借阅申请时间</th>
						<th data-options="field:'inoutCheckUser',width:'10%'">借阅审核人</th>
						<th data-options="field:'inoutCheckDate',width:'10%'">借阅审核时间</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:save">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">保存</a>
		</shiro:hasPermission>
	</div>
</body>

