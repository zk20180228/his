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
	<title>服务管理日志查询</title>
</head>
<script type="text/javascript">
var menuAlias="${menuAlias}";
var serverMap;//服务渲染
$(function(){
	$('#searchText').textbox({});
	bindEnterEvent('searchText',queryFrom,'easyui');//绑定回车事件
	$.ajax({
		url: "<%=basePath%>migrate/outInterfaceManager/renderServer.action",
		async:false,
		type:'post',
		success: function(date) {					
			serverMap= date;	
		}
	});
	searchForm("${code }");
});
//服务名渲染
function serverReader(value,row,index){
	if(value!=null&&value!=''){
		return serverMap[value];
	}
	return value;
}
//查询
function searchForm(code){
	$('#list').datagrid({
		rownumbers: true,
		pageSize: "20",
		fit:true,
		singleSelect:true,
		checkOnSelect:false,
		selectOnCheck:false,
		pageList: [10, 20, 30, 50, 80, 100],
		pagination: true,
		method: "post",
		url: '<%=basePath%>/migrate/ServiceManagement/queryLogService.action',
		queryParams:{serviceName:code,menuAlias:menuAlias}
	});
}
function queryFrom(){
	if($('#searchText').textbox('getValue')!=null&&$('#searchText').textbox('getValue')!=''){
		searchForm($('#searchText').textbox('getValue'));
	}else{
		searchForm("${code }");
	}
}
//重置
function clear(){
	$('#searchText').textbox('setValue','');
	searchForm("${code }");
}
</script>
<body>
	<div id="layoutId" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'north',split:false,border:false" style="height:40px;">
	    	<form id="searchForm" style="padding-top:7px;padding-left:5px;padding-right:5px;">
				<table border="0">
					<tr align="left">
						<td style="width: 190px;" >
							<input id="searchText"  > 
						</td>
						<td align="right">
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0);queryFrom();"  class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left: 3px">查询</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0);clear();"  class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</form>
	    </div>
	    <div data-options="region:'center'" style="padding-right: 5px;">
	    	<table id="list" data-options="pagination:true,fit:true" class="easyui-datagrid">
				<thead>
					 <tr >
					 	<th data-options="field:'id',hidden:true">id</th>
						<th data-options="field:'serviceCode',width:150,formatter:serverReader">服务代码</th>
						<th data-options="field:'ip',width:150" align="center">ip</th>
						<th data-options="field:'heartNewTime',width:150" align="center" >最新心跳时间</th>
						<th data-options="field:'heartRate',width:150" align="center" >心跳频率</th>
					</tr> 
				</thead>
			</table>
	    </div>
	</div> 
</body>
</html>