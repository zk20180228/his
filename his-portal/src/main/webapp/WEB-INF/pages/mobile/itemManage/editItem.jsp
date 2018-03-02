<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp" %>
</head>
	<body>
		<div class="easyui-panel" id = "panelEast" style="padding: 10px;" data-options="fit:true,border:false"  style="overflow :'hidden'">
			<div style="width: 100%;height: 150px">
	    		<form id="editForm" method="post" >
					<input type="hidden" id="id" name="tmItems.id" value="${item.id }">
					<input type="hidden" id="code" name="tmItems.code" value="${item.code }">
					<input type="hidden" id="createTime" name="tmItems.createTime" value="${item.createTime }">
					<input type="hidden" id="parentId" name="tmItems.parentId" value="${id }">
					<input type="hidden" id="typeName" name="tmItems.typeName" value="${item.typeName }">
					<input type="hidden" id="isParent" name="tmItems.isParent" value="${item.isParent }">
					<!-- 隐藏域:解决了修改页面时,数据库的创建时间被删除的问题 -->
					
					<table class="honry-table" cellpadding="1" cellspacing="1" data-options="fit:true"  style="margin-left:auto;margin-right:auto;margin-top:10px;">
		    			<tr>
			    			 <td class="honry-lable">项目分类:</td>
			    			<td class="honry-info"><input  id="type" class="easyui-combobox" name="tmItems.type" value="${item.type}"  style="width:300px" /></td>
			    		</tr>
						<tr>
							<td class="honry-lable">项目名称:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="name" name="tmItems.name" value="${item.name }" data-options="required:true" style="width:300px" /></td>
		    			</tr>
						<tr>
							<td class="honry-lable">项目访问路径:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="path" name="tmItems.path" value="${item.path }" data-options="required:true" style="width:300px" /></td>
		    			</tr>
						
		    		</table>
			    <div style="text-align:center;padding:5px">
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" id="cle" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>
	
	<script>
	var id = $('#id').val();
	//加载页面
		$(function(){
			//加载表单
			 $('#type').combobox({ 
				url: '<%=basePath%>oa/itemManage/getTypes.action',  
				valueField:'id',    
			    textField:'text',
			    editable:true,
			});
		})
		/**
		 * 表单提交
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
			function submit(flg){
			  	$('#editForm').form('submit',{
			  		url:"<%=basePath%>oa/itemManage/saveItem.action",
			  		 onSubmit:function(){ 
			  			if (!$('#editForm').form('validate')) {
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return false;
						}
					 },  
					 success : function(data) {
							closeLayout()
							data = JSON.parse(data);
							if (data.resCode == 'success') {
								$.messager.alert('提示',data.resMsg);
								clear();
								closeLayout();
								reload();
							}else if(data.resCode == 'error'){
								$.messager.alert('提示',data.resMsg);
							}
						},
					error:function(date){
						 $.messager.progress('close');
						 $.messager.alert('提示','保存失败');
					}
			  	});
			  	
	 	 	}
		/**
		 * 清除页面填写信息
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
			function clear(){
				$('#editForm').form('reset');
			}
			function closeLayout(){
				$('#divLayout').layout('remove','east');
				$("#list").datagrid("reload");
				closeW();
			}
			
	</script>
	
	</body>
</html>