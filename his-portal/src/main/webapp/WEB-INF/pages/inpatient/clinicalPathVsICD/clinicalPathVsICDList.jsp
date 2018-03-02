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
<title>临床路径与ICD对照</title>
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
	.layout-panel-north{
		top:5px;
		left:5px
	
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
			<div data-options="region:'center',border:false" style="width:79%;height: 100%">
				<div id="divLayout" class="easyui-layout" data-options="border:false,fit:true">
					<div  data-options="region:'north',border:false" style="margin-top:5px;margin-left:5px">
						<span class="hospitalbedListSearchSize">
				    		<input  class="easyui-textbox" id="keyWord" style="width:182px" name=""  data-options="prompt:'输入Icd名称'" />
							<a href="javascript:searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查&nbsp;询&nbsp;</a>
			    		</span>
					</div>
					<div  data-options="region:'center',border:false" style="margin-top:5px;">
						<table id="list" 
						data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,fit:true,selectOnCheck:false,singleSelect:true,toolbar:'#toolbarId',pagination:true,pageSize:20,pageList:[20,30,50,80,100]">
							<thead>
								<tr>
									<th data-options="field:'cpId',width:'30%'">
										临床路径ID
									</th>
									<th data-options="field:'icdCode',width:'10%'">
										ICD编码
									</th>
									<th data-options="field:'icdName',width:'15%'">
										ICD名称
									</th>
									<th data-options="field:'stop_flg',formatter:TZFormatter,width:'8%'">
										停止标志
									</th>
									<th
										data-options="field:'del_flg',formatter:SCFormatter,width:'8%'">
										删除标志
									</th>
									<th
										data-options="field:'hospitalId',formatter:YYFormatter,width:'15%'">
										所属医院
									</th>
									<th
										data-options="field:'areaCode',formatter:YQFormatter,width:'10%'">
										所属院区
									</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div id="toolbarId">
			<shiro:hasPermission name="${menuAlias}:function:add">
				<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:edit">
				<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
			</shiro:hasPermission>	
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
<script type="text/javascript">
	var propertyList = null;
	//全局变量
	var flagMap=new Map();
	$(function(){
		$('#list').datagrid({
			url : "<c:url value='/inpatient/clinicalPathVsICD/queryClinicalPathVsICD.action'/>",
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onLoadSuccess:function(data){
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
				}
			}
			
		})
	});
	//list列表操作
	var id = "${id}"; //存储数据ID
	function searchTreeNodes(){
	    var searchText = $('#searchTreeInpId').textbox('getValue');
	    $("#tDt").tree("search", searchText);
	}
	function add() {
		var node = $('#tDt').tree('getSelected');
		if (node) {
			var id = node.attributes.id;
			if(id!=null&&""!=id){
				var modelId=node.attributes.id;
				AddOrShowEast('添加', "<c:url value='/inpatient/clinicalPathVsICD/addClinicalPathVsICD.action'/>?modelId=" + modelId);
			}else{
				$.messager.alert('提示','请选择临床路径');
				close_alert();
			}
		}else{
			$.messager.alert('提示','请选择临床路径');
			close_alert();
		}
	}
	function edit() {
		var node = $('#list').datagrid('getSelected');
		if (node) {
			var id = node.id;
			if(id){
				AddOrShowEast('编辑', "<c:url value='/inpatient/clinicalPathVsICD/addClinicalPathVsICD.action'/>?id="+id);
			}else{
				$.messager.alert('提示','请选择修改数据信息');
				close_alert();
			}
		}else{
			$.messager.alert('提示','请选择修改数据信息');
			close_alert();
		}
	}

	function reload() {
		//实现刷新栏目中的数据
		$("#list").datagrid("reload");
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
	
	//查询
	function searchFrom() {
		var node = $('#tDt').tree('getSelected');
		var keyWord = $('#keyWord').textbox('getValue');
		if (node) {
			var id = node.attributes.id;
			if(id){
				$('#list').datagrid('load', {
					modelId : id,
					keyWord: keyWord
				});
			}else{
				$.messager.alert('提示信息','请选择临床路径');
				close_alert();
			}
		}
		
	}
	//加载临床路径树
	$('#tDt').tree({
		url : "<c:url value='/inpatient/clinicalPathwayAction/treeClinicalPath.action'/>",
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
		onLoadSuccess : function(node, data) {
			if(data.length>0){//节点收缩
				$('#tDt').tree('collapseAll');
			}
			$('#tDt').tree('select',$('#tDt').tree('find', 1).target);
			
		},
		onClick : function(node) {//点击节点
			$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			var node = $('#tDt').tree('getSelected');
			console.info(node)
			if (node) {
				var id = node.attributes.id;
				if(id){
					$('#list').datagrid('load', {
						modelId : id
					});
					closeLayout();
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
	//关闭Layout
	function closeLayout() {
		$('#divLayout').layout('remove', 'east');
	}
	function TZFormatter(value,row,index){
		if(value=='0'){
			return "否";
		}else if(value=='1'){
			return "是";
		}
	}
	function SCFormatter(value,row,index){
		if(value=='0'){
			return "否";
		}else if(value=='1'){
			return "是";
		}
	}
	function YYFormatter(value,row,index){
		return "郑州大学第一附属医院";
	}
	function YQFormatter(value,row,index){
		if(value=='1'){
			return "河医院区";
		}else if(value=='2'){
			return "郑东院区";
		}else if(value=='3'){
			return "惠济院区";
		}
	}
</script>
	</body>
</html>