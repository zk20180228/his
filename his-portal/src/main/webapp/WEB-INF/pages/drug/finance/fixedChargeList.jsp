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
	<div id="divLayout" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'west',iconCls:'icon-reload',title:'费用等级',split:true" style="width: 12%;height: 100%">
			<ul id="hospitalTree"></ul>
		</div>
		<div data-options="region:'center',split:false,title:'费用列表',iconCls:'icon-book'" style="width:88%;height: 100%">
		<div id="divLayout" class="easyui-layout" data-options="fit:true">
			<div data-options="title:'信息查询',iconCls:'icon-search',region:'north'" style="width:100%;min-height:7%;">
				<table id="search" cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td style="padding: 5px 15px;">
							单价：
						</td>
						<td>
							<input class="easyui-textbox" ID="encode" name="chargeUnitprice" onkeydown="KeyDown()" />
						</td>
						<td style="padding: 5px 15px;">
							数量：
						</td>
						<td>
							<input class="easyui-textbox" ID="state" name="chargeAmount" onkeydown="KeyDown()" />
						</td>
						<td>
						<shiro:hasPermission name="${menuAlias}:function:query">
							&nbsp;&nbsp;
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center'" style="width:100%;height:93%;">
			<input type="hidden" value="${id }" id="id"></input>
			<table id="list" style="width:100%;height:100%;" data-options="url:'baseinfo/fixedCharge/queryFinanceFixedcharge.action?menuAlias=${menuAlias}',method:'post',selectOnCheck:false,rownumbers:true,idField: 'Id',striped:true,border:true,singleSelect:true,checkOnSelect:true,fitColumns:true,pagination:true,pageSize:20,pageList:[20,40,60,80,100],showRefresh:false,toolbar:'#toolbarId',fit:true">
				<thead>
					<tr>
						<th field="ck" checkbox="true"></th>
						<th data-options="field:'chargeAmount', width : 100">
							数量
						</th>
						<th data-options="field:'chargeUnitprice', width : 100">
							单价
						</th>
						<th data-options="field:'sDate', width : 100">
							开始时间
						</th>
						<th data-options="field:'eDate', width : 100">
							结束时间
						</th>
						<th data-options="field:'chargeIsaboutchildren', width : 100" formatter="replaceTrueOrFalse">
							是否与婴儿相关
						</th>
						<th data-options="field:'chargeIsabouttime', width : 100" formatter="replaceTrueOrFalse">
							是否与时间相关
						</th>
						<th data-options="field:'chargeState', width : 100" formatter="replaceTrueOrFalse">
							状态
						</th>
					</tr>
				</thead>
			</table>
			</div>
			</div>
			<!--<div class="easyui-panel">
				<div class="easyui-pagination"
					data-options="total:114,pageSize:20,pageList:[20,30,50,80,100],showRefresh:false"></div>
			</div>
		-->
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
		<a href="javascript:void(0)" onclick="del1()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<script type="text/javascript">
	var addAndEdit;
	//加载页面
	$(function() {
		var id = "${id}"; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				if (getbachIdUtil('#list', 0, 0).length != 0) {
					addAndEdit = 1;
					closeLayout();
					AddOrShowEast('EditForm', 'drug/finance/ViewgetFinanceFixedchargeById.action?Id=' + getbachIdUtil('#list', 1, 0));
/* =======
					AddOrShowEast('EditForm', 'baseinfo/fixedCharge/ViewgetFinanceFixedchargeById.action?Id=' + getbachIdUtil('#list', 1, 0));
>>>>>>> .r588 */
				}
			}
		});
		/**
		* 病房等级Tree
		*/
		$('#hospitalTree').tree({
			    url:'drug/finance/hospitalbedTree.action',    
/* =======
			    url:'baseinfo/fixedCharge/hospitalbedTree.action',    
>>>>>>> .r588 */
				onClick: function(node){
					//alert(node.text);  // 在用户点击的时候提示
				}
		});
		
	});
	function add(){
		addAndEdit = 0;
		closeLayout();
		AddOrShowEast('EditForm', 'drug/finance/saveFinanceFixedchargeUrl.action');
/* =======
		AddOrShowEast('EditForm', 'baseinfo/fixedCharge/saveFinanceFixedchargeUrl.action');
>>>>>>> .r588 */
	}
	
	function edit(){
		if (getbachIdUtil('#list', 0, 0).length != 0) {
			addAndEdit = 1;
			closeLayout();
			AddOrShowEast('EditForm', 'drug/finance/updataFinanceFixedchargeUrl.action?Id=' + getbachIdUtil('#list', 1, 0));
/* =======
			AddOrShowEast('EditForm', 'baseinfo/fixedCharge/updataFinanceFixedchargeUrl.action?Id=' + getbachIdUtil('#list', 1, 0));
>>>>>>> .r588 */
		}
	}
	
	function del1(){
		$.messager.confirm('确认对话框', '您想要删除改天记录吗？', function(r) {
			if (r) {
				$.post('drug/finance/deleteFinanceFixedcharge.action?Id=' + getbachIdUtil('#list', 0, 1));
/* =======
				$.post('baseinfo/fixedCharge/deleteFinanceFixedcharge.action?Id=' + getbachIdUtil('#list', 0, 1));
>>>>>>> .r588 */
				del();
				$.messager.show({
					title : '提示信息',
					msg : '删除成功!'
				});
			}
		});
	}
	
	function reload(){
		//实现刷新栏目中的数据
		$('#list').datagrid('reload');
	}
	
	//查询
	function searchFrom() {
		var encode = $('#encode').val();
		var state = $('#state').val();
		$('#list').datagrid('load', {
			chargeUnitprice : encode,
			chargeAmount : state
		});
	}
	//获取数据表格选中行的ID checked=0否则是获取勾选行的ID ，获取多个带有拼接''的ID str=0，否则不带有''，
	function getbachIdUtil(tableID, str, checked) {
		var row;
		if (checked == 0) {
			row = $(tableID).datagrid("getSelections");
		} else {
			row = $(tableID).datagrid("getChecked");
		}
		var dgID = "";
		if (row.length < 1) {
			$.messager.alert("操作提示", "请选择一条记录！", "warning");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
		var i = 0;
		for (i; i < row.length; i++) {
			if (str == 0) {
				dgID += "\'" + row[i].Id + "\'";
			} else {
				dgID += row[i].Id;
			}
			if (i < row.length - 1) {
				dgID += ',';
			} else {
				break;
			}
		}
		return dgID;
	}
	//删除选中table row 
	function del() {
		var rows = $('#list').datagrid("getChecked");
		var copyRows = [];
		for ( var j = 0; j < rows.length; j++) {
			copyRows.push(rows[j]);
		}
		for ( var i = 0; i < copyRows.length; i++) {
			var index = $('#list').datagrid('getRowIndex', copyRows[i]);
			$('#list').datagrid('deleteRow', index);
		}
	}
	//获得选中节点 tag=1获取ID tag=0获取nodetype  tag = 2 父节点ID 
	//tag=3 判断选中的是否是叶子节点，如果是叶子节点则获取id，否则赋值1
	function getSelected(tag) {
		var node = $('#hospitalTree').tree('getSelected');
		if (node != null) {
			var Pnode = $('#hospitalTree').tree('getParent', node.target);
			if (node) {
				if (tag == 0) {
					var nodeType = node.nodeType;
					return nodeType;
				}
				if (tag == 1) {
					var id = node.id;
					return id;
				}
				if (tag == 2) {
					var pid = Pnode.id;
					return pid;
				}
				if (tag == 3) {
					if ($('#hospitalTree').tree('isLeaf', node.target)) {
						var id = node.id;
						return id;
					} else {
						return 1;
					}
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
	//替换字符
	function replaceTrueOrFalse(val){
		if(val == true){
			return '是';
		}
		if(val == false){
			return '否';
		}
	}
	/** 动态添加标签页
	 * @author  sunshuo
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-05-21
	 * @version 1.0
	 */
	function AddOrShowEast(title, url) {
		$('#divLayout').layout('add', {
			region : 'east',
			width : 580,
			split : true,
			border : false,
			href : url,
			closable : true
		});
	}
	//关闭Layout
	function closeLayout() {
		$('#divLayout').layout('remove', 'east');
	}
</script>
</body>
</html>