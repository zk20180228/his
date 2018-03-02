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
				<div title="入库中" id="01" >
				</div>
				<div title="草稿箱" id="02" >
				</div>
				<div title="已入库" id="03">
				</div>
				<div title="未批准" id="04">
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
			$('iframe').attr('src',"<%=basePath %>assets/device/deviceStorage.action");
			$('#deviceList').tabs({
				onSelect: function(title,index){
					var url = ''
					if(index == 0){//入库中
						url = "<%=basePath %>assets/device/deviceStorage.action";
					}else if(index == 1){//草稿箱
						url = "<%=basePath %>assets/device/deviceDraft.action";
					}else if(index == 2){//已入库
						url = "<%=basePath %>assets/device/deviceApproval.action";
					}else{//未批准
						url = "<%=basePath %>assets/device/deviceNotApp.action";
					}
					$('iframe').attr('src',url);
				}
			});
			
		})
			
		</script>
	</body>
</html>
