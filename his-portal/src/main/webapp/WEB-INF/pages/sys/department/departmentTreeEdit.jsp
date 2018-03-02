<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<body>
	<form id="treeEditForm" method="post" >
		<div title="Tab1" style="padding: 20px;">
			<input type="hidden" id="id" name="sysDepartment.id" value="${sysDepartment.id }">
			<input type="hidden" id="hospitalId" name="sysDepartment.hospitalId.id" value="${sysDepartment.hospitalId.id }">
			<input type="hidden" id="deptParent" name="sysDepartment.deptParent" value="${sysDepartment.deptParent }">
			<input type="hidden" id="deptCode" name="sysDepartment.deptCode" value="${sysDepartment.deptCode }">
			<input type="hidden" id="deptPinyin" name="sysDepartment.deptPinyin" value="${sysDepartment.deptPinyin }">
			<input type="hidden" id="deptWb" name="sysDepartment.deptWb" value="${sysDepartment.deptWb }">
			<input type="hidden" id="deptHasson" name="sysDepartment.deptHasson" value="${sysDepartment.deptHasson }">
			<input type="hidden" id="deptLevel" name="sysDepartment.deptLevel" value="${sysDepartment.deptLevel }">
			<input type="hidden" id="deptOrder" name="sysDepartment.deptOrder" value="${sysDepartment.deptOrder }">
			<input type="hidden" id="deptUppath" name="sysDepartment.deptUppath" value="${sysDepartment.deptUppath }">
			<input type="hidden" id="deptPath" name="sysDepartment.deptPath" value="${sysDepartment.deptPath }">
			<input type="hidden" id="deptType" name="sysDepartment.deptType" value="${sysDepartment.deptType }">
			<input type="hidden" id="createUser" name="sysDepartment.createUser" value="${sysDepartment.createUser }">
			<input type="hidden" id="createDept" name="sysDepartment.createDept" value="${sysDepartment.createDept }">
			<input type="hidden" id="createTime" name="sysDepartment.createTime" value="${sysDepartment.createTime }">
			<input type="hidden" id="stop_flg" name="sysDepartment.stop_flg" value="${sysDepartment.stop_flg }">
			<input type="hidden" id="del_flg" name="sysDepartment.del_flg" value="${sysDepartment.del_flg }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr align="center">
	    			<td>名称:</td>
	    			<td><input class="easyui-textbox" type="text" id="deptName" name="sysDepartment.deptName" value="${sysDepartment.deptName }" data-options="required:true" style="width:290px" missingMessage="请输入名称"/></td>
	    		</tr>
			</table>	
			<div style="text-align: center; padding: 5px">
				<c:if test="${sysDepartment.id==null }">
					<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
				</c:if>
				<a href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="submitTreeForm()" class="easyui-linkbutton">确定</a>
				<a href="javascript:void(0)" data-options="iconCls:'icon-clear'" onclick="clearTreeForm()" class="easyui-linkbutton">清空</a>
				<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeDialog()" class="easyui-linkbutton">关闭</a>
			</div>
		</div>		
	</form>
	<script type="text/javascript">
	//提交验证
	function submitTreeForm(){
		$('#treeEditForm').form('submit', {
			url : "<c:url value='/baseinfo/department/editTreeInfo.action'/>",
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
			},
			success : function(data) {
				closeDialog();
				refresh();
				$("#list").datagrid("reload");
			},
			error : function(data) {
				$.messager.alert('提示','操作失败！');	
			}
		}); 
	}
	function addContinue(){
		$('#treeEditForm').form('submit', {
			url : "<c:url value='/baseinfo/department/editTreeInfo.action'/>",
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
			},
			success : function(data) {
				refresh();
				clearTreeForm();
				$("#list").datagrid("reload");
			},
			error : function(data) {
				$.messager.alert('提示','操作失败！');	
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