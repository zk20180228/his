<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>知识库添加/编辑</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	//选中节点的level
	var leve;
	//选中节点的知识库分类
	var lib;
	var node = $('#tKonw').tree('getSelected');
	leve = node.attributes.level;
	lib = getLib();
	
	$(function(){
		var id = $('#id').val();
		//如果是添加则显示连续添加按钮，否则不显示
		if(id == null || id == ""){
			$('#addContinue').show();
		}else{
			$('#addContinue').hide();
			$('#cleClass').html("还原");
		}
	});
		/**
		 * 清除页面填写信息
		 */
			function clear(){
				$('#editClassForm').form('reset');
			}
		
			/**
		 * 判断添加/修改表单提交
		 */
		function submit(){ 
			 var url;
			 var id = $('#id').val().toString();
			 var pid = node.id;
			 if(id == null || id == ""){
				 url = "<%=basePath %>emrs/konwledgeBase/add.action?pid=" + pid + "&flag=0&lib=" + lib; 
				 sub(url);
			 }else{
				 url = "<%=basePath %>emrs/konwledgeBase/edit.action?flag=0";
				 sub(url);
			    }
	    }	
			/**
		 * 表单提交
		 */
			function sub(url) {
				$('#editClassForm').form('submit',{  
		        	url: url,  
		        	onSubmit:function(){
						if (!$('#editClassForm').form('validate')) {
							$.messager.alert('提示','请正确输入信息！！');
							setTimeout(function(){$(".messager-body").window('close')},3500);
							return false;
						}
						$.messager.progress({text:'保存中，请稍后...',modal:true});
		        	},  
			        success:function(data){
			        	$.messager.progress('close');
			        	node = $('#tKonw').tree('getSelected');
			        	$.messager.alert('提示','保存成功！！');
			        	closeEast();
			        	$('#tKonw').tree('reload');
			        	$('#list').datagrid('reload');
			        	$('#tKonw').tree('select',node);
			        },
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败！！');
					}							         
		   		}); 
			}
			/**
			 * 连续添加
			 */
			function addContinue() {
				var pid = node.id;
				$('#editClassForm').form('submit',{
					url : "<%=basePath %>emrs/konwledgeBase/add.action?pid="+pid + "&flag=0&lib=" + lib,
					onSubmit:function(){
						if (!$('#editClassForm').form('validate')) {
							$.messager.alert('提示','请正确输入信息！！');
							setTimeout(function(){$(".messager-body").window('close')},3500);
							return false;
							}
							$.messager.progress({text:'保存中，请稍后...',modal:true});
						},
					success:function(data){
						$.messager.progress('close');
						node = $('#tKonw').tree('getSelected');
			        	$.messager.alert('提示','保存成功！！');
						$('#editClassForm').form('clear');
			        	$('#tKonw').tree('reload');
			        	$('#list').datagrid('reload');
			        	$('#tKonw').tree('select',node);
						},
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败！！');
						}
				}); 
			}
			//得到选中节点知识库分类
			function getLib(){
				if(leve != null && leve == "0"){
					return node.id;
				}else if(leve != null){
					return $('#tKonw').tree('getRoot').id;
				}
			}
	</script>
</head>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'分类添加/编辑',iconCls:'icon-form',border: false">
		<div style="padding:10px; ">
			<form id="editClassForm" method="post">
				<input type="hidden" id="id" name="konwledgeBase.id" value="${konwledgeBase.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
						<td class="honry-lable">名称:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="konwName" name="konwledgeBase.konwName" value="${konwledgeBase.konwName }" data-options="required:true"  style="width:200px" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">自定义码:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="konwInputCode" name="konwledgeBase.konwInputCode" value="${konwledgeBase.konwInputCode }" data-options="required:true" style="width:200px" /></td>
	    			</tr>
				</table>
				<div style="text-align:center;padding:5px">
				<shiro:hasPermission name="${menuAlias}:function:add">
					<a href="javascript:addContinue();" class="easyui-linkbutton" id="addContinue" data-options="iconCls:'icon-save'">连续添加</a>
			    </shiro:hasPermission>
			    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" id="cleClass">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="clos()">关闭</a>
			    </div>
			</form>
		</div>
	</div>
</body>