<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-layout" style="width: 100%; height: 100%;">
		<div id="p" data-options="region:'west'" title="住院证明"
			style="width: 20%; padding: 10px">
				
			<ul id="tDt"></ul>
			<div id="tDtmm" class="easyui-menu" style="width:100px;">
					<div onclick="edit()" data-options="iconCls:'icon-edit'" id="editDiv">审批</div>
			</div>
		</div>
		<div id="oper"></div>
		<div data-options="region:'center'">
			<div id="divLayout" class="easyui-layout" fit=true>
				<div
					data-options="region:'center',split:false,title:'患者信息列表',iconCls:'icon-book'"
					style="padding: 5px;">
					<form id="editForm">
						<fieldset style="padding: 1%">
							<div class="easyui-panel"
								data-options="title:'信息查询',iconCls:'icon-search'">
								<table id="list" cellspacing="0" cellpadding="0" border="0">
									<tr>
										<td style="padding: 5px 15px;">
											卡号查询：
										</td>
										<td>
											<input type="text" ID="clinicCode" name="clinicCode.id"
												onkeydown="KeyDown()" />
										</td>
										<td style="padding: 5px 15px;">
											开始时间：
										</td>
										<td>
											<input class="easyui-DateBox" ID="Stime" name="Stime"
												onkeydown="KeyDown()" />
										</td>
										<td style="padding: 5px 15px;">
											结束时间：
										</td>
										<td>
											<input class="easyui-DateBox" ID="Etime" name="Etime"
												onkeydown="KeyDown()" />
										</td>
										<td>
											&nbsp;&nbsp;
											<shiro:hasPermission name="SSSQBGD:function:query">
											<a href="javascript:void(0)" onclick="searchFrom()"
												class="easyui-linkbutton" iconCls="icon-search">查询</a>
											</shiro:hasPermission>
										</td>
									</tr>
								</table>
							</div>
						</fieldset>
						<fieldset style="padding: 1%">
						<table class="honry-table" cellpadding="1" cellspacing="1"
							border="1px solid black">
							<tr>
								<th>
									手术申请单
								</th>
							</tr>
						</table>
						</fieldset>
						<fieldset style="padding: 1%">
							<table class="honry-table" cellpadding="0" cellspacing="0"
								border="0" style="width: 100%">
								<tr>
									<td class="honry-lable">
										姓名：
									</td>
									<td>
										<input id="name" name="name" class="easyui-textbox" data-options="required:true" readonly="readonly">
									</td>
									<td class="honry-lable">
										性别：
									</td>
									<td>
										<input id="CodeSex" name="sex" class="easyui-textbox" data-options="required:true" readonly="readonly">
									</td>
									<td class="honry-lable">
										年龄：
									</td>
									<td>
										<input id="age" name="age" class="easyui-numberbox" data-options="required:true" readonly="readonly">
									</td>
								</tr>
								<tr>
									<td class="honry-lable">
										地址：
									</td>
									<td>
										<input id="address" name="address" class="easyui-textbox" data-options="required:true" readonly="readonly">
									</td>
									<td class="honry-lable">
										职业：
									</td>
									<td>
										<input id="profession" name="profession" data-options="required:true" readonly="readonly"
											class="easyui-textbox">
									</td>
									<td class="honry-lable">
										联系电话：
									</td>
									<td>
										<input id="telephone" name="telephone" data-options="required:true" readonly="readonly"
											class="easyui-numberbox" >
									</td>
								</tr>
								<tr>

									<td class="honry-lable">
										出生年月：
									</td>
									<td>
										<input id="birthday" name="birthday" class="easyui-datebox" data-options="required:true" readonly="readonly">
									</td>
									<td class="honry-lable">
										病房号：
									</td>
									<td>
										<input id="wardNo" name="wardNo" class="easyui-numberbox" data-options="required:true" >
									</td>
									<td class="honry-lable">
										病床号：
									</td>
									<td>
										<input id="bedNo" name="bedNo" class="easyui-numberbox" data-options="required:true"  >
									</td>
								</tr>
								<tr>

									<td class="honry-lable">
										预交金：
									</td>
									<td>
										<input id="prepayCost" name="prepayCost"   data-options="required:true" 
											class="easyui-textbox">
									</td>
									<td class="honry-lable">
										住院科室：
									</td>
									<td>
										<input id="inDept" name="inDept.id" class="easyui-combobox" data-options="required:true"  >
									</td>
									<td class="honry-lable">
										患者血型：
									</td>
									<td>
										<input id="bloodCode" name="bloodCode" class="easyui-textbox"  data-options="required:true" >
									</td>
								</tr>
								<tr>

									<td class="honry-lable">
										病例号：
									</td>
									<td>
										<input id="patientNo" name="patientNo"  class="easyui-textbox" data-options="required:true" readonly="readonly">
									</td>
							</table>
						</fieldset>
						<fieldset>
							<table class="honry-table" cellpadding="1" cellspacing="1"
								border="1px solid black">
								<tr>
									<td class="honry-lable">
										手术台类型：
									</td>
									<td>
										<input id="consoleTypeFlgHidden" type="hidden"
											name="consoleType" value="${consoleType }" />
										<input type="radio" id="pt"
											onclick="javascript:onclickBoxlx()" name="consoletype" />
										普通
										<input type="radio" id="jt"
											onclick="javascript:onclickBoxlx()" name="consoletype" />
										加台
										<input type="radio" id="dt"
											onclick="javascript:onclickBoxlx()" name="consoletype"  />
										点台
										<input type="radio" id="jjt"
											onclick="javascript:onclickBoxlx()" name="consoletype" />
										加急台
									</td>
									<td class="honry-lable">
										麻醉方式：
									</td>
									<td>
										<input id="aneWay" name="aneWay">
									</td>

								</tr>
								<tr>
									<td class="honry-lable">
										是否麻醉：
									</td>
									<td>
										<input id="isaneFlgHidden" type="hidden" name="isane"
											value="${isane }" />
										<input type="radio" id="yes"
											onclick="javascript:onclickBoxmazui()" name="isane"/>
										是
										<input type="radio" id="no"
											onclick="javascript:onclickBoxmazui()" name="isane" />
										否
									</td>
									<td class="honry-lable">
										是否有菌：
									</td>
									<td>
										<input id="aneWayFlgHidden" type="hidden" name="isgerm"
											value="${isgerm }" />
										<input type="radio" id="Wayy"
											onclick="javascript:onclickBoxryaneWay()" name="isgerm"/>
										是
										<input type="radio" id="Wayn"
											onclick="javascript:onclickBoxryaneWay()" name="isgerm"/>
										否
									</td>

								</tr>
								<tr>

									<td class="honry-lable">
										麻醉类型：
									</td>
									<td>
										<input id="aneWayHidden" type="hidden" name="anesType"
											value="${anesType }" />
										<input type="radio" id="jm"
											onclick="javascript:onclickBoxmzlx()"name="anesType" />
										局麻
										<input type="radio" id="qm"
											onclick="javascript:onclickBoxmzlx()" name="anesType"/>
										全麻
									</td>
									<td class="honry-lable">
										手术分类：
									</td>
									<td>
										<input id="opType" type="hidden" name="opType" />
										<input type="radio" id="oppt"
											onclick="javascript:onclickBoxopType()"  />
										普通
										<input type="radio" id="opjz"
											onclick="javascript:onclickBoxopType()" />
										急诊
										<input type="radio" id="opgr"
											onclick="javascript:onclickBoxopType()" />
										感染
									</td>
								</tr>
								<tr>

									<td class="honry-lable">
										状态：
									</td>
									<td>
										<input id="statusFlgHidden" type="hidden" name="status"
											value="${status }" />
										<input type="radio" id="sssq"
											onclick="javascript:onclickBoxstatus()"name="status" />
										手术申请
										<input type="radio" id="sssp"
											onclick="javascript:onclickBoxstatus()"name="status"  />
										手术审批
										<input type="radio" id="ssap"
											onclick="javascript:onclickBoxstatus()" name="status" />
										手术安排
										<input type="radio" id="sswc"
											onclick="javascript:onclickBoxstatus()" name="status" />
										手术完成
										<input type="radio" id="qxssdj"
											onclick="javascript:onclickBoxstatus()"name="status"  />
										取消手术登记
									</td>
									<td class="honry-lable">
										是否已做：
									</td>
									<td>
										<input id="isfinishedHidden" type="hidden" name="isfinished"
											value="${isfinished }" />
										<input type="radio" id="wzss"
											onclick="javascript:onclickBoxfinished()" name="isfinished"/>
										未做手术
										<input type="radio" id="jzss"
											onclick="javascript:onclickBoxfinished()"name="isfinished"/>
										已做手术
									</td>
								</tr>
								<tr>

									<td class="honry-lable">
										幕上 、 幕下：
									</td>
									<td>
										<input id="screenupFlgHidden" type="hidden" name="screenup"
											value="${screenup }" />
										<input type="radio" id="up"
											onclick="javascript:onclickBoxup()"name="screenup"/>
										幕上
										<input type="radio" id="donw"
											onclick="javascript:onclickBoxup()"name="screenup" />
										幕下
									</td>
									<td class="honry-lable">
										是否允许医生查看安排结果：
									</td>
									<td>
										<input id="isagreelookHidden" type="hidden" name="isagreelook"
											value="${isagreelook }" />
										<input type="radio" id="yunxu"
											onclick="javascript:onclickBoxyunxu()"name="isagreelook" />
										允许
										<input type="radio" id="byunxu"
											onclick="javascript:onclickBoxyunxu()" name="isagreelook"/>
										不允许
									</td>
								</tr>
								<tr>

									<td class="honry-lable">
										是否加急：
									</td>
									<td>
										<input id="isurgentHidden" type="hidden" name="isurgent"
											value="${isurgent }" />
										<input type="radio" id="isyes"
											onclick="javascript:onclickBoxisurgent()" name="isurgent"/>
										是
										<input type="radio" id="isno"
											onclick="javascript:onclickBoxisurgent()"name="isurgent" />
										否
									</td>
									<td class="honry-lable">
										是否已计费：
									</td>
									<td>
										<input id="ischangeHidden" type="hidden" name="ischange"
											value="${ischange }" />
										<input type="radio" id="weisf"
											onclick="javascript:onclickBoxischange()"name="ischange" />
										未收费
										<input type="radio" id="yijf"
											onclick="javascript:onclickBoxischange()"name="ischange" />
										已计费
									</td>
								</tr>
								<tr>

									<td class="honry-lable">
										是否重症：
									</td>
									<td>
										<input id="isheavyHidden" type="hidden" name="isheavy"
											value="${isheavy }" />
										<input type="radio" id="heavyy"
											onclick="javascript:onclickBoxheavyy()"name="isheavy" />
										是
										<input type="radio" id="heavyn"
											onclick="javascript:onclickBoxheavyn()"name="isheavy" />
										否
									</td>
									<td class="honry-lable">
										是否特殊手术：
									</td>
									<td>
										<input id="isspecialHidden" type="hidden" name="isspecial"
											value="${isspecial }" />
										<input type="radio" id="specials"
											onclick="javascript:onclickBoxspecial()" name="isspecial"/>
										是
										<input type="radio" id="specialn"
											onclick="javascript:onclickBoxspecial()"name="isspecial" />
										否
									</td>
								</tr>
								<tr>

									<td class="honry-lable">
										是否合并：
									</td>
									<td>
										<input id="isuniteFlgHidden" type="hidden" name="isunite"
											value="${isunite }" />
										<input type="radio" id="unites"
											onclick="javascript:onclickBoxunite()" name="isunite"/>
										是
										<input type="radio" id="uniten"
											onclick="javascript:onclickBoxunite()" name="isunite"/>
										否
									</td>
									<td class="honry-lable">
										是否需要随台护士：
									</td>
									<td>
										<input id="isneedaccoHidden" type="hidden" name="isneedacco"
											value="${isneedacco }" />
										<input type="radio" id="accoy"
											onclick="javascript:onclickBoxacco()" name="isneedacco"/>
										是
										<input type="radio" id="accon"
											onclick="javascript:onclickBoxacco()" name="isneedacco"/>
										否
									</td>
								</tr>
								<tr>

									<td class="honry-lable">
										是否需要巡回护士：
									</td>
									<td>
										<input id="isneedprepHidden" type="hidden" name="isneedprep"
											value="${isneedprep }" />
										<input type="radio" id="prepy"
											onclick="javascript:onclickBoxprep()" name="isneedprep"/>
										是
										<input type="radio" id="prepn"
											onclick="javascript:onclickBoxprep()"name="isneedprep" />
										否
									</td>
									<td class="honry-lable">
										是否需要病理检查：
									</td>
									<td>
										<input id="pathologyHidden" type="hidden"
											name="isneedpathology" value="${isneedpathology }" />
										<input type="radio" id="pathology"
											onclick="javascript:onclickBoxpatholog()" name="isneedpathology"  />
										是
										<input type="radio" id="pathologyn"
											onclick="javascript:onclickBoxpatholog()" name="isneedpathology" />
										否
									</td>
								</tr>
								<tr>

									<td class="honry-lable">
										多重耐药：
									</td>
									<td>
										<input id="dcnyFlgHidden" type="hidden" name="dcny"
											value="${dcny }" />
										<input type="radio" id="dcnyy"
											onclick="javascript:onclickBoxdcny()" name="dcny"/>
										是
										<input type="radio" id="dcnyn"
											onclick="javascript:onclickBoxdcny()" name="dcny"/>
										否
									</td>
									<td class="honry-lable">
										手术体位：
									</td>
									<td>
										<input id="opertionpositionHidden" type="hidden"
											name="opertionposition" value="${opertionposition }" />
										<input type="radio" id="cewei"
											onclick="javascript:onclickBoxwei()" name="opertionposition" />
										侧位
										<input type="radio" id="wowei"
											onclick="javascript:onclickBoxwei()"name="opertionposition"  />
										卧位
									</td>
								</tr>
								<tr>

									<td class="honry-lable">
										是否二次手术：
									</td>
									<td>
										<input id="issecondopertionHidden" type="hidden"
											name="issecondopertion" value="${issecondopertion }" />
										<input type="radio" id="secondy"
											onclick="javascript:onclickBoxsecond()"name="issecondopertion" />
										是
										<input type="radio" id="secondn"
											onclick="javascript:onclickBoxsecond()" name="issecondopertion"/>
										否
									</td>
									<td class="honry-lable">
										是否同意使用自费项目：
									</td>
									<td>
										<input id="isownexpenseHidden" type="hidden"
											name="isownexpense" value="${isownexpense }" />
										<input type="radio" id="expensey"
											onclick="javascript:onclickBoxexpense()" name="isownexpense" />
										是
										<input type="radio" id="expensen"
											onclick="javascript:onclickBoxexpense()" name="isownexpense" />
										否
									</td>
								</tr>

								<tr>

									<td class="honry-lable">
										是否请上级医院会诊：
									</td>
									<td>
										<input id="isgroupconsulHidden" type="hidden"
											name="isgroupconsultation"  />
										<input type="radio" id="groupy"
											onclick="javascript:onclickBoxgroup()" name="isgroupconsultation" />
										是
										<input type="radio" id="groupn"
											onclick="javascript:onclickBoxgroup()"name="isgroupconsultation"  />
										否
									</td>
									<td class="honry-lable">
										来源：
									</td>
									<td>
										<input id="pasourceHidden" type="hidden" name="pasource"/>
										<input type="radio" id="menzhen"
											onclick="javascript:onclickBoxpasource()" name="pasource"/>
										门诊
										<input type="radio" id="zhuyuan"
											onclick="javascript:onclickBoxpasource()" name="pasource"/>
										住院
									</td>
								</tr>
								<tr>

									<td class="honry-lable">
										麻醉注意事项：
									</td>
									<td>
										<input id="zhuyishixiang" name="aneNote"
											class="easyui-textbox"  >
									</td>
									<td class="honry-lable">
										助手数：
									</td>
									<td>
										<input id="zhushoushu" name="helperNum"
											class="easyui-numberbox"  >
									</td>
								</tr>



								<tr>

									<td class="honry-lable">
										麻醉医生编码：
									</td>
									<td>
										<input id="mzysbm" name="aneDoctor"  >
									</td>

									<td class="honry-lable">
										助手1：
									</td>
									<td>
										<input id="zhushou1" name="opAssist1" >
									</td>
								</tr>
								<tr>
									<td class="honry-lable">
										指导医生编码：
									</td>
									<td>
										<input id="zdysbm" name="guiDoctor"  >
									</td>
									<td class="honry-lable">
										助手2：
									</td>
									<td>
										<input id="zhushou2" name="opAssist2" class="easyui-combobox">
									</td>
								</tr>
								<tr>
									<td class="honry-lable">
										手术医生编码：
									</td>
									<td>
										<input id="ssysbm" name="opDoctor" data-options="required:true" >
									</td>

									<td class="honry-lable">
										助手3：
									</td>
									<td>
										<input id="zhushou3" name="opAssist3" class="easyui-combobox">
									</td>
								</tr>

								<tr>
									<td class="honry-lable">
										申请医生编码：
									</td>
									<td>
										<input id="sqysbm" name="applyDoctor"  data-options="required:true" >
									</td>

									<td class="honry-lable">
										临时助手1：
									</td>
									<td>
										<input id="lszs1" name="opTempassist1" >
									</td>

								</tr>

								<tr>
									<td class="honry-lable">
										手术医生科室编码：
									</td>
									<td>
										<input id="keshi" name="opDoctordept"  data-options="required:true" >
									</td>
									<td class="honry-lable">
										临时助手2：
									</td>
									<td>
										<input id="lszs2" name="opTempassist2" class="easyui-combobox">
									</td>
								</tr>


								<tr>
									<td class="honry-lable">
										手术诊断1：
									</td>
									<td>
										<input id="shoushuzd1" name="diagnose1"
											class="easyui-combobox" data-options="required:true">
									</td>
									<td class="honry-lable">
										拟手术名称1：
									</td>
									<td>
										<input id="nssmc1" name="opName1"  data-options="required:true">
									</td>

								</tr>
								<tr>
									<td class="honry-lable">
										手术诊断2：
									</td>
									<td>
										<input id="shoushuzd2" name="diagnose2"
											class="easyui-combobox">
									</td>
									<td class="honry-lable">
										拟手术名称2：
									</td>
									<td>
										<input id="nssmc2" name="opName2" class="easyui-combobox">
									</td>
								</tr>
								<tr>
									<td class="honry-lable">
										手术诊断3：
									</td>
									<td>
										<input id="shoushuzd3" name="diagnose3"
											class="easyui-combobox">
									</td>
									<td class="honry-lable">
										拟手术名称3：
									</td>
									<td>
										<input id="nssmc3" name="opName3" class="easyui-combobox">
									</td>
								</tr>
								<tr>
									<td class="honry-lable">
										切口类型：
									</td>
									<td>
										<input id="inciType" name="inciType"  >
									</td>
									<td class="honry-lable">
										感染类型：
									</td>
									<td>
										<input id="infectType" name="infectType" >
									</td>

								</tr>
								<tr>
									<td class="honry-lable">
										手术规模：
									</td>
									<td>
										<input id="degree" name="degree" class="easyui-textbox" data-options="required:true" >
									</td>
									<td class="honry-lable">
										手术室：
									</td>
									<td>
										<input id="opRoom" name="opRoom" class="easyui-textbox"  data-options="required:true" >
									</td>
								</tr>
								<tr>
									<td class="honry-lable">
										执行科室：
									</td>
									<td>
										<input id="execDept" name="execDept"  data-options="required:true" >
									</td>
								</tr>
								
							</table>
						</fieldset>
						<fieldset>
							<table class="honry-table" cellpadding="1" cellspacing="1"
								border="1px solid black">

								<tr>
									<td class="honry-lable">
										接患者时间：
									</td>
									<td>
										<input id="receptDate" class="easyui-DateBox"
											name="receptDate"  data-options="required:true"  / >
									</td>
									<td class="honry-lable">
										预约时间：
									</td>
									<td>
										<input id="preDate" class="easyui-DateBox" name="preDate" data-options="required:true" / >
									</td>

								</tr>
								<tr>
									<td class="honry-lable">
										合并后手术编号：
									</td>
									<td>
										<input id="uniteOpid" class="easyui-textbox" name="uniteOpid"  / >
									</td>

									<td class="honry-lable">
										是否自体血回输：
									</td>
									<td>
										<input id="isautobloodHidden" type="hidden" name="isautoblood"
											value="${isautoblood }" />
										<input type="radio" id="bloody"
											onclick="javascript:onclickBoxblood()" />
										是
										<input type="radio" id="bloodn"
											onclick="javascript:onclickBoxblood()" />
										否
									</td>
								</tr>
								<tr>
									<td class="honry-lable">
										临床表现：
									</td>
									<td>
										<input id="clinical" class="easyui-textbox" name="clinical"  data-options="required:true"/ >
									</td>
									<td class="honry-lable">
										血量：
									</td>
									<td>
										<input id="bloodNum" class="easyui-textbox" name="bloodNum"  / >
									</td>

								</tr>
								<tr>
									<td class="honry-lable">
										术前诊断：
									</td>
									<td>
										<input id="reportSex" class="easyui-textbox" name="reportSex" data-options="required:true" / >
									</td>
									<td class="honry-lable">
										用血单位：
									</td>
									<td>
										<input id="bloodUnit" class="easyui-textbox" name="bloodUnit"  / >
									</td>

								</tr>
								<tr>
									<td class="honry-lable">
										手术注意事项：
									</td>
									<td>
										<input id="opsNote" class="easyui-textbox" name="opsNote"  / >
									</td>
									<td class="honry-lable">
										手术禁忌症：
									</td>
									<td>
										<input id="contraindication" class="easyui-textbox"
											name="contraindication"  / >
									</td>

								</tr>
								<tr>
									<td class="honry-lable">
										术中术后应对措施：
									</td>
									<td>
										<input id="measures" class="easyui-textbox" name="measures"  / >
									</td>

									<td class="honry-lable">
										手术适应症：
									</td>
									<td>
										<input id="indication" class="easyui-textbox"
											name="indication"  / >
									</td>
								</tr>
								<tr>
									<td class="honry-lable">
										预定用时：
									</td>
									<td>
										<input id="duration" class="easyui-textbox" name="duration" data-options="required:true"/  >
									</td>

									<td class="honry-lable">
										目前病人情况：
									</td>
									<td>
										<input id="stitution" class="easyui-textbox" name="stitution" data-options="required:true" / >
									</td>
								</tr>
								<tr>
									<td class="honry-lable">
										申请备注：
									</td>
									<td>
										<input id="applyRemark" class="easyui-textbox"
											name="applyRemark"  / >
									</td>

									<td class="honry-lable">
										术前准备情况：
									</td>
									<td>
										<input id="preparation" class="easyui-textbox"
											name="preparation"  / >
									</td>
								</tr>
								<tr>
									<td class="honry-lable">
										洗手护士数：
									</td>
									<td>
										<input id="washNurse" class="easyui-numberbox"
											name="washNurse"  / >
									</td>

									<td class="honry-lable">
										可能的并发症：
									</td>
									<td>
										<input id="complication" class="easyui-textbox"
											name="complication"  / >
									</td>
								</tr>
								<tr>
									<td class="honry-lable">
										随台护士数：
									</td>
									<td>
										<input id="accoNurse" class="easyui-numberbox"
											name="accoNurse"  / >
									</td>

									<td class="honry-lable">
										术前讨论情况：
									</td>
									<td>
										<input id="discussion" class="easyui-textbox"
											name="discussion"  data-options="required:true" / >
									</td>
								</tr>
								<tr>
									<td class="honry-lable">
										巡回护士数：
									</td>
									<td>
										<input id="prepNurse" class="easyui-numberbox"
											name="prepNurse"  / >
									</td>
									<td class="honry-lable">
										合并疾病：
									</td>
									<td>
										<input id="uniteDisease" class="easyui-textbox"
											name="uniteDisease"  / >
									</td>

								</tr>
								<tr>
									<td class="honry-lable">
										家属关系：
									</td>
									<td>
										<input id="relaCode" class="easyui-textbox" name="relaCode"/ >
									</td>
									<td class="honry-lable">
										家属意见：
									</td>
									<td>
										<input id="folkComment" class="easyui-textbox"
											name="folkComment"/ >
									</td>

								</tr>
								<tr>
									<td class="honry-lable">
										患者家属、单位签字：
									</td>
									<td>
										<input id="folk" class="easyui-textbox"
											name="folk" data-options="required:true" / >
									</td>
									<td class="honry-lable">
										手术医生签字：
									</td>
									<td>
										<input id="reportBloodqty" class="easyui-textbox"
											name="reportBloodqty"  data-options="required:true"/ >
									</td>

								</tr>
							</table>
						</fieldset>
						<fieldset>
							<table class="honry-table" cellpadding="1" cellspacing="1"
								border="1px solid black">
								<tr>
									<td>
									<shiro:hasPermission name="SSSQBGD:function:add">
										<a href="javascript:submit();void(0)"
											class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
									</shiro:hasPermission>

									</td>
								</tr>
							</table>
						</fieldset>
					</form>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	//单选按钮赋值手术台类型
	function onclickBoxlx(id) {
	
		if ($('#pt').is(':checked')) {
			$('#consoleTypeFlgHidden').val(1);
		}
		if ($('#jt').is(':checked')) {
			$('#consoleTypeFlgHidden').val(2);
		}
		if ($('#dt').is(':checked')) {
			$('#consoleTypeFlgHidden').val(3);
		}
		if ($('#jjt').is(':checked')) {
			$('#consoleTypeFlgHidden').val(4);
		}
	}
	//是否自体血回输
	function onclickBoxblood(id) {
		if ($('#bloody').is(':checked')) {
			$('#isautobloodHidden').val(1);
		}
		if ($('#bloodn').is(':checked')) {
			$('#isautobloodHidden').val(0);
		}
	}
	//是否有菌
	function onclickBoxryaneWay(id) {
		if ($('#Wayy').is(':checked')) {
			$('#aneWayFlgHidden').val(1);
		}
		if ($('#Wayn').is(':checked')) {
			$('#aneWayFlgHidden').val(0);
		}
	}
	//单选按钮赋值是否麻醉
	function onclickBoxmazui(id) {
		if ($('#yes').is(':checked')) {
			$('#isaneFlgHidden').val(1);
		}
		if ($('#no').is(':checked')) {
			$('#isaneFlgHidden').val(0);
		}
	}
	//单选按钮赋值麻醉类型
	function onclickBoxmzlx(id) {
		if ($('#jm').is(':checked')) {
			$('#aneWayHidden').val(1);
		}
		if ($('#qm').is(':checked')) {
			$('#aneWayHidden').val(2);
		}
	}
	//单选按钮赋值手术分类:
	function onclickBoxopType(id) {
		if ($('#oppt').is(':checked')) {
			$('#opType').val(1);
		}
		if ($('#opjz').is(':checked')) {
			$('#opType').val(2);
		}
		if ($('#opgr').is(':checked')) {
			$('#opType').val(3);
		}
	}
	//单选按钮赋值状态:
	function onclickBoxstatus(id) {
		if ($('#sssq').is(':checked')) {
			$('#statusFlgHidden').val(1);
		}
		if ($('#sssp').is(':checked')) {
			$('#statusFlgHidden').val(2);
		}
		if ($('#ssap').is(':checked')) {
			$('#statusFlgHidden').val(3);
		}
		if ($('#sswc').is(':checked')) {
			$('#statusFlgHidden').val(4);
		}
		if ($('#qxssdj').is(':checked')) {
			$('#statusFlgHidden').val(5);
		}
	}
	//单选按钮是否已做:
	function onclickBoxfinished(id) {
		if ($('#wzss').is(':checked')) {
			$('#isfinishedHidden').val(0);
		}
		if ($('#jzss').is(':checked')) {
			$('#isfinishedHidden').val(1);
		}
	}
	//单选按钮幕上 幕下:
	function onclickBoxup(id) {
		if ($('#up').is(':checked')) {
			$('#screenupFlgHidden').val(1);
		}
		if ($('#donw').is(':checked')) {
			$('#screenupFlgHidden').val(2);
		}
	}
	//单选按钮是否允许医生查看安排结果:
	function onclickBoxyunxu(id) {
		if ($('#yunxu').is(':checked')) {
			$('#isagreelookHidden').val(1);
		}
		if ($('#byunxu').is(':checked')) {
			$('#isagreelookHidden').val(2);
		}
	}
	//单选按钮是否加急:
	function onclickBoxisurgent(id) {
		if ($('#isyes').is(':checked')) {
			$('#isurgentHidden').val(1);
		}
		if ($('#isno').is(':checked')) {
			$('#isurgentHidden').val(0);
		}
	}
	//单选按钮是否已计费:
	function onclickBoxischange(id) {
		if ($('#weisf').is(':checked')) {
			$('#ischangeHidden').val(1);
		}
		if ($('#yijf').is(':checked')) {
			$('#ischangeHidden').val(0);
		}
	}
	//单选按钮是否重症:
	function onclickBoxheavyy(id) {
		if ($('#heavyy').is(':checked')) {
			$('#isheavyHidden').val(1);
		}
		if ($('#heavyn').is(':checked')) {
			$('#isheavyHidden').val(0);
		}
	}
	//单选按钮是否特殊手术:
	function onclickBoxspecial(id) {
		if ($('#specials').is(':checked')) {
			$('#isspecialHidden').val(1);
		}
		if ($('#specialn').is(':checked')) {
			$('#isspecialHidden').val(0);
		}
	}
	//单选按钮是否合并:
	function onclickBoxunite(id) {
		if ($('#unites').is(':checked')) {
			$('#isuniteFlgHidden').val(1);
		}
		if ($('#uniten').is(':checked')) {
			$('#isuniteFlgHidden').val(0);
		}
	}
	//单选按钮是否需要随台护士:
	function onclickBoxacco(id) {
		if ($('#accoy').is(':checked')) {
			$('#isneedaccoHidden').val(1);
		}
		if ($('#accon').is(':checked')) {
			$('#isneedaccoHidden').val(0);
		}
	}
	//单选按钮是否需要巡回护士:
	function onclickBoxprep(id) {
		if ($('#prepy').is(':checked')) {
			$('#isneedprepHidden').val(1);
		}
		if ($('#prepn').is(':checked')) {
			$('#isneedprepHidden').val(0);
		}
	}
	//单选按钮是否需要病理检查:
	function onclickBoxpatholog(id) {
		if ($('#pathology').is(':checked')) {
			$('#pathologyHidden').val(1);
		}
		if ($('#pathologyn').is(':checked')) {
			$('#pathologyHidden').val(0);
		}
	}
	//单选按钮多重耐药:
	function onclickBoxdcny(id) {
		if ($('#dcnyy').is(':checked')) {
			$('#dcnyFlgHidden').val(1);
		}
		if ($('#dcnyn').is(':checked')) {
			$('#dcnyFlgHidden').val(0);
		}
	}
	//单选按钮手术体位:
	function onclickBoxwei(id) {
		if ($('#cewei').is(':checked')) {
			$('#opertionpositionHidden').val(1);
		}
		if ($('#wowei').is(':checked')) {
			$('#opertionpositionHidden').val(0);
		}
	}
	//单选按钮是否二次手术:
	function onclickBoxsecond(id) {
		if ($('#secondy').is(':checked')) {
			$('#issecondopertionHidden').val(1);
		}
		if ($('#secondn').is(':checked')) {
			$('#issecondopertionHidden').val(0);
		}
	}
	//单选按钮是否同意使用自费项目:
	function onclickBoxexpense(id) {
		if ($('#expensey').is(':checked')) {
			$('#isownexpenseHidden').val(1);
		}
		if ($('#expensen').is(':checked')) {
			$('#isownexpenseHidden').val(0);
		}
	}
	//单选按钮是否请上级医院会诊:
	function onclickBoxgroup(id) {
		if ($('#groupy').is(':checked')) {
			$('#isgroupconsulHidden').val(1);
		}
		if ($('#groupn').is(':checked')) {
			$('#isgroupconsulHidden').val(0);
		}
	}
	//单选按钮来源:
	function onclickBoxpasource(id) {
		if ($('#menzhen').is(':checked')) {
			$('#pasourceHidden').val(1);
		}
		if ($('#zhuyuan').is(':checked')) {
			$('#pasourceHidden').val(0);
		}
	}
	//麻醉医生编码
	$('#mzysbm').combobox({
		url : 'baseinfo/employee/employeeCombobox.action',
		valueField : 'id',
		textField : 'name',
		multiple : false,
		editable : false
	});
	//指导医生编码
	$('#zdysbm').combobox({
		url : 'baseinfo/employee/employeeCombobox.action',
		valueField : 'id',
		textField : 'name',
		multiple : false,
		editable : false
	});
	//手术医生编码
	$('#ssysbm').combobox({
		url : 'baseinfo/employee/employeeCombobox.action',
		valueField : 'id',
		textField : 'name',
		multiple : false,
		editable : false
	});
	//申请医生编码
	$('#sqysbm').combobox({
		url : 'baseinfo/employee/employeeCombobox.action',
		valueField : 'id',
		textField : 'name',
		multiple : false,
		editable : false
	});
	//助手1
	$('#zhushou1').combobox({
		url : 'baseinfo/employee/employeeCombobox.action',
		valueField : 'id',
		textField : 'name',
		multiple : false,
		editable : false
	});
	//助手2
	$('#zhushou2').combobox({
		url : 'baseinfo/employee/employeeCombobox.action',
		valueField : 'id',
		textField : 'name',
		multiple : false,
		editable : false
	});
	//助手3
	$('#zhushou3').combobox({
		url : 'baseinfo/employee/employeeCombobox.action',
		valueField : 'id',
		textField : 'name',
		multiple : false,
		editable : false
	});
	//临时助手1
	$('#lszs1').combobox({
		url : 'baseinfo/employee/employeeCombobox.action',
		valueField : 'id',
		textField : 'name',
		multiple : false,
		editable : false
	});
	//临时助手2
	$('#lszs2').combobox({
		url : 'baseinfo/employee/employeeCombobox.action',
		valueField : 'id',
		textField : 'name',
		multiple : false,
		editable : false
	});
	//手术诊断1
	$('#shoushuzd1').combobox(
			{
				url : 'business/icdCombobox.action',
				valueField : 'id',
				textField : 'name',
				multiple : false,
				editable : false,
				onChange : function(shoushuzd1) {
					var id1 = $('#shoushuzd1').combobox('getValue');
					var id2 = $('#shoushuzd2').combobox('getValue');
					var id3 = $('#shoushuzd3').combobox('getValue');
					if (id1 != null && id1 != "" && id2 != null && id2 != ""
							&& id3 != null && id3 != "") {
						return false;
					}
					var id = id1 + "," + id2 + "," + id3;
					if (id2 == null || id2 == "") {
						if (id != null) {
							$('#shoushuzd2').combobox('reload',
									'business/icdCombobox.action?ids=' + id);
						}
					}
					if (id3 == null || id3 == "") {
						if (id != null) {
							$('#shoushuzd3').combobox('reload',
									'business/icdCombobox.action?ids=' + id);
						}
					}
				}
			});
	//手术诊断2
	$('#shoushuzd2').combobox(
			{
				url : 'business/icdCombobox.action',
				valueField : 'id',
				textField : 'name',
				multiple : false,
				editable : false,
				onChange : function(shoushuzd1) {
					var id1 = $('#shoushuzd1').combobox('getValue');
					var id2 = $('#shoushuzd2').combobox('getValue');
					var id3 = $('#shoushuzd3').combobox('getValue');
					if (id1 != null && id1 != "" && id2 != null && id2 != ""
							&& id3 != null && id3 != "") {
						return false;
					}
					var id = id1 + "," + id2 + "," + id3;
					if (id1 == null || id1 == "") {
						if (id != null) {
							$('#shoushuzd1').combobox('reload',
									'business/icdCombobox.action?ids=' + id);
						}
					}
					if (id3 == null || id3 == "") {
						if (id != null) {
							$('#shoushuzd3').combobox('reload',
									'business/icdCombobox.action?ids=' + id);
						}
					}
				}
			});
	//手术诊断3
	$('#shoushuzd3').combobox(
			{
				url : 'business/icdCombobox.action',
				valueField : 'id',
				textField : 'name',
				multiple : false,
				editable : false,
				onChange : function(shoushuzd1) {
					var id1 = $('#shoushuzd1').combobox('getValue');
					var id2 = $('#shoushuzd2').combobox('getValue');
					var id3 = $('#shoushuzd3').combobox('getValue');
					if (id1 != null && id1 != "" && id2 != null && id2 != ""
							&& id3 != null && id3 != "") {
						return false;
					}
					var id = id1 + "," + id2 + "," + id3;
					if (id1 == null || id1 == "") {
						if (id != null) {
							$('#shoushuzd1').combobox('reload',
									'business/icdCombobox.action?ids=' + id);
						}
					}
					if (id2 == null || id2 == "") {
						if (id != null) {
							$('#shoushuzd2').combobox('reload',
									'business/icdCombobox.action?ids=' + id);
						}
					}
				}
			});

	//拟手术名称1
	$('#nssmc1').combobox(
			{
				url : 'drug/undrug/undrugCombobox.action',
				valueField : 'id',
				textField : 'undrugName',
				multiple : false,
				editable : false,
				onChange : function(nssmc1) {
					var id1 = $('#nssmc1').combobox('getValue');
					var id2 = $('#nssmc2').combobox('getValue');
					var id3 = $('#nssmc3').combobox('getValue');
					if (id1 != null && id1 != "" && id2 != null && id2 != ""
							&& id3 != null && id3 != "") {
						return false;
					}
					var id = id1 + "," + id2 + "," + id3;
					if (id2 == null || id2 == "") {
						if (id != null) {
							$('#nssmc2').combobox('reload',
									'drug/undrug/undrugCombobox.action?ids=' + id);
						}
					}
					if (id3 == null || id3 == "") {
						if (id != null) {
							$('#nssmc3').combobox('reload',
									'drug/undrug/undrugCombobox.action?ids=' + id);
						}
					}
				}
			});
	//拟手术名称2
	$('#nssmc2').combobox(
			{
				url : 'drug/undrug/undrugCombobox.action',
				valueField : 'id',
				textField : 'undrugName',
				multiple : false,
				editable : false,
				onChange : function(nssmc1) {
					var id1 = $('#nssmc1').combobox('getValue');
					var id2 = $('#nssmc2').combobox('getValue');
					var id3 = $('#nssmc3').combobox('getValue');
					if (id1 != null && id1 != "" && id2 != null && id2 != ""
							&& id3 != null && id3 != "") {
						return false;
					}
					var id = id1 + "," + id2 + "," + id3;
					if (id1 == null || id1 == "") {
						if (id != null) {
							$('#nssmc1').combobox('reload',
									'drug/undrug/undrugCombobox.action?ids=' + id);
						}
					}
					if (id3 == null || id3 == "") {
						if (id != null) {
							$('#nssmc3').combobox('reload',
									'drug/undrug/undrugCombobox.action?ids=' + id);
						}
					}
				}
			});
	//拟手术名称3
	$('#nssmc3').combobox(
			{
				url : 'drug/undrug/undrugCombobox.action',
				valueField : 'id',
				textField : 'undrugName',
				multiple : false,
				editable : false,
				onChange : function(nssmc1) {
					var id1 = $('#nssmc1').combobox('getValue');
					var id2 = $('#nssmc2').combobox('getValue');
					var id3 = $('#nssmc3').combobox('getValue');
					if (id1 != null && id1 != "" && id2 != null && id2 != ""
							&& id3 != null && id3 != "") {
						return false;
					}
					var id = id1 + "," + id2 + "," + id3;
					if (id1 == null || id1 == "") {
						if (id != null) {
							$('#nssmc1').combobox('reload',
									'drug/undrug/undrugCombobox.action?ids=' + id);
						}
					}
					if (id2 == null || id2 == "") {
						if (id != null) {
							$('#nssmc2').combobox('reload',
									'drug/undrug/undrugCombobox.action?ids=' + id);
						}
					}
				}
			});
	$('#CodeSex').combobox({
			    url:'likeSex.action?str='+"CodeSex",    
			    valueField:'id',    
			    textField:'name',
			    multiple:false
			});
	//麻醉方式
	$('#aneWay').combobox({
		url : 'likeAneway.action',
		valueField : 'id',
		textField : 'name',
		multiple : false,
		editable : false
	});
	//切口类型
	$('#inciType').combobox({
		url : 'likeIncitype.action',
		valueField : 'id',
		textField : 'name',
		multiple : false,
		editable : false
	});
	//感染类型
	$('#infectType').combobox({
		url : 'likeInfecttype.action',
		valueField : 'id',
		textField : 'name',
		multiple : false,
		editable : false
	});
	//执行科室
	$('#execDept').combobox({
		url : 'baseinfo/department/departmentCombobox.action',
		valueField : 'id',
		textField : 'deptName',
		multiple : false,
		editable : false
	});


	//挂号科室
	$('#keshi').combobox({
		url : 'baseinfo/department/departmentCombobox.action',
		valueField : 'id',
		textField : 'deptName',
		multiple : false

	});
	//住院科室
	$('#inDept').combobox({
		url : 'baseinfo/department/departmentCombobox.action',
		valueField : 'id',
		textField : 'deptName',
		multiple : false,
		editable : false

	});

	
  /**
		 * 查询
		 * @author  wj
		 * @date 2015-7-2 14:52
		 * @version 1.0
		 */
	     function searchFrom(){
	     	var clinicCode = $('#clinicCode').val();
	     	var Stime = $('#Stime').datebox('getValue');
	     	var Etime = $('#Etime').datebox('getValue');
	     		$('#tDt').tree('options').url = "operation/operationapply/ptreeproof.action?no="+clinicCode+"&Stime="+Stime+"&Etime="+Etime;
				$('#tDt').tree('reload'); 
	     }
	//提交验证
	function submit() {
		$.messager.progress({text:'保存中，请稍后...',modal:true});
			
		$('#editForm').form('submit', {
			url : 'operation/operationapply/operationapplyInfo.action',
			data : $('#editForm').serialize(),
			dataType : 'json',
			onSubmit : function() {
				$.messager.progress('close');
				if (!$('#editForm').form('validate')) {
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
			},
			success : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示信息','保存成功！');
		             //实现刷新栏目中的数据
                    window.location.href='operation/operationapply/listOperationapply.action';
		        },
				error : function(data) {
					$.messager.alert('提示信息','保存失败！');
				$('#divLayout').layout('remove', 'east');
				//实现刷新
				$("#list").datagrid("reload");

			},
			error : function(data) {
				$.messager.progress('close');
			}
		});
	}

	/**
	 * 回车键查询
	 * @author  wujiao
	 * @param title 标签名称
	 * @param title 跳转路径
	 * @date 2015-06-15
	 * @version 1.0
	 */
	function KeyDown() {
		if (event.keyCode == 13) {
			event.returnValue = false;
			event.cancel = true;
			searchFrom();
		}
	}

	function getSelected() {//获得选中节点
		var node = $('#tDt').tree('getSelected');
				if (node){
					if(node.attributes.isNO=="1"||node.attributes.isNO=="2"){
						var id = node.id;
						var type = node.attributes.isNO;
						return type+";"+id;
					}else{
						return "";
					}
				}else{
					return "";
				}
	}
	/**
	 * 在列别页面插入树
	 * @author  wj
	 * @param 
	 * @date 2015-06-03
	 * @version 1.0
	 */
	//加载患者树

	$('#tDt').tree({
		url : 'operation/operationapply/ptreeproof.action?feat=1&type=1&juri=2',
		method : 'get',
		animate : true,
		lines : true,
		formatter : function(node) {//统计节点总数
			var s = node.text;
			if (node.children) {
				s += '&nbsp;<span style=\'color:blue\'>('
						+ node.children.length + ')</span>';
			}
			return s;
		},onClick : function(node) {//点击节点
		 	var idtype = getSelected();
					var s = idtype.split(";");
					var type = s[0].split(";");
					var id = s[1].split(";");
					$('#editForm').form('reset');
			if (id != null && id != "") {
			   if(type=="2"){
			      $.ajax({
								url: 'operation/operationapply/queryOperationApplyById.action?oid='+id,
								type:'post',
								success: function(data) {
									var OperationObj = eval("("+data+")");
									var ssysbmval=OperationObj.opDoctor;
									$('#ssysbm').combobox({
										url : 'baseinfo/employee/employeeCombobox.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										value:ssysbmval
										});
									var shoushuzd1val=OperationObj.diagnose1;
									$('#shoushuzd1').combobox({
										url : 'business/icdCombobox.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										value:shoushuzd1val
										});
									var shoushuzd2val=OperationObj.diagnose2;
									$('#shoushuzd2').combobox({
										url : 'business/icdCombobox.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										value:shoushuzd2val
										});
									var shoushuzd3val=OperationObj.diagnose3;
									$('#shoushuzd3').combobox({
										url : 'business/icdCombobox.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										value:shoushuzd3val
										});
										//手术诊断1
								$('#shoushuzd1').combobox(
										{
											url : 'business/icdCombobox.action',
											valueField : 'id',
											textField : 'name',
											multiple : false,
											editable : false,
											onChange : function(shoushuzd1) {
												var id1 = $('#shoushuzd1').combobox('getValue');
												var id2 = $('#shoushuzd2').combobox('getValue');
												var id3 = $('#shoushuzd3').combobox('getValue');
												if (id1 != null && id1 != "" && id2 != null && id2 != ""
														&& id3 != null && id3 != "") {
													return false;
												}
												var id = id1 + "," + id2 + "," + id3;
												if (id2 == null || id2 == "") {
													if (id != null) {
														$('#shoushuzd2').combobox('reload',
																'business/icdCombobox.action?ids=' + id);
													}
												}
												if (id3 == null || id3 == "") {
													if (id != null) {
														$('#shoushuzd3').combobox('reload',
																'business/icdCombobox.action?ids=' + id);
													}
												}
											}
										});
								//手术诊断2
								$('#shoushuzd2').combobox(
										{
											url : 'business/icdCombobox.action',
											valueField : 'id',
											textField : 'name',
											multiple : false,
											editable : false,
											onChange : function(shoushuzd1) {
												var id1 = $('#shoushuzd1').combobox('getValue');
												var id2 = $('#shoushuzd2').combobox('getValue');
												var id3 = $('#shoushuzd3').combobox('getValue');
												if (id1 != null && id1 != "" && id2 != null && id2 != ""
														&& id3 != null && id3 != "") {
													return false;
												}
												var id = id1 + "," + id2 + "," + id3;
												if (id1 == null || id1 == "") {
													if (id != null) {
														$('#shoushuzd1').combobox('reload',
																'business/icdCombobox.action?ids=' + id);
													}
												}
												if (id3 == null || id3 == "") {
													if (id != null) {
														$('#shoushuzd3').combobox('reload',
																'business/icdCombobox.action?ids=' + id);
													}
												}
											}
										});
								//手术诊断3
								$('#shoushuzd3').combobox(
										{
											url : 'business/icdCombobox.action',
											valueField : 'id',
											textField : 'name',
											multiple : false,
											editable : false,
											onChange : function(shoushuzd1) {
												var id1 = $('#shoushuzd1').combobox('getValue');
												var id2 = $('#shoushuzd2').combobox('getValue');
												var id3 = $('#shoushuzd3').combobox('getValue');
												if (id1 != null && id1 != "" && id2 != null && id2 != ""
														&& id3 != null && id3 != "") {
													return false;
												}
												var id = id1 + "," + id2 + "," + id3;
												if (id1 == null || id1 == "") {
													if (id != null) {
														$('#shoushuzd1').combobox('reload',
																'business/icdCombobox.action?ids=' + id);
													}
												}
												if (id2 == null || id2 == "") {
													if (id != null) {
														$('#shoushuzd2').combobox('reload',
																'business/icdCombobox.action?ids=' + id);
													}
												}
											}
										});
										//拟手术名称1
									var nssmc1=OperationObj.opName1;
									 var nssmc2=OperationObj.opName2;
									 var nssmc3=OperationObj.opName3;
									$('#nssmc1').combobox(
											{
												url : 'baseinfo/undrug/undrugCombobox.action',
												valueField : 'id',
												textField : 'undrugName',
												multiple : false,
												editable : false,
												value:nssmc1
											});
									$('#nssmc2').combobox(
											{
												url : 'drug/undrug/undrugCombobox.action',
												valueField : 'id',
												textField : 'undrugName',
												multiple : false,
												editable : false,
												value:nssmc2
											});
									$('#nssmc3').combobox(
											{
												url : 'drug/undrug/undrugCombobox.action',
												valueField : 'id',
												textField : 'undrugName',
												multiple : false,
												editable : false,
												value:nssmc3
											});
											//拟手术名称1
									$('#nssmc1').combobox(
											{
												url : 'drug/undrug/undrugCombobox.action',
												valueField : 'id',
												textField : 'undrugName',
												multiple : false,
												editable : false,
												onChange : function(nssmc1) {
													var id1 = $('#nssmc1').combobox('getValue');
													var id2 = $('#nssmc2').combobox('getValue');
													var id3 = $('#nssmc3').combobox('getValue');
													if (id1 != null && id1 != "" && id2 != null && id2 != ""
															&& id3 != null && id3 != "") {
														return false;
													}
													var id = id1 + "," + id2 + "," + id3;
													if (id2 == null || id2 == "") {
														if (id != null) {
															$('#nssmc2').combobox('reload',
																	'drug/undrug/undrugCombobox.action?ids=' + id);
														}
													}
													if (id3 == null || id3 == "") {
														if (id != null) {
															$('#nssmc3').combobox('reload',
																	'drug/undrug/undrugCombobox.action?ids=' + id);
														}
													}
												}
											});
									//拟手术名称2
									$('#nssmc2').combobox(
											{
												url : 'drug/undrug/undrugCombobox.action',
												valueField : 'id',
												textField : 'undrugName',
												multiple : false,
												editable : false,
												onChange : function(nssmc1) {
													var id1 = $('#nssmc1').combobox('getValue');
													var id2 = $('#nssmc2').combobox('getValue');
													var id3 = $('#nssmc3').combobox('getValue');
													if (id1 != null && id1 != "" && id2 != null && id2 != ""
															&& id3 != null && id3 != "") {
														return false;
													}
													var id = id1 + "," + id2 + "," + id3;
													if (id1 == null || id1 == "") {
														if (id != null) {
															$('#nssmc1').combobox('reload',
																	'drug/undrug/undrugCombobox.action?ids=' + id);
														}
													}
													if (id3 == null || id3 == "") {
														if (id != null) {
															$('#nssmc3').combobox('reload',
																	'drug/undrug/undrugCombobox.action?ids=' + id);
														}
													}
												}
											});
									//拟手术名称3
									$('#nssmc3').combobox(
											{
												url : 'drug/undrug/undrugCombobox.action',
												valueField : 'id',
												textField : 'undrugName',
												multiple : false,
												editable : false,
												onChange : function(nssmc1) {
													var id1 = $('#nssmc1').combobox('getValue');
													var id2 = $('#nssmc2').combobox('getValue');
													var id3 = $('#nssmc3').combobox('getValue');
													if (id1 != null && id1 != "" && id2 != null && id2 != ""
															&& id3 != null && id3 != "") {
														return false;
													}
													var id = id1 + "," + id2 + "," + id3;
													if (id1 == null || id1 == "") {
														if (id != null) {
															$('#nssmc1').combobox('reload',
																	'drug/undrug/undrugCombobox.action?ids=' + id);
														}
													}
													if (id2 == null || id2 == "") {
														if (id != null) {
															$('#nssmc2').combobox('reload',
																	'drug/undrug/undrugCombobox.action?ids=' + id);
														}
													}
												}
											});
									var keshi=OperationObj.opDoctordept;
									$('#keshi').combobox({
										url : 'baseinfo/department/departmentCombobox.action',
										valueField : 'id',
										textField : 'deptName',
										multiple : false,
										value:keshi
								
									});
										//助手1
									 var zhushou1=OperationObj.opAssist1;
									 var zhushou2=OperationObj.opAssist2;
									 var zhushou3=OperationObj.opAssist3;
									$('#zhushou1').combobox({
										url : 'baseinfo/employee/employeeCombobox.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										editable : false,
										value:zhushou1
									});
									$('#zhushou2').combobox({
										url : 'baseinfo/employee/employeeCombobox.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										editable : false,
										value:zhushou2
									});
									$('#zhushou3').combobox({
										url : 'baseinfo/employee/employeeCombobox.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										editable : false,
										value:zhushou3
									});
									//临时助手1
									var lszs1=OperationObj.opTempassist1;
									 var lszs2=OperationObj.opTempassist2;
									$('#lszs1').combobox({
										url : 'baseinfo/employee/employeeCombobox.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										editable : false,
										value:lszs1
									});
									$('#lszs2').combobox({
										url : 'baseinfo/employee/employeeCombobox.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										editable : false,
										value:lszs2
									});
									//指导医生编码
									var zdysbm=OperationObj.guiDoctor;
									$('#zdysbm').combobox({
										url : 'baseinfo/employee/employeeCombobox.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										editable : false,
										value:zdysbm
									});
									//执行科室
									var execDept=OperationObj.execDept;
									$('#execDept').combobox({
										url : 'baseinfo/department/departmentCombobox.action',
										valueField : 'id',
										textField : 'deptName',
										multiple : false,
										editable : false,
										value:execDept
									});
										//麻醉医生编码
									var mzysbm=OperationObj.aneDoctor;
									$('#mzysbm').combobox({
										url : 'baseinfo/employee/employeeCombobox.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										editable : false,
										value:mzysbm
									});
									//麻醉方式
									var aneWay=OperationObj.aneWay;
									$('#aneWay').combobox({
										url : 'likeAneway.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										editable : false,
										value:aneWay
									});
										//申请医生编码
									var sqysbm=OperationObj.applyDoctor;
									$('#sqysbm').combobox({
										url : 'baseinfo/employee/employeeCombobox.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										editable : false,
										value:sqysbm
									});
									//切口类型
									var inciType=OperationObj.inciType;
									$('#inciType').combobox({
										url : 'likeIncitype.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										editable : false,
										value:inciType
									});
									//感染类型
									var infectType=OperationObj.infectType;
									$('#infectType').combobox({
										url : 'likeInfecttype.action',
										valueField : 'id',
										textField : 'name',
										multiple : false,
										editable : false,
										value:infectType
									});
									var nssmc1=OperationObj.nssmc1;
									$('#nssmc1').combobox({
										url : 'drug/undrug/undrugCombobox.action',
										valueField : 'id',
										textField : 'undrugName',
										multiple : false,
										editable : false,
										value:nssmc1
									});
									var CodeSex=OperationObj.sex;
									$('#CodeSex').combobox({
									    url:'likeSex.action?str='+"CodeSex",    
									    valueField:'id',    
									    textField:'name',
									    multiple:false,
									    value:CodeSex
									});
								$('#folk').textbox('setValue',OperationObj.folk);
								 $('#name').textbox('setValue',OperationObj.name);
								 $('#age').textbox('setValue',OperationObj.age);
								 $('#birthday').datebox('setValue',OperationObj.birthday);
								 $('#patientNo').textbox('setValue',OperationObj.patientNo);
								 $('#profession').textbox('setValue',OperationObj.profession);
								 $('#address').textbox('setValue',OperationObj.address);
								 $('#telephone').textbox('setValue',OperationObj.telephone);
								 $('#prepayCost').textbox('setValue',OperationObj.prepayCost);
								 $('#inDept').textbox('setValue',OperationObj.inDept.deptName);
								 $('#wardNo').textbox('setValue',OperationObj.wardNo);
								 $('#bedNo').textbox('setValue',OperationObj.bedNo);
								 $('#bloodCode').textbox('setValue',OperationObj.bloodCode);
								 $('#opRoom').textbox('setValue',OperationObj.opRoom);
								 $('#preDate').datebox('setValue',OperationObj.preDate);
								 $('#receptDate').datebox('setValue',OperationObj.receptDate);
								 $('#duration').textbox('setValue',OperationObj.duration);
									var opType=OperationObj.opType;
									if (opType="1") {
											$('#oppt').attr("checked", true);  
									}
									if (opType="2") {
											$('#opjz').attr("checked", false);  
									}
									if (opType="3") {
											$('#opgr').attr("checked", true);  
									}
									var statusFlgHidden=OperationObj.status;
									if (statusFlgHidden="1") {
											$('#sssq').attr("checked", true);  
									}
									if (statusFlgHidden="2") {
											$('#sssp').attr("checked", false);  
									}
									if (statusFlgHidden="3") {
											$('#ssap').attr("checked", true);  
									}
									if (statusFlgHidden="4") {
											$('#sswc').attr("checked", false);  
									}
									if (statusFlgHidden="5") {
											$('#qxssdj').attr("checked", false);  
									}
								 	var consoleTypeFlgHidden=OperationObj.consoleType;
								 	if (consoleTypeFlgHidden="1") {
											$('#pt').attr("checked", true);  
									}
									if (consoleTypeFlgHidden="2") {
											$('#jt').attr("checked", false);  
									}
									if (consoleTypeFlgHidden="4") {
											$('#dt').attr("checked", true);  
									}
									if (consoleTypeFlgHidden="5") {
											$('#jjt').attr("checked", false);  
									}
									var isagreelookHiddenval=OperationObj.isagreelook;
									if (isagreelookHiddenval="1") {
											$('#yunxu').attr("checked", true);  
									}
									if (isagreelookHiddenval="2") {
											$('#byunxu').attr("checked", false);  
									}
									var aneWayHidden=OperationObj.anesType;
									if (aneWayHidden="1") {
											$('#jm').attr("checked", true);  
									}
									if (aneWayHidden="2") {
											$('#pm').attr("checked", false);  
									}
									var isfinishedHiddenval=OperationObj.isfinished;
									isbox1or0(wzss,wzss,isfinishedHiddenval);
									var isautobloodHidden=OperationObj.isautoblood;
									isbox1or0(bloodn,bloody,isautobloodHidden);
									var pathologyHidden=OperationObj.isneedpathology;
									isbox1or0(pathologn,pathology,pathologyHidden);
									var aneWayFlgHidden=OperationObj.isgerm;
									isbox1or0(Wayn,Wayy,aneWayFlgHidden);
									var screenupFlgHidden=OperationObj.screenup;
									isbox1or0(donw,up,screenupFlgHidden);
									var isgroupconsulHidden=OperationObj.isgroupconsultation;
									isbox1or0(groupn,groupy,isgroupconsulHidden);
									var dcnyFlgHidden=OperationObj.dcny;
									isbox1or0(dcnyn,dcnyy,dcnyFlgHidden);
									var isurgentHidden=OperationObj.isurgent;
									isbox1or0(isno,isyes,isurgentHidden);
									var isownexpenseHidden=OperationObj.isownexpense;
									isbox1or0(expensen,expensey,isownexpenseHidden);
									var issecondopertionHidden=OperationObj.issecondopertion;
									isbox1or0(secondn,secondy,issecondopertionHidden);
									var isneedprepHidden=OperationObj.isneedprep;
									isbox1or0(prepn,prepy,isneedprepHidden);
									var isneedaccoHidden=OperationObj.isneedacco;
									isbox1or0(accon,accoy,isneedaccoHidden);
									var isuniteFlgHidden=OperationObj.isunite;
									isbox1or0(uniten,unites,isneedaccoHidden);
									var isspecialHidden=OperationObj.isspecial;
									isbox1or0(specialn,specials,isspecialHidden);
									var isheavyHidden=OperationObj.isheavy;
									isbox1or0(heavyn,heavyy,isheavyHidden);
									var ischangeHidden=OperationObj.ischange;
									isbox1or0(yijf,weisf,ischangeHidden);
									var isaneFlgHidden=OperationObj.isane;
									isbox1or0(no,yes,isaneFlgHidden);
									var opertionpositionHidden=OperationObj.opertionposition;
									isbox1or0(wowei,cewei,opertionpositionHidden);
								 $('#degree').textbox('setValue',OperationObj.degree);
								 $('#bloodNum').textbox('setValue',OperationObj.bloodNum);
								 $('#bloodUnit').textbox('setValue',OperationObj.bloodUnit);
								 $('#opsNote').textbox('setValue',OperationObj.opsNote);
								 $('#relaCode').textbox('setValue',OperationObj.relaCode);
								 $('#folkComment').textbox('setValue',OperationObj.folkComment);
								 $('#uniteOpid').textbox('setValue',OperationObj.uniteOpid);
								 $('#uniteDisease').textbox('setValue',OperationObj.uniteDisease);
								 $('#clinical').textbox('setValue',OperationObj.clinical);
								 $('#contraindication').textbox('setValue',OperationObj.contraindication);
								 $('#indication').textbox('setValue',OperationObj.indication);
								 $('#stitution').textbox('setValue',OperationObj.stitution);
								 $('#preparation').textbox('setValue',OperationObj.preparation);
								 $('#complication').textbox('setValue',OperationObj.complication);
								 $('#discussion').textbox('setValue',OperationObj.discussion);
								 $('#"measures"').textbox('setValue',OperationObj.measures);
								 $('#pasource').textbox('setValue',OperationObj.pasource);
								$('#zhushoushu').numberbox('setValue',OperationObj.helperNum);
								$('#washNurse').textbox('setValue',OperationObj.washNurse);
								$('#accoNurse').textbox('setValue',OperationObj.accoNurse);
								$('#prepNurse').textbox('setValue',OperationObj.prepNurse);
								}
							}); 	
			
			
			}else{
				$.ajax({
					url: 'operation/operationapply/queryinpatientinfo.action?id='+id,
					type:'post',
					success: function(data) {
						var idCardObj = eval("("+data+")");
							 $('#name').textbox('setValue',idCardObj.patientName);
							 $('#CodeSex').textbox('setValue',idCardObj.patientSex);
							 $('#age').textbox('setValue',idCardObj.patientAge);
							 $('#bloodCode').textbox('setValue',idCardObj.patientBloodcode);
							 $('#telephone').textbox('setValue',idCardObj.patientPhone);
							 $('#address').textbox('setValue',idCardObj.patientAddress);
							 $('#birthday').datebox('setValue',idCardObj.patientBirthday);
							 $('#profession').textbox('setValue',idCardObj.patientOccupation);
							 $('#patientNo').textbox('setValue',idCardObj.no);
					}
				});
				}
			}
		},onContextMenu: function(e,node){//添加右键菜单
					e.preventDefault();
					$(this).tree('select',node.target);
					if(node.attributes.isNO == "1"){
						$('#editDiv').css("display","none");
					}else{
						$('#editDiv').css("display","block");
						$('#tDtmm').menu('show',{
							left: e.pageX,
							top: e.pageY
						});
					}
		}
				
	});
		
	function isbox1or0(radio1,radio2,value) {
			if (value="0") {
					$('#'+radio1).attr("checked", true);  
			}
			if (value="1") {
					$('#radio2').attr("checked", false);  
			}							
	}
	function check(type) {
		if (type != null) {
			$('#tDt').tree('options').url = "operation/operationapply/ptreeproof.action?type=1&juri=2";
			$('#tDt').tree('reload');
		}
	}

	/**
	 * 格式化生日
	 * @author  zpty
	 * @date 2015-6-19 9:25
	 * @version 1.0
	 */
	function formatDatebox(value) {
		if (value == null || value == '') {
			return '';
		}
		var dt;
		if (value instanceof Date) {
			dt = value;
		} else {

			dt = new Date(value);

		}

		return dt.format("yyyy-MM-dd"); //扩展的Date的format方法
	}
	//扩展的Date的format方法
	Date.prototype.format = function(format) {
		var o = {
			"M+" : this.getMonth() + 1, // month
			"d+" : this.getDate(), // day
			"h+" : this.getHours(), // hour
			"m+" : this.getMinutes(), // minute
			"s+" : this.getSeconds(), // second
			"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
			"S" : this.getMilliseconds()
		// millisecond
		};
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	};
	
	/**
	 * 审批操作
	 * @author  zpty
	 * @date 2015-8-11 9:25
	 * @version 1.0
	 */
	function edit(){//修改部门科室
		   		var id = getOperationSelected();
		   		Adddilog("审批",'operation/operationapply/eidtOperation.action?id='+id);
			}
	
		//加载dialog
			function Adddilog(title, url) {
				$('#oper').dialog({    
				    title: title,    
				    width: '60%',    
				    height:'40%',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				   });    
			}
			//打开dialog
			function openDialog() {
				$('#oper').dialog('open'); 
			}
			//关闭dialog
			function closeDialog() {
				$('#oper').dialog('close');  
			}
	//获得审批单的id,与上面的获取id的if条件刚好相反
	function getOperationSelected() {//获得选中节点
		var node = $('#tDt').tree('getSelected');
		if (node) {
			if (node.attributes.isNO != "1") {
				var id = node.id;
				return id;
			} else {
				return "";
			}
		} else {
			return "";
		}
	}
</script>
</body>
</html>

