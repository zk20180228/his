<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<%@ include file="/common/metas.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>临床路径模板</title>
</head>
<script type="text/javascript">
var typeMap = new Map();
$.ajax({
	url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=systemType",
	success: function(data) {
		var deptType = data;
		for(var i=0;i<deptType.length;i++){
			typeMap.put(deptType[i].encode,deptType[i].name);
		}
	}
});
</script>
<head>
<style type="text/css">
.panel-header{
	border-top:0;
}
</style>
</head>
<body>
		<div class="easyui-layout" data-options="fit:true">
			<div id='p' data-options="region:'west',title:'临床路径模板',split:true,border:true,tools:'#toolSMId'" style="width:300px;">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',border:false" style="height: 35px;padding-top: 5px">
						&nbsp;<input type="text" class="easyui-textbox" id="searchTreeInpId" data-options="prompt:'请输入关键字'" style="width: 200px;"/>
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a>
					</div>
					<div id="treeDiv" data-options="region:'center',border:false" style="width: 85%">
						<ul id="tDt">加载中...</ul>
					</div>
					<div id="toolSMId">
						<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
						<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
						<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
					</div>
				</div>
			</div>
			<div data-options="region:'center',border:false" style="width:79%;height: 100%">
				<div id="divLayout" class="easyui-layout" data-options="border:false,fit:true">
					<!-- <div style="width:100%;height: 40px;border-top:0" data-options="region:'north'">
						<form id="search" method="post">
							<table style="width: 100%; border:0px;padding:5px">
								<tr>
									<td style="width: 350px;" nowrap="nowrap">
										关键字：
										<input  class="easyui-textbox" id="name" name="sysDepartment.deptName"  data-options="prompt:'系统编号,名称,拼音,五笔,自定义,简称,英文'" style="width: 250px;"/>
									</td>
									<td>
										<a href="javascript:void(0)" onclick="searchFrom()"
											class="easyui-linkbutton" iconCls="icon-search">查询</a>
										<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
									</td>
								</tr>
							</table>
						</form>
					</div> -->
					<div  data-options="region:'center',border:false">
						<table id="list" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
							<thead>
								<tr>
									<th field="ck" checkbox="true" data-options="width:'5%'"></th>
									<th data-options="field:'itemCode',width:'10%'">
										项目代码
									</th>
									<th data-options="field:'itemName',width:'15%'">
										项目名称
									</th>
									<th data-options="field:'flag',formatter: flagFormatter,width:'5%'">
										医嘱类型
									</th>
									<th data-options="field:'chooseFlag',formatter: chooseFlagFormatter,width:'5%'">
										是否为必选项目
									</th>
									<th
										data-options="field:'unit',width:'10%'">
										单位
									</th>
									<th
										data-options="field:'num',width:'10%'">
										数量
									</th>
									<th
										data-options="field:'frequencyCode',width:'10%'">
										频次代码
									</th>
									<th
										data-options="field:'directionCode',width:'10%'">
										用法代码
									</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div id="dept" ></div>
		<div id="toolbarId" >
				<a href="javascript:void(0)" onclick="add()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true">添加明细</a>
				<a href="javascript:void(0)" onclick="edit()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-edit',plain:true">修改明细</a>
				<a href="javascript:void(0)" onclick="del()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true">删除明细</a>
			<a href="javascript:void(0)" onclick="reload()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		<!-- tree菜单 -->
		<div id="tDtmm" class="easyui-menu" style="width:100px;">
			<div onclick="addDept()" data-options="iconCls:'icon-add'">添加模板</div>
			<div onclick="editDept()" data-options="iconCls:'icon-edit'">修改模板</div>
<%-- 		<shiro:hasPermission name="${menuAlias }:function:tdelete"> --%>
<!-- 			<div onclick="delDetp()" data-options="iconCls:'icon-remove'">移除模板</div> -->
<%-- 		</shiro:hasPermission> --%>
		<div onclick="viewDetp()" data-options="iconCls:'icon-search'">查看模板</div>
	</div>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
<script type="text/javascript">
	function searchTreeNodes(){
	    var searchText = $('#searchTreeInpId').textbox('getValue');
	    $("#tDt").tree("search", searchText);
	}
	var propertyList = null;
	//全局变量
	var flagMap=new Map();
	$(function(){
		/**医嘱类型渲染**/
		$.ajax({
			url: "<%=basePath%>inpatient/clinicalPathwayModelAction/flagCombobox.action",
			type:'post',
			success: function(data){
				var j = JSON.parse(data);
				for(var i=0;i<j.length;i++){
					flagMap.put(j[i].typeCode,j[i].typeName);
					
				}
			}
		});
	    bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
	});
	
	
	//上移
	function toUp(rowId) {
		editOrder(rowId, 1);
	}
	//下移
	function toDown(rowId) {
		editOrder(rowId, 2);
	}
	//传向后台  移动
	function editOrder(currentId, flag) {
		$.post("<c:url value='/baseinfo/fictitiousDept/editOrderContent.action'/>", {
			"currentId" : currentId,
			"flag" : flag
		}, function(result) {
			if (result > 0) {
				$("#list").datagrid("reload");
			} else {
				$.messager.alert('提示','error');
			}
		});
	}
	//list列表操作
	var id = "${id}"; //存储数据ID
	function add() {
		var node = $('#tDt').tree('getSelected');
		if (node) {
// 			console.log(node);
			var id = node.attributes.id;
			if(id!=null&&""!=id){
				var modelId=node.attributes.id;
				var modelClass=node.attributes.modelClass;
				AddOrShowEast('添加', "<c:url value='/inpatient/clinicalPathwayModelAction/addPathwayDetail.action'/>?modelId=" + modelId+"&modelClass="+modelClass);
// 				AddOrShowEast('添加', "<c:url value='/baseinfo/fictitiousDept/addFictitiousDept.action'/>?deptType=" + getSelected()+"&fictCode="+code+"&fictName="+encodeURIComponent(encodeURIComponent(fitdeptName)));
			}else{
				$.messager.alert('提示','请选择模板');
				close_alert();
			}
		}else{
			$.messager.alert('提示','请选择模板');
			close_alert();
		}
	}
	function edit() {
		var node = $('#list').datagrid('getSelected');
		if (node) {
			var id = node.id;
			if(id){
				AddOrShowEast('编辑', "<c:url value='/inpatient/clinicalPathwayModelAction/addPathwayDetail.action'/>?id="+id);
			}else{
				$.messager.alert('提示','请选择修改模板');
				close_alert();
			}
		}else{
			$.messager.alert('提示','请选择修改模板');
			close_alert();
		}
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
							url : "<c:url value='/inpatient/clinicalPathwayModelAction/delPathwayDetail.action'/>?id="+ ids,
							type : 'post',
							success : function() {
								$.messager.alert('提示','删除成功');
								$('#list').datagrid('reload');
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
		var node = $('#tDt').tree('getSelected');
		if (node) {
			var id = node.attributes.fictitiousId;
			if(id){
				var name = $.trim($('#name').val());
				id=node.attributes.deptCode;
				$('#list').datagrid('load', {
					deptCode : id,
					q : name
				});
			}else{
				$.messager.alert('提示信息','请选择模板');
				close_alert();
			}
		}
		
	}

	//获得选中id	
	function getId(tableID, str) {
		var row = $(tableID).datagrid("getChecked");
		var dgID = "";
		if (row.length < 1) {
			$.messager.alert("操作提示", "请选择一条记录！", "warning");
			close_alert();
		}
		var i = 0;
		for (i; i < row.length; i++) {
			if (str = 0) {
				dgID += "\'" + row[i].BED_ID + "\'";
			} else {
				dgID += row[i].BED_ID;
			}
			if (i < row.length - 1) {
				dhID += ',';
			} else {
				break;
			}
		}
		return dgID;
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
		$('#divLayout').layout('remove','east');
			//重新装载右侧面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : 560,
				split : true,
				href : url,
				closable : true
			});
	}

	//加载部门树
	$('#tDt').tree({
		url : "<c:url value='/inpatient/clinicalPathwayModelAction/treeClinicalPath.action'/>",
		method : 'get',
		animate : true,
		lines : true,
		onContextMenu: function(e,node){//添加右键菜单
			e.preventDefault();
			$(this).tree('select',node.target);
			$('#tDtmm').menu('show',{
				left: e.pageX,
				top: e.pageY
			});
		},
		formatter : function(node) {//统计节点总数
					   var s = node.text;
							if(node.children.length>0){
								if (node.children) {
									s += '&nbsp;<span style=\'color:blue\'>('+ node.children.length + ')</span>';
								}
							}
							return s;
						},
						//设置默认选中根节点
		onLoadSuccess : function(node, data) {
			if(data.length>0){//节点收缩
				$('#tDt').tree('collapseAll');
			}
			$('#tDt').tree('select',$('#tDt').tree('find', 1).target);
			$.ajax({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
				data : {"type" : "systemType"},
				type : 'post',
				success : function(propertydata) {
						  propertyList = propertydata;
						}
					});
							//icon-user_brown
							//添加datagrid事件及分页
							$('#list').datagrid({
												url : "<c:url value='/inpatient/clinicalPathwayModelAction/queryClinicalPathModelDetail.action'/>",
// 												url : "",
												pagination : true,
												pageSize : 20,
												pageList : [ 20, 30, 50,80,100 ],
												onLoadSuccess : function(data) {
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
													var grid = $('#list');
													var options = grid.datagrid('getPager').data("pagination").options;
													var curr = options.pageNumber;
													var total = options.total;
													var pageSize = options.pageSize;
													var rows = data.rows;
													map = null;
													map = new Map();
													var rowData = data.rows;
													$.each(rowData,function(index,value) {
														if (value.id == id) {
															$('#list').datagrid('checkRow',index);
														}
													});
													for ( var i = 0; i < rows.length; i++) {
														var index = $('#list').datagrid('getRowIndex',rows[i]);
														var a = "";
														if (rows.length == 1) {//仅有一个父级不现实按钮
														} else if (curr == 1&& index == 0) {//第一行
															a = '<a class="downCls" onclick="toDown(\''+rows[i].fictitiousContentId+'\')" href="javascript:void(0)" style="height:20"></a>';
														} else if ((index + 1) + ((curr - 1) * pageSize) == total) {//最后一行
															a = '<a class="upCls" onclick="toUp(\''+rows[i].fictitiousContentId+'\')" href="javascript:void(0)" style="height:20"></a>';
														} else {
															a = '<a class="upCls" onclick="toUp(\''+rows[i].fictitiousContentId+'\')" href="javascript:void(0)" style="height:20"></a>';
															a += '<a class="downCls" onclick="toDown(\''+rows[i].fictitiousContentId+'\')" href="javascript:void(0)" style="height:20"></a>';
														}
														$('#list').datagrid('updateRow',{index : index,row : {deptOrder : a}});
													}
													$('.upCls').linkbutton({text:'上移',plain:true,iconCls:'icon-up'}); 
													$('.downCls').linkbutton({text:'下移',plain:true,iconCls:'icon-down'});
													
												},
												onDblClickRow : function(rowIndex, rowData) {//双击查看
													if (getIdUtil("#list").length != 0) {
														AddOrShowEast('EditForm',"<c:url value='/inpatient/clinicalPathwayModelAction/showPathwayDetail.action'/>?id="+ getIdUtil("#list"));
													}
												},
												onBeforeLoad:function(){
													//GH 2017年2月17日 翻页时清空前页的选中项
													$('#list').datagrid('clearChecked');
													$('#list').datagrid('clearSelections');
												}
											});
// 							bindEnterEvent('name',searchFrom,'easyui');//回车键查询
						},
						onClick : function(node) {//点击节点
							$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
							var node = $('#tDt').tree('getSelected');
							if (node) {
								var id = node.attributes.id;
								if(id){
									$('#list').datagrid('load', {
										modelId : id
									});
									closeEast();
								}
							}
						},
						onBeforeCollapse:function(node){
							if(node.id=="1"){
								return false;
							}
					    }
					});
	//模板树操作
	function refresh() {//刷新树
		$('#tDt').tree('options').url = "<c:url value='/inpatient/clinicalPathwayModelAction/treeClinicalPath.action'/>",
		$('#tDt').tree('reload');
	}
	function expandAll() {//展开树
		$('#tDt').tree('expandAll');
	}
	function collapseAll() {//关闭树
		$('#tDt').tree('collapseAll');
	}
	//页面操作
	function closeEast(){
		$('#divLayout').layout('remove','east');
	}
	//菜单操作
	function addDept() {//添加模板
		var type = getSelected();
		if(type == ""||typeof(type)=="undefined"){
			$.messager.alert('提示','请选中临床路径');
			close_alert();
		}else{
			Adddilog("添加模板", "<c:url value='/inpatient/clinicalPathwayModelAction/addPathwayModel.action'/>?type="+ type);
		}
	}
	function editDept() {//修改模板
		var id=getSelectedFictitiousId();
		if(id){
			Adddilog("编辑模板", "<c:url value='/inpatient/clinicalPathwayModelAction/addPathwayModel.action'/>?id="+id);
		}else{
			$.messager.alert('提示','请选中模板');
			close_alert();
		}
	}
	function viewDetp() {//查看模板
		var id=getSelectedFictitiousId();
		if(id){
			Adddilog("查看模板信息", "<c:url value='/inpatient/clinicalPathwayModelAction/showPathwayModel.action'/>?id="+id);
		}else{
			$.messager.alert('提示','请选中模板');
			close_alert();
		}
	}
	//加载dialog
	function Adddilog(title, url) {
		$('#dept').dialog({    
		    title: title,    
		    width: '800',    
		    height:'320',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
	}
	//打开dialog
	function openDialog() {
		$('#dept').dialog('open');
	}
	//关闭dialog
	function closeDialog() {
		$('#dept').dialog('close');
	}

	function getSelected() {//获得选中节点
		var node = $('#tDt').tree('getSelected');
		if (node) {
			var type = node.id;
			if(type==1){
				type="C";
			}else{
				var l= type.indexOf("type_");
				if(l!=-1){
					type=node.id.substring(l+5);
				}else{
					l=node.attributes.pid.split("_")
					type=l[1]
				}
				return type;
			}
			
		} else {
			return "";
		}
	}
	function getSelectedFictitiousId() {
		var node = $('#tDt').tree('getSelected');
		if (node) {
// 			console.log(node);
			var id = node.attributes.id;
			if(id){
				return id;
			}
			return false;
		} else {
			return false;
		}
	}
	
	//适用部门分类
	function chooseFlagFormatter(value,row,index){
		if(value==1){
			return "是";
		}else{
			return "否";
		}
	}	
	/**医嘱类型渲染**/
	function flagFormatter(value,row,index){
		return flagMap.get(value);
	}	
	//格式化部门性质
	function propertyFormatter(value, row, index) {
		if (value != null) {
			for ( var i = 0; i < propertyList.length; i++) {
				if (value == propertyList[i].encode) {
					return propertyList[i].name;
				}
			}
		}
	}
	// 列表查询重置
	function searchReload() {
		$('#name').textbox('setValue','');
		searchFrom();
	}
</script>
	</body>
</html>