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
								data: [{ id: '01', value: '一般入库'},{ id: '02', value: '发票入库' },{id: '03', value: '核准入库' },{id: '04', value: '入库退货' },
									   {id: '05', value: '内部入库申请'},{id: '06', value: '内部入库核准'},{id: '07', value: '内部入库退库申请'},
									   {id: '08', value: '备货入库'},{id: '09', value: '备货退货'},{id: '10', value: '备货换货'},{id: '11', value: '特殊入库',selected:true },{id: '12', value: '特殊入库审核'}
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
						</td>
						<td>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="javascript:void(0)" onclick="" class="easyui-linkbutton">切换库房</a>
						</td>
					</tr>
				</table>
		</div>
		<div id="divLayout" class="easyui-layout" fit=true>
			<!-- 物资特殊入库审核 -->
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
							<th data-options="field:'id',hidden:true">
								Id
							</th>
							<th data-options="field:'itemName',width : '25%'">
								物品名称
							</th>
							<th data-options="field:'customCode',width : '12%'">
								自定义码
							</th>
							<th data-options="field:'gbCode',width : '10%'">
								国家码
							</th>
							<th data-options="field:'otherName',width : '25%'">
								别名
							</th>
							<th data-options="field:'minUnit',width : '10%',formatter:packUnitFamater">
								最小单位
							</th>
							<th data-options="field:'inPrice',width : '15%'">
								最新入库单价
							</th>
							<th data-options="field:'salePrice',width : '12%'">
								零售价格
							</th>
							<th data-options="field:'packUnit',width : '15%',formatter:packUnitFamater">
								大包装单位
							</th>
							<th data-options="field:'packQty',width : '15%'">
								大包装数量
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
					<a href="javascript:void(0)" onclick="save()"class="easyui-linkbutton"data-options="iconCls:'icon-add',plain:true">保存</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="WZRK:function:delete">
					<a href="javascript:void(0)" onclick="delList()"class="easyui-linkbutton"data-options="iconCls:'icon-remove',plain:true">删除</a>
				</shiro:hasPermission>
			</div>
		</div>
		<script type="text/javascript">
			//记录赋值过的条目id
			var arrIds = [];
			//格式化所用
			var packUnitList = "";
			var totalRows = "";
			//加载页面
			$(function() {
				//绑定回车事件
				bindEnterEvent('itemNameSerch',searchFrom,'easyui');
				//初始化供货单位下拉
				$('#queryCompanyCode').combobox({
					width : 180,
					disabled: true
				});
				$.ajax({
					url:"<c:url value='/comboBox.action'/>", 
					data:{"str" : "CodeDrugpackagingunit"},
					type:'post',
					success: function(packUnitdata) {
						packUnitList = eval("("+ packUnitdata +")");
					}
				});
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
						attrValue(rowData);
					}
				});
				$('#infolist').edatagrid({
					striped : true,
					checkOnSelect:true,
					selectOnCheck:true,
					singleSelect:false,
					fitColumns : false,
					columns : [ [ 
					{field:'ck',checkbox:true},
					{
						field : 'inState',
						title : '核准',
						width : '8%',
						editor : {
							type : 'combobox',
							options:{
								valueField:'id',
								textField:'text',
								data:[{"id":2,"text":"是"},{"id":1,"text":"否"}]//1-正式入库,2-核准入库
							}
						}
					},{
						field : 'itemCode',
						title : '物品编码',
						hidden : true
					}, {
						field : 'matBaseinfoId',
						title : '物资主键',
						hidden : true
					},{
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
						width : '9%',
						formatter:packUnitFamater
					}, {
						field : 'inPrice',
						title : '购入价',
						width : '9%'
					}, {
						field : 'inCost',
						title : '入库金额',
						width : '9%'
					},{
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
						field : 'companyName',
						title : '供货公司',
						width : '9%'
					},{
						field : 'produceDate',
						title : '生产日期',
						width : '10%',
						editor : {
							type : 'datebox'
						}
					},{
						field : 'validDate',
						title : '有效期',
						width : '10%',
						editor : {
							type : 'datebox'
						}
					},{
						field : 'invoiceNo',
						title : '发票号',
						editor : {
							type : 'textbox',
							options : {
								required : true
							}
						},
						width : '10%'
					},{
						field : 'invoiceDate',
						title : '发票日期',
						editor : {
							type : 'datebox'
						},
						width : '10%'
					},{
						field : 'salePrice',
						title : '零售价',
						width : '9%'
					},{
						field : 'saleCost',
						title : '零售金额',
						width : '6%'
					},{
						field : 'detectFlag',
						title : '是否有检测报告',
						width : '10%',
						editor : {
							type : 'combobox',
							options:{
								required : true,
								valueField:'id',
								textField:'text',
								data:[{"id":1,"text":"是"},{"id":0,"text":"否"}]
								}
							}
					},
					{
						field : 'companyCode',
						title : '供货公司',
						hidden : true
					},{
						field : 'factoryCode',
						title : '生产厂商',
						hidden : true
					},{
						field : 'memo',
						title : '备注',
						editor : {
							type : 'textbox'
						},
						width : '12%'
					},  {
						field : 'packUnit',
						title : '包装单位',
						hidden : true
					} ,  {
						field : 'packQty',
						title : '大包装包装数量',
						hidden : true
					},  {
						field : 'packPrice',
						title : '大包装价格',
						hidden : true
					},{
						field : 'highvalueFlag',
						title : '高值耗材标志(0－否,1－是)',
						hidden : true
					},{
						field : 'addRate',
						title : '加价规则',
						hidden : true
					},{
						field : 'financeFlag',
						title : '财务标志',
						hidden : true
					},{
						field : 'kindCode',
						title : '物品科目编码',
						hidden : true
					},{
						field : 'privStoreNum',
						title : '入库前库存量',
						hidden : true
					},{
						field : 'returnNum',
						title : '已退数量',
						hidden : true
					}
					] ],
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
									inState:rowData.inState,
									inCost: inCost,
									inNum:inNum,
									saleCost:inNum*rowData.salePrice
								}
							});
							$('#infolist').datagrid('selectRow',rowIndex);
						});
						
					}
				});
			});
			//判断是否已经填充到待入库列表
			function attrValue(row){
				var rows = $('#infolist').datagrid("getRows");
				var tag=row.id;
				if(rows.length>0){
					if(arrIds.indexOf(tag) == -1){
						conform(row);
						arrIds.push(tag);
					}else{
						$.messager.alert("操作提示", "列表已有此条记录！","warning");
					}
				}else{
					conform(row);
					if(arrIds.indexOf(tag)==-1){
						arrIds.push(tag);
					}
				}
			}
			//确认把数据填充到待ru库列表
			function conform(row){
				var row = $('#list').datagrid('getSelected');
				if (row) {
					var index = $('#infolist').edatagrid('appendRow', {
						matBaseinfoId:row.id,
						itemCode : row.itemCode,
						itemName : row.itemName,
						specs : row.specs,
						minUnit : row.minUnit,
						packUnit : row.packUnit,
						inPrice : row.inPrice,
						companyCode : row.companyCode,
						companyName : row.companyName,
						factoryCode : row.factoryCode,
						highvalueFlag:row.highvalueFlag,
						financeFlag:row.financeFlag,
						salePrice:row.salePrice,
						packQty:row.packQty,
						packPrice:row.packPrice,
						addRate:row.addRate,
						kindCode:row.kindCode,
						privStoreNum:0,
						returnNum:0
					}).edatagrid('getRows').length - 1;
					$('#infolist').edatagrid('selectRow', index);
				}
	         	var rows = $('#infolist').datagrid("getRows");
				totalRows = cloneObject(rows);
			}
			function cloneObject(obj){
				var o = obj.constructor === Array ? [] : {};
				for(var i in obj){
					if(obj.hasOwnProperty(i)){
						o[i] = typeof obj[i] === "object" ? cloneObject(obj[i]) : obj[i];
					}
				}
				return o;
			}
			//移除待出库列表
			function delList(){
				var rows = $('#infolist').datagrid("getChecked");
				if(rows.length>0){
					var arr=[];
					for(var i=0; i<rows.length; i++){
						var dd = $('#infolist').edatagrid('getRowIndex',rows[i]);//获得行索引
						if(arrIds.indexOf(rows[i].matBaseinfoId)!=-1){
							arrIds.remove(rows[i].matBaseinfoId);
						}
						arr.push(dd);
					}
					for(var i=arr.length-1;i>=0;i--){
						$('#infolist').edatagrid('deleteRow',arr[i]);//通过索引删除该行
					}
				}else{
					$.messager.alert("操作提示", "请选择要删除的条目！","warning");
				}
			}
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
					$('#infoJson').val(JSON.stringify($('#infolist').edatagrid("getRows")));
					$('#saveForm').form('submit',{
						url : "<%=basePath%>material/instore/saveSpecMat.action",
						onSubmit : function() {
							return $(this).form('validate');
						},
						success : function(data) {
							window.location="<%=basePath%>material/instore/listMatSpecial.action";
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
			//显示包装单位格式化
			function packUnitFamater(value){
				if(value!=null){
					for(var i=0;i<packUnitList.length;i++){
						if(value==packUnitList[i].id){
							return packUnitList[i].name;
						}
					}	
				}
			}
		</script>
	</body>
</html>