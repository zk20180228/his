<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style type="text/css">
.window .panel-header .panel-tool a{
  	  	background-color: red;	
}
.toolbox td{
	width:310px;
	height:22px;
}
.mouse_color{
	background-color: #CFCFCF;
	cursor:pointer;
} 
</style>
<title>电子病历质检评分</title>
	<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp"%>
	<script type="text/javascript">
	$(function(){
		
		tDtTree();
		tab();
		scoreList();
		
	});
	/**
	*加载tabs
	*/
	function tab(){
		$('#tt').tabs({
			onSelect:function(title,index){
				iframsrc();
			}
		});
	}
	//加载患者树
	function tDtTree(){
		$('#tDt').tree({
			url : '<%=basePath%>emrs/emrDeptScore/inpatientTree.action',
			animate : true,
			lines : true,
			onlyLeafCheck:true,
			formatter : function(node) {//统计节点总数
				var s = node.text;
				if(node.attributes.dateState!=null&&node.attributes.dateState!=''){
					s += node.attributes.dateState;
				}
				if(node.attributes.leaveFlag!=null&&node.attributes.leaveFlag!=''){
					s += node.attributes.leaveFlag;
				} 	
				if (node.children.length > 0) {
					s += '<span style=\'color:blue\'>('
							+ node.children.length + ')</span>';
				}
				return s;
			},onClick : function(node){	
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				if(node.attributes.pid != 'root'){
					iframsrc();
				}
			}
		});
	}
	/**
	*加载评分列表
	*/
	function scoreList(){
		$('#scoreList').edatagrid({
			url:'<%=basePath%>emrs/emrDeptScore/standardScoreList.action',
			onBeforeLoad:function(param){
				if(param.itemId){
				}else{
					return false;
				}
				$('#scoreList').datagrid('clearSelections');
			},
			onDblClickRow: function (rowIndex, rowData) {//双击查看
				
			},
			onEdit: function (rowIndex, rowData) {
				var yncheck = $('#scoreList').edatagrid('getEditor', {index: rowIndex,field:'yn'});
				var countnumber = $('#scoreList').edatagrid('getEditor', {index: rowIndex,field:'deptsCount'});
				var single = rowData.single;
				if(single == 1){
					$(countnumber.target).numberbox('readonly')
				}
				var count = countnumber.actions.getValue(countnumber.target);
				if(count != '' && count > 0){
					yncheck.target[0].checked = true;
				}else{
					yncheck.target[0].checked = false;
				}
				$(yncheck.target).click(function(){
					if(yncheck.target[0].checked){
						if(single == 1){
							countnumber.actions.setValue(countnumber.target,1);
						}else{
							$(countnumber.targrt).numberbox({required:true});
						}
					}else{
						$(countnumber.targrt).numberbox({readonly:true});
						countnumber.actions.setValue(countnumber.target,0);
// 						$('#scoreList').edatagrid('updateRow',{
// 							index: rowIndex,
// 							row: {
// 								deptsCount : '',
// 							}
// 						});
					}
				});
			},
			onBeforeSave: function (index) {
				var yn = $('#scoreList').edatagrid('getEditor', {index: index,field:'yn'});
				var deptsCount = $('#scoreList').edatagrid('getEditor', {index: index,field:'deptsCount'});
				var t1 = '否';
				var count = deptsCount.actions.getValue(deptsCount.target);
				if(count > 0){
					yn.target[0].checked = true;
				}else{
					count = 0;
				}
				if(yn.target[0].checked){
					t1 = '是';
				}else{
					count = 0;
				}
				var rows = $('#scoreList').datagrid('getRows');
				var rowData = rows[index];
				var scoreValue = rowData.scoreValue;
				var single = rowData.single;
				var deptsScore = scoreValue * count;
				$('#scoreList').edatagrid('updateRow',{
					index: index,
					row: {
						yn : t1,
						deptsCount : count,
						deptsScore : deptsScore
					}
				});
			},
			columns : [ [ 
			{
				field : 'deptsId',
				title : 'deptsId',
				hidden : true
			}, {
				field : 'deptsPatId',
				title : '患者住院号',
				hidden : true
			}, {
				field : 'scoreId',
				title : '评分编码',
				hidden : true
			}, {
				field : 'scoreCode',
				title : '评分编码',
			}, {
				field : 'scoreDesc',
				title : '评分项目',
			}, {
				field : 'scoreValue',
				title : '每次扣分',
			}, {
				field : 'scoreBak',
				title : '评分标准',
				width : '300'
			}, {
				field : 'single',
				title : '单次扣分',
				formatter: function(value,row,index){
					if (value == 1){
						return '是';
					} else {
						return '否';
					}
				},
				width : '80'
			}, {
				field : 'yn',
				title : '是否扣分',
				formatter: function(value,row,index){
					if (row.deptsScore == undefined || row.deptsScore == 0 || row.deptsScore == ''){
						return '否';
					} else {
						return '是';
					}
				},
				width : '80',
				editor : {
					type : 'checkbox',
					options : {
						
					}
				}
			}, {
				field : 'deptsCount',
				title : '扣分次数',
				editor : {
					type : 'numberbox',
					options : {
						min : 0 
					}
				},
				width : '80',
			}, {
				field : 'deptsScore',
				title : '此项扣分',
				align: 'right',
				width : '80',
			}, {
				field : 'deptCode',
				title : '科室code',
				hidden : true
			}
			] ]
		});
	}
	/**
	* 保存
	* @version 1.0
	*/
	function save(){
		var node = $('#tDt').tree('getSelected');
		if(node == null){
			$.messager.alert('提示',"请选择患者!");
			setTimeout(function(){$(".messager-body").window('close');},3500);
			return false;
		}
		var rows = $('#scoreList').edatagrid('getRows');
		for(var i = 0; i < rows.length; i++){
			$('#scoreList').edatagrid('endEdit',i);
		}
		$('#inpatientNo').val(node.attributes.inpatientNo);
		$('#infoJson').val(JSON.stringify(rows));
		$.messager.confirm('确认', '是否保存?', function(res) {//提示是否删除
			if(res){
				$('#saveForm').form('submit',{
					url : '<%=basePath%>emrs/emrDeptScore/saveScore.action',
					onSubmit : function() {
						$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条 
					},
					success : function(data) {
						$.messager.progress('close');
						if(data == 'success'){
							var tab = $('#tt').tabs('getSelected');
							var tabid = tab.context.id;
							reloadGrids(tabid,node.attributes.inpatientNo);
						}
					}
				});
			}
		});
	}
	/**
	* ifram刷新
	* @version 1.0
	*/
	function iframsrc(){
		var tab = $('#tt').tabs('getSelected');
		var tabid = tab.context.id;
		var nood = $('#tDt').tree('getSelected');
		var inpatientNo = '';
		var url = '';
		if(nood && nood.attributes && nood.attributes.pid != 'root'){
			inpatientNo = nood.attributes.inpatientNo;
		}
		if(tabid == 'A'){
			url = '<%=basePath%>emrs/emrDeptScore/emrFirstView.action?mainId=' + inpatientNo;
		}else{
			url = '<%=basePath%>emrs/emrDeptScore/emrMainListView.action';
		}
		$('iframe').attr('src',url);
		setTimeout(function(){
			reloadGrids(tabid,inpatientNo);
			},100);
		
	}
	/**
	* 刷新列表
	* @version 1.0
	*/
	function reloadGrids(itemId,inpatientNo){
		$('#scoreList').datagrid('load',{
			itemId: itemId,
			inpatientNo: inpatientNo
		});
	}
	/**
	* 得到住院号
	* @version 1.0
	*/
	function getInpatientNo(){
		var nood = $('#tDt').tree('getSelected');
		var inpatientNo = '';
		if(nood && nood.attributes && nood.attributes.pid != 'root'){
			inpatientNo = nood.attributes.inpatientNo;
		}
		return inpatientNo;
	}
	/**
	* 得到大项Id
	* @version 1.0
	*/
	function getTabId(){
		var tab = $('#tt').tabs('getSelected');
		var tabid = tab.context.id;
		return tabid;
	}
	/**
	* 刷新树
	* @version 1.0
	*/
	function refresh(){
		$('#tDt').tree('reload'); 
	}
	/**
	* 展开树
	* @version 1.0
	*/
	function expandAll(){
		$('#tDt').tree('expandAll');
	}
	/**
	* 关闭树
	* @version 1.0
	*/
	function collapseAll(){
		$('#tDt').tree('collapseAll');
	}
	/**
	*获得datagrid正在编辑行
	**/
	function getRowIndex(target){
		var tr = $(target).closest("tr.datagrid-row");
		return paseInt(tr.attr("datagrid-row-index"));
		}
</script>
<style type="text/css">
.panel-header,.tabs-header{
	border-top:0
}
.tabs-header .tabs,.tabs-panels{
	border-bottom:0
}
.tabs-panels{
	height:0 !important;
}
</style>
</head>
<body>
<div  id="divLayout" class="easyui-layout" fit=true> 
	<div data-options="region:'west',tools:'#toolSMId',split:true" title="患者管理" style="width: 20%;">
		<div id="toolSMId">
			<a href="javascript:void(0)" class="icon-reload" onclick="refresh()" ></a>
			<a href="javascript:void(0)" class="icon-fold" onclick="collapseAll()"></a>
			<a href="javascript:void(0)" class="icon-open" onclick="expandAll()"  ></a>
		</div> 
		<div id="treeDiv" style="width: 100%; height: 100%; overflow-y: auto;">
			<ul id="tDt"></ul>
		</div>
	</div>
	
	<div data-options="region:'center',border:false" style="width: 80%; ">
		<div class="easyui-layout" fit=true> 
			<div data-options="region:'north',split:false,border:false" style="width:100%;height:auto;overflow: hidden;">
				<div id="tt" class="easyui-tabs"style="height: 28px;width: 100%">
					<c:forEach var="item" items="${itemList }">
						<div title="${item.itemName }(${item.itemScore })分" id="${item.itemId }" ></div>
					</c:forEach>
				</div>
			</div>
			<div data-options="region:'center',border:true" style="width: 100%">
				<iframe  name="iframe" id="iframe"
					style="width:100%; height:100%;float:right" frameborder=0 marginwidth=0 marginheight=0></iframe>
			</div>
			<div data-options="region:'south',border:true,title:'评分'" style="height: 40%;width: 100%">
				<form id="saveForm" method="post">
					<input type="hidden" name="infoJson" id="infoJson">
					<input type="hidden" name="inpatientNo" id="inpatientNo" >
				</form>
				<table id="scoreList"  data-options="fit:true,method:'post',rownumbers:true,idField: 'deptsId',striped:true,border:false,checkOnSelect:false,selectOnCheck:false,singleSelect:true,toolbar:'#toolbar'">
				</table>
			</div>
		</div>
	</div>
	<div id="toolbar">
		<a id="btnSave" href="javascript:save();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>&nbsp;&nbsp;
	</div>
</div>
</body>
</html>