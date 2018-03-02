<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>病案首页编辑界面</title>
<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
</head>
<body>
	<div id="listeidt" class="easyui-panel" style="height:100%;border: none;">
		<div style="padding: 5px 5px 0px 5px;border: none;">
			<form id="editForm" method="post">
				<div>
					<font style="padding-left: 45%;font-size: 28px">患者病案首页</font>
				</div>
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="honry-lable"  style="font-size: 14;width:150px">
						患者姓名：
						</td>
						<td style="font-size: 14;width:150px">
							${emrFirst.name}
						</td>
						<td  class="honry-lable" style="font-size: 14">
							医疗保险号：
						</td>
						<td style="font-size: 14">
							${emrFirst.siNo}
						</td>
					</tr>
					<tr>
						<td class="honry-lable"  style="font-size: 14">
							出生日期：
						</td>
						<td style="font-size: 14">
							${emrFirst.birth}
						</td>
						<td  class="honry-lable"  style="font-size: 14">
							身份证号：
						</td>
						<td style="font-size: 14" >
							${emrFirst.idNo}
						</td>
					</tr>
					<tr>
						<td  class="honry-lable"  style="font-size: 14">
							性&nbsp;别：
						</td>
						<td style="font-size: 14">
							${emrFirst.strSex}
						</td>
						<td class="honry-lable" style="font-size: 14">
							婚姻状况：
						</td>
						<td style="font-size: 14">
							${emrFirst.strMarriage}
						</td>
					</tr>
					<tr>
						<td  class="honry-lable"  style="font-size: 14">
							民&nbsp;族：
						</td>
						<td style="font-size: 14">
							${emrFirst.strFolk}
						</td>
						<td class="honry-lable" style="font-size: 14">
							国&nbsp;籍：
						</td>
						<td style="font-size: 14">
							${emrFirst.strNation}
						</td>
					</tr>
					<tr>
						<td  class="honry-lable"  style="font-size: 14">
							患者职业：
						</td>
						<td style="font-size: 14">
							${emrFirst.strProfeesion}
						</td>
						<td class="honry-lable" style="font-size: 14">
							出生地：
						</td>
						<td style="font-size: 14">
							${emrFirst.birthPlace}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							工作电话：
						</td>
						<td style="font-size: 14">
							${emrFirst.jobPhone}
						</td>
						<td class="honry-lable" style="font-size: 14">
							工作单位和地址：
						</td>
						<td style="font-size: 14">
							${emrFirst.jobAddress}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							工作地点邮编：
						</td>
						<td style="font-size: 14">
							${emrFirst.jobPost}
						</td>
						<td class="honry-lable" style="font-size: 14">
							户口地址：
						</td>
						<td style="font-size: 14">
							${emrFirst.houseHold}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							家庭电话：
						</td>
						<td style="font-size: 14">
							${emrFirst.homePhone}
						</td>
						<td class="honry-lable" style="font-size: 14">
							户口所在地邮编：
						</td>
						<td style="font-size: 14">
							${emrFirst.homePost}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							联系人：
						</td>
						<td style="font-size: 14">
							${emrFirst.link}
						</td>
						<td class="honry-lable" style="font-size: 14">
							联系人关系：
						</td>
						<td style="font-size: 14">
							${emrFirst.strLinkRelation}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							联系人电话：
						</td>
						<td style="font-size: 14">
							${emrFirst.linkPhone}
						</td>
						<td class="honry-lable" style="font-size: 14">
							联系人地址：
						</td>
						<td style="font-size: 14">
							${emrFirst.linkAddress}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							入院时间：
						</td>
						<td style="font-size: 14">
							${emrFirst.inTime}
						</td>
						<td class="honry-lable" style="font-size: 14">
							入院科别：
						</td>
						<td style="font-size: 14">
							${emrFirst.strInDept}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							所在病区：
						</td>
						<td style="font-size: 14">
							${emrFirst.strInNation}
						</td>
						<td class="honry-lable" style="font-size: 14">
							术前住院天数：
						</td>
						<td style="font-size: 14">
							${emrFirst.opDay}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							转科日期：
						</td>
						<td style="font-size: 14">
							${emrFirst.tranTime}
						</td>
						<td class="honry-lable" style="font-size: 14">
							转科科别：
						</td>
						<td style="font-size: 14">
							${emrFirst.strTranDept}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							转科病区：
						</td>
						<td style="font-size: 14">
							${emrFirst.strTranNation}
						</td>
						<td class="honry-lable" style="font-size: 14">
							再转科别：
						</td>
						<td style="font-size: 14">
							${emrFirst.strAgainTranDept}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							出院日期：
						</td>
						<td style="font-size: 14">
							${emrFirst.outTime}
						</td>
						<td class="honry-lable" style="font-size: 14">
							出院科别：
						</td>
						<td style="font-size: 14">
							${emrFirst.strOutDept}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							出院病区：
						</td>
						<td style="font-size: 14">
							${emrFirst.strOutNation}
						</td>
						<td class="honry-lable" style="font-size: 14">
							实际住院天数：
						</td>
						<td style="font-size: 14">
							${emrFirst.inpatientDay}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							死亡时间：
						</td>
						<td style="font-size: 14">
							${emrFirst.deathTime}
						</td>
						<td class="honry-lable" style="font-size: 14">
							死亡原因：
						</td>
						<td style="font-size: 14">
							${emrFirst.deathReson}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							科主任：
						</td>
						<td style="font-size: 14">
							${emrFirst.strDeptHead}
						</td>
						<td class="honry-lable" style="font-size: 14">
							（副）主任医师：
						</td>
						<td style="font-size: 14">
							${emrFirst.strChiefDoc }
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							主治医师：
						</td>
						<td style="font-size: 14">
							${emrFirst.strAttendingDoc}
						</td>
						<td class="honry-lable" style="font-size: 14">
							住院医师：
						</td>
						<td style="font-size: 14">
							${emrFirst.strInpatientDoc}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							进修医生：
						</td>
						<td style="font-size: 14">
							${emrFirst.strRefresherDoc }
						</td>
						<td class="honry-lable" style="font-size: 14">
							研究生实习医师：
						</td>
						<td style="font-size: 14">
							${emrFirst.strGraduateIntern }
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							门急诊诊断：
						</td>
						<td style="font-size: 14"colspan="6">
								<textarea readonly="readonly" style="width: 100%;height: 50px;font-size: 14px;border: 0px">
								${emrFirst.outPatientDiag }</textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							入院状态：
						</td>
						<td style="font-size: 14">
							${emrFirst.inState}
						</td>
						<td class="honry-lable" style="font-size: 14">
							入院后确诊日期：
						</td>
						<td style="font-size: 14">
							${emrFirst.diagTime}
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							出院诊断1：
						</td>
						<td style="font-size: 14"colspan="6">
							<textarea readonly="readonly" style="width: 100%;height: 50px;font-size: 14px;border: 0px">
								${emrFirst.diag1 }</textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							出院诊断2：
						</td>
						<td style="font-size: 14"colspan="6">
							<textarea readonly="readonly" style="width: 100%;height: 50px;font-size: 14px;border: 0px">
								${emrFirst.diag2 }</textarea>
							
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							出院诊断3：
						</td>
						<td style="font-size: 14"colspan="6">
							<textarea readonly="readonly" style="width: 100%;height: 50px;font-size: 14px;border: 0px">
								${emrFirst.diag3 }</textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							入院诊断：
						</td>
						<td style="font-size: 14"colspan="6">
							<textarea readonly="readonly" style="width: 100%;height: 50px;font-size: 14px;border: 0px">
								${emrFirst.inDiag }</textarea>
						</td>
					</tr>
				</table>
			</form>
    	</div>  
	</div>
</body>