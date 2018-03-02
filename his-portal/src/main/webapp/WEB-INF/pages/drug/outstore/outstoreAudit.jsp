<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>出库审核</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
		//定义全局变量记录总的待出库信息条目
		var totalRows = "";
		//记录已经存在待出库列表条目药品id
		var arrIds = [];
		//标记出库数量为0或者出库数量小于库存数量
		var ischeck=true;
		var minList = null;
		var packList = null;
		//加载页面
		$(function(){
			bindEnterEvent('queryNo',searchFrom,'easyui');//绑定回车事件
			
			//初始化入库科室下拉
			$('#deptList').combobox({
				url : "<%=basePath%>baseinfo/department/getDeptBySessionDept.action?loginDepteId="+$('#loginDeptId').val()+"&type=1",
				valueField : 'deptCode',
				textField : 'deptName',
				width : 170,
				onSelect:function(record){
					searchFrom();
				}
			});

			//添加datagrid事件及分页
			$('#list').datagrid({
				pagination : true,
				pageSize : 20,
				pageList : [ 20, 30, 50, 80, 100 ],
				onBeforeLoad: function (param) {//加载数据
					$.ajax({
						url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
						data:{"type" : "minunit"},
						type:'post',
						success: function(data) {
							minList = data;
						}
					});
					$.ajax({
						url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
						data:{"type" : "packunit"},
						type:'post',
						success: function(data) {
							packList = data;
						}
					});
		        },onDblClickRow: function (rowIndex, rowData) {
					$('#infolist').datagrid('uncheckAll'); 
					$('#infolist').datagrid('unselectAll');
		        	var row = $('#list').datagrid('getSelected');	                        
                    if(row){
		        		searchInfoForDb(row);
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
			//添加datagrid事件及分页
			$('#infolist').datagrid({
				striped:true,
				checkOnSelect:true,
				selectOnCheck:true,
				singleSelect:false,
				fitColumns:false,
				url:"<%=basePath%>drug/outstore/queryApplyoutNowByNo.action",
				onBeforeLoad:function (param) {
					var queryNo = $.trim($('#queryNo').textbox('getValue'));
					if(queryNo == ""){
						return false;
					}
		        },
		        onLoadSuccess:function(){
		        	arrIds=[];
					//处理arr里面存的ID
					var rows = $('#infolist').datagrid("getRows");
					if (rows.length > 0) {//选中几行的话触发事件	  
				    	for(var i=0; i<rows.length; i++){
				    		var tag=rows[i].drugCode;
				    		arrIds.push(tag);
				    	}
					}
					//处理金额
					totalPrice();
					$("#totalDivId").show();
					//处理科室下拉框
					//$('#deptList').combobox("select",rows[0].drugDeptCode);
		        },
		   		columns:[[    
			        {field:'ck',checkbox:true}, 
			        {field:'flagId',title:'flagId',hidden:true,width:'0%',formatter:function(value,row,index){
			        	return "<div id='"+row.drugCode+"'></div>";
			        }},
			        {field:'id',title:'出库记录或是出库申请Id',hidden:true},
			        {field:'applyOpercode',title:'申请人编码',hidden:true},
			        {field:'applyDate',title:'申请日期',hidden:true},
			        {field:'deptCode',title:'申请科室',hidden:true},
			        {field:'deptCodeName',title:'申请科室名称',hidden:true},
			        {field:'drugDeptCode',title:'领药科室',hidden:true},
			        {field:'drugDeptName',title:'领药科室名称',hidden:true},
			        {field:'applyNumber',title:'出库单流水号',hidden:true},
			        {field:'applyBillcode',title:'出库单据号',hidden:true},
			        {field:'batchNo',title:'批号',hidden:true},
			        {field:'differentId',title:'区分是出库记录Id还是出库申请Id',hidden:true},
			        {field:'drugCode',title:'药品编码',hidden:true},
			        {field:'drugType',title:'药品类型',hidden:true},
			        {field:'drugQuality',title:'药品性质',hidden:true},
			        {field:'showUnit',title:'显示的单位',hidden:true},
			        {field:'companyCode',title:'供货单位代码',hidden:true},
			        {field:'purchasePrice',title:'购入价',hidden:true},
			        {field:'wholesalePrice',title:'批发价',hidden:true},
			        {field:'groupCode',title:'批次号',hidden:true},
			        {field:'tradeName',title:'药品名称',hidden : true},
			        {field:'drugCommonname',title : '药品通用名',width : '20%'},
			        {field:'drugCnamepinyin',title : '通用拼音码',hidden : true}, 
			        {field:'drugCnamewb',title : '通用五笔码',hidden : true}, 
			        {field:'drugCnameinputcode',title : '通用自定义码',hidden : true},
			        {field:'storeSum',title:'库存数量',width:'7%'},    
			        {field:'applyNum',title:'出库数量',width:'7%',formatter:function(value,row,index){
			        	var retVal = "";
						if (value != null && value != "" && !isNaN(value) && value >0) {
							retVal = value;
						}else{
							retVal = '';
						}
			        	return "<input type='text' id='"+row.id+"_"+index+"' value='"+retVal+"' onchange = 'upperCase(this)' style='width:100%'>";
			        }},     
			        {field:'specs',title:'规格',width:'10%'},
			        {field:'packUnit',title:'包装单位',width:'7%',formatter: packFamater},    
			        {field:'packQty',title:'包装数量',width:'7%'},
			        {field:'retailPrice',title:'零售价',width:'7%',align: 'right',formatter:function(value,row,index){
			        	if (row != null) { return parseFloat(value).toFixed(4);}
			        	return "<div id='retailPrice_"+index+"'>"+value+"</div>";
			        }},
			        {field:'outlCost',title:'出库金额',align: 'right',width:'7%',formatter:function(value,row,index){
			        	if (value != null && value != "" && !isNaN(value) && value >=0) {
							return value.toFixed(4);
						}else{
							return '';
						}
			        	}},
			        {field:'mark',title:'备注',width:'18%',formatter:function(value,row,index){
			        	var retVal = "";
			        	if(value!=null&&value!=""){
			        		retVal = value;
			        	}
			        	return "<input type='text' id='mark_"+index+"' value='"+retVal+"' onchange = 'upperCasemark(this)' style='width:100%'>";
			        }}
				]]
			});
		});
		//确认把数据填充到待出库列表
		function conform(row){
			var index =$('#infolist').datagrid('appendRow',{
				id:row.id,
				differentId:1,
				drugCode: row.drugCode,
				drugDeptCode: row.drugDeptCode,
				tradeName: row.drugName,
				drugCommonname : row.drugCommonname,
				drugCnamepinyin : row.drugCnamepinyin,
				drugCnamewb : row.drugCnamewb,
				drugCnameinputcode : row.drugCnameinputcode,
				specs: row.drugSpec,
				retailPrice:row.drugRetailprice,
				packUnit:row.drugPackagingunit,
				applyNum:row.storeSumDrug,
				packQty:row.drugPackagingnum,
				storeSum:row.storeSumDrug,
				outlCost:(row.applyNum*row.drugRetailprice).toFixed(2),
				drugType:row.drugType,
		        drugQuality:row.drugQuality,
		        showUnit:row.showUnit,
		        companyCode:row.companyCode,
		        purchasePrice:row.purchasePrice,
		        wholesalePrice:row.wholesalePrice,
		        groupCode:row.groupCode,
			}).edatagrid('getRows').length-1;
         			$('#infolist').edatagrid('beginEdit',index);
         	var rows = $('#infolist').datagrid("getRows");
			totalRows = cloneObject(rows);
			//计算总金额
			totalPrice();
			$("#totalDivId").show();
		}
		function submit(){
		    var applyBillCode="";
			var rows = $('#infolist').datagrid('getChecked');
			var drugStorageCode="";
		    if (rows.length > 0) {//选中几行的话触发事件
		    	if(checkStoreSum()){
			    	//拿出第一行选中的出库单号
					applyBillCode=rows[0].applyBillcode;
			    	
					drugStorageCode=rows[0].deptCode;
					$('#infolist').datagrid('acceptChanges');
					$('#outstoreJson').val(JSON.stringify( $('#infolist').datagrid("getChecked")));
					$('#saveForm').form('submit', {    
					    url:"<%=basePath%>drug/outstore/saveAduitOutstore.action?drugStorageCode="+drugStorageCode,   
					    onSubmit: function(){
					    	if (!$('#saveForm').form('validate')){
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
								$("#totalStoreCost").html("");
								$('#deptList').combobox('setValue','');
								return false;
							}else{
								$.messager.alert('提示',"出库记录保存成功！");
								$('#list').datagrid('reload');
								$('#infolist').datagrid('loadData', { total: 0, rows: [] });
								$("#totalStoreCost").html("");
								$('#queryNo').textbox('setValue','');
								$('#deptList').combobox('setValue','');
								arrIds.length=0;
								rows.length=0;
								$.messager.confirm('确认对话框', '是否打印本次出库单？', function(r){
										if (r){
											print(applyBillCode);
										}
									});
							  }	  
					    }
					 });
		    	}
		    }else{
		    	$.messager.alert("操作提示", "请选择要保存的条目！", "warning");
		    	setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
		    }
		}
		//打印出库单 
		function print(applyBillCode) {
			var timerStr = Math.random();
			window.open ("<%=basePath%>iReport/iReportPrint/iReportToDrugOutStore.action?randomId="+timerStr+"&outBillCode="+applyBillCode+"&fileName=yaopinchukudan",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		}
		//移除待出库列表
		function delList(){
			var rows = $('#infolist').datagrid("getChecked");
			if(rows.length>0){
				var arr=[];
				for(var i=0; i<rows.length; i++){
					var dd = $('#infolist').edatagrid('getRowIndex',rows[i]);//获得行索引
					if(arrIds.indexOf(rows[i].drugCode)!=-1){
						arrIds.remove(rows[i].drugCode);
					}
					arr.push(dd);
				}
				for(var i=arr.length-1;i>=0;i--){
					$('#infolist').edatagrid('deleteRow',arr[i]);//通过索引删除该行
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
		//判断是否已经填充到待出库列表
		function attrValue(row){
			var rows = $('#infolist').datagrid("getRows");
			var tag=row.drugCode;
			if(rows.length>0){
				if(arrIds.indexOf(tag) == -1){
					conform(row);
					arrIds.push(tag);
				}else{
					$.messager.alert("操作提示", "待出库列表已有此条记录！","warning");
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
		
		function cloneObject(obj){
			var o = obj.constructor === Array ? [] : {};
			for(var i in obj){
				if(obj.hasOwnProperty(i)){
					o[i] = typeof obj[i] === "object" ? cloneObject(obj[i]) : obj[i];
				}
			}
			return o;
		}
		//计算总的金额
		function totalPrice(){
			var rows = $('#infolist').datagrid('getRows');
			var price=0;
			for(var i=0;i<rows.length;i++){
				var drugCode = rows[i].drugCode;
				if(!$('#'+drugCode).parent().parent().parent().is(":hidden")){//判断是否隐藏
					price += parseFloat(rows[i].outlCost);
				}
			}
			$("#totalStoreCost").html(price.toFixed(2));
		}
		//页面查询检索
		var arr=[];
		// 药品列表查询
		function searchFrom() {
			deptchange();//修改下拉框内容之后,要清空右侧待入库列表里面的数据,以免发生干扰
			var deptList = $.trim($('#deptList').combobox('getValue'));
			var queryNo = $.trim($('#queryNo').textbox('getValue'));
			$('#list').datagrid('load', {
		    	deptId : deptList,//领药科室
		    	applyBillcode : queryNo,//申请单号
			});
		}
		
		/**
		 * 待入库药品列表查询(输入框)
		 */
		function searchInfo() {
			var queryNo = $.trim($('#queryNo').textbox('getValue'));
			$('#infolist').datagrid('reload', {
				applyBillcode : queryNo,//申请单号
				opType : 11,//申请来源
				applyState : 0,//申请状态
				deptCode: $("#deptList").combobox('getValue'),//出库科室
				drugStorageCode : "lyks",//领药科室
			});
		}
		/**
		 * 待入库药品列表查询(双击单号列表)
		 */
		function searchInfoForDb(row) {
			var queryNo=row.applyBillcode;
			$('#queryNo').textbox('setValue',queryNo);
			var drugdept=row.deptCode;//申请科室
			var drugDeptCode=row.drugDeptCode;//领药科室
			$('#deptList').combobox('setValue',drugDeptCode);
			$('#infolist').datagrid('reload', {
				applyBillcode : queryNo,//申请单号
				opType : 11,//申请来源
				applyState : 0,//申请状态
				deptCode: drugdept,//出库科室
				drugStorageCode : drugDeptCode,//领药科室
			});
		}
		
		//显示包装单位格式化，最小单位
		function minFamater(value){
			if(value!=null){
				for(var i=0;i<minList.length;i++){
					if(value==minList[i].encode){
						return minList[i].name;
					}
				}	
			}
		}
		//显示包装单位格式化，最小单位
		function packFamater(value){
			if(value!=null){
				for(var i=0;i<packList.length;i++){
					if(value==packList[i].encode){
						return packList[i].name;
					}
				}	
			}
		}
		//加载dialog
		function Adddilog(title, url,id) {
			$('#'+id).dialog({    
			    title: title,    
			    width: '60%',    
			    height:'60%',    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true 
			   });  
		}
		//打开dialog
		function openDialog(id) {
			$('#'+id).dialog('open'); 
		}
		//关闭dialog
		function closeDialog(id) {
			$('#'+id).dialog('close');  
		}
		
		//页面验证判断库存
		function upperCase(obj){
			var id = $(obj).prop("id").split("_");
			var index = parseInt(id[1]);
			var mark = document.getElementById('mark_' + index).value;
			$('#infolist').datagrid('updateRow',{
				index: index,
				row: {
					applyNum:$(obj).val(),
					outlCost:($('#retailPrice_'+id[1]).text()*$(obj).val()).toFixed(2),
					mark:mark
				}
			});
			//计算总金额
			totalPrice();
		}
		//备注保存方法
		function upperCasemark(obj){
			var id = $(obj).prop("id").split("_");
			var index = parseInt(id[1]);
			$('#infolist').datagrid('updateRow',{
				index: index,
				row: {
					mark:$(obj).val()
				}
			});
		}
		//科室下拉框改变的时候判断是否清空待入库列表
		function deptchange(){
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
				//计算总金额
				totalPrice();
			}
		}
		
		//验证 某行信息的出库数量为0或者出库数量小于库存数量，给予用户提示“XX药出库数量不能小于0并且不能小于库存数量”
		function checkStoreSum(){
			var index = $('#infolist').datagrid("getRows").length;
			for (var i=0;i<index;i++){
		       var dataGridRowData = $('#infolist').datagrid('getRows')[i];
		       var applyNum = dataGridRowData.applyNum;
		       var storeSum = dataGridRowData.storeSum;
		       var drugname = dataGridRowData.tradeName;
		       if(applyNum==0){
		    	   $.messager.alert('提示',"待出库列表第"+(parseInt(i)+1)+"条记录"+drugname+"出库数量不能为0");
		    	   setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		    	   return false;
		       }else if(parseFloat(applyNum) > parseFloat(storeSum)){
		    	   $.messager.alert('提示',"待出库列表第"+(parseInt(i)+1)+"条记录"+drugname+"出库数量不能大于库存数量");
		    	   setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		    	   return false;
		       }
			}
			return true;
		}
		//导出列表
		function exportList() {
			var rows = $('#infolist').datagrid("getChecked");
			if (rows.length == 0) {
				$.messager.alert("操作提示", "待入库列表无记录！", "warning");
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
						url :"<%=basePath%>drug/applyout/exportApplyout.action?flag=6",
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
		
		// 药品列表查询重置
		function searchReload() {
			$('#deptList').combobox('setValue','');
			$('#queryNo').textbox('setValue','');
			searchFrom();
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
	<body>
	<!-- 出库审核 -->
		<div id="divLayout" class="easyui-layout" fit=true>
			<div data-options="region:'north',split:false,border:false" style="height: 35px">
				<table cellspacing="0" cellpadding="0" border="0" style="padding: 5px 5px 0px 5px;">
					<tr>
						<td  nowrap="nowrap">
						领药科室：<input type="hidden" value="${loginDeptId}" id="loginDeptId"><input class="easyui-combotree" ID="deptList"  style="width:200px;"/>
						&nbsp;申请单号：<input class="easyui-textbox" id="queryNo" style="width:140px"/>
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
				</tr>
			</table>
			</div>
			<div data-options="region:'center',split:false,title:'单号列表',iconCls:'icon-book',border:true" style="height:100%; width: 40%;">
				<div id="divLayout" class="easyui-layout" fit=true>
					<div data-options="region:'center',split:false,border:false" style=" width: 100%;">
						<table id="list" data-options="fit:true,url:'${pageContext.request.contextPath}/drug/outstore/queryAppOutstore.action?menuAlias=${menuAlias}',method:'post',idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
							<thead>
								<tr>
									<th data-options="field:'deptCodeName', width : '20%'">
										申请科室
									</th>
									<th data-options="field:'deptCode',hidden:true">
										申请科室code
									</th> 
									<th data-options="field:'drugDeptName', width : '20%'">
										领药科室
									</th>
									<th data-options="field:'drugDeptCode',hidden:true">
										领药科室code
									</th> 
									<th data-options="field:'applyBillcode', width : '20%'">
										申请单号
									</th>
									<th data-options="field:'drugNum', width : '20%'">
										申请药品种数
									</th>
									<th data-options="field:'applyDateView', width : '20%'">
										申请时间
									</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		<div data-options="region:'east',split:false,iconCls:'icon-book'"style="height:100%;width:60%;">
			<div id="divLayout" class="easyui-layout" fit=true>
				<div data-options="region:'center',split:false,border:false" style="height: 100%;">
					<div style="height: 100%">
						<form id="saveForm" method="post"style="height: 100%;">
							<input type="hidden" name="outstoreJson" id="outstoreJson">
							<input type="hidden" name="infoJson" id="infoJson">
							<table id="infolist" data-options="fit:true,url:'',method:'post',idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar: '#tb'"></table>
						</form>
					</div>
				</div>
				<div id="tb" style="height: auto">
					<table width="100%";">
						<tr>
							<td style="width: 400px;">
								<shiro:hasPermission name="YBCK:function:add">
									<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="submit()">保存</a>
								</shiro:hasPermission>
								<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delList()">删除</a>
								<a id="btnAddUser" href="javascript:void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:true" onclick="exportList()">导出</a>
							</td>
							<td style="font-weight: bold;" >
									<div id="totalDivId"  style="display: none;">总零售金额：<span ID="totalStoreCost" style="font-weight: bold;color: red;"></span></div>
								</td>
							</tr>
				</div>
				<div id="selectApplyOut"></div>
			</div>
		</div>
	</div>
</body>
</html>