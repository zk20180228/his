<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-layout" data-options="fit:true" id="treeLayOut">
		<div data-options="region:'north',border:false" style="height:50px;padding-top: 10px;padding-left: 5px;">
			<form id="search" method="post">
				关键字：<input class="easyui-textbox" id="queryName" name="queryName" onkeydown="KeyDown(0)"/>
				&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
			</form>
		</div>   
		<div data-options="region:'center',border:false"  id="content" style="">
			<table id="list" data-options="url:'<%=basePath%>inpatient/advdrugnatrue/queryHospital.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
				<thead>
					<tr>
	    				<th data-options="field:'name'" style="width:25%">医院名称</th>
	    				<th data-options="field:'brev'" style="width:25%">医院简称</th>
	    				<th data-options="field:'district'" style="width:25%">所在省市县</th>
	    				<th data-options="field:'description'" style="width:24%">说明</th>
				</thead>
			</table>
		</div>
	</div>
</body>
	<script type="text/javascript">
	var textId= "${textId}";
		//加载页面
		$(function(){
			//关键字查询文本框回车事件
			bindEnterEvent('queryName',searchFrom,'easyui');
			//添加操作按钮
			$('#list').datagrid({
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				toolbar: [{
	                  id: 'btnReload',
	                  text: '刷新',
	                  iconCls: 'icon-reload',
	                  handler: function () {
	                    //实现刷新
	                    $("#list").datagrid("reload");
	                  }
	             }],
	             onDblClickRow: function (rowIndex, rowData) {//双击查看
					 if(getIdUtil("#list").length!=0){
			    		var tmpId ="#"+textId;
			    		window.opener.$(tmpId).combobox('select',rowData.code);
				    	window.close();
						
					} 
	            	 
				}    
			});	
		});
	
		 /**
		* @Description 根据文本框内容模糊查询
		* @author   
		* @Modifier tangfeishuai
		* @ModifyDate 2016-04-13
		* @ModifyRmk 
		* @CreateDate 2015-05-23
		* @version   1.0 
		*/
		function searchFrom(){ 
	   		var queryName = $('#queryName').textbox("getValue");
	   		$('#list').datagrid({
				url:'<%=basePath%>inpatient/advdrugnatrue/queryLikeHospital.action?queryNameParam='+encodeURI(encodeURI(queryName)),
				method:'post',
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				toolbar: ['-', {
	                  id: 'btnReload',
	                  text: '刷新',
	                  iconCls: 'icon-reload',
	                  handler: function () {
	                    //实现刷新
	                    $("#list").datagrid("reload");
	                  }
	             }] 
			});	
		}
		 	
	</script>
</html>
