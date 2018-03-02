<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>首页组件维护</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
			//加载页面
			$(function(){
				var winH=$("body").height();
				$('#list').height(winH-78-30-27-26);
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
				        },onDblClickRow: function (rowIndex, rowData) {//双击查看
								if(getIdUtil('#list').length!=0){
							   	    AddOrShowEast('EditForm',"<c:url value='/oa/portalWidget/viewPortalWidget.action'/>?portalWidgetid="+getIdUtil('#list'));
							   	}
						},
						onBeforeLoad:function(){
							//GH 2017年2月17日 翻页时清空前页的选中项
							$('#list').datagrid('clearChecked');
							$('#list').datagrid('clearSelections');
						}
					});
					bindEnterEvent('queryName',searchFrom,'easyui');
				});
				function add(){
					AddOrShowEast('添加',"<c:url value='/oa/portalWidget/addPortalWidget.action'/>");
					$('#roleWins').dialog('close'); 
					$('#roleWins').dialog('destroy');
				}
				function edit(){
					  if(getIdUtil("#list")!= null){
	                      	AddOrShowEast('编辑',"<c:url value='/oa/portalWidget/editPortalWidget.action'/>?portalWidgetid="+getIdUtil("#list"));
	                    	$('#roleWins').dialog('close'); 
	            			$('#roleWins').dialog('destroy');
			   		  }
				}
				function del(){
				 //选中要删除的行
                   var iid = $('#list').datagrid('getChecked');
                  	if (iid.length > 0) {//选中几行的话触发事件	                        
				 	$.messager.confirm('确认', '您当前选中【'+iid.length+'】条数据,确定要全部删除吗?', function(res){//提示是否删除
						if (res){
							var ids = '';
							for(var i=0; i<iid.length; i++){
								if(ids!=''){
									ids += ',';
								}
								ids += iid[i].id;
							};
							$.ajax({
								url: "<c:url value='/oa/portalWidget/delPortalWidget.action'/>?portalWidgetid="+ids,
								type:'post',
								success: function() {
									$.messager.alert('提示信息','删除成功');
								$('#list').datagrid('reload');
								}
							});										
						}
                       });
                   }else{
                   	$.messager.alert('警告！','请选择要删除的信息！','warning');
                   	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
                   }
				}
	         	function reload(){
					//实现刷新栏目中的数据
					 $("#list").datagrid("reload");
				}
	         	
				function searchReload() {
					$('#queryName').textbox('setValue','');
					searchFrom();
				}
				/**
				 * 查询
				 * @author  zpty
				 * @date 2017-07-15
				 * @version 1.0
				 */
				function searchFrom() {
					var queryName = $.trim($('#queryName').val());
					$('#list').datagrid('load', {
						name : queryName
					});
				}
				/**
				 * 动态添加LayOut
				 * @author  zpty
				 * @param title 标签名称
				 * @param url 跳转路径
				 * @date 2017-07-15
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
							width : '40%',
							split : true,
							href : url,
							closable : true
						});
					}
				}
	</script>
</head>
<body style="margin: 0px;padding: 0px">
<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 30px;">
		<table >
			<tr>
				<td>
					<input type="text" id="queryName" name="queryName"  class="easyui-textbox" data-options="prompt:'编号、名称、地址'" />
					<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-search">查询</a>
					<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
	</div>	
	<div data-options="region:'center',border:true">
		<table id="list" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/oa/portalWidget/queryPortalWidget.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,toolbar:'#toolbarId'">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true" ></th>
					<th data-options="field:'id'"width="5%">组件编号</th>
					<th data-options="field:'name'"width="15%">组件名称</th>
					<th data-options="field:'url'"width="40%">组件地址</th>
					<th data-options="field:'status',width : '20%' ,formatter: function(value,row,index){var text = '';switch (value){case 0:text = '正常';break;case 1:text = '禁用';break;default:text='正常';break;}return text;}">组件状态</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
		<div id="toolbarId">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
			<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
<!--此模块中只要停用和启用<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a> -->
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
</body>
</html>