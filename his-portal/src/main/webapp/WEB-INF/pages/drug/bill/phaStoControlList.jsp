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
<title>摆药台设置</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	//加载页面
	$(function() {
		//加载部门树
		$('#tDt').tree({
					url : '<%=basePath%>inpatient/billoffic/QuerytreeSysDepart.action?flag=1',
					method:'get',
					animate:true,
					lines:true,
					formatter:function(node){//统计节点总数
						var s = node.text;
						if (node.children){
							s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
						}
						return s;
					},onContextMenu : function(e, node) {//添加右键菜单
						e.preventDefault();
						$(this).tree('select', node.target);
						if (node.attributes.pid == 'root') {
							$('#editDiv').css("display", "none");
							$('#delDiv').css("display", "none");
							$('#tDtmm').menu('show', {
								left : e.pageX,
								top : e.pageY
							});
						} else {
							$('#editDiv').css("display", "block");
							$('#delDiv').css("display", "block");
							$('#tDtmm').menu('show', {
								left : e.pageX,
								top : e.pageY
							});
						}
					},
					onDblClick : function(node) {//点击节点
						//刷新列表
						var id=node.id;
						$('#phaStoContolList').datagrid('load', {    
						   deptCode:id
						});
						var rows1 = $('#listaddandedit').datagrid('getRows');
						if(rows1.length>0){
							for(var i=0;i<rows1.length;i++){
								$('#listaddandedit').datagrid('deleteRow', $('#listaddandedit').datagrid('getRowIndex', rows1[i]));
							}
						}
						$('#billclass').datagrid({
							url:'<%=basePath %>inpatient/bill/queryBillclass.action',
							onLoadSuccess:function(){
								$('#billclass').datagrid('unselectAll');
							}
						});
					}
				});
		//判断加载页签
		if ($('#step').val() == 'add') {
			$("#tt").tabs('select', "摆药台设置");
		} else if ($('#step').val() == 'edit') {
			$("#tt").tabs('select', "摆药台设置");
		}
		$('#listaddandedit').edatagrid({
			checkOnSelect : true,
			selectOnCheck : true,
			singleSelect : true, 
			columns : [ [ {
				field : 'id',
				title : 'id',
				width : '20%',
				hidden : true
			},{
				field : 'controlName',
				title : '摆药台名称',
				editor : {
					type : 'textbox',
					options : {
						required : true
					}
				},
				width : '18%'
			}, {
				field : 'controlAttr',
				title : '摆药台属性',
				editor : {
					type : 'combobox',
					options : {
						valueField : 'id',
						textField : 'name',
						method : 'get',
						multiple : false,
						editable : false,
						data : [ {
							id : 'G',
							name : '一般摆药'
						}, {
							id : 'S',
							name : '特殊药品摆药'
						}, {
							id : 'O',
							name : '出院带药摆药'
						} ],
						required : true
					}
				},
				width : '18%',
				formatter : function(value, row, index) {
					switch (row.controlAttr) {
					case 'G':
						text = '一般摆药';
						break;
					case 'S':
						text = '特殊药品摆药';
						break;
					case 'O':
						text = '特殊药品摆药';
						break;
					default:
						text = '';
						break;
					}
					return text;
				}
			}, {
				field : 'sendType',
				title : '发送类型',
				editor : {
					type : 'combobox',
					options : {
						valueField : 'id',
						textField : 'name',
						method : 'get',
						multiple : false,
						editable : false,
						data : [ {
							id : '1',
							name : '集中发送'
						}, {
							id : '2',
							name : '临时发送'
						}, {
							id : '3',
							name : '全部'
						}],
						required : true
					}
				},
				width : '18%',
				formatter : function(value, row, index) {
					switch (row.sendType) {
					case '1':
						text = '集中发送';
						break;
					case '2':
						text = '临时发送';
						break;
					case '3':
						text = '全部';
						break;
					default:
						text = '';
						break;
					}
					return text;
				}
			}, {
				field : 'showLevel',
				title : '显示级别',
				editor : {
					type : 'combobox',
					options : {
						valueField : 'id',
						textField : 'name',
						method : 'get',
						multiple : false,
						editable : false,
						data : [ {
							id : 0,
							name : '显示科室汇总'
						}, {
							id : 1,
							name : '显示科室明细'
						}, {
							id : 2,
							name : '显示患者明细'
						}, {
							id : 3,
							name : '全部'
						} ],
						required : true
					}
				},
				width : '18%',
				formatter : function(value, row, index) {
					switch (value) {
					case 0:
						text = '显示科室汇总';
						break;
					case 1:
						text = '显示科室明细';
						break;
					case 2:
						text = '显示患者明细';
						break;
					case 3:
						text = '全部';
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
				editor : {
					type : 'textbox'
				},
				width : '18%'
			} ] ]
		});
		$('#phaStoContolList').datagrid({
			striped : true,
			checkOnSelect : true,
			selectOnCheck : true,
			singleSelect : true,
			fitColumns : false,
			pagination : true,
			rownumbers : true,
			pageSize : 10,
			pageList : [ 10, 20, 30, 50, 100 ],
			url : '<%=basePath%>inpatient/billpha/queryPhaStoControlList.action?menuAlias=${menuAlias}',
			columns : [ [ {
				field : 'id',
				title : 'id',
				width : '10%',
				hidden : true
			}, {
				field : 'controlName',
				title : '摆药台名称',
				width : '18%',
				align : 'center'
			}, {
				field : 'controlAttr',
				title : '摆药台属性',
				width : '18%',
				align : 'center',
				formatter : function(value, row, index) {
					switch (value) {
					case 'G':
						text = '一般摆药';
						break;
					case 'S':
						text = '特殊药品摆药';
						break;
					case 'O':
						text = '出院带药摆药';
						break;
					default:
						text = '';
						break;
					}
					return text;
				}
			}, {
				field : 'sendType',
				title : '发送类型',
				width : '18%',
				align : 'center',
				formatter : function(value, row, index) {
					switch (value) {
					case 1:
						text = '集中发送';
						break;
					case 2:
						text = '临时发送';
						break;
					case 3:
						text = '全部';
						break;
					default:
						text = '';
						break;
					}
					return text;
				}
			}, {
				field : 'showLevel',
				title : '显示级别',
				width : '18%',
				align : 'center',
				formatter : function(value, row, index) {
					switch (value) {
					case 0:
						text = '显示科室汇总';
						break;
					case 1:
						text = '显示科室明细';
						break;
					case 2:
						text = '显示患者明细';
						break;
					case 3:
						text = '全部';
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
				width : '18%',
				align : 'center'
			} ] ],
			onClickRow : function(rowIndex, rowData) {
				$('#billclass').datagrid({
					url:'<%=basePath %>inpatient/bill/queryBillclass.action?controlId1='+rowData.id            
				});
			},onLoadSuccess : function(data){
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
			},onBeforeLoad:function(param){
				$('#phaStoContolList').datagrid('clearChecked');
				$('#phaStoContolList').datagrid('clearSelections');
			}
		});
		$('#billclass').datagrid({
			striped : true,
			checkOnSelect : true,
			selectOnCheck : true,
			singleSelect : false,
			fitColumns : false,
			pagination : true,
			rownumbers : true,
			pageSize : 10,
			pageList : [ 10, 20, 30, 50, 100 ],
			url : '<%=basePath%>inpatient/bill/queryBillclass.action?menuAlias=${menuAlias}',
			columns : [ [ {
				field : 'id',
				title : 'id',
				width : '10%',
				hidden : true
			}, {
				field : 'billclassCode',
				title : '摆药单分类代码',
				width : '15%',
				align : 'center'
			}, {
				field : 'billclassName',
				title : '摆药单分类名称',
				width : '23%',
				align : 'center'
			}, {
				field : 'billclassAttr',
				title : '摆药单属性',
				width : '18%',
				align : 'center',
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
				width : '14%',
				align : 'center',
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
				width : '10%',
				align : 'center',
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
				width : '10%',
				align : 'center'
			} ] ],
		onLoadSuccess : function(data){
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
	});

	//空格事件
	function KeyDown() {
		if (event.keyCode == 32) {
			event.returnValue = false;
			event.cancel = true;
			openDept();
		}
	}
	/**
	 * 添加弹出科室树
	 * @author  lt
	 * @param title 标签名称
	 * @param url 跳转路径
	 * @date 2015-06-25
	 * @version 1.0
	 */
	function AddDeptdilog(title, url) {
		$('#addDept').dialog({
			title : title,
			width : '30%',
			height : '90%',
			closed : false,
			cache : false,
			href : url,
			modal : true
		});
	}
	/**
	 * 打开科室树框
	 * @author  lt
	 * @date 2015-06-26
	 * @version 1.0
	 */
	function openDeptDialog() {
		$('#addDept').dialog('open');
	}
	/**
	 * 加载科室树框及信息
	 * @author  lt
	 * @date 2015-06-26
	 * @version 1.0
	 */
	function openDept(flg) {
		if (flg == 1) {
			AddDeptdilog("科室信息", 'baseinfo/department/deptTree.action?flg=1');
		}
		if (flg == 2) {
			var rows = $('#' + $('#modelWeek').val())
					.edatagrid('getSelections');
			if (rows.length > 0) {//选中几行的话触发事件	
				var ids = '';
				for (var i = 0; i < rows.length; i++) {
					if (ids != '') {
						ids += ',';
					}
					ids += rows[i].id;
				};
				AddDeptdilog("科室信息", 'baseinfo/department/deptTree.action?flg=2&inoutdeptids='
						+ ids);
			} else {
				$.messager.alert("操作提示", "请从列表中选择操作条目！", "warning");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}

		}
	}



	//科室部门树操作
	function refresh() {//刷新树		
		$('#tDt').tree('options').url = "drug/storage/treeDrugstore.action?flag=1";
		$('#tDt').tree('reload');
	}  
	function expandAll() {//展开树
		$('#tDt').tree('expandAll');
	}
	function collapseAll() {//关闭树
		$('#tDt').tree('collapseAll');
	}
	function getSelected() {//获得选中节点
		var node = $('#tDt').tree('getSelected');
		if (node) {
			var id = node.id;
			return id;
		} else {
			return "";
		}
	}
	//添加
	function add() {
		$('#saveByUpdate').linkbutton('enable');
		$('#deletefl').linkbutton('enable');
		$('#classids').val('1');
		$('#billclass').datagrid({
			url:'<%=basePath %>inpatient/bill/queryBillclass.action',
			onLoadSuccess:function(){
				$('#billclass').datagrid('unselectAll');
			}
		});
		
		var rowss=$('#listaddandedit').datagrid('getRows');
		for(var i;i<rowss.length;i++){
			$('#listaddandedit').datagrid('endEdit', i);
		}
		if(rowss.length>0){
			$('#listaddandedit').datagrid('beginEdit', 0);
			return;
		}
		var index = $('#listaddandedit').edatagrid('appendRow', {
		}).edatagrid(
		'getRows').length - 1;
		$('#listaddandedit').edatagrid('beginEdit', index); 
	}
	//删除
	function del() {
		var rows = $('#phaStoContolList').datagrid('getSelected');
		var ids = rows.id;
		if (ids != null && ids != "") {
			$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
				if (res) {
					$.messager.progress({text:'删除中，请稍后...',modal:true});	// 显示进度条
					$.ajax({
						url : '<%=basePath%>inpatient/billpha/delPhaStoControl.action?ids=' + ids,
						type : 'post',
						success : function() {
							$.messager.progress('close');
							$.messager.alert('提示','删除成功！');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							$('#phaStoContolList').datagrid('reload');
						}
					});
				}
			});
		}
	}
	//刷新
	function reload(){
		$('#phaStoContolList').datagrid('reload');
	}
	
	/**
	 * 动态添加LayOut
	 * @author  liujl
	 * @param title 标签名称
	 * @param url 跳转路径
	 * @date 2015-05-21
	 * @modifiedTime 2015-6-18
	 * @modifier liujl
	 * @version 1.0
	 */
		function AddOrShowEast(title, url) {
			var eastpanel=$('#panelEast'); //获取右侧收缩面板
			if(eastpanel.length>0){ //判断右侧收缩面板是否存在
				//重新装载右侧面板
		   		$('#cc').layout('panel','east').panel({
                       href:url 
                });
			}else{//打开新面板
				
				$('#cc').layout('add', {
					region : 'east',
					width : 500,
					split : true,
					border : false,
					href : url,
					closable : true
				});
			}
		}
		function editInfo() {
			$('#saveByUpdate').linkbutton('enable');
			$('#deletefl').linkbutton('enable');
			var rows = $('#phaStoContolList').datagrid('getSelections');
			if(rows==""){
				$.messager.alert('提示','请选择至少一条数据！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
			if (rows.length > 0) {//选中几行的话触发事件
				$('#classids').val('2');
				var ids = rows[0].id;
				$('#listaddandedit').datagrid({
					url : '<%=basePath%>inpatient/billpha/viewOutpatientDrugcontrol.action',
					queryParams:{controlId:ids},
					onLoadSuccess:function(){
						$('#listaddandedit').datagrid('beginEdit', 0);
						$('#billclass').datagrid({
							url:'<%=basePath %>inpatient/bill/queryBillclass.action',
							onLoadSuccess:function(data){
								var result = data.rows;
								 for(var i=0;i<result.length;i++){
									var rows = $('#listaddandedit').datagrid('getRows');
									for(var j=0;j<rows.length;j++){
										if(result[i].controlId == rows[j].id){
											var index =$('#billclass').datagrid('getRowIndex',result[i]);
											$('#billclass').edatagrid('selectRow',index);
										}
									}
								} 
							}
						});
					},
				});
			}
		}
		//保存
		function save(){
			var idss = $('#classids').val();
			if(idss=='1'){
				var node = $('#tDt').tree('getSelected');
				var rows = $('#billclass').datagrid("getSelections");
				if(node!=null&&node!=''){
					if(rows.length>0){
						var ids = '';
						for ( var i = 0; i < rows.length; i++) {
							if(ids != null && ids != ""){
								ids += ",";
								}
							ids += rows[i].id;
							}
						$('#listaddandedit').edatagrid('acceptChanges');
						$('#billJsonby').val(JSON.stringify( $('#listaddandedit').edatagrid("getRows")));
						$('#editForm').form('submit',{
				    			url:'<%=basePath%>inpatient/billpha/savePhaSto.action', 
				    			queryParams:{deptCode5:node.id,billClassIdss:ids},		
				        onSubmit:function(){ 
				        	if (!$(this).form('validate')) {
								$.messager.alert('提示',"验证没有通过,不能提交表单!");
								setTimeout(function(){
									$(".messager-body").window('close');
								},1500);
								return false;
							}
							$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条  
				        },  
				        success:function(data){
				        	$.messager.progress('close');
				        	$.messager.alert("操作提示", "添加成功！", "success");
				        	setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
				        	var rows1 = $('#listaddandedit').datagrid('getRows');
							for(var i=0;i<rows1.length;i++){
								$('#listaddandedit').datagrid('deleteRow', $('#listaddandedit').datagrid('getRowIndex', rows1[i]));
							}
				             //实现刷新栏目中的数据s
				            $("#phaStoContolList").edatagrid("reload");
				            $("#billclass").edatagrid("reload");
				        },
						error : function(data) {
							$.messager.progress('close');
							$.messager.alert('提示','保存失败！');
						}			         
					});
				}else{
					$.messager.alert('提示','还未选择摆药单！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}else{
				$.messager.alert('提示','还没选择科室！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}else{
			var node = $('#tDt').tree('getSelected');
			var deptId = "";
			if(node==null){
				deptId = null;
			}else{
				deptId = node.id;
			}
			var rows = $('#billclass').datagrid('getSelections');
			var contolrows = $('#phaStoContolList').datagrid('getSelections');
			var ids ='';
			for (var i = 0; i < rows.length; i++) {
				if(ids != null && ids != ""){
					ids += ",";
				}
				ids += rows[i].id;
			}
			$('#listaddandedit').edatagrid('acceptChanges');
			$('#billJsonby').val(JSON.stringify( $('#listaddandedit').edatagrid("getRows")));
			$('#editForm').form('submit',{
	    			url:'<%=basePath%>inpatient/billpha/updateOutpatientDrugcontrol.action', 
	    			queryParams:{classId:ids,dept:deptId,ids:contolrows[0].id},
	        onSubmit:function(){ 
	        	if (!$(this).form('validate')) {
					$.messager.alert('提示',"验证没有通过,不能提交表单!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},1500);
					return false;
				}
	        	$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条 
	        }, 
	        success:function(data){
	        	$.messager.progress('close');
	        	$.messager.alert("操作提示", "修改成功！", "success");
	        	setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	        	var rows1 = $('#listaddandedit').datagrid('getRows');
				for(var i=0;i<rows1.length;i++){
					$('#listaddandedit').datagrid('deleteRow', $('#listaddandedit').datagrid('getRowIndex', rows1[i]));
				}
	            //实现刷新栏目中的数据
	            $("#phaStoContolList").edatagrid("reload");
	            $("#billclass").edatagrid("reload");
	        },
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','还没选择科室！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}			         
		}); 
		}
	}
		/**
		 * 删除添加的摆药分类
		 */
		function deleteBill(){
			var rows1 = $('#listaddandedit').datagrid('getRows');
			if(rows1.length==0){
				$('#saveByUpdate').linkbutton('enable');
				$('#deletefl').linkbutton('enable');
			}
			for(var i=0;i<rows1.length;i++){
				$('#listaddandedit').datagrid('deleteRow', $('#listaddandedit').datagrid('getRowIndex', rows1[i]));
			}
		}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style>
.panel-header{
	border-top:0
}
</style>
</head>
<body style="margin: 0px; padding: 0px"> 
	<div id="cc" class="easyui-layout" style="overflow-y: auto;border-top:0" fit=true>   
	    <div data-options="region:'west',title:'摆药台设置',split:true" style="width:16%;height: 100%;min-width: 90px;border-top:0">
			<ul id="tDt"></ul><input id="classids" type="hidden">
		</div>
		<div data-options="region:'center',border:false" style="width:83%;height: 100%">
			<div id="bb" class="easyui-layout" style="width:100%;height:100%;">   
			    <div data-options="region:'north',title:'摆药台列表'" style="width:100%; height: 37%;">
			    	<table id="phaStoContolList" data-options="toolbar:'#toolbarId',fit:true,border:false" ></table>
			    </div>   
			    <div data-options="region:'south',title:'摆药单分类列表'" style="width:100%;height: 37%;">
			    	<table id="billclass" data-options="fit:true,border:false"></table>
			    </div>
			     <div data-options="region:'center'" style="width:100%;height: 26%;border-top:0;margin-top:-1px;">
			     	<form id="editForm" method="post">
						<input type="hidden" id="billJsonby" name="billJsonby">
						<div style="height: 100%;width: 100%;">
							<table id="listaddandedit" style="width: 100%;height: 100%"
								data-options="method:'post',rownumbers:true,idField:'id',striped:false,border:false,fit:true,toolbar:'#tool'">
							</table>
						</div>
					</form>
			     </div>  
			</div>
		</div>   
	</div>
	<div id="tool">
		<shiro:hasPermission name="${menuAlias}:function:save">
			<a href="javascript:void(0)" onclick="save()" id="saveByUpdate" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:true">保存</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">
			<a href="javascript:void(0)" id="deletefl" onclick="deleteBill()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',disabled:true">删除</a>
		</shiro:hasPermission>
	</div>
	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="editInfo()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">编辑</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">
			<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>   
</body>
</html>