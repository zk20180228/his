<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div id="cc" class="easyui-layout" style="width: 100%; height: 100%;">
		<div data-options="region:'west',title:'药房药库'"
			style="width: 15%; height: 100%;">
		</div>
		<div data-options="region:'center'" style="width: 85%; height: 100%;">
			<div id="cc" class="easyui-layout" style="width: 100%; height: 100%;">
				<div data-options="region:'north',title:'摆药台列表'"
					style="width: 100%; height: 45%;">
					<table id="list"
						data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,toolbar:'#toolbarId'">
					</table>
				</div>
				<div data-options="region:'center',title:'摆药台列表明细'"
					style="padding: 5px; width: 100%; height: 50%;">
						<table id="infoList"
							data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,toolbar:'#tbId'">
						</table>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			$('#list').datagrid({
				checkOnSelect : true,
				selectOnCheck : true,
				singleSelect : false,
				pagination : true,
				fitColumns : true,
				pageSize : 12,
				pageList : [ 12, 20, 50, 100 ],
				url : '<%=basePath%>inpatient/bill/queryBillclass.action?menuAlias=${menuAlias}',
				columns : [ [ {field : 'ck',checkbox : true}, 
				              {field : 'id',title : 'id',width : '15%',hidden : true},
				              {field : 'billclassCode',title : '摆药单分类代码',type : 'textbox',
				            	  options : {required : true},width : '15%'}, 
				            	  {field : 'billclassName',title : '摆药单分类名称',type : 'textbox',
				            		  options : {required : true},width : '15%'},
				            		  {field : 'billclassAttr',title : '摆药单属性',type : 'combobox',
				            			  options : {valueField : 'id',textField : 'name',method : 'get',multiple : false,editable : false,
				            				  data : [{id : 'O',name : '一般摆药'},
				            				          {id : 'T',name : '特殊药品摆药'},
				            				          {id : 'R',name : '出院带药摆药'}],
				            				          required : true},width : '15%',
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
						data : [{id : 'T',name : '汇总'},
						         {id : 'D',name : '明细'},
						         {id : 'H',name : '草药'},
						         {id : 'R',name : '出院带药'}],
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
				} ] ]
			});
			$('#infoList').datagrid({
				checkOnSelect : true,
				selectOnCheck : true,
				singleSelect : false,
				pagination : true,
				fitColumns : true,
				pageSize : 20,
				pageList : [ 20, 30, 50, 100 ],
				url : '<%=basePath%>inpatient/billlist/queryBilllist.action?menuAlias=${menuAlias}',
				columns : [ [{
							field : 'ck',
							checkbox : true
						},
						{
							field : 'id',
							title : 'id',
							width : '10%',
							hidden : true
						},
						{
							field : 'typeCode',
							title : '医嘱类别',
							type : 'combobox',
							options : {
								valueField : 'id',
								textField : 'name',
								method : 'get',
								multiple : false,
								editable : false,
								url : '<%=basePath%>comboBox.action?str='
										+ "CodeOrdercategory",
								required : true
							},
							width : '10%',
							formatter : function(value, row,
									index) {
								for (var i = 0; i < typeCodeList.length; i++) {
									if (value == typeCodeList[i].id) {
										return typeCodeList[i].name;
									}
								}
							}
						},
						{
							field : 'usageCode',
							title : '用法代码',
							type : 'combobox',
							options : {
								valueField : 'id',
								textField : 'name',
								method : 'get',
								multiple : false,
								editable : false,
								url : '<%=basePath%>comboBox.action?str='
										+ "CodeUseage",
								required : true
							},
							formatter : function(value, row,
									index) {
								for (var i = 0; i < usageCodeList.length; i++) {
									if (value == usageCodeList[i].id) {
										return usageCodeList[i].name;
									}
								}
							},
							width : '10%'

						},
						{
							field : 'drugType',
							title : '药品类别',
							type : 'combobox',
							options : {
								valueField : 'id',
								textField : 'name',
								method : 'get',
								multiple : false,
								editable : false,
								url : '<%=basePath%>comboBox.action?str='
										+ "CodeDrugType",
								required : true
							},
							width : '10%',
							formatter : function(value, row,
									index) {
								for (var i = 0; i < drugDrugtypeList.length; i++) {
									if (value == drugDrugtypeList[i].id) {
										return drugDrugtypeList[i].name;
									}
								}
							}
						},
						{
							field : 'drugQuality',
							title : '药品性质',
							type : 'combobox',
							options : {
								valueField : 'id',
								textField : 'name',
								method : 'get',
								multiple : false,
								editable : false,
								url : '<%=basePath%>comboBox.action?str='
										+ "CodeDrugproperties",
								required : true
							},
							width : '10%',
							formatter : function(value, row,
									index) {
								for (var i = 0; i < drugpropertiesList.length; i++) {
									if (value == drugpropertiesList[i].id) {
										return drugpropertiesList[i].name;
									}
								}
							}
						},
						{
							field : 'doseModelCode',
							title : '剂型代码',
							type : 'combobox',
							options : {
								valueField : 'id',
								textField : 'name',
								method : 'get',
								multiple : false,
								editable : false,
								url : '<%=basePath%>comboBox.action?str='
										+ "CodeDosageform",
								required : true
							},
							width : '10%',
							formatter : function(value, row,
									index) {
								for (var i = 0; i < drugDosageformList.length; i++) {
									if (value == drugDosageformList[i].id) {
										return drugDosageformList[i].name;
									}
								}
							}
						},
						{
							field : 'ipmState',
							title : '医嘱状态',
							type : 'combobox',
							options : {
								valueField : 'id',
								textField : 'name',
								method : 'get',
								multiple : false,
								editable : false,
								data : [ {
									id : 1,
									name : '长期'
								}, {
									id : 2,
									name : '短期'
								}, {
									id : 3,
									name : '全部'
								} ],
								required : true
							},
							width : '10%',
							formatter : function(value, row,
									index) {
								switch (value) {
								case 1:
									text = '长期';
									break;
								case 2:
									text = '短期';
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
						} ] ]
			});
		});
	</script>
</body>
</html>