<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药品内部入库核准(数据源是--药房内部出库审核--)</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
		//定义全局变量记录总的待出库信息条目
		var totalRows = "";
		//记录已经存在待出库列表条目药品id
		var arrIds = [];
		//删除用
		var tag ='';
		//标记出库数量为0或者出库数量小于库存数量
		var ischeck=true;
		var packUnitList = "";
		//加载页面
		$(function(){
			
			bindEnterEvent('outListCode',searchBillCode,'easyui');//绑定回车事件
			bindEnterEvent('tradNameSerc',searchList,'easyui');//绑定回车事件

			//初始化出库科室下拉
			$('#deptList').combobox({
				url :"<%=basePath%>baseinfo/department/getDeptBySessionDept.action?loginDepteId="+$('#loginDeptId').val()+"&type=1",
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
					var outListCode = $.trim($('#outListCode').textbox('getValue'));
					$('#list').datagrid('load', {
						deptId : deptList,
						outListCode : outListCode
					});
					$('#infolist').datagrid('loadData',[]);
				}
			});
			//申请单列表加载
			$('#list').datagrid({
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 30, 50, 80, 100 ],
					url:'<%=basePath%>drug/approveStore/queryApprove.action?menuAlias=${menuAlias}',
					onDblClickRow: function (rowIndex, rowData) {
						var outListCode = rowData.outListCode;
						$('#deptId').val(rowData.drugDeptCode);
						$('#infolist').datagrid('load', {
							deptId : rowData.drugDeptCode,
							outListCode : outListCode
						});
						totalPrice();
					}
			});
			//待入库药品列表加载
			$('#infolist').edatagrid({
				striped : true,
				checkOnSelect : true,
				selectOnCheck : true,
				singleSelect : false,
				fitColumns : false,
				url:"<%=basePath%>drug/approveStore/queryOutStoreList.action",
				onBeforeLoad:function (param) {
					var row = $('#list').datagrid('getSelected');
					var deptList = $('#deptList').combobox('getValue');
					var outListCode = $.trim($('#outListCode').textbox('getValue'));
					if(row == null && deptList == "" && outListCode == ''){
						return false;
					}
		        },
		        onLoadSuccess : function (param) {
					totalPrice();
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
				},{
					field : 'drugDeptCode',
					title : '出库科室',
					hidden : true
				}, {
					field : 'drugCode',
					title : '药品id',
					formatter : function(value, row, index) {
						return "<div id='"+value+"'></div>";
					},
					hidden : true
				}, {
					field : 'drugCommonname',
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
					field : 'outListCode',
					title : '出库单据号',
					hidden : true
				}, {
					field : 'serialCode',
					title : '序号',
					hidden : true
				}, {
					field : 'tradeName',
					title : '药品名称',
					width : '25%'
				},{
					field : 'applyNum',
					title : '申请数量',
					formatter: function(value,row,index){
						if (value != null && value != ''){
							return value.toFixed(2);
						} else {
							return 0;
						}
					},
					width : '8%'
				}, {
					field : 'outNum',
					title : '出库数量',
					formatter: function(value,row,index){
						if (value != null && value != ''){
							return value.toFixed(2);
						} else {
							return 0;
						}
					},
					width : '7%'
				}, {
					field : 'retailPrice',
					title : '零售价',
					align: 'right',
					width : '8%',
					formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}
				}, {
					field : 'retailCost',
					align: 'right',
					title : '出库金额'
				}, {
					field : 'specs',
					title : '规格',
					width : '10%'
				}, {
					field : 'packUnit',
					title : '包装单位',
					width : '7%'
				}, {
					field : 'packQty',
					title : '包装数量',
					width : '7%'
				}, {
					field : 'remark',
					title : '备注',
					width : '20%',
					formatter : function(value, row, index) {
						var retVal = "";
						if (value != null && value != "") {
							retVal = value;
						}
						return "<input type='text' id='remark_"+index+"' value='"+retVal+"' onChange = 'updateMark(this)' style='width: 100%;'> ";
					}
				}
				] ]
			});
		})
		
		//保存方法
		function addForm(){
			var rows = $('#infolist').edatagrid('getSelections');
			if (rows.length > 0) {//选中几行的话触发事件	
				$('#infolist').datagrid('getSelections');
				$('#infoJson').val(JSON.stringify( $('#infolist').datagrid("getSelections")));
				$('#saveForm').form('submit', {    
				    url:"<%=basePath%>drug/approveStore/approveApply.action",   
				    onSubmit: function(){
				    	$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
				    },    
				    success:function(data){
				    	$.messager.progress('close');
						var data = eval('(' + data + ')');
						if(data.resCode == "success"){
							$.messager.alert('提示',"出库记录保存成功！");
							$('#list').datagrid('reload');
							$('#deptList').combobox('setValue','')
							delList();
						}else{
							$.messager.alert('提示',data.resMes,"warning");
							return false;
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
		
		//计算总的金额
		function totalPrice(){
			var rows = $('#infolist').datagrid('getRows');
			var price=0;
			for(var i=0;i<rows.length;i++){
				var drugCode = rows[i].drugCode;
				if(!$('#'+drugCode).parent().parent().parent().is(":hidden")){//判断是否隐藏
					price += parseFloat(rows[i].retailCost);
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
					remark : $(obj).val()
				}
			});
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
						url :"<%=basePath%>drug/outstore/exportOutStore.action?flag=1",
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
		function researchList(){
			var queryName = $("#tradNameSerc").textbox('setValue','');
			searchList();
		}
		function searchBillCode(){
			var outListCode = $.trim($('#outListCode').textbox('getValue'));
			if(outListCode == ''){
				$.messager.alert("操作提示", "请输入申请单号！", "warning");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
			var deptList = $('#deptList').combobox('getValue');
			$('#list').datagrid('reload', {
				deptId : deptList,
				outListCode : outListCode
			});
		}
		
		// 药品列表查询重置
		function searchReload() {
			$('#deptList').combobox('setValue','');
			$('#outListCode').textbox('setValue','');
			$('#list').datagrid('reload', {
				deptId : "",
				outListCode : ""
			});
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
	<body>
		<div id="divLayout" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:false" style="height: 35px;border-top:0;">
				<table cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td style="padding: 3px;" nowrap="nowrap">
							<input type="hidden" value="${loginDeptId}" id="loginDeptId">
							供货科室：
							<input class="easyui-combobox" type="text" ID="deptList" data-options="prompt:'请选择供货科室'"/>
							&nbsp;
							申请单号：
							<input class="easyui-textbox" id="outListCode" data-options="prompt:'请输入申请单号'"> 
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
									<th data-options="field:'drugDeptName', width : '33%'">
										供货科室
									</th>
									<th data-options="field:'drugDeptCode',hidden:true">
										供货科室code
									</th> 
									<th data-options="field:'outListCode', width : '33%'">
										申请单号
									</th>
									<th data-options="field:'outDrugNum', width : '32%'">
										出库药品种数
									</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			<div data-options="region:'center',title:'待入库药品列表'" style="width: 70%">
				<div id="divLayout" class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',split:false,border:false" style="height: 35px;border-bottom: 1px solid #95b8e7;" class="changeskinBottom">
						<input id="drugCode" type="hidden">
						<table cellspacing="0" cellpadding="0" border="0"style="width: 100%;padding: 3px;">
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
						<form id="saveForm" method="post" >
							<input type="hidden" name="deptId" id="deptId">
							<input type="hidden" name="infoJson" id="infoJson">
						</form>
						<table id="infolist" data-options="fit:true,method:'post',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar:'#toolbarId'">
						</table>
					</div>
					<div id="toolbarId">
						<shiro:hasPermission name="${menuAlias}:function:add">
							<a id="btnAddUser" href="javascript:void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true"onclick="addForm()">保存</a>
						</shiro:hasPermission>
							<a id="btnAddUser" href="javascript:void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true"onclick="delList()">删除</a>
						<shiro:hasPermission name="${menuAlias}:function:export">
							<a id="btnAddUser" href="javascript:void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:true" onclick="exportList()">导出</a>
						</shiro:hasPermission>
						&nbsp;总申请金额：<span ID="totalStoreCost" style="font-weight: bold;color: red;">0.00</span>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>