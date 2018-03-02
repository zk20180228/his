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
		<!-- 药品内部入库退库申请 -->
		
		<div id="divLayout" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'west'" style="width:40%;border-top:0;">
		    <div class="easyui-layout" style="width:100%;height:100%;">   
			    <div  class="easyui-layout" data-options="region:'north',border:false" style="height:50%;" >
					<div  id="divLayout" data-options="region:'north',border:false" style="height: 35px;">
						<table cellspacing="0" cellpadding="0"  style="height: 100%;width: 100%;border-top:none; border-left:none; border-right:none; border-bottom: 1px solid #95b8e7;" class="changeskinBottom"> 
							<tr>
								<td nowrap="nowrap">
									<input type="hidden" value="${loginDeptId}" id="loginDept">
									出库部门：
									<input class="easyui-combobox" type="text" ID="deptList" style="width:100px"/>
									&nbsp;申请单号：
									<input class="easyui-textbox" id="queryName"  style="width:100px"/>
									<shiro:hasPermission name="${menuAlias}:function:query">
									&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',split:false,iconCls:'icon-book',border:false," style="width: 100%;height: 100%;border-bottom:1px solid #95b8e7;" class="changeskinBottom">
						<table id="list"
							data-options="rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fitColumns:false,fit:true">
							<thead>
								<tr>
									<th data-options="field:'outListCode',width : '33%' ">
										申请单号
									</th>
									<th data-options="field:'drugDeptName',width : '33%' ">
										出库部门
									</th>
									<th data-options="field:'drugDeptCode',hidden:'true'">
										出库部门
									</th>
									<th data-options="field:'applyDate',width : '32%'" >
										申请出库日期
									</th>
									
								</tr>
							</thead>
						</table>
					</div>
				</div> 
			    <div  class="easyui-layout" data-options="region:'center',border:false"style="width:100%;height:50%;" >
					<div style="width:100%;height:90%"> 
						<table id="outlist" cellspacing="0" cellpadding="0" border="0"
							data-options="fit:true,method:'post',striped:true,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false">
						</table>
					</div>
				</div>
			</div>  
	    </div>   
			<div data-options="region:'center'" style="width: 60%;border-top:0;">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',split:false" style="height: 35px;border-top:none; border-left:none; border-bottom: 1px solid #95b8e7;" class="changeskinBottom">
						<input id="drugCode" type="hidden">
						<table cellspacing="0" cellpadding="0" border="0">
							<tr>
								<td style="padding: 5px 15px;font-size: 14px" nowrap="nowrap">
									查询条件：
									<input class="easyui-textbox" id="tradNameSerc" data-options="prompt:'药品通用名'" style="width:180px"/>
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
					<div data-options="region:'center',split:false,iconCls:'icon-book',border:false" style="width: 100%;">
						<form id="saveForm" method="post" style="height: 92%">
							<input type="hidden" name="infoJson" id="infoJson">
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
					<a id="btnAddUser" href="javascript:void(0)"
						class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:true"
						onclick="exportList()">导出</a>
				</shiro:hasPermission>
				&nbsp;&nbsp;总申请金额：<span ID="totalPrice" style="font-weight: bold;color: red;"></span>			
			</div>
		</div>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
		<script type="text/javascript">
			//定义全局变量记录总的待入库信息条目
			var totalRows = null;
			//记录赋值过的条目id
			var arrIds = [];
			//科室渲染所用
			//加载页面
			$(function() {
				bindEnterEvent('queryName',searchFrom,'easyui');
				bindEnterEvent('tradNameSerc',searchList,'easyui');
				//初始化目标科室下拉
				$('#deptList').combobox({
					url : "<%=basePath%>baseinfo/department/getDeptBySessionDept.action?loginDepteId="+$('#loginDept').val()+"&type=2",
					valueField : 'deptCode',
					textField : 'deptName',
					width : 170,
					onSelect:function(record){
						var deptList = $('#deptList').combobox('getValue');
						var queryName = $('#queryName').textbox('getValue');
						$('#list').datagrid('load', {
							deptId : deptList,
							queryName : queryName
						});
						//清空可编辑列表
						$('#infolist').datagrid('loadData',{total:0,rows:[]});					
						//清空存放已选择药品id的数组
						arrIds.splice(0,arrIds.length);
						$('#outlist').edatagrid('loadData',{total:0,rows:[]});	
					}
				}); 
				//添加datagrid事件及分页applyBillcode
				$('#list').datagrid({
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 30, 50, 80, 100 ],
					url:"<%=basePath%>drug/instore/queryInstoreBillCode.action?menuAlias=${menuAlias}",
					queryParams:{
						"deptId" : $('#deptList').combobox('getValue')
					},
					onDblClickRow : function(rowIndex, rowData) {
						$('#outlist').edatagrid('load',{
							queryName : rowData.outListCode
						})
						//清空可编辑列表
						$('#infolist').datagrid('uncheckAll');
						$('#infolist').datagrid('unselectAll');
						$('#infolist').datagrid('loadData',{total:0,rows:[]});
						$('#infolist').datagrid('acceptChanges');//提交所有从加载或者上一次调用acceptChanges函数后更改的数据。
						//清空存放已选择药品id的数组
						arrIds.splice(0,arrIds.length);
						$('#infoJson').val('');
						},
					onLoadSuccess:function(row, data){
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
		
				//添加datagrid事件及分页    //需要提交的列表
				$('#infolist').edatagrid({
					striped : true,
					checkOnSelect : true,
					selectOnCheck : true,
					singleSelect : false,
					fitColumns : false,
					idField : 'drugCode',
					columns : [ 
					            [{field : 'ck',checkbox : true},
					             {field : 'id',hidden : true},
					             {field : 'drugCode',title : '药品code',hidden : true},
					             {field : 'flagId',title : 'flagId',hidden : true,width:'0%',
					            	formatter:function(value,row,index){
						        		 return "<div id='"+row.drugCode+'_'+index+"'></div>";
						        	}
					             },
					             {field : 'groupCode',title : '批次号',hidden : true},
					             {field : 'outListCode',title : '出库单据号',hidden : true},
					             {field : 'outBillCode',title : '出库单流水号',hidden : true},
						         {field : 'drugCode',title : '药品code',hidden : true},
						         {field : 'drugDeptCode',title : '出库部门',hidden : true},
						         {field : 'tradeName',title : '药品通用名称',width : '20%'},
						         {field : 'specs',title : '规格',width : '10%'},
								 {field : 'retailPrice',title : '零售价',align: 'right',width : '7%',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}},
								 {field : 'packUnit',title : '包装单位',width : '8%'},
								 {field : 'minUnit',title : '最小单位',width : '8%'},
								 {field : 'outNum',title : '可退数量',width : '8%'}, 
								 {field : 'returnNum',title : '申请数量',width : '10%',
										formatter : function(value, row, index) {
											var retVal = "";
											if (value != null && value != "" && !isNaN(value) && value >0) {
												retVal = value;
											}else{
												retVal = 0;
											}
											return "<input type='text' id='"
													+ row.drugCode
													+ "' value='"
													+ retVal
													+ "' onChange = 'upperCase(this)'>";
										}
								},
								{field : 'totalReturnCost',title : '申请金额',align: 'right',width : '10%',
										formatter:function(value,row,index){
											        	if (value != null && value != "" && !isNaN(value) && value >0) {
															return value;
														}else{
															return 0;
														}
								        		}
								},
								 {field : 'remark',title : '备注',width : '17%',formatter : function(value, row, index) {
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
				
				//添加datagrid事件及分页
				$('#outlist').edatagrid({
					url : "<%=basePath%>drug/instore/queryDrugInfoByBill.action",
					striped : true,
					checkOnSelect : true,
					selectOnCheck : true,
					singleSelect : false,
					fitColumns : false,
					columns : [ 
					            [{field : 'ck',checkbox : true},
					             {field : 'flagId',title : 'flagId',hidden : true,width:'0%',
					            	formatter:function(value,row,index){
						        		 return "<div id='"+row.drugCode+"'></div>";
						        	}
					             },
						         {field : 'drugCode',title : '药品code',hidden : true},
						         {field : 'groupCode',title : '批次号',hidden : true},
						         {field : 'outListCode',title : '出库单据号',hidden : true},
						         {field : 'drugDeptCode',title : '出库部门',hidden : true},
						         {field : 'tradeName',title : '药品通用名称',width : '30%'},
						         {field : 'specs',title : '规格',width : '11%'},
								 {field : 'retailPrice',title : '零售价',align: 'right',width : '13%',
										formatter : function(value, row, index) {
											if(row.retailPrice!=null){
												return "<div id='retailPrice_"+index+"'>"
														+ row.retailPrice.toFixed(4)
														+ "</div>";
											}else{
												return "<div id='retailPrice_"+index+"'>"
													+ row.retailPrice
													+ "</div>";
											}
										}
						         },
								 {field : 'packUnit',title : '包装单位',width : '12%'},
								 {field : 'minUnit',title : '最小单位',width : '12%'},
								 {field : 'outNum',title : '可退数量',width : '20%'}
								] ]
					,toolbar: [{
			               id: 'btnAdd',
			               text: '添加',
			               iconCls: 'icon-add',
			               handler: function () {
			            	   var rows = $('#outlist').edatagrid("getSelections");
			            	   for(var i =0;i<rows.length;i++){
			            		   if (arrIds.indexOf(rows[i].drugCode) == -1) {
			            		    	attrValue(rows[i]);
			            		   }
			            	   }
			          	   }
			            }],
		            onDblClickRow : function(rowIndex, rowData) {
							attrValue(rowData);
					}
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
				if (rows.length > 0) {//选中几行的话触发事件	
					for(var i=0; i<rows.length; i++){
						if(isNaN(rows[i].returnNum)|| rows[i].returnNum <= 0){
							$.messager.alert('提示','药品【'+rows[i].tradeName+'】审批数量不能为空或者零,如不操作,请取消勾选!');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							   return;
						}
					}
					$('#infolist').datagrid('acceptChanges');//提交所有从加载或者上一次调用acceptChanges函数后更改的数据。
					$('#infoJson').val(JSON.stringify(rows));
					var deptid = $('#deptList').combobox('getValue')
					$('#saveForm').form('submit',{
						url : "<%=basePath%>drug/instore/saveReturn.action?deptId="+ $('#deptList').combobox('getValue'),
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
							data = eval("("+data+")");
							$.messager.alert('提示',data.resMesg);
							if(data.resCode == 'error'){
								return false;
							}
							//更新一下总金额
							totalPrice();
							$('#list').edatagrid('reload');
							$('#outlist').edatagrid('reload');
							var rows = $('#infolist').datagrid("getChecked");
							var arr = [];
							for ( var i = 0; i < rows.length ; i++) {
								var dd = $('#infolist').edatagrid('getRowIndex',
										rows[i]);//获得行索引
								if (arrIds.indexOf(rows[i].drugCode) != -1) {
									arrIds.remove(rows[i].drugCode);
								}
								arr.push(dd);
							}
							for ( var i = arr.length - 1; i >= 0; i--) {
								$('#infolist').edatagrid('deleteRow', arr[i]);//通过索引删除该行
							}
							$('#infolist').datagrid('uncheckAll'); 
							$('#infolist').datagrid('unselectAll')
							//更新一下总金额
							totalPrice();
						},
						error : function(data) {
							$.messager.progress('close');
							$.messager.alert('提示',"保存失败！");
						}
					});
				} else {
					$.messager.alert("操作提示", "请选择要保存的条目！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
			//判断是否填充到待入库列表
			function attrValue(row) {
				if (arrIds.indexOf(row.drugCode) > -1) {
					$.messager.alert("操作提示", "【"+row.tradeName+"】 已存在！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
				arrIds.push(row.drugCode);
				appendValue(row);
			}
			//为列表赋值
			function appendValue(row) {
				var totalRePrice = row.retailPrice * row.outNum;
				if (isNaN(totalRePrice)) {
					totalRePrice = "";
				}
				var index = $('#infolist').edatagrid('appendRow', {
					id : row.id,
					drugCode : row.drugCode,
					tradeName : row.tradeName,
					retailPrice : row.retailPrice,
					specs : row.specs,
					packUnit : row.packUnit,
					minUnit : row.minUnit,
					outNum : row.outNum,
					groupCode:row.groupCode,
					drugDeptCode :row.drugDeptCode,
					outListCode:row.outListCode,
					outBillCode:row.outBillCode,
				}).edatagrid('getRows').length - 1;
				$('#infolist').edatagrid('beginEdit', index);
				var rows = $('#infolist').datagrid("getRows");
				totalRows = cloneObject(rows);
				//计算总的申请金额
				totalPrice();
			}
			//onkeyUp触发的函数
			function upperCase(obj) {
				var val = parseFloat($(obj).val());
				var id = $(obj).prop("id");
				var i = $('#infolist').datagrid('getRowIndex',id);
				var rowsInfolist = $('#infolist').datagrid('getRows');
				var retailPrice = rowsInfolist[i].retailPrice;
				var totalretailPrice = retailPrice * val;
				var outNum = rowsInfolist[i].outNum;
				var tradeName = rowsInfolist[i].tradeName;
				if(parseFloat(outNum)<parseFloat(val)){
					$.messager.alert('提示',"药品 "+tradeName+" 申请数量大于可退数量,请核对后重新输入!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					var index = $('#infolist').datagrid('updateRow', {
						index : i,
						row : {
							returnNum : 0,
							totalReturnCost : 0
						}
					}).edatagrid('getRows').length - 1;
					$('#infolist').edatagrid('beginEdit', index);
					return false;
				}
				var index = $('#infolist').datagrid('updateRow', {
					index : i,
					row : {
						returnNum : val,
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
		
			//从xml文件中解析，读到下拉框
			function idCombobox(param) {
				$('#CodeDrugtype').combobox({
					url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type="+param,
					valueField : 'encode',
					textField : 'name',
					multiple : false,
					onLoadSuccess : function() {//请求成功后
						$(list).each(function() {
							if ($('#CodeDrugtype').val() == this.Id) {
								$('#CodeDrugtype').combobox("select", this.Id);
							}
						});
					},
					onSelect : function(record){
						searchFrom();
					}
				});
			}
			
			
			//重置查询条件
			function reset(){
				$("#tradNameSerc").textbox('setValue','');
				searchList();
			}
			
			//页面查询检索
			var arr = [];
			function searchList() {
				var queryName = $("#tradNameSerc").textbox('getValue');
				if (queryName == null || queryName == "") {
					//显示全部
					for ( var i = 0; i < arr.length; i++) {
						arr[i].show();
					}
					arr.length = 0;
				} else {
					for ( var i = 0; i < totalRows.length; i++) {
						var namestr = totalRows[i].tradeName;
						var searchItem=namestr.indexOf(queryName) == -1 ;
						//不匹配数据，执行删除
						if (searchItem) {
							$("#" + totalRows[i].drugCode+"_"+i).parent().parent().parent().hide();
							arr.push($("#" + totalRows[i].drugCode+"_"+i).parent().parent().parent());
						}else{
							$("#" + totalRows[i].drugCode+"_"+i).parent().parent().parent().show();
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
					if (!$('#' + id+"_"+i).parent().parent().parent().is(":hidden")) {//判断是否隐藏
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
								if (arrIds.indexOf(rows[i].drugCode) != -1) {
									arrIds.remove(rows[i].drugCode);
								}
								arr.push(dd);
							}
							for ( var i = arr.length - 1; i >= 0; i--) {
								$('#infolist').edatagrid('deleteRow', arr[i]);//通过索引删除该行
							}
							//更新一下总金额
							totalPrice();
						} 
					});
				}else {
					$.messager.alert("操作提示", "请选择要删除的条目！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
			//导出列表
			function exportList() {
				var rows = $('#infolist').datagrid("getRows");
				if(rows.length<1){
					$.messager.alert("操作提示", "列表无数据,无法导出!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
				for(var i=0; i<rows.length; i++){
					if(isNaN(rows[i].returnNum)|| rows[i].returnNum <= 0){
						$.messager.alert('提示','药品【'+rows[i].tradeName+'】审批数量不能为空或者零,如不操作,请取消勾选!');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						   return;
					}
				}
				$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否删除
					if (res) {
						
						var copyRows = cloneObject(rows);
						for ( var i = 0; i < rows.length; i++) {
							var id = rows[i].id;
							if ($('#' + id).parent().parent().parent().is(":hidden")) {//判断是否隐藏
								copyRows.remove(copyRows[i]);
							}
						}
						$('#infolist').datagrid('acceptChanges');//提交所有从加载或者上一次调用acceptChanges函数后更改的数据。
						$('#infoJson').val(JSON.stringify(copyRows));
						$('#saveForm').form('submit', {
							url : "<%=basePath%>drug/applyout/exportInReturn.action",
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
						remark : $(obj).val()
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
		
	</body>
</html>