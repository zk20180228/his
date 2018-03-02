<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>知识库维护</title>
	<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
	<script type="text/javascript" src="<%=basePath%>ueditor1_4_3_2/ueditor.config.js"></script>
	<script type="text/javascript" src="<%=basePath%>ueditor1_4_3_2/ueditor.all.min.js"></script>
	<script type="text/javascript">
	//页面加载
	$(function(){
		trr('01');
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url: '<%=basePath %>emrs/konwledgeBase/queryKonws.action?menuAlias=${menuAlias}',
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			queryParams: {
				idd: '01'
			},
	    	onDblClickRow :function(rowIndex, rowData){
	    		AddOrShowEast('ViewForm','<%=basePath %>emrs/konwledgeBase/toKonwView.action?menuAlias=${menuAlias}&idd=' + rowData.id);
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
		})
		//知识库选项卡
		$('#tt').tabs({    
	    	border:false,    
	    	onSelect:function(title){    
	    		var tab = $('#tt').tabs('getSelected');
	    		var index = $('#tt').tabs('getTabIndex',tab);
	    		var lib = '0' + (index + 1);
	    		var eastpanel=$('#panelEast'); //获取右侧收缩面板
				if(eastpanel.length>0){
					$('#divLayout').layout('remove','east');
				}
				trr(lib);
	    		$('#list').datagrid('load',{
	    			idd: lib
				});
	    	}
		});
	})
	
	//知识库树
	function trr(lib){
		$('#tKonw').tree({
				url : '<%=basePath %>emrs/konwledgeBase/treeKonw.action?lib='+lib,
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
				},onLoadSuccess: function(node, data) {
					var root = $('#tKonw').tree('getRoot',node);
					$('#tKonw').tree('select', root.target); 
				},onClick : function(node) {//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					closeEast();
					var idd = node.id;
					var flag = node.attributes.flag;
					if(flag == '1'){
						return;
					}
					if(idd.length == 2){
						var tab = $('#tt').tabs('getSelected');
			    		var index = $('#tt').tabs('getTabIndex',tab);
			    		var lib = '0' + (index + 1);
			    		$('#list').datagrid('load',{
			    			idd: lib
						});
					}else{
						$('#list').datagrid('load',{
							idd: idd
						});
					}
				},onDblClick : function(node) {//双击节点
					var idd = node.id;
					var flag = node.attributes.flag;
					//若选中结点为知识库信息节点
					if(idd.toString().length == 2){
						return ;
					}
					if(flag == '0'){
						return ;
					}
					AddOrShowEast('ViewForm','<%=basePath %>emrs/konwledgeBase/toKonwView.action?menuAlias=${menuAlias}&idd=' + idd);
				},onContextMenu: function(e, node){//右击节点
					e.preventDefault();
					var idd = node.id;
					// 选中节点
					$('#tKonw').tree('select', node.target);
					//若选中结点为知识库信息节点将修改于删除按钮隐藏
					if(idd.toString().length == 2){
						$('#edit').hide();
						$('#remove').hide();
					}else{
						$('#edit').show();
						$('#remove').show();
					}
					if(node.attributes.flag == '0'){
						$('#add').show();
						$('#addLeaf').show();
					}else{
						$('#add').hide();
						$('#addLeaf').hide();
					}
					// 显示快捷菜单
					$('#mm').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
				}

		});
	}
	function refresh() {//刷新树
		closeEast();
		$('#tKonw').tree('reload');
	}
	function expandAll(){//展开树
		closeEast();
		$('#tKonw').tree('expandAll');
	}
	function collapseAll(){//关闭树
		closeEast();
		$('#tKonw').tree('collapseAll');
	}
	function add(flag){//添加
		if(flag == 0){
			AddOrShowEast('AddForm','<%=basePath %>emrs/konwledgeBase/toAddKonwClassView.action?menuAlias=${menuAlias}');
		}else{
			Adddilog("添加知识",'<%=basePath %>emrs/konwledgeBase/toAddView.action?menuAlias=${menuAlias}','87%','88%');
		}
		
	
	}
	function edit(){//修改
		var node = $('#tKonw').tree('getSelected');
		var idd = node.id;
		var flag = node.attributes.flag;
		if(flag == '0'){
			AddOrShowEast('EditForm','<%=basePath %>emrs/konwledgeBase/toEditKonwClassView.action?menuAlias=${menuAlias}&idd=' + idd);
		}else{
			Adddilog("编辑知识",'<%=basePath %>emrs/konwledgeBase/toEditView.action?menuAlias=${menuAlias}&idd=' + idd,'87%','88%');
		}
		
		
	}
	function del(){//删除节点
		var node = $('#tKonw').tree('getSelected');
		var idd = node.id;
		$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
			if (res){
				$.messager.progress({text:'删除中，请稍后...',modal:true});
				$.ajax({
					url: '<%=basePath %>emrs/konwledgeBase/remove.action?idd='+idd,
					type:'post',
					success: function() {
						$.messager.progress('close');
						$.messager.alert("提示","删除成功");
						$('#tKonw').tree('reload');
						$('#list').datagrid('reload');
					},
					error: function(){
						$.messager.progress('close');
						$.messager.alert("提示","失败");
					}
				});
			}
    	});
	}
	/**
	 * 动态添加LayOut
	 * @param title 标签名称
	 * @param url 跳转路径
	 */
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
					width : 680,
					split : true,
					href : url,
					closable : true,
					border:false
				})
			}
		}
		/* 
		 * 关闭界面
		 */
			function closeLayout(){
				$('#konwWins').dialog('close'); 
				$('#tKonw').tree('reload');
			}
			/* 
			 * 关闭界面
			 */
				function clos(){
					$('#divLayout').layout('remove','east');
					$('#tKonw').tree('reload');
				}
		//关闭右容器
		function closeEast(){
			var eastpanel=$('#panelEast'); //获取右侧收缩面板
			if(eastpanel.length>0){
				$('#divLayout').layout('remove','east');
			}
		}
		//条件查询
		function searchFrom(){
			var queryName =  $('#queryName').val();
			}
		//加载模式窗口
		function Adddilog(title, url, width, height) {
			$('#konwWins').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true,
			    onClose: function(){
			    	UE.getEditor('editor').destroy();
			    }
			   });    
		}
	</script> 
	<style type="text/css">
		table.honry-table .honry-lable{
			border-left:0;
		}
		.panel-header{
			border-top:0;
		}
	</style>
</head>
<body class="easyui-layout"> 
<!-- 知识库维护 -->
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%">   
  		<div data-options="region:'north',split:false,border:false" style="width:100%;height:auto;overflow: hidden;">
   		<div id="tt" class="easyui-tabs" style="width:100%;height:29px;">
			<div title="治愈好转标准库" id="01" >
    		</div>   
   			<div title="医疗护理技术操作常规" id="02" >   
    		</div>   
   			<div title="药品库" id="03" style="">   
    		</div>   
    		<div title="法律规范库" id="04" >   
    		</div>   
    		<div title="鉴别诊断知识库" id="05" >   
    		</div>   
    		<div title="诊疗计划知识库" id="06" >   
    		</div>   
		</div>   
   		</div>  
    	<div data-options="region:'center',split:true,border:true" style="height:85%;width:50%;overflow: hidden;border-top:0">
    	<table id="list" style="width:100%;" data-options="fit: 'true',url:'<%=basePath %>emrs/konwledgeBase/queryKonws.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				<thead>
					<tr>
						<th data-options="field:'konwCode',width:'16%',align:'center'">编码</th>
						<th data-options="field:'konwName',width:'16%',align:'center'">名称</th>
						<th data-options="field:'konwPinYin',width:'16%',align:'center'">拼音码</th>
						<th data-options="field:'konwWb',width:'16%',align:'center'">五笔码</th>
						<th data-options="field:'konwInputCode',width:'16%',align:'center'">自定义码</th>
						<th data-options="field:'konwOrder',width:'16%',align:'center'">排序号</th>
					</tr>
				</thead>
			</table>
   		</div>
    	<!-- 知识树 --> 
    	<div id="p" data-options="region:'west',tools:'#toolSMId',collapsible: false,split:true" title="知识库" style="width:18%; height:100%;padding: 0px;" >
	    	<div id="toolSMId">
		    	<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
				<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
			</div>
			<div id="treeKonw" style="width: 100%; height: 85%; overflow: auto;">
				<ul id="tKonw">数据加载中...</ul>
			</div>
		</div> 
		<!-- 右键菜单 -->
		<div id="mm" class="easyui-menu" style="width:120px;">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<div id="add" onclick="add(0)" data-options="iconCls:'icon-add'">添加分类</div>
			<div id="addLeaf" onclick="add(1)" data-options="iconCls:'icon-add'">添加知识</div>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<div id="edit" onclick="edit()" data-options="iconCls:'icon-edit'">修改</div>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">
			<div id="remove" onclick="del()" data-options="iconCls:'icon-remove'">删除</div>
		</shiro:hasPermission>
		</div>
		
    	
	</div>  
	<div id="konwWins"></div>
</body> 
</html>