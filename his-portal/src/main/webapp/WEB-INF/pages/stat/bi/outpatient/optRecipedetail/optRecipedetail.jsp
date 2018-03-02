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
var docMap=null;
$(function(){
	$.ajax({
		url:'<%=basePath%>statistics/biOutpatientWorkload/queryDeptForBiPublic.action',
		success:function(datavalue){
			deptMap=datavalue;
		}
	});
	$.ajax({
		url:'<%=basePath%>statistics/wdWin/queryDocForBiPublic.action',
		success:function(datavalue){
			docMap=datavalue;
		}
	});
	
	
 	var dateArray = new Array();
 	var date = "${dateString}";
     dateArray =date.split(',');
 	queryList(dateArray,1,"${dimensionString}","${dimensionValue}");
	 });


function optWin(){
	var timerStr = Math.random();
	window.open ("<c:url value='/statistics/wdWin/WDWinTooptRecipedetail.action?randomId='/>"+timerStr,'newwindow','height=350,width=700,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
}
function queryList(dateArray,dateType,dimensionString,dimensionValue){
	var dimStringArray=dimensionString.split(',');
	
	var dimensionNameArray =['处方量','处方金额','同比','环比'];
	
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
		url:'<%=basePath%>statistics/optRecipedetail/queryOptRecipeDatagrid.action', 
		queryParams:{'dateArray':dateArray,'dimStringArray':dimStringArray,'dateType':dateType,'dimensionValue':dimensionValue},
		onLoadSuccess:function(data){
			 if (data.rows.length > 0) {
	                mergeCellsOfValue("table1", string);
	            }
			 getRowSum();
			 
			}
	});
}
function reg_dept_codefunction(value,row,index){
	if(value!=null&&value!='' && value.indexOf(":")==-1){
		return deptMap[value];
	}else{
		return value;
	}
}
function doct_codefunction(value,row,index){
 	if(value!=null&&value!=''){
 		return docMap[value];
 	}
}
function dept_codefunction(value,row,index){
	if(value!=null&&value!='' && value.indexOf(":")==-1){
		return deptMap[value];
	}else{
		return value;
	}
}
	
	
	
function getRowSum(){
	 var colName='';
	 var sum=0;
	 var field='{reg_dept_code:'+"'总计:'";
	 var fields = $('#table1').datagrid('getColumnFields'); 
	 
	 fields.shift();
	
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

	    	 }
	    	 else{
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
</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" style="width: 100%;height: 100%;">
		<div data-options="region:'north',border:false" style="width: 100%;height: 30px;padding:8px;">
				<a href="javascript:optWin();" style="margin:0px 30px 0px 0px;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">维度选择</a>
			</div>
			<table id="table1" class="easyui-datagrid" data-options="fitColumns:true"></table>
	</div>
</body>
</html>