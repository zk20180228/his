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
    <link href="${pageContext.request.contextPath}/initStyle/imggrid.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/imggrid.js"></script>
<style type="text/css">
.layout-split-east {
    border-left: 0px;
}
.panel-body-noheader{
	border-left: 0px;
}
.layout-split-east .panel-header{
	border-top:0;
	border-left:0;
}
.panel-noscroll{
	border-right:0;
}
</style>
</head>
<body>
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;" data-options="fit:true">
					<div  data-options="region:'north',split:false" style="width: 100%; height: 35px;border-top:0">
						<form id="search" method="post" style="padding: 1px">
							<table style="width:100%;border:false;">
								<tr>
									<td style="width:190px;"><input id="sName" name="menu.name" class="easyui-textbox" data-options="prompt:'栏目名,别名,拼音,五笔,自定义码'" style="width:220px;"/></td>
									<td>
										<shiro:hasPermission name="${menuAlias}:function:query">
											<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
											<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
										</shiro:hasPermission>
									</td>
								</tr>
							</table>
						</form>
				</div>
				<div data-options="region:'center',split:false" style="width: 100%; height: 30px; border-top:0">
					<table id="list" style="width:100%;"class="easyui-treegrid" data-options="idField:'id',treeField:'name',animate:false,striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true,height:40"></th>
								<th data-options="field:'id',hidden:true">主键</th>
								<th data-options="field:'name',width:'15%'">名称</th>
								<th data-options="field:'alias',width:'10%'">别名</th>
								<th data-options="field:'type',width:'10%'">类型</th>
								<th data-options="field:'parameter',width:'10%'">参数</th>
								<th data-options="field:'icon',hidden:true">图标</th>
								<th data-options="field:'description',width:'10%'">说明</th>
								<th data-options="field:'order',hidden:true">排序</th>
								<th data-options="field:'levelOrder',hidden:true">排序号</th>
								<th data-options="field:'move',width:180">操作</th>
								<th data-options="field:'createTime',hidden:true">创建时间</th>
							</tr>
						</thead>
					</table>
				</div>
		</div>
	<div id="menuWin"></div>
	<div id="toolbarId">
	<shiro:hasPermission name="${menuAlias }:function:add">
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias }:function:edit">
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias }:function:delete">		
		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		<a href="javascript:void(0)" onclick="openTree()" class="easyui-linkbutton" data-options="iconCls:'icon-open',plain:true">展开</a>
		<a href="javascript:void(0)" onclick="closedTree()" class="easyui-linkbutton" data-options="iconCls:'icon-fold',plain:true">折叠</a>
	<shiro:hasPermission name="${menuAlias }:function:authorView">
		<a href="javascript:void(0)" id="addRoleButId" onclick="addRole()" class="easyui-linkbutton" data-options="iconCls:'icon-folder_explore',plain:true">授权管理</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" id="funJurisToMobile" onclick="funJurisToMobile()" class="easyui-linkbutton" data-options="iconCls:'icon-change',plain:true">同步至移动端</a>
	</div>
	<script type="text/javascript">
	var parentId="${parentId}";
	//初始化页面
	var indexorder="";
	$(function(){
		$('#addRoleButId').linkbutton('disable');//禁用授权按钮
		var winH=$("body").height();
		$('#list').treegrid({
			url: "<c:url value='/sys/searchMenusByParams.action?menuAlias=${menuAlias}'/>",
			height:winH-170,
			rownumbers: true,
			pagination: true,
			pageSize: 10,
			pageList: [10,20,30,50,80,100],
			onBeforeLoad: function(row,param){
				$('#list').treegrid('uncheckAll');
				if (!row) {	// 加载顶级节点
					param.id = 0;	// id=0表示去加载新的一页
				}
			},
			onLoadSuccess:function(row, data){
				//分页工具栏作用提示
				if(data.rows.length == 0){
					findNone();
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
				var rows = data.rows; 					//全部数据
				var options = $('#list').treegrid('getPager').data("pagination").options;	//获得options对象
				var pageNumber = options.pageNumber;	//当前页数
				var total = options.total; 				//总计条数
				var pageSize = options.pageSize;		//分多少页
				for(var i=0;i<rows.length;i++){//循环遍历每条数据
					var bnt = "";
					if(rows[i]._parentId==null||rows[i]._parentId==''){//当父节点为空时表示此记录为父节点
						if(rows.length==1){//仅有一个父级不现实按钮
						}else if(pageNumber==1&&i==0){//第一行
							bnt='<a class="downCls" onclick="toDown(\''+rows[i].id+'\',\''+rows[i].levelOrder+'\',\''+rows[i]._parentId+'\')" href="javascript:void(0)" style="height:20"></a>';
						}else if((pageSize*(pageNumber-1)+(i+1))==total){//最后一行
							bnt='<a class="upCls" onclick="toUp(\''+rows[i].id+'\',\''+rows[i].levelOrder+'\',\''+rows[i]._parentId+'\')" href="javascript:void(0)" style="height:20"></a>';
						}else{
							bnt='<a class="upCls" onclick="toUp(\''+rows[i].id+'\',\''+rows[i].levelOrder+'\',\''+rows[i]._parentId+'\')" href="javascript:void(0)" style="height:20"></a>';
							bnt+='<a class="downCls" onclick="toDown(\''+rows[i].id+'\',\''+rows[i].levelOrder+'\',\''+rows[i]._parentId+'\')" href="javascript:void(0)" style="height:20"></a>';
						}
					}else{//当父节点不为空时表示此记录子节点
						if(rows.length==1){//仅有一个子级不现实按钮
						}else if(i==0){//第一行
							bnt='<a class="downCls" onclick="toDown(\''+rows[i].id+'\',\''+rows[i].levelOrder+'\',\''+rows[i]._parentId+'\')" href="javascript:void(0)" style="height:20"></a>';
						}else if(i+1==rows.length){//最后一行
							bnt='<a class="upCls" onclick="toUp(\''+rows[i].id+'\',\''+rows[i].levelOrder+'\',\''+rows[i]._parentId+'\')" href="javascript:void(0)" style="height:20"></a>';
						}else{
							bnt='<a class="upCls" onclick="toUp(\''+rows[i].id+'\',\''+rows[i].levelOrder+'\',\''+rows[i]._parentId+'\')" href="javascript:void(0)" style="height:20"></a>';
							bnt+='<a class="downCls" onclick="toDown(\''+rows[i].id+'\',\''+rows[i].levelOrder+'\',\''+rows[i]._parentId+'\')" href="javascript:void(0)" style="height:20"></a>';
						}
					}
					$('#list').treegrid('update',{
							id: rows[i].id,
							row: {
								move : bnt
							}
					});
				}
				$('.upCls').linkbutton({text:'上移',plain:true,iconCls:'icon-up'}); 
				$('.downCls').linkbutton({text:'下移',plain:true,iconCls:'icon-down'}); 
			},
			onDblClickRow: function (row) {//加载之前获得数据权限类型
				if(getIdUtil('#list').length!=0){
			   	    AddOrShowEast("查看","<c:url value='/sys/viewMenu.action'/>?id="+getIdUtil('#list'),'35%');
			   	}
			},
			onClickRow: function(row){
				var eastpanel=$('#panelEast'); //获取右侧收缩面板
				if(eastpanel.length>0){ //判断右侧收缩面板是否存在
					$('#divLayout').layout('remove','east');
				}
				if(row.haveson==0){//判断该栏目是否可以授权
					$('#addRoleButId').linkbutton('disable');
				}else{
					$('#addRoleButId').linkbutton('enable');
				}
			}
		});
		bindEnterEvent('sName',searchFrom,'easyui');//回车键查询	
	});
	function funJurisToMobile(){
		$.messager.confirm('提示信息', '是否向移动端用户推送栏目变动？', function(res){//提示是否删除
				if (res){
					$.ajax({
						url:'<%=basePath %>sys/userMenuFunJuris/jurisToMobile.action',
						success: function(data) {
							if(data.resCode == 'success'){
								$.messager.alert("提示",'推送成功！');
							}else{
								$.messager.alert("提示",'推送失败！');
							}
						},error:function(){
							$.messager.alert("提示",'推送失败！');
						}
					});
				}
			});
	}
	function findNone(){ //页脚0的BUG修复方法
		$(".datagrid-pager.datagrid-pager .pagination-info").html("显示0到0,共0记录")
		$(".datagrid-pager.datagrid-pager table tr td a").addClass("l-btn-disabled l-btn-plain-disabled").off("click")
		var td =	$(".datagrid-pager.datagrid-pager table tr td") 
		td.eq(6).children("input").val("0")
		td.eq(7).children("span").html("共0页")
	}
	//下移
	function toDown(id,order,parentId){
		editOrder(id,"下",parentId);
	}
	
	//上移
	function toUp(id,order,parentId){
		editOrder(id,"上",parentId);
	}
	
	//查询
	function searchFrom(){
	  	var name = $('#sName').textbox('getValue');
	  	$('#list').treegrid('load',{name:name});
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
	//功能名称
	function funMenufun(value,row,index){
		var retVal = "";
		if (row.menufunction){
			return row.menufunction.mfName;
		} else {
			return value;
		}
		return retVal;
	}
	
	
   	function editOrder(id,move,order,parentId){//发请求到后台 把 值传到后台
		$.post("<c:url value='/sys/MoveOrders.action'/>",{"id":id,"move":move},function(result){
			if(result>0){
				reload();
			}else{
				$.messager.alert("提示","无法移动");
				close_alert();
			}
		});
	}
	//添加
   	function add(){
   		var row = $('#list').treegrid('getSelected'); //获取当前选中行       
   		var id = "";
   		var name="";
   		if(row==null){
   			id = "";
   			name="";
   		}else{
   			id = row.id;
   			name=row.name;
   		}
   		
   		AddOrShowEast('EditForm',"<c:url value='/sys/addMenu.action'/>",'40%','post'
				   ,{"id":id,"name":name});
   	}
	
	//修改
   	function edit(){
   		var row = $('#list').treegrid('getSelected'); //获取当前选中行          
   		if(row==null){
   			$.messager.alert("提示","请选择要修改的记录!");
   			close_alert();
   			return;
   		}
   		
   		AddOrShowEast('EditForm',"<c:url value='/sys/editMenu.action'/>",'40%','post'
				   ,{"id":row.id,"name":row.name});
   	}
	
	//删除
   	function del(){
   		var rows = $('#list').treegrid('getChecked');
    	if (rows.length > 0) {//选中几行的话触发事件	  
    		var datasArr = new Array();
    		for(var i=0;i<rows.length;i++){
    			if(rows[i].haveson==0){
    				datasArr[datasArr.length] = rows[i].name;
    			}
    		}
    		var msg = '您确定要删除选中信息吗？';
    		if(datasArr.length>0){
    			var menuName = '您选中的信息中存在以下父级节点信息<br>（删除父级节点会同时删除子级节点）：<br>';
    			for(var i=0;i<datasArr.length;i++){
    				menuName += datasArr[i]+"，";
    			}
    			msg = menuName+'<br>您确认要删除选中信息吗？';
    		}
    		$.messager.defaults={
    				ok:'确定',
    				cancel:'取消',
    				width:350,
    				collapsible:false,
    				minimizable:false,
    				maximizable:false,
    				closable:false
    		};
			$.messager.confirm('提示信息', msg, function(res){//提示是否删除
					if (res){
						var ids = '';
						for(var i=0; i<rows.length; i++){
							if(ids!=''){
								ids += ',';
							}
							ids += rows[i].id;
						};
						$.messager.progress({text:'保存中，请稍后...' });
		 				$.ajax({
		 					url: "<c:url value='/sys/delMenu.action'/>?id="+ids,
		 					type:'post',
		 					dataType:'JSON',
		 					success: function(dataMap) {
		 						if(dataMap.resCode == 'success'){
		 							$('#list').treegrid('reload');
			 						$.messager.alert("提示",dataMap.resMsg);
		 							$.messager.progress('close');
		 						}
		 					},error:function(){
		 						$.messager.alert("提示",'请求失败！');
		 						$.messager.progress('close');
		 					}
		 				});
					}
		    });
    	}else{
    		$.messager.alert("提示","请选择要删除的信息！");
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
	
	//授权
	function addRole(){
		var row = $('#list').treegrid('getSelected'); //获取当前选中行
   		if(row==null||row==""){
   			$.messager.alert("提示","请选择具体栏目");
   			close_alert();
   			return;
   		}     
   		if(row.id!=""&&row.id!=null){
   			AddOrShowEast('EditForm',"<c:url value='/sys/viewRoleMenuRelation.action'/>?menuAlias=${menuAlias}&id="+row.id,'50%');
   		}
	}
	
	//动态添加LayOut
	function AddOrShowEast(title, url,width,method,params) {
		if(!method){
			method="get";
		}
		if(!params){
			params={};
		}
		var eastpanel=$('#panelEast'); //获取右侧收缩面板
		if(eastpanel.length>0){ //判断右侧收缩面板是否存在
			$('#divLayout').layout('remove','east');
			$('#divLayout').layout('add', {
				region : 'east',
				width : width,
				split : true,
				href : url,
				method:method,
				queryParams:params,
				closable : true
			});
		}else{//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : width,
				split : true,
				href : url,
				method:method,
				queryParams:params,
				closable : true
			});
		}
	}
			
    //加载模式窗口
	function Adddilog(title, url, width, height) {
		$('#menuWin').dialog({    
		    title: title,    
		    width: width,    
		    height: height,    
		    closed: false,    
		    cache: false,    
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
		$('#roleWin').dialog('close');  
	}
	</script>
	</body>
</html>