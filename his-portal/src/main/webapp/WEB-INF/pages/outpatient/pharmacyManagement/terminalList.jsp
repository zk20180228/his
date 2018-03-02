<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>门诊终端列表显示界面</title>
		<script type="text/javascript">
	var terminalMap0=null; //渲染信息
	var terminalMap1=null; //渲染信息
	var propertyMap=null; //性质类型
	
	//加载页面
	$(function() {

		propertyMap=getPropertyMap();
		$.ajax({
			url:'<%=basePath%>drug/pharmacyManagement/queryTerminalMap.action?type=0&pid='+window.parent.window.getSelected(2),
			success: function(payData) {
				terminalMap0 = payData;
			}
		});
		$.ajax({
			url:'<%=basePath%>drug/pharmacyManagement/queryTerminalMap.action?type=1&pid='+window.parent.window.getSelected(2),
			success: function(payData) {
				terminalMap1 = payData;
			}
		});
		setTimeout(function(){
			listGrid();
		},100);
		
		//选项卡
		$('#list1').datagrid({ 
			url:'<%=basePath %>drug/pharmacyManagement/queryTerminal.action?type=0&pid='+window.parent.window.getSelected(2),
			pageSize:20,
			onBeforeLoad:function(){
				$('#list1').datagrid('clearChecked');
				$('#list1').datagrid('clearSelections');
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
		
	});
	
	
	function funTerminal0(value,row,index){
		
		if(value!=null&&value!=''){
			if(terminalMap0[value]){
				return terminalMap0[value];
			}
			return value;
		}
	}
	function funTerminal1(value,row,index){
		if(value!=null&&value!=''){
			return terminalMap1[value];
		}
	}
	
	function listGrid(){
		$('#ui').tabs({    
		    border:false,    
		    onSelect:function(title){    
		        if(title=="发药窗口"){
		        	closeLayout();
		        	$('#list1').datagrid({ 
		    			url:'<%=basePath %>drug/pharmacyManagement/queryTerminal.action?type=0&pid='+window.parent.window.getSelected(2),
		    			pageSize:20,
		    			onBeforeLoad:function(){
		    				$('#list1').datagrid('clearChecked');
		    				$('#list1').datagrid('clearSelections');
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
		        }
		        if(title == "配药台"){
		        	closeLayout();
		    		$('#list2').datagrid({ 
		    			url:'<%=basePath %>drug/pharmacyManagement/queryTerminal.action?type=1&pid='+window.parent.window.getSelected(2),
		    			pageSize:20,
		    			onBeforeLoad:function(){
		    				$('#list2').datagrid('clearChecked');
		    				$('#list2').datagrid('clearSelections');
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
		    	}  
		    }
		}); 
	}
	
	function add(){
		closeLayout();
		if(window.parent.window.getSelected(3)=='terminal'){
			tab = $('#ui').tabs('getSelected');
			index = $('#ui').tabs('getTabIndex',tab);
			AddOrShowEast('添加', '<%=basePath %>drug/pharmacyManagement/terminalAddURL.action?menuAlias=${menuAlias}&operate=add&index='+index);
		}else{
			$.messager.alert('提示','请选择相应药房下的门诊终端维护！');
			setTimeout(function(){$(".messager-body").window('close')},1500);
		}
	}
	
	function edit(){
		closeLayout();
		tab = $('#ui').tabs('getSelected');
		index = $('#ui').tabs('getTabIndex',tab);
		if(index==0){
			var row = $('#list1').datagrid('getSelected'); //获取当前选中行                        
		}else{
			var row = $('#list2').datagrid('getSelected'); //获取当前选中行                        
		}
	    if(row){
	    	AddOrShowEast('修改', '<%=basePath %>drug/pharmacyManagement/terminalAddURL.action?menuAlias=${menuAlias}&operate=edit&index='+index+'&id='+row.id);
		}else{
			$.messager.alert('提示','请选择一条要修改的的数据！');
			setTimeout(function(){$(".messager-body").window('close')},1500);
		}
	}
	
	function del(){
		tab = $('#ui').tabs('getSelected');
		index = $('#ui').tabs('getTabIndex',tab);
		if(index==0){
			var obj=$('#list1').datagrid('getChecked');         
		}else{
			var obj=$('#list2').datagrid('getChecked');
		}
		var ids= '';
		if(obj.length>0){
			$.each(obj,function(i,n){
				if(ids != ''){
                    ids +=',';
                }
				ids += n.id;
			});
			var j = obj.length;
			$.messager.confirm('确认对话框', '您确定要删除'+j+'条记录吗？删除后的记录将不可恢复！', function(r){
				if (r){
					$.ajax({
						url:'<%=basePath %>drug/pharmacyManagement/terminalDel.action',
						type:'post',
						data:{'ids':ids},
						success:function(data){
							if(data.resCode == 'error'){
								$.messager.alert('提示',data.resMes);
							}else{
		 						$.messager.alert('提示',data.resMes);
		 						reload();
							}
							
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','请选中要删除的行');
			setTimeout(function(){$(".messager-body").window('close')},1500);
		}
	}
	
	/**
	 * 动态添加标签页
	 * @author  sunshuo
	 * @param title 标签名称
	 * @param title 跳转路径
	 */
	 
	function AddOrShowEast(title, url) {
		$('#divLayout').layout('add', {
			region : 'east',
		    title: title,    
			width : 500,
			split : true,
			border : false,
			href : url,
			closable : true
		});
	}
	
	function reload(){
		closeLayout();
		tab = $('#ui').tabs('getSelected');
		index = $('#ui').tabs('getTabIndex',tab);
		if(index==0){
			$('#list1').datagrid('reload');   
		}else{
			$('#list2').datagrid('reload');   
		}
	}
	
	function listReload(index){
		if(index==0){
			$('#list1').datagrid('reload');   
		}else{
			$('#list2').datagrid('reload');   
		}
	}
	
	//关闭Layout
	function closeLayout() {
		$('#divLayout').layout('remove', 'east');
	}
	
	
	//渲染性质类型
	function funPropertyMap(value,row,index){
		if(value == 0){
			return propertyMap.get(value);
		}
		if(value!=null&&value!=''){
			return propertyMap.get(value);
		}
	}
	//获取所有性质类型的数组
	function getPropertyArray(){
		return [{id:'0',value:'普通'},{id:'1',value:'专科'},{id:'2',value:'特殊'}];
	}
	
	//获取所有性质类型的Map
	function getPropertyMap(){
		var PropertyMap2 = new Map();
		var PropertyArray = getPropertyArray();
		for(var i=0;i<PropertyArray.length;i++){
			PropertyMap2.put(PropertyArray[i].id,PropertyArray[i].value);
		}
		return PropertyMap2;
	}
	
</script>
	</head>
<body>
	<div class="easyui-layout" fit=true
		style="width: 100%; height: 100%; overflow-y: auto;">
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
		<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow: hidden;">
			<div id="ui" class="easyui-tabs" data-options="fit:true">
				 <div title="发药窗口" style="padding:5px;">  
				 	<table id="list1" style="width: 100%;" class="easyui-datagrid"
						data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,
						checkOnSelect:true,selectOnCheck:false,singleSelect:true,pagination:true,toolbar:'#toolbarId',fit:true">
						<thead>
							<tr>
								<th data-options="field:'id',checkbox:true"
									style="width: 8%; text-align: center"></th>
								<th data-options="field:'name'"
									style="width: 12%; text-align: center">名称</th>
								<th data-options="field:'property',formatter:funPropertyMap"
									style="width: 8%; text-align: center">终端性质</th>
								<th data-options="field:'closeFlag',formatter:function(val,row){
														if (val == 0){
															return '开放';
														} else if (val == 1){
															return '关闭';
														}
													}"
									style="width: 8%; text-align: center">是否关闭</th>
								<th data-options="field:'alertNum'"
									style="width: 8%; text-align: center">警戒线（/位）</th>
								<th data-options="field:'replaceCode',formatter:funTerminal0"
									style="width: 8%; text-align: center">替代终端</th>
								<th data-options="field:'refreshInterval1'"
									style="width: 10%; text-align: center">程序刷新间隔（/秒）</th>
								<th data-options="field:'refreshInterval2'"
									style="width: 10%; text-align: center">大屏刷新间隔（/秒）</th>
								<th data-options="field:'showNum'"
									style="width: 8%; text-align: center">显示人数（/位）</th>
								<th data-options="field:'mark'"
									style="width: 15%; text-align: center">备注</th>
							</tr>
						</thead>
					</table>
				 </div>
				 <div title="配药台" style="padding:5px;">  
				 	<table id="list2" style="width: 100%;" class="easyui-datagrid" 
				 	data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,
				 	checkOnSelect:true,selectOnCheck:false,singleSelect:true,pagination:true,toolbar:'#toolbarId',fit:true">
						<thead>
							<tr>
								<th data-options="field:'id',checkbox:true"
									style="width: 8%; text-align: center"></th>
								<th data-options="field:'name'"
									style="width: 8%; text-align: center">名称</th>
								<th data-options="field:'property',formatter:funPropertyMap"
									style="width: 8%; text-align: center">终端性质</th>
								<th data-options="field:'closeFlag',formatter:function(val,row){
														if (val == 0){
															return '开放';
														} else if (val == 1){
															return '关闭';
														}
													}"
									style="width: 8%; text-align: center">是否关闭</th>
								<th data-options="field:'alertNum'"
									style="width: 8%; text-align: center">警戒线（/位）</th>
								<th data-options="field:'sendWindow',formatter:funTerminal0"
									style="width: 10%; text-align: center">发药窗口</th>
								<th data-options="field:'replaceCode',formatter:funTerminal1"
									style="width: 10%; text-align: center">替代终端</th>
								<th data-options="field:'refreshInterval1'"
									style="width: 10%; text-align: center">程序刷新间隔（/秒）</th>
								<th data-options="field:'refreshInterval2'"
									style="width: 10%; text-align: center">大屏刷新间隔（/秒）</th>
								<th data-options="field:'showNum'"
									style="width: 8%; text-align: center">显示人数（/位）</th>
								<th data-options="field:'mark'"
									style="width: 15%; text-align: center">备注</th>
							</tr>
						</thead>
					</table>
				 </div>
			</div>
		</div>
  </div>
</body>
</html>