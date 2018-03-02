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
	<div id="divLayout" class="easyui-layout" data-options="fit:true" id="treeLayOut">
					<div data-options="region:'north',border:false" style="width:100%;">
						<form id="search" method="post">
							<table
								style="width: 100%; border: 1px solid #95b8e7; padding: 5px;">
								<tr>
									<td style="width: 320px;" nowrap="nowrap">
										关键字：<input type="text" id="queryName" name="queryName" onkeydown="KeyDown(0)"/>
										<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</td>
								</tr>
							</table>
						</form>
					</div>
				<div data-options="region:'center',border:false" style="width:100%;">
					<table id="list" style="width:100%" data-options="url:'querylistEmrtype.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
						<thead>
							<tr>
								<th field="ck" checkbox="true"></th>
								<th data-options="field:'name'" style="width: 7%">
									电子病历分类名称
								</th>
								<th data-options="field:'encode'" style="width: 7%">
									代码
								</th>
								<th data-options="field:'description'" style="width: 7%">
									说明
								</th>
								<th data-options="field:'order'" style="width: 7%">
									排序
								</th>
								<th data-options="field:'path'" style="width: 7%">
									路径
								</th>
								<th data-options="field:'levell'" style="width: 7%">
									层级
								</th>
								<th data-options="field:'canSelect'" formatter="formatCheckBox">
									可选标志
								</th>
								<th data-options="field:'isdefault'" formatter="formatCheckBox">
									默认标志
								</th>
								<th data-options="field:'hospital'" style="width: 7%">
									适用医院
								</th>
								<th data-options="field:'nonhospital'" style="width: 7%">
									不适用医院
								</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
	<script type="text/javascript">
	//加载页面
	$(function() {
		var winH=$("body").height();
		$('#p').height(winH-78-30-27-2);
		$('#treeDiv').height(winH-78-30-27-2);
		$('#list').height(winH-78-30-27-22);
		
		var id = "${id}"; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			onLoadSuccess : function(data) {//默认选中
				var rowData = data.rows;
				$.each(rowData, function(index, value) {
					if (value.id == id) {
						$("#list").datagrid("checkRow", index);
					}
				});
			},
			toolbar : [ {
				id : 'btnAdd',
				text : '添加',
				iconCls : 'icon-add',
				handler : function() {
					AddOrShowEast('EditForm', 'addEmrtype.action');
				}
			},'-', {
				id : 'btnEdit',
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					if (getIdUtil("#list").length != 0) {
						AddOrShowEast('EditForm', 'editEmrtype.action?id=' + getIdUtil("#list"));
					}
				}
			}, '-', {
				id : 'btnDelete',
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					//选中要删除的行
					var idss = $('#list').datagrid('getChecked');
					if (getIdUtil("#list").length != 0) {
					if (idss.length > 0) {//选中几行的话触发事件	                        
						$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
							if (res) {
								var ids = '';
								for ( var i = 0; i < idss.length; i++) {
									if (ids != '') {
										ids += ',';
									}
									ids += idss[i].id;
								};
								$.ajax({
									url : 'delEmrtypeToWin.action?id=' + ids,
									type : 'post',
									success : function() {
										$.messager.alert('提示','删除成功');
										$("#list").datagrid("reload");
									}
								});
								for ( var i = 0; i < idss.length; i++) {
									var index = $('#list').datagrid('getRowIndex', idss[i]);//获取某行的行号
									$('#list').datagrid('deleteRow', index); //通过行号移除该行
								};
							}
						});
					}
					}
				}
			}, '-', {
				id : 'btnReload',
				text : '刷新',
				iconCls : 'icon-reload',
				handler : function() {
					//实现刷新栏目中的数据
					$("#list").datagrid("reload");
				}
			} ],
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				if(getIdUtil("#list").length!=0){
					AddOrShowEast('EditForm', 'viewEmrtype.action?id='+getIdUtil("#list"));
					
				}
			}
		});
	});
		function KeyDownchaxun()  
				{  
				    if (event.keyCode == 13)  
				    {  
				        event.returnValue=false;  
				        event.cancel = true;  
				        searchFrom();  
				    }  
				} 
		//查询
	   		function searchFrom(){ 
	   		    var encode =$('#encode').val();
			    $('#list').datagrid('load', {
					encode:encode
				});
			}
			function formatCheckBox(val,row){
					if (val == 1){
						return '是';
					} else {
						return '否';
					}
				}	
	/**
	 * 动态添加标签页
	 * 
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-05-21
	 * @version 1.0
	 */
	//动态添加LayOut
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
				
	
</script>
</body>
</html>
