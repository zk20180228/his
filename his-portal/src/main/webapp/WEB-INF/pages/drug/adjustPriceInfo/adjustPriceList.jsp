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
<style type="text/css">
#cc .tabs-header,#cc .tabs-header .tabs{
	border:0
}
#cc .tabs-header .tabs{
	margin-top:4px;
}
</style>
</head>
	<body style="margin: 0px;padding: 0px;">
	<div id="cc" class="easyui-layout" fit="true">
		<div data-options="region:'north',border:false"style="width:100%;height:30px;">
			<div id="tt" class="easyui-tabs" style="width:100%;height:30px;">
				<div title="添加调价药品" id="01" >
				</div>
				<div title="查看历史" id="02" >
				</div>
				<div title="查询" id="03">
				</div>
			</div>
		</div>
		<div data-options="region:'center',border:false">
			<iframe src="" name="iframe" id="iframe"
				style="width:100%; height:100%;border: none;"></iframe>
		</div>
	</div>
		<script type="text/javascript">
		var manufacturer = '';
		$(function(){
			//渲染生产厂家
			$.ajax({
				url: '<%=basePath%>drug/adjustPriceInfo/drugManufacturerCombobox.action',
				type:'post',
				success: function(data) {
					manufacturer = data;
					$('iframe').attr('src',"<%=basePath %>drug/adjustPriceInfo/toEditView.action");
				}
			});
			$('#tt').tabs({
				onSelect: function(title,index){
					var url = ''
					if(index == 0){
						url = "<%=basePath %>drug/adjustPriceInfo/toEditView.action";
					}else if(index == 1){
						url = "<%=basePath %>drug/adjustPriceInfo/toHistroyView.action";
					}
					else{
						url = "<%=basePath %>drug/adjustPriceInfo/toSearchView.action";
					}
					$('iframe').attr('src',url);
				}
			});
		})
			
		</script>
	</body>
</html>
