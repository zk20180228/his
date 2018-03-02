<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
.panel-header,.panel-body{
	border-top:0
}
</style>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true" >   
	<div data-options="region:'west',title:'报表信息',split:true" style="width:15%;">
		<ul id="tDt"></ul>  
	</div>   
	<div data-options="region:'center',title:'对照列表'">
		<div class="easyui-layout" data-options="fit:true" > 
			<div data-options="region:'north'" style="height:42px;width:100%;border-top:0;border-left:0;border-right:0;">
				<table style="width:100%;padding: 0px 5px 0px 5px;height: 40px;">
					<tr>
						<td nowrap="nowrap"  >查询条件：
							<input  id="codes" data-options="prompt:'代码,名称'"  class="easyui-textbox"  style="width: 200px"/>
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							<a href="javascript:reset();" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
						</td>
					</tr>
				</table>
			</div> 
			<div data-options="region:'center',border: false" style="width:100%;">
				<table id="list" class="easyui-datagrid" data-options="url:'${pageContext.request.contextPath}/baseinfo/minFeeStatCode/queryMinFeeStatCode.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true" text-align: center">id</th>
							<th data-options="field:'reportType'"  style="width:5%" >报表类别</th>
							<th data-options="field:'reportName'"  style="width:10%">报表名称</th>
							<th data-options="field:'minfeeCode'" style="width:10% " >最小费用代码</th>
							<th data-options="field:'minfeeName'"  style="width:10%">最小费用名称</th>
							<th data-options="field:'feeStatCode'"  style="width:10% ">统计费用代码</th>
							<th data-options="field:'feeStatName'"  style="width:10% ">统计费用名称</th>
							<th data-options="field:'centerStatCode'"  style="width:10% ">医保中心统计费用代码</th>
							<th data-options="field:'centerStatName'" style="width:10% ">医保中心统计费用名称</th>
							<th data-options="field:'printOrder'"  style="width:10% ">打印顺序</th>
							<th data-options="field:'exeDeptId',formatter:functionDept"  style="width:10% ">执行科室</th>
						</tr>
					</thead>
				</table>
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
	</div>
</div>
<script type="text/javascript">
var winH=$("body").height();
var departmentMap= "";//科室
$(function(){
	$.ajax({
		url: "<c:url value='/baseinfo/minFeeStatCode/functionExeDeptId.action'/>",
		type:'post',
		success: function(gradeData) {
			departmentMap = gradeData;
			$('#list').datagrid({
				pagination: true,
				pageSize: 20,
				pageList: [20,30,50,100],
		        onDblClickRow: function (rowIndex, rowData) {//双击查看
	   	        	AddOrShowEast('EditForm','<%=basePath %>baseinfo/minFeeStatCode/viewMinFeeStatCode.action?id='+rowData.id);
				},
				onBeforeLoad:function(){
					//GH 2017年2月17日 翻页时清空前页的选中项
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
		}
	});	
	bindEnterEvent('codes',searchFrom,'easyui');
	
});
	
//渲染科室
function functionDept(value,row,index){
	if(value!=null&&value!=''){
		return departmentMap[value];
	}else{
		return value;
	}
}
$('#tDt').tree({    
	url:"<%=basePath %>baseinfo/minFeeStatCode/minCensusTree.action",
	formatter:function(node){//统计节点总数
		var s=node.text;
		return s;
	},onClick:function(node){
		//选择根节点的话  清空所有数据
		$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
		if(node==null||node.id=='root'){
			$('#list').datagrid('loadData',{ total: 0, rows: [] });
		}else{
			$('#list').datagrid({
				url:'<%=basePath %>baseinfo/minFeeStatCode/queryMinFeeStatCode.action',
				queryParams: {reportNames : node.attributes.code}
			});
		}
		
	}
}); 
//条件查询
function searchFrom(){
	var codes =  $.trim($('#codes').textbox('getValue'));
	var node = $('#tDt').tree('getSelected');
	if(node!=null&&node.id!='root'){
		node=node.attributes.code;
	}
    $('#list').datagrid('load', {
    	reportName: codes,
    	reportNames : node
	});
}	
//添加
function add(){
	var node=$('#tDt').tree('getSelected');
	//选择根节点的话  清空所有数据
	if(node==null||node.id=="root"){
		$.messager.alert('提示','请选择一项报表信息再进行添加');
		close_alert();
		return;
	}
	AddOrShowEast('EditForm',"<%=basePath %>baseinfo/minFeeStatCode/addMinFeeStatCode.action",'post'
			   ,{"treeId":node.id,"treeName":node.text});
}
//修改
function edit(){
		var rows = $('#list').datagrid('getSelected');
		if(rows==null){
			$.messager.alert('提示','请选择要修改的信息');
			close_alert();
			return;
		}
		var deptId=rows.exeDeptId;
		var sid=getIdUtil("#list");
		 AddOrShowEast('EditForm','<%=basePath %>baseinfo/minFeeStatCode/editMinFeeStatCode.action','post'
				   ,{"sid":sid,"deptId":deptId});
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
				$.messager.progress({text:'删除中，请稍后...',modal:true});
				$.ajax({
					url: '<%=basePath %>baseinfo/minFeeStatCode/delMinFeeStatCode.action?ids='+ids,
					type:'post',
					success: function() {
						$.messager.progress('close');
						$.messager.alert('提示','删除成功');
						$('#tDt').tree("reload");
						$('#list').datagrid('reload');
					}
				});
			}
		});
	}
}

//跳转
function AddOrShowEast(title, url,method,params) {
	if(!method){
		method="get";
	}
	if(!params){
		params={};
	}
	var eastpanel=$('#panelEast'); //获取右侧收缩面板
	if(eastpanel.length>0){ //判断右侧收缩面板是否存在
		//重新装载右侧面板
   		$('#divLayout').layout('panel','east').panel({
               href:url,
               method:method,
				queryParams:params
        });
	}else{//打开新面板
		$('#divLayout').layout('add', {
			region : 'east',
			width : 580,
			split : true,
			href : url,
			method:method,
			queryParams:params,
			closable : true,
		});
	}
}
//重置
function reset(){
	$("#codes").textbox("setValue","");
	$('#list').datagrid('load',{});
}
</script>
</body>
</html>