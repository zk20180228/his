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
	var kindMap=new Map();//手术分类
	var dimensionNameArray =['全院手术人次','总手术次数','总手术费用',"同比","环比",'人均手术费','日均手术费',"药占比"];
	$(function(){
		$.ajax({
			url:'<%=basePath%>statistics/operationCost/treeOperationCostmap.action',
			success:function(datavalue){
				kindMap=datavalue;
			}
		});
		var dateArray =new Array();
		var date = "${dateString}";
		dateArray =date.split(',');
	    queryList(dateArray,1,"${dimensionString}","${dimensionValue}");
	});
	function queryList(dateArray,dateType,dimensionString,dimensionValue){
		var str='';
		var dimStringArray=dimensionString.split(',');
		var columnsArray=publicStatistics(dateArray,dimStringArray,dateType,dimensionNameArray);
		for(var i=0;i<dimStringArray.length;i+=2 ){
			if(str!=''){
				str +=',';
			}
			str += dimStringArray[i].split(',')[0];
		}
		$('#table1').datagrid({
			columns:columnsArray,
			multiSort:true,
			sortName:dimStringArray[0],
			remoteSort:false,
 			url:'<%=basePath%>statistics/operationCost/queryOperationCost.action', 
			queryParams:{'dateArray':dateArray,'dimStringArray':dimStringArray,'dateType':dateType,'dimensionValue':dimensionValue},
			onLoadSuccess:function(data){
				 if (data.rows.length > 0) {
		                mergeCellsOfValue("table1", str);
		            }
 			}
		});
	}
	function ops_kindfunction(value,row,index){
		if(value!=null&&value!=""){
			return kindMap[value];
		}
	}
	/**
	*打开维度选择弹窗
	*/
	function popWin(){
		var timerStr = Math.random();
		window.open ("<c:url value='/statistics/wdWin/WDOperationCostload.action?randomId='/>"+timerStr,'newwindow','height=350,width=700,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
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