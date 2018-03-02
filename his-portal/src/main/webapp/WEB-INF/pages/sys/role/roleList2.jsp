<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
	<body>
	<div class="easyui-layout" style="width:100%;height:100%;">
		<div data-options="region:'center'" >
			<div id="divLayout" class="easyui-layout" data-options="fit:true">
				 <div data-options="region:'north',split:false" style="padding:5px;min-height:80px;height:auto;">	        
					<div class="easyui-panel" data-options="title:'信息查询',iconCls:'icon-search'">
						<form id="searchForm" name="searchForm" method="post">
							<table style="width:100%;border:1px solid #95b8e7;padding:5px;">
								<tr>
									<td style="width: 300px;">关键字：<input type="text" name="role.name" onkeydown="javascript:if(event.keyCode==13) return false;"onkeyup="javascript:if(event.keyCode==13) searchFrom();"  />
									</td>
									<td>
										&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</td>
								</tr>
								
							</table>
						</form>
					</div>
				</div>		
				<div data-options="region:'center',split:false,title:'角色列表',iconCls:'icon-book'" style="padding:10px;">
					<table id="list" class="easyui-treegrid" data-options="idField:'id',treeField:'name',animate:false,rownumbers:true,striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true" ></th>
								<th data-options="field:'id',hidden:true">角色编号</th>
								<th data-options="field:'name'">角色名称</th>
								<th data-options="field:'alias'">角色别名</th>
								<th data-options="field:'description'">角色描述</th>
								<th data-options="field:'parentRoleId',hidden:true">父级</th>
								<th data-options="field:'menufunction'">层级路径</th>
								<th data-options="field:'order'">排序</th>
								<th data-options="field:'uppath',hidden:true">所有父级</th>
							</tr>
						</thead>
					</table>
				</div>		
			</div>	
		</div>
	</div>
	<div id="roleWin"></div>
	<div id="toolbarId">
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>		
		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		<a href="javascript:void(0)" onclick="openTree()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">展开</a>
		<a href="javascript:void(0)" onclick="closedTree()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">折叠</a>
		<a href="javascript:void(0)" onclick="authorize()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">授权</a>
	</div>
	<script type="text/javascript">
	//初始化页面
	$(function(){
		$('#list').treegrid({
			url:'queryRoleByPage.action',
			pagination: true,
			pageSize: 10,
			pageList: [10,20,30],
			onBeforeLoad: function(row,param){
 					if (!row) {	// 加载顶级节点
 						param.id = 0;	// id=0表示去加载新的一页
 					}
			},
			onDblClickRow: function (row) {//加载之前获得数据权限类型
				if(getIdUtil('#list').length!=0){
			   	    AddOrShowEast('EditForm','viewRole.action?id='+getIdUtil('#list'));
			   	}
			}
		});
	});
	
	//添加
   	function add(){
   		AddOrShowEast('EditForm','addRole.action');
   	}
	
	//修改
   	function edit(){
   		var row = $('#list').treegrid('getSelected'); //获取当前选中行
   		if(row==null||row==""){
   			$.messager.alert('提示','请选择具体角色');
   			close_alert();
   			return;
   		}     
        if(row){
        	AddOrShowEast('EditForm','editRole.action?id='+row.id);
		}
   	}
	
	//删除
   	function del(){
   		var rows = $('#list').treegrid('getChecked');
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
						url: 'delRole.action?id='+ids,
						type:'post',
						success: function() {
							$.messager.alert('提示','删除成功');
							$('#list').treegrid('reload');
						}
					});
				}
        	});
    	}else{
    		$.messager.alert('提示','请选择要删除的信息！');
    		close_alert();
    	}
   	}
	
	//刷新
	function reload(){
		$('#list').treegrid('reload');
	}
	
	//展开
	function openTree(){
		$('#list').treegrid('expandAll');
	}
	
	//折叠
	function closedTree(){
		$('#list').treegrid('collapseAll');
	}
	
	//动态添加LayOut
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
				width : 580,
				split : true,
				href : url,
				closable : true
			});
		}
	}
			
   	//弹框 授权
   	function authorize(){
   		var row = $('#list').treegrid('getSelected'); //获取当前选中行
   		if(row==null||row==""){
   			$.messager.alert('提示','请选择具体角色');
   			close_alert();
   			return;
   		}         
   		if(row.id!=""&&row.id!=null){
   			Adddilog("角色授权",'authorizeRole.action?id='+row.id,'90%','90%');
   		}
   		
   	}
   	//加载模式窗口
	function Adddilog(title, url, width, height) {
		$('#roleWin').dialog({    
		    title: title,    
		    width: width,    
		    height: height,    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
	}
    
	//打开模式窗口
	function openDialog() {
		$('#roleWin').dialog('open'); 
	}
	
	//关闭模式窗口
	function closeDialog() {
		$('#roleWin').dialog('close');  
	}
	//查询
	function searchFrom(){
	    var name =$('#searchForm :input[name="role.name"]').val();
	    if((name==null|| $.trim(name)=="")){
	    	reload();
	    	return;
	    }
	    $.post("searchRolesByParams.action",{"name":name},function(result){
	    	$('#list').treegrid('loadData',result);
	    },'json');
	}	
	</script>
	</body>
</html>