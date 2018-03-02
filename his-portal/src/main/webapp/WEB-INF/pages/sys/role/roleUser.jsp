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
		<div class="easyui-layout" data-options="fit:true,border:false">
			<div data-options="region:'north',border:false" style="margin-top: 8px;">
				<table>
					<tr>
						<td>查询条件：</td>
						<td>
							<input type="text" id="searchUser" class="easyui-textbox" data-options="prompt:'用户名'" style="width: 200px;"/>
						</td>
						<td>
							&nbsp;<a id="search" href="javascript:void(0)" onclick="getUser()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-top: 2px;">查询</a>
						</td>
					</tr>
				</table>
			</div>
		<div  id="panelEast"  data-options="region:'center',title:'',iconCls:'icon-form',border:false" style="width:100%;margin-top: 5px;border: none;">
			<div style="width:100%;height: 100%;" data-options="border:false">
				<table id="userList" style="margin-top: 10px;" class="easyui-datagrid" data-options="idField:'id',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:true,fixed:true,fit:true">
					<thead>
						<tr>
							<th data-options="field:'checkBoxName', checkbox:'ture'">
							<th data-options="field:'name',width:'40%'" >用户名称</th>
							<th data-options="field:'account',width:'40%'" >用户账号</th>
						</tr>
					</thead>
				</table>
	    		<div id="roleaddUserdiv"></div>
			</div>
		</div>		
		</div>
	<script type="text/javascript">
	var roleId= "${param.roleId }";
	
	$(function(){
		
		$('#userList').datagrid({
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			fit:true,
			pagination:false,
			url:'<%=basePath%>sys/getUserList.action',
			queryParams:{roleId:roleId,userName:null},
			toolbar: [{
				id: 'btnAdd',
                text: '添加',
				iconCls: 'icon-add',
				handler: function(){
					AddDeptdilogs("用户", "<%=basePath%>sys/queryRoleUser.action?roleId="+roleId);
				}
			},{
				id: 'btnRemove',
                text: '删除',
				iconCls: 'icon-remove',
				handler: function(){
					var selectedObj = $('#userList').datagrid('getChecked');
					if(selectedObj.length>0){
						var ids = '';
						for(var i=0;i<selectedObj.length;i++){
							ids = ids + selectedObj[i].id + ",";
						}
						$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
							if (res){
								$.messager.progress({text:'保存中，请稍后...',modal:true});
	 								$.ajax({
	 									url:'<%=basePath%>sys/delRoleUser.action',
	 									data:{userId:ids,roleId:roleId},
	 									type:'post',
	 									success: function(data) {
	 										$.messager.progress('close');
	 										if(data=="success"){
	 											$.messager.alert('提示','删除成功！','info');
	 										}else if(data=="error"){
	 											$.messager.alert('提示','删除失败！','info');
	 										}
											$('#userList').datagrid('reload');
	 									}
	 								});
	 							}
						});
					}else{
						$.messager.alert('提示','请选择要删除的信息！','info');
						close_alert();
					}
				}
			},{
				id: 'btnCancel',
                text: '关闭',
				iconCls: 'icon-cancel',
				handler: function(){
					$('#divLayout').layout('remove','east');
				}
			}]
		});
	})
	
	setTimeout(function(){
        	bindEnterEvent('searchUser',getUser,'easyui');
        },100);
	
	 function AddDeptdilogs(title, url) {
				$('#roleaddUserdiv').dialog({    
				    title: title,    
				    width: '30%',    
				    height:'70%',
				    left:'30%',
				    top:'10%',
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				   });    
	}
	
	function getUser(){
		var userName = $('#searchUser').textbox('getValue');
		$('#userList').datagrid('load',{roleId:roleId,userName:userName});
	}
		
	
	</script>
	</body>
</html>