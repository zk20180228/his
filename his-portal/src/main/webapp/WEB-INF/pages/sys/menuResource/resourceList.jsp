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
.panel-body-noheader{
	border-left: 0px;
}
.layout-split-east .panel-header{
	border-top:0;
	border-left:0;
}
.layout-split-east .panel-body{
	border-left:0;
}
.panel-noscroll{
	border-right:0;
}
</style>
</head>
<body>
	<div id="divLayout" class="easyui-layout" fit=true>
		<div data-options="region:'north'" style="height:35px;border-top:0">
				<form id="search" method="post">
					<table style="width:100%;border:false;padding:1px;">
						<tr>
							<td><input class="easyui-textbox" ID="mfName" data-options="prompt:'请输入查询名称...'"/>
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
				</form>
		</div>
		<div
			data-options="region:'center',split:false,iconCls:'icon-book'" style="border-top:0"
			>
			<input type="hidden" value="${id }" id="id"></input>
			<table id="list"
				data-options="url:'${pageContext.request.contextPath}/sys/queryMenuResource.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,
				border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
				<thead>
					<tr>
						<th field="getIdUtil" checkbox="true"></th>
						<th data-options="field:'mrcName'" style="width:16%">资源名称</th>
						<th data-options="field:'mrcAlias'" style="width:16%">资源别名</th>
						<th data-options="field:'mrcType'" style="width:16%">资源类型</th>
						<th data-options="field:'mrcOrder'" style="width:16%">资源排序</th>
						<th data-options="field:'mrcDescription'" style="width:16%">资源说明</th>
						<th data-options="field:'mrcOrderOpr'" style="width:16%">排序操作</th>
						<th data-options="field:'createTime',hidden:true">创建时间</th>
					</tr>
				</thead>
			</table>
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
	</div>
	<script type="text/javascript">
	var map;
		
		function toUp(rowId,index){
			editOrder(rowId,map.get(index-1));
		}
		function toDown(rowId,index){
			editOrder(rowId,map.get(index+1));
		}
		function editOrder(currentId,otherId){
			$.post("editOrder.action",{"currentId":currentId,"otherId":otherId},function(result){
				if(result>0){
					$("#list").datagrid("reload");
				}else{
					$.messager.alert('提示','error');
				}
			});
		}
	
	
	
	//加载页面
	$(function() {
		bindEnterEvent('mfName',searchFrom,'easyui');
		var winH=$("body").height();
		$('#list').height(winH-78-30-27-6);//78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
		var id = "${id}"; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
			pagination : true,
			fitColumns : true,
			pageSize : 20,
			pageList : [ 20, 30, 50, 100 ],
			onLoadSuccess: function(data){
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
				var grid = $('#list');  
				var options = grid.datagrid('getPager').data("pagination").options;  
				var curr = options.pageNumber;  
				var total = options.total; 
				var pageSize = options.pageSize; 
				var rows = data.rows;
				map=null;
				map=new Map();
				var rowData = data.rows;
				$.each(rowData, function (index, value) {
					map.put(index,value.id);
					if(value.id == id){
						$('#list').datagrid('checkRow', index);
					}
				});
				if(rows.length>1){
					for(var i=0;i<rows.length;i++){
						var index = $('#list').datagrid('getRowIndex',rows[i]);
						var a = "";
						if(curr==1&&index==0){//第一行
							a = '<a class="downCls" onclick="toDown(\''+rows[i].id+'\','+index+')" href="javascript:void(0)" style="height:20"></a>';
						}else if((index+1)+((curr-1)*pageSize)==total){//最后一行
							a = '<a class="upCls" onclick="toUp(\''+rows[i].id+'\','+index+')" href="javascript:void(0)" style="height:20"></a>';
						}else{
							a = '<a class="upCls" onclick="toUp(\''+rows[i].id+'\','+index+')" href="javascript:void(0)" style="height:20"></a>';
							a += '<a class="downCls" onclick="toDown(\''+rows[i].id+'\','+index+')" href="javascript:void(0)" style="height:20"></a>';
						}
						$('#list').datagrid('updateRow',{
							index: index,
							row: {
								mrcOrderOpr : a
							}
						});
					}
					$('.upCls').linkbutton({text:'上移',plain:true,iconCls:'icon-up'}); 
					$('.downCls').linkbutton({text:'下移',plain:true,iconCls:'icon-down'});
				}
				
			},
			onBeforeLoad:function(param){
				$(this).datagrid('uncheckAll');
			},
			onDblClickRow : function(rowIndex, rowData) {//双击查看
				if (getIdUtil("#list").length != 0) {
					AddOrShowEast('EditForm','<%=basePath %>sys/viewResource.action?id='+ getIdUtil("#list"));
				}
			}
		});
	});
	//添加
	function add(){
		AddOrShowEast('EditForm',"<%=basePath %>sys/addResource.action");
	}
	//修改
	function edit(){
		if(getIdUtil("#list").length!=0){
			AddOrShowEast('EditForm',"<%=basePath %>sys/editResource.action?id="+getIdUtil("#list"));
		}
	}
	function del(){
	//选中要删除的行
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
							url: "<%=basePath %>sys/delResource.action?ids="+ids,
							type:'post',
							success: function(detaMap) {
								$('#list').datagrid('reload');
								$.messager.alert('提示',dataMap.resCode);
								if(dataMap.resMsg=="success"){
									$('#list').datagrid('reload');
								}
							}
						});
					}
					
			});
		}
	}
	//刷新
	function reload(){
		//实现刷新栏目中的数据
		$('#list').datagrid('reload');
	}
	//查询
	function searchFrom() {
		var name = $('#mfName').val();
		$('#list').datagrid('load', {
			mrcName : name
		});
	}

	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function clears(){
		$('#mfName').textbox('setValue','');
		searchFrom();
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
				href : url
			});
		} else {//打开新面板
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
</body>
</html>