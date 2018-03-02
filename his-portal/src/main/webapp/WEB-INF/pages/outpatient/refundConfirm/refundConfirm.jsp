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
var packMap = "";//药品编码
var minMap = "";//非药品编码
/**
 * 员工全局变量
 */
//  var empMap = "";
$(function(){
	//回车查询发票号
	bindEnterEvent('invoiceNo',query,'easyui');
	
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
});

function query(){
	$('#medicalRecord').textbox('setValue','');
	$('#patientName').textbox('setValue','');
	$('#applyCost').numberbox('setValue','');
	
	$("#list").datagrid('loadData', { total: 0, rows: [] });
	var invoiceNo = $('#invoiceNo').textbox('getValue');
	if(invoiceNo==""||invoiceNo==null){
		$.messager.alert("操作提示","请先录入发票号");
		return;
	}
	
	$.ajax({
		url: "<%=basePath%>finance/refundConfirm/query.action",
		data:{"cancelitem.billNo":invoiceNo},
		type:'post',
		success: function(data) {
			if(data.resMsg=="error"){
				$.messager.alert("操作提示",data.resCode);
			}else{
				$('#patientName').textbox('setValue',data.name);
				$('#medicalRecord').textbox('setValue',data.medicalRecord);
				$('#applyCost').numberbox('setValue',data.applyCost);
				$('#payType').val(data.payType);
				$('#invoice').val(data.invoice);
				findList(data);
			}
		}
	});
	
}

/**
 * 单位渲染
 */
function functionPack(value,row,index){
	if(value!=null&&value!=''){
		if(row.drugFlag==1){
			if(row.extFlag=='1'){
				return packMap[value];
			}else if(row.extFlag=='0'){
				return minMap[value];
			}
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


function findList(data){
	$("#list").datagrid({
		data: data.resCode
	});
}

function functionSum(value,row,index){
	value = (row.quantity)*(row.salePrice);
	return value;
}

function clearFeed(){
	$('#invoiceNo').textbox('setValue','');
	$('#medicalRecord').textbox('setValue','');
	$('#patientName').textbox('setValue','');
	$('#applyCost').numberbox('setValue','');
	
	$("#list").datagrid('loadData', { total: 0, rows: [] });
}


function refundSave(){
	var payType = $('#payType').val();
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

function wondowPaytype(payType){
	var medicalRecord = $('#medicalRecord').textbox('getValue');
	var applyCost = $('#applyCost').numberbox('getValue');
	var invoiceNo = $('#invoice').val();
	var rows = $("#list").datagrid('getRows');
	if(rows.length>0){
		var drugIds = "";
		for(var i=0;i<rows.length;i++){
			if(drugIds!=""){
				drugIds = drugIds + "','";
			}
			drugIds = drugIds + rows[i].id;
		}
		$.messager.progress({text:'执行中，请稍后...',modal:true});
		$.ajax({
			url: "<%=basePath%>finance/refundConfirm/refundSave.action",
			data:{"drugIds":drugIds,"invoiceNo":invoiceNo,"payType":payType,"applyCost":applyCost,"medicalRecord":medicalRecord},
			type:'post',
			success: function(dataMap) {
				$.messager.progress('close');
				if(dataMap.resMsg=="success"){
					if(dataMap.resCode=="success"){
						$.messager.confirm('操作提示', '是否打印发票', function(r){
							if (r){
								var INVOICE_NO=dataMap.invoiceNos;//发票号
								var timerStr = Math.random();
								invoiceArr=INVOICE_NO.split(",");
								for(var i=0;i<invoiceArr.length;i++){
									window.open ("<%=basePath%>iReport/iReportPrint/iReportInvoiceMedicinelist.action?randomId="+timerStr+"&INID="+invoiceArr[i]+"&fileName=Invoice",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no');
								}
								window.location.href="<c:url value='/finance/refundConfirm/toView.action'/>?menuAlias=${menuAlias}";
							}else{
								window.location.href="<c:url value='/finance/refundConfirm/toView.action'/>?menuAlias=${menuAlias}";
							}
						});
					}else{
						$.messager.alert('操作提示', '退费成功','info', function(){
							window.location.href="<c:url value='/finance/refundConfirm/toView.action'/>?menuAlias=${menuAlias}";
						});
					}
				}else if(dataMap.resMsg=="error"){
					$.messager.alert("操作提示", dataMap.resCode);
				}
			}
		});
		
	}else{
		$.messager.alert("操作提示","对不起，没有退费申请，不能进行确认操作");
	}
}
</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north',border:false" style="height:120px;">
    	<div style="padding:5px 5px 5px 5px;">
	    	<a id="btnQuery" class="easyui-linkbutton readCard" data-options="iconCls:'icon-bullet_feed',disabled:false" type_id="refund_card_no" cardNo="">读卡</a>&nbsp;&nbsp;
			<input type="hidden" id="refund_card_no">
			<a id="btnSave" href="javascript:refundSave();" class="easyui-linkbutton" data-options="iconCls:'icon-backfee2',disabled:false">退费确认</a>&nbsp;&nbsp;
			<a id="btnClear" href="javascript:clearFeed();" class="easyui-linkbutton" data-options="iconCls:'icon-clear',disabled:false">清屏</a>&nbsp;&nbsp;
   			<input id="payType" type="hidden">
   			<input id="invoice" type="hidden">
   		</div>
   		<div>
			<fieldset class="changeskin">
		   		<legend><font style="font-weight: bold;font-size: 12px;">患者信息</font></legend>	
		   		<form id="infoForm">
			    	<table border="0" style="float:left">
						<tr>
							<td nowrap="nowrap" align="right">发票号：</td>
							<td nowrap="nowrap"><input class="easyui-textbox" id="invoiceNo" style="width:120px" />&nbsp;&nbsp;</td>
							<td nowrap="nowrap" align="right">病历号：</td>
							<td nowrap="nowrap"><input class="easyui-textbox" id="medicalRecord" style="width:120px" readonly="readonly"/>&nbsp;&nbsp;</td><%--prompt:'请输入病历号' --%>
							<td nowrap="nowrap" align="right">姓名：</td>
							<td nowrap="nowrap"><input class="easyui-textbox" id="patientName"  style="width:120px" readonly="readonly"/>&nbsp;&nbsp;</td>
							<td nowrap="nowrap" align="right">申请金额：</td>
							<td nowrap="nowrap"><input class="easyui-numberbox" id="applyCost"  style="width:120px" readonly="readonly" data-options="min:0,precision:2"/>&nbsp;&nbsp;</td>
						</tr>
					</table>
				</form>
			</fieldset>
		</div>
    </div>   
    <div data-options="region:'center',border:false">
    	<table id="list" class="easyui-datagrid" title="退费申请列表" data-options="fit:true,checkOnSelect:false,selectOnCheck:false,singleSelect:true">   
		    <thead>   
		        <tr>   
		            <th data-options="field:'itemName',width:'15%'">项目名称</th>   
		            <th data-options="field:'specs',width:'15%'">规格</th>
		            <th data-options="field:'priceUnit',formatter:functionPack,width:'10%'">单位</th>   
		            <th data-options="field:'quantity',width:'10%'">申请数量</th>
		            <th data-options="field:'totCost',align:'right',formatter:functionSum,width:'10%'">金额</th>   
		            <th data-options="field:'days',width:'10%'">每次量和付数</th>
		            <th data-options="field:'salePrice',align:'right',width:'10%'">单价</th> 
		            <th data-options="field:'drugFlag',hidden:true">药品/非药品</th>     
		            <th data-options="field:'recipeNo',hidden:true">处方号</th>  
		            <th data-options="field:'sequenceNo',hidden:true">处方内流水号</th> 
		            <th data-options="field:'execDpcd',hidden:true">执行科室</th> 
		            <th data-options="field:'itemCode',hidden:true">项目ID</th>
		            <th data-options="field:'extFlag',hidden:true">包装/最小</th> 
		        </tr>   
		    </thead>   
		</table>
    </div>   
</div>  
</body>
</html>