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
<title>流程模型视图</title>
</head>
<body>
	<div data-options="region:'center',border:true" class="deleteBorder" style="border-top:0">
		<div style="width:100%;height:6%;">    		
			<table style="width:100%;height:100%;padding:0px;margin: 0px;" data-options="fit:true,border:true">
				<tr>
					<td>
						<input class="easyui-textbox"  id="searchId" data-options="prompt: '请输入名称' " style="width: 200px;" />
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
						<th data-options="field:'id'" style="width:10%" >模型编号</th>					
						<th data-options="field:'name'" style="width:35%" >名称</th>  
<!-- 						<th data-options="field:'version'" style="width:10%" >版本</th>	 -->
						<th data-options="field:'createTime'" style="width:10%" >创建时间</th>	
						<th data-options="field:'lastUpdateTime'" style="width:10%">最后更新时间</th>	
						<th data-options="field:'deploymentId'" style="width:10%" >流程定义</th>	
						<th data-options="field:'operation',formatter:operateFormatter" align="center" style="width:7%">操作</th>
					</tr>
				</thead>
			</table>
		</div>
		<div id="toolbarId">
			<a href="javascript:void(0)" onclick="toadd()" class="easyui-linkbutton" iconCls="icon-add" data-options="plain:true">新建模型</a>
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
			url: '<%=basePath%>activiti/modeler/modelerPage.action',
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
	 
	function operateFormatter(value, row, index) {
		return [
			'<div class="btn-group btn-group-xs" role="group">'+
			 	'<a type="button" class="btn btn-primary" href="<%=basePath%>activiti/modeler/openEdit.action?id='+row.id+'">编辑</a>'+
// 			 	'<a type="button" class="btn btn-danger" href="javascript:void(0)" onclick="delModeler(\''+row.id+'\')">删除</a>'+
			 	'<a type="button" class="btn btn-success" href="javascript:void(0)" onclick="deployModeler(\''+row.id+'\')">发布</a>'+
			'</div>'
		]
 	}
	
	
	function deployModeler(id){
		$.messager.confirm('确认','请确认是否要发布该流程吗？',function(r){    
		    if (r){    
		    	$.messager.progress({text:'发布中，请稍后...',modal:true});
		    	$.ajax({ 
					url:'<%=basePath %>activiti/modeler/deployModeler.action',
					type:'post',
					data:{"id":id},
					success: function(dataMap) {
						$.messager.progress('close');
						if(dataMap.resCode=="success"){
							$('#demoGrid').datagrid('reload');
						}
						$.messager.alert('提示',dataMap.resMsg);
					},
					error:function() {
						$.messager.progress('close');
						$.messager.alert('提示','请求失败!');
					}
				});
		    }    
		});  
	}
	function toadd(){
		$.messager.confirm('确认','新建模型会自动生成一条新的模型记录，是否继续？',function(r){    
		    if (r){    
		    	window.location.href="<%=basePath%>activiti/modeler/openEdit.action";
		    }    
		});  
	}
	function searchform(){
		$('#demoGrid').datagrid('load', {
		    'name': $('#searchId').val()
		});
	}
	function reload(){
		$('#searchId').textbox("setValue","");
		searchform();
	}
	</script>
</body>
</html>