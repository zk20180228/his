<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<style type="text/css">
.panel-header,.panel-body{
	border-top:0;
}
</style>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'west',split:true" title="合同单位管理" style="width: 10%;height: 100%;padding:5px;">
			<div id="p" style="width: 100%;height: 100%;">
				<ul id="tDt"></ul> 
			</div>
		</div>
		<div  id="divLayout2" data-options="region:'center'" title="挂号费用维护" style="width: 86%;height: 100%;">
			<div id="divLayout" class="easyui-layout" data-options="fit:true">
			<div style="width:100%;height:100%;">
				<input type="hidden" id="treeCode"> 
				<table id="list" class="easyui-datagrid" style="width:100%;" data-options="url:'${pageContext.request.contextPath }/finance/registerFee/queryRegisterFee.action?menuAlias=${menuAlias}',singleSelect: true,toolbar:'#toolbarId',checkOnSelect:false,selectOnCheck:false,fit:true,border:false">
					<thead> 
						<tr>
							<th data-options="field:'ck',checkbox:true, width : '5%'" ></th>
						    <th data-options="field:'registerGrade',formatter:formatCheckBox"  style="width:15%" >挂号级别</th>
							<th data-options="field:'registerFee'"  style="width:15%">挂号费</th>
							<th data-options="field:'checkFee'"  style="width:15%">检查费</th>
							<th data-options="field:'treatmentFee'"  style="width:15%">治疗费</th>
							<th data-options="field:'otherFee'" style="width:15%">其他费</th>
							<th data-options="field:'description'" style="width: 20%">说明</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>	
		</div>
		<div id="dept"></div>
		<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias }:function:add">
			<a href="javascript:void(0)" onclick="addFee()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias }:function:edit">
			<a href="javascript:void(0)" onclick="editFee()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
		</shiro:hasPermission>	
		<shiro:hasPermission name="${menuAlias }:function:delete">
			<a href="javascript:void(0)" onclick="delFee()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
		</div>
		
	<!-- 合同单位树的右键菜单 -->	
	<div id="tDtmm" class="easyui-menu" style="width:100px;">
		<shiro:hasPermission name="${menuAlias }:function:tadd">
			<div onclick="addDept()" data-options="iconCls:'icon-add'">添加合同单位</div>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias }:function:tedit">
			<div onclick="editDept()" data-options="iconCls:'icon-edit'">修改合同单位</div>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias }:function:tdelete">
			<div onclick="delDetp()" data-options="iconCls:'icon-remove'">移除合同单位</div>
		</shiro:hasPermission>
		<div onclick="viewDetp()" data-options="iconCls:'icon-search'">查看合同单位</div>
	</div>
	<!-- 合同单位树的右键菜单 -->	
<script type="text/javascript">

	//渲染挂号级别	
	function formatCheckBox(value,row,index){
		for(var i=0;i<gradeName.length;i++){
			if(value==gradeName[i].code){
				return gradeName[i].name;
			}
		}
	}
	var gradeName = "";
	var editIndex = undefined;
	$(function(){
		//添加edatagrid事件及分页
		$('#list').datagrid({
			pagination:true,
	   		pageSize:20,
	   		pageList:[20,30,50,100],
	   		onBeforeLoad : function (param) {//加载数据
		   		$.ajax({
					url: "<c:url value='/finance/registerFee/gradeFeeCombobox.action'/>",
					type:'post',
					success: function(gradeData) {
						gradeName = gradeData;
					}
				});	
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
		bindEnterEvent('gradeCombobox',searchFrom);
	  });
	//添加费用
	function addFee(){
		var treeCode= $('#treeCode').val();
	    if(treeCode==""||treeCode=="1"){
	    	$.messager.alert('提示',"请选择具体单位！");
		    return false;
	    }
	    AddOrShowEast('',"<%=basePath%>finance/registerFee/feeAdd.action?treeCode="+treeCode);
	}
	//修改费用
	function editFee(){
		var treeCode= $('#treeCode').val();
	    if(treeCode==""||treeCode=="1"){
	    	$.messager.alert('提示',"请选择具体单位！");
		    return false;
	    }
		if (getIdUtil("#list").length != 0) {
			AddOrShowEast('',"<c:url value='/finance/registerFee/feeEdit.action'/>?feeId="+getIdUtil("#list")+"&treeCode="+treeCode);
		}
	}
	//删除收费标准
	function delFee(){
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
						$.messager.progress({text:'保存中，请稍后...',modal:true});
						$.ajax({
							url: "<c:url value='/finance/registerFee/delRegisterFee.action'/>?id="+ids,
							type:'post',
							success: function(date) {
								$.messager.progress('close');
								if(date.resCode=='success'){
									$.messager.alert('提示','删除成功');
									$('#list').datagrid('reload');
								}else{
									$.messager.alert('提示','删除失败');
								}
								
							}
						});
					}
                });
             }
	}
	/**
	 * 查询
	 * @author  sunshuo
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-05-21
	 * @version 1.0
	 */
		function searchFrom() {
			var registerGrade = $('#gradeCombobox').combobox('getValue');
			$('#list').datagrid('load', {
				registerGrade : registerGrade
			});
			
		}
   	//获得选中id	
	function getId(tableID,str){
		var row = $(tableID).datagrid("getChecked");  
		var dgID = "";
		if(row.length < 1){
			$.messager.alert("操作提示","请选择一条记录！","warning");
		}
		var i = 0;
		for(i;i < row.length;i++){
			if(str = 0){
				dgID += "\'" + row[i].BED_ID + "\'";
			}else{
				dgID += row[i].BED_ID;
			}
			if(i < row.length - 1){
				dhID += ',';
			}else{
				break;
			}
		}
		return dgID;
	}
			
	//加载部门树
   	$('#tDt').tree({    
	    url:"<c:url value='/baseinfo/businessContractunit/treeBusinessContractunit.action'/>",
	    method:'get',
	    animate:true,
	    lines:true,
	    formatter:function(node){//统计节点总数
			var s = node.text;
			if (node.children){
				if(node.children.length>0){
					s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
		    }
			return s;
		},onContextMenu: function(e,node){//添加右键菜单
			e.preventDefault();
			$(this).tree('select',node.target);
			$('#tDtmm').menu('show',{
				left: e.pageX,
				top: e.pageY
			});
		},onClick: function(node){//点击节点
			$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			$('#treeCode').val(node.id);
			$('#list').datagrid('load', {
				id: node.id
			});
			$('#modelWeek').val();
		}
	}); 
		   	
	   	//合同单位树操作
	   	function addDept(){//添加部门科室
	   		var id = getSelected();
	   		Adddilog("添加分类","<c:url value='/baseinfo/businessContractunit/addTreeBusinessContractunit.action'/>?id="+id);
		}
	   	function editDept(){//修改部门科室
	   		var id = getSelected();
	   		Adddilog("编辑分类","<c:url value='/baseinfo/businessContractunit/eidtTreeBusinessContractunit.action'/>?id="+id);
		}
		function viewDetp(){//添加部门科室
	   		var id = getSelected();
	   		Adddilog("查看分类","<c:url value='/baseinfo/businessContractunit/viewTreeBusinessContractunit.action'/>?id="+id);
		}
	   	function delDetp(){//移除部门科室
	   		$.messager.confirm('确认', '确定要删除选中信息吗?', function(r){//提示是否删除
				if(r){
					$.ajax({
						url: "<c:url value='/baseinfo/businessContractunit/delTreeBusinessContractunit.action'/>?id="+getSelected(),
						type:'post',
						success: function() {
							$.messager.alert('提示','删除成功!');
							$('#tDt').tree('reload');
						}
					});	
				}
	   												
			});
		}
		   	
		//树的增删改查加载dialog
		function Adddilog(title, url) {
			$('#dept').dialog({    
			    title: title,    
			    width: '900px',    
			    height:'60%',    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			});    
		}
		//打开dialog
		function openDialog() {
			$('#dept').dialog('open'); 
		}
		//关闭dialog
		function closeDialog() {
			$('#dept').dialog('close');  
		}

		function getSelected(){//获得选中节点
			var node = $('#tDt').tree('getSelected');
			if (node){
				var id = node.id;
				return id;
			}
		}
			
		
       		
      		/**
		 * 动态添加LayOut
		 * @author  liujl
		 * @param title 标签名称
		 * @param url 跳转路径
		 * @date 2015-05-21
		 * @modifiedTime 2015-6-18
		 * @modifier liujl
		 * @version 1.0
		 */
// 		 region : 'east',
//			width : 580,
//			split : true,
//			href : url,
//			closable : true
		function AddOrShowEast(title, url) {
			var eastpanel=$('#panelEast'); //获取右侧收缩面板
			if(eastpanel.length>0){ //判断右侧收缩面板是否存在
				//重新装载右侧面板
		   		$('#divLayout').layout('panel','east').panel({
                          href:url 
                   });
			}else{//打开新面板
				$('#divLayout').layout('add', {
					title : title,
					region : 'east',
					width : 430,
					href : url,
					closable : false,
					collapsible:false
					
				});
			}
		}
		
		//树操作
	function refresh() {//刷新树
		$('#tDt').tree('options').url = "<c:url value='/baseinfo/businessContractunit/treeBusinessContractunit.action'/>?treeAll=" + false;
		$('#tDt').tree('reload');
	}
</script>
</body>
</html>