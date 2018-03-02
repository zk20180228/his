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
		<input type="hidden" id="tag" value="${tag}">
		<input type="hidden" id="row" value="${row}">
		<table >
			<tr>
				<td>备注：</td>
				<td><input class="easyui-textbox" id="meno" name="meno"></td>
			</tr>
		</table>
		<div style="text-align: center; padding: 5px">
			<a href="javascript:submitRemark();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>
		</div>
		<script type="text/javascript">
			function submitRemark (){
				if($("#tag").val()==0){
					$("#baseFile").datagrid("updateRow",{
						index: $("#row").val(),
						row: {
							memo:$("#meno").val()
						}
					});
				}else{
					$("#regFile").datagrid("updateRow",{
						index: $("#row").val(),
						row: {
							memo:$("#meno").val()
						}
					});
				}
				closeDialog('addRemarkDiv');
			}
		</script>
	</body>
</html>