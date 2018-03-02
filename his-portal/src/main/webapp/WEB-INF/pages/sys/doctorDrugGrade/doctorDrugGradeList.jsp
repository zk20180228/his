<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
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
	border:0;
}
</style>
</head>
<body>
	<div id="divLayout" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" style="height:40px;width:100%;padding:5px 5px 0px 5px;">
		<table style="width:100%;">
			<tr>
				<td>查询条件：
					<input  id="codes" data-options="prompt:'职级,职级名称,等级,等级名称'"  class="easyui-textbox"  style="width: 200px" onkeyup="KeyDown()"/>
					<shiro:hasPermission name="YPDJYSZJDZ:function:query">
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',border:false" style="height:90%;width:100%;">
		<table id="list" data-options='fit:true'>
			<thead>
				<tr>
					<th data-options="field:'id',checkbox:true" style="width: 10%; text-align: center">id</th>
					<th data-options="field:'tpost'"  style="width:6%" >医生职级</th>
					<th data-options="field:'postname'" style="width:10%">职级名称</th>
					<th data-options="field:'druggraade'" style="width:6% ">药品等级</th>
					<th data-options="field:'graadename'" style="width:10%">药品等级名称</th>
					<th data-options="field:'order1'"  style="width:6% " >排序</th>
					<th data-options="field:'useFlag',formatter:isOrNoUse"  style="width:10% ">是否适用</th>
					<th data-options="field:'hospitalId',formatter:funhospitalMap"  style="width:15% ">医院</th>
					<th data-options="field:'description'" formatter="funlimit" style="width:34%">说明</th>
				</tr>
			</thead>
		</table>
	</div>  
</div>
<div id="toolbarId">
<shiro:hasPermission name="YPDJYSZJDZ:function:add">
	<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
</shiro:hasPermission>
<shiro:hasPermission name="YPDJYSZJDZ:function:edit">
	<a href="javascript:void(0)" onclick="edit(1)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
</shiro:hasPermission>
<shiro:hasPermission name="YPDJYSZJDZ:function:delete">		
	<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
</shiro:hasPermission>
</div>
<script>
	var hospitalMap=null;
	
	$(function(){
		var winH=$("body").height();
		$('#list').height(winH-78-30-27-22-20+5);
		
		//渲染医院
		$.ajax({
			url:'<%=basePath%>renderingHospital.action',
			type:'post',
			success: function(payData) {
				hospitalMap = payData;
			}
		});
		
		ListGrid();
		bindEnterEvent('codes',searchFrom,"easyui");
		
    });
	
	//渲染医院
	function funhospitalMap(value,row,index){
		if(value!=null&&value!=''){
			return hospitalMap[value];
		}else{
			return value;
		}
	}
	//是否适用
	function isOrNoUse(value,row,index){
		if(value=="1"){
			return "是";
		}else if(value=="0"){
			return "否";
		}else{
			return "";
		}
	}
	//加载信息
	function ListGrid(){
		$('#list').datagrid({
			url:'<%=basePath %>queryGradeAll.action',
			selectOnCheck:false,
			rownumbers:true,
			idField: 'id',
			toolbar:'#toolbarId',pagination:true,pageSize:20,fitColumns:false,singleSelect:true,checkOnSelect:true, 
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onLoadSuccess : function(data) {//默认选中
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
			},
			onDblClickCell: function(){
				view();
			}
			
		});
	}
	function funlimit(value,row,index){
		if(row.description!=null&&row.description!=""){
			if(row.description.length>50){
				return row.description.substring(0, 50)+"......"; 
			}else{
				return row.description;
			}
		}
	}
	
    function add(){
    	closeLayout();
		AddOrShowEast1('EditForm',"<%=basePath%>addContrastURI.action",600);
	}
	// 1表示修改；	
	function edit(paramType){
		closeLayout();
		var row = $('#list').datagrid('getSelected'); //获取当前选中行
		if(row==null){
			$.messager.alert('提示','请选择一条记录！','info');
			close_alert();
			return false;
		}
        if(row){
        	AddOrShowEast1('EditForm','<%=basePath%>getContrast.action?id='+row.id+'&paramType='+paramType,600);
        }
	}
	/**
	 * 查看
	 * @author  huangbiao
	 * @date 2016-4-5
	 * @version 1.0
	 */
	function view(){
		closeLayout();
		var row = $('#list').datagrid('getSelected'); //获取当前选中行 
        if(row){
        	AddOrShowEast('ViewForm','<%=basePath%>ViewContrast.action?useFlag='+row.useFlag+'&hospitalId='+row.hospitalId+'&id='+row.id);
        }
	}
   //删除	
	function del(){
		closeLayout();
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
						url:'<%=basePath%>delContrast.action',
						type:'post',
						traditional:true,//数组提交解决方案
						data:{'ids':arr},
						dataType:'json',
						success:function(data){
							$.messager.alert('提示',data);
		 					$("#list").datagrid("reload");
						},error : function(a,b,c) {
							$.messager.alert('提示',"******"+a+"*****"+b+"********"+c);
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','请勾选要删除的行');
			close_alert();
		}
	}
	
	function reload(){
		//实现刷新栏目中的数据
		$('#list').datagrid('reload');
	}    
	//清除所填信息
	function clear(){
		$('#editForm').form('clear');
	}
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
				
	/**
	 * 查询
	 * @author  wfj
	 * @date 2015-12-29
	 * @version 1.0
	 */
	function searchFrom() {
		var code = $('#codes').val();
		$('#list').datagrid('load', {
			code : code
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
		$('#divLayout').layout('add', {
			region : 'east',
			width : 500,
			split : true,
			border : false,
			href : url,
			closable : true
		});
	}
	function AddOrShowEast1(title, url,width) {
		$('#divLayout').layout('add', {
			region : 'east',
			width : width,
			split : true,
			border : false,
			href : url,
			closable : true
		});
	}
	
	/**
	 * 回车键查询
	 * @author  lt
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-05-27
	 * @version 1.0
	 */
	function KeyDown()  
	{  
	    if (event.keyCode == 13)  
	    {  
	        event.returnValue=false;  
	        event.cancel = true;  
	        searchFrom();   
	    }  
	} 
	
	// 药品列表查询重置
	function searchReload() {
		$('#codes').textbox('setValue','');
		searchFrom();
	}
	</script>
	</body>
</html>