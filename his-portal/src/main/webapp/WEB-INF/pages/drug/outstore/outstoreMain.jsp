<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>一般出库</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
		//定义全局变量记录总的待出库信息条目
		var totalRows = "";
		//记录已经存在待出库列表条目药品id
		var arrIds = [];
		//标记出库数量为0或者出库数量小于库存数量
		var ischeck=true;
		var packList = null;
		var minList = null;
		//加载页面
		$(function(){
			
			bindEnterEvent('tradNameSerc',searchList,'easyui');//绑定回车事件
			bindEnterEvent('queryName',searchFrom,'easyui');//绑定回车事件
			bindEnterEvent('queryNo',KeyDownNo,'easyui');//绑定回车事件
			
			//初始化领药科室下拉
			$('#deptList').combobox({
				url : "<c:url value='/baseinfo/department/getDeptBySessionDept.action'/>?loginDepteId="+$('#loginDeptId').val()+"&type=2",
				valueField : 'deptCode',
				textField : 'deptName',
				width : 170				,
				onSelect : function(record){
					deptchange();
				}
			});
			//初始化下拉框
			//idCombobox("CodeDrugtype");
			idCombobox("drugType");
			var drugType = $('#CodeDrugtype').combobox('textbox'); 
			drugType.keyup(function(){
				KeyDown1(0,"CodeDrugtype");
			});
			
			//添加datagrid事件及分页
			$('#list').datagrid({
				pagination : true,
				pageSize : 20,
				pageList : [ 20, 30, 50, 80, 100 ],
				url:"<c:url value='/drug/outstore/queryStorageJoinDrugInfo.action'/>?menuAlias=${menuAlias}",
				onBeforeLoad:function (param) {
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
		        },
				onDblClickRow: function (rowIndex, rowData) {
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
			//添加datagrid事件
			$('#listApplyNo').datagrid({
				url:"<%=basePath%>/drug/outstore/queryAppOutstore.action",
				onBeforeLoad:function (param) {
					var drugStorageCode=$("#deptList").combobox('getValue');//得到选择的科室code
					if(drugStorageCode == ""){
						return false;
					}
		        },onDblClickRow: function (rowIndex, rowData) {
		        	$('#infolist').datagrid('uncheckAll'); 
		        	$('#infolist').datagrid('unselectAll');
					var row = $('#listApplyNo').datagrid('getSelected');	                        
                    if(row){
                  	   attrSearch(row);
	   				}
				}    
			});
			//添加datagrid事件及分页
			$('#infolist').datagrid({
				striped : true,
				checkOnSelect : true,
				selectOnCheck : true,
				singleSelect : false,
				fitColumns : false,
				url:"<%=basePath%>drug/outstore/queryApplyoutNowByNo.action",
				onBeforeLoad:function (param) {
					var queryNo = $('#queryNo').textbox('getValue');
					if(queryNo == ""){
						return false;
					}
		        },onLoadSuccess:function(){
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
					if(rows.length>0){
						$('#deptList').combobox("select",rows[0].drugDeptCode);
					}
		        },
		   		columns:[[    
			        {field:'ck',checkbox:true}, 
			        {field:'id',title:'id',hidden:true},
			        {field:'applyDate',title:'申请时间',hidden:true},
			        {field:'applyNumber',title:'出库单流水号',hidden:true},
			        {field:'outSerialCode',title:'出库单序号',hidden:true},
			        {field:'flagId',title:'flagId',hidden:true,width:'0%',formatter:function(value,row,index){
			        	return "<div id='"+row.drugCode+"'></div>";
			        }},
			        {field:'drugCode',title:'药品编码',hidden:true},
			        {field:'drugType',title:'药品类型',hidden:true},
			        {field:'drugQuality',title:'药品性质',hidden:true},
			        {field:'showUnit',title:'显示的单位',hidden:true},
			        {field:'companyCode',title:'供货单位代码',hidden:true},
			        {field:'purchasePrice',title:'购入价',hidden:true},
			        {field:'wholesalePrice',title:'批发价',hidden:true},
			        {field:'groupCode',title:'批次号',hidden:true},
			        {field:'tradeName',title:'药品名称',hidden : true}, 
			        {field : 'drugCommonname',title : '药品通用名',width : '20%'},
			        {field : 'drugCnamepinyin',title : '通用拼音码',hidden : true}, 
			        {field : 'drugCnamewb',title : '通用五笔码',hidden : true}, 
			        {field : 'drugCnameinputcode',title : '通用自定义码',hidden : true},
			        {field:'storeSum',title:'库存数量',width:'7%'},    
			        {field:'applyNum',title:'出库数量',width:'7%',formatter:function(value,row,index){
			        	var retVal = "";
						if (value != null && value != "" && !isNaN(value) && value >0) {
							retVal = value;
						}else{
							retVal = '0';
						}
			        	return "<input type='text' id='"+row.id+"_"+index+"' value='"+retVal+"' onchange = 'upperCase(this)' style='width:100%'>";
			        }},     
			        {field:'specs',title:'规格',width:'7%'},
			        {field:'packUnit',title:'包装单位',width:'7%',formatter: packFamater}, 
			        {field:'packQty',title:'包装数量',width:'7%'},    
			        {field:'retailPrice',title:'零售价',width:'7%',align: 'right',formatter:function(value,row,index){
			        	if (row != null) 
			        	return "<div id='retailPrice_"+index+"'>"+parseFloat(value).toFixed(4)+"</div>";
			        }},
			        {field:'outlCost',title:'出库金额',width:'7%',align: 'right',formatter:function(value,row,index){
			        	if (value != null && value != "" && !isNaN(value) && value >=0) {
							return value.toFixed(4);
						}else{
							return '0.00';
						}
			        	}},
			        {field:'mark',title:'备注',width:'7%',formatter:function(value,row,index){
			        	var retVal = "";
			        	if(value!=null&&value!=""){
			        		retVal = value;
			        	}
			        	return "<input type='text' id='mark_"+index+"' value='"+retVal+"' onchange = 'upperCasemark(this)' style='width:100%'>";
			        }}
				]]
			});
		});
		function submit(){
			if(check()){
				if(checkStoreSum()){
					var rows = $('#infolist').datagrid('getChecked');
				    if (rows.length > 0) {//选中几行的话触发事件	  
				    	for(var i=0; i<rows.length; i++){
							if(!isNaN(rows[i].applyNum) && rows[i].applyNum >= 0){
							}else{
								$.messager.alert('提示','请在第'+(i+1)+'行出库数量中输入大于0的数字');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								   return;
							}
						}
					$('#infolist').datagrid('acceptChanges');
					$('#applyoutJson').val(JSON.stringify( $('#infolist').datagrid("getChecked")));
					$('#saveForm').form('submit', {    
					    url: "<c:url value='/drug/outstore/saveOutstore.action'/>?drugStorageCode="+$("#deptList").combobox('getValue'),   
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
							  if(data=="success"){
								  $.messager.alert('提示',"出库记录保存成功！");
								  $('#list').datagrid('reload');
								  arrIds.length=0;
								  $("#totalStoreCost").html("");
								  $('#deptList').combobox('setValue','');
								  $('#queryNo').textbox('setValue','');
								  $('#infolist').datagrid('loadData', { total: 0, rows: [] });
								  rows.length=0;
								  //改变科室下拉后,查询列表数据
								  listApplyNo();
							  }else if(data!="error"){
								  $.messager.alert('提示',data);
								  $("#totalStoreCost").html("");
								  $('#deptList').combobox('setValue','');
								  $('#queryNo').textbox('setValue','');
								  //改变科室下拉后,查询列表数据
								  listApplyNo();
							  }		  
					    }
					});
				    }else {
						$.messager.alert("操作提示", "请选择要保存的条目！", "warning");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}
			}
			}else{
				return ;
			}
		}
		function check(){
			var drugStorageCode=$("#deptList").combobox('getValue');
			if(drugStorageCode==""){
				$.messager.alert('操作提示',"请选择领药科室");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
			return true;
		}
		//验证 某行信息的出库数量为0或者出库数量小于库存数量，给予用户提示“XX药出库数量不能小于0并且不能小于库存数量”
		function checkStoreSum(){
			var index = $('#infolist').datagrid("getChecked").length;
			for (var i=0;i<index;i++){
		       var dataGridRowData = $('#infolist').datagrid('getChecked')[i];
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
		//移除待出库列表
		function delList(){
			var rows = $('#infolist').datagrid("getChecked");
			var delids="";
			if(rows.length>0){
				var arr=[];
				for(var i=0; i<rows.length; i++){
					var dd = $('#infolist').edatagrid('getRowIndex',rows[i]);//获得行索引
					var tag=rows[i].drugCode;
					if(arrIds.indexOf(tag)!=-1){
						arrIds.remove(tag);
					}
					//如果数据中id不为空,说明这是存在数据库中的药品记录ID
					if(rows[i].applyNumber!="" && rows[i].applyNumber!=null){
						delids=delids+rows[i].applyNumber;
						if(i<rows.length-1){
							delids=delids+",";
						}
					}
					arr.push(dd);
				}
				//如果删除的数据里面记录到了ID,需要将此id的数据删除掉
				if(delids!=""){
					$.ajax({
						url: "<%=basePath%>drug/outstore/delAppDrugForId.action",
						data:{"ids" : delids},
						type:'post',
						success: function() {
							for(var i=arr.length-1;i>=0;i--){
								$('#infolist').edatagrid('deleteRow',arr[i]);//通过索引删除该行
							}
							//计算总金额
							totalPrice();
						}
					});
				}else{
					for(var i=arr.length-1;i>=0;i--){
						$('#infolist').edatagrid('deleteRow',arr[i]);//通过索引删除该行
					}
					//计算总金额
					totalPrice();
				}
			}else{
				$.messager.alert("操作提示", "请选择要删除的条目！","warning");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
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
					outlCost:($('#retailPrice_'+id[1]).text()*$(obj).val()),
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
		//判断是否已经填充到待出库列表
		function attrValue(row){
			var rows = $('#infolist').datagrid("getRows");
			var tag=row.drugId;
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
		
		//双击展示列表数据后,将选中的数据里面的申请单号取出来,放入查询条件后,查询
		function attrSearch(row){
			var tag=row.applyBillcode;
			$('#queryNo').textbox('setValue',tag);
			KeyDownNo();
		}
		//确认把数据填充到待出库列表
		function conform(row){
			var index =$('#infolist').datagrid('appendRow',{
				id : row.id,
				drugCode: row.drugId,
				tradeName: row.drugName,
				drugCommonname : row.drugCommonname,
				drugCnamepinyin : row.drugCnamepinyin,
				drugCnamewb : row.drugCnamewb,
				drugCnameinputcode : row.drugCnameinputcode,
				specs: row.drugSpec,
				retailPrice:row.retailPrice,
				packUnit:row.drugPackagingunit,
				applyNum:row.storeSumDrug,
				packQty:row.drugPackagingnum,
				storeSum:row.storeSumDrug,
				applyNum:0,
				outlCost:0.00,
				drugType:row.drugType,
		        drugQuality:row.drugQuality,
		        showUnit:row.showUnit,
		        companyCode:row.companyCode,
		        purchasePrice:row.drugPurchaseprice,
		        wholesalePrice:row.drugWholesaleprice,
		        groupCode:row.groupCode
			}).edatagrid('getRows').length-1;
         			$('#infolist').edatagrid('beginEdit',index);
         	var rows = $('#infolist').datagrid("getRows");
			totalRows = cloneObject(rows);
			//计算总金额
			totalPrice();
			$("#totalDivId").show();
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
			if(!isNaN(price) && price >= 0){
				price = price.toFixed(4);
			}else{
				price = '';
			}
			$("#totalStoreCost").html(price);
		}
		//页面查询检索
		var arr=[];
		function searchList(){
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
		//页面查询检索重置,下方列表
		function listReload(){
			$("#tradNameSerc").textbox('setValue','');
			searchList();
		}
		//回车事件
		function KeyDownNo() {
			searchFrom();
			searchInfo();
		}
		// 药品列表查询
		function searchFrom() {
			var drugName = $.trim($('#queryName').textbox('getValue'));
			var drugType = $.trim($('#CodeDrugtype').combobox('getValue'));
			$('#list').datagrid('load', {
				drugNameweb: drugName,
		    	drugTypeSerc:drugType
			});
		}
		// 药品列表查询重置
		function searchReload() {
			$('#CodeDrugtype').combobox('setValue','');
			$('#queryName').textbox('setValue','');
			searchFrom();
		}
		// 药品列表查询重置
		function searchReloadNext() {
			$('#deptList').combobox('setValue','');
			deptchange();
			$('#queryNo').textbox('setValue','');
			KeyDownNo();
		}
		/**
		 * 待入库药品列表查询
		 */
		function searchInfo() {
			var queryNo = $.trim($('#queryNo').textbox('getValue'));
			$('#infolist').datagrid('reload', {
				applyBillcode : queryNo,//申请单号
				opType : 11,//申请来源
				applyState : 0,//申请状态
				deptCode: "sqks",//申请科室;
				drugStorageCode : $.trim($("#deptList").combobox('getValue')),//领药科室代码
			});
		}
		//空格弹窗事件
		function KeyDown1(flg,tag){ 	    	
	    	if(flg==1){//回车键光标移动到下一个输入框
		    	if(event.keyCode==13){	
		    		event.keyCode=9;
		    	}
		    } 
		    if(flg==0){	//空格键打开弹出窗口
			    if (event.keyCode == 32)  
			    { 
			        event.returnValue=false;  
			        event.cancel = true; 
			        if(tag=="CodeDrugtype"){
			        	showWin("请药品类别","<c:url value='/ComboxOut.action'/>?xml="+"CodeDrugtype,0","50%","80%");
			        }
			    }
		    }
		} 
		//从xml文件中解析，读到下拉框
		function idCombobox(param){
			$('#CodeDrugtype').combobox({
			    url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type="+param,    
			    valueField:'encode',    
			    textField:'name',
			    multiple:false,
			    onLoadSuccess: function() {//请求成功后
					$(list).each(function(){
						  if ($('#CodeDrugtype').val() == this.Id) {
							  $('#CodeDrugtype').combobox("select",this.Id);
						   }
					});
				},
				onSelect : function(record){
					searchFrom();
				}
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
		//科室下拉框改变的时候判断是否清空待入库列表
		function deptchange(){
			var drugStorageCode=$("#deptList").combobox('getValue');//得到选择的科室code
			var rows = $('#infolist').datagrid("getRows");//得到待入库列表的行
			if (rows.length > 0) {
		    	for(var i=0; i<rows.length; i++){
		    		if(rows[i].drugDeptCode!=null && rows[i].drugDeptCode!="" && rows[i].drugDeptCode!=drugStorageCode){
		    			$('#queryNo').textbox('setValue','');
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
		    			break;
		    		}
		    	}
			}
			//改变科室下拉后,查询列表数据
			$('#listApplyNo').datagrid({
				url:"<%=basePath%>/drug/outstore/queryAppOutstore.action?deptId="+drugStorageCode,
			});
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
		//清空列表中的数据
		function listApplyNo(){
			$('#listApplyNo').datagrid('loadData',[]);
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
	<body>
	<!-- 一般出库 -->
		<div id="divLayout" class="easyui-layout" fit=true>
			<div data-options="region:'north',split:false,border:false" style="height: 150px;">
				<div id="divLayout" class="easyui-layout" fit=true>
					<div data-options="region:'north',split:false,border:false" style="height: 35px">
						<table cellspacing="0" cellpadding="0" border="0" style="padding: 5px 5px 0px 5px;border: none;">
							<tr>
								<td nowrap="nowrap">
									领药科室：<input type="hidden" value="${loginDeptId}" id="loginDeptId"><input ID="deptList"  style="width:200px;"/>
									&nbsp;申请单号：<input class="easyui-textbox" id="queryNo" style="width:140px"/>
									<a href="javascript:void(0)" onclick="KeyDownNo()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="searchReloadNext()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',split:false,border:false" style="width: 100%;">	
						<table id="listApplyNo"  data-options="fit:true,url:'${pageContext.request.contextPath}/drug/outstore/queryAppOutstore.action?menuAlias=${menuAlias}',method:'post',idField: 'id',striped:true,striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false" style="border-top:  1px solid #95b8e7;border-bottom: none;">
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
			<div data-options="region:'center',split:false,iconCls:'icon-book',border:false" style="width: 100%;">
				<div id="divLayout" class="easyui-layout" fit=true>
					<div data-options="region:'north',split:false,border:false" style="height: 35px">
						<table cellspacing="0" cellpadding="0" border="0" style="padding: 5px 5px 0px 5px;">
							<tr>
								<td nowrap="nowrap" >
									药品类型：<input type="text" id="CodeDrugtype"/>
									&nbsp;查询条件：	<input class="easyui-textbox" id="queryName" onkeydown="keyDown()" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:180px" />
									<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',split:false,border:false" style=" width: 100%;">
						<table id="list" data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
							<thead>
								<tr>
									<th data-options="field:'drugId',hidden:true">
										药品编码
									</th>
									<th data-options="field:'drugCommonname',width : '20%'">
										药品通用名
									</th>
									<th data-options="field:'storageDeptName',width : '10%'">
										库存科室
									</th>
									<th data-options="field:'drugSpec',width : '10%'">
										药品规格
									</th>
									<th data-options="field:'storeSumDrug',width : '10%'">
										库存数
									</th>
									<th data-options="field:'showUnit',formatter: packFamater,width : '10%'">
										包装单位
									</th>
									<th data-options="field:'drugPackagingnum',width : '10%'">
										包装数量
									</th>
									<th data-options="field:'drugPurchaseprice',align: 'right',width : '10%',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}">
										购入价
									</th>
									<th data-options="field:'retailPrice',align: 'right',width : '10%',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}">
										零售价
									</th>
									<th data-options="field:'drugWholesaleprice',align: 'right',width : '8%',formatter: function (value, row, index) {if (row != null && value != null) { return parseFloat(value).toFixed(4);}}">
										批发价
									</th>
									<th data-options="field:'producerCode',hidden:true">厂家id</th>
									<th data-options="field:'drugPackagingunit',hidden:true" ></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
		</div>	
			<div data-options="region:'south',split:false,iconCls:'icon-book'"style="height:40%;">
				<div id="divLayout" class="easyui-layout" fit=true>
					<div data-options="region:'north',split:false,border:false" style="height: 35px">
						<table cellspacing="0" cellpadding="0" border="0" >
							<tr>
								<td style="padding: 5px 5px 0px 5px;" nowrap="nowrap" >
									查询条件：<input class="easyui-textbox" id="tradNameSerc" data-options="prompt:'通用名,拼音,五笔,自定义'" style="width:180px" />
									<a href="javascript:void(0)" onclick="searchList()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="listReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',split:false,border:false" style="height: 100%;">
						<div style="height: 100%">
							<form id="saveForm" method="post" style="height: 100%;">
								<input type="hidden" name="applyoutJson" id="applyoutJson">
								<input type="hidden" name="infoJson" id="infoJson">
								<table id="infolist" data-options="fit:true,url:'',method:'post',idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar: '#tb'"></table>
							</form>
						</div>
					</div>
						<div id="tb" style="height: auto">
							<table width="100%";">
								<tr>
									<td style="width: 220px;">
										<shiro:hasPermission name="${menuAlias}:function:add">
											<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="submit()">保存</a>
										</shiro:hasPermission>
										<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delList()">删除</a>
										<a id="btnAddUser" href="javascript:void(0)"class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:true" onclick="exportList()">导出</a>
									</td>
									<td style="font-weight: bold;" >
										<div id="totalDivId"  style="display: none;">总零售金额：<span ID="totalStoreCost" style="font-weight: bold;color: red;"></span></div>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
</body>
</html>