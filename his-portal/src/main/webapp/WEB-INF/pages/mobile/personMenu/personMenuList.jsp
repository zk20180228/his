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
<script type="text/javascript" src="<%=basePath%>easyui1.4.5/datagrid-dnd.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
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
.panel-noscroll{
	border-right:0;
}
</style>
</head>
<body>
	<div id="divLayout" class="easyui-layout"  fit="true" >
		<div  data-options="region:'north',split:false" style="width: 100%; height: 35px;border-top:0">
			<table style="width:100%;border:false;padding-top: 2px;">
				<tr>
					<td>用户:
						<input id="userId">
						栏目:
						<input id="menuId">
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',split:false" style="width: 100%;border-top:0;height: 100%">
 			<table id="list" > 
			</table>  
		</div>
	</div>
	<div id="toolbarId">
		<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">保存</a>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<script type="text/javascript">
	$(function(){
		$('#userId').combogrid({
			idField:'account',
			textField:'name',
			mode:'remote',
			panelWidth:600,
			panelHeight:560,
			striped:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			pagination:true,
			pageNumber:1,
			pageSize:20,
			pageList:[20,30,50,100],
			url:'<%=basePath %>mosys/personMenu/queryUserList.action',
			columns:[[
				{field:'account',title:'账户',width:80},
				{field:'name',title:'姓名',width:90},
				{field:'nickName',title:'曾用名',width:100},
				{field:'phone',title:'手机',width:100},
				{field:'email',title:'电子邮箱',width:120},
				{field:'remark',title:'备注',width:100}
			]],
			onSelect:function(index,row){
				var parentId = $('#menuId').combobox('getValue');
				if(parentId){
					$('#list').edatagrid('load',{parentId : parentId, userAccount : row.account});
				}
			} 
		});
		$('#userId').combogrid('textbox').bind('focus',function(){
			$('#userId').combogrid('showPanel');
		});
		
		$('#menuId').combobox({
			url : '<%=basePath %>mosys/personMenu/queryMobileParentMenu.action',
			valueField : 'id',
			textField : 'name',
			editable : false,
			onSelect : function(record){
				var userAccount = $('#userId').combogrid('getValue');
				if(userAccount){
					$('#list').edatagrid('load',{parentId : record.id, userAccount : userAccount});
				}
			}
		});
		$('#menuId').combobox('textbox').bind('focus',function(){
			$('#menuId').combobox('showPanel');
		});
		$('#list').edatagrid({
			url : '<%=basePath %>mosys/personMenu/queryMobileMenuList.action',
			fitColumns : true, //自适应
			idField : "id",//指定id字段为行标记字段,复选框选中的值
			rownumbers : true,//行号
			height : 'auto',
			singleSelect:true,
			toolbar:'#toolbarId',
			autoSave:true,
			fit:true,
			columns:[[
					{field:'id',title:'主键',hidden:true},
					{field:'menuName',title:'栏目名',width:100},
					{field:'rmIsVisible',title:'是否隐藏',formatter:formatVisible,width:100,
						editor : 
						{
							type:'combobox',
							options:{
								data:[{id : 0, text : "显示"},{id : 1, text : "隐藏"}],
								valueField:'id',
								textField:'text',
								editable : false
							}
						}
					},
					{field:'menuAlias',title:'别名',width:100},
					{field:'menuParameter',title:'参数',width:100},
					{field:'menuDescription',title:'说明',width:100},
					{field:'rmMenuOrder',title:'栏目序号',hidden:true},
					{field:'userAcc',title:'用户账号 ',hidden:true}
				]],
			onBeforeLoad: function(param){
				if(param == undefined || param.parentId == null || param.userAccount == null || param.parentId == "" || param.userAccount == ""){
					return false;
				}
			},
			onLoadSuccess:function(){
				$(this).datagrid('enableDnd');
			}
		});
		
	})
	/**
	 * 刷新方法
	**/
	function reload(){
		$('#list').edatagrid('reload');
	}
	/**
	 * 渲染是否隐藏
	**/
	function formatVisible(value,row,index){
		if(value == 1){
			return '隐藏';
		}else{
			return '显示';
		}
	}
	/**
	 * 重置方法
	**/
	function clears(){
		$('#menuId').combobox("setValue","");
		$('#userId').combogrid("setValue","");
		$('#list').edatagrid('loadData',[]);
	}
	/**
	 * 保存方法
	**/
	function save(){
		var rows = $('#list').edatagrid('getRows');
		if(rows.length > 0){
			for(var i = 0; i < rows.length; i++){
				rows[i].rmMenuOrder = i;
			}
			var userAccount = $('#userId').combogrid('getValue');
			var parentId = $('#menuId').combobox('getValue');
			var infoJson = JSON.stringify(rows);
			$.messager.progress({text:'保存中，请稍后...',modal:true});	
			$.post('<%=basePath %>mosys/personMenu/saveMenus.action', {"infoJson" : infoJson, "userAccount" : userAccount, "parentId" : parentId},
				function(res) {
					$.messager.progress('close');
					$.messager.alert("操作提示", res.resMsg);
					if (res.resCode == 'success') {
						$("#list").datagrid("reload");
					}
			});
		}else{
			$.messager.alert("操作提示", "列表中无数据，无法保存！");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	
	</script>
	</body>
</html>