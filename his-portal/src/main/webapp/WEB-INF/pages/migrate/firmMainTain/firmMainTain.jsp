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
					<div  data-options="region:'north',split:false" style="width: 100%; height: 40px;border-top:0;padding-left: 5px;">
						<form id="searchForm" method="post" style="padding-top:7px;padding-left:5px;padding-right:5px;">
							<table border="0">
								<tr>
									<td style="width:250px;">查询条件:<input id="name" name="menu.name" class="easyui-textbox" data-options="" style="width:150px;"/></td>
									<td>
										<shiro:hasPermission name="${menuAlias}:function:query">
											<a href="javascript:void(0)" onclick="searchForm()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
											<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
										</shiro:hasPermission>
									</td>
								</tr>
							</table>
						</form>
				</div>
				<div data-options="region:'center',split:false" style="width: 100%; height: 30px; border-top:0">
					<table id="list" style="width:100%;"class="easyui-datagrid" data-options="idField:'id',treeField:'name',striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true,height:40"></th>
								<th data-options="field:'id',hidden:true">id</th>
<!-- 								<th data-options="field:'firmCode',width:'5%'">厂商编码</th> -->
								<th data-options="field:'firmCode',width:'10%'">厂商编码/账户</th>

								<th data-options="field:'firmName',width:'300'">厂商名称</th>
								<th data-options="field:'passWord',width:'7%'">厂商接口密码</th>
<!-- 								<th data-options="field:'createTime',width:'10%'">创建时间</th> -->
								<th data-options="field:'firmUser',width:'10%'">厂商数据库账户</th>
								<th data-options="field:'firmPassword',width:'10%'">厂商数据库密码</th>
								<th data-options="field:'firmView',width:'50%'">厂商视图</th>

<!-- 								<th data-options="field:'updateTime',width:'10%'">修改时间</th> -->

							</tr>
						</thead>
					</table>
				</div>
		</div>
	<div id="toolbarId">
	<shiro:hasPermission name="${menuAlias }:function:add">
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias }:function:edit">
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias }:function:delete">		
		<a href="javascript:void(0)" onclick="delRwo()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	<shiro:hasPermission name="${menuAlias }:function:edit">		
		<a href="javascript:void(0)" onclick="updatePas()" class="easyui-linkbutton" data-options="iconCls:'icon-resetPassward',plain:true">修改密码</a>
	</shiro:hasPermission>
	</div>
	
	
	<div id="menuWin">
		
	
	</div>
</body>
	<script type="text/javascript">
	var menuAlias="${menuAlias}";
	$(function(){
		searchForm();
		bindEnterEvent('name',searchForm,'easyui');//绑定回车事件
	});
	//修改密码
	function updatePas(){
		var row = $('#list').datagrid('getSelected');
		if(row){
			Adddilog('修改密码','<%=basePath%>migrate/firmMainTain/resPassword.action?userId='+row.id+'&menuAlias='+menuAlias,'500','200');
		}else{
			$.messager.alert('提示信息','请选中要修改密码的厂商!');
			setTimeout(function(){$(".messager-body").window('close')},3500);
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
	//条件查询
	function searchForm(){
		$('#list').datagrid({
			rownumbers: true,
			pageSize: "20",
			fit:true,
			singleSelect:true,
			checkOnSelect:false,
			selectOnCheck:false,
			pageList: [10, 20, 30, 50, 80, 100],
			pagination: true,
			method: "post",
			url: '<%=basePath%>migrate/firmMainTain/queryFirmMainTain.action',
			queryParams:{code:$('#name').textbox('getValue'),menuAlias:menuAlias},
			onLoadSuccess:function(data){    
	        	 $("a[name='opera']").linkbutton({});
	        	 $('#list').datagrid('unselectAll');
	           	 $('#list').datagrid('uncheckAll');
			 },
			 
			onDblClickRow: function (index,row) {//加载之前获得数据权限类型
				if(getIdUtil('#list').length!=0){
					var data=$('#list').datagrid('getSelected');
			   	    AddOrShowEast("查看",'<%=basePath %>migrate/firmMainTain/viewFirmMainTain.action?code='+data.id,'30%');
			   	}
			},
		});
	}
	
	 //重置
	function clears(){
		$('#name').textbox('setValue','');
		searchForm();
	}
	 
	//添加
	function add(){
		AddOrShowEast('EditForm',"<%=basePath %>migrate/firmMainTain/addFirmMainTain.action","30%");
	}
	
	//修改
	function edit(){
		var data=$('#list').datagrid('getSelected');
		if(data==""||data==null){
			$.messager.alert("操作提示", "请选择要修改的数据");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{
			AddOrShowEast('EditForm','<%=basePath %>migrate/firmMainTain/addFirmMainTain.action?code='+data.id,'30%');
		}
	}
	
	//删除
	function delRwo(){
		var row=$("#list").datagrid('getSelected');
		if(row!=null){
			$.messager.confirm("提示","确认删除"+row.firmName+"吗？",function(r){
				if(r){
					$.messager.progress({text:'处理中，请稍后...',modal:true});
					$.ajax({
						url: '<%=basePath %>migrate/firmMainTain/delFirmMainTain.action',
						data : {code:row.id},
						success : function(result){
							$.messager.progress('close');
							$.messager.alert('提示',result.resMsg);
							if('success'==result.resCode){
								searchForm();
							}
						}
						,error : function(){
							$.messager.progress('close');
							$.messager.alert('提示','网络繁忙,请稍后重试...','info');
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','请选择删除的数据','info');
		}
	}
	
	//刷新
	function reload(){
		$('#list').datagrid('reload');
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
    
	//打开模式窗口
	function openDialog() {
		$('#menuWin').dialog('open'); 
	}
	
	//关闭模式窗口
	function closeDialog() {
		$('#menuWin').dialog('close'); 
		searchForm();
	}
	
	function formatterPass(value,row,index){
		return "***";
	}
	</script>
	
</html>