<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@ include file="/common/metas.jsp"%>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>themes/system/css/public.css">
	<style type="text/css">
		.window .panel-header .panel-tool a{
			background-color: red;	
		}
	</style>
<body>
	<div style="padding:10px" id="panelEast" >
		<form id="editForm" enctype="multipart/form-data" method="post">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="0px" style="width:100%;border-left:0;" data-options="border:false">
					<tr>
						<td class="honry-lable">上传ini文件：</td>
						<td class="honry-info">
						<input type="file" id="iniFile" name="iniFile" style="width:200px" data-options="required:true"  >
					</tr>
					<tr>
						<td class="honry-lable">上传zip文件：</td>
						<td class="honry-info">
						<input type="file" id="zipFile" name="zipFile" style="width:200px" data-options="required:true"  >
					</tr>
			</table>
			<div style="text-align:center;padding:5px">
				<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
<!-- 				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('edit')">关闭</a> -->
			</div>
		</form>
	</div>
<script>
		$(function(){
		});
 			
		/*
		* form提交
		* flag 1连续添加 2保存
		*/
		function submit(){
				var ini=$('#iniFile').val();
				var zip=$('#zipFile').val();
				if(ini==null||ini==""){
					$.messager.alert('提示信息','请上传ini文件!');
					close_alert();
					return;
				}
				if(zip==null||zip==""){
					$.messager.alert('提示信息','请上传zip文件!');
					close_alert();
					return;
				}
			$('#editForm').form('submit', {
				url : "<%=basePath%>sys/hrBrowser/saveBrowser.action",
				onSubmit : function() {
					if(!$('#editForm').form('validate')){
						$.messager.alert('提示信息','验证没有通过,不能提交表单!');
						close_alert();
						return false ;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
				},
				success:function(data){ 
					$.messager.progress('close');
					var res = eval("(" + data + ")");
					if (res.resCode == "0") {
							$.messager.alert('提示',res.resMsg);
							close_alert();
					}else {
						$.messager.alert('提示',res.resMsg);
					}
				},
				error : function(data) {
					$.messager.progress('close');
				}
			}); 
		}
		//清除所填信息
		function clear(){
			var id = '${mApkVersion.id }';
			$('#editForm').form('reset');
			if(id){
				$('#id').val(id);
			}
		}
		
		/* 
		* 关闭界面
		*/
// 		function closeLayout(flag){
// 			$('#divLayout').layout('remove','east');
// 			if(flag == 'edit'){
// 				$('#list').datagrid('reload');
// 			}
// 		}
	</script>
	</body>
</html>
