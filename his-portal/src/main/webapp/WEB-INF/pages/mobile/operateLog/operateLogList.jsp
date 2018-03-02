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
textarea {
resize: none;
disabled: disabled;
border:0;
height:19px; 
font-size:14px;
}
</style>
</head>
	<body>
		<div id="divLayout" class="easyui-layout" data-options="fit:true"style="width:100%;height:100%">
		 <div data-options="region:'north'" style="height: 45px;">
				<form id="search" method="post">
					<table style="width:100%;border-bottom:1px solid #95b8e7;padding:6px;" class="changeskinBottom">
						<tr>
							<td>
								<input id="sName" class="easyui-textbox" data-options="prompt:'操作用户'"/>
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="clearw()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
				</form>
		</div>
		<div data-options="region:'center'" style="height: 89%;border-top:0">
			<table id="list" fit="true" data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				<thead>
						<tr>
							<th data-options="field:'log_id',hidden:true">操作id</th>
							<th data-options="field:'log_account',align:'center',width:100">操作用户</th>
							<th data-options="field:'log_action',align:'center',width:80">操作行为</th>
							<th data-options="field:'log_menu_id',align:'center',width:200">操作栏目</th>
							<th data-options="field:'log_sql',align:'center',width:100">操作sql</th>
							<th data-options="field:'log_table',align:'center',width:200">操作表</th>
							<th data-options="field:'log_target_id',align:'center',width:500">目标编号</th>
							<th data-options="field:'log_time',align:'center',width:200">操作时间</th>
							<th data-options="field:'log_description',align:'center',width:150">操作备注</th>
						</tr>
					</thead>
			</table>
		</div>
<!-- 		<div data-options="region:'south'" style="height:10%;background:#FFFFFF"> -->
<!-- 		</div> -->
	</div>
	
	</body>
		<script type="text/javascript">
		var userMap =new Map();//用户数据源
			//加载页面
			 $(function(){
			  $.ajax({
				 url:'<%=basePath%>mosys/moOperateLog/updateUser.action',
					type:'post',
					success: function(userData) {
					}
				}); 
				$('#list').datagrid({
						pagination:true,
						pageSize:20,
						pageList:[20,30,50,80,100],
						url:'<%=basePath%>mosys/moOperateLog/queryOperateLog.action',
						onBeforeLoad:function(){
							$('#list').datagrid('clearChecked');
							$('#list').datagrid('clearSelections');
						},
						onDblClickRow: function (rowIndex, rowData) {//双击查看
							AddOrShowEast('查看',"<%=basePath %>mosys/moOperateLog/toViewOperateLog.action?id="+rowData.log_id);
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
				bindEnterEvent('sName',searchFrom,'easyui');//回车键查询	
			});
			//查询
			function searchFrom() {
				closeLayout();
				var queryName = $('#sName').textbox('getText');
				$('#list').datagrid('clearSelections');
				$('#list').datagrid('load', {
					queryName : queryName
				});
			}
			
			/**
			 * 动态添加标签页
			 * @author  zxl
			 * @param title 标签名称
			 * @param title 跳转路径
			 * @date 2015-05-21
			 * @version 1.0
			 */
			function AddOrShowEast(title, url) {
				var eastpanel = $('#panelEast'); //获取右侧收缩面板
				if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
					//重新装载右侧面板
					$('#divLayout').layout('panel', 'east').panel({
						href : url
					});
				} else {//打开新面板
					$('#divLayout').layout('add', {
						region : 'east',
						width : 580,
						title:title,
						split : true,
						maxHeight:820,
						href : url
						
					});
				}
			}
			
			/**
			 * 重置
			 * @author zxl
			 * @date 2017-03-17
			 * @version 1.0
			 */
			function clearw(){
				$('#sName').textbox('setValue','');
				searchFrom();
			}
			
			/* 
			* 关闭界面
			*/
			function closeLayout(flag){
				if(flag == 'edit'){
					$('#list').datagrid('reload');
				}
			}
		</script>
	</body>
</html>