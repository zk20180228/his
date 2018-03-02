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
<style type="text/css">
#oldPassword{
		border: 1px solid #0aa2a1;border-radius: 5px;outline:none;
}
	
</style>
</head>
<body>
<div class="easyui-panel"  style="padding: 10px;" data-options="fit:'true',border:'false'">
	<form id="editForm" method="post">
		<input type="hidden" id="id" name="id" value="${user.id }">
		<input type="hidden" id="createUser" name="createUser" value="${user.createUser }">
		<input type="hidden" id="createDept" name="createDept" value="${user.createDept }">
		<input type="hidden" id="createTime" name="createTime" value="${user.createTime }">
		<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
			<tr>
				<td class="honry-lable">原密码：</td>
				<td class="honry-info">
					<span style = "border: 1px solid #0aa2a1;border-radius: 5px;padding:3px 5px">
						<input class = "pass" style="border:none;outline:none;" id="oldPassword" name="name" type="password"  placeholder="请输入原密码">
					</span>
				</td>
			</tr>
			<tr>
				<td class="honry-lable">新密码:</td>
				<td class="honry-info">
					<span style = "border: 1px solid #0aa2a1;border-radius: 5px;padding:3px 5px">
						<input  class = "pass" style="border:none;outline:none;" id="newPassword" name="Password" type="password"  placeholder="请输入新密码">
					</span>
				</td>
			</tr>
			<tr>
				<td class="honry-lable">确认密码:</td>
				<td class="honry-info">
					<span style = "border: 1px solid #0aa2a1;border-radius: 5px;padding:3px 5px">
						<input   class = "pass" style="border:none;outline:none;" id="txtPassword" name="Password1" type="password"  placeholder="请输入确认密码">
					</span>
				</td>
			</tr>
		</table>
		<div style="text-align:center;padding:5px">
			<a href="javascript:testpwd();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
		</div>
	</form>
</div>
<script type="text/javascript">
	$(".pass").on("focus",function(){
		$(this).parent().css({
				borderColor:"#00b7b3",
				boxShadow:"0 0 3px 0 #027776"			
			})
	}).on("blur",function (){
		$(this).parent().css({
			borderColor:"#0aa2a1",
			boxShadow:"none"			
		})
	})


	function testpwd(){
		var _oldPassword=$("#oldPassword").val();
		var _newPassword=$("#newPassword").val();
		var _txtPassword=$("#txtPassword").val();
		$.messager.progress({text:'正在修改密码,请稍等...',modal:true});
		$.ajax({
			url:"<%=basePath%>sys/userUpdataPWD.action",
			data:{'oldPassword':_oldPassword,'newPassword':_newPassword,'txtPassword':_txtPassword},
			success:function(dataMap){
				$.messager.progress('close');
				$.messager.alert('提示',dataMap.resMsg);
				if(dataMap.resCode=="success"){
					//退出系统
					document.location="<%=basePath%>userLoginAction!loginout"
				}else{
					$('#editForm').form('clear');
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