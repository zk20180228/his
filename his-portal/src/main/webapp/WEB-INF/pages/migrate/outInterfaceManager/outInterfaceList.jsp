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
<title>对外接口管理</title>
<script type="text/javascript">
var timeMap={'S':'秒','M':'分','H':'时','D':'天','W':'周'};
var menuAlias="${menuAlias}";
var serverMap;//服务渲染
var firedMap;//厂商渲染
$(function(){
	bindEnterEvent('searchText',dateFrom,'easyui');//绑定回车事件
	//服务
	$.ajax({
		url: "<%=basePath%>migrate/outInterfaceManager/renderServer.action",		
		type:'post',
		success: function(date) {					
			serverMap= date;	
		}
	});
	//厂商
	$.ajax({
		url: "<%=basePath%>migrate/outInterfaceManager/renderFired.action",		
		type:'post',
		success: function(date) {					
			firedMap= date;	
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
		pageList: [20, 30, 50],
		pagination: true,
		method: "post",
		url: '<%=basePath%>migrate/outInterfaceManager/interfaceList.action',
		toolbar:'#toolbarId',
		queryParams:{queryCode:$('#searchText').textbox('getValue'),menuAlias:menuAlias,serviceState:$('#serviceState').combobox('getValue')},
		onLoadSuccess:function(data){    
       	 $("a[name='opera']").linkbutton({}); 
       	 $('#list').datagrid('unselectAll');
       	 $('#list').datagrid('uncheckAll');
		 },
		onDblClickRow:function(index, row){
			AddOrShowEast('EditForm', '<%=basePath%>migrate/outInterfaceManager/viewInter.action?id='+ row.id);
		}
	});
}
		
		
		function add() {
			var code=$("#codes").val();
			 AddOrShowEast('EditForm','<%=basePath%>migrate/outInterfaceManager/addInter.action','post'
					   ,{"codes":code});
		}

		function edit() {
			if (getIdUtil("#list") != null) {
				console.log(getIdUtil("#list"));
				AddOrShowEast('EditForm', '<%=basePath%>migrate/outInterfaceManager/editInter.action?id='+ getIdUtil("#list"));
			}
		}

		function del() {
			//选中要删除的行
			var rows = $('#list').datagrid('getChecked');
			if (rows.length > 0) {//选中几行的话触发事件	                        
				$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
					if (res) {
						var ids = '';
						for (var i = 0; i < rows.length; i++) {
							if (ids != '') {
								ids += ',';
							}
							ids += rows[i].id;
						}
						$.ajax({
							url : "<%=basePath%>migrate/outInterfaceManager/delInter.action?ids="+ ids,
							type : 'post',
							success : function(data) {
													$.messager
															.progress('close');
													$.messager.alert('提示',
															data.resMsg);
													if (data.resCode == 'success') {
														$('#list').datagrid(
																'reload');
													}
												}
											});
								}
							});
		} else {
			$.messager.alert('提示信息', '请选择要删除的信息！');
			setTimeout(function() {
				$(".messager-body").window('close');
			}, 3500);
		}
	}

	function clear() {
		$('#searchText').textbox('setValue','');
		$('#serviceState').combobox('clear');
		//实现刷新栏目中的数据
		dateFrom();
	}


	//按回车键提交表单！
	$('#searchTab').find('input').on('keyup', function(event) {
		if (event.keyCode == 13) {
			searchFrom();
		}
	});
	//列表查询重置
	function searchReload() {
		$('#name').textbox('setValue', '');
		$('#code').textbox('setValue', '');
		searchFrom();
	} 
	//渲染时间
	function formatterTime(value,row,index){
		if(value!=null&&value!=''){
			return timeMap[value];
		}
		return value;
	}
	//显示接口读写
	function formatterrwJuri(value, row, index) {
		if (value == "0") {
			return "只读"
		}
		if (value == "1") {
			return "只写"
		}
		if (value == "2"){
			return "读写"
		}
	}
	//接口状态
	function formatterstate(value, row, index) {
		if (value == "0") {
			return "正常"
		}
		if (value == "1"){
			return "停用"
		}
	}
	//是否安全认证
	function formatterisAuth(value, row, index) {
		if (value == "0") {
			return "是"
		}
		if (value == "1") {
			return "否"
		}
	}
	/**
	 * 动态添加LayOut
	 * @author  liujl
	 * @param title 标签名称
	 * @param url 跳转路径
	 * @date 2015-05-21
	 * @modifiedTime 2015-6-18
	 * @modifier liujl
	 * @version 1.0
	 */
	function AddOrShowEast(title, url, method, params) {
		if (!method) {
			method = "get";
		}
		if (!params) {
			params = {};
		}
		var eastpanel = $('#panelEast'); //获取右侧收缩面板
		if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
			//重新装载右侧面板
			$('#divLayout').layout('panel', 'east').panel({
				href : url,
				method : method,
				queryParams : params,
			});
		} else {//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : '30%',
				split : true,
				href : url,
				method : method,
				queryParams : params,
				closable : true
			});
		}
	}
	//停用  启用
	
	function updateState(id,rowstate){
		var tip='启用';
		if(rowstate=='1'){
			tip='停用';
		}
		$.messager.confirm("提示","确认"+tip+"数据表？",function(r){
			if(r){
				$.messager.progress({text:'处理中，请稍后...',modal:true});
				$.ajax({
					url:'<%=basePath%>migrate/outInterfaceManager/updateState.action',
					data : {id:id,state:rowstate},
					success : function(result){
						$.messager.progress('close');
						$.messager.alert('提示',result.resMsg);
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
		var htm='<a  name="opera" href="javascript:void(0);" onclick="logAccess(\''+row.code+'\')" style="width:50px;height:20px;margin-left:40px;" class="easyui-linkbutton" >日志</a>&nbsp;';
		if(row.state!=null&&row.state=='1'){
			htm+='<a name="opera"  href="javascript:void(0);updateState(\''+row.id+'\',\'0\');" style="width:50px;height:20px;" class="easyui-linkbutton" >启用</a>';
		}else{
			htm+='<a  name="opera" href="javascript:void(0);updateState(\''+row.id+'\',\'1\');" style="width:50px;height:20px;" class="easyui-linkbutton" >停用</a>';
		}
	return htm;
}

//跳转到日志查询页面
function logAccess(code){
	window.open('<%=basePath %>migrate/LogAccess/logAccess.action?code='+code,'日志查询','width='+(window.screen.availWidth-150)+',height='+(window.screen.availHeight-90)+',top=20,left=60,resizable=yes,status=yes,menubar=no,scrollbars=yes');
}

function serverReader(value,row,index){
	if(value!=null&&value!=''){
		return serverMap[value];
	}
	return value;
}
function firecodeReader(value,row,index){
	if(value!=null&&value!=''){
		return firedMap[value];
	}
	return value;
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
	<div id="divLayout" class="easyui-layout"
		data-options="fit:true,border:false">
		<div data-options="region:'north',border:false" style="width: 100%; height: 40px">
			<form id="searchForm" style="padding-top:7px;padding-left:5px;padding-right:5px;">
				<table border="0">
					<tr>
						<td style="width: 250px;" >查询条件:<input id="searchText" class="easyui-textbox" style="width:150px;" data-options=""> </td>
						<td  style="width:250px;">状态:<input id="serviceState"  class="easyui-combobox"  style="width:150px;" readonly="readonly"/></td>
						<td align="right">
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0);searchFrom();"  class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left: 3px">查询</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0);clear();"  class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false" style="width: 100%;">
			<input type="hidden" id="codes">
			<table id="list" data-options="pagination:true,fit:true" >
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true"></th>
						<th data-options="field:'id',hidden:true">id</th>
						<th data-options="field:'code',width:'4%'" >接口代码</th>
						<th data-options="field:'name',width:'7%'" >接口名称</th>
						<th data-options="field:'serve',width:'14%'" >接口服务</th>
						<th data-options="field:'serveCode',width:'9%',formatter:serverReader" >服务名称</th>
						<th data-options="field:'parameterField',width:'4%'" >参数字段</th>
						<th data-options="field:'firmCode',width:'13%',formatter:firecodeReader" >厂商名称</th>
<!-- 						<th data-options="field:'way',width:'5%'" align="center">接口方式</th> -->
						<th data-options="field:'curWay',width:'7%'" >当前方式</th>
						<th data-options="field:'rwJuri',width:'5%',formatter:formatterrwJuri" >接口读写</th>
						<th data-options="field:'callSapce',width:'7%'" >接口调用间隔</th>
						<th data-options="field:'callUnit',width:'5%',formatter:formatterTime" >间隔单位</th>
						<th data-options="field:'frequency',width:'5%'" >频次</th>
						<th data-options="field:'isAuth',width:'7%',formatter:formatterisAuth" >是否安全认证</th>
						<th data-options="field:'authVali',width:'5%'" >认证有效期</th>
						<th data-options="field:'authUnit',width:'7%',formatter:formatterTime" >认证有效期单位</th>
						<th data-options="field:'authStime',width:'10%'" >有效期开始时间</th>
						<th data-options="field:'authEtime',width:'10%'" >有效期结束时间</th>
						<th data-options="field:'state',width:'5%',formatter:formatterstate" >状态</th>
						<th data-options="field:'operation',width:200,formatter:fromatterButton" style="padding-left: 5px;" >操作</th>
						<th data-options="field:'implementSql',width:'20%'" >执行SQL</th>
						<th data-options="field:'remarks',width:'9%'" >备注</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>


	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-add',plain:true">添加</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="edit()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-edit',plain:true">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">
			<a href="javascript:void(0)" onclick="del()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
	</div>

</body>
</html>