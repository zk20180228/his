<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

	<head>
		<style type="text/css">
			* {
				box-sizing: border-box;
				padding: 0;
				margin: 0;
			}
			
			html,
			body {
				width: 100%;
				height: 100%;
				overflow: hidden;
			}
			
			.navTop {
				position: absolute;
				z-index: 5;
			}
			
			.navContnt {
				position: absolute;
				width: 100%;
				height: 100%;
				padding-top: 40px;
			}
			
			.fixed-table-container {
				border: none;
			}
			
			
			.tabs-panels{
			    position: absolute;
   		 		top: 0;
    			left: 0;
    			padding-top: 71px;
			}
			.tabs-header {
				z-index: 10;
			}
			.fixed-table-body {
				height: 672px
			}
		</style>
	</head>

	<body>
		<div>
			<ul class="navTop nav nav-tabs" role="tablist">
				<li role="presentation" class="active">
					<a href="#xinjian" aria-controls="xinjian" role="tab" data-toggle="tab">新建事务</a>
				</li>
				<li role="presentation">
					<a href="#home" aria-controls="home" role="tab" data-toggle="tab">我的事务</a>
				</li>
				<li role="presentation">
					<a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">事务办理</a>
				</li>
			</ul>
			<div class=" navContnt tab-content">
				<div role="tabpanel" class="tab-pane" id="home" style="width: 100%;height: 100%;">
					<div id="tt" class="easyui-tabs" data-options="fit:true" style="width: 100%;height: 100%;position: absolute;left: 0;top: 0;padding-top: 39px;">
						<div title="草稿箱" style="width: 100%;height: 100%">
							<%-- <div style="width: 100%;height: 50px;padding-top: 5px;padding-left: 10px;">
								时间:<input id="startTime" class="Wdate" type="text" name="startTime" value="${startTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								<input id="endTime" class="Wdate" type="text" name="endTime" value="${endTime }"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})" style="height:22px;width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
								标题:<input id="biaoti" class="easyui-textbox"/>
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							</div> --%>
							<table id="gaoGrid"></table>
						</div>
						<div title="退件箱" style="width: 100%;height: 100%">
							<div style="width: 100%;height: 50px;padding-top: 5px;padding-left: 10px;">
								时间:<input id="tuiJianStartTime" class="Wdate" type="text" name="startTime" value="${startTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								<input id="tuiJianEndTime" class="Wdate" type="text" name="endTime" value="${endTime }"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})" style="height:22px;width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
								标题:<input id="tuiJianBiaoti" class="easyui-textbox"/>
								<a href="javascript:void(0)" onclick="searchTuijianFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							</div>
							<table id="tuiGrid"></table>
						</div>
						<div title="未结流程" style="width: 100%;height: 100%">
							<table id="demoGrid"></table>
						</div>
						<div title="已结流程" style="width: 100%;height: 100%">
							<table id="demoGridFinish"></table>
						</div>
						<div title="我发起的催办" style="width: 100%;height: 100%">
							<table id="cuibanFinish"></table>
						</div>
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="profile" style="width: 100%;height: 100%">
					<div id="tt" class="easyui-tabs" data-options="fit:true">
						<div title="待处理" style="width: 100%;height: 100%">
							<table id="daichuliFinish"></table>
						</div>
						<div title="历史记录" style="width: 100%;height: 100%">
							<table id="lishiFinish"></table>
						</div>
						<div title="我收到的催办" style="width: 100%;height: 100%">
							<table id="shoudaoFinish"></table>
						</div>
					</div>
				</div>
				<div role="tabpanel" class="tab-pane active" id="xinjian">
					
					<table id="xinjianshiwu"></table>
				</div>
			</div>
		</div>
<div id="dialogDivId"></div>
		<script type="text/javascript">
			var processInstanceIdView="";
			var nameView="";
			var attr2View="";
			var codeView="";
			var assigneeView="";
			function shenq(id){
				AddOrShowEast(id,"<%=basePath%>operation/viewStartForm.action?id="+ id);
			}
			var nameMap = new Map();
			var userMap = new Map();
			$(function() {
				$.ajax({
					url: "<%=basePath%>activiti/queryFlow/listOaTaskInfo.action",
					type: 'post',
					async: false,
					success: function(data) {
						nameMap = data;
					}
				});
				
				//新建事务
				$('#xinjianshiwu').bootstrapTable({
					method: 'post',
					striped: true,
					cache: false,
					uniqueId: "id",
					strictSearch: false,
					showToggle:false,
					cardView: false,
					detailView: false,
					clickToSelect: true,
					singleSelect:false,
					url: '<%=basePath%>activiti/queryFlow/listxinian.action',
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
						field: 'categoryName',
						title: '分类',
						align: 'left',
						halign: 'center'
					},{
						field: 'name',
						title: '名称',
						align: 'left',
						halign: 'center'
					},{
						field: 'descn',
						title: '描述',
						align: 'left',
						halign: 'center'
					}, {
						field: 'operationcao',
						title: '操作',
						align: 'center',
						halign: 'center',
						formatter: faqiFormatter
					}]
				});
				$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
					// 获取已激活的标签页的名称
					var activeTab = $(e.target).text();
					// 获取前一个激活的标签页的名称
					var previousTab = $(e.relatedTarget).text();
					if(activeTab=='我的事务'){
						$.ajax({
							url: "<%=basePath%>baseinfo/employee/getEmplMap.action",
							type: 'post',
							async: false,
							success: function(data) {
								userMap = data;
							}
						});
						
						//草稿箱
						$('#gaoGrid').bootstrapTable({
							method: 'post',
							striped: true,
							cache: false,
							uniqueId: "id",
							strictSearch: false,
							showToggle:false,
							cardView: false,
							detailView: false,
							clickToSelect: true,
							singleSelect:false,
							sidePagination: "server",
							pagination: true,
							pageNumber:1,
							pageSize: 10,
							pageList: [20, 30, 50, 100],
							url: '<%=basePath%>activiti/queryFlow/listOaKVRecord.action',
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
								field: 'name',
								title: '标题',
								align: 'left',
								halign: 'center'
							}, {
								field: 'createTime',
								title: '申请时间',
								align: 'center',
								halign: 'center'
							}, {
								field: 'createUser',
								title: '申请人',
								align: 'center',
								halign: 'center',
								formatter: userFormatter
							}, {
								field: 'operationcao',
								title: '操作',
								align: 'center',
								halign: 'center',
								formatter: caogaoFormatter
							}, ]
						});
						//退收箱
						$('#tuiGrid').bootstrapTable({
							method: 'post',
							striped: true,
							cache: false,
							uniqueId: "id",
							pagination: true,
							pageNumber:1,
							pageSize: 10,
							pageList: [20, 30, 50, 100],
							strictSearch: false,
							showToggle: false,
							cardView: false,
							detailView: false,
							clickToSelect: true,
							singleSelect: false,
							//	 			toolbar: '#toolbar',
							url: '<%=basePath%>activiti/queryFlow/listtui.action',
							queryParams: queryParams,
							queryParamsType: '',
							contentType: "application/x-www-form-urlencoded",
							formatLoadingMessage: function() {
								return "请稍等，正在加载中...";
							},
							formatNoMatches: function() {
								return '无符合条件的记录';
							},
							columns: [{
								checkbox: true
							}, {
								field: 'name',
								title: '标题',
								align: 'left',
								halign: 'center'
							}, {
								field: 'actendtime',
								title: '申请时间',
								align: 'center',
								halign: 'center'
							}, {
								field: 'completeStatus',
								title: '申请人',
								align: 'center',
								halign: 'center',
								formatter: userFormatter
							}, {
								field: 'operationcao',
								title: '操作',
								align: 'center',
								halign: 'center',
								formatter: caogaoFormatter
							}, ]
						});
						//未完成
						$('#demoGrid').bootstrapTable({
							method: 'post',
							striped: true,
							cache: false,
							uniqueId: "id",
							strictSearch: false,
							showToggle: false,
							cardView: false,
							detailView: false,
							clickToSelect: true,
							singleSelect: false,
							url: '<%=basePath%>activiti/queryFlow/listWeiwan.action',
							queryParams: queryParams,
							queryParamsType: '',
							contentType: "application/x-www-form-urlencoded",
							formatLoadingMessage: function() {
								return "请稍等，正在加载中...";
							},
							formatNoMatches: function() {
								return '无符合条件的记录';
							},
							columns: [{
								checkbox: true
							}, {
								field: 'attr2',
								title: '标题',
								align: 'left',
								halign: 'center'
							}, {
								field: 'name',
								title: '当前流程',
								align: 'left',
								halign: 'center'
							}, {
								field: 'completeTime',
								title: '创建时间',
								align: 'center',
								halign: 'center'
							}, {
								field: 'completeStatus',
								title: '状态',
								align: 'center',
								halign: 'center',
								formatter: stateFormatter
							}, {
								field: 'operation',
								title: '操作',
								align: 'center',
								halign: 'center',
								formatter: operateFormatter
							}, ]
						});
						$('#demoGridFinish').bootstrapTable({
							method: 'post',
							striped: true,
							cache: false,
							sidePagination: "server",
							pagination: true,
							pageNumber: 1,
							pageSize: 20,
							pageList: [20, 30, 50, 100],
							uniqueId: "id",
							strictSearch: false,
							showToggle: false,
							cardView: false,
							detailView: false,
							clickToSelect: true,
							singleSelect: false,
							// 			toolbar: '#toolbar',
							url: '<%=basePath%>activiti/queryFlow/queryyijie.action',
							queryParams: queryParams,
							queryParamsType: '',
							contentType: "application/x-www-form-urlencoded",
							formatLoadingMessage: function() {
								return "请稍等，正在加载中...";
							},
							formatNoMatches: function() {
								return '无符合条件的记录';
							},
							columns: [{
								checkbox: true
							},{
								field: 'name',
								title: '标题',
								align: 'left',
								halign: 'center'
							}, {
								field: 'completeTime',
								title: '创建时间',
								align: 'center',
								halign: 'center'
							}, {
								field: 'completeStatus',
								title: '状态',
								align: 'center',
								halign: 'center',
								formatter: stateFormatterFinish
							},{
								field: 'operation',
								title: '操作',
								align: 'center',
								halign: 'center',
								formatter: operateFormatterFinish
							}, ]
						});
						//我的催办
						$('#cuibanFinish').bootstrapTable({
							method: 'post',
							striped: true,
							cache: false,
							strictSearch: false,
							showToggle: false,
							cardView: false,
							detailView: false,
							clickToSelect: true,
							singleSelect: false,
							//	 			toolbar: '#toolbar',
							url: '<%=basePath%>activiti/queryFlow/listcuiban.action',
							queryParams: queryParams,
							queryParamsType: '',
							contentType: "application/x-www-form-urlencoded",
							formatLoadingMessage: function() {
								return "请稍等，正在加载中...";
							},
							formatNoMatches: function() {
								return '无符合条件的记录';
							},
							columns: [{
								checkbox: true
							}, {
								field: 'procedureName',
								title: '流程名称',
								align: 'left',
								halign: 'center'
							}, {
								field: 'reminderNum',
								title: '催办次数',
								align: 'center',
								halign: 'center'
							}, {
								field: 'remindTime',
								title: '催办时间',
								align: 'center',
								halign: 'center'
							}, {
								field: 'operationcao',
								title: '操作',
								align: 'center',
								halign: 'center',
								formatter: operateFormatterFinish
							}, ]
						});
					}else if(activeTab=='事务办理'){
						//待处理
						$('#daichuliFinish').bootstrapTable({
							method: 'post',
							striped: true,
							cache: false,
							uniqueId: "id",
							strictSearch: false,
							showToggle: false,
							cardView: false,
							detailView: false,
							clickToSelect: true,
							singleSelect: false,
							url: '<%=basePath%>activiti/queryFlow/queryAgency.action',
							queryParams: queryParams,
							queryParamsType: '',
							contentType: "application/x-www-form-urlencoded",
							formatLoadingMessage: function() {
								return "请稍等，正在加载中...";
							},
							formatNoMatches: function() {
								return '无符合条件的记录';
							},
							columns: [{
								checkbox: true
							}, {
								field: 'attr2',
								title: '标题',
								align: 'left',
								halign: 'center'
							}, {
								field: 'createUser',
								title: '创建人',
								align: 'center',
								halign: 'center',
								formatter: userFormatter
							}, {
								field: 'createTime',
								title: '创建时间',
								align: 'center',
								halign: 'center'
							}, {
								field: 'processStarter',
								title: '提交人',
								align: 'center',
								halign: 'center',
								formatter: userFormatter
							}, {
								field: 'completeTime',
								title: '提交时间',
								align: 'center',
								halign: 'center'
							}, {
								field: 'operationcao',
								title: '操作',
								align: 'center',
								halign: 'center',
								formatter: daichuliFormatter
							}, ]
						});
						//历史
						$('#lishiFinish').bootstrapTable({
							method: 'post',
							striped: true,
							cache: false,
							uniqueId: "id",
							strictSearch: false,
							showToggle:false,
							cardView: false,
							detailView: false,
							clickToSelect: true,
							singleSelect:false,
							height: $(window).height()-50,
//				 			toolbar: '#toolbar',
							url: '<%=basePath%>activiti/queryFlow/querylishi.action',
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
								field: 'name',
								title: '标题',
								align: 'left',
								halign: 'center'
							}, {
								field: 'createUser',
								title: '创建人',
								align: 'center',
								halign: 'center',
								formatter: userFormatter
							}, {
								field: 'createTime',
								title: '创建时间',
								align: 'center',
								halign: 'center'
							}, {
								field: 'processStarter',
								title: '提交人',
								align: 'center',
								halign: 'center',
								formatter: userFormatter
							}, {
								field: 'completeTime',
								title: '提交时间',
								align: 'center',
								halign: 'center'
							}, {
								field: 'operationcao',
								title: '操作',
								align: 'center',
								halign: 'center',
								formatter: operateFormatterFinish
							}, ]
						});
						//我收到的催办
						$('#shoudaoFinish').bootstrapTable({
							method: 'post',
							striped: true,
							cache: false,
							uniqueId: "id",
							strictSearch: false,
							showToggle:false,
							cardView: false,
							detailView: false,
							clickToSelect: true,
							singleSelect:false,
							url: '<%=basePath%>activiti/queryFlow/listWOcuiban.action',
							queryParams: queryParams,
							queryParamsType: '',
							contentType: "application/x-www-form-urlencoded",
							formatLoadingMessage: function() {
								return "请稍等，正在加载中...";
							},
							formatNoMatches: function() {
								return '无符合条件的记录';
							},
							columns: [{
								checkbox: true
							}, {
								field: 'porcedurename',
								title: '标题',
								align: 'left',
								halign: 'center'
							}, {
								field: 'remindernum',
								title: '创建人',
								align: 'center',
								halign: 'center',
								formatter: userFormatter
							}, {
								field: 'remindtime',
								title: '创建时间',
								align: 'center',
								halign: 'center'
							}, {
								field: 'remindtime',
								title: '提交人',
								align: 'center',
								halign: 'center',
								formatter: userFormatter
							}, {
								field: 'remindtime',
								title: '提交时间',
								align: 'center',
								halign: 'center'
							}, {
								field: 'operationcao',
								title: '操作',
								align: 'center',
								halign: 'center',
								formatter: shoudaocuibanFormatter
							}, ]
						});
					}
				});
				
				
			})

			function queryParams(params) {
				console.log(params);
				return {
					rows: params.pageSize,
					page: params.pageNumber
				}
			}

			function nameFormatter(value, row, index) {
				return nameMap[value];
			}
			function userFormatter(value, row, index) {
				return userMap[value];
			}

			function stateFormatter(value, row, index) {
				return '运行中';
			}

			function stateFormatterFinish(value, row, index) {
				return '完成';
			}

			function operateFormatter(value, row, index) {
				return [
						'<div class="btn-group btn-group-xs" role="group">' +
						'<a type="button" class="btn btn-primary" href="javascript:void(0)" onclick="zhongzhi(\''+row.processInstanceId+'\')">终止</a>' +
						'<a type="button" class="btn btn-danger" href="javascript:void(0)" onclick="cuiban(\''+row.processInstanceId+'\',\''+row.name+'\',\''+row.attr2+'\',\''+row.code+'\',\''+row.assignee+'\')">催办</a>' +
						'<a type="button" class="btn btn-success" href="javascript:void(0)" onclick="query(\''+row.processInstanceId+'\')">详情</a>' +
						'</div>'
				]
			}
			function query(processInstanceId){
				AddOrShowEast1(processInstanceId,"<%=basePath%>activiti/operation/viewHistory.action");
			}
			function zhongzhi(businessKey) {
				$.ajax({
					url: "<%=basePath%>activiti/operation/endProcessInstance.action?id=" + businessKey,
					type: 'post',
					async: false,
					success: function(data) {
						console.info(data);
						if(data.resMsg=="success"){
							$('#demoGrid').bootstrapTable('refresh');
						}
					}
				});
			}
			
			function cuiban(processInstanceId,name,attr2,code,assignee) {
				processInstanceIdView=processInstanceId;
				nameView=name;
				attr2View=attr2;
				codeView=code;
				assigneeView=assignee;
				AddDeptdilogs("催办信息", "<%=basePath%>activiti/queryFlow/viewremind.action");
			}
			function cuibanSubmit(remindcontent){
				var processInstanceId=processInstanceIdView;
				var name=nameView;
				var attr2=attr2View;
				var code=codeView;
				var assignee=assigneeView;
				$.ajax({
					url: "<%=basePath%>activiti/queryFlow/savecuiban.action",
 					data:{name:name,processInstanceId:processInstanceId,attr2:attr2,code:code,assignee:assignee,remindcontent:remindcontent},
 					type: 'post',
 					async: false,
 					success: function(data) {
 						$.messager.alert('提示', "催办成功！");
 						$('#demoGrid').bootstrapTable('refresh');
 					}
 				});
			}
			function AddDeptdilogs(title, url) {
				$('#dialogDivId').dialog({    
				    title: title,    
				    width: '30%',    
				    height:'30%',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				});    
			}
			function load(){
				window.location.href ="<%=basePath%>activiti/queryFlow/queryFlowView.action";
			}
			function operateFormatterFinish(value, row, index) {
				return [
						'<div class="btn-group btn-group-xs" role="group">' +
						'<a type="button" class="btn btn-success" href="javascript:void(0)" onclick="query(\''+row.processInstanceId+'\')">详情</a>' +
						'</div>'
				]
			}

			function caogaoFormatter(value, row, index) {
				return [
					'<div class="btn-group btn-group-xs" role="group">' +
					'<a type="button" class="btn btn-primary" href="javascript:void(0)" onclick="zhongzhi(\''+row.processInstanceId+'\')">终止</a>' +
					'<a type="button" class="btn btn-success" href="javascript:void(0)" onclick="add(\''+row.businessKey+'\')">编辑</a>' +
					'</div>'
				]
			}
			function shoudaocuibanFormatter(value, row, index){
				return [
					'<div class="btn-group btn-group-xs" role="group">' +
					'<a type="button" class="btn btn-primary" href="javascript:void(0)" onclick="updateyidu(\''+row.procedureId+'\',\''+row.procedureId+'\')">已读</a>' +
					'<a type="button" class="btn btn-success" href="javascript:void(0)" onclick="add(\''+row.businessKey+'\')">回复</a>' +
					'</div>'
				]
			}
			function updateyidu(){
				
			}
			function daichuliFormatter(value, row, index){
				return [
						'<div class="btn-group btn-group-xs" role="group">' +
						'<a type="button" class="btn btn-success" href="javascript:void(0)" onclick="shenpi(\''+row.id+'\')">审批</a>' +
						'</div>'
					]
			}
			function shenpi(id){
				AddOrShowEast(id,"<%=basePath%>activiti/humanTask/viewTaskForm.action");
			}
			function add(businessKey){
				AddOrShowEast(businessKey,"<%=basePath%>activiti/humanTask/viewTaskForm.action");
			}
			function AddOrShowEast(id, url) {
				var id = id;
				var url = url;
				var name = '查看';
				var width = 1550;
				var height = 800;
				var top = (window.screen.availHeight-30-height)/2;
				var left = (window.screen.availWidth-10-width)/2;
				if($("#winOpenFrom").length<=0){  
					var form = "<form id='winOpenFrom' action='"+url+"' method='post' target='"+name+"'>" +
							"<input type='hidden' id='winOpenFromInpId' name='humanTaskId'/></form>";
					$("body").append(form);
				} 
				$('#winOpenFromInpId').val(id);
				openWindow('about:blank',name,width,height,top,left);
				$('#winOpenFrom').prop('action',url);
				$("#winOpenFrom").submit();
			}
			function AddOrShowEast1(processInstanceId, url) {
				var processInstanceId = processInstanceId;
				var url = url;
				var name = '查看';
				var width = 1550;
				var height = 800;
				var top = (window.screen.availHeight-30-height)/2;
				var left = (window.screen.availWidth-10-width)/2;
				if($("#winOpenFrom1").length<=0){  
					var form = "<form id='winOpenFrom1' action='"+url+"' method='post' target='"+name+"'>" +
							"<input type='hidden' id='winOpenFromInpId1' name='id'/></form>";
					$("body").append(form);
				} 
				$('#winOpenFromInpId1').val(processInstanceId);
				openWindow('about:blank',name,width,height,top,left);
				$('#winOpenFrom1').prop('action',url);
				$("#winOpenFrom1").submit();
			}
			function chuliFormatter(value, row, index) {
				return [
					'<div class="btn-group btn-group-xs" role="group">' +
					'<a type="button" class="btn btn-danger">处理</a>' +
					'</div>'
				]
			}
			//打开窗口
			function openWindow(url,name,width,height,top,left){
				window.open(url, name, 'height=' + height + ',innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no'); 
			}
			function faqiFormatter(value, row, index){
				return [
						'<div class="btn-group btn-group-xs" role="group">' +
						'<a type="button" class="btn btn-success" href="javascript:void(0)" onclick="faqi(\''+row.id+'\',\''+row.categoryCode+'\')">申请</a>' +
						'</div>'
					]
			}
			function faqi(id,categoryCode){
				if("828281905dd66246015dd6b620f20001" == categoryCode){
					AddOrShowEast1(id,"<%=basePath%>oa/extend/sickLeavePrivateList.action");
				}else{
					AddOrShowEast(id,"<%=basePath%>activiti/operation/viewStartForm.action?id="+ id);
				}
			}
			function searchFrom(){
				var startTime = $('#startTime').val();
				var endTime = $('#endTime').val();
				var biaoti = $('#biaoti').textbox('getValue');
				$('#gaoGrid').bootstrapTable('refresh',{startTime:startTime,endTime:endTime,param:biaoti});
			}
			function searchTuijianFrom(){
				var tuiJianStartTime = $('#tuiJianStartTime').val();
				var tuiJianEndTime = $('#tuiJianEndTime').val();
				var tuiJianBiaoti = $('#tuiJianBiaoti').textbox('getValue');
				var param = {startTime:tuiJianStartTime,endTime:tuiJianEndTime,param:tuiJianBiaoti};
				$('#tuiGrid').bootstrapTable('refresh',{
					query:param,
					});
			}
		</script>
	</body>

</html>