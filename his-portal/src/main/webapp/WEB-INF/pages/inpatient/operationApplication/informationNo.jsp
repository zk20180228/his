<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-layout" style="width:100%;height:100%;" fit=true>
		<table id="list" border="false"
			style="font-size: 14px;"
			data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarIdRoleMenuView'">
			<thead>
				<tr>
					<th data-options="field:'id'" width="25%" >
						编号
					</th>
					<th data-options="field:'opName1'" width="20%">
						手术名称
					</th>
					<th data-options="field:'preDate'" width="20%">
						预约时间
					</th>
					<th data-options="field:'opDoctor',formatter:functionxuexing" width="20%">
						手术医生
					</th>
				</tr>
			</thead>
		</table>  
		<input id="ids" value="${pids}" type="hidden"/>
	</div>
	<div id="toolbarIdRoleMenuView">
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-delete', plain:true"  onclick="closeWin()">关闭</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="reloadRoleMenuView()">刷新</a>
	</div>
	<script type="text/javascript">
	var blood="";
	$(function(){
		var ids = $('#ids').val();
		//查询
		$('#list').datagrid({
			url:"<%=basePath %>operation/operationbusiness/queryBusinessOperationapplyID.action?ids="+ids,
			onBeforeLoad:function (param) {
				$.ajax({
					url : '<%=basePath %>baseinfo/employee/employeeCombobox.action',
					type:'post',
					success: function(unitData) {
						blood = eval("("+unitData+")");
					}
				});
		 	},
		 	onDblClickRow: function (rowIndex, rowData) {//双击查看
	               var a = getInvoidNoUtil('#list');
					if(getIdUtil('#list').length!=0){
					    IdQuery(getInvoidNoUtil());
						$('#menuWin').dialog('close');
				   	}
			}
		});
	});
	function getInvoidNoUtil(){
		   var row = $('#list').datagrid("getSelections");  
	       var i = 0;    
	       var id = ""; 
	       if(row.length!=1){
	       		$.messager.alert("操作提示", "请选择一条用户记录！","warning");
	       		return null;
	       }else{  
		   	 	 for(i;i<row.length;i++){ 
		            id = row[i].id; 
				 }
			}
			return id;	
	}
	function functionxuexing(value,row,index){
		for ( var i = 0; i < blood.length; i++) {
			if (value == blood[i].id) {
				return blood[i].name;
			}
		}
	}
	//关闭
	function closeWin(){
		$('#menuWin').dialog('close');
	}
	//刷新
	function reloadRoleMenuView(){
		$('#dataList').datagrid('reload');
	}
	</script>
</body>
</html>