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
var statusMap={'0':'运行中','1':'成功','2':'失败'};//调用结果
var synchTimeMap={'S':'秒','M':'分','H':'时','D':'天','W':'周'};
var menuAlias="${menuAlias}";
var code = "${code}";
$(function(){
	$('#searchText').textbox({});
	bindEnterEvent('searchText',searchForm,'easyui');//绑定回车事件
	searchForm();
});
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
		url: '<%=basePath%>/migrate/LogManage/queryLogManage.action',
		toolbar:'#toolbarId',
		queryParams:{param:$('#searchText').textbox('getValue'),menuAlias:menuAlias,code:code},
		onLoadSuccess:function(data){    
        	 $("a[name='opera']").linkbutton({});    
		 }
	});
}
//渲染状态0成功 1失败
function formatterStatus(value,row,index){
	if(value=='0'||value!=null&&value!=''){
		return statusMap[value];
	}
	return value;
}
//渲染时间
function formatterSynchTime(value,row,index){
	if(value!=null&&value!=''){
		return synchTimeMap[value];
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
						<th data-options="field:'serverCode',width:150">服务代码</th>
						<th data-options="field:'tableName',width:150" align="center">表名</th>
						<th data-options="field:'tableZhname',width:150" align="center" >表注释</th>
						<th data-options="field:'synchTime',width:150" align="center" >迁移任务起始时间</th>
						<th data-options="field:'interVal',width:150" align="center">频次</th>
						<th data-options="field:'interNuit',width:150,formatter:formatterSynchTime" align="center" >频次单位</th>
						<th data-options="field:'dataStime',width:150" align="center">任务开始时间</th>
						<th data-options="field:'dataEtime',width:150" align="center">任务结束时间</th>
						<th data-options="field:'synchNum',width:150" align="center">同步数量</th>
						<th data-options="field:'status',width:150,formatter:formatterStatus" align="center">状态</th>
						<th data-options="field:'createtime',width:150" align="center">创建时间</th>
						<th data-options="field:'updatetime',width:150" align="center">更新时间</th>
						<th data-options="field:'remarks',width:150" align="center">备注</th>
						<th data-options="field:'hissyschStartTime',width:170" align="center">同步历史数据开始时间</th>
						<th data-options="field:'hissyschUpdateTime',width:170" align="center">同步历史数据更新时间</th>
						<th data-options="field:'nowsyschStartTime',width:170" align="center">同步当前数据开始时间</th>
						<th data-options="field:'nowsyschUpdateTime',width:170" align="center">同步当前数据更新时间</th>
					</tr> 
				</thead>
			</table>
	    </div>
	</div> 
</body>
</html>