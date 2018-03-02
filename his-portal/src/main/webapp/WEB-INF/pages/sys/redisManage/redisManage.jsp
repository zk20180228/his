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
<title>缓存管理</title>
<style type="text/css">
.panel-header{
	border-top:0
}
</style>
</head>
<body style="margin: 0px; padding: 0px;">
	<div class="easyui-layout" style="width: 100%; height: 100%;"data-options="fit:true">
		<div data-options="region:'west',split:true,tools:'#toolSMId'" title="缓存类别" style="width: 23%; height:100%;padding: 0px;">
			<div class="easyui-layout" data-options="fit:true">
			<div id="toolSMId">
						<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
						<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
						<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
			</div>
			<div data-options="region:'north',border:false" style="height: 35px;padding-top: 5px">
						&nbsp;<input type="text" class="easyui-textbox" id="searchTreeInpId" data-options="prompt:'缓存类别'" style="width: 200px;"/>
						<shiro:hasPermission name="${menuAlias}:function:query ">
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a>
						</shiro:hasPermission>
			</div>
			<div id="treeDiv" style="width: 100%; height: 100%;" data-options="region:'center',border:false">
					<ul id="tDt" class="easyui-tree"></ul>
			</div>
		</div>
		</div>
		<div data-options="region:'center',title:'缓存信息',iconCls:'icon-book',split:true" style="width: 77%; height: 90%;">
			<div style="height:25px;padding:5px">
				关键字：
				<input  class="easyui-textbox" id="btnSearch" data-options="prompt:'key'" style="width: 250px;"/>
				<shiro:hasPermission name="${menuAlias}:function:query ">
					<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
				</shiro:hasPermission>
			</div>
			<div style="height:calc(100% - 35px);border-left: 0px">
				<table id="grid2" style="width:100%;" data-options="fit:true,border:false"></table>
			</div>
			<div id="tb">
				<a onclick="selectAll(2)" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true">全选</a>
				<a onclick="unSelectRow(2)" class="easyui-linkbutton" data-options="iconCls:'icon-arrow_turn_left',plain:true">反选</a>
				<shiro:hasPermission name="${menuAlias}:function:initialization">
					<a onclick="initData()" class="easyui-linkbutton" data-options="iconCls:'icon-initialization',plain:true">初始化</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:set">
					<a onclick="setFailedTime()" class="easyui-linkbutton" data-options="iconCls:'icon-cog_edit',plain:true">设置过期时间</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:delete">
					<a onclick="delRows()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
				</shiro:hasPermission>
			</div>
			
		</div>
	</div>
	
	<div id="win" class="easyui-window" title="初始化" style="width: 30%; height: 70%;" data-options="iconCls:'icon-book',collapsible:false,minimizable:false,maximizable:false,closed:true,modal:true">
		<div class="easyui-layout" style="width: 100%; height: 100%" data-options="fit:true">
						<div data-options="region:'west',split:false" style="width: 45%; height: 100%;padding-top: 15%;padding-left: 7%">
							<input id="check1" type="checkbox"  value='0'>全选<br/><br/>
							<input id="check2" type="checkbox"  value='1'>反选<br/><br/>
							<button type="button" id="btnSave" class="easyui-linkbutton">确定</button>
						</div>
						<div data-options="region:'center',split:false" style="width: 55%; height: 100%">
							<table id="grid1" style="width: 100%" data-options="fit:true"></table>
						</div>
					</div>
	</div> 
	<div id="winTime" class="easyui-window" title="过期时间设置" style="width: 30%; height: 70%;" data-options="iconCls:'icon-book',collapsible:false,minimizable:false,maximizable:false,closed:true,modal:true">
		<div class="easyui-layout" style="width: 100%; height: 100%" data-options="fit:true">
						<div data-options="region:'center',split:false" style="width: 55%; height: 100%">
							<table id="gridT" style="width: 100%" data-options="fit:true"></table>
						</div>
						<div data-options="region:'east',split:true" style="width: 45%; height: 100%;padding-top: 15%;"align="center" >
						过期时间:<input id="outTime" class="easyui-numberbox" data-options="required:true,missingMessage:'请输入过期时间'" style="width:120px"/><br><br>
						注:过期时间单位为秒,输入负数代表永不过期.<br><br>
						<button type="button" id="btnSaveTime" class="easyui-linkbutton">确定</button>
						</div>
					</div>
	</div> 
</body>
<script type="text/javascript">
(function($){	
	$.extend($.fn.tree.methods, {
		searchOne: function(jqTree, param) {
			var searchText = "";
			var isCanOpen = false;
			if(jQuery.isArray(param)){
				searchText = param[0];
				isCanOpen = param[1];
			}else{
				searchText = param;
			}
			var tree = this;
			var nodeList = getAllNodes(jqTree, tree);
			searchText = $.trim(searchText);
			if (searchText == "") {
				for (var i=0; i<nodeList.length; i++) {
					$(".tree-node-targeted", nodeList[i].target).removeClass("tree-node-targeted");
					$(nodeList[i].target).show();
				}
				var selectedNode = tree.getSelected(jqTree);
				if (selectedNode) {
					tree.expandTo(jqTree, selectedNode.target);
				}
				return;
			}
			var matchedNodeList = [];
			if (nodeList && nodeList.length>0) {
				var node = null;
				for (var i=0; i<nodeList.length; i++) {
					node = nodeList[i];
					if (isMatch(searchText,node.text)) {
						matchedNodeList.push(node);
					}
				}
				//隐藏所有节点
				for (var i=0; i<nodeList.length; i++) {
					$(".tree-node-targeted", nodeList[i].target).removeClass("tree-node-targeted");
					$(nodeList[i].target).hide();
				}			
				//折叠所有节点
				tree.collapseAll(jqTree);
				//展示所有匹配的节点以及父节点			
				for (var i=0; i<matchedNodeList.length; i++) {
					showMatchedNode(jqTree, tree, matchedNodeList[i],isCanOpen);
				}
			} 	 
		}
		})
		function showMatchedNode(jqTree, tree, node,isCanOpen) {
		//展示所有父节点
		$(node.target).show();
		$(".tree-title", node.target).addClass("tree-node-targeted");
		var pNode = node;
		while ((pNode = tree.getParent(jqTree, pNode.target))) {
			$(pNode.target).show();			
		}
		//展开到该节点
		tree.expandTo(jqTree, node.target);
		//如果是非叶子节点，需折叠该节点的所有子节点
		if (!tree.isLeaf(jqTree, node.target)) {
			tree.collapse(jqTree, node.target);
			if(isCanOpen){
				$(jqTree).tree('showChildren',node);
			}
		}
	}	 
	/**
	 * 判断searchText是否与targetText匹配
	 * @param searchText 检索的文本
	 * @param targetText 目标文本
	 * @return true-检索的文本与目标文本匹配；否则为false.
	 */
	function isMatch(searchText, targetText,wb,pinyin,inputcode) {
		return ($.trim(targetText)!="" && targetText.indexOf(searchText)!=-1);
	}
	
	/**
	 * 获取easyui tree的所有node节点
	 */
	function getAllNodes(jqTree, tree) {
		var allNodeList = jqTree.data("allNodeList");
		var roots = tree.getRoots(jqTree);
		allNodeList = getChildNodeList(jqTree, tree, roots);
		jqTree.data("allNodeList", allNodeList);
		return allNodeList;
	}
	
	/**
	 * 定义获取easyui tree的子节点的递归算法
	 */
	function getChildNodeList(jqTree, tree, nodes) {
		var childNodeList = [];
		if (nodes && nodes.length>0) {			
			var node = null;
			for (var i=0; i<nodes.length; i++) {
				node = nodes[i];
				childNodeList.push(node);
				if (!tree.isLeaf(jqTree, node.target)) {
					var children = tree.getChildren(jqTree, node.target);
					childNodeList = childNodeList.concat(getChildNodeList(jqTree, tree, children));
				}
			}
		}
		return childNodeList;
	}
	})(jQuery);
	function searchTreeNodes(){
	    var searchText = $('#searchTreeInpId').textbox('getValue');
	    $("#tDt").tree("searchOne", searchText);
	}
$(function(){
	 bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
	 bindEnterEvent('btnSearch',searchFrom,'easyui');
	$('#tDt').tree({
		url:'<%=basePath%>sys/redisTree.action',
		animate:true,
		lines:true,
		onLoadSuccess:function(node,data){
			if(data!=null){
				$('#tDt').tree('collapseAll');
				$('#tDt').tree('expandTo',$('#tDt').tree('find', 1).target);
			}
		},
		onClick:function(node){
			$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			var id= node.text;
			var url='<%=basePath%>sys/getKeys.action?id='+id;
			$('#grid2').datagrid('load', url);
		}
	});
	
	$('#grid2').datagrid({
		url:'<%=basePath%>sys/getKeys.action',
		columns:[[
		          {field:'-',title:'-',width:'2%',checkbox:true},
		          {field:'keyName',title:'key名称',width:'50%',formatter:
		        	  function(value,row,index){
			        	  var v=format(value);
			        	  return v;
		              }
		          },
		          {field:'keyTime',title:'过期时间',width:'20%'},
		          ]],
		toolbar: '#tb',
		pagination:true,
		pageSize:30,
		onLoadSuccess: function(data){
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
			}
		}
	});

	$('#grid1').datagrid({
		url:'<%=basePath%>sys/initData.action',
		columns:[[
		          {field:'-',title:'-',width:'2%',checkbox:true},
		          {field:'id',title:'初始化列表',hidden:true},
		          {field:'fieldName',title:'初始化列表',width:'50%'}
		          ]],
		 rownumbers:true
	});
	
	$('#check1').bind('click',function(){
		selectAll(1);
	});
	$('#check2').bind('click',function(){
		unSelectRow(1);
	});
	
	$('#btnSave').bind('click',function(){
		$('#win').window('close');
		var ids='';
		var rows=$('#grid1').datagrid('getChecked');
		if(rows.length>0){
			for (var i = 0; i < rows.length; i++) {
				ids=rows[i].id+","+ids;
			}
			$.ajax({
				url:'<%=basePath%>sys/initValue.action',
				data:{"json":ids},
				type:'post',
				dataType:'json',
				success:function(value){
					if(value){
						$.messager.alert("提示", "初始化完成！", "info");
						setTimeout(function(){
							  $(".messager-body").window('close');  
							},1000);
						$('#grid2').datagrid('reload');
					}
				}
			});
		}
	});
	
	$('#btnSaveTime').bind('click',function(){
		var t=$('#outTime').numberbox('getValue');
		if(t==null||t==''){
			$.messager.alert("提示", "请输入过期时间！", "error");
			close_alert();
			return;
		}
		$('#winTime').window('close');
		var rows=$('#gridT').datagrid('getRows');
		var json=t;
		for (var i = 0; i < rows.length; i++) {
			json+=','+rows[i].keyName;
		}
		
		$.ajax({
			url:'<%=basePath%>sys/setKeysTime.action?menuAlias=${menuAlias}',
			data:{"json":json},
			type:'post',
			dataType:'json',
			success:function(value){
				if(value){
					$.messager.alert("提示", "设置成功！", "info");
					setTimeout(function(){
						  $(".messager-body").window('close');  
						},1000);
					$('#grid2').datagrid('reload');
				}else{
					$.messager.alert("提示", "设置失败！", "info");
				}
			}
		});
	});
});

/*
 * 查询功能
 */
function searchFrom(){
	var id=$.trim($('#btnSearch').val());
	var url='<%=basePath%>sys/getKeys.action?id='+id;
	$('#grid2').datagrid('load', url);
}

/**
 * 全选功能
 */
function selectAll(i){
	$('#grid'+i).datagrid('selectAll');
}

/**
 * 反选功能
 */
function unSelectRow(i) {  
    var s_rows = $.map($('#grid'+i).datagrid('getSelections'),  
            function(n) {  
                return $('#grid'+i).datagrid('getRowIndex', n);  
            });  
    $('#grid'+i).datagrid('selectAll');  
    $.each(s_rows, function(m, n) {  
        $('#grid'+i).datagrid('unselectRow', n);  
    });  
}

/**
 * 删除功能
 */
function delRows(){
	var keys='';
	var rows=$('#grid2').datagrid('getChecked');
	if(rows.length==0){
		$.messager.alert("提示", "请选择要删除的行！", "info");
		close_alert();
		return;
	}else{
		$.messager.confirm('提示','确定要删除吗?',function(res){
			if(res){
				for (var i = 0; i < rows.length; i++) {
					var index=$('#grid2').datagrid("getRowIndex",rows[i]);
					var key=rows[i].keyName;
					keys=key+","+keys;
					//删除行
					$('#grid2').datagrid('deleteRow', index);
					//获取表格数据
					var data = $('#grid2').datagrid('getData');
				}
				
				$.ajax({
					url:'<%=basePath%>sys/delKeys.action',
					data:{"json":keys},
					type:'post',
					dataType:'json',
					success:function(value){
						if(value){
							$.messager.alert("提示", "已删除！", "info");
							setTimeout(function(){
								  $(".messager-body").window('close');  
								},500);
						}
					}
				});
			}
		});
	}
}

/**
 * 初始化功能
 */
function initData(){
	$('#win').panel('resize',{    
		  left:'60%',    
		  top:'20%'    
		}); 
	$('#win').window('open');
}

/**
 * 设置过期时间功能
 */
function setFailedTime(){
	var rows=$('#grid2').datagrid('getChecked');
	if(rows.length==0){
		$.messager.alert("提示", "请选择要设置的行！", "info");
		close_alert();
		return;
	}else{
		$('#gridT').datagrid({
			rownumbers:true,
			columns:[[    
			          {field:'keyName',title:'key名称',width:'90%',formatter:
		        	  function(value,row,index){
			        	  var v=format(value);
			        	  return v;
		              }}    
			      ]] 
		});
		$('#gridT').datagrid('loadData',{total:0,rows:[]});
		$('#outTime').numberbox('clear');
		for (var i = 0; i < rows.length; i++) {
			$('#gridT').datagrid('appendRow',{
				keyName: rows[i].keyName,
			});
		$('#winTime').window('open');
		}
	}
}

/**
 * 格式化功能
 */
function format(s){
	if(typeof(s)=='undefined'){
		  s='';
	  }
	  if(s.indexOf("serIdq")!=-1){
		s=s.substring(s.indexOf("serIdq")+13);
	  }
	  if(s.indexOf("HONRYDB_")!=-1){
		s=s.substring(s.indexOf("HONRYDB_")+8);
	  }
	  if(s.indexOf("bean.model.")!=-1){
		s=s.substring(s.indexOf("bean.model.")+11);
	  }
	  if(s.indexOf("honry_user_")!=-1){
		s=s.substring(s.indexOf("honry_user_")+11);
	  }
	  return s;
}
//缓存树操作
function refresh(){//刷新树
	$('#tDt').tree('options').url = "<%=basePath%>sys/redisTree.action";
 	$('#tDt').tree('reload'); 
}
function expandAll(){//展开树
	$('#tDt').tree('expandAll');
}
function collapseAll(){//关闭树
	$('#tDt').tree('collapseAll');
	$('#tDt').tree('expand',$('#tDt').tree('find', 0).target);
}
</script>
</html>