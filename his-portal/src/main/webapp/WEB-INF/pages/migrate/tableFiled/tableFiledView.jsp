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
	<div class="easyui-panel" id = "panelEast" data-options="title:'表维护编辑',iconCls:'icon-form',border:false,fit:true" style="width:580px">
		<div style="padding:10px">
			<form id="editForm" method="post">
				<input id="id" name="tableFiled.id" type="hidden" value="${tableFiled.id}"/>
				<input id="code" name="tableFiled.code" type="hidden" value="${tableFiled.code}"/>
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
						<td class="honry-lable">表名:</td>
		    			<td class="honry-view">${tableFiled.tableName }</td>
	    			</tr>
					<tr>
						<td class="honry-lable">字段名称:</td>
		    			<td class="honry-view">${tableFiled.fieldName }</td>
	    			</tr>
					<tr>
						<td class="honry-lable">类型:</td>
		    			<td class="honry-view">${tableFiled.fieldType }</td>
	    			</tr>
					<tr>
						<td class="honry-lable">长度:</td>
		    			<td class="honry-view">${tableFiled.fieldLength }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">java类型:</td>
		    			<td class="honry-view">${tableFiled.javaType }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">备注:</td>
		    			<td class="honry-view">${tableFiled.remarks }</td>
	    			</tr>
				</table>
				</form>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
		</div>
	</div>
<script type="text/javascript">

   //关闭页面
	function closeLayout(){
		$('#divLayout').layout('remove','east');
		$("#list").datagrid("reload");
	}
	</script>
</body>