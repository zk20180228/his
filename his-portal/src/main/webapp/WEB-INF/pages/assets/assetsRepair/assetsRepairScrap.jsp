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
<title>设备采购计划申报</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
	<div id="cc" class="easyui-layout" fit="true";"> 
    	<div data-options="region:'center',border:false" ">
			<form id="editForm" method="post" enctype="multipart/form-data">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px auto 0;">
					<input type="hidden" id="userId" name="deviceDossier.id" value="${deviceDossier.id }">
					<input type="hidden" name="deviceDossier.officeCode" value="${deviceDossier.officeCode }">
					<input type="hidden" name="deviceDossier.officeName" value="${deviceDossier.officeName }">
					<input type="hidden"  name="deviceDossier.classCode" value="${deviceDossier.classCode }">
					<input type="hidden"  name="deviceDossier.className" value="${deviceDossier.className }">
					<input type="hidden"  name="deviceDossier.deviceNo" value="${deviceDossier.deviceNo }">
					<input type="hidden"  name="deviceDossier.deviceCode" value="${deviceDossier.deviceCode }">
					<input type="hidden"  name="deviceDossier.deviceName" value="${deviceDossier.deviceName }">
					<input type="hidden"  name="deviceDossier.meterUnit" value="${deviceDossier.meterUnit }">
					<input type="hidden"  name="deviceDossier.maintainNum" value="${deviceDossier.maintainNum}">
					<tr>
						<td class="honry-lable">报废原因：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="assetScrap"
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
		var assetScrap = $('#assetScrap').textbox('getValue');
		$.messager.progress({text:'保存中，请稍后...',modal:true});
		$('#editForm').form('submit', {
			url : "<c:url value='/assets/deviceDossier/assetScrap.action'/>",
			queryParams:{assetScrap:assetScrap},
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
					var state = 1;
					$('#tt').tabs('select',"待维修");
					$("#declared").datagrid({
						url: '<%=basePath %>assets/deviceDossier/queryAssetsRepair.action?state='+state,
						pageSize:10,
						pageList:[10,20,30,50,80,100],
						pagination:true
					})
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