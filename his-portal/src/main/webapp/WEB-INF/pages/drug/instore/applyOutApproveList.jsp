<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药房内部出库审核(数据源是--药房内部入库申请--)</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
		//定义全局变量记录总的待出库信息条目
		var totalRows = "";
		//记录已经存在待出库列表条目药品id
		var arrIds = [];
		//标记出库数量为0或者出库数量小于库存数量
		var ischeck=true;
		var packUnitList = "";
		//加载页面
		$(function(){
			
			bindEnterEvent('applyBillcode',searchBillCode,'easyui');//绑定回车事件
			bindEnterEvent('tradNameSerc',searchList,'easyui');//绑定回车事件

			//初始化出库科室下拉
			$('#deptList').combobox({
				url :"<%=basePath%>baseinfo/department/getDeptBySessionDept.action?loginDepteId="+$('#loginDeptId').val()+"&type=2",
				valueField : 'deptCode',
				textField : 'deptName',
				width : 170,
				onBeforeLoad:function (param) {
					$.ajax({
						url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
						data:{"type" : "packunit"},
						type:'post',
						success: function(data) {
							packUnitList = data;
						}
					});
				},
				onSelect:function(record){
					var deptList = record.deptCode;
					var applyBillcode = $.trim($('#applyBillcode').textbox('getValue'));
					$('#infolist').datagrid('loadData', { total: 0, rows: [] });
					$('#list').datagrid('load', {
						deptId : deptList,
						applyBillcode : applyBillcode
					});
				}
			});
			//申请单列表加载
			$('#list').datagrid({
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 30, 50, 80, 100 ],
					url:'<%=basePath%>drug/approve/queryApplyOut.action?menuAlias=${menuAlias}',
					onDblClickRow: function (rowIndex, rowData) {
						$('#deptId').val(rowData.deptCode);
						$('#applyBill').val(rowData.applyBillcode);
						var applyBillcode = rowData.applyBillcode;
						var deptId = rowData.deptCode;
						$('#infolist').datagrid('loadData', { total: 0, rows: [] });
						$('#adulist').datagrid('loadData', { total: 0, rows: [] });
						$.messager.progress({text:'查询中，请稍后...',modal:true});	// 显示进度条
						$.ajax({
							url:"<%=basePath%>drug/approve/queryApplyOutByNo.action",
							data:{"deptId" : deptId,"applyBillcode" : applyBillcode},
							type:'post',
							success: function(data) {
								$.messager.progress('close');
								var list1 = data.list1;//待入库药品信息
								var list2 = data.list2;//带调价药品信息
								if(list1.length > 0){
									$('#infolist').datagrid('loadData', list1);
								}
								if(list1.length > 0){
									$('#adulist').datagrid('loadData', list2);
								}
							},
							error : function(){
								$.messager.progress('close');
							}
						});
						totalPrice();
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
			//待入库药品列表加载
			$('#adulist').edatagrid({
				striped : true
			})
			//待入库药品列表加载
			$('#infolist').edatagrid({
				striped : true,
				checkOnSelect : true,
				selectOnCheck : true,
				singleSelect : false,
				fitColumns : false,
				idField : 'id',
				onBeforeLoad:function (param) {
					var row = $('#list').datagrid('getSelected');
					var deptList = $('#deptId').val()
					var applyBillcode = $('#applyBill').val();
					if(row == null && deptList == "" && applyBillcode == ''){
						return false;
					}
		        },
		      	//改变颜色
				rowStyler: function(index,row){
					if(row.storeSumDrug <= 0){
							return 'background-color:#FF0000;color:black;';
					}
				},
				columns : [ [ {
					field : 'ck',
					checkbox : true
				}, {
					field : 'id',
					title : 'id',
					hidden : true
				}, {
					field : 'drugCnamepinyin',
					hidden : true
				}, {
					field : 'drugCnamewb',
					hidden : true
				}, {
					field : 'drugCnameinputcode',
					hidden : true
				}, {
					field : 'deptCode',
					title : '申请科室',
					hidden : true
				}, {
					field : 'applyNumber',
					title : '申请流水号',
					hidden : true
				}, {
					field : 'drugDeptCode',
					title : '出库科室',
					hidden : true
				}, {
					field : 'drugDeptName',
					title : '出库科室',
					hidden : true
				}, {
					field : 'deptName',
					title : '申请科室',
					hidden : true
				}, {
					field : 'drugCode',
					title : '药品id',
					hidden : true
				}, {
					field : 'tradeName',
					title : '药品通用名',
					width : '25%'
				}, {
					field : 'storeSumDrug',
					title : '库存数量',
					formatter: function(value,row,index){
						if (value != null && value != ''){
							return value.toFixed(2);
						} else {
							return 0;
						}
					},
					width : '8%'
				}, {
					field : 'applyNum',
					title : '申请数量',
					formatter: function(value,row,index){
						if (value != null && value != ''){
							return value.toFixed(2);
						} else {
							return 0;
						}
					},
					width : '7%'
				}, {
					field : 'outNum',
					title : '出库数量',
					width : '7%',
					formatter : function(value, row, index) {
						var retVal = 0;
						if (value != null && value != "" && !isNaN(value) && value >0) {
							retVal = value;
						}else{
							reVal = 0;
						}
						return "<input type='text' class='easyui-numberbox' id='"
								+ row.id
								+ "' value='"
								+ retVal
								+ "' onChange = 'upperCase(this)' style='width: 100%;'>";
					}
				}, {
					field : 'retailPrice',
					title : '零售价',
					align: 'right',
					width : '8%',
					formatter : function(value, row, index) {
						if (row != null) { return parseFloat(value).toFixed(4);}
						return "<div id='retailPrice_"+index+"'>"+ value+"</div>";
					}
				}, {
					field : 'totalretailPrice',
					title : '出库金额',
					align: 'right',
					width : '10%',
					formatter : function(value, row, index) {
						if (row != null) { return parseFloat(value).toFixed(4);}
						if (value != null && value != "" && !isNaN(value)){
							return value;
						}else{
							return 0.00;
						}
					}
				}, {
					field : 'drugCommonname',
					title : '药品通用名',
					hidden : true
				}, {
					field : 'applyDate',
					title : '出库申请时间',
					hidden : true
				}, {
					field : 'specs',
					title : '规格',
					width : '10%'
				}, {
					field : 'packUnit',
					title : '包装单位',
					formatter : packUnitFamater,
					width : '7%'
				} ,{
					field : 'mark',
					title : '备注',
					width : '17%',
					formatter : function(value, row, index) {
						var retVal = "";
						if (value != null && value != "") {
							retVal = value;
						}
						return "<input type='text' id='mark_"+index+"' value='"+retVal+"' onChange = 'updateMark(this)' style='width: 100%;'>";
					}
				}
				] ]
			});
		})
		//保存方法
		function addForm(){
			var rows = $('#infolist').edatagrid('getSelections');
			if (rows.length > 0) {//选中几行的话触发事件	
				for(var i=0; i<rows.length; i++){
					if(!isNaN(rows[i].outNum) && rows[i].outNum >= 0){
					}else{
						$.messager.alert('提示','请在第'+(i+1)+'行出库数量中输入大于0的数字');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						   return;
					}
				}
				$('#infolist').datagrid('getSelections');
				$('#infoJson').val(JSON.stringify( $('#infolist').datagrid("getSelections")));
				$('#saveForm').form('submit', {    
				    url:"<%=basePath%>drug/approve/approveApplyOut.action",   
				    onSubmit: function(){
				    	if (!$('#saveForm').form('validate')) {
							$.messager.alert('提示',"验证没有通过,不能提交表单!");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return false;
						}
				    	$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
				    },    
				    success:function(data){
				    	$.messager.progress('close');
						var data = eval('(' + data + ')');
						if(data.resCode == "error"){
							$.messager.alert('提示',data.resMes,"warning");
							return false;
						}else{
							$.messager.alert('提示',"出库记录保存成功！");
							$('#list').datagrid('reload');
							delList();
						}
						$('#infolist').datagrid('clearSelections');
					}
				});
			}else{
				$.messager.alert("操作提示", "请选择要保存的条目！", "warning");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
		
		//移除待出库列表
		function delList(){
			var rows = $('#infolist').datagrid("getChecked");
			if(rows.length>0){
				for(var i = rows.length - 1; i >= 0; i--){
					var dd = $('#infolist').edatagrid('getRowIndex',rows[i]);//获得行索引
					$('#infolist').edatagrid('deleteRow',dd);//通过索引删除该行
				}
			}else{
				$.messager.alert("操作提示", "请选择要删除的条目！","warning");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
			//计算总金额
			totalPrice();
		}
		
		//onkeyUp触发的函数
		function upperCase(obj) {
			var val = $(obj).val();
			var id = $(obj).prop("id");
			var i = $('#infolist').datagrid('getRowIndex',id);
			var rowsInfolist = $('#infolist').datagrid('getRows');
			var retailPrice = rowsInfolist[i].retailPrice;
			var mark = rowsInfolist[i].mark;
			var totalretailPrice = retailPrice * val;
			var drugCommonname = rowsInfolist[i].drugCommonname;
			var storeSum = rowsInfolist[i].storeSumDrug;
			var applyNum = rowsInfolist[i].applyNum;
			$('#infolist').edatagrid('endEdit', i);
			if(parseFloat(storeSum) < parseFloat(val) || parseFloat(applyNum) < parseFloat(val)){
				if(storeSum < val){
					$.messager.alert('提示',"出库数量不能大于库存数量，请核对后重新输入！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
				if(parseFloat(applyNum) < parseFloat(val)){
					$.messager.alert('提示',"出库数量不能大于申请数量，请核对后重新输入！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
				var index = $('#infolist').datagrid('updateRow', {
					index : i,
					row : {
						outNum : 0,
						totalretailPrice : 0,
						mark : mark
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
					outNum : val,
					totalretailPrice : (retailPrice * val).toFixed(4),
					mark : mark
				}
			}).edatagrid('getRows').length - 1;
			$('#infolist').edatagrid('beginEdit', index);
			//更新一下总金额
			totalPrice();
		}
		
		//计算总的金额
		function totalPrice(){
			var rows = $('#infolist').datagrid('getRows');
			var price=0;
			for(var i=0;i<rows.length;i++){
				var id = rows[i].id;
				if(!$('#'+id).parent().parent().parent().is(":hidden")){//判断是否隐藏
					price += parseFloat(rows[i].totalretailPrice);
				}
			}
			if(!isNaN(price) && price >= 0){
				price = price.toFixed(2);
			}else{
				price = '';
			}
			$("#totalStoreCost").html(price);
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
		//显示包装单位格式化，最小单位
		function packUnitFamater(value){
			if(value!=null){
				for(var i=0;i<packUnitList.length;i++){
					if(value==packUnitList[i].encode){
						return packUnitList[i].name;
					}
				}	
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
						url :"<%=basePath%>drug/applyout/exportApplyout.action?flag=5",
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
		
		//页面查询检索
		var arr=[];
		function searchList(){
			totalRows = $('#infolist').datagrid("getRows");
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
							$("#" + totalRows[i].id).parent().parent().parent().hide();
							arr.push($("#" + totalRows[i].id).parent().parent().parent());
						}else{
							$("#" + totalRows[i].id).parent().parent().parent().show();
						}
					}
				}
			}
			//计算总的申请金额
			totalPrice();
		}
		function researchList(){
			var queryName = $("#tradNameSerc").textbox('setValue','');
			searchList();
		}
		
		function searchBillCode(){
			var applyBillcode = $.trim($('#applyBillcode').textbox('getValue'));
			if(applyBillcode == ''){
				$.messager.alert("操作提示", "请输入申请单号！", "warning");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
			var deptList = $('#deptList').combobox('getValue');
			$('#list').datagrid('load', {
				deptId : deptList,
				applyBillcode : applyBillcode
			});
		}
		// 列表查询重置
		function searchReload() {
			$('#deptList').combobox('setValue','');
			$('#applyBillcode').textbox('setValue','');
			$('#list').datagrid('load', {
				deptId : null,
				applyBillcode : null
			});
			$('#infolist').datagrid('loadData', { total: 0, rows: [] });
			totalPrice();
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
	<body>
		<!-- 药房内部出库审核(数据源是--药房内部入库--) -->
		<div id="divLayout" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:false" style="height: 35px;border-top:0;">
			<input type="hidden" id="loginDeptId" value="${loginDeptId }">
				<table cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td style="padding: 3px;" nowrap="nowrap">
							<input type="hidden" value="${loginDeptId}" id="loginDeptId">
							领药科室：
							<input class="easyui-combobox" type="text" ID="deptList" data-options="prompt:'请选择领药科室'"/>
							&nbsp;
							申请单号：
							<input class="easyui-textbox" id="applyBillcode" data-options="prompt:'请输入申请单号'"> 
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchBillCode()"class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'west',split:false,title:'申请单列表',iconCls:'icon-book'"style="width: 30% ;">
				<div id="divLayout" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'center',split:false,iconCls:'icon-book',border:false" style="width: 100%;height: 100%">
						<table id="list" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fit:true,fitColumns:false">
							<thead>
								<tr>
									<th data-options="field:'deptCodeName', width : '33%'">
										领药科室
									</th>
									<th data-options="field:'deptCode',hidden:true">
										领药科室code
									</th> 
									<th data-options="field:'applyBillcode', width : '33%'">
										申请单号
									</th>
									<th data-options="field:'drugNum', width : '32%'">
										申请药品种数
									</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			<div data-options="region:'center'" style="width: 70%;">
				<div id="aduDiv" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',title:'科室调价药品列表',split:false,border:true,collapsible:false,border:false" style="height: 30%;border-bottom: 1px solid #95b8e7;" class="changeskinBottom">
					<table id="adulist" data-options="fit:true,striped:true,rownumbers:true,border:false,checkOnSelect:false,selectOnCheck:false,singleSelect:false,fitColumns:false">
						<thead>
								<tr>
									<th data-options="field:'tradeName', width : '28%'">
										药品通用名
									</th>
									<th data-options="field:'storeSumDrug',width : '8%'">
										库存数量
									</th>
									<th data-options="field:'applyNum',width : '7%'">
										申请数量
									</th>
									<th data-options="field:'retailPrice',align: 'right',width : '8%',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}">
										零售价
									</th>
									<th data-options="field:'totalretailPrice',align: 'right',width : '10%',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}">
										申请金额
									</th>
									<th data-options="field:'specs',width : '10%'">
										规格
									</th> 
									<th data-options="field:'packUnit', width : '7%',formatter:packUnitFamater">
										包装单位
									</th>
									<th data-options="field:'mark',width : '13%'">
										备注
									</th>
								</tr>
							</thead>
					</table>
					</div>
					<div data-options="region:'center',title:'待入库药品列表',border:false" style="width: 100%; height: 70%">
						<div id="divLayout" class="easyui-layout" data-options="fit:true">
							<div data-options="region:'north',split:false,border:false" style="height: 35px;">
								<input id="drugCode" type="hidden">
								<table cellspacing="0" cellpadding="0" border="0"style="width: 100%;padding: 3px;border-bottom: 1px solid #95b8e7;" class="changeskinBottom">
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
								<form id="saveForm" method="post" style="height: 93%">
									<input type="hidden" name="deptId" id="deptId">
									<input type="hidden" name="applyBill" id="applyBill">
									<input type="hidden" name="infoJson" id="infoJson">
										<table id="infolist" data-options="fit:true,url:'',method:'post',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar:'#toolbarId'">
										</table>
								</form>
							</div>
							<div id="toolbarId">
								<shiro:hasPermission name="${menuAlias}:function:save">
									<a id="btnAddUser" href="javascript:void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true"onclick="addForm()">保存</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:delete">
									<a id="btnAddUser" href="javascript:void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true"onclick="delList()">删除</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:export">	
									<a id="btnAddUser" href="javascript:void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:true" onclick="exportList()">导出</a>
								</shiro:hasPermission>
								&nbsp;总申请金额：<span ID="totalStoreCost" style="font-weight: bold;color: red;">0.00</span>
							</div>	
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>