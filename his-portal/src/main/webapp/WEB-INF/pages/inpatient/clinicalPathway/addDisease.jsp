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
	<div class="easyui-panel" id="panelEast"
		data-options="iconCls:'icon-form',border:false" style="width: 100%">
		<div style="padding: 5px">
			<form id="editForm" method="post">
				<input type="hidden" id="id" name="cpWay.id" value="${cpWay.id }">
				<table class="honry-table" style="width: 100%" cellpadding="1" cellspacing="1"
					border="1px solid black" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td class="honry-lable">
							临床路径名称:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="cpName"
								name="cpWay.cpName" value="${cpWay.cpName }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请输入临床路径名称" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							自定义码:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="customCode"
								name="cpWay.customCode" value="${cpWay.customCode }"
								style="width: 290px" 
								missingMessage="请输入模板Code" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							分类:
						</td>
						<td class="honry-info">
							<input class="easyui-combobox"  id="typeId"
								name="cpWay.typeId" value="${cpWay.typeId }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请选择分类" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							简要说明:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="memo"
								name="cpWay.memo" value="${cpWay.memo }" style="width: 290px"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							病理分类:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="caseType"
								name="cpWay.caseType"
								value="${cpWay.caseType }" style="width: 290px" />
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:submit();void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'">保存</a>
					<c:if test="${sysDepartment.id==null }">
						<a href="javascript:clear();void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-clear'">清除</a>
					</c:if>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script type="text/javascript">
	$(function() {
		/**模板类型下拉**/
		$('#typeId').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=cpWay",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
		});
	});
	//表单提交   
	function submit() {
		$('#editForm').form('submit', {
			url : "<%=basePath%>inpatient/clinicalPathwayAction/addPathway.action",
			data : $('#editForm').serialize(),
			dataType : 'json',
			onSubmit : function() {
				if (!$('#editForm').form('validate')) {
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
					close_alert();
					return false;
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});
			},
			success : function(data) {
				$.messager.progress('close');
				if(data=='true'){
					$.messager.alert('提示','保存成功！');
					//实现刷新科室树
					closeDialog();
					$('#tDt').tree('reload');
				}else{
					$.messager.alert('提示','保存失败');
				}
				
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','保存失败！');
			}
		});
	}
	//清除所填信息
	function clear() {
		$('#editForm').form('reset');
	}
</script>
</body>
</html>