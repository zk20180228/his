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
			<input id="id" type="hidden" name="detial.id" value="${detial.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">					
					<tr>
						<td class="honry-lable">标准代码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="standCode" name="detial.standCode" value="${detial.standCode }" data-options="editable:false" style="width:200px"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">标准名称：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="standName" value="${detial.standName }" data-options="editable:false" style="width:200px"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">标准版本号：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="standVersionNo" name="detial.standVersionNo" value="${detial.standVersionNo }" data-options="editable:false" style="width:200px"/></td>
	    			</tr>
	    			<tr id="trType">
						<td class="honry-lable">出入径标志</td>
		    			<td class="honry-info">
		    				<input class="easyui-combobox" id="flag" value="${detial.flag }" data-options="required:true" style="width:200px"/>
		    				<input type="hidden" id="flagH" name="detial.flag" value="${detial.flag }"/>
		    			</td>
	    			</tr>	
	    			<tr>
						<td class="honry-lable">出径标准：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="assessName" name="detial.assessName" value="${detial.assessName }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">标准值：</td>
		    			<td class="honry-info">
		    				<input class="easyui-textbox" id="assessValue" name="detial.assessValue" value="${detial.assessValue }" style="width:200px" data-options="required:true"/>
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
$(function(){
	$('#flag').combobox({
		valueField: 'id',
		textField: 'text',
		multiple: false,
		editable: true,
		required: true,
		data: [{'id': '0','text': '入径'	},{'id': '1','text': '出径'}],
	    onSelect: function(rec){
	    	$("#flagH").val(rec.id);
	    }
	});
	
});
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
	$('#editForm').form('submit', {
		url:"<%=basePath %>inpatient/inoroutStandard/saveStandartDetail.action",
		onSubmit: function(){
			$.messager.progress({text:'保存中，请稍后...',modal:true});
		},
		success : function(data) {
			$.messager.progress('close');
			data = eval("("+data+")");
			$.messager.alert('提示',data.resMsg);	
			closeLayout();
			reload2();
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