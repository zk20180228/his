<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>住院药品明细</title>
</head>
	<body>
		<div class="easyui-layout" fit="true">		   
			<div   region="center" border="false" style="height: 100%;width: 100%">
				<table id="listMedicine" class="easyui-datagrid"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th data-options="field:'drug_name'" style="width:12%"align="center" halign="left">名称</th>
							<th data-options="field:'specs'" style="width:6%" align="center" halign="left">规格</th>
							<th data-options="field:'unitPrice'" style="width:4%"  align="right" halign="center">单价</th>
							<th data-options="field:'qty'" style="width:6%" align="right" halign="center">数量</th>
							<th data-options="field:'days'" style="width:6%" align="right" halign="center">付数</th>
							<th data-options="field:'currentUnit',formatter:functionUnit" style="width:4%" align="right" halign="center">单位</th>
							<th data-options="field:'totCost'" style="width:5%" align="right" halign="center">金额</th>
							<th data-options="field:'ownCost'" style="width:5%" align="right" halign="center">自费</th>
							<th data-options="field:'pubCost'" style="width:5%" align="right" halign="center">公费</th>
							<th data-options="field:'payCost'" style="width:5%" align="right" halign="center">自付</th>
							<th data-options="field:'ecoCost'" style="width:5%" align="right" halign="center">优惠</th>
							<th data-options="field:'executeDeptname'" style="width:6%" align="center" halign="center">执行科室</th>
							<th data-options="field:'inhosDeptname'" style="width:6%" align="center" halign="center">患者科室</th>
							<th data-options="field:'feeDate'" style="width:6%"align="center" halign="center">收费时间</th>
							<th data-options="field:'feeOpercode',formatter:functionEmp" style="width:6%" align="center" halign="center">收费员</th>
							<th data-options="field:'senddrugDate'" style="width:6%"align="center" halign="center">发药时间</th>
							<th data-options="field:'senddrugOpercode',formatter:functionEmp" style="width:6%" align="center" halign="center">发药员</th>
							<!-- <th data-options="field:'ownCost'" style="width:12%">来源</th> -->
						</tr>
					</thead>
				</table>		
			</div>
		</div>
	
<script type="text/javascript">
var empMap='';
var packUnit='';
$(function(){
	//渲染表单中的挂号专家
	$.ajax({
		url: '<%=basePath%>stat/inpatientFee/queryEmployeeMap.action',
		success: function(empData) {
			empMap = empData;
		}
	})
	//查询单位map
	$.ajax({
		url:'<%=basePath%>/statistics/InteQuery/getPackUnit.action',
		type:'post',
		success:function(data){
			packUnit= data;
		}
	});
});
//渲染人员
function functionEmp(value,row,index){
	if(value!=null&&value!=''){
		return empMap[value];
	}
}
	
//渲染单位以及包装单位
function functionUnit(value,row,index){
	for (var i = 0; i < packUnit.length; i++) {
		if(packUnit[i].encode==value){
			return packUnit[i].name;
		}
	}
}
</script>
	</body>
</html>