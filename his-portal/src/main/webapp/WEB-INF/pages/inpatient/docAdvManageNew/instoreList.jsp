<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>病案首页</title>
<%@ include file="/common/metas.jsp"%>
</head>
	<body>
		<div id="divLayout"  class="easyui-layout" data-options="fit:true" style="overflow: auto;">
				<div style="text-align: center;width: 100%;" >
					<font style="font-size: 28px;">郑州大学第一附属医院</font><br>
					<font style="font-size: 28px;">住  院  病  历</font>
				</div>
				<table id="paitentInfo" class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;width: 95%;">
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							患者姓名：
						</td>
						<td style="font-size: 14;">
							<input class="easyui-textbox"  id="patientName" style="" value="${emrFirst.name}"/>
						</td>
						<td class="honry-lable" style="font-size: 14;">
							患者性别：
						</td>
						<td style="font-size: 14;">
							<input class="easyui-textbox"  id="patientSex" style="" value="${emrFirst.name}"/>
						</td>
						<td class="honry-lable" style="font-size: 14;">
							入院时间：
						</td>
						<td style="font-size: 14;">
							<input id="patientbirthday" class="Wdate" type="text"  data-options="showSeconds:true" onClick="WdatePicker({maxDate:'%y-%M-%d'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							患者年龄：
						</td>
						<td style="font-size: 14;">
							<input class="easyui-textbox"  id="patientAge" style="" value="${emrFirst.name}"/>
						</td>
						<td class="honry-lable" style="font-size: 14;">
							民族：
						</td>
						<td style="font-size: 14;">
							<input class="easyui-combobox" id="patientNation" style="" value="${emrFirst.name}"/>
						</td>
						<td class="honry-lable" style="font-size: 14;">
							病史叙述者：
						</td>
						<td style="font-size: 14;">
							<input class="easyui-textbox"  id="patientTalk" style="" value="${emrFirst.name}"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							联系电话：
						</td>
						<td style="font-size: 14;">
							<input class="easyui-textbox"  id="patientTel" style="" value="${emrFirst.name}"/>
						</td>
						<td class="honry-lable" style="font-size: 14;">
							住址：
						</td>
						<td style="font-size: 14;" colspan="3">
							<input class="easyui-textbox"  id="patientAdd" style="width: 80%" value="${emrFirst.name}"/>
						</td>
					</tr>
				</table>
				<div style="text-align: center;padding-top: 20px;width: 100%;">
					<font style="font-size: 28px;">病&nbsp;&nbsp;&nbsp;&nbsp;史</font>
					<table id="paitentHitory" class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;width: 95%;">
						<tr>
							<td class="honry-lable" style="font-size: 14;">
								主诉：
							</td>
							<td style="font-size: 14;" colspan="3">
								<input class="easyui-textbox"  id="patientZhusu" style="width: 90%" value="${emrFirst.name}"  data-options="multiline:'true',height:'100px'"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="font-size: 14;">
								现病史：
							</td>
							<td style="font-size: 14;" colspan="3">
								<input class="easyui-textbox"  id="patientBingshi" style="width: 90%" value="${emrFirst.name}"  data-options="multiline:'true',height:'100px'"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="font-size: 14;">
								既往史：
							</td>
							<td style="font-size: 14;height:200px;" colspan="3">
								<table id="paitentHitory" class="honry-table" style="width: 90%;height: 90%;"  cellpadding="0" cellspacing="0"  >
									<tr>
										<td>
											平素健康状况：
										</td>
										<td>
											<input type="hidden" id="health" value="0">
											<input type="checkbox" id="health1" name="healthState">&nbsp;健康
											<input type="checkbox" id="health2" name="healthState">&nbsp;异常
										</td>
									</tr>
									<tr>
										<td>
											传染病史：
										</td>
										<td>
											<input class="easyui-textbox"  id="chuanranbing" style="width: 90%;" value="${emrFirst.name}" />
										</td>
									</tr>
									<tr>
										<td>
											预防接种史：
										</td>
										<td>
											<input class="easyui-textbox"  id="yufang" style="width: 90%;" value="${emrFirst.name}" />
										</td>
									</tr>
									<tr>
										<td>
											过敏史或不良药物反应史：
										</td>
										<td>
											<input type="checkbox" id="anaphyFlag1" name="healthState">&nbsp;有
											<input type="checkbox" id="anaphyFlag2" name="healthState">&nbsp;无
										</td>
									</tr>
									<tr>
										<td>
											过敏原：
										</td>
										<td>
											<input class="easyui-textbox"  id="guominyuan" style="width: 90%;" value="${emrFirst.name}" />
										</td>
									</tr>
									<tr>
										<td>
											临床表现：
										</td>
										<td>
											<input class="easyui-textbox"  id="linchuang" style="width: 90%;" value="${emrFirst.name}" />
										</td>
									</tr>
									<tr>
										<td>
											外伤史：
										</td>
										<td>
											<input class="easyui-textbox"  id="waishang" style="width: 90%;" value="${emrFirst.name}" />
										</td>
									</tr>
									<tr>
										<td>
											手术史：
										</td>
										<td>
											<input class="easyui-textbox"  id="shoushu" style="width: 90%;" value="${emrFirst.name}" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="font-size: 14;">
								个人史：
							</td>
							<td style="font-size: 14;height:200px;" colspan="3">
								<table id="paitentself" class="honry-table" style="width: 90%;height: 90%;"  cellpadding="0" cellspacing="0"  >
									<tr>
										<td>
											职业：
										</td>
										<td>
											<input class="easyui-textbox"  id="chuanranbing" style="width: 200px;" value="${emrFirst.name}" />
										</td>
									</tr>
									<tr>
										<td>
											地方病居住情况：
										</td>
										<td>
											<input class="easyui-textbox"  id="chuanranbing" style="width: 90%;" value="${emrFirst.name}" />
										</td>
									</tr>
									<tr>
										<td>
											预防接种史：
										</td>
										<td>
											<input class="easyui-textbox"  id="yufang" style="width: 90%;" value="${emrFirst.name}" />
										</td>
									</tr>
									<tr>
										<td>
											过敏史或不良药物反应史：
										</td>
										<td>
											<input type="checkbox" id="health" name="healthState">&nbsp;有
											<input type="checkbox" id="health" name="healthState">&nbsp;无
										</td>
									</tr>
									<tr>
										<td>
											过敏原：
										</td>
										<td>
											<input class="easyui-textbox"  id="yufang" style="width: 90%;" value="${emrFirst.name}" />
										</td>
									</tr>
									<tr>
										<td>
											临床表现：
										</td>
										<td>
											<input class="easyui-textbox"  id="yufang" style="width: 90%;" value="${emrFirst.name}" />
										</td>
									</tr>
									<tr>
										<td>
											外伤史：
										</td>
										<td>
											<input class="easyui-textbox"  id="yufang" style="width: 90%;" value="${emrFirst.name}" />
										</td>
									</tr>
									<tr>
										<td>
											手术史：
										</td>
										<td>
											<input class="easyui-textbox"  id="yufang" style="width: 90%;" value="${emrFirst.name}" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="font-size: 14;">
								个人史：
							</td>
							<td style="font-size: 14;height:200px;" colspan="3">
								<table id="paitentself" class="honry-table" style="width: 90%;height: 90%;"  cellpadding="0" cellspacing="0"  >
									<tr>
										<td>
											职业：
										</td>
										<td>
											<input class="easyui-textbox"  id="chuanranbing" style="width: 200px;" value="${emrFirst.name}" />
										</td>
									</tr>
									<tr>
										<td>
											地方病居住情况：
										</td>
										<td>
											<input class="easyui-textbox"  id="chuanranbing" style="width: 90%;" value="${emrFirst.name}" />
										</td>
									</tr>
									<tr>
										<td colspan="2">
											嗜烟：&nbsp;
											<input type="checkbox" id="health" name="healthState">&nbsp;有
											<input type="checkbox" id="health" name="healthState">&nbsp;无
											&nbsp;约
											<input class="easyui-textbox"  id="yufang"  />年，平均&nbsp;<input class="easyui-textbox"  id="yufang"  />支/日&nbsp;戒烟&nbsp;
											<input type="checkbox" id="health" name="healthState">&nbsp;已
											<input type="checkbox" id="health" name="healthState">&nbsp;未&nbsp;约
											<input class="easyui-textbox"  id="yufang"  />年
										</td>
									</tr>
									<tr>
										<td colspan="2">
											嗜酒：&nbsp;
											<input type="checkbox" id="health" name="healthState">&nbsp;有
											<input type="checkbox" id="health" name="healthState">&nbsp;无
											&nbsp;约
											<input class="easyui-textbox"  id="yufang"  />年，平均&nbsp;<input class="easyui-textbox"  id="yufang"  />两/日
										</td>
									</tr>
									<tr>
										<td>
											其他：
										</td>
										<td>
											<input class="easyui-textbox"  id="yufang"  />
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="font-size: 14;">
								家  族  史：<br>（注意与患者现病有关的遗传病及传染性疾病）
							</td>
							<td style="font-size: 14;" colspan="3">
								<table id="paitentself" class="honry-table" style="width: 90%;"  cellpadding="0" cellspacing="0"  >
									<tr>
										<td colspan="2">
											<input type="checkbox" id="health" name="healthState">&nbsp;有
											<input type="checkbox" id="health" name="healthState">&nbsp;无
											&nbsp;&nbsp;<input class="easyui-textbox"  id="yufang"  />
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
				<div style="text-align: center;padding-top: 20px;width: 100%;">
					<font style="font-size: 28px;">体&nbsp;格&nbsp;检&nbsp;查</font>
					<table id="paitentInfo" class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;width: 95%;">
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							体温：
						</td>
						<td style="font-size: 14;">
							<input class="easyui-textbox"  id="patientName" style="" value="${emrFirst.name}"/>℃
						</td>
						<td class="honry-lable" style="font-size: 14;">
							脉搏：
						</td>
						<td style="font-size: 14;">
							<input class="easyui-textbox"  id="patientSex" style="" value="${emrFirst.name}"/>次/分
						</td>
						<td class="honry-lable" style="font-size: 14;">
							呼吸：
						</td>
						<td style="font-size: 14;">
							<input type="text" class="easyui-textbox"  id="patientbirthday" style="" value="${emrFirst.name}"/>次/分
						</td>
						<td class="honry-lable" style="font-size: 14;">
							血压：
						</td>
						<td style="font-size: 14;">
							<input type="text" class="easyui-textbox"  id="patientbirthday" style="" value="${emrFirst.name}"/>/
							<input type="text" class="easyui-textbox"  id="patientbirthday" style="" value="${emrFirst.name}"/>mmHg
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							一般状况：
						</td>
						<td style="font-size: 14;" colspan="7">
							<table id="paitentself" class="honry-table" style="width: 90%;"  cellpadding="0" cellspacing="0"  >
								<tr>
									<td>
										神志：
									</td>
									<td>
										<input type="checkbox" id="health" name="healthState">&nbsp;清楚&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;嗜睡&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;模糊&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;昏睡&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;昏迷&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;谵妄&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;朦胧
									</td>
								</tr>
								<tr>
									<td>
										体位：
									</td>
									<td>
										<input type="checkbox" id="health" name="healthState">&nbsp;自主&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;被动&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;半卧位&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;其他&nbsp;（
										<input class="easyui-textbox"  id="patientName" />&nbsp;）
									</td>
								</tr>
								<tr>
									<td>
										发育：
									</td>
									<td>
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;不良&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;超常
									</td>
								</tr>
								<tr>
									<td>
										营养：
									</td>
									<td>
										<input type="checkbox" id="health" name="healthState">&nbsp;良好&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;中等&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;不良&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;恶病质
									</td>
								</tr>
								<tr>
									<td>
										配合检查：
									</td>
									<td>
										<input type="checkbox" id="health" name="healthState">&nbsp;配合&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;不配合&nbsp;
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							皮肤粘膜：
						</td>
						<td style="font-size: 14;" colspan="7">
							<table id="paitentself" class="honry-table" style="width: 90%;"  cellpadding="0" cellspacing="0"  >
								<tr>
									<td colspan="1"> 
										色泽：
									</td>
									<td >
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;潮红&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;苍白&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;紫绀&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;黄染&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;色素沉着
									</td>
								</tr>
								<tr>
									<td >
										温度及湿度：
									</td>
									<td >
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;冷&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;干&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;湿&nbsp;
									</td>
								</tr>
								<tr>
									<td >
										皮疹：
									</td>
									<td >
										<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;（类型及分布：
										<input class="easyui-textbox"  id="patientName" />&nbsp;）
									</td>
								</tr>
								<tr>
									<td >
										皮下出血：
									</td>
									<td >
										<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;（类型及分布：
										<input class="easyui-textbox"  id="patientName" />&nbsp;）
									</td>
								</tr>
								<tr>
									<td >
										水肿：
									</td>
									<td >
										<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;（类型及分布：
										<input class="easyui-textbox"  id="patientName" />&nbsp;）
									</td>
								</tr>
								<tr>
									<td >
										其他异常：
									</td>
									<td >
										<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;（
										<input class="easyui-textbox"  id="patientName" />&nbsp;）
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							淋 巴 结：
						</td>
						<td style="font-size: 14;"colspan="7">
							全身浅表淋巴结：
							<input type="checkbox" id="health" name="healthState">&nbsp;无肿大&nbsp;
							<input type="checkbox" id="health" name="healthState">&nbsp;肿大&nbsp;（部位及特点
							<input class="easyui-textbox"  id="patientName" />&nbsp;）
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							头部：
						</td>
						<td style="font-size: 14;" colspan="7">
							<table id="paitentself" class="honry-table" style="width: 90%;"  cellpadding="0" cellspacing="0"  >
								<tr>
									<td style="font-size: 14;"colspan="1">
										头颅：
									</td>
									<td colspan="6">
										<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
											<tr>
												<td colspan="6">
													<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;压痛&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;包块
												</td>
											</tr>
											<tr>
												<td colspan="1">
													其他异常：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（
													<input class="easyui-textbox"  id="patientName" />&nbsp;部位:&nbsp;
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
										</table>
										
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										眼：
									</td>
									<td colspan="6">
										<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
											<tr>
												<td colspan="1">
													眼睑：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;水肿&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;下垂&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;倒睫&nbsp;
												</td>
											</tr>
											<tr>
												<td colspan="1">
													结膜：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;充血&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;水肿&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;出血&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;苍白&nbsp;
												</td>
											</tr>
											<tr>
												<td colspan="1">
													眼球：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;突出&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;凹陷&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;震颤&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;运动障碍&nbsp;（
													<input type="checkbox" id="health" name="healthState">&nbsp;左&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;右&nbsp;）
												</td>
											</tr>
											<tr>
												<td colspan="1">
													 巩膜：
												</td>
												<td colspan="5">
													黄染&nbsp;（
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;）
												</td>
											</tr>
											<tr>
												<td colspan="1">
													角膜：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;异常&nbsp;（
													<input type="checkbox" id="health" name="healthState">&nbsp;左&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;右&nbsp;）
												</td>
											</tr>
											<tr>
												<td colspan="1">
													瞳孔：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;等圆&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;等大&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;不等&nbsp;&nbsp;左
													<input class="easyui-textbox"  id="patientName" />&nbsp;mm&nbsp;&nbsp;右&nbsp;
													<input class="easyui-textbox"  id="patientName" />&nbsp;mm
												</td>
											</tr>
											<tr>
												<td colspan="1">
													对光反射：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;迟钝&nbsp;（
													<input type="checkbox" id="health" name="healthState">&nbsp;左&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;右&nbsp;）
													<input type="checkbox" id="health" name="healthState">&nbsp;消失&nbsp;（
													<input type="checkbox" id="health" name="healthState">&nbsp;左&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;右&nbsp;）
												</td>
											</tr>
											<tr>
												<td colspan="1">
													其他异常：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（&nbsp;
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										耳：
									</td>
									<td colspan="6">
										<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
											<tr>
												<td colspan="1">
													外耳道分泌物：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;左&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;右&nbsp;性质：&nbsp;
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
											<tr>
												<td colspan="1">
													其他异常：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（&nbsp;
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										鼻：
									</td>
									<td colspan="6">
										<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
											<tr>
												<td colspan="1">
													鼻窦压痛：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（&nbsp;部位：&nbsp;
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
											<tr>
												<td colspan="1">
													其他异常：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（&nbsp;
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										口唇：
									</td>
									<td colspan="6">
										<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
											<tr>
												<td colspan="6">
													<input type="checkbox" id="health" name="healthState">&nbsp;红润&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;发绀&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;苍白&nbsp;
												</td>
											</tr>
											<tr>
												<td colspan="1">
													其他异常：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（&nbsp;
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										粘膜：
									</td>
									<td colspan="6">
										<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
											<tr>
												<td colspan="6">
													<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;异常&nbsp;（&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;苍白&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;出血点&nbsp;）
												</td>
											</tr>
											<tr>
												<td colspan="1">
													其他异常：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（&nbsp;
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										舌：
									</td>
									<td>
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;异常&nbsp;（
										<input type="checkbox" id="health" name="healthState">&nbsp;舌苔&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;伸舌震颤&nbsp;&nbsp;偏移
										<input type="checkbox" id="health" name="healthState">&nbsp;向左&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;向右&nbsp;）
									</td>
								</tr>
								<tr>
									<td>
										齿龈：
									</td>
									<td>
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;肿胀&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;溢脓&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;出血&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;色素沉着&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;铅线&nbsp;
									</td>
								</tr>
								<tr>
									<td>
										咽：
									</td>
									<td>
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;红肿&nbsp;
									</td>
								</tr>
								<tr>
									<td>
										扁桃体：
									</td>
									<td>
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;肿大&nbsp;（&nbsp;
										<input class="easyui-textbox"  id="patientName" />度）
									</td>
								</tr>
								<tr>
									<td>
										声音：
									</td>
									<td>
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;嘶哑&nbsp;
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							颈：
						</td>
						<td style="font-size: 14;" colspan="7">
							<table id="paitentself" class="honry-table" style="width: 90%;"  cellpadding="0" cellspacing="0"  >
								<tr>
									<td style="font-size: 14;"colspan="1">
										抵抗感：
									</td>
									<td colspan="6">
										<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										颈动脉：
									</td>
									<td colspan="6">
										<input type="checkbox" id="health" name="healthState">&nbsp;搏动正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;搏动增强&nbsp;&nbsp;一侧减弱（
										<input type="checkbox" id="health" name="healthState">&nbsp;左&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;右）
										<input type="checkbox" id="health" name="healthState">&nbsp;血管杂音
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										颈静脉：
									</td>
									<td colspan="6">
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;充盈&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;怒张
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										气管：
									</td>
									<td colspan="6">
										<input type="checkbox" id="health" name="healthState">&nbsp;居中&nbsp;&nbsp;偏移（
										<input type="checkbox" id="health" name="healthState">&nbsp;向左&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;向右）
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										肝颈静脉回流征：
									</td>
									<td colspan="6">
										<input type="checkbox" id="health" name="healthState">&nbsp;阴性&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;阳性
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										甲状腺：
									</td>
									<td colspan="6">
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;肿大&nbsp;&nbsp;（特征描述：&nbsp;
										<input class="easyui-textbox"  id="patientName" />&nbsp;）
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										其他异常：
									</td>
									<td colspan="6">
										<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（&nbsp;
										<input class="easyui-textbox"  id="patientName" />&nbsp;）
									</td>
								</tr>
							</table>
						</td>
					</tr>
				<tr>
						<td class="honry-lable" style="font-size: 14;">
							胸：
						</td>
						<td style="font-size: 14;" colspan="7">
							<table id="paitentself" class="honry-table" style="width: 90%;"  cellpadding="0" cellspacing="0"  >
								<tr>
									<td style="font-size: 14;"colspan="1">
										胸廓：
									</td>
									<td colspan="6">
										<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
											<tr>
												<td colspan="1">
													<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;桶状胸&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;扁平胸&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;鸡胸&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;漏斗胸
												</td>
											</tr>
											<tr>
												<td style="font-size: 14;"colspan="1">
													膨隆或凹陷：
													<input type="checkbox" id="health" name="healthState">&nbsp;左&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;右
												</td>
											</tr>
											<tr>
												<td style="font-size: 14;"colspan="1">
													胸骨叩痛：
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										乳房：
									</td>
									<td colspan="6">
										<input type="checkbox" id="health" name="healthState">&nbsp;正常对称&nbsp;&nbsp;异常：
										<input type="checkbox" id="health" name="healthState">&nbsp;左&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;右&nbsp;（
										<input type="checkbox" id="health" name="healthState">&nbsp;男乳女化&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;包块&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;压痛&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;乳头分泌物）
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										肺：
									</td>
									<td colspan="6">
										<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
											<tr>
												<td colspan="1">
													视诊：
												</td>
												<td colspan="5">
													呼吸运动：
													<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;异常&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;左&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;右&nbsp;（
													<input type="checkbox" id="health" name="healthState">&nbsp;增强&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;减弱）
												</td>
											</tr>
											<tr>
												<td colspan="1">
													触诊：
												</td>
												<td colspan="5">
													<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
														<tr>
															<td >
																语颤：
																<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;异常&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;左&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;右&nbsp;（
																<input type="checkbox" id="health" name="healthState">&nbsp;增强&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;减弱）
															</td>
														</tr>
														<tr>
															<td >
																胸膜摩擦感：
																<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（部位：
																<input class="easyui-textbox"  id="patientName" />&nbsp;）
															</td>
														</tr>
														<tr>
															<td >
																皮下捻发感：
																<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（部位：
																<input class="easyui-textbox"  id="patientName" />&nbsp;）
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td colspan="1">
													叩诊：
												</td>
												<td colspan="5">
													<input type="checkbox" id="health" name="healthState">&nbsp;正常清音&nbsp;异常叩诊音：
													<input type="checkbox" id="health" name="healthState">&nbsp;浊音&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;实音 &nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;过清音&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;鼓音
												</td>
											</tr>
											<tr>
												<td colspan="1">
													听诊：
												</td>
												<td colspan="5">
													<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
														<tr>
															<td >
																呼吸：
																<input type="checkbox" id="health" name="healthState">&nbsp;规整&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;不规整&nbsp;
															</td>
														</tr>
														<tr>
															<td >
																呼吸音：
																<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;异常&nbsp;（性质及部位描写：
																<input class="easyui-textbox"  id="patientName" />&nbsp;）
															</td>
														</tr>
														<tr>
															<td >
																啰音：
																<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;有（干性：
																<input type="checkbox" id="health" name="healthState">&nbsp;鼾音&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;哨笛音&nbsp;湿性：
																<input type="checkbox" id="health" name="healthState">&nbsp;大&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;中&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;小）
															</td>
														</tr>
														<tr>
															<td >
																 胸膜摩擦音：
																<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（部位：&nbsp;
																<input class="easyui-textbox"  id="patientName" />）
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td colspan="1">
													视诊：
												</td>
												<td colspan="5">
													<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
														<tr>
															<td >
																心前区隆起：
																<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;
															</td>
														</tr>
														<tr>
															<td >
																<div>
																	心尖搏动位置：
																	<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
																	<input type="checkbox" id="health" name="healthState">&nbsp;异常&nbsp;
																</div>
																<div style="margin-top: 3px;">
																	（距左锁骨中线：
																	<input type="checkbox" id="health" name="healthState">&nbsp;内&nbsp;
																	<input type="checkbox" id="health" name="healthState">&nbsp;外&nbsp;
																	<input class="easyui-textbox"  id="patientName" />cm）
																<div>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td colspan="1">
													触诊：
												</td>
												<td colspan="5">
													<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
														<tr>
															<td >
																心尖搏动：
																<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;增强&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;抬举感&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;触不清
															</td>
														</tr>
														<tr>
															<td >
																心包摩擦感：
																<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（&nbsp;
																<input class="easyui-textbox"  id="patientName" />）
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td colspan="1">
													听诊：
												</td>
												<td colspan="5">
													<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
														<tr>
															<td >
																心率：&nbsp;
																<input class="easyui-textbox"  id="patientName" />次/分
															</td>
														</tr>
														<tr>
															<td >
																心律：
																<input type="checkbox" id="health" name="healthState">&nbsp;齐&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;不齐&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;绝对不齐
															</td>
														</tr>
														<tr>
															<td >
																额外心音：
																<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;开瓣音&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;奔马律&nbsp;（
																<input type="checkbox" id="health" name="healthState">&nbsp;舒张早期&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;收缩前期&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;重叠）
															</td>
														</tr>
														<tr>
															<td >
																杂音：
																<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;有（描述强度、传导：
																<input class="easyui-textbox"  id="patientName" />&nbsp;）
															</td>
														</tr>
														<tr>
															<td >
																其他异常：
																<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;有（&nbsp;
																<input class="easyui-textbox"  id="patientName" />&nbsp;）
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								
								<tr>
									<td style="font-size: 14;"colspan="1">
										气管：
									</td>
									<td colspan="6">
										<input type="checkbox" id="health" name="healthState">&nbsp;居中&nbsp;&nbsp;偏移（
										<input type="checkbox" id="health" name="healthState">&nbsp;向左&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;向右）
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										肝颈静脉回流征：
									</td>
									<td colspan="6">
										<input type="checkbox" id="health" name="healthState">&nbsp;阴性&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;阳性
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										甲状腺：
									</td>
									<td colspan="6">
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;肿大&nbsp;&nbsp;（特征描述：&nbsp;
										<input class="easyui-textbox"  id="patientName" />&nbsp;）
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										其他异常：
									</td>
									<td colspan="6">
										<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（&nbsp;
										<input class="easyui-textbox"  id="patientName" />&nbsp;）
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							腹：
						</td>
						<td style="font-size: 14;" colspan="7">
							<table id="paitentself" class="honry-table" style="width: 90%;"  cellpadding="0" cellspacing="0"  >
								<tr>
									<td style="font-size: 14;"colspan="1">
										视诊：
									</td>
									<td colspan="6">
										<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
											<tr>
												<td>
													外形：
													<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;膨隆&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;蛙腹&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;舟状&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;尖腹 &nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;胃型&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;肠型&nbsp;
												</td>
											</tr>
											<tr>
												<td>
													腹式呼吸：
													<input type="checkbox" id="health" name="healthState">&nbsp;存在&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;消失
												</td>
											</tr>
											<tr>
												<td >
													脐：
													<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;异常&nbsp;（&nbsp;
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
											<tr>
												<td >
													其他异常：
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（&nbsp;
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										触诊：
									</td>
									<td colspan="6">
										<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
											<tr>
												<td colspan="2">
													<input type="checkbox" id="health" name="healthState">&nbsp;柔软&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;腹肌紧张&nbsp;
												</td>
											</tr>
											<tr>
												<td colspan="2">
													压痛：
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（部位：
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
											<tr>
												<td colspan="2">
													 反跳痛：
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（部位：
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
											<tr>
												<td colspan="2">
													腹部包块：
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（特征描述：
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
											<tr>
												<td colspan="2">
													肝脏：
													<input type="checkbox" id="health" name="healthState">&nbsp;未触及&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;可触及&nbsp;大小
													<input class="easyui-textbox"  id="patientName" />cm&nbsp;（特征描述：
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
											<tr>
												<td colspan="1">
													胆囊：
												</td>
												<td colspan="1">
													<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
														<tr>
															<td >
																<input type="checkbox" id="health" name="healthState">&nbsp;触及&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;未触及&nbsp;大小&nbsp;
																<input class="easyui-textbox"  id="patientName" />cm
															</td>
														</tr>
														<tr>
															<td >
																压痛：
																<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;有
															</td>
														</tr>
														<tr>
															<td >
																 Murphy征：
																<input type="checkbox" id="health" name="healthState">&nbsp;阴性&nbsp;
																<input type="checkbox" id="health" name="healthState">&nbsp;阳性
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td colspan="2">
													脾：
													<input type="checkbox" id="health" name="healthState">&nbsp;未触及&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;可触及&nbsp;腋下
													<input class="easyui-textbox"  id="patientName" />cm&nbsp;（特征描述：
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
											<tr>
												<td colspan="2">
													<div>
														肾：
														<input type="checkbox" id="health" name="healthState">&nbsp;未触及&nbsp;
														<input type="checkbox" id="health" name="healthState">&nbsp;可触及&nbsp;大小
														<input class="easyui-textbox"  id="patientName" />&nbsp;
													</div>
													<div style="margin-top: 3px">
														硬度
														<input class="easyui-textbox"  id="patientName" />&nbsp;
														<input type="checkbox" id="health" name="healthState">&nbsp;压痛&nbsp;移动度
														<input class="easyui-textbox"  id="patientName" />&nbsp;
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										叩诊：
									</td>
									<td colspan="6">
										<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
											<tr>
												<td >
												肝浊音界：
													<input type="checkbox" id="health" name="healthState">&nbsp;存在&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;缩小&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;消失
												</td>
											</tr>
											<tr>
												<td >
													肝上界：位于右锁骨中线&nbsp;
													<input class="easyui-textbox"  id="patientName" />肋间
												</td>
											</tr>
											<tr>
												<td >
													 移动性浊音：
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有
												</td>
											</tr>
											<tr>
												<td >
													肾区叩痛：
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;左&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;右&nbsp;）
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										听诊：
									</td>
									<td colspan="6">
										<table id="paitentself" class="honry-table" style="width: 100%;"  cellpadding="0" cellspacing="0"  >
											<tr>
												<td >
												肠鸣音：
													<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;亢进&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;活跃&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;减弱 &nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;消失
												</td>
											</tr>
											<tr>
												<td >
													气过水声：
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有
												</td>
											</tr>
											<tr>
												<td >
													 血管杂音：
													<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
													<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（部位：
													<input class="easyui-textbox"  id="patientName" />&nbsp;）
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							生殖器：
						</td>
						<td style="font-size: 14;"colspan="7">
							
							<input type="checkbox" id="health" name="healthState">&nbsp;未查&nbsp;
							<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
							<input type="checkbox" id="health" name="healthState">&nbsp;异常&nbsp;（
							<input class="easyui-textbox"  id="patientName" />&nbsp;）
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							肛门直肠：
						</td>
						<td style="font-size: 14;"colspan="7">
							<input type="checkbox" id="health" name="healthState">&nbsp;未查&nbsp;
							<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
							<input type="checkbox" id="health" name="healthState">&nbsp;异常&nbsp;（
							<input class="easyui-textbox"  id="patientName" />&nbsp;）
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							腹：
						</td>
						<td style="font-size: 14;" colspan="7">
							<table id="paitentself" class="honry-table" style="width: 90%;"  cellpadding="0" cellspacing="0"  >
								<tr>
									<td style="font-size: 14;"colspan="1">
										脊柱：
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;畸形
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										四肢：
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;异常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;畸形&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;关节红肿&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;关节强直&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;肌肉压痛&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;肌肉萎缩
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										下肢静脉曲张：
										<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（部位及特征：
										<input class="easyui-textbox"  id="patientName" />&nbsp;）
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										其它异常：
									<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（部位：
										<input class="easyui-textbox"  id="patientName" />&nbsp;）
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14;">
							神经系统：
						</td>
						<td style="font-size: 14;" colspan="7">
							<table id="paitentself" class="honry-table" style="width: 90%;"  cellpadding="0" cellspacing="0"  >
								<tr>
									<td style="font-size: 14;"colspan="1">
										肌张力：(
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;↑&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;↓&nbsp;)
										肌力：（
										<input class="easyui-textbox"  id="patientName" />&nbsp;）
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										肢体瘫痪：
										<input type="checkbox" id="health" name="healthState">&nbsp;无&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;有&nbsp;（
										<input type="checkbox" id="health" name="healthState">&nbsp;上&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;下&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;左&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;右&nbsp;）
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										膝键反射：&nbsp;左&nbsp;（
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;↓&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;↑&nbsp;）&nbsp;&nbsp;右&nbsp;（
										<input type="checkbox" id="health" name="healthState">&nbsp;正常&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;↓&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;↑&nbsp;）
									</td>
								</tr>
								<tr>
									<td style="font-size: 14;"colspan="1">
										Babinski征：
									<input type="checkbox" id="health" name="healthState">&nbsp;阴性&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;阳性（
										<input type="checkbox" id="health" name="healthState">&nbsp;左&nbsp;
										<input type="checkbox" id="health" name="healthState">&nbsp;右&nbsp;）
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
			<div style="text-align: center;width: 100%;margin-top: 20px" >
					<font style="font-size: 28px;">初  步  诊  断</font>
				</div>
				<table id="paitentInfo" class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;width: 95%;">
					<tr>
						<td colspan="1">
							<input class="easyui-textbox"  id="patientTalk" style="width: 100%" value="${emrFirst.name}"  data-options="multiline:'true',height:'100px'"/>
						</td>
					</tr>
				</table>
		</div>
		<script type="text/javascript">
	$(function(){
		$('#patientNation').combobox({
			valueField:'encode',
			textField:'name',
			editable:true,
			url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',
			queryParams:{type:'nationality'} 
		})
	});
	function queryinpa(){
		var node = $('#tDt').tree('getSelected');
		if(node==null){
		}else{
			if(node.attributes.inpatientNo==""||node.attributes.inpatientNo==null){
			}else{
				$.ajax({
					url: "<%=basePath%>inpatient/docAdvManage/queryInfo.action",
					data:{inpatientNo:node.attributes.inpatientNo},
					type:'post',
					success: function(data) {
						$('#patientName').textbox('setValue',data.patientName)//姓名
						$('#patientSex').textbox('setValue',data.reportSexName)//性别
						$('#patientbirthday').val(data.inDate)//入院时间
						$('#patientAge').textbox('setValue',data.reportAge+data.reportAgeunit)//年龄
						$('#patientNation').combobox('setValue',data.nationCode)//民族
						$('#patientTalk').textbox('setValue',"")//病史叙述者
						$('#patientTel').textbox('setValue',data.linkmanTel)//联系电话
						$('#patientAdd').textbox('setValue',data.linkmanAddress)//地址
						$('#patientZhusu').textbox('setValue',"")//主诉
						$('#patientBingshi').textbox('setValue',"")//现病史
						//平素健康状况：
						if($('#health').val()==0){
							$('#health1').prop("checked","checked");
						}else if($('#health').val()==1){
							$('#health2').prop("checked","checked");
						}
						//过敏史或不良药物反应史
						if(data.anaphyFlag==1){
							$('#anaphyFlag1').prop("checked","checked");
						}else{
							$('#anaphyFlag2').prop("checked","checked");
						}
						$('#chuanranbing').textbox('setValue',"")//传染病史
						$('#yufang').textbox('setValue',"")//预防接种史
						$('#guominyuan').textbox('setValue',"")//过敏原
						$('#linchuang').textbox('setValue',"")//临床表现
					}
				});
			}
		}
	}
</script>
	</body>
</html>