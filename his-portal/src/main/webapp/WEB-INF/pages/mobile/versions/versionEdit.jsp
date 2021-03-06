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
			<input type="hidden" id="id" name="mApkVersion.id" value="${mApkVersion.id}">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="0px" style="width:100%;border-left:0;" data-options="border:false">
				<tr>
					<td class="honry-lable">最新版本号：</td>
					<td class="honry-info">
					<input class="easyui-numberbox" id="apkNewestVNum" name="mApkVersion.apkNewestVnum" value="${mApkVersion.apkNewestVnum }" data-options="required:true" style="width:200px" missingMessage="请输入最新版本号"/></td>					
				</tr>
				<tr>
					<td class="honry-lable">版本名称：</td>
					<td class="honry-info">
					<input id="apkVersionName" name="mApkVersion.apkVersionName" value="${mApkVersion.apkVersionName}" data-options="required:true" style="width:200px" missingMessage="请输入版本名称" class="easyui-textbox"/>
				</td>
				<tr>
					<td class="honry-lable">是否强制更新：</td>
					<td>
						<input id="forceUpdateFlg" class="easyui-combobox" name="mApkVersion.forceUpdateFlg" value="${mApkVersion.forceUpdateFlg}" data-options="required:true" missingMessage="请选择是否强制更新"  style="width:200px"> 
					</td>
				</tr>
				<tr>
					<td class="honry-lable">是否清理缓存：</td>
					<td>
						<input id="apkClearCache" class="easyui-combobox" name="mApkVersion.apkClearCache" value="${mApkVersion.apkClearCache}" data-options="required:true" missingMessage="请选择清理缓存 "  style="width:200px"> 
					</td>
				</tr>
				<c:if test="${mApkVersion.id!=null }">
					<tr>
						<td class="honry-lable">apk下载地址：</td>
						<td class="honry-info">
						<span>${mApkVersion.apkDownloadAddr }</span>
						</td>					
					</tr>
				</c:if>
				<c:if test="${mApkVersion.id!=null }">
					<tr>
						<td class="honry-lable">二维码：</td>
						<td class="honry-info">
						<span>${mApkVersion.apkDownloadQRAddr }</span>
						</td>					
					</tr>
				</c:if>
				<c:if test="${mApkVersion.id!=null }">
					<tr>
						<td class="honry-lable">二维码下载地址：</td>
						<td class="honry-info">
						<span>${mApkVersion.apkDownloadFixedAddr }</span>
						</td>					
					</tr>
				</c:if>
				 <c:if test="${mApkVersion.id==null }">
					<tr>
						<td class="honry-lable">上传apk：</td>
						<td class="honry-info">
						<input type="file" id="mFile" name="mFile" style="width:200px" data-options="required:true"  >
					</tr>
				</c:if>
				<c:if test="${mApkVersion.id!=null }">
					<tr>
						<td class="honry-lable">上传新apk：</td>
						<td class="honry-info">
						<input type="file" id="mFile" name="mFile" style="width:200px" >
					</tr>
				</c:if>
			</table>
			<div style="text-align:center;padding:5px">
				<c:if test="${mApkVersion.id==null }">
					<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
				</c:if>
				<a href="javascript:submit(2);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('edit')">关闭</a>
			</div>
		</form>
	</div>
<script>
		$(function(){
			//类型
			$('#forceUpdateFlg').combobox({
				valueField : 'id',	
				textField : 'value',
				editable : false,
				data : [{id : 1, value : '否'},{id : 2, value : '是'}]
			});
			//是否更新缓存
			$('#apkClearCache').combobox({
				valueField : 'id',	
				textField : 'value',
				editable : false,
				data : [{id : 1, value : '否'},{id : 2, value : '是'}]
			});
		});
 			
		/*
		* form提交
		* flag 1连续添加 2保存
		*/
		function submit(flag){
			var id=$('#id').val();
			if(id==null||id==""){
				var apk=$('#mFile').val();
				if(apk==null||apk==""){
					$.messager.alert('提示信息','请上传apk!');
					close_alert();
					return;
				}
			}
			$('#editForm').form('submit', {
				url : "<%=basePath%>mosys/updateVersion/saveVersion.action",
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
			var id = '${mApkVersion.id }';
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
	</script>
	</body>
</html>
