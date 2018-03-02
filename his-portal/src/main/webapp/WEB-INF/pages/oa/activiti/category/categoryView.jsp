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
<title>流程分类视图</title>
<style type="text/css">
.layout-split-east {
    border-left: 0px; 
}
table.honry-table td{
	border-left:0px;
}
.panel-header{
	border-top-width:0;
	border-left-width:0;
}
.deleteBorder .panel-header,.deleteBorder .panel-body{
	border-top-width:1px;
	border-left:0;
	border-right:0;
	border-bottom:0;
}
#panelEast{
	border-left:0;
}
</style>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:true" class="deleteBorder" style="border-top:0">
		<div style="width:100%;height:6%;">    		
			<table style="width:100%;height:100%;padding:0px;margin: 0px;" data-options="fit:true,border:true">
				<tr>
					<td>
						<input class="easyui-textbox" id="searchId" data-options="prompt: '请输入名称' " style="width: 200px;" />
						<a href="javascript:void(0)" onclick="searchform()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
				</tr>
			</table>
		</div>
		<div style="width:100%;height:94%;">
			<table id="demoGrid" data-options="method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
				<thead>
					<tr>
<!-- 						<th data-options="field:'ck',checkbox:true" ></th>	 -->
<!-- 						<th data-options="field:'id'" style="width:20%">编号</th>					 -->
						<th data-options="field:'name'" style="width:25%">名称</th>  
						<th data-options="field:'priority'" style="width:10%">排序</th>	
						<th data-options="field:'operation',formatter:operateFormatter" align="center" style="width:10%">操作</th>
					</tr>
				</thead>
			</table>
		</div>
		<div id="toolbarId">
			<a href="javascript:void(0)" onclick="creatCategory()" class="easyui-linkbutton" iconCls="icon-add" data-options="plain:true">新建</a>
		</div>
	</div>
</div>

<script type="text/javascript">

	$(function () {
		bindEnterEvent("searchId", searchform, 'easyui');
		$('#demoGrid').datagrid({
			method: 'post',
			pagination: true,
			pageSize: 20,
			pageList: [20, 30, 50, 100],
			url: '<%=basePath%>activiti/category/categoryViewPage.action',
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
				}
			}
		});
	});
	 
	function creatCategory(){
		AddOrShowEast('EditForm',"${pageContext.request.contextPath}/activiti/category/editView.action");
	}
	
	function updateCategory(rowId){
		AddOrShowEast('EditForm',"${pageContext.request.contextPath}/activiti/category/editView.action?id="+rowId);
	}
	
	function operateFormatter(value, row, index) {
		return [
			'<div class="btn-group btn-group-xs" role="group">'+
<%-- 			 	'<a type="button" class="btn btn-primary" href="<%=basePath%>activiti/category/editView.action?id='+row.id+'">编辑</a>'+ --%>
			 	'<a type="button" class="btn btn-primary" onclick="updateCategory(\''+row.id+'\')">编辑</a>'+
			'</div>'
		]
 	}
	
	
	function searchform(){
		$('#demoGrid').datagrid('load', {
		    'name': $('#searchId').val()
		});
	}
	function reload(){
		$('#searchId').textbox("clear");
		//实现刷新栏目中的数据
		searchform();
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
				closable : true,
				border: false
			});
		}			
	}
	</script>
</body>
</html>