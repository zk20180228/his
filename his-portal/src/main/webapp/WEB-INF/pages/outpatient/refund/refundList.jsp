<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
/**
 * 单位全局变量
 */
var packMap = "";
var minMap = "";
$('#isApplyItem').val(0)//为isApplyItem赋默认值，如果为0则表示不是从退费申请过来的项目，1表示是退费申请过来的项目
/**
 * 员工全局变量
 */
//  var empMap = "";

$(function(){
	//回车查询发票号
	bindEnterEvent('invoiceNo',getFeedetailByInvoiceNo,'easyui');
	
	$('#num').numberbox('textbox').bind('keyup', function(event) {
		//1、首先判断修改的是药品还是非药品
		var tab = $('#untab').tabs('getSelected');
		var index = $('#untab').tabs('getTabIndex',tab);
		if(index==0){//药品
			//2、判断是否选中要修改的项目
			var rowsList = $('#refundDrugList').datagrid('getSelected');
			if(rowsList==null||rowsList==""){//没有选中 提示
				$.messager.alert("操作提示","请先选择要退药品");
				var num = $('#num').numberbox('setValue',1);
				totalPrice();
			}else{//选中
				//3,、判断输入的数值是否大于1和为空的时候
				var num = $('#num').numberbox('getText');
				var price = $('#price').textbox('getValue');
				if(num==""||num==null||num<1){
					return;
				}else{
					//4、判断输入的数值有没有超过可退数量
					if(rowsList.qty<num){
						$.messager.alert("操作提示","请不要超过可退数量");
						var num = $('#num').numberbox('setValue',1);
						totalPrice();
					}else{
						//5、改变下面的列表中的数值与金额
						var rows = $('#refundDrugList').datagrid('getRows');
						if(rows.length>0){
							for(var i=0;i<rows.length;i++){
								if(rows[i].id==rowsList.id){
									$('#refundDrugList').datagrid('updateRow',{
										index: $('#refundDrugList').datagrid('getRowIndex',rows[i]),
										row: {
											totCost: rows[i].unitPrice*num,
											nobackNum: num
										}
									});
								}
							}
						}
						//6、改变上面的数值
						var rowDrug = $('#drugList').datagrid('getRows');
						if(rowDrug.length>0){
							for(var i=0;i<rowDrug.length;i++){
								if(rowDrug[i].id==rowsList.id){
									$('#drugList').datagrid('updateRow',{
										index: $('#drugList').datagrid('getRowIndex',rowDrug[i]),
										row: {
											nobackNum: rowDrug[i].qty-num
										}
									});
								}
							}
						}
						totalPrice();
					}
				}
				
			}
		}else{//非药品
			//2、判断是否选中要修改的项目
			var rowsList = $('#refundUnDrugList').datagrid('getSelected');
			if(rowsList==null||rowsList==""){//没有选中 提示
				$.messager.alert("操作提示","请先选择要退药品");
				var num = $('#num').numberbox('setValue',1);
				totalPrice();
			}else{//选中
				//3,、判断输入的数值是否大于1和为空的时候
				var num = $('#num').numberbox('getText');
				var price = $('#price').textbox('getValue');
				if(num==""||num==null||num<1){
					return;
				}else{
					//4、判断输入的数值有没有超过可退数量
					if(rowsList.qty<num){
						$.messager.alert("操作提示","请不要超过可退数量");
						var num = $('#num').numberbox('setValue',1);
						totalPrice();
					}else{
						//5、改变下面的列表中的数值与金额
						var rows = $('#refundUnDrugList').datagrid('getRows');
						if(rows.length>0){
							for(var i=0;i<rows.length;i++){
								if(rows[i].id==rowsList.id){
									$('#refundUnDrugList').datagrid('updateRow',{
										index: $('#refundUnDrugList').datagrid('getRowIndex',rows[i]),
										row: {
											totCost: rows[i].unitPrice*num,
											nobackNum: num
										}
									});
								}
							}
						}
						//6、改变上面的数值
						var rowDrug = $('#undrugList').datagrid('getRows');
						if(rowDrug.length>0){
							for(var i=0;i<rowDrug.length;i++){
								if(rowDrug[i].id==rowsList.id){
									$('#undrugList').datagrid('updateRow',{
										index: $('#undrugList').datagrid('getRowIndex',rowDrug[i]),
										row: {
											nobackNum: rowDrug[i].qty-num
										}
									});
								}
							}
						}
						totalPrice();
					}
				}
			}
		}
	});

	/**
	 * 初始化加载
	 */
	
	//查询药品单位
	$.ajax({
		url: "<%=basePath%>finance/refund/packFunction.action",
		type:'post',
		success: function(packData) {
			packMap = packData.drugPackUint;
			minMap = packData.drugMinUint;
		}
	});
	
// 	//查询员工
// 	$.ajax({
<%-- 		url: "<%=basePath%>finance/refund/empFunction.action", --%>
// 		type:'post',
// 		success: function(empData) {
// 			empMap = empData
// 		}
// 	});
	
	//实现同时切换tab效果药品
	$('#tab').tabs({    
	    border:false,    
	    onSelect:function(title){
	    	if(title=="药品"){
	    		$('#untab').tabs('select','未退药品'); 
	    		$('#num').numberbox('setValue',"");
				$('#price').textbox('setValue',"");
				$('#DrugName').textbox('setValue',"");
	    	}    
	        if(title=="非药品"){
	    		$('#untab').tabs('select','未退非药品'); 
	    		$('#num').numberbox('setValue',"");
				$('#price').textbox('setValue',"");
				$('#DrugName').textbox('setValue',"");
	    	}  
	   	 }    
	});
	
	$('#untab').tabs({    
	    border:false,    
	    onSelect:function(title){
	    	if(title=="未退药品"){
	    		$('#tab').tabs('select','药品');
	    		$('#num').numberbox('setValue',"");
				$('#price').textbox('setValue',"");
				$('#DrugName').textbox('setValue',"");
	    	}    
	        if(title=="未退非药品"){
	    		$('#tab').tabs('select','非药品'); 
	    		$('#num').numberbox('setValue',"");
				$('#price').textbox('setValue',"");
				$('#DrugName').textbox('setValue',"");
	    	}  
	   	 }    
	});
	$('#invoiceNoList').datagrid({
		onClickRow: function(rowIndex,rowData){
			$('#invoiceNosWin').window('close');
			//门诊号
		 	$('#clinicCode').val(rowData.clinicCode);
			//病历卡号
		 	$('#medicalRecord').textbox('setValue',rowData.cardNo);
			//姓名
			$('#patientName').textbox('setValue',rowData.name);
			//合同单位名称
			$('#contractId').textbox('setValue',rowData.pactName);
			//合同单位ID
			$('#pactCode').val(rowData.pactCode);
			//总钱数
			$('#totalOwnCost').numberbox('setValue',rowData.totCost);
			//剩余钱数
			$('#totalCountPrice').numberbox('setValue',rowData.totCost);
			//发票张数
			$('#no').val(1);
			//已退金额
			$('#totalUndrugPrice').numberbox('setValue',0.00);
			//自费金额
			$('#ownCost').numberbox('setValue',0.00);
			//自付金额
			$('#totalPayCost').numberbox('setValue',0.00);
			//记账金额
			$('#gradeId').numberbox('setValue',0.00);
			//应退金额
			$('#gradeId').numberbox('setValue',0.00);
			//发票号隐藏域
			$('#invoice').val(rowData.invoiceNo);
			 var invoiceNos = rowData.invoiceNo;
			 //发送请求，查询明细
			 $.messager.progress({text:'查询中，请稍后...',modal:true});
			 $.ajax({
				url: "<%=basePath%>finance/refund/getDetailByInvoiceNos.action",
				type:'post',
				data:{"invoiceNos":invoiceNos,"clinicCode":rowData.clinicCode},
				success: function(data) {
					 $.messager.progress('close');
					if(data.resMsg=="success"){
						setValueAndDatagrid(data);
					}else{
						$.messager.alert("提示","无查询结果");
					}
				}
			 });
		}
	});
	//未退费非药品列表添加双击事件
	$("#refundUnDrugList").datagrid({
		onClickRow: function(rowIndex,rowData){
			if(rowData.isApplyItem==1){
				$.messager.alert("提示","申请项目不可编辑！");
				return ;
			}
			$('#num').numberbox('setValue',rowData.nobackNum);
			$('#price').textbox('setValue',rowData.unitPrice);
			$('#DrugName').textbox('setValue',rowData.itemName);
		},
		onDblClickRow: function (rowIndex,rowData) {//双击查看
			if(rowData.isApplyItem==1){
				$.messager.alert("提示","申请项目不可取消！");
				return ;
			}
			//初始化索引
			var dIndex = "";
			//或得全部未退药品
			var listRowsUnDrugY = $('#refundUnDrugList').datagrid('getRows');
			if(listRowsUnDrugY.length>0){
				for(var i=listRowsUnDrugY.length-1;i>=0;i--){
					if(rowData.combNo==listRowsUnDrugY[i].combNo){
						//或得选中的索引
						dIndex = $("#undrugList").datagrid('getRowIndex',listRowsUnDrugY[i].id);
						//修改可退数量
						$('#undrugList').datagrid('updateRow',{
							index: dIndex,
							row: {
								nobackNum: listRowsUnDrugY[i].qty,
							}
						});
						//删除选中的药品
						$("#refundUnDrugList").datagrid('deleteRow',$("#refundUnDrugList").datagrid('getRowIndex',listRowsUnDrugY[i]));
						totalPrice();
					}
				}
			}
		}
	});
	//未退费药品列表添加双击事件
	$("#refundDrugList").datagrid({
		onClickRow: function(rowIndex,rowData){
			if(rowData.isApplyItem==1){
				$.messager.alert("提示","申请项目不可编辑！");
				return ;
			}
			$('#num').numberbox('setValue',rowData.nobackNum);
			$('#price').textbox('setValue',rowData.unitPrice);
			$('#DrugName').textbox('setValue',rowData.itemName);
		},
		onDblClickRow: function (rowIndex, rowData) {//双击查看
			if(rowData.isApplyItem==1){
				$.messager.alert("提示","申请项目不可取消！");
				return ;
			}
			//初始化索引
			var dIndex = "";
			//或得全部未退药品
			var listRowsDrugY = $('#refundDrugList').datagrid('getRows');
			if(listRowsDrugY.length>0){
				for(var i=listRowsDrugY.length-1;i>=0;i--){
					if(rowData.combNo==listRowsDrugY[i].combNo){
						//或得选中的索引
						dIndex = $("#drugList").datagrid('getRowIndex',listRowsDrugY[i].id);
						//修改可退数量
						$('#drugList').datagrid('updateRow',{
							index: dIndex,
							row: {
								nobackNum: listRowsDrugY[i].qty,
							}
						});
						//删除选中的药品
						$("#refundDrugList").datagrid('deleteRow',$("#refundDrugList").datagrid('getRowIndex',listRowsDrugY[i]));
						totalPrice();
					}
				}
			}
		}
	});
});

/*******************************开始读卡***********************************************/
//定义一个事件（读卡）
function read_card_ic(){
	var card_value = app.read_ic();
	if(card_value=='0'||card_value==undefined||card_value=='')
	{
		$.messager.alert('提示','此卡号['+card_value+']无效');
		return;
	}
	getFeedetailByInvoiceNo();
};
/*******************************结束读卡***********************************************/
/**
 * 清空表单
 */
function clears(){
	$('#medicalRecord').textbox('setValue','');
	$('#patientName').textbox('setValue','');
	$('#contractId').textbox('setValue','');
	$('#totalUndrugPrice').numberbox('setValue','');
	$('#totalOwnCost').numberbox('setValue','');
	$('#ownCost').numberbox('setValue','');
	$('#gradeId').numberbox('setValue','');
	$('#totalPayCost').numberbox('setValue','');
	$('#totalCountPrice').numberbox('setValue','');
	//清一下未退列表
	var undrugListRow = $("#undrugList").datagrid('getData');
	var drugListRow = $("#drugList").datagrid('getData');
	//清空非药品列表
	if(undrugListRow!=null&&undrugListRow.length>0){
		for (var int = 0; int < undrugListRow.length; int++) {
			$('#undrugList').datagrid('deleteRow',0);
		}
	}
	//清空药品列表
	if(drugListRow!=null&&drugListRow.length>0){
		for (var int = 0; int < drugListRow.length; int++) {
			$('#drugList').datagrid('deleteRow',0);
		}
	}
}
		
/**
 * 通过发票号查询患者退费信息，和患者退费明细
 */
 function getFeedetailByInvoiceNo(){
	//清一下未退列表
	$("#refundUnDrugList").datagrid('loadData', { total: 0, rows: [] });
	$("#refundDrugList").datagrid('loadData', { total: 0, rows: [] });
	$("#unDrugList").datagrid('loadData', { total: 0, rows: [] });
	$("#drugList").datagrid('loadData', { total: 0, rows: [] });
	
	clears();
	
	//获得录入发票号且判断是否录入
	var invoiceNo = $('#invoiceNo').textbox('getValue');
	if(invoiceNo==null||invoiceNo==""){
		$.messager.alert("操作提示","请录入发票号！");
		return;
	}
	
	//发送请求查询患者退费信息，和患者退费明细
	$.messager.progress({text:'查询中，请稍后...',modal:true});
	$.ajax({
<%-- 		url: "<%=basePath%>finance/refund/getInvoiceInfoByInvoiceNo.action", --%>
		url: "<%=basePath%>finance/refund/getInvoiceNosByInvoiceNo.action",
		type:'post',
		data:{"invoiceInfo.invoiceNo":invoiceNo},
		success: function(data) {
			$.messager.progress('close');
			if(data.resMsg=="apply"){//存在申请项目
				//输入框赋值和退费申请标识
				$('#medicalRecord').textbox('setValue',data.medicalRecord);//病历号
				$('#totalUndrugPrice').numberbox('setValue',data.applyCost)//应退金额
				$('#patientName').textbox('setValue',data.name);//姓名
				$('#contractId').textbox('setValue',data.packName);//合同单位
				$('#invoice').val(data.invoice);//发票号
				$('#payType').val(data.payType);//支付类型
				$('#isApplyItem').val(1)//为isApplyItem赋值，如果为0则表示不是从退费申请过来的项目，1表示是退费申请过来的项目
				//datagrid增加行
				var list = data.resCode;
				if(list!=null&&list.length>0){
					for(var i=0;i<list.length;i++){
						if(list[i].drugFlag==1){//药品
							addApplyDrug(list[i])
						}else if(list[i].drugFlag==2){//非药品
							addApplyUndrug(list[i])
						}
					}
				}
			}else{
				if(data.resMsg=="error"){
					$.messager.alert("操作提示",data.resCode);
				}else if(data.resMsg=="success"){
					if(data.no==1){
						$.messager.confirm('操作提示', '此发票下只有一个发票序号，将会被收回！', function(r){
							if (r){
								valueSet(data);
								setValueAndDatagrid(data);
							}
						});
					}else if(data.no>1){
	// 					$.messager.confirm('操作提示', '此发票下有多个发票序号所有发票序号将都被收回！', function(r){
	// 						if (r){
	// 							setValueAndDatagrid(data);
	// 						}
	// 					});
					var datalist = JSON.stringify(data);
					$('#invoiceNosWin').window("open");
					$('#invoiceNoList').datagrid('uncheckAll');
					$('#invoiceNoList').datagrid('loadData',data.resCode);
					}else{
						$.messager.alert("操作提示","该发票号下没有费用信息，请核对后重新查询！");
					}
				}
			}
		}
	});
}
/**
 * 申请项目增加行
 * 2017年3月6日13:37:34 By abing
 */
 function addApplyDrug(data){
	 var index =$('#refundDrugList').datagrid('appendRow',{
			id:data.id,
			itemName:data.itemName,
			specs:data.specs,
			nobackNum:data.quantity,
			doseUnit:data.priceUnit,
			confirmFlag:data.confirmFlag,
			unitPrice:data.salePrice,
			totCost:data.salePrice*data.quantity,
			qty:data.qty,
			extFlag3:data.extFlag,
			isApplyItem:1
		}).edatagrid('getRows').length-1;
 }
function addApplyUndrug(data){
	var index =$('#refundUnDrugList').datagrid('appendRow',{
		id:data.id,
		itemName:data.itemName,
		specs:data.specs,
		nobackNum:data.quantity,
		doseUnit:data.priceUnit,
		confirmFlag:data.confirmFlag,
		unitPrice:data.salePrice,
		totCost:data.salePrice*data.quantity,
		qty:data.qty,
		extFlag3:data.extFlag,
		isApplyItem:1
	}).edatagrid('getRows').length-1;
 }
/**
 * 为患者信息赋值
 */
function valueSet(data){
	//门诊号
 	$('#clinicCode').val(data.invoiceInfo.clinicCode);
	 //病历卡号
	$('#medicalRecord').textbox('setValue',data.invoiceInfo.cardNo);
	//姓名
	$('#patientName').textbox('setValue',data.invoiceInfo.name);
	//合同单位名称
	$('#contractId').textbox('setValue',data.invoiceInfo.pactName);
	//合同单位ID
	$('#pactCode').val(data.invoiceInfo.pactCode);
	//总钱数
	$('#totalOwnCost').numberbox('setValue',data.invoiceInfo.totCost);
	//剩余钱数
	$('#totalCountPrice').numberbox('setValue',data.invoiceInfo.totCost);
	//发票张数
	$('#no').val(data.no);
	//已退金额
	$('#totalUndrugPrice').numberbox('setValue',0.00);
	//自费金额
	$('#ownCost').numberbox('setValue',0.00);
	//自付金额
	$('#totalPayCost').numberbox('setValue',0.00);
	//记账金额
	$('#gradeId').numberbox('setValue',0.00);
	//应退金额
	$('#gradeId').numberbox('setValue',0.00);
	//发票号隐藏域
	$('#invoice').val(data.invoice);
}
/** 新增方法  2016-12-15 15:57:37
 * 根据发票号获取处方明细  
 */
 function getDetialByInvoiceNos(){
	 $('#invoiceNosWin').window('close');
	 var checkrows =  $('#invoiceNoList').datagrid('getChecked');
 	//门诊号
 	$('#clinicCode').val(checkrows[0].clinicCode);
	//病历卡号
 	$('#medicalRecord').textbox('setValue',checkrows[0].cardNo);
	//姓名
	$('#patientName').textbox('setValue',checkrows[0].name);
	//合同单位名称
	$('#contractId').textbox('setValue',checkrows[0].pactName);
	//合同单位ID
	$('#pactCode').val(checkrows[0].pactCode);
	//发票张数
	$('#no').val(checkrows.length);
	//已退金额
	$('#totalUndrugPrice').numberbox('setValue',0.00);
	//自费金额
	$('#ownCost').numberbox('setValue',0.00);
	//自付金额
	$('#totalPayCost').numberbox('setValue',0.00);
	//记账金额
	$('#gradeId').numberbox('setValue',0.00);
	//应退金额
	$('#gradeId').numberbox('setValue',0.00);
	 var invoiceNos = "";
	 var totCost = 0.0;
	 for(var i = 0;i<checkrows.length;i++){
		 if(invoiceNos!=""){
			 invoiceNos+="','";
		 }
		 invoiceNos += checkrows[i].invoiceNo;
		 totCost += checkrows[i].totCost;
	 }
	//总钱数
	$('#totalOwnCost').numberbox('setValue',totCost);
	//剩余钱数
	$('#totalCountPrice').numberbox('setValue',totCost);
	//发票号隐藏域
	$('#invoice').val(invoiceNos);
	 //发送请求，查询明细
	  $.messager.progress({text:'查询中，请稍后...',modal:true});
	 $.ajax({
		url: "<%=basePath%>finance/refund/getDetailByInvoiceNos.action",
		type:'post',
		data:{"invoiceNos":invoiceNos,"clinicCode":checkrows[0].clinicCode},
		success: function(data) {
			 $.messager.progress('close');
			if(data.resMsg=="success"){
				setValueAndDatagrid(data);
			}else{
				$.messager.alert("提示","无查询结果");
			}
		}
	 });
 }

/**
 * 单位渲染
 */
function functionPack(value,row,index){
	if(value!=null&&value!=''){
		if(row.extFlag3=='1'){
			return packMap[value];
		}else if(row.extFlag3=='0'){
			return minMap[value];
		}
		return value;
	}
}

/**
 * 员工渲染
 */
// function functionEmp(value,row,index){
// 	if(value!=null&&value!=''){
// 		return empMap[value];
// 	}
// }

/**
 * 封装赋值与加载列表
 */
 function setValueAndDatagrid(data){
	 $('#invoiceNosWin').window('close');
	//或得系统参数是否根据收费人来退费
	var pamaer = $('#pamaer').val();
	//获得当前登陆用户
	var userId = $('#user').val();
// 	//门诊号
// 	$('#clinicCode').val(data.invoiceInfo.clinicCode);
// 	//病历卡号
// 	$('#medicalRecord').textbox('setValue',data.invoiceInfo.cardNo);
// 	//姓名
// 	$('#patientName').textbox('setValue',data.invoiceInfo.name);
// 	//合同单位名称
// 	$('#contractId').textbox('setValue',data.invoiceInfo.pactName);
// 	//合同单位ID
// 	$('#pactCode').val(data.invoiceInfo.pactCode);
// 	//总钱数
// 	$('#totalOwnCost').numberbox('setValue',data.invoiceInfo.totCost);
// 	//剩余钱数
// 	$('#totalCountPrice').numberbox('setValue',data.invoiceInfo.totCost);
// 	//发票张数
// 	$('#no').val(data.no);
// 	//已退金额
// 	$('#totalUndrugPrice').numberbox('setValue',0.00);
// 	//自费金额
// 	$('#ownCost').numberbox('setValue',0.00);
// 	//自付金额
// 	$('#totalPayCost').numberbox('setValue',0.00);
// 	//记账金额
// 	$('#gradeId').numberbox('setValue',0.00);
// 	//应退金额
// 	$('#gradeId').numberbox('setValue',0.00);
// 	//发票号隐藏域
// 	$('#invoice').val($('#invoiceNo').textbox('getValue'));
	//加载药品退费列表
	$("#drugList").datagrid({
		data: data.drugInfoList,
		idField:'id',
		onDblClickRow: function (rowIndex, rowData) {
			if(pamaer==0){//不根据收费员
				if(rowData.flay==""||rowData.flay==null){//已发药状态的下的医嘱不许退费
					$.messager.alert("操作提示","已发药项目，不可退费！");
					return;
				}else{
					if(rowData.nobackNum>0){
						$('#num').numberbox('setValue',1);
						$('#price').textbox('setValue',rowData.unitPrice);
						$('#DrugName').textbox('setValue',rowData.itemName);
						onDbRow(rowData.id,rowData.combNo,rowData.nobackNum);
					}else{
						$.messager.alert("操作提示","对不起，您选择的项目可退数量为0，请重新选择");
					}
				}
			}else{//根据收费员
				if(userId==rowData.feeCpcd){//当收费员等于退费员的时候
					if(rowData.flay==""||rowData.flay==null){//已发药状态的下的医嘱不许退费
						$.messager.alert("操作提示","已发药项目，不可退费！");
					}else{
						$('#num').numberbox('setValue',1);
						$('#price').textbox('setValue',rowData.unitPrice);
						$('#DrugName').textbox('setValue',rowData.itemName);
						onDbRow(rowData.id,rowData.combNo,rowData.nobackNum);
					}
				}else{//当收费员不等于退费员的时候
					$.messager.alert("操作提示","对不起，您没有权限对它人收费项目进行退费");
				}
			}
		},
		rowStyler: function(index,row){
			if(row.flay==""||row.flay==null){
				return 'background-color:#98FB98;color:black;';
			}
		}
	});
	//加载非药品退费列表
	$("#undrugList").datagrid({
		data: data.unDrugInfoList,
		idField:'id',
		onDblClickRow: function (rowIndex, rowData) {
			if(rowData.flay!=""&&rowData.flay!=null){
				$.messager.alert("操作提示","该项目为辅材且主药已发药，不可退费！");
				return;
			}
			if(pamaer==0){//不根据收费员
				if(rowData.nobackNum>0){
					$('#num').numberbox('setValue',1);
					$('#price').textbox('setValue',rowData.unitPrice);
					$('#DrugName').textbox('setValue',rowData.itemName);
					onDbUnRow(rowData.id,rowData.combNo,rowData.nobackNum);
				}else{
					$.messager.alert("操作提示","对不起，您选择的项目可退数量为0，请重新选择");
				}
			}else{//根据收费员
				if(userId==rowData.feeCpcd){//当收费员等于退费员的时候
					$('#num').numberbox('setValue',1);
					$('#price').textbox('setValue',rowData.unitPrice);
					$('#DrugName').textbox('setValue',rowData.itemName);
					onDbUnRow(rowData.id,rowData.combNo,rowData.nobackNum);
				}else{//当收费员不等于退费员的时候
					$.messager.alert("操作提示","对不起，您没有权限对它人收费项目进行退费");
				}
			}
		},
		rowStyler: function(index,row){
			if(row.flay!=""&&row.flay!=null){
				return 'background-color:#98FB98;color:black;';
			}
		}
	});
}
 /**
  * 药品封装双击项目时 联动及验证
  */
 function onDbRow(id,combNo,nobackNum){
 	var sturt = 0;
 	//判断选择项目时候已经被选过的项目
 	var listRowsDrugY = $('#refundDrugList').datagrid('getRows');
 	if(listRowsDrugY!=null&&listRowsDrugY.length>0){
 		for(var n=0;n<listRowsDrugY.length;n++){
 			if(listRowsDrugY[n].id==id){
 				$.messager.alert("操作提示","对不起，该项目已被选中");
 				return;
 			}else{
 				sturt = 1;
 			}
 		}
 		if(sturt==1){
 			updateDrug(id,combNo,nobackNum);
 			totalPrice();
 		}
 	}else{
 		updateDrug(id,combNo,nobackNum);
 		totalPrice();
 	}
 }

 /**
  * 封装循环添加选中退费项目 与 修改可退数量 
  */
 function updateDrug(id,combNo,nobackNum){
	var numbers = 0;
 	var listRows = $('#drugList').datagrid('getRows');
 	if(listRows!=null&&listRows.length>0){
 		for(var j=0;j<listRows.length;j++){
 			if(listRows[j].combNo==combNo){
 				if($("#cheacks").is(":checked")){
 					numbers=listRows[j].nobackNum;				
 				}else{
 					numbers=1;
 				}
 				if(listRows[j].flay!=null&&listRows[j].flay!=""){
	 				var index =$('#refundDrugList').datagrid('appendRow',{
	 					id:listRows[j].id,
	 					itemName:listRows[j].itemName,
	 					specs:listRows[j].specs,
	 					combNo:listRows[j].combNo,
	 					nobackNum:numbers,
	 					doseUnit:listRows[j].priceUnit,
	 					confirmFlag:0,
	 					unitPrice:listRows[j].unitPrice,
	 					totCost:listRows[j].unitPrice*numbers,
	 					qty:listRows[j].nobackNum,
	 					extFlag3:listRows[j].extFlag3,
	 					isApplyItem:0
	 				}).edatagrid('getRows').length-1;
	 				
	 				$('#drugList').datagrid('updateRow',{
	 					index: $('#drugList').datagrid('getRowIndex',listRows[j]),
	 					row: {
	 						nobackNum: listRows[j].nobackNum-numbers,
	 					}
	 				});
 				}
 			}
 		}
 	}
 }

/**
 * 非药品封装双击项目时 联动及验证
 */
function onDbUnRow(id,combNo,nobackNum){
 	var sturt = 0;
 	//判断选择项目时候已经被选过的项目
 	var listRowsUnDrugY = $('#refundUnDrugList').datagrid('getRows');
 	if(listRowsUnDrugY!=null&&listRowsUnDrugY.length>0){
 		for(var n=0;n<listRowsUnDrugY.length;n++){
 			if(listRowsUnDrugY[n].id==id){
 				$.messager.alert("操作提示","对不起，该项目已被选中");
 				return;
 			}else{
 				sturt = 1;
 			}
 		}
 		if(sturt==1){
 			updateUnDrug(id,combNo,nobackNum);
 			totalPrice();
 		}
 	}else{
 		updateUnDrug(id,combNo,nobackNum);
 		totalPrice();
 	}
}

/**
 * 封装循环添加选中退费项目 与 修改可退数量 
 */
function updateUnDrug(id,combNo,nobackNum){
	var numbers = 0;
 	var listRows = $('#undrugList').datagrid('getRows');
 	if(listRows!=null&&listRows.length>0){
 		for(var j=0;j<listRows.length;j++){
 			if(listRows[j].combNo==combNo){
 				if($("#cheacks").is(":checked")){
 					numbers=listRows[j].nobackNum;				
 				}else{
 					numbers=1;
 				}
 				var index =$('#refundUnDrugList').datagrid('appendRow',{
 					id:listRows[j].id,
 					itemName:listRows[j].itemName,
 					specs:listRows[j].specs,
 					combNo:listRows[j].combNo,
 					nobackNum:numbers,
 					doseUnit:listRows[j].priceUnit,
 					confirmFlag:0,
 					unitPrice:listRows[j].unitPrice,
 					totCost:listRows[j].unitPrice*numbers,
 					qty:listRows[j].nobackNum,
 					isApplyItem:0
 				}).edatagrid('getRows').length-1;
 				
 				$('#undrugList').datagrid('updateRow',{
 					index: $('#undrugList').datagrid('getRowIndex',listRows[j]),
 					row: {
 						nobackNum: listRows[j].nobackNum-numbers,
 					}
 				});
 			}
 		}
 	}
}
 
 
/**
 * 金额计算
 */
function totalPrice(){
	var totalDrugPrice = 0;
	var totalUndrugPrice = 0;
	var totalRefundDrugPrice = 0;
	var totalRefundUnfrugPrice = 0;
	
	var rows = $("#drugList").datagrid("getRows");
	var unRows = $("#undrugList").datagrid("getRows");
	var refundRows = $("#refundDrugList").datagrid("getRows");
	var refundUnDrugRows = $("#refundUnDrugList").datagrid("getRows");
	if(rows.length!=null&&rows.length!=0){
		for(var i=0;i<rows.length;i++){
			totalDrugPrice += rows[i].totCost;
		}
	}
	if(unRows.length!=null&&unRows.length!=0){
		for(var i=0;i<unRows.length;i++){
			totalUndrugPrice += unRows[i].totCost;
		}
	}
	if(refundRows.length!=null&&refundRows.length!=0){
		for(var i=0;i<refundRows.length;i++){
			totalRefundDrugPrice += refundRows[i].totCost;
		}
	}
	if(refundUnDrugRows.length!=null&&refundUnDrugRows.length!=0){
		for(var i=0;i<refundUnDrugRows.length;i++){
			totalRefundUnfrugPrice += refundUnDrugRows[i].totCost;
		}
	}
	$("#totalOwnCost").numberbox('setValue',totalDrugPrice+totalUndrugPrice);
	$("#totalUndrugPrice").numberbox('setValue',totalRefundDrugPrice+totalRefundUnfrugPrice);
	$("#totalCountPrice").numberbox('setValue',totalDrugPrice+totalUndrugPrice-totalRefundDrugPrice-totalRefundUnfrugPrice);
	$('#ownCost').numberbox('setValue',totalDrugPrice+totalUndrugPrice);
}

/**
 *清屏操作
 */
function clearFeed(){
	$('#infoForm').form('clear');
	$("#drugList").datagrid('loadData', { total: 0, rows: [] });
	$("#undrugList").datagrid('loadData', { total: 0, rows: [] });	
	$("#refundUnDrugList").datagrid('loadData', { total: 0, rows: [] });
	$("#refundDrugList").datagrid('loadData', { total: 0, rows: [] });
	$('#DrugName').textbox('clear');
	$('#price').textbox('clear');
	$('#num').numberbox('clear');
}

/**
 *退费操作
 */
function refund(){
	//获取退费金额
	var totalUndrugPrice = $('#totalUndrugPrice').numberbox('getValue');
	if(totalUndrugPrice==""||totalUndrugPrice==0||totalUndrugPrice==null){
		$.messager.alert("操作提示","请先选择退费项目");
		return;
	}
	//先清空一下弹出窗口数据
	$('#windowFeedForm').form('clear');
	//系统参数退费路径
	var payType = $('#payType').val();
	//药品列表条数
	var rows = $("#drugList").datagrid("getRows");
	var rowsLength = rows.length;
	//非药品列表条数
	var unRows = $("#undrugList").datagrid("getRows");
	var unRowsLength = unRows.length;
	//退药品列表条数
	var refundRows = $("#refundDrugList").datagrid("getRows");
	var refundRowsLength = refundRows.length;
	//退非药品列表条数
	var refundUnDrugRows = $("#refundUnDrugList").datagrid("getRows");
	var refundUnDrugRowsLength = refundUnDrugRows.length;
	
	if(rowsLength+unRowsLength==refundRowsLength+refundUnDrugRowsLength){//判断是否是全部退费
		//全部退费
		$('#flay').val(0);
	}else{
		//不菲退费
		$('#flay').val(1);
	}
	if(payType==1){
		$.messager.confirm('操作提示', '系统默认选择现金退费', function(r){
			if (r){
				wondowPaytype(payType);
			}
		});
	}else if(payType==2){
		$.messager.confirm('操作提示', '系统默认选择患者账户退费', function(r){
			if (r){
				wondowPaytype(payType);
			}
		});
	}
	
}

/**
 *弹窗先显示退费情况
 */
function wondowPaytype(payType){
	//获取发票号
	var invoiceNo = $('#invoice').val();
	//获取退费金额
	var maney = $('#totalCountPrice').numberbox('getValue');
	var invo = invoiceNo.split("','");
	$('#invoices').text(invo[0]);
	if(payType==1){
		$('#cash').numberbox('setValue',maney);
	}else if(payType==2){
		$('#hospitalAccount').numberbox('setValue',maney);
	}
	//赋值金额
	$('#actualCollection').text(maney);
	$('#sumFees').text(maney);
	$('#shouldPay').text(maney);
	$('#giveChange').text(0);
	$('#zje').text(maney);
	//打开弹窗
	$('#saveWondowFeed').window('open');
}

/**
 *退费保存
 */
function saveRefund(){
	//获取发票号
	var invoiceNo = $('#invoice').val();
	//获取药品退费内容
	var rows = $('#refundDrugList').datagrid("getRows");
	var dataArr = new Array();
	if(rows!=null&&rows.length>0){
		for(var i=0;i<rows.length;i++){
			if(rows[i].flay==null||rows[i].flay==''){
				dataArr[dataArr.length] = rows[i];
			}
		}
	}
	var refundDrugList = JSON.stringify(dataArr);
	
	//获得非药品退费内容
	var refundUnDrugList = JSON.stringify($('#refundUnDrugList').datagrid("getRows"));
	//获取药品内容
	var drugLists = $('#drugList').datagrid("getRows");
	var dataArrs = new Array();
	if(drugLists!=null&&drugLists.length>0){
		for(var i=0;i<drugLists.length;i++){
			if(drugLists[i].flay!=null&&drugLists[i].flay!=""){
				dataArrs[dataArrs.length] = drugLists[i];
			}
		}
	}
	
	var drugList = JSON.stringify(dataArrs);
	//获取非药品内容
	var undDrugList = JSON.stringify($('#undrugList').datagrid("getRows"));
	//系统参数退费路径
	var payType = $('#payType').val();
	//或得病历号
	var patientNo = $('#patientNo').val();
	//获取退费金额
	var maney = $('#totalUndrugPrice').numberbox('getValue');
	//获取剩余金额
	var price = $('#totalCountPrice').numberbox('getValue');
	
	$.messager.progress({text:'保存中，请稍后...',modal:true});
	var isApplyItem = $('#isApplyItem').val();
	//退费保存
	if(1==isApplyItem){//走退费申请项目保存方法
		var drugId = '';
		var unDrugId = '';
		var medicalRecord = $('#medicalRecord').textbox('getValue');//病历号
		var applyCost = $('#totalUndrugPrice').numberbox('getValue')//应退金额
		var invoiceNo = $('#invoice').val();
		var cancelDrug = $('#refundDrugList').datagrid('getRows');
		var cancelUnDrug = $('#refundUnDrugList').datagrid('getRows');
		if(cancelDrug!=null&&cancelDrug.length>0){
			for(var i=0;i<cancelDrug.length;i++){
				if(drugId!=""){
					drugId += "','";
				}
				drugId += cancelDrug[i].id;
			}
		}
		if(cancelUnDrug!=null&&cancelUnDrug.length>0){
			for(var i=0;i<cancelUnDrug.length;i++){
				if(unDrugId!=""){
					unDrugId += "','";
				}
				unDrugId += cancelUnDrug[i].id;
			}
		}
		$.ajax({
			url: "<%=basePath%>finance/refundConfirm/refundSave.action",
			data:{"drugIds":drugId+"','"+unDrugId,"invoiceNo":invoiceNo,"payType":payType,"applyCost":applyCost,"medicalRecord":medicalRecord},
			type:'post',
			success: function(dataMap) {
				if(dataMap.resMsg=="success"){
					if(dataMap.resCode=="success"){
						$.messager.confirm('操作提示', '是否打印发票', function(r){
							if (r){
								var INVOICE_NO=invoiceNo;//发票号
								var timerStr = Math.random();
								invoiceArr=INVOICE_NO.split(",");
								for(var i=0;i<invoiceArr.length;i++){
									window.open ("<%=basePath%>iReport/iReportPrint/iReportInvoiceMedicinelist.action?randomId="+timerStr+"&INID="+invoiceArr[i]+"&fileName=Invoice",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no');
								}
								window.location.href="<c:url value='/finance/refund/refundList.action'/>?menuAlias=${menuAlias}";
							}else{
								window.location.href="<c:url value='/finance/refund/refundList.action'/>?menuAlias=${menuAlias}";
							}
						});
					}else{
						$.messager.confirm('操作提示', '退费成功', function(r){
							if (r){
								window.location.href="<c:url value='/finance/refund/refundList.action'/>?menuAlias=${menuAlias}";
							}else{
								window.location.href="<c:url value='/finance/refund/refundList.action'/>?menuAlias=${menuAlias}";
							}
						});
					}
				}else if(dataMap.resMsg=="error"){
					$.messager.alert("操作提示", dataMap.resCode);
				}
			}
		});
	}else{//直接退费保存方法
		$.ajax({
			url: "<%=basePath%>finance/refund/saveRefund.action",
			type:'post',
			data:{"invoiceInfo.invoiceNo":invoiceNo,"refundDrugList":refundDrugList,"refundUnDrugList":refundUnDrugList,"drugList":drugList,"undDrugList":undDrugList,"payType":payType,"patientNo":patientNo,"maney":maney,"price":price,"invoiceNo":invoiceNo},
			success: function(dataMap) {
				$.messager.progress('close');
				if(dataMap.resMsg=="success"){
					if(dataMap.resCode=="success"){
						$.messager.confirm('操作提示', '是否打印发票', function(r){
							if (r){
								var INVOICE_NOS = dataMap.invoiceNos//发票号集合
								var invoice =new Array();
								invoice=INVOICE_NOS.split("','");
								var timerStr = Math.random();
								for(var v=0;v<invoice.length;v++){
									window.open ("<%=basePath%>iReport/iReportPrint/iReportInvoiceMedicinelist.action?randomId="+timerStr+"&INID="+invoice[v]+"&fileName=Invoice",'newwindow'+v,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no');
									window.location.href="<c:url value='/finance/refund/refundList.action'/>?menuAlias=${menuAlias}";
								}
							}else{
								window.location.href="<c:url value='/finance/refund/refundList.action'/>?menuAlias=${menuAlias}";
							}
						});
					}else{
						$.messager.confirm('操作提示', '退费成功', function(r){
							if (r){
								window.location.href="<c:url value='/finance/refund/refundList.action'/>?menuAlias=${menuAlias}";
							}else{
								window.location.href="<c:url value='/finance/refund/refundList.action'/>?menuAlias=${menuAlias}";
							}
						});
					}
				}else if(dataMap.resMsg=="error"){
					$.messager.alert("操作提示", dataMap.resCode);
				}
			}
		});
	}
}
/**  
 *  
 * 药品组合号渲染
 *
 *
 */
function functionGroup(value,row,index){
	var rwos = $('#drugList').datagrid('getRows');
	if(value==null||value==''){
		return null;
	}else{
		if(value==null||value==''){
			return null;
		}else{
			if(index==0&&rwos.length>1&&value==rwos[index+1].combNo){
				return "┓";
			}else if(index==0){
				return "";
			}else if(index==rwos.length-1&&value==rwos[index-1].combNo){
				return "┛";
			}else if(index==rwos.length-1){
				return "";
			}else if(value!=rwos[index-1].combNo&&value==rwos[index+1].combNo){
				return "┓";
			}else if(value==rwos[index-1].combNo&&value!=rwos[index+1].combNo){
				return "┛";
			}else if(value==rwos[index-1].combNo&&value==rwos[index+1].combNo){
				return "┫";
			}else{
				return "";
			}
		}
	}
}
/**
 * 非药品组合号渲染
 */
function functionUndrugGroup(value,row,index){
	var rwos = $('#undrugList').datagrid('getRows');
	if(value==null||value==''){
		return null;
	}else{
		if(index==0&&rwos.length>1&&value==rwos[index+1].combNo){
			return "┓";
		}else if(index==0){
			return "";
		}else if(index==rwos.length-1&&value==rwos[index-1].combNo){
			return "┛";
		}else if(index==rwos.length-1){
			return "";
		}else if(value!=rwos[index-1].combNo&&value==rwos[index+1].combNo){
			return "┓";
		}else if(value==rwos[index-1].combNo&&value!=rwos[index+1].combNo){
			return "┛";
		}else if(value==rwos[index-1].combNo&&value==rwos[index+1].combNo){
			return "┫";
		}else{
			return "";
		}
	}
}

</script>
</head>
<body> 
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" style="width: 100%;height: 120px;">
		<div style="padding:5px 5px 5px 5px;">
			<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>&nbsp;&nbsp;
			<a id="btnSave" href="javascript:refund();" class="easyui-linkbutton" data-options="iconCls:'icon-backfee2',disabled:false">退费</a>&nbsp;&nbsp;
			<a id="btnClear" href="javascript:clearFeed();" class="easyui-linkbutton" data-options="iconCls:'icon-clear',disabled:false">清屏</a>&nbsp;&nbsp;
			<input id="pactCode" type="hidden">
			<input id="user" type="hidden" value="${feedetail.feeCpcd}">
			<input id="pamaer" type="hidden" value="${isFeeUser }">
			<input id="payType" type ="hidden" value="${payType }">
			<input id="no" type="hidden" >
			<input id="invoice" type="hidden" >
			<input id="flay" type="hidden" >
			<inpur id="isApplyItem" type="hidden"/>
			<input id="patientNo" type ="hidden" value="${feedetail.patientNo}">
		</div> 
		<div>
			<fieldset class="changeskin">
		   		<legend><font style="font-weight: bold;font-size: 12px;">患者信息</font></legend>	
		   		<form id="infoForm">
			    	<table border="0" style="float:left">
						<tr>
							<td nowrap="nowrap" align="right">发票号：</td>
							<td nowrap="nowrap"><input class="easyui-textbox" id="invoiceNo" style="width:120px" /></td>
							<td nowrap="nowrap" align="right">病历号：</td>
							<td nowrap="nowrap"><input class="easyui-textbox" id="medicalRecord" style="width:120px" readonly="readonly"/></td><%--prompt:'请输入病历号' --%>
							<td nowrap="nowrap" align="right">姓名：</td>
							<td nowrap="nowrap"><input class="easyui-textbox" id="patientName"  style="width:120px" readonly="readonly"/></td>
							<td nowrap="nowrap" align="right">合同单位：</td>
							<td nowrap="nowrap"><input class="easyui-textbox" id="contractId"  style="width:120px" readonly="readonly"/></td>
							<td nowrap="nowrap" align="right">应退金额：</td>
							<td nowrap="nowrap"><input class="easyui-numberbox" id="totalUndrugPrice" style="width:120px"  readonly="readonly" data-options="min:0,precision:2"/></td>
						</tr>
						<tr>
							<td nowrap="nowrap" align="right">总金额：</td>
							<td nowrap="nowrap"><input class="easyui-numberbox" id="totalOwnCost" style="width:120px" readonly="readonly" data-options="min:0,precision:2"/></td>
							<td nowrap="nowrap" align="right">自费金额：</td>
							<td nowrap="nowrap"><input class="easyui-numberbox" id="ownCost" style="width:120px" readonly="readonly" data-options="min:0,precision:2"/></td>
							<td nowrap="nowrap" align="right">自付金额：</td>
							<td nowrap="nowrap"><input class="easyui-numberbox" id="totalPayCost" style="width:120px" readonly="readonly" data-options="min:0,precision:2"/></td>
							<td nowrap="nowrap" align="right">记账金额：</td>
							<td nowrap="nowrap"><input class="easyui-numberbox" id="gradeId" style="width:120px" readonly="readonly" data-options="min:0,precision:2"/></td>
							<td nowrap="nowrap" align="right">剩余金额：</td>
							<td nowrap="nowrap"><input class="easyui-numberbox" id="totalCountPrice" style="width:120px" readonly="readonly" data-options="min:0,precision:2"/></td>
						</tr>
					</table>
				</form>
			</fieldset>
		</div>
	</div>   
    <div data-options="region:'center',border:false" style="height: 400px;width: 100%;">
   		<div id="tab" class="easyui-tabs"  data-options="tabPosition:'bottom',toolbar:'#toolbarId',fit:true">   
		    <div title="药品" style="padding:3px;">
		    	<table id="drugList" class="easyui-datagrid" data-options="toolbar:'#toolbarId',fit:true,checkOnSelect:false,selectOnCheck:false,singleSelect:true">   
				    <thead>   
				        <tr>   
				            <th data-options="field:'itemName',width:'15%'">药品名称</th>   
				            <th data-options="field:'combNo',formatter:functionGroup,sortable:'true'" width='10%'>组</th>   
				            <th data-options="field:'specs',width:'15%'">规格</th>
				            <th data-options="field:'qty',width:'10%'">数量</th>   
				            <th data-options="field:'priceUnit',formatter:functionPack,width:'10%'">单位</th>   
				            <th data-options="field:'nobackNum',width:'10%'">可退数量</th>
				            <th data-options="field:'totCost',align:'right',width:'10%'">金额</th>   
				            <th data-options="field:'doseOnce',width:'10%'">每次量和付数</th> 
				            <th data-options="field:'feeCpcdname', width:'10%'">收费员</th> 
				            <th data-options="field:'id',hidden:true">id</th>
				            <th data-options="field:'unitPrice',hidden:true">单价</th>
				            <th data-options="field:'drugFlag',hidden:true">药品/非药品</th>     
				            <th data-options="field:'recipeNo',hidden:true">处方号</th>  
				            <th data-options="field:'sequenceNo',hidden:true">处方内流水号</th> 
				            <th data-options="field:'execDpcd',hidden:true">执行科室</th> 
				            <th data-options="field:'itemCode',hidden:true">项目ID</th>
				            <th data-options="field:'extFlag3',hidden:true">包装/最小</th> 
				        </tr>   
				    </thead>   
				</table>
		    </div>
		    <div title="非药品" style="padding:3px;">
		    	<table id="undrugList" class="easyui-datagrid" data-options="fit:true,checkOnSelect:false,selectOnCheck:false,singleSelect:true">   
				    <thead>   
				        <tr>   
				            <th data-options="field:'itemName',width:'15%'">非药品名称</th>   
				            <th data-options="field:'combNo',formatter:functionUndrugGroup,width:'10%'">组</th>   
				            <th data-options="field:'specs',width:'15%'">规格</th>
				            <th data-options="field:'qty',width:'10%'">数量</th>   
				            <th data-options="field:'priceUnit',width:'10%'">单位</th>   
				            <th data-options="field:'nobackNum',width:'10%'">可退数量</th>
				            <th data-options="field:'totCost',align:'right',width:'10%'">金额</th>   
				            <th data-options="field:'doseOnce',width:'10%'">每次量和付数</th> 
				            <th data-options="field:'id',hidden:true">id</th>
				            <th data-options="field:'feeCpcdname', width:'10%'">收费员</th> 
				            <th data-options="field:'unitPrice',align:'right',hidden:true">单价</th> 
				            <th data-options="field:'drugFlag',hidden:true">药品/非药品</th>     
				            <th data-options="field:'recipeNo',hidden:true">处方号</th> 
				            <th data-options="field:'sequenceNo',hidden:true">处方内流水号</th> 
				            <th data-options="field:'execDpcd',hidden:true">执行科室</th>
				            <th data-options="field:'itemCode',hidden:true">项目ID</th>
				            <th data-options="field:'extFlag3',hidden:true">包装/最小</th>            
				        </tr>   
				    </thead>   
				</table>  
		    </div>   
		</div>
    </div>
    <div data-options="region:'south'" style="height: 50%;width: 100%;">
    	<div class="easyui-layout" data-options="fit:true">   
		    <div data-options="region:'north',border:false" style="height:40px;width:100%">
		    	<table style="padding:8px 5px 1px 5px;" >
					<tr >
						<td style="padding-left: 10px;">药品名称：<input class="easyui-textbox" id="DrugName" readonly="readonly"/></td>
						<td style="padding-left: 10px;">价格：<input class="easyui-textbox" id="price" readonly="readonly"/></td>
						<td style="padding-left: 10px;">数量：<input class="easyui-numberbox" id="num" data-options="min:1"/></td>					
						<td style="padding-left: 20px;"><input type="checkbox" id="cheacks"/>全退</td>
					</tr>
				</table> 
		    </div>   
		    <div data-options="region:'center',border:false" style="height:40%;width:100%">
			    <div id="untab" class="easyui-tabs"  data-options="tabPosition:'bottom',fit:true">   
				    <div title="待退药品" style="padding:3px;">
				    	<table id="refundDrugList" class="easyui-datagrid" data-options="fit:true,checkOnSelect:false,selectOnCheck:false,singleSelect:true">   
						    <thead>   
						        <tr>   
						            <th data-options="field:'itemName',width:'20%'">待退药品名称</th>   
						            <th data-options="field:'specs',width:'15%'">规格</th>   
						            <th data-options="field:'combNo',hidden:true,width:'15%'">组合号</th>   
						            <th data-options="field:'nobackNum',width:'15%'">待退数量</th> 
						            <th data-options="field:'doseUnit',formatter:functionPack,width:'15%'">单位</th>   
						            <th data-options="field:'confirmFlag',width:'15%'">标志</th>
						            <th data-options="field:'unitPrice',align:'right',width:'10%'">单价</th>   
						            <th data-options="field:'totCost',align:'right',width:'10%'">金额</th>
						            <th data-options="field:'id',hidden:true">id</th> 
						            <th data-options="field:'qty',hidden:true">原可退数量</th>   
						            <th data-options="field:'extFlag3',hidden:true">最大/最小单位</th>   
						        </tr>   
						    </thead>   
						</table>  
				    </div>   
				    <div title="待退非药品"  style="padding:3px;">   
				        <table id="refundUnDrugList" class="easyui-datagrid" data-options="fit:true,checkOnSelect:false,selectOnCheck:false,singleSelect:true">   
						    <thead>   
						        <tr>   
						            <th data-options="field:'itemName',width:'20%'">待退非药品名称</th>   
						            <th data-options="field:'specs',width:'15%'">规格</th>   
						            <th data-options="field:'combNo',hidden:true,width:'15%'">组合号</th>   
						            <th data-options="field:'nobackNum',width:'15%'">待退数量</th> 
						            <th data-options="field:'doseUnit',width:'15%'">单位</th>   
						            <th data-options="field:'confirmFlag',width:'15%'">标志</th>
						            <th data-options="field:'unitPrice',align:'right',width:'10%'">单价</th>   
						            <th data-options="field:'totCost',align:'right',width:'10%'">金额</th>
						            <th data-options="field:'id',hidden:true">id</th>
						            <th data-options="field:'qty',hidden:true">原可退数量</th>   
						        </tr>   
						    </thead>   
						</table>
				    </div>   
				</div>
		    </div>   
		</div>  
    </div>   
</div>
<div id="saveWondowFeed" class="easyui-window" title="支付方式" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:800px;height:500px;">
    <form id="windowFeedForm">
    	<div style="padding:5px 5px 5px 5px;">	   	
			<table>
				<tr>
					<td>
						<a href="javascript:saveRefund();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-money_yen'">退费</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#saveWondowFeed').window('close')" data-options="iconCls:'icon-cancel'">关闭</a>
					</td>
				</tr>
			</table>
		</div>
		<div style="padding:0px 5px 5px 5px;">	   	
			<table class="honry-table" style="width:100%">
				<tr>
					<td style="background: #E0ECFF;width:50px;text-align: right;font-size:14px;">实付金额：</td>
					<td width="20%" id="actualCollection"></td>
					<td style="background: #E0ECFF;width:50px;text-align: right;font-size:14px;">应缴金额：</td>
					<td width="15%" id="shouldPay"></td>
					<td style="background: #E0ECFF;width:50px;text-align: right;font-size:14px;">找零金额：</td>
					<td width="20%" id="giveChange"></td>
				</tr>
				<tr>
					<td style="background: #E0ECFF;width:50px;text-align: right;font-size:14px;">自费金额：</td>
					<td width="15%"></td>
					<td style="background: #E0ECFF;width:50px;text-align: right;font-size:14px;">记账金额：</td>
					<td width="20%"></td>
					<td style="background: #E0ECFF;width:50px;text-align: right;font-size:14px;">总金额：</td>
					<td width="20%" id="zje"></td>
				</tr>
				<tr>
					<td style="background: #E0ECFF;width:50px;text-align: right;font-size:14px;">应缴金额：</td>
					<td width="15%" id="yjjes"></td>
					<td style="background: #E0ECFF;width:50px;text-align: right;font-size:14px;">超标药金额：</td>
					<td width="20%"></td>
					<td style="background: #E0ECFF;width:50px;text-align: right;font-size:14px;">自负金额：</td>
					<td width="20%"></td>
				</tr>
			</table>
		</div>
		<div style="padding:0px 5px 5px 5px;">
			<div id="scheduleTabId" class="easyui-tabs" style="width:100%;height:300px;">   
			    <div title="支付方式">   
			    	<table class="honry-table" style="width:100%">
			    		<tr>
			    			<td>支付方式</td>
			    			<td>金额</td>
			    		</tr>
			    		<tr>
			    			<td>现金</td>
			    			<td><input class="easyui-numberbox" data-options="precision:2" id="cash" readonly="readonly" style="width:100px"></td>
			    		</tr>
			    		<tr>
			    			<td>院内账户</td>
			    			<td><input class="easyui-numberbox" data-options="precision:2" id="hospitalAccount" style="width:100px" readonly="readonly"></td>
			    		</tr>
			    	</table>
			    </div>   
			    <div title="分发票">   
			       <table class="honry-table"   style="width:100%">
						<tr>
							<td colspan="6" style="background:#E0ECFF;font-size:14px;">分发票</td>
						</tr>
						<tr>
							<td style="background:#E0ECFF;font-size:14px;width:50px;">票号：</td>
							<td id="invoices" style="font-size:14px;width:150px;"></td>
							<td style="background:#E0ECFF;font-size:14px;width:50px;">金额：</td>
							<td style="font-size:14px;width:150px;" id="sumFees"></td>
							<td style="background:#E0ECFF;font-size:14px;width:50px;">发票类型：</td>
							<td style="font-size:14px;width:150px;">收费</td>
						</tr>
						<tr>
							<td style="background:#E0ECFF;font-size:14px;width:50px;">自费：</td>
							<td style="font-size:14px;width:150px;"></td>
							<td style="background:#E0ECFF;font-size:14px;width:50px;">自付：</td>
							<td style="font-size:14px;width:150px;"></td>
							<td style="background:#E0ECFF;font-size:14px;width:50px;">记账：</td>
							<td style="font-size:14px;width:150px;"></td>
						</tr>
					</table>
			    </div>   
			</div>  
		</div>
	</form>
	<div id="toolbarId">
		<table>
			<tr>
				<td>已发药项目：<span style="background-color: #98FB98">&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
			</tr>
		</table>
	</div>
	  <div id="invoiceNosWin" class="easyui-window" title="请选择发票" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false"style="top:25%;left:30%;width:700px;height:400px;">
	  	<td>
			<a href="javascript:getDetialByInvoiceNos();void(0)" class="easyui-linkbutton"data-options="iconCls:'icon-tick'" >确定</a>
			&nbsp;&nbsp;
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#invoiceNosWin').window('close')" data-options="iconCls:'icon-cancel'">关闭</a>
		</td>
    	<table id="invoiceNoList" class="easyui-datagrid" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true" style="width:100%;height:100%">
   			  <thead>   
			        <tr> 
			       	 	<th data-options="field:'ck',checkbox:true" ></th>
			            <th data-options="field:'invoiceNo'" style="width: 25%">发票号</th>   
			            <th data-options="field:'name'" style="width: 15%">姓名</th>   
			            <th data-options="field:'totCost'" style="width: 10%">总金额</th>
			            <th data-options="field:'realCost'" style="width: 15%">实付金额</th>
			            <th data-options="field:'regDate'" style="width: 30%">挂号日期</th>
			        </tr>   
			  </thead>   
   		</table>
    </div>
</div>
</body>
</html>