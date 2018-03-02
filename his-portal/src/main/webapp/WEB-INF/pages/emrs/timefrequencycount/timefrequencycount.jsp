<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>科室质控书写时限和频率统计</title>
<script type="text/javascript">
var empMap="";
$(function(){
	$.ajax({//渲染人员
				url: '<%=basePath%>baseinfo/employee/getEmplMap.action',
		 		type:'post',
		 		async:true,
		 		success: function(empData) {	
		 			empMap = empData;
		 		}
		 	});
	setTimeout(function(){
		$('#list').datagrid({
			url:'<%=basePath %>emrs/emrSetCount/getTimeAndFrequency.action'
		});
	},800);
});
function formatterDocCode(value,row,index){
	if(value!=null&&value!=''){
		return empMap[value];
	}
}
</script>
</head>
<body>
	<table id="list" class="easyui-datagrid" style="width:auto;height:auto"   
	        data-options="fitColumns:true,singleSelect:true,border:false">   
	    <thead>   
	        <tr>   
	            <th data-options="field:'docCode',width:100,formatter:formatterDocCode">医生姓名</th>   
	            <th data-options="field:'patientNum',width:100">患者数量</th>   
	            <th data-options="field:'emrNum',width:100">病历数量</th>   
	            <th data-options="field:'timeOutEmrNum',width:100">超时病历数量</th>   
	            <th data-options="field:'unWriteEmrNum',width:100">未写病历数量</th>   
	        </tr>   
	    </thead>   
	</table>  
</body>
</html>