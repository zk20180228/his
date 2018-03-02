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
<title></title>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/datagrid-detailview.js"></script>
<script type="text/javascript">
$(function(){
	$('#edv').datagrid({
		view:detailview,
        fit:true,
        rownumbers:true,
        pagination:false,
        striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,
        url: "<%=basePath%>finance/invoiceReprint/getInvoiceBycode.action",
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="edvc-' + index + '"></table></div>';  
        },  
        onExpandRow:function(index,row){
            $('#edvc-'+index).datagrid({  
            	url: "<%=basePath%>finance/invoiceReprint/getByinvoiceNo.action",
            	queryParams:{invoiceNo:row.invoiceNo},
                rownumbers:true,
	            striped:true,
	            border:true,
	            checkOnSelect:true,
	            selectOnCheck:false,
	            singleSelect:true,
                height:'auto',  
                columns:[[  
                    {field:'cardNo',title:'就诊卡号',width:'10%',halign:'center',align:'right'},  
                    {field:'regDate',title:'挂号日期',width:'15%',halign:'center',align:'right'},  
                    {field:'itemName',title:'项目名称',width:'20%',halign:'center',align:'right'},  
                    {field:'feeDate',title:'收费日期',width:'15%',halign:'center',align:'right'},  
                    {field:'qty',title:'数量',width:'10%',halign:'center',align:'right'},  
                    {field:'unitPrice',title:'单价',width:'10%',halign:'center',align:'right'},  
                    {field:'totCost',title:'金额',width:'10%',halign:'center',align:'right'},  
                ]],  
                onResize:function(){  
                    $('#edv').datagrid('fixDetailRowHeight',index);  
                },  
                onLoadSuccess:function(){  
                    setTimeout(function(){  
                        $('#edv').datagrid('fixDetailRowHeight',index);  
                    },0);  
                }  
            }); 
            $('#edv').datagrid('fixDetailRowHeight',index);  
        }  
    });  
	bindEnterEvent('invoiceNo',searchList,'easyui');
	bindEnterEvent('clinicCode',searchList,'easyui');
});
function searchList(){
	var invoiceNo = $('#invoiceNo').textbox('getText');
	var clinicCode = $('#clinicCode').textbox('getText');
	$('#edv').datagrid('load',{invoiceNo : invoiceNo,clinicCode : clinicCode});
}
function openDg(){
	var rows = $('#edv').datagrid('getRows')
	if(rows!=null&&rows.length>0){
		for(var i=0;i<rows.length;i++){
			$('#edv').datagrid('expandRow',i);
		}
	}
}
function foldDg(){
	var rows = $('#edv').datagrid('getRows')
	if(rows!=null&&rows.length>0){
		for(var i=0;i<rows.length;i++){
			$('#edv').datagrid('collapseRow',i);
		}
	}
}
/***
 * 因为业务逻辑存在问题，这里补打和重打使用相同的方法；
 * 该方法是重打和补打都是更改数据库里的发票号，但是对于补打而言，之前的发票号没有使用，会造成发票的浪费
 */
function reprint(){
	var row = $('#edv').datagrid('getChecked');
	var invoiceNo = "";
	if(row!=null&&row.length>0){
		for(var i=0;i<row.length;i++){
			if(invoiceNo!=""){
				invoiceNo += ",";
			}
			invoiceNo += row[i].invoiceNo;
		}
	}else{
		$.messager.alert('提示','请选择发票号!');
		return;
	}
	$.messager.progress({text:'执行中，请稍后...',modal:true});
	$.ajax({
		url:'<%=basePath%>finance/invoiceReprint/reprint.action',
		data:{"invoiceNo":invoiceNo},
		success:function(data){
			$.messager.progress('close');
			if(data.resCode=="success"){
				var timerStr = Math.random();
				var INVOICE_NO = data.newInvoiceNo;
				var invoiceArr = new Array();
				invoiceArr=INVOICE_NO.split(",");
				for(var i=0;i<invoiceArr.length;i++){
					window.open ("<%=basePath%>iReport/iReportPrint/iReportInvoiceMedicinelist.action?randomId="+timerStr+"&INID="+invoiceArr[i]+"&fileName=Invoice",'newwindow'+i,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no');		
				}
			}else{
				$.messager.progress('close');
				$.messager.alert('提示','操作失败!');
				return;
			}
		},
		error:function(){
			$.messager.progress('close');
			$.messager.alert('提示','请求发送失败!请检查网络是否流畅...');
			return;
		}
	});
}
function delData(){
	$('#invoiceNo').textbox('setText','');
	$('#clinicCode').textbox('setText','');
}
</script>
</head>
<body>
	<div id="cc" class="easyui-layout" style="width:100%;height:100%;">   
	    <div data-options="region:'north',split:true" style="height:43px;padding:5px 5px 5px 1px;border-top:0;">
	    	发票号:<input id="invoiceNo" class="easyui-textbox" style="width:200px" />
	    	门诊号:<input id="clinicCode" class="easyui-textbox" style="width:200px" />
	    	<a href="javascript:delData();"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
	    	<a href="javascript:searchList()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-fold'" onclick="foldDg()">合并</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-open'" onclick="openDg()">展开</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-printer'" onclick="reprint()">重打</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-printer'" onclick="reprint()">补打</a>
	    </div>   
	    <div data-options="region:'center',border:false" >
	    	<table id="edv" class="easyui-datagrid" data-options="rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fit:true">
				    <thead>   
				        <tr>   
				        	<th data-options="field:'ck',checkbox:true" ></th>
				            <th data-options="field:'invoiceNo',width:'20%',align:'center'">发票号</th>   
				            <th data-options="field:'feeCode',width:'20%',align:'center'">收费员姓名</th>   
				            <th data-options="field:'deptName',width:'20%',align:'center'">开方医生所在科室</th>   
				        </tr>   
				    </thead> 
			</table>
	    </div>   
	</div>  
</body>
</html>