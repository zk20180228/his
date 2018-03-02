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
<title>患者临床路径</title>
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
									onclick="impPath()" class="easyui-linkbutton"  iconCls="icon-page_white_key">导入入径</a> <a
									href="javascript:void(0)" onclick="outPath()" iconCls="icon-page_white_lightning"
									class="easyui-linkbutton">退出入径</a> <a href="javascript:void(0)"
									onclick="executePath()" class="easyui-linkbutton" iconCls="icon-forward_green">执行</a></td>
							</tr>
						</table>
					</form>
				</div>
				<div style="height: 30%;" >
					<table id="list" data-options="method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
						<thead>
							<tr>
								<th field="ck" checkbox="true" data-options="width:'5%'"></th>
								<th data-options="field:'planCode',width:'10%',formatter:planTypeFormatter">计划类别</th>
								<th data-options="field:'modelCode',width:'10%',formatter:flagModelNature">项目名称</th>
								<th data-options="field:'flag',width:'10%'">长期/临时</th>
								<th data-options="field:'createUser',width:'10%',formatter:flagUser">导入人</th>
								<th data-options="field:'createTime',width:'10%'">导入时间</th>
								<th data-options="field:'executeUser',width:'10%',formatter:flagUser">执行人</th>
								<th data-options="field:'executeDate',width:'10%'">执行时间</th>
<!-- 								<th data-options="field:'nursSign',width:'10%'">护士签名</th> -->
<!-- 								<th data-options="field:'nursSignDate',width:'10%'">护士签名时间</th> -->
<!-- 								<th data-options="field:'doctSign',width:'10%'">医生签名</th> -->
<!-- 								<th data-options="field:'doctSignDate',width:'10%'">医生签名时间</th> -->
							</tr>
						</thead>
					</table>
				</div>
				<div style="height: calc( 70% - 40px )" >
					<table id="listDetail" data-options="method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
						<thead>
							<tr>
<!-- 								<th field="ck" checkbox="true" data-options="width:'5%'"></th> -->
<!-- 								<th data-options="field:'itemCode',width:'10%'">组内序号</th> -->
								<th data-options="field:'itemCode',width:'15%'">项目代码</th>
								<th data-options="field:'itemName',width:'5%'">
									项目名称</th>
								<th data-options="field:'orderType',width:'5%',formatter:flagFormatter">
									医嘱类型</th>
								<th data-options="field:'unit',width:'10%'">单位</th>
								<th data-options="field:'num',width:'10%'">数量</th>
								<th data-options="field:'frequencyCode',width:'10%'">频次代码</th>
								<th data-options="field:'directionCode',width:'10%'">用法代码</th>
								<th data-options="field:'useDays',width:'10%'">付数</th>
								<th data-options="field:'specimen',width:'10%'">标本</th>
								<th data-options="field:'executeDeptCode',width:'10%',formatter:flagDept">执行科室</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="dept"></div>
	<div id="toolbarId">
		<span style="background: red;">&nbsp;&nbsp;&nbsp;</span><span>已执行</span>
	</div>
	
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
		/**医嘱类型渲染**/
		$.ajax({
			url: "<%=basePath%>inpatient/clinicalPathwayModelAction/flagCombobox.action",
			type : 'post',
			success : function(data) {
				var j = JSON.parse(data);
				for (var i = 0; i < j.length; i++) {
					flagMap.put(j[i].typeCode, j[i].typeName);

				}
			}
		});
		/**人员渲染**/
		$.ajax({
			url: "<c:url value='/outpatient/scheduleModel/queryEmpCodeAndNameMap.action'/>",
			type:'post',
			success: function(empData) {
				empMap = empData;
			}
		});	
		/**科室渲染**/
		$.ajax({
			url: "<c:url value='/outpatient/scheduleModel/querydeptCodeAndNameMap.action'/>",
			type:'post',
			success: function(deptData) {
				deptMap = deptData;
			}
		});	
			/**属性(计划类别)渲染**/
			$.ajax({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=cpProperty",
				type : 'post',
				success : function(data) {
					var j = data;
					for(var i=0;i<j.length;i++){
						planTypeMap.put(j[i].encode,j[i].name);
					}
				}
			});
			/**临床路径模板渲染**/
			$.ajax({
				url: "<%=basePath%>inpatient/clinicalPathwayModelAction/searchAllClinicalModel.action",
				type:'post',
				success: function(data){
					var j = data;
					for(var i=0;i<j.length;i++){
						modelCodeMap.put(j[i].id,j[i].modelName);
					}
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
							url : "<c:url value='/inpatient/clinicalPathwayAction/treePatientClinicalPath.action'/>",
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
								$('#listDetail').datagrid({});
							},
							onSelect : function(node) {//显示患者的执行信息
								$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
// 								var node = $('#tDt1').tree('getSelected');
								if (node) {
									if("undefined"!=typeof(node.attributes)){
										var id = node.id;//住院流水号
										$('#list').datagrid({
											url: "<%=basePath%>inpatient/clinicalPathwayAction/executeInfoByInpatientNo.action?id="+id,
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
		        for(var i=0;i<rowData.length;i++){
		            $("#list").datagrid("selectRow", i);  
					if("1"==data.rows[i].executeFlag){
						 $("[datagrid-row-index='" + i + "']").css({ "background-color": "red" });
					}						
				}
		    },
		    onBeforeSelect: function (rowIndex, rowData) {
				var rows = $('#list').datagrid('getChecked');
				for(var i=0 ; i<rows.length; i++){
					if(rows[i].id==rowData.id){
						isSelect = true;
					}
				}
		    },
			onClickRow: function (rowIndex, rowData) {//单击查看
				if(isSelect){
					$("#list").datagrid("uncheckRow", rowIndex);
					isSelect = false;
				}else{
					$("#list").datagrid("checkRow", rowIndex);
					isSelect = false;
				}
				haveSaveModel = true;
				$('#listDetail').datagrid({
					url: "<%=basePath%>inpatient/clinicalPathwayAction/executeDetail.action?inpatientNo="+rowData.inpatientNo+"&cpId="+rowData.cpId+"&modelCode="+rowData.modelCode,
				});
			},
		});
		//模板树操作
		function refresh() {//刷新树
					$('#tDt').tree('options').url = "<c:url value='/inpatient/clinicalPathwayAction/treePatientClinicalPath.action'/>",
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
		function viewDetp() {//查看模板
			var id = getSelectedFictitiousId();
			if (id) {
				Adddilog(
						"查看模板信息",
						"<c:url value='/inpatient/clinicalPathwayModelAction/showPathwayModel.action'/>?id="
								+ id);
			} else {
				$.messager.alert('提示', '请选中模板');
				close_alert();
			}
		}

		//导入路径
		function impPath() {
			var node = $('#tDt').tree('getSelected');
			if (node) {
				var id = node.id;//住院流水号
				if("undefined"!=typeof(node.attributes)){
					var inDeptCode = node.attributes.deptCode;//住院科室
					if (id != null && "" != id) {
						Adddilog("导入临床路径",
								"<c:url value='/inpatient/clinicalPathwayAction/showImpPath.action'/>?id="
										+ id +"&deptCode="+inDeptCode);
					} else {
						$.messager.alert('提示', '请选择患者');
						close_alert();
					}
				}else{
					$.messager.alert('提示', '请选择患者');
					close_alert();
				}
			} else {
				$.messager.alert('提示', '请选择患者');
				close_alert();
			}
		}
		//退出路径
		function outPath() {
			var node = $('#tDt').tree('getSelected');
			if (node) {
				if("0"==node.children.length){
					var id = node.id;//住院流水号
					$.ajax({
						url : "<c:url value='/inpatient/clinicalPathwayAction/outPath.action'/>?id="
								+ id,
						type : 'post',
						success : function() {
							$.messager.alert('提示',
									'退出成功');
							$('#list').datagrid(
									'reload');
						}
					});
				}else{
					$.messager.alert('提示', '请选择患者');
					close_alert();
				}
			} else {
				$.messager.alert('提示', '请选择患者');
				close_alert();
			}
		}
		//执行路径
		function executePath() {
			var rows = $('#list').datagrid('getChecked');
			if (rows.length > 0) {//选中几行的话触发事件	                        
				$.messager.confirm(
								'确认',
								'确定要执行选中信息吗?',
								function(res) {//提示是否执行
									if (res) {
										var ids = '';
										for (var i = 0; i < rows.length; i++) {
											if (ids != '') {
												ids += ',';
											}
											ids += rows[i].id;
										};
										$.ajax({
											url : "<c:url value='/inpatient/clinicalPathwayAction/executePath.action'/>?id="
													+ ids,
											type : 'post',
											success : function() {
												$.messager.alert('提示',
														'执行成功');
												$('#list').datagrid(
														'reload');
											}
										});
									}
								});
			} else {
				$.messager.alert('提示', '请选择要执行的记录!');
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
				// 			console.log(node);
				var id = node.attributes.id;
				if (id) {
					return id;
				}
				return false;
			} else {
				return false;
			}
		}

		//适用部门分类
		function chooseFlagFormatter(value, row, index) {
			if (value == 1) {
				return "是";
			} else {
				return "否";
			}
		}
		/**计划类别渲染**/
		function planTypeFormatter(value,row,index){
			return planTypeMap.get(value);
		}	
		/**医嘱类型渲染**/
		function flagFormatter(value, row, index) {
			return flagMap.get(value);
		}
		/**临床路径模板渲染**/
		function flagModelNature(value,row,index){
			return modelCodeMap.get(value);
		}	
		/**执行人模板渲染**/
		function flagUser(value,row,index){
			if("undefined"!=typeof(value)){
				return empMap[value];
			}
		}	
		/**执行科室模板渲染**/
		function flagDept(value,row,index){
			if("undefined"!=typeof(value)){
        		return deptMap[value];
			}
		}	
		//格式化部门性质
		function propertyFormatter(value, row, index) {
			if (value != null) {
				for (var i = 0; i < propertyList.length; i++) {
					if (value == propertyList[i].encode) {
						return propertyList[i].name;
					}
				}
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