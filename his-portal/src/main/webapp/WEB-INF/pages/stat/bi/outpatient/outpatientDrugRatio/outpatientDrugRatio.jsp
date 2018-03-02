<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>
门诊药品比例分析
</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
		var deptMap=null;
		$(function(){
			//查询科室map
			$.ajax({
				url:'<%=basePath%>bi/outpatientDrugRatio/queryDeptForBiPublic.action',
				async:false,
				success:function(datavalue){
					deptMap=datavalue;
				}
			});
			var dateArray = new Array();
			var date = "${dateString}";
		    dateArray =date.split(',');
			queryList(dateArray,1,"${dimensionString}","${dimensionValue}");
		});
	
	/**
	*打开维度选择弹窗
	*/
	function popWin(){
		var timerStr = Math.random();
		window.open ("<c:url value='/statistics/wdWin/WDWinToOutPatientDrugRatio.action?randomId='/>"+timerStr,'newwindow','height=350,width=700,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}
	
	function EXEC_DPCDfunction(value,row,index){
		if(value!=null&&value!='' && value.indexOf(":")==-1){
			return deptMap[value];
		}else{
			return value;
		}
	}
	
		
	function queryList(dateArray,dateType,dimensionString,dimensionValue){
		var dimStringArray=dimensionString.split(',');
		if(dateType==1){
			dimensionNameArray =['门诊人次','门诊费用','药品费用','构成比例','环比','人均药品费用'];
		}else{
			dimensionNameArray =['门诊人次','门诊费用','药品费用','构成比例','同比','环比','人均药品费用'];
		}
		var columnsArray=publicStatistics(dateArray,dimStringArray,dateType,dimensionNameArray);
	
		var string="";
		for(var i=0;i<dimStringArray.length;i+=2 ){
			if(string!=''){
				string +=',';
			}
			string += dimStringArray[i].split(',')[0];
		}
		$('#listDrugRatio').datagrid({
			columns:columnsArray,
			multiSort:true,
			sortName:dimStringArray[0],
			remoteSort:false,
 			url:'<%=basePath%>bi/outpatientDrugRatio/queryOutpatientDrugRatio.action', 
			queryParams:{'dateArray':dateArray,'dimStringArray':dimStringArray,'dateType':dateType,'dimensionValue':dimensionValue},
			onLoadSuccess:function(data){
				 if (data.rows.length > 0) {
		                mergeCellsOfValue("listDrugRatio", string);
		         }
				 getRowSum();
 			}
		});
	}

	
	function getRowSum(){
		 var colName='';
		 var sum=0;
		 var field='{EXEC_DPCD:'+"'合计:'";
		 var fields = $('#listDrugRatio').datagrid('getColumnFields'); 
		 fields.shift();
		 
//	 	 fields.shift();
	 	 var rows = $('#listDrugRatio').datagrid('getRows')//获取当前的数据行
	    for (var i = 0; i < fields.length;i++) {
	    	
	   	 var totSum=0;
			 colName=fields[i];
		     for (var j = 0; j < rows.length; j++) {
		    	 
//	 	    	 alert(typeof(rows[j][colName]));
		    	 if(typeof(rows[j][colName])=="number"){
		    		 totSum +=rows[j][colName];
		    	 }else if(typeof(rows[j][colName])=="undefind"||typeof(rows[j][colName])=="NaN"){
//	 	    		 alert(j);
//	 	    		 alert(colName);
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
	    $('#listDrugRatio').datagrid('appendRow',field);
	}

</script>
</head>
<body  class="easyui-layout" data-options="fit:true">
<div data-options="region:'center',border:false" style="width: 100%;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="width: 100%;height: 50px;padding:8px;">
				<a href="javascript:popWin();" style="margin:0px 30px 0px 0px;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">维度选择</a>
			</div>
			<div data-options="region:'center',border:false" style="width: 100%;">
				<table id="listDrugRatio" data-options="fit:true"></table>
			</div>
		</div>
	</div>

</div>
</body>
</html>