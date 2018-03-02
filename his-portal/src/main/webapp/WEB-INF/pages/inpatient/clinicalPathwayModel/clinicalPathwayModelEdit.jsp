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
				<input type="hidden" id="id" name="modelDict.id" value="${modelDict.id }">
				<table class="honry-table" style="width: 100%" cellpadding="1" cellspacing="1"
					border="1px solid black" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td class="honry-lable">
							模板类别:
						</td>
						<td class="honry-info">
							<input id="modelClass" class="easyui-combobox" 
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
							<input class="easyui-textbox"  id="modelCode"
								name="modelDict.modelCode" value="${modelDict.modelCode }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请输入模板Code" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							模板版本号:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="modelVersion"
								name="modelDict.modelVersion" value="${modelDict.modelVersion }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请输入版本号" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							模板名称:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="modelName"
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
							<input class="easyui-textbox"  id="modelNature"
								name="modelDict.modelNature" value="${modelDict.modelNature }"
								data-options="required:true,editable:false" style="width: 290px" 
								missingMessage="请选择属性" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							自定义码:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="customCode"
								name="modelDict.customCode"
								value="${modelDict.customCode }" style="width: 290px"
								missingMessage="请输入自定义码" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							执行科室:
						</td>
						<td class="honry-info">
							<input id="deptCode" data-options="prompt:'执行科室'" class="easyui-combobox" 
									name="modelDict.deptCode"
									value="${modelDict.deptCode }" data-options=""
									style="width: 290px"/>
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
		$('#modelClass').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=systemType",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
		});
		/**属性下拉下拉**/
		$('#modelNature').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=cpProperty",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
			panelHeight:'100px'
		});
		/**科室下拉**/
		$('#deptCode').combobox({
			url: "<%=basePath%>baseinfo/department/departmentCombobox.action",
			valueField : 'deptCode',
			textField : 'deptName',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'deptCode';
				keys[keys.length] = 'deptName';
				keys[keys.length] = 'deptPinyin';
				keys[keys.length] = 'deptWb';
				keys[keys.length] = 'deptInputcode';
				if(filterLocalCombobox(q, row, keys)){
					row.selected=true;
				}else{
					row.selected=false;
				}
				return filterLocalCombobox(q, row, keys);
		    }
		})
	});
	//编码回车事件
	function KeyDown(flg, tag) {
		if (flg == 1) {//回车键光标移动到下一个输入框
			if (event.keyCode == 13) {
				event.keyCode = 9;
			}
		}
		if (flg == 0) { //空格键打开弹出窗口
			if (event.keyCode == 32) {
				event.returnValue = false;
				event.cancel = true;
			}
		}
	}
	//表单提交   
	function submit() {
		$('#editForm').form('submit', {
			url : "<%=basePath%>inpatient/clinicalPathwayModelAction/saveOrUpdate.action",
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