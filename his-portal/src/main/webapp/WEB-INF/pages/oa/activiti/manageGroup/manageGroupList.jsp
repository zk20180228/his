<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>决策组维护</title>
<%@ include file="/common/metas.jsp"%>
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
							<input class="easyui-textbox"  name="nameSerc" id="nameSerc" data-options="prompt:'名称,代码,拼音码,五笔码'" style="width: 200px;" />
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</shiro:hasPermission>
							<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</td>
					</tr>
				</table>
			</div>			 
			<div style="width:100%;height:94%;">                                                                                                                  
				<table id="list" data-options="method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>	
							<th data-options="field:'id',hidden:true">Id</th>					
							<th data-options="field:'type',hidden:true" style="width:10%">编码类型</th>
							<th data-options="field:'name'" style="width:10%">名称</th>	
							<th data-options="field:'encode'" style="width:7%">代码</th>
							<th data-options="field:'sortId',hidden:true" style="width:8%">顺序号</th> 
							<th data-options="field:'validState',formatter:stopformatter" style="width:5%">停用状态</th> 				
							<th data-options="field:'pinyin'" style="width:8%">拼音码</th>  
							<th data-options="field:'wb'" style="width:8%">五笔码</th> 
							<th data-options="field:'inputCode'" style="width:8%">自定义码</th> 
							<th data-options="field:'mark'" style="width:8%">备注</th>  
							<th data-options="field:'operCode'" style="width:12%">操作员</th> 
							<th data-options="field:'operDate'" style="width:12%">操作时间</th> 
							<th data-options="field:'createTime',hidden:true" style="width:12%">创建时间</th> 
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	<div id="toolbarId">
	<shiro:hasPermission name="${menuAlias}:function:add">
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:edit">
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:delete">		
		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="roleaddUserdiv"></div>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
	<script type="text/javascript">

	//加载页面
	$(function(){
		var id='${id}'; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
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
		bindEnterAndBlackEvent('nameSerc', searchFrom,'easyui');
	});
	
	$('#list').datagrid({
		url:'<%=basePath %>oa/manageGroup/queryManageGroup.action',
		onDblClickRow : function(rowIndex, rowData) {//双击查看
			var row = $('#list').datagrid('getSelected'); //获取当前选中行    
            if(row){
           		AddOrShowEast('EditForm','<%=basePath %>oa/manageGroup/toManageGroupView.action?dictionary.id='+rowData.id);
		   	}
		},
		onBeforeLoad:function(param){
			$(this).datagrid('uncheckAll');
		},
		onClickRow: function(row){							
			var eastpanel=$('#panelEast'); //获取右侧收缩面板
			if(eastpanel.length>0){ //判断右侧收缩面板是否存在				
				$('#divLayout').layout('remove','east');			
			}
		}	                
	});
	function stopformatter(value,row,index){		
		if(value==1){
			return '停用';
		}
		else {		
			return '在用';
		}	
	}
	
		//弹出添加编辑区域
		function add(){
			AddOrShowEast('EditForm','<%=basePath %>oa/manageGroup/addManageGroup.action?type=manageGroup');			
		}
		//弹出修改编辑区域
		function edit(){
			var row = $('#list').datagrid('getSelected'); //获取当前选中行         
            	if(row){            		
            		AddOrShowEast('EditForm','<%=basePath %>oa/manageGroup/editManageGroup.action?dictionary.id='+row.id);
				}else{
					$.messager.alert('提示','请选择一条记录!');
	        		return;
				}
		}
		//删除
		function del(){		
            //选中要删除的行
		    var iid = $('#list').datagrid('getChecked');
		    if (iid.length > 0) {//选中几行的话触发事件	                        
				$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
					if (res){
						 $.messager.progress({text:'删除中，请稍后...',modal:true});
						var ids = '';
						for(var i=0; i<iid.length; i++){
							if(ids!=''){
								ids += ',';
							}
							ids += iid[i].id;
						};
						$.ajax({
							url: '<%=basePath %>oa/manageGroup/removeManageGroup.action',
							data:'dictionary.id='+ids,
							type:'post',
							success: function() {
								 $.messager.progress('close');
								 $.messager.alert('提示','删除成功！');	
								$('#list').datagrid('reload');
							}
						});										
					}
				});
		    }else{
                $.messager.alert('操作提示', '请选择要删除的信息！'); 
            }
		}
		
		function reload(){
			//实现刷新栏目中的数据
			$('#list').datagrid('reload');
		}
	
		//查询
		function searchFrom(){
		    var name =	$.trim($('#nameSerc').textbox('getText'));
		    $('#list').datagrid('load', {
		    	'dictionary.name': name
			});
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
		
	 	// 列表查询重置
		function searchReload() {
			$('#nameSerc').textbox('setValue','');
			searchFrom();
		}
		/**
	 	* 关闭查看窗口
	    */
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
	</script>
</body>
</html>