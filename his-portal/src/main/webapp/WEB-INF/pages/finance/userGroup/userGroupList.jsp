<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>财务分组</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
</head>
<body>
	<div id="divLayout" class="easyui-layout" data-options="fit:true">
		<div id="q" data-options="region:'north',border:false" style="width: 100%;height: 40px;">
		<div style="width:100%;height: 100%;margin-top:auto;margin-bottom:auto;">    
				<table style="width:100%;height: 100%;" data-options="fit:true">
					<tr>
						<td>
							组名：<input type="text" class="easyui-textbox"  name="name" id="name"  style="width:260px" onkeydown="KeyDown()" data-options="prompt:'分组名称、拼音码、五笔码、自定义码'"/>
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查&nbsp;询&nbsp;</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>  
		</div>
		<div id="p" data-options="region:'center'" style="width: 100%;height: 91%">			
			<div style="width: 100%;height: 100%">                                                                                                                  
			<table id="list" data-options="url:'<%=basePath %>finance/userGroup/queryUserGroup.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',title:'查询列表',fit:true">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'groupName'" style="width:25%">分组名称</th>
						<th data-options="field:'groupPinyin'" style="width:12%">拼音码 </th>
						<th data-options="field:'groupWb'" style="width:10%">五笔码</th>
						<th data-options="field:'groupInputcode'" style="width:10%">自定义码</th>
						<!--<th data-options="field:'groupOrder'" style="width:10%">排序</th>-->
						<th data-options="field:'stackRemark'" style="width:30%">备注</th>  
					</tr>
				</thead>
			</table>
			</div>
		</div>
	</div>
	<div id="toolbarId">
	<shiro:hasPermission name="${menuAlias}:function:add">
		<a href="javascript:void(0)" onclick="addGroup()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:edit">
		<a href="javascript:void(0)" onclick="editGroup()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:delete">		
		<a href="javascript:void(0)" onclick="delGroup()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="roleaddUserdiv"></div>
	<script type="text/javascript">
	//加载页面
	$(function(){
		var id='${id}'; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			onLoadSuccess: function (data) {//默认选中
	            var rowData = data.rows;
	            $.each(rowData, function (index, value) {
	            	if(value.id == id){
	            		$('#list').datagrid('checkRow', index);
	            	}
	            });
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
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				var row = $('#list').datagrid('getSelected'); //获取当前选中行    
                if(row){
                	var groupName = encodeURIComponent(encodeURIComponent(row.groupName));
               		AddOrShowEast('EditForm','<%=basePath %>finance/userGroup/viewUserGroup.action?financeUsergroup.groupName='+groupName);
			   	}
			},
			onClickRow: function(row){							
				var eastpanel=$('#panelEast'); //获取右侧收缩面板
				if(eastpanel.length>0){ //判断右侧收缩面板是否存在				
					$('#divLayout').layout('remove','east');
					$('#roleaddUserdiv').window('close');
				}
			}	                
		});
		});
		//弹出添加编辑区域
		function addGroup(){
			AddOrShowEast('EditForm','<%=basePath %>finance/userGroup/addOrUpdateUserGroupUrl.action');
		}
		//弹出修改编辑区域
		function editGroup(){
			var row = $('#list').datagrid('getSelected'); //获取当前选中行         
            	if(row){
            		var groupName = encodeURIComponent(encodeURIComponent(row.groupName));
            		AddOrShowEast('EditForm','<%=basePath %>finance/userGroup/addOrUpdateUserGroupUrl.action?financeUsergroup.groupName='+groupName);
				}else{
					$.messager.alert('提示','请选择一条记录!');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
	        		return;
				}
		}
		//删除财务组及其下面的员工
		function delGroup(){
			 //选中要删除的行
                    var rows = $('#list').datagrid('getChecked');
                	if (rows.length > 0) {//选中几行的话触发事件	                        
					 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
							if (res){
								var id ='';
								for(var i=0;i<rows.length;i++){
									if(id!=''){
										id = id+','+rows[i].id;
									}else{
										id = rows[i].id;
									}
								} 
								$.ajax({
									  url : "<%=basePath %>finance/userGroup/deleteGroup.action",
									  type : "post", 
									  data:'financeUsergroup.id='+id,
									  success : function(data){  
										  var retVal = eval("("+data+")");
										  if(retVal=="no"){
											  $.messager.alert('操作提示', '删除失败！'); 
										  }else if(retVal=="yes"){
											  $.messager.alert('操作提示', '删除成功！'); 
											  setTimeout(function(){
													$(".messager-body").window('close');
												},3500);
											  $('#list').datagrid('reload');
										  }else{
											  $.messager.alert('操作提示', '连接错误，操作失败!'); 
										  }
									  }
								 });
							}
                    	});
                	}else{
                		$.messager.alert('操作提示', '请选择要删除的信息！');
                		setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
                	}
		}
		
		function reload(){
			//实现刷新栏目中的数据
			$('#list').datagrid('reload');
		}
	
	
		//查询财务组信息
		function searchFrom(){
		    var name =	$.trim($('#name').val());
		    $('#list').datagrid('load', {
		    	'financeUsergroup.groupName': name
			});
		}
		function KeyDown() {  
	   		if (event.keyCode == 13) {  
		        event.returnValue=false;  
		        event.cancel = true;  
		        searchFrom();  
		    }  
		} 
		
		function AddOrShowEast(title, url) {
			var eastpanel=$('#divLayout').layout('panel','east'); //获取右侧收缩面板
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
	</script>
</body>
</html>