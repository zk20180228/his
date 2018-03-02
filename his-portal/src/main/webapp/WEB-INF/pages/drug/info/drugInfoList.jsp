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
	<title>药品管理</title>
	
<style type="text/css">
.window .panel-header .panel-tool a{
  	  	background-color: red;	
	}
</style>
	<script type="text/javascript">
	//加载页面
	$(function() {
		var id = "${id}"; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
		    pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				var row = $('#list').datagrid('getSelected'); //获取当前选中行     
                if(row){
               	  Adddilog("查看药品","<c:url value='/drug/info/viewDrugInfo.action'/>?id="+row.id);
			   	}
			},onLoadSuccess:function(row, data){
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
		//加载药品类别树
	   	$('#tDrug').tree({  
		    url:"<c:url value='/drug/info/drugInfoTreeBytype.action'/>",
		    method:'get',
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if(node.id=='0'&&node.children.length>0){
					s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
				return s;
			},onClick: function(node){//点击节点
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				$('#list').datagrid('load', {
					drugType: node.id
				});
			}
		}); 
		bindEnterEvent('name',searchFrom,'easyui');
	});
	function add(){
		Adddilog("编辑药品","<c:url value='/drug/info/addOrUpdateDrugInfoUrl.action'/>");
	}  
	
	function edit(){
		var row = $('#list').datagrid('getSelected'); //获取当前选中行     
            if(row){
           		Adddilog("编辑药品","<c:url value='/drug/info/addOrUpdateDrugInfoUrl.action'/>?id="+row.id);
			}
	}
	
	function del(){
		 //选中要删除的行
                    var rows = $('#list').datagrid('getChecked');
                	if (rows.length > 0) {//选中几行的话触发事件	                        
					 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
							if (res){
								$.messager.progress({text:'删除中，请稍后...',modal:true});	// 显示进度条
								var ids = '';
								for(var i=0; i<rows.length; i++){
									if(ids!=''){
										ids += ',';
									}
									ids += rows[i].id;
								};
								$.ajax({
									url: "<c:url value='/drug/info/deleteDrugInfo.action'/>?ids="+ids,
									type:'post',
									success: function(data) {
										$.messager.progress('close');
										$.messager.alert('提示',data.resMsg);
										if(data.resCode=='success'){
											$('#list').datagrid('reload');
										}
										setTimeout(function(){
											$(".messager-body").window('close');
										},3500);
										rows.length = 0;
									}
									});
							}
                    	});
                	}
	}
	
	function reload(){
		//实现刷新栏目中的数据
		$('#list').datagrid('reload');
	}
     //加载dialog
 	function Adddilog(title, url) {
 		$('#add').dialog({    
 		    title: title,    
 		    width: '95%',    
 		    height:'77%',
 		    top:'15',
 		    closed: false,    
 		    cache: false,    
 		    href: url,    
 		    modal: true   
 		   });    
 	}
     
	
	//打开dialog
	function openDialog() {
		$('#add').dialog('open'); 
	}
	//关闭dialog
	function closeDialog() {
		$('#add').dialog('close');  
	}
	//查询
	function searchFrom(){
	    var name =	$.trim($('#name').textbox('getValue'));
	    $('#list').datagrid('load', {
			name: name
		});
	}
	function KeyDown() {  
   		if (event.keyCode == 13) {  
	        event.returnValue=false;  
	        event.cancel = true;  
	        searchFrom();  
	    }  
	} 
	
	// 药品列表查询重置
	function searchReload() {
		$('#name').textbox('setValue','');
		searchFrom();
	}
</script>
</head>
<body style="margin: 0px;padding: 0px;">
	 <div id="divLayout"  class="easyui-layout" data-options="fit:true" style="width: 100%; height: 100%; overflow-y: auto;">
		<div id="p" data-options="region:'west',split:true" style="width:12%;border-top:0">
			<ul id="tDrug"></ul>
		</div>
		<div data-options="region:'center',split:false,iconCls:'icon-book'" style="width:85%;border-top:0">
			<div id="el" class="easyui-layout" data-options="fit:true">   
				<div data-options="region:'north',split:false,border:false" style="width:100%;height: 40px">
					<table id="search"  style="width: 100%;padding: 5px 0px 5px 0px;">
						<tr>
							<td>
								查询条件：<input class="easyui-textbox" name="name" id="name"  onkeydown="KeyDown()" data-options="prompt:'药品名称,拼音,五笔,自定义'" style="width:220px" />
								<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</shiro:hasPermission>
								<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
				</div>   
				<div data-options="region:'center'" style="width:100%;height:100%;border-left:0">
					<table id="list" style="width: 100%;"id="list" data-options="url:'${pageContext.request.contextPath}/drug/info/queryDrugInfo.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',fit:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true" ></th>
								<th data-options="field:'name'" style="width:18%">名称</th>
								<th data-options="field:'drugNamepinyin'" style="width:12%" >名称拼音码</th>
								<th data-options="field:'drugNamewb'" style="width:12%">名称五笔码</th>
								<th data-options="field:'drugNameinputcode'" >名称自定义码</th>
								<th data-options="field:'drugCommonname'" style="width:12%">通用名称</th>
								<th data-options="field:'drugCnamepinyin'" style="width:8%">通用名称拼音码</th>
								<th data-options="field:'drugCnamewb'" style="width:8%">通用名称五笔码</th>
								<th data-options="field:'drugCnameinputcode'" style="width:12%">通用名称自定义码</th>
								<th data-options="field:'drugBiddingcode'" style="width:8%" >招标识别码</th>
							</tr>
						</thead>
					</table>
				</div> 
			</div>
	</div>
	</div>
	<div id="add"></div>
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
</body>
</html>