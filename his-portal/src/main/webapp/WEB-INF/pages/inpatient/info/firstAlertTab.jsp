<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
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
		<table id="friTable" class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
						<tr>
							<td>住院流水号：</td>
			    			<td><input class="easyui-textbox" id="inpatientNo" name="inpatientNo" value="${inpatientInfo.inpatientNo}"  readonly
			    			 style="width:60%"/></td>
			    			<td>医疗类别：</td>
			    			<td>
			    				<input id="CodeMedicaltype"  name="medicalType" value="${inpatientInfo.medicalType}" onkeydown="KeyDown(0,'CodeMedicaltype')" readonly
			    				   style="width:60%" />
			    			</td>
			    		</tr>
						<tr>
							<td>病历号:</td>
			    			<td><input class="easyui-validatebox" id="medicalrecordId" name="medicalrecordId" value="${inpatientInfo.medicalrecordId }"  
			    			readonly  style="width:60%" style="width:60%" onkeydown="KeyDownByp('medicalrecordId')"/></td>
		    			
							<td>就诊卡号：</td>
							<td><input class="easyui-validatebox" id="idcardNo" name="idcardNo" value="${inpatientInfo.idcardNo }" readonly 
							 style="width:60%" onkeydown="KeyDownByp('idcardNo')"/></td>
						</tr>
						<tr>					
							<td>医疗证号:</td>
			    			<td><input class="easyui-textbox" id="mcardNo" name="mcardNo" value="${inpatientInfo.mcardNo }" readonly
			    			  style="width:60%" /></td>
							<td>姓名:</td>
			    			<td><input class="easyui-textbox" id="patientName" name="patientName" value="${inpatientInfo.patientName }" readonly
			    			 style="width:60%" /></td>
						</tr>
						<tr>					
							<td>证件类型:</td>
			    			<td>
			    				<input id="CodeCertificate" name="certificatesType" value="${inpatientInfo.certificatesType }" readonly
			    				onkeydown="KeyDown(0,'CodeCertificate')"  style="width:60%"/>
			    			</td>
							<td>证件号码:</td>
			    			<td><input class="easyui-textbox" id="certificatesNo" name="certificatesNo" value="${inpatientInfo.certificatesNo }" readonly
			    			 style="width:60%" /></td>
						</tr>
						<tr>					
							<td>性别:</td>
			    			<td><input id="CodeSex" name="reportSex" value="${inpatientInfo.reportSex }" onkeydown="KeyDown(0,'CodeSex')"
			    			readonly  style="width:60%"  /></td>
							<td>出生日期:</td>
			    			<td><input class="easyui-datebox" id="reportBirthday" name="reportBirthday" value="${inpatientInfo.reportBirthday }" readonly 
			    			 style="width:60%" /></td>
						</tr>
						<tr>					
							<td>年龄:</td>
			    			<td><input class="easyui-textbox" id="reportAge" name="reportAge" value="${inpatientInfo.reportAge }" 
			    			 readonly  style="width:60%" /></td>
							<td>年龄单位(年月天):</td>
			    			<td><input class="easyui-textbox" id="reportAgeunit" name="reportAgeunit" value="${inpatientInfo.reportAgeunit }" readonly 
			    			 style="width:60%" /></td>
						</tr>
						<tr>					
							<td>职业代码:</td>
			    			<td><input  id="CodeOccupation" name="profCode" value="${inpatientInfo.profCode }" 
			    			readonly onkeydown="KeyDown(0,'CodeOccupation')"  style="width:60%"  /></td>
							<td>工作单位:</td>
			    			<td><input class="easyui-textbox" id="workName" name="workName" value="${inpatientInfo.workName }" readonly  
			    			 style="width:60%" /></td>
						</tr>
						<tr>					
							<td>工作单位电话:</td>
			    			<td><input class="easyui-textbox" id="workTel" name="workTel" value="${inpatientInfo.workTel }" readonly 
			    			 style="width:60%" /></td>
							<td>单位编码:</td>
			    			<td><input class="easyui-textbox" id="workZip" name="workZip" value="${inpatientInfo.workZip }" readonly 
			    			 style="width:60%" /></td>
						</tr>
						<tr>					
							<td>户口家庭住址:</td>
			    			<td><input class="easyui-textbox" id="home" name="home" value="${inpatientInfo.home }" 
			    			readonly  style="width:60%" /></td>
							<td>家庭电话:</td>
			    			<td><input class="easyui-textbox" id="homeTel" name="homeTel" value="${inpatientInfo.homeTel }" readonly  style="width:60%"/></td>
		    			</tr>
		    			<tr>
							<td>户口或家庭邮编:</td>
			    			<td><input class="easyui-textbox" id="homeZip" name="homeZip" value="${inpatientInfo.homeZip }" readonly  style="width:60%" /></td>
							<td>籍贯:</td>
			    			<td><input class="easyui-textbox" id="dist" name="dist" value="${inpatientInfo.dist }" readonly  style="width:60%" /></td>
		    			</tr>
		    			<tr>
							<td>出生地代码:</td>
			    			<td><input class="easyui-textbox" id="birthArea" name="birthArea" value="${inpatientInfo.birthArea }"readonly   style="width:60%" /></td>
							<td>民族:</td>
			    			<td><input id="CodeNationality" name="nationCode" value="${inpatientInfo.nationCode }" readonly onkeydown="KeyDown(0,'CodeNationality')"  style="width:60%" /></td>
		    			</tr>
		    			<tr>
							<td>联系人姓名:</td>
			    			<td><input class="easyui-textbox" id="linkmanName" name="linkmanName" value="${inpatientInfo.linkmanName }" readonly  style="width:60%" /></td>
							<td>联系人电话:</td>
			    			<td><input class="easyui-textbox" id="linkmanTel" name="linkmanTel" value="${inpatientInfo.linkmanTel }" readonly  style="width:60%" /></td>
		    			</tr>
		    			<tr>
							<td>联系人地址:</td>
			    			<td><input class="easyui-textbox" id="linkmanAddress" name="linkmanAddress" value="${inpatientInfo.linkmanAddress }" readonly  style="width:60%" /></td>
							<td>关系:</td>
			    			<td><input  id="CodeRelation" name="relaCode" value="${inpatientInfo.relaCode }" onkeydown="KeyDown(0,'CodeRelation')" readonly  style="width:60%"  /></td>
		    			</tr>
		    			<tr>					
							<td>婚姻状况:</td>
			    			<td><input id="CodeMarry" name="mari" value="${inpatientInfo.mari }" onkeydown="KeyDown(0,'CodeMarry')" readonly  style="width:60%"  /></td>
							<td>国籍:</td>
			    			<td><input id="CodeCountry" name="counCode" value="${inpatientInfo.counCode }" onkeydown="KeyDown(0,'CodeCountry')" readonly  style="width:60%" /></td>
						</tr>
						<tr>					
							<td>身高:</td>
			    			<td><input class="easyui-textbox" id="height" name="height" value="${inpatientInfo.height }"  readonly style="width:60%" /></td>
							<td>体重:</td>
			    			<td><input class="easyui-textbox" id="weight" name="weight" value="${inpatientInfo.weight }"  readonly style="width:60%" /></td>
						</tr>
						<tr>					
							<td style="background-color:#CCFFFF">预约金额:</td>
			    			<td><input class="easyui-textbox"  id="prepayCost" name="prepayCost" value="${inpatientInfo.prepayCost }" data-options="required:true" 
			    			 style="width:60%" missingMessage="请输入预约金"/></td>
							<td style="background-color:#CCFFFF">警戒线:</td>
			    			<td><input class="easyui-textbox"  id="moneyAlert"  name="moneyAlert" value="${inpatientInfo.moneyAlert }" 
			    			data-options="required:true"   style="width:60%" missingMessage="请输入警戒线"/></td>
						</tr>
						<tr>					
							<td style="background-color:#CCFFFF">警戒线开始时间:</td>
			    			<td><input class="easyui-datebox" id=alterBegin name="alterBegin" value="${inpatientInfo.alterBegin }"  
			    			data-options="required:true"  style="width:60%" missingMessage="请输入警戒线开始时间"/></td>
							<td style="background-color:#CCFFFF">警戒线结束时间:</td>
			    			<td><input class="easyui-datebox" id="alterEnd" name="alterEnd" value="${inpatientInfo.alterEnd }" 
			    			data-options="required:true"  style="width:60%" missingMessage="请输入警戒线结束时间"/></td>
						</tr>
						<tr>					
							<td>血压:</td>
			    			<td><input class="easyui-textbox" id="bloodDress" name="bloodDress" value="${inpatientInfo.bloodDress }"  readonly style="width:60%"  /></td>
							<td>血型编码:</td>
			    			<td><input  id="CodeBloodtype" name="bloodCode" value="${inpatientInfo.bloodCode }"  readonly onkeydown="KeyDown(0,'CodeBloodtype')"  
			    			  style="width:60%" /></td>
		    			</tr>
		    			<tr>
							<td>重大疾病标志:</td>
			    			<td><input class="easyui-combobox" id="hepatitisFlag" name="hepatitisFlag" readonly value="${inpatientInfo.hepatitisFlag }" 
			    			data-options=" valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]"  style="width:60%" 
			    			/></td>
							<td>过敏标志:</td>
			    			<td><input class="easyui-combobox" id="anaphyFlag" name="anaphyFlag" readonly value="${inpatientInfo.anaphyFlag }" 
			    			data-options="valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]" 
			    			style="width:60%"/></td>
		    			</tr>
		    			
		    	</table>
				<div style="text-align: center; padding: 5px">
					<c:if test="${empty inpatientInfo.id}">
						<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
					</c:if>
					<a id="hospitalOKbtn" href="javascript:submit(0)" data-options="iconCls:'icon-save'"  class="easyui-linkbutton">确定</a>
					<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-clear'" onclick="clear()" class="easyui-linkbutton">清空</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
				</div>
	</body>
	
	</html>
