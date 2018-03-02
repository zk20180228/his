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
<title>流程实例视图</title>
</head>
<body>
	<div class="container-fluid">
		<div class="input-group pull-right" style="width:200px;margin-top:5px;margin-bottom:5px;">
			<input id="searchId" type="text" class="form-control" placeholder="输入名称查询">
			<span class="input-group-btn">
				<button class="btn btn-default" type="button" href="javascript:void(0)" onclick="search()">查询</button>
			</span>
	    </div>
		<div>
			<table id="demoGrid"></table>
		</div>
	</div>
	<script type="text/javascript">
	$(function () {
		$('#demoGrid').bootstrapTable({
			method: 'post',
			striped: true,
			cache: false,
			sidePagination: "server",
			pagination: true,
			pageNumber:1,
			pageSize: 20,
			pageList: [20, 30, 50, 100],
			uniqueId: "id",
			strictSearch: false,
			showToggle:false,
			cardView: false,
			detailView: false,
			clickToSelect: true,
			singleSelect:false,
			height: $(window).height()-50,
			url: '<%=basePath%>activiti/processInstance/instanceViewPage.action',
			queryParams:queryParams,
			queryParamsType:'',
			contentType: "application/x-www-form-urlencoded",
			formatLoadingMessage: function () {  
				return "请稍等，正在加载中...";  
			},  
			formatNoMatches: function () {
				return '无符合条件的记录';  
			}, 
			columns: [{
				field: 'id',
				title: '编号',
				halign: 'center'
			},{
				field: 'name',
				title: '名称',
				halign: 'center'
			}, {
				field: 'processDefinitionId',
				title: '流程定义',
				halign: 'center'
			}, {
				field: 'activityId',
				title: '环节',
				halign: 'center'
			}, {
				field: 'suspensionState',
				title: '状态',
				align:'center',
				halign: 'center',
				formatter: suspended  
			}, {
				field: 'operation',
				title: '操作',
				align:'center',
				halign: 'center',
				formatter: operateFormatter  
			},]
		});
		$('#searchId').bind('keydown',function(event){  
			if(event.keyCode == "13"){  
				search();
			}  
		});  
	});
	 
	function suspended(value, row, index) {
		if(value=='1'){
			return ['<div class="btn-group btn-group-xs" role="group"><a type="button" class="btn btn-danger" href="javascript:void(0)" onclick="suspend(\''+row.id+'\')">挂起</a></div>'];
		}else{
			return ['<div class="btn-group btn-group-xs" role="group"><a type="button" class="btn btn-success" href="javascript:void(0)" onclick="active(\''+row.id+'\')">激活</a></div>'];
		}
 	}
	 
	function operateFormatter(value, row, index) {
		return [
			'<div class="btn-group btn-group-xs" role="group">'+
				'<a type="button" class="btn btn-danger" href="javascript:void(0)" onclick="del(\''+row.id+'\',0)">删除</a>'+
	          	'<a type="button" class="btn btn-primary" href="javascript:void(0)" onclick="migrate(\''+row.id+'\',1)">迁移</a>'+
// 	          	'<a type="button" class="btn btn-success" href="viewHistory.action?id=${item.id}">历史</a>'+
	          	'<a type="button" class="btn btn-danger" href="javascript:void(0)" onclick="del(\''+row.id+'\',1)">删除（包含历史）</a>'+
			'</div>'
		]
 	}
	function migrate(id){
		attWindow(id,650,400,'<%=basePath%>activiti/processInstance/migrateInputView.action?v='+Math.random());
	}
	function queryParams(params){
		return{
			rows:params.pageSize,
			page:params.pageNumber,
			name:$('#searchId').val()
		}
	}
	
	function search(){
		$('#demoGrid').bootstrapTable('refresh',{pageIndex:1});
	}
	
	function suspend(id){
		$.ajax({ 
			url:'<%=basePath%>activiti/processInstance/suspend.action',
			type:'post',
			data:{"id":id},
			success: function(dataMap) {
				if(dataMap.resCode=="success"){
					$('#demoGrid').bootstrapTable('refresh');
				}
				$.messager.alert('提示',dataMap.resMsg);
			},
			error:function() {
				$.messager.alert('提示','请求失败!');
			}
		});
	}
	
	function active(id){
		$.ajax({ 
			url:'<%=basePath%>activiti/processInstance/active.action',
			type:'post',
			data:{"id":id},
			success: function(dataMap) {
				if(dataMap.resCode=="success"){
					$('#demoGrid').bootstrapTable('refresh');
				}
				$.messager.alert('提示',dataMap.resMsg);
			},
			error:function() {
				$.messager.alert('提示','请求失败!');
			}
		});
	}
	
	function del(id,type){
		$.messager.confirm('确认','是否删除该流程实例？',function(r){
			if(r){
				$.ajax({ 
					url:'<%=basePath%>activiti/processInstance/del.action',
					type:'post',
					data:{"id":id,"type":type},
					success: function(dataMap) {
						$('#demoGrid').bootstrapTable('refresh');
						if(dataMap.resCode=="success"){
							$.messager.alert('提示',dataMap.resMsg);
						}
					},
					error:function() {
						$.messager.alert('提示','请求失败!');
					}
				});
			}
		});
		
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
	</script>
</body>
</html>