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
					<input type="hidden" name="inpatientNurass.id">
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
								<input class="easyui-textbox" name="inpatientNurass.pname" value="${inpatientNurass.pname}" style="width:200px" readonly="true"/>
							</td>
							<td  class="honry-lable">
								性别：
							</td>
							<td>
								<input class="easyui-textbox" name="inpatientNurass.psex" value="${inpatientNurass.psex}" style="width:200px" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								年龄：
							</td>
							<td>
								<input class="easyui-textbox" name="inpatientNurass.page" value="${inpatientNurass.page}" style="width:200px" readonly="true"/>
							</td>
							<td class="honry-lable">
								科别：
							</td>
							<td>
								<input class="easyui-textbox" name="inpatientNurass.deptName" value="${inpatientNurass.deptName}" style="width:200px" readonly="true"/>
								<input type="hidden" name="inpatientNurass.deptCode" value="${inpatientNurass.deptCode}" />
							</td>
						</tr>
						<tr>
						    <td class="honry-lable">
								床号：
							</td>
							<td>
								<input class="easyui-textbox" style="width:200px" name="inpatientNurass.bedNo" value="${inpatientNurass.bedNo}" readonly="true"/>
							</td>
							<td  class="honry-lable">
								住院号：
							</td>
							<td>
								<input class="easyui-textbox" name="inpatientNurass.medicalrecodeId" value="${inpatientNurass.medicalrecodeId}" style="width:200px" readonly="true"/>
								<input type="hidden" name="inpatientNurass.inpatientNo" value="${inpatientNurass.inpatientNo}"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								职业：
							</td>
							<td>
								<input class="easyui-textbox" name="inpatientNurass.occupationa" value="${inpatientNurass.occupationa}" style="width:200px" readonly="true"/>
							</td>
							<td class="honry-lable">
								文化程度：
							</td>
							<td>
								<input id="culture" class="easyui-combobox" name="inpatientNurass.culture" style="width:200px" />
							</td>
						</tr>
						<tr>
						   <td class="honry-lable">
								婚姻状况：
							</td>
							<td>
								<input class="easyui-textbox" name="inpatientNurass.marriage" value="${inpatientNurass.marriage}" style="width:200px" readonly="true"/>
							</td>
							<td  class="honry-lable">
								患者本人电话：
							</td>
							<td>
							<input class="easyui-textbox" name="inpatientNurass.pphone" value="${inpatientNurass.pphone}" style="width:200px" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								联系人：
							</td>
							<td>
							<input class="easyui-textbox" name="inpatientNurass.cperson" value="${inpatientNurass.cperson}" style="width:200px" readonly="true"/>
							</td>
							<td class="honry-lable">
								联系人电话：
							</td>
							<td>
							<input class="easyui-textbox" name="inpatientNurass.cphone" value="${inpatientNurass.cphone}" style="width:200px" readonly="true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								入院日期：
							</td>
							<td>
								<input id="bhDate" name="inpatientNurass.bhDate" class="Wdate" type="text" value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'{%y+10}-%M-%d %H:%m'})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;" readonly="true"/>
							</td>
							<td class="honry-lable">
								入院方式：
							</td>
							<td>
								<input id="bhWay" class="easyui-combobox" name="inpatientNurass.bhWay" style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								其他入院方式：
							</td>
							<td>
								<input id="bhWayOth" class="easyui-textbox" name="inpatientNurass.bhWayOth" style="width:200px" data-options="required: true,disabled:true"/>
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
								<input id="mind" name="inpatientNurass.mind" class="easyui-combobox" style="width:200px"/>
							</td>
							<td class="honry-lable">
								表情：
							</td>
							<td>
								<input id="expression" name="inpatientNurass.expression" class="easyui-combobox" style="width:200px;"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								情绪状态：
							</td>
							<td>
								<input id="emotion" style="width:200px" name="inpatientNurass.emotion" class="easyui-combobox"/>
							</td>
							<td class="honry-lable">
								视力：
							</td>
							<td>
								<input id="vision" style="width:200px" name="inpatientNurass.vision" class="easyui-combobox"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								视力异常情况：
							</td>
							<td>
								<input id="visionRem" style="width:200px" name="inpatientNurass.visionRem" class="easyui-textbox" data-options="required: true,disabled:true"/>
							</td>
							<td class="honry-lable">
								听力：
							</td>
							<td>
								<input id="hearing" name="inpatientNurass.hearing" class="easyui-combobox" style="width:200px;"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								听力异常情况：
							</td>
							<td>
								<input id="hearingRem" name="inpatientNurass.hearingRem" class="easyui-textbox" style="width:200px" data-options="required: true,disabled:true"/>
							</td>
							<td class="honry-lable">
								沟通方式：
							</td>
							<td>
								<input id="commMode" name="inpatientNurass.commMode" class="easyui-combobox" style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								理解能力：
							</td>
							<td>
								<input id="compAbility" name="inpatientNurass.compAbility" class="easyui-combobox" style="width:200px"/>
							</td>
							<td class="honry-lable">
								口腔黏膜：
							</td>
							<td>
								<input id="oralMucosa" style="width:200px" name="inpatientNurass.oralMucosa" class="easyui-combobox"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								义齿：
							</td>
							<td>
								<input id="falseTooth" class="easyui-combobox" name="inpatientNurass.falseTooth" style="width:200px"/>
							</td>
							<td class="honry-lable">
								皮肤：
							</td>
							<td>
								<input id="skin" class="easyui-combobox" name="inpatientNurass.skin" style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								其他皮肤状况：
							</td>
							<td>
								<input id="skinOth" class="easyui-textbox" name="inpatientNurass.skinOth" style="width:200px;" data-options="required: true,disabled:true"/>
							</td>
							<td class="honry-lable" width="140px">
								压疮：
							</td>
							<td>
								<input id="sore" class="easyui-combobox" name="inpatientNurass.sore" style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								压疮部位：
							</td>
							<td>
								<input id="sorePosi" class="easyui-textbox" name="inpatientNurass.sorePosi" style="width:200px;" data-options="required: true,disabled:true"/>
							</td>
							<td class="honry-lable">
								压疮范围：
							</td>
							<td>
								<input id="soreRange" class="easyui-textbox" name="inpatientNurass.soreRange" style="width:200px" data-options="required: true,disabled:true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								小便：
							</td>
							<td>
								<input id="urine" class="easyui-combobox"  name="inpatientNurass.urine" style="width:200px;"/>
							</td>
							<td class="honry-lable">
								其他小便情况：
							</td>
							<td>
								<input id="urineOth" class="easyui-textbox" name="inpatientNurass.urineOth" style="width:200px" data-options="required: true,disabled:true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								大便：
							</td>
							<td>
								<input id="shit" class="easyui-combobox" name="inpatientNurass.shit" style="width:200px;"/>
							</td>
							<td class="honry-lable">
								腹泻：
							</td>
							<td>
								<input id="shitDiarr" class="easyui-numberbox" name="inpatientNurass.shitDiarr" style="width:200px" data-options="required: true,disabled:true"/>&nbsp<span style="font-size: 14px">次/日</span>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								其他大便情况：
							</td>
							<td>
								<input id="shitOth" class="easyui-textbox" name="inpatientNurass.shitOth" style="width:200px;" data-options="required: true,disabled:true"/>
							</td>
							<td class="honry-lable">
								自理能力：
							</td>
							<td>
								<input id="scAbility" class="easyui-combobox" name="inpatientNurass.scAbility" style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								Braden评分：
							</td>
							<td>
								<input class="easyui-numberbox" name="inpatientNurass.braden" style="width:200px;"/>
							</td>
							<td class="honry-lable">
								Morse评分 ：
							</td>
							<td>
								<input class="easyui-numberbox" name="inpatientNurass.morse" style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								体型：
							</td>
							<td>
								<input id="shape" class="easyui-combobox" name="inpatientNurass.shape" style="width:200px;"/>
							</td>
							<td class="honry-lable">
								饮食习惯：
							</td>
							<td>
								<input id="eating" class="easyui-combobox" name="inpatientNurass.eating" style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								其他饮食习惯：
							</td>
							<td>
								<input id="eatingOth" class="easyui-textbox" name="inpatientNurass.eatingOth" style="width:200px;" data-options="required: true,disabled:true"/>
							</td>
							<td class="honry-lable">
								忌食：
							</td>
							<td>
								<input class="easyui-textbox" name="inpatientNurass.eatingDiet" style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								异常忌食习惯：
							</td>
							<td>
								<input id="eatingAbn" class="easyui-combobox" name="inpatientNurass.eatingAbn" style="width:200px;"/>
							</td>
							<td class="honry-lable">
								吸烟：
							</td>
							<td>
								<input id="smoke" class="easyui-combobox" name="inpatientNurass.smoke" style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								吸烟情况：
							</td>
							<td>
								<input id="smokeRem" class="easyui-textbox" name="inpatientNurass.smokeRem" style="width:200px;" data-options="required: true,disabled:true"/>
							</td>
							<td class="honry-lable">
								饮酒：
							</td>
							<td>
								<input id="wine" class="easyui-combobox" name="inpatientNurass.wine" style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								饮酒情况：
							</td>
							<td>
								<input id="wineRemark" class="easyui-textbox" name="inpatientNurass.wineRemark" style="width:200px;" data-options="required: true,disabled:true"/>
							</td>
							<td class="honry-lable">
								睡眠：
							</td>
							<td>
								<input id="sleep" class="easyui-combobox" name="inpatientNurass.sleep" style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								每日睡眠：
							</td>
							<td>
								<input class="easyui-numberbox" name="inpatientNurass.sleepDay" style="width:200px;"/>&nbsp<span style="font-size: 14px">小时</span>
							</td>
							<td class="honry-lable">
								药物辅助睡眠：
							</td>
							<td>
								<input id="sleepMedi" class="easyui-combobox" name="inpatientNurass.sleepMedi" style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								药物辅助睡眠情况：
							</td>
							<td>
								<input id="sleepMediRem" class="easyui-textbox" name="inpatientNurass.sleepMediRem" style="width:200px;" data-options="required: true,disabled:true"/>
							</td>
							<td class="honry-lable">
								家属态度：
							</td>
							<td>
								<input id="fstate" class="easyui-combobox" name="inpatientNurass.fstate" style="width:200px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								宗教信仰：
							</td>
							<td>
								<input id="religion" class="easyui-combobox" name="inpatientNurass.religion" style="width:200px;"/>
							</td>
							<td class="honry-lable">
								宗教信仰情况：
							</td>
							<td>
								<input id="religionRem" class="easyui-textbox"  name="inpatientNurass.religionRem" style="width:200px" data-options="required: true,disabled:true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								既往史：
							</td>
							<td>
								<input id="pastHis" class="easyui-combobox" name="inpatientNurass.pastHis" style="width:200px;"/>
							</td>
							<td class="honry-lable">
								其他既往史：
							</td>
							<td>
								<input id="pastHisOther" class="easyui-textbox" name="inpatientNurass.pastHisOther" style="width:200px" data-options="required: true,disabled:true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								过敏史：
							</td>
							<td>
								<input id="allerHis" class="easyui-combobox" name="inpatientNurass.allerHis" style="width:200px;"/>
							</td>
							<td class="honry-lable">
								食物：
							</td>
							<td>
								<input id="allerHisFood" class="easyui-textbox" name="inpatientNurass.allerHisFood" style="width:200px" data-options="required: true,disabled:true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								药物：
							</td>
							<td>
								<input id="allerHisMedi" class="easyui-textbox" name="inpatientNurass.allerHisMedi" style="width:200px;" data-options="required: true,disabled:true"/>
							</td>
							<td class="honry-lable">
								其他过敏史：
							</td>
							<td>
								<input id="allerHisOther" class="easyui-textbox" name="inpatientNurass.allerHisOther" style="width:200px" data-options="required: true,disabled:true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								责任护士：
							</td>
							<td>
								<input class="easyui-textbox" name="inpatientNurass.nurseName" style="width:200px;"/>
							</td>
							<td class="honry-lable">
								评估日期：
							</td>
							<td>
								<input id="createTime" name="inpatientNurass.createTime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'{%y+10}-%M-%d %H:%m'})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
						</tr>
				</table>
			</form>	
		<div style="text-align: center; padding: 5px;height:150px ">
			<a href="javascript:submit(0);" class="easyui-linkbutton"
				data-options="iconCls:'icon-save'">保存</a>
			<a href="javascript:clear();" class="easyui-linkbutton"
				data-options="iconCls:'icon-clear'">清除</a>
			<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script>
		var dateVal = "${inpatientNurass.bhDate}"
		$("#bhDate").val(dateVal.substr(0,dateVal.length-5))
		$('#createTime').val(getNowFormatDate())
		//文化程度
		$('#culture') .combobox({ 
			url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=degree'/>",
			valueField:'name',    
			textField:'name',
			editable:false
		});

		//入院方式
		$('#bhWay').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '步行',
		    	name: '步行'
			},{
				encode: '轮椅',
				name: '轮椅'
			},{
				encode: '平车',
				name: '平车'
			},{
				encode: '其他',
				name: '其他'
			}],
			onChange: function(node){
				var texts=$('#bhWay').combobox('getText');
				if (texts=="其他") {
					$('#bhWayOth').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#bhWayOth').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#bhWayOth').textbox('setValue','');
				}
			}
		});
		//神志
		$('#mind').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '清楚',
		    	name: '清楚'
			},{
				encode: '嗜睡',
				name: '嗜睡'
			},{
				encode: '意识模糊',
				name: '意识模糊'
			},{
				encode: '昏睡',
				name: '昏睡'
			},{
				encode: '浅昏迷',
				name: '浅昏迷'
			},{
				encode: '深昏迷',
				name: '深昏迷'
			},{
				encode: '痴呆',
				name: '痴呆'
			}]
		})
		//表情
		$('#expression').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '正常',
		    	name: '正常'
			},{
				encode: '淡漠',
				name: '淡漠'
			},{
				encode: '痛苦',
				name: '痛苦'
			},{
				encode: '紧张',
				name: '紧张'
			}]
		})
		//情绪状态
		$('#emotion').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '稳定',
		    	name: '稳定'
			},{
				encode: '易激动',
				name: '易激动'
			},{
				encode: '焦虑',
				name: '焦虑'
			},{
				encode: '恐惧',
				name: '恐惧'
			},{
				encode: '抑郁',
				name: '抑郁'
			}]
		})
		//视    力
		$('#vision').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '正常',
		    	name: '正常'
			},{
				encode: '异常',
				name: '异常'
			}],
			onChange: function(node){
				var texts=$('#vision').combobox('getText');
				if (texts=="异常") {
					$('#visionRem').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#visionRem').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#visionRem').textbox('setValue','');
				}
			}
		})
		//听力
		$('#hearing').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '正常',
		    	name: '正常'
			},{
				encode: '异常',
				name: '异常'
			}],
			onChange: function(node){
				var texts=$('#hearing').combobox('getText');
				if (texts=="异常") {
					$('#hearingRem').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#hearingRem').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#hearingRem').textbox('setValue','');
				}
			}
		})
		//沟通方式
		$('#commMode').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '语言',
		    	name: '语言'
			},{
				encode: '文字',
				name: '文字'
			},{
				encode: '手势',
				name: '手势'
			}]
		})
		//理解能力
		$('#compAbility').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '良好',
		    	name: '良好'
			},{
				encode: '一般',
				name: '一般'
			},{
				encode: '差',
				name: '差'
			}]
		})
		//口腔黏膜
		$('#oralMucosa').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '正常',
		    	name: '正常'
			},{
				encode: '充血',
				name: '充血'
			},{
				encode: '破损',
				name: '破损'
			},{
				encode: '霉菌感染',
				name: '霉菌感染'
			},{
				encode: '溃疡',
				name: '溃疡'
			}]
		})
		//义齿
		$('#falseTooth').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '无',
		    	name: '无'
			},{
				encode: '有',
				name: '有'
			}]
		})
		//皮    肤
		$('#skin').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    multiple:true,
		    data: [{
		    	encode: '正常',
		    	name: '正常'
			},{
				encode: '水肿',
				name: '水肿'
			},{
				encode: '黄疸',
				name: '黄疸'
			},{
				encode: '苍白',
				name: '苍白'
			},{
				encode: '紫绀',
				name: '紫绀'
			},{
				encode: '皮疹',
				name: '皮疹'
			},{
				encode: '瘀斑',
				name: '瘀斑'
			},{
				encode: '搔痒',
				name: '搔痒'
			},{
				encode: '破损',
				name: '破损'
			},{
				encode: '其他',
				name: '其他'
			}],
			onChange: function(node){
				var texts=$('#skin').combobox('getValues');
				$('#skin').combobox('setValues',texts);
				if (texts.indexOf("其他") >= 0) {
					$('#skinOth').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#skinOth').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#skinOth').textbox('setValue','');
				}
			}
		})
		//压疮
		$('#sore').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '无',
		    	name: '无'
			},{
				encode: '有',
				name: '有'
			}],
			onChange: function(node){
				var texts=$('#sore').combobox('getText');
				if (texts=="有") {
					$('#sorePosi').textbox({ 
						required: true, 
					    disabled:false
					});
					$('#soreRange').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#sorePosi').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#soreRange').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#sorePosi').textbox('setValue','');
					$('#soreRange').textbox('setValue','');
				}
			}
		})
		//小便
		$('#urine').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '正常',
		    	name: '正常'
			},{
				encode: '失禁',
				name: '失禁'
			},{
				encode: '尿频',
				name: '尿频'
			},{
				encode: '尿少',
				name: '尿少'
			},{
				encode: '尿急',
				name: '尿急'
			},{
				encode: '尿痛',
				name: '尿痛'
			},{
				encode: '尿潴留',
				name: '尿潴留'
			},{
				encode: '尿管',
				name: '尿管'
			},{
				encode: '造口',
				name: '造口'
			},{
				encode: '其他',
				name: '其他'
			}],
			onChange: function(node){
				var texts=$('#urine').combobox('getText');
				if (texts=="其他") {
					$('#urineOth').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#urineOth').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#urineOth').textbox('setValue','');
				}
			}
		})
		//大便
		$('#shit').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    multiple:true,
		    data: [{
		    	encode: '正常',
		    	name: '正常'
			},{
				encode: '失禁',
				name: '失禁'
			},{
				encode: '便秘',
				name: '便秘'
			},{
				encode: '黑便',
				name: '黑便'
			},{
				encode: '造口',
				name: '造口'
			},{
				encode: '腹泻',
				name: '腹泻'
			},{
				encode: '尿潴留',
				name: '尿潴留'
			},{
				encode: '尿管',
				name: '尿管'
			},{
				encode: '造口',
				name: '造口'
			},{
				encode: '腹泻',
				name: '腹泻'
			},{
				encode: '其他',
				name: '其他'
			}],
			onChange: function(node){
				var texts=$('#shit').combobox('getValues');
				$('#shit').combobox('setValues',texts);
				if (texts.indexOf("腹泻") >= 0) {
					$('#shitDiarr').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#shitDiarr').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#shitDiarr').textbox('setValue','');
				}
				if (texts.indexOf("其他") >= 0) {
					$('#shitOth').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#shitOth').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#shitOth').textbox('setValue','');
				}
			}
		})
		//自理能力
		$('#scAbility').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '无需依赖(100分)',
		    	name: '无需依赖(100分)'
			},{
				encode: '轻度依赖(75-95分)',
				name: '轻度依赖(75-95分)'
			},{
				encode: '中度依赖(50-70分)',
				name: '中度依赖(50-70分)'
			},{
				encode: '重度依赖(25-45分)',
				name: '重度依赖(25-45分)'
			},{
				encode: '完全依赖(0-20分)',
				name: '完全依赖(0-20分)'
			}]
		})
		//体    型
		$('#shape').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '正常',
		    	name: '正常'
			},{
				encode: '肥胖',
				name: '肥胖'
			},{
				encode: '消瘦',
				name: '消瘦'
			},{
				encode: '恶液质',
				name: '恶液质'
			}]
		})
		//饮食习惯
		$('#eating').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    multiple:true,
		    data: [{
		    	encode: '正常',
		    	name: '正常'
			},{
				encode: '咸',
				name: '咸'
			},{
				encode: '甜',
				name: '甜'
			},{
				encode: '辛辣',
				name: '辛辣'
			},{
				encode: '油腻',
				name: '油腻'
			},{
				encode: '清淡',
				name: '清淡'
			},{
				encode: '其他',
				name: '其他'
			}],
			onChange: function(node){
				var texts=$('#eating').combobox('getValues');
				$('#eating').combobox('setValues',texts);
				if (texts.indexOf("其他") >= 0) {
					$('#eatingOth').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#eatingOth').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#eatingOth').textbox('setValue','');
				}
			}
		})
		//异常饮食习惯
		$('#eatingAbn').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    multiple:true,
		    data: [{
		    	encode: '食欲不振',
		    	name: '食欲不振'
			},{
				encode: '吞咽困难',
				name: '吞咽困难'
			},{
				encode: '咀嚼困难',
				name: '咀嚼困难'
			},{
				encode: '恶心',
				name: '恶心'
			},{
				encode: '呕吐',
				name: '呕吐'
			}],
			onChange: function(node){
				var texts=$('#eatingAbn').combobox('getValues');
				$('#eatingAbn').combobox('setValues',texts);
			}
		})
		//吸烟
		$('#smoke').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '否',
		    	name: '否'
			},{
				encode: '是',
				name: '是'
			}],
			onChange: function(node){
				var texts=$('#smoke').combobox('getText');
				if (texts=="是") {
					$('#smokeRem').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#smokeRem').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#smokeRem').textbox('setValue','');
				}
			}
		})
		//饮酒
		$('#wine').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '否',
		    	name: '否'
			},{
				encode: '是',
				name: '是'
			}],
			onChange: function(node){
				var texts=$('#wine').combobox('getText');
				if (texts=="是") {
					$('#wineRemark').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#wineRemark').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#wineRemark').textbox('setValue','');
				}
			}
		})
		// 睡眠
		$('#sleep').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '正常',
		    	name: '正常'
			},{
				encode: '多梦',
				name: '多梦'
			},{
				encode: '易醒',
				name: '易醒'
			}]
		})
		//药物辅助睡眠
		$('#sleepMedi').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '无',
		    	name: '无'
			},{
				encode: '有',
				name: '有'
			}],
			onChange: function(node){
				var texts=$('#sleepMedi').combobox('getText');
				if (texts=="有") {
					$('#sleepMediRem').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#sleepMediRem').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#sleepMediRem').textbox('setValue','');
				}
			}
		})
		//家属态度
		$('#fstate').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '关心',
		    	name: '关心'
			},{
				encode: '不关心',
				name: '不关心'
			},{
				encode: '过于关心',
				name: '过于关心'
			},{
				encode: '无人照顾',
				name: '无人照顾'
			}]
		})
		//宗教信仰
		$('#religion').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '无',
		    	name: '无'
			},{
				encode: '有',
				name: '有'
			}],
			onChange: function(node){
				var texts=$('#religion').combobox('getText');
				if (texts=="有") {
					$('#religionRem').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#religionRem').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#religionRem').textbox('setValue','');
				}
			}
		})
		//既 往 史
		$('#pastHis').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    multiple:true,
		    data: [{
		    	encode: '高血压',
		    	name: '高血压'
			},{
				encode: '心脏病',
				name: '心脏病'
			},{
				encode: '糖尿病',
				name: '糖尿病'
			},{
				encode: '脑血管病',
				name: '脑血管病'
			},{
				encode: '手术史',
				name: '手术史'
			},{
				encode: '精神病',
				name: '精神病'
			},{
				encode: '其他',
				name: '其他'
			}],
			onChange: function(node){
				var texts=$('#pastHis').combobox('getValues');
				$('#pastHis').combobox('setValues',texts);
				if (texts.indexOf("其他") >= 0) {
					$('#pastHisOther').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#pastHisOther').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#pastHisOther').textbox('setValue','');
				}
			}
		})
		//过 敏 史
		$('#allerHis').combobox({
			valueField:'encode',    
		    textField:'name',
		    editable:false,
		    data: [{
		    	encode: '无',
		    	name: '无'
			},{
				encode: '有',
				name: '有'
			}],
			onChange: function(node){
				var texts=$('#allerHis').combobox('getText');
				if (texts=="有") {
					$('#allerHisFood').textbox({ 
						required: true, 
					    disabled:false
					});
					$('#allerHisMedi').textbox({ 
						required: true, 
					    disabled:false
					}); 
					$('#allerHisOther').textbox({ 
						required: true, 
					    disabled:false
					}); 
				}else{
					$('#allerHisFood').textbox({ 
						required: true, 
					    disabled:true
					});
					$('#allerHisMedi').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#allerHisOther').textbox({ 
						required: true, 
					    disabled:true
					}); 
					$('#allerHisFood').textbox('setValue','');
					$('#allerHisMedi').textbox('setValue','');
					$('#allerHisOther').textbox('setValue','');
				}
			}
		})
		
		
	//表单提交submit信息
  	function submit(){
			$('#infEditForm').form('submit',{
		  	     url:"<c:url value='/publics/assessment/save.action'/>",
		  		 onSubmit:function(){
		  			if(!$('#infEditForm').form('validate')){
						$.messager.show({  
							title:'提示信息' ,   
							msg:'验证没有通过,不能提交表单!'  
						}); 
						return false ;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
				     return $(this).form('validate');  
				 },  
				success:function(data){
					$.messager.progress('close');
					if(data=="success"){
						$.alert('提示','保存成功',function(){
							window.opener.$("#invoiceNoSummary").datagrid("load");
							window.close();
						});
				   }else {
					   $.messager.alert('提示','保存失败');
				  	}
				 },
				error:function(date){
					$.messager.progress('close');
					$.messager.alert('提示','保存失败');
				}
		  	});
  	}

	//清除所填信息
	function clear() {
		window.location.reload();
	}
	//关闭窗口
	function closeLayout() {
		window.close();
	}
	//获取当前时间
	function getNowFormatDate() {
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + date.getHours() + seperator2 + date.getMinutes();
	    return currentdate;
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>