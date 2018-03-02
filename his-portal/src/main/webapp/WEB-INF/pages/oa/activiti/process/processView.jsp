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
<title>流程配置视图</title>
</head>
<body>
<!-- <div class="container-fluid"> -->
<!-- 	<div class="input-group pull-right" style="width:200px;margin-top:5px;margin-bottom:5px;"> -->
<!-- 		<input id="searchId" type="text" class="form-control" placeholder="输入名称查询"> -->
<!-- 		<span class="input-group-btn"> -->
<!-- 			<button class="btn btn-default" type="button" href="javascript:void(0)" onclick="searchform()">查询</button> -->
<!-- 		</span> -->
<!--     </div> -->
<!--     <div class="pull-left" style="margin-top:5px;margin-bottom:5px;"> -->
<!--     	<a class="btn btn-default" type="button" href="javascript:void(0)" onclick="addModeler('')" style="float:right">新建</a> -->
<!-- 	</div> -->
<!-- 	<table id="demoGrid"></table> -->
<!-- </div> -->


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
						<th data-options="field:'ck',hidden:true" ></th>
						<th data-options="field:'name'" style="width:25%">名称</th>  
						<th data-options="field:'stop_flg',formatter:stopFormatter" style="width:10%">排序</th>	
						<th data-options="field:'operation',formatter:operateFormatter" align="center" style="width:10%">操作</th>
					</tr>
				</thead>
			</table>
		</div>
		<div id="toolbarId">
			<a href="javascript:void(0)" onclick="addModeler('')" class="easyui-linkbutton" iconCls="icon-add" data-options="plain:true">新建</a>
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
			url: '<%=basePath%>activiti/process/processViewPage.action',
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
	 function stopFormatter(value, row, index){
		if(0==value){
			return '启用';
		}else{
			return '停用';
		}
	}
	function operateFormatter(value, row, index) {
		return [
			'<div class="btn-group btn-group-xs" role="group">'+
			 	'<a type="button" class="btn btn-success" href="javascript:void(0)" onclick="editModeler(\''+row.id+'\')">编辑</a>'+
			 	'<a type="button" class="btn btn-primary" href="javascript:void(0)" onclick="nodeView(\''+row.confBaseCode+'\')">配置</a>'+
// 			 	'<a type="button" class="btn btn-warning" href="javascript:void(0)" onclick="juris(\''+row.id+'\')">授权</a>'+
			'</div>'
		]
 	}
	
	function delModeler(id){
		$.messager.alert('提示','功能建设中');
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
	function nodeView(id){
		attWindow(id,1250,650,'<%=basePath%>activiti/process/nodeView.action?v='+Math.random());
	}
	
	function editModeler(id){
		AddOrShowEast('EditForm','<%=basePath%>activiti/process/editView.action?id='+id+'&v='+Math.random(),580);
	}
	
	function addModeler(id){
		AddOrShowEast('EditForm','<%=basePath%>activiti/process/editView.action?id='+id+'&v='+Math.random(),580);
	}
	
	//以post方式打开窗口
	function attWindow(id,width,height,url){
		var id = id;
		var url = url;
		var name = '查看';
		var width = width;
		var height = height;
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
	
	function juris(id){
		attWindow(id,1200,750,'<%=basePath%>oa/juris/jurisView.action');
	}	
	function AddOrShowEast(title, url,wid) {
		var eastpanel=$('#divLayout').layout('panel','east'); //获取右侧收缩面板
		if(eastpanel.length>0){ //判断右侧收缩面板是否存在
			//重新装载右侧面板
	   		$('#divLayout').layout('panel','east').panel({
                   href:url 
            });
		}else{//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : wid,
				split : true,
				href : url,
				closable : true,
				border: false
			});
		}			
	}
	
	</script>
</form>
</body>