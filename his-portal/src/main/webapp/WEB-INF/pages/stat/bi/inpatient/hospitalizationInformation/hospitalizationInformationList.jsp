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
	var statusMap=null;
	var sourceMap=null;
	var homeMap=null;
	$(function(){
		$.ajax({
			url:'<%=basePath%>statistics/biOutpatientWorkload/queryDeptForBiPublic.action',
			async:false,
			success:function(datavalue){
				deptMap=datavalue;
			}
		});
		$.ajax({
			url:'<%=basePath%>statistics/wdWin/statusMap.action',
			success:function(datavalue){
				statusMap=datavalue;
			}
		});
		$.ajax({
			url:'<%=basePath%>statistics/wdWin/inSourseMap.action',
			success:function(datavalue){
				sourceMap=datavalue;
			}
		});
		$.ajax({
			url:'<%=basePath%>statistics/biHospitalizationInformation/homeMap.action',
			success:function(datavalue){
				homeMap=datavalue;
			}
		});
		var dateArray = new Array();
		var date = "${dateString}";
		dateArray=date.split(',');
		queryList(dateArray,1,"${dimensionString}","${dimensionValue}");
	});
	function queryList(dateArray,dateType,dimensionString,dimensionValue){
		var dimStringArray=dimensionString.split(',');
		var dimensionNameArray =['人次'];
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
			sortName:dimStringArray[0],
			multiSort:true,
			remoteSort:false,
			url:'<%=basePath%>statistics/biHospitalizationInformation/querytDatagrid.action',
			queryParams:{'dateArray':dateArray,'dimStringArray':dimStringArray,'dateType':dateType,'dimensionValue':dimensionValue},
			onLoadSuccess:function(data){
				 if (data.rows.length > 0) { 
		                mergeCellsOfValue("table1", string);
		            }
		}
		});
		}
	
		function popWin(){
			var timerStr = Math.random();
			window.open ("<c:url value='/statistics/wdWin/wdWinToInformation.action?randomId='/>"+timerStr,'newwindow','height=500,width=800,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		}
		function dept_codefunction(value,row,index){
			if(value!=null&&value!=''){
				return deptMap[value];
			}
		}
		function homefunction(value,row,index){
			if(value!=null&&value!=''){
				return homeMap[value];
			}
		}		
		function in_sourcefunction(value,row,index){
			if(value!=null&&value!=''){
				return sourceMap[value];
			}
		}
		function statusfunction(value,row,index){
			if(value!=null&&value!=''){
				return statusMap[value];
			}
		}
		function passengersfunction(value,row,index){
			if(value!=null&&value!=''){
				return value;
			}
		}
		function agefunction(value,row,index){
			if(value!=null&&value!=''){
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