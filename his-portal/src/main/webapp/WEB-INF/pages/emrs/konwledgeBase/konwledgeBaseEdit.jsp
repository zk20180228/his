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

</head>
<body>
	<div class="easyui-panel" data-options="iconCls:'icon-form',border: false">
		<div style="padding:10px;">
			<form id="editForm" method="post">
				<input type="hidden" id="id" name="konwledgeBase.id" value="${konwledgeBase.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" style="width:90%; border:0">
					<tr>
						<td class="honry-lable">名称:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="konwName" name="konwledgeBase.konwName" value="${konwledgeBase.konwName }" data-options="required:true"  /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">自定义码:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="konwInputCode" name="konwledgeBase.konwInputCode" value="${konwledgeBase.konwInputCode }" data-options="required:true"/></td>
	    			</tr>
	    			<tr >
						<td class="honry-lable">内容:</td>
		    			<td  id = "td_cont">
		    				<textarea id="editor" >${konwledgeBase.strContent}</textarea>
		    				<input type="hidden" id="strContent" name="konwledgeBase.strContent"/>
		    			</td>
	    			</tr>
				</table>
				<div style="text-align:center;padding:5px">
				<shiro:hasPermission name="${menuAlias}:function:add">
					<a href="javascript:addContinue();" class="easyui-linkbutton" id="addContinue" data-options="iconCls:'icon-save'">连续添加</a>
			    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    </shiro:hasPermission>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" id="cle">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	//选中节点的level
	var leve;
	//选中节点的知识库分类
	var lib;
	var node = $('#tKonw').tree('getSelected');
	leve = node.attributes.level;
	lib = getLib();
	var width = $('#konwWins').width * 0.8;
	/**
	 * 初始化编辑器
	 */
	var editor = UE.getEditor("editor",{
		initialFrameWidth: null,
	     initialFrameHeight: 400,
	     toolbars:[[
	        	    'fullscreen', 'source', '|', 'undo', 'redo', '|','bold', 'italic', 'underline', 'fontborder', 
	        	    'strikethrough',  'removeformat', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist',
	        	    '|', 'fontfamily', 'fontsize', '|', 'indent', '|', 'justifyleft', 'justifycenter', 'justifyright', 
	        	    'justifyjustify', '|', 'link', 'unlink', '|', 'horizontal', 'spechars', 'wordimage', '|','inserttable', 
	        	    'deletetable', 'mergecells', 'splittocells']],
	     focus: true,
	});
	
	$(function(){
		var id = $('#id').val();
		//如果是添加则显示连续添加按钮，否则不显示
		if(id == null || id == ""){
			$('#addContinue').show();
		}else{
			$('#addContinue').hide();
			$('#cle').html("还原");
		}
	});
		/**
		 * 清除页面填写信息
		 */
			function clear(){
				$('#editForm').form('reset');
				editor.setContent('${konwledgeBase.strContent}');
			}
		/* 
		 * 关闭界面
		 */
			function closeLayout(){
				$('#konwWins').dialog('close'); 
				$('#tKonw').tree('reload');
			}
			
			/**
		 * 判断添加/修改表单提交
		 */
		function submit(){ 
			 var url;
			 var id = $('#id').val().toString();
			 var pid = node.id;
			 if(id == null || id == ""){
				 url = "<%=basePath %>emrs/konwledgeBase/add.action?pid=" + pid + "&flag=1&lib=" + lib; 
				 sub(url);
			 }else{
				 url = "<%=basePath %>emrs/konwledgeBase/edit.action?flag=1";
				 sub(url);
			    }
	    }	
			/**
		 * 表单提交
		 */
			function sub(url) {
				$('#editForm').form('submit',{  
		        	url: url,  
		        	onSubmit:function(){
						if (!$('#editForm').form('validate')) {
							$.messager.alert('提示',"验证不通过");
							setTimeout(function(){$(".messager-body").window('close')},3500);
							return false;
						}
							//将内容赋值
					    	var chtml = UE.getEditor('editor').getContent();
					    	$('#strContent').val(chtml);
					    	if(chtml.length == 0){
					    		$.messager.alert('友情提示','请输入内容!','warning');
					    		setTimeout(function(){$(".messager-body").window('close')},3500);
					    		return false;
						}
					   $.messager.progress({text:'保存中，请稍后...',modal:true});
		        	},  
			        success:function(data){  
			        	$.messager.progress('close');
			        	$.messager.alert('提示','保存成功');
			        		$('#tKonw').tree('reload');
			        		$('#list').datagrid('reload');
				        	closeLayout();
			        },
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败！');	
					}							         
		   		}); 
			}
			/**
			 * 连续添加
			 */
			function addContinue() {
				var pid = node.id;
				$('#editForm').form('submit',{
					url : "<%=basePath %>emrs/konwledgeBase/add.action?pid="+pid + "&flag=1&lib=" + lib,
					onSubmit:function(){
						if (!$('#editForm').form('validate')) {
							$.messager.alert('提示','请正确输入信息！！');
							setTimeout(function(){$(".messager-body").window('close')},3500);
							return false;
							}
							//将内容赋值
					    	var chtml = UE.getEditor('editor').getContent();
					    	$('#strContent').val(chtml);
					    	if(chtml.length == 0){
					    		$.messager.alert('友情提示','请输入内容!','warning');
					    		setTimeout(function(){$(".messager-body").window('close')},3500);
					    		return false;
							}
					    	$.messager.progress({text:'保存中，请稍后...',modal:true});
						},
					success:function(data){
						$.messager.progress('close');
						$('#tKonw').tree('reload');
						editor.setContent("");
						$('#editForm').form('clear');
						$('#list').datagrid('reload');
						},
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败！');
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
</body>