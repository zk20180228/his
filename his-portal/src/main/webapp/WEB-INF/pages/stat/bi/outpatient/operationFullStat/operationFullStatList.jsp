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
<title>全院手术次数统计</title>
<script type="text/javascript">
	var deptMap=null;
	var opKindMap=null;
	var docMap=null;
	var opNameMap=null;
	$(function(){
		
		$.ajax({
			url:'<%=basePath%>statistics/biOutpatientWorkload/queryDeptForBiPublic.action',
			async:false,
			success:function(datavalue){
				deptMap=datavalue;
			}
		});
		$.ajax({
			url:'<%=basePath%>statistics/bioperationNum/queryOperatetypeMap.action',
			success:function(datavalue){
				opKindMap=datavalue;
			}
		});
		$.ajax({
			url:'<%=basePath%>operation/operationList/ssComboboxMap.action',
			success:function(datavalue){
				docMap=datavalue;
			}
		});
		$.ajax({
			url:'<%=basePath%>operation/operationList/undrugComboboxMap.action',
			success:function(datavalue){
				opNameMap=datavalue;
			}
		});
		var dateArray = new Array();
		var date = "${dateString}";
		dateArray = date.split(',');
		queryList(dateArray,1,"${dimensionString}","${dimensionValue}");
	});
	function queryList(dateArray,dateType,dimensionString,dimensionValue){
		var dimStringArray=dimensionString.split(',');
		var dimensionNameArray =['人次(次)','涨跌幅(%)'];
		
		var columnsArray=publicStatistics(dateArray,dimStringArray,dateType,dimensionNameArray);
		var string="";
		for(var i=0;i<dimStringArray.length;i++ ){
			if(string!=''){
				string +=',';
			}
			string += dimStringArray[i].split(',')[0];
		}
		$('#table1').datagrid({
			columns:columnsArray,
 			url:'<%=basePath%>statistics/biOperationFullStat/queryOperationFullStat.action', 
			queryParams:{'dateArray':dateArray,'dimStringArray':dimStringArray,'dateType':dateType,'dimensionValue':dimensionValue},
			onLoadSuccess:function(data){
				 if (data.rows.length > 0) {
		                mergeCellsOfValue("table1", string);
		            }
				 //添加合计行
				 var colName='';
				 var sum=0;
				 var field='{"'+dimStringArray[0]+'":'+"'合计:'"+',colspan:'+dimStringArray.length*0.5+'';
				 //得到列表中所有列的列名
				 var fields = $('#table1').datagrid('getColumnFields'); 
				 //循环维度种类数组，删除列名数组中维度列的列名
				 for(var i=0;i<dimStringArray.length*0.5;i++){
				 	fields.shift();
				 }
			 	 var rows = $('#table1').datagrid('getRows')//获取当前的数据行
			    for (var i = 0; i < fields.length;i++) {
			   	 var totSum=0;
					 colName=fields[i];
				     for (var j = 0; j < rows.length; j++) {
				    	 if(typeof(rows[j][colName])=="number"){
				    		 totSum +=rows[j][colName];
				    	 }else if(typeof(rows[j][colName])=="undefind"||typeof(rows[j][colName])=="NaN"){
							 rows[j][colName]=0;
							 totSum += 0;
				    	 }else{
				             totSum += 0;
				    	 }
				     }
				     sum+=totSum;
				     field=field+','+colName+':'+totSum;
				}
				 field+='}';
				 field = eval("(" + field + ")");
			    //新增一行显示统计信息
			    $('#table1').datagrid('appendRow',field);
 			}
		});
	}
	/**
	*打开维度选择弹窗
	*/
	function popWin(){
		var timerStr = Math.random();
		window.open ("<c:url value='/statistics/wdWin/WDWinToOperaFullStat.action?randomId='/>"+timerStr,'newwindow','height=370,width=700,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}
	
	function exec_deptfunction(value,row,index){
		if(value!=null&&value!='' && value.indexOf(":")==-1){
			return deptMap[value];
		}else{
			return value;
		}
	}
	function ops_kindfunction(value,row,index){
		if(value!=null&&value!='' && value.indexOf(":")==-1){
			return opKindMap[value];
		}else{
			return value;
		}
	}
	function ops_docdfunction(value,row,index){
		if(value!=null&&value!='' && value.indexOf(":")==-1){
			return docMap[value];
		}else{
			return value;
		}
	}
	function operation_idfunction(value,row,index){
		if(value!=null&&value!='' && value.indexOf(":")==-1){
			return opNameMap[value];
		}else{
			return value;
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