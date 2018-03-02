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
	border-left:0;
	border-top:0;
}
.layout-split-east .panel-body{
	border-left:0;
}
.panel-noscroll{
	border-right:0;
}
</style>
</head>
<body>
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;"fit=true>
		 <div data-options="region:'north'" style="height:35px;border-top:0">
				<form id="search" method="post">
					<table style="width:100%;border:false;padding:1px;">
						<tr>
							<td  ><input class="easyui-textbox"ID="mfName" name="menufunction.mfName" data-options="prompt:'请输入查询名称...'"/>
								<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
								
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</form>
		</div>
		<div data-options="region:'center'" style="border-top:0">
			<table id="list"style="width:100%;" data-options="url:'${pageContext.request.contextPath}/sys/queryMenufunction.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'mfName',width:'10%'">名称</th>
						<th data-options="field:'mfClass',width:'10%'">分类</th>
						<th data-options="field:'mfIcon',width:'10%'">图标</th>
						<th data-options="field:'mfAction',width:'20%'">访问页面请求</th>
						<th data-options="field:'mfFile',width:'20%'">页面路径</th>
						<th data-options="field:'mfDescription',width:'10%'">说明</th>
						<th data-options="field:'createTime',hidden:true">创建时间</th>
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
</body>
<script type="text/javascript">
		//定义一个map
		var map;
		//加载页面
			$(function(){
			var winH=$("body").height();
		    $('#list').height(winH-78-30-27-26);//78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
				var id='${id}'; //存储数据ID
				//添加datagrid事件及分页
				$('#list').datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					onLoadSuccess: function(data){
						//分页工具栏作用提示
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
					        	var grid = $('#list');  
								var options = grid.datagrid('getPager').data("pagination").options;  
								var curr = options.pageNumber;  
								var total = options.total; 
								var pageSize = options.pageSize; 
								var rows = data.rows;
								map=null;
								map=new Map();
					            var rowData = data.rows;
					            $.each(rowData, function (index, value) {
					            	map.put(index,value.id);
					            	if(value.id == id){
					            		$('#list').datagrid('checkRow', index);
					            	}
					            });
					        }, 
					        onBeforeLoad:function(param){
								$(this).datagrid('uncheckAll');
							},
			          		onDblClickRow: function (rowIndex, rowData) {//双击查看
								if(getIdUtil('#list').length!=0){
						   	   	  AddOrShowEast('EditForm',"<%=basePath %>sys/viewMenufunction.action?id="+getIdUtil('#list'));
						   		}
							}    
						});
						bindEnterEvent('mfName',searchFrom,'easyui');
					});
				//添加
				function add(){
					AddOrShowEast('EditForm',"<%=basePath %>sys/addMenufunction.action");
				}
				//修改
				function edit(){
					if(getIdUtil("#list") != null){
	                      AddOrShowEast('EditForm',"<%=basePath %>sys/editMenufunction.action?id="+getIdUtil("#list"));
			   		}
				}
				
				function del(){
				 //选中要删除的行
	                    var rows = $('#list').datagrid('getChecked');
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
									$.messager.progress({text:'保存中，请稍后...',modal:true});
									$.ajax({
										url: '<%=basePath %>sys/delMenufunction.action?ids='+ids,
										type:'post',
										success: function(dataMap) {
											$.messager.progress('close');
											$.messager.alert('提示',dataMap.resMsg);
											if(dataMap.resCode=="success"){
												$('#list').datagrid('reload');
											}
										}
									});
								}
                        });
                    }else{
                    	$.messager.alert('提示','请勾选要删除的记录！');
				    	close_alert();
                    }
				}
				//刷新
				function reload(){
					//实现刷新栏目中的数据
					$('#list').datagrid('reload');
				}
				
				
				
				//获得选中id	
				function getId(parameter){
					var row = $("#list").datagrid("getSelections");  
					var i = 0;   
					if(parameter=='single'){//获得单个id
					    if(row.length<1){
					    	$.messager.alert('提示','请选择一条记录！');
					    	close_alert();
					       	return null;
					    }else if(row.length>1){
					    	$.messager.alert('提示','只能选择一条记录！');
					    	close_alert();
					    	return null;
					    }else{ 
					    	var id = ""; 
						  	for(i;i<row.length;i++){    
						  		id += row[i].id; 
						      	return id;
							}
					  	} 	
					}else if(parameter=='plurality'){//获得多个id
					    if(row.length<1){
					    	$.messager.alert('提示','请至少选择一条记录！');
					    	close_alert();
					       	return null;
					    }else{  
					    	var ids = ""; 
						  	for(i;i<row.length;i++){   
						  		ids += row[i].id+","; 
							}
						  	return id;
					  	} 
					}else if(parameter=='notNull'){//至少获得一个id
						var id = ""; 
					    if(row.length<1){//如果没有选择数据，默认选中第一行数据
					    	$('#list').datagrid('selectRow', 0);
					    	var row = $("#list").datagrid("getSelections");  
					    }
					    id += row[0].id; 
					    return id;
					}else{
						$.messager.alert('提示','参数无效！');
						close_alert();
						return null;
					}
				}
				
				/**
			 * 动态添加LayOut
			 * @author  liujl
			 * @param title 标签名称
			 * @param url 跳转路径
			 * @date 2015-05-21
			 * @modifiedTime 2015-6-18
			 * @modifier liujl
			 * @version 1.0
			 */
				function AddOrShowEast(title, url) {
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
				//条件查询
				function searchFrom(){
		   		    var mfName =  $('#mfName').val();
				    $('#list').datagrid('load', {
						mfName: mfName 
					});
				}
				/**
				 * 重置
				 * @author huzhenguo
				 * @date 2017-03-17
				 * @version 1.0
				 */
				function clears(){
					$('#mfName').textbox('setValue','');
					searchFrom();
				}
		</script>
