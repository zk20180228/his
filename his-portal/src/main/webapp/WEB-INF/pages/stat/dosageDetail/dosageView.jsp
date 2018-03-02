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
<title>门诊配发药工作量统计</title>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
.tableCss {
	border-collapse: collapse;
	border-spacing: 0;
	border-left: 1px solid #95b8e7;
	border-top: 1px solid #95b8e7;
}

.datagrid-footer-inner {
	border-width: 0px 0 0 0;
}

.tableLabel {
	text-align: center;
	width: 150px;
}

.tableCss td {
	border-right: 1px solid #95b8e7;
	border-bottom: 1px solid #95b8e7;
	padding: 5px 15px;
	word-break: keep-all;
	white-space: nowrap;
}

#s1 .textbox {
	margin-top: -5px;
}

#s2 .easyui-linkbutton {
	margin-top: -4px;
}
.container .div_scroll {
	width: 100%;
	overflow: auto;
	height: auto;
	float: left;
}
</style>

<script type="text/javascript">

$(function(){
// 	$('#emp').hide();
// 	$('#deptEmp').hide();
// 	$('#deptTerminal').hide();
// 	fundivshow('0');
// 	$("#type_dept").attr("checked","checked");
	$("#view_dept").attr("checked","checked");
	$("input[name=type]").click(function(){
		showCont();
	});
	loadTerminal();
	$('#type_Value').combobox('setValue','6307');
	$('#view').show();
// 	$('#type_Value').combobox('setText','门诊中药房');
// 	showCont();
	/* 2017-04-19 zhangkui */
// 	loadEmployee();
	//默认选中人员的第一个
// 	$.ajax({
// 		type:"post",
<%-- 		url:'<%=basePath%>baseinfo/employee/employeeCombobox.action', --%>
// 		date:"",
// 		success:function(msg){
// 			$("#type_Value").combobox('select', msg[0].jobNo);
			searchEvent();
// 		}
// 	});
});
//渲染处方金额
function changeCost(value,row,index){
	
	return value==null?value:value.toFixed(2);
}

/***
 * radio 事件
 */
function showCont(){
	var val = $("input[name=type]:checked").attr("value");
	if(val == 0){
		loadTerminal();
		$('#type_Value').combobox('setValue', '');
		$('#view').show();
	}else{
		loadEmployee();
		$('#type_Value').combobox('setValue', '');
		$('#view').hide();
	}
}

/***
 * 加载药房
 */
function loadTerminal(){
	$('#type_Value').combobox({     
		url:'<%=basePath%>statistics/DosDetail/deptForType.action',
		valueField:'id', 
		textField:'deptName'
	});
}

/***
 * 加载员工
 */
function loadEmployee(){
	$('#type_Value').combobox({     
		url:'<%=basePath%>/outpatient/transfuse/queryEmptrans.action',
		valueField:'jobNo', 
		textField:'name'
	});
}

/***
 * 检索方法
 */
function searchEvent(){
	if (!$('#searchForm').form('validate')) {
		$.messager.alert('提示','请填写完整信息!','warning');
		return ;
	}
	
	var typeView;
	var typeVal = $("input[name=type]:checked").attr("value");
	if(typeVal == 1){
		var typeValue = $('#type_Value').combobox('getValue');
		if(typeValue==null||typeValue==""||typeof typeValue == "undefined"){
			$.messager.alert('提示','请选择人员!','warning');
			return ;
		}
		//人员统计
		typeView = 2;
		$('#typeView').val(2);
		emptables(typeView,typeValue);
	}else if(typeVal == 0){
		var typeValue = $('#type_Value').combobox('getValue');
		if(typeValue==null||typeValue==""||typeof typeValue == "undefined"){
			$.messager.alert('提示','请选择药房!','warning');
			return ;
		}
		var viewVal = $("input[name=view]:checked").attr("value");
		if(viewVal==null||viewVal==""||typeof viewVal == "undefined"){
			$.messager.alert('提示','请选择展示方式!','warning');
			return ;
		}
		if(viewVal == 1){
			//按人员显示
			typeView = 1;
			$('#typeView').val(1);
			deptemptables(typeView,typeValue);
		}else if(viewVal == 0){
			//按终端显示
			typeView = 0;
			$('#typeView').val(0);
			deptterminals(typeView,typeValue);
		}
	}else{
		if(typeView == null){
			$.messager.alert('提示','请选择药房或人员!','warning');
			return ;
		}
	}
}
function searchFinal1(Stime,Etime){
	if(Stime&&Etime){
        if(Stime>Etime){
          $.messager.alert("提示","开始时间不能大于结束时间！");
          return ;
        }
      }
	$('#beginDate').val(Stime);
	$('#endDate').val(Etime);
	searchEvent();
 }
 //修改检索事件 zhangkui 2017-04-21 
 function searchFinal(){
	var Stime=	$('#beginDate').val();
	var Etime=$('#endDate').val();
	if(Stime==null || Stime=="" || Etime==null || Etime==""){
		$.messager.alert("提示","请填写正确的时间范围！");
		return ;
		}
	if(Stime&&Etime){
        if(Stime>Etime){
          $.messager.alert("提示","开始时间不能大于结束时间！");
          return ;
        }
      }
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
	searchFinal1(Stime,Etime);
	$("#endDate").val(GetDateStr(0));
 }
 //查询前三天
function searchThree(){
	var Stime = GetDateStr(-3);
	var Etime = GetDateStr(-1);
	searchFinal1(Stime,Etime);
	$("#endDate").val(GetDateStr(-1));
}
//查询前七天
function searchSeven(){
	var Stime = GetDateStr(-7);
	var Etime = GetDateStr(-1);
	searchFinal1(Stime,Etime);
	$("#endDate").val(GetDateStr(-1));
}
//查询前15天 zhangkui 2017-04-17
function searchFifteen(){
	var Stime = GetDateStr(-15);
	var Etime = GetDateStr(-1);
	searchFinal1(Stime,Etime);
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

    searchFinal1(Stime,lastDay);
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
	//var Etime = getCurrentMonthLast();
	//需求：统计当月时，统计1号到当前时间 zhangkui 2017-04-17
	//2017-04-17新的
// 	var date=new Date();
// 	var Etime = date.Format("yyyy-MM-dd");
	var Etime = GetDateStr(0);	
	searchFinal1(Stime,Etime);
	$("#endDate").val(GetDateStr(0));
}
//查询当年
function searchYear(){
	//var Etime = new Date().getFullYear()+"-12-31";
	//需求：统计当年时，统计1号到当前时间 zhangkui 2017-04-17
	//2017-04-17新的
// 	var date=new Date();
// 	var Etime = date.Format("yyyy-MM-dd");
	var Etime = GetDateStr(0);	
	var Stime = new Date().getFullYear()+"-01-01";
	searchFinal1(Stime,Etime);
	$("#endDate").val(GetDateStr(0));
}


//按人员
function emptables(typeView,typeVal){
	var code = $('#type_Value').combobox('getValue');
	var beginDate = $('#beginDate').val();
	var endDate = $('#endDate').val();
	var date = new Date(endDate);
	date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
	var end = date.Format("yyyy-MM-dd");
	var pfbs = "${pfbs}";
	$.messager.progress({text:'查询中，请稍后...',modal:true});
	$.ajax({
		url:"<%=basePath%>statistics/DosDetail/queryDetail.action",
		type:'post',
		data:{typeView:typeView, typeValue:typeVal, beginDate:beginDate, endDate:end, pfbs:pfbs,code:code},
		success:function(dataMap) {
			$.messager.progress('close');
			if(dataMap.resMsg=='error'){
				$.messager.alert('提示','查询信息失败!');
			}else{
// 				$('#emptable').datagrid('loadData',dataMap.resCode).datagrid('freezeRow',0);
				//var json = eval('(' + dataMap.resCode + ')'); 
				var winH = $("body").height()-86;
				$('#emptable').datagrid({
					singleSelect: true,
					showFooter: true,
					data: dataMap.rows,
					fit:true
				});
				var depttableH = $("#emptable")["0"].parentElement.clientHeight;
				if(depttableH>winH){
					$('#emptable').datagrid({
						singleSelect: true,
						showFooter: true,
						data: dataMap.rows,
						height:winH+"px",
						fit:true
					});
				}
			}
		},
		error:function(){
			$.messager.progress('close');
			$.messager.alert('提示','请求失败！');
		}
	});
	$('#emp').show();
	$('#deptEmp').hide();
	$('#deptTerminal').hide();
}
//药房按人员显示
function deptemptables(typeView,typeVal){
	var code = $('#type_Value').combobox('getValue');
	var beginDate = $('#beginDate').val();
	var endDate = $('#endDate').val();
	var date = new Date(endDate);
	date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
	var end = date.Format("yyyy-MM-dd");
	var pfbs = "${pfbs}";
	$.messager.progress({text:'查询中，请稍后...',modal:true});
	$.ajax({
		url:"<%=basePath%>statistics/DosDetail/queryDetail.action",
		type:'post',
		data:{typeView:typeView, typeValue:typeVal, beginDate:beginDate, endDate:end, pfbs:pfbs,code:code},
		success:function(dataMap) {
			$.messager.progress('close');
			if(dataMap.resMsg=='error'){
				$.messager.alert('提示','查询信息失败!');
			}else{
// 				$('#dapeEmptable').datagrid('loadData',dataMap.resCode).datagrid('freezeRow',0);
				//var json = eval('(' + dataMap.resCode + ')'); 
				var winH = $("body").height()-86;
				$('#dapeEmptable').datagrid({
					singleSelect: true,
					showFooter: true,
					data: dataMap.rows,
					fit:true
				});
				var depttableH = $("#dapeEmptable")["0"].parentElement.clientHeight;
				if(depttableH>winH){
					$('#dapeEmptable').datagrid({
						singleSelect: true,
						showFooter: true,
						data: dataMap.rows,
						height:winH+"px",
						fit:true
					});
				}
				$('#endDate').val(endDate);
				
			}
		},
		error:function(){
			$.messager.progress('close');
			$.messager.alert('提示','请求失败！');
		}
	});
	$('#deptEmp').show();
	$('#emp').hide();
	$('#deptTerminal').hide();
}
//药房终端显示
function deptterminals(typeView,typeVal){
	var code = $('#type_Value').combobox('getValue');
	var beginDate = $('#beginDate').val();
	var endDate = $('#endDate').val();
	var date = new Date(endDate);
	date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
	var end = date.Format("yyyy-MM-dd");
	var pfbs = "${pfbs}";
	$.messager.progress({text:'查询中，请稍后...',modal:true});
	$.ajax({
		url:"<%=basePath%>statistics/DosDetail/queryDetail.action",
		type:'post',
		data:{typeView:typeView, typeValue:typeVal, beginDate:beginDate, endDate:end, pfbs:pfbs,code:code},
		success:function(dataMap) {
			$.messager.progress('close');
			if(dataMap.resMsg=='error'){
				$.messager.alert("提示","查询信息失败!");
			}else{
				//var json = eval('(' + dataMap.rows + ')');
				var winH = $("body").height()-86;
				$('#depttable').datagrid({
					singleSelect: true,
					showFooter: true,
					data: dataMap.rows,
					fit:true 
				});
				var depttableH = $("#depttable")["0"].parentElement.clientHeight;
				if(depttableH>winH){
					$('#depttable').datagrid({
						singleSelect: true,
						showFooter: true,
						data: dataMap.rows,
						fit:true
					});
				}
			}
		},
		error:function(){
			$.messager.progress('close');
			$.messager.alert('提示','请求失败！');
		}
	});
	$('#deptTerminal').show();
	$('#deptEmp').hide();
	$('#emp').hide();
}


function fundivshow(typeView){
	if(typeView != ''){
		if(typeView == 2){
			loadEmployee();
			$('#view').hide();
			
			//人员统计
			$('#emp').show();
			$('#deptTerminal').hide();
			$('#deptEmp').hide();
		}else{
			if(typeView == 1){
				$('#view').show();
				loadTerminal();
				//按人员显示
				$('#deptEmp').show();
				$('#emp').hide();
				$('#deptTerminal').hide();
			}else if(typeView == 0){
				$('#view').show();
				loadTerminal();
				//按终端显示
				$('#deptTerminal').show();
				$('#emp').hide();
				$('#deptEmp').hide();
			}
		}
	}
}

/***
 * 打印功能
 */
 function queryTopWeek(){
	var beginDate = $('#beginDate').val();
	var endDate = $('#endDate').val();
	if(beginDate==null||beginDate==''||endDate==null||endDate==''){
        $.messager.alert("提示","日期不能为空！");
        return;
	}
	var typeView = $('#typeView').val();
	if(typeView!=null&&typeView!=''){
		var typeValue = $('#type_Value').combobox('getValue');
		
		if(typeValue == null || typeValue == ""){
			$.messager.alert('提示','请添加搜索条件！');
			return false;
		}
		if(typeValue==1 || typeValue==0){
			var viewVal = $("input[name=view]:checked").attr("value");
			if(viewVal==null||viewVal==""||typeof viewVal == "undefined"){
				$.messager.alert('提示','请选择展示方式!','warning');
				return ;
			}
		}
		
		var timerStr = Math.random();
		//var rows="${map.listSto }";
		var id = "";
		if(typeView==0){
			id = "depttable"
		}
		if(typeView==1){
			id = "dapeEmptable"
		}
		if(typeView==2){
			id = "emptable"
		}
				//hedong 20170316  报表打印参数传递改造为表单POST提交的方式示例
			    //给表单的隐藏字段赋值
			    var pfbs="${pfbs}";
				var rows=$('#'+id).datagrid('getRows');
				var typeValueName = $('#type_Value').combobox('getText');
		   		 $("#reportToRows").val(JSON.stringify(rows));
			   // alert(JSON.stringify(rows));
			    $("#reportTypeView").val(typeView);
			    $("#typeValue").val(typeValueName);
			    $("#reportPfbs").val(pfbs);	
			    $("#reportToLogin").val(beginDate);
			    $("#reportToEnd").val(endDate);
			   var formTarget="hiddenFormWin";
				var fileName="";
		        if (typeView==0) { 
		        	fileName="iReportDetail";
				 }else{
					 fileName="iReportDetail2";
			 	}	
		       var tmpPath = "<%=basePath%>statistics/DosDetail/iReportDetail.action?fileName="+fileName;
			   
		        //设置表单target
		        $("#reportToHiddenForm").attr("target",formTarget);
		        //设置表单访问路径
				$("#reportToHiddenForm").attr("action",tmpPath);
			
		       //表单提交 target
			if(rows==null||rows.length==1){
				$.messager.alert("提示","没有数据，不能打印！"); 	
				return;
			}else{
				$.messager.confirm('确认', '确定要打印吗?', function(res) {  //提示是否打印假条
					if (res){	
							//表单提交时打开一个空的窗口
						    $("#reportToHiddenForm").submit(function(e){
						   		var timerStr = Math.random();
						 		window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
						 
						    });
					        //表单提交
						    $("#reportToHiddenForm").submit();
						 
					  }
					});		
		}
	}else{
		$.messager.alert('提示','请先进行检索！');
	}
} 
/***
 * 导出功能
 */
function queryBottenWeek(){
// 	var typeView = $('#typeView').val();
// 	var beginDate = $('#beginDate').val();
// 	var endDate = $('#endDate').val();
// 	var typeValue = $('#type_Value').combobox('getValue');
// 	var pfbs = $("#pfbs").val();
// 	var code = $('#type_Value').combobox('getValue');
	
	var beginDate = $('#beginDate').val();
	var end = $('#endDate').val();
	if(beginDate==null||beginDate==''||end==null||end==''){
        $.messager.alert("提示","日期不能为空！");
        return;
	}
	var code = $('#type_Value').combobox('getValue');
	var date = new Date(end);
	date.setDate(date.getDate()+1);//获取AddDayCount天后的日期
	var endDate= date.Format("yyyy-MM-dd");
	var pfbs = "${pfbs}";
	var typeView = $('#typeView').val();
	var typeValue = $('#type_Value').combobox('getValue');
	
	if(beginDate&&end){
		if(beginDate>end){
			$.messager,alert("提示","开始时间不能大于结束时间！");
			return ;
		}
	}
	
	var id = "";
	if(typeView==0){
		id = "depttable"
	}
	if(typeView==1){
		id = "dapeEmptable"
	}
	if(typeView==2){
		id = "emptable"
	}
	var rows=$('#'+id).datagrid('getRows');
	if(rows==null||rows.length==1){
		$.messager.alert("提示","没有数据，不能导出！");
		return;
	}else{
		$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
			if (res) {
				$('#saveForm').form('submit', {
					url :"<%=basePath%>statistics/DosDetail/exportExcel.action?code="+code+"&beginDate="+beginDate+"&endDate="+endDate+"&pfbs="+pfbs+"&typeView="+typeView+"&typeValue="+typeValue
				});
				
				//setTimeout('$.messager.alert("操作提示", "导出成功！", "success")',800);
				
			}
		});
	}
	
	
}


</script>
<script type="text/javascript"
	src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>

</head>
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" fit="true">
		<div data-options="region:'north',border:false"
			style="height: 85px; padding-bottom: 5px; overflow: hidden;"
			class="dosageViewSize">

			<div class = "container" style="width: 100%;">
				<div id = "scroll" class="">
					<div id="tabTitle" style="padding-top: 10px; /* min-width: 1570px */;height: 38px;">
						<span style="margin-left: 5"> <input type="radio"
							id="type_dept" name="type" value='0' checked="checked"
							<c:if test="${typeView == 0 || typeView == 1}">checked="checked"</c:if>>药房
							<input type="radio" id="type_emp" name="type" value='1'>人员
							<!--默认选中人员  --> <!--<c:if test="${typeView == 2}">checked="checked"</c:if>  -->
						</span> <span style="margin-left: 10" id="s1"> <input
							id="type_Value" class="easyui-combobox"
							data-options="required:true" name="typeValue"
							value="${typeValue}" style="height: 25px; width: 120px">
						</span> <span id="view" style="margin-left: 10; display: none"> <input
							type="radio" id="view_dept" name="view" value='0'
							<c:if test="${typeView == 0}">checked="checked"</c:if>>按终端显示
							<input type="radio" id="view_emp" name="view" value='1'
							<c:if test="${typeView == 1}">checked="checked"</c:if>>按人员显示
						</span> <span style="margin-left: 10"> <!-- 开始时间： -->日期: <input
							id="beginDate" class="Wdate" type="text" name="beginDate"
							value="${beginDate}" data-options="showSeconds:true"
							onClick="WdatePicker()"
							style="height: 25px; width: 110px; border: 1px solid #95b8e7; border-radius: 5px;" />
						</span> <span style="margin-left: 3"> <!-- 终止时间： -->至 <input
							id="endDate" class="Wdate" type="text" name="endDate"
							value="${endDate}" data-options="showSeconds:true"
							onClick="WdatePicker()"
							style="height: 25px; width: 110px; border: 1px solid #95b8e7; border-radius: 5px;" />
						</span> <span> <input type="hidden" id="pfbs" name="pfbs"
							value="${pfbs }"> <input type="hidden" id="typeView"
							name="typeView" value="${typeView }">
						</span> <span id="s2"> <a href="javascript:searchFinal()"
							class="easyui-linkbutton" data-options="iconCls:'icon-search'"
							style="height: 25px">查询</a> <span id="funBon" style="display:">
								<a href="javascript:queryTopWeek()" class="easyui-linkbutton"
								data-options="iconCls:'icon-printer'">打印</a> <a
								href="javascript:queryBottenWeek()" class="easyui-linkbutton"
								data-options="iconCls:'icon-down'">导出</a>
						</span>
						</span> <span style="float: right; margin-right:20"> <a
							href="javascript:void(0)" onclick="searchOne()"
							class="easyui-linkbutton" data-options="iconCls:'icon-date'">当天</a>
							<a href="javascript:void(0)" onclick="searchThree()"
							class="easyui-linkbutton" data-options="iconCls:'icon-date'">三天</a>
							<a href="javascript:void(0)" onclick="searchSeven()"
							class="easyui-linkbutton" data-options="iconCls:'icon-date'">七天</a>
							<a href="javascript:void(0)" onclick="searchFifteen()"
							class="easyui-linkbutton" data-options="iconCls:'icon-date'">十五天</a>
							<a href="javascript:void(0)" onclick="beforeMonth()"
							class="easyui-linkbutton" data-options="iconCls:'icon-date'">上月</a>
							<a href="javascript:void(0)" onclick="searchMonth()"
							class="easyui-linkbutton" data-options="iconCls:'icon-date'">当月</a>
							<a href="javascript:void(0)" onclick="searchYear()"
							class="easyui-linkbutton" data-options="iconCls:'icon-date'">当年</a>
						</span>
					</div>
				</div>
			</div>
			
			
			
			<div style="margin-top: 10px">
				<div
					style="font-size: 32px; font-weight: 700px; position: absolute; bottom: 0px; left: 50%; margin-left: -144px;"
					id="dosageViewFontSize">
					<c:choose>
						<c:when test="${pfbs == 1 }"> 
					  	  配药工作量统计查询
					   </c:when>
						<c:otherwise> 
					   	  发药工作量统计查询
					   </c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<div data-options="region:'center',border:false">
			<div id="emp" style="width: 100%; height: 100%;">
				<table id="emptable" class="easyui-datagrid" style="width: 100%;">
					<thead>
						<tr>
							<c:choose>
								<c:when test="${pfbs == 1 }">
									<th
										data-options="field:'drugedOper',width:'16%',halign:'center',align:'right'">配药人</th>
									<th
										data-options="field:'drugedTerminal',width:'16%',halign:'center',align:'right'">配药台名称</th>
								</c:when>
								<c:otherwise>
									<th
										data-options="field:'drugedOper',width:'16%',halign:'center',align:'right'">发药人</th>
									<th
										data-options="field:'drugedTerminal',width:'16%',halign:'center',align:'right'">发药窗口名称</th>
								</c:otherwise>
							</c:choose>
							<th
								data-options="field:'recipeCount',width:'16%',halign:'center',align:'right'">处方数</th>
							<th
								data-options="field:'recipeQty',width:'16%',halign:'center',align:'right'">药品数量</th>
							<th
								data-options="field:'recipeCost',width:'16%',halign:'center',align:'right',formatter:changeCost">处方金额</th>
							<th
								data-options="field:'sumDays',width:'16%',halign:'center',align:'right'">剂数</th>
						</tr>
					</thead>
				</table>
			</div>
			<div id="deptTerminal" style="width: 100%; height: 100%;">
				<table id="depttable" class="easyui-datagrid" style="width: 100%;">
					<thead>
						<tr>
							<c:choose>
								<c:when test="${pfbs == 1 }">
									<th
										data-options="field:'drugedTerminal',width:'19%',halign:'center',align:'right'">配药台名称</th>
								</c:when>
								<c:otherwise>
									<th
										data-options="field:'drugedTerminal',width:'19%',halign:'center',align:'right'">发药窗口名称</th>
								</c:otherwise>
							</c:choose>
							<th
								data-options="field:'recipeCount',width:'19%',halign:'center',align:'right'">处方数</th>
							<th
								data-options="field:'recipeQty',width:'19%',halign:'center',align:'right'">药品数量</th>
							<th
								data-options="field:'recipeCost',width:'19%',halign:'center',align:'right',formatter:changeCost">处方金额</th>
							<th
								data-options="field:'sumDays',width:'19%',halign:'center',align:'right'">剂数</th>
						</tr>
					</thead>
				</table>
			</div>
			<div id="deptEmp" style="width: 100%; height: 100%;">
				<table id="dapeEmptable" class="easyui-datagrid"
					style="width: 100%;">
					<thead>
						<tr>
							<th
								data-options="field:'jobNo',width:'16%',halign:'center',align:'right'">工号</th>
							<c:choose>
								<c:when test="${pfbs == 1 }">
									<th
										data-options="field:'drugedOper',width:'16%',halign:'center',align:'right'">配药员</th>
								</c:when>
								<c:otherwise>
									<th
										data-options="field:'drugedOper',width:'16%',halign:'center',align:'right'">发药员</th>
								</c:otherwise>
							</c:choose>
							<th
								data-options="field:'recipeCount',width:'16%',halign:'center',align:'right'">处方数</th>
							<th
								data-options="field:'recipeQty',width:'16%',halign:'center',align:'right'">药品数量</th>
							<th
								data-options="field:'recipeCost',width:'16%',halign:'center',align:'right'">处方金额</th>
							<th
								data-options="field:'sumDays',width:'16%',halign:'center',align:'right'">剂数</th>
						</tr>
					</thead>
				</table>
			</div>
			<form id="saveForm" method="post"></form>
			<form method="post" id="reportToHiddenForm">
				<input type="hidden" name="beginDate" id="reportToLogin" value="" />
				<input type="hidden" name="endDate" id="reportToEnd" value="" /> <input
					type="hidden" name="rows" id="reportToRows" value="" /> <input
					type="hidden" name="typeView" id="reportTypeView" value="" /> <input
					type="hidden" name="typeValue" id="typeValue" value="" /> <input
					type="hidden" name="pfbs" id="reportPfbs" value="" /> <input
					type="hidden" name="fileName" id="reportToFileName" value="" />
			</form>

		</div>
	</div>
</body>
</html>
<script type="text/javascript">
	$(function() {
		var fontSize = $(".l-btn-text").css("fontSize")
		if($("body").width() < 1570  ){
			$("#scroll").addClass("div_scroll")
			if(fontSize == "14px"){
				$("#tabTitle").css("minWidth","1450px")
			}
			if(fontSize == "15px"){
				$("#tabTitle").css("minWidth","1500px")
			}
			if(fontSize == "16px"){
				$("#tabTitle").css("minWidth","1570px")
			}
		}
	});
</script>