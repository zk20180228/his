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
</head>
	<body>
				<div id="roleWinss" data-options="region:'center',split:false,title:'',iconCls:'icon-book'" style="padding:10px;">
					<table id="userrolelist" class="easyui-treegrid" data-options="idField:'id',treeField:'name',animate:false,rownumbers:true,striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarIds'">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true" ></th>
								<th data-options="field:'id',hidden:true">角色编号</th>
								<th data-options="field:'name'">角色名称</th>
								<th data-options="field:'alias'">角色别名</th>
								<th data-options="field:'description'">角色描述</th>
								<th data-options="field:'parentRoleId',hidden:true">父级</th>
								<th data-options="field:'menufunction'">层级路径</th>
								<th data-options="field:'order'">排序</th>
								<th data-options="field:'uppath',hidden:true">所有父级</th>
							</tr>
						</thead>
					</table>
				</div>		
	<div id="toolbarIds">
	<shiro:hasPermission name="${menuAlias}:function:add">
		<a href="javascript:void(0)" onclick="addRole()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">保存</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		<a href="javascript:void(0)" onclick="openTree()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">展开</a>
		<a href="javascript:void(0)" onclick="closedTree()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true">折叠</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="closeLayout()">关闭</a>
	</div>
	<input id="paramUserIds" type="hidden" value="${userId}"/>
	<script type="text/javascript">
		//初始化页面
			$('#userrolelist').treegrid({
				url:"<c:url value='/sys/queryRole.action'/>",
				onDblClickRow: function (row) {//加载之前获得数据权限类型
					if(getIdUtil('#userrolelist').length!=0){
						
				   	    AddOrShowEasts('EditForm',"<c:url value='/sys/viewRoles.action'/>?id="+getIdUtil('#userrolelist'));
				   	}
				}
			});
			
		
		//添加
	   	function addRole(){
	   	 var rows = $('#userrolelist').treegrid('getChecked');
	             	if (rows.length > 0) {//选中几行的话触发事件	    
				 		$.messager.confirm('确认', '确定要添加选中角色吗?', function(res){
							if (res){
								var ids = '';
								for(var i=0; i<rows.length; i++){
									if(ids!=''){
										ids += ',';
									}
									ids += rows[i].id;
								};
								$.messager.progress({text:'保存中，请稍后...',modal:true});
							   		$.post("<c:url value='/sys/saveRoleUsers.action'/>",{'Roleid':ids,'Userid':$('#paramUserIds').val()},function(result){
							   			$.messager.progress('close');
							   			if (result="success") {
							   					$.messager.alert('提示','操作成功');
												$('#RolelistdivLayout').layout('remove','east');
												$('#Rolelist').datagrid('reload');
											}else{
												$.messager.alert('提示','操作失败');
											}
										});
							}
			                    });
			                 }else{
			                	 $.messager.alert('提示','请选择信息！');
			                	 close_alert();
			                 }
			   	}
		
		
		
		//刷新
		function reload(){
			$('#userrolelist').treegrid('reload');
		}
		
		//展开
		function openTree(){
			$('#userrolelist').treegrid('expandAll');
		}
		
		//折叠
		function closedTree(){
			$('#userrolelist').treegrid('collapseAll');
		}
		//关闭
		function closeLayout(){
			$('#RolelistdivLayout').layout('remove','east');
		}
	</script>
	</body>
</html>