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
	<title>采购计划暂存列表</title>
<body>
	<script type="text/javascript">
	var addAndEdit;
	//加载页面
	$(function() {
		var id = "${id}"; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				var row = $('#list').datagrid('getSelected'); //获取当前选中行    
                if(row){
		    		window.opener.$("#masterId").val(rowData.id);
		    		window.opener.searchReload();
		    		window.close();
			   	}
			}
		});
		
		//回车查询
        bindEnterEvent('name',searchFrom,'easyui');//查询条件
	});
	
	function reload(){
		//实现刷新栏目中的数据
		$('#list').datagrid('reload');
	}
	
	//查询
	function searchFrom() {
		var name = $.trim($('#name').val());
		$('#list').datagrid('load', {
			depotName : name,
		});
	}
	
	// 列表查询重置
	function searchReload() {
		$('#name').textbox('setValue','');
		searchFrom();
	}
	
	//替换字符
	function replaceTrueOrFalse(val){
		if(val == 0){
			return '启用';
		}
		if(val == 1){
			return '停用';
		}
	}
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" fit=true>
			<div data-options="region:'center',split:false,border:false" style=" width: 100%;">
				<input type="hidden" value="${id}" id="id"></input>
				<table id="list" style="height: 557px;" data-options="fit:true,url:'${pageContext.request.contextPath}/mat/plan/findZCZBList.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th field="ck" checkbox="true"></th>
							<th data-options="field:'procurementNo', width : 300">
								采购流水号
							</th>
							<th data-options="field:'procurementDeptName', width : 300">
								采购仓库
							</th>
							<th data-options="field:'createUser', width : 200">
								创建人
							</th>
							<th data-options="field:'createTime', width : 300">
								创建时间
							</th>
						</tr>
					</thead>
				</table>
		</div>
	</div>
		<div id="toolbarId">
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
</body>
</html>