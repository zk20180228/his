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
<title>默认取药药房维护</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	var addAndEdit;	//0为添加，1为修改
	var drType = '';
	/* 因为历史数据中药品类别多了“全部”类别对应值为A 所以后台手动添加了这个分类 */
	$.ajax({//渲染药品类别
		url: '<%=basePath %>publics/putGetDrug/querydrugtype.action',
		success:function(data){
			drType = data;
		}
	})
	//加载页面
	$(function() {
		comboGrid();
		Tree();
		searchFrom();
	});
	
	//查询下拉框
	function comboGrid(){
		$('#codes').combogrid({ 
	  		url : "<%=basePath%>publics/putGetDrugByDispensAction/queryDept.action",
	  		disabled : false,
	  		mode:'remote',
	  		panelWidth:450, 
			rownumbers : true,//显示序号 
			pagination : true,//是否显示分页栏
			fitColumns : true,//自适应列宽
			pageSize : 5,//每页显示的记录条数，默认为10  
			pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
			idField : 'deptName',
			textField : 'deptName',
			columns : [ [ {
				field : 'deptCode',
				title : '科室编码',
				width : 200
			}, {
				field : 'deptName',
				title : '科室名称',
				width : 200,
			}, {
				field : 'deptInputcode',
				title : '自定义码',
				width : 200
			}] ],
			onSelect :function(rowIndex, rowData){
				searchFrom();
			},
			onBeforeLoad:function(param){
				param.q= $.trim(param.q);
			}
			
	});
	}
	//查询树
	function Tree(){
		//加载部门树
	   	$('#tDt').tree({    
		    url:'<%=basePath%>publics/putGetDrug/queryTree.action?flag=1',
		    method:'get',
		    animate:true,
		    lines:true,
			onContextMenu: function(e,node){//添加右键菜单
				e.preventDefault();
				$(this).tree('select',node.target);
				if(node.attributes.pid=='root'){
					$('#editDiv').css("display","none");
					$('#delDiv').css("display","none");
					$('#tDtmm').menu('show',{
						left: e.pageX,
						top: e.pageY
					});
				}else{
					$('#editDiv').css("display","block");
					$('#delDiv').css("display","block");
					$('#tDtmm').menu('show',{
						left: e.pageX,
						top: e.pageY
					});
				}
			},onClick: function(node){//点击节点
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				closeLayout();
				searchFrom(node.id);
			},onLoadSuccess : function(node, data) {
				if(data.resCode=='error'){
					   $("body").setLoading({
							id:"body",
							isImg:false,
							text:data.resMsg
						});
				   }else{
					$('#tDt').tree('select',$('#tDt').tree('find', 1).target);
				   }
			}
		}); 
	}
	
	//list数据初始化
	function ListGrid(id){
		//添加datagrid事件及分页
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url:'<%=basePath %>publics/putGetDrug/queryAll.action?menuAlias=${menuAlias}',
			queryParams: {
				id:id,
				codes:$('#codes').combogrid('getValue')
			},
			selectOnCheck:false,
			rownumbers:true,
			idField: 'id',
			toolbar:'#toolbarId',
			singleSelect:true,
			checkOnSelect:true,  
			onBeforeLoad:function(){//清空上页选中
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onLoadSuccess:function(row, data){
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
				}}
			
		});
	}
	
	
	function add(){
		closeLayout();
		var putDeptName=$("#putDeptName").val();
		 AddOrShowEast('添加','<%=basePath%>publics/putGetDrug/addPutGetDrugURl.action','post'
				   ,{"selected":getSelected(1),"addAndEdit":0,"putDeptName":putDeptName});
	}
	
	function edit(){
		closeLayout();
		var row = $('#list').datagrid('getSelected'); //获取当前选中行          
	    if(row){
	    	 AddOrShowEast('修改','<%=basePath%>publics/putGetDrug/addPutGetDrugURl.action','post'
					   ,{"id":row.id,"addAndEdit":1,"deptName":row.deptName,"putDeptName":row.putDeptName});
	    }else{
			$.messager.alert('提示', '请选中修改的行');
			setTimeout(function(){$(".messager-body").window('close')},1500);
		}
	}
	
	function del(){
		var obj=$('#list').datagrid('getChecked');
		var arr =new Array();
		if(obj.length>0){
			$.each(obj,function(i,n){
				arr[i]=n.id;
				j=i+1;
			});
			
			$.messager.confirm('确认对话框', '您想要删除'+j+'条记录吗？', function(r){
				if (r){
					$.messager.progress({text:'保存中，请稍后...',modal:true});
					$.ajax({
						url:'<%=basePath %>publics/putGetDrug/deletePutGetDrug.action',
						type:'post',
						traditional:true,//数组提交解决方案
						data:{'ids':arr},
						dataType:'json',
						success:function(data){
							$.messager.progress('close');	
	 						$.messager.alert('提示','删除成功！');
	 						listReload();
						},error : function(a,b,c) {
							$.messager.progress('close');	
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','请选中要删除的行！');
			setTimeout(function(){$(".messager-body").window('close')},1500);
		}
	}
	
	function listReload(){
		//实现刷新栏目中的数据
		$('#list').datagrid('reload');   
	}
	
	
	//点击科室树查询
	function searchFrom() {
		var	id=getSelected(1);
		//添加datagrid事件及分页
		$('#list').datagrid({
			pagination:true,
			pageSize:30,
			pageList:[20,30,50,80,100],
			url:'<%=basePath %>publics/putGetDrugByDispensAction/conditionQuery.action?',
			queryParams: {
				"deptName":id,
				"search": $('#codes').combogrid('getValue')
			},
			selectOnCheck:false,
			rownumbers:true,
			idField: 'id',
			toolbar:'#toolbarId',
			singleSelect:true,
			checkOnSelect:true, 
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onLoadSuccess:function(row, data){
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
				}}
		});
	}
	
	/**
	 * tag=0获取nodetype
	 * tag=1获取选中节点ID
	 * tag = 2 父节点ID  
	 * tag=3 判断选中的是否是叶子节点，如果是叶子节点则获取id，否则赋值1
	 * tag = 4 所选节点名称
	 */
	function getSelected(tag) {
		var node = $('#tDt').tree('getSelected');//获取所选节点
		if (node != null) {
			var Pnode = $('#tDt').tree('getParent', node.target);
			if (Pnode) {
				if (tag == 0) {
					var nodeType = node.nodeType;
					return nodeType;
				}
				if (tag == 1) {
					var id = node.id;
					var dept=$("#putDeptName").val(node.text);
					return id;
				}
				if (tag == 2) {
					var pid = Pnode.id;
					return pid;
				}
				if (tag == 3) {
					if ($('#tDt').tree('isLeaf', node.target)) {//判断是否是叶子节点
						var id = node.id;
						return id;
					} else {
						return 1;
					}
				}
				if(tag==4){
					var text = node.text;
					return text;
				}
			}
		} else {
			return 1;
		}
	}
	
	//按回车键提交表单！
	$('#search').find('input').on('keyup', function(event) {
		if (event.keyCode == 13) {
			searchFrom();
		}
	});
	
	/**
	 * 动态添加标签页
	 * @author  sunshuo
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-05-21
	 * @version 1.0
	 */
	function AddOrShowEast(title, url,method,params) {
		if(!method){
			method="get";
		}
		if(!params){
			params={};
		}
		$('#divLayout').layout('add', {
			title:title,
			region : 'east',
			width : 400,
			split : false,
			collapsible : false,			
			href : url,
			method:method,
			queryParams:params,
			closable : false,
			border : true
		});
	}
	
	function expandAll(){//展开树
		$('#tDt').tree('expandAll');
	}
	function collapseAll(){//关闭树
		var root = $('#tDt').tree('getRoot');
		var nodes = root.children;
		for(var i = 0; i < nodes.length; i++){
			$('#tDt').tree('collapse',nodes[i].target);
		}
	}
	//清除查询条件
	function delsele(){
		delSelectedData('codes');
		searchFrom();
	}
	//关闭Layout
	function closeLayout() {
		$('#divLayout').layout('remove', 'east');
	}
	function fmDrugType(value,row,index){
		for(var i = 0; i < drType.length; i++){
			if(value == drType[i].encode){
				return drType[i].name;
			}
		}
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style type="text/css">
.putGetDrugList .panel-body{
	border-left:0
}
.panel-header{
	border-top-width:0
}
.layout-panel-east .panel-header{
	border-top-width:1px;
}
</style>
</head>
<body style="margin: 0px; padding: 0px"> 
	<div class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow-y: auto;">
		<div id="p" data-options="region:'west',tools:'#toolSMId',split:true" title="药房药库信息" style="width: 15%; height:100%;padding: 0px; overflow: hidden;">
			<div id="toolSMId">
				<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
			</div>
			<div id="treeDiv" style="width: 100%; height: 100%; overflow-y: auto;">
				<ul id="tDt">数据加载中...</ul>
			</div>
		</div>
		<div data-options="region:'center',split:false,title:'取药药房维护信息',iconCls:'icon-book'" style="min-height: 60px; height:100%;">
			<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow: hidden;">
			 	<div data-options="region:'north',border:false" style="height:40px;">
			 		<table style="width:100%;border:none; ">
						<tr>
							<td style="padding: 4px 0px 4px 0px;" >取药科室：
							<input id="codes" style="width: 150px;">
								<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="delsele()" data-options="iconCls:'icon-opera_clear',plain:true"></a>
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
			 	</div>   
				<div data-options="region:'center',border:false" style="height: 90%" class="putGetDrugList">
					<input type="hidden" name="putDeptName" id="putDeptName"/>
					<table id="list" style="width: 100%;border-left:0"
						data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,
						checkOnSelect:true,selectOnCheck:false,singleSelect:true,
						toolbar:'#toolbarId',fit:true">
						<thead>
							<tr>
								<th data-options="field:'id',checkbox:true"
								style="text-align: center">id</th>
								<th data-options="field:'deptName'"
									style="width: 10%; text-align: center">取药科室</th>
								<th data-options="field:'drugType' ,formatter:fmDrugType"
									style="width: 10%; text-align: center">药品类别</th>
								<th data-options="field:'beginTime'"
									style="width: 15%; text-align: center">开始时间</th>
								<th data-options="field:'endTime'"
									style="width: 15%">结束时间</th>
								<th
									data-options="field:'mark'"
									style="width: 40%">备注</th>
							</tr>
						</thead>
					</table>
				</div>   
			</div>
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
		<a href="javascript:void(0)" onclick="listReload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
</body>
</html>