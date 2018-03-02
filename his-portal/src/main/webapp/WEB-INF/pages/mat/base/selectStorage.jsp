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
		<input type="hidden" id="kindType" name="kindType" value="${kindType}">
		<input type="hidden" id="kindPath" name="kindPath" value="${kindPath}">
		<input type="hidden" id="preCode" name="preCode" value="${preCode}">
		<table >
			<tr>
				<td>所属仓库：</td>
				<td><input class="easyui-combobox" id="storageCode" name="storageCode"></td>
			</tr>
		</table>
		<div style="text-align: center; padding: 5px">
			<a href="javascript:submitStorageCode();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>
		</div>
		
		<script type="text/javascript">
			$('#storageCode').combobox({ 
				valueField:'id',    
			    textField:'deptName',
				url : "<c:url value='/material/base/getDepartmentContact.action'/>"   
			});
			function submitStorageCode(){
				closeDialog('selectStorage');
				Adddilogs("编辑分类","<c:url value='/material/base/addMatKindinfoUrl.action'/>?parentId="+$('#preCode').val()+"&kindType="+$('#kindType').val()+
																						"&kindPath="+$('#kindPath').val()+"&storageCode="+$('#storageCode').combobox('getValue')+
																						"&storageName="+$('#storageCode').combobox('getText'),"addMatKindinfo");
			}
			
		</script>
	</body>
</html>