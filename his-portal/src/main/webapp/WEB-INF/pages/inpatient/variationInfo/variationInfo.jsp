<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>变异信息记录</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>    
<script type="text/javascript">
	var variationCodeMap=new Map();//床位等级map
	var variationFactorCodeMap=new Map();//病房号map
	var stageIdMap=new Map();//病房号map
	$(function() {
		trr();
		$.ajax({
			url:  "<%=basePath%>inpatient/variationInfo/queryDictionary.action?type=variationName",
			type:'post',
			success: function(data) {
				for(var i=0;i<data.length;i++){
					variationCodeMap.put(data[i].code,data[i].name);
				}
			}
		});
		$.ajax({
			url:"<%=basePath%>inpatient/variationInfo/queryDictionary.action?type=variationFactorCode",
			type:'post',
			success: function(data) {
				for(var i=0;i<data.length;i++){
					variationFactorCodeMap.put(data[i].code,data[i].name)
				}
			}
		});
		//按回车键提交表单！
		$('#search').find('input').on('keyup', function(event) {
			if (event.keyCode == 13) {
				searchFrom();
			}
		});
		$('#list').datagrid({
			url:'<%=basePath %>inpatient/variationInfo/queryPathVsIcdList.action',
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},onLoadSuccess:function(data){
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
	});
	//患者信息树的初始化
	function trr(){
	$('#hospitalTree').tree({
			url : '<%=basePath %>inpatient/variationInfo/nurseStation.action',
			method:'get',
			lines : true,
			cache : false,
			animate : true,
			formatter : function(node) {//统计节点总数
				var s = node.text;
				if (node.children.length) {
					s += '&nbsp;<span style=\'color:blue\'>('
							+ node.children.length + ')</span>';
				}
				return s;
			},onClick : function(node) {//点击节点
				var cpId = node.attributes.cpId;
				var versionNo = node.attributes.versionNo;
				$.ajax({
					url:"<%=basePath%>inpatient/variationInfo/queryStageId.action?cpId="+cpId+"&versionNo="+versionNo,
					type:'post',
					success: function(data) {
						for(var i=0;i<data.length;i++){
							stageIdMap.put(data[i].code,data[i].name);
						}
					}
				});
				closeLayout();
				$('#list').datagrid('unselectAll');
				$('#list').datagrid('uncheckAll');
				$('#list').datagrid('clearChecked');
				ListGrid(node.attributes.inpatientNo);
			},onLoadSuccess : function(node, data) {
				$('#hospitalTree').tree('select',$('#hospitalTree').tree('find', 'root').target);
			},onBeforeCollapse:function(node){
				if(node.id=="1"){
					return false;
				}
			}
		});
	}
	
	//list数据初始化
	function ListGrid(inpatientNo){
		//添加datagrid事件及分页
		$('#list').datagrid({
			url:'<%=basePath %>inpatient/variationInfo/queryPathVsIcdList.action',
			queryParams: {
				inpatientNo:inpatientNo,
			}
		});
	}
	
	
	function add(){
		var node = $('#hospitalTree').tree('getSelected');//获取所选节点
		if(node.attributes.inpatientNo){
			var cpId =node.attributes.cpId;
			var versionNo = node.attributes.versionNo;
			var inpatientNo = node.attributes.inpatientNo;
			addAndEdit = 0;
			closeLayout();
			AddOrShowEast('EditForm', '<%=basePath %>inpatient/variationInfo/addVariationInfo.action?inpatientNo='+inpatientNo);
		}else{
			$.messager.alert('提示','请选择具体的患者信息');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	
	function edit(){
		closeLayout();
		var row = $('#list').datagrid('getSelected'); //获取当前选中行                        
	    if(row){
	    	AddOrShowEast('EditForm', '<%=basePath %>inpatient/variationInfo/addVariationInfo.action?id='+row.id);
		}else{
			$.messager.alert('提示','请选择一条要修改的的数据！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	
	function del1(){
		var obj=$('#list').datagrid('getChecked');
		var arr =new Array();
		if(obj.length>0){
			$.each(obj,function(i,n){
				arr[i]=n.id;
				j=i+1;
			});
			
			$.messager.confirm('确认对话框', '您想要删除'+j+'条记录吗？', function(r){
				if (r){
					$.ajax({
						url:'<%=basePath %>inpatient/variationInfo/deleteCpVariation.action',
						type:'post',
						traditional:true,//数组提交解决方案
						data:{'ids':arr},
						dataType:'json',
						success:function(data){
	 						$.messager.alert("提示","删除成功!");
	 						setTimeout(function(){
		  						$(".messager-body").window('close');
		  					},3500);
	 						reload();
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','请选中要删除的行');
			setTimeout(function(){
					$(".messager-body").window('close');
			},3500);
		}
	}
	
	function reload(){
		$('#list').datagrid('reload');
	}
	
	function ok(){
		var row = $('#list').datagrid('getSelected');                     
	       if(row){
	      	   $('#addBed').dialog('close');
	           $('#bedId').val(row.id);
	           $('#showBedNum').val(row.bedName);
		   }
	}
	
	function closebedward() {
		$('#addBedwardWin').panel('close');
	}
	
	//查询
	function searchFrom() {
		var encode = $('#encode').val();
		var state = $('#state').val();
		$('#list').datagrid('load', {
			bedName : encode,
			bedState : state
		});
	}
	
	//获取数据表格选中行的ID checked=0否则是获取勾选行的ID ，获取多个带有拼接''的ID str=0，否则不带有''，    #List 0  1
	function getbachIdUtil(tableID, str, checked) {
		var row;
		if (checked == 0) {
			row = $(tableID).datagrid("getSelections");
		} else {
			row = $(tableID).datagrid("getChecked");
		}
		var dgID = "";
		if (row.length < 1) {
			$.messager.alert("操作提示", "请选择一条记录！", "warning");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
		var i = 0;
		for (i; i < row.length; i++) {
			if (str == 0) {
				dgID += "\'" + row[i].id + "\'";
			} else {
				dgID += row[i].id;
			}
			if (i < row.length - 1) {
				dgID += ',';
			} else {
				break;
			}
		}
		return dgID;
	}
	
	//删除选中table row 
	function del() {
		var rows = $('#list').datagrid("getChecked");
		var copyRows = [];
		for ( var j = 0; j < rows.length; j++) {
			copyRows.push(rows[j]);
		}
		for ( var i = 0; i < copyRows.length; i++) {
			var index = $('#list').datagrid('getRowIndex', copyRows[i]);
			$('#list').datagrid('deleteRow', index);
		}
	}
	
	//获得选中节点 tag=1获取ID tag=0获取nodetype  tag = 2 父节点ID 
	//tag=3 判断选中的是否是叶子节点，如果是叶子节点则获取id，否则赋值1
	function getSelected(tag) {
		var node = $('#hospitalTree').tree('getSelected');//获取所选节点
		if (node != null) {
			var Pnode = $('#hospitalTree').tree('getParent', node.target);
			if (Pnode) {
				if (tag == 0) {
					var nodeType = node.nodeType;
					return nodeType;
				}
				if (tag == 1) {
					var id = node.id;
					return id;
				}
				if (tag == 2) {
					var pid = Pnode.id;
					return pid;
				}
				if (tag == 3) {
					if ($('#hospitalTree').tree('isLeaf', node.target)) {//判断是否是叶子节点
						var id = node.id;
						return id;
					} else {
						return 1;
					}
				}
			}
		} else {
			return 1;
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
		$('#divLayout').layout('add', {
			region : 'east',
			width : '35%',
			split : true,
			border : false,
			href : url,
			closable : true
		});
	}
	
	//科室部门树操作
	function refresh(){//刷新树 
		$('#hospitalTree').tree('reload'); 
	}
	function expandAll(){//展开树
		$('#hospitalTree').tree('expandAll');
	}
	function collapseAll(){//关闭树
		$('#hospitalTree').tree('collapseAll');
	}
	//关闭Layout
	function closeLayout() {
		$('#divLayout').layout('remove', 'east');
	}
	$(function(){
		//回车事件
		bindEnterEvent('searchTree',searchTree,'easyui');
	})
	function searchTree(){
		var nodes = $('#hospitalTree').tree('getChecked');
		if (nodes.length > 0) {                        
			for(var i=0; i<nodes.length; i++){
				$('#hospitalTree').tree('uncheck',nodes[i].target);
			};
		}		
		var queryName = $('#searchTree').textbox('getValue');
		$.ajax({
			url : "<%=basePath %>inpatient/variationInfo/queryDeptLsit.action",
			data:{queryName:queryName},
			type : 'post',
			success : function(data) {
				$('#hospitalTree').tree('collapseAll');//单独展开一个节点,先收缩树
				if(data!=null&&data.length>0){
					var node = $('#hospitalTree').tree('find',data[0].id);
					$('#hospitalTree').tree('expandTo', node.target).tree('select',node.target).tree('scrollTo',node.target); 
				}
			}
		});
	}
	function funBYName(value,row,index){
		if(value!=null&&value!=''){
			return variationCodeMap.get(value);
		}
	}
	function funBYReason(value,row,index){
		if(value!=null&&value!=''){
			return variationFactorCodeMap.get(value);
		}
	}
	function funBYId(value,row,index){
		if(value!=null&&value!=''){
			return stageIdMap.get(value);
		}
	}
	function funBYFx(value,row,index){
		if(value!=null&&value!=''){
			if('1'==value){
				return '正变异';
			}else if('2'==value){
				return '反变异';
			}
		}
	}
</script>
<style type="text/css">
.panel-header{
	border-top:0
}
</style>
</head>
<body style="margin: 0px;padding: 0px">
	<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'west',split:true,tools:'#toolSMId',title:'科室患者信息',border:true" style="width:320px; min-width: 310px;">
			<div id="cc" class="easyui-layout" data-options="fit:true">   
			    <div data-options="region:'north',border:false" style="height:40px;padding-top: 7px;">
			    	<span class="hospitalbedListSearchSize">
			    		<input  class="easyui-textbox" ID="searchTree" style="width:182px" name=""  data-options="prompt:'输入科室信息回车查询'" />
						<a href="javascript:searchTree()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查&nbsp;询&nbsp;</a>
			    	</span>
			    </div>   
			    <div data-options="region:'center',border:false" style="">
			    	<ul id="hospitalTree">数据加载中...</ul>
			    </div>   
			</div>
		</div>
		<div data-options="region:'center',split:false,iconCls:'icon-book',border:true" style="width: 82%;height: 100%;border-top:0">
			<div  style="width: 100%; height: 100%;">
				<input type="hidden" id="node"></input>
				<input type="hidden" id="isdept"></input>
				<table id="list" 
					data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,fit:true,selectOnCheck:false,singleSelect:true,toolbar:'#toolbarId',pagination:true,pageSize:20,pageList:[20,30,50,80,100]">
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true" style=" text-align: center">id</th>
							<th data-options="field:'stageId',formatter:funBYId" style="width: 12% ">变异阶段</th>
							<th data-options="field:'variationCode',formatter:funBYName" style="width: 12% ">变异名称</th>
							<th data-options="field:'variationDate'" style="width: 12% ">变异时间</th>
							<th data-options="field:'variationDirection',formatter:funBYFx" style="width: 14%">变异方向</th>
							<th data-options="field:'variationFactorCode',formatter:funBYReason" style="width: 8%">变异因素</th>
							<th data-options="field:'variationReason'" style="width: 8%">变异原因</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>  
	<div id="dept"></div>
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
			<a href="javascript:void(0)" onclick="del1()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
</body>
</html>