<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'栏目功能编辑',iconCls:'icon-form',border:false,fit:true" style="width:580px">
		<div style="padding:10px">
			<form id="editForm" method="post">
				<input type="hidden" id="id" name="menufunction.id" value="${menufunction.id }">
				<input type="hidden" name="mfOrder" value="${menufunction.mfOrder }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
						<td class="honry-lable">名称:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="mfName" name="mfName" value="${menufunction.mfName }" data-options="required:true"  style="width:200px" /></td>
	    			</tr>
					<tr>
						<td class="honry-lable">分类:</td>
		    			<td class="honry-info"><input class="easyui-numberbox" id="mfClass" name="mfClass" value="${menufunction.mfClass }" data-options="min:0,max:99,precision:0" style="width:200px"/></td>
	    			</tr>
					<tr id="belongTr">
						<td class="honry-lable">功能归属:</td>
		    			<td class="honry-info"><input id="mfBelong" name="mfBelong" value="${menufunction.mfBelong }"  style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">图标:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="mfIcon" name="mfIcon" value="${menufunction.mfIcon }"  style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">访问页面请求:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="mfAction" name="mfAction" value="${menufunction.mfAction }" data-options="required:true" style="width:200px" onchange="CheckInput('mfAction');"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">页面路径:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="mfFile" name="mfFile" value="${menufunction.mfFile }" data-options="required:true" style="width:200px" onchange="CheckInput('mfFile');"/></td>
	    			</tr>
	    			<tr id="interfaceTr">
						<td class="honry-lable">接口路径:</td>
						<td class="honry-info"><input class="easyui-textbox" id="mfInterface" name="mfInterface" value="${menufunction.mfInterface }" style="width:200px" onchange="CheckInput('mfInterface');"/></td>
					</tr>
	    			<tr>
						<td class="honry-lable">说明:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="mfDescription" name="mfDescription" value="${menufunction.mfDescription }" data-options="multiline:true" style="width:200px;height:60px;" /></td>
	    			</tr>
				</table>
				<div style="text-align:center;padding:5px">
				<c:if test="${menufunction.id==null }">
			    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    </c:if>
			    	<a href="javascript:submitForm();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	<script type="text/javascript">
	$(function(){
		var belong = '${menufunction.mfBelong}';
		if(belong != 1){
			$("#interfaceTr").show();
			$("#mfInterface").textbox({
				onChange:function(newValue, oldValue){
					if(/[\u4e00-\u9fa5]/.test(newValue))
					{
						$('#mfInterface').textbox('setText', oldValue);
					}
				},
				required : true
			});
		}else{
			$("#interfaceTr").hide();
			$("#mfInterface").textbox({
				onChange:function(newValue, oldValue){
					if(/[\u4e00-\u9fa5]/.test(newValue))
					{
						$('#mfInterface').textbox('setText', oldValue);
					}
				},
				required : false
			});
		}
		$("#mfBelong").combobox({
			valueField:'id',
			textField:'text',
			data:[{'id' : 1, 'text' : '平台'},{'id' : 2, 'text' : '移动端'}],
			editable : false,
			required : true,
			onSelect: function(rec){
				if(rec.id != 1){
					$("#interfaceTr").show();
					$("#mfInterface").textbox({
						onChange:function(newValue, oldValue){
							if(/[\u4e00-\u9fa5]/.test(newValue))
							{
								$('#mfInterface').textbox('setText', oldValue);
							}
						},
						required : true
					});
				}else{
					$("#interfaceTr").hide();
					$("#mfInterface").textbox({
						onChange:function(newValue, oldValue){
							if(/[\u4e00-\u9fa5]/.test(newValue))
							{
								$('#mfInterface').textbox('setText', oldValue);
							}
						},
						required : false
					});
				}
			}
		});
	});
		/**
		 * 清除页面填写信息
		 * @author liudelin
		 * @date 2015-7-29 10:53
		 * @version 1.0
		 */
			function clear(){
				$('#editForm').form('clear');
			}
			function closeLayout(){
				$('#divLayout').layout('remove','east');
				$("#list").datagrid("reload");
			}
			
			/**
		 * 表单提交
		 * @author liudelin
		 * @date 2015-7-79 9:30
		 * @version 1.0
		 */
			//表单提交
		function submit(){ 
        	$('#editForm').form('submit',{  
	        	url:"<%=basePath %>sys/saveMenufunction.action",  
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
		        	var res = jQuery.parseJSON(data);
		        	$.messager.progress('close');
		        	if(res.resCode == "success"){
			        	$("#list").datagrid("reload");
			        	closeLayout();
		        	}else if(res.resCode == "repeat"){
		        		$('#mfName').val("");
						$("#mfName").focus();
						close_alert();
		        	}
		        	$.messager.alert('提示',res.resMsg);
		        },
				error : function(data) {
					$.messager.alert('提示','保存失败！');	
				}							         
	   		}); 
	    }	
	    
	    function submitForm(){
	    	var mfAction = $('#mfAction').val();
			var mfFile = $('#mfFile').val();
			var id = "${menufunction.id}";
			var mfName = "${menufunction.mfName}";
	    	if(mfAction.indexOf(".") > 0&&mfFile.indexOf(".") > 0 ){
	    		var s = mfAction.split(".");
				var smfAction = s[1].split(".");
				var z = mfFile.split(".");
				var zmfFile = z[1].split(".");
				if(smfAction=="action"&&zmfFile=="jsp"){
					submit();
				}else{
					if(smfAction!="action"){
						$.messager.alert('提示','保存失败！请输入正确的"访问页面请求"');
						close_alert();
					}else if(zmfFile!="jsp"){
						$.messager.alert('提示','保存失败！请输入正确的"页面路径"');
						close_alert();
					}
						
				}
	    	}else{
	    		$.messager.alert('提示','保存失败！');	
			}
	    }    
	    
	    function submitContinue(){
	    	$('#editForm').form('submit',{  
		        url:"<%=basePath %>sys/saveMenufunction.action",  
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
		        	var res = jQuery.parseJSON(data);
		        	$.messager.progress('close');
		        	if(res.resCode == "success"){
		        		//实现刷新栏目中的数据
	                    $('#list').datagrid('reload');
	                    $('#editForm').form('reset');
		        	}else if(res.resCode == "repeat"){
		        		$('#mfName').val("");
						$("#mfName").focus();
						close_alert();
		        	} 
		        	$.messager.alert('提示',res.resMsg);
		        },
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示','保存失败！');	
				}							         
		  	  }); 
	    }
	 	 /**
		 * 连续添加
		 * @author  liudelin
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
	 	 	
	 	 	function addContinue(){ 
	 	 		var mfAction = $('#mfAction').val();
				var mfFile = $('#mfFile').val();
				var id = "${menufunction.id}";
				var mfName = "${menufunction.mfName}";
		    	if(mfAction.indexOf(".") > 0&&mfFile.indexOf(".") > 0 ){
		    		var s = mfAction.split(".");
					var smfAction = s[1].split(".");
					var z = mfFile.split(".");
					var zmfFile = z[1].split(".");
					if(smfAction=="action"&&zmfFile=="jsp"){
						 submitContinue();
					}else{
						if(smfAction!="action"){
							$.messager.alert('提示','保存失败！请输入正确的"访问页面请求"');
							close_alert();
						}else if(zmfFile!="jsp"){
							$.messager.alert('提示','保存失败！请输入正确的"页面路径"');
							close_alert();
						}
							
					}
		    	}else{
		    		$.messager.alert('提示','保存失败！');	
				}
	    	 }
	    	 
	    	 function CheckInput(id){
				var str = $('#'+id).textbox('getText');
				if(/[\u4e00-\u9fa5]/.test(str))
				{
					$('#'+id).textbox('setText', str.substring(0,str.length-str.length));
				}
			 }
	</script>
</body>