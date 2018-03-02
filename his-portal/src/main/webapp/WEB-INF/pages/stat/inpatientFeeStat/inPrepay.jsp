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
		<div class="easyui-layout" fit="true">		   
			<div   region="center" border="false" style="height: 100%;width: 100%">
				<table id="listInprepay" class="easyui-datagrid"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th data-options="field:'receiptNo'" style="width:12%"align="center" halign="center">票据号</th>
							<th data-options="field:'prepayCost'" style="width:10%" align="right" halign="center">预交金额</th>
							<th data-options="field:'payWay',formatter:functionPayway"style="width:8%" align="center" halign="center">支付方式</th>
							<th data-options="field:'createUser',formatter:functionEmp" style="width:8%"align="center" halign="center">操作员</th>
							<th data-options="field:'createTime'" style="width:12%"align="center" halign="center">操作日期</th>
							<th data-options="field:'deptName'" style="width:12%"align="center" halign="center">所在科室</th>
							<th data-options="field:'balanceState',formatter:functionState" style="width:8%" align="center" halign="center">结算状态</th>
						</tr>
					</thead>
				</table>		
			</div>
		</div>
	
<script type="text/javascript">
var empMap='';
var payWay='';
$(function(){
	//操作员
	$.ajax({
		url: '<%=basePath%>baseinfo/employee/getEmplMap.action',
		success: function(empData) {
			empMap = empData;
		}
	})
	
	$.ajax({
		url: '<%=basePath%>stat/inpatientFee/payWay.action',
		success: function(empData) {
			payWay = empData;
		}
	})
});
//渲染人员
function functionEmp(value,row,index){
	if(value!=null&&value!=''){
		return empMap[value];
	}
}

//渲染支付方式
function functionPayway(value,row,index){
	for(var i=0;i<payWay.length;i++){
		if(payWay[i].encode==value){
			return payWay[i].name;
		}
	}
}

//渲染结算状态
function functionState(value,row,index){
	if(value=="0"){
		return "未结算";
	}else if(value=="1"){
		return "已结算";
	}else if(value=="2"){
		return "已结转";
	}else{
		return "";
	}
}
</script>
	</body>
</html>