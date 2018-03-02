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
	var deptMap=new Map();
	var regMap=new Map();
	var feeMap=new Map();
	var dimensionNameArray =['门诊人次','挂号费用','挂号费占比','医疗费用','医疗占比','药品费用','药品占比','合计','门诊人次费用'];
// 	var dimensionString="dept_code,科室,age,年龄";
// 	var dimStringArray=dimensionString.split(',');
// 	var dimensionValue="dept_code,8030,8131,8751?DOCT_CODE,555,222,111?YNBOOK,777,1,2?age,1-2月,3-4月";
// 	var dimValueArray=dimensionValue.split('?');
// 	var dateType=4;//所选择的时间跨度的等级标识（1-按年统计;2-按季统计;3-按月统计,4-按日统计）
//  var dateArray=new Array();//数组元素为年、季度、月、日的值
	$(function(){
		//查询科室map
		$.ajax({
			url:"<%=basePath%>statistics/register/queryDeptForBiPublic.action",
			async:false,
			success:function(datavalue){
					deptMap=datavalue;
			}
		});
		//查询医生职级map
		$.ajax({
			url:'<%=basePath%>statistics/wdWin/queryregcode.action',
			success:function(datavalue){
					regMap=datavalue;
			}
		});
		//费用
		$.ajax({
			url:"<%=basePath%>statistics/wdWin/queryFeecodecom.action",
			success:function(datavalue){
				var datavalue=eval("("+datavalue+")");
				for(var i=0;i<datavalue.length;i++){
					feeMap.put(datavalue[i].encode,datavalue[i].name);
				}
			}
		});
		var dateArray = new Array();
		var date = "${dateString}";
	    dateArray =date.split(',');
	    queryList(dateArray,1,"${dimensionString}","${dimensionValue}");
	});
	function queryList(dateArray,dateType,dimensionString,dimensionValue){
		var dimStringArray=dimensionString.split(',');
		var columnsArray=publicStatistics(dateArray,dimStringArray,dateType,dimensionNameArray);
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
 			url:'<%=basePath%>statistics/Outpatientcost/getOutpatientcostlist.action', 
			queryParams:{'dateArray':dateArray,'dimStringArray':dimStringArray,'dateType':dateType,'dimensionValue':dimensionValue},
			onLoadSuccess:function(data){
				 if (data.rows.length > 0) {
		                mergeCellsOfValue("table1", string);
		         }
 			}
		});
	}
	/**
	*打开维度选择弹窗
	*/
	function popWin(){
		var timerStr = Math.random();
		window.open ("<c:url value='/statistics/wdWin/WDWinToOutpatientCostloadcost.action?randomId='/>"+timerStr,'newwindow','height=350,width=700,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}
	function dept_codefunction(value,row,index){
		if(value!=null&&value!=""){
			return deptMap[value];
		}
	}
	function reglevl_codefunction(value,row,index){
		if(value!=null&&value!=''){
			return regMap[value];
		}
	}
	function fee_stat_codefunction(value,row,index){
		if(value!=null&&value!=''){
			return feeMap[value];
		}
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="width: 100%;">
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
</html>