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
</head>
	<body>
		<div id="divLayout"  class="easyui-layout" fit=true style="min-width:580px;width:auto;">
				<div class="easyui-panel" id="panelEast" data-options="region:'north',split:false,title:'相关科室信息添加',iconCls:'icon-book'" style="width:550px;min-height:220px;height:auto;">
							<input type="hidden" id="id" name="sysDepartment.id" value="${sysDepartment.id }">
							<input type="hidden" id="type" name="sysDepartment.deptType"  value="${sysDepartment.deptType }"/>
							<input type="hidden" id="name" name="sysDepartment.deptName"  value="${sysDepartment.deptName }"/>
				    		<table id="yklist"  class="honry-table" cellpadding="1" cellspacing="1" 
				    		data-options="
							iconCls: 'icon-edit',
							singleSelect: false,
							toolbar: '#bmb'
						">
							<thead>
								<tr>
									<input type="hidden" id="deptid" name="sysDepartment.id" value="${sysDepartment.id }">
									<th field="ck" checkbox="true" ></th>
									<th data-options="field:'deptCode'" >系统编号</th>
									<th data-options="field:'deptName'" >部门名称</th>
									<th data-options="field:'deptType'" >部门分类</th>
									<th data-options="field:'deptProperty'" >部门性质</th>
								</tr>
							</thead>
						</table>
				</div>
		<div data-options="region:'center',split:false,iconCls:'icon-book'" style="padding:5px;width:550px">
				<table id="dg" class="easyui-datagrid" title="添加详细信息" style="width:550px;height:451px"
						data-options="
							iconCls: 'icon-edit',
							singleSelect: true,
							toolbar: '#tb',
							onClickRow: onClickRow
						">
					<thead>
						<tr>
							<th data-options="field:'deptCode',width:100,align:'center',editor:'textbox'">系统编号</th>
							<th data-options="field:'deptName',width:100,align:'center',editor:'textbox'">部门名称</th>
							<th data-options="field:'deptType',width:100,align:'right',editor:'textbox'">部门分类</th>
							<th data-options="field:'deptProperty',width:100,align:'right',editor:'textbox'">部门性质</th>
						</tr>
					</thead>
				</table>
				<div id="tb" style="height:auto">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">删除</a>
				</div>
		</div>
		<div data-options="region:'south',split:false,iconCls:'icon-book'" style="padding:5px;">
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			</div>
		</div>
	</div>
	<script>
		$(function(){
				//添加datagrid事件
				$('#yklist').datagrid({
					pagination:true,
					fitColumns:true,
					pageSize:10,
					toolbar: [{
	                    id: 'btnAdd',
	                    text: '确定',
	                    iconCls: 'icon-add',
	                    handler: function () {
	                       addxgDept();
	                    }
	                }]
					,onDblClickRow: function (rowIndex, rowData) {//双击添加
							if(getIdUtil("#yklist").length!=0){
						   	    addxgDept();
						   	}
						}    
					});
				});
		var editIndex = undefined;
		function onClickRow(index){
			if (editIndex != index){
				if (endEditing()){
					$('#dg').datagrid('selectRow', index)
							.datagrid('beginEdit', index);
					editIndex = index;
				} else {
					$('#dg').datagrid('selectRow', editIndex);
				}
			}
		}

		/**
		 * 删除相关部门的方法
		 * @author  zpty
		 * @param 
		 * @date 2015-06-10
		 * @version 1.0
		 */
		function removeit(index){
			var id=$('#id').val();
			var row=$('#dg').datagrid('getSelected');
			var ids=row.id;
			$.ajax({
				url: '<%=basePath %>baseinfo/department/delDept.action?id='+id+'&deptid='+ids,
				type:'post',
				success: function() {
					$.messager.alert('提示','删除成功');
				}
			});
			$('#dg').datagrid({
				url:'<%=basePath %>baseinfo/department/findRelInfoList.action?id='+id
			});										
			$('#dg').datagrid('reload');
		}		 
		//关闭页面
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		
		/**
		 * 取出所有的相关部门信息
		 * @author  zpty
		 * @param 
		 * @date 2015-06-10
		 * @version 1.0
		 */
		var id =$('#id').val() ;
		var deptNameOld =$('#deptName').val() ;
		$(function(){
			$('#dg').datagrid({
				url:'<%=basePath %>baseinfo/department/findRelInfoList.action?id='+id
			});
			$("#dg").datagrid("reload");
		});
		
		/**
		 * 取出所有的药库部门信息
		 * @author  zpty
		 * @param 
		 * @date 2015-06-10
		 * @version 1.0
		 */
		$(function(){
			$('#yklist').datagrid({
				url:'baseinfo/department/findykList.action'
			});
			$("#yklist").datagrid("reload");
		});
		
		/**
		 * 增加相关部门的方法
		 * @author  zpty
		 * @param 
		 * @date 2015-06-10
		 * @version 1.0
		 */
		function addxgDept(){
			var id=$('#id').val();
			var type=$('#type').val();
            var rows = $('#yklist').datagrid('getChecked'); 
			var ids = '';
			var types='';
			var names='';
			for(var i=0; i<rows.length; i++){
				if(ids!=''){
					ids += ',';
				}
				if(types!=''){
					types+=',';
				}
				if(names!=''){
					names+=',';
				}
				ids += rows[i].id;
				types+=rows[i].deptType;
				names+=rows[i].deptName;
			};
			$.ajax({
				url: '<%=basePath %>baseinfo/department/addDept.action?id='+id+'&type='+type+'&deptid='+ids+'&depttype='+types+'&deptname='+encodeURI(encodeURI(names)),
				type:'post',
				success: function(data) {
					if(data==""){
						$.messager.alert('提示','新增成功');
					}else{
						$.messager.alert('提示','部门:'+data+'已存在');
						close_alert();
					}				
				}
			});
			$('#dg').datagrid({
				url:'<%=basePath %>baseinfo/department/findRelInfoList.action?id='+id
			});										
			$('#dg').datagrid('reload');
		}		 
		
	</script>
	</body>
</html>