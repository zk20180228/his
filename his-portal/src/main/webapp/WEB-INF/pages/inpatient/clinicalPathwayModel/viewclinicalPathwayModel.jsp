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
	<div class="easyui-panel" id="panelEast"
		data-options="iconCls:'icon-form',border:false" style="width: 100%">
		<div style="padding: 5px">
			<form id="editForm" method="post">
				<input type="hidden" id="id" name="modelDict.id" value="${modelDict.id }">
				<table class="honry-table" style="width: 100%" cellpadding="1" cellspacing="1"
					border="1px solid black" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td class="honry-lable">
							模板类别:
						</td>
						<td class="honry-info">
							<input id="modelClass" class="easyui-combobox"  readonly="readonly";
								name="modelDict.modelClass"  value="${modelDict.modelClass }" 
							 data-options="required:true,editable:false" 
							 missingMessage="请输入选择分类"" style="width: 290px" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							模板Code:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="modelCode" readonly="readonly";
								name="modelDict.modelCode" value="${modelDict.modelCode }"
								data-options="required:true" style="width: 290px;" 
								missingMessage="请输入模板Code" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							模板版本号:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="modelVersion" readonly="readonly";
								name="modelDict.modelVersion" value="${modelDict.modelVersion }"
								data-options="required:true" style="width: 290px;disabled:true;" 
								missingMessage="请输入版本号" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							模板名称:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="modelName" readonly="readonly";
								name="modelDict.modelName" value="${modelDict.modelName }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请输入名称" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							属性:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="modelNature" readonly="readonly";
								name="modelDict.modelNature" value="${modelDict.modelNature }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请输入名称" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							自定义码:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="customCode"
								name="modelDict.customCode" readonly="readonly";
								value="${modelDict.customCode }" style="width: 290px"
								missingMessage="请输入自定义码" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							执行科室:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="deptCode"
								name="modelDict.deptCode" readonly="readonly";
								value="${modelDict.deptCode }" data-options=""
								style="width: 290px" missingMessage="请输入执行科室" />
						</td>
					</tr>
					
				</table>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	$(function() {
		$('#modelClass').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=systemType",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
		});
		$('#modelNature').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=cpProperty",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
		});
	});
</script>
</body>
</html>