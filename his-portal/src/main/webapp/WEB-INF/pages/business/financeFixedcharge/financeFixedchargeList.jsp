<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>床位绑定费用维护</title>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
.panel-header{
	border-top:0
}
</style>
</head>
<body style="margin: 0px;padding: 0px">
  <div id="divLayout"   class="easyui-layout" style="width:100%;height:100%;">
		
  	<div id="p" data-options="region:'west',split:true,title:'床位绑定费用维护',tools:'#toolSMId'" style="width:260px;padding:0px;">
  			<!-- 模糊查询 -->
		<input type="text" class="easyui-textbox" id="searchTreeInpId" data-options="prompt:'输入床位名称'" style="position:fixed;width: 150px;padding-top: 2px;margin-top: 2px;margin-left: 5px;padding-left: 10px;"/>
  		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a>	
		<div style="width:100%;height:94%;overflow-y: auto;">
			<ul id="tDt">数据加载中...</ul>  
		</div>
	</div>
	<div data-options="region:'center'" style="border-top:0">
		<input type="hidden" id="uId" name="uId">
       	<input type="hidden" id="treeId" name="treeId">
       	<input type="hidden" id="treeText" name="treeText" />
       	
		<table id="list" style="width: 100%;" class="easyui-datagrid"
			data-options="striped:true,border:false,fit:true,checkOnSelect:false,selectOnCheck:false,
			singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true, width : '5%'" ></th>
					<th data-options="field:'drugUndrug',width:'20%',formatter:UnDrug">项目名称</th> 
					<th data-options="field:'chargeAmount',width: '8%'">数量</th>
					<th data-options="field:'chargeUnitprice',width: '8%'">单价</th>
					<th data-options="field:'chargeStarttime',width: '13%'">开始时间</th>
					<th data-options="field:'chargeEndtime',width: '13%'">结束时间</th>
					<th data-options="field:'chargeIsaboutchildren',width: '8%',formatter:isTimeFormatter">婴儿相关</th>
					<th data-options="field:'chargeIsabouttime',width: '8%',formatter:isTimeFormatter">时间有关</th>
					<th data-options="field:'chargeState',width: '8%',formatter:isStateFormatter">是否有效</th>
					<th data-options="field:'chargeOrder',width: '8%'">顺序号</th>
				</tr>
			</thead>
	  	</table>
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
  </div>
</body>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>	
<script type="text/javascript">

//床位等级树
var selectId="";
var undrug='';

$('#tDt').tree({    
    url:'<%=basePath%>baseinfo/financeFixedcharge/treeCostBed.action',
    animate:true,  //点在展开或折叠的时候是否显示动画效果
    lines:true,    //是否显示树控件上的虚线
    formatter:function(node){//统计节点总数
		var s = node.text;
		return s;
	},onSelect: function(node){//点击节点
		selectId = node.id;
		$('#treeId').val(node.id);  //拿到树的id
		$('#treeText').val(node.text);  //拿到树的text
		$('#list').datagrid('load',{
			treeId:selectId
		});
	},onClick:function(node){
		$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
	}
}); 

	$.ajax({
 	url: "<%=basePath%>baseinfo/financeFixedcharge/queryNames.action?",   
	type:'post',
	success: function(data) {
		undrug = data
		//列表显示
		$('#list').datagrid({
			url:'<%=basePath%>baseinfo/financeFixedcharge/queryFinanceFixedcharge.action',
			method:'post',
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			onDblClickCell:function(rowIndex, field, value){
				view();
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

	function searchTreeNodes(){
	    var searchText = $('#searchTreeInpId').textbox('getValue');
	    $("#tDt").tree("search", searchText);
	 }
	$(function(){
		bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
	});

	
//添加
function add(){
	var treeId=$('#treeId').val()
	if (treeId == null||treeId==''|| treeId == "1") {
		$.messager.alert('提示信息','请选择具体的床位等级,在进行添加！');
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return false;
	}
     AddOrShowEast('EditForm',"<%=basePath%>baseinfo/financeFixedcharge/addFinanceFixedcharge.action",'post'
    		 ,{treeId:treeId});
}
//删除
function del(){
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
				 	url: "<%=basePath%>baseinfo/financeFixedcharge/delFinanceFixedcharge.action?id="+ids,   
					type:'post',
					success: function() {
						$.messager.alert('提示信息','删除成功！');
						$('#list').datagrid('reload');
					}
				});
			}
     });
 }
}
function UnDrug(val,row,index){
	for(var i=0;i<undrug.length;i++){
		if(val==undrug[i].code){
			return undrug[i].name;
		}
	}
}	
//修改
function edit(){
	var treeId=$('#treeId').val();
	if (treeId == null||treeId==''|| treeId == "1") {
		$.messager.alert('提示信息','请选择具体的床位等级,在进行修改！');
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return false;
	}
	var rows=$('#list').datagrid('getSelected');
	if(rows!=null){
        AddOrShowEast('EditForm',"<%=basePath%>baseinfo/financeFixedcharge/updateFinanceFixedcharge.action?id="+getIdUtil("#list")+"&treeId="+treeId+"&drugUndrugName="+encodeURI(UnDrug(rows.drugUndrug)));
	}else{
		$.messager.alert('提示信息','请选择一项费用进行修改');
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
	}
}
function view(){
	
	var treeObj = $('#tDt').tree('getSelected');
	var rows=$("#list").datagrid('getSelected');
	if(getIdUtil("#list").length!=0){
        AddOrShowEast('EditForm',"<%=basePath%>baseinfo/financeFixedcharge/viewFinanceFixedcharge.action?id="+getIdUtil("#list")+"&treeId="+$('#treeId').val()+"&drugUndrugName="+encodeURI(UnDrug(rows.drugUndrug)));
		}
}
//刷新
function reload(){
	//实现刷新栏目中的数据
	$('#list').datagrid('reload');
}
/**
 * 动态添加LayOut
 * @author  lyy
 * @param title 标签名称
 * @param url 跳转路径
 * @date 2016-08-16
 * @version 1.0
 */
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
                     href:url 
              });
	}else{//打开新面板
		$('#divLayout').layout('add', {
			region : 'east',
			width : 500,
			split : true,
			href : url,
			method:method,
			queryParams:params,
			closable : true,
			border : false
		});
	}
}
/**
 * 是否有关 
 */
function isTimeFormatter(value,row,index){
	if(value=='1'){
		return "有关";
	}else{
		return "不相关";
	}
}
/**
 * 是否有效 -- 1在用2停用3废弃
 */
function isStateFormatter(value,row,index){
	if(value=='1'){
		return "在用";
	}else if(value=='2'){
		return "停用";
	}else if(value=='3'){
		return "废弃";
	}
}
</script>
</html>