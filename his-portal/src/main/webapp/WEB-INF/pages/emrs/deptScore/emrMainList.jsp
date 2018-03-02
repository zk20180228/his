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
			url:'<%=basePath%>emrs/emrDeptScore/emrMainList.action',
			onBeforeLoad:function(param){
				var inpatientNo = parent.getInpatientNo();
				var itemId = parent.getTabId();
				if(inpatientNo != '' && itemId != 'A'){
					param.inpatientNo = inpatientNo;
					param.itemId = itemId;
				}else{
					return false;
				}
				$('#mainList').datagrid('clearSelections');
			},
			onSelect: function (rowIndex, rowData) {//双击查看
				window.open ('<%=basePath%>emrs/emrDeptScore/emrMainView.action?mainId=' + rowData.id,'newwindow',' left=400,top=200,width='+ (screen.availWidth -1000) +',height='+ (screen.availHeight-370) 
						+',scrollbars,resizable=yes,toolbar=yes');
			}
		});
	});
	function mainReload(itemId,inpatientNo){
		$('#mainList').datagrid('load',{
			itemId: itemId,
			inpatientNo: inpatientNo
		});
	}
	</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div id="divLhmMessage" data-options="region:'center',border:'true'"  fit=true>
			<table id="mainList"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:false,selectOnCheck:false,singleSelect:true">
				<thead>
					<tr>
						<th data-options="field:'emrSN',width:'13%',align:'center'">流水号</th>
						<th data-options="field:'patientName',width:'14%',align:'center'">患者姓名</th>
						<th data-options="field:'tempName',width:'14%',align:'center'">模板名称</th>
						<th data-options="field:'typeName',width:'14%',align:'center'">病历类型</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>