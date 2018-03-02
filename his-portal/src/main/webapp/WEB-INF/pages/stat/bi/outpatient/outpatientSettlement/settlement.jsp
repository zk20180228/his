<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
var deptMap=null;
var feeMap=null;
$(function(){
	$.ajax({
		url:'<%=basePath%>statistics/biOutpatientWorkload/queryDeptForBiPublic.action',
		async:false,
		success:function(datavalue){
			deptMap=datavalue;
		}
	});
	$.ajax({
		url:'<%=basePath%>bi/project/casminfeeFunction.action',
		async:false,
		success:function(datavalue){
			feeMap=datavalue;
		}
	});
	var dateArray = new Array();
	var date = "${dateString}";
    dateArray =date.split(',');
	queryList(dateArray,1,"${dimensionString}","${dimensionValue}");
});

function dept_codefunction(value,row,index){
	if(value!=null&&value!=''){
		return deptMap[value];
	}
}
function invo_codefunction(value,row,index){
	if(value!=null&&value!=''){
		return feeMap[value];
	}
}

function popWin(){
	var timerStr = Math.random();
	window.open ("<c:url value='/statistics/wdWin/WDWinToSettlement.action?randomId='/>"+timerStr,'newwindow','height=350,width=700,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
}

function queryList(datevo,dateType,dimensionString,dimensionValue){
	var dimensionNameArray =['费用（元）','比例'];
    var dimStringArray=dimensionString.split(',');//所选的维度的种类数组，即数组长度等于维度数量
    var string = "";
	for(var i=0;i<dimStringArray.length;i++ ){
		if(string!=''){
			string +=',';
		}
		string += dimStringArray[i].split(',')[0];
	}
	
	var columnsArray=publicStatistics(datevo,dimStringArray,dateType,dimensionNameArray);
	$('#table1').datagrid({
		columns:columnsArray,
// 		multiSort:true,
// 		sortName:dimStringArray[0],
// 		remoteSort:false,
		url:'<%=basePath%>bi/settlement/querytSettlementloadDatagrid.action', 
		queryParams:{'datevo.year1':datevo[0],'datevo.year2':datevo[1],'datevo.quarter1':datevo[2],'datevo.quarter2':datevo[3],'datevo.month1':datevo[4],'datevo.month2':datevo[5],'datevo.day1':datevo[6],'datevo.day2':datevo[7],'dimensionString':dimensionString,'dateType':dateType,'dimensionValue':dimensionValue},
		onLoadSuccess:function(data){
			 if (data.rows.length > 0) {
	                mergeCellsOfValue("table1", string);
	            }

			}
	});
}
</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" style="width: 100%;height:50px;">
		<a href="javascript:popWin();" style="margin:0px 30px 0px 0px;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">维度选择</a>
	</div>
	<div data-options="region:'center',border:false" style="width: 100%;">
		<table id="table1" data-options="fit:true"></table>
	</div>
	<div id="deptChoseDiv" class="easyui-dialog" title="患者选择" style="width:500;height:500;padding:5px" data-options="modal:true, closed:true">   
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				科室名称：<input class="easyui-textbox"/>
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryList()" data-options="iconCls:'icon-search'" style="margin:0px 0px 0px 50px" >查&nbsp;询&nbsp;</a>
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="Chosefinish()" data-options="iconCls:'icon-search'" style="margin:0px 0px 0px 50px" >确&nbsp;定&nbsp;</a>
			</div>
			<div data-options="region:'center',border:false"></div>
		</div>
		<table id="infoDatagrid" class="easyui-datagrid" data-options="fitColumns:true"></table>
	</div>
	 <input type="text" id="returnVal" value="">
</body>
</html>