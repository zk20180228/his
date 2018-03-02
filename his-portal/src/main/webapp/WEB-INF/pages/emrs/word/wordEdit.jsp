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
<title>电子病历常用词常用词维护修改界面</title>

</head>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'常用词编辑',iconCls:'icon-form',fit:true" style="width:570px;">
		<div >
			<form id="editForm" method="post">
				<input type="hidden" id="id" name="emrWord.id" value="${emrWord.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="margin-left:auto;margin-right:auto;margin-top:10px;">
					<tr>
						<td class="honry-lable">名称:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="wordName" name="emrWord.wordName" value="${emrWord.wordName }" data-options="required:true"  style="width:200px" /></td>
	    			</tr>
					<tr id = "cod">
						<td class="honry-lable">编码:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="wordCode" name="emrWord.wordCode" value="${emrWord.wordCode }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">分类:</td>
		    			<td class="honry-info"><input class="easyui-combobox" id="wordType" name="emrWord.wordType" value="${emrWord.wordType }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">自定义:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="wordInputCode" name="emrWord.wordInputCode" value="${emrWord.wordInputCode }" data-options="required:true" style="width:200px" /></td>
	    			</tr>
				</table>
				<div style="text-align:center;padding:5px">
				<shiro:hasPermission name="${menuAlias}:function:add">
					<a href="javascript:addContinue();" class="easyui-linkbutton" id="addContinue" data-options="iconCls:'icon-save'">连续添加</a>
			    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    </shiro:hasPermission>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" id="cle">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		$('#wordType').combobox({
		    url:"<%=basePath %>emrs/word/wordTypeCombobox.action",    
		    valueField:'encode',
		    textField:'name',
		});
		var id = $('#id').val();
		if(id == null || id == ""){
			$('#cod').hide();
			$('#addContinue').show();
		}else{
			$('#cod').show();
			$('#addContinue').hide();
			$('#cle').html("还原");
		}
	});
		/**
		 * 清除页面填写信息
		 */
			function clear(){
				$('#editForm').form('reset');
			}
			function closeLayout(){
				$('#divLayout').layout('remove','east');
				$("#list").datagrid("reload");
			}
			
			/**
		 * 表单提交验证
		 */
		function submit(){ 
			 var url;
			 var flag ="";
			 var id = $('#id').val().toString();
			 if(id == null || id == ""){
				 url = "<%=basePath %>emrs/word/add.action";
				 $('#wordCode').textbox('setValue',"1");
				 sub(url);
			 }else{
				 url = "<%=basePath %>emrs/word/edit.action";
				 var wordCode = $('#wordCode').val();
			    	if(wordCode != null && wordCode != ""){
			    		 $.ajax({
			 				url: "<%=basePath %>emrs/word/queryWordByWordCode.action",
			 				type:'post',
			 				data:{code: wordCode, idd: id},
			 				success: function(data) {
			 					if(data == "0"){
						    		$.messager.alert('提示','您输入的编码已存在！！');
						    		setTimeout(function(){$(".messager-body").window('close')},3500);
						    		return ;
						    	}else{
						    		sub(url);
						    	}
			 				}
			    		 });
			    	}
			 }
	    }	
			/**
		 * 表单提交
		 */
			function sub(url) {
				$('#editForm').form('submit',{  
		        	url: url,  
		        	onSubmit:function(){
						if (!$('#editForm').form('validate')) {
							return false;
						}
						$.messager.progress({text:'保存中，请稍后...',modal:true});
		        	},  
			        success:function(data){  
			        	$.messager.progress('close');
			        	$.messager.alert('提示','保存成功');
				        	$("#list").datagrid("reload");
				        	closeLayout();
			        },
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败！');	
					}							         
		   		}); 
			}
			/**
			 * 连续添加
			 */
			function addContinue() {
				$('#wordCode').textbox('setValue',"1");
				$('#editForm').form('submit',{
					url : "<%=basePath %>emrs/word/add.action",
					onSubmit:function(){
						if (!$('#editForm').form('validate')) {
							$.messager.alert('提示','请正确输入信息！！');
							setTimeout(function(){$(".messager-body").window('close')},3500);
							return false;
							}
						$.messager.progress({text:'保存中，请稍后...',modal:true});
						},
					success:function(data){
						$.messager.progress('close');
						$("#list").datagrid("reload");
						$('#editForm').form('clear');
						},
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败！');
						}
				}); 
			}
	 	 	
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>