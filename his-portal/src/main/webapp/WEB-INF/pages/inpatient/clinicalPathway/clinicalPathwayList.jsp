<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<%@ include file="/common/metas.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>临床路径</title>
</head>
<script type="text/javascript">
var typeMap = new Map();
$.ajax({
	url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=systemType",
	success: function(data) {
		var deptType = data;
		for(var i=0;i<deptType.length;i++){
			typeMap.put(deptType[i].encode,deptType[i].name);
		}
	}
});
</script>
<head>
<style type="text/css">
.panel-header{
	border-top:0;
}
</style>
</head>
<body>
		<div class="easyui-layout" data-options="fit:true">
			<div id='p' data-options="region:'west',title:'临床路径',split:true,border:true,tools:'#toolSMId'" style="width:400px;">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',border:false" style="height: 35px;padding-top: 5px">
						&nbsp;<input type="text" class="easyui-textbox" id="searchTreeInpId" data-options="prompt:'请输入关键字'" style="width: 200px;"/>
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a>
						<a href="javascript:void(0)" onclick="addDisease()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="margin-top: 2px;">添加临床路径</a>
					</div>
					<div id="treeDiv" data-options="region:'center',border:false" style="width: 85%">
						<ul id="tDt">加载中...</ul>
					</div>
					<div id="toolSMId">
						<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
						<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
						<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
					</div>
				</div>
			</div>
			<div data-options="region:'center',border:false" style="width:79%;height: 200px">
				<div id="divLayout" class="easyui-layout" style="height: 250px">
					<div>
<!-- 						<p style="font-weight: bolder;">临床路径明细</p> -->
						<table style="height: 100px" id="list1" data-options="method:'post',title:'临床路径明细',idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
							<thead>
								<tr>
									<th field="id" hidden='true'></th>
									<th data-options="field:'cpName',width:'10%'">
										临床路径名称
									</th>
									<th data-options="field:'customCode',width:'10%'">
										自定义码
									</th>
									<th data-options="field:'typeId',formatter: flagTypeId,width:'10%'">
										分类
									</th>
									<th data-options="field:'memo',width:'10%'">
										简要说明
									</th>
									<th data-options="field:'caseType',width:'10%'">
										病理分类
									</th>
								</tr>
							</thead>
						</table>
					</div>
					<div style="margin-top: 30;">
<!-- 						<p style="font-weight: bolder;">版本明细</p> -->
						<table style="height: 100px" id="list" data-options="method:'post',title:'版本明细',idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
							<thead>
								<tr>
									<th field="id" hidden='true'></th>
									<th field="cpId" hidden='true'></th>
									<th data-options="field:'cpName',width:'10%'">
										临床路径名称
									</th>
									<th data-options="field:'versionNo',width:'10%'">
										临床路径版本号
									</th>
									<th data-options="field:'standCode',formatter: flagStandCode,width:'10%'">
										标准名称
									</th>
									<th data-options="field:'standVersionNo',width:'5%'">
										标准版本号
									</th>
									<th data-options="field:'versionMemo',width:'10%'">
										标准说明
									</th>
									<th data-options="field:'versionDate',width:'10%'">
										标准日期
									</th>
									<th data-options="field:'applyScope',width:'5%',formatter: flagApplyScope">
										适用范围
									</th>
									<th data-options="field:'approvalUser',width:'5%'">
										审批人
									</th>
									<th data-options="field:'approvalDate',width:'10%'">
										审批日期
									</th>
									<th data-options="field:'approvalFlag',width:'5%'">
										审批标志
									</th>
									<th data-options="field:'standardDate',width:'10%'">
										标准住院日
									</th>
									<th data-options="field:'dateUnit',width:'5%'">
										日期单位
									</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
				<div style="height: calc(100% - 251px);border-top: 1px solid #77d7d6;" >
					<div style="float: left;width: 20%;">
						<div data-options="region:'north',border:false" style="height: 35px;padding-top: 5px">
							<a href="javascript:void(0)" onclick="addTime()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="margin-top: 2px;">添加时间段</a>
						</div>
						<div id="treeDiv" data-options="region:'center',border:false" style="width: 85%">
							<ul id="tDt1"></ul>
						</div>
					</div>
					<div style="float: left;width: calc(40% - 2px);height: 100%;border-left: 1px solid #77d7d6 ;">
						<div style="height: 35px;padding-top: 5px">
							<input type="text" class="easyui-combobox" id="searchClinicalModel" data-options="prompt:'请输入关键字'" style="width: 200px;"/>
<!-- 							<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchModel()" style="margin-top: 2px;">查询</a> -->
							<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onClick="saveModel()" style="margin-top: 2px;">保存</a>
						</div>
						<div >
							<table id="listModel" data-options="method:'post',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
								<thead>
									<tr>
										<th field="id" checkbox="true" data-options="width:'5%'"></th>
										<th data-options="field:'modelCode',formatter: flagModelNature,width:'95%'">临床路径模板</th>
										<th data-options="field:'modelNature',hidden:'true'"></th>
									</tr>
								</thead>
							</table>
						</div>
<!-- 						<div style="text-align: center"> -->
<!-- 							<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="saveModel()" style="margin-top: 2px;">保存</a> -->
<!-- 						</div> -->
					</div>
					<div style="float: left;width: 40%">
						<div style="height: 35px;padding-top: 5px">
						</div>
						<div style="border-left: 1px solid #77d7d6;height: calc(100% - 40px)">
							<table id="listModelDetail" data-options="method:'post',idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
								<thead>
									<tr>
										<th data-options="field:'itemName',width:'100%'">临床路径模板明细</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="dept" ></div>
		<!-- tree菜单 -->
		<div id="tDtmm" class="easyui-menu" style="width:100px;">
				<div onclick="addDept()" data-options="iconCls:'icon-add'">添加版本</div>
				<div onclick="editDept()" data-options="iconCls:'icon-edit'">修改版本</div>
	<%-- 		<shiro:hasPermission name="${menuAlias }:function:tdelete"> --%>
	<!-- 			<div onclick="delDetp()" data-options="iconCls:'icon-remove'">移除</div> -->
	<%-- 		</shiro:hasPermission> --%>
<!-- 			<div onclick="viewDetp()" data-options="iconCls:'icon-search'">查看版本</div> -->
		</div>
		<div id="addTimeWindow" class="easyui-window" title="添加时间段" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:420px;height:120px;">
			<div data-options="region:'north'" style="width:400px ;padding:8px 5px 5px 5px;">
			    <table class="honry-table" cellpadding="0" cellspacing="0"   id="table1"
					border="0" style="margin-left:auto;margin-right:auto;margin-down:auto; overflow: auto;" >
					<tr>
						<td class="honry-lable">时间段：</td>
						<td>
							第
							<input id="timeStart" class="easyui-numberbox" style="width: 50px"></input>
							天 --- 第
							<input id="timeEnd" class="easyui-numberbox" style="width: 50px"></input>
							天
						</td>
					</tr>
				</table>
				<div style="text-align: center;padding: 5px">
					<a href="javascript:void(0)" onclick="addTimeToClinical()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-ok">确定</a>
					<a href="javascript:void(0)" onclick="closeAddTime()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-no">关闭</a>
				</div>
			</div>
		</div>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
	function searchTreeNodes(){
	    var searchText = $('#searchTreeInpId').textbox('getValue');
	    $("#tDt").tree("search", searchText);
	}
	var propertyList = null;
	//全局变量
	var standCodeMap=new Map();
	var typeIdMap=new Map();
	var modelNatureMap=new Map();
	var applyScopeMap=new Map();
	var versionId = "";//临床路径版本ID
	var allCpId = "";//临床路径id
	$(function(){
		/**标准名称渲染**/
		$.ajax({
			url: "<%=basePath%>inpatient/clinicalPathwayAction/queryStand.action",
			type:'post',
			success: function(data){
				var j = JSON.parse(data);
				for(var i=0;i<j.length;i++){
					standCodeMap.put(j[i].standCode,j[i].standName);
					
				}
			}
		});
		/**临床路径分类渲染**/
		$.ajax({
			url: "<%=basePath%>/baseinfo/pubCodeMaintain/queryDictionary.action?type=cpWay",
			type:'post',
			success: function(data){
				var j = data;
				for(var i=0;i<j.length;i++){
					typeIdMap.put(j[i].encode,j[i].name);
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
					modelNatureMap.put(j[i].id,j[i].modelName);
				}
			}
		});
		/**适用范围渲染**/
		$.ajax({
			url: "<%=basePath%>/baseinfo/pubCodeMaintain/queryDictionary.action?type=applyScope",
			type:'post',
			success: function(data){
				var j = data;
				for(var i=0;i<j.length;i++){
					applyScopeMap.put(j[i].encode,j[i].name);
				}
			}
		});
	    bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
	});
	
	
	
	//list列表操作
	var id = "${id}"; //存储数据ID
	function add() {
		var node = $('#tDt').tree('getSelected');
		if (node) {
			var id = node.attributes.id;
			if(id!=null&&""!=id){
				var modelId=node.attributes.id;
				var modelClass=node.attributes.modelClass;
				AddOrShowEast('添加', "<c:url value='/inpatient/clinicalPathwayModelAction/addPathwayDetail.action'/>?modelId=" + modelId+"&modelClass="+modelClass);
// 				AddOrShowEast('添加', "<c:url value='/baseinfo/fictitiousDept/addFictitiousDept.action'/>?deptType=" + getSelected()+"&fictCode="+code+"&fictName="+encodeURIComponent(encodeURIComponent(fitdeptName)));
			}else{
				$.messager.alert('提示','请选择');
				close_alert();
			}
		}else{
			$.messager.alert('提示','请选择');
			close_alert();
		}
	}
	function edit() {
		var node = $('#list').datagrid('getSelected');
		if (node) {
			var id = node.id;
			if(id){
				AddOrShowEast('编辑', "<c:url value='/inpatient/clinicalPathwayModelAction/addPathwayDetail.action'/>?id="+id);
			}else{
				$.messager.alert('提示','请选择修改');
				close_alert();
			}
		}else{
			$.messager.alert('提示','请选择修改');
			close_alert();
		}
	}
	function del() {
		//选中要删除的行
		var rows = $('#list').datagrid('getChecked');
		if (rows.length > 0) {//选中几行的话触发事件	                        
			$.messager.confirm(
				'确认',
				'确定要删除选中信息吗?',
				function(res) {//提示是否删除
					if (res) {
						var ids = '';
						for ( var i = 0; i < rows.length; i++) {
							if (ids != '') {
								ids += ',';
							}
							ids += rows[i].id;
						};
						$.ajax({
							url : "<c:url value='/inpatient/clinicalPathwayModelAction/delPathwayDetail.action'/>?id="+ ids,
							type : 'post',
							success : function() {
								$.messager.alert('提示','删除成功');
								$('#list').datagrid('reload');
							}
						});
					}
				});
		} else {
			$.messager.alert('提示','请选择要删除的记录!');
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
			if(id){
				var name = $.trim($('#name').val());
				id=node.attributes.deptCode;
				$('#list').datagrid('load', {
					deptCode : id,
					q : name
				});
			}else{
				$.messager.alert('提示信息','请选择');
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
		$('#divLayout').layout('remove','east');
			//重新装载右侧面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : 560,
				split : true,
				href : url,
				closable : true
			});
	}

	//加载临床路径树
	$('#tDt').tree({
		url : "<c:url value='/inpatient/clinicalPathwayAction/treeClinicalPath.action'/>",
		method : 'get',
		animate : true,
		lines : true,
		onContextMenu: function(e,node){//添加右键菜单
			e.preventDefault();
			$(this).tree('select',node.target);
			$('#tDtmm').menu('show',{
				left: e.pageX,
				top: e.pageY
			});
		},
		formatter : function(node) {//统计节点总数
					   var s = node.text;
							if(node.children.length>0){
								if (node.children) {
									s += '&nbsp;<span style=\'color:blue\'>('+ node.children.length + ')</span>';
								}
							}
							return s;
						},
						//设置默认选中根节点
		onLoadSuccess : function(node, data) {
			if(data.length>0){//节点收缩
				$('#tDt').tree('collapseAll');
			}
			$('#tDt').tree('select',$('#tDt').tree('find', 1).target);
// 			$.ajax({
// 				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
// 				data : {"type" : "systemType"},
// 				type : 'post',
// 				success : function(propertydata) {
// 						  propertyList = propertydata;
// 						}
// 				});
				$('#list').datagrid({
									url : "<c:url value='/inpatient/clinicalPathwayAction/queryVersionById.action'/>",
								});
				$('#list1').datagrid({
									url : "<c:url value='/inpatient/clinicalPathwayAction/queryDiseaseById.action'/>",
								});
				$('#listModel').datagrid({
// 									url : "<c:url value='/inpatient/clinicalPathwayAction/queryClinicalModel.action'/>",
								});
				$('#listModelDetail').datagrid({
								});
				},
				onClick : function(node) {//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					var node = $('#tDt').tree('getSelected');
					if (node) {
						var id = node.attributes.id;
						var cpId = node.attributes.cpId;
						allCpId = cpId; 
						if(id){
							versionId = id;
							$('#tDt1').tree('options').url = "<c:url value='/inpatient/clinicalPathwayAction/treeStage.action'/>?cpId="+versionId,
							$('#tDt1').tree('reload');
							$('#list').datagrid('load', {
								id : id
							});
							$('#list1').datagrid('load', {
								cpId : cpId
							});
							closeEast();
						}
					}
				},
				onBeforeCollapse:function(node){
					if(node.id=="1"){
						return false;
					}
			    }
			});
	//树操作
	function refresh() {//刷新树
		$('#tDt').tree('options').url = "<c:url value='/inpatient/clinicalPathwayAction/treeClinicalPath.action'/>",
		$('#tDt').tree('reload');
	}
	function expandAll() {//展开树
		$('#tDt').tree('expandAll');
	}
	function collapseAll() {//关闭树
		$('#tDt').tree('collapseAll');
	}
	//页面操作
	function closeEast(){
		$('#divLayout').layout('remove','east');
	}
	//菜单操作
	function addDept() {//添加
		var type = getSelected();
		if(type == ""){
			$.messager.alert('提示','请选中临床路径');
			close_alert();
		}else{
			Adddilog("添加", "<c:url value='/inpatient/clinicalPathwayAction/addVersion.action'/>?type="+ type);
		}
	}
	/**添加病种**/
	function addDisease(){
		AdddilogDisease("添加临床路径", "<c:url value='/inpatient/clinicalPathwayAction/addDisease.action'/>");
	}
	/**添加时间段**/
	function addTime(){
		if(versionId==""){
			$.messager.alert('提示','请选择临床路径版本');
		}else{
			$('#addTimeWindow').window('open').window('resize',{top: top,left:(screen.availWidth/3)});
		}
	}
	/**关闭时间段弹窗**/
	function closeAddTime(){
		$('#addTimeWindow').window('close');
	}
	function editDept() {//修改
		var id=getSelectedFictitiousId();
		if(id){
			$.ajax({
				url: "<%=basePath%>/inpatient/clinicalPathwayAction/checkApproval.action?id="+id,
				type:'post',
				success: function(data){
					if(data=="success"){
						Adddilog("编辑", "<c:url value='/inpatient/clinicalPathwayAction/addVersion.action'/>?id="+id);
					}else{
						$.messager.alert('提示',data);
						close_alert();
					}
				}
			});
		}else{
			$.messager.alert('提示','请选中');
			close_alert();
		}
	}
	function viewDetp() {//查看
		var id=getSelectedFictitiousId();
		if(id){
			Adddilog("查看信息", "<c:url value='/inpatient/clinicalPathwayModelAction/showPathwayModel.action'/>?id="+id);
		}else{
			$.messager.alert('提示','请选中');
			close_alert();
		}
	}
	//加载dialog
	function Adddilog(title, url) {
		$('#dept').dialog({    
		    title: title,    
		    width: '800',    
		    height:'470',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
	}
	function AdddilogDisease(title, url) {
		$('#dept').dialog({    
		    title: title,    
		    width: '800',    
		    height:'250',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
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
			if(node.attributes.pid!=1){
				return "";
			}else{
				var l= type.indexOf("type_");
				if(l!=-1){
					type=node.id.substring(l+5);
				}else{
					l=node.attributes.pid.split("_")
					type=l[1]
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
			if(id){
				return id;
			}
			return false;
		} else {
			return false;
		}
	}
	
	//适用部门分类
	function chooseFlagFormatter(value,row,index){
		if(value==1){
			return "是";
		}else{
			return "否";
		}
	}	
	/**医嘱类型渲染**/
	function flagStandCode(value,row,index){
		return standCodeMap.get(value);
	}	
	/**临床路径分类渲染**/
	function flagTypeId(value,row,index){
		return typeIdMap.get(value);
	}	
	/**临床路径模板渲染**/
	function flagModelNature(value,row,index){
		return modelNatureMap.get(value);
	}	
	/**适用范围渲染**/
	function flagApplyScope(value,row,index){
		return applyScopeMap.get(value);
	}	
	
	//格式化部门性质
// 	function propertyFormatter(value, row, index) {
// 		if (value != null) {
// 			for ( var i = 0; i < propertyList.length; i++) {
// 				if (value == propertyList[i].encode) {
// 					return propertyList[i].name;
// 				}
// 			}
// 		}
// 	}
	// 列表查询重置
	function searchReload() {
		$('#name').textbox('setValue','');
		searchFrom();
	}
	/**给临床路径添加时间段**/
	function addTimeToClinical(){
		var s = $("#timeStart").val();
		var e = $("#timeEnd").val();
		if(versionId==""){
			$.messager.alert('提示','请选择临床路径版本');
		}else if(s==""||e==""){
			$.messager.alert('提示','开始时间或结束时间为空');
		}else if(s>e){
			$.messager.alert('提示','开始时间不能大于结束时间');
		}else{
			/**将时间段添加到对应临床路径版本**/
			$.ajax({
				url: "<%=basePath%>inpatient/clinicalPathwayAction/addTimeToClinical.action",
				data:{sTime:s,eTime:e,versionId:versionId,cpId:allCpId},
				type:'post',
				success: function(data){
					if("success"==data){
						$.messager.alert('提示','添加成功');
					}else{
						$.messager.alert('提示',data);
					}
					$('#tDt1').tree('options').url = "<c:url value='/inpatient/clinicalPathwayAction/treeStage.action'/>?cpId="+versionId,
					$('#tDt1').tree('reload');
					closeAddTime();
				}
			});
		}
	}
	//是否当前节点的标识,用节点id记录
	var isNowNode = "";
	/**加载时间段树**/
	$('#tDt1').tree({
		url : "<c:url value='/inpatient/clinicalPathwayAction/treeStage.action'/>?cpId="+versionId,
		method : 'get',
		animate : true,
		lines : true,
		formatter : function(node) {//统计节点总数
					   var s = node.text;
							if(node.children.length>0){
								if (node.children) {
									s += '&nbsp;<span style=\'color:blue\'>('+ node.children.length + ')</span>';
								}
							}
							return s;
						},
						//设置默认选中根节点
		onLoadSuccess : function(node, data) {
			if(data.length>0){//节点收缩
				$('#tDt1').tree('collapseAll');
			}
		},
		onBeforeSelect : function(node){
			if("undefined"!=typeof(node.attributes)){
				var parent = $('#tDt1').tree('getParent', node.target);
				var id = node.attributes.id;
				if(id){
					if(isNowNode != id || isNowNode == ""){
						isNowNode = id;
						if(haveSaveModel){
							$.messager.alert('提示','尚未保存组套信息');
							return false;
						}
					}
				}
			}
		},
		onSelect : function(node) {//点击节点
			$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			var node = $('#tDt1').tree('getSelected');
			if (node) {
				if("undefined"!=typeof(node.attributes)){
					var parent = $('#tDt1').tree('getParent', node.target);
					var id = node.attributes.id;
					if(id){
							modelNatureAll = node.id;
							idAll = parent.id;
							cpIdAll = parent.attributes.cpId;
							stageIdAll = parent.attributes.stageId;
							$('#listModelDetail').datagrid('loadData', { total: 0, rows: [] });
// 							haveSaveModel = true;
							$('#listModel').datagrid({
								url: "<%=basePath%>inpatient/clinicalPathwayModelAction/searchClinicalModelByStage.action?modelNature="+id+"&planId="+cpIdAll+"&stageId="+stageIdAll,
							    onLoadSuccess:function(data){  
							        var rowData = data.rows;    
							        for(var i=0;i<rowData.length;i++){
							            $("#listModel").datagrid("selectRow", i);  
									}
							    },
							    onBeforeSelect: function (rowIndex, rowData) {
									var rows = $('#listModel').datagrid('getChecked');
									for(var i=0 ; i<rows.length; i++){
										if(rows[i].id==rowData.id){
											isSelect = true;
										}
									}
							    },
								onClickRow: function (rowIndex, rowData) {//单击查看
									if(isSelect){
										$("#listModel").datagrid("uncheckRow", rowIndex);
										isSelect = false;
									}else{
										$("#listModel").datagrid("checkRow", rowIndex);
										isSelect = false;
									}
									haveSaveModel = true;
									$('#listModelDetail').datagrid({
										url: "<%=basePath%>inpatient/clinicalPathwayModelAction/queryClinicalPathModelDetail.action?modelId="+rowData.modelCode,
									});
								},
							});
							/**临床路径模板下拉**/
							$('#searchClinicalModel').combobox({
								url: "<%=basePath%>inpatient/clinicalPathwayModelAction/searchClinicalModelByNature.action?modelNature="+id,
								valueField : 'modelCode',
								textField : 'modelName',
								filter:function(q,row){
									var keys = new Array();
									keys[keys.length] = 'modelCode';
									keys[keys.length] = 'modelName';
									if(filterLocalCombobox(q, row, keys)){
										row.selected=true;
									}else{
										row.selected=false;
									}
									return filterLocalCombobox(q, row, keys);
							    },
	 						    onSelect: function(rec){
	 						    	if(checkHaveModel(rec.id)){
	 						    		haveSaveModel = true;
		 						    	$("#listModel").datagrid('insertRow',
	 						    			{index: 0,	// 索引从0开始
		 						    		row: {
												modelCode:rec.id,  
												modelNature : rec.modelNature}
	 						    			}
	 						    		);
	 						    	}
	 						    }
							});
							closeEast();
						}
					}
				}
		},
		onBeforeCollapse:function(node){
			if(node.id=="1"){
				return false;
			}
	    }
	});
	//检查listModel里面有没有重复的临床路径模板
	function checkHaveModel(id){
		var rows = $("#listModel").datagrid("getRows");
    	for(var i = 0;i<rows.length;i++){
    		if(id==rows[i].modelCode){
    			return false;
    		}
    	}
    	return true;
	}
	//是否保存的标识
	var haveSaveModel = false;
	//是否选择模板的标识
	var isSelect = false;
	//保存是需要用到的常亮
	var modelNatureAll = "";
	var idAll = "";
	var cpIdAll = "";
	var stageIdAll = "";
	//保存组套的临床路径模板
	function saveModel(){
		//选中要保存的行
		var rows = $('#listModel').datagrid('getChecked');
		if (rows.length > 0) {//选中几行的触发事件	                        
			$.messager.confirm(
				'确认',
				'确定要保存选中模板吗?',
				function(res) {//提示是否保存
					if (res) {
						var ids = '';
						for ( var i = 0; i < rows.length; i++) {
							if (ids != '') {
								ids += ',';
							}
							ids += rows[i].modelCode;
						};
						var node = $('#tDt1').tree('getSelected');
						var parent = $('#tDt1').tree('getParent', node.target);
						$.ajax({
							url : "<c:url value='/inpatient/clinicalPathwayAction/saveModelToPlan.action'/>?modelCode="+ ids +"&modelNature="+modelNatureAll+"&id="+idAll+"&cpId="+cpIdAll+"&stageId="+stageIdAll,
							type : 'post',
							success : function() {
								$.messager.alert('提示','保存成功');
								haveSaveModel = false;
								$('#listModel').datagrid('reload');
							}
						});
					}
				}
			);
		}
	}
</script>
	</body>
</html>