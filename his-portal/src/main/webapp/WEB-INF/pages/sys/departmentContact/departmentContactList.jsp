<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
		var ClassTypeMap=new Map();
		$.ajax({
		    url:  "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action",
		    data:{type:"deptclass"},
			type:'post',
			success: function(data) {
				var type = data;
				for(var i=0;i<type.length;i++){
					ClassTypeMap.put(type[i].encode,type[i].name);
				}
			}
		});
	</script>
	<style type="text/css">
	.panel-header,.layout-body{
		border-top:0;
	}
	.panel-header,.departmentContactEdit{
		border-left:0;
	}
	</style>
</head>
<body>
	<div id="divLayout" class="easyui-layout" class="easyui-layout" fit=true style="width:100%;height:100%;overflow-y: auto;">
		<div id="p" data-options="region:'west',split:true,tools:'#toolSMId'" title="部门科室关系" style="width:290px;padding:0px;overflow: hidden;">
			<div id="toolSMId">
				<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
				<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
			</div>
			  <input type="text" class="easyui-textbox" id="searchTreeInpId" data-options="prompt:'科室名'" style="width: 200px;"/>
  			  <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a>
<!-- 			<input id="searchDept" class="easyui-textbox" style="width: 150px;" data-options="prompt:'拼音,五笔,自定义,编码,名称'"  /> -->
			<div style="width:100%;height:100%;overflow-y: auto;">
				<ul id="tDt"></ul>
			</div>	
		</div>
		<div data-options="region:'center'" style="width:95%;height:95%;border-top:0px;" >
			<input type="hidden" id="treeId" name="treeId">
			<input type="hidden" id="treeName" name="treeName">
			<input type="hidden" id="deptClass">
			<table id="list" style="width: 100%;"
					data-options="url:'${pageContext.request.contextPath}/baseinfo/departmentContact/queryDeptContact.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true, width : '5%'" ></th>
						<th data-options="field:'deptName',width:'10%' ">部门名称</th>
						<th data-options="field:'deptCalss',width:'10%',formatter:formatCheckClass" >部门分类</th>
						<th data-options="field:'spellCode',width:'10%'">拼音码</th>
						<th data-options="field:'wbCode',width:'10%'">五笔码</th>
						<th data-options="field:'userDefinedCode',width:'10%'" >自定义码</th>
						<th data-options="field:'sortId',width:'10%'" >顺序号</th>
						<th data-options="field:'deptEnCode',width:'10%'">英文名</th>
						<th data-options="field:'gradeCode',width:'10%'">节点层级</th>
						<th data-options="field:'validState',width:'7%',formatter:formatCheckState" >状态</th>
						<th data-options="field:'mark',width:'7%'">备注</th>
					</tr>
				</thead>
			</table>
		</div>	
	</div>	
	<div id="dept"></div>
	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="edit()"class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">	
			<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
	</div> 
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>	
<script type="text/javascript">
	//科室下拉框及时定位查询 
	  function searchTreeNodes(){
         var searchText = $('#searchTreeInpId').textbox('getValue');
         $("#tDt").tree("search", searchText);
      }
	var id='';
	//加载科室诊室间关系树
   	$('#tDt').tree({ 
	    url:"<%=basePath %>baseinfo/departmentContact/TreeContact.action",
	    method:'get',
	    animate:false,
	    lines:true,
	    formatter:function(node){//统计节点总数
			var s = node.text;
			if(node.children.length>0){
				if (node.children){
					s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
			}
			return s;
		},onLoadSuccess:function(node,data){
			if(data.length>0){
				$('#tDt').tree('collapseAll');
			}
			
		},
		onSelect: function(node){//点击节点
			$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			 id = node.id;
			$('#list').datagrid('load', {
				id: id
			});
			$('#treeId').val(node.id);  //拿到树的id
			$('#treeName').val(node.text);
			$('#deptClass').val(node.attributes.dclass);  //树的分类
		},onBeforeCollapse:function(node){
			if(node.id=="root"){
				return false;
			}
		}  
	});
  //科室部门树操作
	function refresh() {//刷新树
		$('#tDt').tree('options').url = "<%=basePath %>baseinfo/departmentContact/TreeContact.action";
		$('#tDt').tree('reload');
	}
	function expandAll() {//展开树
		$('#tDt').tree('expandAll');
	}
	function collapseAll() {//关闭树
		$('#tDt').tree('collapseAll');
	}
	//加载页面
	$(function(){
		bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
		$('#list').datagrid({
			pagination:true,
	   		pageSize:20,
	   		pageList:[20,30,50,80,100],
			onLoadSuccess: function (data) {//默认选中
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
				 var rowData = data.rows;
		            $.each(rowData, function (index, value) {
		            	if(value.id == id){
		            		$("#list").datagrid("checkRow", index);
		            	}
		            });
	        },onDblClickRow: function (rowIndex, rowData) {//双击查看
	        	if (getIdUtil("#list").length != 0) {
					AddOrShowEast(
							'EditForm',"<%=basePath%>baseinfo/departmentContact/viewDeptContact.action?deptcontactId="+ getIdUtil("#list"));
				}	
			}, 
	        onBeforeLoad:function(){
	    		 $('#list').datagrid('clearChecked');
	    		 $('#list').datagrid('clearSelections');
	    	}
		});
	});

	//添加
	function add() {
		var treeId = $('#treeId').val();//当前选择的tree id
		var treeName=$('#treeName').val();
		if (treeId == null||treeId==''|| treeId == "root") {
			$.messager.alert('提示','请选择科室！');
			close_alert();
			return false;
		}
		var deptClass=$('#deptClass').val();
		if(deptClass==3){
			$.messager.alert('提示','该节点为终极科室，不能再添加');
			close_alert();
			return false;
		}
		$('#divLayout').layout('remove','east');
		AddOrShowEast('添加','<%=basePath%>baseinfo/departmentContact/addDeptContact.action','post'
				   ,{"treeId":treeId,"treeName":treeName});
	}
	//修改
	function edit() {
		var treeId = $('#treeId').val();//当前选择的tree id
		var treeName=$('#treeName').val();
		if (treeId == null||treeId==''|| treeId == "root") {
			$.messager.alert('提示','请选择科室！');
			close_alert();
			return false;
		}
		var deptClass=$('#deptClass').val();
		if(deptClass==3){
			$.messager.alert('提示','该节点为终极科室，不能再添加');
			close_alert();
			return false;
		}
		$('#divLayout').layout('remove','east');
		var row = $('#list').datagrid("getSelections");
		if(''==row||null==row){
			$.messager.alert("操作提示", "请选择一条用户记录！","warning");
			close_alert();
			return false;
		}
		var treeName=$('#treeName').val();
		if (getIdUtil("#list").length != 0) {
			AddOrShowEast('编辑','<%=basePath%>baseinfo/departmentContact/editDeptContact.action','post'
					   ,{"id":getIdUtil("#list"),"treeName":treeName});
		}
	}
	//删除
	function del() {
		//选中要删除的行
		var rows = $('#list').datagrid('getChecked');
		if (rows.length > 0) {//选中几行的话触发事件	                        
			$.messager.confirm('确认','确定要删除选中信息吗?',function(res) {//提示是否删除
				if (res) {
				    var ids = '';
					for ( var i = 0; i < rows.length; i++) {
					   if (ids != '') {
						  ids += ',';
					   }
						  ids += rows[i].id;
					}
					$.ajax({
						url: "<%=basePath %>baseinfo/departmentContact/delContaction.action?id="+ids,
						type:'post',
						success: function() {
							$.messager.alert('提示','删除成功');
						$('#list').datagrid('reload');
					    $('#tDt').tree('reload');
						}
					});	
				}
			});
		} else {
			$.messager.alert('提示','请选择要删除的记录!');
			close_alert();
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
	function AddOrShowEast(title, url,method,params) {
		if(!method){
			method="get";
		}
		if(!params){
			params={};
		}
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
				method:method,
				queryParams:params,
				closable : true
			});
		}
	}
	//渲染部门分类
	function formatCheckClass(value,row,index){
		if(value!=null&&value!=''){
			return ClassTypeMap.get(value);
		}
	}
	//渲染部门类别
	function formatCheckState(value,row,index){
		if(value==0){
			return '停用';
		}else if(value==1){
			return '在用';
		}else if(value==2){
			return '废弃';
		}else{
			return '在用';
		}
	}
</script>
</body>
</html>