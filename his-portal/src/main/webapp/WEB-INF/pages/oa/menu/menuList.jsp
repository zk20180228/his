<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>栏目管理</title>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
/* #toolbarId { */
/* 	background-color: #ced3d8; */
/* } */
.layout-split-west{
	border-right: 5px solid #ced3d8;
}
#divLayout .datagrid-pager {
	width: 82%;
    position: fixed;
    bottom: 0px;
}
#divLayout .datagrid{
	height: 100% !important;
}

#divLayout .datagrid-wrap{
	height: 100% !important;
}	
</style>



<script type="text/javascript">
var cid="1";
			$(function(){
				
// 				加载节点树					
				$('#tt').tree({    
				    url:'<%=basePath%>oa/menuManager/showTree.action',
				    method:'get',
				    animate:true,
				    lines:true,
					onClick:function(node){
						var noid=(node.id);
						loadDatagrid(noid);
					},
					onBeforeCollapse:function(node){
						if(node.id=="1"){
							return false;
						}
				    },
				    onLoadSuccess : function(node, data) {
						if(data.length>0){//节点收缩
							$('#tt').tree('collapseAll');
							loadDatagrid("0");
						}
				    },
				}); 
			
			function loadDatagrid(cid){
				$("#toolbarId").show();
				$('#dg').datagrid({  
					url:'<%=basePath%>oa/menuManager/queryMenuById.action?id='+cid,
					method:'get',
					toolbar:"#toolbarId",
					singleSelect:true,
					onDblClickRow : function(row,index){
						var code = index.code;
						AdddilogModel("addContent-window","栏目查看",'<%=basePath%>oa/menuManager/menuInfo.action?code='+code,'40%','60%');
					},
					columns:[[    
					          {field:'name',title:'栏目名称',width:"15%",},    
					          {field:'publishdirt',title:'发布类型',width:"10%",
					        	  formatter:function(value,row,index){
					        		  if(value == 0){
					        			  return "审核发布";
					        		  }else{
					        			  return "直接发布";
					        		  }
					          },},    
//						          {field:'mcomment',title:'评论',width:"15%",formatter:function(value,row,index){
//					        		  if(value == "1"){
//					        			  return "开启评论";
//					        		  }else{
//					        			  return "关闭评论";
//					        		  }
//					          },},    
					          {field:'stop_flag',title:'状态',width:"15%",formatter:function(value,row,index){
				        		  if(value == 0){
				        			  return "启用";
				        		  }else{
				        			  return "停用";
				        		  }
				          },},    
					          {field:'explain',title:'栏目说明',width:"15%",},    
					      ]],
					});
			}	
			
			$('#add-window').window({    
				width:'80%',    
			    height:'90%',     
			    modal:true,
			    title:"添加栏目"
			}).window('close');
			
		}); 
			
			//加载标题模式窗口
			function AdddilogModel(id,title,url,width,height) {
				$('#'+id).dialog({    
				    title: title,
				    width: width,
				    height: height,
				    closed: false,
				    cache: false,
				    href: url,
				    modal: true
				});    
			}
			
			
			//关闭窗口
			function closeLayout(){
				$('#addContent-window').window('close');
			}
			
			//弹出添加栏目页面
			function addContent(){
				var nodes = $('#tt').tree('getSelected');
				if(nodes == null){
					AdddilogModel("addContent-window","栏目添加",'<%=basePath%>oa/menuManager/addMenu.action','40%','60%');
				}else{
					AdddilogModel("addContent-window","栏目添加",'<%=basePath%>oa/menuManager/addMenu.action?id='+nodes.id,'40%','60%');
<%-- 						$("#add-window .iframeid")[0].src = '<%=basePath%>oa/menuManager/addMenu.action?id='+nodes.id'; --%>
// 						$('#add-window').window('open');
				}
			}
			//弹出修改栏目页面
			function editContent(){
				var node= $('#dg').datagrid('getChecked');
				if(node.length > 0){
					AdddilogModel("addContent-window","栏目编辑",'<%=basePath%>oa/menuManager/editMenu.action?code='+node[0].code,'40%','60%');
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
							var ids = rows[0].id;
							var code = rows[0].code;
							$.ajax({
							 	url: "<%=basePath%>oa/menuManager/delMenu.action?id="+ids+"&code="+code,   
								type:'post',
								success: function() {
									$.messager.alert('提示信息','删除成功！');
									reload();
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
			
			//上移
			function moveUp() {
				var row = $('#dg').datagrid('getSelected');
				if(row == null){
					$.messager.alert('提示','请先选择一条数据!'); 
					setTimeout(function(){
						$(".messager-body").window('close');
					},1500);
			 	 return;
				}
				var id = row.id;
				var morder = row.morder;
				var parentcode = row.parentcode;
				var index = $("#dg").datagrid('getRowIndex', row);
				mysort(index,id,morder,parentcode,'up', 'dg');
			}
			//下移
			function moveDown() {
				var row = $('#dg').datagrid('getSelected'); 
				if(row == null){
					$.messager.alert('提示','请先选择一条数据!'); 
					setTimeout(function(){
						$(".messager-body").window('close');
					},1500);
			 	 return;
				}
				var id = row.id;
				var morder = row.morder;
				var parentcode = row.parentcode;
				var index = $("#dg").datagrid('getRowIndex', row);
				mysort(index,id,morder,parentcode, 'down', 'dg');
			}
			//传向后台  移动
			function mysort(index,id,morder,parentcode, type, gridname) {
				var rows = $('#' + gridname).datagrid('getRows').length;
				console.log(rows)
				if (index == 0 && 'up' == type){
					$.messager.alert('提示信息','第一条数据不能上移!');
					setTimeout(function(){
						$(".messager-body").window('close');
					},1500);
				}
				else if( index == rows - 1 && 'down' == type ){ 
					$.messager.alert('提示信息','最后一条数据不能下移!');
					setTimeout(function(){
						$(".messager-body").window('close');
					},1500);
				}
				else{
						$.ajax({
					 	url: "<%=basePath%>oa/menuManager/move.action",
					 	data:{id:id,type:type,morder:morder,parentcode:parentcode},
						type:'post',
						success: function(date) {
							$('#dg').datagrid('reload');
						}
					});
				}
			}
</script> 
</head>
<body>
		<div id="divLayout" class="easyui-layout" data-options="fit:true">
			<div id='p' data-options="region:'west',title:'OA栏目管理',split:true,border:true,tools:'#toolSMId'" style="width:300px;">
				<div class="easyui-layout" data-options="fit:true">	
					  <ul id="tt" style="width:100%;height:100%;overflow:auto"></ul>  							 
					 <div id = "addData-window"></div>
					 <div id = "addContent-window"></div>
				</div>
				<div id="toolSMId">
						<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
						<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
						<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
					</div>
			</div>
	<div data-options="region:'center'" style="height:80%">	
		 <table id="dg" style="height:850px"></table>
		 <div id="toolbarId" >
				<a href="javascript:void(0)"  onclick="addContent()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
				<a href="javascript:void(0)" onclick="editContent()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
			<shiro:hasPermission name="${menuAlias}:function:delete">
				<a href="javascript:void(0)" onclick="delContent()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		  	</shiro:hasPermission>
		  		<a href="javascript:void(0)" onclick="moveUp()" class="easyui-linkbutton" data-options="iconCls:'icon-up',plain:true">上移</a>
				<a href="javascript:void(0)" onclick="moveDown()" class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:true">下移</a>
		  </div>
	 </div>
</div>

</body>
</html>


