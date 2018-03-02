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
<style type="text/css">
.layout-split-east,.panel-body-noheader {
    border-left: 0px;
}

</style>
</head>
	<body>
	<div class="easyui-layout" data-options="fit:true" style="width:100%;height:100%;overflow-y: auto;">
		<div data-options="region:'center'" style="border-top:0">
			<div id="divLayout" class="easyui-layout" data-options="fit:true">
				 <div data-options="region:'north',split:false,border:false" style="width: 100%; height: 35px;">	 
					<table style="width:100%;border:0px;padding:2px">
						<tr>
							<td style="width:190px;"><input id="sName" name="menu.name" class="easyui-textbox" data-options="prompt:'角色名,别名,拼音,五笔,自定义码'" style="width:220px;"/></td>
							<td>
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchFrom()" data-options="iconCls:'icon-search'" class="easyui-linkbutton" >查询</a>
								<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</div>	
				<div data-options="region:'center',split:false,border:false" style="width:100%;height:95%;">
					<table id="list" style="width:100%;"class="easyui-treegrid" data-options="idField:'id',treeField:'name',animate:false,rownumbers:true,striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true" ></th>
								<th data-options="field:'id',hidden:true">角色编号</th>
								<th data-options="field:'name',width:'250px'">角色名称</th>
								<th data-options="field:'alias',width:'150px'">角色别名</th>
								<th data-options="field:'description',width:'200px'">角色描述</th>
								<th data-options="field:'_parentId',hidden:true">父级</th>
								<th data-options="field:'menufunction',hidden:true">层级路径</th>
								<th data-options="field:'order',hidden:true">排序</th>
								<th data-options="field:'icon',hidden:true">图标</th>
								<th data-options="field:'uppath',hidden:true">所有父级</th>
								<th data-options="field:'createTime',hidden:true">创建时间</th>
							</tr>
						</thead>
					</table>
				</div>		
			</div>	
		</div>
	</div>
	<div id="roleWin"></div>
	<div id="toolbarId">
	<shiro:hasPermission name="${menuAlias}:function:add">
		<a href="javascript:void(0)" onclick="addR()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:edit">
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:delete">		
		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		<a href="javascript:void(0)" onclick="openTree()" class="easyui-linkbutton" data-options="iconCls:'icon-open',plain:true">展开</a>
		<a href="javascript:void(0)" onclick="closedTree()" class="easyui-linkbutton" data-options="iconCls:'icon-fold',plain:true">折叠</a>
	<shiro:hasPermission name="${menuAlias}:function:authorView">
		<a href="javascript:void(0)" onclick="addMenu()" class="easyui-linkbutton" data-options="iconCls:'icon-folder_explore',plain:true">授权管理</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:anduser">
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511913',plain:true" onclick="queryLayout()">关联用户</a>
	</shiro:hasPermission>
	</div>
	<script type="text/javascript">
	//初始化页面
	$(function(){
		var isData = false
		var winH=$("body").height();
		$('#list').treegrid({
			url: "<c:url value='/sys/queryRoleByPage.action?menuAlias=${menuAlias}'/>",
			height:winH-170,
			pagination: true,
			pageSize: 20,
			pageList: [20,30,50,80,100],

			onLoadSuccess: function(row, data){
				if(data.rows.length == 0){
					add() 
				}
				
					
				//分页工具栏作用提示
				if(isData){
					isData = false
				}
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
			},
			onBeforeLoad: function(row,param){
				$('#list').treegrid('uncheckAll');
				if (!row) {	// 加载顶级节点
					param.id = 0;	// id=0表示去加载新的一页
				}
			},
			onDblClickRow: function (row) {//加载之前获得数据权限类型
				if(getIdUtil('#list').length!=0){
			   	    AddOrShowEast('EditForm',"<c:url value='/sys/viewRole.action'/>?id="+getIdUtil('#list'),'35%');
			   	}
			},
			onClickRow: function(row){
				var eastpanel=$('#panelEast'); //获取右侧收缩面板
				if(eastpanel.length>0){ //判断右侧收缩面板是否存在
					$('#divLayout').layout('remove','east');
				}
			}
			
		});
		bindEnterEvent('sName',searchFrom,'easyui');//回车键查询	
	});
	
	//添加
   	function addR(){
   		var row = $('#list').treegrid('getSelected'); //获取当前选中行       
   		var id = "";
   		if(row==null){
   			id = "";
   		}else{
   			id = row.id;
   		}
   		AddOrShowEast('EditForm',"<c:url value='/sys/addRole.action'/>?id="+id,'35%');
   	}
	
	//修改
   	function edit(){
   		var row = $('#list').treegrid('getSelected'); //获取当前选中行
   		if(row==null||row==""){
   			$.messager.alert('提示','请选择具体角色');
   			close_alert();
   			return;
   		}     
        if(row){
        	AddOrShowEast('EditForm',"<c:url value='/sys/editRole.action'/>?id="+row.id,'35%');
		}
   	}
	
	//删除
   	function del(){
   		var rows = $('#list').treegrid('getChecked');
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
						url: "<c:url value='/sys/delRole.action'/>?id="+ids,
						type:'post',
						success: function(dataMap) {
							$.messager.alert('提示',dataMap.resCode);
							if(dataMap.resMsg=="success"){
								$('#list').treegrid('reload');
							}
						}
					});
				}
        	});
    	}else{
    		$.messager.alert('提示',"请选择要删除的信息！");
    		close_alert();
    	}
   	}
	
	//刷新
	function reload(){
		$('#list').treegrid('reload');
	}
	
	//展开
	function openTree(){
		$('#list').treegrid('expandAll');
	}
	
	//折叠
	function closedTree(){
		$('#list').treegrid('collapseAll');
	}

	//查看用户
   function queryLayout(){
        if(getIdUtil("#list") != null){
        	$('#roleaddUserdiv').dialog('destroy');
        	AddOrShowEast('EditForm',"<c:url value='/sys/editRoleUser.action'/>?roleId="+getIdUtil("#list"),'35%');
		}

   }
	
 //动态添加LayOut
	function AddOrShowEast(title, url,width) {
		var eastpanel=$('#panelEast'); //获取右侧收缩面板
		if(eastpanel.length>0){ //判断右侧收缩面板是否存在
			$('#divLayout').layout('remove','east');
			$('#divLayout').layout('add', {
				region : 'east',
				width : width,
				split : true,
				maxHeight:755,
				href : url,
				closable : true
			});
		}else{//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : width,
				split : true,
				maxHeight:755,
				href : url,
				closable : true
			});
		}
	}
	
	//动态添加LayOut
	function AddOrShowEastDept(title, url) {
		var eastpanel=$('#panelEast'); //获取右侧收缩面板
		if(eastpanel.length>0){ //判断右侧收缩面板是否存在
			//重新装载右侧面板
	   		$('#divLayout').layout('panel','east').panel({
                   href:url
            });
		}else{//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : 580,
				split : true,
				href : url,
				closable : true
			});
		}
	}
			
   	//弹框 授权
   	function addMenu(){
   		var row = $('#list').treegrid('getSelected'); //获取当前选中行
   		if(row==null||row==""){
   			$.messager.alert('提示','请选择具体角色');
   			close_alert();
   			return;
   		}         
   		if(row.id!=""&&row.id!=null){
   			AddOrShowEast('EditForm',"<c:url value='/sys/authorizeRoleView.action'/>?id="+row.id,'50%');
   		}
   	}
   	//加载模式窗口
	function Adddilog(title, url, width, height) {
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
    
	//打开模式窗口
	function openDialog() {
		$('#roleWin').dialog('open'); 
	}
	
	//关闭模式窗口
	function closeDialog() {
		$('#roleWin').dialog('destroy');  
	}
	
	//查询
	function searchFrom(){
	    var name =$('#sName').textbox('getText');
	    $('#list').treegrid("load",{name:name})
	}
	function add (){ //页脚0的BUG修复方法
		$(".datagrid-pager.datagrid-pager .pagination-info").html("显示0到0,共0记录")
		$(".datagrid-pager.datagrid-pager table tr td a").addClass("l-btn-disabled l-btn-plain-disabled").off("click")
		var td =	$(".datagrid-pager.datagrid-pager table tr td") 
		td.eq(6).children("input").val("0")
		td.eq(7).children("span").html("共0页")
	}

	
	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function clears(){
		$('#sName').textbox('setValue','');
		searchFrom();
	}
	</script>
	</body>
</html>