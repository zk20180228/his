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
	<div id="device" class="easyui-layout" fit="true">
		<div data-options="region:'north',border:false"style="width:100%;height:30px;">
			<div id="deviceList" class="easyui-tabs" style="width:100%;height:30px;">
				<div title="我的设备" id="01" >
				</div>
				<div title="设备领用" id="02" >
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
			$('iframe').attr('src',"<%=basePath %>assets/deviceDossier/deviceMyUse.action");
			$('#deviceList').tabs({
				onSelect: function(title,index){
					var url = ''
					if(index == 0){//我的设备
						url = "<%=basePath %>assets/deviceDossier/deviceMyUse.action";
					}else{//设备领用
						url = "<%=basePath %>assets/deviceDossier/deviceReceive.action";
					}
					$('iframe').attr('src',url);
				}
			});
			
		})
			
		</script>
	</body>
</html>
