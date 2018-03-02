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
	<link rel="stylesheet" type="text/css" href="<%=basePath %>themes/system/css/public.css">
	<style type="text/css">
		.window .panel-header .panel-tool a{
			background-color: red;	
		}
	</style>
<body>
	<div style="padding:10px" id="panelEast" >
		<form id="editForm"  method="post">
			<input type="hidden" id="id" name="machineManage.id" value="${machineManage.id}">
			<input type="hidden" id="accunt"  value="${machineManage.user_account}">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="0px" style="width:100%;border-left:0;" data-options="border:false">
				<tr>
					<td class="honry-lable">用户账户：</td>
					<td class="honry-info">
					<input  class="easyui-textbox"   id="userAccunt"  name="machineManage.user_account" value="${machineManage.user_account }"  data-options="required:true" style="width:200px" missingMessage="请输入用户账户"/></td>
				</tr>
				<tr>
					<td class="honry-lable">设备码：</td>
					<td class="honry-info">
					<input class="easyui-textbox" id="machineCode" name="machineManage.machine_code" value="${machineManage.machine_code }" data-options="required:true"  style="width:200px" missingMessage="请输入设备码,多个用逗号隔开"/></td>
				</tr>
				<tr>
					<td class="honry-lable">手机号：</td>
					<td class="honry-info">
					<input class="easyui-textbox" id="machineMobile" name="machineManage.machine_mobile" value="${machineManage.machine_mobile }" data-options="required:true" style="width:200px" missingMessage="请输入手机号,多个用逗号隔开"/></td>					
				</tr>
			</table>
			<div style="text-align:center;padding:5px">
				<c:if test="${machineManage.id==null }">
					<a href="javascript:checkMes(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
				</c:if>
				<a href="javascript:checkMes(2);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('edit')">关闭</a>
			</div>
		</form>
	</div>
<script>
		$(function(){
		}); 
		//提交校验
		function checkMes(flag){
			var userAccunt=$('#userAccunt').textbox('getValue');
			var id=$("#id").val();
			var machineCode=$("#machineCode").textbox('getValue');
			var machineMobile=$("#machineMobile").textbox('getValue');
		    	if(userAccunt!=null&&userAccunt!=""){
		    		$.ajax({
						url:"<%=basePath%>machineManage/findMachineByUserAccunt.action",
						data:{'userAccunt':userAccunt,'id':id,'machineCode':machineCode,'machineMobile':machineMobile},
						type:"POST",
						success:function(data){
							if(data.resCode=="1"){
								$.messager.alert('提示信息',data.resMsg);
							}else{
								submit(flag);
							}
						}
					});
		    	}
		}
		/*
		* form提交
		* flag 1连续添加 2保存
		*/
		function submit(flag){
			$('#editForm').form('submit', {
				url : "<%=basePath%>mosys/machineManage/saveMachine.action",
				onSubmit : function() {
					if(!$('#editForm').form('validate')){
						$.messager.alert('提示信息','验证没有通过,不能提交表单!');
						close_alert();
						return false ;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
				},
				success:function(data){ 
					$.messager.progress('close');
					var res = eval("(" + data + ")");
					if (res.resCode == "0") {
						if(flag == 1){
							clear();
							$('#list').datagrid('reload');
						}else if(flag == 2){
							closeLayout('edit');
							$.messager.alert('提示',res.resMsg);
							close_alert();
						}
					}else {
						$.messager.alert('提示',res.resMsg);
					}
				},
				error : function(data) {
					$.messager.progress('close');
				}
			}); 
		}
		//清除所填信息
		function clear(){
			var id = '${machineManage.id}';
			$('#editForm').form('reset');
			if(id){
				$('#id').val(id);
			}
		}
		
		/* 
		* 关闭界面
		*/
		function closeLayout(flag){
			$('#divLayout').layout('remove','east');
			if(flag == 'edit'){
				$('#list').datagrid('reload');
			}
		}
	</script>
	</body>
</html>
