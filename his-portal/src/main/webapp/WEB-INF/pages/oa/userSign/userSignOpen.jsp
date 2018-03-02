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
	<body>
	<div id="cc" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north'" style="height:50px;padding-top: 5px;">
			<input id="names"  data-options="prompt:'名称，拼音，笔码，自定义码'" />
			<input id="codes" type="hidden" />
			<shiro:hasPermission name="${menuAlias}:function:query"> 
  					<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
  			</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="confirm()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>
		</div>
   		<div data-options="region:'center',split:false" style="width:100%;height:100%;">
			<table id="dataList" class="easyui-datagrid" data-options="fit:true,method:'post',rownumbers:true,striped:true,border:true,fitColumns:false">
				<thead>
					<tr>
						<th data-options="field:'jobNo'" width="20%">编号</th>
						<th data-options="field:'name'" width="20%">名称</th>
					</tr>
				</thead>
			</table>
		</div>
   </div>
	<script type="text/javascript">
	var type='${type}';
	var signType='${signType}';
	//初始化页面
	if(signType==1){
		$('#dataList').datagrid({
			singleSelect:true,
			pagination:true,
			pageSize:10,
			checkOnSelect:true,
			selectOnCheck:true,
			pageList:[10,20,50,80,100],
			url:'<%=basePath %>oa/userSign/getEmpInfo.action',
			queryParams:{type:type,q:null}
			});
		
	}else{
		$('#dataList').datagrid({
			singleSelect:false,
			pagination:true,
			pageSize:10,
			checkOnSelect:true,
			selectOnCheck:true,
			pageList:[10,20,50,80,100],
			url:'<%=basePath %>oa/userSign/getEmpInfo.action',
			queryParams:{type:type,q:null}
			});
		
	}
	$('#names').textbox({});
	bindEnterEvent('names',searchFrom,'easyui');
	function searchValue(){
		
	}
	function searchFrom(){
		var q=$('#names').textbox('getText');
		var textVal=q.toLowerCase();
		re=/select|update|delete|truncate|join|union|exec|insert|drop|count|'|"|;|>|<|%/i;
		if(re.test(textVal))
		{
			$.messager.alert("提示","请勿输入非法字符","error");
			return false;
		}
		$('#dataList').datagrid('load',{type:type,q:q});
	}
	function confirm(){
		var rows = $('#dataList').datagrid('getSelections');
		if(rows!=null){
			var names = "";
			var codes = "";
			for (var i = 0; i < rows.length; i++) {
				if(names==""){
					names = rows[i].name;
				}else{
					names += ","+rows[i].name;
				}
				if(codes==""){
					codes = rows[i].jobNo;
				}else{
					codes += ","+rows[i].jobNo;
				}
			}
			$('#names').textbox('setValue',names);
			$('#codes').val(codes);
			queryValue(names,codes);
			$('#menuWin').dialog('close');
		}
	}
	</script>
	</body>
</html>