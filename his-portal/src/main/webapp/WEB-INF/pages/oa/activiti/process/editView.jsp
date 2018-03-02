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
<title>添加/修改流程配置</title>
</head>
<body style="text-align:center;overflow:-Scroll;overflow-x:hidden">
<div class="easyui-panel" id = "panelEast" data-options="title:'添加/编辑',iconCls:'icon-form',fit:true" style="width:580px;">
	<form id="userRepoForm" method="post" class="form-horizontal">
		<c:if test="${model != null}">
			<input id="userRepo_id" type="hidden" name="oaBpmProcess.id" value="${model.id}">
		</c:if><br>
		<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">
		  	<tr>
				<td class="honry-lable">名称：</td>
				<td class="honry-info">
					<input id="bpm-category_name" class="easyui-textbox" name="oaBpmProcess.name" value="${model.name}" data-options="required:true" style="width:300px">
				</td>
			</tr>
		  	<tr>
				<td class="honry-lable">流程分类：</td>
				<td class="honry-info">
					<input id="bpm-process_bpmCategoryId" class="easyui-combobox" name="oaBpmProcess.categoryCode" value="${model.categoryCode}" data-options="required:true" style="width:300px">
				</td>
			</tr>
		  	<tr>
				<td class="honry-lable">绑定流程：</td>
				<td class="honry-info">
					<input id="bpm-process_bpmConfBaseId" name="oaBpmProcess.confBaseCode" class="easyui-combobox"  value="${model.confBaseCode}" data-options="required:true" style="width:420px">
				</td>
			</tr>
		  	<tr>
				<td class="honry-lable">绑定表单：</td>
				<td class="honry-info">
					<input id="bpm-process_bpmConformcode" name="oaBpmProcess.formCode" class="easyui-combobox"  value="${model.formCode}" data-options="required:true" style="width:300px">
				</td>
			</tr>
		  	<tr>
				<td class="honry-lable">绑定科室：</td>
				<td class="honry-info">
					<input id="bpm-process_bpmConfDeptCode" name="oaBpmProcess.deptCode" class="easyui-combobox"  value="${model.deptCode}" style="width:300px">
				</td>
			</tr>
			<tr>
				<td class="honry-lable">排序：</td>
				<td class="honry-info"><input class="easyui-textbox" id="bpm-process_priority" name="oaBpmProcess.priority" value="${model.priority}" style="width:300px"/></td>
			</tr>
			<tr>
				<td class="honry-lable">描述：</td>
				<td class="honry-info"><input class="easyui-textbox" id="bpm-process_descn" name="oaBpmProcess.descn" value="${model.descn}" data-options="required:true" style="width:300px"/></td>
			</tr>
		  	<tr>
				<td class="honry-lable">前置流程：</td>
				<td class="honry-info">
					<input id="bpm-process_topFlow" name="oaBpmProcess.topFlow" class="easyui-combobox"  value="${model.topFlow}" style="width:300px">
				</td>
			</tr>
		  	<tr>
				<td class="honry-lable">后置流程：</td>
				<td class="honry-info">
					<input id="bpm-process_downFlow" name="oaBpmProcess.downFlow" class="easyui-combobox"  value="${model.downFlow}" style="width:300px">
				</td>
			</tr>
		  	<tr>
				<td class="honry-lable">停启用：</td>
				<td >
					<input type="radio" name="oaBpmProcess.stop_flg" value="0" ${model.stop_flg == 0 ? 'checked' : ''} />启用
      				<input type="radio" name="oaBpmProcess.stop_flg" value="1" ${model.stop_flg != 0 ? 'checked' : ''} />停用
				</td>
			</tr>
		</table>
		<div style="text-align:center;padding:5px">
		  <a href="javascript:submitFrom();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		  <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialogs()">关闭</a>
	 	</div>
	</form>
</div>



<script type="text/javascript">
$(function(){
	$('#bpm-process_bpmCategoryId').combobox({    
		url:"<%=basePath%>activiti/process/bpmCategories.action",    
	    valueField:'id',    
	    textField:'name'   
	});  
	$('#bpm-process_bpmConfBaseId').combobox({    
		url:"<%=basePath%>activiti/process/bpmConfBases.action",    
	    valueField:'id',    
	    textField:'processDefinitionId'   
	});  
	$('#bpm-process_bpmConformcode').combobox({    
		url:"<%=basePath%>activiti/process/keyValList.action",    
	    valueField:'code',    
	    textField:'name'   
	});  
	$('#bpm-process_bpmConfDeptCode').combobox({    
		url:"<%=basePath%>activiti/process/deptList.action",    
	    valueField:'deptCode',    
	    textField:'deptName'   
	});  
	$('#bpm-process_topFlow').combobox({    
		url:"<%=basePath%>activiti/process/flowList.action",    
	    valueField:'id',    
	    textField:'name'   
	});  
	$('#bpm-process_downFlow').combobox({
		url:"<%=basePath%>activiti/process/flowList.action",    
	    valueField:'id',    
	    textField:'name'   
	});  


});
//关闭dialog
function closeDialogs() {
	//关闭当前窗口
	$('#divLayout').layout('remove','east');
}

function submitFrom() {
	$.messager.progress({text:'保存中，请稍后...',modal:true});
	$('#userRepoForm').form('submit', {
		url : "<c:url value='/activiti/process/save.action'/>",
		success : function(data) {
			$.messager.progress("close");
			data = eval("("+data+")");
			if (data.resMsg == 'success') {
// 				window.opener.location.reload();
				$('#demoGrid').datagrid('reload');
				$.messager.alert('提示',"保存成功",'info',function(){
					closeDialogs();
				});
			} else {
				$.messager.alert('提示',"保存失败",'info',function(){
					closeDialogs();
				});
			}
		},
		error : function(data) {
			$.messager.progress("close");
			$.messager.alert('提示',"保存失败",'info',function(){
				closeDialogs();
			});
		}
	});

}
</script>
</body>
</html>