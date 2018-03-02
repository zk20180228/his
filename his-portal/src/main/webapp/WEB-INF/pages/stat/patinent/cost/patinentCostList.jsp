<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>病人费用汇总查询</title>
<script type="text/javascript">

/**
*科室Map
**/
var deptMap=null;
/**
*员工Map
*/
var empMap=null;

//查询住院科室（渲染）
$.ajax({
	url:'<%=basePath%>statistics/ChargeBill/queryZYDept.action',
	success:function(data){
		deptMap=data;
	}
});
//查询员工（渲染）
$.ajax({
	url:'<%=basePath%>statistics/ChargeBill/queryEmpbill.action',
	success:function(data){
		empMap=data;
	}
});

/**
*执行科室渲染
**/
function formatdeptId(value,row,index){
	if(value!=null&&value!=''){
		return deptMap[value];
	}
}
/**
*收费人渲染
**/
function fromaterempId(value,row,index){
	if(value!=null&&value!=''){
		return empMap[value];
	}
}
//重置
function reset(){
	$("#startData").val("${firstData }");
    $("#endData").val("${endData }");
    $("#idCard").textbox("clear");
    searchFrom();
}
$(function(){
	$("#startData").val("${firstData }");
    $("#endData").val("${endData }");
    bindEnterEvent('idCard', idCard, 'easyui');
	//查询人员map
	$.ajax({
		url:'<%=basePath%>baseinfo/employee/getEmplMap.action',
		async:false,
		success:function(datavalue){
			empMap=datavalue;
		}
	});	
	
	var firstData=$("#startData").val();
	var endData=$("#endData").val();
	$("#list").datagrid({
		pagination:true,
		pageSize:20,
		pageList:[20,30,50,80,100],
		fitColumns:true,
		singleSelect:true,
		border:true,
		fit:true,
		rownumbers:true,
		checkOnSelect:true,
		selectOnCheck:false,
		url:"<%=basePath%>statistics/CostStatistics/queryPatientCostStatistice.action",
		queryParams:{firstData:firstData,endData:endData,inpatientNo:null},
		onLoadSuccess:function(data){
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
	
});
	
	function idCard(){
		var idCard=$('#idCard').textbox('getValue');
		if(idCard!=null&&idCard!=""){
			$.ajax({
				url:'<%=basePath%>statistics/CostStatistics/queryInpatientInfo.action',
				data:{idCard:idCard},
				success:function(data){
					plist=data;
					if(plist.length>1){
						$("#InpatientMes").window('open');
						$("#infoDatagridMes").datagrid({
							data:plist,
							    columns:[[  
									{field:'ck',checkbox : true} ,  
							        {field:'medicalrecordId',title:'病历号',width:'25%',align:'center'} ,  
							        {field:'inpatientNo',title:'住院流水号',width:'25%',align:'center'},
							        {field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
							        {field:'inDate',title:'入院时间',width:'30%',align:'center'}
							    ]] ,
							    onClickRow:function(index,row){
							    	$("#inpatientNo").val(row.inpatientNo);
							    	$('#InpatientMes').window('close');
							    	searchFrom();
							    }
							});
					  }else if(plist.length==1){
						  $("#inpatientNo").val(plist[0].inpatientNo);
						  searchFrom();
					  }else{
						  $.messager.alert('提示',"该时间段内该患者没有住院信息");
					  }
				}
			});
		}else{
			//searchFrom();
			$.messager.alert('提示',"请输入病历号");
		}
	}
	
	function findMesIn(){
			var data=$("#infoDatagridMes").datagrid('getSelections')
			var inpatientNo='';
			if(data.length>0){
				$("#InpatientMes").window('close');
				$('#name').text(data[0].patientName);//患者姓名
				for(var i=0;i<data.length;i++){
					if(i<data.length-1){
						inpatientNo+="'"+data[i].inpatientNo+"',";
					}else{
						inpatientNo+="'"+data[i].inpatientNo+"'";
					}
				}
			}else{
				 $.messager.alert('提示',"至少选择一条数据");
			}
			
			$("#inpatientNo").val(inpatientNo);
			searchFrom();
	}
	
	
	//查询
	function searchFrom(){
		var firstData=$("#startData").val();
		var endData=$("#endData").val();
		var idCard=$('#idCard').textbox('getValue');
		if(firstData&&endData){
		    if(firstData>endData){
		      $.messager.alert("提示","开始时间不能大于结束时间！");
		      close_alert();
		      return ;
		    }
		  }
		if(idCard==null||idCard==""){
		      var inpatientNo=null;			
		}else{
			 var inpatientNo=$("#inpatientNo").val();
		}
		 $("#list").datagrid(
			'reload',{
			 firstData:firstData,
			 endData:endData,
			 inpatientNo:inpatientNo
		}) 
	}
	//导出
	function exportDown(){
		var firstData=$("#startData").val();
		var endData=$("#endData").val();
		var inpatientNo=$("#inpatientNo").val();
		var rows = $("#list").datagrid('getRows');
		if(rows.length==0){
			$.messager.alert('提示',"列表无数据，无法导出！");
			close_alert();
			return;
		}
		$.messager.confirm('提示:','确认要导出吗?',function(event){ 
			if(event){ 
				$('#exportForm').form('submit', {
					url:"<%=basePath%>statistics/CostStatistics/expPatientCostStatistice.action",
					queryParams:{firstData:firstData,endData:endData,inpatientNo:inpatientNo},
					success : function(data) {
						$.messager.alert("操作提示", "导出成功！", "success");
					},
					error : function(data) {
						$.messager.alert("操作提示", "导出失败！", "error");
					}
				});
			} 
		});
	}
	//打印方法
	function experStamp() {
		var firstTime=$("#startData").val();
		var endTime=$("#endData").val();
		var inpatientNo=$("#inpatientNo").val();
		var rows = $('#list').datagrid('getRows');
		if(rows==null||rows==""){
			$.messager.alert("提示", "列表无数据,无法打印！");
			return;
		}
		$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
			if (res) {
				window.open ("<%=basePath%>statistics/CostStatistics/printPatientCostStatistice.action?fileName=brfyhzcx&firstData="+firstTime+"&endData="+endTime+"&inpatientNo="+inpatientNo,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
				}
			});
	}
	function empFunction(value,row,index){
		if(value!=null&&value!=""){
			return empMap[value];
		}
	}
	//按时间段查询
	function queryMidday(val){
		if(val==1){
			var myDate = new Date();
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			var day=year+"-"+month+"-"+date;
			var Stime = $('#startData').val(day);
		    var Etime = $('#endData').val(day);
		    idCard();
		}else if(val==2){
			var nowd = new Date();
			var myDate=new Date(nowd.getTime() - 3 * 24 * 3600 * 1000);
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			var day2=year+"-"+month+"-"+date;
			var Stime = $('#startData').val(day2);
			 nowd = new Date();
			 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
			 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			 date=myDate.getDate();
		  	 month=month<10?"0"+month:month;
			 date=date<10?"0"+date:date;
			 day2=year+"-"+month+"-"+date;
		    $('#endData').val(day2);
		    idCard();
		}else if(val==3){
			var nowd = new Date();
			var myDate=new Date(nowd.getTime() - 7 * 24 * 3600 * 1000);
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			var day3=year+"-"+month+"-"+date;
			var Stime = $('#startData').val(day3);
			 nowd = new Date();
			 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
			 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			 date=myDate.getDate();
		  	month=month<10?"0"+month:month;
			 date=date<10?"0"+date:date;
			 day3=year+"-"+month+"-"+date;
		    $('#endData').val(day3);
		    idCard();
		}else if(val==4){
			var myDate = new Date();
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			   month=month<10?"0"+month:month;
			var day=year+"-"+month+"-"+"01";
			var Stime = $('#startData').val(day);
			 myDate = new Date();
			 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			 date=myDate.getDate();
			 month=month<10?"0"+month:month;
			 date=date<10?"0"+date:date;
			 day=year+"-"+month+"-"+date;
		     $('#endData').val(day);
		    idCard();
		}else if(val==5){
			var myDate = new Date();
			var year=myDate.getFullYear();
			var day=year+"-"+"01"+"-"+"01";
			var Stime = $('#startData').val(day);
			 myDate = new Date();
			 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			 date=myDate.getDate();
			 month=month<10?"0"+month:month;
			 date=date<10?"0"+date:date;
			 day=year+"-"+month+"-"+date;
		     $('#endData').val(day);
		    idCard();
		}else if(val==6){
			var nowd = new Date();
			var myDate=new Date(nowd.getTime() - 15 * 24 * 3600 * 1000);
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			var day3=year+"-"+month+"-"+date;
			var Stime = $('#startData').val(day3);
			 nowd = new Date();
			 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
			 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			 date=myDate.getDate();
		  	month=month<10?"0"+month:month;
			 date=date<10?"0"+date:date;
			 day3=year+"-"+month+"-"+date;
		    $('#endData').val(day3);
		    idCard();
		}else if(val==7){
			var date=new Date();
			var year=date.getFullYear();
			var month=date.getMonth();
			if(month==0){
				year=year-1;
				month=12;
			}
			var startTime=year+'-'+month+'-01';
			 $('#startData').val(startTime);
			var date=new Date(year,month,0);
			var endTime=year+'-'+month+'-'+date.getDate();
			$('#endData').val(endTime);
			  idCard();
		}
		
		
	}
</script>
</head>
<body>
<div id="cc" class="easyui-layout" data-options="fit:true">   
	<div data-options="region:'north',border:false"style="height: 40px;padding: 7px 5px 5px 5px;">
		<input type="hidden" id="inpatientNo">
		日期：
		<input id="startData" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		至
		<input id="endData" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		&nbsp;&nbsp;病历号：<input class="easyui-textbox" id="idCard">
		<a href="javascript:void(0)" onclick="idCard()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
		<a href="javascript:void(0)" onclick="reset()" class="easyui-linkbutton" iconCls="reset">重置</a>
		<a href="javascript:void(0)" onclick="experStamp()" class="easyui-linkbutton" iconCls="icon-printer">打印</a>
		<a href="javascript:void(0)" onclick="exportDown()" class="easyui-linkbutton" iconCls="icon-down">导出</a>
		<a href="javascript:void(0)" onclick="queryMidday(5)" class="easyui-linkbutton" style="float: right;margin-left: 9px" iconCls="icon-date">当年</a>
		<a href="javascript:void(0)" onclick="queryMidday(4)" class="easyui-linkbutton" style="float: right;margin-left: 9px" iconCls="icon-date">当月</a>
		<a href="javascript:void(0)" onclick="queryMidday(7)" class="easyui-linkbutton" style="float: right;margin-left: 9px" iconCls="icon-date">上月</a>
		<a href="javascript:void(0)" onclick="queryMidday(6)" class="easyui-linkbutton" style="float: right;margin-left: 9px" iconCls="icon-date">十五天</a>
		<a href="javascript:void(0)" onclick="queryMidday(3)" class="easyui-linkbutton" style="float: right;margin-left: 9px" iconCls="icon-date">七天</a>
		<a href="javascript:void(0)" onclick="queryMidday(2)" class="easyui-linkbutton" style="float: right;margin-left: 9px" iconCls="icon-date">三天</a>
		<a href="javascript:void(0)" onclick="queryMidday(1)" class="easyui-linkbutton" style="float: right;margin-left: 9px" iconCls="icon-date">当天</a>
	</div>
	<div data-options="region:'center',border:false" style="height:95%" align="center">
		<table id="list"  class="easyui-datagrid" >
			<thead>
				<tr>
					<th data-options="field:'inpatientNo',width:'10%'">住院流水号</th>
					<th data-options="field:'name',width:'10%'">姓名</th>
					<th data-options="field:'feeCode',width:'10%'">项目类别</th>
					<th data-options="field:'totCost',width:'10%'">合计金额</th>
					<th data-options="field:'recipeDeptcode',width:'10%',formatter:formatdeptId">开立科室</th>
					<th data-options="field:'recipeDoccode',width:'10%',formatter:empFunction">开立医生</th>
					<th data-options="field:'executeDeptcode',width:'10%',formatter:formatdeptId">记账科室</th>
					<th data-options="field:'feeOpercode',formatter:empFunction,width:'10%'">收费员</th>
					<th data-options="field:'feeDate',width:'10%'">收费日期</th>
				</tr>
			</thead>
		</table>
		<form id="exportForm"/>
	</div>
</div>
<div id="InpatientMes" class="easyui-dialog" title="选择" style="width:650;height:600;padding:10" data-options="modal:true, closed:true">   
	 	<a href="javascript:void(0)" onclick="findMesIn()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
	 	<br></br>
	   <table id="infoDatagridMes" class="easyui-datagrid" data-options="fitColumns:true,checkOnSelect:true,fit:true"></table>
	</div>
</body>
</html>