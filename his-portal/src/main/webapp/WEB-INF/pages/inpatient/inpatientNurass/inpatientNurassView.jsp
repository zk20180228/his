<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
	<div id="listeidt" class="easyui-panel" style="height:auto;padding: 5px 5px 0px 25px">
			<form id="infEditForm" method="post">
				<div>
					<font style="padding-left: 45%;font-size: 28px" class="title">入院护理评估记录单</font>
				</div>
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width:100%;">
						<tr>
							<td colspan="6">
								一般资料
							</td>
						</tr>
						<tr>
							<td class="honry-lable"  style="width:150px">
							姓名：
							</td>
							<td style="width:150px">
								<input class="easyui-textbox" value="${inpatientNurass.pname}" style="width:200px" readonly="true"/>
							</td>
							<td  class="honry-lable">
								性别：
							</td>
							<td>
								<input class="easyui-textbox" value="${inpatientNurass.psex}" style="width:200px" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								年龄：
							</td>
							<td>
								<input class="easyui-textbox" value="${inpatientNurass.page}" style="width:200px" readonly="true"/>
							</td>
							<td class="honry-lable">
								科别：
							</td>
							<td>
								<input class="easyui-textbox"  value="${inpatientNurass.deptName}" style="width:200px" readonly="true"/>
								<input type="hidden"  value="${inpatientNurass.deptCode}" />
							</td>
						</tr>
						<tr>
						    <td class="honry-lable">
								床号：
							</td>
							<td>
								<input class="easyui-textbox" style="width:200px"  value="${inpatientNurass.bedNo}" readonly="true"/>
							</td>
							<td  class="honry-lable">
								住院号：
							</td>
							<td>
								<input class="easyui-textbox"  value="${inpatientNurass.medicalrecodeId}" style="width:200px" readonly="true"/>
								<input type="hidden" value="${inpatientNurass.inpatientNo}"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								职业：
							</td>
							<td>
								<input class="easyui-textbox" value="${inpatientNurass.occupationa}" style="width:200px" readonly="true"/>
							</td>
							<td class="honry-lable">
								文化程度：
							</td>
							<td>
								<input id="culture" class="easyui-textbox" style="width:200px" value="${inpatientNurass.culture}" readonly="true"/>
							</td>
						</tr>
						<tr>
						   <td class="honry-lable">
								婚姻状况：
							</td>
							<td>
								<input class="easyui-textbox"  value="${inpatientNurass.marriage}" style="width:200px" readonly="true"/>
							</td>
							<td  class="honry-lable">
								患者本人电话：
							</td>
							<td>
							<input class="easyui-textbox"  value="${inpatientNurass.pphone}" style="width:200px" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								联系人：
							</td>
							<td>
							<input class="easyui-textbox"  value="${inpatientNurass.cperson}" style="width:200px" readonly="true"/>
							</td>
							<td class="honry-lable">
								联系人电话：
							</td>
							<td>
							<input class="easyui-textbox" value="${inpatientNurass.cphone}" style="width:200px" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								入院日期：
							</td>
							<td>
								<input id="bhDate" class="Wdate" type="text" value="${inpatientNurass.bhDate}" style="width:200px;" readonly="true"/>
							</td>
							<td class="honry-lable">
								入院方式：
							</td>
							<td>
								<input id="bhWay" class="easyui-textbox" style="width:200px" value="${inpatientNurass.bhWay}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								其他入院方式：
							</td>
							<td>
								<input id="bhWayOth" class="easyui-textbox" style="width:200px" value="${inpatientNurass.bhWayOth}" readonly="true"/>
							</td>
							<td class="honry-lable">
								入院诊断：
							</td>
							<td colspan="3">
								<textarea class="easyui-textbox" data-options="multiline:'true'" style="width: 100%;height: 50px;font-size: 14px"  
									name="inpatientNurass.bhDiag" value="${inpatientNurass.bhDiag}" readonly="true"></textarea>
							</td>
						</tr>	
						<tr>
							<td colspan="6">
								评估护理
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								神志：
							</td>
							<td>
								<input id="mind" class="easyui-textbox" style="width:200px" value="${inpatientNurass.mind}" readonly="true"/>
							</td>
							<td class="honry-lable">
								表情：
							</td>
							<td>
								<input id="expression" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.expression}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								情绪状态：
							</td>
							<td>
								<input id="emotion" style="width:200px" class="easyui-textbox" value="${inpatientNurass.emotion}" readonly="true"/>
							</td>
							<td class="honry-lable">
								视力：
							</td>
							<td>
								<input id="vision" style="width:200px" class="easyui-textbox" value="${inpatientNurass.vision}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								视力异常情况：
							</td>
							<td>
								<input id="visionRem" style="width:200px" class="easyui-textbox"  value="${inpatientNurass.visionRem}" readonly="true"/>
							</td>
							<td class="honry-lable">
								听力：
							</td>
							<td>
								<input id="hearing" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.hearing}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								听力异常情况：
							</td>
							<td>
								<input id="hearingRem"  class="easyui-textbox" style="width:200px" value="${inpatientNurass.hearingRem}" readonly="true"/>
							</td>
							<td class="honry-lable">
								沟通方式：
							</td>
							<td>
								<input id="commMode" class="easyui-textbox" style="width:200px" value="${inpatientNurass.commMode}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								理解能力：
							</td>
							<td>
								<input id="compAbility" class="easyui-textbox" style="width:200px" value="${inpatientNurass.compAbility}" readonly="true"/>
							</td>
							<td class="honry-lable">
								口腔黏膜：
							</td>
							<td>
								<input id="oralMucosa" style="width:200px" class="easyui-textbox" value="${inpatientNurass.oralMucosa}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								义齿：
							</td>
							<td>
								<input id="falseTooth" class="easyui-textbox" style="width:200px" value="${inpatientNurass.falseTooth}" readonly="true"/>
							</td>
							<td class="honry-lable">
								皮肤：
							</td>
							<td>
								<input id="skin" class="easyui-textbox" style="width:200px" value="${inpatientNurass.skin}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								其他皮肤状况：
							</td>
							<td>
								<input id="skinOth" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.skinOth}" readonly="true"/>
							</td>
							<td class="honry-lable" width="140px">
								压疮：
							</td>
							<td>
								<input id="sore" class="easyui-textbox" style="width:200px" value="${inpatientNurass.sore}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								压疮部位：
							</td>
							<td>
								<input id="sorePosi" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.sorePosi}" readonly="true"/>
							</td>
							<td class="honry-lable">
								压疮范围：
							</td>
							<td>
								<input id="soreRange" class="easyui-textbox" style="width:200px" value="${inpatientNurass.soreRange}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								小便：
							</td>
							<td>
								<input id="urine" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.urine}" readonly="true"/>
							</td>
							<td class="honry-lable">
								其他小便情况：
							</td>
							<td>
								<input id="urineOth" class="easyui-textbox" style="width:200px"  value="${inpatientNurass.urineOth}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								大便：
							</td>
							<td>
								<input id="shit" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.shit}" readonly="true"/>
							</td>
							<td class="honry-lable">
								腹泻：
							</td>
							<td>
								<input id="shitDiarr" class="easyui-textbox" style="width:200px" value="${inpatientNurass.shitDiarr}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								其他大便情况：
							</td>
							<td>
								<input id="shitOth" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.shitOth}" readonly="true"/>
							</td>
							<td class="honry-lable">
								自理能力：
							</td>
							<td>
								<input id="scAbility" class="easyui-textbox" style="width:200px" value="${inpatientNurass.scAbility}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								Braden评分：
							</td>
							<td>
								<input class="easyui-textbox" style="width:200px;" value="${inpatientNurass.braden}" readonly="true"/>
							</td>
							<td class="honry-lable">
								Morse评分 ：
							</td>
							<td>
								<input class="easyui-textbox" style="width:200px" value="${inpatientNurass.morse}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								体型：
							</td>
							<td>
								<input id="shape" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.shape}" readonly="true"/>
							</td>
							<td class="honry-lable">
								饮食习惯：
							</td>
							<td>
								<input id="eating" class="easyui-textbox" style="width:200px" value="${inpatientNurass.eating}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								其他饮食习惯：
							</td>
							<td>
								<input id="eatingOth" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.eatingOth}" readonly="true"/>
							</td>
							<td class="honry-lable">
								忌食：
							</td>
							<td>
								<input class="easyui-textbox" style="width:200px" value="${inpatientNurass.eatingDiet}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								异常忌食习惯：
							</td>
							<td>
								<input id="eatingAbn" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.eatingAbn}" readonly="true"/>
							</td>
							<td class="honry-lable">
								吸烟：
							</td>
							<td>
								<input id="smoke" class="easyui-textbox" style="width:200px" value="${inpatientNurass.smoke}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								吸烟情况：
							</td>
							<td>
								<input id="smokeRem" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.smokeRem}" readonly="true"/>
							</td>
							<td class="honry-lable">
								饮酒：
							</td>
							<td>
								<input id="wine" class="easyui-textbox" style="width:200px" value="${inpatientNurass.wine}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								饮酒情况：
							</td>
							<td>
								<input id="wineRemark" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.wineRemark}" readonly="true"/>
							</td>
							<td class="honry-lable">
								睡眠：
							</td>
							<td>
								<input id="sleep" class="easyui-textbox" style="width:200px" value="${inpatientNurass.sleep}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								每日睡眠：
							</td>
							<td>
								<input class="easyui-textbox" style="width:200px;" value="${inpatientNurass.sleepDay}" readonly="true"/>
							</td>
							<td class="honry-lable">
								药物辅助睡眠：
							</td>
							<td>
								<input id="sleepMedi" class="easyui-textbox" style="width:200px" value="${inpatientNurass.sleepMedi}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								药物辅助睡眠情况：
							</td>
							<td>
								<input id="sleepMediRem" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.sleepMediRem}" readonly="true"/>
							</td>
							<td class="honry-lable">
								家属态度：
							</td>
							<td>
								<input id="fstate" class="easyui-textbox" style="width:200px" value="${inpatientNurass.fstate}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								宗教信仰：
							</td>
							<td>
								<input id="religion" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.religion}" readonly="true"/>
							</td>
							<td class="honry-lable">
								宗教信仰情况：
							</td>
							<td>
								<input id="religionRem" class="easyui-textbox" style="width:200px" value="${inpatientNurass.religionRem}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								既往史：
							</td>
							<td>
								<input id="pastHis" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.pastHis}" readonly="true"/>
							</td>
							<td class="honry-lable">
								其他既往史：
							</td>
							<td>
								<input id="pastHisOther" class="easyui-textbox" style="width:200px" value="${inpatientNurass.pastHisOther}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								过敏史：
							</td>
							<td>
								<input id="allerHis" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.allerHis}" readonly="true"/>
							</td>
							<td class="honry-lable">
								食物：
							</td>
							<td>
								<input id="allerHisFood" class="easyui-textbox" style="width:200px" value="${inpatientNurass.allerHisFood}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								药物：
							</td>
							<td>
								<input id="allerHisMedi" class="easyui-textbox" style="width:200px;" value="${inpatientNurass.allerHisMedi}" readonly="true"/>
							</td>
							<td class="honry-lable">
								其他过敏史：
							</td>
							<td>
								<input id="allerHisOther" class="easyui-textbox" style="width:200px" value="${inpatientNurass.allerHisOther}" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								责任护士：
							</td>
							<td>
								<input class="easyui-textbox" style="width:200px;" value="${inpatientNurass.nurseName}" readonly="true"/>
							</td>
							<td class="honry-lable">
								评估日期：
							</td>
							<td>
								<input id="createTime" class="Wdate" type="text" style="width:200px;" value="" readonly="true"/>
							</td>
						</tr>
				</table>
			</form>	
		<div style="text-align: center; padding: 5px;height:150px ">
			<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-textbox-filterLocal.js"></script>
	<script>
	var dateVal = "${inpatientNurass.bhDate}"
	var dateVal2 = "${inpatientNurass.createTime}"
	$("#bhDate").val(dateVal.substr(0,dateVal.length-5))
	$("#createTime").val(dateVal2.substr(0,dateVal2.length-5))
	//关闭窗口
	function closeLayout() {
		window.close();
	}
	
</script>
</body>
</html>