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
	<title>日志查询</title>
</head>
<script type="text/javascript">
var resultMap={'0':'成功','1':'失败'};//调用结果
var menuAlias="${menuAlias}";
var code = "${code}";
var firmMap;
$(function(){
	$('#searchText').textbox({});
	bindEnterEvent('searchText',searchForm,'easyui');//绑定回车事件
	$.ajax({
		url: "<%=basePath%>migrate/firmMainTain/renderFirm.action",		
		type:'post',
		success: function(date) {					
			firmMap= date;	
		}
	});
	searchForm();
});
function firmReader(value,row,index){
	if(value!=null&&value!=''){
		return firmMap[value];
	}
	return value;
}
//查询
function searchForm(){
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
		url: '<%=basePath%>/migrate/LogAccess/queryLogAccess.action',
		toolbar:'#toolbarId',
		queryParams:{param:$('#searchText').textbox('getValue'),menuAlias:menuAlias,code:code},
		onLoadSuccess:function(data){    
        	 $("a[name='opera']").linkbutton({});    
		 }
	});
}
//渲染状态0用1停用
function formatterResult(value,row,index){
	if(value=='0'||value!=null&&value!=''){
		return resultMap[value];
	}
	return value;
}
//重置
function clear(){
	$('#searchText').textbox('setValue','');
	searchForm();
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
								<a href="javascript:void(0);searchForm();"  class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left: 3px">查询</a>
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
					 	<th data-options="field:'id',hidden:true">服务代码</th>
						<th data-options="field:'interName',width:150" align="center" >接口名称</th>
						<th data-options="field:'vendorCode',width:200,formatter:firmReader" align="center" >厂商代码</th>
						<th data-options="field:'interPara',width:200" align="center">传递参数</th>
						<th data-options="field:'accessIp',width:150" align="center" >客户端IP</th>
						<th data-options="field:'accessTime',width:150" align="center">调用时间</th>
						<th data-options="field:'authInfo',width:150" align="center">认证信息</th>
						<th data-options="field:'result',width:150,formatter:formatterResult" align="center">调用结果</th>
						<th data-options="field:'errorInfo',width:150" align="center">失败原因</th>
						<th data-options="field:'createtime',width:150" align="center">创建时间</th>
					</tr> 
				</thead>
			</table>
	    </div>
	</div> 
</body>
</html>