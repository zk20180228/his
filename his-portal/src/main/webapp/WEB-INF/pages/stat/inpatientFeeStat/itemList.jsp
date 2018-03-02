<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>住院非药品明细</title>
</head>
	<body>
		<div class="easyui-layout" fit="true">		   
			<div   region="center" border="false" style="height: 100%;width: 100%">
				<table id="listItem" class="easyui-datagrid"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th data-options="field:'itemName'" style="width:18%"align="center" halign="center">项目名称</th>
							<th data-options="field:'unitPrice'" style="width:12%" align="right" halign="left">单价</th>
							<th data-options="field:'qty'" align="center" halign="center" >数量</th>
							<th data-options="field:'currentUnit'" style="width:8%">单位</th>
							<th data-options="field:'totCost'" style="width:8%" align="right" halign="center">金额</th>
							<th data-options="field:'ownCost'" style="width:12%" align="right" halign="center">自费</th>
							<th data-options="field:'pubCost'" style="width:8%" align="right" halign="center">公费</th>
							<th data-options="field:'payCost'" style="width:12%" align="right" halign="center">自付</th>
							<th data-options="field:'ecoCost'" style="width:8%" align="right" halign="center">优惠</th>
							<th data-options="field:'executeDeptname'" style="width:12%"align="center" halign="center">执行科室</th>
							<th data-options="field:'inhosDeptname'" style="width:8%"align="center" halign="center" >患者科室</th>
							<th data-options="field:'feeDate'" style="width:12%"align="center" halign="center">收费时间</th>
							<th data-options="field:'feeOpercode',formatter:functionEmp" style="width:8%" align="center" halign="center">收费员</th>
							<!-- <th data-options="field:'ownCost'" style="width:12%">来源</th> -->
						</tr>
					</thead>
				</table>		
			</div>
		</div>
	
<script type="text/javascript">
var empMap='';
$(function(){
	//渲染表单中的挂号专家
	$.ajax({
		url: '<%=basePath%>stat/inpatientFee/queryEmployeeMap.action',
		success: function(empData) {
			empMap = empData;
		}
	})
});
//渲染人员
function functionEmp(value,row,index){
	if(value!=null&&value!=''){
		return empMap[value];
	}
}
	
</script>
	</body>
</html>