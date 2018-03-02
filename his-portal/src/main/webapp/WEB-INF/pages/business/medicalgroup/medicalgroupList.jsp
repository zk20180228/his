<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style type="text/css">
.panel-header{
	border-top:0;
}
</style>
</head>
	<body>
		<div class="easyui-layout"  fit=true style="width: 100%; height: 100%; overflow-y: auto;">
			<div id="p" data-options="region:'west',split:true" title="部门科室管理" style="width: 260px; padding: 0px; overflow: hidden;">
				<div style="width: 100%; height: 100%; overflow-y: auto;">
					<ul id="tDt"></ul>
				</div>
			</div>
			<div data-options="region:'center',border:false">
				<div id="divLayout" class="easyui-layout" data-options="fit:true">
					<div style="padding: 5px 5px 0px 5px;">
						查询条件：<input class="easyui-textbox" id="queryName" name="queryName" onkeydown="KeyDown(0)" data-options="prompt:'医疗组名称,拼音,五笔,科室,自定义'"  style="width: 230px;"/></td>
						&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</div>
					<div data-options="region:'center',border:true" style="border-top:0">
						<input type="hidden" name="deptId" id="deptId"/>
						<input type="hidden" name="deptNames" id="deptNames"/>
						
						<table id="medicalGroup" style="width: 100%;"
							data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
							<thead>
								<tr>
									<th data-options="field:'ID',align:'center',checkbox: true" style="width:50"></th>
									<th data-options="field:'deptId',formatter:deptFamater, width : '10%'">科室</th>
									<th data-options="field:'name',width : '10%'">名称</th>
									<th data-options="field:'inputCode', width : '10%'">自定义码</th>
									<th data-options="field:'remark', width : '20%'">备注</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
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
		<script>
			function reload(){
				 $('#medicalGroup').datagrid('reload');
			}
			var delptList='';//用于显示科室集合
			$.ajax({
				url: "${pageContext.request.contextPath}/baseinfo/department/queryDepartments.action",
				type:'post',
				async:false,
				success: function(deptListData) {
					delptList = deptListData;
				}
			});
			$(function(){
				//
				bindEnterEvent('queryName',searchFrom,'easyui');//绑定回车事件
				//添加datagrid事件及分页
				//加缓冲时间1毫秒
// 				setTimeout(function(){
					//添加datagrid事件及分页
					$('#medicalGroup').datagrid({
						pagination:true,
				   		pageSize:20,
				   		pageList:[20,30,50,80,100],
						url:"<c:url value='/baseinfo/medicalGroup/queryMedicalGroup.action?menuAlias=${menuAlias}'/>",
						onDblClickRow : function(rowIndex, rowData) {//双击查看
							if(getIdUtil("#medicalGroup")!=null){
								AddOrShowEast('查看',"<c:url value='/baseinfo/medicalGroup/viewMedicalgroup.action'/>?id="+getIdUtil("#medicalGroup"),'500');
						   	}
						},
						onBeforeLoad:function(){
						    //GH 2017年2月17日 翻页时清空前页的选中项
							$('#medicalGroup').datagrid('clearChecked');
							$('#medicalGroup').datagrid('clearSelections');
						},
						onLoadSuccess:function(row, data){
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
							}}
					});
// 	            },100);
			});
			function add(){
				var treeId = $('#deptId').val();
				var deptNames=$('#deptNames').val();
				//数据库表修改  判断科室code是否为4位而不是32位
			    if(treeId==""||treeId.length < 4){
			    	$.messager.alert('提示',"请选择具体单位！");
			    	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				    return false;
			    }
				AddOrShowEast('添加','<%=basePath%>baseinfo/medicalGroup/medicalGroupDeptAdd.action','500','post'
						    ,{"deptId":treeId,"deptNames":encodeURI(encodeURI($('#deptNames').val()))}
				);
			}
			function edit(){
				var data=$('#medicalGroup').datagrid("getSelected");
				if(data){
					$('#deptNames').val(deptFamater(data.deptId));
					AddOrShowEast('修改',"<c:url value='/baseinfo/medicalGroup/medicalGroupEdit.action'/>?id="+getIdUtil("#medicalGroup"),'500');
			   	}else{
			   		$.messager.alert('提示',"请选择需要修改的信息");
			   		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
			   	}
			}
			
			//删除
			function del(){
				//选中要删除的行
	            var rows = $('#medicalGroup').datagrid('getChecked');
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
							$.post("<c:url value='/baseinfo/medicalGroup/delMedicalInfo.action'/>?ids="+ids+"&deleteFalg=0",function(data){
								 var retVal = data;
								  if(retVal=="no"){
									  $.messager.alert('提示信息','删除失败!');
								  }else if(retVal=="yes"){
									  $.messager.alert('提示信息','操作成功！');
									  $('#medicalGroup').datagrid('reload');
							  		}else{
	 									$.messager.alert('提示信息','连接错误，操作失败!');
	 									setTimeout(function(){
	 										$(".messager-body").window('close');
	 									},3500);
								  }
							});
						}
	               	});
	           	}else{
          	    	 $.messager.alert('提示信息','请选择要删除的信息！');
        	    	 setTimeout(function(){
        					$(".messager-body").window('close');
        				},3500);
        	     }	
			}
			
			//清除所填信息
			function clear() {
				$('#editForm').form('clear');
			}
			function closeLayout() {
				$('#divLayout').layout('remove', 'east');
			}
		
			//加载部门树
			$('#tDt').tree({
				url : "<c:url value='/baseinfo/medicalGroup/treeDept.action'/>",
				method : 'get',
				animate : true,
				lines : true,
				formatter : function(node) {//统计节点总数
					var s = node.text;
					if(node.children.length>0){
						if (node.children) {
							s += '&nbsp;<span style=\'color:blue\'>('
									+ node.children.length + ')</span>';
						}
					}
					return s;
				},onClick : function(node) {//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					$('#medicalGroup').datagrid('uncheckAll');
					$('#medicalGroup').datagrid('load', {
						deptId : node.id
					});
					$('#deptId').val(node.id);
					$('#deptNames').val(node.text);
					
				}
			});
			function getSelected() {//获得选中节点
				var id="";
				var node = $('#tDt').tree('getSelected');
				if (node) {
					id = node.id;
					return id;
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
						width :width ,
						split : true,
						href : url,
						method:method,
						queryParams:params,
						closable : true,
						border : false
					});
				}else{//打开新面板
					$('#divLayout').layout('add', {
						region : 'east',
						width :width ,
						split : true,
						href : url,
						method:method,
						queryParams:params,
						closable : true,
						border : false
					});
				}
			}
			//显示科室格式化
			function deptFamater(value){
				if(value!=null){
					for(var i=0;i<delptList.length;i++){
						if(value==delptList[i].id){
							return delptList[i].deptName;
						}
					}	
				}
			}
			function searchFrom() {
				var dept = getSelected();
				if(dept != null && dept.length < 32){
					dept="";
				}
				var queryName = $('#queryName').textbox('getValue');
				$('#medicalGroup').datagrid('load', {
					deptId : dept,
					name : queryName
				});
			}
		</script>
	</body>
</html>