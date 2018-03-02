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
	<div class="easyui-panel" id="panelEast" data-options="title:'角色授权查看',iconCls:'icon-form',border:false,fit:true">
		<div data-options="region:'center',split:false,title:'角色列表',iconCls:'icon-book'" style="width:100%;height: 100%;border-top:0">
			<input type="hidden" id="roleIdView" value="${role.id }">
			<table id="roleAuthListView" class="easyui-treegrid" data-options="url:'${pageContext.request.contextPath}/sys/queryMenuTreegrid.action?showAll=false&roleId=${role.id }&menuAlias=${menuAlias}',fitColumns:true,idField:'id',treeField:'name',toolbar:'#toolbarIdRoleAuthView',fit:true,border:false">
				<thead>
					<tr>
						<th data-options="field:'id',hidden:true">主键</th>
						<th data-options="field:'name'">名称</th>
						<th data-options="field:'dataRight',formatter: functionComboView">数据权限</th>
						<th data-options="field:'menuBut',formatter: functionCheckView">功能权限</th>
						<th data-options="field:'iconCls',hidden:true">图标</th>
						<th data-options="field:'_parentId',hidden:true">父节点</th>
						<th data-options="field:'path',hidden:true">层级路径</th>
					</tr>
				</thead>
			</table>
		</div>
		<div id="toolbarIdRoleAuthView">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-close', plain:true"  onclick="closeRoleAuthView()">关闭</a> 
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="reloadRoleAuthView()">刷新</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-open',plain:true" onclick="roleAuthOpenTreeView()">展开</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-fold',plain:true" onclick="roleAuthClosedTreeView()">折叠</a>
			<shiro:hasPermission name="JSGL:function:author">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-group_key',plain:true" onclick="addroleAuthView()">授权</a>
			</shiro:hasPermission>
		</div>
	</div>
	<script type="text/javascript">
		var dataRightListView="";
		//初始化页面
		$(function(){
			$.ajax({
				url: "<c:url value='/sys/queryDataRightCombobox.action'/>",
				type:'post',
				success: function(dataRight) {
					dataRightListView = dataRight;
				}
			});
		});
		
		//初始化数据权限
		function functionComboView(value,row,index){
			var retVal = "";
			if((dataRightListView!=null&&dataRightListView!="")&&(row.haveson=="1")){
				for(var i=0;i<dataRightListView.length;i++){
					if(value!=null&&value!=""){
						if(value==dataRightListView[i].id){
							retVal = ''+dataRightListView[i].name+'';
						}
					}else{
						if(dataRightListView[i].alias=="GR"){
							retVal = ''+dataRightListView[i].name+'';
						}
					}
				}
			}
			return '&nbsp;&nbsp;'+retVal+'&nbsp;&nbsp;';
		}
		
		//初始化功能权限
		function functionCheckView(value,row,index){
			var retVal = ""; 
			var buts = row.but;
			if((buts!=null&&buts!="")&&(row.haveson=="1")){
				for(var i=0;i<buts.length;i++){
					if(value!=null&&value!=""){
						var id = value.split(",");
						var checked = false;
						for(var j=0;j<id.length;j++){
							if(buts[i].id==id[j]){
								checked = true;
							}
						}
						if(checked){
							retVal += '<input type="checkbox" checked="checked" disabled="disabled"><span>'+buts[i].name+'</span>&nbsp;&nbsp;';
						}
					}
				}
			}
			return '&nbsp;&nbsp;'+retVal+'&nbsp;&nbsp;';
		}	
		
		//关闭
		function closeRoleAuthView(){
			$('#divLayout').layout('remove','east');
		}	
		
		//刷新
		function reloadRoleAuthView(){
			$('#roleAuthListView').treegrid('reload');
		}
		
		//展开
		function roleAuthOpenTreeView(){
			$('#roleAuthListView').treegrid('expandAll');
		}
		
		//折叠
		function roleAuthClosedTreeView(){
			$('#roleAuthListView').treegrid('collapseAll');
		}
		
		//授权
		function addroleAuthView(){
			AdddilogView("${role.name }授权","<c:url value='/sys/authorizeRole.action'/>?id="+$('#roleIdView').val(),'80%','80%');
		}
		
		//加载模式窗口
		function AdddilogView(title, url, width, height) {
			$('#roleWin').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,
			    resizable:true,
			    href: url,    
			    modal: true   
			   });    
		}
	</script>
	</body>
</html>
