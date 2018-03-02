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
	<div class="easyui-panel" id="panelEast" style="width:100%"border="0" >
		<div style="width:100%"border="0">
			<form id="editForm" method="post"border="0">
				<input type="hidden" id="id" name="id" value="${employee.id }">
				
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0"  style="width:100%;padding:5px;">
					<tr>
						<td class="honry-lable">
							<span>工作号：</span>
						</td>
						<td style="text-align: left;">
							${employee.jobNo}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>姓名：</span>
						</td>
						<td class="honry-info">
							${employee.name}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>工资账号:</span>
						</td>
						<td class="honry-info">
							${employee.wagesAccount}
						</td>
					</tr>		
					<tr>
						<td class="honry-lable">新密码:</td>
						<td class="honry-info">
							<input id="newEmailPwd" name="newEmailPwd" class="easyui-textbox" style="width: 260px" data-options="required:true,missingMessage:'请输入新密码'">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">确认密码:</td>
						<td class="honry-info">
							<input id="txtEmailPwd" name="txtEmailPwd" class="easyui-textbox" style="width: 260px" data-options="required:true,missingMessage:'请输入确认密码',invalidMessage:'两次密码输入不一致！'" validType="equals['#newEmailPwd']" >
						</td>
					</tr>
				</table>
				 <div style="text-align:center;padding:5px">
					<shiro:hasPermission name="${menuAlias}:function:edit">
				    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	</shiro:hasPermission>
			    	<a href="javascript:closeDialogs()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
    	</div>
    </div>
	<script type="text/javascript">
		function submit() {
			var newEmailPwd = $('#newEmailPwd').textbox('getValue');
			var txtEmailPwd = $('#txtEmailPwd').textbox('getValue');
			if(newEmailPwd != txtEmailPwd){
				$.messager.alert('提示','两次输入密码不一致！');
				return false
			}
			
			$('#editForm').form('submit', {
				url : "<%=basePath%>baseinfo/employee/saveWagesPwd.action",
				onSubmit : function() {
					if (!$('#editForm').form('validate')) {
						$.messager.progress('close');
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});	
				},
				success : function(data) {
					var res = eval('(' + data + ')');
					$.messager.progress('close');
					if(res.resCode == 'success'){
						$.messager.confirm('确认对话框', '保存成功，是否关闭窗口？', function(r){
							if (r){
								closeDialogs();
							}
						});
					}else{
						$.messager.alert('提示',res.resMsg);
					}
				},
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示','保存失败！');
				}
			});
		}
		//关闭dialog
		function closeDialogs() {
			window.close();  
		}
	</script>
</body>
</html>