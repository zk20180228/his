<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="iconCls:'icon-form',fit:true" style="width:580px;">
		<div style="padding:5px">
			<form id="editForm" method="post" enctype="multipart/form-data">
			<input id="id" type="hidden" name="dictionary.id" value="${dictionary.id }">
			<input id="flag" type="hidden" value="${flag }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">					
					<tr>
						<td class="honry-lable">编码类型：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="type" name="dictionary.type" value="${dictionary.type }" data-options="required:true" style="width:200px"/></td>
	    			</tr>	
	    			<tr>
						<td class="honry-lable">类型名称：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="typeName" name="dictionary.extC1" value="${dictionary.name }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
				</table>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
			    </div>
			</form>
		</div>
	</div>
<script type="text/javascript">
//清除
function clear(){
	$('#editForm').form('clear');
}
//关闭
function closeLayout(){
	$('#divLayout').layout('remove','east');
}
//提交表单
function submit() {
	var node = $('#tDt').tree('getSelected');
	var flag = $('#flag').val();
	$('#editForm').form('submit', {
		url:"<%=basePath %>baseinfo/pubCodeMaintain/editTreeInfo.action?id="+node.id+"&flag="+flag,
		onSubmit: function(){
			if($('#type').val()==""){
				$.messager.alert('操作提示', '编码类型不能为空！','error');  
                return false;  
			}
			if($('#typeName').val()==""){
       		 	$.messager.alert('操作提示', '类型名称不能为空！','error');  
                return false;  
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});
		},
		success : function(data) {
			$.messager.progress('close');
			if(data=="success"){
					$.messager.alert('提示','保存成功！');	
					closeDialog();
					//实现刷新
					refresh();
			}else if(data=="type"){
				$.messager.alert('提示','此编码类型已存在，请重新输入！');
			}else{
				$.messager.alert('提示','保存失败！');	
			}
		},
		error : function(date) {
			 $.messager.progress('close');
			 $.messager.alert('提示','保存失败！');	
		}
	});
}
</script>
</body>
</html>