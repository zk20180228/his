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
		<div id="divLayout" class="easyui-layout" fit=true>
			<div data-options="region:'north',split:false"
				style="padding: 5px;background: #eee;">
					<table cellspacing="0" cellpadding="0" border="0">
						<tr>
							<!--<td style="padding: 5px 15px;">
							入库类型：
						</td>
						<td>
							<input type="text" ID="CodeInstoretype" />
						</td>
						-->
							<td style="padding: 5px 15px;" nowrap="nowrap">
								供货公司：
								<input class="easyui-combotree" type="text" ID="companyCode" />
							</td>
							<td style="padding: 5px 15px;" nowrap="nowrap">
								入库日期：
								<input type="text" ID="inTime" class="easyui-datebox" />
							</td>
							<td style="padding: 5px 15px;" nowrap="nowrap">
								采购单号：
								<input type="text" ID="purchaseOrders" onkeydown="keyDown1()" />
							</td>
							<td style="padding: 5px 15px;" onkeydown="keyDown1()" nowrap="nowrap">
								申请单：
								<input type="text" ID="bedIdSerc" />
							</td>
						</tr>
					</table>
			</div>
			<div
				data-options="region:'west',split:false,title:'药品列表',iconCls:'icon-book'"
				style="padding: 5px; width: 30% ;background: #eee;">
					<table cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td style="padding: 2px 15px;" nowrap="nowrap">
								药品类型：

								<input type="text" id="CodeDrugtype" />
							</td>
							<td style="padding: 2px 15px;" nowrap="nowrap">
								药品名称：

								<input type="text" ID="drugNameSerc" onkeydown="keyDown()" />
							</td>
							<td style="padding: 2px 15px;" nowrap="nowrap">
								拼音码：

								<input type="text" ID="pinyimaSerc" onkeydown="keyDown()" />
							</td>
						</tr>
						<tr>
							<td style="padding: 2px 15px;" nowrap="nowrap">
								五笔码：

								<input type="text" ID="wbSerc" onkeydown="keyDown()" />
							</td>
							<td style="padding: 2px 15px;" nowrap="nowrap">
								自定义码：

								<input type="text" ID="zdySerc" onkeydown="keyDown()" />
							</td>
							<td>
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchFrom()"
									class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</td>
						</tr>
					</table>
				<table id="list"
					data-options="url:'${pageContext.request.contextPath}/drug/stockinfo/queryDrugStockInfo.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
					<thead>
						<tr>
							<th data-options="field:'drugName'">
								药品名称
							</th>
							<th data-options="field:'drugSpec'">
								药品规格
							</th>
							<th data-options="field:'drugRetailprice'">
								零售价
							</th>
							<th data-options="field:'showUnit'">
								单位
							</th>
						</tr>
					</thead>
				</table>
			</div>
			<div data-options="region:'center',title:'编辑入库信息'"
				style="padding: 5px; background: #eee;">
					<fieldset style="padding:3px;border: 0px" >
						<table cellspacing="5" cellpadding="5" border="0">
							<tr>
								<td style="padding: 7px 15px;">
									药品名称：
								</td>
								<td id="vdrugName">
								</td>
								<td style="padding: 7px 15px;">
									规格：
								</td>
								<td id="vdrugSpec">
								</td>
								<td style="padding: 7px 15px;">
									零售价：
								</td>
								<td id="vdrugRetailprice">
								</td>

							</tr>
							<tr>
								<td style="padding: 7px 15px;">
									包装数量：
								</td>
								<td id="vdrugPackagingnum">
								</td>
								<td style="padding: 7px 15px;">
									包装单位：<input id="vdrugPackagingunit" type="hidden">
								</td>
								<td id="showPackUnit">
								</td>
								<td style="padding: 7px 15px;">
									最小单位：<input id="vdrugMinimumunit" type="hidden">
								</td>
								<td id="showMinUnit">
									<br>
								</td>
							</tr>
						</table>
					</fieldset>
					<br>
					<fieldset style="padding:3px;border: 0px" >
						<form name="editForm">
							<table cellspacing="0" cellpadding="0" border="0">
								<tr>
									<td style="padding: 7px 15px;">
										数量：
									</td>
									<td>
										<input class="easyui-numberbox" type="text" ID="inNum"
											data-options="required:true" missingMessage="请输入数量"
											onkeyup="sumValue()" />
									</td>
									<td style="padding: 7px 15px;">
										零售金额：
									</td>
									<td>
										<input class="easyui-textbox" type="text" ID="retailCost"
											readonly="readonly" />
									</td>
									<td style="padding: 7px 15px;">
										购入金额：
									</td>
									<td>
										<input class="easyui-textbox" type="text" ID="purchaseCost"
											readonly="readonly" />
									</td>
								</tr>
								<tr>
									<td style="padding: 7px 15px;">
										批号：
									</td>
									<td>
										<input class="easyui-textbox" type="text" ID="batchNo"
											data-options="required:true" missingMessage="请输入批号" />
									</td>
									<td style="padding: 7px 15px;" onkeydown="keyDown1()">
										有效期：
									</td>
									<td>
										<input class="easyui-datebox" type="text" ID="validDate"
											data-options="required:true,showSeconds:false"
											missingMessage="请选择有效期" />
									</td>
									<td style="padding: 7px 15px;">
										货位号：
									</td>
									<td>
										<input class="easyui-textbox" type="text" ID="placeCode" />
									</td>
								</tr>
								<tr>
									<td style="padding: 7px 15px;">
										发票号：
									</td>
									<td>
										<input class="easyui-textbox" type="text" ID="invoiceNo" />
									</td>
									<!--<td style="padding: 7px 15px;">
							发票分类：
						</td>
						<td>
							<input class="easyui-textbox" type="text" ID="objectDept"/>
						</td>
						//无发票分类字段 
						-->
									<td style="padding: 7px 15px;">
										送货单号：
									</td>
									<td>
										<input class="easyui-textbox" type="text" ID="deliveryNo"
											style="width: 100%;" />
									</td>
									<td style="padding: 7px 15px;">
										购入价：
									</td>
									<td>
										<input class="easyui-numberbox" type="text"
											ID="drugPurchaseprice" data-options="required:true"
											missingMessage="请输入购入价" onkeyup="sumValue()" />
									</td>
								</tr>
								<tr>
									<td style="padding: 7px 15px;">
										备注：
									</td>
									<td colspan="5">
										<input class="easyui-textbox" type="text" ID="remark"
											data-options="multiline:true"
											style="width: 100%; height: 100px;" />
									</td>
								</tr>
								<tr>
									<td style="padding: 7px 15px;">
										生产厂家：
									</td>
									<td colspan="4">
										<input class="easyui-combobox" type="text" ID="producerCode"
										 readonly="readonly" style="width: 100%;" />
									</td>
									<td style="padding: 7px 15px;" align="right">
										<a id="conform" href="javascript:conform();void(0)"
											class="easyui-linkbutton" data-options="iconCls:'icon-save'">确认</a>
									</td>
								</tr>
								<tr>

								</tr>
							</table>
						</form>
					</fieldset>
			</div>
			<div
				data-options="region:'south',split:false,title:'待入库药品列表',iconCls:'icon-book'"
				style="padding: 10px; height: 40%;background: #eee;">
				<input id="drugCode" type="hidden">
				<input id="drugType" type="hidden">
				<input id="drugNamepinyin" type="hidden">
				<input id="drugNamewb" type="hidden">
				<input id="drugNameinputcode" type="hidden">
					<table cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td style="padding: 2px 15px;" nowrap="nowrap">
								药品名称：

								<input type="text" id="tradNameSerc" />
							</td>
							<td style="padding: 2px 15px;" nowrap="nowrap">
								拼音码：
								<input type="text" id="pySerc" />
							</td>
							<td style="padding: 2px 15px;" nowrap="nowrap">
								五笔码：

								<input type="text" id="wubiSerc" />
							</td>
							<td style="padding: 2px 15px;" nowrap="nowrap">
								自定义码：

								<input type="text" id="inputCodeSerc" />
							</td>
							<td>
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchList()"
									class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</td>
						</tr>
						
					</table>
				<form id="saveForm" method="post">
					<input type="hidden" name="infoJson" id="infoJson">
					<table id="infolist"
						data-options="url:'',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar:'#toolbarId'">
					</table>
				</form>
				<div id="totalDivId" style="display: none;">
					<table>
					<tr>	
						<td style="padding: 2px 15px;">
							<font style="padding-left:400px;">总零售金额：</font>
							<span ID="totalStoreCost"></span>
						</td>
						<td style="padding: 2px 15px;">
							总购入金额：
							<span ID="totalPurchaseCost" style="color: red;"></span>
						</td>
						<td style="padding: 2px 15px;">
							差价：
							<span ID="totalSpread" style="color: red;"></span>
						</td>
					</tr>
					</table>
				</div>	
			</div>
			<div id="toolbarId">
				<shiro:hasPermission name="${menuAlias}:function:save">
				<a href="javascript:void(0)" onclick="save()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true">保存</a>
				</shiro:hasPermission>
				<a href="javascript:void(0)" onclick="del()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true">删除</a>
				<shiro:hasPermission name="${menuAlias}:function:export">
				<a href="javascript:void(0)" onclick="exportList()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-print',plain:true">导出</a>
				</shiro:hasPermission>
				<!--<a href="javascript:void(0)" onclick="reload()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-reload',plain:true">刷新</a>
				-->
			</div>
		</div>
		<script type="text/javascript">
	//定义全局变量记录总的待入库信息条目
	var totalRows = null;
	//记录赋值过的条目id
	var arrIds = [];
	//格式化所用
	var packUnitList = "";
	var minUnitList = "";
	var typeList = "";
	//加载页面
	$(function() {
		//初始化下拉框
		idCombobox("CodeDrugtype");
		//下拉框的keydown事件   调用弹出窗口
		var drugType = $('#CodeDrugtype').combobox('textbox');
		drugType.keyup(function() {
			KeyDown1(0, "CodeDrugtype");
		});
		//初始化供货单位下拉
		$('#companyCode').combobox({
			url : "<c:url value='/drug/supply/queryCompanyList.action'/>",
			valueField : 'Id',
			textField : 'companyName',
			width : 200
		});
		//初始化生产厂家下拉
		$('#producerCode').combobox({
			url : "<c:url value='/drug/manufacturer/queryManufacturerList.action'/>",
			valueField : 'id',
			textField : 'manufacturerName',
			width : 400
		});
		//添加datagrid事件及分页
		$('#list').datagrid(
				{
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 30, 50, 80, 100 ],
					onBeforeLoad:function (param) {
						$.ajax({
							url:  "<c:url value='/comboBox.action'/>",
							data:{"str" : "CodeDrugpackagingunit"},
							type:'post',
							success: function(packUnitdata) {
								packUnitList = packUnitdata;
							}
						});
						$.ajax({
							url:  "<c:url value='/comboBox.action'/>",
							data:{"str" : "CodeMinimumunit"},
							type:'post',
							success: function(minUnitdata) {
								minUnitList = minUnitdata;
							}
						});
						$.ajax({
							url:  "<c:url value='/comboBox.action'/>",
							data:{"str" : "CodeDrugtype"},
							type:'post',
							success: function(typedata) {
								typeList = typedata;
							}
						});
			        },
					onDblClickRow : function(rowIndex, rowData) {//双击查看
						var row = $('#list').datagrid('getSelected');
						if (row) {
							$("#vdrugName").text(row.drugName);
							$("#vdrugSpec").text(row.drugSpec);
							$("#vdrugRetailprice").text(row.drugRetailprice);
							$("#vdrugPackagingunit").val(row.drugPackagingunit);
							$("#vdrugMinimumunit").val(row.drugMinimumunit);
							$("#vdrugPackagingnum").text(row.drugPackagingnum);
							$("#validDate").datebox("setValue", row.validDate);
							$("#placeCode").textbox("setValue", row.placeCode);
							$("#drugPurchaseprice").val(row.drugPurchaseprice);
							$("#producerCode").combobox("setValue",
									row.drugManufacturer);
							$("#drugCode").val(row.drugId);
							$("#drugNamepinyin").val(row.drugNamepinyin);
							$("#drugNamewb").val(row.drugNamewb);
							$("#drugNameinputcode").val(row.drugNameinputcode);
							$("#drugType").val(row.drugType);
							$("#showPackUnit").text(packUnitFamater(row.drugPackagingunit));
							$("#showMinUnit").text(MinUnitFamater(row.drugMinimumunit));
						}
					}
				});
		var id = '${id}'; //存储数据ID
		//添加datagrid事件及分页
		$('#infolist').edatagrid({
			pagination : true,
			pageSize : 20,
			pageList : [ 20, 30, 50, 80, 100 ],
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
				hidden : true,
				width : '5%'
			}, {
				field : 'flagId',
				title : 'flagId',
				hidden : true,
				width : '5%',
				formatter : function(value, row, index) {
					return "<div id='"+row.drugCode+"'></div>";
				}
			}, {
				field : 'tradeName',
				title : '药品名称',
				width : '5%'
			}, {
				field : 'drugNamepinyin',
				title : '拼音码',
				width : '5%'
			}, {
				field : 'drugNamewb',
				title : '五笔码',
				width : '5%'
			}, {
				field : 'drugNameinputcode',
				title : '自定义码',
				width : '5%'
			},{
				field : 'drugType',
				title : '药品类型',
				width : '5%',
				hidden: true
			}
			, {
				field : 'showDrugType',
				title : '药品类型',
				width : '5%'
			}, {
				field : 'specs',
				title : '规格',
				width : '5%'
			}, {
				field : 'retailPrice',
				title : '零售价',
				width : '5%'
			}, {
				field : 'showPackUnit',
				title : '包装单位',
				width : '5%'
			}, {
				field : 'packUnit',
				title : '包装单位',
				width : '5%',
				hidden : true
			}, {
				field : 'packQty',
				title : '包装数量',
				width : '5%'
			}, {
				field : 'inNum',
				title : '入库数量',
				width : '5%'
			}, {
				field : 'storeCost',
				title : '入库金额',
				width : '5%'
			}, {
				field : 'batchNo',
				title : '批号',
				width : '5%'
			}, {
				field : 'vValidDate',
				title : '有效期',
				width : '5%'
			}, {
				field : 'invoiceNo',
				title : '发票号',
				width : '5%'
			}, {
				field : 'purchasePrice',
				title : '购入价',
				width : '5%'
			}, {
				field : 'purchaseCost',
				title : '购入金额',
				width : '5%'
			}, {
				field : 'inType',
				title : '入库类别',
				width : '5%',
				hidden: true
			}, {
				field : 'companyCode',
				title : '目标科室',
				width : '4%',
				hidden: true
			}, {
				field : 'showCompanyCode',
				title : '目标科室',
				width : '5%'
			},{
				field : 'remark',
				title : '备注',
				editor : {
					type : 'textbox'
				},
				width : '8%'
			}, {
				field : 'placeCode',
				title : '货位号',
				hidden : true
			}, {
				field : 'producerCode',
				title : '生产厂家',
				hidden : true
			}, {
				field : 'deliveryNo',
				title : '送货单号',
				hidden : true
			} ] ]
		});

	});

	/**
	 * 确认把数据填充到待入库列表
	 * @author  lt
	 * @date 2015-07-21
	 * @version 1.0
	 */
	function conform() {
		var rows = $('#infolist').edatagrid("getRows");
		if ($("#inNum").val() != null && $("#inNum").val() != "") {
			if ($("#batchNo").textbox('getValue') != null
					&& $("#batchNo").textbox('getValue') != "") {
				if ($("#validDate").datebox('getValue') != null
						&& $("#validDate").datebox('getValue') != "") {
					if ($("#drugPurchaseprice").val() != null
							&& $("#drugPurchaseprice").val() != "") {
						if ($("#inTime").datebox('getValue') != null
								&& $("#inTime").datebox('getValue') != "") {
							if ($("#companyCode").combobox('getValue') != null
									&& $("#companyCode").combobox('getValue') != "") {
								if (rows.length > 0) {
									if (arrIds.indexOf($("#drugCode").val()) == -1) {
										appendValue();
										arrIds.push($("#drugCode").val());
									} else {
										$.messager
												.alert("操作提示",
														"待入库列表已有此药品请直接双击编辑！",
														"warning");
									}
								} else {
									appendValue();
									if (arrIds.indexOf($("#drugCode").val()) == -1) {
										arrIds.push($("#drugCode").val());
									}
								}
							} else {
								$.messager.alert("操作提示", "请选择供货公司！", "warning");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}
						} else {
							$.messager.alert("操作提示", "请选择入库日期！", "warning");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}
					} else {
						$.messager.alert("操作提示", "请输入购入价！", "warning");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}
				} else {
					$.messager.alert("操作提示", "请选择有效期！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			} else {
				$.messager.alert("操作提示", "请输入批号！", "warning");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}

		} else {
			$.messager.alert("操作提示", "请输入数量！", "warning");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	//为列表赋值
	function appendValue() {
		var index = $('#infolist').edatagrid('appendRow', {
			tradeName : $("#vdrugName").text(),
			drugNamepinyin : $("#drugNamepinyin").val(),
			drugNamewb : $("#drugNamewb").val(),
			drugNameinputcode : $("#drugNameinputcode").val(),
			drugType : $("#drugType").val(),
			showDrugType : typeFamater($("#drugType").val()),
			specs : $("#vdrugSpec").text(),
			retailPrice : $("#vdrugRetailprice").text(),
			packUnit : $("#vdrugPackagingunit").val(),
			showPackUnit:$("#showPackUnit").text(),
			packQty : $("#vdrugPackagingnum").text(),
			inNum : $("#inNum").val(),
			retailCost : $("#retailCost").textbox('getValue'),
			batchNo : $("#batchNo").textbox('getValue'),
			vValidDate : $("#validDate").datebox('getValue'),
			vInputDate : $("#inTime").datebox('getValue'),
			companyCode : $("#companyCode").combobox('getValue'),
			showCompanyCode : $("#companyCode").combobox('getText'),
			drugCode : $("#drugCode").val(),
			purchasePrice : $("#drugPurchaseprice").val(),
			purchaseCost : $("#purchaseCost").textbox('getValue'),
			placeCode : $("#placeCode").textbox('getValue'),
			inType : '402880a34e961f16014e9622c4170001',
			storeCost : $("#retailCost").textbox('getValue'),
			remark : $("#remark").textbox('getValue'),
			deliveryNo : $("#deliveryNo").textbox('getValue'),
			producerCode : $("#producerCode").combobox('getValue')
		}).edatagrid('getRows').length - 1;
		$('#infolist').edatagrid('beginEdit', index);
		var rows = $('#infolist').edatagrid("getRows");
		totalRows = cloneObject(rows);
		totalPrice();
		$("#totalDivId").show();
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
	/**
	 * 自动计算零售金额，购入金额
	 * @author  lt
	 * @date 2015-07-22
	 * @version 1.0
	 */
	function sumValue() {
		if ($("#inNum").val() != null && $("#inNum").val() != "") {
			var retailCost = $("#inNum").val() * $("#vdrugPackagingnum").text()
					* $("#vdrugRetailprice").text();
			var purchaseCost = $("#inNum").val()
					* $("#vdrugPackagingnum").text()
					* $("#drugPurchaseprice").val();
			$("#purchaseCost").textbox('setValue', purchaseCost);
			$("#retailCost").textbox('setValue', retailCost);
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
		var drugName = $('#drugNameSerc').val();
		var pinyimaSerc = $('#pinyimaSerc').val();
		var wbSerc = $('#wbSerc').val();
		var zdySerc = $('#zdySerc').val();
		var drugType = $('#CodeDrugtype').combobox('getValue');
		$('#list').datagrid('load', {
			drugName : drugName,
			pinyimaSerc : pinyimaSerc,
			wbSerc : wbSerc,
			zdySerc : zdySerc,
			drugTypeSerc : drugType
		});
	}

	//待入库列表查询
	//页面查询检索
	var arr = [];
	function searchList() {
		var tradeName = $("#tradNameSerc").val();
		var pySerc = $("#pySerc").val();
		var wubiSerc = $("#wubiSerc").val();
		var inputSerc = $("#inputCodeSerc").val();
		if ((tradeName == null || tradeName == "")
				&& (pySerc == null || pySerc == "")
				&& (wubiSerc == null || wubiSerc == "")
				&& (inputSerc == null || inputSerc == "")) {
			//显示全部
			for ( var i = 0; i < arr.length; i++) {
				arr[i].show();
			}
			arr.length = 0;
		} else {
			if (tradeName != null && tradeName != "") {
				for ( var i = 0; i < totalRows.length; i++) {
					var str = totalRows[i].tradeName;
					if (str.indexOf(tradeName) == -1) {//不匹配数据，执行删除
						$("#" + totalRows[i].drugCode).parent().parent()
								.parent().hide();
						arr.push($("#" + totalRows[i].drugCode).parent()
								.parent().parent());
					}
				}
			}
			if (pySerc != null && pySerc != "") {
				for ( var i = 0; i < totalRows.length; i++) {
					var str = totalRows[i].drugNamepinyin;
					if (str.indexOf(pySerc) == -1) {//不匹配数据，执行删除
						$("#" + totalRows[i].drugCode).parent().parent()
								.parent().hide();
						arr.push($("#" + totalRows[i].drugCode).parent()
								.parent().parent());
					}
				}
			}
			if (wubiSerc != null && wubiSerc != "") {
				for ( var i = 0; i < totalRows.length; i++) {
					var str = totalRows[i].drugNamewb;
					if (str.indexOf(wubiSerc) == -1) {//不匹配数据，执行删除
						$("#" + totalRows[i].drugCode).parent().parent()
								.parent().hide();
						arr.push($("#" + totalRows[i].drugCode).parent()
								.parent().parent());
					}
				}
			}
			if (inputSerc != null && inputSerc != "") {
				for ( var i = 0; i < totalRows.length; i++) {
					var str = totalRows[i].drugNameinputcode;
					if (str.indexOf(inputSerc) == -1) {//不匹配数据，执行删除
						$("#" + totalRows[i].drugCode).parent().parent()
								.parent().hide();
						arr.push($("#" + totalRows[i].drugCode).parent()
								.parent().parent());
					}
				}
			}
		}
		totalPrice();
	}

	//计算总的金额及差价
	function totalPrice() {
		var rows = $('#infolist').datagrid('getRows');
		var storeCost = 0;
		var purchaseCost = 0;
		var totalSpread = 0;
		for ( var i = 0; i < rows.length; i++) {
			var id = rows[i].drugCode;
			if (!$('#' + id).parent().parent().parent().is(":hidden")) {//判断是否隐藏
				storeCost += rows[i].storeCost * 1;//因为没有parseDouble()方法      乘以一即可变成数值型
				purchaseCost += rows[i].purchaseCost * 1;//因为没有paseDouble()方法 乘以一即可变成数值型
			}
		}
		$("#totalStoreCost").html(storeCost);
		$("#totalPurchaseCost").html(purchaseCost);
		totalSpread = $("#totalStoreCost").html()- $("#totalPurchaseCost").html();
		$("#totalSpread").html(totalSpread);
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
					showWin("请药品类别", "<c:url value='/ComboxOut.action'/>?xml="+ "CodeDrugtype,0", "50%", "80%");
				}
			}
		}
	}
	//从xml文件中解析，读到下拉框
	function idCombobox(param) {
		$('#' + param).combobox({
			url : "<c:url value='/comboBox.action'/>?str="+param,    
			valueField : 'id',
			textField : 'name',
			multiple : false,
			onLoadSuccess : function() {//请求成功后
				$(list).each(function() {
					if ($('#' + param).val() == this.Id) {
						$('#' + param).combobox("select", this.Id);
					}
				});
			}
		});
	}
	//保存
	function save() {
		var rows = $('#infolist').edatagrid('getChecked');
		if (rows.length > 0) {//选中几行的话触发事件	                        
			$('#infolist').datagrid('acceptChanges');//提交所有从加载或者上一次调用acceptChanges函数后更改的数据。
			$('#infoJson').val(
					JSON.stringify($('#infolist').edatagrid("getSelections")));
			$('#saveForm').form(
					'submit',
					{
						url : "<c:url value='/drug/applyin/saveApplyin.action'/>",    
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
							var arr = [];
							for ( var i = 0; i < rows.length; i++) {
								var dd = $('#infolist').edatagrid(
										'getRowIndex', rows[i]);//获得行索引
								if (arrIds.indexOf(rows[i].drugCode) != -1) {
									arrIds.remove(rows[i].drugCode);
								}
								arr.push(dd);
							}
							for ( var i = arr.length - 1; i >= 0; i--) {
								$('#infolist').edatagrid('deleteRow', arr[i]);//通过索引删除该行
							}
							totalPrice();
						},
						error : function(data) {
							$.messager.progress('close');
							$.messager.alert("操作提示", "保存失败！", "warning");
						}
					});
		} else {
			$.messager.alert("操作提示", "请选择要保存的条目！", "warning");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
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
					var id = rows[i].drugCode;
					if ($('#' + id).parent().parent().parent().is(":hidden")) {//判断是否隐藏
						copyRows.remove(copyRows[i]);
					}
				}
				$('#infolist').datagrid('acceptChanges');//提交所有从加载或者上一次调用acceptChanges函数后更改的数据。
				$('#infoJson').val(JSON.stringify(copyRows));
				$('#saveForm').form('submit', {
					url : "<c:url value='/drug/instore/exportInstore.action'/>?flag=0",    
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
	var win;	
		function showWin (title,url, width, height) {
		   	var content = '<iframe id="myiframe" src="' + url + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
		    var divContent = '<div id="treeDeparWin">';
		    win = $('<div id="treeDeparWin"><div/>').dialog({
		        content: content,
		        width: width,
		        height: height,
		        modal: true,
		        resizable:true,
		        shadow:true,
		        center:true,
		        title: title
		    });
		    win.dialog('open');
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
	//显示最小单位单位格式化 
	function MinUnitFamater(value){
		if(value!=null){
			for(var i=0;i<minUnitList.length;i++){
				if(value==minUnitList[i].id){
					return minUnitList[i].name;
				}
			}	
		}
	}
	//显示药品类型单位格式化    
	function typeFamater(value){
		if(value!=null){
			for(var i=0;i<typeList.length;i++){
				if(value==typeList[i].id){
					return typeList[i].name;
				}
			}	
		}
	}
</script>
	</body>
</html>