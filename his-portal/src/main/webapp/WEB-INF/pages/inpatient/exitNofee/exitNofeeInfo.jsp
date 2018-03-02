<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	 <div id="divLayout" class="easyui-layout" fit=true>
   		<div data-options="region:'center',split:false,border:false" style="width:100%;height:100%;">
			<table id="dataList" data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarIdRoleMenuView'">
				<thead>
					<tr>
						<th data-options="field:'medicalrecordId'" width="30%">病历号</th>
						<th data-options="field:'inpatientNo'" width="30%">住院流水号</th>
						<th data-options="field:'patientName'" width="20%">姓名</th>
						<th data-options="field:'reportSexName'" width="10%">性别</th>
					</tr>
				</thead>
			</table>
		</div>
   </div>
<script type="text/javascript">
	var  idcardNo="${inpatientNoMerc}";
	$(function(){
		console.info(idcardNo)
		$('#dataList').datagrid({
			url:'<%=basePath%>inpatient/exitNofee/queryExitNoFeeM.action',
			queryParams:{'inpatientInfo.medicalrecordId':idcardNo},
			onDblClickRow: function (rowIndex, rowData) {//双击查看
				queryByInfo(rowData);
				$('#menuWin').dialog('close');
			} 
		});
	});	
</script>
</body>
</html>