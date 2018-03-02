<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
<div >
	<div style="fit:true;hight:100%;width:100%">
		<button type="button" onclick="querys()" class="easyui-linkbutton" iconCls="icon-search">查询</button>
	</div>
	<div >
		<table id="selectDialoglist" style="width: 100%;"></table>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		//查询当前登录科室的发药窗口
		$('#selectDialoglist').datagrid({
			data:list,
			rownumbers:true,
   			fitColumns:true,
   			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
   			columns:[[
					  {field:'ck',checkbox:true},
					  {field:'inpatientNo',title:'住院流水号',width : '30%'},
			          {field:'outDate',title:'出院日期', width : '30%'},
			          {field:'inDate',title:'入院日期', width : '35%' },
			  ]]
		});
	});
	function querys(){
		var rows=$('#selectDialoglist').datagrid('getChecked');
		if(rows.length==0){
			$.messager.alert("提示","请选择需要查询的数据");
			return;
		}else{
			var lsh="";
			for(var i=0;i<rows.length;i++){
				var row=rows[i];
				lsh+="'"+row.inpatientNo+"'";
				if(i<rows.length-1){
					lsh+=",";
				}
			}
			queryByChecks(lsh);
			$('#dialog').dialog('close');  
		}
	}
</script>
</body>
</html>