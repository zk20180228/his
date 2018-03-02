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
</head>
<body style="width: 100%;height: 100%;">
	<div  style="width: 100%;height: 100%;" data-options="fit:true">
	        <div data-options="region:'north',split:false,border:false" style="height: 30px;margin-left: 5px;">	        
				<table style="width:100%;border:false;collapsible:false;padding-top: 2px;">
					<tr>
						<td  >
						<input id="queryUser" data-options="prompt:'用户账户、姓名回车查询'" style="width: 150px;"/>
						<a href="javascript:void(0)" onclick="searchUserFrom()"class="easyui-linkbutton" iconCls="icon-search">查询</a>
						<a href="javascript:void(0)" onclick="clearsUser()" class="easyui-linkbutton" iconCls="reset">重置</a>
						<a href="javascript:void(0)" onclick="saveUser()" class="easyui-linkbutton" iconCls="icon-save">保存</a>
						</td>
					</tr>
				</table>
			</div>
			<div  style="width:100%;height:92%">
				<table id="userList" class="easyui-datagrid" data-options="">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'id',hidden:true" ></th>
							<th data-options="field:'account',width:200">用户账户</th>
							<th data-options="field:'name',width:200">用户姓名</th>
							<th data-options="field:'sex',formatter:functionSex,width:70">性别</th>
							<th data-options="field:'type',formatter:functionEmpType,width:200">类型</th>
							</tr>
					</thead>
				</table>				
			</div>	
	</div>
	<script type="text/javascript">
	var sexMap=new Map();//性别
	var empType=new Map();//人员类型
	/**
	 * 渲染性别
	 * @Author: zhangjin
	 * @CreateDate: 2017年2月17日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	$.ajax({
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=sex ",
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
	/**
	 * 渲染用户类别
	 * @Author: zhangjin
	 * @CreateDate: 2017年2月17日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	$.ajax({
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=empType ",
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				empType.put(v[i].encode,v[i].name);
			}
		}
	});
	$('#userList').datagrid({
		pagination:true,
		fitColumns:false,
		rownumbers: true,
		method:'post',
		idField: 'id',
		striped:true,
		border:true,
		checkOnSelect:true,
		selectOnCheck:false,
		singleSelect:true,
		fit:true,
		url:'<%=basePath%>mosys/adviceManage/findUserManageList.action',
   		pageSize:10,
   		pageList:[10,20,30,50],
   		onLoadSuccess: function(data){
			//分页工具栏作用提示
			var pager = $(this).datagrid('getPager');
			var aArr = $(pager).find('a');
			var iArr = $(pager).find('input');
			$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
			for(var i=0;i<aArr.length;i++){
				$(aArr[i]).tooltip({
					content:toolArr[i],
					hideDelay:1
				});
				$(aArr[i]).tooltip('hide');
			}
   		}
	});
	
	$(function(){
		$("#queryUser").textbox({});
		bindEnterEvent('queryUser',searchUserFrom,'easyui');
	});
	//查询
	function searchUserFrom(){
		$('#userList').datagrid('clearChecked');
	    $('#userList').datagrid('load', {
	    	queryUser: $('#queryUser').val(),
		});
	}
	/**
	 * 重置
	 * @author zxl
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function clearsUser(){
		$('#queryUser').textbox('setValue','');
		searchUserFrom();
	}

	
	/**
	 * 保存
	 */
	function saveUser() {
		var rows = $('#userList').datagrid('getChecked');
		if(rows.length > 0){
			var userStr = '';
			for(var i = 0; i < rows.length; i++){
				if(userStr != ''){
					userStr += ','+rows[i].account+"_"+rows[i].name;
				}else{
					userStr += rows[i].account+"_"+rows[i].name;
				}
			}
			$.ajax({
				url:"<%=basePath%>mosys/adviceManage/saveAdviceManage.action",
				async:false,
				cache:false,
				data:{'userStr':userStr},
				type:"POST",
				success:function(data){
					$.messager.alert("提示",data.resMsg);
					setTimeout(function(){
						$(".messager-body").window('close');
					},2500);
					if(data.resCode==0){
						$("#addWindow").window('close');
						$('#list').datagrid('reload');
					}
					
				}
			});
		}else{
			$.messager.alert('提示','请勾选要添加的用户信息！');	
			setTimeout(function(){
				$(".messager-body").window('close');
			},2500);
		}
	}
	
	function functionSex(value,row,index){
		if(value){
			return sexMap.get(value);
		}else{
			return "";
		}
	}
	
	function functionEmpType(value,row,index){
		if(value){
			return empType.get(value);
		}else{
			return "";
		}
	}
	</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	
<style  type="text/css">
  .window .panel-header .panel-tool .panel-tool-close{
  	  	background-color: red;
  	}
 </style>
</body>