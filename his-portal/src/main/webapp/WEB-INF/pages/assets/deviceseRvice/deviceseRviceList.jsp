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
	<title>设备维护</title>
<body>
	<script type="text/javascript">
	var addAndEdit;
	//加载页面
	$(function() {
		var id = "${id}"; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			onBeforeLoad:function(){
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
			},
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				var row = $('#list').datagrid('getSelected'); //获取当前选中行    
                if(row){
                	AddOrShowEast("查看",'<%=basePath %>assets/deviceseRvice/viewAssetsDeviceseRvice.action?id='+rowData.id);
			   	}
			}
		});
		
		//回车查询
        bindEnterEvent('office',searchFrom,'easyui');//查询条件
        bindEnterEvent('name',searchFrom,'easyui');//查询条件
        bindEnterEvent('classes',searchFrom,'easyui');//查询条件
        bindEnterEvent('device',searchFrom,'easyui');//查询条件
	});
	//添加
	function add(){
		addAndEdit = 0;
		closeLayout();
		AddOrShowEast('EditForm', '<%=basePath %>assets/deviceseRvice/saveDeviceseRviceUrl.action');
	}
	//修改
	function edit(){
		var rows = $('#list').datagrid('getSelected');
		if (rows != null && rows.length != 0) {
			addAndEdit = 1;
			closeLayout();
			AddOrShowEast('EditForm', '<%=basePath %>assets/deviceseRvice/updateDeviceseRviceUrl.action?id=' + getIdUtil('#list'));
		}else{
			$.messager.alert('操作提示',"请选择要修改的信息！");
			close_alert();
		}
	}
	
	function del1(){
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
							url: '<%=basePath %>assets/deviceseRvice/deleteDeviceseRvice.action?id='+ids,
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
							}
						});
					}
	           });
           }else{
        	   $.messager.alert('操作提示',"请选择要删除的信息！");
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
		var office = $.trim($('#office').val());
		var name = $.trim($('#name').val());
		var classes = $.trim($('#classes').val());
		var device = $.trim($('#device').val());
		$('#list').datagrid('load', {
			officeName : office,
			classCode : classes,
			className : name,
			deviceName : device,
		});
	}
	/**
	 * 动态添加标签页
	 * @author  sunshuo
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-05-21
	 * @version 1.0
	 */
	function AddOrShowEast(title, url) {
		//获取右侧收缩面板
		var eastpanel=$('#panelEast'); 
		//判断右侧收缩面板是否存在
		if(eastpanel.length>0){
			//重新装载右侧面板
	   		$('#divLayout').layout('panel','east').panel({
                      href:url 
               });
		}else{
			$('#divLayout').layout('add', {
				region : 'east',
				width : 580,
				split : true,
				border : false,
				href : url,
				closable : true
			});
		}
			
	}
	//关闭Layout
	function closeLayout(){
		$('#divLayout').layout('remove', 'east');
	}
	
	// 列表查询重置
	function searchReload() {
		$('#office').textbox('setValue','');
		$('#name').textbox('setValue','');
		$('#classes').textbox('setValue','');
		$('#device').textbox('setValue','');
		searchFrom();
	}
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" fit=true>
			<div data-options="region:'north',split:false,border:false,iconCls:'icon-search'" style="height: 40px;">
				<table cellspacing="0" cellpadding="0" border="0" style="padding: 7px 0px 5px 0px">
					<tr>
						<td>
							&nbsp;办公用途：<input class="easyui-textbox" name="office" id="office" data-options="prompt:'请输入办公用途'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;设备分类：<input class="easyui-textbox" name="name" id="name" data-options="prompt:'请输入公司名称'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;类别代码：<input class="easyui-textbox" name="classes" id="classes" data-options="prompt:'请输入设备分类'" onkeydown="KeyDown()" style="width:150px"/>
							&nbsp;设备名称：<input class="easyui-textbox" name="device" id="device" data-options="prompt:'请输入设备名称'" onkeydown="KeyDown()" style="width:150px"/>
						</td>
						<td>
						<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</shiro:hasPermission>
						<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',split:false,border:false" style=" width: 100%;">
				<input type="hidden" value="${id}" id="id"></input>
				<table id="list" style="height: 557px;" data-options="fit:true,url:'${pageContext.request.contextPath}/assets/deviceseRvice/queryAssetsDeviceseRvice.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th field="ck" checkbox="true"></th>
							<th data-options="field:'officeName', width : 200">
								办公用途
							</th>
							<th data-options="field:'classCode', width : 200">
								类别代码
							</th>
							<th data-options="field:'className', width : 200">
								设备分类
							</th>
							<th data-options="field:'deviceCode', width : 200">
								设备代码
							</th>
							<th data-options="field:'deviceName', width : 200">
								设备名称
							</th>
							<th data-options="field:'meterUnit', width : 200">
								计量单位
							</th>
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
			<a href="javascript:void(0)" onclick="del1()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
</body>
</html>