<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>门诊收费清单</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
$(function(){
	bindEnterEvent('invoiceNo',findInvoiceNo,'easyui');//绑定回车事件
});

function findInvoiceNo(){
	//显示数据前先清空	
	$('#sumMonay').text("");//总金额
	$('#ownCost').text("");//自费
	$('#payCost').text("");//自付
	$('#name').text("");//姓名
	$('#age').text("");//年龄
	$('#no').text("");//门诊号
	$('#dept').text("");//就诊科室
	$('#dates').text("");//挂号时间
	$('#sumtotla').text("");//合计
	$('#emp').text("");//医生
	$('#user').text("");//收费员
	 
	var invoiceNo = $('#invoiceNo').textbox('getValue');
	if(invoiceNo==null||invoiceNo==""){
		$.messager.alert("操作提示","请先输入发票号");
		return;
	}
	$.messager.progress({text:'查询中，请稍后...',modal:true});
	$.ajax({
		url: "<%=basePath%>statistics/outpatientInvoice/findInvoiceNo.action", 
		data:{invoiceNo:invoiceNo},
		success: function(data) {
			$.messager.progress('close');
			if(data.resMsg=="error"){
				$.messager.alert("操作提示",data.resCode);
			}else{
				$('#list').datagrid({
					url: "<%=basePath%>statistics/outpatientInvoice/findOutpatient.action",
					queryParams:{invoiceNo:invoiceNo}
					
				});
// 				发票号：<input id="invoiceNo" class="easyui-textbox">  
// 				总金额：<input id="sumMonay" class="easyui-textbox">  
// 				自费：<input id="ownCost" class="easyui-textbox">  
// 				自付：<input id="payCost" class="easyui-textbox">  
// 				统筹：<input id="planning" class="easyui-textbox">  
				//alert(JSON.stringify(data.resCode));
				//显示数据前先清空	
// 				$('#sumMonay').text("");//总金额
// 				$('#ownCost').text("");//自费
// 				$('#payCost').text("");//自付
// 				$('#name').text("");//姓名
// 				$('#age').text("");//年龄
// 				$('#no').text("");//门诊号
// 				$('#dept').text("");//就诊科室
// 				$('#dates').text("");//挂号时间
// 				$('#sumtotla').text("");//合计
// 				$('#emp').text("");//医生
// 				$('#user').text("");//收费员
				
				$('#sumMonay').text(data.resCode.sumMoney);//总金额
				$('#ownCost').text(data.resCode.sumMoney);//自费
				$('#payCost').text(data.resCode.sumMoney);//自付
// 				<td class="TDlabel">姓名：</td>
//     			<td class="Input" id="name"></td>
//     			<td class="TDlabel">年龄：</td>
//     			<td class="Input" id="age"></td>
//     			<td class="TDlabel">门诊号：</td>
//     			<td class="Input" id="no"></td>
//     			<td class="TDlabel">就诊科室：</td>
//     			<td class="Input" id="dept"></td>
//     			<td class="TDlabel">挂号时间：</td>
//     			<td class="Input" id="dates"></td>
				$('#name').text(data.resCode.name);//姓名
				if(data.resCode.age!=null&&data.resCode.age!=''){
					var age = data.resCode.age;
					var ages=DateOfBirth(age);
					$('#age').text(ages.get("nianling")+ages.get("ageUnits"));//年龄
				}
				$('#no').text(data.resCode.no);//门诊号
				$('#dept').text(data.resCode.dept);//就诊科室
				$('#dates').text(data.resCode.dates);//挂号时间
				$('#sumtotla').text(data.resCode.sumMoney);//合计
				$('#emp').text(data.resCode.emp);//医生
				$('#user').text(data.resCode.user);//收费员
				//alert(data.resCode.user);
				
			}
		},
		error:function(){
			$.messager.progress('close');
			$.messager.alert('提示','请求失败！');
		}
	});
	
	
	
}

function functionFlag(val,row,index){
	if(val==1){
		return '药品项目';
	}else if(val==0){
		return '非药品项目';
	}else{
		return '';
	}
}

//对数值保留两位小数
function functionDecimal(val,row,index){
	if($.trim(val)!=null&&$.trim(val)!=""){
		return val.toFixed(2);
	}

	
}


function iReportInvoiceNo(){
	var invoiceNo = $('#invoiceNo').textbox('getValue');
	if(invoiceNo==null||invoiceNo==""){
		$.messager.alert("操作提示","请先输入发票号");
		return;
	}
	var rows = $('#list').datagrid('getRows');
	if (rows==null||rows.length==0) {
		$.messager.alert("提示","没有需要打印的信息！");
		return;
	}else{
			var timerStr = Math.random();
			window.open ("<%=basePath%>iReport/iReportPrint/iReportMedicinelist.action?randomId="+timerStr+"&INVOICE_NO="+invoiceNo+"&fileName=MZSF",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no');
	}
}
/**
 * 重置
 * @author huzhenguo
 * @date 2017-03-17
 * @version 1.0
 */
function clears(){
	$("#tab").form('clear');
	$(".Input").text("");
	$("#list").datagrid('loadData',{total:0,rows:[]}); 
}
</script>
<style type="text/css">
	.tableCss{
		border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:100%;
	}
	.tableCss .TDlabel{
		text-align: right;
		font-size:14px;
		width:200px;
	}
	.tableCss td{
		border: 1px solid #95b8e7;
		padding: 5px 5px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.easyuiInput{
		width:200px;
	}
	.Input{
		width:200px;
	}
</style>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north',border:false" style="height:42px;width:100%">
    	<table id="tab" style="width: 100%;padding: 5px 5px 5px 5px;">
			<tr>
				<td>
					发票号：<input id="invoiceNo" class="easyui-textbox">  
					<a href="javascript:void(0)" onclick="findInvoiceNo()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					<a href="javascript:void(0)" onclick="iReportInvoiceNo()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a>
					<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
    </div>
    <div data-options="region:'center'" style="width:100%">
    	<div class="easyui-layout" data-options="fit:true">
    		<div data-options="region:'north',border:false" style=";width:100%">
    			<table style="width:100%;padding: 5px 5px 5px 0px;">
		    		<tr>
		    			<td align="center"><font size="6" class="outpatienttit">郑州大学第一附属医院</font></td>
		    		</tr>
		    		<tr>
		    			<td align="center"><font size="5" class="outpatientsubtit">门诊清单</font></td>
		    		</tr>
		    	</table>
		    	<table  class="tableCss">
		    		<tr>
		    			<td class="TDlabel">姓名：</td>
		    			<td class="Input" id="name"></td>
		    			<td class="TDlabel">年龄：</td>
		    			<td class="Input" id="age"></td>
		    			<td class="TDlabel">门诊号：</td>
		    			<td class="Input" id="no"></td>
		    			<td class="TDlabel">就诊科室：</td>
		    			<td class="Input" id="dept"></td>
		    			<td class="TDlabel">挂号时间：</td>
		    			<td class="Input" id="dates"></td>
		    		</tr>
		    	</table>
    		</div>
    		<div data-options="region:'center',border:false" style="width:100%">
    			<table class="easyui-datagrid"  id="list"  data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">   
				    <thead>   
				        <tr> 
				            <th data-options="field:'drugFlag',formatter:functionFlag,halign:'center',align:'right'" style="width: 15%">分类类别</th>   
				            <th data-options="field:'feeName',halign:'center',align:'right'" style="width: 15%">分类名称</th>
				            <th data-options="field:'itemName',halign:'center',align:'right'" style="width: 15%">项目名称</th>
				            <th data-options="field:'unitPrice',formatter:functionDecimal,align:'right',halign:'center'" style="width: 15%">单价</th>
				            <th data-options="field:'qty',align:'right',halign:'center'" style="width: 15%">数量</th>
				            <th data-options="field:'money',formatter:functionDecimal,align:'right',halign:'center'," style="width: 15%">金额</th>
				        </tr>   
				    </thead>   
				</table>
    		</div>
    		<div data-options="region:'south'" style="height:40px;width:100%" align="center">
    			<table style="width:60%" >
		    		<tr>
		    			<td nowrap="nowrap" class="TDlabel">总金额：</td>
		    			<td nowrap="nowrap" class="Input" id="sumMonay"></td>
		    			<td nowrap="nowrap" class="TDlabel">自费：</td>
		    			<td nowrap="nowrap" class="Input" id="ownCost"></td>
		    			<td nowrap="nowrap" class="TDlabel">自付：</td>
		    			<td nowrap="nowrap" class="Input" id="payCost"></td>
		    			<td nowrap="nowrap" class="TDlabel">统筹：</td>
		    			<td nowrap="nowrap" class="Input" id="planning"></td>
<!-- 		    			总金额：<input id="sumMonay" class="easyui-textbox">   -->
<!-- 						自费：<input id="ownCost" class="easyui-textbox">   -->
<!-- 						自付：<input id="payCost" class="easyui-textbox">   -->
<!-- 						统筹：<input id="planning" class="easyui-textbox"> -->
		    			<td nowrap="nowrap" class="TDlabel">合计：</td>
		    			<td nowrap="nowrap" class="Input" id="sumtotla"></td>
		    			<td nowrap="nowrap" class="TDlabel">医生：</td>
		    			<td nowrap="nowrap" class="Input" id="emp"></td>
		    			<td nowrap="nowrap" class="TDlabel">收费员：</td>
		    			<td nowrap="nowrap" class="Input" id="user"></td>
		    		</tr>
		    	</table>
    		</div>   
    	</div>
    </div>   
</div>  
</body>
</html>