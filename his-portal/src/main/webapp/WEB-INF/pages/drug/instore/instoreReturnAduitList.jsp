<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药品内部入库退库审核</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
			//加载页面
			$(function() {
				//下拉框的keydown事件   调用弹出窗口
				
				bindEnterEvent('tradNameSerc',searchList,'easyui');//绑定回车事件
				bindEnterEvent('queryName',searchFrom,'easyui');//绑定回车事件
		
				//初始化目标科室下拉
				$('#deptList').combobox({
					url :"<%=basePath%>baseinfo/department/getDeptBySessionDept.action?loginDepteId="+$('#loginDept').val()+'&type=1',
					valueField : 'deptCode',
					textField : 'deptName',
					width : 120,
					onSelect:function(record){
						//清空待入库药品列表
						$('#infolist').edatagrid('loadData', { total: 0, rows: [] });
						var deptList = $('#deptList').combobox('getValue');
						$('#list').datagrid('load', {
							deptId : deptList
						});
					}
				});
				//添加datagrid事件及分页
				$('#list').datagrid({
					url:"<%=basePath%>drug/instore/queryInstoreReturnApply.action?menuAlias=${menuAlias}",
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 30, 50, 80, 100 ],
					onDblClickRow : function(rowIndex, rowData) {
						$('#infolist').edatagrid('load',{
							queryName:rowData.applyBillcode						
						})
						totalPrice();
						$("#appDeptId").val(rowData.deptCode);
						$("#totalDivId").show();
					},onLoadSuccess:function(row, data){
						$('#list').datagrid('uncheckAll'); 
						$('#list').datagrid('unselectAll');
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
		
				//添加datagrid事件及分页
				$('#infolist').edatagrid({
						url : "<%=basePath%>drug/instore/queryApplyDrugInfoByBill.action",
						striped : true,
						checkOnSelect : true,
						selectOnCheck : true,
						singleSelect : false,
						fitColumns : false,
						onLoadSuccess:function(row, data){
							$('#infolist').datagrid('uncheckAll'); 
							$('#infolist').datagrid('unselectAll');
						},
						columns : [ 
						            [{field : 'ck',checkbox : true},
						             {field : 'flagId',title : 'flagId',hidden : true,width:'0%',
						            	formatter:function(value,row,index){
							        		 return "<div id='"+row.drugCode+"'></div>";
							        	}
						             },
							         {field : 'id',title : '出库申请id',hidden : true},
							         {field : 'outBillCode',title : '出库单号',hidden : true},//出库流水号
							         {field : 'applyBillcode',title : '申请单号',hidden : true},
							         {field : 'applyOpercode',title : '申请人编码',hidden : true},
							         {field : 'drugCode',title : '药品code',hidden : true},
							         {field : 'tradeName',title : '药品通用名称',width : '20%'},
							         {field : 'specs',title : '规格',width : '10%'},
									 {field : 'retailPrice',title : '零售价',align: 'right',width : '7%',
											formatter : function(value, row, index) {
												return "<div id='retailPrice_"+index+"'>"
														+ row.retailPrice
														+ "</div>";
											}
							         },
									 {field : 'packUnit',title : '包装单位',width : '7%'},
									 {field : 'minUnit',title : '最小单位',width : '7%'},
									 {field : 'applyNum',title : '申请数量',width : '10%'}, //取的申请数量
									 {field : 'examNum',title : '审批数量',width : '10%',//审核数量 /**内部入库退库审核时的退回数量*/
											formatter : function(value, row, index) {
												var retVal = "";
												if (value != null && value != "" && !isNaN(value) && value >0) {
													retVal = value;
												}else{
													retVal = 0;
												}
												return "<input type='text' id='"
														+ row.id
														+ "_"
														+ index
														+ "' value='"
														+ retVal
														+ "' onChange = 'upperCase(this)'>";
											}
									},
									{field : 'totalReturnCost',title : '金额',align: 'right',width : '10%',
											formatter:function(value,row,index){
												        	if (value != null && value != "" && !isNaN(value) && value >0) {
																return value;
															}else{
																return 0;
															}
									        		}
									},
									 {field : 'mark',title : '备注',width : '17%',formatter : function(value, row, index) {
										 var val = "";
											if (value != null && value != "" && !isNaN(value) && value >0) {
												val = value;
											}else{
												val = '';
											}
											return "<input type='text' id='"
													+ row.drugCode
													+ "_"
													+ index
													+ "' value='"
													+ val
													+ "' onChange = 'updateMark(this)'>";
										  }
								    }] ]
						});
				});
			/**
			 * 批量提交列表信息
			 * @author  lt
			 * @date 2015-07-31
			 * @version 1.0
			 */
			function addForm() {
				var rows = $('#infolist').edatagrid("getSelections");
				if (rows.length > 0) {
					for(var i=0; i<rows.length; i++){
						if(isNaN(rows[i].examNum)|| rows[i].examNum <= 0){
							$.messager.alert('提示','药品【'+rows[i].tradeName+'】审批数量不能为空或者零,如不操作,请取消勾选!');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							   return;
						}
					}
					$('#infoJson').val(JSON.stringify(rows));
					$('#saveForm').form('submit',{
						url : "<%=basePath%>drug/instore/saveExamine.action",
						onSubmit : function() {
							if (!$(this).form('validate')) {
								$.messager.alert('提示',"验证没有通过,不能提交表单!");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								return false;
							}
							$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
						},
						success : function(data) {
							$.messager.progress('close');
							$.messager.alert('提示',data);
							$('#list').edatagrid("reload");
							$('#infolist').edatagrid('loadData', { total: 0, rows: [] });
							//更新一下总金额
							totalPrice();
						},
						error : function(data) {
							$.messager.progress('close');
							$.messager.alert('提示',"保存失败！");
						}
					});
				}else{
					$.messager.alert("操作提示", "请选择要保存的条目！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
			//onkeyUp触发的函数
			function upperCase(obj) {
				var val = $(obj).val();
				var index = $(obj).prop("id").split("_");
				var i = parseInt(index[1]);
				var retailPrice = $('#retailPrice_' + i).text();
				var totalretailPrice = retailPrice * val;
				var rowsInfolist = $('#infolist').datagrid('getRows');
				var applyNum = rowsInfolist[i].applyNum;
				var tradeName = rowsInfolist[i].tradeName;
				if(parseFloat(applyNum) < parseFloat(val)){
					$.messager.alert("操作提示", "药品【 "+tradeName+"】 审批数量大于申请出库数量，请核对后进行申请！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					var index = $('#infolist').datagrid('updateRow', {
						index : i,
						row : {
							examNum : 0,
							totalReturnCost : 0
						}
					}).edatagrid('getRows').length - 1;
					$('#infolist').edatagrid('beginEdit', index);
					//更新一下总金额
					totalPrice();
					return false;
				}
				var index = $('#infolist').datagrid('updateRow', {
					index : i,
					row : {
						examNum : val,
						totalReturnCost : totalretailPrice.toFixed(4)
					}
				}).edatagrid('getRows').length - 1;
				$('#infolist').edatagrid('beginEdit', index);
				//更新一下总金额
				totalPrice();
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
				 var queryName = $('#queryName').textbox('getValue');
					var deptList = $('#deptList').combobox('getValue');
					$('#list').datagrid('load', {
						deptId : deptList,
						queryName : queryName,
				});
			}

		
			//回车事件
			function KeyDown() {
				if (event.keyCode == 13) {
					event.returnValue = false;
					event.cancel = true;
					searchFrom();
				}
			}
		
			
			//js克隆对象 
			function cloneObject(obj) {
				var o = obj.constructor === Array ? [] : {};
				for ( var i in obj) {
					if (obj.hasOwnProperty(i)) {
						o[i] = typeof obj[i] === "object" ? cloneObject(obj[i])
								: obj[i];
					}
				}
				return o;
			}
			//重置查询条件
			function reset(){
				$("#tradNameSerc").textbox('setValue','');
				searchList();
			}
			//页面查询检索
			var arr = [];
			function searchList() {
				var rows = $('#infolist').datagrid('getRows');
				var queryName = $("#tradNameSerc").textbox('getValue');
				if (queryName == null || queryName == "") {
					//显示全部
					for ( var i = 0; i < arr.length; i++) {
						arr[i].show();
					}
					arr.length = 0;
				} else {
					for ( var i = 0; i < rows.length; i++) {
						var namestr = rows[i].tradeName;
						var searchItem=namestr.indexOf(queryName) == -1 ;
						//不匹配数据，执行删除
						if (searchItem) {
							$("#" + rows[i].drugCode).parent().parent().parent().hide();
							arr.push($("#" + rows[i].drugCode).parent().parent().parent());
						}else{
							$("#" + rows[i].drugCode).parent().parent().parent().show();
						}
					}
				}
				//计算总的申请金额
				totalPrice();
			}
			//计算总的退回金额
			function totalPrice() {
				var rows = $('#infolist').datagrid('getRows');
				var price = 0;
				for ( var i = 0; i < rows.length; i++) {
					var id = rows[i].drugCode;
					if (!$('#' + id).parent().parent().parent().is(":hidden")) {//判断是否隐藏
						if(rows[i].totalReturnCost==null || rows[i].totalReturnCost==""){
							price += 0;
						}else{
							price += parseFloat(rows[i].totalReturnCost);
						}
					}
				}
				if(!isNaN(price) && price >= 0){
					price = price.toFixed(2);
				}else{
					price = '';
				}
				$("#totalPrice").html(price);
			}
			//删除待入库列表
			function delList() {
				var rows = $('#infolist').datagrid("getChecked");
				if (rows.length > 0) {
					$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
						if (res) {
							var arr = [];
							for ( var i = 0; i < rows.length; i++) {
								var dd = $('#infolist').edatagrid('getRowIndex',
										rows[i]);//获得行索引
								arr.push(dd);
							}
							for ( var i = arr.length - 1; i >= 0; i--) {
								$('#infolist').edatagrid('deleteRow', arr[i]);//通过索引删除该行
							}
							//更新一下总金额
							totalPrice();
						}
					});
				} else {
					$.messager.alert("操作提示", "请选择要删除的条目！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
			//导出列表
			function exportList() {
				var rows = $('#infolist').edatagrid("getRows");
				if(rows.length==0){
					$.messager.alert("操作提示", "列表无数据,无法导出!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
				$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
					if (res) {
						//为了结束编辑行 不然获取的rows里没有编辑状态下的数据
						for ( var i = 0; i < rows.length; i++) {
							$('#infolist').edatagrid("endEdit",
									$('#infolist').edatagrid("getRowIndex", rows[i]));
						}
						var copyRows = cloneObject(rows);
						for ( var i = 0; i < rows.length; i++) {
							var id = rows[i].id;
							if ($('#' + id).parent().parent().parent().is(":hidden")) {//判断是否隐藏
								copyRows.remove(copyRows[i]);
							}
						}
						$('#infoJson').val(JSON.stringify(copyRows));
						$('#saveForm').form('submit', {
							url :"<%=basePath%>drug/applyout/exportInReturnExem.action",
							onSubmit : function() {
								return $(this).form('validate');
							},
							success : function(data) {
								$.messager.alert("操作提示", "导出成功！", "success");
							},
							error : function(data) {
								$.messager.alert("操作提示", "导出失败！", "error");
							}
						});
					}
				});
			}
			
			//备注输入框改变事件
			function updateMark(obj){
				var id = $(obj).prop("id").split("_");
				var index = parseInt(id[1]);
				$('#infolist').datagrid('updateRow', {
					index : index,
					row : {
						mark : $(obj).val()
					}
				});
			}
			
			// 药品列表查询重置
			function searchReload() {
				$('#deptList').combobox('setValue','');
				$('#queryName').textbox('setValue','');
				searchFrom();
			}
		</script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
	<body>
		<!-- 药品内部入库退库审核 -->
		<div id="divLayout" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'west',split:false,iconCls:'icon-book'" style=" width: 35%;border-top:0;">
				<div id="divLayout" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',split:false"  style="border-top:none; border-right:none; border-bottom: 1px solid #95b8e7;" class="changeskinBottom">
						<table cellspacing="0" cellpadding="0" border="0">
							<tr>
								<td style="padding: 6px;font-size: 14px" nowrap="nowrap">
									<input type="hidden" value="${loginDeptId}" id="loginDept">
									&nbsp;申请部门：
									<input class="easyui-combobox" type="text" ID="deptList" />
								</td>
								<td nowrap="nowrap" >
									&nbsp;申请单号：
									<input class="easyui-textbox" id="queryName" onkeydown="keyDown()" style="width:120px"/>
								</td>
								<td>
									<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search"style="width: 65px ;">查询</a>
								</shiro:hasPermission>
								<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',split:false,iconCls:'icon-book'" style="width: 80%;height: 100%;border-top:none; border-right:none; border-bottom: 1px solid #95b8e7;" class="changeskinBottom">
						<table id="list"
							data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fitColumns:false,fit:true">
							<thead>
								<tr>
									<th data-options="field:'applyBillcode',width : '38%' ">
									申请单号
								</th>
								<th data-options="field:'drugDeptCode',hidden:'true'">
									出库部门
								</th>
								<th data-options="field:'drugDeptName',width : '30%' ">
									出库部门
								</th>
								<th data-options="field:'deptCode',hidden:'true' ">
									申请部门
								</th>
								<th data-options="field:'deptName',width : '30%' ">
									申请部门
								</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			<div data-options="region:'center'" style="border-top:0;">
				<div id="divLayout" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',split:false"  style="height: 35px;border-top:none; border-left:none; border-bottom: 1px solid #95b8e7;" class="changeskinBottom">
						<input id="drugCode" type="hidden">
						<table cellspacing="0" cellpadding="0" border="0">
							<tr>
								<td style="padding: 5px 15px;font-size: 14px" nowrap="nowrap">
									查询条件：
									<input class="easyui-textbox" id="tradNameSerc" data-options="prompt:'通用名称'" style="width:180px"/>
								</td>
								<td>
									<a href="javascript:void(0)" onclick="searchList()"class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</td>
								<td>
									<a href="javascript:void(0)" onclick="reset()"class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',split:false,iconCls:'icon-book'" style="width: 100%;border-top:none; border-left:none; border-bottom: 1px solid #95b8e7;" class="changeskinBottom">
						<form id="saveForm" method="post" style="height: 92%">
							<input type="hidden" name="infoJson" id="infoJson" value="${infoJson}">
							<input type="hidden" name="appDeptId" id="appDeptId">
							
							<table id="infolist"
								data-options="fit:true,method:'post',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar:'#toolbarId'">
								
							</table>
						</form>
					</div>
				</div>
			</div>
			<div id="toolbarId">
				<shiro:hasPermission name="${menuAlias}:function:save">
					<a id="btnAddUser" href="javascript:void(0)"
						class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true"
						onclick="addForm()">保存</a>
				</shiro:hasPermission>
					<a id="btnAddUser" href="javascript:void(0)"
						class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true"
						onclick="delList()">删除</a>
				<shiro:hasPermission name="${menuAlias}:function:export">
					<a id="btnAddUser" href="javascript:void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:true" onclick="exportList()">导出</a>
				</shiro:hasPermission>
				&nbsp;&nbsp;总申请金额：<span ID="totalPrice" style="font-weight: bold;color: red;"></span>	
				<br>
			</div>
		</div>
	</body>
</html>