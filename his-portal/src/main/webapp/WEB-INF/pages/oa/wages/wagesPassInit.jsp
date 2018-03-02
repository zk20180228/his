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
				<td class="honry-lable">员工编号/工资号：</td>
				<td class="honry-info">
					<span style = "border: 1px solid #0aa2a1;border-radius: 5px;padding:3px 5px">
						<input class="text" style="border:none;outline:none;" id="account" name="wagesAccount"   placeholder="请输入员工编号">
					</span>
				</td>
			</tr>
			<tr>
				<td class="honry-lable">密码:</td>
				<td class="honry-info">
					<span style = "border: 1px solid #0aa2a1;border-radius: 5px;padding:3px 5px">
						<input  class = "pass" style="border:none;outline:none;" id="wagesPassword" name="wagesPassword" type="password"  placeholder="请输入密码">
					</span>
				</td>
			</tr>
			<tr>
				<td class="honry-lable">确认密码:</td>
				<td class="honry-info">
					<span style = "border: 1px solid #0aa2a1;border-radius: 5px;padding:3px 5px">
						<input   class = "pass" style="border:none;outline:none;" id="txtPassword" name="txtPassword" type="password"  placeholder="请输入确认密码">
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
		var wagesAccount=$("#account").val();
		var wagesPassword=$("#wagesPassword").val();
		var txtPassword=$("#txtPassword").val();
		$.messager.progress({text:'正在保存密码,请稍等...',modal:true});
		$.ajax({
			url:"<%=basePath%>oa/Wages/wagesInit.action",
			type:'post',
			data:{'wagesAccount':wagesAccount,'wagesPassword':wagesPassword,'txtPassword':txtPassword},
			success:function(data){
				console.info(data);
				console.info(data.resMsg);
				$.messager.progress('close');
				if(data.resCode=="success"){
					$.messager.alert('提示',data.resMsg);
					$('#editForm').form('clear');
					$('#add').dialog('close'); 
<%-- 					document.location="<%=basePath%>oa/Wages/toEmployee.action" --%>
				}else{
					$.messager.alert('提示',data.resMsg);
					$('#editForm').form('clear');
				}
			},error:function(){
				$.messager.progress('close');
				$.messager.alert('提示','保存失败');
			}
		});
	}
	//关闭dialog
	function closeDialog() {
		$('#add').dialog('close');  
	}
</script>
</body>
</html>