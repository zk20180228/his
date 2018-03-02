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
	<title>经营日报初始化</title>
</head>
<script type="text/javascript">
$(function(){
// 	$.extend($.fn.datagrid.defaults.editors, {
// 		numberspinner: {
// 			init: function(container, options){
				
// 				var input = $("<a  href='#' class='easyui-linkbutton' data-options=\"iconCls:'icon-search'\"></a> ").appendTo(container);
// 				console.info(container);
// // 				console.info(options);
// 				return input.numberspinner(options);
// 			},
// 			destroy: function(target){
// 				$(target).numberspinner('destroy');
// 			},
// 			getValue: function(target){
// 				return $(target).numberspinner('getValue');
// 			},
// 			setValue: function(target, value){
// 				$(target).numberspinner('setValue',value);
// 			},
// 			resize: function(target, width){
// 				$(target).numberspinner('resize',width);
// 			}
// 		}
// 	});
	dateFrom(null,null);
});
//查询
function searchFrom(){
	var startTime=$('#begin').val();
	var endTime=$('#end').val();
	if(noSpace('begin','end')){
		dateFrom(startTime,endTime);
	}
}
function dateFrom(time1,time2){
	$('#list').datagrid({
		rownumbers: true,
		pageSize: "20",
		fit:true,
		singleSelect:true,
		checkOnSelect:true,
		selectOnCheck:true,
		pageList: [10, 20, 30, 50, 80, 100],
		pagination: true,
		method: "post",
		url: '<%=basePath%>statistics/hospitalday/hospitalMoreDay.action',
		toolbar:'#toolbarId',
		queryParams:{startTime:time1,endTime:time2}
// 		,
// 		onDblClickCell: function(index,field,value){
// 			$(this).datagrid('beginEdit', index);
// 			var ed = $(this).datagrid('getEditor', {index:index,field:field});
// 		}
// 		,
	    ,onLoadSuccess:function(data){    
           $(this).datagrid('uncheckAll');
           $(this).datagrid('unselectAll');
   		},  

	});
}
//清空
function clear(){
	$('#begin').val('');
	$('#end').val('');
	dateFrom(null,null);
}
//空判断
function noSpace(time1,time2){
	var timeOne=$('#'+time1).val();
	var timeTwo=$('#'+time2).val();
	if((timeOne==null||timeOne=='')&&(timeTwo==null||timeTwo=='')){
		$.messager.alert('提示','时间不能为空');
		return false;
	}
	return true;
}
//预处理
function initFunc(){
	var begain = $('#pretreatFirstDate').val();
	var end = $('#pretreatEndDate').val();
	if(begain==null||begain==""){
		$.messager.alert('提示','请选择开始时间...');
		return;
	}
	if(end==null||end==""){
		$.messager.alert('提示','请选择结束时间...');
		return ;
	}
	$.messager.confirm('请确认开始时间结束时间',"您选择的时间是："+begain+"至"+end,function(r){
		if(r){
			$.messager.progress({text:'处理中，请稍后...',modal:true});
			$.ajax({
				url:'<%=basePath%>inner/hospitalday/saveBusiness.action',
				data : {startTime:begain,endTime:end},
				success : function(result){
					$.messager.progress('close');
					$.messager.alert('提示',result.resMsg,result.resCode);
					$('#pretreatwindow').window('close');
					dateFrom(null,null);
				}
				,error : function(){
					$.messager.progress('close');
					$.messager.alert('提示','网络繁忙,请稍后重试...','info');
				}
			});
		}
	});
	
	
}
function del(){
	var row=$("#list").datagrid("getSelected");
	console.info(row);
	if(row!=null&&row!=''){
		$.messager.confirm("提示","您删除的记录是："+row.timeValue,function(r){
			if(r){
				$.messager.progress({text:'处理中，请稍后...',modal:true});
				$.ajax({
					url:'<%=basePath%>inner/hospitalday/delHospital.action',
					data : {startTime:row.timeValue},
					success : function(result){
						$.messager.progress('close');
						$.messager.alert('提示',result.resMsg);
						dateFrom(null,null);
					}
					,error : function(){
						$.messager.progress('close');
						$.messager.alert('提示','网络繁忙,请稍后重试...');
					}
				});
			}
		});
	}else{
		$.messager.alert('提示','请选择删除数据','info');
	}
	
}
function update(){
	var row=$('#list').datagrid('getSelected');//获取选中行
	if(row!=null){
		var index=$('#list').datagrid('getRowIndex', row);//获取索引
		$('#list').datagrid('beginEdit', index);
//	 	var ed = $(this).datagrid('getEditor', {index:index,field:field});
		$('#list').datagrid('checkRow',index);
	}else{
		$.messager.alert('提示','请点击修改行','info');
	}
	
}
//更新字段
function updateRow(){
// 	if(index=='0'||index!=null&&index!=''){
		var rows=$('#list').datagrid('getChecked');
		var index=null;
		var show=null;
		if(rows!=null&&rows!=''&&rows.length>0){
			for(var i=0;i<rows.length;i++){
				index=$('#list').datagrid('getRowIndex', rows[i]);//获取索引
				if(show==null){
					show=rows[i].timeValue+'号,';
				}else{
					show+=rows[i].timeValue+'号,'
				}
				$('#list').datagrid('endEdit',index);
			}
			show=show.substr(0,show.length-1);
			$.messager.confirm("提示","确认修改:"+show+"的数据？",function(r){
				if(r){
					$.ajax({
						url:'<%=basePath%>inner/hospitalday/updateHospital.action',
						data : {upDateRow:JSON.stringify(rows)},
						success : function(result){
							$.messager.progress('close');
							$('#list').datagrid('uncheckAll');
							$.messager.alert('提示',result.resMsg,result.resCode);
							
						}
						,error : function(){
							$.messager.progress('close');
							$.messager.alert('提示','网络繁忙,请稍后重试...','info');
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','没有修改数据','info');
		}
		
// 		$("a[name='opera']").linkbutton({iconCls:'icon-save'});
// 		$('#list').datagrid('selectRow',index);
		
// 		var row=$('#list').datagrid('getSelected');
		
// 		$('#list').datagrid('endEdit',index);
		
// 	}
}
//渲染字段
function formaterButton(value,row,index){
	if(value!=''&&value!=null){
		return "<a href='javascript:updateRow("+index+");' name=\"opera\" class=\"easyui-linkbutton\" style=\"height:20px;\" >"+value+"</a>";
	}
}
</script>
<body>
	<div id="layoutId" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'north',split:false,border:false" style="height:40px;">
	    	<form id="searchForm" style="padding-top:7px;padding-left:5px;padding-right:5px;">
				<table border="0">
					<tr align="left">
						<td style="width: 400px;" >
							开始时间:
							<input id="begin" class="Wdate" type="text" value="${startTime}" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'end\')}'&&'%y-%M-{%d-1}'})"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;height: 24px;"/>
							结束时间:
							<input id="end" class="Wdate" type="text" value="${endTime}" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-{%d-1}',minDate:'#F{$dp.$D(\'begin\')}'})"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;height: 24px;"/>
						</td>
						<td align="right">
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:searchFrom();void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left: 3px">查询</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:clear();void(0)"  class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:edit">
								<a href="javascript:updateRow();void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="margin-left: 3px">提交</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</form>
	    </div>
	    <div data-options="region:'center'" >
	    	<table id="list" data-options="pagination:true,fit:true" >
				<thead>
					 <tr >
					 	<th data-options="field:'ck',checkbox:true,rowspan:2"></th>
						<th data-options="field:'timeValue',rowspan:2,width:100">统计日期</th>
						<th data-options="field:'oldNum',colspan:4" align="center">门诊人次合计</th>
						<th data-options="field:'inNum',colspan:4" align="center" >在院人次合计</th>
						<th data-options="field:'exInNum',colspan:4" align="center">手术人次合计</th>
						<th data-options="field:'exOutNum',colspan:4" align="center" >总收入</th>
						<th data-options="field:'outNum',colspan:4" align="center">出院人次合计</th>
						<th data-options="field:'nowNum',colspan:4" align="center">入院人次合计</th>
						<th data-options="field:'realBedNum',colspan:4" align="center">院区药品收入</th>
					</tr> 
					<tr sytle="height:100px;">
						<th data-options="field:'outpatientNumHj',width:100,editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[8]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[8]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"  align="center">惠济院区</th>
						<th data-options="field:'outpatientNumZd',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[8]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[8]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">郑东院区</th>
						<th data-options="field:'outpatientNumHy',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[8]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[8]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">河医院区</th>
						<th data-options="field:'outpatientNum',width:100"   align="center">合计</th>
						
						
						
						
						<!--  -->
						<th data-options="field:'inpatientNumHj',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[15]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[15]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"  align="center">惠济院区</th>
						<th data-options="field:'inpatientNumZd',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[15]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[15]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">郑东院区</th>
						<th data-options="field:'inpatientNumHy',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[15]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[15]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">河医院区</th>
						<th data-options="field:'inpatientNum',width:100"   align="center">合计</th>
						
						
						
						
						
						<th data-options="field:'operationNumHj',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[22]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[22]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"  align="center">惠济院区</th>
						<th data-options="field:'operationNumZd',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[22]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[22]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">郑东院区</th>
						<th data-options="field:'operationNumHy',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[22]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[22]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">河医院区</th>
						
						
						
						
						<th data-options="field:'operationNum',width:100"  align="center">合计</th>
						
						
						<th data-options="field:'incomeCostHj',width:100,editor:{type:'numberbox',options:{
						precision:2,
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[29]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseFloat(num)-parseFloat(oldValue)+parseFloat(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[29]).find('div').html(sum.toFixed(2));
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"  align="center">惠济院区</th>
						<th data-options="field:'incomeCostZd',width:100,editor:{type:'numberbox',options:{
						precision:2,
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[29]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseFloat(num)-parseFloat(oldValue)+parseFloat(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[29]).find('div').html(sum.toFixed(2));
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">郑东院区</th>
						<th data-options="field:'incomeCostHy',width:100,editor:{type:'numberbox',options:{
						precision:2,
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[29]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseFloat(num)-parseFloat(oldValue)+parseFloat(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[29]).find('div').html(sum.toFixed(2));
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">河医院区</th>
						
						<th data-options="field:'incomeCost',width:100"   align="center">合计</th>
						
						<th data-options="field:'leaveHospitalNumHj',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[36]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[36]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"  align="center">惠济院区</th>
						<th data-options="field:'leaveHospitalNumZd',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[36]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[36]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">郑东院区</th>
						<th data-options="field:'leaveHospitalNumHy',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[36]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[36]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">河医院区</th>
						
						
						
						<th data-options="field:'leaveHospitalNum',width:100"   align="center">合计</th>
						
						<th data-options="field:'inHospitalNumHj',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[43]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[43]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"  align="center">惠济院区</th>
						<th data-options="field:'inHospitalNumZd',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[43]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[43]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">郑东院区</th>
						<th data-options="field:'inHospitalNumHy',width:100,editor:{type:'numberbox'},editor:{type:'numberbox',options:{
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[43]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseInt(num)-parseInt(oldValue)+parseInt(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[43]).find('div').html(sum);
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">河医院区</th>
						<th data-options="field:'inHospitalNum',width:100"   align="center">合计</th>
						
						
						
						
						
						<th data-options="field:'drugProportionHj',width:100,editor:{type:'numberbox',options:{
						precision:2,
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[50]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseFloat(num)-parseFloat(oldValue)+parseFloat(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[50]).find('div').html(sum.toFixed(2));
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"  align="center">惠济院区</th>
						<th data-options="field:'drugProportionZd',width:100,editor:{type:'numberbox',options:
						{precision:2,
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[50]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseFloat(num)-parseFloat(oldValue)+parseFloat(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[50]).find('div').html(sum.toFixed(2));
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">郑东院区</th>
						<th data-options="field:'drugProportionHy',width:100,editor:{type:'numberbox',options:{
						precision:2,
						onChange:function(newValue,oldValue){
							var num=$($(this).parent('td').parent('tr').parents('tr').find('td')[50]).find('div').html();
							if(num==''&&num==null){
								num=0;
							}
							if(isNaN(newValue)||(newValue==''||newValue==null)){
								newValue=0;
							}
							if(oldValue!=''&&oldValue!=null){
								var sum=parseFloat(num)-parseFloat(oldValue)+parseFloat(newValue);
								$($(this).parent('td').parent('tr').parents('tr').find('td')[50]).find('div').html(sum.toFixed(2));
							}
							$(this).numberbox('setValue',newValue);
								}
							}
						}"   align="center">河医院区</th>
						<th data-options="field:'drugProportion',width:100"   align="center">合计</th>
					
					</tr>
				</thead>
			</table>
	    </div>
	    <!-- 表格菜单 -->
	    <div id="toolbarId">
	    	<shiro:hasPermission name="${menuAlias}:function:initialization">
				<a href="javascript:$('#pretreatwindow').dialog('open');void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cdr_edit',plain:true">初始化</a>
			</shiro:hasPermission>
	   		<shiro:hasPermission name="${menuAlias}:function:edit">
				<a href="javascript:update();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
			</shiro:hasPermission>
	   		<shiro:hasPermission name="${menuAlias}:function:delete">
				<a href="javascript:del();" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
			
	    </div>  
	    <!--弹窗  -->
	    <div id="pretreatwindow" class="easyui-dialog" title="初始化操作" data-options="modal:true,closed:true" style="width:40%;height:55%;overflow: visible;">
			<form>
				<table class="honry-table" id="DatapreTable" cellpadding="0" cellspacing="0" border="0"  style="width:100%;padding:5px">
					<tr>
						<td class="honry-lable">开始时间:</td>
						<td>
							<input id="pretreatFirstDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'pretreatEndDate\')}'&&'%y-%M-{%d-1}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" /> 
						</td>
					</tr>
					<tr>
						<td class="honry-lable">结束时间:</td>
						<td>
							<input id="pretreatEndDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'pretreatFirstDate\')}',maxDate:'%y-%M-{%d-1}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" />
						</td>
					</tr>
				</table>
			</form>
				<div id="tool3" style="text-align: center;padding: 10px 0;">
					<a href="javascript:initFunc()" class="easyui-linkbutton" iconCls="icon-save">确定</a>
					<a href="javascript:$('#pretreatwindow').window('close');" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
				</div>
			<div id="pretreatwindowInfo"  style="margin-top: -20px;height: 100%"></div>
		</div>
	    
	</div> 
	
</body>
</html>