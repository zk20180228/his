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
					<td class="honry-lable">
						性别：
					</td>
					<td>
						${sex }&nbsp;
					</td>										
				</tr>				
				<tr>
					<td class="honry-lable">
						出生日期：
					</td>
					<td>
						<fmt:formatDate value="${patient.patientBirthday}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;
					</td>
					<td class="honry-lable">
						联系方式：
					</td>
					<td class="honry-view">
						${patient.patientPhone }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						家庭地址：
					</td>
					<td class="honry-view" colspan="3">
						${patient.patientCity }&nbsp;
						${patient.patientAddress }&nbsp;
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