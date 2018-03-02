<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<div id="layoutId" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'west',split:false" style="width:300px;">
				<table style="padding-top:5px;padding-left:5px;">
					<tr>
						<td>用户：<input id="userId"></td>
					</tr>
					<tr>
						<td><ul id="menuId"></ul></td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center'">
				<table id="gridId">
					<thead frozen="true">
						<tr>
							<th data-options="field:'ck',checkbox:true"></th>
							<th data-options="field:'userAcc',hidden:true,width:200">用户账户</th>
							<th data-options="field:'menuId',hidden:true,width:200">栏目</th>
							<th data-options="field:'menuName',width:200">栏目名</th>
							<th data-options="field:'menuAlias',width:200">栏目别名</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<div id="toolbarId">
			<shiro:hasPermission name="${menuAlias }:function:delete">
				<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias }:function:author">
				<a href="javascript:void(0)" onclick="juris()" class="easyui-linkbutton" data-options="iconCls:'icon-house_link',plain:true">授权</a>
				<a href="javascript:void(0)" onclick="resetJuris()" class="easyui-linkbutton" data-options="iconCls:'reset',plain:true">初始化授权</a>
			</shiro:hasPermission>
		</div>
		<script type="text/javascript">
		var treeData = '';
		$(function(){
			$('#userId').combogrid({
				idField:'account',
				textField:'name',
				mode:'remote',
				panelWidth:600,
				panelHeight:560,
				striped:true,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				pagination:true,
				pageNumber:1,
				pageSize:20,
				pageList:[20,30,50,100],
				url:'<%=basePath %>sys/userMenuFunJuris/queryFunJurisUserList.action',
				columns:[[
					{field:'account',title:'账户',width:80},
					{field:'name',title:'姓名',width:90},
					{field:'nickName',title:'曾用名',width:100},
					{field:'phone',title:'手机',width:100},
					{field:'email',title:'电子邮箱',width:120},
					{field:'remark',title:'备注',width:100}
				]],
				onSelect:function(index,row){
					lodatree(row.account)
					$('#gridId').datagrid('load',{userAcc:row.account});
				} 
			});
			$('#userId').combogrid('textbox').bind('focus',function(){
				$('#userId').combogrid('showPanel');
			});
			
			lodatree('');
			
			$('#gridId').datagrid({
				fit:true,
				rownumbers:true,
				striped:true,
				border:false,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				pagination:false,
				url:'<%=basePath %>sys/userMenuFunJuris/queryFunJuris.action',
				queryParams:{userAcc:null},
				toolbar:'#toolbarId',
				onLoadSuccess:function(data){
					$(this).datagrid('uncheckAll');
				}
			});
		});
		
		
		function lodatree(userAcc){
			$('#menuId').tree({
				url:'<%=basePath %>sys/userMenuFunJuris/queryFunJurisMenuTree.action',
				queryParams: {
					userAcc : userAcc
				},
				animate:true,
				checkbox:true,
				onlyLeafCheck:false,
				onSelect:function(node){
					$(this).tree('check',node.target)
				},
				onLoadSuccess : function(node, data){
					treeData = data;
				},
				onCheck:function(node, checked){
					var userAcc = $('#userId').combogrid('getValue');
					if(userAcc != null && userAcc != ''){
						var nodes = new Array();
						nodes[0] = node;
						var rows = $('#gridId').datagrid('getRows');
						if(!$(this).tree('isLeaf',node.target)){
							nodes = $(this).tree('getChildren',node.target);
						}
						if(checked){
							for(var i=0;i<nodes.length;i++){
								if(nodes[i].attributes.haveson == 1){
									var f = 0;
									for(var j = 0; j < rows.length; j++){
										if(rows[j].menuAlias == nodes[i].attributes.alias){
											f = 1;
											break;
										}
									}
									if(f == 0){
										var row = $('#gridId').datagrid('appendRow',{
											userAcc:userAcc,
											menuId:nodes[i].id,
											menuName:nodes[i].text,
											menuAlias:nodes[i].attributes.alias
										});
									}
								}
							}
							$('#gridId').datagrid('checkAll');
						}else{
							for(var i = 0; i < nodes.length; i++){
								if(nodes[i].attributes.haveson == 1){
									for(var j = 0; j < rows.length; j++){
										if(rows[j].menuAlias == nodes[i].attributes.alias){
											if($('#gridId').datagrid('getRowIndex', rows[j]) >= 0){
												$('#gridId').datagrid('uncheckRow', $('#gridId').datagrid('getRowIndex', rows[j]));
												$('#gridId').datagrid('deleteRow', $('#gridId').datagrid('getRowIndex', rows[j]));
											}
										}
									}
								}
							}
							var rs = $('#gridId').datagrid('getChecked');
							if(rs != null && rs.length > 0){
								$('#gridId').datagrid('checkRow', $('#gridId').datagrid('getRowIndex', rs[0]));
							}
						}
					}else{
						$.messager.alert('提示','请选择用户!');
					}
				}
			});
		}
		function juris(){
			var userAcc = $('#userId').combogrid('getValue');
			if(userAcc!=null&&userAcc!=''){
				$.messager.confirm('确认','您确认想要权限吗？',function(r){
					if(r){
						var rows = $('#gridId').datagrid('getChecked');
						var menuAlias = '';
						for(var i=0;i<rows.length;i++){
							if(menuAlias != ''){
								menuAlias += ',';
							}
							menuAlias += rows[i].menuAlias;
						}
						$.messager.progress({text:'保存中，请稍后...',modal:true});
						$.ajax({ 
							url:'<%=basePath %>sys/userMenuFunJuris/saveFunJuris.action',
							type:'post',
							data:{"userAcc":userAcc,"q":menuAlias},
							success: function(dataMap) {
								$.messager.progress('close');
								if(dataMap.resCode=="error"){
									$.messager.alert('提示',dataMap.resMsg);
								}else{
									lodatree(userAcc);
									$('#gridId').datagrid('load');
									$.messager.alert('提示',dataMap.resMsg);
								}
							},
							error:function() {
								$.messager.progress('close');
								$.messager.alert('提示','请求失败!');
							}
						});
					}
				});
			}else{
				$.messager.alert('提示','请选择用户!');
			}
		}
		
		function resetJuris(){
			var userAcc = $('#userId').combogrid('getValue');
			$.messager.confirm('确认','您确认想要所有用户栏目权限初始化为列表所示？',function(r){
				if (r){
					var rows = $('#gridId').datagrid('getChecked');
					var menuAlias = '';
					for(var i=0;i<rows.length;i++){
						if(menuAlias != ''){
							menuAlias += ',';
						}
						menuAlias += rows[i].menuAlias;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
					$.ajax({ 
						url:'<%=basePath %>sys/userMenuFunJuris/resetJuris.action',
						type:'post',
						data:{"q" : menuAlias},
						success: function(dataMap) {
							$.messager.progress('close');
							if(dataMap.resCode=="error"){
								$.messager.alert('提示',dataMap.resMsg);
							}else{
								lodatree(userAcc);
								$('#gridId').datagrid('load');
								$.messager.alert('提示',dataMap.resMsg);
							}
						},
						error:function() {
							$.messager.progress('close');
							$.messager.alert('提示','请求失败!');
						}
					});
				}
			});
		}
		
		function del(){
			var userAcc = $('#userId').combogrid('getValue');
			if(userAcc!=null&&userAcc!=''){
				var rows = $('#gridId').datagrid('getChecked');
				if(rows!=null&&rows.length>0){
					$.messager.confirm('确认','您确认想要删除选中权限吗？',function(r){
						if (r){
							var menuAlias = '';
							for(var i=0;i<rows.length;i++){
								if(menuAlias != ''){
									menuAlias += ',';
								}
								menuAlias += rows[i].menuAlias;
							}
							if(menuAlias!=''){
								$.messager.progress({text:'保存中，请稍后...',modal:true});
								$.ajax({ 
									url:'<%=basePath %>sys/userMenuFunJuris/delFunJuris.action',
									type:'post',
									data:{"userAcc":userAcc,"q":menuAlias},
									success: function(dataMap) {
										$.messager.progress('close');
										if(dataMap.resCode=="success"){
											lodatree(userAcc);
											$('#gridId').datagrid('load');
										}
										$.messager.alert('提示',dataMap.resMsg);
									},
									error:function() {
										$.messager.progress('close');
										$.messager.alert('提示','请求失败!');
									}
								});
							}else{
								$.messager.alert('提示','请选择需要删除的授权信息!');
							}
						}
					})
				}else{
					$.messager.alert('提示','请选择需要删除的授权信息!');
				}
			}else{
				$.messager.alert('提示','请选择用户!');
			}
		}
		</script>
	</body>
</html>