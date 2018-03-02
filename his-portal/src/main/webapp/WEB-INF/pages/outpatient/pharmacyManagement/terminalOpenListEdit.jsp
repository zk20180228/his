<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>模板编辑界面</title>
	</head>
	<body>
      	<div align="center" class="easyui-panel" style="padding:10px" border="none">
			<form id="treeEditForm" method="post" >
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<input type="hidden" id="deptCode" name="deptCode" />
				<input type="hidden" id="type" name="type" value="${type}" />
					<tr id="name">
		    			<td>模版名称:</td>
		    			<td><input class="easyui-textbox" id="templetName" name="templetName" value="" data-options="required:true"/></td>
		    		</tr>
					<tr id="newname" style="display: none">
		    			<td>模版名称:</td>
		    			<td><input class="easyui-textbox" id="newTempletName" name="newname" value="" /></td>
		    		</tr>
		    		<tr id="code">
	    				<td>模版编码:</td>
		    			<td><input class="easyui-textbox" id="templetCode" name="templetCode" value="" data-options="required:true"/></td>
		    		</tr>
					<tr>
						<td colspan="2" align="center">
	   					<a href="javascript:void(0)" data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="submitTreeForm()">保存</a>
	   					<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" class="easyui-linkbutton" onclick="closeDialog()">关闭</a>
	   					</td>
					</tr>
				</table>	
			</form>
		</div>
		<script type="text/javascript">
			$(function(){
				$('#deptCode').val(window.parent.window.getSelected(2));
				//0--添加       1--修改
				if($('#type').val()==1){
					$('#templetName').val($('#nodetext').val());
					$('#templetCode').val($('#nodeid').val());
					$('#name').hide();
					$('#code').hide()
					$('#newname').show();
					$('#newTempletName').textbox({    
					    required: true 
					});
				}
			})
			//提交验证
			function submitTreeForm(){
				if($('#templetCode').val()==1&&$('#templetName').val()=='模版列表'){
					$.messager.alert('提示',"该命名已被占用，请重新命名！");
					setTimeout(function(){$(".messager-body").window('close')},1500);
				}else{
					//0--添加       1--修改
					if($('#type').val()==0){
						$('#treeEditForm').form('submit', {
							url : '<%=basePath %>drug/pharmacyManagement/addTemplateVerify.action?type=0',
							onSubmit : function() {
								if(!$('#treeEditForm').form('validate')){
									$.messager.show({  
								         title:'提示信息' ,   
								         msg:'验证没有通过,不能提交表单!'  
								    }); 
							       return false ;
								}
							},
							success : function(data) {
								if(data=='Y'){
									$.ajax({
										url:'<%=basePath%>drug/pharmacyManagement/addTemplate.action',
										type:'post',
										data:$('#treeEditForm').serialize(),
										success:function(data){
											if(data=='success'){
												refresh();
												closeDialog();
											}
										},error : function(a,b,c) {
						 					//$.messager.alert('提示',"******"+a+"*****"+b+"********"+c);
										}
									});
								}else{
									$.messager.alert('提示','模版信息已存在！');
									setTimeout(function(){$(".messager-body").window('close')},1500);
								}
							},
							error : function(data) {
								$.messager.alert('提示',"操作失败！");	
							}
						});
					}else{
						$.ajax({
							url : '<%=basePath %>drug/pharmacyManagement/addTemplateVerify.action?type=1',
							type:'post',
							data:{'deptCode':$('#deptCode').val(),'templetCode':$('#templetCode').val(),'templetName':$('#newTempletName').val()},
							onSubmit : function() {
								if(!$('#treeEditForm').form('validate')){
									$.messager.show({  
								         title:'提示信息' ,   
								         msg:'验证没有通过,不能提交表单!'  
								    }); 
							       return false ;
								}
							},
							success : function(data) {
								if(data=='Y'){
									$.ajax({
										url:'<%=basePath%>drug/pharmacyManagement/editTemplateName.action',
										type:'post',
										data:$('#treeEditForm').serialize(),
										success:function(data){
											if(data=='success'){
												refresh();
												closeDialog();
											}
										}
									});
								}else{
									$.messager.alert('提示','模版信息已存在！');
									setTimeout(function(){$(".messager-body").window('close')},1500);
								}
							},
							error : function(data) {
								$.messager.alert('提示',"操作失败！");	
							}
						});
					}
				}
			}
			//清除所填信息
			function clearTreeForm(){
				$('#treeEditForm').form('reset');
			}
		</script>
	</body>
</html>