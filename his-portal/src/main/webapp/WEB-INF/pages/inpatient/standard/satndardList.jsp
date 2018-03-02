<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title></title>

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
		<div data-options="region:'west',tools:'#toolSMId',title:'标准查询',split:true" style="width: 14%;">
			<div id="toolSMId">
				<a href="javascript:void(0)" class="icon-reload" onclick="refresh()" ></a>
				<a href="javascript:void(0)" class="icon-fold" onclick="collapseAll()"></a>
				<a href="javascript:void(0)" class="icon-open" onclick="expandAll()" ></a>
			</div> 
			<div style="height: 30px;padding: 5px 0px 5px 0px">
				<input class="easyui-textbox" id="searchTreeInpId" data-options="prompt:'分类名称'" style="width: 120px;"/>
				<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a>
				</shiro:hasPermission>
			</div>
			<div id="treeDiv" style="width: 100%; height: 90%; overflow-y: auto;">
    			<ul id="tDt"></ul>
    		</div>
		</div>
		<div data-options="region:'center',border:true" class="deleteBorder" style="border-top:0">
			<div id="divLayout" class="easyui-layout" data-options="fit:true">
				<div data-options="region:'center',border:true" class="deleteBorder" style="border-top:0">
					<div style="width:100%;height:94%;">                                                                                                                  
						<table id="list" data-options="method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',title:'标准列表',fit:true">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true" ></th>	
									<th data-options="field:'id',hidden:true">Id</th>					
									<th data-options="field:'standCode'" style="width:8%">标准代码</th>  
									<th data-options="field:'standName'" style="width:10%">标准名称</th>	
									<th data-options="field:'standVersionNo'" style="width:10%">版本号</th>	
									<th data-options="field:'inputCode'" style="width:7%">输入码</th>
									<th data-options="field:'inputCodeWb'" style="width:7%">五笔码</th>
									<th data-options="field:'customCode'" style="width:7%">自定义码</th>
									<th data-options="field:'stop_flg',formatter:stopformatter" style="width:5%">停用标志</th> 				
									<th data-options="field:'createUserName'" style="width:7%">创建人</th>
									<th data-options="field:'createTime'" style="width:7%">创建时间</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
				<div data-options="region:'south',border:true" style="height:65%;" class="deleteBorder" style="border-top:0">
					<div style="width:100%;height:94%;">                                                                                                                  
						<table id="detialList" data-options="method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId2',title:'标准明细列表',fit:true">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true" ></th>	
									<th data-options="field:'id',hidden:true">Id</th>					
									<th data-options="field:'assessName'" style="width:70%">标准</th>  
									<th data-options="field:'assessValue'" style="width:10%">值</th>	
									<th data-options="field:'flag',formatter:flagformatter" style="width:10%">出/入标志</th> 				
								</tr>
							</thead>
						</table>
					</div>
				</div>
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
	<div id="toolbarId2">
	<shiro:hasPermission name="${menuAlias}:function:add">
		<a href="javascript:void(0)" onclick="add2()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:edit">
		<a href="javascript:void(0)" onclick="edit2()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:delete">	
		<a href="javascript:void(0)" onclick="del2()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload2()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<div id="roleaddUserdiv"></div>
	
	
	<div id="diaInpatient" class="easyui-dialog" title="编码类别选择" style="width:350;height:500;padding:1" data-options="modal:true, closed:true">
			<div style="height: 30px;padding: 5px 0px 5px 0px">
				&nbsp;<input class="easyui-textbox" id="searchTreeExport" data-options="prompt:'编码类型'" style="width: 145px;"/>
				<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodeExport()" style="margin-top: 2px;">搜索</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:export">
					<a href="javascript:void(0)" onclick="confirm()" class="easyui-linkbutton"  data-options="iconCls:'icon-save'">确定</a>
				</shiro:hasPermission>
			</div>
<!-- 			<div id="treeDiv" style="width: 100%; height: 90%; overflow-y: auto;"> -->
<!--     			<ul id="tGt"></ul> -->
<!--     		</div> -->
		</div>
	
	
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
	<script type="text/javascript">
	var hospt = new Map();
	var area = new Map();
	//加载页面
	$(function(){
		var id='${id}'; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
			url:'<%=basePath %>inpatient/inoroutStandard/getStandardList.action'
			,onBeforeLoad:function(param){
				$(this).datagrid('uncheckAll');
			}
			,onDblClickRow: function(index,row){							
				console.log(row)
				$('#detialList').datagrid('load', {
					'standardCode' : row.standCode,
					'versionNO' : row.standVersionNo
				});
			}
			,onLoadSuccess:function(row, data){
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
		$('#detialList').datagrid({
			url:'<%=basePath %>inpatient/inoroutStandard/getStandardDetialList.action'
			,onBeforeLoad:function(param){
				$(this).datagrid('uncheckAll');
			}
			,onLoadSuccess:function(row, data){
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
		bindEnterEvent("searchTreeInpId", searchTreeNodes, 'easyui');
		//加载编码树
		$('#tDt').tree({
			url : '<%=basePath%>inpatient/inoroutStandard/getSandardTree.action',
			method : 'post',
			animate : true,
			lines : true,
			onlyLeafCheck:true
			,formatter : function(node) {//统计节点总数
				var s = node.text;
				if (node.children.length>0) {					 						
					s += '<span style=\'color:blue\'>('
							+ node.children.length + ')</span>';					
				}					
				return s;
			}
			,onSelect : function(node){
				removeAllDetail();
				$('#list').datagrid('load', {
					'standardCode' : node.id
				});
			}
			,onClick : function(node){
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				if(node.id=='root'){
					return;
				}
			}
		});
	});
	function removeAllDetail(){
		var rows = $('#detialList').datagrid('getRows');
		var rlentgh = rows.length;
		for(var i = 0;i<rlentgh;i++){
			$('#detialList').datagrid('deleteRow',0);
		}
	}
	function searchTreeNodes(){
        var searchText = $('#searchTreeInpId').textbox('getValue');
        $("#tDt").tree("search", searchText);
  }
	$('#list').datagrid({
		url:'<%=basePath %>/inpatient/inoroutStandard/getStandardList.action',
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
	function flagformatter(value,row,index){
		if(value==1){
			return '入径';
		}
		else {		
			return '出径';
		}	
	}
		//弹出添加编辑区域
		function add(){
			AddOrShowEast('EditForm','<%=basePath %>inpatient/inoroutStandard/toStandardADD.action');			
		}
		//弹出修改编辑区域
		function edit(){
			var row = $('#list').datagrid('getSelected'); //获取当前选中行         
            	if(row){            		
            		AddOrShowEast('EditForm','<%=basePath %>inpatient/inoroutStandard/toStandardADD.action?standardID='+row.id);
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
							url: '<%=basePath %>inpatient/inoroutStandard/delStandard.action',
							data:{standardIDs: ids},
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
		//弹出添加编辑区域
		function add2(){
			var row = $('#list').datagrid('getSelected');
			if(row){            		
				AddOrShowEast('EditForm','<%=basePath %>inpatient/inoroutStandard/toStandardDetialADD.action?standardID='+row.id);			
			}else{
				$.messager.alert('提示','请选择一条记录!');
        		return;
			}
		}
		//弹出修改编辑区域
		function edit2(){
			var row = $('#detialList').datagrid('getSelected'); //获取当前选中行         
           	if(row){            		
           		AddOrShowEast('EditForm','<%=basePath %>inpatient/inoroutStandard/toStandardDetialADD.action?detialID='+row.id);
			}else{
				$.messager.alert('提示','请选择一条记录!');
        		return;
			}
		}
		//删除
		function del2(){
            //选中要删除的行
		    var iid = $('#detialList').datagrid('getChecked');
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
							url: '<%=basePath %>inpatient/inoroutStandard/delStandardDetail.action',
							data:{detialIDs: ids},
							type:'post',
							success: function() {
								 $.messager.progress('close');
								 $.messager.alert('提示','删除成功！');	
								$('#detialList').datagrid('reload');
							}
						});										
					}
				});
		    }else{
                $.messager.alert('操作提示', '请选择要删除的信息！'); 
            }
		}
		function reload2(){
			//实现刷新栏目中的数据
			$('#detialList').datagrid('reload');
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
		
		/**
	 	* 刷新树
	    * @author  yeguanqun
	    * @date 2016-4-12 10:53
	    * @version 1.0
	    */
	   	function refresh(){
			$('#tDt').tree('reload'); 
		}
		/**
	 	* 展开树
	    * @author  yeguanqun
	    * @date 2016-4-12 10:53
	    * @version 1.0
	    */
		function expandAll(){
			$('#tDt').tree('expandAll');
		}
		/**
	 	* 关闭树
	    * @author  yeguanqun
	    * @date 2016-4-12 10:53
	    * @version 1.0
	    */
	   	function collapseAll(){
			$('#tDt').tree('collapseAll');
		}
		
	 // 列表查询重置
		function searchReload() {
			$('#nameSerc').textbox('setValue','');
			searchFrom();
		}
		/**
	 	* 关闭查看窗口
	    * @date 2017-5-18 10:39:23
	    * @version 1.0
	    */
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		//树操作
		function refresh() {//刷新树
			$('#tDt').tree('options').url = "<c:url value='/inpatient/inoroutStandard/getSandardTree.action'/>";
			$('#tDt').tree('reload');
		}
	</script>
</body>
</html>