<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药房内部入库审核(数据源是--药房内部入库--)</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
</head>
	<body>
		<!-- 药房核准入库(数据源是--药房内部入库--) -->
		<div id="divLayout" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',split:false,border:false" style="width: 100%;height: 100%">
			<input type="hidden" id="deptId" name="deptId" value="${deptId }">
			<input type="hidden" id="applyBillcode" name="applyBillcode" value="${applyBillcode }">
				<table id="applyOutList" data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fit:true,fitColumns:false">
					<thead>
						<tr>
							<th data-options="field:'drugDeptName', width : '33%'">
								发药科室
							</th>
							<th data-options="field:'drugDeptCode',hidden:true">
								发药科室code
							</th> 
							<th data-options="field:'applyBillcode', width : '33%'">
								申请单号
							</th>
							<th data-options="field:'drugNum', width : '33%'">
								申请药品种数
							</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<script type="text/javascript">
		//加载页面
		$(function(){
			//申请单列表加载
			$('#applyOutList').datagrid({
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 30, 50, 80, 100 ],
					url:'<%=basePath%>drug/applyout/queryApplyOutList.action',
					queryParams: {
						deptId: $('#deptId').val(),
						applyBillcode: $('#applyBillcode').val()
					},
					onDblClickRow: function (rowIndex, rowData) {
						var applyBillcode = rowData.applyBillcode;
						$('#infolist').datagrid('load', {
							applyBillcode : applyBillcode
						});
						closeLayout();
					}
			});
		})
		
		</script>
		</body>
</html>