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
		<%@ include file="/common/metas.jsp"%>
		<title>非药品维护</title>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
		
		<style type="text/css">
		.window .panel-header .panel-tool a{
  	  			background-color: red;	
			}
		</style>
		<script type="text/javascript">
			var addAndEdit;
			var systemtypeList = null;
			var deptMap=null;
			var diseaseclassificationList = null;
			//加载页面
			$(function() {
				var id = "${id}"; //存储数据ID
				//添加datagrid事件及分页
				$.ajax({//根据id获取执行科室名称
					url: "<%=basePath%>baseinfo/department/getDeptMap.action",
					type:'post',
					success: function(deptdata) {
						deptMap = deptdata;
						$('#list').datagrid({
							url:'<%=basePath%>drug/undrug/queryunDrug.action?menuAlias=${menuAlias}',
							onBeforeLoad:function(){
								$('#list').datagrid('clearChecked');
								$('#list').datagrid('clearSelections');
							},
							 onDblClickRow : function(rowIndex, rowData) {//双击查看
									Adddilog("查看非药品","<%=basePath%>drug/undrug/viewUndrug.action?id="+rowData.id);
							},
							onBeforeLoad:function(){
								//GH 2017年2月17日 翻页时清空前页的选中项
								$('#list').datagrid('clearChecked');
								$('#list').datagrid('clearSelections');
							},onLoadSuccess:function(row, data){
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
				});
				$('#feeType').combobox({
					url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=drugMinimumcost",
					valueField:'encode',    
				    textField:'name',
				    multiple : false,
				    onHidePanel:function(none){
				        var data = $(this).combobox('getData');
				        var val = $(this).combobox('getValue');
				        var result = true;
				        for (var i = 0; i < data.length; i++) {
				            if (val == data[i].encode) {
				                result = false;
				            }
				        }
				        if (result) {
				            $(this).combobox("clear");
				        }else{
				            $(this).combobox('unselect',val);
				            $(this).combobox('select',val);
				        }
				    },
			    	filter: function(q, row){
                        var keys = new Array();
                        keys[keys.length] = 'encode';
                        keys[keys.length] = 'name';
                        keys[keys.length] = 'pinyin';
                        keys[keys.length] = 'wb';
                        keys[keys.length] = 'inputCode';
                        return filterLocalCombobox(q, row, keys);
               }
				});
				$('#systemType').combobox({
					url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=systemType",
					valueField:'encode',    
				    textField:'name',
				    multiple : false,
				    onChange:function(value){
				    	searchFrom();
			    	}
				});
				$.ajax({
						url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=systemType",
						type:'post',
						success: function(systemtypeData) {
							systemtypeList = systemtypeData;
							}
						});	
				$.ajax({
						url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=diseasetype",
						type:'post',
						success: function(diseasetypeData) {
							diseaseclassificationList = diseasetypeData;
							}
						});	
				$('#stateText').textbox({
				});
				bindEnterEvent('encode',searchFrom,'easyui');
				bindBlackEvent('stateText',popWinToDept,'easyui');//绑定回车事件	
				bindEnterEvent('stateText',searchFrom,'easyui');
			});
			 
			function add(){
				addAndEdit = 0;
				Adddilog('编辑', "<%=basePath%>drug/undrug/saveDrugUndrugUrl.action");
			}
			
			
			function edit(){
				var row = $('#list').datagrid('getSelected'); //获取当前选中行  
				if (row) {
					Adddilog('编辑', "<%=basePath%>drug/undrug/updataDrugUndrugUrl.action?Id="+row.id);
				}
			}
			function delSelect(){
				$('#state').val("");
				 $('#stateText').textbox('setValue',"");
			}
			function del1(){
				 var rows = $('#list').datagrid('getChecked');
		     	if (rows.length > 0) {
					$.messager.confirm('确认对话框', '您当前选中了【'+rows.length+'】条记录,确定要删除吗', function(r) {
						if (r) {
							$.messager.progress({text:'删除中，请稍后...',modal:true});	// 显示进度条
							var ids = '';
							for(var i=0; i<rows.length; i++){
								if(ids!=''){
									ids += ',';
								}
								ids += rows[i].id;
							};
							
							$.ajax({
								url: "<%=basePath%>drug/undrug/deleteDrugUndrug.action?ids="+ids,
								type:'post',
								success: function() {
									$.messager.progress('close');
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
				$('#list').datagrid('reload');
			}
			//查询
			function searchFrom() {
				var encode = $.trim($('#encode').textbox('getValue'));
				var state = $.trim($('#state').val());
				var sys = $.trim($('#systemType').combobox('getValue'));
				var fee = $.trim($('#feeType').combobox('getValue'));
				$('#list').datagrid('load', {
					undrugNamequery: encode,
					undrugDeptquery : state,
					undrugSystypequery : sys,
					undrugMinimumcostquery : fee
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
			//获得选中id	
			function getId(parameter) {
				var row = $("#list").datagrid("getSelections");
				var i = 0;
				if (parameter == 'single') {//获得单个id
					if (row.length < 1) {
						$.messager.alert('提示',"请选择一条记录！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return null;
					} else if (row.length > 1) {
						$.messager.alert('提示',"只能选择一条记录！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return null;
					} else {
						var id = "";
						for (i; i < row.length; i++) {
							id += row[i].id;
							return id;
						}
					}
				} else if (parameter == 'plurality') {//获得多个id
					if (row.length < 1) {
						$.messager.alert('提示',"请至少选择一条记录！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return null;
					} else {
						var ids = "";
						for (i; i < row.length; i++) {
							ids += row[i].id + ",";
						}
						return ids;
					}
				} else if (parameter == 'notNull') {//至少获得一个id
					var id = "";
					if (row.length < 1) {//如果没有选择数据，默认选中第一行数据
						$('#list').datagrid('selectRow', 0);
						var row = $("#list").datagrid("getSelections");
					}
					id += row[0].id;
					return id;
				} else {
					$.messager.alert('提示',"参数无效！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return null;
				}
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
			//替换字符
			function replaceTrueOrFalse(val){
				if(val == 1){
					return '是';
				}if(val == 0){
					return '否';
				}if(val == ""||val==null){
					return '否';
				}
				
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
				var eastpanel = $('#divLayout').layout('panel', 'east'); //获取右侧收缩面板
				if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
					//重新装载右侧面板
					$('#divLayout').layout('panel', 'east').panel({
						href : url
					});
				} else {//打开新面板
					$('#divLayout').layout('add', {
						region : 'east',
						width : 800,
						split : true,
						href : url,
						closable : true
					});
				}
		
			}
			/**
			 * 动态添加标签页
			 * @author  sunshuo
			 * @param title 标签名称
			 * @param title 跳转路径
			 * @date 2015-05-21
			 * @version 1.0
			 */
			function Adddilog(title, url) {
				$('#undrug').dialog({    
				    title: title,    
				    width: '90%',    
				    height: '77%',
				    top:'15',
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				   });    
			}
			//打开dialog
			function openDialog() {
			$('#undrug').dialog('open'); 
		
			}
			//关闭dialog
			function closeDialog() {
			$('#undrug').dialog('close');  
			}
			//显示系统类别格式化 
			function systemtypeFamater(value){
				if(value!=null){
					for(var i=0;i<systemtypeList.length;i++){
						if(value==systemtypeList[i].encode){
							return systemtypeList[i].name;
						}
					}	
				}
			}
			//显示疾病类别格式化 
			function diseasetypeFamater(value){
				if(value!=null){
					for(var i=0;i<diseaseclassificationList.length;i++){
						if(value==diseaseclassificationList[i].encode){
							return diseaseclassificationList[i].name;
						}
					}	
				}
			}
			
			
			/**
				 * 科室列表页 显示
				 * @author  zpty
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-12-9
				 * @version 1.0
				 */
				function deptFamater(value,row,index){
					if(value!=null&&value!=''){
						return deptMap[value];
					}
				}
				 /**
				   * 回车弹出执行科室弹框
				   * @author  zhuxiaolu
				   * @param deptIsforregister 是否是挂号科室 1是 0否
				   * @param textId 页面上commbox的的id
				   * @date 2016-03-22 14:30   
				   * @version 1.0
				   */
				  
			 function popWinToDept(){
				 popWinDeptCallBackFn = function(node){
					 $("#state").val(node.deptCode);
					 $('#stateText').textbox('setValue',node.deptName);
					 var encode = $.trim($('#encode').textbox('getValue'));
					var state = $('#state').val();
						$('#list').datagrid('load', {
							undrugNamequery : encode,
							undrugDeptquery : state
						});
				   };
					var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=stateText";
					window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
				}
				
			// 药品列表查询重置
				function searchReload() {
					$('#encode').textbox('setValue','');
					$('#state').val("");
					$('#stateText').textbox('setValue',"");
					$('#feeType').combobox('setValue','');
					$('#systemType').combobox('setValue','');
					searchFrom();
				}
		</script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</head>
	<body  style="margin: 0px;padding: 0px;">
		<div id="el" class="easyui-layout" data-options="fit:true">   
			<div data-options="region:'north',split:false,border:false" style="height:40px;">
				<table id="search" style="width: 100%;">
					<tr>
						<td style="padding: 5px;">
							<span>项目名称：</span>
							<input class="easyui-textbox" id="encode" name="undrugName" />&nbsp;
							<span>执行科室：</span>
							<input type="hidden" id="state" style="width: 200px" />
							<input id="stateText" readonly="readonly" style="width: 200px" data-options="prompt:'请空格选择科室'" />
							<a href="javascript:void(0)" onclick="delSelect()" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							&nbsp;
							<span>费用类别：</span>
							<input id="feeType"  class="eseyui-combobox" type="text" />
							&nbsp;
							<span>系统类别：</span>
							<input id="systemType"  class="eseyui-combobox" type="text" />
							<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:void(0)" onclick="searchFrom()"
									class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</shiro:hasPermission>
							<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',border:false" style="height:100%">
				<table id="list" style="width: 100%;"
					data-options="method:'post',selectOnCheck:false,fit:true,rownumbers:true,idField: 'id',striped:true,singleSelect:true,checkOnSelect:true,pagination:true,pageSize:20,pageList:[20,40,60,80,100],showRefresh:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th field="ck" checkbox="true"></th>
							<th data-options="field:'name', width : '12%'">
								项目名称
							</th>
							<th
								data-options="field:'undrugState', width : '10%',formatter: function(value,row,index){
																							if (value=='1'){
																								return '在用';
																							}if (value=='2'){
																								return '停用';
																							} if (value=='3'){
																								return '废弃';
																							} 
																						}">
								状态
							</th>
							<th
								data-options="field:'undrugDept',formatter:deptFamater, width : '10%'">
								执行科室
							</th>
							<th data-options="field:'defaultprice', width : '10%' ">
								默认价
							</th>
							<th
								data-options="field:'undrugIsownexpense', width :'11%',formatter:replaceTrueOrFalse">
								自费
							</th>
							<th
								data-options="field:'undrugIsspecificitems', width :'11%',formatter:replaceTrueOrFalse">
								特定项目
							</th>
							<th
								data-options="field:'undrugIsstack', width : '11%',formatter:replaceTrueOrFalse">
								组套
							</th>
							<th
								data-options="field:'undrugDiseaseclassification', width :'11%',formatter:diseasetypeFamater">
								疾病分类
							</th>
							<th data-options="field:'undrugRemark', width : '11%'">
								备注
							</th>
						</tr>
					</thead>
				</table>
			</div>
		<div id="undrug"></div>
		<div id="toolbarId">
			<shiro:hasPermission name="${menuAlias}:function:add">
				<a href="javascript:void(0)" onclick="add()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true">添加</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:edit">
				<a href="javascript:void(0)" onclick="edit()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-edit',plain:true">修改</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:delete">
				<a href="javascript:void(0)" onclick="del1()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reload()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
	</body>
</html>