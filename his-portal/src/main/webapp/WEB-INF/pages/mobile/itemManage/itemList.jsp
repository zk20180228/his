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
<title>移动端模块管理</title>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
/* #toolbarId { */
/* 	background-color: #ced3d8; */
/* } */
.layout-split-west {
	border-right: 5px solid #ced3d8;
}

#divLayout .datagrid-pager {
	width: 82%;
	position: fixed;
	bottom: 0px;
}

#divLayout .datagrid {
	height: 100% !important;
}

#divLayout .datagrid-wrap {
	height: 100% !important;
}
</style>



<script type="text/javascript">
var cid="1";
			$(function(){
				//加载节点树					
				$('#tt').tree({    
				    url:'<%=basePath%>oa/itemManage/queryAllType.action',
				    method:'post',
				    animate:true,
				    lines:true,
				    onLoadSuccess : function(node, data) {
				    	var node = $('#tt').tree('find', "ROOT");
				    	$('#tt').tree('select', node.target);
				    },
					onClick:function(node){
						var id=(node.attributes.ids);
						var parentId=(node.attributes.parentId);
						var path=(node.attributes.path);
						loadDatagrid(id,parentId,path);
					},
					onBeforeCollapse:function(node){
						if(node.id=="1"){
							return false;
						}
				    },
				    onContextMenu: function(e, node){
				    	e.preventDefault();
				    	$('#tt').tree('select', node.target);
				    	if(node.attributes.path==""||node.attributes.path==null){
					    	$('#mm').menu('show', {
								left: e.pageX,
								top: e.pageY
							});
				    	}
				    },
				}); 
			
				$('#dg').datagrid({  
					url:'<%=basePath%>oa/itemManage/getItemsByType.action?cid='+cid,
					method:'get',
					toolbar:"#toolbarId",
					pageList: [20, 30, 50],
					pageSize:20,
					pagination: false,
					columns:[[    
					          {field:'field:',checkbox:true},
					          {field:'code',title:'项目Code',width:"10%"},    
					          {field:'name',title:'项目名称',width:"15%"},    
					          {field:'typeName',title:'项目分类名称',width:"15%"},  
					          {field:'path',title:'路径',width:"20%"},   
					      ]],
					});
			
			$('#add-window').window({    
				width:'80%',    
			    height:'90%',     
			    modal:true,
			    title:"添加"
			}).window('close');
			
		}); 
			
			function loadDatagrid(id,parentId,path){
				$('#dg').datagrid({  
					url:'<%=basePath%>oa/itemManage/getItemsByType.action',
					queryParams:{id:id,parentId:parentId,path:path},
					method:'post',
					toolbar:"#toolbarId",
					pageList: [20, 30, 50],
					pageSize:20,
					pagination: false,
					columns:[[    
					          {field:'field:',checkbox:true},
					          {field:'code',title:'项目Code',width:"10%"},    
					          {field:'name',title:'项目名称',width:"15%"},    
					          {field:'typeName',title:'项目分类名称',width:"15%"},  
					          {field:'path',title:'路径',width:"20%"},
					      ]],
					});
			}	
			//加载标题模式窗口
			function AdddilogModel(id,title,url,width,height) {
				$('#'+id).dialog({    
				    title: title,
				    width: width,
				    height: height,
				    closed: false,
				    cache: false,
				    href: url,
				    resizable:true,
				    modal: true
				});    
			}
			
			
			//关闭窗口
			function closeW(){
				$('#addContent-window').window('close');
			}
			
			//弹出添加栏目页面
			function addContent(){
				var nodes = $('#tt').tree('getSelected');
				if(nodes == null){
					AdddilogModel("addContent-window","添加",'<%=basePath%>oa/itemManage/addItem.action','500px','250px');
				}else{
					var id=(nodes.attributes.ids);
					if(id!="ROOT"){
						AdddilogModel("addContent-window","添加",'<%=basePath%>oa/itemManage/addItem.action?id='+id+"&typeName"+nodes.text,'500px','250px');
					}
				}
			}
			//弹出修改栏目页面
			function editContent(){
				var node= $('#dg').datagrid('getChecked');
				if(node.length > 0){
					AdddilogModel("addContent-window","编辑",'<%=basePath%>oa/itemManage/editItem.action?id='+node[0].parentId+'&&ids='+node[0].id,'500px','250px');
				}else{
					$.messager.alert('提示','请先选择要修改的信息!'); 
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
			 		 return;
				}
			}
			//删除
			function delContent(){
				var rows = $('#dg').datagrid('getChecked');
			 	if (rows.length > 0) {//选中几行的话触发事件	                        
					$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
						if (res){
							var ids = '';
							for(var i=0; i<rows.length; i++){
								if(ids!=''){
									ids += ',';
								}
								ids += rows[i].id;
							};
							
							$.ajax({
							 	url: "<%=basePath%>oa/itemManage/delItem.action?ids="+ids,   
								type:'post',
								success: function(data) {
									if (data.resCode == 'success') {
										$.messager.alert('提示',data.resMsg);
										reload();
									}else if(data.resCode == 'error'){
										$.messager.alert('提示',data.resMsg);
										reload();
									}
								}
							});
						}
			     });
			 }else{
				$.messager.alert('提示','请先选择要删除的信息!'); 
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
			    return;
			 }
			}
			//刷新
			function reload(){
				//实现刷新栏目中的数据
				$('#dg').datagrid('reload');
				$('#tt').tree('reload');
			}
			function refresh(){
				$('#tt').tree('reload');
			}
			function collapseAll() {//关闭树
				$('#tt').tree('collapseAll');
			}
			function expandAll() {//展开树
				$('#tt').tree('expandAll');
			}
			
			function add1(){
				var node=$("#tt").tree("getSelected");
				var id=(node.attributes.ids);
				var type=(node.attributes.type);
				AdddilogModel("addContent-window","添加",'<%=basePath%>oa/itemManage/addItemType.action?id='+id+'&&type='+type,'500px','250px');
			}
			function del(){
				var node=$("#tt").tree("getSelected");
				var cid=node.attributes.type;
				var id = node.attributes.id;
				var parentId = node.attributes.parentId;
				if(cid != null){
					$.messager.confirm('确认', '确定要删除选中信息及其子项吗?',function(res){
						$.ajax({
						 	url: "<%=basePath%>oa/itemManage/delType.action",
						 	data:{cid:cid,id:id,parentId:parentId},
							type:'post',
							success: function(data) {
								if (data.resCode == 'success') {
									$.messager.alert('提示',data.resMsg);
									closeLayout();
								}else if(data.resCode == 'error'){
									$.messager.alert('提示',data.resMsg);
								}
								reload();
							}
						});
					})
				}
			}
			
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" data-options="fit:true">
		<div id='p'
			data-options="region:'west',title:'管理',split:true,border:true,tools:'#toolSMId'"
			style="width: 300px;">
			<div class="easyui-layout" data-options="fit:true">
				<ul id="tt" style="width: 100%; height: 100%; overflow: auto"></ul>
				<div id="addData-window"  style="overflow :'hidden'"></div>
				<div id="addContent-window" style="overflow :'hidden'"></div>
			</div>
			<div id="mm" class="easyui-menu" style="width: 120px;">
				<div onclick="add1()" data-options="iconCls:'icon-add'">添加</div>
				<div onclick="del()" data-options="iconCls:'icon-remove'">删除</div>
			</div>
			<div id="toolSMId">
				<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
				<a href="javascript:void(0)" onclick="collapseAll()"
					class="icon-fold"></a> <a href="javascript:void(0)"
					onclick="expandAll()" class="icon-open"></a>
			</div>
		</div>
		<div data-options="region:'center'" style="height: 80%">
			<table id="dg" style="height: 850px"></table>
			<div id="toolbarId">
				<a href="javascript:void(0)" onclick="addContent()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true">添加</a> <a
					href="javascript:void(0)" onclick="editContent()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-edit',plain:true">修改</a>
				<shiro:hasPermission name="${menuAlias}:function:delete">
					<a href="javascript:void(0)" onclick="delContent()"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-remove',plain:true">删除</a>
				</shiro:hasPermission>
			</div>
		</div>
	</div>

</body>
</html>
