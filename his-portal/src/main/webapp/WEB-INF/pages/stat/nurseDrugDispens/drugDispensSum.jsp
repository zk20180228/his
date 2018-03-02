<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>摆药汇总</title>
</head>
	<body>
		<div class="easyui-layout" fit="true">		   
			<div region="center" border="false" style="height: 100%">
				<div class="easyui-layout" fit="true">	
					<div region="north" border="false" style="height:40px;text-align:center;">
						<span style="font-size:25px;" class="drugDispensTit"><span id="weiFont" style="font-size:25px;">未</span><span id="yiFont" style="font-size:25px;">已</span>摆药处方查询</span>
					</div>
					<div region="center" border="true" style="height:98%;border-left:0">
						<table id="drugDispensSumList" class="easyui-datagrid"  data-options="fit:true,pagination:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,toolbar:'#toolbarId'" style="height:100%;">
							<thead>
								<tr>
									<th data-options="field:'drugCommonname'" style="width:15%">药品名称</th>
									<th data-options="field:'spec'" style="width:6%">规格</th>
									<th data-options="field:'unit',formatter:drugUnitFamaters" style="width:6%">单位</th>
									<th data-options="field:'drugDosageform',formatter:jixingfunction" style="width:6%">剂型</th>
									<th data-options="field:'qtys'" style="width:6%" align="right" halign="left">数量</th> 
									<th data-options="field:'drugRetailprice'" style="width:6%"  align="right" halign="left">单价</th>
									<th data-options="field:'totCosts'" style="width:8%"  align="right" halign="left">金额</th>
									<th data-options="field:'billclassName',formatter:billClassFormatter" style="width:13%">摆药单</th>
									<th data-options="field:'deptDrugName'" style="width:11%">取药药房</th>
									<th data-options="field:'drugId'" style="width:11%">药品编码</th>
									<th data-options="field:'deptName'" style="width:11%">申请科室</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>		
			</div>
		</div>
	</body>
</html>