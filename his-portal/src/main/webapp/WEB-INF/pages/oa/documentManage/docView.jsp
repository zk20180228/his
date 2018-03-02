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
					<td class="honry-lable">文档名称：</td>
					<td class="honry-info">${docManage.docName}</td>
				</tr>
				<tr>
					<td class="honry-lable">下载地址：</td>
					<td class="honry-info">${docManage.docDownAddr}</td>
				</tr>
				<tr>
					<td class="honry-lable">所属范围：</td>
					<td class="honry-info">${docManage.deptType}</td>
				</tr>
				<tr>
					<td class="honry-lable">上传人员：</td>
					<td class="honry-info">${docManage.createUser}</td>
				</tr>
				<tr>
					<td class="honry-lable">上传时间：</td>
					<td class="honry-info">${docManage.createDate}</td>
				</tr>
				<tr>
					<td class="honry-lable">上传科室：</td>
					<td class="honry-info">${docManage.uploadDept }</td>
				</tr>
				<tr>
					<td class="honry-lable">文档简述：</td>
					<td class="honry-info">${docManage.docDes}</td>
				</tr>
			</table>
			<div style="text-align:center;padding:5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('edit')">关闭</a>
			</div>
		</form>
	</div>
<script>
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
