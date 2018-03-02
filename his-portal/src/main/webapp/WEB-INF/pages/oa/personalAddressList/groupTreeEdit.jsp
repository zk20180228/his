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
</head>
<body>
      	<div align="center" class="easyui-panel" style="padding:10px">
		<form id="treeEditForm" method="post" >
			<input type="hidden" id="id" name="personalAddress.id" value="${id }">
			<input type="hidden" id="parentCode" name="personalAddress.parentCode" value="${parentCode}">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
	    			<td>分组名称:</td>
	    			<td><input class="easyui-textbox" type="text" id="groupName" name="personalAddress.groupName" value="${groupName}" data-options="required:true"/></td>
	    		</tr>
				<tr>
					<td colspan="2" align="center">
   					<shiro:hasPermission name="${menuAlias}:function:save"> 
   					<a href="javascript:void(0)" data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="submitTreeForm()">保存</a>
   					</shiro:hasPermission>
   					<a href="javascript:void(0)" data-options="iconCls:'icon-clear'" class="easyui-linkbutton" onclick="clearTreeForm()">清除</a>
   					<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" class="easyui-linkbutton" onclick="closeDialog()">关闭</a>
   					</td>
				</tr>
			</table>	
		</form>
	</div>
	<script type="text/javascript">
		$(function(){
		
			
		});
	
	//提交验证
	function submitTreeForm(){
					$('#treeEditForm').form('submit', {
						url : '<%=basePath %>oa/personalAddressList/saveGroup.action',
						data:$('#treeEditForm').serialize(),
				        dataType:'json',
						onSubmit : function() {
							if(!$('#treeEditForm').form('validate')){
								$.messager.show({  
							         title:'提示信息' ,   
							         msg:'验证没有通过,不能提交表单!'  
							    }); 
							       return false ;
							}
							$.messager.progress({text:'保存中，请稍后...',modal:true});
						},
						success : function(data) {
							var json = eval('(' + data + ')'); 
							$.messager.progress('close');
							$.messager.alert("提示",json.resMsg);
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							closeDialog();
							refresh();
							$("#list").datagrid("reload");
						}
					}); 
		
	}
	//清除所填信息
	function clearTreeForm(){
		$('#treeEditForm').form('reset');
	}
	
</script>	
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	
<style  type="text/css">
  .window .panel-header .panel-tool .panel-tool-close{
  	  	background-color: red;
  	}
 </style>
</body>

</html>