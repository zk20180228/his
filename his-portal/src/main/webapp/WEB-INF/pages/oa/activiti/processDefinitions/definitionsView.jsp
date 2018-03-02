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
<title>流程定义视图</title>
</head>
<body>
<!-- 	<div class="container-fluid"> -->
<!-- 		<div class="input-group pull-right" style="width:200px;margin-top:5px;margin-bottom:5px;"> -->
<!-- 			<input id="searchId" type="text" class="form-control" placeholder="输入名称查询"> -->
<!-- 			<span class="input-group-btn"> -->
<!-- 				<button class="btn btn-default" type="button" href="javascript:void(0)" onclick="search()">查询</button> -->
<!-- 			</span> -->
<!-- 	    </div> -->
<!-- 	    <div class="pull-left" style="margin-top:5px;margin-bottom:5px;"> -->
<!-- 	    	<a class="btn btn-default" type="button" href="javascript:void(0)" onclick="uploadView()" style="float:right">上传</a> -->
<!-- 		</div> -->
<!-- 		<div> -->
<!-- 			<table id="demoGrid"></table> -->
<!-- 		</div> -->
<!-- 	</div> -->
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
<!-- 						<th data-options="field:'ck',hidden:true" ></th> -->
						<th data-options="field:'id'" style="width:15%">编号</th>  
						<th data-options="field:'name'" style="width:30%">名称</th>  
<!-- 						<th data-options="field:'category'" style="width:20%">分类</th>   -->
						<th data-options="field:'version'" style="width:10%">版本</th>  
						<th data-options="field:'createTime'" style="width:10%">创建时间</th>  
<!-- 						<th data-options="field:'description'" style="width:10%">描述</th>   -->
<!-- 						<th data-options="field:'suspended'" style="width:10%">状态</th>   -->
						<th data-options="field:'operation',formatter:operateFormatter" align="center" style="width:10%">操作</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</div>
	<script type="text/javascript">
	$(function () {
		bindEnterEvent("searchId", searchform, 'easyui');
		$('#demoGrid').datagrid({
			pagination: true,
			pageSize: 20,
			pageList: [20, 30, 50, 100],
			url: '<%=basePath%>activiti/processDefinitions/definitionsViewPage.action'
		});
		
		$("#xmlModal").on("hidden.bs.modal", function() {
	        $(this).removeData("bs.modal");
	    });
	});
	
	function operateFormatter(value, row, index) {
		return [
			'<div class="btn-group btn-group-xs" role="group">'+
			 	'<a type="button" class="btn btn-primary" href="javascript:void(0)" onclick="graphProcessDefinition(\''+row.id+'\')">流程图</a>'+
			 	'<a type="button" class="btn btn-success" href="javascript:void(0)" onclick="viewXml(\''+row.id+'\')">查看XML</a>'+
// 			 	'<a type="button" class="btn btn-danger" href="javascript:void(0)" onclick="editView(\''+row.id+'\')">修改</a>'+
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
	function graphProcessDefinition(id){
		attWindow(id,'<%=basePath%>activiti/processDefinitions/graphProcessDefinition.action?v='+Math.random());
	}
	
	function viewXml(id){
		attWindow(id,'<%=basePath%>activiti/processDefinitions/viewXml.action?v='+Math.random());
	}
	
	function editView(id){
		attWindow(id,'<%=basePath%>activiti/processDefinitions/editView.action?v='+Math.random());
	}
	
	function uploadView(){
		attWindow(null,'<%=basePath%>activiti/processDefinitions/uploadView.action?v='+Math.random());
	}
	
	//以post方式打开窗口
	function attWindow(id,url){
		var id = id;
		var url = url;
		var name = '查看';
		var width = 1350;
		var height = 700;
		var top = (window.screen.availHeight-30-height)/2;
		var left = (window.screen.availWidth-10-width)/2;
		if($("#winOpenFrom").length<=0){  
			var form = "<form id='winOpenFrom' action='"+url+"' method='post' target='"+name+"'>" +
					"<input type='hidden' id='winOpenFromInpId' name='id'/></form>";
			$("body").append(form);
		} 
		$('#winOpenFromInpId').val(id);
		openWindow('about:blank',name,width,height,top,left);
		$('#winOpenFrom').prop('action',url);
		$("#winOpenFrom").submit();
	}
	
	//打开窗口
	function openWindow(url,name,width,height,top,left){
		window.open(url, name, 'height=' + height + ',innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no'); 
	}
	</script>
</body>
</html>