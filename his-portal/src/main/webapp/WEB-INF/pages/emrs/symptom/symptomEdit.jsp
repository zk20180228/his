<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>电子病历医技症状维护</title>
	<%@ include file="/common/metas.jsp"%>
	
</head>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'医技症状编辑',iconCls:'icon-form',border:false" style="width:100%">
		<div style="padding:10px">
			<form id="editForm" method="post">
				<input type="hidden" id="id" name="symptom.id" value="${symptom.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="margin-left:auto;margin-right:auto;" >
					<tr>
						<td class="honry-lable">名称:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="symptomName" name="symptom.symptomName" value="${symptom.symptomName }" data-options="required:true"  style="width:200px" /></td>
	    			</tr>
					<tr id = "cod">
						<td class="honry-lable">编码:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="symptomCode" name="symptom.symptomCode" value="${symptom.symptomCode }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">分类:</td>
		    			<td class="honry-info"><input class="easyui-combobox" id="symptomType" name="symptom.symptomType" value="${symptom.symptomType }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">自定义:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="symptomInputCode" name="symptom.symptomInputCode" value="${symptom.symptomInputCode }" data-options="required:true" style="width:200px" /></td>
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
		var id = $('#id').val();
		if(id == null || id == ""){
			$('#cod').hide();
			$('#addContinue').show();
		}else{
			$('#cod').show();
			$('#addContinue').hide();
			$('#cle').html("还原");
		}
		$('#symptomType').combobox({
		    url:"<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=symptomtype",    
		    valueField:'encode',
		    textField:'name',
		    multiple:false
		});
	});
		/**
		 * 清除页面填写信息
		 */
			function clear(){
				$('#editForm').form('reset');
			}
			
			/**
		 * 表单提交验证
		 */
		function submit(){ 
			 var url;
			 var flag ="";
			 var id = $('#id').val().toString();
			 if(id == null || id == ""){
				 url = "<%=basePath %>emrs/symptom/add.action";
				 $('#symptomCode').textbox('setValue',"1");
				 sub(url);
			 }else{
				 url = "<%=basePath %>emrs/symptom/edit.action";
				 var symptomCode = $('#symptomCode').val();
			    	if(symptomCode != null && symptomCode != ""){
			    		 $.ajax({
			 				url: "<%=basePath %>emrs/symptom/querySymptomBySymptomCode.action",
			 				type:'post',
			 				data:{code: symptomCode, idd: id},
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
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
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
				$('#symptomCode').textbox('setValue',"1");
				$('#editForm').form('submit',{
					url : "<%=basePath %>emrs/symptom/add.action",
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
</body>
</html>