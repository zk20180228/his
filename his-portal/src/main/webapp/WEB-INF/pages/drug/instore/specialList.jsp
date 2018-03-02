<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药品特殊入库</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
</head>
	<body>
		<div id="divLayout" class="easyui-layout" data-options="fit:true">
			<div id="northMes" data-options="region:'north'" style="height: 32px;">
				<div style="width:100%;">
					<!-- 药品特殊入库 -->
					<table cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td style="padding: 2px 2px; font-size: 14px" nowrap="nowrap">
								&nbsp;供货公司：
								<input  type="text" ID="companyCode" />
								<a href="javascript:delSelectedData('companyCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
							<td style="padding: 2px 2px;font-size: 14px" nowrap="nowrap">
								入库日期：
								<input ID="inTime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
						</tr>
					</table>
				</div>
			</div>			
			
			<div  data-options="region:'west',border:false" style="width:50%;height:35%;" >
				<div class="easyui-layout" data-options="fit:true">
					<div id="westMes"  data-options="region:'north'" style="height: 32px;">
						<table>
							<tr>
								<td style="padding: 2px 2px;font-size: 14px" nowrap="nowrap">
									&nbsp;药品类型：
									<input type="text" id="CodeDrugtype" style="width: 70px;"/>
								</td>
								<td style="padding: 2px 2px;font-size: 14px" nowrap="nowrap">
									&nbsp;查询条件：
									<input class="easyui-textbox" id="queryName" onkeydown="keyDown()" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:168px" />
								</td>
								<td>
									<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search" >查询</a>
								</shiro:hasPermission>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',border:false">
						<table id="list"  style="overflow-y:auto;height:185px;" data-options="url:'${pageContext.request.contextPath}/drug/stockinfo/getInstoreGeneral.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fitColumns:false,fit:true">
							<thead>
								<tr>
									<th data-options="field:'drugCommonname',width : '30%'">
										药品通用名
									</th>
									<th data-options="field:'deptName',width : '20%'">
										库存科室
									</th>
									<th data-options="field:'drugSpec',width : '17%'">
										药品规格
									</th>
									<th data-options="field:'drugRetailprice', width : '12%'">
										零售价
									</th>
									<th data-options="field:'drugMinimumunit',formatter:minFamater, width : '10%'">
										单位
									</th>
									<th data-options="field:'storeSum', width : '8%'">
										库存
									</th>
								</tr>
							</thead>
						</table>						
					</div>	
				</div>		
				</div>
				<div data-options="region:'center'" id="centerMes" style="height:35%;width: 50%">
					<table cellspacing="5" cellpadding="5" border="0" data-options="fit:true">
						<tr>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								药品名称：
							</td>
							<td id="vdrugName">
							</td>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								规格：
							</td>
							<td id="vdrugSpec">
							</td>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								零售价：
							</td>
							<td id="vdrugRetailprice">
							</td>

						</tr>
						<tr>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								包装数量：
							</td>
							<td id="vdrugPackagingnum">
							</td>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								包装单位：<input id="vdrugPackagingunit" type="hidden">
							</td>
							<td id="showPackUnit">
							</td>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								最小单位：<input id="vdrugMinimumunit" type="hidden">
							</td>
							<td id="showMinUnit">
								<br>
							</td>
						</tr>
					</table>
					<table cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								数量：
							</td>
							<td>
								<input  class="easyui-numberbox" ID="inNum" data-options="required:true" missingMessage="请输入数量"/>
							</td>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								零售金额：
							</td>
							<td>
								<input class="easyui-textbox" type="text" ID="retailCost" readonly="readonly" />
							</td>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								购入金额：
							</td>
							<td>
								<input class="easyui-textbox" type="text" ID="purchaseCost"readonly="readonly" />
							</td>
						</tr>
						<tr>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								批号：
							</td>
							<td>
								<input class="easyui-textbox" type="text" ID="batchNo" data-options="required:true" missingMessage="请输入批号" />
							</td>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;" onkeydown="keyDown1()">
								有效期：
							</td>
							<td>
							<input ID="validDate" class="Wdate" type="text" onClick="WdatePicker({minDate:'%y-%M-%d'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								<!--  <input class="easyui-datebox" type="text" ID="validDate"data-options="required:true,showSeconds:false" missingMessage="请选择有效期" />-->
							</td>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								货位号：
							</td>
							<td>
								<input class="easyui-numberbox" type="text" ID="placeCode" />
							</td>
						</tr>
						<tr>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
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
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								送货单号：
							</td>
							<td>
								<input class="easyui-textbox" type="text" ID="deliveryNo" />
							</td>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								购入价：
							</td>
							<td>
								<input  class="easyui-numberbox" ID="drugPurchaseprice"  data-options="precision:4,required:true" data-options="required:true" missingMessage="请输入购入价"  />
							</td>
						</tr>
						<tr>
							
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								生产厂家：
							</td>
							<td>
								<input class="easyui-combobox" type="text" ID="producerCode"  readonly="readonly"/>
							</td>
							<td style="padding: 7px 15px;font-size: 14px;text-align:right;white-space: nowrap;">
								备注：
							</td>
							<td>
								<input class="easyui-textbox" type="text" ID="remark"  style=" width: 250px;" />
							</td>
							<td style="padding: 7px 15px;">
								<a id="conform" href="javascript:conform();void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-save'">确认</a>
							</td>
						</tr>
					</table>
				</div>
			<div data-options="region:'south',border:false,title:'待入库药品列表',collapsible:false"  style="width:100%;height:60%;">
				<div id="southMes" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north'" style="width: 100%;height: 35px;">
						<input id="drugCode" type="hidden">
						<input id="drugType" type="hidden">
						<input id="drugNamepinyin" type="hidden">
						<input id="drugNamewb" type="hidden">
						<input id="drugNameinputcode" type="hidden">
						<input id="drugCommonname" type="hidden">
						<input id="drugCnamepinyin" type="hidden">
						<input id="drugCnamewb" type="hidden">
						<input id="drugCnameinputcode" type="hidden">
						<table cellspacing="0" cellpadding="0" border="0">
							<tr>
								<td style="padding: 5px 15px;font-size: 14px" nowrap="nowrap">
									查询条件：
									<input class="easyui-textbox" id="tradNameSerc" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:180px"/>
								</td>
								<td>
									&nbsp;&nbsp;
									<a href="javascript:void(0)" onclick="searchList()"class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',border:false" style="width: 100%;">
						<form id="saveForm" method="post" style="height: 88%">
							<input type="hidden" name="infoJson" id="infoJson">					
								<table id="infolist" style="width:100%;height:100px;" 
									data-options="fit:true,url:'',method:'post',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar:'#toolbarId'">							
								</table>
						</form>
						<div id="totalDivId" style="height: 7%">
							<table width="100%">
								<tr>	
									<td style="padding: 2px 15px;">
										<font style="padding-left:40%;">总零售金额：</font>
										<span ID="totalStoreCost" style="color: red;"></span>
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
				</div>
			</div>
				</div>
			</div>
			<div id="toolbarId">
			<shiro:hasPermission name="${menuAlias}:function:save">
				<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a>
			</shiro:hasPermission>
				<a href="javascript:void(0)" onclick="del()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-remove',plain:true">删除</a>
			<shiro:hasPermission name="YPYBRK:function:export">
				<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:true">导出</a>
			</shiro:hasPermission>
			<!--<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
			-->
			</div>
		</div>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
		<script type="text/javascript">
			//定义全局变量记录总的待入库信息条目
			var totalRows = null;
			//记录赋值过的条目id
			var arrIds = [];
			//格式化所用
			var packList = "";
			var minList = "";
			var typeList = "";
			//加载页面
			$(function() {
					
				bindEnterEvent('tradNameSerc',searchList,'easyui');//绑定回车事件
				bindEnterEvent('queryName',searchFrom,'easyui');//绑定回车事件
				
				//初始化下拉框
				//idCombobox("CodeDrugtype");
				idCombobox("drugType");
				//下拉框的keydown事件   调用弹出窗口
				var drugType = $('#CodeDrugtype').combobox('textbox');
				drugType.keyup(function() {
					KeyDown1(0, "CodeDrugtype");
				});
				
				//入库时间默认当前
				var date = new Date();
				var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
				var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
				+ (date.getMonth() + 1);
				var strDate = date.getFullYear() + '-' + month + '-' + day;
				$('#inTime').val(strDate);
				
				//初始化供货单位下拉
				$('#companyCode').combobox({
					url : "<%=basePath%>drug/supply/queryCompanyList.action",
					valueField : 'Id',
					textField : 'companyName',
					width : 200,
					onHidePanel:function(none){
					    var data = $(this).combobox('getData');
					    var val = $(this).combobox('getValue');
					    var result = true;
					    for (var i = 0; i < data.length; i++) {
					        if (val == data[i].Id) {
					            result = false;
					        }
					    }
					    if (result) {
					        $(this).combobox("clear");
					    }else{
					        $(this).combobox('unselect',val);
					        $(this).combobox('select',val);
					    }
					},
					filter: function(q, row){
					    var keys = new Array();
					    keys[keys.length] = 'Id';
					    keys[keys.length] = 'companyName';
					    keys[keys.length] = 'companyPinyin';
					    keys[keys.length] = 'companyWb';
					    keys[keys.length] = 'companyInputcode';
					    return filterLocalCombobox(q, row, keys);
					}
				});
				bindEnterEvent('companyCode',popWinToCompany,'easyui');//绑定回车事件
				//初始化生产厂家下拉
				$('#producerCode').combobox({
					url : "<%=basePath%>drug/manufacturer/queryManufacturerList.action",
					valueField : 'id',
					textField : 'manufacturerName',
					onHidePanel:function(none){
					    var data = $(this).combobox('getData');
					    var val = $(this).combobox('getValue');
					    var result = true;
					    for (var i = 0; i < data.length; i++) {
					        if (val == data[i].id) {
					            result = false;
					        }
					    }
					    if (result) {
					        $(this).combobox("clear");
					    }else{
					        $(this).combobox('unselect',val);
					        $(this).combobox('select',val);
					    }
					},
					filter: function(q, row){
					    var keys = new Array();
					    keys[keys.length] = 'id';
					    keys[keys.length] = 'manufacturerName';
					    keys[keys.length] = 'manufacturerPinyin';
					    keys[keys.length] = 'manufacturerWb';
					    keys[keys.length] = 'inputCode';
					    return filterLocalCombobox(q, row, keys);
					}
				});
				$('#inNum').numberbox('textbox').bind('keyup', function(event) {
					if ($("#inNum").numberbox('getText') != null && $("#inNum").numberbox('getText') != "") {
						var retailCost = $("#inNum").numberbox('getText') * $("#vdrugRetailprice").text();
						var purchaseCost = $("#inNum").numberbox('getText') * $("#drugPurchaseprice").numberbox('getText');
						$("#purchaseCost").textbox('setValue', purchaseCost.toFixed(3));
						$("#retailCost").textbox('setValue', retailCost.toFixed(3));
					}
				});
				$('#drugPurchaseprice').numberbox('textbox').bind('keyup', function(event) {
					if ($("#inNum").numberbox('getText') != null && $("#inNum").numberbox('getText') != "") {
						var purchaseCost = $("#inNum").numberbox('getText') * $("#drugPurchaseprice").numberbox('getText');
						$("#purchaseCost").textbox('setValue', purchaseCost.toFixed(3));
					}
				});
				//添加datagrid事件及分页
				$('#list').datagrid(
						{
							pagination : true,
							pageSize : 20,
							pageList : [ 20, 30, 50, 80, 100 ],
							onBeforeLoad:function (param) {
								$.ajax({
									url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action", 
									data:{"type" : "packunit"},
									type:'post',
									success: function(data) {
										packList = data;
									}
								});
								$.ajax({
									url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action", 
									data:{"type" : "minunit"},
									type:'post',
									success: function(data) {
										minList = data;
									}
								});
								$.ajax({
									url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
									data:{"type" : "drugType"},
									type:'post',
									success: function(data) {
										typeList = data;
									}
					     });
<%-- 								$.ajax({
									url:"<%=basePath%>comboBox.action", 
									data:{"str" : "CodeDrugpackagingunit"},
									type:'post',
									success: function(packUnitdata) {
										packUnitList = eval("("+ packUnitdata +")");
									}
								});
								$.ajax({
									url: "<%=basePath%>comboBox.action",
									data:{"str" : "CodeDrugtype"},
									type:'post',
									success: function(typedata) {
										typeList = eval("("+ typedata +")");
									}
								}); --%>
					        },
							onDblClickRow : function(rowIndex, rowData) {
								$('#inNum').numberbox('setValue','');
								$('#retailCost').textbox('setValue','');
								$('#purchaseCost').textbox('setValue','');
								$('#batchNo').textbox('setValue','');
								$('#validDate').val('');
								$('#drugPurchaseprice').numberbox('setValue',"");
								//$('#companyCode').combobox('setValue','');
								$('#vdrugName').text('');
								$('#vdrugSpec').text('');
								$('#vdrugRetailprice').text('');
								$('#vdrugPackagingnum').text('');
								$('#showPackUnit').text('');
								$('#showMinUnit').text('');
								$('#producerCode').combobox('setValue','');
								$('#placeCode').numberbox('setValue','');
								$('#invoiceNo').textbox('setValue','');
								var row = $('#list').datagrid('getSelected');
								if (row) {
									$("#vdrugName").text(row.drugName);
									$("#vdrugSpec").text(row.drugSpec);
									$("#vdrugRetailprice").text(row.drugRetailprice);
									$("#vdrugPackagingunit").val(row.drugPackagingunit);
									$("#vdrugMinimumunit").val(row.drugMinimumunit);
									$("#vdrugPackagingnum").text(row.drugPackagingnum);
									$("#validDate").val(row.validDate);
									$("#placeCode").textbox("setValue", row.placeCode);
									$("#drugPurchaseprice").numberbox("setValue",row.drugPurchaseprice);
									$("#producerCode").combobox("setValue",row.drugManufacturer);
									$("#drugCode").val(row.drugId);
									$("#drugNamepinyin").val(row.drugNamepinyin);
									$("#drugNamewb").val(row.drugNamewb);
									$("#drugNameinputcode").val(row.drugNameinputcode);
									$("#drugType").val(row.drugType);
									/** 2016年10月18日  GH  药品单位 修改  包装单位和最小单位区分开 **/
									$("#showPackUnit").text(packFamater(row.drugPackagingunit));
									$("#showMinUnit").text(minFamater(row.drugMinimumunit));
									
									$("#drugCommonname").val(row.drugCommonname);
									$("#drugCnamepinyin").val(row.drugCnamepinyin);
									$("#drugCnamewb").val(row.drugCnamewb);
									$("#drugCnameinputcode").val(row.drugCnameinputcode);
								}
							}
						});
				var id = '${id}'; //存储数据ID
				//添加datagrid事件及分页
				$('#infolist').edatagrid({
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
						field : 'drugCommonname',
						title : '药品通用名',
						width : '9%'
					}, {
						field : 'drugCnamepinyin',
						title : '通用拼音码',
						width : '5%'
					}, {
						field : 'drugCnamewb',
						title : '通用五笔码',
						width : '5%'
					}, {
						field : 'drugCnameinputcode',
						title : '通用自定义码',
						width : '5%'
					},{
						field : 'tradeName',
						title : '药品名称',
						hidden: true
					}, {
						field : 'drugNamepinyin',
						title : '拼音码',
						hidden: true
					}, {
						field : 'drugNamewb',
						title : '五笔码',
						hidden: true
					}, {
						field : 'drugNameinputcode',
						title : '自定义码',
						hidden: true
					}, {
						field : 'drugType',
						title : '药品类型',
						width : '5%',
						hidden: true
					}
					, {
						field : 'showDrugType',
						title : '药品类型',
						width : '5%'
					},  {
						field : 'specs',
						title : '规格',
						width : '8%'
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
						field : 'retailPrice',
						title : '零售价',
						width : '5%',
						formatter:function(value,row,index){
				        	return "<div id='retailPrice_"+index+"'>"+value+"</div>";
						}
					}, {
						field : 'purchasePrice',
						title : '购入价',
						width : '5%',
						formatter:function(value,row,index){
				        	return "<div id='purchasePrice_"+index+"'>"+value+"</div>";
						}
					},{
						field : 'inNum',
						title : '入库数量',
						formatter:function(value,row,index){
				        	var retVal = "";
							if (value != null && value != "" && !isNaN(value) && value >0) {
								retVal = value;
							}else{
								retVal = '';
							}
					        return "<input type='text' id='"+row.id+"_"+index+"' value='"+retVal+"' onChange = 'upperCase(this)'>";
				        },
						width : '5%'
					}, {
						field : 'storeCost',
						title : '零售金额',
						width : '5%',
						formatter:function(value,row,index){
				        	if (value != null && value != "" && !isNaN(value) && value >0) {
								return value;
							}else{
								return '';
							}
				        	}
					}, {
						field : 'purchaseCost',
						title : '购入金额',
						width : '5%',
						formatter:function(value,row,index){
				        	if (value != null && value != "" && !isNaN(value) && value >0) {
								return value;
							}else{
								return '';
							}
				        	}
					},{
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
					},   {
						field : 'companyCode',
						title : '目标科室',
						width : '4%',
						hidden: true
					}, {
						field : 'showCompanyCode',
						title : '目标科室',
						width : '5%',
						hidden: true
					}, {
						field : 'vIndate',
						title : '入库日期',
						width : '5%'
					}, {
						field : 'remark',
						title : '备注',
						formatter:function(value,row,index){
				        	var retVal = "";
				        	if(value!=null&&value!=""){
				        		retVal = value;
				        	}
				        	return "<input type='text' id='remark_"+index+"' value='"+retVal+"'>";
				        },
						width : '5%'
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
// 				//有效期
// 				$('#validDate').datebox().datebox('calendar').calendar({
// 								validator: function(date){
// 									var now = new Date();
// 									var startTime = new Date(now.getFullYear(), now.getMonth(), now.getDate());
// 									return startTime <= date ;
// 								}
// 							});
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
						if ($("#validDate").val() != null
								&& $("#validDate").val() != "") {
							if ($("#drugPurchaseprice").val() != null
									&& $("#drugPurchaseprice").val() != "") {
								if ($("#inTime").val() != null
										&& $("#inTime").val() != "") {
									if ($("#companyCode").combobox('getValue') != null
											&& $("#companyCode").combobox('getValue') != "") {
										if (rows.length > 0) {
											if (arrIds.indexOf($("#drugCode").val()) == -1) {
												appendValue();
												arrIds.push($("#drugCode").val());
											} else {
												$.messager.alert("操作提示","待入库列表已有此药品请直接双击编辑！","warning");
												setTimeout(function(){
													$(".messager-body").window('close');
												},3500);
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
				var placeCodeval=$("#placeCode").textbox('getValue');
				if(placeCodeval==""){
					$("#placeCode").textbox('setValue', 0);
				}
				var index = $('#infolist').edatagrid('appendRow', {
					tradeName : $("#vdrugName").text(),
					drugNamepinyin : $("#drugNamepinyin").val(),
					drugNamewb : $("#drugNamewb").val(),
					drugNameinputcode : $("#drugNameinputcode").val(),
					drugCommonname : $("#drugCommonname").val(),
					drugCnamepinyin : $("#drugCnamepinyin").val(),
					drugCnamewb : $("#drugCnamewb").val(),
					drugCnameinputcode : $("#drugCnameinputcode").val(),
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
					vValidDate : $("#validDate").val(),
					vIndate : $("#inTime").val(),
					companyCode : $("#companyCode").combobox('getValue'),
					showCompanyCode : $("#companyCode").combobox('getText'),
					drugCode : $("#drugCode").val(),
					purchasePrice : $("#drugPurchaseprice").val(),
					purchaseCost : $("#purchaseCost").textbox('getValue'),
					placeCode : $("#placeCode").textbox('getValue'),
					storeCost : $("#retailCost").textbox('getValue'),
					remark : $("#remark").textbox('getValue'),
					deliveryNo : $("#deliveryNo").textbox('getValue'),
					producerCode : $("#producerCode").combobox('getValue'),
					invoiceNo: $("#invoiceNo").textbox('getValue')
				}).edatagrid('getRows').length - 1;
				$('#infolist').edatagrid('beginEdit', index);
				var rows = $('#infolist').edatagrid("getRows");
				totalRows = cloneObject(rows);
				totalPrice();
				$("#totalDivId").show();
			}
			//保存
			function save() {
				var rows = $('#infolist').edatagrid('getChecked');
				if (rows.length > 0) {//选中几行的话触发事件	                        
					$('#infolist').datagrid('acceptChanges');//提交所有从加载或者上一次调用acceptChanges函数后更改的数据。
					$('#infoJson').val(
							JSON.stringify($('#infolist').edatagrid("getSelections")));
					
					$('#saveForm').form('submit',{
						url :"<%=basePath%>drug/instore/saveSpecialInstore.action", 
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
							var data = eval('(' + data + ')');
							if(data.resCode == "error"){
								$.messager.alert('提示',data.resMes,"warning");
								return false;
							}
							$.messager.alert('提示',"入库成功！");
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
							$('#inNum').numberbox('setValue','');
							$('#retailCost').textbox('setValue','');
							$('#purchaseCost').textbox('setValue','');
							$('#batchNo').textbox('setValue','');
							$('#validDate').val('');
							$('#drugPurchaseprice').numberbox('setValue',"");
							$('#companyCode').combobox('setValue','');
							$('#vdrugName').text('');
							$('#vdrugSpec').text('');
							$('#vdrugRetailprice').text('');
							$('#vdrugPackagingnum').text('');
							$('#showPackUnit').text('');
							$('#showMinUnit').text('');
							$('#producerCode').combobox('setValue','');
							$('#invoiceNo').combobox('setValue','');
							$('#placeCode').combobox('setValue','');
							$('#deliveryNo').combobox('setValue','');
							$('#remark').combobox('setValue','');
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
				var queryName = $.trim($('#queryName').textbox('getValue'));
				var drugType = $('#CodeDrugtype').combobox('getValue');
				$('#list').datagrid('load', {
					drugName : queryName,
					drugTypeSerc : drugType
				});
			}
		
			//待入库列表查询
			//页面查询检索
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
				totalPrice();
			}
			function upperCase(obj){
				var val = $(obj).val();
				var index = $(obj).prop("id").split("_");
				var i = parseInt(index[1]);
				var retailPrice = $('#retailPrice_' + i).text();
				var purchasePrice = $('#purchasePrice_' + i).text();
				var storeCost = (retailPrice * val).toFixed(4);
				var purchaseCost = (purchasePrice * val).toFixed(4);
				var rowsInfolist = $('#infolist').datagrid('getRows');
				var inNum = rowsInfolist[i].inNum;
				var remark = document.getElementById('remark_' + i).value;
				var index = $('#infolist').datagrid('updateRow', {
					index : i,
					row : {
						inNum : val,
						storeCost : storeCost,
						purchaseCost : purchaseCost,
						remark : remark
					}
				}).edatagrid('getRows').length - 1;
				//更新一下总金额
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
				$("#totalSpread").html(totalSpread.toFixed(2));
				
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
					url :"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type="+param,  
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
								if (arrIds.indexOf(rows[i].drugCode) != -1) {
									arrIds.remove(rows[i].drugCode);
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
							url :"<%=basePath%>drug/instore/exportInstore.action?flag=3",
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
			//显示包装单位和包装单位格式化
			function packFamater(value){
				if(value!=null){
					for(var i=0;i<packList.length;i++){
						if(value==packList[i].encode){
							return packList[i].name;
						}
					}	
				}
			}
			//显示最小单位和最小单位格式化
			function minFamater(value){
				if(value!=null){
					for(var i=0;i<minList.length;i++){
						if(value==minList[i].encode){
							return minList[i].name;
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
			 /**
			   * 回车弹出供货公司弹框
			   * @author  zhuxiaolu
			   * @param textId 页面上commbox的的id
			   * @date 2016-03-22 14:30   
			   * @version 1.0
			   */
			   function popWinToCompany(){
					$('#companyCode').combobox('setValue','');
					var tempWinPath = "<%=basePath%>popWin/popWinSupplyCompany/toDrugSupplycompanyPopWin.action?textId=companyCode";
					window.open (tempWinPath,'newwindow',' left=150,top=70,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-150)+',scrollbars,resizable=yes,toolbar=yes')
				}
		</script>
	</body>
</html>