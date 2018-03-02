<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'表维护编辑',iconCls:'icon-form',border:false,fit:true" style="width:580px">
		<div style="padding:10px">
			<form id="editForm" method="post">
				<input id="id" name="tableFiled.id" type="hidden" value="${tableFiled.id}"/>
				<input id="code" name="tableFiled.code" type="hidden" value="${tableFiled.code}"/>
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
						<td class="honry-lable">表名:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="tableName" name="tableFiled.tableName" value="${tableFiled.tableName }" data-options="required:true"  style="width:200px" /></td>
	    			</tr>
					<tr>
						<td class="honry-lable">字段名称:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="fieldName" name="tableFiled.fieldName" value="${tableFiled.fieldName }" data-options="required:true"  style="width:200px" /></td>
	    			</tr>
					<tr>
						<td class="honry-lable">类型:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="fieldType" name="tableFiled.fieldType" value="${tableFiled.fieldType }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">长度:</td>
		    			<td class="honry-info"><input class="easyui-numberbox" id="fieldLength" name="tableFiled.fieldLength" value="${tableFiled.fieldLength }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">java类型:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="javaType" name="tableFiled.javaType" value="${tableFiled.javaType }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">备注:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="remarks" name="tableFiled.remarks" value="${tableFiled.remarks }" data-options="multiline:true" style="width:200px;height:50px;"/></td>
	    			</tr>
				</table>
				</form>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:submitForm();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
		</div>
	</div>
<%-- 	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script> --%>
<script type="text/javascript">
function submitForm(){ 
      	$('#editForm').form('submit',{  
       	url:"<%=basePath %>migrate/tableField/editTableField.action",  
       	onSubmit:function(){
       	 if($(this).form('validate')){
    		 $.messager.progress({text:'保存中，请稍后...',modal:true});
    		 return true;
    	 }else{
    		 $.messager.alert('提示','请填写完整信息');
    		 return false;
    	 	}
		},  
        success:function(data){  
        	$.messager.progress('close');
        	var res = eval('('+data+')');
        	if(res.resCode == 'success'){
        		$.messager.alert('提示','保存成功！');
	        	$('#list').datagrid('reload');
	        	$('#divLayout').layout('remove','east');
        	}else{
				$.messager.alert('提示','保存失败！');	
        	}
        },
  		}); 
   }
	function clear(){
		$('#editForm').form('clear');
	}
   //关闭页面
	function closeLayout(){
		$('#divLayout').layout('remove','east');
		$("#list").datagrid("reload");
	}
	</script>
</body>