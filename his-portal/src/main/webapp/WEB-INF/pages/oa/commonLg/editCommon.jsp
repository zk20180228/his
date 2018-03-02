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
		<div class="easyui-panel" id = "panelEast" data-options="title:'常用语编辑',iconCls:'icon-form',fit:true,border:false" >
			<div style="width: 100%;height: 98%">
	    		<form id="editForm" method="post" >
					<input type="hidden" id="id" name="id" value="${oaCommon.id }">
					<!-- 隐藏域:解决了修改页面时,数据库的创建时间被删除的问题 -->
					
					<table class="honry-table" cellpadding="1" cellspacing="1" data-options="fit:true"  style="margin-left:auto;margin-right:auto;margin-top:10px;">
		    			<tr>
			    			 <td class="honry-lable">表单名称:</td>
			    			<td class="honry-info"><input  id="tableCode" class="easyui-combobox" name="tableCode" value="${oaCommon.tableCode}"  style="width:300px" /></td>
			    		</tr>
						<tr>
							<td class="honry-lable">常用语:</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="common" name="common" value="${oaCommon.common }" data-options="required:true" style="width:300px" missingMessage="请输常用语"/></td>
		    			</tr>
						
		    		</table>
			    <div style="text-align:center;padding:5px">
<!-- 			    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a> -->
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
			
		})
		/**
		 * 表单提交
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
			function submit(flg){
			  	$('#editForm').form('submit',{
			  		url:"<%=basePath%>oa/commonLg/saveCommon.action",
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
							data = JSON.parse(data);
							if (data.resCode == 'success') {
								$.messager.alert('提示',data.resMsg);
								clear();
								closeLayout();
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
		 * 连续添加
		 * @author  liudelin
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
	 	 	function addContinue(){ 
			    $('#editForm').form('submit',{  
			        url:"<%=basePath%>oa/commonLg/saveCommon.action",  
			        onSubmit:function(){
			        	$.messager.progress('close');
			        	var codes=$("#codes").val();
						var codeOld=codes.split(",");
						for(var i=0;i<codeOld.length;i++){
							if(codeOld[i]==codeNew){
								$.messager.alert('提示','该系统编码已存在，请重新输入');
								close_alert();
								return false;
							}
						 }
						if (!$('#editForm').form('validate')) {
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return false;
						}
						$.messager.progress({text:'保存中，请稍后...',modal:true});
			        },  
			        success:function(){ 
			        	$.messager.progress('close');
			             //实现刷新栏目中的数据
	                     $('#list').datagrid('reload');
	                     clear();
			        },
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示','保存失败！');	
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
			}
			
			//加载表单
			 $('#tableCode').combobox({ 
				url: '<%=basePath%>oa/commonLg/fandAllFrom.action',  
				valueField:'id',    
			    textField:'text',
			    filter:function(q,row){
					var keys = new Array();
					keys[keys.length] = 'id';
					keys[keys.length] = 'text';
					if(filterLocalCombobox(q, row, keys)){
						row.selected=true;
					}else{
						row.selected=false;
					}
					return filterLocalCombobox(q, row, keys);
			    },
			    editable:true,
			});
			
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