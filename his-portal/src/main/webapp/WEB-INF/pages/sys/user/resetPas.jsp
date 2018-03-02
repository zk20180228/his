<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
<div class="easyui-panel"  style="padding: 10px;" data-options="fit:'true',border:'false'">
	<form id="editForm" method="post">
	<input type="hidden" id="userId" name="userId" value="${userId}"/>
		<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
			<tr>
				<td class="honry-lable">新密码:</td>
				<td class="honry-info">
					<input id="newPassword" name="newPassword" class="easyui-textbox" style="width: 260px" data-options="required:true,missingMessage:'请输入新密码'">
				</td>
			</tr>
			<tr>
				<td class="honry-lable">确认密码:</td>
				<td class="honry-info">
					<input id="txtPassword" name="txtPassword" class="easyui-textbox" style="width: 260px" data-options="required:true,missingMessage:'请输入确认密码',invalidMessage:'两次密码输入不一致！'" validType="equals['#newPassword']" >
				</td>
			</tr>
		</table>
		<div style="text-align:center;padding:5px">
			<shiro:hasPermission name="${menuAlias}:function:resetPas">
				<a href="javascript:testpwd();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			</shiro:hasPermission>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
		</div>
	</form>
</div>
<script type="text/javascript">
	$.extend($.fn.validatebox.defaults.rules, {
		equals: {
			validator: function(value,param){
				return value == $(param[0]).val();
			},
			message: '两次输入密码不一致！'
		}
	});
	function testpwd(){
		$.messager.progress({text:'正在修改密码,请稍等...',modal:true});
		$('#editForm').form('submit', {
			url:"<%=basePath%>sys/resetPas.action",
			onSubmit: function(){
				var isValid = $(this).form('validate');
				if (!isValid){
					$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
					$.messager.alert('提示信息','请检查输入的信息!');
					setTimeout(function(){$(".messager-body").window('close')},3500);
				}
				return isValid;// 返回false终止表单提交
			},
			success:function(dataMap){
				dataMap = eval('(' + dataMap + ')');
				$.messager.progress('close');
				if(dataMap.resCode=="success"){
					closeDialog();
				}else{
					$.messager.alert('提示',dataMap.resMsg);
				}
			},error:function(){
				$.messager.progress('close');
				$.messager.alert('提示','保存失败');
			}
		});
	}
</script>
</body>
</html>