<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>医嘱管理历史医嘱</title>
</head>
<body>
	 <div id="divLay1out"  class="easyui-layout" data-options="fit:true" style="width: 100%; height: 100%;">
		<div data-options="region:'center',split:false,iconCls:'icon-book'" style="width:100%;">
			<div data-options="region:'center'" style="width:100%;height:100%">
				<table id="list" style="width: 100%;" class="easyui-datagrid" data-options="method:'post',rownumbers:true,idField: 'id',fit:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'name'" style="width:18%">名称</th>
							<th data-options="field:'drugNamepinyin'" style="width:12%" >名称拼音码</th>
							<th data-options="field:'drugNamewb'" style="width:12%">名称五笔码</th>
							<th data-options="field:'drugNameinputcode'" >名称自定义码</th>
							<th data-options="field:'drugCommonname'" style="width:12%">通用名称</th>
							<th data-options="field:'drugCnamepinyin'" style="width:8%">通用名称拼音码</th>
							<th data-options="field:'drugCnamewb'" style="width:8%">通用名称五笔码</th>
							<th data-options="field:'drugCnameinputcode'" style="width:12%">通用名称自定义码</th>
							<th data-options="field:'drugBiddingcode'" style="width:8%" >招标识别码</th>
						</tr>
					</thead>
				</table>
			</div> 
		</div>
	</div>

<script type="text/javascript">
	function queryDrug(){
		$('#list').datagrid({
		    pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url : '<%=basePath%>inpatient/docAdvManage/docAdvManageDrugInfo.action',
		});
	}
</script>
</body>
</html>