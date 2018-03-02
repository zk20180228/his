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
	border-top:0;
	border-left:0;
}
.panel-noscroll{
	border-right:0;
}
</style>
</head>
	<body>
		<div id="divLayout" class="easyui-layout" fit=true>
	        <div data-options="region:'north',border:false" style="height:35px;padding:1px;">	        
					<table style="width:100%;border:false;padding:1px;">
						<tr style="width:600px;">
							<td>
								<input id="sName" class="easyui-textbox" data-options="prompt:'条件查询'"/>
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</shiro:hasPermission>
							</td>
						</tr>
					</table>
			</div>
	           <div data-options="region:'center'" style="border-top:0">
				<table id="list" data-options="url:'${pageContext.request.contextPath}/sys/queryOperation.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true,width:100"></th>
							<th data-options="field:'user',formatter: funUser,width:100">操作用户</th>
							<th data-options="field:'action',width:80">操作行为</th>
							<th data-options="field:'deptId',width:100">操作部门</th>
							<th data-options="field:'menuId',width:100">操作栏目</th>
							<th data-options="field:'sql',width:100">操作sql</th>
							<th data-options="field:'table',width:180">操作表</th>
							<th data-options="field:'targetId',width:300">目标编号</th>
							<th data-options="field:'time',width:150">操作时间</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<div id="toolbarId">
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
		<script type="text/javascript">
			//加载页面
			$(function(){
			var winH=$("body").height();
				//$('#p').height(winH-78-30-27-2);//78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
				$('#list').height(winH-78-30-27-26);
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
					},
					onBeforeLoad: function (param) {//加载数据
			        }
					,onDblClickRow: function (rowIndex, rowData) {//双击查看
						if(getIdUtil('#list').length!=0){
					   	    AddOrShowEast('EditForm',"<c:url value='/sys/viewOperation.action'/>?id="+getIdUtil('#list'));
					   	}
					}    
				});
				bindEnterEvent('sName',searchFrom,'easyui');//回车键查询	
			});
				
				function reload(){
					//实现刷新栏目中的数据
					 $('#list').datagrid('reload');
				}
			
				//查询
				function searchFrom() {
					var queryName = $('#sName').textbox('getText');
					$('#list').datagrid('load', {
						action : queryName
					});
				}
				/**
				 * 重置
				 * @author huzhenguo
				 * @date 2017-03-17
				 * @version 1.0
				 */
				function clears(){
					$('#sName').textbox('setValue','');
					searchFrom();
				}
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
				
				//操作用户
				function funUser(value,row,index){
					if (row.user){
						return row.user.name;
					} else {
						return value;
					}
				}
				
		</script>
	</body>
</html>