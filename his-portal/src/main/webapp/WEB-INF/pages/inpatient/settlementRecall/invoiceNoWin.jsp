<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<body>
   <div id="divLayout" class="easyui-layout" fit=true>
   		<div data-options="region:'center',split:false" style="width:100%;height:100%;">
			<table id="dataList" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarIdRoleMenuView'">
				<thead>
					<tr>
						<th data-options="field:'invoiceNo'" width="95%">发票号</th>
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
		$('#dataList').datagrid({
			url:'<%=basePath %>inpatient/settlementRecall/queryInvoiceNo.action',
			queryParams:{invoiceNos:"${invoiceNos}"},
			onDblClickRow: function (rowIndex, rowData) {//双击查看
				var a = getInvoidNoUtil('#dataList');
				if(getIdUtil('#dataList').length!=0){
					//带回至父页面 $('#PViewMenuId').val(getInvoidNoUtil('#dataList'));
					queryByInvoiceNo(getInvoidNoUtil('#dataList'));
					$('#menuWin').dialog('close');
			   	}
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
