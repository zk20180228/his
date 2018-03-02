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
			<input type="hidden" id="id" name="menuIcon.id" value="${menuIcon.id}">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="0px" style="width:100%;border-left:0;" data-options="border:false">
				<tr>
					<td class="honry-lable">图片名称：</td>
					<td class="honry-info">
					<input class="easyui-textbox" id="picName" name="menuIcon.picName" value="${menuIcon.picName }" data-options="required:true" style="width:200px" missingMessage="请输入图片名称"/></td>					
				</tr>
				<c:if test="${menuIcon.id!=null }">
					<tr>
						<td class="honry-lable">图片：</td>
						<td class="honry-info">
						<span><img style="height:50px" src="${menuIcon.picShow }">&nbsp;</span>
						</td>					
					</tr>
					<tr>
						<td class="honry-lable">图片路径：</td>
						<td class="honry-info">
						<span>${menuIcon.picPath }&nbsp;</span>
						</td>					
					</tr>
				</c:if>
				<c:if test="${menuIcon.id==null }">
					<tr>
						<td class="honry-lable">上传图片：</td>
						<td class="honry-info">
						<input type="file" id="mFile" name="mFile" style="width:200px" data-options="required:true"  >
					</tr>
				</c:if>
				<c:if test="${menuIcon.id!=null }">
					<tr>
						<td class="honry-lable">上传新图片：</td>
						<td class="honry-info">
						<input type="file" id="mFile" name="mFile" style="width:200px" >
					</tr>
				</c:if>
			</table>
			<div style="text-align:center;padding:5px">
				<c:if test="${menuIcon.id==null }">
					<a href="javascript:checkName(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
				</c:if>
				<a href="javascript:checkName(2);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('edit')">关闭</a>
			</div>
		</form>
	</div>
<script>
	$(function(){
		
	})
 			
		/*
		* form提交
		* flag 1连续添加 2保存
		*/
		function submit(flag){
			var id=$('#id').val();
			if(id==null||id==""){
				var apk=$('#mFile').val();
				if(apk==null||apk==""){
					$.messager.alert('提示信息','请上传图片!');
					close_alert();
					return;
				}
			}
			$('#editForm').form('submit', {
				url : "<%=basePath%>mosys/menuIcon/saveMenuIcon.action",
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
						if(flag == 1){
							clear();
							$('#list').datagrid('reload');
						}else if(flag == 2){
							closeLayout('edit');
							$.messager.alert('提示',res.resMsg);
							close_alert();
						}
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
			var id = '${menuIcon.id }';
			$('#editForm').form('reset');
			if(id){
				$('#id').val(id);
			}
		}
		
		/* 
		* 关闭界面
		*/
		function closeLayout(flag){
			$('#divLayout').layout('remove','east');
			if(flag == 'edit'){
				$('#list').datagrid('reload');
			}
		}
		
		function checkName(flag){
			var name=$('#picName').textbox('getValue');
			if(name!=null&&name!=""){
				$.ajax({
					url:"<%=basePath%>mosys/menuIcon/ckeckName.action",
					async:false,
					cache:false,
					data:{'name':name,'id':$('#id').val()},
					type:"POST",
					success:function(data){
						if(data.resCode=="0"){
							submit(flag);
						}else{
							$.messager.alert("提示",data.resMsg);
						}
						
					}
				});
			}else{
				$.messager.alert('提示',"请填写图片名称！！！");
				close_alert();
			}
			
		}
		
	</script>
	</body>
</html>
