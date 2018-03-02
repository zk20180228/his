<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>预交金</title>
</head>
	<body>
		<div class="easyui-layout" fit="true" style="height: 100%">		   
			<div region="center" border="false" style="height: 100%">				
				<table id="listDetailsCost" style="height: 100%" class="easyui-datagrid"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th data-options="field:'recipeNo'" style="width:10%"align="center" halign="center">处方号</th>
							<th data-options="field:'itemCode'" style="width:15%"align="center" halign="center">项目编号</th>
							<th data-options="field:'itemName'" style="width:10%"align="center" halign="center">项目名称</th>
							<th data-options="field:'unitPrice'" style="width:6%" align="right" halign="center">单价</th>
							<th data-options="field:'qty'" style="width:6%"align="right" halign="center">数量</th>
							<th data-options="field:'currentUnit',formatter:drugpackagingunitFamaters" style="width:6%"align="center" halign="center">单位</th>
							<th data-options="field:'totcost',formatter:purchaseCostFormatter" style="width:8%" align="right" halign="center">金额</th>
							<th data-options="field:'feeOpercode',formatter:employeeFamaters" style="width:10%"align="center" halign="center">操作员</th>
							<th data-options="field:'feeDate'" style="width:15%"align="center" halign="center">计费日期</th>
							<th data-options="field:'itemType',hidden:true"align="center" halign="center">药品非药品标识</th>
						</tr>
					</thead>
				</table>		
			</div>
		</div>
	
<script type="text/javascript">
var drugpackagingunitMaps = "";//包装单位Map
var nonmedicineencodingMaps="";//非药品单位	Map
var employeeMap = "";//员工Map
//查询包装单位
$.ajax({
	url: "<%=basePath%>inpatient/docAdvManage/queryDrugpackagingunit.action",				
	type:'post',
	success: function(drugpackagingunitdata) {					
		drugpackagingunitMaps = drugpackagingunitdata;										
	}
});
//查询非药品单位
$.ajax({
	url: "<%=basePath%>inpatient/docAdvManage/queryNonmedicineencoding.action",				
	type:'post',
	success: function(nonmedicineencodingdata) {					
		nonmedicineencodingMaps = nonmedicineencodingdata;										
	}
});
//员工
$.ajax({
	url: "<%=basePath%>outpatient/changeDeptLog/queryempComboboxs.action",				
	type:'post',
	success: function(data) {					
		employeeMap = data;					
	}
});
//单位 列表页 显示	
function drugpackagingunitFamaters(value,row,index){	
	if(value!=null&&value!=""){
		if(row.itemType=='1'){					
			if(drugpackagingunitMaps[value]!=null&&drugpackagingunitMaps[value]!=""){
				return drugpackagingunitMaps[value];
			}
			return value;
		}
		if(row.itemType=='2'){
			if(nonmedicineencodingMaps[value]!=null&&nonmedicineencodingMaps[value]!=""){
				return nonmedicineencodingMaps[value];
			}
			return value;
		}
	}			
}	
//操作员 列表页 显示		
function employeeFamaters(value,row,index){			
	if(value!=null&&value!=""){	
		return employeeMap[value];									
	}			
}
//保留两位小数
function purchaseCostFormatter(value,row,index){//入库总金额
	var num=value;
	if(typeof(num)=='undefined'){
		num=0;
	}
	return num.toFixed(2);
}
</script>
	</body>
</html>