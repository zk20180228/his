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
var userMap = "";//用户渲染

var countMap = "";//渲染合同单位

var empMap = "";//人员渲染

var deptMap = "";//科室渲染

var sysMap = "";//系统类别 

var feeMap = "";//最小费用

var drugMap = "";//药品性质

var doseMap = "";//剂型

var frequencyMap = "";//频次

var itemMap = "";//统计大类

function beginSelect(){
	var beginTime = $('#beginTime').val();
	var endTime = $('#endTime').val();
	var invoiceNo = $('#invoiceNo').textbox('getValue');
	var tab = $('#tabs').tabs('getSelected');
	var index = $('#tabs').tabs('getTabIndex',tab);
	if(beginTime==null || beginTime==""){
		beginTime="${beginTime}";
  	}
  	if(endTime==null || endTime==""){
  		endTime="${endTime}";
  	}
	if(index==0){//选择发票汇总
		findInvoiceNoSummary(beginTime,endTime,invoiceNo);
	}else if(index==1){//选择发票明细
		findInvoiceDetailed(beginTime,endTime,invoiceNo);
	}else{//选择费用明细
		findCostDetailed(beginTime,endTime,invoiceNo);
	}
};

function endSelect(){
    	var beginTime = $('#beginTime').val();
		var endTime = $('#endTime').val();
		var invoiceNo = $('#invoiceNo').textbox('getValue');
		var tab = $('#tabs').tabs('getSelected');
		var index = $('#tabs').tabs('getTabIndex',tab);
		if(beginTime==null || beginTime==""){
			beginTime="${beginTime}";
	  	}
	  	if(endTime==null || endTime==""){
	  		endTime="${endTime}";
	  	}
		if(index==0){//选择发票汇总
			findInvoiceNoSummary(beginTime,endTime,invoiceNo);
		}else if(index==1){//选择发票明细
			findInvoiceDetailed(beginTime,endTime,invoiceNo);
		}else{//选择费用明细
			findCostDetailed(beginTime,endTime,invoiceNo);
		}
};

$(function(){
	bindEnterEvent('invoiceNo',query,'easyui');//绑定回车事件
	searchOne();
	$('#tabs').tabs({    
	    border:false,    
	    onSelect:function(title){
	    	var beginTime = $('#beginTime').val();
    		var endTime = $('#endTime').val();
    		
    		var dd=new Date(endTime); 
    	    dd.setDate(dd.getDate()+1);//获取AddDayCount天后的日期
    	    var y = dd.getFullYear();
    	    var m = dd.getMonth()+1;//获取当前月份的日期
    	    if(Number(m)<10){
    	          m = "0"+m;
    	        }
    	        var d = dd.getDate();
    	        if(Number(d)<10){
    	          d = "0"+d;
    	        }
    	    endTime =  y+"-"+m+"-"+d;
    	        
    		var invoiceNo = $('#invoiceNo').textbox('getValue');
    		if(beginTime==""&&endTime==""&&invoiceNo==""){
    			$.messager.alert("操作提示","请先录入时间或发票号");
    		}else{
    			if(title=="发票汇总"){
    	    		findInvoiceNoSummary(beginTime,endTime,invoiceNo);
    	    	}else if(title=="发票明细"){
    	    		findInvoiceDetailed(beginTime,endTime,invoiceNo);
    	    	}else{
    	    		findCostDetailed(beginTime,endTime,invoiceNo);
    	    	}
    		}
	   	 }    
	});
	
	//查询用户
	$.ajax({
		url: "<%=basePath%>statistics/outpatientCostQuery/userFunction.action",
		type:'post',
		success: function(empData) {
			userMap = empData
		}
	});

	//查询合同单位
	$.ajax({
		url: "<%=basePath%>statistics/outpatientCostQuery/countFunction.action",
		type:'post',
		success: function(countData) {
			countMap = countData
		}
	});
	
	//查询人员
	$.ajax({
		url: "<%=basePath%>statistics/outpatientCostQuery/empFunction.action",
		type:'post',
		success: function(empData) {
			empMap = empData
		}
	});
	
	//查询科室
	$.ajax({
		url: "<%=basePath%>statistics/outpatientCostQuery/deptFunction.action",
		type:'post',
		success: function(deptData) {
			deptMap = deptData
		}
	});
	
	//查询系统类别
	$.ajax({
		url: "<%=basePath%>statistics/outpatientCostQuery/sysFunction.action",
		type:'post',
		success: function(sysData) {
			sysMap = sysData
		}
	});
	
	//查询最小费用
	$.ajax({
		url: "<%=basePath%>statistics/outpatientCostQuery/feeFunction.action",
		type:'post',
		success: function(feeData) {
			feeMap = feeData
		}
	});
	
	//查询药品性质
	$.ajax({
		url: "<%=basePath%>statistics/outpatientCostQuery/drugFunction.action",
		type:'post',
		success: function(drugData) {
			drugMap = drugData
		}
	});
	
	//查询剂型
	$.ajax({
		url: "<%=basePath%>statistics/outpatientCostQuery/doseFunction.action",
		type:'post',
		success: function(doseData) {
			doseMap = doseData
		}
	});
	
	//查询频次
	$.ajax({
		url: "<%=basePath%>statistics/outpatientCostQuery/frequencyFunction.action",
		type:'post',
		success: function(frequencyData) {
			frequencyMap = frequencyData
		}
	});
	
	//查询统计大类
	$.ajax({
		url: "<%=basePath%>statistics/outpatientCostQuery/itemFunction.action",
		type:'post',
		success: function(itemData) {
			itemMap = itemData
		}
	});
	
	
});

function query(){
	var beginTime = $('#beginTime').val();
	var endTime = $('#endTime').val();
// 	if(invoiceNo==null||invoiceNo==""){
// 		$.messager.alert("操作提示","请先输入发票号");
// 		return;
// 	}
	if(beginTime==null || beginTime=="" || endTime==null || endTime==""){
		$.messager.alert("提示","请填写正确的时间范围！");
		return ;
		}
	var dd=new Date(endTime); 
    dd.setDate(dd.getDate()+1);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    if(Number(m)<10){
          m = "0"+m;
        }
        var d = dd.getDate();
        if(Number(d)<10){
          d = "0"+d;
        }
        endTime =  y+"-"+m+"-"+d;
	searchFinal(beginTime,endTime);
}
function searchFinal(Stime,Etime){

	costDetailed = true;
	noSummary = true;
	detailed = true;
	if(Stime&&Etime){
        if(Stime>Etime){
          $.messager.alert("提示","开始时间不能大于结束时间！");
          return ;
        }
      }
	$('#beginTime').val(Stime);
	var invoiceNo = $('#invoiceNo').textbox('getValue');
	if(Stime==null || Stime==""){
		Stime="${beginTime}";
  	}
  	if(Etime==null || Etime==""){
  		Etime="${endTime}";
  	}
	var tab = $('#tabs').tabs('getSelected');
	var index = $('#tabs').tabs('getTabIndex',tab);
	if(index==0){//选择发票汇总
		findInvoiceNoSummary(Stime,Etime,invoiceNo);
	}else if(index==1){//选择发票明细
		findInvoiceDetailed(Stime,Etime,invoiceNo);
	}else{//选择费用明细
		findCostDetailed(Stime,Etime,invoiceNo);
	}
 }
 function GetDateStr(AddDayCount) {
	 var dd = new Date();
	 dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
	 var y = dd.getFullYear();
	 var m = dd.getMonth()+1;//获取当前月份的日期
	 if(Number(m)<10){
         m = "0"+m;
       }
       var d = dd.getDate();
       if(Number(d)<10){
         d = "0"+d;
       }
	 return y+"-"+m+"-"+d;
}
 //查询当天
function searchOne(){
	var Stime = GetDateStr(0);
	var Etime = GetDateStr(1);
	searchFinal(Stime,Etime);
	$('#endTime').val(GetDateStr(0));
 }
 //查询前三天
function searchThree(){
	var Etime = GetDateStr(0);
	var Stime = GetDateStr(-3);
	searchFinal(Stime,Etime);
	$('#endTime').val(GetDateStr(-1));
}
//查询前七天
function searchSeven(){
	var Etime = GetDateStr(0);
	var Stime = GetDateStr(-7);
	searchFinal(Stime,Etime);
	$('#endTime').val(GetDateStr(-1));
}
//查询前15天 zhangkui 2017-04-17
function searchFifteen(){
	var Stime = GetDateStr(-15);
	var Etime = GetDateStr(0);
	searchFinal(Stime,Etime);
	$('#endTime').val(GetDateStr(-1));
}
//上月
function beforeMonth(){
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth();
    var nowMonth = month;
    var nowYear = year;
    if(month==0)
    {
        month=12;
        nowMonth = "01";
        year=year-1;
    }
    if (month < 10) {
        nowMonth = "0" +(month+1);
        month = "0" + month;
    }
    var Stime = year + "-" + month + "-" + "01";//上个月的第一天
    var lastDate = new Date(year, month, 0);
    var lastDay = year + "-" + month + "-" + (lastDate.getDate() < 10 ? "0" + lastDate.getDate() : lastDate.getDate());//上个月的最后一天
    var Etime= nowYear+"-"+nowMonth+"-01";
    searchFinal(Stime,Etime);
    $('#endTime').val(lastDay);
}
//日期格式转换
Date.prototype.Format = function(fmt)   
{   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
} 
//获取每月第一天
function getCurrentMonthFirst(){
	 var date=new Date();
	 date.setDate(1);
	 return date.Format("yyyy-MM-dd");
}
//获取每月最后一天
function getCurrentMonthLast(){
	 var date=new Date();
	 var currentMonth=date.getMonth();
	 var nextMonth=++currentMonth;
	 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
	 var oneDay=1000*60*60*24;
	 return new Date(nextMonthFirstDay-oneDay).Format("yyyy-MM-dd");
} 
//查询当月
function searchMonth(){
	//var Etime = getCurrentMonthLast();
	//需求：统计当月时，统计1号到当前时间 zhangkui 2017-04-17
	//2017-04-17新的
// 	var date=new Date();
// 	var Etime = date.Format("yyyy-MM-dd");
	var Stime = getCurrentMonthFirst();
	var Etime = GetDateStr(1);	
	searchFinal(Stime,Etime);
	$('#endTime').val(GetDateStr(0));
}
//查询当年
function searchYear(){
	//var Etime = new Date().getFullYear()+"-12-31";
	//需求：统计当年时，统计1号到当前时间 zhangkui 2017-04-17
	//2017-04-17新的
// 	var date=new Date();
// 	var Etime = date.Format("yyyy-MM-dd");
	var Stime = new Date().getFullYear()+"-01-01";
	var Etime = GetDateStr(1);	
	searchFinal(Stime,Etime);
	$('#endTime').val(GetDateStr(0));
}

//渲染处方金额
function changeCost(value,row,index){
	return value==null?value:value.toFixed(2);
}

/**
 * 重置
 * @author huzhenguo
 * @date 2017-03-17
 * @version 1.0
 */
function clears(){
// 	window.location.reload();
	$('#beginTime').val(GetDateStr(0));
	$('#endTime').val(GetDateStr(1));
	$('#invoiceNo').textbox('setValue','');
	var beginTime = $('#beginTime').val();
	var endTime = $('#endTime').val();
	var invoiceNo = $('#invoiceNo').textbox('getValue');
	var tab = $('#tabs').tabs('getSelected');
	var index = $('#tabs').tabs('getTabIndex',tab);
	if(index==0){//选择发票汇总
		noSummary = true;
		findInvoiceNoSummary(beginTime,endTime,invoiceNo);
	}else if(index==1){//选择发票明细
		detailed = true;
		findInvoiceDetailed(beginTime,endTime,invoiceNo);
	}else{//选择费用明细
		costDetailed = true;
		findCostDetailed(beginTime,endTime,invoiceNo);
	}
	$('#endTime').val(GetDateStr(0));
}
var noSummary = true;
function findInvoiceNoSummary(beginTime,endTime,invoiceNo){
	if(noSummary){
		$('#invoiceNoSummary').datagrid({
			url: "<%=basePath%>statistics/outpatientCostQuery/findInvoiceNoSummary.action",
			queryParams:{"invoiceNo":invoiceNo,"endTime":endTime,"beginTime":beginTime},
			pagination:true,
        	pageSize:20,
        	pageList:[20,30,50,100],
        	singleSelect: true,
        	onLoadSuccess:function(data){
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
			}
		});
		noSummary = false;
	}
}
var detailed = true;
function findInvoiceDetailed(beginTime,endTime,invoiceNo){
	if(detailed){
		$('#invoiceDetailed').datagrid({
			url: "<%=basePath%>statistics/outpatientCostQuery/findInvoiceDetailed.action",
			queryParams:{"invoiceNo":invoiceNo,"endTime":endTime,"beginTime":beginTime},
			pagination:true,
			pageSize:20,
        	pageList:[20,30,50,100],
        	singleSelect: true,
        	onLoadSuccess:function(data){
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
			}
		});
		detailed = false;		
	}
}
var costDetailed = true;
function findCostDetailed(beginTime,endTime,invoiceNo){
	if(costDetailed){
		$('#costDetailed').datagrid({
			url: "<%=basePath%>statistics/outpatientCostQuery/findCostDetailed.action",
			queryParams:{"invoiceNo":invoiceNo,"endTime":endTime,"beginTime":beginTime},
			pagination:true,
			pageSize:20,
        	pageList:[20,30,50,100],
        	singleSelect: true,
        	onLoadSuccess:function(data){
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
			}
		});
		costDetailed = false;		
	}
}





function functionTransType(val,row,index){
	if(val==1){
		return '正交易';
	}else{
		return '反交易';
	}
}

function functionCancelFlag(val,row,index){
	if(val==1){
		return '正常';
	}else if(val==0){
		return '作废';
	}else if(val==2){
		return '重打';
	}else{
		return '注销';
	}
}

function functionBalanceFlag(val,row,index){
	if(val==0){
		return '未日结';
	}else{
		return '已日结';
	}
}

function functionUserName(value,row,index){
	if(value!=null&&value!=''){
		return userMap[value];
	}
}

function functionPactCode(value,row,index){
	if(value!=null&&value!=''){
		return countMap[value];
	}
}

function functionEmpName(value,row,index){
	if(value!=null&&value!=''){
		return empMap[value];
	}
}

function functionDept(value,row,index){
	if(value!=null&&value!=''){
		return deptMap[value];
	}
}

function functionCheckFlag(val,row,index){
	if(val==0){
		return '未核查';
	}else{
		return '已核查';
	}
}

function functionExtFlag(val,row,index){
	if(val==1){
		return '自费';
	}else if(val==2){
		return '记账';
	}else{
		return '特殊';
	}
}

function functionDrugFlag(val,row,index){
	if(val==1){
		return '药品';
	}else{
		return '非药品';
	}
}

function functionSelfMade(val,row,index){
	if(val==3){
		return '自制药品';
	}else{
		return '非自制药品';
	}
}

function functionPaykindCode(val,row,index){
	if(val!=""&&val!=null){
		if(val==01){
			return '自费';
		}else{
			return '医保';
		}
	}
}

function functionExtendOne(val,row,index){
	if(val==""||val==null){
		return '主药';
	}else{
		return '辅材';
	}
}

function functionConfirmFlag(val,row,index){
	if(val!=""&&val!=null){
		if(val==0){
			return '未确认';
		}else{
			return '确认';
		}
	}
	
}

function functionClassCode(value,row,index){
	if(value!=null&&value!=''){
		return sysMap[value];
	}
}

function functionFeeCode(value,row,index){
	if(value!=null&&value!=''){
		return feeMap[value];
	}
}

function functionDrugQuality(value,row,index){
	if(value!=null&&value!=''){
		return drugMap[value];
	}
}

function functionDoseModelCode(value,row,index){
	if(value!=null&&value!=''){
		return doseMap[value];
	}
}

function functionFrequency(value,row,index){
	if(value!=null&&value!=''){
		return frequencyMap[value];
	}
}

function functionItem(value,row,index){
	if(value!=null&&value!=''){
		return itemMap[value];
	}
}



</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north',border:false" style="height:40px;width: 100%;padding:5px 5px 0px 5px;">
    	<table style="width: 100%;">
			<tr>
				<!-- 开始时间 --> 
					<td style="width:40px;" align="left">日期:</td>								
					<td style="width:100px;">
					<input id="beginTime" class="Wdate" type="text" name="beginTime" value="${beginTime}" onSelect="endSelect()" onClick="WdatePicker()" style="height:22px; width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<!-- 结束时间 --> 
					<td style="width:40px;" align="center">至</td>
					<td style="width:110px;">
					<input id="endTime" class="Wdate" type="text" name="endTime" value="${endTime}" onSelect="beginSelect()" onClick="WdatePicker()" style="height:22px;width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<td style="width:60px;" align="center">发票号:</td>
					<td style="width:120px;">
					<input id="invoiceNo" class="easyui-textbox">
					</td>
					<td>
					<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
				<td style='text-align: right'>
					<a href="javascript:void(0)" onclick="searchOne()" class="easyui-linkbutton"   data-options="iconCls:'icon-date'">当天</a>
					<a href="javascript:void(0)" onclick="searchThree()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">三天</a>
					<a href="javascript:void(0)" onclick="searchSeven()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">七天</a>
					<a href="javascript:void(0)" onclick="searchFifteen()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">十五天</a>
					<a href="javascript:void(0)" onclick="beforeMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">上月</a>
					<a href="javascript:void(0)" onclick="searchMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a>
					<a href="javascript:void(0)" onclick="searchYear()" class="easyui-linkbutton"  data-options="iconCls:'icon-date'">当年</a>
				</td>
			</tr>
		</table>
    </div>   
    <div data-options="region:'center'" style="width: 100%">
    	<div id="tabs" class="easyui-tabs" data-options="fit:true,border:false" style="height: 100%;" >
		    <div title="发票汇总" data-options="fit:true,border:false" style="height: 100%;">
		        <table id="invoiceNoSummary" class="easyui-datagrid" style="width:100%;height: 100%;" data-options="fit:true,border:false">
				    <thead>   
				        <tr>
				            <th data-options="field:'invoiceNo',width:'8%'">发票号</th>
				            <th data-options="field:'cardNo',width:'7%'">病历号</th>  
				            <th data-options="field:'regDate',width:'8%'">挂号日期</th>   
				            <th data-options="field:'name',width:'5%'">患者姓名</th>   
				            <th data-options="field:'paykindCode',formatter:functionPaykindCode,width:'5%'">结算类别</th> 
				            <th data-options="field:'pactCode',formatter:functionPactCode,width:'5%'">合同单位</th>   
				            <th data-options="field:'mcardNo',width:'7%'">个人编码</th>
				            <th data-options="field:'pubCost',width:'5%',align:'right',formatter:changeCost">可报销金额</th>   
				            <th data-options="field:'ownCost',width:'5%',align:'right',formatter:changeCost">不可报销金额</th>
				            <th data-options="field:'payCost',width:'5%',align:'right',formatter:changeCost">自付金额</th>
				            <th data-options="field:'totCost',width:'5%',align:'right',formatter:changeCost">总金额</th>
				            <th data-options="field:'realCost',width:'5%',align:'right',formatter:changeCost">实付金额</th>
				            <th data-options="field:'operName',width:'5%'">结算人</th>
				            <th data-options="field:'operDate',width:'8%'">结算时间</th>
				            <th data-options="field:'cancelFlag',formatter:functionCancelFlag,width:'5%'">发票状态</th>
				            <th data-options="field:'cancelInvoice',width:'8%'">作废发票号</th>
				            <th data-options="field:'cancelCode',formatter:functionUserName,width:'5%'">作废操作员</th>
				            <th data-options="field:'cancelDate',width:'8%'">作废时间</th>
				            <th data-options="field:'checkFlag',formatter:functionCheckFlag,width:'5%'">是否核查</th>
				            <th data-options="field:'checkOpcdName',width:'5%'">核查人</th>
				            <th data-options="field:'checkDate',width:'8%'">核查日期</th>
				            <th data-options="field:'balanceFlag',formatter:functionBalanceFlag,width:'5%'">是否已日结</th>
				            <th data-options="field:'balanceOpceName',width:'5%'">日结人</th>
				            <th data-options="field:'balanceDate',width:'8%'">日结日期</th>
				            <th data-options="field:'invoiceSeq',width:'7%'">发票序号</th>
				            <th data-options="field:'extFlag',formatter:functionExtFlag,width:'5%'">自费记账特殊</th>
				            <th data-options="field:'transType',width:'5%',formatter:functionTransType">交易类型</th>
				        </tr>   
				    </thead>   
				</table>
		    </div>   
		    <div title="发票明细"  data-options="fit:true">   
		    	<table id="invoiceDetailed" class="easyui-datagrid" style="width:100%" data-options="fit:true,border:false">    
				    <thead>   
				        <tr>
				            <th data-options="field:'invoiceNo',width:'8%'">发票号</th>
				            <th data-options="field:'transType',formatter:functionTransType,width:'5%'">交易类型</th>  
				            <th data-options="field:'invoSequence',width:'5%'">发票内流水号</th>   
				            <th data-options="field:'invoName',width:'5%'">科目名称</th>   
				            <th data-options="field:'pubCost',width:'5%',align:'right',formatter:changeCost">可报销金额</th> 
				            <th data-options="field:'ownCost',width:'5%',align:'right',formatter:changeCost">不可报销金额</th>   
				            <th data-options="field:'payCost',width:'5%',align:'right',formatter:changeCost">自付金额</th>
				            <th data-options="field:'payCost',width:'5%',align:'right',formatter:changeCost">总金额</th>   
				            <th data-options="field:'operDate',width:'8%'">操作时间</th>
				            <th data-options="field:'operCode',formatter:functionUserName,width:'5%'">操作员</th>
				            <th data-options="field:'balanceFlag',formatter:functionBalanceFlag,width:'5%'">是否已经日结</th>
				            <th data-options="field:'balanceOpcd',formatter:functionUserName,width:'5%'">日结人</th>
				            <th data-options="field:'balanceDate',width:'8%'">日结时间</th>
				            <th data-options="field:'cancelFlag',formatter:functionCancelFlag,width:'5%'">发票状态</th>
				        </tr>   
				    </thead>   
				</table>
		    </div>   
		    <div title="费用明细" data-options="fit:true">   
		        <table id="costDetailed" class="easyui-datagrid" style="width:100%" data-options="fit:true,border:false">    
				    <thead>   
				        <tr>
				            <th data-options="field:'invoiceNo',width:'8%'">发票号</th>
				            <th data-options="field:'itemCode',hidden:true,width:'10%'">项目代码</th>  
				            <th data-options="field:'itemName',width:'10%'">项目名称</th>   
				            <th data-options="field:'unitPrice',width:'5%',align:'right',formatter:changeCost">单价</th>   
				            <th data-options="field:'qty',width:'5%',align:'right'">数量</th> 
				            <th data-options="field:'days',width:'5%',align:'right'">付数</th>   
				            <th data-options="field:'pubCost',width:'5%',align:'right',formatter:changeCost">报销金额</th>
				            <th data-options="field:'payCost',width:'5%',align:'right',formatter:changeCost">自付金额</th>   
				            <th data-options="field:'ownCost',width:'5%',align:'right',formatter:changeCost">现金金额</th>
				            <th data-options="field:'ecoCost',width:'5%',align:'right',formatter:changeCost">优惠金额</th>
				            <th data-options="field:'invoCode',width:'5%',formatter:functionItem">发票科目</th>
				            <th data-options="field:'doctCodename',width:'5%'">开方医师</th>
				            <th data-options="field:'doctDeptname',width:'5%'">开方医师科室</th>
				            <th data-options="field:'operCode',formatter:functionUserName,width:'5%'">划价人</th>
				            <th data-options="field:'operDate',width:'8%'">划价时间</th>
				            <th data-options="field:'feeCpcdname',width:'5%'">收费员</th>
				            <th data-options="field:'feeDate',width:'8%'">收费时间</th>
				            <th data-options="field:'invoSequence',width:'5%'">发票内流水号</th>
				            <th data-options="field:'cancelFlag',formatter:functionCancelFlag,width:'5%'">发票状态</th>
				            <th data-options="field:'extFlag',width:'5%',formatter:functionExtFlag">自费记账特殊</th>
				            <th data-options="field:'transType',formatter:functionTransType,width:'5%'">交易类型</th>
				            <th data-options="field:'extFlag2',formatter:functionBalanceFlag,width:'5%'">是否已日结</th>
				            <th data-options="field:'feeCode',width:'5%',formatter:functionFeeCode">最小费用</th>
				            <th data-options="field:'classCode',formatter:functionClassCode,width:'5%'">系统类别</th>
				            <th data-options="field:'recipeNo',width:'5%'">处方号</th>
				            <th data-options="field:'sequenceNo',width:'5%'">流水号</th>
				            <th data-options="field:'drugFlag',formatter:functionDrugFlag,width:'5%'">是否药品</th>
				            <th data-options="field:'nobackNum',width:'5%',align:'right'">可退数量</th>
				            <th data-options="field:'packQty',width:'5%',align:'right'">包装数量</th>
				            <th data-options="field:'drugQuality',formatter:functionDrugQuality,width:'5%'">药品性质</th>
				            <th data-options="field:'specs',width:'5%'">规格</th>
				            <th data-options="field:'doseModelCode',formatter:functionDoseModelCode,width:'5%'">剂型</th>
				            <th data-options="field:'selfMade',formatter:functionSelfMade,width:'5%'">是否自制药</th>
				            <th data-options="field:'frequencyCode',formatter:functionFrequency,width:'5%',align:'right'">频次</th>
				            <th data-options="field:'useName',width:'5%'">用法</th>
				            <th data-options="field:'injectNumber',width:'5%',align:'right'">院注次数</th>
				            <th data-options="field:'execDpnm',formatter:functionDept,width:'5%'">执行科室</th>
				            <th data-options="field:'centerCode',width:'5%'">医保中心代码</th>
				            <th data-options="field:'itemGrade',width:'5%'">项目等级</th>
				            <th data-options="field:'newItemrate',width:'5%'">新项目比例</th>
				            <th data-options="field:'oldItemrate',width:'5%'">原项目比例</th>
				            <th data-options="field:'extendOne',formatter:functionExtendOne,width:'5%'">是否主药</th>
				            <th data-options="field:'combNo',width:'5%'">组合号</th>
				            <th data-options="field:'packageName',width:'5%'">复合项目名称</th>
				            <th data-options="field:'confirmFlag',formatter:functionConfirmFlag,width:'5%'">医技终端确认</th>
				            <th data-options="field:'confirmCode',formatter:functionUserName,width:'5%'">终端确认人</th>
				            <th data-options="field:'confirmDeptname',width:'5%'">终端确认科室</th>
				            <th data-options="field:'confirmDate',width:'8%'">终端确认时间</th>
				            <th data-options="field:'confirmNum',width:'5%',align:'right'">确认数量</th>
				            <th data-options="field:'clinicCode',width:'8%'">门诊号</th>
				            <th data-options="field:'patientNo',width:'8%'">病历号</th>
				            <th data-options="field:'regDate',width:'8%'">挂号日期</th>
				            <th data-options="field:'regDpcdname',width:'5%'">挂号科室</th>
				        </tr>   
				    </thead>   
				</table>
		    </div>   
		</div>  
    </div>   
</div>

</body>
</html>