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
<body>
	<div id="divLayout" class="easyui-layout" fit=true>
   		<div data-options="region:'center',split:false" style="width:100%;height:100%;">
			<table id="dataList" calss="easyui-datagrid" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarIdRoleMenuView'">
				<thead>
					<tr>
						<th data-options="field:'id'" width="45%">手术序号</th>
						<th data-options="field:'name'" width="55%">手术名称</th>
					</tr>
				</thead>
			</table>
		</div>
   </div>
	<div id="toolbarIdRoleMenuView">
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-delete', plain:true"  onclick="closeWin()">关闭</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="reloadRoleMenuView()">刷新</a>
	</div>
	<script type="text/javascript">
	var dataRightListView="";
	//初始化页面
	$(function(){
	    var id='${id}'; //存储数据ID
	    var ids='${ids}';
		$('#dataList').datagrid({
			url:'<%=basePath %>operation/operationbusiness/queryBusinessOperationapplyID.action',
			queryParams:{ids:ids},
			method:'post',
			onDblClickRow: function (rowIndex, rowData) {//双击查看
			             rowData.name
							
			} 
		});
	});
	//刷新
	function reloadRoleMenuView(){
		$('#dataList').datagrid('reload');
	}
	function getInvoidNoUtil(tableID){
		   var row = $(tableID).datagrid("getSelections");  
	       var i = 0;    
	       var getInvoiceNo = ""; 
	       if(row.length!=1){
	       		$.messager.alert("操作提示", "请选择一条用户记录！","warning");
	       		return null;
	       }else{  
		   	 	 for(i;i<row.length;i++){ 
		            getInvoiceNo = row[i].invoiceNo; 
				 }
   			}
   			return getInvoiceNo;	
   }
	//关闭
	function closeWin(){
		$('#menuWin').dialog('close');
	}
	
	
	</script>
</body>
</html>