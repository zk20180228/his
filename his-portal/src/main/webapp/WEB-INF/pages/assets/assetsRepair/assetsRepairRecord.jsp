<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
	<div id="cc" class="easyui-layout" fit="true";"> 
    	<input type="hidden"  name="assetsDeviceMaintain.deviceNo" value="${assetsDeviceMaintain.deviceNo }">
	    <input type="hidden"  name="assetsDeviceMaintain.state" value="${assetsDeviceMaintain.state }"> 
    	<div data-options="region:'center',border:false" ">
    		<table  id="unratified" class="easyui-datagrid"   
		        data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,
		        singleSelect:true,fitColumns:true,fit:true,pagination:true,toolbar:'#toolbarId3'">   
			    <thead> 
			        <tr>   
		           		<th data-options="field:'id',width:100,align:'center',hidden:true"></th>
			            <th data-options="field:'applName',width:150,align:'center'">负责人</th>   
			            <th data-options="field:'applDate',width:200,align:'center'">维修时间</th>  
			            <th data-options="field:'repairReson',width:150,align:'center'">维修内容</th>   
			        </tr>   
			    </thead>   
			</table>  
    	
    	</div>
   	</div>
	<script type="text/javascript">
		$(function(){
			var deviceNo ="${assetsDeviceMaintain.deviceNo }";
			var state = "${assetsDeviceMaintain.state }"
			$("#unratified").datagrid({
				url: '<%=basePath %>assets/assetsRepair/queryRepairRecode.action?deviceNo='+deviceNo,
				pageSize:10,
				pageList:[10,20,30,50,80,100],
				pagination:true
			})
			
		});
	</script>
</body>
</html>