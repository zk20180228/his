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
		data-options="title:'科室编辑',iconCls:'icon-form',border:false" style="width: 100%">
		<div style="padding: 5px">
			<form id="editForm" method="post">
				<input type="hidden" id="id" name="sysDepartment.id"
					value="${sysDepartment.id }">
				<input type="hidden" id="deptParent" name="sysDepartment.deptParent" value="${sysDepartment.deptParent }">
				<input type="hidden" id="deptCode" name="sysDepartment.deptCode"
					value="${sysDepartment.deptCode }">
				<input type="hidden" id="deptOrder" name="sysDepartment.deptOrder"
					value="${sysDepartment.deptOrder }" />
				<table class="honry-table" style="width: 98%" cellpadding="1" cellspacing="1"
					border="1px solid black" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td class="honry-lable">
							分类名称:
						</td>
						<td class="honry-info">
							<input id="deptType" class="easyui-combobox" 
								name="sysDepartment.deptType"
							 value="${sysDepartment.deptType }" 
							 data-options="required:true" 
							 missingMessage="请输入选择分类"" style="width: 290px"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							部门名称:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="deptName"
								name="sysDepartment.deptName" value="${sysDepartment.deptName }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请输入名称" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							部门简称:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="deptBrev"
								name="sysDepartment.deptBrev" value="${sysDepartment.deptBrev }"
								data-options="required:true" style="width: 290px"
								missingMessage="请输入部门简称" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							部门性质:
						</td>
						<td class="honry-info">
							<input id="CodeDepanature" name="sysDepartment.deptProperty" value="${sysDepartment.deptProperty }" data-options="required:true,missingMessage:'请选择部门性质!'" style="width: 290px"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							部门英文:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="deptEname"
								name="sysDepartment.deptEname"
								value="${sysDepartment.deptEname }" data-options="required:true"
								style="width: 290px" missingMessage="请输入部门英文" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							部门地点:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="deptAddress"
								name="sysDepartment.deptAddress"
								value="${sysDepartment.deptAddress }"
								data-options="required:true" style="width: 290px"
								missingMessage="请输入部门地点" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							自定义码:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="deptInputcode"
								name="sysDepartment.deptInputcode"
								value="${sysDepartment.deptInputcode }" style="width: 290px"
								missingMessage="请输入自定义码" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							所属院区:
						</td>
						<td class="honry-info">
							<input id="areaCode" class="easyui-combobox" 
								name="sysDepartment.areaCode"
							 value="${sysDepartment.areaCode }" 
							 data-options="required:true" 
							 missingMessage="请选择所属院区"" style="width: 290px"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							是否是挂号部门:
						</td>
						<td>
							<input id="deptIsforregisterHidden" type="hidden"
								name="sysDepartment.deptIsforregister"
								value="${sysDepartment.deptIsforregister }">
							<input type="checkBox" id="deptIsforregister"
								onclick="javascript:onclickBoxDep()" />
						</td>
					</tr>
					<tr id="registernoTrId" style="display: none">
						<td class="honry-lable">
							挂号顺序号:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox" id="deptRegisterno"
								name="sysDepartment.deptRegisterno"
								value="${sysDepartment.deptRegisterno }" style="width: 290px"
								missingMessage="请输入挂号顺序号" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							是否是核算部门:
						</td>
						<td>
							<input id="deptIsforaccountingHidden" type="hidden"
								name="sysDepartment.deptIsforaccounting"
								value="${sysDepartment.deptIsforaccounting }">
							<input type="checkBox" id="deptIsforaccounting"
								onclick="javascript:onclickBox('deptIsforaccounting')" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							停用标志:
						</td>
						<td>
							<input id="stopFlgHidden" type="hidden"
								name="sysDepartment.stop_flg" value="${sysDepartment.stop_flg }">
							<input type="checkBox" id="stopFlg"
								onclick="javascript:onclickBox('stopFlg')" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							备注:
						</td>
						<td class="honry-info">
							<textarea class="easyui-validatebox" rows="4" cols="32" id="deptRemark" name="sysDepartment.deptRemark"
								data-options="multiline:true">${sysDepartment.deptRemark }</textarea>
							
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<c:if test="${sysDepartment.id==null }">
						<a href="javascript:addContinue();" class="easyui-linkbutton"
							data-options="iconCls:'icon-save'">连续添加</a>
					</c:if>
					<a href="javascript:submit();void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'">保存</a>
					<c:if test="${sysDepartment.id==null }">
						<a href="javascript:clear();void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-clear'">清除</a>
					</c:if>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	$(function() {
		$('#deptType').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=depttype",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
		});
		$('#CodeDepanature').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=depaNature",
			valueField : 'encode',
			textField : 'name',
			multiple : false
		});
		$('#areaCode').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=hospitalArea",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
		});
		//下拉框的keydown事件   调用弹出窗口
		var deptPropertykeydown = $('#CodeDepanature').combobox('textbox');
		deptPropertykeydown.keyup(function() {
			KeyDown(0, "CodeDepanature");
		});

		if ($('#deptIsforregisterHidden').val() == 1) {
			$('#deptIsforregister').attr("checked", true);
			$('#deptRegisterno').textbox({
				disabled : false
			});
		} else {
			$('#deptRegisterno').textbox({
				disabled : true
			});
		}
		if ($('#deptIsforaccountingHidden').val() == 1) {
			$('#deptIsforaccounting').attr("checked", true);
		}
		if ($('#stopFlgHidden').val() == 1) {
			$('#stopFlg').attr("checked", true);
		}
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
			url : "<%=basePath%>baseinfo/department/editDepartmentInfo.action",
			data : $('#editForm').serialize(),
			dataType : 'json',
			onSubmit : function() {
				if (!$('#editForm').form('validate')) {
					$.messager.progress('close');
// 					$.messager.show({
// 						title : '提示信息',
// 						msg : '验证没有通过,不能提交表单!'
// 					});
					$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
					close_alert();
					return false;
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});
			},
			success : function(data) {
				$.messager.progress('close');
				$('#divLayout').layout('remove', 'east');
				//实现刷新科室树
				$('#tDt').tree('reload');
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','保存失败！');
			}
		});
	}
	//连续添加
	function addContinue() {
		$('#editForm').form('submit', {
			url : "<%=basePath%>baseinfo/department/editDepartmentInfo.action",
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
			success : function() {
				$.messager.progress('close');
				$('#list').datagrid('reload');
				$('#editForm').form('reset');
				$('#deptRegisterno').textbox({
					disabled : true
				});
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
	function closeLayout() {
		$('#divLayout').layout('remove', 'east');
	}

	function onclickBox(id) {
		if ($('#' + id).is(':checked')) {
			$('#' + id + 'Hidden').val(1);
		} else {
			$('#' + id + 'Hidden').val(0);
		}
	}
	//单选按钮赋值
	function onclickBoxDep(id) {
		if ($('#deptIsforregister').is(':checked')) {
			$('#deptIsforregisterHidden').val(1);
			$('#deptRegisterno').textbox({
				disabled : false,
				required : true
			});
		} else {
			$('#deptIsforregisterHidden').val(0);
			$('#deptRegisterno').textbox({
				disabled : true
			});
		}
	}

	/**
	 * 弹出框
	 * @author  lt
	 * @date 2015-06-29
	 * @version 1.0
	 */
	var win;
	function showWin(title, url, width, height) {
		var content = '<iframe id="myiframe" src="'
				+ url
				+ '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
		var divContent = '<div id="treeDeparWin">';
		win = $('<div id="treeDeparWin"><div/>').dialog({
			content : content,
			width : width,
			height : height,
			modal : true,
			minimizable : false,
			maximizable : true,
			resizable : true,
			shadow : true,
			center : true,
			title : title
		});
		win.dialog('open');
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>