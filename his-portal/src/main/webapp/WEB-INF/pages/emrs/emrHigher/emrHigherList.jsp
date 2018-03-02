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
		//加载datagrid
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url:'<%=basePath%>emrs/reviewEmrMain/queryEmrMainList.action',
			queryParams : {
				'menuAlias' : '${menuAlias}',
				'state' : '0'
			},
			onDblClickRow: function (rowIndex, rowData) {//双击查看
				if(rowData.emrState == 2){
					$.messager.confirm('确认对话框', '此病历已审签，只可查看，是否查看？', function(r){
						if (r){
							window.open ('<%=basePath%>emrs/reviewEmrMain/toEmrHigherEditView.action?id=' + rowData.id,'newwindow',' left=400,top=200,width='+ (screen.availWidth -1000) +',height='+ (screen.availHeight-370) 
									+',scrollbars,resizable=yes,toolbar=yes');
						}
					});
				}else{
					window.open ('<%=basePath%>emrs/reviewEmrMain/toEmrHigherEditView.action?id=' + rowData.id,'newwindow',' left=400,top=200,width='+ (screen.availWidth -1000) +',height='+ (screen.availHeight-370) 
							+',scrollbars,resizable=yes,toolbar=yes');
				}
			},
			onLoadSuccess:function(row, data){
				$("#list").datagrid('uncheckAll');
				$("#list").datagrid('unselectAll');
				//分页工具栏作用提示
				var pager = $(this).datagrid('getPager');
				var aArr = $(pager).find('a');
				var iArr = $(pager).find('input');
				$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
				for(var i=0;i<aArr.length;i++){
					$(aArr[i]).tooltip({
						content:toolArr[i],
						hideDelay:1
					});
					$(aArr[i]).tooltip('hide');
				}}
		});
		
	});
	function reload(){
		$("#list").datagrid('reload');
	}
	function formattState(value,row,index){
		if(value == 1){
			return '未审签';
		}
		if(value == 2){
			return '审检通过';
		}
	}
	</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div id="divLhmMessage" data-options="region:'center',border:false"  fit=true>
			<input type="hidden" name="flag" value="Mainlist" id="flag">
			<table id="list"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				<thead>
					<tr>
						<th data-options="checkbox:true,field : 'ck'"></th>
						<th data-options="field:'emrSN',width:'13%',align:'center'">流水号</th>
						<th data-options="field:'patientName',width:'14%',align:'center'">患者姓名</th>
						<th data-options="field:'tempName',width:'14%',align:'center'">模板名称</th>
						<th data-options="field:'typeName',width:'14%',align:'center'">病历类型</th>
						<th data-options="field:'emrState',width:'14%',align:'center',formatter:formattState">状态</th>
						<th data-options="field:'emrScore',width:'14%',align:'center'">分数</th>
						<th data-options="field:'emrLevel',width:'14%',align:'center'">级别</th>
						<th data-options="field:'strContent',hidden:'true' ,align:'center',">模板内容</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>