<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
<div id="panelEast" class="easyui-panel"
		data-options="title:'补办就诊卡',iconCls:'icon-form'">
		<div style="padding: 10px">
			<form id="fillForm" method="post">
				<input type="hidden" id="id" name="idcard.id" value="${idcard.id }">
				<input type="hidden" id="pid" name="patient.id" value="${patient.id }">
					<table class="honry-table" cellpadding="0" cellspacing="0"
						border="0" style="margin-left:auto;margin-right:auto;">
						<tr>
							<td class="honry-lable">
								患者姓名:
							</td>
							<td>
								${patient.patientName }
							</td>
							<td class="honry-lable">
								性别:
							</td>
							<td>
								${sex }
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								出生日期:
							</td>
							<td>
								<fmt:formatDate value="${patient.patientBirthday}" pattern="yyyy-MM-dd"/>&nbsp;
							</td>
							<td class="honry-lable">
								联系方式:
							</td>
							<td>
								${patient.patientPhone }
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								家庭地址:
							</td>
							<td colspan="3">
								${patient.patientCity }-${patient.patientAddress }&nbsp;
							</td>
						</tr>
				<tr>					
					<td class="honry-lable">
						证件类型：
					</td>
					<td class="honry-view">
						${certificatestype}&nbsp;
					</td>
					<td class="honry-lable">
						证件号码：
					</td>
					<td class="honry-view">
						${patient.patientCertificatesno}&nbsp;
					</td>
				</tr>
						<tr>
							<td class="honry-lable" style="width: 20%">
								原卡号：
							</td>
							<td class="honry-info" style="width: 60%">
								${idcard.idcardNo }
							</td>
							<td class="honry-lable" style="width: 20%">
								卡号：
							</td>
							<td class="honry-info" style="width: 30%">
								<input class="easyui-textbox" id="idcardNo"
									name="idcard.idcardNo" style="width:180px"
									data-options="required:true" missingMessage="请输入卡号" onblur="validCheckIC()"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								原卡类型：
							</td>
							<td>
								${idcardType }
							</td>
							<td class="honry-lable">
								卡类型：
							</td>
							<td>
								<input id="CodeIdcardType" name="idcard.idcardType"
									data-options="required:true"
									style="width: 90%;" missingMessage="请选择卡类型"
									onkeydown="KeyDown(0,'CodeIdcardType')" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								备注:
							</td>
							<td colspan="3">
								<textarea class="easyui-validatebox" rows="4" cols="65" id="idcardRemark"
									name="idcard.idcardRemark" data-options="multiline:true">
									</textarea>
							</td>
						</tr>
					</table>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:submit();void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'">保存</a>
					<a href="javascript:clear();void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-clear'">清除</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	//验证标记
	var check = "";
	$(function() {
		//初始化下拉框
		idCombobox("CodeIdcardType","idcardType");
	});
	//从xml文件中解析，读到下拉框
	function idCombobox(paramId,param) {
		$('#' + paramId).combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=" + param,
			valueField : 'encode',
			textField : 'name',
			multiple : false
		});
	}
	//验证卡号是否已存在
	function validCheckIC(){
		$.ajax({
			url : "<%=basePath%>patient/idcard/checkIdcardNoVSMedicalNO.action?menuAlias=${menuAlias}",
			data : {
				"idcardNo" : $("#idcardNo").val(),
				"idcardId" : $("#id").val()
			},
			type : 'post',
			success : function(result) {
				if (result == "no1") {
					check = "idcardNo";
				}else{
					check = "";
				}
				if(check==""){
					submits();
				}else{
					$.messager.progress('close');
					$.messager.alert('提示',"卡号已在用,不能提交表单,请修改后提交!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
			}
		});
	}
	/**
	 * 表单提交
	 * @author  lt
	 * @date 2015-6-1
	 * @version 1.0
	 */
	function submit() {
		$.messager.progress({text:'保存中，请稍后...',modal:true});	
		validCheckIC();
	 }
	function submits() {
		$('#fillForm').form('submit', {
			url : "<%=basePath%>patient/idcard/fillIdcard.action",
			onSubmit : function() {
				if (!$('#editForm').form('validate')) {
					$.messager.progress('close');	
					$.messager.alert('提示',"验证没有通过,不能提交表单!")
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
			},
			success : function(data) {
				$('#divLayout').layout('remove', 'east');
				//实现刷新栏目中的数据
				$("#list").datagrid("reload");
				//清空主页面
				clearValue();
				//将主页面的按钮不可点击
				$('#editlinkbutton').linkbutton('disable');
				$('#dellinkbutton').linkbutton('disable');
				$('#backlinkbutton').linkbutton('disable');
				$('#losslinkbutton').linkbutton('disable');
				$('#activatelinkbutton').linkbutton('disable');
				$('#filllinkbutton').linkbutton('disable');
				$.messager.alert('提示','补办成功！');
				$.messager.progress('close');	
				$('#divLayout').layout('remove', 'east');
			},
			error : function(data) {
				//清空主页面
				clearValue();
				//将主页面的按钮不可点击
				$('#editlinkbutton').linkbutton('disable');
				$('#dellinkbutton').linkbutton('disable');
				$('#backlinkbutton').linkbutton('disable');
				$('#losslinkbutton').linkbutton('disable');
				$('#activatelinkbutton').linkbutton('disable');
				$('#filllinkbutton').linkbutton('disable');
				$.messager.alert('提示',"补办失败！");
				$.messager.progress('close');	
			}
		});
	}
	/**
	 * 清除页面填写信息
	 * @author  lt
	 * @date 2015-11-18
	 * @version 1.0
	 */
	function clear() {
		$('#fillForm').form('reset');
	}
	/**
	 * 关闭编辑窗口
	 * @author  lt
	 * @date 2015-11-18
	 * @version 1.0
	 */
	function closeLayout() {
		$('#divLayout').layout('remove', 'east');
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>