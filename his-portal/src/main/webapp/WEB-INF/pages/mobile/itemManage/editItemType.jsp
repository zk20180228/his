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
		<div class="easyui-panel" id = "panelEast" data-options="title:'',iconCls:'icon-form',fit:true,border:false" style="overflow :'hidden'">
			<div style="width: 100%;height: 150px">
	    		<form id="editForm" method="post" >
					<input type="hidden" id="parentId" name="tmItems.parentId" value="${id}">
					<input type="hidden" id="type" name="tmItems.type" value="${type }">
					<table class="honry-table" cellpadding="1" cellspacing="1" data-options="fit:true"  style="margin-left:auto;margin-right:auto;margin-top:10px;">
						<tr>
							<td class="honry-lable">项目分类名称:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="typeName" name="tmItems.typeName" value="${item.typeName }" data-options="required:true" style="width:300px"/></td>
		    			</tr>
						<tr>
							<td class="honry-lable">项目名称:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="name" name="tmItems.name" value="${item.name }" data-options="required:true" style="width:300px"/></td>
		    			</tr>
		    		</table>
			    <div style="text-align:center;padding:5px">
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>
	
	<script>
	//加载页面
		/**
		 * 表单提交
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
			function submit(flg){
			  	$('#editForm').form('submit',{
			  		url:"<%=basePath%>oa/itemManage/saveItemType.action",
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
			
			 function filterLocalCombobox(q, row, keys){
					if(keys!=null && keys.length > 0){
						for(var i=0;i<keys.length;i++){
							if(row[keys[i]]!=null&&row[keys[i]]!=''){
								var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1||row[keys[i]].indexOf(q) > -1;
								if(istrue==true){
									return true;
								}
							}
						}
					}else{
						var opts = $(this).combobox('options');
						return row[opts.textField].indexOf(q.toUpperCase()) > -1||row[opts.textField].indexOf(q) > -1;
					}
				}

	</script>
	
	</body>
</html>