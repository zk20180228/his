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
<title>添加/修改流程分类</title>
</head>
<body style="    width: 500;height: 250;">
<!-- <form id="userRepoForm" method="post" action="save.action" class="form-horizontal" style="    margin: 50;width: 500;"> -->
<div class="easyui-panel" id = "panelEast" data-options="title:'添加/编辑',iconCls:'icon-form',fit:true" style="width:580px;">
	<div style="padding:5px">
		<form id="userRepoForm" method="post" action="save.action" style="margin: 10;width: 500;">
		  <c:if test="${model != null}">
		  <input id="userRepo_id" type="hidden" name="category.id" value="${model.id}">
		  </c:if>
		  <table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">
		  	<tr>
				<td class="honry-lable">名称：</td>
				<td class="honry-info">
					<input id="bpm-category_name" class="easyui-textbox" name="category.name" value="${model.name}" data-options="required:true" style="width:200px">
				</td>
			</tr>	
			<tr>
				<td class="honry-lable">排序：</td>
				<td class="honry-info"><input class="easyui-textbox" id="bpm-category_priority" name="category.priority" value="${model.priority}" data-options="required:true" style="width:200px"/></td>
			</tr>
		  </table>
		  <div style="text-align:center;padding:5px">
			  <a href="javascript:submitWin();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			  <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeWin()">关闭</a>
		  </div>
		</form>
	</div>
</div>
<script>
	function submitWin(){
		
		$('#userRepoForm').form('submit',{
			url : "<%=basePath%>activiti/category/save.action",
			success : function(data){
				$('#demoGrid').datagrid('reload');
				//关闭当前窗口
				closeWin()
			}
		});
	}
	function closeWin(){
		//关闭当前窗口
		$('#divLayout').layout('remove','east');
	}
</script>
</body>
</html>