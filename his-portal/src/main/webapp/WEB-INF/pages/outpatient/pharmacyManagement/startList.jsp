<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>门诊药房终端维护初始界面</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">

	//加载页面
	$(function() {
		//栏目树
		trr();
		$('iframe').attr('src','<%=basePath %>drug/pharmacyManagement/terminalList.action?menuAlias=${menuAlias}');
	});
	
	//病房树初始化
	function trr(){
		$('#tDt').tree({
				url : '<%=basePath %>drug/pharmacyManagement/pharmacyTree.action',
				method:'get',
				lines : true,
				cache : false,
				animate : true,
				onBeforeCollapse : function(node) {
					if ($('#tDt').tree('isLeaf',node) && node.id == 1) {
						return false;
					}
				},
				formatter : function(node) {//统计节点总数
					var s = node.text;
					if (node.children.length > 0) {
						s += '&nbsp;<span style=\'color:blue\'>('
								+ node.children.length + ')</span>';
					}
					return s;
				},onClick : function(node) {//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					if(node.id=='terminal'){   //门诊终端维护
						$('iframe').attr('src','<%=basePath %>drug/pharmacyManagement/terminalList.action?menuAlias=${menuAlias}');
					}else if(node.id=='recipe'){  //门诊处方调剂
						$('iframe').attr('src','<%=basePath %>drug/pharmacyManagement/recipeList.action?menuAlias=${menuAlias}&pid='+getSelected(2));
					}else if(node.id=='terminalOpen'){  //门诊终端维护模版	
						$('iframe').attr('src','<%=basePath %>drug/pharmacyManagement/terminalOpenList.action?menuAlias=${menuAlias}');
					}
				},onLoadSuccess : function(node, data) {//默认选中
// 					$('#tDt').tree('select',$('#tDt').tree('find', 'terminal').target);
				}
		});
	}
	
	function expandAll(){//展开树
		$('#tDt').tree('expandAll');
	}
	function collapseAll(){//关闭树
		$('#tDt').tree('collapseAll');
	}
	
	/**
	 * tag=0获取nodetype
	 * tag=1获取选中节点ID
	 * tag = 2 父节点ID  
	 * tag=3 判断选中的是否是叶子节点，如果是叶子节点则获取id，否则赋值1
	 * tag = 4 所选节点名称
	 */
	function getSelected(tag) {
		var node = $('#tDt').tree('getSelected');//获取所选节点
		if (node != null) {
			var Pnode = $('#tDt').tree('getParent', node.target);
			if (Pnode) {
				if (tag == 0) {
					var nodeType = node.nodeType;
					return nodeType;
				}
				if (tag == 1) {
					var id = node.id;
					return id;
				}
				if (tag == 2) {
					var pid = Pnode.id;
					return pid;
				}
				if (tag == 3) {
					if ($('#tDt').tree('isLeaf', node.target)) {//判断是否是叶子节点
						var id = node.id;
						return id;
					} else {
						return 1;
					}
				}
				if(tag==4){
					var text = node.text;
					return text;
				}
			}
		} else {
			return 1;
		}
	}
</script>
<style type="text/css">
.panel-header{
	border-top:0;
}
</style>
</head>
<body style="margin: 0px;padding: 0px;">
	<div class="easyui-layout"  fit=true style="width: 100%; height: 100%; overflow-y: auto;">
		<div id="p" data-options="region:'west',split:true,tools:'#toolSMId'" title="门诊药房" style="width:15%; height:100%;padding: 0px; overflow: hidden;border-top:0">
			<div id="toolSMId">
				<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
			</div>
			<div id="treeDiv" style="width: 100%; height: 100%; overflow-y: auto;">
				<ul id="tDt">数据加载中...</ul>
			</div>
		</div>
		<div id="iframe" data-options="region:'center'" style="border-top:0">
			<iframe  name="terminal" 
				style="width:100%; height:100%;float:right" frameborder=0 marginwidth=0 marginheight=0></iframe>
		</div>
	</div>
</body>
</html>