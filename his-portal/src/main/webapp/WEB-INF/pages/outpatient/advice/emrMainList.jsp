<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>电子病历列表</title>
<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
	<script type="text/javascript">
	//加载页面
	$(function(){
		//加载datagrid
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			//url:'<%=basePath%>emrs/writeMedRecord/queryEmrMainList.action?menuAlias=${menuAlias}',
			data:[],
			onBeforeLoad:function(param){
				if(param.patId == undefined && parent.window.getIdCardNo() == ''){
					$.messager.alert('提示',"未选择患者！");
					setTimeout(function(){$(".messager-body").window('close')},1500);
					return false;
				}else{
					if(param.erType == undefined && parent.window.getErType() != ''){
						param.erType = parent.window.getErType();
					}
					if(param.patId == undefined && parent.window.getIdCardNo() != ''){
						param.patId = parent.window.getIdCardNo();
					}
					param.state = '0';
					$('#list').datagrid('clearChecked');
					$('#list').datagrid('clearSelections');
					$("#list").datagrid({
						url:'<%=basePath%>emrs/writeMedRecord/queryEmrMainList.action?menuAlias=${menuAlias}',
					});
					
				}
				
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
				}
			}
		});
		
	});
	
	/**
	* 提交审核
	*/
	function tick(){
		var rows = $('#list').datagrid('getChecked');   
		var ids = '';
		if(rows == null || rows == 'undefined' || rows.length <1){
			$.messager.alert('提示',"请选择要提交的病历！");
			setTimeout(function(){$(".messager-body").window('close')},1500);
			return;
		}else{
			for(var i = 0; i < rows.length; i ++){
				if(rows[i].emrState != 0){
					$.messager.alert('提示',rows[i].tempName + "已经提交审检，不能重复提交！");
					setTimeout(function(){$(".messager-body").window('close')},1500);
					return;
				}
				if(ids != ''){
					ids += ',' + rows[i].id;
				}else{
					ids = rows[i].id;
				}
			}
			parent.tick(ids);
		}
	}
	
	function add(){
		parent.add();
	}
	
	function edit(){
		var row = $('#list').datagrid('getSelected');   
		if(row == null || row == 'undefined'){
			$.messager.alert('提示',"请选择要修改的病历！！！");
			setTimeout(function(){$(".messager-body").window('close')},1500);
			return;
		}
		if(row.emrState != 0){
			$.messager.alert('提示',"您选择的病历已经提交审检，无法继续修改！");
			setTimeout(function(){$(".messager-body").window('close')},1500);
			return;
		}
		var idd = row.id;
		parent.edit(idd);
	}
	
	function del(){
		var rows = $('#list').datagrid('getChecked', 'name');   
		if(rows == null || rows == 'undefined' || rows.length < 1){
			$.messager.alert('提示',"请选择要删除的病历！！！");
			setTimeout(function(){$(".messager-body").window('close')},1500);
			return;
		}
		var idd = '';
		for(var i = 0; i < rows.length; i++){
			if(rows[i].emrState != 0){
				$.messager.alert('提示',rows[i].tempName + "已经提交审检，无法删除！");
				setTimeout(function(){$(".messager-body").window('close')},1500);
				return;
			}
			idd += rows[i].id + ",";
		}
		idd += rows[rows.length-1].id;
		 $.messager.confirm('确认', '确定要删除选中信息吗?', function(res){
			   if (res){
				   parent.del(idd); 
			   }
		 })
		
	}
	
	function reload(){
		$("#list").datagrid('uncheckAll');
		$("#list").datagrid('unselectAll');
		$("#list").datagrid('reload');
	}
	function load(patId,erType){
		$("#list").datagrid('uncheckAll');
		$("#list").datagrid('unselectAll');
		$("#list").datagrid('load',{patId: patId,erType: erType});
	}
	function formattState(value,row,index){
		if(value == 0){
			return '编辑';
		}
		if(value == 1){
			return '提交';
		}
		if(value == 2){
			return '上级医师审检通过';
		}
		if(value == 3){
			return '主任医师审检通过';
		}
	}
	</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div id="divLhmMessage" data-options="region:'center',border:false"  fit=true>
			<input type="hidden" name="flag" value="Mainlist" id="flag">
			<table id="list"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th data-options="checkbox:true,field : 'ck'"></th>
						<th data-options="field:'emrSN',width:'13%',align:'center'">流水号</th>
						<th data-options="field:'patientName',width:'14%',align:'center'">患者姓名</th>
						<th data-options="field:'tempName',width:'14%',align:'center'">模板名称</th>
						<th data-options="field:'typeName',width:'14%',align:'center'">病历类型</th>
						<th data-options="field:'emrState',width:'14%',align:'center',formatter:formattState">状态</th>
						<th data-options="field:'emrScore',width:'14%',align:'center'">分数</th>
						<th data-options="field:'emrLevel',width:'14%',align:'center'">级别</th>
						<th data-options="field:'strContent',hidden:'true' ,align:'center',">模板内容</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="toolbarId">
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		<a href="javascript:void(0)" onclick="tick()" class="easyui-linkbutton" data-options="iconCls:'icon-tick',plain:true">提交</a>
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>	
		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</div>
</body>