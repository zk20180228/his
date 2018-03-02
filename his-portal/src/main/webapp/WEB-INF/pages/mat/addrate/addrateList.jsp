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
	.panel-body{
		border-top:0;
	}
	.panel-header{
		border-left-width:1px;
	}
</style>
</head>
	<body>
		<div id="divLayout" class="easyui-layout" fit=true>
	        <div style="padding:5px 5px 0px 5px;">	        
					<table style="width:100%;border:1px solid #95b8e7;padding:5px;" class="changeskin">
						<tr style="width:600px;">
							<td>
								<input class="easyui-textbox " id="sName" data-options="prompt:'输入加价规则,规格,物资分类查询'" style="width:225px"/> 
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</td>
						</tr>
					</table>
			</div>
	           <div style="padding-top: 5px;">
				<table id="list" data-options="url:'${pageContext.request.contextPath}/material/addrate/queryAddrate.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true"></th>
							<th data-options="field:'addRate',width:100">加价规则</th>
							<th data-options="field:'kindCode',width:150,formatter:funKindCode">物品科目</th>
							<th data-options="field:'specs',width:100">规格</th>
							<th data-options="field:'lowPrice',width:100">起始价格</th>
							<th data-options="field:'highPrice',width:100">终止价格</th>
							<th data-options="field:'rate',width:100">加价率</th>
							<th data-options="field:'addFee',width:100">附加费</th>
							<th data-options="field:'operCode',width:100,formatter:funOperCode">操作员</th>
							<th data-options="field:'operDate',width:200">操作日期</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<div id="toolbarId">
			<shiro:hasPermission name="${menuAlias}:function:add">
				<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:edit">
				<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:delete">	
				<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
				<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		<script type="text/javascript">
			//加载
			var kindMap = null;
			var userMap = null;
			$(function(){
				//获得物品科目
				$.ajax({
					url: "<c:url value='/material/addrate/findMatKindInfoMap.action'/>",
					type:'post',
					success: function(date) {
						kindMap = eval("("+date+")");
					}
				});
				//获得操作员
				$.ajax({
					url: "<c:url value='/material/addrate/findUserMap.action'/>",
					type:'post',
					success: function(date) {
						userMap = eval("("+date+")");
					}
				});
				var winH=$("body").height();
				
				$('#list').height(winH-54);
				setTimeout(function(){
					$('#list').datagrid({
						pagination:true,
						pageSize:20,
						pageList:[20,30,50,80,100],
						onDblClickRow: function (rowIndex, rowData) {//查看
							 AddOrShowEast('查看',"<c:url value='/material/addrate/viewAddrate.action'/>?id="+rowData.id);
						},
						onLoadSuccess : function(data){
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
				},100)
				bindEnterEvent('sName',searchFrom,'easyui');//回车
			});
			//刷新
			function reload(){
				 $('#list').datagrid('reload');
			}
		
			//查询
			function searchFrom() {
				var sName = $('#sName').textbox('getText');
				$('#list').datagrid('load', {
					kindCode : sName
				});
			}
			//删除
			function del(){
				var rows = $('#list').datagrid('getChecked');
				if(rows!=null&&rows.length>0){
					$.messager.confirm('提示信息','是否删除所选信息？', function(res){//提示是否删除
						if (res){
							var ids = '';
							for(var i=0;i<rows.length;i++){
								if(ids!=''){
									ids+=',';
								}
								ids += rows[i].id;
							}
			 				$.ajax({
			 					url: "<c:url value='/material/addrate/delAddrate.action'/>?id="+ids,
			 					type:'post',
			 					success: function(data) {
			 						var dataMap = eval("("+data+")");
			 						reload();
			 						$.messager.alert('提示',dataMap.resCode);
			 					}
			 				});
						}
			    	});
				}else{
					$.messager.alert('提示',"请选择要删除的信息!");	
					return;	
				}
			}
			//添加
			function add(){
				AddOrShowEast('添加',"<c:url value='/material/addrate/addAddrate.action'/>");
			}
			//修改
			function edit(){
				var row = $('#list').datagrid('getSelected');
				if(row!=null&&row!=''){
					AddOrShowEast('修改',"<c:url value='/material/addrate/editAddrate.action'/>?id="+row.id);
				}else{
					$.messager.alert('提示',"请选择要修改的信息!");	
					return;
				}
			}
			//判断是否添加LayOut
			function AddOrShowEast(title, url) {
				var eastpanel=$('#panelEast'); //获取右侧收缩面板
				if(eastpanel.length>0){ //判断右侧收缩面板是否存在
					$('#divLayout').layout('remove','east');
					addPanel(title, url);
				}else{//打开新面板
					addPanel(title, url);
				}
			}
			//添加LayOut
			function addPanel(title, url){
				$('#divLayout').layout('add', {
					title:title,
					region:'east',
					width:580,
					split:true,
					href:url,
					closable:false,
					minimizable:false,
					maximizable:false,
					collapsible : false,
					border:false
				});
			}
			//物品科目
			function funKindCode(value,row,index){
				if(kindMap!=null){
					return kindMap[value];
				}
			}
			//操作员
			function funOperCode(value,row,index){
				if(userMap!=null){
					return userMap[value];
				}
			}
		</script>
	</body>
</html>