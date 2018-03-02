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
		<div  id="divLayout" class="easyui-layout" style="width: 100%; height: 100%;">
				<div
					data-options="region:'north',split:false,title:'摆药分类列表',iconCls:'icon-book'"
					style="padding: 5px; height: 30%">
					<table id="list"
						data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,toolbar:'#toolbarId'">
					</table>
				</div>
				<div data-options="region:'east',title:'药品用法',split:true,collapsible:false" style="width:20%;height: 60%;">
					<ul id="tDtyaopinyongfa"></ul>
				</div>   
			    <div data-options="region:'west',title:'医嘱类别',split:true,collapsible:false" style="width:18%;height: 60%;">
			    	<ul id="tDt"></ul>
			    </div>   
			    <div data-options="region:'center'">
			    	<div id="cc" class="easyui-layout" style="width:100%;height:85%;">   
					    <div data-options="region:'east',title:'药品药剂',split:true,collapsible:false" style="width:33%;">
					    	<ul id="tDtyaoji"></ul>
					    </div>   
					    <div data-options="region:'west',title:'药品类别',split:true,collapsible:false" style="width:33%;">
					    	<ul id="tDtyaopin"></ul>
					    </div>   
					    <div data-options="region:'center',title:'药品性质'" style="width:33%;">
					    	<ul id="tDtxingzhi"></ul>
					    </div>   
					</div>
			    </div>
		</div>	
		<script type="text/javascript">
		$(function(){
		$('#list').datagrid({
			checkOnSelect : true,
			selectOnCheck : true,
			singleSelect : false,
			pagination : true,
			fitColumns : true,
			pageSize : 7,
			pageList : [ 7, 10, 30, 100 ],
			url : '<%=basePath%>inpatient/bill/queryBillclass.action?menuAlias=${menuAlias}',
			columns : [ [ {
					field : 'ck',
					checkbox : true
				}, {
					field : 'id',
					title : 'id',
					width : '15%',
					hidden : true
				}, {
					field : 'billclassCode',
					title : '摆药单分类代码',
					type : 'textbox',
					options : {
						required : true
					},
					width : '15%'
				}, {
					field : 'billclassName',
					title : '摆药单分类名称',
					type : 'textbox',
					options : {
						required : true
					},
					width : '15%'
				}, {
					field : 'billclassAttr',
					title : '摆药单属性',
					type : 'combobox',
					options : {
						valueField : 'id',
						textField : 'name',
						method : 'get',
						multiple : false,
						editable : false,
						data : [ {
							id : 'O',
							name : '一般摆药'
						}, {
							id : 'T',
							name : '特殊药品摆药'
						}, {
							id : 'R',
							name : '出院带药摆药'
						} ],
						required : true
					},
					width : '15%',
					formatter : function(value, row, index) {
						switch (row.billclassAttr) {
						case 'O':
							text = '一般摆药';
							break;
						case 'T':
							text = '特殊药品摆药';
							break;
						case 'R':
							text = '特殊药品摆药';
							break;
						default:
							text = '';
							break;
						}
						return text;
					}
				}, {
					field : 'printType',
					title : '打印类型',
					type : 'combobox',
					options : {
						valueField : 'id',
						textField : 'name',
						method : 'get',
						multiple : false,
						editable : false,
						data : [ {
							id : 'T',
							name : '汇总'
						}, {
							id : 'D',
							name : '明细'
						}, {
							id : 'H',
							name : '草药'
						}, {
							id : 'R',
							name : '出院带药'
						} ],
						required : true
					},
					width : '15%',
					formatter : function(value, row, index) {
						switch (row.printType) {
						case 'T':
							text = '汇总';
							break;
						case 'D':
							text = '明细';
							break;
						case 'H':
							text = '草药';
							break;
						case 'R':
							text = '出院带药';
							break;
						default:
							text = '';
							break;
						}
						return text;
					}
				}, {
					field : 'validFlag',
					title : '是否有效',
					type : 'combobox',
					options : {
						valueField : 'id',
						textField : 'name',
						method : 'get',
						multiple : false,
						editable : false,
						data : [ {
							id : 1,
							name : '有效'
						}, {
							id : 0,
							name : '无效'
						} ],
						required : true
					},
					width : '15%',
					formatter : function(value, row, index) {
						switch (value) {
						case 1:
							text = '有效';
							break;
						case 0:
							text = '无效';
							break;
						default:
							text = '';
							break;
						}
						return text;
					}
				}, {
					field : 'mark',
					title : '备注',
					type : 'textbox',
					options : {
						required : true
					},
					width : '15%'
				} ] ],
				onClickRow : function(rowIndex, rowData) {
					$('#billId').val(rowData.id);
					$('#infoList').datagrid('load', {
						pid : rowData.id
					});
				}
		});
		//加载医嘱类别树
	   	$('#tDt').tree({ 
		    url : '<%=basePath%>business/treeInpatientKind.action',
		    method:'get',
		    animate:true,
		    lines:true,
		    checkbox:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children){
					s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
				return s;
		    	}
			});
	  //加载药品类别树
	   	$('#tDtyaopin').tree({ 
		    url : '<%=basePath%>business/treeCodeDrugtype.action',
		    method:'get',
		    animate:true,
		    lines:true,
		    checkbox:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children){
					s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
				return s;
		    	}
			});
	  //加载药品性质树
	   	$('#tDtxingzhi').tree({ 
		    url : '<%=basePath%>business/treeCodeDrugproperties.action',
		    method:'get',
		    animate:true,
		    lines:true,
		    checkbox:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children){
					s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
				return s;
		    	}
			});
	  //加载药品药剂树
	   	$('#tDtyaoji').tree({ 
		    url : '<%=basePath%>business/treeCodeDosageform.action',
		    method:'get',
		    animate:true,
		    lines:true,
		    checkbox:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children){
					s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
				return s;
		    	}
			});
	  //加载药品用法树
	   	$('#tDtyaopinyongfa').tree({ 
		    url : '<%=basePath%>business/treeCodeUseage.action',
		    method:'get',
		    animate:true,
		    lines:true,
		    checkbox:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children){
					s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
				return s;
		    	}
			});
		 });
		</script>	
</body>
</html>