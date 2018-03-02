<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药品封账</title>
<%@ include file="/common/metas.jsp"%>
</head>
	<body>
		<div id="divLayout" class="easyui-layout" data-options="fit:true">   
		    <div data-options="region:'north'" style="padding: 5px 5px 0px 5px;height:40px;width:100%;border-top:0">
		    	盘点科室：<input ID="deptList"  style="width:200px;"/>&nbsp;&nbsp;&nbsp;
		    	<shiro:hasPermission name="${menuAlias}:function:closeAccount">
				<a id="btnAddUser" href="javascript:void(0)"
					class="easyui-linkbutton" data-options="iconCls:'icon-fengzhang'"
					onclick="addForm(0)">封账</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:delete">
				<a id="btnAddUser" href="javascript:void(0)"
					class="easyui-linkbutton" data-options="iconCls:'icon-remove'"
					onclick="delList()">删除</a>
				</shiro:hasPermission>
		    </div>   
		    <div data-options="region:'center',border:false" style="width:100%;" >
		    	<div class="easyui-layout" data-options="fit:true,border:false">   
			    	<div data-options="region:'west',border:false" style="height:60px;width:40%;">
			    		<div class="easyui-layout" data-options="fit:true,border:false">   
						    <div data-options="region:'north',border:false" style="height:40px;padding: 5px 0px 0px 5px;">
						    	<table cellspacing="0" cellpadding="0" border="0">
									<tr>
										<td nowrap="nowrap">
											药品类型：<input type="text" id="CodeDrugtype" />
											&nbsp;药品名称：<input class="easyui-textbox" ID="drugNameSerc"/>
										<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
										<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
										</td>
									</tr>
								</table>
						    </div>   
						    <div data-options="region:'center',border:true" style="border-right:0">
						    	<table id="list" data-options="url:'${pageContext.request.contextPath}/drug/check/queryDrugStockInfo.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fitColumns:false,fit:true">
									<thead>
										<tr>
											<th data-options="field:'drugNameinputcode'">
												自定义码
											</th>
											<th data-options="field:'drugName'">
												药品名称
											</th>
											<th data-options="field:'drugSpec'">
												药品规格
											</th>
											<th data-options="field:'placeCode'">
												货位号
											</th>
										</tr>
									</thead>
								</table>
						    </div>   
						</div>  
			    	</div>   
	    			<div data-options="region:'center',border:false" style="width:100%;">
		    			<div class="easyui-layout" data-options="fit:true,border:false">   
						    <div data-options="region:'north'," style="height:41px;">
						    	<table cellspacing="0" cellpadding="0" border="0" style="padding: 5px 0px 0px 5px;">
							    <input id="drugCode" type="hidden">
									<tr>
										<td>
											药品名称：<input class="easyui-textbox" id="tradNameSerc" />
											<a href="javascript:void(0)" onclick="searchList()"
												class="easyui-linkbutton" iconCls="icon-search">查询</a>
											<a href="javascript:void(0)" onclick="searchReloadNext()" class="easyui-linkbutton" iconCls="reset">重置</a>
										</td>
									</tr>
								</table>
						    </div>   
						    <div data-options="region:'center',border:false" style="">
						    	<form id="saveForm" method="post" style="width: 100%;height: 100%;">
									<input type="hidden" name="infoJson" id="infoJson">
									<table id="infolist" data-options="url:'',method:'post',idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,fit:true"></table>
								</form>
						    </div>   
						</div>  
	    			</div>
    			</div>
		    </div>   
		</div>  
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
		<script type="text/javascript">
	//定义全局变量记录总的待入库信息条目
	var totalRows = null;
	//记录赋值过的条目id
	var arrIds = [];
	var drugTypeMap = "";//药品类别Map
	var drugpackagingunitMap = "";//包装单位Map
	var minunitMap = "";//最小单位Map
	var drugManufacturerMap = "";//生成厂家Map
	var drugpropertiesMap = "";//药品性质List
	//加载页面
	$(function() {
		bindEnterEvent('tradNameSerc',searchList,'easyui');//绑定回车事件
		bindEnterEvent('drugNameSerc',searchFrom,'easyui');//绑定回车事件
		//初始化下拉框
		idCombobox("drugType");
		//下拉框的keydown事件   调用弹出窗口
		var drugType = $('#CodeDrugtype').combobox('textbox');
		drugType.keyup(function() {
			KeyDown1(0, "CodeDrugtype");
		});

		//初始化封账科室下拉
		$('#deptList').combobox({
			url : "<%=basePath%>drug/check/findDeptByYFYK.action",
			valueField : 'deptCode',
			textField : 'deptName',
			width : 170				,
			onSelect : function(record){
				deptchange();
			},onHidePanel:function(none){
			    var data = $(this).combobox('getData');
			    var val = $(this).combobox('getValue');
			    var result = true;
			    for (var i = 0; i < data.length; i++) {
			        if (val == data[i].deptCode) {
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
			    keys[keys.length] = 'deptCode';
			    keys[keys.length] = 'deptName';
			    keys[keys.length] = 'deptPinyin';
			    keys[keys.length] = 'deptWb';
			    keys[keys.length] = 'deptInputcode';
			    return filterLocalCombobox(q, row, keys);
			}
		});
		//添加datagrid事件及分页
		$('#list').datagrid({
			selectOnCheck : true,
			singleSelect : true,
			pagination : true,
			rownumbers : true,
			pageSize : 20, 
			pageList : [ 20, 30, 50, 80, 100 ],
			onLoadSuccess:function(data){
				$('#list').datagrid('unselectAll');
				$('#list').datagrid('uncheckAll');
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
			},
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				var row = $('#list').datagrid('getSelected');
				var drugStorageCode=$("#deptList").combobox('getValue');//得到选择的科室code
				if(drugStorageCode){
					if (row) {
						$.ajax({
						url:"<%=basePath%>drug/check/checkIsExit.action?menuAlias=${menuAlias}",
						data : {
							deptCode:drugStorageCode,
							drugRowCode:row.drugId,
						},
						type:'post',
						success:function(data){
							if(data == "yes"){
								attrValue(row);
							}else{
								$.messager.alert("操作提示", "此药品已经封账不能操作，请选择其他药品！", "warning");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}
						}
						});
					}
				}else{
					$.messager.alert("操作提示", "请选择要封账的科室！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
		});
		//查询药品类别
		$.ajax({
			url: "<%=basePath%>drug/stockinfo/queryDrugType.action",				
			type:'post',
			success: function(drugTypedata) {					
				drugTypeMap = drugTypedata;										
			}
		});	
		//药品性质
		$.ajax({
			url: "<%=basePath%>drug/stockinfo/queryDrugproperties.action",				
			type:'post',
			success: function(drugpropertiesdata) {					
				drugpropertiesMap= drugpropertiesdata;
			}
		});
		//生产厂家
		$.ajax({
			url: "<%=basePath%>drug/stockinfo/queryDrugManufacturer.action",				
			type:'post',
			success: function(drugManufacturerdata) {					
				drugManufacturerMap= drugManufacturerdata;										
			}
		});
		//查询包装单位
		$.ajax({
			url: "<%=basePath%>inpatient/docAdvManage/queryDrugpackagingunit.action",				
			type:'post',
			success: function(drugpackagingunitdata) {					
				drugpackagingunitMap = drugpackagingunitdata;										
			}
		}); 
		//查询最小单位
		$.ajax({
			url: "<%=basePath%>inpatient/docAdvManage/queryMinunit.action",				
			type:'post',
			success: function(queryminunitdata) {					
				minunitMap = queryminunitdata;										
			}
		}); 
		setTimeout(function(){//时间戳
		//添加datagrid事件及分页
		$('#infolist').datagrid({
			striped : true,
			checkOnSelect : true,
			selectOnCheck : true,
			singleSelect : false,
			pagination : false,
			pageSize : 20,
			pageList : [ 20, 30, 50, 100 ],
			columns : [ [ {
				field : 'ck',
				checkbox : true
			}, {
				field : 'id',
				title : 'id',
				hidden : true
			}, {
				field : 'flagId',
				title : 'flagId',
				hidden : true,
				width : '0%',
				formatter : function(value, row, index) {
					return "<div id='"+row.id+"'></div>";
				}
			}, {
				field : 'drugCode',
				title : '药品code',
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
				width : '7%',
				hidden : true
			}, {
				field : 'drugDeptCode',
				title : '科室代码',
				width : '7%',
				hidden : true
			},  {
				field : 'tradeName',
				title : '药品名称',
				width : '14%'
			}, {
				field : 'specs',
				title : '规格',
				width : '7%'
			}, {
				field : 'retailPrice',
				title : '零售价',
				width : '7%',
				formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}
			},  {
				field : 'wholesalePrice',
				title : '批发价',
				width : '7%',
				formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}
			}, {
				field : 'purchasePrice',
				title : '购入价',
				width : '7%',
				formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}
			},{
				field : 'drugType',
				title : '药品类别',
				width : '7%',
				formatter : function (value,row,index){			
					if(value!=null&&value!=""){
						if(drugTypeMap[value]!=null&&drugTypeMap[value]!=""){
							return drugTypeMap[value];
						}
						return value;
					}			
				}
			},{
				field : 'drugQuality',
				title : '药品性质',
				width : '7%',
				formatter : function(value,row,index){		
					if(value!=null&&value!=""){											
						if(drugpropertiesMap[value]!=null&&drugpropertiesMap[value]!=""){
							return drugpropertiesMap[value];
						}
						return value;												
					}	
				}
			},{
				field : 'packQty',
				title : '包装数',
				width : '7%'
			},{
				field : 'producer',
				title : '生产厂家',
				width : '7%',
				formatter : function(value,row,index){	
					if(value!=null&&value!=""){											
						if(drugManufacturerMap[value]!=null&&drugManufacturerMap[value]!=""){
							return drugManufacturerMap[value];
						}
						return value;												
					}			
				}
			},{
				field : 'packUnit',
				title : '包装单位',
				width : '7%',
				formatter : function(value,row,index){	
					if(drugpackagingunitMap[value]!=null&&drugpackagingunitMap[value]!=""){
						return drugpackagingunitMap[value];
					}
				}
			}, {
				field : 'minUnit',
				title : '最小单位',
				width : '7%',
				formatter : function(value,row,index){	
					if(value!=null&&value!=""){											
						if(minunitMap[value]!=null&&minunitMap[value]!=""){
							return minunitMap[value];
						}
						return value;												
					}			
				}
			}, {
				field : 'fstoreNum',
				title : '库存数量',
				width : '7%'
			}, {
				field : 'placecode',
				title : '货位号',
				width : '7%'
			} ] ]
		});
		},100); 
	});
	/**
	 * 药品列表查询
	 * @author  lt
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-06-19
	 * @version 1.0
	 */
	function searchFrom() {
		var drugName = $.trim($('#drugNameSerc').val());
		var drugType = $.trim($('#CodeDrugtype').combobox('getValue'));
		var drugStorageCode=$.trim($("#deptList").combobox('getValue'));//得到选择的科室code
		$('#list').datagrid('load', {
			deptCode : drugStorageCode,
			drugName : drugName,
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

	/**
	 * 弹出框
	 * @author  lt
	 * @date 2015-07-16
	 * @version 1.0
	 */
	var win;
	function showWin(title, url, width, height) {
		var content = '<iframe id="myiframe" src="'
				+ url
				+ '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
		var divContent = '<div id="treeDeparWin">';
		win = $('<div id="treeDeparWin"><div/>').dialog({
			content : content,
			width : width,
			height : height,
			modal : true,
			resizable : true,
			shadow : true,
			center : true,
			title : title
		});
		win.dialog('open');
	}
	/**
	 * 批量提交列表信息
	 * @author  lt
	 * @date 2015-07-31
	 * @version 1.0
	 */
	function addForm(flag) {
		var rows = $('#infolist').edatagrid('getSelections');
		var drugStorageCode=$("#deptList").combobox('getValue');//得到选择的科室code
		if (rows.length > 0) {//选中几行的话触发事件	                        
			$('#infoJson').val(JSON.stringify($('#infolist').edatagrid("getSelections")));
			$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
			$('#saveForm').form(
					'submit',
					{
						url : "<%=basePath %>drug/check/saveCheckStatic.action?flag="+flag+"&deptCode="+drugStorageCode,
						onSubmit : function() {
							return $(this).form('validate');
						},
						success : function(data) {
							$.messager.progress('close');
							if(flag===0){//判断是封账操作才打印封账单
								var cdid = data;
									$.messager.confirm('确认对话框', '是否打印本次封账单？', function(r){
										if (r){
											var timerStr = Math.random();
											window.open("<%=basePath %>iReport/iReportPrint/iReportToFSTORE.action?randomId="+timerStr+"&cdid="+cdid+"&deptCode="+drugStorageCode+"&fileName=fengzhangdan",'newwindow'+cdid,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
										}
									});
							}
							
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
							$('#infolist').datagrid('uncheckAll'); 
							$('#infolist').datagrid('unselectAll');
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
	//为列表赋值
	function attrValue(row) {
		var rows = $('#infolist').datagrid("getRows");
		if (rows.length > 0) {
			if (arrIds.indexOf(row.id) == -1) {
				appendValue(row);
				arrIds.push(row.id);
			} else {
				$.messager.alert("操作提示", "待封账药品列表已有此药品，请勿重新添加！", "warning");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		} else {
			appendValue(row);
			if (arrIds.indexOf(row.id) == -1) {
				arrIds.push(row.id);
			}
		}
	}
	//为列表赋值
	function appendValue(row) {
		var index = $('#infolist').edatagrid('appendRow', {
			id : row.id,
			tradeName : row.drugName,
			drugNamepinyin : row.drugNamepinyin,
			drugNamewb : row.drugNamewb,
			drugNameinputcode : row.drugNameinputcode,
			drugType : row.drugType,
			specs : row.drugSpec,
			drugQuality:row.drugQuality,
			drugDeptCode:row.storageDeptid,
			fstoreNum : row.storeSum,
			wholesalePrice : row.drugWholesaleprice,
			purchasePrice : row.drugPurchaseprice,
			producer : row.drugManufacturer,
			packUnit : row.drugPackagingunit,
			minUnit : row.drugMinimumunit,
			packQty:row.packQty,
			retailPrice : row.drugRetailprice,
			totalretailPrice : -row.drugRetailprice * row.storeSum,
			placecode : row.placeCode,
			drugCode : row.drugId
		}).edatagrid('getRows').length - 1;
		$('#infolist').edatagrid('beginEdit', index);
		var rows = $('#infolist').datagrid("getRows");
		totalRows = cloneObject(rows);
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

	//警戒线查询
	function cordon() {
		$('#list').datagrid('load', {
			flag : 1
		});

		//加缓冲时间50毫秒
		setTimeout(function() {
			var rows = $('#list').datagrid('getRows');
			if (rows) {
				for ( var i = 0; i < rows.length; i++) {
					attrValue(rows[i]);
				}
				;
			}
		}, 50);
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
					showWin("请药品类别", "<c:url value='/ComboxOut.action'/>?xml="
							+ "CodeDrugtype,0", "50%", "80%");
				}
			}
		}
	}
	//从xml文件中解析，读到下拉框
	function idCombobox(param) {
		$('#CodeDrugtype').combobox({
			url : "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=" + param,
			valueField : 'encode',
			textField : 'name',
			multiple : false,
			onLoadSuccess : function() {//请求成功后
				$(list).each(function() {
					if ($('#CodeDrugtype').val() == this.Id) {
						$('#CodeDrugtype').combobox("select", this.Id);
					}
				});
			},onSelect : function(record){
				searchFrom();
			}
		});
	}
	//页面查询检索
	var arr = [];
	function searchList() {
		var tradeName = $("#tradNameSerc").val();
		if ((tradeName == null || tradeName == "")) {
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
						$("#" + totalRows[i].id).parent().parent().parent()
								.hide();
						arr.push($("#" + totalRows[i].id).parent().parent()
								.parent());
					}
				}
			}
		}
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
						if (arrIds.indexOf(rows[i].id) != -1) {
							arrIds.remove(rows[i].id);
						}
						arr.push(dd);
					}
					for ( var i = arr.length - 1; i >= 0; i--) {
						$('#infolist').edatagrid('deleteRow', arr[i]);//通过索引删除该行
					}
					$('#infolist').datagrid('uncheckAll'); 
					$('#infolist').datagrid('unselectAll');
				} else {
					$.messager.alert("操作提示", "请选择要删除的条目！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			});
		}
	}
	//科室下拉框改变的时候判断是否清空待入库列表
	function deptchange(){
		$('#infolist').datagrid('uncheckAll'); 
		$('#infolist').datagrid('unselectAll');
		var drugStorageCode=$("#deptList").combobox('getValue');//得到选择的科室code
		var rows = $('#infolist').datagrid("getRows");//得到待入库列表的行
		if (rows.length > 0) {
   			var arrs=[];
   			for(var j=0; j<rows.length; j++){
				var dd = $('#infolist').edatagrid('getRowIndex',rows[j]);//获得行索引
				arrs.push(dd);
   			}
   			for(var w=arrs.length-1;w>=0;w--){
				$('#infolist').edatagrid('deleteRow',arrs[w]);//通过索引删除该行
			}
		}
		//清空药品名称可药品类别
		$('#drugNameSerc').textbox('setValue','');
		$('#CodeDrugtype').combobox('setValue','');
		//改变科室下拉后,查询列表数据
		$('#list').datagrid('load', {
			deptCode : drugStorageCode
		});
	}
	
	// 药品列表查询重置
	function searchReload() {
		$('#CodeDrugtype').combobox('setValue','');
		$('#drugNameSerc').textbox('setValue','');
		searchFrom();
	}
	
	// 药品列表查询重置
	function searchReloadNext() {
		$('#tradNameSerc').textbox('setValue','');
		searchList();
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>