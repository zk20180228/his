<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>待办任务列表视图</title>
</head>
<body>
	<div class="container-fluid">
<!-- 		<div class="input-group pull-right" style="width:200px;margin-top:5px;margin-bottom:5px;"> -->
<!-- 			<input id="searchId" type="text" class="form-control" placeholder="输入名称查询"> -->
<!-- 			<span class="input-group-btn"> -->
<!-- 				<button class="btn btn-default" type="button" href="javascript:void(0)" onclick="search()">查询</button> -->
<!-- 			</span> -->
<!-- 	    </div> -->
		<div>
			<table id="demoGrid"></table>
		</div>
	</div>
	<script type="text/javascript">
	$(function () {
		$('#demoGrid').bootstrapTable({
			method: 'post',
			striped: true,
			cache: false,
			sidePagination: "server",
			pagination: true,
			pageNumber:1,
			pageSize: 20,
			pageList: [20, 30, 50, 100],
			uniqueId: "id",
			strictSearch: false,
			showToggle:false,
			cardView: false,
			detailView: false,
			clickToSelect: true,
			singleSelect:false,
			height: $(window).height()-50,
// 			toolbar: '#toolbar',
			url: '<%=basePath%>activiti/humanTask/taskInfoViewPage.action',
			queryParams:queryParams,
			queryParamsType:'',
			contentType: "application/x-www-form-urlencoded",
			formatLoadingMessage: function () {  
				return "请稍等，正在加载中...";  
			},  
			formatNoMatches: function () {
				return '无符合条件的记录';  
			}, 
			columns: [{
				checkbox: true
			}, {
				field: 'businessKey',
				title: '编号',
				align:'right',
				halign: 'center'
			}, {
				field: 'presentationSubject',
				title: '标题',
				align:'right',
				halign: 'center'
			}, {
				field: 'createTime',
				title: '到达时间',
				align:'right',
				halign: 'center'
			}, {
				field: 'processDefinitionId',
				title: '流程',
				align:'right',
				halign: 'center'
			},{
				field: 'name',
				title: '环节',
				align:'right',
				halign: 'center'
			},{
				field: 'catalog',
				title: '状态',
				align:'right',
				halign: 'center'
			},{
				field: 'operation',
				title: '操作',
				align:'right',
				halign: 'center',
				formatter: operateFormatter  
			},]
		});
// 		$('#searchId').bind('keydown',function(event){  
// 			if(event.keyCode == "13"){  
// 				search();
// 			}  
// 		});  
	});
	 
	function operateFormatter(value, row, index) {
		return [
			'<div class="btn-group btn-group-xs" role="group">'+
			 	'<a type="button" class="btn btn-primary" href="<%=basePath%>activiti/humanTask/viewTaskForm.action?humanTaskId='+row.id+'">处理</a>'+
			 	'<a type="button" class="btn btn-success" href="<%=basePath%>activiti/operation/viewHistory.action?id='+row.processInstanceId+'">详情</a>'+
			'</div>'
		]
 	}
	
	function queryParams(params){
		return{
			rows:params.pageSize,
			page:params.pageNumber
// 			,name:$('#searchId').val()
		}
	}
	function search(){
		$('#demoGrid').bootstrapTable('refresh',{page:1});
	}
	</script>
</body>
</html>