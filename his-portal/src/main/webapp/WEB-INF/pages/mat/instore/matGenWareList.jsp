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
		<div>
			<table cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td style="padding: 5px 15px;" nowrap="nowrap" >
							操作类型：
							<input class="easyui-combobox" id="queryName" data-options="valueField: 'id', textField: 'value', 
								data: [{ id: '01', value: '一般入库',selected:true },{ id: '02', value: '发票入库' },{id: '03', value: '核准入库' },{id: '04', value: '入库退货' },
									   {id: '05', value: '内部入库申请'},{id: '06', value: '内部入库核准'},{id: '07', value: '内部入库退库申请'},
									   {id: '08', value: '备货入库'},{id: '09', value: '备货退货'},{id: '10', value: '备货换货'},{id: '11', value: '特殊入库'},{id: '12', value: '特殊入库审核'}
										],onSelect: function(rec){ 
									if(rec.id=='01'){
										window.location.href='<%=basePath%>material/instore/listMatGenWare.action?menuAlias=WZRK';
									}
									if(rec.id=='02'){
										window.location.href='<%=basePath%>material/instore/listMatInvoiceWare.action?menuAlias=WZRK';
									}
									if(rec.id=='03'){
										window.location.href='<%=basePath%>material/instore/listMatApproveWare.action?menuAlias=WZRK';
									}
									if(rec.id=='04'){
										window.location.href='<%=basePath%>material/instore/listMatReturnWare.action?menuAlias=WZRK';
									}
									if(rec.id=='05'){
										window.location.href='<%=basePath%>material/instore/listMatInsideStorageApply.action?menuAlias=WZRK';
									}
									if(rec.id=='06'){
										window.location.href='<%=basePath%>material/instore/listMatInsideStorageApproval.action?menuAlias=WZRK';
									}
									if(rec.id=='07'){
										window.location.href='<%=basePath%>material/instore/listMatInsideStorageRefundApply.action?menuAlias=WZRK';
									}
									if(rec.id=='08'){
										window.location.href='<%=basePath%>material/instore/listMatChoiceStorage.action?menuAlias=WZRK';
									}
									if(rec.id=='09'){
										window.location.href='<%=basePath%>material/instore/listMatChoiceRefundStorage.action?menuAlias=WZRK';
									}
									if(rec.id=='10'){
										window.location.href='<%=basePath%>material/instore/listMatChoiceChangeStorage.action?menuAlias=WZRK';
									}
									if(rec.id=='11'){
										window.location.href='<%=basePath%>material/instore/listMatSpecial.action?menuAlias=WZRK';
									}
									if(rec.id=='12'){
										window.location.href='<%=basePath%>material/instore/listMatSpecialApprove.action?menuAlias=WZRK';
									}
						        }" style="width:180px"/>
						</td>
						<td>
							&nbsp;&nbsp;
							目标单位：
							<input id="queryCompanyCode" style="width:180px"/>
							<a href="javascript:delSelectedData('queryCompanyCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="javascript:void(0)" onclick="" class="easyui-linkbutton">切换库房</a>
						</td>
					</tr>
				</table>
		</div>
		<div id="divLayout" class="easyui-layout" fit=true>
			<!-- 物资一般入库 -->
			<div data-options="region:'west',split:false,title:'物资列表',iconCls:'icon-book'" style="padding: 5px; width: 35% ;">
				<table cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td style="padding: 5px 15px;" nowrap="nowrap" >
							查询条件：
							<input class="easyui-textbox" id="itemNameSerch" onkeydown="keyDown()" data-options="prompt:'编码,名称'" style="width:180px"/>
						</td>
						<td>
							&nbsp;&nbsp;
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</td>
					</tr>
				</table>
				<table id="list" data-options="url:'<%=basePath%>material/instore/queryMatBaseInfo.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fitColumns:false">
					<thead>
						<tr>
							<th data-options="field:'itemCode',width : '25%'">
								物品编码
							</th>
							<th data-options="field:'itemName',width : '12%'">
								物品名称
							</th>
							<th data-options="field:'specs',width : '20%'">
								物品规格
							</th>
						</tr>
					</thead>
				</table>
			</div>
			<div data-options="region:'center',title:'待入库物资列表'" style="padding: 5px;">
				<form id="saveForm" method="post">
					<input type="hidden" name="infoJson" id="infoJson">
					<table id="infolist"
						data-options="url:'',method:'post',idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar:'#toolbarId'">
					</table>
				</form>
			</div>
			<div id="toolbarId">
				<shiro:hasPermission name="WZRK:function:save">
					<a href="javascript:void(0)" onclick="save()"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-add',plain:true">保存</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="WZRK:function:delete">
					<a href="javascript:void(0)" onclick="del()"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-remove',plain:true">删除</a>
				</shiro:hasPermission>
				<!--<shiro:hasPermission name="WZYBRK:function:export">
					<a href="javascript:void(0)" onclick="exportList()"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-down',plain:true">导出列表</a>
				</shiro:hasPermission>
				<a href="javascript:void(0)" onclick="reload()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-reload',plain:true">刷新</a>
				-->
			</div>
		</div>
		<script type="text/javascript">
			//记录赋值过的条目id
			var arrIds = [];
			//加载页面
			$(function() {
				//绑定回车事件
				bindEnterEvent('itemNameSerch',searchFrom,'easyui');
				//初始化供货单位下拉
				$('#queryCompanyCode').combobox({
					url : "<c:url value='/drug/supply/queryCompanyList.action'/>",
					valueField : 'Id',
					textField : 'companyName',
					width : 180,
					onSelect: function(rec){    
			           $('#list').datagrid({
			           		url:'<%=basePath%>material/instore/queryMatBaseInfo.action?companyCode='+rec.id
			           });     
			        }
				});
				
				/**
				* 绑定供货单位的回车事件
				* @author  zhuxiaolu
				* @date 2016-03-22 14:30   
				* @version 1.0
				*/
				bindEnterEvent('queryCompanyCode',popWinToCompany,'easyui');//绑定回车事件
				$('#list').datagrid({
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 30, 50, 80, 100 ],
					onBeforeLoad:function (param) {
						$.ajax({
							url:"<c:url value='/comboBox.action'/>", 
							data:{"str" : "CodeDrugpackagingunit"},
							type:'post',
							success: function(packUnitdata) {
								packUnitList = eval("("+ packUnitdata +")");
							}
						});
					},
					onDblClickRow : function(rowIndex, rowData) {//双击赋值
						var row = $('#list').datagrid('getSelected');
						if (row) {
							var index = $('#infolist').edatagrid('appendRow', {
								itemCode : row.itemCode,
								itemName : row.itemName,
								specs : row.specs,
								minUnit : row.minUnit,
								packUnit : row.packUnit,
								inPrice : row.inPrice,
								inCost : row.inCost,
								validDate : row.validDate,
								companyCode : row.companyCode,
								companyName : formatCompany(row.companyCode),
								factoryCode : row.factoryCode,
								highvalueFlag:row.highvalueFlag,
								financeFlag:row.financeFlag,
								salePrice:row.salePrice,
								packQty:row.packQty,
								packPrice:row.packPrice,
								addRate:row.addRate,
								kindCode:row.kindCode
							}).edatagrid('getRows').length - 1;
							$('#infolist').edatagrid('selectRow', index);
							//$('#infolist').edatagrid('beginEdit', index);
						}
					}
				});
				$('#infolist').edatagrid({
					striped : true,
					checkOnSelect : true,
					selectOnCheck : true,
					singleSelect : false,
					fitColumns : false,
					columns : [ [ {
						field : 'ck',
						checkbox : true
					},
					{
						field : 'itemCode',
						title : '物品编码',
						hidden : true
					}, {
						field : 'itemName',
						title : '物品名称',
						width : '9%'
					},{
						field : 'specs',
						title : '规格',
						width : '9%'
					},{
						field : 'inNum',
						title : '入库数量',
						editor : {
							type : 'numberbox',
							options : {
								required : true
							}
						},
						width : '6%'
					}, {
						field : 'minUnit',
						title : '单位',
						width : '9%'
					}, {
						field : 'inPrice',
						title : '购入价',
						width : '9%'
					}, {
						field : 'inCost',
						title : '入库金额',
						width : '9%'
					},
//					  {
//						field : 'invoiceNo',
//						title : '发票号',
//						editor : {
//							type : 'textbox',
//							options : {
//								required : true
//							},
//							missingMessage : '请填写发票号'
//						},
//						width : '12%'
//					}, 
						{
						field : 'batchNo',
						title : '批号',
						editor : {
							type : 'textbox',
							options : {
								required : true
							}
						},
						width : '9%'
					},{
						field : 'validDate',
						title : '有效期',
						width : '9%'
					}, {
						field : 'companyName',
						title : '供货公司',
						width : '9%'
					},{
						field : 'companyCode',
						title : '供货公司',
						hidden : true
					},{
						field : 'memo',
						title : '备注',
						editor : {
							type : 'textbox'
						},
						width : '12%'
					}, {
						field : 'highvalueBarcode',
						title : '高值耗材条形码',
						width : '9%'
					}, {
						field : 'packUnit',
						title : '包装单位',
						hidden : true
					} , {
						field : 'kindCode',
						title : '科目编码',
						hidden : true
					} ] ],
					onSelect:function(rowIndex, rowData){
						$('#infolist').datagrid('endEdit',rowIndex);
						$('#infolist').datagrid('beginEdit',rowIndex);
						var ed = $('#infolist').datagrid('getEditor', {index:rowIndex,field:'inNum'});
						var t = $(ed.target).numberbox('getText');
						$(ed.target).next("span").children().first().val("").focus().val(t);
						var inPrice = rowData.inPrice;
						$(ed.target).numberbox('textbox').bind('keyup', function(event) {
							var inNum = $(ed.target).numberbox('getText');
							var inCost = inNum*inPrice;
							$('#infolist').datagrid('updateRow',{
								index: rowIndex,
								row: {
									inCost: inCost,
									inNum:inNum
								}
							});
							$('#infolist').datagrid('selectRow',rowIndex);
						});
					}
				});
			});
			/**
			 * 药品列表查询
			 * @author  lt
			 * @param title 标签名称
			 * @param title 跳转路径
			 * @date 2015-06-19
			 * @version 1.0
			 */
			function searchFrom() {
				var queryName = $('#itemNameSerch').textbox('getValue');
				$('#list').datagrid('load', {
					itemName : queryName
				});
			}
			//保存
			function save() {
				var rows = $('#infolist').edatagrid('getSelections');
				if (rows.length > 0) {//选中几行的话触发事件	                        
					$('#infolist').datagrid('acceptChanges');//提交所有从加载或者上一次调用acceptChanges函数后更改的数据。
					var info = $('#infolist').edatagrid("getSelections");
					$('#infoJson').val(JSON.stringify($('#infolist').edatagrid("getSelections")));
					$('#saveForm').form('submit',{
						url : "<%=basePath%>material/instore/saveMatInput.action",
						onSubmit : function() {
							return $(this).form('validate');
						},
						success : function(data) {
							$('#list').edatagrid("reload");
							var arr = [];
							for ( var i = 0; i < rows.length; i++) {
								var dd = $('#infolist').edatagrid(
										'getRowIndex', rows[i]);//获得行索引
								if (arrIds.indexOf(rows[i].id) != -1) {
									arrIds.remove(rows[i].id);
								}
								arr.push(dd);
							}
							for ( var i = arr.length - 1; i >= 0; i--) {
								$('#infolist').edatagrid('deleteRow', arr[i]);//通过索引删除该行
							}
							$.messager.alert('提示',"保存成功！");
						},
						error : function(data) {
							$.messager.alert('提示',"保存失败！");
						}
				 });
				} else {
					$.messager.alert("操作提示", "请选择要保存的条目！", "warning");
				}
			}
			//删除
			function del() {
				var rows = $('#infolist').datagrid("getChecked");
				if (rows.length > 0) {
					var arr = [];
					$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
						if (res) {
							for ( var i = 0; i < rows.length; i++) {
								var dd = $('#infolist').edatagrid('getRowIndex',
										rows[i]);//获得行索引
								if (arrIds.indexOf(rows[i].id) != -1) {
									arrIds.remove(rows[i].id);
								}
								arr.push(dd);
							}
							for ( var i = arr.length - 1; i >= 0; i--) {
								$('#infolist').edatagrid('deleteRow', arr[i]);//通过索引删除该行
							}
						}
					});
				} else {
					$.messager.alert("操作提示", "请选择要删除的条目！", "warning");
				}
			}
			//format供货商
			function formatCompany(value){
				$.ajax({
					url : "<c:url value='/drug/supply/queryCompanyByCode.action'/>",
					data:{"code" : value},
					type:'post',
					success: function(data) {
						return data;
					}
				});
			}
			
			/**
			* 回车弹出供货单位选择窗口
			* @author  zhuxiaolu
			* @param textId 页面上commbox的的id
			* @date 2016-03-22 14:30   
			* @version 1.0
			*/

			function popWinToCompany(){
				var tempWinPath = "<%=basePath%>popWin/popWinSupplyCompany/toDrugSupplycompanyPopWin.action?textId=queryCompanyCode";
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -900) +',height='+ (screen.availHeight-370)+',scrollbars,resizable=yes,toolbar=yes')
			}
		</script>
	</body>
</html>