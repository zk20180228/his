<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
</head>
<body>
	<table id="friTable" class="honry-table" cellpadding="1"
		cellspacing="1" border="1px solid black">
		<tr>
			<td>住院流水号：</td>
			<td><input class="easyui-radio" id="inpatientNo"
				name="inpatientNo" value="${inpatientInfo.inpatientNo }"
				data-options="required:true" style="width: 60%"
				" missingMessage="医院编号" /></td>
			<td>医疗类别：</td>
			<td><input id="CodeMedicaltype" name="medicalType"
				value="${inpatientInfo.medicalType}"
				onkeydown="KeyDown(0,'CodeMedicaltype')"
				data-options="required:true" style="width: 60%"
				missingMessage="请输入参数名称"/ ></td>
		</tr>
		<tr>
			<td>病历号:</td>
			<td><input class="easyui-validatebox" id="medicalrecordId"
				name="medicalrecordId" value="${inpatientInfo.medicalrecordId }"
				data-options="required:true" style="width: 60%" style="width:60%"
				missingMessage="请输入参数代码" onkeydown="KeyDownByp('medicalrecordId')" /></td>

			<td>就诊卡号：</td>
			<td><input class="easyui-validatebox" id="idcardNo"
				name="idcardNo" value="${inpatientInfo.idcardNo }"
				data-options="required:true" style="width: 60%"
				missingMessage="请输入参数类型" onkeydown="KeyDownByp('idcardNo')" /></td>
		</tr>
		<tr>
			<td>医疗证号:</td>
			<td><input class="easyui-textbox" id="mcardNo" name="mcardNo"
				value="${inpatientInfo.mcardNo }" data-options="required:true"
				style="width: 60%" missingMessage="请输入参数值" /></td>
			<td>姓名:</td>
			<td><input class="easyui-textbox" id="patientName"
				name="patientName" value="${inpatientInfo.patientName }"
				data-options="required:true" style="width: 60%"
				missingMessage="请输入参数单位" /></td>
		</tr>
		<tr>
			<td>证件类型:</td>
			<td><input id="CodeCertificate" name="certificatesType"
				value="${inpatientInfo.certificatesType }"
				onkeydown="KeyDown(0,'CodeCertificate')"
				data-options="required:true" style="width: 60%"
				missingMessage="请输入参数上限" /></td>
		
			<td>证件号码:</td>
			<td><input class="easyui-textbox" id="certificatesNo"
				name="certificatesNo" value="${inpatientInfo.certificatesNo }"
				data-options="required:true" style="width: 60%"
				missingMessage="请输入参数下限" /></td>
		</tr>
		<tr>
			<td>性别:</td>
			<td>
			<input id="CodeSex" name="reportSex"
				value="${inpatientInfo.reportSex }" onkeydown="KeyDown(0,'CodeSex')"
				data-options="required:true" style="width: 60%"
				missingMessage="请输入备注" /></td>
				
			<td>出生日期:</td>
			<td><input class="easyui-datebox" id="reportBirthday"
				name="reportBirthday" value="${inpatientInfo.reportBirthday }"
				data-options="required:true" style="width: 60%"
				missingMessage="请输入参数单位" /></td>
		</tr>
		<tr>
			<td>年龄:</td>
			<td><input class="easyui-textbox" id="reportAge"
				name="reportAge" value="${inpatientInfo.reportAge }" data-options=""
				style="width: 60%" /></td>
			<td>年龄单位(年月天):</td>
			<td><input class="easyui-textbox" id="reportAgeunit"
				name="reportAgeunit" value="${inpatientInfo.reportAgeunit }"
				data-options="" style="width: 60%" /></td>
		</tr>
		<tr>
			<td>职业代码:</td>
			<td><input id="CodeOccupation" name="profCode"
				value="${inpatientInfo.profCode }"
				onkeydown="KeyDown(0,'CodeOccupation')" data-options=""
				style="width: 60%" /></td>
			<td>工作单位:</td>
			<td><input class="easyui-textbox" id="workName" name="workName"
				value="${inpatientInfo.workName }" data-options=""
				style="width: 60%" /></td>
		</tr>
		<tr>
			<td>工作单位电话:</td>
			<td><input class="easyui-textbox" id="workTel" name="workTel"
				value="${inpatientInfo.workTel }" data-options="" style="width: 60%" /></td>
			<td>单位编码:</td>
			<td><input class="easyui-textbox" id="workZip" name="workZip"
				value="${inpatientInfo.workZip }" data-options="" style="width: 60%" /></td>
		</tr>
		<tr>
			<td>户口家庭住址:</td>
			<td><input class="easyui-textbox" id="home" name="home"
				value="${inpatientInfo.home }" data-options="required:true"
				style="width: 60%" missingMessage="请输入户口家庭住址" /></td>
			<td>家庭电话:</td>
			<td><input class="easyui-textbox" id="homeTel" name="homeTel"
				value="${inpatientInfo.homeTel }" data-options="required:true"
				style="width: 60%" missingMessage="请输入家庭电话" /></td>
		</tr>
		<tr>
			<td>户口或家庭邮编:</td>
			<td><input class="easyui-textbox" id="homeZip" name="homeZip"
				value="${inpatientInfo.homeZip }" data-options="" style="width: 60%" /></td>
			<td>籍贯:</td>
			<td><input class="easyui-textbox" id="dist" name="dist"
				value="${inpatientInfo.dist }" data-options="required:true"
				style="width: 60%" missingMessage="请输入籍贯" /></td>
		</tr>
		<tr>
			<td>出生地代码:</td>
			<td><input class="easyui-textbox" id="birthArea"
				name="birthArea" value="${inpatientInfo.birthArea }" data-options=""
				style="width: 60%" /></td>
			<td>民族:</td>
			<td><input id="CodeNationality" name="nationCode"
				value="${inpatientInfo.nationCode }"
				onkeydown="KeyDown(0,'CodeNationality')" data-options=""
				style="width: 60%" /></td>
		</tr>
		<tr>
			<td>联系人姓名:</td>
			<td><input class="easyui-textbox" id="linkmanName"
				name="linkmanName" value="${inpatientInfo.linkmanName }"
				data-options="required:true" style="width: 60%"
				missingMessage="请输入联系人姓名" /></td>
			<td>联系人电话:</td>
			<td><input class="easyui-textbox" id="linkmanTel"
				name="linkmanTel" value="${inpatientInfo.linkmanTel }"
				data-options="required:true" style="width: 60%"
				missingMessage="请输入联系人电话" /></td>
		</tr>
		<tr>
			<td>联系人地址:</td>
			<td><input class="easyui-textbox" id="linkmanAddress"
				name="linkmanAddress" value="${inpatientInfo.linkmanAddress }"
				data-options="required:true" style="width: 60%"
				missingMessage="联系人地址" /></td>
			<td>关系:</td>
			<td><input id="CodeRelation" name="relaCode"
				value="${inpatientInfo.relaCode }"
				onkeydown="KeyDown(0,'CodeRelation')" data-options=""
				style="width: 60%" /></td>
		</tr>
		<tr>
			<td>婚姻状况:</td>
			<td><input id="CodeMarry" name="mari"
				value="${inpatientInfo.mari }" onkeydown="KeyDown(0,'CodeMarry')"
				data-options="" style="width: 60%" /></td>
			<td>国籍:</td>
			<td><input id="CodeCountry" name="counCode"
				value="${inpatientInfo.counCode }"
				onkeydown="KeyDown(0,'CodeCountry')" data-options=""
				style="width: 60%" /></td>
		</tr>
		<tr>
			<td>身高:</td>
			<td><input class="easyui-textbox" id="height" name="height"
				value="${inpatientInfo.height }" data-options="" style="width: 60%" /></td>
			<td>体重:</td>
			<td><input class="easyui-textbox" id="weight" name="weight"
				value="${inpatientInfo.weight }" data-options="" style="width: 60%" /></td>
		</tr>
		<tr>
			<td>血压:</td>
			<td><input class="easyui-textbox" id="bloodDress"
				name="bloodDress" value="${inpatientInfo.bloodDress }"
				data-options="" style="width: 60%" /></td>
			<td>血型编码:</td>
			<td><input id="CodeBloodtype" name="bloodCode"
				value="${inpatientInfo.bloodCode }"
				onkeydown="KeyDown(0,'CodeBloodtype')" data-options=""
				style="width: 60%" missingMessage="请输入备注" /></td>
		</tr>
		<tr>
			<td>重大疾病标志:</td>
			<td><input class="easyui-combobox" id="hepatitisFlag"
				name="hepatitisFlag" value="${inpatientInfo.hepatitisFlag }"
				data-options="valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]"
				style="width: 60%" missingMessage="请输入参数单位" /></td>
			<td>过敏标志:</td>
			<td><input class="easyui-combobox" id="anaphyFlag"
				name="anaphyFlag" value="${inpatientInfo.anaphyFlag }"
				data-options="valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]"
				style="width: 60%" missingMessage="请输入参数上限" /></td>
		</tr>

	</table>
	<div style="text-align: center; padding: 5px">
		<c:if test="${empty inpatientInfo.id}">
			<a href="javascript:submit(1);" class="easyui-linkbutton"
				data-options="iconCls:'icon-save'">连续添加</a>
		</c:if>
		<a id="hospitalOKbtn" href="javascript:submit(0)"
			data-options="iconCls:'icon-save'" class="easyui-linkbutton">确定</a> <a
			id="hospitalClearbtn" href="javascript:void(0)"
			data-options="iconCls:'icon-clear'" class="easyui-linkbutton">清空</a>
		<a id="closelog" href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'">关闭</a>
	</div>

	<script type="text/javascript">
		//关闭添加Dialog
		$(function() {
			$("#closelog").linkbutton({
				onClick : function() {
					$("#addinfo").dialog('close');f
				}
			});
			//清空添加form
			$("#hospitalClearbtn").linkbutton({
				onClick : function() {
					$("#editForm").form('reset');
				}
			})
		});
	</script>
</body>

</html>
