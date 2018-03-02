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
	<div id="RolelistdivLayout" class="easyui-layout" style="width:100%;height:100%;">  
     <div data-options="region:'center',split:false,title:'',border:false" >
					<table id="Rolelist" class="easyui-datagrid" data-options="url:'${pageContext.request.contextPath}/sys/queryRole.action?userId=${userId}',idField:'id',treeField:'name',animate:false,rownumbers:true,striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',border:false">
						<thead>
							<tr>
								<th data-options="field:'name'">拥有角色名称</th>
								<th data-options="field:'alias'">角色别名</th>
								<th data-options="field:'description'">角色描述</th>
								<th data-options="field:'parentRoleId',hidden:true">父级</th>
								<th data-options="field:'menufunction'">层级路径</th>
								<th data-options="field:'order'">排序</th>
								<th data-options="field:'uppath',hidden:true">所有父级</th>
							</tr>
						</thead>
					</table>
		</div>  
   	 </div>    
	<input id="paramUserId" type="hidden" value="${userId }"/>
	</body>
</html>