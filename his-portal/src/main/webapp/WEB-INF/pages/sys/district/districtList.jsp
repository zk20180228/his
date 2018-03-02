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
<style type="text/css">
.layout-split-east {
    border-left: 0px; 
}
table.honry-table td{
	border-left:0px;
}
.panel-body-noheader{
	border-right:0;
}
.layout-split-east .panel-header{
	
}
</style>
</head>
<body>
	<div id="divLayout" class="easyui-layout" data-options="fit:true" style="width: 100%; height: 100%; overflow-y: auto;">
			<div data-options="region:'north',border: false"  style="padding: 5px 5px 0px 5px;height: 40px;">
				<form id="search" method="post">
					<table
						style="width: 100%; border: false;">
						<tr>
							<td style="width: 500px;">
								<span>查询条件：</span>
								<input class="easyui-textbox" id="cityName" name="cityName" onkeydown="KeyDown(0)"data-options="prompt:'城市名称,拼音,五笔,自定义'" style="width: 200px;" />
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
									<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'center',border: false"  style="height:90%;">
				<input type="hidden" value="${id }" id="id"></input>
				<table id="list" style="width: 100%;" class="easyui-datagrid"
					data-options="fit:true,idField:'id',animate:false,striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,rownumbers:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th data-options="field:'getIdUtil',checkbox:true" ></th>
							<th data-options="field:'cityCode',width : '8%' ">城市代码 </th>
							<th data-options="field:'cityName',formatter:forcityName , width : '15%'"> 城市名称 </th>
							<th data-options="field:'shortname',width : '8%'">	城市简称</th>
							<th data-options="field:'ename', width : '8%'">城市英文名称</th>
							<th data-options="field:'level', width : '8%' ">城市层级</th>
							<th data-options="field:'municpalityFlag', width : '8%' ,formatter:formatCheckBox">直辖市</th>
							<th data-options="field:'pinyin', width : '8%' ">拼音码</th>
							<th data-options="field:'wb', width : '8%'">五笔码</th>
							<th data-options="field:'defined', width : '8%'">自定义码</th>
							<th data-options="field:'validFlag', width : '8%',formatter:formatCheckBoxFlag" >有效标识</th>
							<th data-options="field:'remark', width : '10%'">备注</th>
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
			<a href="javascript:void(0)" onclick="edit()"class="easyui-linkbutton"data-options="iconCls:'icon-edit',plain:true">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">
			<a href="javascript:void(0)" onclick="del()"class="easyui-linkbutton"	data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()"class="easyui-linkbutton"data-options="iconCls:'icon-reload',plain:true">刷新</a>
		<shiro:hasPermission name="${menuAlias }:function:template">
			<a href="javascript:void(0)" onclick="imports()" class="easyui-linkbutton"data-options="iconCls:'icon-down',plain:true">导入</a>
		</shiro:hasPermission>
	</div>
	<script type="text/javascript">
	//加载页面
	$(function() {
		var id = "${id}"; //存储数据ID
		//添加treegrid事件及分页
		$('#list').datagrid({
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 30, 50,80, 100 ],
					url:'${pageContext.request.contextPath}/baseinfo/district/queryDistrict.action',
					onDblClickRow : function(rowIndex, rowData) {//双击查看
						if (getIdUtil("#list").length != 0) {
							AddOrShowEast('查看',"<%=basePath%>baseinfo/district/viewDistrict.action?id="+getIdUtil("#list"),'560');
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
				bindEnterEvent('cityName',searchFrom,'easyui');
	});
	
	function add() {
		var row = $('#list').datagrid('getSelected'); //获取当前选中行       
		var id ="";
		if (row == null) {
			id = "";
		} else {
			id = row.id;
		}
		AddOrShowEast('添加', "<%=basePath%>baseinfo/district/addDistrict.action?id="+id);
	}

	function edit() {
	var row = $('#list').datagrid('getSelected'); //获取当前选中行          
   		if(row==null){
   			$.messager.alert('提示','请选择要修改的记录!');
   			close_alert();
   			return;
   		}
   		 	AddOrShowEast('编辑',"<%=basePath%>baseinfo/district/editDistrict.action?id="+row.id,'35%');
	}

	function del() {
		//选中要删除的行
		var iid = $('#list').datagrid('getChecked');
		if (iid.length > 0) {//选中几行的话触发事件	                        
			$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
				if (res) {
					var ids = '';
					for ( var i = 0; i < iid.length; i++) {
						if (ids != '') {
							ids += ',';
						}
						ids += iid[i].id;
					}
					;
					$.ajax({
					url: "<%=basePath%>baseinfo/district/delDistrict.action?id="+ids,
						type : 'post',
						success : function() {
							$.messager.alert('提示','删除成功');
							$('#list').datagrid('reload');
						}
					});
				}
			});
		}
	}

	function reload() {
		//实现刷新栏目中的数据
		$("#list").datagrid("reload");
	}
	function imports(){
		AddOrShowEast('导入模板', "<%=basePath%>baseinfo/district/listDistrictFileUpload.action");
	}
//查询
function searchFrom() {
	var cityName = $.trim($('#cityName').val());
	$('#list').datagrid('load', {
		name : cityName
	});
}

/**
 * 格式化复选框
 * @author  
 * @date 2015-5-26 9:25       
 * @version 1.0
 */
function formatCheckBox(val,row){
	if (val == 1){
		return '非直辖市';
	} else {
		return '直辖市';
	}
}	
function formatCheckBoxFlag(val,row){
	if (val == 1){
		return '有效';
	} else {
		return '无效';
	}
}			
function forcityName(value,row,index){
	for(var i=0;i<row.level-1;i++){
		value="&nbsp;&nbsp;"+value;
	}
	return value;
}			
/**
 * 回车键查询
 * @author 
 * @param title 标签名称
 * @param title 跳转路径
 * @date 2015-05-27
 * @version 1.0
 */
function KeyDown() {
	if (event.keyCode == 13) {
		event.returnValue = false;
		event.cancel = true;
		searchFrom();
	}
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
		var eastpanel = $('#panelEast'); //获取右侧收缩面板
		if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
			//重新装载右侧面板
			$('#divLayout').layout('panel', 'east').panel({
					region : 'east',
							width :580,
							split : true,
							href : url,
							closable : true
			});
		} else {//打开新面板
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
		$('#cityName').textbox('setValue','');
		searchFrom();
	}
</script>
</body>
</html>