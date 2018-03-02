<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药品核准入库 </title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
			//定义全局变量记录总的待入库信息条目
			var totalRows = null;
			//记录赋值过的条目id
			var arrIds = [];
			var packUnitList = "";
			//加载页面
			$(function() {
				
				bindEnterEvent('tradNameSerc',searchList,'easyui');//绑定回车事件
				bindEnterEvent('queryName',searchFrom,'easyui');//绑定回车事件
				//初始化下拉框
				idCombobox("drugType");
				//下拉框的keydown事件   调用弹出窗口
				var drugType = $('#CodeDrugtype').combobox('textbox');
				drugType.keyup(function() {
					KeyDown1(0, "CodeDrugtype");
				});
				//初始化供货单位下拉
				$('#companyCode').combobox({
					url :"<%=basePath%>drug/supply/queryCompanyList.action",
					valueField : 'Id',
					textField : 'companyName',
					width : 200
					
				});
				$.ajax({
					url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
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
					onDblClickRow : function(rowIndex, rowData) {//双击查看
						var row = $('#list').datagrid('getSelected');	
						if(row){
		                  	   attrValue(row);
			   				}
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
			});
			$(function() {
				$('#infolist').edatagrid({
					striped : true,
					checkOnSelect : true,
					selectOnCheck : true,
					singleSelect : false,
					fitColumns : false,
					columns : [ [ {
						field : 'ck',
						checkbox : true
					}, {
						field : 'id',
						title : '出库记录Id',
						hidden : true
					},{
						field : 'inBillCode',
						title : '入库单流水号',
						hidden : true
					},{
						field : 'inListCode',
						title : '入库单号',
						hidden : true
					},{
						field : 'drugCode',
						title : '药品id',
						hidden : true
					}, {
						field : 'flagId',
						title : 'flagId',
						hidden : true,
						formatter : function(value, row, index) {
							return "<div id='"+row.drugCode+"'></div>";
						}
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
					}, {
						field : 'drugCommonname',
						title : '药品通用名',
						width : '200'
					}, {
						field : 'drugCnamepinyin',
						title : '通用拼音码',
						hidden : true
					}, {
						field : 'drugCnamewb',
						title : '通用五笔码',
						hidden : true
					},{
						field : 'specs',
						title : '规格',
						width : '100'
					}, {
						field : 'batchNo',
						title : '批号',
						width : '60'
					}, {
						field : 'packUnit',
						title : '包装单位',
						formatter : packUnitFamater,
						width : '80'
					}, {
						field : 'showPackUnit',
						title : '包装单位',
						hidden : true
					}, {
						field : 'packQty',
						title : '包装数量',
						width : '80'
					}, {
						field : 'inNum',
						title : '入库数量',
						hidden : true
					}, {
						field : 'invoiceNo',
						title : '发票号',
						width : '100'
					}, {
						field : 'purchasePrice',
						title : '购入价',
						width : '80',
						formatter : function(value, row, index) {
							var retVal = "";
							if (value != null && value != "" && !isNaN(value) && value >0) {
								retVal = value;
							}else{
								reVal = '';
							}
							return "<input type='text' id='"
									+ row.id
									+ "_"
									+ index
									+ "' value='"
									+ retVal
									+ "' onChange = 'upperCase(this)' style='width: 100%;'>";
						}
					}, {
						field : 'applyNum',
						title : '申请数量',
						width : '80',
						formatter : function(value, row, index) {
							return "<div id='applyNum_"+index+"'>"+ value+"</div>";
						}
					}, {
						field : 'remark',
						title : '备注',
						width : '100',
						formatter : function(value, row, index) {
							var retVal = "";
							if (value != null && value != "") {
								retVal = value;
							}
							return "<input type='text' id='remark_"+index+"' value='"+retVal+"' style='width: 100%;'>";
						}
					} , {
						field : 'companyId',
						title : '供货公司id',
						hidden : true
					}] ]
				});
			});
			
			//判断是否已经填充到待出库列表
			function attrValue(row){
				var rows = $('#infolist').datagrid("getRows");
				var tag = row.id;
				if(rows.length>0){
					if(arrIds.indexOf(tag) == -1){
						conform(row);
						arrIds.push(tag);
					}else{
						$.messager.alert("操作提示", "待入库列表已有此条记录！","warning");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}
				}else{
					conform(row);
					if(arrIds.indexOf(tag)==-1){
						arrIds.push(tag);
					}
				}
			}
			//确认把数据填充到待出库列表
			function conform(row){
					var index = $('#infolist').edatagrid('appendRow', {
						tradeName : row.drugName,
						drugNamepinyin : row.drugNamepinyin,
						drugNamewb : row.drugNamewb,
						drugNameinputcode : row.drugNameinputcode,
						drugCommonname : row.drugCommonname,
						drugCnamepinyin : row.drugCnamepinyin,
						drugCnamewb : row.drugCnamewb,
						drugCnameinputcode : row.drugCnameinputcode,
						drugType : row.drugType,
						drygQuality : row.drugQuality,
						specs : row.drugSpec,
						batchNo:row.batchNo,
						retailPrice : row.drugRetailprice,
						packUnit : row.drugPackagingunit,
						showPackUnit:packUnitFamater(row.drugPackagingunit),
						packQty : row.drugPackagingnum,
						inNum : row.inNum,
						retailCost : row.retailCost,
						drugCode : row.drugCode,
						purchasePrice : row.purchasePrice,
						purchaseCost : (row.purchasePrice * row.inNum).toFixed(4),
						remark : row.remark,
						placeCode : row.placeCode,
						invoiceNo : row.invoiceNo,
						companyCode : row.companyCode,
						applyNum : row.inNum,
						id : row.id,
						companyId : row.companyId,
						inBillCode : row.inBillCode,
						inListCode :row.inListCode
					}).edatagrid('getRows').length - 1;
					$('#infolist').edatagrid('beginEdit', index);
					var rows = $('#infolist').edatagrid("getRows");
					totalRows = cloneObject(rows);
			}
			function ids(rows){
				var map={};
				for ( var i = 0; i < rows.length; i++) {
					var keyOf=rows[i].inListCode+','+rows[i].companyCode+','+rows[i].invoiceNo;
					var id=rows[i].id;
					if(map[keyOf]){
						id=map[keyOf]+','+id;
						map[keyOf]=id;
					}else{
						map[keyOf]=id;
					}
				}
				return map;
			}
			var res = '';
			//保存
			function save() {
				var rows = $('#infolist').edatagrid('getSelections');
				if (rows.length > 0) {//选中几行的话触发事件	
					rowsAll=rows;
					$('#infolist').datagrid('acceptChanges');//提交所有从加载或者上一次调用acceptChanges函数后更改的数据。
					$('#infoJson').val(JSON.stringify($('#infolist').edatagrid("getSelections")));
					$('#saveForm').form('submit',{
						url :"<%=basePath%>drug/instore/updateInstore.action?flag=2",
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
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								return false;
							}
							$('#list').edatagrid("reload");
							var arr = [];
							for ( var i = 0; i < rows.length; i++) {
								var dd = $('#infolist').edatagrid('getRowIndex', rows[i]);//获得行索引
								if (arrIds.indexOf(rows[i].id) != -1) {
									arrIds.remove(rows[i].id);
								}
								arr.push(dd);
							}
							var id="";
							var codes="";
							$.messager.confirm('确认对话框', '保存成功,是否打印药品入库单', function(r){
								if (r){
									var idsMap=ids(rows);
									for(var key in idsMap){  
							 			reportIds = idsMap[key];
							 			reportListCodes = key.split(',')[0];
							 			if(id!=""){
											id+=",";
										}
										id+=reportIds;
										if(codes!=""){
											codes+=",";
										}
										codes+=reportListCodes;
									} 
									var timerStr = new Date().getTime();
							 		window.open("<%=basePath%>drug/approve/iReportToYPRKD.action?randomId="+timerStr+"&reportIds="+id+"&fileName=yaopinrukudan_new&reportListCodes="+codes,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
								}/**
								 *删除选中
								 */
								for ( var i = arr.length - 1; i >= 0; i--) {
									$('#infolist').edatagrid('deleteRow', arr[i]);//通过索引删除该行
								}
								$('#infolist').datagrid('clearSelections');
							});
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
			/**
			 * 药品列表查询
			 */
			function searchFrom() {
				var queryName = $.trim($('#queryName').textbox('getValue'));
				var drugType = $('#CodeDrugtype').combobox('getValue');
				$('#list').datagrid('load', {
					drugName : queryName,
					drugTypeSerc : drugType
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
							showWin("请药品类别","<%=basePath%>ComboxOut.action?xml="+"CodeDrugtype,0", "50%", "80%");
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
					onSelect : function(record) {//请求成功后
						var drugType = $('#CodeDrugtype').combobox('getValue');
						$('#list').datagrid('load', {
							drugTypeSerc : drugType
						});
					}
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
			}
			//计算总的金额及差价
			function totalPrice() {
				var rows = $('#infolist').datagrid('getRows');
				var retailCost = 0;
				var purchaseCost = 0;
				var totalSpread = 0;
				for ( var i = 0; i < rows.length; i++) {
					var id = rows[i].drugCode;
					if (!$('#' + id).parent().parent().parent().is(":hidden")) {//判断是否隐藏
						retailCost += rows[i].retailCost * 1;//因为没有parseDouble()方法      乘以一即可变成数值型
						purchaseCost += rows[i].purchaseCost * 1;//因为没有paseDouble()方法 乘以一即可变成数值型
					}
				}
				$("#totalStoreCost").val(retailCost);
				$("#totalPurchaseCost").val(purchaseCost);
				totalSpread = $("#totalStoreCost").val()
						- $("#totalPurchaseCost").val();
				$("#totalSpread").val(totalSpread);
		
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
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
			//导出列表
			function exportList() {
				var rows = $('#infolist').edatagrid("getRows");
				//为了结束编辑行 不然获取的rows里没有编辑状态下的数据
				for ( var i = 0; i < rows.length; i++) {
					$('#infolist').edatagrid("endEdit",
							$('#infolist').edatagrid("getRowIndex", rows[i]));
				}
				var copyRows = cloneObject(rows);
				$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否删除
					if (res) {
						for ( var i = 0; i < rows.length; i++) {
							var id = rows[i].drugCode;
							if ($('#' + id).parent().parent().parent().is(":hidden")) {//判断是否隐藏
								copyRows.remove(copyRows[i]);
							}
						}
						$('#infoJson').val(JSON.stringify(copyRows));
						$('#saveForm').form('submit', {
							url :"<%=basePath%>drug/instore/exportInstore.action?flag=2",
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
			//显示包装单位格式化
			function packUnitFamater(value){
				if(value!=null){
					for(var i=0;i<packUnitList.length;i++){
						if(value==packUnitList[i].encode){
							return packUnitList[i].name;
						}
					}	
				}
			}
			function upperCase(obj){
				var val = $(obj).val();
				var index = $(obj).prop("id").split("_");
				var i = parseInt(index[1]);
				var applyNum = $('#applyNum_' + i).text();
				var remark = document.getElementById('remark_' + i).value;
				var purchaseCost = applyNum * val;
				$('#infolist').edatagrid('endEdit', i);
				var index = $('#infolist').datagrid('updateRow', {
					index : i,
					row : {
						purchasePrice : val,
						purchaseCost : (applyNum * val).toFixed(4),
						remark : remark
					}
				}).edatagrid('getRows').length - 1;
				$('#infolist').edatagrid('beginEdit', index);
			}
			// 药品列表查询重置
    		function searchReload() {
    			$('#CodeDrugtype').combobox('setValue','');
    			$('#queryName').textbox('setValue','');
    			searchFrom();
    		}
    		// 药品列表查询重置
    		function searchReloadNext() {
    			$('#tradNameSerc').textbox('setValue','');
    			searchList();
    		}
		</script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
	<body>
		<!-- 药品核准入库 -->
		<div id="divLayout" class="easyui-layout" fit=true>
			<div data-options="region:'west',split:false,iconCls:'icon-book'" style="width: 40% ;border-top:0">
				<div id="divLayout" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north'" style="height: 40px;border-top:none; border-right:none;">
						<table cellspacing="0" cellpadding="0" border="0" style="padding-top:5px;">
							<tr>
								<td nowrap="nowrap" class="approveList">
									药品类型：
									<input type="text" id="CodeDrugtype" style="width:125px"/>&nbsp;
									查询条件：
									<input class="easyui-textbox" id="queryName" onkeydown="keyDown()" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:120px"/>
									
									<shiro:hasPermission name="${menuAlias}:function:query">
										<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</shiro:hasPermission>
									<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',split:false,iconCls:'icon-book',border:false" style="width: 100%;height: 100%">
						<table id="list"
							data-options="url:'${pageContext.request.contextPath}/drug/instore/queryInstore.action?flag=1&menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fit:true,fitColumns:false">
							<thead>
								<tr>
									<th data-options="field:'inListCode',width : '130'">
										入库单据号
									</th>
									<th data-options="field:'invoiceNo',width : '100'">
										发票号
									</th>
									<th data-options="field:'drugCommonname',width : '200'">
										药品通用名
									</th>
									<th data-options="field:'drugSpec',width : '80'">
										药品规格
									</th>
									<th data-options="field:'inNum',width : '80'">
										入库数量
									</th>
									<th data-options="field:'companyId',width : '200'">
										供货单位
									</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			<div data-options="region:'center'" style="height:100%; width: 60%;border-top:0">
				<div id="divLayout" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',split:false" style="height: 40px; border-top:none; border-left:none;">
						<input id="drugCode" type="hidden">
						<table cellspacing="0" cellpadding="0" border="0" style="width: 100%;">
							<tr>
								<td style="padding: 5px 15px;font-size: 14px" nowrap="nowrap">
									查询条件：
									<input class="easyui-textbox" id="tradNameSerc" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:180px"/>
									<a href="javascript:void(0)" onclick="searchList()"class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="searchReloadNext()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',split:false,iconCls:'icon-book',border:false" style="width: 100%;">
						<form id="saveForm" method="post" style="height: 100%">
							<input type="hidden" name="infoJson" id="infoJson">
								<table id="infolist" 
									data-options="fit:true,url:'',method:'post',idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fit:true,fitColumns:false,toolbar:'#toolbarId'">
								</table>
						</form>
						<form id="printForm" method="post">
							<input type="hidden" name="reportListCodes" id="reportListCodes" value=""/>
							<input type="hidden" name="reportIds" id="reportIds" value=""/>
							<input type="hidden" name="randomId" id="randomId" value=""/>
							<input type="hidden" name="fileName" id="fileName" value="yaopinrukudan"/>
						</form>
					</div>
				</div>
			</div>
			<div id="toolbarId">
				<shiro:hasPermission name="${menuAlias}:function:save">
					<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a>
				</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="del()"class="easyui-linkbutton"data-options="iconCls:'icon-remove',plain:true">删除</a>
				<shiro:hasPermission name="${menuAlias}:function:export">
					<a href="javascript:void(0)" onclick="exportList()"class="easyui-linkbutton"data-options="iconCls:'icon-down',plain:true">导出</a>
				</shiro:hasPermission>
			</div>
		</div>
	</body>
</html>