<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>结算信息</title>
</head>
	<body>
		<div class="easyui-layout" fit="true">		   
			<div region="center" border="false" style="height: 100%;width: 100%">
				<table id="listHead" class="easyui-datagrid"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
					<thead>
						<tr>
							<th data-options="field:'invoiceNo'" style="width:18%">发票号</th>
							<th data-options="field:'totCost'" style="width:12%"align="right" halign="left" >费用金额</th>
							<th data-options="field:'supplyCost'" style="width:12%"align="right" halign="left">补收金额</th>
							<th data-options="field:'returnCost'" style="width:12%"align="right" halign="left">返还金额</th>
							<th data-options="field:'balanceOpername'" style="width:12%">结算人</th>
							<th data-options="field:'balanceDate'" style="width:12%">结算时间</th>
							<th data-options="field:'balanceoperDeptname'" style="width:12%">结算员科室</th>
						</tr>
					</thead>
				</table>		
			</div>
		</div>
	
<script type="text/javascript">
	var minMap = "";
	$(function(){
		$.ajax({
			url: '<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action',
			data:{type:"drugMinimumcost"},
			success: function(minData) {
				minMap = minData;
			}
		})
	})
	//渲染人员
function functionMin(value,row,index){
	if(value!=null&&value!=''){
		return minMap[value];
	}
}
</script>
	</body>
</html>