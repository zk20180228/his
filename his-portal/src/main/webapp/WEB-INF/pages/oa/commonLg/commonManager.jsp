<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp" %>
<meta charset="UTF-8">
		<title></title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<script src="<%=basePath%>javascript/js/bootstrap3-editable/js/bootstrap-editable.min.js" type="text/javascript" charset="utf-8"></script>
		<style type="text/css">
			* {
				padding: 0;
				margin: 0;
				box-sizing: border-box;
			}
			
			.container-fluid {
				height: 100%;
			}
			
			.panel {
				height: 100%;
			}
			
			.panel-body {
				padding: 15px;
			}
			
			.container-fluid>div {
				padding: 0;
				height: 100%;
			}
			
			.tableInfo {
				height: 100%;
			}
			
			.panel-body.info {
				padding: 15px;
			}
			
			.fixed-table-toolbar .pull-right.search input {
				width: auto;
				height: auto;
			}
			
			#myTab>li {
				width: 100%;
				text-align: center;
			}
			
			#myTab>li.active a {
				background-color: #0081C2;
				color: #FFFFFF;
			}
			
			.fixed-table-container tbody .active td {
				background-color: #b3e7fa !important;
			}
			
			.myinput {
			    display: inline-block;
			    width: auto;
			    white-space: normal;
			    zoom: 1;
			}
			.mybtn {
				vertical-align: top;
			    display: inline-block;
			    width: auto;
			    white-space: normal;
			    zoom: 1;
			    float: right;
			}
			
			#mybtnyus{
				color: #fff;
    			background-color: #428bca;
    			border-color: #357ebd;
			}
			#addExmybtnyus {
				color: #fff;
    			background-color: #428bca;
    			border-color: #357ebd;
			}
			#myContent{
				width: 200px;
			}
			#addExmyContent {
				width: 200px;
			}
		</style>

</head>
	<body>
		<div class="panel-body" style="padding-bottom:0px;">
			<!--全部都支持的方式-->
			<div id="toolbar" class="btn-group">
				<button id="btn_add"  type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
            </button>
			</div>
			<table id="tb_departments"></table>
		</div>

		<div class="modal fade" id="AddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="width: 90%;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title">提示详情信息</h4>
					</div>
					<div class="modal-body">
						<div class="col-md-12" style="text-align:left;">
							<button type="button" id="seveUsefulExpressions" class="btn btn-primary bg borderColor">
								<span class="glyphicon glyphicon-save" aria-hidden="true"></span> 保存
							</button>
						</div>
						<div class="clearfix">
							<div class="col-md-4">
								<table id="UserTable"></table>
							</div>
							<div class="col-md-4">
								<table id="FormTable"></table>
							</div>
							<div class="col-md-4">
								<div id="AddInfotoolbar" class="btn-group">
									<button id="AddInfotoolbar_btn" type="button" class="btn btn-default">
				               			 <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
					           	 	</button>
									<button id="delInfotoolbar_Btn" type="button" class="btn btn-default">
					             	   <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
					           		</button>
								</div>
								<table id="AddInfoTable"></table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" id="EditModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="width: 90%;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title">提示详情信息</h4>
					</div>
					<div class="modal-body">
						<div class="clearfix">
							<div class="col-md-6">
								<table id="EditFormTable"></table>
							</div>
							<div class="col-md-6">
								<div id="EditInfotoolbar" class="btn-group">
<!-- 									<button id="EditAddInfotoolbar_btn"  type="button" class="btn btn-default"> -->
<!-- 				               			<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增 -->
<!-- 					           	 	</button> -->
									<button id="EditdelInfotoolbar_Btn" type="button" class="btn btn-default">
					             	    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
					           		</button>
								</div>
								<table id="EditAddInfoTable"></table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		
		
		
		
	</body>
</html>
<script type="text/javascript">
	var oTable;
	$(function() {
		//1.初始化Table
		oTable = new TableInit();
		oTable.Init();
	
		//2.初始化Button的点击事件
		var oButtonInit = new ButtonInit(oTable);
		oButtonInit.Init();
	});

	var TableInit = function() {
		var oTableInit = new Object();
		//初始化Table
		oTableInit.Init = function() {
			$('#tb_departments').bootstrapTable({
				url: "<%=basePath%>baseinfo/employee/queryEmployee.action", //请求后台的URL（*）
				method: 'post', //请求方式（*）
				contentType : "application/x-www-form-urlencoded",
				toolbar: '#toolbar', //工具按钮用哪个容器
				striped: true, //是否显示行间隔色
				cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
				pagination: true, //是否显示分页（*）
				sortable: false, //是否启用排序
				sortOrder: "asc", //排序方式
				queryParams: oTableInit.queryParams, //传递参数 也可以直接写
				sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*） 实时性要求不高且数据少于500条可以使用客户端分页
				pageNumber: 1, //初始化加载第一页，默认第一页
				pageSize: 20, //每页的记录行数（*）
				pageList: [20, 30, 50, 100], //可供选择的每页的行数（*）
				search: true, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，只能严格匹配
				strictSearch: false, // 严格匹配？
				showColumns: true, //是否显示所有的列
				showRefresh: true, //是否显示刷新按钮
				minimumCountColumns: 2, //最少允许的列数
				clickToSelect: true, //是否启用点击选中行
				height: $("body").height()-30, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
				uniqueId: "account", //每一行的唯一标识，一般为主键列
				showToggle: true, //是否显示详细视图和列表视图的切换按钮
				cardView: oTableInit.issize(), //是否显示详细视图 移动平台默认显示
				detailView: true, //是否显示父子表
				onExpandRow: function(index, row, $detail) { //注册子表事件
					oTableInit.InitSubTable(index, row, $detail);
				},
				columns: [{
						field: 'name',
						title: '姓名'
					}, {
						field: 'jobNo',
						title: '账号'
					},
					{
						title: '操作',
						formatter: function(value, row, index) { // 过滤器函数
							return "<a onclick = parentEdit('" + row.jobNo + "')>修改</a>";
						}
					}
				],

			});
		};

		oTableInit.issize = function() {
			if($("body").width() < 800) {
				return true;
			} else {
				return false;
			}
		}
		oTableInit.InitSubTable = function(index, row, $detail) {
			var account = row.jobNo;
			var cur_table = $detail.html('<table></table>').find('table');
			$(cur_table).bootstrapTable({
				url: '<%=basePath%>oa/commonManager/findFrom.action',
				method: 'post',
				contentType : "application/x-www-form-urlencoded",
				queryParams: {
					account: account
				},
				ajaxOptions: {
					account: account
				},
				pagination: false, //是否显示分页（*）
				clickToSelect: true,
				detailView: true,
				uniqueId: "id",
				pageSize: 10,
				pageList: [10, 25],
				columns: [ {
						field: 'tableCode',
						title: '表单Code'
					}, {
						field: 'tableName',
						title: '表单名称'
					}
				],
				onExpandRow: function(index, row, $detail) { //注册子表事件
					oTableInit.InitSubSubTable(index, row, $detail,account);
				},
				//无线循环取子表，直到子表里面没有记录
			});
		}
		oTableInit.InitSubSubTable = function(index, row, $detail,account) {
			var account = account
			var tableCode = row.tableCode;
			var cur_table = $detail.html('<table></table>').find('table');
			$(cur_table).bootstrapTable({
				url: '<%=basePath%>oa/commonManager/findMyCommon.action',
				method: 'post',
				pagination: false, //是否显示分页（*）
				contentType : "application/x-www-form-urlencoded",
				queryParams: {
					tableCode: tableCode,
					account:account
				},
				ajaxOptions: {
					tableCode: tableCode,
					account:account
				},
				clickToSelect: true,
				sidePagination: "client", //分页方式：client客户端分页，server服务端分页（*） 实时性要求不高且数据少于500条可以使用客户端分页
				pageSize: 10,
				pageList: [10, 25],
				columns: [{
					field: 'common',
					title: '常用语'
				}]
			});
		}
		oTableInit.UserTable = function() {
			$('#UserTable').bootstrapTable({
				url: "<%=basePath%>baseinfo/employee/queryEmployee.action", //请求后台的URL（*）
				method: 'post', //请求方式（*）
				contentType : "application/x-www-form-urlencoded",
// 				toolbar: '#toolbar', //工具按钮用哪个容器
				striped: true, //是否显示行间隔色
				cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
				pagination: true, //是否显示分页（*）
				sortable: false, //是否启用排序
				sortOrder: "asc", //排序方式
				queryParams: oTableInit.queryParams, //传递参数 也可以直接写
				sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*） 实时性要求不高且数据少于500条可以使用客户端分页
				pageNumber: 1, //初始化加载第一页，默认第一页
				pageSize: 10, //每页的记录行数（*）
				pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
				search: true, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，只能严格匹配
				strictSearch: false, // 严格匹配？
				showColumns: true, //是否显示所有的列
				showRefresh: true, //是否显示刷新按钮
				minimumCountColumns: 2, //最少允许的列数
				clickToSelect: true, //是否启用点击选中行
				height: 530, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
				uniqueId: "account", //每一行的唯一标识，一般为主键列
				showToggle: true, //是否显示详细视图和列表视图的切换按钮
				cardView: oTableInit.issize(), //是否显示详细视图 移动平台默认显示
				detailView: false, //是否显示父子表
				columns: [
					{
						checkbox: true
					}, {
						field: 'name',
						title: '姓名'
					}, {
						field: 'jobNo',
						title: '账号'
					},
					{
						title: '操作',
						formatter: function(value, row, index) { // 过滤器函数
							return "<a onclick = parentEdit(" + row.jobNo + ")>修改</a>";
						}
					}
				],

			});
		};
		oTableInit.FormTable = function() {
			$("#FormTable").bootstrapTable({
				url: '<%=basePath%>oa/formInfo/getValidFormInfo.action',   
				method: 'post', //请求方式（*）
				//toolbar: '#toolbar', //工具按钮用哪个容器
				//striped: true, //是否显示行间隔色
				cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
				pagination: true, //是否显示分页（*）
				//sortable: true, //是否启用排序
				//sortOrder: "asc", //排序方式
				//queryParams: oTableInit.queryParams, //传递参数 也可以直接写
				sidePagination: "client", //分页方式：client客户端分页，server服务端分页（*） 实时性要求不高且数据少于500条可以使用客户端分页
				pageNumber: 1, //初始化加载第一页，默认第一页
				pageSize: 10, //每页的记录行数（*）
				pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
				search: true, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，只能严格匹配
				strictSearch: false, // 严格匹配？
				showColumns: true, //是否显示所有的列
				showRefresh: true, //是否显示刷新按钮
				minimumCountColumns: 2, //最少允许的列数
				clickToSelect: true, //是否启用点击选中行
				height: 530, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
				uniqueId: "id", //每一行的唯一标识，一般为主键列
				//showToggle: true, //是否显示详细视图和列表视图的切换按钮
				cardView: oTableInit.issize(), //是否显示详细视图 移动平台默认显示
				//detailView: true, //是否显示父子表
				columns: [
					{
						checkbox: true
					}, {
						field: 'code',
						title: '表单Code'
					}, {
						field: 'name',
						title: '表单名称'
					}
				]
			});
		}

		oTableInit.AddInfoTable = function() {
			$("#AddInfoTable").bootstrapTable({
				data:addExmybtnyusData,
				toolbar: '#AddInfotoolbar', //工具按钮用哪个容器
				//striped: true, //是否显示行间隔色
				cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
				pagination: false, //是否显示分页（*）
				//sortable: true, //是否启用排序
				//sortOrder: "asc", //排序方式
				//queryParams: oTableInit.queryParams, //传递参数 也可以直接写
				pageNumber: 1, //初始化加载第一页，默认第一页
				pageSize: 10, //每页的记录行数（*）
				pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
				search: true, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，只能严格匹配
				strictSearch: false, // 严格匹配？
				showColumns: true, //是否显示所有的列
				showRefresh: true, //是否显示刷新按钮
				minimumCountColumns: 2, //最少允许的列数
				clickToSelect: true, //是否启用点击选中行
				height: 530, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
// 				uniqueId: "id", //每一行的唯一标识，一般为主键列
				//showToggle: true, //是否显示详细视图和列表视图的切换按钮
				cardView: oTableInit.issize(), //是否显示详细视图 移动平台默认显示
				//detailView: true, //是否显示父子表
				columns: [
					{
						checkbox: true
					}, {
						field: 'usefulExpressions',
						title: '常用语',
						editable: true
					}
				],

			});
		}
		oTableInit.EditFormTable = function(id,tmpOne) {
			$("#EditFormTable").bootstrapTable({
				url: '<%=basePath%>oa/commonManager/findFrom.action',
				method: 'post',
				contentType : "application/x-www-form-urlencoded",
				queryParams: {
					account: id
				},
				ajaxOptions: {
					account: id
				},
				//toolbar: '#EditInfotoolbar', //工具按钮用哪个容器
				//striped: true, //是否显示行间隔色
				cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
				pagination: true, //是否显示分页（*）
				//sortable: true, //是否启用排序
				//sortOrder: "asc", //排序方式
				//queryParams: oTableInit.queryParams, //传递参数 也可以直接写
				sidePagination: "client", //分页方式：client客户端分页，server服务端分页（*） 实时性要求不高且数据少于500条可以使用客户端分页
				pageNumber: 1, //初始化加载第一页，默认第一页
				pageSize: 10, //每页的记录行数（*）
				pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
				search: true, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，只能严格匹配
				strictSearch: false, // 严格匹配？
				showColumns: true, //是否显示所有的列
				showRefresh: true, //是否显示刷新按钮
				minimumCountColumns: 2, //最少允许的列数
				clickToSelect: true, //是否启用点击选中行
				height: 530, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
				uniqueId: "id", //每一行的唯一标识，一般为主键列
				//showToggle: true, //是否显示详细视图和列表视图的切换按钮
				cardView: oTableInit.issize(), //是否显示详细视图 移动平台默认显示
				//detailView: true, //是否显示父子表
				columns: [{
						field: 'id',
						visible: false
					},{
						radio:true
					},{
						field: 'tableCode',
						title: '表单Code'
					}, {
						field: 'tableName',
						title: '表单名称'
					}
				],
				onClickRow: function(row, $element) {
					$("#EditAddInfoTable").bootstrapTable("destroy");
					oTableInit.EditAddInfoTable(id,row.tableCode);
				},
				onLoadSuccess: function(data) {
					$("#EditAddInfoTable").bootstrapTable("destroy")
					if(data.length>0){
						
						oTableInit.EditAddInfoTable(id,data[0].tableCode)
						//$("#EditAddInfoTable").bootstrapTable("check")
					}else{
						oTableInit.EditAddInfoTable(id,null)
					}
					
					
					if(data.length>0){
						if(tmpOne){
							$("#EditFormTable").bootstrapTable("check","0")
						}
					}
				}
			});
		}
		oTableInit.EditAddInfoTable = function(id,fromcode) {
			console.log(id)
			$("#EditAddInfoTable").bootstrapTable({
				url: '<%=basePath%>oa/commonManager/findMyCommon.action',
				method: 'post',
				contentType : "application/x-www-form-urlencoded",
				queryParams: {
					account: id,
					tableCode:fromcode
				},
				ajaxOptions: {
					account: id,
					code:fromcode
				},
				toolbar: '#EditInfotoolbar', //工具按钮用哪个容器
				//striped: true, //是否显示行间隔色
				cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
				pagination: true, //是否显示分页（*）
				//sortable: true, //是否启用排序
				//sortOrder: "asc", //排序方式
				//queryParams: oTableInit.queryParams, //传递参数 也可以直接写
				sidePagination: "client", //分页方式：client客户端分页，server服务端分页（*） 实时性要求不高且数据少于500条可以使用客户端分页
				pageNumber: 1, //初始化加载第一页，默认第一页
				pageSize: 10, //每页的记录行数（*）
				pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
				search: true, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，只能严格匹配
				strictSearch: false, // 严格匹配？
				showColumns: true, //是否显示所有的列
				showRefresh: true, //是否显示刷新按钮
				minimumCountColumns: 2, //最少允许的列数
				clickToSelect: true, //是否启用点击选中行
				height: 530, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
				uniqueId: "id", //每一行的唯一标识，一般为主键列
				//showToggle: true, //是否显示详细视图和列表视图的切换按钮
				cardView: oTableInit.issize(), //是否显示详细视图 移动平台默认显示
				//detailView: true, //是否显示父子表
				columns: [{
					checkbox: true
				}, {
					field: 'common',
					title: '常用语',
					formatter: function(value, row, index) { // 过滤器函数
						return '<a data-type="textarea" data-pk="1" data-placeholder="请输入常用语！" data-title="修改" class="editable editable-pre-wrapped editable-click" data-original-title="" title="">' + value + '</a>'
					}
				}],
				onClickRow: function(row, $element) {
					curRow = row;
				},
				onLoadSuccess: function(aa, bb, cc) {
					$("#EditAddInfoTable a").editable({
						url: function(params) {
							var sName = $(this).attr("name");
							curRow["newCommon"] = params.value;
							$.ajax({
								type: 'POST',
								url: "<%=basePath%>oa/commonManager/editCommon.action",
								data: curRow,
								dataType: 'JSON',
								success: function(data, textStatus, jqXHR) {
								},
								error: function() {
									alert("error");
								}
							});
						},
						type: 'text',
						validate: function (value) { //字段验证
			                if (!$.trim(value)) {
			                    return '不能为空';
			                }
			            }
					});
				},

			});
		}
		//得到查询的参数
		oTableInit.queryParams = function(params) {
			return {  //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
				rows: params.limit, //页面大小
				page: (params.offset + params.limit) / params.limit	 , //页码
				ename: params.search, // 后台搜索
			}
		};
		return oTableInit;
	};

	var parentEditItem = false
	function parentEdit(id) {
		$("#EditModal").modal("show").off("shown.bs.modal").on('shown.bs.modal', function () {
			if(parentEditItem) {
				$("#EditFormTable").bootstrapTable("destroy")
				oTable.EditFormTable(id)
			} else {
				parentEditItem = true
				oTable.EditFormTable(id,true)
				//oTable.EditAddInfoTable(id)
				var str = '<div class="popover-content">' +
								'<div class="control-group form-group">'+
									'<div class="myinput">'+
										'<textarea id = "myContent" class="form-control input-large" placeholder="请输入常用语！" rows="7"></textarea>'+
									'</div>'+
									'<div class="mybtn">'+
										'<button type="button" onclick="mybtnyus()" id = "mybtnyus" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-ok"></i></button>'+
										'<button type="button" onclick="mybtnno()" id = "mybtnno" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-remove"></i></button>'+
									'</div>'+
								'</div>'+
						'</div>';
				$("#EditAddInfotoolbar_btn").on("click",function(){
					$(this).popover('toggle')
				}).popover({
					  	title:"常用语添加",
					  	html:true,
						content:str,
						placement:"bottom",
						trigger: 'manual'
					})
			}
		})
	}
	
	function addEx (){
		
	}
	
	
	var addExmybtnyusData = []
	function addExmybtnyus (){
		var text= $("#addExmyContent").val(); 
		if(!text){
			$.alert("提示","请输入常用语！")
		}else{
			console.log(addExmybtnyusData)
			addExmybtnyusData.push({"usefulExpressions":text})
			 $("#addExmyContent").val("")
			 console.log(addExmybtnyusData)
			 $("#AddInfoTable").bootstrapTable("load",addExmybtnyusData)
		}
	}
	
	function addExmybtnno (){
		$("#AddInfotoolbar_btn").popover('hide')
	}
	
	
	function mybtnyus (id){
		var fromData = $("#EditFormTable").bootstrapTable("getAllSelections")
		fromData = fromData[0]
		console.log(id)
		$.ajax({
			type:"get",
			url:"xxxxxxasdasd",
			data:fromData,
			dataType: 'JSON',
			async:true,
			success:function(data){
				$("#myContent").val(null);
				$("#EditAddInfoTable").bootstrapTable("refresh");
			},
			error:function(){
				$.alert("提示","网络异常")
			}
		});
	}
	
	function mybtnno (){
		$("#EditAddInfotoolbar_btn").popover('hide')
	}

	function formEdit(id) {
		$.alert("提示", "建设中！！" + id + "")
	}

	function usefulExpressionsDel(id) {
		$.alert("提示", "删除常用语" + id + "")
	}
	var ButtonInit = function(table) {
		var oInit = new Object();
		var postdata = {};
		var $this = this
		oInit.Init = function() {
			$("#btn_add").on("click", function() {
				$("#AddModal").modal("show").off("shown.bs.modal").on('shown.bs.modal', function () {
					if(!$this.modal) {
						$this.modal = true
						table.UserTable()
						table.FormTable()
						table.AddInfoTable()
						$("#seveUsefulExpressions").on("click", function() {
							var UserTableData = $("#UserTable").bootstrapTable("getAllSelections")
							var FormTableData = $("#FormTable").bootstrapTable("getAllSelections")
							var AddInfoTable = $("#AddInfoTable").bootstrapTable("getAllSelections")
							var code ='' ;//获取code字符串
							var common = '' ;//获取common字符串
							var account = '';
							var name ='' ;
							for(var i = 0;i < UserTableData.length; i++) {
								if(i != UserTableData.length -1){
									account += UserTableData[i].jobNo +"#";
									name += UserTableData[i].name +"#";
								}else{
									account += UserTableData[i].jobNo 
									name += UserTableData[i].name 
									}
							}	
							
							for(var i = 0;i < FormTableData.length; i++) {
								if(i != FormTableData.length -1){
									code += FormTableData[i].code +"#";
								}else{
									code += FormTableData[i].code 
									}
							}	
							
							for(var i = 0;i < AddInfoTable.length; i++) {
								if(i != AddInfoTable.length -1){
									common += AddInfoTable[i].usefulExpressions +"#";
								}else{
									common += AddInfoTable[i].usefulExpressions 
									}
							}				
							if(UserTableData.length > 0 && FormTableData.length > 0 && AddInfoTable.length > 0) {
								$.ajax({
									type: "post",
									url: "<%=basePath%>oa/commonManager/saveCommon.action",
									data: {
										account: account,
										code: code,
										name: name,
										common: common
									},
									async: true,
									success: function(data) {
										$("#AddInfoTable").bootstrapTable("refresh")
										if(data.message = "success") {
											$.alert("提示", "操作成功！", function() {
												$("#AddModal").modal("hide")
											})
										} else {
											$.alert("提示", "操作失败！")
										}
									},
									error: function() {
										$.alert("提示", "网络超时！")
									}
								});
							} else {
								$.alert("提示", "请选择要添加的信息")
							}
						})
						
						
						var str = '<div class="popover-content">' +
						'<div class="control-group form-group">'+
							'<div class="myinput">'+
								'<textarea id = "addExmyContent" class="form-control input-large" placeholder="请输入常用语！" rows="7"></textarea>'+
							'</div>'+
							'<div class="mybtn">'+
								'<button type="button" onclick="addExmybtnyus()" id = "addExmybtnyus" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-ok"></i></button>'+
								'<button type="button" onclick="addExmybtnno()" id = "addExmybtnno" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-remove"></i></button>'+
							'</div>'+
						'</div>'+
						'</div>';
						$("#AddInfotoolbar_btn").on("click",function(){
							$(this).popover('toggle')
						}).popover({
							title:"常用语添加",
							html:true,
							content:str,
							placement:"bottom",
							trigger: 'manual'
						})
						
					} else {
						$("#AddModal input").val("")
						$("#UserTable").bootstrapTable("refresh")
						$("#FormTable").bootstrapTable("refresh")
						$("#AddInfoTable").bootstrapTable("refresh")
					}
					
				})
				
				
			
			})
			$("#btn_delete").on("click", function() {
				var data = $("#tb_departments").bootstrapTable("getAllSelections")
				console.log(data)
				if(data.length > 0) {
					$.ajax({
						type: "post",
						url: "xxx",
						async: true,
						success: function(data) {
							if(data.message = "success") {
								$.alert("提示", "删除成功！")
								$("#tb_departments").bootstrapTable("refresh")
							} else {
								$.alert("提示", "删除失败！")
							}
						},
						error: function() {
							$.alert("提示", "网络超时删除失败！")
						}
					});
				} else {
					$.alert("提示", "请选择要删除的信息!")
				}
			})
		};
		return oInit;
	};
	
	$('body').on('click', function(event) {  
 	   	var target = $(event.target); // One jQuery object instead of 3  
    	if (!target.hasClass('popover')  && target.parents('.popover').length === 0    && target.attr("type") !== "button") {  
       			$("#EditAddInfotoolbar_btn").popover('hide');  
       			$("#AddInfotoolbar_btn").popover('hide');
   		}
	})
	
	
	$("#EditdelInfotoolbar_Btn").on("click",function(){
		var selectInof =  $("#EditAddInfoTable").bootstrapTable("getAllSelections")
		var str = ""
		for(var i = 0 ;i< selectInof.length;i++){
			str+= selectInof[i]["id"]+","
		}
		str =  str.substr(0,str.length-1) 
		$.ajax({
			url : "<%=basePath%>oa/commonLg/delCommon.action?ids="+ str,
			type : 'post',
			success : function(data) {
				$("#EditAddInfoTable").bootstrapTable("refresh")
				$("#EditFormTable").bootstrapTable("refresh")
			}
		});
	})
</script>