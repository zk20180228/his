<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
<div class="easyui-panel"  style="padding: 10px;" data-options="fit:'true',border:'false'">
	<div style="text-align:north;padding:5px;width: 100%;height:70%">
		<form id="editForm" method="post">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%;height:100%">
				<tr>
					<td class="honry-lable">内容:</td>
					<td class="honry-info">
						<textarea id="content" style="width: 100%;height:100%" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div style="text-align:center;padding:5px">
		<a href="javascript:testreturn();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>
	</div>
</div>
<script type="text/javascript">
	function testreturn(){
				var content = $('#content').val();
				cuibanSubmit(content);
				$('#dialogDivId').dialog('close');//关闭模式窗口
	}
</script>
</body>
</html>