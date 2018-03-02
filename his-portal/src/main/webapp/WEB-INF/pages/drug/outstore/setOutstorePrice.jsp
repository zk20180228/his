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
		<div id="p" class="easyui-panel"  style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
			<form id="editForm">
				设置价让率：<input class="easyui-numberbox" id="setPriceRate" name="setPriceRate">
			</form>
			<br>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeDialog()" class="easyui-linkbutton">关闭</a>
			</div>	
		</div>
		<script type="text/javascript">
		  	function submit(){
		  		var setPriceRate=$('#setPriceRate').numberbox('getValue');
		  		$('#priceRate').numberbox('setValue',parseInt(setPriceRate)+1.1);
		  		closeDialog();
		  		reCalculateRetailPrice();
		  	}
		</script>
	</body>
</html>
