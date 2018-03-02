<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style type="text/css">
				.panel-body,.panel{
					overflow:visible;
				}
				
				.combo-panel{
					overflow:auto !important;
				}
				
				.addList dl:first-child ul {
					overflow:visible !important; 
					clear:both;
				}
				.clearfix:after{
					content:"";
					display:table;
					height:0;
					visibility:hidden;
					clear:both;
				}
				.xmenu dl dd ul{
					overflow:visible !important; 
					clear:both;
				}
				.clearfix{
				*zoom:1; 
				}
		</style>
<title></title>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var map = new Map();
$(function(){
	map.put('5m','每5分钟一次');
	map.put('10m','每10分钟一次');
	map.put('15m','每15分钟一次');
	map.put('20m','每20分钟一次');
	map.put('30m','每30分钟一次');
	map.put('1h','每1小时一次');
	map.put('2h','每2小时一次');
	map.put('3h','每3小时一次');
	map.put('4h','每4小时一次');
	map.put('6h','每6小时一次');
	map.put('8h','每8小时一次');
	map.put('12h','每12小时一次');
	$('#dd').dialog('close');
	$('#list').datagrid({
		url:'<%=basePath%>sys/timingRule/getTimingRule.action',
		pagination : true,
		pageSize : 20,
		pageList : [20,40,60,80,100]
	});
	$(".deptInput").MenuList({
		width :710,
		height :500,
		menulines:2,
		dropmenu:"#m2",//弹出层
		isSecond:false,
		para:'',
		firsturl: '<%=basePath%>sys/timingRule/getMenuList.action?para=',
		spreadNum:1
	});
	
});
function submitT(){
	$('#editForm').form('submit',{
		url:'<%=basePath%>sys/timingRule/saveOrUpdate.action',
		success : function(data){
			$.messager.progress('close');
			data = JSON.parse(data);
			$.messager.alert('提示',data.resMsg,'info',function(){
				$('#dd').dialog('close');
				$('#list').datagrid('reload');
			});
		}
		,error : function(){
			$.messager.progress('close');
			$.messager.alert('提示','网络繁忙，请稍后重试...');
		}
	});
}
function add(){
	Adddilog('添加', "<%=basePath %>sys/timingRule/addOrEdit.action", '600px', '300px');
}
function edit(){
	var row = $('#list').datagrid('getSelected');
	if(row==null||row==""){
		$.messager.alert('提示','请选择一条记录');
		return ;
	}
	Adddilog('添加', "<%=basePath %>sys/timingRule/addOrEdit.action?id="+row.id, '600px', '300px');
}
function del(){
	var row = $('#list').datagrid('getSelected');
	if(row==null||row==""){
		$.messager.alert('提示','请选择一条记录');
		return ;
	}
	$.messager.progress({text:'删除中，请稍后...',modal:true});
	$.ajax({
		url : '<%=basePath%>sys/timingRule/delTimingRules.action',
		data:{id:row.id},
		success : function(data){
			$.messager.progress('close');
			$.messager.alert('提示',data.resMsg,'info',function(){
				$('#list').datagrid('reload');
			});
		}
		,error : function(){
			$.messager.progress('close');
			$.messager.alert('提示','网络繁忙，请稍后重试...');
		}
	});
}
/**
*加载模式窗口
*/
function Adddilog(title, url, width, height) {
	$('#dd').dialog({    
	    title: title,    
	    width: width,    
	    height: height,    
	    closed: false,    
	    cache: false,    
	    href: url,    
	    modal: true,
	   });
	$('#dd').window('center')
}
function closeDialog(){
	$('#dd').dialog('close');
}
function fmttype(value,row,index){
	if(value=="3"){
		return "年";
	}else if(value=="2"){
		return "月";
	}else{
		return "日";
	}
}
function fmtstate(value,row,index){
	if(value=="1"){
		return "开启";
	}else{
		return "关闭";
	}
}
function fmtRules(value,row,index){
	return map.get(value);
}
function searchMenu(){
	var type = $('#type').combobox('getValue');
	var dept = $('#ksnew').getMenuIds();
	$('#list').datagrid('reload',{menuType:dept,type:type});
}
</script>
</head>
<body>
<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;"fit=true>
	<div data-options="region:'north',border:false" style="width:100%;height:40px;">
		<form id="search" method="post">
			<table>
				<tr>
					<td>类型:</td>
					<td>
						<input class="easyui-combobox" id="type" name="rules.type" value="${rules.type }" 
			    							data-options="valueField: 'id',textField: 'text',
			    								data:[{'id' : 3,'text' : '年'},{'id' : 2,'text' : '月'},{'id' : 1,'text' : '日'}]  "  style="width:200px" /></td>
					</td>
					<td style="width:55px;" align="center">栏目:</td>
					<td class="newMenu" style="width:110px;z-index:30;position: relative;">
						<div class="deptInput menuInput">
							<input class="ksnew" id="ksnew" readonly="readonly"/><span></span></div> 
						<div id="m2" class="xmenu" style="display: none; ">
							<div class="searchDept" >
								<input type="text" name="searchByDeptName" placeholder="回车查询"/>
								<span class="searchMenu"><i></i>查询</span>
								<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
									<span class="a-btn-text">取消</span>
								</a>						
								<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
									<span class="a-btn-text">清空</span>
								</a>
								<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
									<span class="a-btn-text">确定</span>
								</a>
							</div>
							<div class="select-info" style="display:none;">
								<label class="top-label">已选栏目：</label>
								<ul class="addDept">
								</ul>
							</div>	
							<div class="depts-dl">
								<div class="addList"></div>
								<div class="tip" style="display:none">没有检索到数据</div> 
							</div>	
						</div>
					</td>
					<td>
						<shiro:hasPermission name="${menuAlias}:function:query"> 
							<a href="javascript:void(0)" onclick="searchMenu()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						</shiro:hasPermission>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false" >
		<table id="list" class="easyui-datagrid" fit="true" style="width:100%;" data-options="method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true" ></th>
					<th data-options="field:'aliasName',width:'15%'">栏目名称</th>
					<th data-options="field:'type',width:'15%',formatter:fmttype">类型</th>
					<th data-options="field:'rules',width:'20%',formatter:fmtRules">规则</th>
					<th data-options="field:'state',width:'15%',formatter:fmtstate">状态</th>
					<th data-options="field:'remark',width:'15%'">备注</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add"> 
 			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a> 
		</shiro:hasPermission> 
		<shiro:hasPermission name="${menuAlias}:function:edit"> 
 			<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a> 
 		</shiro:hasPermission> 
 		<shiro:hasPermission name="${menuAlias}:function:delete">	
 			<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a> 
 		</shiro:hasPermission> 
	</div>
	<div id="dd" class="easyui-dialog"></div>
</div>
</body>
</html>