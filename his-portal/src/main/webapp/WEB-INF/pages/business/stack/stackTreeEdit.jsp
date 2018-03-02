<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
<div class="easyui-layout" data-options='fit:true'>
	<form id="treeEditForm" method="post" >
		<div data-options="region:'center',border:false" style="padding:10px">
			<input type="hidden" id="id" name="businessStack.id" value="${businessStack.id}">
			<input type="hidden" id="type" name="businessStack.type" value="${businessStack.type}">
			<input type="hidden" id="deptId" name="businessStack.deptId" value="${businessStack.deptId}">
			<input type="hidden" id="parent" name="businessStack.parent" value="${businessStack.parent}">
			<input type="hidden" id="path" name="businessStack.path" value="${businessStack.path}">
			<input type="hidden" id="doc" name="businessStack.doc" value="${businessStack.doc}">
			<input type="hidden" id="source" name="businessStack.source" value="${businessStack.source}">
			<input type="hidden" id="shareFlag" name="businessStack.shareFlag" value="${businessStack.shareFlag}">
			<input type="hidden" id="stackObject" name="businessStack.stackObject" value="${businessStack.stackObject}">
			<input type="hidden" id="isValid" name="businessStack.isValid" value="${businessStack.isValid}">
			<input type="hidden" id="memoryCode" name="businessStack.memoryCode" value="${businessStack.memoryCode}">
			<input type="hidden" id="isConfirm" name="businessStack.isConfirm" value="${businessStack.isConfirm}">
			<input type="hidden" id="isOrder" name="businessStack.isOrder" value="${businessStack.isOrder }">
			<input type="hidden" id="createUser" name="businessStack.createUser" value="${businessStack.createUser}">
			<input type="hidden" id="createDept" name="businessStack.createDept" value="${businessStack.createDept}">
			<input type="hidden" id="createTime" name="businessStack.createTime" value="${businessStack.createTime}">
			<table class="honry-table" style="width:100%">
				<tr>
					<td class="honry-lable">名称：</td>
					<td>
						<input id="name" name="businessStack.name" value="${businessStack.name}" class="easyui-textbox" data-options="required:true" size="25px" />
					</td>
				</tr>
				<tr>
					<td class="honry-lable">自定义码：</td>
					<td>
						<input id="inputCode" name="businessStack.inputCode" value="${businessStack.inputCode }" class="easyui-textbox" data-options="required:true" size="25px" />
					</td>
				</tr>
				<tr>
					<td class="honry-lable">备注：</td>
					<td>
						<input class="easyui-textbox"  id="remark" name="businessStack.remark" value="${businessStack.remark}" size="25px" />
					</td>
				</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<c:if test="${businessStack.id==null }">
					<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_disk'">连续添加</a>
				</c:if>
				<a  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="submitTreeForm()">保存</a>
				<a  data-options="iconCls:'icon-clear'" class="easyui-linkbutton" onclick="clearTreeForm()">重置</a>
				<a  data-options="iconCls:'icon-cancel'"  class="easyui-linkbutton" onclick="closeUpdate()">关闭</a>
			</div>
		</div>
	</form>
</div>
	
<script type="text/javascript">
	//提交验证
	function submitTreeForm(){
		$('#treeEditForm').form('submit', {
			url : "<c:url value='/baseinfo/stack/saveOrupdataBusinessStack.action'/>",
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
				closeUpdate();
				refresh();
				$.messager.progress('close');
				$.messager.alert('提示',"操作成功");
				$("#list").datagrid("reload");
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示',"操作失败！");	
			}
		}); 
	}
		function addContinue(){
			$('#treeEditForm').form('submit', {
				url :  "<c:url value='/stack/editTreeInfo.action'/>",
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
					$.messager.progress('close');
					$.messager.alert('提示',"修改成功");
					refresh();
					clearTreeForm();
					$("#list").datagrid("reload");
				},
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示',"操作失败！");	
				}
			}); 
		}
		//清除所填信息
		function clearTreeForm(){
			$('#treeEditForm').form('reset');
		}
</script>
</body>
</html>