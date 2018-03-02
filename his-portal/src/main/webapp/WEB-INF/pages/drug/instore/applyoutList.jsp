<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药房内部入库</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
			//定义全局变量记录总的待入库信息条目
			var totalRows = null;
			//记录赋值过的条目id
			var arrIds = [];
			//格式化所用
			var packUnitList = "";
			var dosageformMap = new Map();//剂型Map
			//加载页面
			$(function() {
				
				bindEnterEvent('tradNameSerc',searchList,'easyui');//绑定回车事件
				bindEnterEvent('queryName',searchFrom,'easyui');//绑定回车事件
				bindEnterEvent('queryNo',KeyDownNo,'easyui');//绑定回车事件
				//初始化下拉框
				idCombobox("drugType");
				
				//初始化目标科室下拉
				$('#deptList').combobox({
					url :"<%=basePath%>baseinfo/department/getDeptBySessionDept.action?loginDepteId="+$('#loginDeptId').val()+"&type=1",
					valueField : 'deptCode',
					textField : 'deptName',
					width : 170,
					onSelect:function(record){
						var deptList = $('#deptList').combobox('getValue');
						var drugTypeSerc = $('#CodeDrugtype').combobox('getValue');
						$('#deptCode').val(record.deptCode);
						$('#list').datagrid('load', {
							deptId : deptList,
							drugTypeSerc : drugTypeSerc
						});
						$('#infolist').datagrid('loadData',[]);
					}
				});
				$.ajax({
					url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
					data:{"type" : "packunit"},
					type:'post',
					success: function(data) {
						packUnitList = data;
					}
				});
					$('#list').datagrid({
						pagination : true,
						pageSize : 20,
						pageList : [ 20, 30, 50, 80, 100 ],
						url:"<%=basePath%>drug/outstore/queryStockinfoJoinDrugInfo.action?flag=0&menuAlias=${menuAlias}",
						onBeforeLoad:function (param) {
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
							}
							var deptList = $.trim($('#deptList').combobox('getValue'));
							var queryNo =$.trim($('#queryNo').textbox('getValue'));
							if(deptList == "" && queryNo == ""){
								$('#list').datagrid('loadData',{total:0,rows:[]});
								return false;
							}
				        },
						onDblClickRow : function(rowIndex, rowData) {//双击查看
							if(rowData.storeSumDrug <= 0){
								$.messager.alert('提示','药品库存不足！！');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								return false;
							}
							var flagSubmit;
							if($('#deptList').combobox('getValue')==null ||$('#deptList').combobox('getValue')==""){
								flagSubmit=1;
								$.messager.alert('操作提示',"请先选择供货科室!");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}if(flagSubmit !=1){
								var row = $('#list').datagrid('getSelected');
								if (row) {
									attrValue(row);
								}
							}
						},//改变颜色
						rowStyler: function(index,row){
							if(row.storeSumDrug <= 0){
									return 'background-color:#FF0000;color:black;';
							}
						}
					});
				$('#infolist').datagrid({
					striped : true,
					checkOnSelect : true,
					selectOnCheck : true,
					singleSelect : false,
					fitColumns : false,
					fixed : true,
					idField : 'drugCode',
					url:"<%=basePath%>drug/applyout/queryApplyInOutByNo.action",
					onBeforeLoad:function (param) {
						var deptList = $.trim($('#deptList').combobox('getValue'));
						var queryNo = $.trim($('#queryNo').textbox('getValue'));
						if(deptList == "" && queryNo == ""){
							return false;
						}
			        },
			        onLoadSuccess : function (data) {
			        	setTimeout(function(){
			        		arrIds.length = 0;
			        		for (var i = 0; i < data.total; i ++){
								arrIds.push(data.rows[i].drugCode);
			        		}
			        		totalRows = data.rows;
			        		totalPrice();//计算金额
						});
						
			        },
					columns : [ [ {
						field : 'ck',
						checkbox : true
					}, {
						field : 'id',
						title : 'id',
						hidden : true
					}, {
						field : 'drugCode',
						title : '药品id',
						hidden : true
					}, {
						field : 'drugNamepinyin',
						title : '拼音码',
						hidden : true
					}, {
						field : 'drugNamewb',
						title : '五笔码',
						hidden : true
					}, {
						field : 'drugNameinputcode',
						title : '自定义码',
						hidden : true
					}, {
						field : 'tradeName',
						title : '药品通用名',
						width : '300'
					}, {
						field : 'storeSumDrug',
						title : '库存数量',
						width : '80'
					}, {
						field : 'applyNum',
						title : '申请数量',
						width : '80',
						formatter : function(value, row, index) {
							var retVal = 0;
							if (value != null && value != "" && !isNaN(value)) {
								retVal = value;
							}else{
								reVal = 0;
							}
							return "<input type='text' id='"
									+ row.drugCode
									+ "' value='"
									+ retVal
									+ "' onChange = 'upperCase(this)' style='width: 100%;'>";
						}
					}, {
						field : 'retailPrice',
						title : '零售价',
						align: 'right',
						width : '80',
						formatter : function(value, row, index) {
							return "<div id='retailPrice_"+index+"'>"+ value+"</div>";
						}
					}, {
						field : 'totalretailPrice',
						title : '申请金额',
						align: 'right',
						width : '80',
						formatter : function(value, row, index) {
							if (value != null && value != "" && !isNaN(value)){
								return value;
							}else{
								return 0;
							}
						}
					}, {
						field : 'specs',
						title : '规格',
						width : '100'
					}, {field : 'packUnit',
						title : '包装单位',
						formatter : packUnitFamater,
						width : '80'
					}, {
						field : 'mark',
						title : '备注',
						width : '150',
						formatter : function(value, row, index) {
							var retVal = "";
							if (value != null && value != "") {
								retVal = value;
							}
							return "<input type='text' id='mark_"+index+"' value='"+retVal+"' onChange = 'updateMark(this)' style='width: 100%;'>";
						}
					}, {
						field : 'drugCommonname',
						title : '药品通用名',
						hidden : true
					}, {
						field : 'drugCnamepinyin',
						title : '通用拼音码',
						hidden : true
					}, {
						field : 'drugCnamewb',
						title : '通用五笔码',
						hidden : true
					}, {
						field : 'drugCnameinputcode',
						title : '通用自定义码',
						hidden : true
					}, {
						field : 'doseModelCode',
						title : '剂型',
						hidden:true
					}, {
						field : 'drugDosageform',
						title : '剂型',
						hidden:true
					}, {
						field : 'drugPackagingunit',
						title : '包装单位',
						hidden:true
					}, {
						field : 'minUnit',
						title : '最小单位',
						hidden:true
					}, {
						field : 'showUnit',
						title : '最小单位',
						hidden : true,
					},{
						field : 'placeCode',
						title : '货位号',
						hidden:true
					},{
						field : 'groupCode',
						title : '批次号',
						hidden:true
					},{
						field : 'drugPackagingnum',
						title : '包装数',
						hidden:true
					}
					] ]
				});
			});
			//为列表赋值
			function appendValue(row) {
				var index = $('#infolist').datagrid('appendRow', {
					id : '',
					tradeName : row.drugCommonname,
					drugNamepinyin : row.drugNamepinyin,
					drugNamewb : row.drugNamewb,
					drugNameinputcode : row.drugNameinputcode,
					drugCommonname : row.drugCommonname,
					drugCnamepinyin : row.drugCnamepinyin,
					drugCnamewb : row.drugCnamewb,
					drugCnameinputcode : row.drugCnameinputcode,
					drugType : row.drugType,
					specs : row.specs,
					doseModelCode : row.drugDosageform,
					drugDosageform: row.drugDosageform,
					packUnit : row.packUnit,
					drugPackagingunit : row.packUnit,
					minUnit : row.showUnit,
					showUnit : row.showUnit,
					applyNum : 0,
					storeSumDrug : row.storeSumDrug.toFixed(2),
					retailPrice : row.retailPrice,
					totalretailPrice : 0.0000,
					mark : row.remark,
					placeCode : row.placeCode,
					invoiceNo : row.invoiceNo,
					drugCode : row.drugId,
					groupCode : row.groupCode,
					drugPackagingnum : row.drugPackagingnum
				}).edatagrid('getRows').length - 1;
				$('#infolist').datagrid('beginEdit', index);
				var rows = $('#infolist').datagrid("getRows");
				totalRows = cloneObject(rows);
				//计算总的申请金额
				totalPrice();
			}
			/**
			 * 批量提交列表信息
			 * @author  lt
			 * @date 2015-07-31
			 * @version 1.0
			 */
			function adSaveAdvice() {
				var rows = $('#infolist').datagrid('getSelections');
				if (rows.length > 0) {//选中几行的话触发事件	  
					if(checkStoreSum()){
					var ids = '';
					for ( var i = 0; i < rows.length; i++) {
						if(!isNaN(rows[i].applyNum) && rows[i].applyNum >= 0){
							var index = $('#infolist').datagrid('getRowIndex', rows[i]);//获得行索引
							$('#infolist').datagrid('endEdit',index);
						}else{
							$.messager.alert('提示','请在第'+(i+1)+'行申请数量中输入大于0的数字',"warning");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							   return;
						}
						if (rows[i].id == null) {//如果id为null 则为新添加行
						} else {
							if (ids != '') {
								ids += ',';
							}
							ids += rows[i].id;
						}
					};
					$('#infoJson').val(JSON.stringify($('#infolist').datagrid("getSelections")));
					var applyBillcode = $.trim($('#queryNo').textbox('getValue'));
					$('#saveForm').form('submit',{
						url :"<%=basePath%>drug/applyout/saveApplyInOut.action",
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
							var rows = $('#infolist').datagrid("getSelections");
							$.messager.progress('close');
							var data = eval('(' + data + ')');
							if(data.resCode == "error"){
								$.messager.alert('提示',data.resMes,"warning");
								return false;
							}
							var arr = [];
							for ( var i = 0; i < rows.length; i++) {
								var dd = $('#infolist').datagrid(
										'getRowIndex', rows[i]);//获得行索引
								if (arrIds.indexOf(rows[i].drugCode) != -1) {
									arrIds.remove(rows[i].drugCode);
								}
								arr.push(dd);
							}
							for ( var i = arr.length - 1; i >= 0; i--) {
								$('#infolist').datagrid('deleteRow', arr[i]);//通过索引删除该行
							}
							totalPrice();
							var rs = $('#infolist').datagrid("getRows");
							totalRows = cloneObject(rs);
							$('#list').datagrid('reload');
							$('#infolist').datagrid('clearSelections');
							$.messager.alert('提示',"内部入库申请成功！");
						},
						error : function(data) {
							$.messager.progress('close');
							$.messager.alert('提示',"保存失败！");
						}
					});
				} else {
					return ;
				}
				}else{
					$.messager.alert("操作提示", "请选择要保存的条目！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
			//验证 某行信息的出库数量为0或者出库数量小于库存数量，给予用户提示“XX药出库数量不能小于0并且不能小于库存数量”
			function checkStoreSum(){
				var index = $('#infolist').datagrid("getSelections").length;
				for (var i=0;i<index;i++){
			       var dataGridRowData = $('#infolist').datagrid('getSelections')[i];
			       var applyNum = dataGridRowData.applyNum;
			       var storeSum = dataGridRowData.storeSumDrug;
			       if(applyNum==0 || parseFloat(applyNum) > parseFloat(storeSum)){
			    	   $.messager.alert('提示',"待入库列表第"+(parseInt(i)+1)+"条记录请求入库数量为0了或者请求入库数量大于供货库存数量了");
			    	   setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
			    	   return false;
			       }
				}
				return true;
			}
			
			//onkeyUp触发的函数
			function upperCase(obj) {
				var val = $(obj).val();
				var id = $(obj).prop("id");
				var i = $('#infolist').datagrid('getRowIndex',id);
				var rowsInfolist = $('#infolist').datagrid('getRows');
				var retailPrice = rowsInfolist[i].retailPrice;
				var totalretailPrice = retailPrice * val;
				var drugCommonname = rowsInfolist[i].drugCommonname;
				var storeSum = rowsInfolist[i].storeSumDrug;
				var mark = rowsInfolist[i].mark;
				$('#infolist').datagrid('endEdit', i);
				if(parseFloat(storeSum) < parseFloat(val)){
					$.messager.alert('提示',"药品 "+drugCommonname+" 申请数量大于库存数量，请核对后重新输入");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					var index = $('#infolist').datagrid('updateRow', {
						index : i,
						row : {
							applyNum : 0,
							totalretailPrice : 0,
							mark : mark
						}
					}).edatagrid('getRows').length - 1;
					$('#infolist').datagrid('beginEdit', index);
					//更新一下总金额
					totalPrice();
					return false;
				}
				var index = $('#infolist').datagrid('updateRow', {
					index : i,
					row : {
						applyNum : val,
						totalretailPrice : (retailPrice * val).toFixed(4),
						mark : mark
					}
				}).edatagrid('getRows').length - 1;
				$('#infolist').datagrid('beginEdit', index);
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
				var queryDept = $.trim($('#deptList').combobox('getValue'));
				var queryName = $.trim($('#queryName').textbox('getValue'));
				var queryNo = $.trim($('#queryNo').textbox('getValue'));
				var drugType = $.trim($('#CodeDrugtype').combobox('getValue'));
				$('#list').datagrid('load', {
					deptId : queryDept,
					drugNameweb : queryName,
					drugTypeSerc : drugType,
					applyBillcode : queryNo
				});
			}
			/**
			 * 药品列表重置
			 */
			function researchFrom() {
				var queryName = $('#queryName').textbox('setValue','');
				var drugType = $('#CodeDrugtype').combobox('setValue','');
				searchFrom();
			}
		
			/**
			 * 药品列表重置
			 */
			function researchFromNext() {
				$('#queryNo').textbox('setValue','');
				$('#infolist').datagrid('loadData',[]);
				totalRows = null;
				arrIds = [];
				totalPrice();
			}
			//回车事件
			function KeyDownNo() {
				var deptId = $.trim($('#deptList').combobox('getValue'));
				var applyBillcode = $.trim($('#queryNo').textbox('getValue'));
				if(deptId == ''){
					$.messager.alert("操作提示", "请先选择出库科室！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				Adddilog("申请单查询", "<%=basePath%>drug/applyout/selectApplyOutList.action?deptId=" + deptId + "&applyBillcode=" + applyBillcode);
			}
			
			/* 
			* 关闭界面
			*/
			function closeLayout(){
				$('#applyOut').dialog('close'); 
			}
			
			//警戒线查询
			function loadInfo() {
				var deptId = $('#deptList').combobox('getValue');
				if(deptId == ''){
					$.messager.alert("操作提示", "请先选择出库科室！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				$.ajax({
					url:"<%=basePath%>drug/applyout/queryCordon.action",
					data:{"deptId" : deptId},
					type:'post',
					beforeSend : function(e,xhr,o){
						$.messager.progress({text:'查询中，请稍后...',modal:true});	// 显示进度条 
					},
					success: function(data) {
						$.messager.progress('close');
						if(data.length > 0){
							totalRows = cloneObject(data);
							for(var i = 0; i < data.length; i++){
								arrIds.push(data[i].drugCode);
							}
							$('#infolist').datagrid('loadData', data);
							totalPrice();
						}else{
							$.messager.alert("操作提示", "您选中的科室没有符合条件的药品！", "warning");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}
					}
				});
			}
		
			//为列表赋值
			function attrValue(row) {
				var rows = $('#infolist').datagrid("getRows");
				if (rows.length > 0) {
					if (arrIds.indexOf(row.drugId) == -1) {
						appendValue(row);
						arrIds.push(row.drugId);
					} else {
						$.messager.alert("操作提示", "待入库列表已有此药品请直接在右侧修改申请数量！", "warning");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}
				} else {
					appendValue(row);
					if (arrIds.indexOf(row.drugId) == -1) {
						arrIds.push(row.drugId);
					}
				}
			}
			//计算总的申请金额
			function totalPrice() {
				var rows = $('#infolist').datagrid('getRows');
				var price = 0;
				for ( var i = 0; i < rows.length; i++) {
					var id = rows[i].drugCode;
					if (!$('#' + id).parent().parent().parent().is(":hidden")) {//判断是否隐藏
						price += rows[i].totalretailPrice * 1;
					}
				}
				if(!isNaN(price) && price >= 0){
					price = price.toFixed(2);
				}else{
					price = 0.00;
				}
				
				$("#totalPrice").html(price);
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
					url :"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=" + param,
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
					onSelect:function(record){
						var drugTypeSerc = $('#CodeDrugtype').combobox('getValue');
						var deptList = $('#deptList').combobox('getValue');
						$('#list').datagrid('load', {
							drugTypeSerc : drugTypeSerc,
							deptId : deptList
						});
					}
				});
			}
			//页面查询检索
			var arr = [];
			function searchList() {
				var queryName = $.trim($("#tradNameSerc").textbox('getValue'));
				if ((queryName == null || queryName == "")) {
					//显示全部
					for ( var i = 0; i < arr.length; i++) {
						arr[i].show();
					}
					arr.length = 0;
				} else {
					if (queryName != null && queryName != "") {
						for ( var i = 0; i < totalRows.length; i++) {
							var namestr = totalRows[i].tradeName;
							var pinstr = totalRows[i].drugCnamepinyin;
							var wbstr = totalRows[i].drugCnamewb;
							var searchItem=namestr.indexOf(queryName) == -1 && pinstr.indexOf(queryName) == -1 && wbstr.indexOf(queryName) == -1;
							if(totalRows[i].drugCnameinputcode!=null){
								var inputstr = totalRows[i].drugCnameinputcode;
								searchItem=namestr.indexOf(queryName) == -1 && pinstr.indexOf(queryName) == -1 && wbstr.indexOf(queryName) == -1&& inputstr.indexOf(queryName) == -1;
							}
							//不匹配数据，执行删除
							if (searchItem) {
								$("#" + totalRows[i].drugCode).parent().parent().parent().hide();
								arr.push($("#" + totalRows[i].drugCode).parent().parent().parent());
							}else{
								$("#" + totalRows[i].drugCode).parent().parent().parent().show();
							}
						}
					}
				}
				//计算总的申请金额
				totalPrice();
			}
			function researchList() {
				var queryName = $("#tradNameSerc").textbox('setValue','');
				searchList();
			}
			//删除待入库列表
			function delList() {
				var rows = $('#infolist').datagrid("getSelections");
				if (rows.length > 0) {
					$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
						if (res) {
							var ids = '';
							var arr = [];
							for ( var i = 0; i < rows.length; i++) {
								if(rows[i].id){
									if(ids != ''){
										ids += ',' + rows[i].id;
									}else{
										ids = rows[i].id;
									}
								}
								var dd = $('#infolist').datagrid('getRowIndex',
										rows[i]);//获得行索引
								if (arrIds.indexOf(rows[i].drugCode) != -1) {
									arrIds.remove(rows[i].drugCode);
								}
								arr.push(dd);
							}
							for ( var i = arr.length - 1; i >= 0; i--) {
								$('#infolist').datagrid('deleteRow', arr[i]);//通过索引删除该行
							}
							totalPrice();
							if(ids != ''){
								$.ajax({
									url: "<%=basePath%>drug/applyout/deleteApplyInOut.action",
									data:{"ids" : ids},
									type:'post',
									success: function(data) {
										if(data == 'success'){
											$.messager.alert("提示", "删除成功！");
										}
									}
								});
							}
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
				var rows = $('#infolist').datagrid("getChecked");
				if (rows.length == 0) {
					$.messager.alert("操作提示", "请选中要导出的记录！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
					if (res) {
						var rows = $('#infolist').datagrid("getRows");
						//为了结束编辑行 不然获取的rows里没有编辑状态下的数据
						for ( var i = 0; i < rows.length; i++) {
							$('#infolist').datagrid("endEdit",
									$('#infolist').datagrid("getRowIndex", rows[i]));
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
							url :"<%=basePath%>drug/applyout/exportApplyout.action?flag=1",
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
			//显示包装单位格式化，最小单位单位格式化
			function packUnitFamater(value){
				if(value!=null){
					for(var i=0;i<packUnitList.length;i++){
						if(value==packUnitList[i].encode){
							return packUnitList[i].name;
						}
					}	
				}
			}
			//库存数量小数位数渲染
			function formatStoreSumDrug(value){
				if(value != null && value != ''){
					return value.toFixed(2);
				}
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
			//加载模式窗口
			function Adddilog(title, url) {
				$('#applyOut').dialog({    
					title: title,
					width: '40%',
					height: '60%',
					closed: false,
					cache: false,
					href: url,
					modal: true,
				   });
			}
			
		</script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
	<body>
		<div id="divLayout" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:false" style="height: 35px;border-top:0;">
				<!-- 药房内部入库-->
				<table cellspacing="0" cellpadding="0" border="0" style="width: 100%;">
					<tr>
						<td style="padding: 3px " nowrap="nowrap">
							供货科室：
							<input type="hidden" value="${loginDeptId}" id="loginDeptId">
							<input class="easyui-combobox" type="text" ID="deptList" data-options="prompt:'请选择供货科室'"/>
							&nbsp;申请单号：
							<input class="easyui-textbox" id="queryNo" style="width:140px" data-options="prompt:'请输入申请单号'"/>
							<a href="javascript:void(0)" onclick="KeyDownNo()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							<a href="javascript:void(0)" onclick="researchFromNext()" class="easyui-linkbutton" iconCls="reset">重置</a>
							<a href="javascript:void(0)" onclick="loadInfo()" class="easyui-linkbutton" iconCls="icon-jingjiexian">警戒线</a>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'west',split:false,title:'药品列表',iconCls:'icon-book'" style=" width: 40%;">
				<div id="divLayout" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north'" style="height: 35px;border-top: none;border-right: none;border-left:0;">
						<table cellspacing="0" cellpadding="0" border="0" style="padding: 3px;">
							<tr>
								<td nowrap="nowrap">
									药品类型：
									<input class="easyui-combobox" type="text" id="CodeDrugtype" style="width:110px" data-options="prompt:'请选择药品类型'"/>
									&nbsp;查询条件：
									<input class="easyui-textbox" id="queryName" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:110px"/>
									<shiro:hasPermission name="${menuAlias}:function:query">
										<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
										<a href="javascript:void(0)" onclick="researchFrom()" class="easyui-linkbutton" iconCls="reset">重置</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',split:false,iconCls:'icon-book',border:false" style="width: 100%;height: 100%">
					<table id="list" data-options="rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fit:true,fitColumns:false">
						<thead>
							<tr>
								<th data-options="field:'drugCommonname', width : '300'">
									药品通用名
								</th>
								<th data-options="field:'specs', width : '100'">
									药品规格
								</th>
								<th data-options="field:'drugDosageform',hidden : true, width : '80'">
									剂型
								</th>
								<th data-options="field:'groupCode',hidden:true">
									批次号
								</th> 
								<th data-options="field:'drugPackagingnum', width : '65'">
									包装数
								</th>
								<th data-options="field:'packUnit',formatter : packUnitFamater, width : '65'">
									单位
								</th>
								<th data-options="field:'retailPrice',align: 'right',width : '80',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}">
									零售价
								</th>
								<th data-options="field:'storeSumDrug', width : '80',formatter : formatStoreSumDrug">
									库存
								</th>
							</tr>
						</thead>
					</table>
					</div>
				</div>
			</div>
			<div data-options="region:'center',title:'待入库药品列表'" style="width: 60%">
				<div id="divLayout" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',split:false,border:false" style="height: 35px;">
						<input id="drugCode" type="hidden">
						<table cellspacing="0" cellpadding="0" border="0" style="width: 100%;padding: 3px;">
							<tr>
								<td style="font-size: 14px" nowrap="nowrap">
									查询条件：
									<input class="easyui-textbox" id="tradNameSerc" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:180px"/>
									<a href="javascript:void(0)" onclick="searchList()"class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="researchList()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',split:false,iconCls:'icon-book',border:false" style="width: 100%;">
						<form id="saveForm" method="post" style="border-top: 1px solid #95b8e7;">
							<input type="hidden" name="infoJson" id="infoJson">
							<input type="hidden" name="deptCode" id="deptCode">
						</form>
						<table id="infolist" data-options="fit:true,url:'',method:'post',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar:'#toolbarId'">
						</table>	
					</div>
					<div id="toolbarId">
						<shiro:hasPermission name="${menuAlias}:function:save">
							<a id="btnAddUser" href="javascript:adSaveAdvice();"class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" >保存</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:delete">
							<a id="btnAddUser" href="javascript:void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delList()">删除</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:export">
							<a id="btnAddUser" href="javascript:void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:true" onclick="exportList()">导出</a>
						</shiro:hasPermission>
						&nbsp;总申请金额：<span ID="totalPrice" style="font-weight: bold;color: red;">0.00</span>
					</div>
				</div>
			</div>
			<div id="applyOut"></div>
		</div>	
	</body>
</html>