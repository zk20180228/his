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
					<td class="honry-lable">档案编号：</td>
					<td class="honry-info">${docManage.fileNumber}</td>
				</tr>
				<tr>
					<td class="honry-lable">档案名称：</td>
					<td class="honry-info">${docManage.name}</td>
				</tr>
				<tr>
					<td class="honry-lable">档案分类：</td>
					<td class="honry-info">${docManage.fileClassify}</td>
				</tr>
				<tr>
					<td class="honry-lable">档案级别：</td>
					<td class="honry-info">${docManage.fileRank}</td>
				</tr>
				<tr>
					<td class="honry-lable">档案类型：</td>
					<td class="honry-info">${docManage.fileType}</td>
				</tr>
				<tr>
					<td class="honry-lable">档案状态：</td>
					<td class="honry-info">${docManage.fileStatus}</td>
				</tr>
				<tr >
					<td class="honry-lable">科室：</td>
	    			<td class="honry-info">${docManage.deptName}</td>
	    		</tr>
				<tr>
					<td class="honry-lable">是否可借阅：</td>
					<td class="honry-info">${docManage.borrow}</td>
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
