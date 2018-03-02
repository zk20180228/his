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
<title>全院病房维护</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>    
<script type="text/javascript">
	var stationMap=null; //护士站map
	
	$(function(){
		funTree();
		$('#datagrid1').datagrid({
			rownumbers:true,idField: 'id',striped:true,border:true,pageSize:20,pageNumber: 1,
			checkOnSelect:false,selectOnCheck:false,singleSelect:true,pagination:true,toolbar:'#toolbarId',
			border:false,fit:true,fitColumns:true,
			url : '<%=basePath %>baseinfo/BedWard/loadBedWard.action',
			queryParams:{'bedward.nursestation':null},
			columns:[[    
					{field:'id',checkbox:'true'},
					{field:'nursestation',title:'护士站',
						formatter:funStation
					},
					{field:'bedwardName',title:'病房编号'},
			        {field:'codePinyin',title:'拼音码'},    
			        {field:'codeWb',title:'五笔码'},    
			        {field:'codeInputcode',title:'自定义码'},  
			        {field:'planbednum',title:'额定床位数'},  
			        {field:'openbednum',title:'开放床位数'},  
			    ]],
			onBeforeLoad:function(){
				$('#datagrid1').datagrid('clearChecked');
				$('#datagrid1').datagrid('clearSelections');
			},onLoadSuccess : function(data){
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
			}
		});
		loadView();
	});
	
	function expandAll(){//展开树
		$('#tDt').tree('expandAll');
	}
	function collapseAll(){//关闭树
		$('#tDt').tree('collapseAll');
	}

/***
 * 左侧护士站树信息
 */
function funTree(){
	$('#tDt').tree({
		url : '<%=basePath %>baseinfo/BedWard/stationTree.action',
		method:'get',
		lines : true,
		cache : false,
		animate : true,
		onClick : function(node) {//点击节点
			if(node.id=='1'){
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				/* $.messager.alert('提示','请选择具体的护士站');
				setTimeout(function(){
						$(".messager-body").window('close');
					},3500);*/
				return ; 
			}
			$('#datagrid1').datagrid('load',{
				'bedward.nursestation':node.attributes.deptCode
			});
		},onLoadSuccess : function(node, data) {
			$('#tDt').tree('select',$('#tDt').tree('find', 1).target);
		}
	});
}

/***
 * 加载病房信息
 */
function funGrid(){
	
}

function add(){
	var node = $('#tDt').tree('getSelected');//获取所选节点
	if(node.id == ""|| node.id == null || node.id =='1'){
		$.messager.alert('友情提示','请选择具体护士站操作!','warning');
		setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		return false;
	}
	AddOrShowEast('添加病房信息','<%=basePath %>baseinfo/BedWard/addOReditView.action?bedward.nursestation='+node.attributes.deptCode);
}

function edit(){
	var row = $('#datagrid1').datagrid('getSelected'); //获取当前选中行                 
	if(!row){
		$.messager.alert('友情提示','请选择具体房间操作!','warning');
		setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		return false;
	}
	AddOrShowEast('修改病房信息','<%=basePath %>baseinfo/BedWard/addOReditView.action?bedward.id='+row.id);
}

function del(){
	var obj=$('#datagrid1').datagrid('getChecked');
	var arr =new Array();
	if(obj.length>0){
		$.each(obj,function(i,n){
			arr[i]=n.id;
			j=i+1;
		});
		$.messager.confirm('确认对话框', '您想要删除'+j+'条记录吗？', function(r){
			if (r){
				$.ajax({
					url:'<%=basePath %>baseinfo/BedWard/del.action',
					type:'post',
					traditional:true,//数组提交解决方案
					data:{'ids':arr},
					dataType:'json',
					success:function(data){
	 					if(data.resCode == 0){
	 						$.messager.alert('友情提示','操作成功!','warning');
	 						setTimeout(function(){
		  						$(".messager-body").window('close');
		  					},3500);
	 						reload();
	 					}else{
	 						$.messager.alert('友情提示',data.resMsg,'warning');
	 					}
					}
				});
			}
		});
	}else{
		$.messager.alert('提示','请选中要删除的行！');
		setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
	}
}

/*===================================================================================================================================*/

//获取渲染后的护士站
function funStation(value,row,index){
	 if(value!=null&&value!=''){
		return stationMap[value];
	}
}

function loadView(){
	//渲染护士站
	$.ajax({
		url: "<c:url value='/baseinfo/hospitalbed/queryStation.action'/>",
		type:'post',
		success: function(payData) {
			stationMap =payData;
		}
	});	
}



//判断是否添加LayOut
function AddOrShowEast(title, url) {
	var eastpanel=$('#panelEast'); //获取右侧收缩面板
	if(eastpanel.length>0){ //判断右侧收缩面板是否存在
		$('#cc').layout('remove','east');
		addPanel(title, url);
	}else{//打开新面板
		addPanel(title, url);
	}
}
//添加LayOut
function addPanel(title, url){
	$('#cc').layout('add', {
		title:title,
		region:'east',
		width:500,
		split:true,
		href:url,
		closable:false,
		collapsible : true,
		border : true
	});
}

//关闭编辑窗口
function closeLayout(){
	$('#cc').layout('remove','east');
}

function reload(){
	//实现刷新栏目中的数据
	$('#datagrid1').datagrid('reload');
}

function searchTree(){
	var text = $('#searchTree').textbox('getText');
	$.ajax({
		url : "<%=basePath %>baseinfo/BedWard/queryDeptLsit.action",
		data:{queryName:text},
		type : 'post',
		success : function(data) {
			if(data!=null&&data.length>0){
				var node = $('#tDt').tree('find',data[0].id);
				$('#tDt').tree('expandTo', node.target).tree('select',node.target).tree('scrollTo',node.target); 
			}
		}
	});

}
$(function(){
	//回车事件
	bindEnterEvent('searchTree',searchTree,'easyui');
})

</script>
<style type="text/css">
.panel-header{
	border-top:0
}
.bedWardEdit{
	border-left:0;
	border-bottom:0;
	border-top:0;
}
</style>
</head>
<body style="margin: 0px;padding: 0px">
<div id="cc" class="easyui-layout" data-options="fit:true,border:false">   
    <div data-options="region:'west',split:true,tools:'#toolSMId',title:'护士站病房信息',border:true" style="width:310px;min-width: 305px">
   		<div id="cc1" class="easyui-layout" data-options="fit:true">   
		    <div data-options="region:'north',border:false" style="height:40px;padding-top: 7px;">
		    	<span>
					<input class="easyui-textbox" ID="searchTree" style="width:175px" name="" data-options="prompt:'输入护士站信息回车查询'" />
					&nbsp;<a href="javascript:searchTree()" class="easyui-linkbutton" data-options="iconCls:'icon-search' ">查&nbsp;询&nbsp;</a>
				  </span>
		    </div>   
		    <div id="treeDiv" data-options="region:'center',border:false" style="">
		    	<ul id="tDt">数据加载中...</ul>
		    </div>   
		</div>
    </div>   
    <div data-options="region:'center',border:true" style="border-top:0">
    	<table id="datagrid1"></table>
    </div>   
     <div id="toolSMId" style="margin:5px">
			<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
			<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
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
</body>
</html>