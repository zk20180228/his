<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	</head>
	<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false,fit:true">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="width: 100%;height: 50px;padding:8px;">
			   <a href="javascript:popWin();" style="margin:0px 30px 0px 0px;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">维度选择</a>
			</div>
			<div data-options="region:'center',border:false" style="width: 100%;">
				<table id="table1" data-options="fit:true"></table>
			</div>
		</div>
	</div>
</body>
	<script type="text/javascript">
	var dimensionNameArray =['费用金额','费用比例(%)','同比(%)'];
	var string="";
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
			url:'<%=basePath%>statistics/expensesAnaly/casminfeeComXr.action',
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
	function inhos_deptcodefunction(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	function fee_codefunction(value,row,index){
		if(value!=null&&value!=''){
			return feeMap[value];
		}
	}
	function popWin(){
		var timerStr = Math.random();
		window.open ("<%=basePath%>statistics/wdWin/WDWinToExpensesAnaly.action?flag=2",'newwindow','height=350,width=700,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}
	
	function queryList(dateVo,dateType,dimensionString,dimensionValue,flag){
		var dimStringArray=dimensionString.split(',');
		var columnsArray=publicStatistics(dateVo,dimStringArray,dateType,dimensionNameArray);
		var string="";
		for(var i=0;i<dimStringArray.length;i+=2 ){
			if(string!=''){
				string +=',';
			}
			string += dimStringArray[i].split(',')[0];
		}
		$('#table1').datagrid({
			columns:columnsArray,
			multiSort:true,
			sortName:dimStringArray[0],
			remoteSort:false,
 			url:'<%=basePath%>statistics/settlementAnalysis/settlementAnalysisDatagrid.action', 
			queryParams:{'dateArray':dateVo,'dimensionString':dimensionString,'dateType':dateType,'dimensionValue':dimensionValue},
			onLoadSuccess:function(data){
				 if (data.rows.length > 0) {
		                mergeCellsOfValue("table1", string);
		            }
 			}
		});
	}
	</script>
</html>