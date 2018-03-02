<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>电子病历模板维护</title>
	<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp"%>
	<style type="text/css">
		.window .panel-header .panel-tool a{
  	  		background-color: red;	
		}
	</style>
	<script type="text/javascript">
	var node;//选中的节点
	var level;//选中节点的级别
	var id;//选中节点的id
	var tempErtypeList = "";
	//加载页面
	$(function(){
		$.ajax({
			url: "<%=basePath %>emrs/emrTemplate/tempErtypeFrmatter.action",
				type:'post',
				success: function(data) {
					tempErtypeList = data;
			}
		});
		$('#tDt').tree({
			url : '<%=basePath %>emrs/emrTemplate/treeErType.action?menuAlias=${menuAlias}',
			method:'get',
			lines : true,
			cache : false,
			animate : true,
			formatter : function(node) {//统计节点总数
				var s = node.text;
				if (node.children.length > 0) {
					s += '&nbsp;<span style=\'color:blue\'>('
						+ node.children.length + ')</span>';
				}
				return s;
			},
			onLoadSuccess: function(node, data) {
				var root = $('#tDt').tree('getRoot',node);
				$('#tDt').tree('select', root.target); 
			},
			onClick : function(node) {//点击节点
				
				 var children = $(this).tree('getChildren', node.target);//判断是否有子节点
				 if(children.length>0){
					 $(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				 }else{
					 var level = node.attributes.level;
						var erType = "";
						if(level == '0'){
							$('#list').datagrid('showColumn','tempErtype');
							erType = 'root';
						}else if(level == '1'){
							$('#list').datagrid('showColumn','tempErtype');
							var nodes = $('#tDt').tree('getChildren',node.target);
							for(var i = 0;i < nodes.length-1; i++){
								erType += nodes[i].id + ",";
							}
							erType += nodes[nodes.length-1].id;
						}else if(level == '2'){
							$('#list').datagrid('hideColumn','tempErtype');
							erType=node.id;
						}
						var tab = $('#tt').tabs('getSelected');
						var index = $('#tt').tabs('getTabIndex',tab);
						unsece();
						$('#list').datagrid('load',{erType: erType,tempChkflg: index}); 
				 }
				
			}
		});
		
		//知识库选项卡
		$('#tt').tabs({    
	    	border:false,    
	    	onSelect:function(title){    
	    		var tab = $('#tt').tabs('getSelected');
	    		var index = $('#tt').tabs('getTabIndex',tab);
	    		if(index == 1){
	    			$('#edit').hide();
	    		}else{
	    			$('#edit').show();
	    		}
	    		if(index == 2){
	    			$('#add').hide();
	    		}else{
	    			$('#add').show();
	    		}
				var node = $('#tDt').tree('getSelected');
				var erType = getErType(node);
				unsece();
	    		$('#list').datagrid('load',{
	    			erType: erType,
	    			tempChkflg: index
				});
	    	}     
		});
		
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url: '<%=basePath %>emrs/emrTemplate/queryTemplates.action?menuAlias=${menuAlias}',
			onLoadSuccess : function(data){
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
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
				var tab = $('#tt').tabs('getSelected');
	    		var index = $('#tt').tabs('getTabIndex',tab);
			},
			queryParams: {
				erType: 'root',
				tempChkflg: 0
			},
			onDblClickRow: function(rowIndex,rowData){
				Adddilog('查看模板',"<%=basePath %>emrs/emrTemplate/toTemplateView.action?ids="+rowData.id,'820','600');
			}
		});	
	});
	
	//添加方法
	function add(){
		node = $('#tDt').tree('getSelected');
		level = node.attributes.level;
		if(level == '2'){
			var tab = $('#tt').tabs('getSelected');
			var index = $('#tt').tabs('getTabIndex',tab);
			Adddilog("添加模板",'<%=basePath %>emrs/emrTemplate/toAddView.action?menuAlias=${menuAlias}&erType='+node.id+'&tempChkflg='+index,'820px','300px');
		}else{
			$.messager.alert("操作提示","请选择要添加模板的类别！");
			setTimeout(function(){$(".messager-body").window('close')},3500);
			return;
		}
	}
	function refresh() {//刷新树
		$('#tDt').tree('reload');
	}
	function expandAll(){//展开树
		$('#tDt').tree('expandAll');
	}
	function collapseAll(){//关闭树
		$('#tDt').tree('collapseAll');
	}
	//修改方法
	function edit(){
		var row = $("#list").datagrid("getSelected");  
	    if(row == undefined || row == null){
	    	$.messager.alert("操作提示","请选择一条记录！");
	    	setTimeout(function(){$(".messager-body").window('close')},3500);
	       	return null;
	    }else{ 
	    	Adddilog("修改模板",'<%=basePath %>emrs/emrTemplate/toEditView.action?menuAlias=${menuAlias}&ids='+row.id,'820px','300px');
		}
	}
	
	//删除方法
	function del(){
		 //选中要删除的行
               var rows = $('#list').datagrid('getChecked');
               if(rows != undefined || rows != null){//选中行的话触发事件	                        
				 	$.messager.confirm('确认', '确定要删除选中模板吗?', function(res){//提示是否删除
						if (res){
							var ids = '';
							for(var i=0; i<rows.length; i++){
								if(ids!=''){
									ids += ',';
								}
								ids += rows[i].id;
							};
							$.messager.progress({text:'保存中，请稍后...',modal:true});
							$.ajax({
								url: '<%=basePath %>emrs/emrTemplate/remove.action?ids='+ids,
								type:'post',
								success: function(data) {
									$.messager.progress('close');
									if(data == 'success'){
										$.messager.alert('提示','删除成功');
										load();
									}else {
										$.messager.alert('提示','模板'+data+'正在使用，不能被删除！！');
										setTimeout(function(){$(".messager-body").window('close')},3500);
									}
									rows.length = 0;
								}
							});
						}
               		});
	           }else{
	           	$.messager.alert('提示','请选择要删除的模板');
	           	setTimeout(function(){$(".messager-body").window('close')},3500);
	           }
			}
	
	function load(){
		var tab = $('#tt').tabs('getSelected');
		var index = $('#tt').tabs('getTabIndex',tab);
		var node = $('#tDt').tree('getSelected');
		var erType = getErType(node);
		unsece();
		$('#list').datagrid('load',{erType: erType,tempChkflg: index});
	}
	//datagrid取消选中选择
	function unsece(){
		$('#list').datagrid('unselectAll');
		$('#list').datagrid('uncheckAll');
	}
	//获得病历分类
	function getErType(node){
		var erType ='';
		if(node != null){
			var level = node.attributes.level;
			if(level == '0'){
				erType = 'root';
			}
			if(level == '1'){
				var nodes = $('#tDt').tree('getChildren',node.target);
				for(var i = 0;i < nodes.length-1; i++){
					erType += nodes[i].id + ",";
				}
				erType += nodes[nodes.length-1].id;
			}
			if(level == '2'){
				erType = node.id;
			}
   		}
		return erType;
	}
	//渲染模板分类,0通用1科室2个人
	function formatType(val,row,index){
		if(val == '0'){
			return '通用'
		}
		if(val == '1'){
			return '科室'
		}
		if(val == '2'){
			return '个人'
		}
	}
	//书写类型  0不限次书写1仅首次单次书写2单次书写
	function formatWritetype(val,row,index){
		if(val == '0'){
			return '不限次书写'
		}
		if(val == '1'){
			return '仅首次单次书写'
		}
		if(val == '2'){
			return '单次书写'
		}
	}
	//渲染审签标志 0代签1已审核2退回
	function formatChkflg(val,row,index){
		if(val == '0'){
			return '代签'
		}
		if(val == '1'){
			return '已审核'
		}
		if(val == '2'){
			return '退回'
		}
	}
	
	/**
	 * 动态添加LayOut
	 * @param title 标签名称
	 * @param url 跳转路径
	 */
		function AddOrShowCenter(title, url) {
			var centerpanel=$('#panelCenter'); //获取右侧收缩面板
			if(centerpanel.length>0){ //判断右侧收缩面板是否存在
				//重新装载右侧面板
		   		$('#divLayout').layout('panel','center').panel({
                       href:url 
                });
			}else{//打开新面板
				$('#divLayout').layout('add', {
					region : 'center',
					width : "100%",
					split : false,
					href : url,
					collapsible : false
				});
			}
		}
	
	//加载模式窗口
	function Adddilog(title, url, width, height) {
		$('#temWins').dialog({    
		    title: title,    
		    width: width,    
		    height: height,    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true,
		   });    
	}
	
	//渲染类别
	function formatErtype(val,row,index){
		for(var i = 0;i < tempErtypeList.length;i++){
			if(val == tempErtypeList[i].encode){
				return tempErtypeList[i].name;
			}
		}
	}	
</script>
</head>
<body>
<!-- 电子病历模板维护 -->
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;" fit=true>
		<!-- 病历分类树 --> 
		<div id="p" data-options="region:'west',tools:'#toolSMId',split:true" title="病历分类" style="width: 15%; padding: 10px">
	    	<div id="toolSMId">
						<a href="javascript:void(0)" class="icon-reload" onclick="refresh()" ></a>
						<a href="javascript:void(0)" class="icon-fold" onclick="collapseAll()"></a>
						<a href="javascript:void(0)" class="icon-open" onclick="expandAll()" ></a>
			</div> 
			<div id="treeDiv" style="width: 100%; height: 100%; overflow-y: auto;">
	    		<ul id="tDt">数据加载中...</ul>
	    	</div>
		</div>
		<div id="panelCenter" data-options="region:'center',split:true,border:false" style="height:100%;width:85%;overflow: hidden;">
			<div id="divLayout" class="easyui-layout" fit=true>
				<div data-options="region:'north',split:false,border:false" style="width:100%;height:29px;">
			   		<div id="tt" class="easyui-tabs" style="height:29px;">
						<div title="未审核" id="0" >
			    		</div>   
			   			<div title="已通过审核" id="1" >
			    		</div>   
			   			<div title="未通过审核" id="2" >
			    		</div>   
					</div>   
		   		</div>  
				<div data-options="region:'center',split:false,border:true" style="width: 100%;height: 100%;border-top:0">
		    		<table id="list" style="width:100%;" data-options="fit:true,url:'<%=basePath %>emrs/emrTemplate/queryTemplates.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
						<thead>
							<tr>
								<th data-options="checkbox:true,field : 'ck'"></th>
								<th data-options="field:'tempCode',width:'14%',align:'center'">编码</th>
								<th data-options="field:'tempName',width:'14%',align:'center'">名称</th>
								<th data-options="field:'tempErtype',width:'14%',align:'center',formatter:formatErtype">病历类型</th>
								<th data-options="field:'inputcode',width:'14%',align:'center'">自定义码</th>
								<th data-options="field:'tempType',width:'14%',align:'center',formatter:formatType">模板分类</th>
								<th data-options="field:'tempDiag',width:'14%',align:'center'">诊断名称</th>
								<th data-options="field:'tempWritetype',width:'14%',align:'center',formatter:formatWritetype">书写类别</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
   		</div>
   		<div id="toolbarId">
			<shiro:hasPermission name="${menuAlias}:function:add">
				<span id="add" style="display: '';"><a href="javascript:void(0)" onclick="add()" id="add" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:edit">
				<span id="edit" style="display: '';"><a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:delete">
				<a href="javascript:void(0)" onclick="del()" id="delete" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
		</div>
	</div>
	<div id="temWins"></div>
</body>
