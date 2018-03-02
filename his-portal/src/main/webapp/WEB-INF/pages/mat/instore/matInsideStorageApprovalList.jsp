<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head></head>
	<body>
		<div>
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td style="padding: 5px 15px;" nowrap="nowrap" >
						操作类型：
						<input class="easyui-combobox" id="queryName" data-options="valueField: 'id', textField: 'value', 
							data: [{ id: '01', value: '一般入库' },{ id: '02', value: '发票入库' },{id: '03', value: '核准入库' },{id: '04', value: '入库退货' },
									   {id: '05', value: '内部入库申请'},{id: '06', value: '内部入库核准',selected:true},{id: '07', value: '内部入库退库申请'},
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
						&nbsp;&nbsp;目标单位：
						<input class="easyui-combobox" id="queryName" style="width:180px"/>
					</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="" class="easyui-linkbutton">切换库房</a>
					</td>
				</tr>
			</table>
		</div>
		<div id="layoutId" class="easyui-layout" data-options="fit:true"> 
			<div data-options="region:'west',title:'物资列表',collapsible:false" style="padding:5px; width:35% ;">
				<table style="width:100%;border:1px solid #95b8e7;padding:5px,5px,5px,5px;">
					<tr>
						<td>
							<input class="easyui-textbox " id="sName" data-options="prompt:'回车查询'" style="width: 200"/>
							&nbsp;&nbsp;&nbsp;<input id="sCheck" type="checkbox">&nbsp;模糊
							&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</td>
					</tr>
				</table>
				<div style="padding:5px,5px,5px,5px;">
					<table id="list" data-options="url:'${pageContext.request.contextPath}/material/instore/queryMatInsideStorageApproval.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true"></th>
								<th data-options="field:'applyListCode',width:100">单据号</th>
								<th data-options="field:'applyCost',width:150">出库金额</th>
								<th data-options="field:'saleCost',width:100">出库零售金额</th>
								<th data-options="field:'applyOper',width:100">申请人</th>
								<th data-options="field:'applyDate',width:100">申请时间</th>
								<th data-options="field:'examOper',width:100">审批人</th>
								<th data-options="field:'examDate',width:100">审批时间</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>   
			<div data-options="region:'center',title:'待入库物资列表'" style="">
				<table id="infolist" data-options="method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true"></th>
								<th data-options="field:'code',width:100">单号</th>
								<th data-options="field:'no',width:100">单内序号</th>
								<th data-options="field:'itemCode',width:100">物品编码</th>
								<th data-options="field:'itemName',width:100">物品名称</th>
								<th data-options="field:'specs',width:100">规格</th>
								<th data-options="field:'applyNum',width:100">申请数量</th>
								<th data-options="field:'applyCost',width:100">申请金额</th>
								<th data-options="field:'saleCost',width:100">申请零售金额</th>
								<th data-options="field:'inNum',width:100,editor:{type:'numberbox',options:{required:true}}">审批数量</th>
								<th data-options="field:'packQty',width:100">审批金额</th>
								<th data-options="field:'inPrice',width:100">审批零售金额</th>
								<th data-options="field:'applyPrice',width:100">购入价</th>
								<th data-options="field:'salePrice',width:100">零售价 </th>
								<th data-options="field:'memo',width:100,editor:{type:'textbox',options:{}}">备注</th>
								<th data-options="field:'highvalueBarcode',width:100">高值耗材条形码</th>
							</tr>
						</thead>
				</table>
			</div> 
			<div id="toolbarId">
				<shiro:hasPermission name="WZRK:function:save">
					<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">保存</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="WZRK:function:delete">
					<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
				</shiro:hasPermission>
			</div>
		</div>
		<script type="text/javascript">
		$(function(){
			$('#list').datagrid({
				pagination:true,
				pageSize:20,
				pageList:[20,30,50,80,100],
				onDblClickRow: function (rowIndex, rowData) {//双击赋值
					var rows = $('#infolist').datagrid('getRows');
					if(rows!=null&&rows!=''){
						for(var i=0;i<rows.length;i++){
							if(rows[i].itemCode==rowData.itemCode){
								$.messager.alert('提示','该物品已被添加！');	
								return;
							}
						}
					}
					var index = $('#infolist').datagrid('appendRow', {
						code : rowData.applyListCode,//单号
						no : rowData.applySerialNo,//单内序号
						itemCode : rowData.itemCode,//物品编码
						itemName : rowData.itemName,//物品名称
						specs : rowData.specs,//规格
						applyNum : rowData.applyNum,//申请数量
						applyCost : rowData.applyCost,//申请金额
						saleCost : rowData.saleCost,//申请零售金额
						inNum : 1,//审批数量
						packQty : rowData.packQty,//审批金额
						inPrice : rowData.inPrice,//审批零售金额
						applyPrice : rowData.applyPrice,//购入价
						salePrice : rowData.salePrice,//零售价
						memo : rowData.memo,//备注
						highvalueBarcode : rowData.highvalueBarcode//高值耗材条形码c
					}).edatagrid('getRows').length - 1;
					$('#infolist').edatagrid('selectRow', index);
					$('#infolist').edatagrid('beginEdit', index);
				}
			});
			$('#infolist').datagrid({
				pagination:false,
				onDblClickRow: function (rowIndex, rowData) {//双击赋值
					
				}
			});
			bindEnterEvent('sName',searchFrom,'easyui');//回车
		});
		//查询
		function searchFrom() {
			var queryName = $('#sName').textbox('getValue');
			$('#list').datagrid('load', {
				itemName : queryName
			});
		}
		//删除
		function del(){
			var rows = $('#infolist').datagrid('getChecked');
			if(rows!=null&&rows.length>0){
				for(var i=0;i<rows.length;i++){
					$('#infolist').datagrid('deleteRow',$('#infolist').datagrid('getRowIndex',rows[i]));
				}
			}else{
				$.messager.alert('提示','请选择要删除的物品！');	
			}
		}
		//保存
		function save(){
			var rows = $('#infolist').datagrid('getChecked');
			if(rows!=null&&rows.length>0){
				$.messager.defaults={
	    				ok:'确定',
	    				cancel:'取消',
	    				width:250,
	    				collapsible:false,
	    				minimizable:false,
	    				maximizable:false,
	    				closable:false
	    		};
				jQuery.messager.confirm("提示","确定保存所选核准信息？",function(event){
					if(event){
						$.post("<c:url value='/material/instore/queryMatInsideStorageApplyIsValidSave.action'/>",
				        {"jsonData":JSON.stringify(rows)},
				        function(data){
				        	var dataMap = eval("("+data+")");
				   			if(dataMap.resMsg=="error"){
				   				$.messager.alert('提示',dataMap.resCode);
				        		return;
				        	}else if(dataMap.resMsg=="success"){
				        		jQuery.messager.confirm("提示","保存成功，是否打印申请单？",function(event){
				        			if(event){
				        				$.messager.alert('提示','该功能正在建设中！','',function(){
				        					window.location.href="<c:url value='/material/instore/listMatInsideStorageApproval.action?menuAlias=WZRK'/>";
				        				});
				        			}else{
				        				window.location.href="<c:url value='/material/instore/listMatInsideStorageApproval.action?menuAlias=WZRK'/>";
				        			}
				        		});
				        	}else{
				        		$.messager.alert('提示','未知错误,请联系管理员!');
				        		return;
				        	}
				   		});
					}
				});
			}else{
				$.messager.alert('提示',"请选择需要保存的申请信息！");
				return;
			}
		}
		</script>
	</body>
</html>
