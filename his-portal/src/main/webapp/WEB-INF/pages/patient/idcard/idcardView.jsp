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
<body>
	<div id="panelEast" class="easyui-panel" data-options="title:'就诊卡查看',iconCls:'icon-form'">
		<div style="padding: 5px">
			
			<fieldset style="padding: 1%">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
					<td class="honry-lable" >
						患者姓名：
					</td>
					<td class="honry-view"  width="30%">
						${patient.patientName }&nbsp;
					</td>
					<td class="honry-lable"  width="20%">
						拼音码：
					</td>
					<td class="honry-view"  width="30%">
						${patient.patientPinyin }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						五笔码：
					</td>
					<td class="honry-view">
						${patient.patientWb }&nbsp;
					</td>
					<td class="honry-lable">
						自定义码：
					</td>
					<td class="honry-view">
						${patient.patientInputcode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						性别：
					</td>
					<td>
						${sex }&nbsp;
					</td>
					<td class="honry-lable">
						出生日期：
					</td>
					<td>
						<fmt:formatDate value="${patient.patientBirthday}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						家庭地址：
					</td>
					<td class="honry-view">
						${patient.patientCity }&nbsp;
						${patient.patientAddress }&nbsp;
					</td>
					<td class="honry-lable">
						门牌号：
					</td>
					<td class="honry-view">
						${patient.patientDoorno }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						电话：
					</td>
					<td class="honry-view">
						${patient.patientPhone }&nbsp;
					</td>
					<td class="honry-lable">
						证件类型：
					</td>
					<td class="honry-view">
						${certificatestype}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						证件号码：
					</td>
					<td class="honry-view">
						${patient.patientCertificatesno}&nbsp;
					</td>
					<td class="honry-lable">
						出生地：
					</td>
					<td class="honry-view">
						${patient.patientBirthplace}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						国籍：
					</td>
					<td class="honry-view">
						${nationality}&nbsp;
					</td>
					<td class="honry-lable">
						民族：
					</td>
					<td class="honry-view">
						${nation}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						工作单位：
					</td>
					<td class="honry-view">
						${patient.patientWorkunit }&nbsp;
					</td>
					<td class="honry-lable">
						单位电话：
					</td>
					<td class="honry-view">
						${patient.patientWorkphone }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						婚姻状况：
					</td>
					<td class="honry-view">
						${warriage }&nbsp;
					</td>
					<td class="honry-lable">
						职业：
					</td>
					<td class="honry-view">
						${occupation }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						医保手册号：
					</td>
					<td class="honry-view">
						${patient.patientHandbook }&nbsp;
					</td>
					<td class="honry-lable">
						电子邮箱：
					</td>
					<td class="honry-view">
						${patient.patientEmail }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						母亲姓名：
					</td>
					<td class="honry-view">
						${patient.patientMother }&nbsp;
					</td>
					<td class="honry-lable">
						联系人：
					</td>
					<td class="honry-view">
						${patient.patientLinkman }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						联系人关系：
					</td>
					<td class="honry-view">
						${relation }&nbsp;
					</td>
					<td class="honry-lable">
						联系人地址：
					</td>
					<td class="honry-view">
						${patient.patientLinkaddress }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						联系人门牌号：
					</td>
					<td class="honry-view">
						${patient.patientLinkdoorno }&nbsp;
					</td>
					<td class="honry-lable">
						联系电话：
					</td>
					<td class="honry-view">
						${patient.patientLinkphone }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						病历号：
					</td>
					<td class="honry-view" colspan="3">
						${patient.medicalrecordId}&nbsp;
					</td>
				</tr>
			</table>
			</fieldset>
			
			<fieldset style="padding: 1%">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="honry-lable">
						卡号：
					</td>
					<td class="honry-view">
						${idcard.idcardNo }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						建卡时间：
					</td>
					<td class="honry-view">
						<fmt:formatDate value="${idcard.idcardCreatetime }" pattern="yyyy-MM-dd HH:mm:ss"/>	&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						卡类型：
					</td>
					<td class="honry-view">
						${idcardType }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						操作人员：
					</td>
					<td class="honry-view">
						${idcard.idcardOperator }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						备注：
					</td>
					<td class="honry-view">
						${idcard.idcardRemark }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						停用标志：
					</td>
					<td>
						<c:if test="${idcard.stop_flg eq '1'}">
							是
						</c:if>
						<c:if test="${idcard.stop_flg eq '0'}">
							否
						</c:if>
					</td>
				</tr>
			</table>
			</fieldset>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			</div>
		</div>
		</div>
			<script>
			/**
			 * 关闭查看窗口
			 * @author  lt
			 * @date 2015-6-19 10:53
			 * @version 1.0
			 */
			function closeLayout(){
				$('#divLayout').layout('remove','east');
			}
		</script>
</body>
</html>