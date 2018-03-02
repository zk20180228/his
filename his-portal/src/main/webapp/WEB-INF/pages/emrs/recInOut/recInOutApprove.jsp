<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>电子病历借阅审核编辑界面</title>
<%@ include file="/common/metas.jsp"%>

</head>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="iconCls:'icon-form',border:false" style="width:570px;">
		<div >
			<form id="editForm" method="post">
				<input type="hidden" id="inoutState" name="emrRecInOut.inoutState" value="${emrRecInOut.inoutState }">
				<input type="hidden" id="id" name="emrRecInOut.id" value="${emrRecInOut.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td class="honry-lable">档案编号:</td>
						<td class="honry-info"><input class="easyui-textbox" id="inoutRecid" name="emrRecInOut.inoutRecid" value="${emrRecInOut.inoutRecid }" data-options="editable:false"  style="width:200px" /></td>
					</tr>
					<tr id = "cod">
						<td class="honry-lable">住院流水号:</td>
						<td class="honry-info"><input class="easyui-textbox" id="cardId" name="emrRecInOut.cardId"  data-options="editable:false" style="width:200px"/></td>
					</tr>
					<tr>
						<td class="honry-lable">患者姓名:</td>
						<td class="honry-info"><input class="easyui-textbox" id="patientName" name="emrRecInOut.patientName"  data-options="editable:false" style="width:200px"/></td>
					</tr>
					<tr>
						<td class="honry-lable">借阅申请人:</td>
						<td class="honry-info"><input class="easyui-textbox" id="appperson" name="emrRecInOut.appperson"  data-options="editable:false" style="width:200px" /></td>
					</tr>
					<tr>
						<td class="honry-lable">借阅申请时间:</td>
						<td class="honry-info"><input class="easyui-textbox" id="inoutAppdate" name="emrRecInOut.inoutAppdate"  data-options="editable:false" style="width:200px" /></td>
					</tr>
					<tr>
						<td class="honry-lable">借阅期限(小时):</td>
						<td class="honry-info"><input class="easyui-numberbox" id="inoutDeadlinr" name="emrRecInOut.inoutDeadlinr" value="${emrRecInOut.inoutDeadlinr }" data-options="required:true,min:0,precision:2" style="width:200px" /></td>
					</tr>
				</table>
				<div style="text-align:center;padding:5px">
				<shiro:hasPermission name="${menuAlias}:function:add">
					<a href="javascript:submit(3);" class="easyui-linkbutton" id="addContinue" data-options="iconCls:'icon-shenhetongguo'">不同意</a>
					<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-shenheshibai'">同意</a>
				</shiro:hasPermission>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDilog()">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var rowData = $('#list').datagrid('getSelected');
		$('#cardId').val(rowData.cardId);
		$('#patientName').val(rowData.patientName);
		$('#appperson').val(rowData.appperson);
		$('#inoutAppdate').val(rowData.inoutAppdate);
		
	});
	/**
	 * 表单提交
	 */
	function submit(state) {
		$('#editForm').form('submit',{
			url: '<%=basePath %>emrs/emrRecInOut/approveRecInOut.action',
			onSubmit:function(param){
				if (!$('#editForm').form('validate')) {
					$.messager.alert('提示','请输入借阅时限！');
					setTimeout(function(){$(".messager-body").window('close')},3500);
					return false;
				}else{
					$.messager.progress({text:'保存中，请稍后...',modal:true});
					$("#inoutState").val(state);
				}
			},  
			success:function(data){
				$.messager.progress('close');
				$.messager.alert('提示','保存成功');
					$("#list").datagrid("reload");
					closeDilog();
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','保存失败！');	
			} 
		});
	}
	</script>
</body>