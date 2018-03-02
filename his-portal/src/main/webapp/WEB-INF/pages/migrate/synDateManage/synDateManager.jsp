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
	<title>数据同步管理</title>
</head>
<script type="text/javascript">
var menuAlias="${menuAlias}";
//时间间隔（单位）S秒M分H时D天W周
var timeMap={'S':'秒','M':'分','H':'时','D':'天','W':'周'};
var workSignMap={'0':'是','1':'否'};
var synchSignMap={'0':'增量','1':'全量'};
var stateMap={'0':'启用','1':'停用'};
var serverMap;//服务渲染
$(function(){
	$('#searchText').textbox({});
	bindEnterEvent('searchText',dateFrom,'easyui');//绑定回车事件
	$.ajax({
		url: "<%=basePath%>migrate/outInterfaceManager/renderServer.action",		
		type:'post',
		success: function(date) {					
			serverMap= date;	
		}
	});
	$('#serviceState').combobox({
		valueField: 'id',
		textField: 'value',
		data:[
		{id:'0',value:'正常'},
		{id:'1',value:'停用'}
		]
	});
	dateFrom();
	
});
//查询
function searchFrom(){
	dateFrom();
}
function dateFrom(){
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
		url: '<%=basePath%>/migrate/SynDateManager/querySynDate.action',
		toolbar:'#toolbarId',
		queryParams:{queryCode:$('#searchText').textbox('getValue'),menuAlias:menuAlias,dateState:$('#serviceState').combobox('getValue')},
		onLoadSuccess:function(data){    
        	 $("a[name='opera']").linkbutton({});
        	 $('#list').datagrid('unselectAll');
           	 $('#list').datagrid('uncheckAll');
		},
		onDblClickRow:function(index, row){
			window.open('<%=basePath %>/migrate/SynDateManager/viewSynDate.action?queryCode='+row.id,'同步数据管理','width='+(window.screen.availWidth-150)+',height='+(window.screen.availHeight-90)+',top=20,left=60,resizable=yes,status=yes,menubar=no,scrollbars=yes');
		}
	});
}
//跳转到添加修改页面
function add(){
	window.open('<%=basePath %>/migrate/SynDateManager/addSynDate.action','同步数据管理','width='+(window.screen.availWidth-150)+',height='+(window.screen.availHeight-90)+',top=20,left=60,resizable=yes,status=yes,menubar=no,scrollbars=yes');
}
//清空
function clear(){
	$('#searchText').textbox('setValue','');
	$('#serviceState').combobox('clear');
	dateFrom();
}
function del(){
	var row=$("#list").datagrid('getSelected');
	if(row!=null){
		$.messager.confirm("提示","确认删除选中数据？",function(r){
			if(r){
				$.messager.progress({text:'处理中，请稍后...',modal:true});
				$.ajax({
					url:'<%=basePath%>migrate/SynDateManager/delSynDate.action',
					data : {queryCode:row.id},
					success : function(result){
						$.messager.progress('close');
						$.messager.alert('提示',result.resMsg,result.resCode);
						dateFrom();
					}
					,error : function(){
						$.messager.progress('close');
						$.messager.alert('提示','网络繁忙,请稍后重试...','info');
					}
				});
			}
		});
	}else{
		$.messager.alert('提示','请选择删除数据','info');
	}
	
}
function update(){
	var row=$('#list').datagrid('getSelected');//获取选中行
	if(row!=null){
		window.open('<%=basePath %>/migrate/SynDateManager/addSynDate.action?queryCode='+row.id,'同步数据管理','width='+(window.screen.availWidth-150)+',height='+(window.screen.availHeight-90)+',top=20,left=60,resizable=yes,status=yes,menubar=no,scrollbars=yes');
	}else{
		$.messager.alert('提示','请选中修改数据','info');
	}
}
//渲染时间
function formatterTime(value,row,index){
	if(value!=null&&value!=''){
		return timeMap[value];
	}
	return value;
}
//渲染是否业务关联0是1否
function formatterWork(value,row,index){
	if(value=='0'||value!=null&&value!=''){
		return workSignMap[value];
	}
	return value;
}
//同步方式0增量1全量
function formatterSynch(value,row,index){
	if(value=='0'||value!=null&&value!=''){
		return synchSignMap[value];
	}
	return value;
}
//渲染状态0用1停用
function formatterState(value,row,index){
	if(value=='0'||value!=null&&value!=''){
		return stateMap[value];
	}
	return value;
}
function updateState(id,rowstate,tableName){
	var tip='启用';
	if(rowstate=='1'){
		tip='停用';
	}
	$.messager.confirm("提示","确认"+tip+tableName+"数据表？",function(r){
		if(r){
			$.messager.progress({text:'处理中，请稍后...',modal:true});
			$.ajax({
				url:'<%=basePath%>migrate/SynDateManager/updateState.action',
				data : {id:id,state:rowstate},
				success : function(result){
					$.messager.progress('close');
					$.messager.alert('提示',result.resMsg,result.resCode);
					if('success'==result.resCode){
						dateFrom();
					}
				}
				,error : function(){
					$.messager.progress('close');
					$.messager.alert('提示','网络繁忙,请稍后重试...','info');
				}
			});
		}
	});
}
//渲染按钮 
function  fromatterButton(value,row,index){
		var htm="<a  name='opera' href='javascript:void(0);' style='width:50px;height:20px;' class='easyui-linkbutton' >手动</a>&nbsp;";
		htm+='<a  name="opera" onclick="logManage(\''+row.code+'\')" href="javascript:void(0);" style="width:50px;height:20px;" class="easyui-linkbutton" >日志</a>&nbsp;';
		if(row.state!=null&&row.state=='1'){
			htm+='<a id=\'"+row.id+"\' name="opera"  href="javascript:void(0);updateState(\''+row.id+'\',\'0\',\''+row.tableName+'\');" style="width:50px;height:20px;" class="easyui-linkbutton" >启用</a>';
		}else{
			htm+='<a  id=\''+row.id+'\' name="opera" href="javascript:void(0);updateState(\''+row.id+'\',\'1\',\''+row.tableName+'\');" style="width:50px;height:20px;" class="easyui-linkbutton" >停用</a>';
		}
	return htm;
}
function serverReader(value,row,index){
	if(value!=null&&value!=''){
		return serverMap[value];
	}
	return value;
}
//跳转到日志查询页面
function logManage(code){
	window.open('<%=basePath %>migrate/LogManage/logManage.action?code='+code,'日志查询','width='+(window.screen.availWidth-150)+',height='+(window.screen.availHeight-90)+',top=20,left=60,resizable=yes,status=yes,menubar=no,scrollbars=yes');
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<body>
	<div id="layoutId" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'north',split:false,border:false" style="height:40px;">
	    	<form id="searchForm" style="padding-top:7px;padding-left:5px;padding-right:5px;">
				<table border="0">
					<tr align="left">
						<td style="width: 230px;" >
							查询条件:<input id="searchText" class="easyui-textbox" data-options="" style="width:150px;" > 
						</td>
						<td  style="width:230px;">状态:<input id="serviceState"  class="easyui-combobox"  style="width:150px;" readonly="readonly"/></td>
						<td align="left">
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a id="searchFrom" href="javascript:void(0);searchFrom();"  class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left: 3px">查询</a>
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
	    	<table id="list" data-options="pagination:true,fit:true" >
				<thead>
					 <tr >
					 	<th data-options="field:'ck',checkbox:true "></th>
					 	<th data-options="field:'id',hidden:true">同步代码</th>
						<th data-options="field:'code',width:100">同步代码</th>
						<th data-options="field:'tableName',width:200" >相关表</th>
						<th data-options="field:'tableZhName',width:150"  >表名</th>
						<th data-options="field:'tableFromUser',width:150"  >所属用户</th>
						<th data-options="field:'primaryColum',width:200"  >主键</th>
						<th data-options="field:'viewName',width:250" >所在视图</th>
						<th data-options="field:'viewZhName',width:250"  >视图名称</th>
						<th data-options="field:'workSign',width:100,formatter:formatterWork" >是否业务关联</th>
						<th data-options="field:'synchSign',width:100,formatter:formatterSynch" >同步方式</th>
						<th data-options="field:'synchCond',width:100" >增量字段</th>
						<th data-options="field:'threadNum',width:100" >开启线程数</th>
						<th data-options="field:'timeSpace',width:100" >同步间隔</th>
						<th data-options="field:'timeUnit',width:100,formatter:formatterTime" >间隔单位</th>
						<th data-options="field:'synchLength',width:100" >同步时长</th>
						<th data-options="field:'synchUnit',width:100,formatter:formatterTime" >时长单位</th>
						<th data-options="field:'schema',width:100" >schema(账户)</th>
						<th data-options="field:'queryField',width:100" >查询字段</th>
						<th data-options="field:'tablePartition',width:100" >分区字段</th>
						<th data-options="field:'groupFiled',width:100" >分组字段</th>
						<th data-options="field:'orderFiled',width:100" >排序字段</th>
						<th data-options="field:'orderCond',width:100" >排序条件</th>
						<th data-options="field:'tableOrder',width:100" >表排序</th>
						<th data-options="field:'viewOrder',width:100" >视图排序</th>
						<th data-options="field:'serveCode',width:150,formatter:serverReader" >执行服务分类(主)</th>
						<th data-options="field:'serveCodeprepare',width:150,formatter:serverReader" >执行服务分类(备)</th>
						<th data-options="field:'newestTime',width:200" >最新同步时间</th>
						<th data-options="field:'state',width:100,formatter:formatterState" >状态</th>
						<th data-options="field:'operation',width:200,formatter:fromatterButton" style="padding-left: 5px;" >操作</th>
						<th data-options="field:'remarks',width:300" >备注</th>
					</tr> 
				</thead>
			</table>
	    </div>
	    <!-- 表格菜单 -->
	    <div id="toolbarId">
	    	<shiro:hasPermission name="${menuAlias}:function:add">
				<a href="javascript:void(0);add();" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
			</shiro:hasPermission>
	   		<shiro:hasPermission name="${menuAlias}:function:edit">
				<a href="javascript:void(0);update();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
			</shiro:hasPermission>
	   		<shiro:hasPermission name="${menuAlias}:function:delete">
				<a href="javascript:void(0);del();" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
			
	    </div>  
	</div> 
	
</body>
</html>