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
	var sourseMap=null;
	$(function(){
		$.ajax({
			url:'<%=basePath%>statistics/biOutpatientWorkload/queryDeptForBiPublic.action',
			async:false,
			success:function(datavalue){
				deptMap=datavalue;
			}
		});
		$.ajax({
			url:'<%=basePath%>statistics/wdWin/inSourseMap.action',
			success:function(datavalue){
				sourseMap=datavalue;
			}
		}); 
		var dateArray = new Array();
		var date = "${dateString}";
	    dateArray =date.split(',');
		queryList(dateArray,1,"${dimensionString}","${dimensionValue}");
	});
	function queryList(dateArray,dateType,dimensionString,dimensionValue){
		var dimStringArray=dimensionString.split(',');//所选的维度的种类数组，即数组长度等于维度数量
		var dimensionNameArray =['人次','比例(%)'];
		
		var string="";
		for(var i=0;i<dimStringArray.length;i+=2){
			if(string!=''){
				string +=',';
			}
			
			string += dimStringArray[i].split(',')[0];
		}
		var columnsArray=publicStatistics(dateArray,dimStringArray,dateType,dimensionNameArray);
		$('#table1').datagrid({
			columns:columnsArray,
			multiSort:true,
			sortName:dimStringArray[0],
			remoteSort:false,
 			url:'<%=basePath%>statistics/peopleInHospital/queryPeopleInHospital.action', 
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
		window.open ("<c:url value='/statistics/wdWin/WDWinToPeopleInhospital.action?randomId='/>"+timerStr,'newwindow','height=370,width=700,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}
	function dept_codefunction(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	//入院来源
	function in_sourcefunction(value,row,index){
		if(value!=null&&value!=''){
			return sourseMap[value];
		}
	}
	//病危
	function critical_flagfunction(value,row,index){
	if(value==0){
		return '普通';
	}else if(value==1){
		return '病重';
	}else{
		return '病危';
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

