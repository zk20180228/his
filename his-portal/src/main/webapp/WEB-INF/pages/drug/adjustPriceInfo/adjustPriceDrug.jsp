<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<div id="divLayout" class="easyui-layout" fit=true>
			<div  data-options="region:'north',title:'信息查询',iconCls:'icon-search'">
				<table cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td>
							<input type="text"  name="name" id="name"  onkeydown="KeyDown()"/>
						</td>
						<td>
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</td>
					</tr>
				</table>
			</div>
			<div class="easyui-panel" data-options="region:'center'">
				<table id="drugList" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'drugName'" style="width:13%">名称</th>
							<th data-options="field:'drugNamepinyin'" style="width:13%">名称拼音码</th>
							<th data-options="field:'drugNamewb'" style="width:12%">名称五笔码</th>
							<th data-options="field:'drugNameinputcode'" style="width:10%">名称自定义码</th>
							<th data-options="field:'drugCommonname'" style="width:10%">通用名称</th>
							<th data-options="field:'drugCnamepinyin'" style="width:10%">通用名称拼音码</th>
							<th data-options="field:'drugCnamewb'" style="width:10%">通用名称五笔码</th>
							<th data-options="field:'drugCnameinputcode'" style="width:10%">通用名称自定义码</th>
							<th data-options="field:'drugBiddingcode'" style="width:10%">招标识别码</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<div id="add"></div>
		<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		</shiro:hasPermission>
		</div>
		<script type="text/javascript">
			//加载页面
			$(function() {
				$("#drugList").datagrid({
					url:"<c:url value='/drug/info/queryDrugInfo.action?menuAlias=${menuAlias}'/>",
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100]
				});
			});
			
			function add(){
				submit();
			}
			//查询
			function searchFrom(){
			    var name =	$('#name').val();
			    $('#list').datagrid('load', {
					name: name
				});
			}
			function KeyDown() {  
		   		if (event.keyCode == 13) {  
			        event.returnValue=false;  
			        event.cancel = true;  
			        searchFrom();  
			    }  
			} 
			//点击确定按钮
			function  submit(){
				 var rows = $('#drugList').datagrid('getSelections');
				 if(rows.length>1){
					 $.messager.alert('提示',"只能选择一条记录");
					 setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
				 }if(rows.length<1){
					 $.messager.alert('提示',"选择一条记录");
					 setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
				 }else if(rows.length=1){
					 $.post("<c:url value='/drug/adjustPriceInfo/getDrugName.action'/>?id="+rows[0].id,function(name){
						//下拉框赋值
						parent.$('#tradeName').combobox('setValue',name );
						parent.$('#drugId').val(rows[0].id);
						parent.win.dialog('close');
					});
				 }
			}
		</script>
	</body>
</html>