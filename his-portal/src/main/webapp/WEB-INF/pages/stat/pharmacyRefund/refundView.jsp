<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>门诊药房退费统计</title>
<%@ include file="/common/metas.jsp"%>

<script type="text/javascript">
var sendWinMap = null;
var feeStatCodeMap = null;


$(function(){
	funformat();
	//默认显示前三天
	//发药窗口
	$.ajax({
		url : "<%=basePath%>statistics/RefundStat/queryAllSendWin.action",
		type:'post',
		success: function(data) {
			sendWinMap = data;
			loadGrid();
		}
	});
	//统计大类窗口
	$.ajax({
		url : "<%=basePath%>statistics/RefundStat/queryFeeStatCode.action",
		type:'post',
		success: function(data) {
			feeStatCodeMap = data;
		}
	});
});
//渲染发药窗口
// function functionSendWin(value,row,index){
// 	var winName = '';
// 	if(value!=null&&valu!=''){
// 		var win = value.split(",");
// 		for (var i = 0; i < win.length; i++) {
// 			if(i!=0){
// 				winName += ',';
// 			}
// 			winName += sendWinMap[win[i]];
// 		}
// 		return winName;
// 	}
// }
// //渲染费用类别
// function functionFeeCode(value,row,index){
// 	var feeName = '';
// 	if(value!=null&&valu!=''){
// 		var win = value.split(",");
// 		for (var i = 0; i < win.length; i++) {
// 			if(i!=0){
// 				feeName += ',';
// 			}
// 			feeName += feeStatCodeMap[win[i]];
// 		}
// 		return feeName;
// 	}
// }
/***
 * 加载表格
 */
function loadGrid(){
	//默认显示当月	
	var beginDate = getCurrentMonthFirst();
	var Etime = GetDateStr(0);
	//结束时间加一天
	var date = new Date(Etime);
	date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
	var endDate = date.Format("yyyy-MM-dd");
	//设置时间
	$('#beginDate').val(beginDate);
	$('#endDate').val(Etime);
	
	$('#dataGrid1').datagrid({ 
		rownumbers:true,idField: 'id',striped:true,border:true,pageSize:20,pageNumber: 1,
		checkOnSelect:false,selectOnCheck:false,singleSelect:true,pagination:true,
		border:false,fit:true,
		url:'<%=basePath%>statistics/RefundStat/queryByMongo.action',
		queryParams:{'beginDate':beginDate,'endDate':endDate},
		onLoadSuccess: function(data){
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
	    columns:[[    
	        {field:'invoiceNo',title:'发票流水号',width:'10%',align:'center'}, 
	        {field:'patientName',title:'患者名称',width:'10%',align:'center'}, 
	        {field:'feeStatCode',title:'退费项目',width:'10%',align:'center'
	        	,formatter:function(value,row,index){
		        	var feeName = '';
		        	if(value!=null&&value!=''){
		        		var feeCode = value.split(",");
		        		for (var i = 0; i < feeCode.length; i++) {
		        			if(i!=0){
		        				feeName += ',';
		        			}
		        			feeName += feeStatCodeMap[feeCode[i]];
		        		}
		        		return feeName;
		        	}
	        	}
	        }, 
	        {field:'confirmDate',title:'退费日期',width:'10%',align:'center',formatter:function(value,row,index){
				if (value!=null&&value!=''){
					var date = new Date(value);
					var dateFormater = date.Format("yyyy-MM-dd");
					return dateFormater;
				} 
			}}, 

	        {field:'refundMoney',title:'退费金额',width:'10%',halign:'center',align:'right'
	        	,formatter:function(value,row,index){
	        		return value==null?value:value.toFixed(2);
	        	}}, 
	        {field:'sendWin',title:'发药窗口',width:'30%',align:'center'
	        	,formatter:function(value,row,index){
	        	var winName = '';
		        	if(value!=null&&value!=''){
		        		var winCode = value.split(",");
		        		for (var i = 0; i < winCode.length; i++) {
		        			if(sendWinMap[winCode[i]]!=null){
		        				winName += sendWinMap[winCode[i]]+',';
		        			}
		        		}
		        		return winName.substring(0,winName.lastIndexOf(","));
		        	}
	        	}
	        }
	    ]],
	}); 
}


/***
 * 检索
 */
function searchEvent(){
	var beginDate = $('#beginDate').val();
	var endDate = $('#endDate').val();
	if(beginDate==null || beginDate=="" || endDate==null || endDate==""){
		$.messager.alert("提示","请填写正确的时间范围！");
		return ;
		}
	var feeStatCode = $('#feeStatCode').combobox('getValue');
	if(feeStatCode == null || feeStatCode == ""){
		$.messager.alert('提示','请选择药费类别!','warning');
		return false;
	}
    //结束时间要加一天
	var date = new Date(endDate);
	date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
	var end = date.Format("yyyy-MM-dd");
	if(beginDate&&endDate){
        if(beginDate>endDate){
          $.messager.alert("提示","开始时间不能大于结束时间！");
          return ;
        }
      }
	if(beginDate==null || beginDate==""){
		beginDate="${beginDate}";
  	}
  	if(endDate==null || endDate==""){
  		endDate="${endDate}";
  	}
	$('#dataGrid1').datagrid('load',{
		beginDate: beginDate,
		endDate: end,
		feeStatCode:feeStatCode
	});
}
function searchFinal(Stime,Etime){
	$('#beginDate').val(Stime);
	$('#endDate').val(Etime);
	searchEvent();
 }
//距离当前多少天的日期
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
	var Etime = GetDateStr(0);
	searchFinal(Stime,Etime);
	$("#endDate").val(GetDateStr(0));  
 }
 //查询前三天
function searchThree(){
	var Stime = GetDateStr(-3);
	var Etime = GetDateStr(-1);
	searchFinal(Stime,Etime);
	$("#endDate").val(GetDateStr(-1));     
}
//查询前七天
function searchSeven(){
	var Stime = GetDateStr(-7);
	var Etime = GetDateStr(-1);
	searchFinal(Stime,Etime);
	$("#endDate").val(GetDateStr(-1));   
}
//查询前15天 zhangkui 2017-04-17
function searchFifteen(){
	var Stime = GetDateStr(-15);
	var Etime = GetDateStr(-1);
	searchFinal(Stime,Etime);
	$("#endDate").val(GetDateStr(-1));   
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
    searchFinal(Stime,lastDay);
    $('#endDate').val(lastDay);
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
	var Stime = getCurrentMonthFirst();
	var Etime = GetDateStr(0);	
	searchFinal(Stime,Etime);
	$("#endDate").val(GetDateStr(0));  
}
//查询当年
function searchYear(){
	var Etime = GetDateStr(0);
	var Stime = new Date().getFullYear()+"-01-01";
	searchFinal(Stime,Etime);
	$("#endDate").val(GetDateStr(0));  
}



/**
 * 重置
 * @author huzhenguo
 * @date 2017-03-17
 * @version 1.0
 */
function clears(){
	funformat();
	loadGrid();
}	
/***
 * 打印
 */
function queryTopWeek(){
	var beginDate = $('#beginDate').val();
	var endDate = $('#endDate').val();
	var fileName="iReportPhaRefund";
	if(beginDate==null||beginDate==''||endDate==null||endDate==''){
        $.messager.alert("提示","日期不能为空！");
        return;
	}
	var rows = $("#dataGrid1").datagrid("getRows");
	var feeStatCode = $('#feeStatCode').combobox("getValue");
	if(feeStatCode == null || feeStatCode == ""){
		$.messager.alert('提示','请添加药费类别！');
		return ;
	}
	if (rows==null||rows.length==0) {
		$.messager.alert("提示","没有数据，不能打印！"); 	
		return;
	}else{
	//var timerStr = Math.random();
	$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
			if (res) {
			window.open ("<%=basePath%>statistics/RefundStat/iReportPhaRefund.action?feeStatCode="+feeStatCode+"&beginDate="+beginDate+"&endDate="+endDate+"&fileName="+fileName+"",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
			}
		});
		/* window.open ("<c:url value='/statistics/RefundStat/iReportPhaRefund.action?randomId='/>"
				+timerStr+"&feeStatCode="+feeStatCode+"&beginDate="+beginDate+"&endDate="+endDate
				+"&fileName=MZYFTFTJ",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); */
	}
}

/***
 * 导出
 */
function queryBottenWeek(){
	var rows = $("#dataGrid1").datagrid("getRows");
	var beginDate = $('#beginDate').val();
	var endDate = $('#endDate').val();
	if(beginDate==null||beginDate==''||endDate==null||endDate==''){
        $.messager.alert("提示","日期不能为空！");
        return;
	}
	if (rows==null||rows.length==0) {
		$.messager.alert("提示","没有数据，不能导出！");
		return;
	}else{
	$.messager.confirm('确认', '确定要导出吗?', function(res) {
		if (res) {
			var opt = $('#dataGrid1').datagrid('options');
			var page = opt.pageNumber;
			var rows = opt.pageSize;
			
			$('#form1').form('submit', {
				url:'<%=basePath%>statistics/RefundStat/exportExcel.action',
				queryParams:{'page':page,'rows':rows},
				success : function(data) {
					$.messager.alert("提示", "导出成功！", "success");
				},
				error : function(data) {
					$.messager.alert("提示", "导出失败！", "error");
				}
			});
		}
	});
   }
}

/***
 * 初始数据
 */
function funformat(){
	$('#feeStatCode').combobox({
		data:[{'id':'01,02,03','value':'全部',"selected":true},{'id':'01','value':'西药费'},{'id':'02','value':'中成药费'},{'id':'03','value':'中草药费'}],
		valueField: 'id',    
        textField: 'value',
        editable:false
	});
}

//格式化日期
function formatDate(val,row){
	var date = new Date(val);
	var dateFormater = date.Format("yyyy-MM-dd");
	return dateFormater;
}

</script>
<!-- 修改行数宽度 -->
<style type="text/css">
	.datagrid-header-rownumber,.datagrid-cell-rownumber{
		   width:30px;
		 }
</style>
</head>
<body style="margin: 0px;padding: 0px;"> 
<div id="cc" class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north',border:false" >
  		<form id="form1" method="post" style="height:38px;padding:12px 5px 0px 7px;">
	   	<!-- 	开始时间： -->日期:
	   			<span style="margin-left:1"><input id="beginDate" class="Wdate" type="text" name="beginDate" value="${beginDate}" data-options="showSeconds:true" onClick="WdatePicker()" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/></span> 
	    <!-- 结束时间：  --><span style="margin-left:4;margin-right:4">至</span>
	    		<input id="endDate" class="Wdate" type="text" name="endDate" value="${endDate}" data-options="showSeconds:true" onClick="WdatePicker()" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
	    	<span style="margin-left:8">药费类别:</span>
	    		<input class="easyui-combobox" id="feeStatCode" name="feeStatCode" style="width:110px;margin-left:3"/> 
	    		<a href="javascript:searchEvent()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
	    		<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
				<a href="javascript:queryTopWeek()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a>
				<a href="javascript:queryBottenWeek()" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a>
				<span style="float:right;margin-right: 3">
		    		<a href="javascript:void(0)" onclick="searchOne()" class="easyui-linkbutton"   data-options="iconCls:'icon-date'">当天</a>
					<a href="javascript:void(0)" onclick="searchThree()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">三天</a>
					<a href="javascript:void(0)" onclick="searchSeven()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">七天</a>
					<a href="javascript:void(0)" onclick="searchFifteen()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">十五天</a>
					<a href="javascript:void(0)" onclick="beforeMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">上月</a>
					<a href="javascript:void(0)" onclick="searchMonth()" class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a>
					<a href="javascript:void(0)" onclick="searchYear()" class="easyui-linkbutton"  data-options="iconCls:'icon-date'">当年</a>
		    	</span>
  		</form>
    </div>   
    <div data-options="region:'center',noheader:true">
	    <table id="dataGrid1"></table> 
    </div>   
</div>  
</body>
</html>