<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
.datagrid-wrap{
	border-left:0;
	border-bottom:0
}
</style>
</head>
<body>
<div id="cc" class="easyui-layout" style="width:100%;height:100%;">   
    <div data-options="region:'north',split:true" style="height:50px;padding-top: 5px;padding-left: 15px;border-top:0">
    <a class="easyui-linkbutton" onclick="queryInfo()" data-options="iconCls:'icon-search'">查询</a>
 	<a class="easyui-linkbutton" onclick="savefunction()" data-options="iconCls:'icon-disk'">保存</a> 
 	<a class="easyui-linkbutton" onclick="SaveExcel()"  data-options="iconCls:'icon-page_white_excel'">导出</a> 
    </div>   
   <!--   <div data-options="region:'west',split:true" style="width:10%;padding:2 18">
      <div><ul id="deptTree"></ul> </div>
     </div>   --> 
    <div data-options="region:'center',split:true" >
    <div style="padding:15px;">
            结存单位：<input id="queryCompanyCode" class="easyui-combobox" data-options="url:'<%=basePath%>material/orderCompany/companyList.action',valueField:'id',textField:'companyName'" />  
		    　　<input id="r1" type="radio" name="radiobutton" value="2">结存
		   　　<input id="r2" type="radio" name="radiobutton" value="01"  checked>未结存
    　　起始日期：<input id="date1" class="Wdate" type="text" onClick="WdatePicker()" />  
    　　结束日期：<input id="date2" class="Wdate" type="text" onClick="WdatePicker()" />  
    </div>
    <table id="err" class="easyui-edatagrid" style="width:100%;height:45%" data-options="url:'<%=basePath%>material/supplierInfo/querySupplier.action'">   
    <thead>   
        <tr>   
            <th field='payFlag'width='5%'align='center'formatter='funcheckBox'>付款</th>   
            <th field='invoiceNo'width='6%'align='center'>发票号</th>   
            <th field='invoiceDate'width='6%'align='center' >发票日期</th> 
            <th field='purchaseCose'width='6%'align='center'>发票金额</th>   
            <th field='disCount'width='6%'align='center'  editor="{type:'numberbox',options:{precision:2}}">优惠金额</th>   
            <th field='a'width='6%'align='center'>应付金额</th> 
            <th field='payCost'width='6%'align='center'>已付金额</th>   
            <th field='patCost1' width='6%' align='center' editor="{type:'numberbox',options:{precision:2}}">本次金额</th>   
            <th field='carriageCost'width='6%'align='center' editor="{type:'numberbox',options:{precision:2}}">运费</th> 
            <th field='payType'width='6%'align='center' editor="{type:'combobox',options:{
															valueField:'id',
															textField:'name',
															data:[{ 'id':'现金',    
															 'name':'现金'   },
															 {  'id':'发票',  
															  'name':'发票'}],
															   onSelect: function(rec){    
														         if(rec.name=='发票'){
														
														         } 
														       }
																		  }}">付款类型</th>   
            <th field='openBank'width='6%'align='center' editor="{type:'textbox',options:{}}">开户银行</th>   
            <th field='openCount'width='6%'align='center' editor="{type:'textbox',options:{}}">银行账户</th> 
            <th field='storageCode'width='6%'align='center'>入库科室</th>   
            <th field='inListCode'width='6%'align='center'>入库单据号</th>   
            <th field='paycreadence'width='6%'align='center' editor="{type:'textbox',options:{}}">付款凭证</th> 
            <th field='unpayCreadence'width='6%'align='center' editor="{type:'textbox',options:{}}">未付款凭证</th>   
            <th field='creadenceDate'width='6%'align='center' editor="{type:'datebox',options:{}}">未付款凭证日期</th>    
        </tr>   
    </thead>   
</table> 
 <table id="asd" class="easyui-datagrid" style="width:100%;height:38%" data-options="">    
    <thead>  
        <tr>   
            <th field='invoiceNo'width='8.5%'align='center'>发票号</th>   
            <th field='payCost'width='8.5%'align='center'>本次付款</th> 
            <th field='carriageCost'width='8.5%'align='center'>运费</th> 
            <th field='payType'width='8.5%'align='center'>付款类型</th> 
            <th field='openBank'width='8.5%'align='center'>开户银行</th> 
            <th field='openCount'width='8%'align='center'>银行账户</th> 
            <th field='payOperCode'width='8%'align='center'>付款人</th> 
            <th field='payCredence'width='8%'align='center'>付款凭证</th> 
            <th field='unPayCredence'width='8%'align='center'>未付款凭证</th> 
            <th field='credenceDate'width='9%'align='center'>未付款凭证日期</th> 
            <th field='payDate'width='9%'align='center'>付款日期</th> 
            <th field='payDetailNo'width='8%'align='center'>流水单号</th>    
        </tr>   
    </thead>   
</table>  
    </div>  
    <div id="win" class="easyui-window" title="请选择库房" style="width:200;height:100;text-align: center;padding:4px" data-options="collapsible:false,minimizable:false,maximizable:false,closable:false,iconCls:'icon-save',modal:true">  
     库房：<input id="cc001"  /> 
</div> 
<input id="hideText" class="easyui-textbox" hidden > 
<input id="hideText002" class="easyui-textbox" hidden > 
</div> 
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
$(function(){
<%-- 	//患者树 
	$('#deptTree').tree({    
		url:'<%=basePath%>material/supplierInfo/InfoTree.action',
		lines:true,
		onBeforeCollapse:function(node){ 
		  	if(node.id=="1"){
				return false;
		  	}
		},
		onClick:function(node){
			var no=node.id;
			if(node.text=='仓库列表'){
			}
			else{
			     $('#deptCom').combobox('reload', '<%=basePath%>material/supplierInfo/companyCombobox.action?deptId='+no);    
			     $('#err').datagrid('reload',{
			    	 comPid:no
			     });
			     $('#asd').datagrid('reload');
			}
		}
	}); --%>
	//选择库房 
	$("#cc001").combobox({
		<%--url:'<%=basePath%>material/supplierInfo/deptCombobox.action',--%>
		url : "<c:url value='/material/orderDepartment/getStorage.action'/>",
 	    valueField:'id',
		 textField:'deptName',
		 onSelect: function(rec){    
			     var url = '<%=basePath%>material/supplierInfo/companyCombobox.action?deptId='+rec.id;    
			     $('#deptCom').combobox('reload', url);    
			     $('#err').datagrid('reload',{
			    	 comPid:rec.id
			     });
			     $("#hideText").textbox('setValue',rec.id);
			     $('#asd').datagrid('reload');
				 $('#win').window('close');
			} 
	});//结存单位 
	$('#queryCompanyCode').combobox({//供货公司
		url : "<c:url value='/material/orderCompany/companyList.action'/>",
		valueField : 'companyCode',
		textField : 'companyName',
		filter : function(q,row){
			var keys = new Array();
			keys[keys.length] = "companyCode";
			keys[keys.length] = "companyName";
			return filterLocalCombobox(q, row, keys);
		}
	});
// 	//结存单位 
// 	$("#queryCompanyCode").combobox({
// 		url : "<c:url value='/material/orderCompany/companyList.action'/>",
// 		valueField:'id',
// 		textField:'companyName',
// 		mode:'remote',
// 		onSelect: function(rec){    
// 		     $('#err').datagrid('reload',{
// 		    	ggId:rec.id
// 		     });
// 		 }
// 	});
	
$("#err").datagrid({
		onDblClickRow:function(rowIndex, rowData){
			$("#asd").datagrid({
				url:'<%=basePath%>material/supplierInfo/queryDetail.action',
				queryParams: {
					invoiceNo: rowData.invoiceNo,
					payheadNo: rowData.payHeadNo
				}
			});
	  }}
	)
});
//导出easyui-datagrid到excel
function SaveExcel() {
	 var rowss=$('#err').datagrid('getRows');
	 var data=JSON.stringify(rowss);
	 $.ajax({
		 type:'post',
		 url:'<%=basePath%>material/supplierInfo/exportToExcel.action',
		 data:{
			data:data 
		 }
	 });
	 }

//复选框
function funcheckBox(value,row,index){
		if(value=="1"||value=="2"){
			 return "<input type='checkbox' checked='checked'>";
		}else{
			 return '<input type="checkbox" >';
		}
	
	 }
 //保存方法
function  savefunction(){
	var rows = $('#err').datagrid('getRows');
	 for(var i=0;i<rows.length;i++){
		$('#err').datagrid('endEdit',i); 
	 }
	var rowss=$('#err').datagrid('getRows');
	var data=JSON.stringify(rowss);
	$.ajax({
		url:"<%=basePath%>material/supplierInfo/saveDatagrid.action",
		data:{data:data},
		type:'post',
		success:function(){
			$('#err').datagrid('reload');
		}
	});
}

 //查询
 function queryInfo(){
	var c= $('input:radio[name="radiobutton"]:checked').val();
	var date1=$("#date1").val();
	var date2=$("#date2").val();
	var comPid= $("#hideText").textbox('getValue');
	  $('#err').datagrid('reload',{
		  comPid:comPid,
		  payFlag:c, 
		  beginTime:date1,
		  endTime:date2
	     });
 }
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>