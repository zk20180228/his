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
<title>门诊充值消费明细统计</title>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
.easyui-dialog .panel-header .panel-tool a{
	background-color: red;	
}
</style>
<script type="text/javascript">
	var paywayMap=new Map();
	$.ajax({
		url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=payway",
		type:'post',
		success: function(data) {
			var waytype = data;
			for(var i=0;i<waytype.length;i++){
				paywayMap.put(waytype[i].encode,waytype[i].name);
			}
		}
	});
	var sexMap=new Map();
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
</script>
</head>
<body style="margin: 0px;padding: 0px;"> 
<div id="cc" class="easyui-layout" data-options="fit:true">   
	<div data-options="region:'north'" style="height:8%;min-height: 70px;border-top:0">
		<div style="margin:5px 5px 0px 5px;padding: 0px;">
			<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
			<a id="search" href="javascript:void(0)"  onclick="searchEvent(1)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" >查询</a> 
			<a id="reload" href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" data-options="iconCls:'reset'">重置</a> 
			<a href="javascript:report()" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511202'">打印</a>
			<a href="javascript:exportList();" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a>
			<a href="javascript:searchEvent(4);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">当年</a>
			<a href="javascript:searchEvent(5);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">当月</a>
			<a href="javascript:searchEvent(7);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">上月</a>
			<a href="javascript:searchEvent(0);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">十五天</a>
			<a href="javascript:searchEvent(3);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">七天</a>
			<a href="javascript:searchEvent(2);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">三天</a>
			<a href="javascript:searchEvent(6);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 12px">当天</a>
		</div>
		<div style="margin:6px 5px 0px 5px;padding: 0px;">
			<input id="medicalrecordId" class="easyui-textbox"  style="height:24px;margin-left: 5px;" data-options="iconCls:'icon-bullet_feed'" /><select id="typeClass" class="easyui-combobox" style="width:85px;height:24px;">   
				<option value="1" >身份证</option>   
				<option value="2">就诊卡</option>   
			</select>&nbsp;  
			<span>日期:</span>
			<input id="begintime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d-%H-%m-%s'})"  style="width:150px;height:24px;border: 1px solid #95b8e7;border-radius: 5px;"/>
			&nbsp;<span>至</span>&nbsp;
			<input id="endtime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d-%H-%m-%s'})"  style="width:150px;height:24px;border: 1px solid #95b8e7;border-radius: 5px;"/>
			
			<span style="margin-left: 100px;" align="center">姓名:</span>&nbsp;
			<span id="patientName"></span>&nbsp;&nbsp;
			<span style="margin-left: 50px;">病历号:</span>
			<span id="num"></span>
		</div>
	</div>   
	<div data-options="region:'center'">
		<div id="tt" class="easyui-tabs" fit="true" border="false">   
			<div title="门诊患者充值明细" id="1">   
				<table id="dataGrid1"></table>	 
			</div>   
			<div title="门诊患者消费明细" id="2">   
				<table id="dataGrid2"></table>	 
			</div> 
		</div> 
		<form id="reportForm" method="post">
			<input type="hidden" id="fileName" name="fileName">
			<input type="hidden" id="iPatientName" name="iPatientName">
			<input type="hidden" id="iMedicalrecordId" name="iMedicalrecordId">
			<input type="hidden" id="iBeginDate" name="iBeginDate">
			<input type="hidden" id="iEndDate" name="iEndDate">
			<input type="hidden" id="iIc" name="iIc">
			<input type="hidden" id="iIdCard" name="iIdCard">
			<input type="hidden" id="eMedicalrecordId" name="medicalrecordId">
			<input type="hidden" id="eBeginDate" name="beginDate">
			<input type="hidden" id="eEndDate" name="endDate">
			<input type="hidden" id="index" name="index">
		</form>
	</div>
</div>

<script type="text/javascript">
//定义一个事件（读卡）
function read_card_ic(){
	var card_value = app.read_ic();
	if(card_value=='0'||card_value==undefined||card_value==''){
		$.messager.alert('提示','此卡号['+card_value+']无效');
		setTimeout(function(){$(".messager-body").window('close')},3500);
		return;
	}
	$("#medicalrecordId").textbox('setValue',card_value);
	$("#typeClass").combobox("select",'2');	
	searchEvent(1); 
};
/*******************************结束读卡***********************************************/
function formatDate(now) { 
	var year=now.getFullYear(); 
	var month=now.getMonth()+1; 
	var date=now.getDate(); 
	var hour=now.getHours(); 
	var minute=now.getMinutes(); 
	var second=now.getSeconds(); 
	return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second; 
	} 
//开始时间
var today = new Date();
var yesterday_milliseconds = today.getTime()-1000*60*60*24;
var yesterday = new Date();
yesterday.setTime(yesterday_milliseconds); 
var endStartDate = formatDate(today);//当前时间为结束时间
var startDate = formatDate(yesterday);//默认设置一天前为开始时间
startDate=startDate.replace(/-(\d)\b/g,'-0$1');
startDate=startDate.replace(/:(\d)\b/g,':0$1');
endStartDate=endStartDate.replace(/-(\d)\b/g,'-0$1');
endStartDate=endStartDate.replace(/:(\d)\b/g,':0$1');
$('#begintime').val(startDate);
$('#endtime').val(endStartDate);
//科室map
var deptMap=null;
//人员map
var employeeMap=null;

funformat();
$(function(){
	dataGrid1();
	dataGrid2();
	
	bindEnterEvent('medicalrecordId',searchEvent,'easyui');
});


	
/***
 * 患者充值明细
 */
function dataGrid1(){
	$('#dataGrid1').datagrid({ 
		rownumbers:true,
		idField: 'id',
		striped:true,
		border:true,
		pageSize:20,
		pageNumber: 1,
		checkOnSelect:false,
		selectOnCheck:false,
		singleSelect:true,
		pagination:true,
		border:false,
		fit:true,
		url:'<%=basePath%>statistics/OutDetail/query.action',
		queryParams:{index:1},
		columns:[[	
			{field:'prepayCost',title:'充值金额',width:'13%',align:'right',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(2);}}},	
			{field:'prepayType',title:'充值方式',width:'13%',
				formatter:funPayTypeMap	
			},	
			{field:'createTime',title:'充值时间',width:'13%'},	
			{field:'createUser',title:'操作员',width:'13%',
				formatter:funEmployeeMap
			}	
		]] ,
		onBeforeLoad:function (param) {
			var medicalrecordId = $.trim($('#medicalrecordId').textbox('getValue'));
			if(medicalrecordId==""){
				$('#dataGrid1').datagrid('loadData',{total:0,rows:[]});
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
}

/***
 * 患者院内卡消费明细
 */
function dataGrid2(){
	$('#dataGrid2').datagrid({ 
		rownumbers:true,
		idField: 'id',
		striped:true,
		border:true,
		pageSize:20,
		pageNumber: 1,
		checkOnSelect:false,
		selectOnCheck:false,
		singleSelect:true,
		pagination:true,
		border:false,
		fit:true,
		url:'<%=basePath%>statistics/OutDetail/query.action',
		queryParams:{index:2},
		columns:[[
			{field:'money',title:'金额',width:'10%',align:'right',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(2);}}},	
			{field:'accountBalance',title:'余额',width:'10%',align:'right',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(2);}}},	
			{field:'deptCode',title:'科室',width:'10%',
				formatter:funDeptMap
			},	
			{field:'operDate',title:'操作时间',width:'10%'},	
			{field:'operCode',title:'操作人',width:'10%',
				formatter:funEmployeeMap
			}	
		]] ,
		onBeforeLoad:function (param) {
			var medicalrecordId = $.trim($('#medicalrecordId').textbox('getValue'));
			if(medicalrecordId==""){
				$('#dataGrid2').datagrid('loadData',{total:0,rows:[]});
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
}
/***
 * 查询
 */
function searchEvent(flag){
	var startTime;
	var endTime;
		if(flag==1){
			startTime = $('#begintime').val();
			endTime = $('#endtime').val();
		}else{
			var date
			if(flag<=3){
				date=new Date(new Date().getTime()-1000*3600*24);
				endTime=date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()+' 23:59:59';
			}else{
				date=new Date();
				endTime=date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()+' '+date.getHours()+':'+date.getMinutes()+':'+date.getSeconds();
			}
			if(flag==2){
				var lon=date.getTime()-1000*3600*24*3;
				startTime=new Date(lon);
				startTime=startTime.getFullYear()+'-'+(startTime.getMonth()+1)+'-'+startTime.getDate()+' 00:00:00'; 
			}else if(flag==3){
				var lon=date.getTime()-1000*3600*24*7;
				startTime=new Date(lon);
				startTime=startTime.getFullYear()+'-'+(startTime.getMonth()+1)+'-'+startTime.getDate()+' '+date.getHours()+':'+date.getMinutes()+':'+date.getSeconds();  
			}else if(flag==4){
				startTime=(date.getFullYear())+'-01-01 00:00:00';
			}else if(flag==5){
				startTime=(date.getFullYear())+'-'+(date.getMonth()+1)+'-01 00:00:00'
			}else if(flag==0){
				var lon=date.getTime()-1000*3600*24*15;
				startTime=new Date(lon);
				startTime=startTime.getFullYear()+'-'+(startTime.getMonth()+1)+'-'+startTime.getDate()+' 00:00:00'; 
			}else if(flag==7){//上月
				var lon=date.getTime();
			    var year=date.getFullYear();
			    var month=date.getMonth();
			    if(month==0){
			    	month=12;
			    	year=year-1;
			    }
			    startTime=year+'-'+month+'-01 00:00:00'
				var date=new Date(year,month,0);
			    endTime=year+'-'+month+'-'+date.getDate()+' 23:59:59'; 
			}else{
				startTime=endTime.split(' ')[0]+' 00:00:00';
			}
			startTime = startTime.replace(/-(\d{1})\b/g,'-0$1');
			startTime = startTime.replace(/:(\d{1})\b/g,':0$1');
			endTime = endTime.replace(/-(\d{1})\b/g,'-0$1');
			endTime = endTime.replace(/:(\d{1})\b/g,':0$1');
			$('#begintime').val(startTime);
			$('#endtime').val(endTime);
		}
	var medicalrecordId = $.trim($('#medicalrecordId').textbox('getValue'));
	if(medicalrecordId == '0' || medicalrecordId==undefined || medicalrecordId == ''){
		$.messager.alert('友情提示','所填信息不能为空!','warning');
		setTimeout(function(){$(".messager-body").window('close')},3500);
		return;
	}
	var type=$('#typeClass').combobox('getValue');
	var urlPath='<%=basePath%>statistics/OutDetail/cardQueryMedicalrecord.action';
	if(type=='1'){
		urlPath+='?idCard='+medicalrecordId;
	}else{
		urlPath+='?ic='+medicalrecordId;
	}
	$.ajax({
		url:urlPath,
		success:function(date){
			if(date){
				$.ajax({
					url:'<%=basePath%>statistics/OutDetail/querymedicalrecord.action',
					data:{"medicalrecordId":date},
					type:'post',
					success: function(data) {
						var patientobj = data;
						if(patientobj.length == 1){
							var span = document.getElementById("patientName");
							span.innerHTML='';
							if(span){
								if( patientobj[0].patientName){
									span.innerHTML = patientobj[0].patientName;
									$('#iPatientName').val(patientobj[0].patientName);
									$('#iMedicalrecordId').val(patientobj[0].medicalrecordId);
									$('#iBeginDate').val(startTime);
									$('#iEndDate').val(endTime);
									$('#eMedicalrecordId').val(patientobj[0].medicalrecordId);
									$('#eBeginDate').val(startTime);
									$('#eEndDate').val(endTime);
									$('#iIc').val(patientobj[0].cardNo);
									$('#iIdCard').val(patientobj[0].patientCertificatesno);
								}else{
									span.innerHTML ='名字无法正常显示';
								}
							}
							var num = document.getElementById("num");
							num.innerHTML='';
							if(patientobj[0].medicalrecordId){
								num.innerHTML = patientobj[0].medicalrecordId;
							}
							searchFrom(patientobj[0].medicalrecordId);
						}else{
							$.messager.alert('友情提示','无法查询到此患者!','warning');
							setTimeout(function(){$(".messager-body").window('close')},3500);
							return;
						}
					}
				});
			}else{
				$.messager.alert('友情提示','无法查询到此患者','warning');
				setTimeout(function(){$(".messager-body").window('close')},3500);
				return;
			}
		}
	});
}

/**
 * 导出列表
 */
function exportList() {
	var tab = $('#tt').tabs('getSelected');
	var index = tab.context.id;
	if(index == '1'){
		var rows = $('#dataGrid1').datagrid('getRows');
		if(rows.length > 0){
			$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
				if (res) {
					$('#index').val(index);
					$('#reportForm').form('submit', {
						url :"<%=basePath%>statistics/OutDetail/expOperactionlist.action",
						success : function(data) {
							$.messager.alert("操作提示", "导出成功！", "success");
						},
						error : function(data) {
							$.messager.alert("操作提示", "导出失败！", "error");
						}
					});
				}
			});
		}else{
			$.messager.alert('友情提示','当前统计无数据，无法导出！','warning');
			setTimeout(function(){$(".messager-body").window('close')},3500);
			return;
		}
	}else{
		var rows = $('#dataGrid1').datagrid('getRows');
		if(rows.length > 0){
			$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
				if (res) {
					$('#index').val(index);
					$('#reportForm').form('submit', {
						url :"<%=basePath%>statistics/OutDetail/expOperactionlist.action",
						success : function(data) {
							$.messager.alert("操作提示", "导出成功！", "success");
						},
						error : function(data) {
							$.messager.alert("操作提示", "导出失败！", "error");
						}
					});
				}
			});
		}else{
			$.messager.alert('友情提示','当前统计无数据，无法导出！','warning');
			setTimeout(function(){$(".messager-body").window('close')},3500);
			return;
		}
		
	}
}

function report() {
	var tab = $('#tt').tabs('getSelected');
	var index = tab.context.id;
	if(index == '1'){
		var rows = $('#dataGrid1').datagrid('getRows');
		if(rows.length > 0){
			$('#fileName').val('MZHZCZMXTJ');
			//表单提交 target
			var formTarget="reportForm";
			var tmpPath = "<%=basePath%>statistics/OutDetail/iReportMZHZCZMXTJ.action";
			//设置表单target
			$("#reportForm").attr("target",formTarget);
			//设置表单访问路径
			$("#reportForm").attr("action",tmpPath); 
			//表单提交时打开一个空的窗口
			$("#reportForm").submit(function(e){
				var timerStr = Math.random();
				window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');
				window.close();
			});
			$("#reportForm").submit();
		}else{
			$.messager.alert('友情提示','当前统计无数据，无法打印！','warning');
			setTimeout(function(){$(".messager-body").window('close')},3500);
			return;
		}
	}else{
		var rows = $('#dataGrid1').datagrid('getRows');
		if(rows.length > 0){
			$('#fileName').val('MZHZXFMXTJ');
			//表单提交 target
			var formTarget="reportForm";
			var tmpPath = "<%=basePath%>statistics/OutDetail/iReportMZHZXFMXTJ.action";
			//设置表单target
			$("#reportForm").attr("target",formTarget);
			//设置表单访问路径
			$("#reportForm").attr("action",tmpPath); 
			//表单提交时打开一个空的窗口
			$("#reportForm").submit(function(e){
				var timerStr = Math.random();
				window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');
				window.close();
			});
			$("#reportForm").submit();
		}else{
			$.messager.alert('友情提示','当前统计无数据，无法打印！','warning');
			setTimeout(function(){$(".messager-body").window('close')},3500);
			return;
		}
		
	}
}


function searchFrom(blh){
	var beginDate = $('#begintime').val();
	var endDate = $('#endtime').val();
	$('#dataGrid1').datagrid('load',{
		index: '1',
		medicalrecordId : blh,
		beginDate : beginDate,
		endDate : endDate
	});
	$('#dataGrid2').datagrid('load',{
		index: '2',
		medicalrecordId : blh,
		beginDate : beginDate,
		endDate : endDate
	});

}

//渲染支付方式
function funPayTypeMap(value,row,index){
	if(value){
		return paywayMap.get(value);
	}
}

//渲染科室Map
function funDeptMap(value,row,index){
	if(value!=null&&value!=''){
		return deptMap[value];
	}
}

//渲染人员map
function funEmployeeMap(value,row,index){
	if(value!=null&&value!=''){
		return employeeMap[value];
	}
}

<%----------------------------------------  渲染		-------------------------------------------------------------------------------------------------------%>
function funformat(){
	//科室
	$.ajax({
		url:'<%=basePath%>statistics/OperationCost/getDeptMap.action',
		type:'post',
		success: function(data) {
			deptMap =  data ;
		}
	});
	//人员
	$.ajax({
		url:'<%=basePath%>baseinfo/employee/getEmplMap.action',
		type:'post',
		success: function(data) {
			employeeMap = data;
		}
	});
}

// 药品列表查询重置
function searchReload() {
	$('#medicalrecordId').textbox('setValue','');
	$('#begintime').val(startDate);
	$('#endtime').val(endStartDate);
	$('#dataGrid1').datagrid('loadData',[]);
	$('#dataGrid2').datagrid('loadData',[]);
	$('#reportForm').form('clear');
	var span = document.getElementById("patientName");
	if(span){
		span.innerHTML = '';
	}
	var num = document.getElementById("num");
	num.innerHTML='';
}
</script>
</body>
</html>