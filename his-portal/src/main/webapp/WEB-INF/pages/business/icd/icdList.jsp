<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>ICD管理</title>
<%@ include file="/common/metas.jsp"%>


<style type="text/css">
.window .panel-header .panel-tool a{
  	  	background-color: red;	
	}
</style>

<script type="text/javascript">
	var disList = "";
	$(function(){
		var id='${id}'; //存储数据ID
		$.ajax({
			url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
			data:{"type" : "diseasetype"},
			type:'post',
			success: function(disData) {
				disList = disData;
			}
		});	
		//添加datagrid事件及分页
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url : "<c:url value='/baseinfo/icd/queryIcd.action'/>",
			queryParams:{menuAlias:'${menuAlias}'},
			onDblClickRow: function (rowIndex, rowData) {//双击查看
				if(getIdUtil('#list').length!=0){
			   	    Adddilog("查看","<c:url value='/baseinfo/icd/viewIcd.action'/>?id="+getIdUtil('#list')+"&type="+$('#type').val());
			   	}
			},
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
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
		bindEnterEvent('queryName',searchFrom,'easyui');
		$('#tDtree').tree({
			onLoadSuccess : function(node, data) {
					$('#tDtree').tree('select',$('#tDtree').tree('find', 1).target);
			},
			onSelect:function(data){
				if(data.id=='0'){
					$('#type').val('0');
				}
				if(data.id=='1'){
					$('#type').val('1');
				}
				if(data.id=='2'){
					$('#type').val('2');
					
				}
				if(data.id=='3'){
					$('#type').val('3');
				}
				$('#list').datagrid('load',{
					type:$('#type').val()
				});
			},
			data: [{
				id:'0',
				text: 'ICD分类',
				state: 'open',
				children: [{
					id:'1',
					text: 'ICD10'
				},{
					id:'2',
					text: 'ICD9'
				},{
					id:'3',
					text: 'ICDOperation'
				}]
			}],
			onClick : function(node) {
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			}
		});
	});
	function add(){
		if($('#type').val()==''|| $('#type').val()==0){
			$.messager.alert('提示信息','请您选择ICD分类!');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}else{
			Adddilog("添加","<c:url value='/baseinfo/icd/addIcd.action'/>?type="+$('#type').val());
		}
	}
	//修改
	function edit(){
		var row = $('#list').datagrid('getSelected'); //获取当前选中行                        
	       if(row != null){
	       	Adddilog("修改","<c:url value='/baseinfo/icd/editIcd.action'/>?id="+row.id+"&type="+$('#type').val());
		}else{
   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
   		}
	}
		
	function del(){
		var rows = $('#list').datagrid('getChecked');
	    if (rows.length > 0) {//选中几行的话触发事件	                        
			 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
					if (res){
						var ids = '';
						for(var i=0; i<rows.length; i++){
							if(ids!=''){
								ids += ',';
							}
							ids += rows[i].id;
						};
						$.ajax({
							url: "<c:url value='/baseinfo/icd/delIcd.action'/>?id="+ids+"&type="+$('#type').val(),
							type:'post',
							success: function(data) {
								$.messager.alert('提示',data.resMsg);
								if(data.resCode=='success'){
									reload();
								}
							}
						});
					}
	             });
	     }else{
	    	 $.messager.alert('提示信息','请选择要删除的信息！');
	    	 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	     }
	}
		
	function reload(){
		//实现刷新栏目中的数据
		 $('#list').datagrid('reload');
	}

	//查询
	function searchFrom() {
		var queryName = $.trim($('#queryName').val());
		$('#list').datagrid('load', {
			name : queryName,
			type:$('#type').val()
		});
	}
	
	//适用性别
	function functionSex(value,row,index){
		if(value){
			if(value == 'A'){
				return '全部';
			}else if(value == 'M'){
				return '男';
			}else if(value == 'F'){
				return '女';
			}else if(value == 'U'){
				return '未知';
			}
		}
	}	
		
	//疾病分类
	function functionDis(value,row,index){
		for(var i=0;i<disList.length;i++){
			if(value==disList[i].encode){
				return disList[i].name;
			}
		}
	}	

	//加载dialog
	function Adddilog(title, url) {
		$('#add').dialog({    
		    title: title,    
		    width: '800',    
		    height:'650',    
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
	
	// 药品列表查询重置
	function searchReload() {
		$('#queryName').textbox('setValue','');
		searchFrom();
	}
</script>
<style type="text/css">
	#divLayout .panel-header{
		border-top:0
	}
</style>
</head>
<body style="margin: 0px;padding: 0px">
	<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
		<!-- 树状 -->
		<div id="p" data-options="region:'west',split:true" title="ICD分类管理" style="width: 15%; height:100%;">
			<ul id="tDtree"></ul>
		</div>
		<div data-options="region:'center'" style="width: 85%;height: 100%;border-top:0">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false" style="height:35px">
					<table style="width: 100%;margin-top:2px;margin-bottom:auto;">
						<tr>
							<td style="width: 500px;">
								<span style="font-size: 14" class="icd">&nbsp;过滤条件：</span>
								<input class="easyui-textbox " id="queryName" name="queryName" onkeydown="KeyDown(0)" data-options="prompt:'诊断码,名称,拼音,五笔,自定义'" style="width: 360px;" />
								<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center',border:false">
					<input type="hidden" name="type" id="type" />
					<table id="list" style="width:100%;height:90%;" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true,width:'5%'"></th>
								<th data-options="field:'code' ,width : '7%' ">诊断码</th>
								<th data-options="field:'name' ,width : '12%' ">诊断名称</th>
								<th data-options="field:'pinyin' ,width : '7%' ">拼音码</th>
								<th data-options="field:'wb' ,width : '7%' ">五笔码</th>
								<th data-options="field:'inputcode' ,width : '8%' ">自定义码</th>
								<th data-options="field:'sex' ,width : '8%' ,formatter: functionSex">适用性别</th>
								<th data-options="field:'diseasetype' ,width : '8%' ,formatter: functionDis">疾病分类</th>
								<th data-options="field:'isThirty',width : '8%' ,formatter: function(value,row,index){var text = '';switch (value){case 0:text = '否';break;case 1:text = '是';break;default:text='否';break;}return text;}">是否30中疾病</th>
								<th data-options="field:'isCom',width : '8%' ,formatter: function(value,row,index){var text = '';switch (value){case 0:text = '否';break;case 1:text = '是';break;default:text='否';break;}return text;}">是否传染病</th>
								<th data-options="field:'isTumor',width : '8%' ,formatter: function(value,row,index){var text = '';switch (value){case 0:text = '否';break;case 1:text = '是';break;default:text='否';break;}return text;}">是否肿瘤</th>
								<th data-options="field:'isTcm',formatter: function(value,row,index){var text = '';switch (value){case 0:text = '否';break;case 1:text = '是';break;default:text='否';break;}return text;}">是否中医诊断 </th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="add"></div>
	<!-- 操作选项 -->
	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" 
				data-options="iconCls:'icon-add',plain:true">添加</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit',plain:true">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">
			<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton"
				data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton"
			data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
</body>
</html>