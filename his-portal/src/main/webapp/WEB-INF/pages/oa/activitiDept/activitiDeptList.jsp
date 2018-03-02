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
<title>工作流科室管理</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	var typeMap = new Map();
	$.ajax({
		url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=depttype",
		success: function(data) {
			var deptType = data;
			for(var i=0;i<deptType.length;i++){
				typeMap.put(deptType[i].encode,deptType[i].name);
			}
		}
	});
</script>
<style type="text/css">
	.panel-header{
		border-top:0
	}
</style>
</head>
<body>
		<div class="easyui-layout" data-options="fit:true">
			<div id="p" data-options="region:'west',split:true ,tools:'#toolSMId'"" title="工作流科室管理"
				style="width:300px;overflow: hidden;height: 35px;padding-top: 5px">
				<div id="toolSMId">
				<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
				<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
				</div>
				<div style="width: 100%; height: 100%; overflow-y: auto;">
					<ul id="tDt"></ul>
				</div>
			</div>
			<div data-options="region:'center',border:false" style="width:79%;height: 100%">
				<div id="divLayout" class="easyui-layout" data-options="border:false,fit:true">
					<div style="width:100%;height: 40px;border-top:0" data-options="region:'north'">
						<table style="width: 100%; border:0px;padding:5px">
							<tr>
								<td style="width: 322px;" nowrap="nowrap">
									关键字：
									<input class="easyui-textbox" id="searchName" data-options="prompt:'部门编号,名称'" style="width: 200px;"/>
								</td>
								<td>
									<a href="javascript:void(0)" onclick="searchFrom()"	class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
								<input id="parentCodes" type="hidden"></input>	
								<input id="deptNames" type="hidden"></input>
							</tr>
						</table>
					</div>
					<div  data-options="region:'center',border:false">
						<table id="list" 
							data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
							<thead>
								<tr>
									<th field="ck" checkbox="true" data-options="width:'5%'"></th>
									<th data-options="field:'deptCode',width:'20%'">
										部门编号
									</th>
									<th data-options="field:'deptName',width:'45%'">
										部门名称
									</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div id="dialogDivId"></div>
		<div id="toolbarId" >
			<shiro:hasPermission name="${menuAlias }:function:add">
				<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:edit">
				<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias }:function:delete">
				<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
				<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
<script type="text/javascript">
	//加载页面
	$(function(){
		//添加datagrid事件及分页
		$('#list').datagrid({
			url : "<c:url value='/oa/activitiDept/queryActivitiDept.action?menuAlias=${menuAlias}'/>",
			pagination:true,
	   		pageSize:20,
	   		pageList:[10,20,30,40,50],
	   		onBeforeLoad:function (param) {
				//GH 2017年2月17日 翻页时清空前页的选中项
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
	        },
			onLoadSuccess: function (data) {//默认选中
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
		bindEnterEvent('searchName',searchFrom,'easyui');
	});
	
	/**
	 * 加载工作流科室树
	 * @author  zpty
	 * @param 
	 * @date 2017-08-21
	 * @version 1.0
	 */		
   	$('#tDt').tree({    
   		url:"<c:url value='/oa/activitiDept/treeActivitiDept.action'/>",
	    method:'get',
	    animate:false,
	    lines:true,
	    formatter:function(node){//统计节点总数
			var s = node.text;
			if(node.children.length>0){
				if (node.children){
					s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
			}
			return s;
		},onLoadSuccess:function(node,data){
			if(data.length>0){
				$('#tDt').tree('collapseAll');
			}
		},
		onSelect: function(node){//点击节点
			$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
		    $('#deptNames').val(node.text);
		    $('#parentCodes').val(node.id);
		    var searchName=$.trim($('#searchName').textbox('getValue'));
			$('#list').datagrid('load', {
				searchName: searchName,
				parentCode: node.id
			});
			$('#divLayout').layout('remove','east');
		},
		onBeforeCollapse:function(node){
			if(node.id=='root'){
				return false;
			}
		}
	}); 
   	function refresh(){//刷新树
   		$('#tDt').tree('options').url = "<%=basePath%>oa/activitiDept/treeActivitiDept.action";
		$('#tDt').tree('reload'); 
	}
   	function expandAll(){//展开树
		$('#tDt').tree('expandAll');
	}
   	function collapseAll(){//关闭树
		$('#tDt').tree('collapseAll');
	}
	/**
	 * 加载科室添加信息
	 * @author  zpty
	 * @date 2017-08-14
	 * @version 1.0
	 */
	function add(){
		var node = $('#tDt').tree('getSelected');
		if(node){
				Adddilog("添加工作流科室",'<%=basePath%>oa/activitiDept/addActivitiDept.action?dId='+node.id);
		}else{
			$.messager.alert('提示','请选择菜单！');	
			close_alert();
		}
	}
	//修改
	function edit(){
		var row = $('#list').datagrid('getSelected');
		  if(row != null){
		   		Adddilog("修改工作流科室",'<%=basePath%>oa/activitiDept/editActivitiDept.action?dId='+row.id);
               $('#list').datagrid('reload');
		  }else{
	   			$.messager.alert('提示','请点中要修改信息！');	
				close_alert();
   		  }
	}
	//加载dialog
	function Adddilog(title, url) {
		$('#dialogDivId').dialog({    
		    title: title,    
		    width: '30%',    
		    height:'40%',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		});    
	}
    function closeDialog() {
		$('#dialogDivId').dialog('close');
	}
	function del() {
		//选中要删除的行
		var rows = $('#list').datagrid('getChecked');
		if (rows.length > 0) {//选中几行的话触发事件	                        
			$.messager.confirm(
				'确认',
				'确定要删除选中信息吗?',
				function(res) {//提示是否删除
					if (res) {
						var ids = '';
						for ( var i = 0; i < rows.length; i++) {
							if (ids != '') {
								ids += ',';
							}
							ids += rows[i].id;
						};
						$.ajax({
							url : "<c:url value='/oa/activitiDept/delActivitiDept.action'/>?dId="+ ids,
							type : 'post',
							success : function(data) {
									$.messager.alert('提示','删除成功！');
									$('#list').datagrid('reload');
									refresh();
					        },error: function(data){					        	
									$.messager.alert('提示','删除失败！');	
							}
						});
					}
				});
		} else {
			$.messager.alert('提示','请选择要删除的记录!');
			close_alert();
		}
	}

	function reload() {
		//实现刷新栏目中的数据
		$("#list").datagrid("reload");
	}

	//查询
	function searchFrom() {
		var searchName = $.trim($('#searchName').val());
		var parentCodes = $('#parentCodes').val()
		$('#list').datagrid('load', {
			searchName : searchName,
			parentCode: parentCodes
		});
	}
	// 列表查询重置
	function searchReload() {
		$('#searchName').textbox('setValue','');
		searchFrom();
	}
</script>
	</body>
</html>