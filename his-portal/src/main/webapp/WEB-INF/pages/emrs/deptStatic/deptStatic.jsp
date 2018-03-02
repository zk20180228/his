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
			url:'<%=basePath%>emrs/emrDeptStatic/deptStaticList.action',
			onBeforeLoad:function(param){
				$('#mainList').datagrid('clearSelections');
			},
			onSelect: function (rowIndex, rowData) {//双击查看
				window.open ('<%=basePath%>emrs/emrDeptStatic/toDeptStaticDetil.action?inpatientNo=' + rowData.inpatientNo + '&deptCode=' + rowData.deptCode,'newwindow',' left=400,top=200,width='+ (screen.availWidth -1000) +',height='+ (screen.availHeight-370) 
						+',scrollbars,resizable=yes,toolbar=yes');
			}
		});
	});
	</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div id="divLhmMessage" data-options="region:'center',border:false"  fit=true>
			<table id="mainList"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:false,selectOnCheck:false,singleSelect:true">
				<thead>
					<tr>
						<th data-options="field:'inpatientNo',width:'13%',align:'center'">住院号</th>
						<th data-options="field:'deptName',width:'14%',align:'center'">科室</th>
						<th data-options="field:'patName',width:'14%',align:'center'">患者姓名</th>
						<th data-options="field:'patSex',width:'14%',align:'center'">性别</th>
						<th data-options="field:'patAge',width:'14%',align:'center'">年龄</th>
						<th data-options="field:'bedName',width:'13%',align:'center'">床号</th>
						<th data-options="field:'inDays',width:'14%',align:'center'">住院日</th>
						<th data-options="field:'inDate',width:'14%',align:'center'">入院时间</th>
						<th data-options="field:'overTime',width:'14%',align:'center'">超时项目数</th>
						<th data-options="field:'lostTime',width:'13%',align:'center'">缺写项目数</th>
						<th data-options="field:'times',width:'14%',align:'center'">质控环节扣分次数</th>
						<th data-options="field:'score',width:'14%',align:'center'">质控环节扣分数</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>