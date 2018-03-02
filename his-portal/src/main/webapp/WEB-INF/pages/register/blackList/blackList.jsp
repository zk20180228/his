<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>挂号员黑名单</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
var employeeType=null;
		//加载页面
		$(function(){
		    var winH=$("body").height();
			//$('#p').height(winH-78-30-27-2);//78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
			$('#list').height(winH-78-30-27-26);
			var id="${id}"; //存储数据ID
			//添加datagrid事件及分页
			$('#list').datagrid({
				pagination:true,
				fitColumns:true,
		   		pageSize:20,
		   		pageList:[20,30,50,100],
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
		   		},
		   		onBeforeLoad:function(param){
					$(this).datagrid('uncheckAll');
				},
				onDblClickRow: function (rowIndex, rowData) {//双击查看
						if(getIdUtil("#list").length!=0){
					   	    AddOrShowEast('黑名单查看',"<c:url value='/outpatient/blacklist/viewBlack.action'/>?id="+getIdUtil("#list"));
					   	}
					}
				});
			$.ajax({//员工类型 
			      url :"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action" ,  
				    type : "post",  
					data:{"type":"empType"},
					success: function(list) { 
						employeeType=list;
				    }
		   	 });	
			
				bindEnterEvent('queryName',searchFrom,'easyui');
		});
		//添加
   		function add(){
			   AddOrShowEast('添加',"<c:url value='/outpatient/blacklist/addBlack.action'/>");
		}
		//修改	
		function edit(){
			  if(getIdUtil("#list")!=null){
                  AddOrShowEast('编辑',"<c:url value='/outpatient/blacklist/editBlack.action'/>?id="+getIdUtil("#list"));
	   		  }
		}
			
		function del(){
		  //选中要删除的行
          var iid = $('#list').datagrid('getChecked');
          if (iid.length > 0) {//选中几行的话触发事件	                        
		 	 $.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
				if (res){
					var ids = '';
					for(var i=0; i<iid.length; i++){
						if(ids!=''){
							ids += ',';
						}
						ids += iid[i].id;
					};
					$.ajax({
						url: "<c:url value='/outpatient/blacklist/delBlack.action'/>?id="+ids,
						type:'post',
						success: function() {
							$.messager.alert('提示','删除成功');
						$('#list').datagrid('reload');
						}
					});										
				  }
              });
           }
		}
		function reload(){
			//实现刷新栏目中的数据
			 $("#list").datagrid("reload");
		}
		//渲染员工类型
		function formatEmpType(value, row, index){
			if (value != null) {
				for ( var i = 0; i < typeList.length; i++) {
					if (value == typeList[i].encode) {//id改为encode
						return typeList[i].name; //value改为name 
					}
				}
			}
		}
	//获取easyui-datagrid中的id进行批量删除  
	function delInfo() {
		$.post("<c:url value ='/outpatient/blacklist/delBlack.action'/>?id=" + ids("#list"));
		del('#list');
		$('#divLayout').layout('remove', 'east');
		//实现刷新
		$("#list").datagrid("reload");

	}
	//模糊查询
	function searchFrom() {
		var queryName=$('#queryName').val();
		$('#list').datagrid('load', {
			name:queryName
		});
	}
	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function clears(){
		$('#queryName').textbox('setValue','');
		searchFrom();
	}
	/**
	 * 动态添加标签页
	 * @author  sunshuo
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-05-21
	 * @version 1.0
	 */
	function AddOrShowEast(title, url) {
		var eastpanel = $('#panelEast'); //获取右侧收缩面板
		if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
			//重新装载右侧面板
			$('#divLayout').layout('panel', 'east').panel({
				title : title,
				href : url
			});
		} else {//打开新面板
			$('#divLayout').layout('add', {
				title : title,
				region : 'east',
				width : 580,
				split : true,
				href : url
			});
		}
	}
</script>
<style type="text/css">
	.panel-header{
		border-left:0
	}
	#panelEast{
		border:0
	}
</style>
</head>
<body style="margin: 0px; padding: 0px">
   <div id="divLayout" class="easyui-layout" fit=true style="width:100%;height:100%;overflow-y: auto;">
   		<div data-options="region:'north'" style="border: 1px solid #FFFFFF;height:70px;width:100%;padding: 5px 0px 0px 0px;padding-top: 13px;">
   			<table id="search"  style="width: 100%; border: 1px solid #95b8e7; padding: 5px;" class="changeskin">
				<tr>
					<td style="width: 400px;">查询条件：<input  class="easyui-textbox" id="queryName" name="queryName" data-options="prompt:'员工姓名,曾用名,拼音,五笔,自定义'" style="width: 250px;"/>
						<shiro:hasPermission name="${menuAlias }:function:query">
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</shiro:hasPermission>
					</td>
				</tr>
			</table>
   		</div>
   		<div data-options="region:'center'" style="height:90%;width:100%;border-right:0">
   			<input type="hidden" value="${id }" id="id" ></input>
			<table id="list" style="width: 100%" data-options="url:'${pageContext.request.contextPath}/outpatient/blacklist/queryBlack.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,border:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
				<thead>
					<tr>
						<th field="getIdUtil" checkbox="true" ></th>
						<th data-options="field:'name',formatter: 
										function(value,row,index){
											if (row.dmployeeId){
												return row.dmployeeId.name;
											} else {
												return value;
											}
										}" style="width: 20%">挂号员姓名</th>
										
						<th data-options="field:'oldName',formatter: 
										function(value,row,index){
											if (row.dmployeeId){
												return row.dmployeeId.oldName;
											} else {
												return value;
											}
										}"style="width: 20%">挂号员曾用名</th>	
						<th data-options="field:'resaon'" style="width: 20%">原因</th>
					</tr>
				</thead>
			</table>
   		</div>
	</div>
	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias }:function:add">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias }:function:edit">
			<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias }:function:delete">	
			<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
</body>
</html>