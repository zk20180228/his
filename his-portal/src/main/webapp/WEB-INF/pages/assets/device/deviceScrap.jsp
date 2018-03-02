<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>设备入库审批管理</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
	<div id="cc" class="easyui-layout" fit="true";"> 
    	<div data-options="region:'center',border:false" ">
			<form id="editForm" method="post" enctype="multipart/form-data">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px auto 0;">
				<input type="hidden" id="ids" value="${device.id }">
					<tr>
						<td class="honry-lable">不通过原因：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="reason"
		    			data-options="required:true,width:400,height:200"/></td>
					</tr>
				</table>
			</form>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:close();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">取消</a>
				<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>
			</div>
   		</div>
	</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
	$(function(){
		window.onbeforeunload = function() { 
	}});
	function submit(){
		var reason = $('#reason').textbox('getValue');
		var ids = $('#ids').val();
		$.messager.progress({text:'保存中，请稍后...',modal:true});
		$('#editForm').form('submit', {
			url : "<c:url value='/assets/device/disableDevice.action'/>",
			queryParams:{id:ids,reason:reason},
			onSubmit : function() {
				if (!$('#editForm').form('validate')) {
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
			},
			success : function(data) {
				var dataMap = eval('('+data+')');
				$.messager.progress('close');
				if('success'==dataMap.resCode){
					$('#editForm').form('reset');
					closeDialog();
					$('#list').datagrid('reload');
					$.messager.alert('提示',dataMap.resMsg);
				}else{
					$.messager.alert('提示',dataMap.resMsg);
				}
			}
		});
	}
	function close(){
		$('#editForm').form('reset');
		$('#add').dialog('close');  
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>