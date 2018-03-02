<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>费用汇总信息</title>
</head>
	<body>
		<div class="easyui-layout" fit="true">		   
			<div region="center" border="false" style="height: 100%;width: 100%">
				<table id="listFee" class="easyui-datagrid"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
					<thead>
						<tr>
							<th data-options="field:'feeCode',formatter:functionMin" style="width:18%">最小费用</th>
							<th data-options="field:'tot'" style="width:12%"align="right" halign="left">费用金额</th>
							<th data-options="field:'own'" style="width:12%"align="right" halign="left">自费金额</th>
							<th data-options="field:'pub'" style="width:12%"align="right" halign="left">自付金额</th>
							<th data-options="field:'pay'" style="width:12%"align="right" halign="left">公费金额</th>
							<th data-options="field:'eco'" style="width:12%"align="right" halign="left">优惠金额</th>
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