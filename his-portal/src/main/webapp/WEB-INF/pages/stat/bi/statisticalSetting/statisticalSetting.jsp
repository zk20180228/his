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
<title>统计图表设计</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px; padding: 0px">
	<div id="divLayout" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north'" style="height: 30px;padding-top: 1px;" >
			&nbsp;查询条件：<input class="easyui-textbox" id="parameterNameSerc"
			    name="parameter.parameterName" data-options="prompt:'统计名称'" />
			<shiro:hasPermission name="${menuAlias}:function:query">	
			    <a href="javascript:void(0)" onclick="searchFrom()"
				    class="easyui-linkbutton" iconCls="icon-search">查询</a>
			</shiro:hasPermission>
		</div>
		<div data-options="region:'center'">
			<table id="list" style="width:100%;" class="easyui-datagrid"
				data-options="url:'${pageContext.request.contextPath}/statistics/bi/statisticalSetting/queryStatSetList.action',method:'post',
				rownumbers:true,striped:true,border:false,checkOnSelect:true,
				selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
				<thead>
					 <tr>
					 <th data-options="field:'ck',checkbox:true"></th>
						<th data-options="field:'setStatname'" width="10%">
							统计名称
						</th>
						<th data-options="field:'setPicType',formatter: function(value,row,index){
						if (row.setPicType==1){return '柱状图';}
						else if (row.setPicType==2){return '折线图';} 
 						else if (row.setPicType==3){return '散点图';}
 						else if (row.setPicType==4){return 'K线图';} 
 						else if (row.setPicType==5){return '饼图';}
 						else if (row.setPicType==6){return '雷达图';} 
 						else if (row.setPicType==7){return '和弦图';}
 						else if (row.setPicType==8){return '地图';} 
 						else if (row.setPicType==9){return '仪表盘';}
 						else if (row.setPicType==10){return '沙漏图';} 
 						else if (row.setPicType==11){return '力导布局图';} 
						else {return '孤岛';}}" width="8%">
							图形类型
						</th>
						<th data-options="field:'setGroupname'" width="8%">
							分组名称
						</th>
						<th data-options="field:'setType',formatter: function(value,row,index){
						if (row.setPicType==1){return '统计图';}  
						else if (row.setPicType==2){return '统计表';}}" width="8%">
							类型
						</th>
						<th data-options="field:'setTvname'" width="10%">
							表和视图名称
						</th>
						<th data-options="field:'setKeyField'" width="10%">
							标识字段
						</th>
						<th data-options="field:'setXDescription'" width="10%">
							横轴字段描述
						</th>
						<th data-options="field:'setYDescription'" width="10%">
							纵轴字段描述
						</th>
						<th data-options="field:'setOrderChinese'" width="10%">
							排序字段描述
						</th>
					</tr>
				</thead>
			</table>
		</div>
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
	<div id="menuWin"></div>
	<div id="menuWin3"></div>
	<script type="text/javascript">
	//加载页面
	$(function(){
		//添加datagrid事件及分页
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			onDblClickRow:function(index, row){
				Adddilog3("列表显示",'<%=basePath%>statistics/bi/statisticalSetting/showlist.action','43%','100%');
			}
		});
		$('#parameterNameSerc').textbox('textbox').bind('keyup', function(event) {
			searchFrom();
		});
	})
	//加载模式窗口
	function Adddilog3(title, url, width, height) {
		$('#menuWin3').dialog({    
		    title: title,    
		    width: width,    
		    height: height,    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		});
	}
	function searchFrom(){
		var parameterNameSerc = $('#parameterNameSerc').textbox('getText');
		$('#list').datagrid('load',{
			'statSet.setStatname':parameterNameSerc
		});
	}
	function add(){
		   Adddilog("统计图表设置-添加",'<%=basePath%>statistics/bi/statisticalSetting/statisticalSettingadd.action?flag=1','43%','100%');
		   $('#list').datagrid('reload');
	}
	//加载模式窗口
	function Adddilog(title, url, width, height) {
		$('#menuWin').dialog({    
		    title: title,    
		    width: width,    
		    height: height,    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		});
	}
	//删除
	function del(){
		var rows = $('#list').datagrid('getChecked');
		if (rows.length > 0) {
			var ids = '';
			for ( var i = 0; i < rows.length; i++) {
				if (rows[i].id == null) {//如果id为null 则为新添加行
					var dd = $('#list').edatagrid('getRowIndex', rows[i]);//获得行索引
					$('#list').edatagrid('deleteRow', dd);//通过索引删除该行
				} else {
					if(ids != null && ids != ""){
						ids += ",";
					}
					ids += rows[i].id;
				}
			}
			if (ids != null && ids != "") {
				$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
					if (res) {
						$.messager.progress({text:'删除中，请稍后...',modal:true});
						$.ajax({
							url : '<%=basePath%>statistics/bi/statisticalSetting/delStatSet.action',
							data:{idss:ids},
							type : 'post',
							success : function() {
								$.messager.progress('close');
								$.messager.alert("操作提示", "删除成功！", "success");
								$('#list').datagrid('load',{
								});
							}
						});
					}
				});
			}
		} else {
			$.messager.alert("操作提示", "请选择一条记录！", "error");
		}
	}
	function edit(){
		var rows = $('#list').datagrid('getChecked');
		if (rows.length == 1) {
			Adddilog("统计图表设置-修改",'<%=basePath%>statistics/bi/statisticalSetting/statisticalSettingadd.action?flag=1&idss='+rows[0].id,'43%','100%');
		} else if(rows.length > 1){
			$.messager.alert("操作提示", "请选择一条记录！", "error");
		}else {
			$.messager.alert("操作提示", "请选择一条记录！", "error");
		}
	}
	</script>
</body>
</html>