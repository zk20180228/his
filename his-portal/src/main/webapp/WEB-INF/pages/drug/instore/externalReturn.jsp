<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>外部入库退库页面</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
			//定义全局变量记录总的待入库信息条目
			var totalRows = null;
			//记录赋值过的条目id
			var arrIds = [];
			//格式化所用
			//格式化所用
			var packUnitList = "";
			var minUnitList = "";
			var dosageformList = null;
			var typeList = "";
			//加载页面
			$(function() {
				bindEnterEvent('tradNameSerc',searchList,'easyui');//绑定回车事件
				bindEnterEvent('queryName',searchFrom,'easyui');//绑定回车事件
				bindEnterEvent('billcode',billcodelist,'easyui');//绑定回车事件
				//初始化下拉框
				//idCombobox("CodeDrugtype");
				idCombobox("drugType");
				//下拉框的keydown事件   调用弹出窗口
				var drugType = $('#CodeDrugtype').combobox('textbox');
				
				drugType.keyup(function() {
					KeyDown1(0, "CodeDrugtype");
				});
				//添加datagrid事件及分页
				$('#list').datagrid({
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 30, 50, 80, 100 ],
					url:"<%=basePath%>drug/instore/queryStorage.action?flag=2&menuAlias=${menuAlias}",
					onBeforeLoad:function (param) {
						$.ajax({
							url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
							data:{"type" : "packunit"},
							type:'post',
							success: function(data) {
								packUnitList = data;
							}
						});
						$.ajax({
							url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
							data:{"type" : "minunit"},
							type:'post',
							success: function(data) {
								minUnitList = data;
							}
						});
						$.ajax({
							url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
							data:{"type" : "dosageForm"},
							type:'post',
							success: function(data) {
								dosageformList = data;
							}
						});
						$.ajax({
							url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
							data:{"type" : "drugType"},
							type:'post',
							success: function(data) {
								typeList = data;
							}
						});
			        },
					onDblClickRow : function(rowIndex, rowData) {//双击查看
						$('#outlist').edatagrid('load',{
							queryName : rowData.drugId
						})
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
				
				$('#infolist').edatagrid({
					pagination : false,
			        onLoadSuccess : function (data) {
			        	setTimeout(function(){
			        		arrIds.length = 0;
			        		for (var i = 0; i < data.total; i ++){
								arrIds.push(data.rows[i].drugId);
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
						formatter : function(value, row, index) {
							return "<div id='"+row.id+"'></div>";
						},
						hidden : true
					}, {
						field : 'flagId',
						title : 'flagId',
						hidden : true,
						formatter : function(value, row, index) {
							return "<div id='"+row.drugId+"'></div>";
						}
					}, {
						field : 'drugId',
						title : '药品id',
						hidden : true
					}, {
						field : 'companyCode',
						title : '供货公私',
						hidden : true
					}, {
						field : 'inListCode',
						title : '入库单据号',
						hidden : true
					}, {
						field : 'inBillCode',
						title : '入库单流水号',
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
						title : '药品名称',
						hidden : true
					},{
						field : 'drugCommonname',
						title : '药品通用名',
						width : '15%'
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
						field : 'specs',
						title : '规格',
						width : '7%'
					}, {
						field : 'doseModelCode',
						title : '剂型',
						hidden:true
					}, {
						field : 'doseModelCode',
						title : '剂型',
						hidden:true
					}, {
						field : 'retailPrice',
						title : '零售价',
						align: 'right',
						width : '7%',
						formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}
					}, {
						field : 'packUnit',
						title : '包装单位',
						hidden:true
					}, {
						field : 'showPackUnit',
						title : '包装单位',
						width : '7%'
					}, {
						field : 'minUnit',
						title : '最小单位',
						hidden:true
					},{
						field : 'packQty',
						title : '包装数量',
						width : '8%'
					}, {
						field : 'showMinUnit',
						title : '最小单位',
						width : '7%'
					}, {
						field : 'storeSum',
						title : '入库数量',
						width : '7%'
					}, {
						field : 'storeCost',
						title : '入库金额',
						align: 'right',
						width : '7%',
						formatter:function(value,row,index){
				        	if (value != null && value != "" && !isNaN(value) && value >0) {
								return value;
							}else{
								return '';
							}
				        	}
					},{
						field : 'returnNum',
						title : '退回数量',
						formatter : function(value, row, index) {
							var retVal = "";
							if (value != null && value != "" && !isNaN(value) && value>0) {
								retVal = value;
							}else{
								retVal = '';
							}
							return "<input type='text' id='"
									+ row.id
									+ "_"
									+ index
									+ "' value='"
									+ retVal
									+ "' onChange = 'upperCase(this)'>";
						},
						width : '10%'
					},{
						field : 'totalReturnCost',
						title : '退回总金额',
						align: 'right',
						width : '10%',
						formatter:function(value,row,index){
					        	if (value != null && value != "" && !isNaN(value)) {
									return value;
								}else{
									return '';
								}
				        	}
					},{
						field : 'placeCode',
						title : '货位号',
						width : '7%'
					} ,{
						field : 'groupCode',
						title : '批次号',
						hidden:true
					}] ]
				});
				
				
				//添加datagrid事件及分页
				$('#outlist').edatagrid({
					url : "<%=basePath%>drug/instore/queryDrugId.action",
					striped : true,
					checkOnSelect : true,
					selectOnCheck : true,
					singleSelect : false,
					fitColumns : false,
					columns : [ 
					            [{field : 'ck',checkbox : true},
					             {field : 'id',title : 'id', hidden : true,
					            	formatter : function(value, row, index) {
										return "<div id='"+row.id+"'></div>";
									}
					             },
					             {field : 'flagId',title : 'flagId',hidden : true,width:'0%',
					            	formatter:function(value,row,index){
						        		 return "<div id='"+row.drugId+"'></div>";
						        	}
					             },
						         {field : 'drugId',title : '药品Id',hidden : true},
						         {field : 'companyCode',title : '供货公司',hidden : true},
						         {field : 'inListCode',title : '入库单据号',hidden : true},
						         {field : 'inBillCode',title : '入库单流水号',hidden : true},
						         {field : 'drugNamepinyin',title : '拼音码',hidden : true},
						         {field : 'drugNamewb',title : '五笔码',hidden : true},
						         {field : 'drugNameinputcode',title : '自定义码',hidden : true},
						         {field : 'tradeName',title : '药品名称',hidden : true},
						         {field : 'drugCnamepinyin',title : '通用拼音码',hidden : true},
						         {field : 'drugCnamewb',title : '通用五笔码',hidden : true},
						         {field : 'drugCnameinputcode',title : '通用自定义码',hidden : true},
						         {field : 'doseModelCode',title : '剂型',hidden : true},
						         {field : 'drugName',title : '药品通用名称',width : '30%'},
						         {field : 'drugSpec',title : '规格',width : '25%'},
								 {field : 'retailPrice',title : '零售价',align: 'right',width : '25%',
										formatter : function(value, row, index) {
											if (value) {
											return "<div id='retailPrice_"+index+"'>"
													+ value.toFixed(4)
													+ "</div>";
											}
										}
						         },
								 {field : 'storeSum',title : '入库数量',width : '15%'} 
								] ]
					,toolbar: [{
			               id: 'btnAdd',
			               text: '添加',
			               iconCls: 'icon-add',
			               handler: function () {
			            	   var rows = $('#outlist').edatagrid("getSelections");
			            	   for(var i =0;i<rows.length;i++){
			            		   attrValue(rows[i]);	
			            	   }
			          	   }
			            }],
		            onDblClickRow : function(rowIndex, rowData) {
							attrValue(rowData);
					}
				});
				
				
				
			});
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
			//为列表赋值
			function appendValue(row) {
				var index = $('#infolist').edatagrid('appendRow', {
					id : row.id,
					companyCode : row.companyCode,
					tradeName : row.drugName,
					inListCode : row.inListCode,
					inBillCode : row.inBillCode,
					drugNamepinyin : row.drugNamepinyin,
					drugNamewb : row.drugNamewb,
					drugNameinputcode : row.drugNameinputcode,
					drugCommonname : row.drugName,
					drugCnamepinyin : row.drugCnamepinyin,
					drugCnamewb : row.drugCnamewb,
					drugCnameinputcode : row.drugCnameinputcode,
					drugType : row.drugType,
					specs : row.drugSpec,
					doseModelCode : row.drugDosageform,
					showDoseModelCode: dosageformFamater(row.drugDosageform,null,null),
					packUnit : row.drugPackagingunit,
					showPackUnit : packUnitFamater(row.drugPackagingunit),
					minUnit : row.minUnit,
					showMinUnit : minUnitFamater(row.minUnit),
					retailPrice : row.retailPrice,
					storeCost : (row.retailPrice * row.storeSum).toFixed(4),
					batchNo : row.batchNo,
					validDate : row.validDate,
					invoiceNo : row.invoiceNo,
					purchasePrice : row.purchasePrice,
					purchaseCost : row.purchaseCost,
					placeCode : row.placeCode,
					drugId : row.drugId,
					groupCode:row.groupCode,
					storeSum:row.storeSum,
					retailCost:row.retailCost,
					packQty:row.drugPackagingnum
				}).edatagrid('getRows').length - 1;
				$('#infolist').edatagrid('beginEdit', index);
				var rows = $('#infolist').datagrid("getRows");
				totalRows = cloneObject(rows);
				//计算总的申请金额
				totalPrice();
				$("#totalDivId").show();
				arrIds.push(row.drugId);
			}
			//保存
			function save() {
				if(checkStoreSum()){
					var rows = $('#infolist').edatagrid('getChecked');
					if (rows.length > 0) {                        
						for(var i=0; i<rows.length; i++){
							if(!isNaN(rows[i].returnNum) && rows[i].returnNum >= 0){
							}else{
								$.messager.alert('提示','请在第'+(i+1)+'行退回数量中输入大于0的数字');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								   return;
							}
						}
						$('#infolist').datagrid('acceptChanges');
						$('#infoJson').val(JSON.stringify($('#infolist').edatagrid("getSelections")));
						$('#saveForm').form('submit',{
							url :"<%=basePath%>drug/instore/saveInstoreReturn.action?flag=1",
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
								data = eval("("+data+")")
								$.messager.progress('close');
								if(data.resCode == 'error'){
									$.messager.alert('提示',data.resMes);
								}else{
									$.messager.alert('提示',data.resMes);
									$('#list').edatagrid("reload");
									$('#outlist').edatagrid('load',{
										queryName : ''
									})
									var arr = [];
									for ( var i = 0; i < rows.length; i++) {
										var dd = $('#infolist').edatagrid('getRowIndex', rows[i]);//获得行索引
										if (arrIds.indexOf(rows[i].drugId) != -1) {
											arrIds.remove(rows[i].drugId);
										}
										arr.push(dd);
									}
									for ( var i = arr.length - 1; i >= 0; i--) {
										$('#infolist').edatagrid('deleteRow', arr[i]);//通过索引删除该行
									}
									$("#totalPrice").html("");
								}
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
			}
			//验证 某行信息的出库数量为0或者出库数量小于库存数量，给予用户提示“XX药出库数量不能小于0并且不能小于库存数量”
			function checkStoreSum(){
				var index = $('#infolist').datagrid("getRows").length;
				for (var i=0;i<index;i++){
			       var dataGridRowData = $('#infolist').datagrid('getRows')[i];
			       var inNum = dataGridRowData.storeSum;
			       var returnNum = dataGridRowData.returnNum;
			       if(parseFloat(returnNum) > parseFloat(inNum)){
			    	   $.messager.alert('提示',dataGridRowData.drugCommonname+"退回数量大于入库数量了");
			    	   setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
			    	   return false;
			       }
				}
				return true;
			}
			//计算总的退回金额
			function totalPrice() {
				var rows = $('#infolist').datagrid('getRows');
				var price = 0;
				for ( var i = 0; i < rows.length; i++) {
					var id = rows[i].id;
					if (!$('#' + id).parent().parent().parent().is(":hidden")) {//判断是否隐藏
						if(rows[i].totalReturnCost==null || rows[i].totalReturnCost==""){
							price +=0;
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
			
		function upperCase(obj){
			
			var val=$(obj).val();
			var index=$(obj).prop("id").split("_");
			var i =parseInt(index[1]);
			var rows = $('#infolist').datagrid('getRows');
			var totalReturnCost = rows[i].retailPrice * val;
			$('#infolist').edatagrid('endEdit', i);
			var index = $('#infolist').datagrid('updateRow', {
				index : i,
				row : {
					returnNum : val,
					totalReturnCost : totalReturnCost.toFixed(4),
				}
			}).edatagrid('getRows').length - 1;
			//更新退库金额
			totalPrice();
		}
			/**
			*	
			*根据入库单查询退库药品
			*/
			function billcodelist(){
				var billcode = $.trim($('#billcode').textbox('getValue'));
				$('#infolist').datagrid({
					url : "<%=basePath%>drug/instore/queryStorage.action",
					queryParams: {
						billcode: billcode,
					}
				});
				
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
				var queryName = $.trim($('#queryName').textbox('getValue'));
				var drugType = $.trim($('#CodeDrugtype').combobox('getValue'));
				$('#list').datagrid('load', {
					drugName : queryName,
					drugTypeSerc : drugType
				});
			}
			//待入库列表查询
			var arr = [];
			function searchList() {
				var queryName = $("#tradNameSerc").textbox('getValue');
				if ((queryName == null || queryName == "")) {
					//显示全部
					for ( var i = 0; i < arr.length; i++) {
						arr[i].show();
					}
					arr.length = 0;
				} else {
					if (queryName != null && queryName != "") {
						for ( var i = 0; i < totalRows.length; i++) {
							var namestr = totalRows[i].drugCommonname;
							var codestr = totalRows[i].drugId;
							var searchItem=namestr.indexOf(queryName) == -1 && codestr.indexOf(queryName) == -1;
							//不匹配数据，执行删除
							if (searchItem) {
								$("#" + totalRows[i].drugId).parent().parent().parent().hide();
								arr.push($("#" + totalRows[i].drugId).parent().parent().parent());
								
							}else{
								$("#" + totalRows[i].drugId).parent().parent().parent().show();
							}
						}
					}
				}
				//计算总的申请金额
				totalPrice();
			}
			//回车事件
			function KeyDown() {
				if (event.keyCode == 13) {
					event.returnValue = false;
					event.cancel = true;
					searchFrom();
				}
			}
			//空格弹窗事件
			function KeyDown1(flg, tag) {
				if (flg == 1) {//回车键光标移动到下一个输入框
					if (event.keyCode == 13) {
						event.keyCode = 9;
					}
				}
				if (flg == 0) { //空格键打开弹出窗口
					if (event.keyCode == 32) {
						event.returnValue = false;
						event.cancel = true;
						if (tag == "CodeDrugtype") {
							showWin("请药品类别", "<%=basePath%>ComboxOut.action?xml="+ "CodeDrugtype,0", "50%", "80%");
						}
					}
				}
			}
			//从xml文件中解析，读到下拉框
			function idCombobox(param) {
				$('#CodeDrugtype').combobox({
					url :"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type="+ param,
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
								if (arrIds.indexOf(rows[i].drugId) != -1) {
									arrIds.remove(rows[i].drugId);
								}
								arr.push(dd);
							}
							for ( var i = arr.length - 1; i >= 0; i--) {
								$('#infolist').edatagrid('deleteRow', arr[i]);//通过索引删除该行
							}
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
				var rows = $('#infolist').datagrid("getRows");
				var copyRows = cloneObject(rows);
				$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否删除
					if (res) {
						for ( var i = 0; i < rows.length; i++) {
							var id = rows[i].drugId;
							if ($('#' + id).parent().parent().parent().is(":hidden")) {//判断是否隐藏
								copyRows.remove(copyRows[i]);
							}
						}
						$('#infolist').datagrid('acceptChanges');//提交所有从加载或者上一次调用acceptChanges函数后更改的数据。
						$('#infoJson').val(JSON.stringify(copyRows));
						$('#saveForm').form('submit', {
							url : "<%=basePath%>drug/applyout/exportApplyout.action?flag=8",
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
			//包装单位格式化
			function packUnitFamater(value){
				if(value!=null){
					for(var i=0;i<packUnitList.length;i++){
						if(value==packUnitList[i].encode){
							return packUnitList[i].name;
						}
					}	
				}
			}
			//最小单位单位格式化 
			function minUnitFamater(value){
				if(value!=null){
					for(var i=0;i<minUnitList.length;i++){
						if(value==minUnitList[i].encode){
							return minUnitList[i].name;
						}
					}	
				}
			}
			//显示药品类型单位格式化    
			function typeFamater(value){
				if(value!=null){
					for(var i=0;i<typeList.length;i++){
						if(value==typeList[i].encode){
							return typeList[i].name;
						}
					}	
				} 
			}
			//显示药品类型单位格式化    
			function dosageformFamater(value,row,index){
				if(value!=null){
					for(var i=0;i<dosageformList.length;i++){
						if(value==dosageformList[i].encode){
							return dosageformList[i].name;
						}
					}	
				}
			}
			
			// 药品列表查询重置
			function searchReload() {
				$('#billcode').textbox('setValue','');
				$('#infolist').datagrid('loadData',[]);
				totalRows = null;
				arrIds = [];
			}
			// 药品列表查询重置
			function searchReloadNext() {
				$('#CodeDrugtype').combobox('setValue','');
				$('#queryName').textbox('setValue','');
				searchFrom();
			}
			// 药品列表查询重置
			function searchReloadSec() {
				$('#tradNameSerc').textbox('setValue','');
				searchList();
			}
		</script>
</head>
	<body>
		<!-- 外部入库退库页面 -->
		<div id="divLayout1" class="easyui-layout" fit=true>
			<div data-options="region:'north'" style="height: 32px;border-top:0">
				<table cellspacing="0" cellpadding="0" border="0" style="width: 90%">
					<tr>
						<td style="padding: 2px 3px;" nowrap="nowrap">
							<!--  &nbsp;供货公司：
							<input  type="text" ID="companyCode" />
							<a href="javascript:delSelected('companyCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>  -->
							&nbsp;入库单据号：
							<input class="easyui-textbox" id="billcode" onkeydown="keyDown()"/>
							<a href="javascript:void(0)" onclick="billcodelist()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'west',split:false,iconCls:'icon-book',border:false,collapsible:false" style=" width: 40% ;">
				<div id="divLayout2" class="easyui-layout" data-options="fit:true,split:false,collapsible:false">
					<div data-options="region:'north',title:'药品列表',split:false,border:false,collapsible:false" style="height: 61px;width: 100%">
						<table cellspacing="0" cellpadding="0" style="width: 100%;border-bottom: 1px solid #95b8e7;padding: 0px;border: none;padding: 3px 0px 3px 0px">
							<tr>
								<td  nowrap="nowrap" class="externalReturn">
									&nbsp;药品类型：
									<input type="text" id="CodeDrugtype" style="width:125px"/>
									&nbsp;查询条件：
									<input class="easyui-textbox" id="queryName" onkeydown="keyDown()" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:140px"/>
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="searchReloadNext()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center'" style="width: 100%;height:20%; border-bottom: 1px solid #95b8e7;border-right:0">
						<table id="list"  data-options="fit:true,border: false,rownumbers:true,idField: 'id',striped:true,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fitColumns:false">
							<thead>
								<tr>
									<th data-options="field:'drugCommonname',width : '25%'">
										药品通用名
									</th>
									<th data-options="field:'drugSpec',width : '15%'">
										药品规格
									</th>
									<th data-options="field:'retailPrice',align: 'right',width : '10%',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}">
										零售价
									</th>
									<th data-options="field:'packUnit',width : '10%'">
										单位
									</th>
									<th data-options="field:'storeSum',width : '15%',formatter: function(value,row,index){
																					if (value!=null){
																						return  value
																					}else{
																						return 0
																					}}">剩余数量</th>
									<th data-options="field:'returnNum',width : '10%'">
										退回数量
									</th>												
								</tr>
							</thead>
						</table>
					</div>
					<div data-options="region:'south'" class="easyui-layout" style="height:50%;width:100%;border-left:0;border-right:0" data-options="region:'center',title:'退库记录',border:false">
						<div style="width:100%;height:100%;"> 
							<input type="hidden" name="outJson" id="outJson">
							<table id="outlist" 
								data-options="fit:true,method:'post',striped:true,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,border:false">
							</table>
						</div>
					</div>
				</div>
			</div>
			<div data-options="region:'center',title:'待入库药品列表'" style="width: 60%;height: 100%">
				<div id="divLayout3" class="easyui-layout" data-options="fit:true,border:false">
					<div data-options="region:'north',split:false,border:false" style="height: 34px;">
						<input id="drugId" type="hidden">
						<table cellspacing="0" cellpadding="0" style="padding: 3px;" >
							<tr>
								<td  nowrap="nowrap">
									查询条件：
									<input class="easyui-textbox" id="tradNameSerc" />
									<a href="javascript:void(0)" onclick="searchList()"class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="searchReloadSec()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',split:false,iconCls:'icon-book',border:true" style="width: 100%;border-left:0">
						<form id="saveForm" method="post" style="height: 100%">
							<input type="hidden" name="infoJson" id="infoJson">
							<table id="infolist"
								data-options="fit:true,url:'',method:'post',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar:'#toolbarId'">
							</table>
						</form>
					</div>
				</div>
			</div>
				<div id="toolbarId">
					<a id="btnAddUser" href="javascript:void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="save()">保存</a>
					<a id="btnAddUser" href="javascript:void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true"onclick="del()">删除</a>
					<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:true">导出</a>
					&nbsp;总退回金额：<span id="totalPrice" style="font-weight: bold;color: red;">0.00</span>
				</div>
		</div>
	</body>
</html>