<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>电子病历列表</title>
<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
	<script type="text/javascript">
	//加载页面
	$(function(){
		$('#mainList').datagrid({
			url:'<%=basePath%>emrs/emrDeptStatic/deptStaticDetilList.action',
			onBeforeLoad:function(param){
				var inpatientNo = '${inpatientNo}';
				var deptCode = '${deptCode}';
				param.inpatientNo = inpatientNo;
				param.deptCode = deptCode;
			},
			onLoadSuccess: function (data) {//默认选中
				$('#mainList').datagrid('clearSelections');
				$('#mainList').datagrid("autoMergeCell", 'itemName');
			}
			,onClickRow:function(rowIndex, rowData){
				$('#mainList').datagrid('unselectAll');
				var itemName = rowData.itemName;
				var rows = $('#mainList').datagrid('getRows');
				if(rows!=null&&rows.length>0){
					for(var i=0;i<rows.length;i++){
						if(rows[i].itemName == itemName){
							var index = $('#mainList').datagrid('getRowIndex',rows[i])
							$('#mainList').datagrid('selectRow',index);
						}
					}
				}
			}
		});
	});
	
	$.extend($.fn.datagrid.methods, {
		autoMergeCell: function (dg, fldList) {
			var Arr = fldList.split(",");
			var fldName;
			var RowCount = dg.datagrid("getRows").length;
			var span;
			var PerValue = "";
			var CurValue = "";
			var length = Arr.length - 1;
			for (i = length; i >= 0; i--) {
				fldName = Arr[i];
				PerValue = "";
				span = 1;
				for (row = 0; row <= RowCount; row++) {
					if (row == RowCount) {
						CurValue = "";
					}
					else {
						CurValue = dg.datagrid("getRows")[row][fldName];
					}
					if (PerValue == CurValue) {
						span += 1;
					}
					else {
						var index = row - span;
						dg.datagrid('mergeCells', {
							index: index,
							field: fldName,
							rowspan: span,
							colspan: null
						});
						span = 1;
						PerValue = CurValue;
					}
				}
			}
		}
	});
	</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div id="divLhmMessage" data-options="region:'center',border:'true'"fit=true>
			<table id="mainList" data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:false,selectOnCheck:false,singleSelect:false">
				<thead>
					<tr>
						<th data-options="field:'itemName',width:'20%',align:'center'">缺陷分类</th>
						<th data-options="field:'scoreDesc',width:'55%',align:'center'">缺陷名称</th>
						<th data-options="field:'scoreCount',width:'15%',align:'center'">发生频次</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>