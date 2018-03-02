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
								data: [{ id: '01', value: '一般入库' },{ id: '02', value: '发票入库' },{id: '03', value: '核准入库' },{id: '04', value: '入库退货' },
									   {id: '05', value: '内部入库申请'},{id: '06', value: '内部入库核准'},{id: '07', value: '内部入库退库申请'},
									   {id: '08', value: '备货入库'},{id: '09', value: '备货退货',selected:true},{id: '10', value: '12.备货换货'},{id: '11', value: '特殊入库'},{id: '12', value: '特殊入库审核'}
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
							供货公司：
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
			<!-- 物资备货退货 -->
			<div data-options="region:'west',split:false,title:'物资列表',iconCls:'icon-book'" style="padding: 5px; width: 35% ;">
				<table cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td style="padding: 5px 15px;" nowrap="nowrap" colspan="2" >
							查询条件：
							<input id="startTime" type="text" class="easyui-datetimebox" data-options="showSeconds:false" style="width:180px"></input> 
							至
							<input id="endTime" type="text" class="easyui-datetimebox" data-options="showSeconds:false" style="width:180px"></input><br>
						</td>
					</tr>
					<tr>
						<td style="padding: 5px 15px;" nowrap="nowrap">
							供货公司：<input id="queryCompanyCode" style="width:180px"/> 
						</td>
						<td>
							物品：<input id="itemCode" type="text" class="easyui-combobox" data-options="valueField: 'id', textField: 'value', 
								data: [{ id: '-1', value: '全部' ,selected:true },{ id: '0', value: '申请'},{id: '1', value: '审核'},{id: '2', value: '核准' }]" style="width:180px"></input> 
						</td>
					</tr>
					
					<tr>
						<td style="padding: 5px 15px;" nowrap="nowrap">
							过滤条件：<input id="serchName" type="text" class="easyui-textbox" style="width:180px"></input> 
						</td>
						<td>
							&nbsp;&nbsp;
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</td>
					</tr>
				</table>
				<table id="list" data-options="url:'<%=basePath%>material/instore/queryMatInput.action?flg=3',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fitColumns:false">
					<thead>
						<tr>
							<th data-options="field:'inListCode',width : '25%'">
								入库单号
							</th>
							<th data-options="field:'inDate',width : '25%'">
								入库时间
							</th>
							<th data-options="field:'companyName',width : '20%'">
								供货公司
							</th>
							<th data-options="field:'inClass3',formatter:formatInListCode,width : '25%'">
								入库类型
							</th>
						</tr>
					</thead>
				</table>
			</div>
			<div data-options="region:'center',title:'待入库物资列表'" style="padding: 5px;">
				<form id="saveForm" method="post">
					<input type="hidden" name="infoJson" id="infoJson">
					<table id="infolist"
						data-options="url:'',method:'post',idField: 'stockCode',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar:'#toolbarId'">
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
				//bindEnterEvent('itemNameSerch',searchFrom,'easyui');
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
						$('#infolist').edatagrid({
							url:"<%=basePath%>material/instore/queryStockDetailVir.action?stockCode="+rowData.stockCode
						});
						setTimeout(function(){
							$('#infolist').edatagrid('selectRow',$('#infolist').edatagrid('getRowIndex',rowData.stockCode));
						},100);
						//$('#infolist').edatagrid('beginEdit',$('#infolist').edatagrid('getRowIndex',rowData.stockCode));
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
					},{
						field : 'id',
						title : 'id',
						hidden : true
					},{
						field : '库存流水号',
						title : 'stockCode',
						hidden : true
					},
					{
						field : 'itemCode',
						title : '物品编码',
						hidden : true
					}, {
						field : 'itemName',
						title : '物品名称',
						width : '6%'
					},{
						field : 'specs',
						title : '规格',
						width : '6%'
					},{
						field : 'inPrice',
						title : '购入价',
						width : '6%'
					}, {
						field : 'minUnit',
						title : '单位',
						width : '6%'
					},{
						field : 'inNum',
						title : '入库数量',
						width : '6%'
					}, {
						field : 'inCost',
						title : '入库金额',
						width : '6%'
					}
					,{
						field : 'storeNum',
						title : '可退数量',
						width : '6%'
					}
					,{
						field : 'returnedNum',
						title : '退库数量',
						editor : {
							type : 'numberbox',
							options : {
								required : true
							}
						},
						width : '6%'
					},{
						field : 'returnCost',
						title : '退库金额',
						width : '6%'
					},{
						field : 'memo',
						title : '备注',
						editor : {
							type : 'textbox'
						},
						width : '10%'
					}, {
						field : 'highvalueBarcode',
						title : '高值耗材条形码',
						width : '8%'
					}, {
						field : 'packUnit',
						title : '包装单位',
						hidden : true
					}  ] ],
					onSelect:function(rowIndex, rowData){
						$('#infolist').datagrid('endEdit',rowIndex);
						$('#infolist').datagrid('beginEdit',rowIndex);
						var ed = $('#infolist').datagrid('getEditor', {index:rowIndex,field:'returnedNum'});
						var t = $(ed.target).numberbox('getText');
						$(ed.target).next("span").children().first().val("").focus().val(t);
						var inPrice = rowData.inPrice;
						$(ed.target).numberbox('textbox').bind('keyup', function(event) {
							var returnedNum = $(ed.target).numberbox('getText');
							var returnCost = returnedNum*inPrice;
							$('#infolist').datagrid('updateRow',{
								index: rowIndex,
								row: {
									returnCost: returnCost,
									returnedNum:returnedNum
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
				var startTime = $('#startTime').datetimebox('getValue');
				var endTime = $('#endTime').datetimebox('getValue');
				var inState = $("#inState").combobox('getValue');
				var queryCompanyCode = $("#queryCompanyCode").combobox('getValue');
				var serchName = $("#serchName").textbox('getValue');
				$('#list').datagrid('load', {
					startTime : startTime,
					endTime:endTime,
					flg:inState,
					companyCode:queryCompanyCode,
					serchName:serchName
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
						url : "<%=basePath%>material/instore/returnMatChoice.action",
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
								$('#infolist').datagrid('endEdit',rowIndex);
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
			//format入库单号
			function formatInListCode(value,row,index){
				if(value!=null){
					if(value==01){
						return "一般入库";
					}
					if(value==02){
						return "发票入库";
					}
					if(value==03){
						return "核准入库";
					}
					if(value==04){
						return "入库退货";
					}
					if(value==05){
						return "内部入库申请";
					}
					if(value==06){
						return "内部入库核准";
					}
					if(value==07){
						return "内部入库退库申请";
					}
					if(value==08){
						return "备货入库";
					}
					if(value==09){
						return "备货退货";
					}
					if(value==10){
						return "备货换货";
					}
				}
			}
		</script>
	</body>
</html>