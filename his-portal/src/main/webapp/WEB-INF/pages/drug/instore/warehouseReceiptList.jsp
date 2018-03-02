<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药品发票入库</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
			//定义全局变量记录总的待入库信息条目
			var totalRows = null;
			//记录赋值过的条目id
			var arrIds = [];
			//格式化所用
			var packUnitList = "";
			//加载页面
			$(function() {
				//初始化下拉框
				idCombobox("drugType");
				//下拉框的keydown事件   调用弹出窗口
				var drugType = $('#CodeDrugtype').combobox('textbox');
				drugType.keyup(function() {
					KeyDown1(0, "CodeDrugtype");
				});
				
				bindEnterEvent('tradNameSerc',searchList,'easyui');//绑定回车事件
				bindEnterEvent('queryName',searchFrom,'easyui');//绑定回车事件
				
				$('#list').datagrid({ 
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 30, 50, 80, 100 ],
					onBeforeLoad:function (param) {
						$.ajax({
							url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action", 
							/** 2016年10月18日  GH  包装单位和最小单位区分开 **/
							data:{"type" : "packunit"},
							type:'post',
							success: function(data) {
								packUnitList = data;
							}
						});
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
						}},
					onDblClickRow : function(rowIndex, rowData) {//双击查看
						
						var row = $('#list').datagrid('getSelected');
						if(row){
		                  	   attrValue(row);
			   				}
					}
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
					},{
						field : 'id',
						title : '出库记录Id',
						hidden : true
					},
					{
						field : 'drugCode',
						title : '药品id',
						hidden : true
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
						hidden : true
					},{
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
					},{
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
					}, {
						field : 'drugCnameinputcode',
						title : '通用自定义码',
						hidden : true
					}, {
						field : 'specs',
						title : '规格',
						width : '100'
					}, {
						field : 'retailPrice',
						title : '零售价',
						align: 'right',
						width : '100',
						formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}
					}, {
						field : 'packUnit',
						title : '包装单位',
						hidden:true
					}, {
						field : 'showPackUnit',
						title : '包装单位',
						width : '100'
					}, {
						field : 'packQty',
						title : '包装数量',
						width : '100'
					}, {
						field : 'inNum',
						title : '入库数量',
						width : '100'
					}, {
						field : 'invoiceNo',
						title : '发票号',
						editor : {
							type : 'textbox',
							options : {
								required : true,
								validType : ['text','length[0,10]']
							},
							missingMessage : '请填写发票号'
						},
						width : '100'
					}, {
						field : 'purchasePrice',
						title : '购入价',
						align: 'right',
						width : '100',
						formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}
					}, {
						field : 'remark',
						title : '备注',
						editor : {
							type : 'textbox'
						},
						width : '100'
					} ] ]
				});
			});
			
			//判断是否已经填充到待出库列表
			function attrValue(row){
				var rows = $('#infolist').datagrid("getRows");
				if(rows.length>0){
					if(arrIds.indexOf(row.id) == -1){ //del方法 删除的是临时数据中的id  所以双击赋值方法应该判断id是否存在
						conform(row);
						arrIds.push(row.id);
					}else{
						$.messager.alert("操作提示", "待入库列表已有此条记录！","warning");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}
				}else{
					conform(row);
					if(arrIds.indexOf(row.id)==-1){
						arrIds.push(row.id);
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
					specs : row.drugSpec,
					retailPrice : row.drugRetailprice,
					packUnit : row.drugPackagingunit,
					showPackUnit : packUnitFamater(row.drugPackagingunit),
					packQty : row.drugPackagingnum,
					inNum : row.inNum,
					retailCost : row.retailCost,
					drugCode : row.drugCode,
					purchasePrice : row.purchasePrice,
					remark : row.remark,
					placeCode : row.placeCode,
					invoiceNo : row.invoiceNo,
					id : row.id
				}).edatagrid('getRows').length - 1;
				$('#infolist').edatagrid('beginEdit', index);
				var rows = $('#infolist').edatagrid("getRows");
				totalRows = cloneObject(rows);
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
					url :'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type='+ param,
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
				if(arr.length==0 && totalRows==null){
					return;
				}
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
			//保存
			function save() {
				var rows = $('#infolist').edatagrid('getSelections');
				if (rows.length > 0) {//选中几行的话触发事件	                        
					$('#infolist').datagrid('acceptChanges');//提交所有从加载或者上一次调用acceptChanges函数后更改的数据。
					var info = $('#infolist').edatagrid("getSelections");
					$('#infoJson').val(JSON.stringify($('#infolist').edatagrid("getSelections")));
					$('#saveForm').form('submit',{
						url : "<%=basePath%>drug/instore/updateInstore.action?flag=1",
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
							url : "<%=basePath%>drug/instore/exportInstore.action?flag=1",
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
		<div id="divLayout" class="easyui-layout" fit=true>
			<!-- 发票入库 -->
			<div data-options="region:'west',split:false,iconCls:'icon-book'" style="width: 40% ;border-top:0">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north'" style="height:40px;border-top:none; border-right:none;padding-top: 6px;border-top:0">
						<table cellspacing="0" cellpadding="0"  style="padding: 0px 0px 0px 4px;">
							<tr>
								<td nowrap="nowrap">
									药品类型：
									<input type="text" id="CodeDrugtype" style="width:80px"/>
								</td>
								<td nowrap="nowrap" >
									&nbsp;查询条件：
									<input class="easyui-textbox" id="queryName" onkeydown="keyDown()" data-options="prompt:'通用名,拼音,五笔,自定义,入库单号'" style="width:140px"/>
								</td>
								<td>
									<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search" style="width:62px">查询</a>
								</shiro:hasPermission>
								<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div id="q" data-options="region:'center',border:false">
						<table id="list" style="height: 535px" data-options="url:'${pageContext.request.contextPath}/drug/instore/queryInstore.action?flag=0&menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fitColumns:false,fit:true">
							<thead>
								<tr>
									<th data-options="field:'inListCode',width : '130'">
										入库单据号
									</th>
									<th data-options="field:'drugCommonname',width : '200'">
										药品通用名
									</th>
									<th data-options="field:'drugSpec',width : '100'">
										药品规格
									</th>
									<th data-options="field:'inNum',width : '65'">
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
			<div data-options="region:'center'" style="width: 40% ;border-top:0">
				<div id="divLayout" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',split:false" style="height: 40px;border-top:none; border-left:none;">
						<input id="drugCode" type="hidden">
						<table cellspacing="0" cellpadding="0" border="0" style="padding: 5px 0px 0px 4px;"> 
							<tr>
								<td style="font-size: 14px" nowrap="nowrap">
									查询条件：
									<input class="easyui-textbox" id="tradNameSerc" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:180px"/>
								</td>
								<td>
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
								data-options="fit:true,url:'',method:'post',idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar:'#toolbarId'">
							</table>
						</form>
					</div>
			</div>
			<div id="toolbarId">
				<shiro:hasPermission name="${menuAlias}:function:save">
					<a href="javascript:void(0)" onclick="save()"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-save',plain:true">保存</a>
				</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="del()"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-remove',plain:true">删除</a>
				<shiro:hasPermission name="${menuAlias}:function:export">
					<a href="javascript:void(0)" onclick="exportList()"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-down',plain:true">导出</a>
				</shiro:hasPermission>
			</div>
		</div>
	</body>
</html>