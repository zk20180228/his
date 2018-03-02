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
			<input id="id" type="hidden" name="standard.id" value="${standard.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">					
					<tr id="trType">
						<td class="honry-lable">标准代码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="standCode" name="standard.standCode" value="${standard.standCode }" data-options="required:true" style="width:200px"/></td>
	    			</tr>	
					<tr id="trType">
						<td class="honry-lable">标准名称：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="standName" name="standard.standName" value="${standard.standName }" data-options="required:true" style="width:200px"/></td>
	    			</tr>	
					<tr id="trType">
						<td class="honry-lable">版本号：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="standVersionNo" name="standard.standVersionNo" value="${standard.standVersionNo }" data-options="required:true" style="width:200px"/></td>
	    			</tr>	
					<tr>
						<td class="honry-lable">输入码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="inputCode" name="standard.inputCode" value="${standard.inputCode }" style="width:200px"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">自定义码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="customCode" name="standard.customCode" value="${standard.customCode }"style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">是否停用：</td>
		    			<td class="honry-info">&nbsp;<input id="yesId" type="checkbox" style="width:18px;" onclick="onClickIsStop('yesId')"/><input type="hidden" id="stopId" name="standard.stop_flg" value="0"/></td>
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
var stopflg = "${standard.stop_flg}";
$(function(){
	if(stopflg == 1){
		$("#yesId").attr("checked",'checked');
	}
});
//本地下拉查询方法
function filterLocalCombobox(q, row, keys){
	if(keys!=null && keys.length > 0){//
		for(var i=0;i<keys.length;i++){ 
			if(row[keys[i]]!=null&&row[keys[i]]!=''){
					var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
					if(istrue==true){
						return true;
					}
			}
		}
	}else{
		var opts = $(this).combobox('options');
		return row[opts.textField].indexOf(q.toUpperCase()) > -1;
	}
}
//清除
function clear(){
	$('#editForm').form('clear');
}
//关闭
function closeLayout(){
	$('#divLayout').layout('remove','east');
}
//是否停用
function onClickIsStop(id){
	if($('#'+id).is(':checked')){	
		$('#stopId').val(1);
	}else{
		$('#stopId').val(0);
	}
}
function onClickIsDel(id){
	if($('#'+id).is(':checked')){	
		$('#delIdH').val(1);
	}else{
		$('#delIdH').val(0);
	}
}
$(function(){
	var node = $('#tDt').tree('getSelected');
	if(node){
		if(node.id!='root'){
			$('#deptCode').val(node.deptCode);
		}
	}
});
//提交表单
function submit(flg) {
	var checkflg=1;
	if($('#yesId').is(':checked')){
		checkflg=1;
	}else{
		checkflg=2;
	}
	var flag=1;
	var node = $('#tDt').tree('getSelected');
	if(node){
		if(node.id=='root'){
			flag=0;
		}
	}
	$('#editForm').form('submit', {
		url:"<%=basePath %>inpatient/inoroutStandard/saveStandart.action",
		onSubmit: function(){
			$.messager.progress({text:'保存中，请稍后...',modal:true});
		},
		success : function(data) {
			$.messager.progress('close');
			data = eval("("+data+")");
			$.messager.alert('提示',data.resMsg);	
			closeLayout();
			reload();
			refresh();
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