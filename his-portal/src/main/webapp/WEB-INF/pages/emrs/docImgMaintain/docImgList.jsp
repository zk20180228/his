<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>医用图片维护</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	var checkpointList = "";//身体部位List
	
	//获得单位
	$.ajax({
		url: "<%=basePath%>emrs/docImgMaintain/queryCheckpoint.action",	
		type:'post',
		success: function(data) {
			checkpointList = data;			
		}
	});
	//加载页面
	$(function(){
		var winH=$("body").height();
		//$('#p').height(winH-78-30-27-2);//78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
		$('#list').height(winH-78-30-27-26);
		var id='${id}'; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			onLoadSuccess: function (data) {//默认选中
	            var rowData = data.rows;
	            $.each(rowData, function (index, value) {
	            	if(value.id == id){
	            		$('#list').datagrid('checkRow', index);
	            	}
	            });
	        },
	        onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				var row = $('#list').datagrid('getSelected'); //获取当前选中行    
                if(row){
               		AddOrShowEast('EditForm','<%=basePath %>emrs/docImgMaintain/toAddViewEmcPicture.action?emcPicture.id='+rowData.id);
			   	}
			},
			onClickRow: function(row){							
				var eastpanel=$('#panelEast'); //获取右侧收缩面板
				if(eastpanel.length>0){ //判断右侧收缩面板是否存在				
					$('#divLayout').layout('remove','east');			
				}
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
	
	function checkpointformatter(value,row,index){
		var retVel=null;
		if(value!=null&&value!=""){
			for(var i=0;i<checkpointList.length;i++){
				if(value==checkpointList[i].encode){
					return checkpointList[i].name;
					break;
				}
			}
		}
		if(retVel==null){
			retVel = value;
		}
		return retVel;			
	}
	
	function stopformatter(value,row,index){		
		if(value==1){
			return '停用';
		}
		else {		
			return '使用';
		}	
	}
	
		//弹出添加编辑区域
		function add(){
			AddOrShowEast('EditForm','<%=basePath %>emrs/docImgMaintain/addEmcPicture.action');
		}
		//弹出修改编辑区域
		function edit(){
			var row = $('#list').datagrid('getSelected'); //获取当前选中行         
            	if(row){            		
            		AddOrShowEast('EditForm','<%=basePath %>emrs/docImgMaintain/editEmcPicture.action?emcPicture.id='+row.id);
				}else{
					$.messager.alert('提示','请选择一条记录!');
					setTimeout(function(){$(".messager-body").window('close')},3500);
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
						var path = '';
						for(var i=0; i<iid.length; i++){
							if(ids!=''){
								ids += ',';
								path +=',';
							}
							ids += iid[i].id;
							path +=iid[i].picPath;
						};
						$.ajax({
							url: '<%=basePath %>emrs/docImgMaintain/removeEmcPicture.action',
							data:'emcPicture.id='+ids+'&emcPicture.picPath='+path,
							type:'post',
							success: function() {
								 $.messager.progress('close');
								 $.messager.alert('提示','删除成功');
								$('#list').datagrid('reload');
								rows.length = 0;
							}
						});										
					}
				});
		    }else{
                $.messager.alert('操作提示', '请选择要删除的信息！'); 
                setTimeout(function(){$(".messager-body").window('close')},3500);
            }
		}
		
		function reload(){
			//实现刷新栏目中的数据
			$('#list').datagrid('reload');
		}
	
	
		//查询医用图片信息
		function searchFrom(){
		    var name =	$.trim($('#name').val());
		    $('#list').datagrid('load', {
		    	'emcPicture.picName': name
			});
		}
		function KeyDown() {  
	   		if (event.keyCode == 13) {  
		        event.returnValue=false;  
		        event.cancel = true;  
		        searchFrom();  
		    }  
		} 
		//关闭
		function closeLayout(){
			$('#divLayout').layout('remove','east');
			$('#list').datagrid('reload');
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
					closable : true
				});
			}			
		}
	</script>
	<style type="text/css">
		.panel-header{
			border-top-width:0;
			border-left:0
		}
		.wordTable .panel-header{
			border-top-width:1px;
		}
	</style>
</head>
<body>
	<div id="divLayout" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="width:100%;height:40px">    
		
				<table style="width:100%;height:100%;border: none;padding:5px;" data-options="fit:true">
					<tr>
						<td>
							图片名称：<input type="text" class="inputCss"  name="name" id="name"  onkeydown="KeyDown()"/>
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>
			 
			<div data-options="region:'center',border:false" style="width:100%;height:94%;" class="wordTable">                                                                                                                  
			<table id="list" data-options="url:'<%=basePath %>emrs/docImgMaintain/queryEmcPicture.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',title:'查询列表',fit:true">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>	
						<th data-options="field:'id',hidden:true">图片Id</th>					
						<th data-options="field:'picCode'" >图片编码</th>
						<th data-options="field:'picName'" >图片名称</th>
						<th data-options="field:'picBody',formatter:checkpointformatter" >身体部位</th>
						<th data-options="field:'picPath',width:'26%'" >图片路径 </th>						
						<th data-options="field:'pingyin',width:'8%'" >拼音码</th>  
						<th data-options="field:'wb',width:'8%'" >五笔码</th> 
						<th data-options="field:'picBak',width:'8%'">备注</th> 
						<th data-options="field:'stop_flg',formatter:stopformatter" >停用状态</th> 
						<th data-options="field:'createTime'">添加时间</th> 
					</tr>
				</thead>
			</table>
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
	
</body>
</html>