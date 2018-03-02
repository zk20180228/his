<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>组套管理（全院）</title>
<%@ include file="/common/metas.jsp" %>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>    
<script type="text/javascript">
	var oo ="${menuAlias}";
	$(function(){
		//回车事件
		bindEnterEvent("queryName",searchFrom, "easyui")
		$('#list').datagrid({});
		/*************************科室组套树BEGIN************************************/
		//加载部门下的组套树
		$('#tDt').tree({
			url : "<%=basePath %>baseinfo/stack/stackTree.action",
			method : 'get',
			animate : true,
			lines : true,
			fit:true,
			formatter : function(node) {//统计节点总数
				var s = node.text;
				if (node.children.length>0) {
					s += '&nbsp;<span style=\'color:blue\'>('+ node.children.length + ')</span>';
				}
				return s;
			}, 
		    onClick : function(node) {
		    	$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				var node = $('#tDt').tree('getSelected');
				if(node.id!=null){
					if($('#tDt').tree('getSelected').attributes.isNo!=1){//isNo=1 组套
						$('#list').datagrid('load', {
							deptId: node.id
						});
					}
					var eastpanel = $('#divLayout').layout('panel', 'east'); //获取右侧收缩面板
					if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
						$('#divLayout').layout('remove','east');
					} 
				}
			},onContextMenu: function(e, node){
				e.preventDefault();
				// 查找节点
				$('#tDt').tree('select', node.target);
				var id = getSelected();
				if(id!=null){
					if($('#tDt').tree('getSelected').attributes.isNo!=1){
						$('#removeStackTree').css("display","none");
						$('#editStackTree').css("display","none");
						$('#viewStackTree').css("display","none");
					}else{
						$('#removeStackTree').css("display","block");
						$('#editStackTree').css("display","block");
						$('#viewStackTree').css("display","block");
						// 显示快捷菜单
						$('#mm').menu('show', {
							left: e.pageX,
							top: e.pageY
						});
					}
				}
			},onDblClick : function(node) {
			view();
			}
		});
		/*************************科室组套树END************************************/
	});
	/*************************组套操作BEGIN************************************/
	//添加组套
	function addStack(){
		var id = getSelected();
		if(id!=null){
			if($('#tDt').tree('getSelected').attributes.isNo!=1){
				AddOrShowEast('EditForm',"<%=basePath %>baseinfo/stack/addStack.action?flag="+id+"&deptId="+id+"&menuAlias="+oo);
			}else{
				AddOrShowEast('EditForm',"<%=basePath %>baseinfo/stack/addStack.action?flag="+id+"&menuAlias="+oo+"&deptId="+$('#tDt').tree('getSelected').attributes.pid);
			}
		}else{
			$.messager.alert('提示',"请选择具体科室");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	//查看组套
	function view(){
		var id = getSelected();
		if(id!=null){
			if($('#tDt').tree('getSelected').attributes.isNo!=1){//选中科室节点
				$.messager.alert('警告',"请选择组套进行查看！");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}else{
				AddOrShowEast('EditForm',"<%=basePath %>baseinfo/stack/viewStack.action?id="+id);
			}
		}
	}
	//修改组套
	function edit(){
		var id = getSelected();
		if(id!=null){
			if($('#tDt').tree('getSelected').attributes.isNo!=1){//选中科室节点
				$.messager.alert('警告',"请选择组套进行修改！");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}else{ 
				AddOrShowEast('EditForm',"<%=basePath %>baseinfo/stack/editStack.action?id="+id+"&menuAlias="+oo);
			}
		}
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
		var eastpanel = $('#divLayout').layout('panel', 'east'); //获取右侧收缩面板
		if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
			//重新装载右侧面板
			$('#divLayout').layout('panel', 'east').panel({
				href : url
			});
		} else {//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : '100%',
				split : false,
				border:false,
				href : url,
				closable : true
			});
		}
	}
	/**
	 * 关闭面板
	 */
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
	/*************************组套操作END************************************/
	/*************************组套树操作开始************************************/
	//修改组套弹出窗口
	function editStackTree() {
		var node = $('#tDt').tree('getSelected'); 
		AdddilogUpdate("编辑组套分类","<%=basePath %>baseinfo/stack/updateTreeStack.action?id="+node.id);
	// 	$('#tDt').tree('reload');
	}
	//查看组套弹出窗口
	function viewStackTree() {
		var node = $('#tDt').tree('getSelected');
		AdddilogUpdate("查看组套分类","<%=basePath %>baseinfo/stack/viewTreeStack.action?id="+node.id);
		//$('#tDt').tree('reload');
	}
	//删除组套信息
	function removeStackTree() {
		var node = $('#tDt').tree('getSelected');
		$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
			if(res){
				$.messager.progress({text:'删除中，请稍后...',modal:true});
				$.ajax({
					url: "<%=basePath %>baseinfo/stack/delTreeStack.action?id="+node.id,
					type:'post',
					success: function() {
						$.messager.progress('close');
						$.messager.alert('提示','删除成功!');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						$('#tDt').tree('reload');
						$("#list").datagrid("reload");
					},error:function(){
						$.messager.progress('close');
						$.messager.alert('提示','删除失败！');
					}
				});	
			}
		});
	}
	//判断选择的是不是具体的科室
	function getSelected() {
		var node = $('#tDt').tree('getSelected');
		if (node) {
			if(node.id ==1||"type_C"==node.id||"type_D"==node.id
					||"type_F"==node.id||"type_I"==node.id
					||"type_L"==node.id||"type_N"==node.id
					||"type_O"==node.id||"type_OP"==node.id
					||"type_P"==node.id||"type_PI"==node.id
					||"type_S"==node.id||"type_T"==node.id
					||"type_U"==node.id){
				return null;
			}else{
				return node.id;
			}
		} else {
			return null;
		}
	}
	function refresh() {//刷新树
		$('#tDt').tree('reload');
	}
	function expandAll() {//展开树
		$('#tDt').tree('expand',$('#tDt').tree('find', 'type_C').target);
		$('#tDt').tree('expand',$('#tDt').tree('find', 'type_I').target);
		$('#tDt').tree('expand',$('#tDt').tree('find', 'type_F').target);
		$('#tDt').tree('expand',$('#tDt').tree('find', 'type_L').target);
		$('#tDt').tree('expand',$('#tDt').tree('find', 'type_PI').target);
		$('#tDt').tree('expand',$('#tDt').tree('find', 'type_T').target);
		$('#tDt').tree('expand',$('#tDt').tree('find', 'type_D').target);
		$('#tDt').tree('expand',$('#tDt').tree('find', 'type_P').target);
		$('#tDt').tree('expand',$('#tDt').tree('find', 'type_N').target);
		$('#tDt').tree('expand',$('#tDt').tree('find', 'type_OP').target);
		$('#tDt').tree('expand',$('#tDt').tree('find', 'type_O').target);
	}
	
	function collapseAll() {//关闭树C,I,F,L,PI,T,D,P,N,OP,O
		$('#tDt').tree('collapse',$('#tDt').tree('find', 'type_C').target);
		$('#tDt').tree('collapse',$('#tDt').tree('find', 'type_I').target);
		$('#tDt').tree('collapse',$('#tDt').tree('find', 'type_F').target);
		$('#tDt').tree('collapse',$('#tDt').tree('find', 'type_L').target);
		$('#tDt').tree('collapse',$('#tDt').tree('find', 'type_PI').target);
		$('#tDt').tree('collapse',$('#tDt').tree('find', 'type_T').target);
		$('#tDt').tree('collapse',$('#tDt').tree('find', 'type_D').target);
		$('#tDt').tree('collapse',$('#tDt').tree('find', 'type_P').target);
		$('#tDt').tree('collapse',$('#tDt').tree('find', 'type_N').target);
		$('#tDt').tree('collapse',$('#tDt').tree('find', 'type_OP').target);
		$('#tDt').tree('collapse',$('#tDt').tree('find', 'type_O').target);
	}
	function searchFrom() {//查询组套树
		var nodes = $('#tDt').tree('getChecked');
		if (nodes.length > 0) {                        
			for(var i=0; i<nodes.length; i++){
				$('#tDt').tree('uncheck',nodes[i].target);
			};
		}		
		var queryName = $('#queryName').textbox('getValue');
		$.ajax({
			url : "<%=basePath %>baseinfo/stack/queryStackId.action",
			data:{queryName:queryName},
			type : 'post',
			success : function(data) {
				$('#tDt').tree('collapseAll');//单独展开一个节点,先收缩树
				if(data!=null&&data.length>0){
					var node = $('#tDt').tree('find',data[0].id);
					$('#tDt').tree('expandTo', node.target).tree('select',node.target).tree('scrollTo',node.target); 
				}
			}
		});
	}
	//加载dialog
	function AdddilogUpdate(title, url) {
		$('#stackUpdate').dialog({    
		    title: title,    
		    width: '25%',    
		    height:'30%',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true
		   });    
	}
	//关闭dialog
	function closeUpdate() {
		$('#stackUpdate').dialog('close');  
	}
/***************************组套树操作结束******************************************/

// 药品列表查询重置
		function searchReload() {
			$('#queryName').textbox('setValue','');
			refresh();
		}
</script>
<style type="text/css">
#cc .panel-header{
	border-top:0
}
</style>
</head>
<body style="margin: 0px; padding: 0px">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'west',title:'部门科室管理',split:true,border:true,tools:'#toolSMId'" style="width:300px;">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false" style="height: 35px;padding-top: 5px" class="stack">
					&nbsp;<input class="easyui-textbox" id="queryName" name="queryName" data-options="prompt:'科室名称,拼音,五笔,科室,自定义'" placeHolder="" style="width: 50%"/>
					<shiro:hasPermission name="${menuAlias}:function:query ">
					<a onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</shiro:hasPermission>
				</div>
				<div data-options="region:'center',border:false" style="width: 85%">
					<ul id="tDt">组套加载中...</ul>
				</div>
				<div id="toolSMId">
					<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
					<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
					<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
				</div>
			</div>
		</div>
		<div data-options="region:'center',split:false,title:'组套信息'" style="width: 85%" >
			<div id="divLayout" class="easyui-layout" data-options="fit:true,split:false" style="border-top:0;border-left:0">
				<table id="list" style="width:100%;" data-options="toolbar:'#toolbarId',border:false"></table>
				<input type="hidden" name="deptId" id="deptId"/>
				<div id="toolbarId" style="border:0">
					<shiro:hasPermission name="${menuAlias}:function:add">
						<a  onclick="addStack()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:edit">
						<a  onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
					</shiro:hasPermission>
					<a  onclick="view()" class="easyui-linkbutton" data-options="iconCls:'icon-see',plain:true">查看</a>
				</div>
			</div>
		</div>
		<div id="stack"></div>
	</div>
	
	<div id="mm" class="easyui-menu" data-options="" style="width: 120px;">
		<shiro:hasPermission name="${menuAlias }:function:save">
			<div id="removeStackTree" onclick="removeStackTree()" data-options="name:'save',iconCls:'icon-bullet_minus'">
				删除组套
			</div>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias }:function:edit">
			<div id="editStackTree" onclick="editStackTree()" data-options="name:'print',iconCls:'icon-bullet_edit'">
				修改组套
			</div>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:print">
			<div id="viewStackTree" onclick="viewStackTree()" data-options="name:'print',iconCls:'icon-bullet_magnify'">
				查看组套
			</div>
		</shiro:hasPermission>
	</div>
	<div id="stackUpdate"></div>
</body>
</html>