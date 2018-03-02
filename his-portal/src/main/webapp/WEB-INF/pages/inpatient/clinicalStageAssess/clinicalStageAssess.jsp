<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ include file="/common/metas.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>阶段评估</title>
</head>

<head>
<style type="text/css">
.panel-header {
	border-top: 0;
}
</style>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div id='p'
			data-options="region:'west',title:'住院科室',split:true,border:true,tools:'#toolSMId'"
			style="width: 300px;">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false"
					style="height: 35px; padding-top: 5px">
					&nbsp;<input type="text" class="easyui-textbox"
						id="searchTreeInpId" data-options="prompt:'请输入关键字'"
						style="width: 200px;" /> <a href="javascript:void(0)"
						class="easyui-linkbutton" data-options="iconCls:'icon-search'"
						onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a>
				</div>
				<div id="treeDiv" data-options="region:'center',border:false"
					style="width: 85%">
					<ul id="tDt">加载中...</ul>
				</div>
				<div id="toolSMId">
					<a href="javascript:void(0)" onclick="refresh()"
						class="icon-reload"></a> <a href="javascript:void(0)"
						onclick="collapseAll()" class="icon-fold"></a> <a
						href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
				</div>
			</div>
		</div>
		<div data-options="region:'center',border:false"
			style="width: 79%; height: 100%">
			<div id="divLayout" class="easyui-layout"
				data-options="border:false,fit:true">
				<div
					style="width: 100%; height: 40px; border-top: 0; overflow: hidden">
					<form id="search" method="post">
						<table style="width: 100%; border: 0px; padding: 5px">
							<tr style="width: 100%">
								<td style="width: 100%"><a href="javascript:void(0)"
									onclick="addAssess()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" >添加</a> <a
									href="javascript:void(0)" onclick="updateAssess()"
									class="easyui-linkbutton" data-options="iconCls:'icon-edit'" >修改</a> <a href="javascript:void(0)"
									onclick="approveAssess()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >审核</a></td>
							</tr>
						</table>
					</form>
				</div>
				<div style="height: calc( 100% - 40px );" >
					<table id="list" data-options="pagination:true,method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
						<thead>
							<tr>
								<th field="ck" checkbox="true" data-options="width:'5%'"></th>
								<th data-options="field:'stageId',width:'10%',formatter:flagStage">阶段</th>
								<th data-options="field:'accessResult',width:'5%'">评估结果</th>
								<th data-options="field:'accessDate',width:'5%'">评估时间</th>
								<th data-options="field:'accessUser',width:'10%',formatter:flagUser">评估人</th>
								<th data-options="field:'roleFlag',width:'10%'">角色</th>
								<th data-options="field:'days',width:'10%'">天数</th>
								<th data-options="field:'accessCheckUser',width:'10%',formatter:flagUser">审核人</th>
								<th data-options="field:'accessCheckDate',width:'10%'">审核时间</th>
								<th data-options="field:'accessCheckInfo',width:'10%'">审核信息</th>
							</tr>
						</thead>
					</table>
				</div>
				<div id="addAssessWindow" class="easyui-window" title="添加评估" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:425px;height:180px;">
					<div data-options="region:'north'" style="width:400px ;padding:8px 5px 5px 5px;">
					    <table class="honry-table" cellpadding="0" cellspacing="0"   id="table1"
							border="0" style="margin-left:auto;margin-right:auto;margin-down:auto; overflow: auto;" >
							<tr>
								<td class="honry-lable">阶段：</td>
								<td>
									<input id="stageAssess"  class="easyui-textbox" readonly="readonly"></input>
								</td>
							</tr>
							<tr>
								<td class="honry-lable">天数：</td>
								<td>
									<input id="dayNums"  class="easyui-numberbox"></input>
								</td>
							</tr>
							<tr>
								<td class="honry-lable">评估结果：</td>
								<td>
									<input id="assessResult"  class="easyui-textbox"></input>
								</td>
							</tr>
						</table>
						<div style="text-align: center;padding: 5px">
							<a href="javascript:void(0)" onclick="saveAddAssess()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-ok">确定</a>
							<a href="javascript:void(0)" onclick="closeAddAssess()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-no">关闭</a>
						</div>
					</div>
				</div>
				<div id="approveWindow" class="easyui-window" title="审核内容" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:425px;height:120px;">
					<div data-options="region:'north'" style="width:400px ;padding:8px 5px 5px 5px;">
					    <table class="honry-table" cellpadding="0" cellspacing="0"   id="table1"
							border="0" style="margin-left:auto;margin-right:auto;margin-down:auto; overflow: auto;" >
							<tr>
								<td class="honry-lable">审核内容：</td>
								<td>
									<input id="accessCheckInfo"  class="easyui-textbox"></input>
								</td>
							</tr>
						</table>
						<div style="text-align: center;padding: 5px">
							<a href="javascript:void(0)" onclick="addApproveAssess()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-ok">确定</a>
							<a href="javascript:void(0)" onclick="closeApproveAssess()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-no">关闭</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="dept"></div>
	
	<script type="text/javascript"
		src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
	<script type="text/javascript">
	function searchTreeNodes(){
	    var searchText = $('#searchTreeInpId').textbox('getValue');
	    $("#tDt").tree("search", searchText);
	}
	var propertyList = null;
	//全局变量
	var flagMap=new Map();
	var planTypeMap=new Map();
	var userMap=new Map();
	var modelCodeMap=new Map();
	var deptMap = null;
	var empMap = null;
	$(function(){
		/**人员渲染**/
		$.ajax({
			url: "<c:url value='/outpatient/scheduleModel/queryEmpCodeAndNameMap.action'/>",
			type:'post',
			success: function(empData) {
				empMap = empData;
			}
		});	
			bindEnterEvent('searchTreeInpId', searchTreeNodes, 'easyui');
		});

		//list列表操作
		var id = "${id}"; //存储数据ID
		function add() {
			var node = $('#tDt').tree('getSelected');
			if (node) {
				console.log(node);
				var id = node.attributes.id;
				if (id != null && "" != id) {
					var modelId = node.attributes.id;
					var modelClass = node.attributes.modelClass;
					AddOrShowEast(
							'添加',
							"<c:url value='/inpatient/clinicalPathwayModelAction/addPathwayDetail.action'/>?modelId="
									+ modelId + "&modelClass=" + modelClass);
					// 				AddOrShowEast('添加', "<c:url value='/baseinfo/fictitiousDept/addFictitiousDept.action'/>?deptType=" + getSelected()+"&fictCode="+code+"&fictName="+encodeURIComponent(encodeURIComponent(fitdeptName)));
				} else {
					$.messager.alert('提示', '请选择模板');
					close_alert();
				}
			} else {
				$.messager.alert('提示', '请选择模板');
				close_alert();
			}
		}
		function edit() {
			var node = $('#list').datagrid('getSelected');
			if (node) {
				var id = node.id;
				if (id) {
					AddOrShowEast(
							'编辑',
							"<c:url value='/inpatient/clinicalPathwayModelAction/addPathwayDetail.action'/>?id="
									+ id);
				} else {
					$.messager.alert('提示', '请选择修改模板');
					close_alert();
				}
			} else {
				$.messager.alert('提示', '请选择修改模板');
				close_alert();
			}
		}
		function del() {
			//选中要删除的行
			var rows = $('#list').datagrid('getChecked');
			if (rows.length > 0) {//选中几行的话触发事件	                        
				$.messager
						.confirm(
								'确认',
								'确定要删除选中信息吗?',
								function(res) {//提示是否删除
									if (res) {
										var ids = '';
										for (var i = 0; i < rows.length; i++) {
											if (ids != '') {
												ids += ',';
											}
											ids += rows[i].id;
										};
										$.ajax({
													url : "<c:url value='/inpatient/clinicalPathwayModelAction/delPathwayDetail.action'/>?id="
															+ ids,
													type : 'post',
													success : function() {
														$.messager.alert('提示',
																'删除成功');
														$('#list').datagrid(
																'reload');
													}
												});
									}
								});
			} else {
				$.messager.alert('提示', '请选择要删除的记录!');
				close_alert();
			}
		}

		function reload() {
			//实现刷新栏目中的数据
			$("#list").datagrid("reload");
		}

		//查询
		function searchFrom() {
			var node = $('#tDt').tree('getSelected');
			if (node) {
				var id = node.attributes.fictitiousId;
				if (id) {
					var name = $.trim($('#name').val());
					id = node.attributes.deptCode;
					$('#list').datagrid('load', {
						deptCode : id,
						q : name
					});
				} else {
					$.messager.alert('提示信息', '请选择模板');
					close_alert();
				}
			}

		}

		//获得选中id	
		function getId(tableID, str) {
			var row = $(tableID).datagrid("getChecked");
			var dgID = "";
			if (row.length < 1) {
				$.messager.alert("操作提示", "请选择一条记录！", "warning");
				close_alert();
			}
			var i = 0;
			for (i; i < row.length; i++) {
				if (str = 0) {
					dgID += "\'" + row[i].BED_ID + "\'";
				} else {
					dgID += row[i].BED_ID;
				}
				if (i < row.length - 1) {
					dhID += ',';
				} else {
					break;
				}
			}
			return dgID;
		}

		/**
		 * 动态添加标签页
		 * @author  sunshuo
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2015-05-21
		 * @version 1.0
		 */
		function AddOrShowEast(title, url) {
			$('#divLayout').layout('remove', 'east');
			//重新装载右侧面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : 560,
				split : true,
				href : url,
				closable : true
			});
		}

		//加载部门树
		$('#tDt').tree({
					url : "<c:url value='/inpatient/clinicalPathwayAction/treeClinicalStageAssess.action'/>",
					method : 'get',
					animate : true,
					lines : true,
					formatter : function(node) {//统计节点总数
						var s = node.text;
						if (node.children.length > 0) {
							if (node.children) {
								s += '&nbsp;<span style=\'color:blue\'>('
										+ node.children.length
										+ ')</span>';
							}
						}
						return s;
					},
					onLoadSuccess : function(node, data) {
						$('#list').datagrid({});
					},
					onSelect : function(node) {//显示患者的执行信息
						$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
						if (node) {
							if("0"==node.children.length){
								var parent = $('#tDt').tree('getParent', node.target);
								var id = parent.id;//住院流水号
								var stage = node.attributes.stage;//阶段id
								$('#list').datagrid({
									url: "<%=basePath%>inpatient/clinicalPathwayAction/assessList.action?id="+id+"&stage="+stage,
								});
							}
						}
					},
					//设置默认选中根节点
					onBeforeCollapse : function(node) {
						if (node.id == "1") {
							return false;
						}
					}
				});
		$('#list').datagrid({
		    onLoadSuccess:function(data){  
		        var rowData = data.rows;
		    },
		});
		//模板树操作
		function refresh() {//刷新树
					$('#tDt').tree('options').url = "<c:url value='/inpatient/clinicalPathwayAction/treeClinicalStageAssess.action'/>",
					$('#tDt').tree('reload');
		}
		function expandAll() {//展开树
			$('#tDt').tree('expandAll');
		}
		function collapseAll() {//关闭树
			$('#tDt').tree('collapseAll');
		}
		//页面操作
		function closeEast() {
			$('#divLayout').layout('remove', 'east');
		}
		//菜单操作
		function addDept() {//添加模板
			var type = getSelected();
			if (type == "" || typeof (type) == "undefined") {
				$.messager.alert('提示', '请选中临床路径');
				close_alert();
			} else {
				Adddilog(
						"添加模板",
						"<c:url value='/inpatient/clinicalPathwayModelAction/addPathwayModel.action'/>?type="
								+ type);
			}
		}
		function editDept() {//修改模板
			var id = getSelectedFictitiousId();
			if (id) {
				Adddilog(
						"编辑模板",
						"<c:url value='/inpatient/clinicalPathwayModelAction/addPathwayModel.action'/>?id="
								+ id);
			} else {
				$.messager.alert('提示', '请选中模板');
				close_alert();
			}
		}
		var inpAccessId = '';
		//添加评估弹窗
		function addAssess() {
			inpAccessId = '';
			var node = $('#tDt').tree('getSelected');
			if(node){
				if("0"==node.children.length){
					var s = node.attributes.stage;
					if("1"==s){
						s = "住院日"
					}else{
						s = "入院" + s + "天";
					}
					$("#stageAssess").textbox("setValue",s);
					$('#addAssessWindow').window('open').window('resize',{top: top,left:(screen.availWidth/3)});
				}else{
					$.messager.alert('提示', '请选中患者临床阶段阶段');
				}
			}else{
					$.messager.alert('提示', '请选中患者临床阶段阶段');
			}
		}
		//修改评估弹窗
		function updateAssess() {
			var node = $('#list').datagrid('getSelected');
			if (node) {
				if(""!=node.accessCheckUser&&"undefined"!=typeof(node.accessCheckUser)){
					$.messager.alert('提示','该记录已审核,不能修改');
					close_alert();
				}else{
					var accessResult = node.accessResult;//评估结果
					var stage = node.stageId;//阶段id
					var dayNums = node.days;
					$("#stageAssess").textbox("setValue",stage);
					$("#dayNums").textbox("setValue",dayNums);
					$("#assessResult").textbox("setValue",accessResult);
					inpAccessId = node.id;
					$('#addAssessWindow').window('open').window('resize',{top: top,left:(screen.availWidth/3)});
				}
			}else{
				$.messager.alert('提示','请选择要修改的评估');
				close_alert();
			}
		}
		//审核弹窗
		function approveAssess() {
			var node = $('#list').datagrid('getSelected');
			if (node) {
				if(""!=node.accessCheckUser&&"undefined"!=typeof(node.accessCheckUser)){
					$.messager.alert('提示','该记录已审核');
					close_alert();
				}else{
					inpAccessId = node.id;
					$('#approveWindow').window('open').window('resize',{top: top,left:(screen.availWidth/3)});
				}
			}else{
				$.messager.alert('提示','请选择审核记录');
				close_alert();
			}
		}
		
		/**关闭评估弹窗**/
		function closeAddAssess(){
			$('#addAssessWindow').window('close');
		}
		/**关闭审核弹窗**/
		function closeApproveAssess(){
			$('#approveWindow').window('close');
		}
		//审核
		function addApproveAssess(){
			var accessCheckInfo = $("#accessCheckInfo").val();
			$.ajax({
				url : "<c:url value='/inpatient/clinicalPathwayAction/approveAssess.action'/>",
				data : {
					accessCheckInfo:accessCheckInfo,
					id : inpAccessId
				},
				type : 'post',
				success : function() {
					$.messager.alert('提示','审核成功');
					$('#list').datagrid('reload');
					closeApproveAssess();
				}
			});
		}
		//添加评估
		function saveAddAssess() {
			var node = $('#tDt').tree('getSelected');
			if("0"==node.children.length){
				var parent = $('#tDt').tree('getParent', node.target);
				var inpatientNo = parent.id;//住院流水号
				var cpId = node.attributes.cpId;//临床计划表id
				var stage = node.attributes.stage;//阶段id
				var dayNums = $("#dayNums").val();
				var assessResult = $("#assessResult").val();
				$.ajax({
					url : "<c:url value='/inpatient/clinicalPathwayAction/saveOrUpdateAssess.action'/>",
					data : {
						inpatientNo : inpatientNo,
						cpId : cpId,
						stage : stage,
						dayNums : dayNums,
						assessResult : assessResult,
						id : inpAccessId
					},
					type : 'post',
					success : function() {
						$.messager.alert('提示','评估成功');
						$('#list').datagrid('reload');
						closeAddAssess();
					}
				});
			} else {
				$.messager.alert('提示', '请选中患者临床阶段阶段');
			}
		}
		//退出路径
		function outPath() {
			var node = $('#tDt').tree('getSelected');
			if (node) {
				var id = node.id;//住院流水号
				$.ajax({
					url : "<c:url value='/inpatient/clinicalPathwayAction/delPathwayDetail.action'/>?id="
							+ id,
					type : 'post',
					success : function() {
						$.messager.alert('提示',
								'退出成功');
						$('#list').datagrid(
								'reload');
					}
				});
			} else {
				$.messager.alert('提示', '请选择退出路径');
				close_alert();
			}
		}
		//执行路径
		function executePath() {
			var node = $('#list').datagrid('getSelected');
			if (node) {
				var id = node.id;
				$.ajax({
					url : "<c:url value='/inpatient/clinicalPathwayAction/executePath.action'/>?id="
							+ id,
					type : 'post',
					success : function() {
						$.messager.alert('提示',
								'执行成功');
						$('#list').datagrid(
								'reload');
					}
				});
			} else {
				$.messager.alert('提示', '请选择修改执行路径');
				close_alert();
			}
		}
		//加载dialog
		function Adddilog(title, url) {
			$.dialog({
				title : title,
				width : '90%',
				height : '80%',
				href : url
			})
		}
		//打开dialog
		function openDialog() {
			$('#dept').dialog('open');
		}
		//关闭dialog
		function closeDialog() {
			$('#dept').dialog('close');
		}

		function getSelected() {//获得选中节点
			var node = $('#tDt').tree('getSelected');
			if (node) {
				var type = node.id;
				if (type == 1) {
					type = "C";
				} else {
					var l = type.indexOf("type_");
					if (l != -1) {
						type = node.id.substring(l + 5);
					} else {
						l = node.attributes.pid.split("_")
						type = l[1]
					}
					return type;
				}

			} else {
				return "";
			}
		}
		function getSelectedFictitiousId() {
			var node = $('#tDt').tree('getSelected');
			if (node) {
				var id = node.attributes.id;
				if (id) {
					return id;
				}
				return false;
			} else {
				return false;
			}
		}
		/**执行人模板渲染**/
		function flagUser(value,row,index){
			if("undefined"!=typeof(value)){
				return empMap[value];
			}
		}
		/**阶段模板渲染**/
		function flagStage(value,row,index){
			if("1"==typeof(value)){
				return "住院日";
			}else{
				return "入院"+value+"天";
			}
		}
		// 列表查询重置
		function searchReload() {
			$('#name').textbox('setValue', '');
			searchFrom();
		}
	</script>
</body>
</html>