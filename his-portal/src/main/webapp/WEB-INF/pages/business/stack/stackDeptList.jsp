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
<title>组套管理（科室）</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
 <script type="text/javascript">
	 	var deptId = "${deptId}";
	 	var deptCode = deptId;
		var deptidlist = "";
		var oo ="${menuAlias}";
		//加载页面
		$(function(){
			//加载部门树
			$('#tDt').tree({
				url : "<c:url value='/baseinfo/stack/stackDeptTree.action'/>",
				method : 'get',
				animate : true,
				lines : true,
				onlyLeafCheck:true,
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
						if((node.attributes)!=null){
							if($('#tDt').tree('getSelected').attributes.isNo!=1){//isNo=1 组套
								$('#list').datagrid('load', {
									deptId: node.id
								});
							}
						}
						var eastpanel = $('#divLayout').layout('panel', 'east'); //获取右侧收缩面板
						if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
							$('#divLayout').layout('remove','east');
						} 
					}

				},
				onContextMenu : function(e, node) {
					e.preventDefault();
					$(this).tree('select',node.target);
					var id = getSelected();
					if(id!=null){
						if($('#tDt').tree('getSelected').id==deptId){
							$('#removeStackTree').css("display","none");
							$('#editStackTree').css("display","none");
							$('#viewStackTree').css("display","none");
						}
						else{
							$('#removeStackTree').css("display","block");
							$('#editStackTree').css("display","block");
							$('#viewStackTree').css("display","block");
							$('#mm').menu('show',{
								left: e.pageX,
								top: e.pageY
							});
						}
					}
				},onDblClick : function(node) {
					view();
				},onLoadSuccess:function(node, data){
					   console.info(data);
					   if(data.resCode=='error'){
						   $("body").setLoading({
								id:"body",
								isImg:false,
								text:data.resMsg
							});
					   }
				   }
			});
			$.ajax({
				url: "<c:url value='/baseinfo/department/queryDepartments.action'/>",
				type:'post',
				success: function(datas) {
					deptidlist = datas;
				}
			});	
			setTimeout(function(){
				$('#list').datagrid({
				});
            },1);
		});
		function view(){
			var id = getSelected();
			if(id!=null){
				if($('#tDt').tree('getSelected').id==deptId){//选中科室节点
					$.messager.alert('警告',"请选择组套进行查看！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					AddOrShowEast('EditForm',"<c:url value='/baseinfo/stack/viewStack.action'/>?id="+id);
				}
			}
		}
		function addStack(){
			var id = getSelected();
			if(id!=null){
				if($('#tDt').tree('getSelected').id==deptCode){
					AddOrShowEast('EditForm',"<c:url value='/baseinfo/stack/addStack.action'/>?deptId="+id+"&menuAlias="+oo);
				}else{
					AddOrShowEast('EditForm',"<c:url value='/baseinfo/stack/addStack.action'/>?deptId="+deptId+"&menuAlias="+oo);
				}
			}else{
				$.messager.alert('警告',"请选择科室");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}
		function edit(){
			var id = getSelected();
			if(id!=null){
				if($('#tDt').tree('getSelected').id==deptId){//选中科室节点
					$.messager.alert('警告',"请选择组套进行修改！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					AddOrShowEast('EditForm',"<c:url value='/baseinfo/stack/editStack.action'/>?id="+id+"&menuAlias="+oo);
				}
			}
		}
		function getSelected() {
			var node = $('#tDt').tree('getSelected');
			if (node) {
				if(node.id !=1){
					return node.id;
				}
			} else {
				return null;
			}
		}
		function del(){
			 //选中要删除的行
			 var rows = $('#list').datagrid('getSelections');
			 if (rows.length > 0) {                        
			 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){
			 		$.messager.progress({text:'删除中，请稍后...',modal:true});
			 	if (res){
					var ids = '';
					for(var i=0; i<rows.length; i++){
					if(ids!=''){
						ids += ',';
					}
					ids += rows[i].id;
				};
				$.post("<c:url value='/baseinfo/stack/delStack.action'/>?id="+ids,function(data){
					$.messager.progress('close');	
					  var retVal = data;
					  if(retVal=="no"){
						  $.messager.alert('警告',"删除失败!");
					  }else if(retVal=="yes"){
						  $.messager.alert('警告',"操作成功！");
						  setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						  window.location="<%=basePath%>baseinfo/stack/listStack.action";
							}
					  else {
						  $.messager.alert('警告',"连接错误，操作失败!");
							}
						});
					}
				});
			}
		}
		function delInfo() {
			var id = getId("plurality");
			if (id != null) {
				$.post("<c:url value='/baseinfo/stack/delStack.action'/>?id="+id);
				del('#list');
			}
		}
		function reload(){
			$("#list").datagrid("reload");
		}
		//添加组套弹出窗口
		function addStackTree() {
			var node = $('#tDt').tree('getSelected');
			Adddilog("添加组套分类","<c:url value='/baseinfo/stack/addTreeStack.action'/>?deptId="+node.id+"&menuAlias="+oo);
			$('#tDt').tree('reload');
		};
		//删除组套信息
		function removeStackTree() {
			var node = $('#tDt').tree('getSelected');
			$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
				if(res){
					$.messager.progress({text:'删除中，请稍后...',modal:true});
					$.ajax({
						url: "<c:url value='/baseinfo/stack/delTreeStack.action'/>?id="+node.id,
						type:'post',
						success: function() {
							$.messager.progress('close');	
							$.messager.alert('警告','删除成功!');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							$('#tDt').tree('reload');
							$("#list").datagrid("reload");
						}
					});	
				}
			});
		}
		//修改组套弹出窗口
		function editStackTree() {
			var node = $('#tDt').tree('getSelected'); 
			AdddilogUpdate("编辑组套分类","<c:url value='/baseinfo/stack/updateTreeStack.action'/>?id="+node.id+"&menuAlias="+oo);
		}
		//查看组套弹出窗口
		function viewStackTree() {
			var node = $('#tDt').tree('getSelected');
			AdddilogUpdate("查看组套分类","<c:url value='/baseinfo/stack/viewTreeStack.action'/>?id="+node.id);
		}
			
		//组套树操作
		function refresh() {//刷新树
			$('#tDt').tree('options').url = "<c:url value='/baseinfo/stack/stackDeptTree.action'/>" ;
			$('#tDt').tree('reload');
		}
		function expandAll() {//展开树
			$('#tDt').tree('expandAll');
		}
		function collapseAll() {//关闭树
			$('#tDt').tree('collapseAll');
		}
		//获得选中id	
		function getId(parameter) {
			var row = $("#list").datagrid("getSelections");
			var i = 0;
			if (parameter == 'single') {//获得单个id
				if (row.length < 1) {
					$.messager.alert('警告',"请选择一条记录！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return null;
				} else if (row.length > 1) {
					$.messager.alert('警告',"只能选择一条记录！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return null;
				} else {
					var id = "";
					for (i; i < row.length; i++) {
						id += row[i].id;
						return id;
					}
				}
			} else if (parameter == 'plurality') {//获得多个id
				if (row.length < 1) {
					$.messager.alert('警告',"请至少选择一条记录！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return null;
				} else {
					var ids = "";
					for (i; i < row.length; i++) {
						ids += row[i].id + ",";
					}
					return ids;
				}
			} else if (parameter == 'notNull') {//至少获得一个id
				var id = "";
				if (row.length < 1) {//如果没有选择数据，默认选中第一行数据
					$('#list').datagrid('selectRow', 0);
					var row = $("#list").datagrid("getSelections");
				}
				id += row[0].id;
				return id;
			} else {
				$.messager.alert('警告',"参数无效！");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return null;
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
		//加载dialog
		function Adddilog(title, url) {
			$('#stack').dialog({    
			    title: title,    
			    width: '40%',    
			    height:'30%',    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			   });    
		}
		//打开dialog
		function openDialog() {
			$('#stack').dialog('open'); 
		}
		//关闭dialog
		function closeDialog() {
			$('#stack').dialog('close');  
		}
		//科室id显示name
		function functiondeptid(value,row,index){
			for(var i=0;i<deptidlist.length;i++){
				if(value==deptidlist[i].id){
					return deptidlist[i].deptName;
				}
			}
		}	
	</script>
</head>
<body style="margin: 0px; padding: 0px">
		<div class="easyui-layout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow-y: auto;">
			<c:if test="${depttreeisOrNot eq '1'}">
				<div id="p" data-options="region:'west',split:true,tools:'#toolSMId'" title="部门科室管理" style="width: 300px; padding: 0px; overflow: hidden;">
					<div id="toolSMId">
						<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
						<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
						<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
					</div>
					<div id ="treeFlag" style="width: 100%; height: 100%; overflow-y: auto; overflow-x:hidden">
						<ul id="tDt">组套加载中...</ul>
					</div>
					<div id="mm" class="easyui-menu" data-options="" style="width: 120px;">
						<div id="removeStackTree" onclick="removeStackTree()" data-options="name:'save',iconCls:'icon-bullet_minus'">
							删除组套
						</div>
						<div id="editStackTree" onclick="editStackTree()" data-options="name:'print',iconCls:'icon-bullet_edit'">
							修改组套
						</div>
						<div id="viewStackTree" onclick="viewStackTree()" data-options="name:'print',iconCls:'icon-bullet_magnify'">
							查看组套
						</div>
					</div>
				</div>
			</c:if>
			<div id="centreDiv" data-options="region:'center',title:'组套信息',border:false">
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow-y: auto;">
					<div id="stackDiv" >
						<input type="hidden" name="deptId" id="deptId"/>
						<table id="list"style="width:100%;" data-options="toolbar:'#toolbarId'"></table>
						<div id="toolbarId">
							<shiro:hasPermission name="ZTGL:function:add">
								<a  onclick="addStack()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="ZTGL:function:edit">
								<a  onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
							</shiro:hasPermission>
							<a  onclick="view()" class="easyui-linkbutton" data-options="iconCls:'icon-see',plain:true">查看</a>
						</div>
					</div>
					<div id="stack"></div>
					<div id="stackUpdate"></div>
				</div>
			</div>
		 </div>
	</body>
</html>