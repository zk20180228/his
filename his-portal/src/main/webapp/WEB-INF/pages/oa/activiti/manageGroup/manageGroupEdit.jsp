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
	<div class="easyui-panel" id = "panelEast" data-options="title:'编辑',iconCls:'icon-form',fit:true" style="width:580px;">
		<div style="padding:5px">
			<form id="editForm" method="post" enctype="multipart/form-data">
			<input id="id" type="hidden" name="dictionary.id" value="${dictionary.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">					
					<tr id="trType">
						<td class="honry-lable">编码类型：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="type" name="dictionary.type" value="${dictionary.type }" style="width:200px"/></td>
	    			</tr>	
	    			<tr id="typeName1">
						<td class="honry-lable">类型名称：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="typeName" name="dictionary.extC1" value="${dictionary.extC1 }" style="width:200px"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">名称：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="name" name="dictionary.name" value="${dictionary.name }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">自定义码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="inputCode" name="dictionary.inputCode" value="${dictionary.inputCode }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">是否自定义编码：</td>
		    			<td class="honry-info">
		    			<input id="myId" type="checkbox" style="width:18px;" onclick="onClickMyId('myId')" checked="checked"/>
		    			</td>
	    			</tr>
	    			<tr id="code">
						<td class="honry-lable">编码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="enCode" name="dictionary.encode" value="${dictionary.encode }" data-options="required:true" style="width:200px"/>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">备注：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="mark" name="dictionary.mark" value="${dictionary.mark }" data-options="multiline:true" style="width:200px;height:60px;"/></td>
	    			</tr> 
	    			<tr>
						<td class="honry-lable">是否停用：</td>
		    			<td class="honry-info">&nbsp;
		    				<input id="yesId" type="checkbox" style="width:18px;" onclick="onClickIsStop('yesId')"/>
		    				<input type="hidden" id="stopId" name="dictionary.validState" value="${dictionary.validState }"/>
		    			</td>
	    			</tr>		    		 
				</table>
				<div style="text-align:center;padding:5px">
			    	<c:if test="${dictionary.id == null }"><a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a></c:if>
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			</form>
		</div>
	</div>
<script type="text/javascript">
	$('#trType').hide();
	$('#typeName1').hide();
	if($('#stopId').val()==1){
		$('#yesId')[0].checked=true
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
//是否自定义
function onClickMyId(id){
	if($('#'+id).is(':checked')){	
		$('#code').show();
	}else{
		$('#code').hide();
	}
}
//提交表单
function submit(flg) {
	var checkflg=1;
	if($('#myId').is(':checked')){
		checkflg=1;
	}else{
		checkflg=2;
	}
	$('#editForm').form('submit', {
		url:"<%=basePath %>baseinfo/pubCodeMaintain/saveOrUpdatePubCode.action?flag=1",
		onSubmit: function(){
			if($('#name').val()==""){
       		 	$.messager.alert('操作提示', '名称不能为空！','error');  
                return false;  
			}
			if($('#myId').is(':checked') && $('#enCode').val()==""){
       		 	$.messager.alert('操作提示', '编码不能为空！','error');  
                return false;  
			}
			var reCode=/^[0-9a-zA-Z]*$/g;
			if($('#myId').is(':checked') && !reCode.test($('#enCode').val())){
       		 	$.messager.alert('操作提示', '编码不能输入符号！','error');  
                return false;  
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});
		},
		success : function(data) {
			$.messager.progress('close');
			if(data=="success"){
				if (flg == 0) {
					$.messager.alert('提示','保存成功！');	
					$('#divLayout').layout('remove', 'east');
					//实现刷新
					$("#list").datagrid("reload");
				} else if (flg == 1) {
					$("#list").datagrid("reload");
					//清除editForm
					$('#editForm').form('reset');
					if(checkflg==1){
						$('#myId').attr('checked', true);
					}else{
						$('#myId').attr('checked', false);
					}
				}
			}else if(data=="encode"){
				$.messager.alert('提示','此编码已存在，请重新输入！');
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