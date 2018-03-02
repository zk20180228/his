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
	<div class="easyui-panel" id = "panelEast" data-options="title:'厂商展示',iconCls:'icon-form',border:false,fit:true" >
		<div style="padding:10px">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
					<tr>
					<input id="createTime" name="firmMainTain.createTime" type="hidden" value="${firmMainTain.createTime }"/>
						<td class="honry-lable">厂商编码:</td>
		    			<td class="honry-view">${firmMainTain.firmCode }</td>
	    			</tr>
					<tr>
						<td class="honry-lable">厂商名称:</td>
		    			<td class="honry-view">${firmMainTain.firmName }</td>
	    			</tr>
					<tr>
						<td class="honry-lable">厂商接口密码:</td>
		    			<td class="honry-view">${firmMainTain.passWord }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">厂商视图:</td>
		    			<td class="honry-view">${firmMainTain.firmView }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">厂商账户:</td>
		    			<td class="honry-view">${firmMainTain.firmUser }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">厂商密码:</td>
		    			<td class="honry-view">${firmMainTain.firmPassword }</td>
	    			</tr>
				</table>
		</div>
		<div region="center" style="text-align:center;padding:7px;hight:5%" >
			   <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		</div>
	</div>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<script>
//关闭页面
function closeLayout(){
	$('#divLayout').layout('remove','east');
	$("#list").datagrid("reload");
}
</script>
</body>