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
	var reglevlMap=null;
	$(function(){
		$.ajax({
			url:'<%=basePath%>statistics/register/queryDeptForBiPublic.action',
			async:false,
			success:function(datavalue){
				deptMap=datavalue;
			}
		});
		$.ajax({
			url:'<%=basePath%>statistics/register/queryreglevlForBiPublic.action',
			success:function(datavalue){
				reglevlMap=datavalue;
			}
		}); 
		var dateArray = new Array();
		var date = "${dateString}";
	    dateArray =date.split(',');
		queryList(dateArray,1,"${dimensionString}","${dimensionValue}");
	});
	function queryList(dateArray,dateType,dimensionString,dimensionValue){
		var dimStringArray=dimensionString.split(',');//所选的维度的种类数组，即数组长度等于维度数量
		var dimensionNameArray =['挂号人数','挂号费用'];
		
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
 			url:'<%=basePath%>statistics/register/queryregisterlist.action', 
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
		window.open ("<c:url value='/statistics/wdWin/WDWinToRegister.action?randomId='/>"+timerStr,'newwindow','height=350,width=700,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}
	function dept_codefunction(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	function reglevl_codefunction(value,row,index){
		if(value!=null&&value!=''){
			return reglevlMap[value];
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

