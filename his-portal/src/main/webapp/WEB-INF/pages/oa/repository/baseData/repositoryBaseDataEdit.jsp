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
	<div class="easyui-panel" id = "panelEast" data-options="title:'添加/编辑',iconCls:'icon-form',fit:true" style="width:580px;">
		<div style="padding:5px">
			<form id="editForm" method="post" enctype="multipart/form-data">
			<input id="id" type="hidden" name="data.id" value="${data.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">					
					
					<tr>
						<td class="honry-lable">术语：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="name" name="data.term" value="${data.term }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr >
						<td class="honry-lable">术语缩写：</td>
		    			<td class="honry-info">
		    				<textarea id="interpretation" name="data.interpretation" data-options="required:true" rows="6" cols="50" >${data.interpretation}</textarea>
		    			</td>
	    			</tr>
	    			<tr id="code">
						<td class="honry-lable">备注：</td>
		    			<td class="honry-info">
		    				<textarea id="remark" name="data.remark" rows="6" cols="50" >${data.remark}</textarea>
		    			</td>
	    			</tr>
	    				    		 
				</table>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
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
function submit(flg) {
	var interpretation = $('#interpretation').val();
	if(interpretation==null||interpretation==''){
		$.messager.alert('提示',"术语缩写不能为空");	
		return ;
	}
	$('#editForm').form('submit', {
		url:"<%=basePath %>oa/reposirotyBaseData/saveData.action",
		onSubmit: function(){
			$.messager.progress({text:'保存中，请稍后...',modal:true});
		},
		success : function(data) {
			$.messager.progress('close');
			data = eval("("+data+")");
			$.messager.alert('提示',data.resMsg);	
			closeLayout();
			searchFrom();
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