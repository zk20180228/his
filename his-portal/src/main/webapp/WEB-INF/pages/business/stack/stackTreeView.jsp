<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
<div class="easyui-layout" data-options='fit:true'>
	<div data-options="region:'center',border:false" style="padding:10px">
		<input type="hidden" id="id" name="id" value="${businessStack.id}">
		<input type="hidden" id="deptId" name="deptId" value="${businessStack.deptId}">
		<table class="honry-table" style="width: 100%">
			<tr>
				<td class="honry-lable">
					名称：
				</td>
				<td>
					${businessStack.name}
				</td>
			</tr>
			<tr>
				<td class="honry-lable">
					自定义码：
				</td>
				<td>
					${businessStack.inputCode }
				</td>
			</tr>
			<tr>
				<td class="honry-lable">
					备注:
				</td>
				<td>
					${businessStack.remark}
				</td>
			</tr>
		</table>	
		<div style="text-align: center; padding: 5px">
			<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeUpdate()" class="easyui-linkbutton">关闭</a>
		</div>
	</div>	
</div>
</body>
</html>