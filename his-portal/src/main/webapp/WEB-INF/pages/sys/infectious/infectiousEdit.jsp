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
<script>
	var sexMap=new Map();
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});

</script>
</head>
<body>
	<div id="listeidt" class="easyui-panel" style="height:100%">
		<div style=" padding: 5px 5px 0px 25px">
			<form id="infEditForm" method="post">
				<div>
					<font style="padding-left: 45%;font-size: 28px" class="title">传染病报告卡</font>
				</div>
				<div  style=" padding: 3px 3px;" class="sub_title">
					&nbsp;<font style="font-size: 14px">卡片编号：</font>
					<input class="easyui-textbox" name="infectious.reportNo" value="${infectious.reportNo}">
					<font style="margin-left: 3%;font-size: 14px">报卡类别：</font>
					<input id="reportType"value="${infectious.reportType}"  />
					<input id="reportTypes" name="infectious.reportType" value="${infectious.reportType}" type="hidden"/>
					<font style="font-size: 14px;margin-left: 3%;">病历号：</font>
					<input class="inputCss"   name="infectious.medicalrecordId" id="medicalrecord" value="${infectious.medicalrecordId}" >
					<a href="javascript:void(0)" onclick="searchFromcx()"
										class="easyui-linkbutton" iconCls="icon-search">查询</a><br>
				</div>
					<input type="hidden" id="id" name="id" value="${infectious.id }">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width:100%;">
						<tr>
							<td class="honry-lable"  style="width:150px">
							患者姓名：
							</td>
							<td style="width:150px">
								<input class="easyui-textbox" name="patientName" id="patientNameId"
								style="width:200px"	value="${infectious.patientName}"
									/>
							</td>
							<td  class="honry-lable">
								患者家长姓名：
							</td>
							<td>
								<input class="easyui-textbox" name="patientParents" id="patientParents"
								style="width:200px"	value="${infectious.patientParents}"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								证件类型：
							</td>
							<td>
								<input class="easyui-combobox" name="infectious.certificatesType" id="CodeCertificate" 
								style="width:200px"	value="${infectious.certificatesType}" />
							</td>
							<td class="honry-lable">
								证件号码：
							</td>
							<td>
								<input class="easyui-textbox" name="infectious.certificatesNo"  id="certificatesNo"
									style="width:200px" value="${infectious.certificatesNo}"/>
							</td>
						</tr>
						<tr>
						    <td class="honry-lable">
								性别：
							</td>
							<td>
								<input type="hidden" id="sexHid" value="${infectious.reportSex }" >
								<input style="width:200px" id="CodeSex" name="infectious.reportSex"data-options="required:true"/>
							</td>
							<td  class="honry-lable">
								联系电话：
							</td>
							<td>
								<input id="telephone"class="easyui-textbox" name="infectious.telephone" 
								style="width:200px"	value="${infectious.telephone}"data-options="validType:'phoneRex',required:true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								患者职业：
							</td>
							<td>
								<input id="patientProfession"   name="infectious.patientProfession" 
								style="width:200px" value="${infectious.patientProfession}"  />
								<a href="javascript:delSelectedData('patientProfession');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
							<td class="honry-lable">
								其他职业：
							</td>
							<td>
								<input class="easyui-textbox" id="otherProfession" name="infectious.otherProfession" 
									value="${infectious.otherProfession}" style="width:200px" disabled="disabled" />
							</td>
						</tr>
						<tr>
						   <td class="honry-lable">
								工作单位：
							</td>
							<td>
								<input class="easyui-textbox" name="infectious.workPlace"  id="workPlace"
									style="width:200px" value="${infectious.workPlace}" data-options="disabled:${save }" />
							</td>
							<td  class="honry-lable">
								病人来源：
							</td>
							<td>
							<input id="CodeBrlydq" name="infectious.homeArea" 
								style="width:200px"value="${infectious.homeArea}" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								出生日期：
							</td>
							<td colspan="3">
								<input id="reportBirthday" name="infectious.reportBirthday" value="${infectious.reportBirthday}" class="Wdate" type="text" onClick="WdatePicker({maxDate:'%y-%M-%d',onpicked:changeAge})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								<font style="font-size: 12px" class="birth">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如出生日期不详，实足年龄：</font>
								<input class="easyui-numberbox" name="infectious.reportAge"  id="reportAge"
									value="${infectious.reportAge}" style="width: 50px"  data-options="min:0,max:120,precision:0"/>
									<font style="font-size: 12px" class="birth">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年龄单位：</font>
									<input id="agedw" name="infectious.reportAgeunit" value="${infectious.reportAgeunit}" />
									<font style="font-size: 14px;color: red" class="birthTip">&nbsp;&nbsp;&nbsp;出生日期，实足年龄必填其中一个!</font>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								现住址：
							</td>
							<td  colspan="3">
							<div style="text-align: 25px; height: 25px;">
								<input id="patientCitys" name="infectious.homeCouty" value="${infectious.homeCouty}" type="hidden"/>
								<input   id="homeones" value="${oneCode }" style="width: 130px"  data-options="prompt:'省/直辖市',disabled:${save }"/>
								<input  id="hometwos" value="${twoCode }" style="width: 130px"  data-options="prompt:'市',disabled:${save }"/>
								<input   id="homethrees" value="${threeCode }" style="width: 130px"  data-options="prompt:'县',disabled:${save }"/>
								<input  id="homefours" value="${fourCode }" style="width: 130px"  data-options="prompt:'区',disabled:${save }"/>
							</div>
							<div style="margin-top:5px;text-align: 25px; height: 25px;">
								<input class="easyui-textbox" id="homeTown"   name="infectious.homeTown" data-options="disabled:${save }" value="${infectious.homeTown}"style="width:200px"  />
							<span>乡（镇,乡）</span>
								<input  class="easyui-textbox" id="homeAddress"   data-options="disabled:${save }" name="infectious.homeAddress" value="${infectious.homeAddress}"style="width:200px"  />
							<span>村（门牌号）</span>
							</div>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								疾病：
							</td>
							<td colspan="6">
									<input class="easyui-combotree" style="width:200px" id="diseaseType" name="infectious.diseaseType" value="${infectious.diseaseType}" data-options="disabled:${save }"/>
									<input type="hidden"  id="diseaseTypename" name="infectious.diseaseTypename" value="${infectious.diseaseTypename}" />
									<input type="hidden"  id="otherDiseasename" name="infectious.otherDiseasename" value="${infectious.otherDiseasename}" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								监测其他性病：
							</td>
							<td colspan="6">
									<input class="easyui-combobox" style="width:200px" id="otherDisease" name="otherDisease" value="${infectious.otherDisease}"disabled="disabled"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								病例分类1：
							</td>
							<td colspan="1">
								<input  class="easyui-textbox" id="caseClass1" name="infectious.caseClass1" 
									style="width:200px" disabled="disabled" />
							</td>
							<td class="honry-lable">
								病例分类2：
							</td>
							<td colspan="4">
								<input  class="easyui-textbox" id="caseClass2"name="infectious.caseClass2"
									style="width:200px" disabled="disabled" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								发病日期：
							</td>
							<td>
								<input id="infectDate" name="infectious.infectDate" value="${infectious.infectDate}" class="Wdate" type="text" onClick="WdatePicker({maxDate:'%y-%M-{%d+10}',minDate:'%y-%M-%d',onpicked:onSelectDate})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
					
							<td class="honry-lable">
								诊断时间：
							</td>
							<td>
								<input id="diagnosisDate" name="infectious.diagnosisDate" value="${infectious.diagnosisDate}" class="Wdate" type="text" onClick="WdatePicker({maxDate:'%y-%M-{%d+10}',minDate:'%y-%M-%d',onpicked:onSelectDate})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								性病附加栏（报告性病时必须填）
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								婚姻状况：
							</td>
							<td>
								<input id="CodeMarry" name="infectious.patientMari"  style="width:200px"
								disabled="disabled" />
							</td>
					
							<td class="honry-lable">
								文化程度：
							</td>
							<td colspan="4">
								<input   id="CodeDegree" name="infectious.patientEducation" style="width:200px"
								 style="width:200px"disabled="disabled" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								接触史：
							</td>
							<td>
								<input  id="infectHistory" name="infectious.infectHistory" style="width:200px"
								disabled="disabled"/>
							</td>
							<td class="honry-lable">
								其他接触史：
							</td>
							<td colspan="4">
								<input class="easyui-textbox" id="otherInfect" name="infectious.otherInfect" value="${infectious.otherInfect}"
								 style="width:200px"disabled="disabled" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								感染途径：
							</td>
							<td colspan="1">
								<input  id="CodeGrtj" name="infectious.infectSource" disabled="disabled"style="width:200px"/>
							</td>
							<td class="honry-lable">
								其他途径：
							</td>
							<td colspan="4">
								<input class="easyui-textbox" id="otherSource" name="infectious.otherSource" value="${infectious.otherSource}" style="width:200px"
								disabled="disabled"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								样本来源：
							</td>
							<td colspan="1">
								<input  id="CodeYbly" name="infectious.sampleSource" style="width:200px"
								disabled="disabled"/>
							</td>
							<td class="honry-lable">
								其他来源：
							</td>
							<td colspan="4">
								<input  class="easyui-textbox" id="otherSample" name="infectious.otherSample" value="${infectious.otherSample}"style="width:200px"
								disabled="disabled" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
								乙肝病例附加栏（报告乙肝时必须填）
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								HBsAg阳性时间：
							</td>
							<td colspan="1">
								<input  id="reportHbsag"  name="infectious.reportHbsag"
								style="width:200px" disabled="disabled"/>
							</td>
							<td class="honry-lable">
								本次ALT：
							</td>
							<td colspan="4">
								<input  id="alt" name="infectious.alt" 
								style="width:200px"	disabled/>U/L
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								首次出现症状：
							</td>
							<td colspan="1">
								<input id="firsthbDate" name="infectious.firsthbDate" value="${infectious.firsthbDate}" class="Wdate" type="text" onClick="WdatePicker({maxDate:'%y-%M-%d',onpicked:onSelectDate})"  style="width:200px;disabled:disabled;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td class="honry-lable" width="140px">
								抗-HBc IgM 1:1000 检测结果：
							</td>
							<td colspan="3">
								<input  id="againstHbc"name="infectious.againstHbc" 
								style="width:200px" disabled="disabled"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								肝穿检测结果：
							</td>
							<td>
								<input  id="liverbiopsyResult" name="infectious.liverbiopsyResult"
								style="width:200px"disabled="disabled"/>
							</td>
							<td class="honry-lable">
								恢复期血清HBsAg阴转，抗HBs阳转：
							</td>
							<td colspan="3">
								<input  id="rps" name="infectious.rps" 
								style="width:200px"disabled="disabled"/>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								备注
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								报告单位：
							</td>
							<td>
								<input id="reportUnitphone"class="easyui-textbox"" name="infectious.reportUnit" 
								style="width:200px"	value="${infectious.reportUnit}" />
							</td>
							<td class="honry-lable">
								报告单位联系电话：
							</td>
							<td>
								<input id="reportUnitphone"class="easyui-textbox"" name="infectious.reportUnitphone" 
									style="width:200px"value="${infectious.reportUnitphone}"data-options="validType:'phoneRex'" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
									报卡日期：
							</td>
							<td>
								<input id="reportDate" name="infectious.reportDate" value="${infectious.reportDate}" class="Wdate" type="text" onClick="WdatePicker({maxDate:'%y-%M-{%d+10}',minDate:'%y-%M-%d',onpicked:onSelectDate})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td class="honry-lable">
								死亡日期：
							</td>
							<td>
								<input id="deadDate" name="infectious.deadDate" value="${infectious.deadDate}" class="Wdate" type="text" onClick="WdatePicker({maxDate:'%y-%M-{%d+10}',minDate:'%y-%M-%d',onpicked:onSelectDate})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								报告医生：
							</td>
							<td>
								<input class="easyui-textbox" name="infectious.reportDoctor" 
								style="width:200px"	value="${infectious.reportDoctor}" />
							</td>
							<td class="honry-lable">
								医生科室代码：
							</td>
							<td colspan="3">
								<input class="easyui-textbox" name="infectious.doctorDept" 
								style="width:200px"	value="${infectious.doctorDept}" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								作废人：
							</td>
							<td>
								<input class="easyui-textbox"" name="infectious.cancelOper" 
									style="width:200px"value="${infectious.cancelOper}" />
							</td>
							<td class="honry-lable">
								作废时间：
							</td>
							<td colspan="3">
								<input id="cancelDate" name="infectious.cancelDate" value="${infectious.cancelDate}" class="Wdate" type="text" onClick="WdatePicker({maxDate:'%y-%M-{%d+10}',minDate:'%y-%M-%d',onpicked:onSelectDate})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								事由：
							</td>
							<td colspan="6">
								<textarea class="easyui-textbox" data-options="multiline:'true'" style="width: 100%;height: 50px;font-size: 14px"  
									name="infectious.reportReason"  >${infectious.reportReason }</textarea>
							</td>
						</tr>	
						<tr>
							<td class="honry-lable">
								订正标记：
							</td>
							<td>
								<input class="easyui-textbox" id="correctFlag" 
								style="width:200px"	value="${infectious.correctFlag}" />
								<input  id="correctFlags" name="infectious.correctFlag" value="${infectious.correctFlag}" type="hidden"/>
							</td>
							<td class="honry-lable">
								订正卡编号：
							</td>
							<td>
								<input class="easyui-textbox" name="infectious.correctReportNo" 
								style="width:200px"	value="${infectious.correctReportNo}" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								原卡编号：
							</td>
							<td>
								<input class="easyui-textbox" name="infectious.correctedReportNo" 
								style="width:200px"	value="${infectious.correctedReportNo}" />
							</td>
							<td class="honry-lable">
								订正前病名：
							</td>
							<td colspan="6">
								<input class="easyui-textbox" name="infectious.correctedDisease" 
								 style="width:200px" value="${infectious.correctedDisease}" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								扩展信息1：
							</td>
							<td colspan="6" >
								<textarea class="easyui-textbox" data-options="multiline:'true'" style="width: 100%;height: 50px;font-size: 14px"  
									name="infectious.extendInfo1">${infectious.extendInfo1 }</textarea>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								扩展信息2：
							</td>
							<td colspan="6">
								<textarea class="easyui-textbox" data-options="multiline:'true'" style="width: 100%;height: 50px;font-size: 14px"  
									name="infectious.extendInfo2">${infectious.extendInfo2 }</textarea>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								扩展信息3：
							</td>
							<td colspan="6">
								<textarea class="easyui-textbox" data-options="multiline:'true'" style="width: 100%;height: 50px;font-size: 14px"  
									name="infectious.extendInfo3">${infectious.extendInfo3 }</textarea>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								存住院号：
							</td>
							<td>
								<input class="easyui-textbox"" name="infectious.inpatientNo" 
								style="width:200px"	value="${infectious.inpatientNo}" />
							</td>
							<td class="honry-lable">
								梅毒螺旋体抗体：
							</td>
							<td>
								<input class="easyui-textbox" name="infectious.extendInfo4" 
								style="width:200px"	value="${infectious.extendInfo4}" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								审核人：
							</td>
							<td>
								<input class="easyui-textbox"  name="infectious.approveOper" id="approveOper"
								style="width:200px"	value="${infectious.approveOper}" />
							</td>
							<td class="honry-lable">
								审核日期：
							</td>
							<td colspan="3">
								<input id="approveDate" name="infectious.approveDate" value="${infectious.approveDate}" class="Wdate" type="text" onClick="WdatePicker({maxDate:'%y-%M-{%d+10}',minDate:'%y-%M-%d'})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								备注：
							</td>
							<td colspan="6">
							<textarea class="easyui-textbox" data-options="multiline:'true'"" style="width: 100%;height: 50px;font-size: 12px"  
									name="infectious.reportRemark">${infectious.reportRemark }</textarea>
							</td>
						</tr>
				</table>
			</form>	
    	</div>  
		<div style="text-align: center; padding: 5px">
			
			<a href="javascript:submit(0);" class="easyui-linkbutton"
				data-options="iconCls:'icon-save'">保存</a>
			<a href="javascript:clear();" class="easyui-linkbutton"
				data-options="iconCls:'icon-clear'">清除</a>
			<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
		<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:500;height:500;" data-options="modal:true, closed:true">  
		&nbsp;<input class="inputCss"    id="medicalSex" data-options="prompt:'请输入姓名、证件号'">
		<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a><br>
	     	<table id="infoDatagrid"  fit="true" data-options="fitColumns:true,singleSelect:true">   
			</table>  
		</div>
		<div id="roleaddUserdiv"></div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script>
		$('#print').linkbutton({    
		    iconCls: 'icon-save'   
		});
		(function(){
			var txtVal = $('#id').val();
			if (!txtVal) {
				 $('#print').linkbutton('disable');
			}else{
				$('#print').linkbutton('enable');
			}
		})();
		var selsctage="";//选择的出生日期时算出年龄
		//默认加载
		$(function(){
			//初始化下拉框
			if($('#diseaseType').val()=='8'){
				$('#otherDisease').combobox({
					valueField: 'id',
					textField: 'text',
					data:[{"id":1,"text":"尖锐湿疣"},{"id":2,"text":"生殖器疱疹"},{"id":3,"text":"生殖道衣原体感染"}],
					width:200,
					multiple:false,
					disabled:false
				});
				if($('#otherDisease').val()=='1'||$('#otherDisease').val()=='2'){
					var CodeYbly="${infectious.sampleSource}";
					var CodeDegree="${infectious.patientEducation}";
					var infectHistory="${infectious.infectHistory}";
					var CodeGrtj="${infectious.infectSource}";
					var CodeMarry="${infectious.patientMari}";
					var caseClass1="${infectious.caseClass1}";
					$('#CodeMarry').combobox({
							value:CodeMarry,
						  	 disabled:false
					});
					$('#CodeDegree').combobox({
							value:CodeDegree,
						  	 disabled:false
					});
					$('#infectHistory').combobox({
							value:infectHistory,
						  	 disabled:false
					});
					
					$('#CodeYbly').combobox({
							value:CodeYbly,
						  	 disabled:false
					});
					//感染途径下拉框
					$('#CodeGrtj').combobox({
						url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=grtj'/>",
						valueField:'encode',    
						textField:'name',
						multiple:false,
						value:CodeGrtj,
						disabled:false,
						onChange: function(node){
							var texts=$('#CodeGrtj').combobox('getText');
							if (texts.indexOf("其他")>=0) {
								$('#otherSource').textbox({ 
								    disabled:false
								});
							}else{
								$('#otherSource').textbox({ 
								    disabled:true
								});
								$('#otherSource').textbox('setValue','');
							}
						},
						onLoadSuccess: function(node){
							var texts=$('#CodeGrtj').combobox('getText');
							if (texts.indexOf("其他")>=0) {
								$('#otherSource').textbox({ 
								    disabled:false
								});
							}else{
								$('#otherSource').textbox({ 
								    disabled:true
								});
							}
						}
					});
					if($('#CodeGrtj').combobox('getText').indexOf("其他")>=0){
						$('#otherSource').textbox({ 
						    disabled:false
						});
					}else{
						$('#otherSource').textbox({ 
							disabled:true
						});
					}
					//接触史
					$('#infectHistory').combobox({
						valueField:'encode',    
						textField:'name',
						url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=jcs'/>",
						width:200,
						value:infectHistory,
						disabled:false,
						onChange: function(node){
						var texts=$('#infectHistory').combobox('getText');
							if (texts.indexOf("其他")>=0) {
								$('#otherInfect').textbox({ 
								    disabled:false
								});
							}else{
								$('#otherInfect').textbox({ 
								    disabled:true
								});
								$('#otherInfect').textbox('setValue','');
							}
						},
						onLoadSuccess: function(node){
							var texts=$('#infectHistory').combobox('getText');
							if (texts.indexOf("其他")>=0) {
								$('#otherInfect').textbox({ 
								    disabled:false
								});
							}else{
								$('#otherInfect').textbox({ 
								    disabled:true
								});
							}
						} 
					});
					
					if($('#infectHistory').combobox('getText').indexOf("其他")>=0){
						$('#otherInfect').textbox({ 
							disabled:false
						});
					}else{
						$('#otherInfect').textbox({ 
							disabled:true
						});
					}
					//样本来源
					$('#CodeYbly').combobox({
						url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=ybly'/>", 
						valueField:'encode',    
						textField:'name',
						multiple:false,
						value:CodeYbly,
						disabled:false,
						onChange: function(node){
							var texts=$('#CodeYbly').combobox('getText');
								if (texts.indexOf("其他")>=0) {
									$('#otherSample').textbox({ 
									    disabled:false
									});
								}else{
									$('#otherSample').textbox({ 
									    disabled:true
									});
									$('#otherSample').textbox('setValue','');
								}
						},
						onLoadSuccess: function(node){
							var texts=$('#CodeYbly').combobox('getText');
							if (texts.indexOf("其他")>=0) {
								$('#otherSample').textbox({ 
								    disabled:false
								});
							}else{
								$('#otherSample').textbox({ 
								    disabled:true
								});
							}
						}
					});
					if($('#CodeYbly').combobox('getText').indexOf("其他")>=0){
						$('#otherSample').textbox({ 
								disabled:false
						});
					}else{
						$('#otherSample').textbox({ 
							disabled:true
						});
					}
				}
				$('#caseClass1').combotree({
					data:[{"id":1,"text":"疑似病例"},{"id":2,"text":"临床诊断"}],
					multiple:false,
					width:200,
					value:caseClass1,
					disabled:false
				});
			}else{
				$('#otherDisease').combobox({
						valueField: 'id',
						textField: 'text',
						data:[{"id":1,"text":"尖锐湿疣"},{"id":2,"text":"生殖器疱疹"},{"id":3,"text":"生殖道衣原体感染"}],
						width:200,
						multiple:false,
						disabled:true   
				});
			}
			if($('#diseaseType').val()=='232'){
				var caseClass2s="${infectious.caseClass2}";
				$('#caseClass2').combobox({
					valueField: 'id',
					textField: 'text',
					data:[{"id":1,"text":"急性"},{"id":2,"text":"慢性"}],
					width:200,
					value:caseClass2s,
					disabled:false
				});
				var rps="${infectious.rps}";
				var liverbiopsyResult="${infectious.liverbiopsyResult}";
				var againstHbc="${infectious.againstHbc}";
				var reportHbsag="${infectious.reportHbsag}";
				var alt="${infectious.alt}";
				var firsthbDates="${infectious.firsthbDate}";
				$('#firsthbDate').val(firsthbDates);
				$('#reportHbsag').combobox({
						value:reportHbsag,
						disabled:false
				});
				$('#againstHbc').combobox({
						value:againstHbc,
						disabled:false
				});
				$('#liverbiopsyResult').combobox({
						value:liverbiopsyResult,
						disabled:false
				});
				$('#rps').combobox({
						value:rps,
						disabled:false
				});
				$('#alt').textbox({ 
					disabled:false
				});
			}else{
				$('#reportHbsag').combobox({
						disabled:true
				});
				$('#againstHbc').combobox({
						disabled:true
				});
				$('#liverbiopsyResult').combobox({
						disabled:true
				});
				$('#rps').combobox({
						disabled:true
				});
				$('#alt').textbox({ 
					disabled:true
				});
			}
			
			bindEnterEvent('approveOper',adduserRole); 
			
			queryDistrictSJLDOne();
			queryDistrictSJLDTwo('');
			queryDistrictSJLDThree('');
			queryDistrictSJLDFour('',false);	
			
			//如果是修改页面,进入的时候,让三级联动内每个输入框都可以下拉出值
			var oneCode=$('#homeones').combobox('getValue');
			queryDistrictSJLDTwo(oneCode);
			var twoCode=$('#hometwos').combobox('getValue');
			queryDistrictSJLDThree(twoCode);
			var threeCode=$('#homethrees').combobox('getValue');
			var fourCode=$('#homefours').combobox('getValue');
			if(fourCode==null||fourCode==""){
				$('#homefours').combobox({
					disabled:true
				});
			}else{
				queryDistrictSJLDFour(threeCode,true);
			}
			
		});
		
		 /**
		* 绑定现住址回车事件
		* @author  zhuxiaolu
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/
		bindEnterEvent('medicalrecord',searchFromcx); 
		bindEnterEvent('medicalSex',searchFrom); 
		
		
		 function searchFrom(){
			var medicalSex = $('#medicalSex').val();
			$('#infoDatagrid').datagrid('load', {    
				medicalSex: medicalSex  
			});
		}
		//接触史
		if($('#diseaseType').val()=='8'){
		 $('#otherDisease').combobox({
				valueField: 'id',
				textField: 'text',
				data:[{"id":1,"text":"尖锐湿疣"},{"id":2,"text":"生殖器疱疹"},{"id":3,"text":"生殖道衣原体感染"}],
				width:200,
				multiple:false,
				disabled:false,
				onChange: function(node){
				var textss=$('#otherDisease').combobox('getText');
					if (textss=="尖锐湿疣"||textss=="生殖器疱疹") {
						var CodeYbly="${infectious.sampleSource}";
						var CodeDegree="${infectious.patientEducation}";
						var infectHistory="${infectious.infectHistory}";
						var CodeGrtj="${infectious.infectSource}";
						var CodeMarry="${infectious.patientMari}";
						var caseClass1="${infectious.caseClass1}";
						$('#CodeMarry').combobox({
								value:CodeMarry,
								disabled:false
						});
						$('#CodeDegree').combobox({
								value:CodeDegree,
								disabled:false
						});
						$('#infectHistory').combobox({
								value:infectHistory,
								disabled:false
						});
						
						$('#CodeYbly').combobox({
								value:CodeYbly,
								disabled:false
						});
						//感染途径下拉框
						$('#CodeGrtj').combobox({
							url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=grtj'/>",   
							valueField:'encode',    
							textField:'name',
							multiple:false,
							value:CodeGrtj,
							disabled:false
						});
						if($('#CodeGrtj').combobox('getText').indexOf("其他")>=0){
							$('#otherSource').textbox({ 
								disabled:false
							});
						}else{
							$('#otherSource').textbox({ 
								disabled:true
							});
						}
						//接触史
						$('#infectHistory').combobox({
							valueField:'encode',    
							textField:'name',
							url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=jcs'/>",  
							width:200,
							value:infectHistory,
							disabled:false,
							onChange: function(node){
							var texts=$('#infectHistory').combobox('getText');
								if (texts.indexOf("其他")>=0) {
									$('#otherInfect').textbox({disabled:false});
								}else{
									$('#otherInfect').textbox({disabled:true});
								}
							 }
						});
						if($('#infectHistory').combobox('getText').indexOf("其他")>=0){
							$('#otherInfect').textbox({ 
								disabled:false
							});
						}else{
							$('#otherInfect').textbox({ 
								disabled:true
							});
						}
							
						$('#CodeYbly').combobox({
							url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=ybly'/>",
							valueField:'encode',    
							textField:'name',
							multiple:false,
							value:CodeYbly,
							disabled:false  
						});
						if($('#CodeYbly').combobox('getText').indexOf("其他")>=0){
							$('#otherSample').textbox({ 
								disabled:false
							});
						}else{
							$('#otherSample').textbox({ 
								disabled:true
							});
						}
					}else{
						var caseClass1="${infectious.caseClass1}";
						if (caseClass1=="实验室确诊病例"||caseClass1=="病原携带者") {
							$('#caseClass1').combotree({
								data:[{"id":3,"text":"实验室确诊病例"},{"id":4,"text":"病原携带者"}],
								multiple:false,
								width:200,
								disabled:false
							});
						}else {
							$('#caseClass1').combotree({
								data:[{"id":3,"text":"实验室确诊病例"},{"id":4,"text":"病原携带者"}],
								multiple:false,
								width:200,
								disabled:false
							});
						}
						$('#infectHistory').textbox({ 
							disabled:true
						});
						$('#infectHistory').textbox('setValue','');
						$('#CodeGrtj').textbox({ 
							disabled:true
						});
						$('#CodeGrtj').textbox('setValue','');
						$('#CodeYbly').textbox({ 
							disabled:true
						});
						$('#CodeYbly').textbox('setValue','');
						$('#CodeDegree').textbox({ 
							disabled:true
						});
						$('#CodeDegree').textbox('setValue','');
						
						$('#CodeMarry').textbox({ 
							disabled:true
						});
						$('#CodeMarry').textbox('setValue','');
						$('#otherInfect').textbox({ 
							disabled:true
						});
						$('#otherInfect').textbox('setValue','');
						$('#otherSource').textbox({ 
							disabled:true
						});
						$('#otherSource').textbox('setValue','');
						$('#otherSample').textbox({ 
							disabled:true
						});
						$('#otherSample').textbox('setValue','');
					}
				}
			});	
		}else if($('#diseaseType').val()=='232'){
			var alt="${infectious.alt}";
			$('#reportHbsag').combobox({
				valueField: 'id',
				textField: 'text',
				data:[{"id":1,"text":"大于6个月"},{"id":2,"text":"6个月内由阴性转为阳性"},{"id":3,"text":"既往未检测或结果不详"}],
				width:200,
				onChange: function(node){
					var texts=$('#reportHbsag').combobox('getValue');
					document.getElementById("reportHbsags").value=texts;
				}
			});
			$('#againstHbc').combobox({
				valueField: 'id',
				textField: 'text',
				data:[{"id":1,"text":"阴性"},{"id":2,"text":"阳性"},{"id":3,"text":"未测"}],
				width:200,
				onChange: function(node){
					var texts=$('#againstHbc').combobox('getValue');
					document.getElementById("againstHbcs").value=texts;
				}
			});
			$('#liverbiopsyResult').combobox({
				valueField: 'id',
				textField: 'text',
				data:[{"id":1,"text":"急性"},{"id":2,"text":"慢性"},{"id":3,"text":"未测"}],
				width:200,
				multiple:false,
				onChange: function(node){
					var texts=$('#liverbiopsyResult').combobox('getValue');
					document.getElementById("liverbiopsyResults").value=texts;
				}
			});
			$('#rps').combobox({
				valueField: 'id',
				textField: 'text',
				data:[{"id":1,"text":"是"},{"id":2,"text":"否"}],
				multiple:false,
				onChange: function(node){
					var texts=$('#rps').combobox('getValue');
					document.getElementById("rpss").value=texts;
				}
			});		
			$('#alt').textbox({disabled:false});
			$('#alt').textbox('setValue',alt);	
		}else{
			$('#infectHistory').textbox({ 
				disabled:true
			});
			$('#infectHistory').textbox('setValue','');
			$('#CodeGrtj').textbox({disabled:true});
			$('#CodeGrtj').textbox('setValue','');
			$('#CodeYbly').textbox({disabled:true});
			$('#CodeYbly').textbox('setValue','');
			$('#CodeDegree').textbox({disabled:true});
			$('#CodeDegree').textbox('setValue','');
			$('#CodeMarry').textbox({disabled:true});
			$('#CodeMarry').textbox('setValue','');
			$('#otherInfect').textbox({disabled:true});
			$('#otherInfect').textbox('setValue','');
			$('#otherSource').textbox({disabled:true});
			$('#otherSource').textbox('setValue','');
			$('#otherSample').textbox({disabled:true});
			$('#otherSample').textbox('setValue','');
			$('#alt').textbox({disabled:true});
			$('#alt').textbox('setValue','');
		}
		//证件类型
		$('#CodeCertificate') .combobox({ 
			url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=certificate'/>",
			valueField:'encode',    
			textField:'name',
			multiple:false,
			editable:false,
			onChange:function(post){
				$('#CodeCertificate').combobox('reload', "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=certificate'/>");
			}
		});
		//感染途径
		$('#CodeGrtj') .combobox({ 
			url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=grtj'/>",     
		    valueField:'id',    
		    textField:'name',
		    multiple:false,
		    editable:false,
		    onChange: function(node){
	    	var texts=$('#CodeGrtj').combobox('getText');
				if (texts.indexOf("其他")>=0) {
					$('#otherSource').textbox({ 
					    disabled:false
					});
				}else{
					$('#otherSource').textbox({ 
						disabled:true
					});
					$('#otherSource').textbox('setValue','');
				}
				$('#CodeGrtj').combobox('reload', "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=grtj'/>");
			}
		    
		});
		//接触史
		$('#infectHistory') .combobox({ 
			url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=jcs'/>",   
			valueField:'encode',    
			textField:'name',
			multiple:false,
			editable:false,
			onChange:function(post){
			var texts=$('#infectHistory').combobox('getText');
				if (texts.indexOf("其他")>=0) {
					$('#otherInfect').textbox({ 
						disabled:false
					});
				}else{
					$('#otherInfect').textbox({ 
						disabled:true
					});
					$('#otherInfect').textbox('setValue','');
				}
	    		$('#infectHistory').combobox('reload', "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=jcs'/>");
	    	}
		    
		});
		//样本来源
		$('#CodeYbly') .combobox({ 
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=ybly'/>",
			valueField:'id',    
			textField:'name',
			multiple:false,
			editable:false,
			onChange: function(node){
			var texts=$('#CodeYbly').combobox('getText');
				if (texts.indexOf("其他")>=0) {
					$('#otherSample').textbox({ 
					    disabled:false
					});
				}else{
					$('#otherSample').textbox({ 
					    disabled:true
					});
					$('#otherSample').textbox('setValue','');
				}
				$('#CodeYbly').combobox('reload', "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=ybly'/>");
			}
		});
		//病人来源 brlydq
		$('#CodeBrlydq') .combobox({ 
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=brlydq'/>",     
			valueField:'encode',    
			textField:'name',
			multiple:false,
			editable:false,
		});
		//婚姻情况
		$('#CodeMarry') .combobox({ 
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=marry'/>" ,   
			valueField:'encode',    
			textField:'name',
			multiple:false,
			editable:false,
			onChange:function(post){
				$('#CodeMarry').combobox('reload', "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=marry&encode='/>"+post);
			}
		});
		//文化程度
		$('#CodeDegree') .combobox({ 
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=degree'/>" ,   
			valueField:'id',    
			textField:'name',
			multiple:false,
			editable:false,
			onChange:function(post){
				$('#CodeDegree').combobox('reload', "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=degree'/>");
			}
		});
		//患者职业
		$('#patientProfession') .combobox({ 
			url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=occupation'/>" ,   
		    valueField:'encode',    
		    textField:'name',
		    onHidePanel:function(none){
		        var data = $(this).combobox('getData');
		        var val = $(this).combobox('getValue');
		        var result = true;
		        for (var i = 0; i < data.length; i++) {
		            if (val == data[i].encode) {
		                result = false;
		            }
		        }
		        if (result) {
		            $(this).combobox("clear");
		        }else{
		            $(this).combobox('unselect',val);
		            $(this).combobox('select',val);
		        }
		    },
		    filter: function(q, row){
		        var keys = new Array();
		        keys[keys.length] = 'encode';
		        keys[keys.length] = 'name';
		        keys[keys.length] = 'pinyin';
		        keys[keys.length] = 'wb';
		        keys[keys.length] = 'inputCode';
		        return filterLocalCombobox(q, row, keys);
		    }
		});
		//适用性别
		$('#CodeSex').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
			    valueField:'encode',    
			    textField:'name',
			    editable:false
			});
			var sexHid = $('#sexHid').val();
			if(sexHid!=null&&sexHid!=""){
				$('#CodeSex').combobox('setValue',sexHid);
			}
		
		/**
		 * 下拉框
		 * @author liuhl
		 * @date 2015-6-18
		 *
		 */
		$('#reportType').combotree({
				data:[{"id":1,"text":"初次报告"},{"id":2,"text":"订正报告","iconCls":"icon-book","state":"open","children":[{"id":3,"text":"变更诊断"},{"id":4,"text":"死亡"},{	"id":5,"text":"填卡错误"}]}],
				width:200,
				multiple:false,
				onChange: function(node){
	    			var texts=$('#reportType').combotree('getValue');
					document.getElementById("reportTypes").value=texts;
			}
		});
		$('#agedw').combotree({
				data:[{"id":1,"text":"岁"},{"id":2,"text":"月"},{"id":3,"text":"日"}],
				multiple:false,
				width:80
		});
	//疾病下拉树
	$('#diseaseType').combotree({
		valueField: 'id',
		textField: 'text',
			data:[{
				"id":1,
				"text":"甲类传染病",
					"children":[{
						"id":11,
						"text":"鼠疫"
						},{
						"id":12,
						"text":"霍乱"
					}]
				},{
				"id":2,
				"text":"乙类传染病",
				"children":[{
						"id":21,
						"text":"传染性非典型肺炎"
						},{
						"id":22,
						"text":"艾滋病",
						"children":[{
							"id":221,
							"text":"艾滋病"
							},{
							"id":222,
							"text":"HIV阳性"
						}]
						},{
						"id":23,
						"text":"病毒性肝炎",
						"children":[{
							"id":231,
							"text":"甲型"
							},{
							"id":232,
							"text":"乙型"
							},{
							"id":233,
			                "text":"丙型"
			                },{
			                "id":234,
			                "text":"丁型"
			                },{
			                "id":235,
			                "text":"戍型"
			                },{
			                "id":236,
			                "text":"未分类"
			                }]
			           },{
			           "id":24,
			           "text":"脊髓灰质炎"
			           },{
			           "id":25,
			           "text":"人感染高致病性禽流感"
			           },{
			           "id":26,
			           "text":"麻疹"
			           },{
			           "id":27,
			           "text":"流行性出血热"
			           },{
			           "id":28,
			           "text":"狂犬病"
			           },{
			           "id":29,
			           "text":"流行性乙型脑炎"
			           },{
			           "id":30,
			           "text":"登革热、炭疽",
			           "children":[{
			             "id":301,
			             "text":"肺炭疽"
			             },{
			             "id":302,"text":"皮肤炭疽"
			             },{
			             "id":303,"text":"未分类"
			             }]
			             },{
			        "id":31,
			        "text":"痢疾",
			        "children":[{
			             "id":311,
			             "text":"细菌性"
			             },{
			             "id":312,
			             "text":"阿米巴性"
			             }]
			       },{
			      "id":32,
			      "text":"肺结核",
			      "children":[{
			          "id":321,
			          "text":"涂阳"
			          },{
			          "id":322,
			          "text":"仅培阳"
			          },{
			          "id":323,
			          "text":"菌阴"
			          },{
			          "id":324,
			          "text":"未痰检"
			          }]
			     },{
			     "id":33,
			     "text":"伤寒",
				     "children":[{
				     "id":331,
				     "text":"伤寒"
				     },{
				     "id":332,
				     "text":"副伤寒"
				     }]
			     },{
			     "id":34,
			     "text":"流行性脑脊髓膜炎"
			     },{
			     "id":35,
			     "text":"百日咳"
			     },{
			     "id":36,
			     "text":"白喉"
			     },{
			     "id":37,
			     "text":"新生儿破伤风"
			     },{
			     "id":38,
			     "text":"猩红热"
			     },{
			     "id":39,
			     "text":"布鲁氏菌病"
			     },{
			     "id":40,
			     "text":"淋病、梅毒",
			     "children":[{
			      	"id":401,
			        "text":"Ⅰ期"
			        },{
			        "id":402,
			        "text":"Ⅱ期"
			        },{
			        "id":403,
			        "text":"Ⅲ期"
			        },{
			        "id":404,
			        "text":"胎传"
			        },{
			        "id":405,
			        "text":"隐性"
			        }]
			     },{
			     "id":41,
			     "text":"钩端螺旋体病"
			     },{
			     "id":42,
			     "text":"血吸虫病"
			     },{
			     "id":43,
			     "text":"疟疾",
			     "children":[{
			        "id":431,
			        "text":"间日疟"
			        },{
			        "id":432,
			        "text":"恶性疟"
			        },{
			        "id":433,
			        "text":"未分型"}]
			        },{
			        "id":434,
			        "text":"人感染H7N9禽流感"
			        }]
			  },{
			  "id":5,
			  "text":"丙类类传染病",
			  "children":[ {
			        "id":51,
			        "text":"流行性感冒"
			        },{
			        "id":52,
			        "text":"流行性腮腺炎"
			        },{
			        "id":53,
			        "text":"风疹"
			        } ,{
			        "id":54,
			        "text":"急性出血性结膜炎"
			        },{
			        "id":55,
			        "text":"麻风病"
			        },{
			        "id":56,
			        "text":"流行性和地方性斑疹伤寒"
			        },{
			        "id":57,
			        "text":"黑热病"
			        },{
			        "id":58,
			        "text":"包虫病"
			        },{
			        "id":59,
			        "text":"丝虫病"
			        },{
			        "id":60,
			        "text":"除霍乱、细菌性和阿米巴性痢疾、伤寒和副伤寒以外的感染性腹泻病"
			        },{
			        "id":61,
			        "text":"手足口病"
			        }]
			 },{
			 "id":7,
			 "text":"其他法定管理以及重点监测传染病",
			 "children":[{
			     "id":71,
			     "text":"不明原因肺炎"
			     },{
			     "id":72,
			     "text":"AFP"
			     },{
			     "id":73,
			     "text":"水痘"
			     },{
			     "id":74,
			     "text":"人粒细胞无形体病"
			     },{
			     "id":75,
			     "text":"发热伴血小板减少综合症"
			     },{
			     "id":76,
			     "text":"肝吸虫病"
			     },{
			     "id":77,
			     "text":"恙虫病"
			     }]
			   },{
			   "id":8,
			   "text":"其他"
			   }],
							
						width:200,
						multiple:false,
						onSelect: function(node){
			    	    var nodep = $('#diseaseType').combotree('tree').tree('getSelected');
			    	    var texts=nodep.text;
			    	    var text = nodep.text;
			    	    $('#diseaseTypename').val(texts);
			    	    $('#otherDiseasename').val(text);
						if (texts.indexOf("其他")>=0) {
									$('#otherDisease').combobox({
									valueField: 'id',
									textField: 'text',
									data:[{"id":1,"text":"尖锐湿疣"},{"id":2,"text":"生殖器疱疹"},{"id":3,"text":"生殖道衣原体感染"}],
									width:200,
									multiple:false,
									disabled:false,
									onChange: function(node){
										var textss=$('#otherDisease').combobox('getText');
									if (textss=="尖锐湿疣"||textss=="生殖器疱疹") {
											var CodeYbly="${infectious.sampleSource}";
											var CodeDegree="${infectious.patientEducation}";
											var infectHistory="${infectious.infectHistory}";
											var CodeGrtj="${infectious.infectSource}";
											var CodeMarry="${infectious.patientMari}";
											var caseClass1="${infectious.caseClass1}";
											if (caseClass1=="疑似病例"||caseClass1=="临床诊断") {
												$('#caseClass1').combotree({
													data:[{"id":1,"text":"疑似病例"},{"id":2,"text":"临床诊断"}],
													multiple:false,
													width:200,
													value:caseClass1,
													disabled:false
												});
											}else {
												$('#caseClass1').combotree({
													data:[{"id":1,"text":"疑似病例"},{"id":2,"text":"临床诊断"}],
													multiple:false,
													width:200,
													disabled:false
												});
											}
											$('#CodeMarry').combobox({
													value:CodeMarry,
												  	 disabled:false
											});
											$('#CodeDegree').combobox({
													value:CodeDegree,
												  	 disabled:false
											});
											$('#infectHistory').combobox({
													value:infectHistory,
												  	 disabled:false
											});
											$('#CodeGrtj').combobox({
													value:CodeGrtj,
												  	 disabled:false
											});
											$('#CodeYbly').combobox({
													value:CodeYbly,
												  	 disabled:false
											});
										
											$('#CodeGrtj').combobox({
												url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=grtj'/>",
											    valueField:'encode',    
											    textField:'name',
											    multiple:false,
										    	onChange: function(node){
										    	var texts=$('#CodeGrtj').combobox('getText');
													if (texts.indexOf("其他")>=0) {
														$('#otherSource').textbox({ 
														    disabled:false
														});
													}else{
														$('#otherSource').textbox({ 
															disabled:true
														});
													}
												}
											});
											//接触史
											$('#infectHistory').combobox({
											valueField:'encode',    
											textField:'name',
											url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=jcs'/>",  
											width:200,
											onChange: function(node){
											var texts=$('#infectHistory').combobox('getText');
												if (texts.indexOf("其他")>=0) {
													$('#otherInfect').textbox({ 
													    disabled:false
													});
												}else{
													$('#otherInfect').textbox({ 
													    disabled:true
													});
												}
												}
											});
											//样本来源	
											$('#CodeYbly').combobox({
												url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=ybly'/>",    
												valueField:'encode',    
												textField:'name',
												multiple:false,
												onChange: function(node){
												var texts=$('#CodeYbly').combobox('getText');
													if (texts.indexOf("其他")>=0) {
														$('#otherSample').textbox({ 
														    disabled:false
														});
													}else{
														$('#otherSample').textbox({ 
														    disabled:true
														});
													}
												}
											});
									}else if (textss=="生殖道衣原体感染") {
										var caseClass1="${infectious.caseClass1}";
										if (caseClass1=="实验室确诊病例"||caseClass1=="病原携带者") {
											$('#caseClass1').combotree({
												data:[{"id":3,"text":"实验室确诊病例"},{"id":4,"text":"病原携带者"}],
												multiple:false,
												width:200,
												disabled:false
											});
										}else {
											$('#caseClass1').combotree({
												data:[{"id":3,"text":"实验室确诊病例"},{"id":4,"text":"病原携带者"}],
												multiple:false,
												width:200,
												disabled:false
											});
										}
										  $('#infectHistory').textbox({ 
											 disabled:true
										      });
											$('#infectHistory').textbox('setValue','');
											$('#CodeGrtj').textbox({ 
											 disabled:true
										      });
											$('#CodeGrtj').textbox('setValue','');
											$('#CodeYbly').textbox({ 
											 disabled:true
										      });
											$('#CodeYbly').textbox('setValue','');
											$('#CodeDegree').textbox({ 
											 disabled:true
										      });
											$('#CodeDegree').textbox('setValue','');
											
											$('#CodeMarry').textbox({ 
											 disabled:true
										      });
											$('#CodeMarry').textbox('setValue','');
											$('#otherInfect').textbox({ 
											disabled:true
										      });
											$('#otherInfect').textbox('setValue','');
											 $('#otherSource').textbox({ 
															disabled:true
														});
											$('#otherSource').textbox('setValue','');
											 $('#otherSample').textbox({ 
															disabled:true
														});
											$('#otherSample').textbox('setValue','');
										        $('#alt').textbox({ 
															disabled:true
															});
												$('#alt').textbox('setValue','');
																	
									}else if (texts=="梅毒"||texts=="淋病") {
										var caseClass1="${infectious.caseClass1}";
										if (caseClass1=="实验室确诊病例"||caseClass1=="疑似病例") {
											$('#caseClass1').combotree({
												data:[{"id":3,"text":"实验室确诊病例"},{"id":4,"text":"疑似病例"}],
												multiple:false,
												width:200,
												disabled:false
											});
										}else {
											$('#caseClass1').combotree({
												data:[{"id":3,"text":"实验室确诊病例"},{"id":4,"text":"疑似病例"}],
												multiple:false,
												width:200,
												disabled:false
											});
										}
									}
								}
									});
						}else{
						 $('#otherInfect').textbox({ 
										disabled:true
									});
						$('#otherInfect').textbox('setValue','');
						 $('#otherSource').textbox({ 
										disabled:true
									});
						$('#otherSource').textbox('setValue','');
						 $('#otherSample').textbox({ 
										disabled:true
									});
						$('#otherSample').textbox('setValue','');
						}
						
						if (texts.indexOf("其他")<0) {
									var kong="";
									$('#otherDisease').combobox({
										value:kong,
									  	 disabled:true
										});
											
								$('#CodeMarry').combobox({    
								    required:true,
								    value:kong,
								    disabled:true
								});
								$('#CodeDegree').combobox({
										value:kong,
									  	 disabled:true
								});
								$('#infectHistory').combobox({
										value:kong,
									  	 disabled:true
								});
								$('#CodeGrtj').combobox({
										value:kong,
									  	 disabled:true
								});
								$('#CodeYbly').combobox({
										value:kong,
									  	 disabled:true
								});
								$('#caseClass1').combotree({
									value:kong,
									disabled:true
								});
								$('#otherInfect').val("");
								$("#otherInfect").attr("disabled","true");
								$('#otherSource').val("");
								$("#otherSource").attr("disabled","true");
								$('#otherSample').val("");
								$("#otherSample").attr("disabled","true");
						}
						if (texts=="乙型"||texts=="丙型"||texts=="血吸虫") {
								var caseClass2s=value="${infectious.caseClass2}";
								$('#caseClass2').combobox({
								valueField: 'id',
								textField: 'text',
								data:[{"id":1,"text":"急性"},{"id":2,"text":"慢性"}],
								width:200,
								value:caseClass2s,
								 disabled:false
										});
								var rps=document.getElementById("rps");
								rps.disabled=false;
								var rps="${infectious.rps}";
								var liverbiopsyResult="${infectious.liverbiopsyResult}";
								var againstHbc="${infectious.againstHbc}";
								var reportHbsag="${infectious.reportHbsag}";
								var alt="${infectious.alt}";
								var firsthbDates="${infectious.firsthbDate}";
								document.getElementById("alt").disabled=false;
								$('#firsthbDate').val(firsthbDates);
								$('#reportHbsag').combobox({
										value:reportHbsag,
									  	 disabled:false
								});
								$('#againstHbc').combobox({
										value:againstHbc,
									  	 disabled:false
								});
								$('#liverbiopsyResult').combobox({
										value:liverbiopsyResult,
									  	 disabled:false
								});
								$('#rps').combobox({
										value:rps,
									  	 disabled:false
								});
								$('#caseClass2').combobox({
										value:caseClass2s,
									  	 disabled:false
								});
								$('#alt').textbox({
									  	 disabled:false
								});
								$('#alt').textbox('setValue',alt);
								
								$('#reportHbsag').combobox({
								valueField: 'id',
								textField: 'text',
								data:[{"id":1,"text":"大于6个月"},{"id":2,"text":"6个月内由阴性转为阳性"},{"id":3,"text":"既往未检测或结果不详"}],
								width:200,
								onChange: function(node){
					    			var texts=$('#reportHbsag').combobox('getValue');
									document.getElementById("reportHbsags").value=texts;
									}
								});
								$('#againstHbc').combobox({
								valueField: 'id',
								textField: 'text',
								data:[{"id":1,"text":"阴性"},{"id":2,"text":"阳性"},{"id":3,"text":"未测"}],
								width:200,
							    onChange: function(node){
							    	var texts=$('#againstHbc').combobox('getValue');
									document.getElementById("againstHbcs").value=texts;
									}
								});
								$('#liverbiopsyResult').combobox({
									valueField: 'id',
									textField: 'text',
									data:[{"id":1,"text":"急性"},{"id":2,"text":"慢性"},{"id":3,"text":"未测"}],
									width:200,
									multiple:false,
									onChange: function(node){
							    	var texts=$('#liverbiopsyResult').combobox('getValue');
									document.getElementById("liverbiopsyResults").value=texts;
									}
								});
								$('#rps').combobox({
							  	valueField: 'id',
								textField: 'text',
								data:[{"id":1,"text":"是"},{"id":2,"text":"否"}],
							    multiple:false,
						    	onChange: function(node){
							    	var texts=$('#rps').combobox('getValue');
									document.getElementById("rpss").value=texts;
										}
							      	});
						}
						if (texts!="乙型"){
								var firsthbDates="";
								$('#firsthbDate').val('');//首次出现症状
								$('#reportHbsag').combobox({
										value:firsthbDates,
									  	 disabled:true
								});
								$('#againstHbc').combobox({
										value:firsthbDates,
									  	 disabled:true
								});
								$('#liverbiopsyResult').combobox({
										value:firsthbDates,
									  	 disabled:true
								});
								$('#rps').combobox({
										value:firsthbDates,
									  	 disabled:true
								});
								$('#caseClass2').combobox({
										value:firsthbDates,
									  	 disabled:true
								});
								$('#alt').textbox({
										value:firsthbDates,
									  	 disabled:true
								});
								$("#alt").val();
						}
						
					}
				});
				
				$('#patientProfession').combobox({
					 	url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=occupation'/>",  
						valueField:'encode',    
			    		textField:'name',
						width:200,
						onChange: function(node){
							var texts=$('#patientProfession').combobox('getText');
						if (texts=="其他") {
							$('#otherProfession').textbox({ 
								required: true, 
							    disabled:false
							}); 
						}else{
							$('#otherProfession').textbox({ 
								required: true, 
							    disabled:true
							}); 
							$('#otherProfession').textbox('setValue','');
						}
					},
					onLoadSuccess: function(node){
						var texts=$('#patientProfession').combobox('getText');
					if (texts=="其他") {
						$('#otherProfession').textbox({ 
							required: true, 
						    disabled:false
						}); 
					}else{
						$('#otherProfession').textbox({ 
							required: true, 
						    disabled:true
						}); 
					}
				}
				});
				if($('#patientProfession').combobox('getText')=="其他"){
			         $('#otherProfession').textbox({ 
								required: true, 
							    disabled:false
							}); 
			   
			   }else{
			            $('#otherProfession').textbox({ 
								required: true, 
							    disabled:true
							}); 
			   }
				$('#correctFlag').combobox({
						valueField: 'id',
						textField: 'text',
						data:[{"id":1,"text":"已订正"},{"id":2,"text":"未订正"}],
						width:200,
						onChange: function(node){
				    	var texts=$('#correctFlag').combobox('getValue');
						document.getElementById("correctFlags").value=texts;
					}
				});
			
	//表单提交submit信息
  	function submit(){
		//单独验证身份证号是否正确
  		if($('#CodeCertificate').combobox('getText').indexOf("身份证")>=0 && !isIdCardNo($("#certificatesNo").val())){
			return;
		}
  		onSelectDate();//验证死亡时间,入院时间,诊断时间
		var reportBirthday=$('#reportBirthday').val();
		if (reportBirthday == null || reportBirthday =="") {
			var reportAge=$('#reportAge').numberbox('getValue');
			var ageUnit = $('#agedw').combobox('setValue', '001');
			if (reportAge == null || reportAge == "") {
				$.messager.alert('提示','请填写年龄或出生日期');
				close_alert();
				return;
			}else if(ageUnit == null || ageUnit == ""){
				$.messager.alert('提示','请选择年龄单位');
				close_alert();
                return;
			}else{
				$('#infEditForm').form('submit',{
			  		url:"<c:url value='/publics/infectious/saveOrUpdateInfectious.action'/>",
			  		queryParams:{
			  			'bigDecimal':reportAge
			  		},
			  		 onSubmit:function(){ 
					     return $(this).form('validate');  
					 },  
					success:function(data){
						if(data=="success"){
							//实现刷新
							window.opener.reloads(); 
							$.messager.alert('提示','保存成功');
							setTimeout(function(){
								$(".messager-body").window('close');
								window.close();
							},2000);
					   }else {
						   $.messager.alert('提示','保存失败');
					  	}
					 },
					error:function(date){
						$.messager.alert('提示','保存失败');
					}
			  	});
			}
		}else {
			if (reportAge==""||reportAge==null) {
				$('#infEditForm').form('submit',{
			  		url:"<c:url value='/publics/infectious/saveOrUpdateInfectious.action'/>",
			  		onSubmit:function(){ 
					     return $(this).form('validate');  
					 }, 
					success:function(data){
						if(data=="success"){
							//实现刷新
							window.opener.reloads(); 
							$.messager.alert('提示','保存成功');
							setTimeout(function(){
								$(".messager-body").window('close');
								window.close();
							},2000);
					   }else {
						   $.messager.alert('提示','保存失败');
					  	}
					 },
					error:function(date){
						$.messager.alert('提示','保存失败');
					}
			  	});
			}else{
				$('#infEditForm').form('submit',{
			  		url:"<c:url value='/publics/infectious/saveOrUpdateInfectious.action'/>",
			  		onSubmit:function(){ 
					     return $(this).form('validate');  
					 }, 
					 queryParams:{
						 'bigDecimal':reportAge
					 },
					 onBeforeLoad:function(p){
					 },
					success:function(data){
						if(data=="success"){
							//实现刷新
							window.opener.reloads(); 
							$.messager.alert('提示','保存成功');
							setTimeout(function(){
								$(".messager-body").window('close');
								window.close();
							},2000);
					   }else {
						   $.messager.alert('提示','保存失败');
					  	}
					 },
					error:function(date){
						$.messager.alert('提示','保存失败');
					}
			  	});
			}
			
		}
  	}

	//清除所填信息
	function clear() {
		$('#infEditForm').form('clear');
	}
	function closeLayout() {
		window.close();
	}
	$.extend($.fn.validatebox.defaults.rules, {
		  phoneRex: {
		    validator: function(value){
		    var rex=/^1[3-9]+\d{9}$/;
		    //var rex=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
		    //区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}
		    //电话号码：7-8位数字： \d{7,8
		    //分机号：一般都是3位数字： \d{3,}
		     //这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/		 
		    var rex2=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
		    if(rex.test(value)||rex2.test(value))
		    {
		      return true;
		    }else
		    {
		       return false;
		    }
		      
		    },
		    message: '请输入正确电话或手机格式'
		  }
	});
	
	 function onSelectDate() {
        	var infect=$('#infectDate').val();
			var infectDate=getDate(infect);
			var dead=$('#deadDate').val();
			var deadDate=getDate(dead);
			var diagnosis=$('#diagnosisDate').val();
			var diagnosisDate=getDate(diagnosis);
			if(infect!=null && infect!="" && dead!=null && dead!=""){
	            if (deadDate < infectDate) {
	            	$.messager.alert('提示','发病日期应小于死亡日期');
	            	close_alert();
	            	$('#infectDate').focus();
	            	return;
	            }
			}
			if(diagnosis!=null && diagnosis!="" && dead!=null && dead!=""){
	            if (deadDate < diagnosisDate) {
	            	$.messager.alert('提示','诊断时间应小于死亡日期');
	            	close_alert();
	            	$('#diagnosisDate').focus();
	               	return;
	            }
			}
			var report=$('#reportDate').val();
			var reportDate=getDate(report);
			var cancel=$('#cancelDate').val();
			var cancelDate=getDate(cancel);
			if(report!=null && report!="" && cancel!=null && cancel!=""){
	            if (cancelDate < reportDate) {
	            	$.messager.alert('提示','报卡日期应小于作废时间');
	            	close_alert();
	            	$('#reportDate').focus();
	               	return;
	            }
			}
	  }
	  
           function searchFromcx(){
     		  var medicalrecord = $.trim($('#medicalrecord').val());
     			if(medicalrecord==null||$.trim(medicalrecord)==''){
     				$.messager.alert('提示','请正确录入病历号');
     				close_alert();
     				$("#medicalrecord").textbox('textbox').focus();
     				return;
     			}
     			$.ajax({
     				url : '<%=basePath%>publics/infectious/queryByMedicalrecord.action?medicalrecord='+medicalrecord,
     				type:'post',
     				success: function(data) {
     					var infectious=data;
     					if(infectious.length>1){
     						$("#diaInpatient").window('open');
     						$("#infoDatagrid").datagrid({
     							url:'<%=basePath%>publics/infectious/queryByMedicalrecord.action?medicalrecord='+medicalrecord,    
     						    columns:[[    
     						        {field:'medicalrecordId',title:'病历号',width:'30%',align:'center'} ,  
     						        {field:'patientSex',title:'性别',width:'15%',align:'center',formatter:function(value,row,index){
     						        	return sexMap.get(value);
							        }} ,
     						        {field:'patientName',title:'姓名',width:'15%',align:'center'} ,   
     						        {field:'patientCertificatesno',title:'证件号',width:'40%',align:'center'} 
     						    ]] ,
     						    onDblClickRow:function(rowIndex, rowData){
     						    	$('#patientNameId').textbox('setValue',rowData.patientName);
     						    	$('#medicalrecord').val(rowData.medicalrecordId);
     							 	$('#patientParents').textbox('setValue',rowData.patientMother);
     							 	$('#CodeSex').combobox('setValue',rowData.patientSex);
     							 	$('#CodeCertificate').combobox('setValue',rowData.patientCertificatestype);
     							 	$('#certificatesNo').textbox('setValue',rowData.patientCertificatesno);
     							 	$('#patientProfession').combobox('setValue',rowData.patientOccupation);
     							 	$('#reportAgeunit').combobox('setValue',rowData.reportAgeunit);
     							 	$('#workPlace').textbox('setValue',rowData.patientWorkunit);
     							 	$('#telephone').textbox('setValue',rowData.patientPhone);
     							 	$('#reportAge').textbox('setValue',rowData.reportAge);
     							    $('#reportBirthday').val(rowData.patientBirthday);
     							    $("#diaInpatient").window('close');
     						    }
     						});
     					}else if(infectious.length==1){
     						$('#patientNameId').textbox('setValue',infectious[0].patientName);
     					 	$('#patientParents').textbox('setValue',infectious[0].patientMother);
     					 	$('#CodeSex').combobox('setValue',infectious[0].patientSex);
     					 	$('#CodeCertificate').combobox('setValue',infectious[0].patientCertificatestype);
     					 	$('#certificatesNo').textbox('setValue',infectious[0].patientCertificatesno);
     					 	$('#patientProfession').combobox('setValue',infectious[0].patientOccupation);
     					 	$('#workPlace').textbox('setValue',infectious[0].patientWorkunit);
     					 	$('#telephone').textbox('setValue',infectious[0].patientPhone);
     					    $('#reportBirthday').val(infectious[0].patientBirthday);
     					}else{
     						$.messager.alert('提示','您输入的病历号有误');
     						close_alert();
     					    $('#patientNameId').textbox('setValue','');
     					 	$('#patientParents').textbox('setValue','');
     					 	$('#CodeSex').combobox('setValue','');
     					 	$('#CodeCertificate').combobox('setValue','');
     					 	$('#certificatesNo').textbox('setValue','');
     					 	$('#patientProfession').combobox('setValue','');
     					 	$('#workPlace').textbox('setValue','');
     					 	$('#telephone').textbox('setValue','');
     					    $('#reportBirthday').val('');
     					}
     				}
     			});
     		} 
	  
	  
 	/**
	 * 加载左侧
	 * @author  wj
	 * @date 2015-06-26
	 * @version 1.0
	 */
	function adduserRole(){
		AddDeptdilogs("选择审核人", "<c:url value='/publics/infectious/queryRoleUserInfections.action'/>?pid="+"1");
	}
	 function AddDeptdilogs(title, url) {
		$('#roleaddUserdiv').dialog({    
		    title: title,    
		    width: '30%',    
		    height:'100%',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
		}
	 
	 /**
	  * 职业回车弹出事件高丽恒
	  * 2016-03-22 18:41
	  */
	 bindEnterEvent('patientProfession',popWinToCodeOccupation,'easyui');
	function popWinToCodeOccupation(){
		var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?classNameTmp=CodeOccupation&textId=patientProfession&type=occupation";
		var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	/******** 三级联动 ************/
	function queryDistrictSJLDOne() {
			$('#homeones').combobox({  
				url: "<c:url value='/baseinfo/district/queryDistrictTreeOne.action'/>?parId=",
				valueField:'cityCode',    
			    textField:'cityName',
			    multiple:false,
			    editable:true,
			    onSelect:function(node) {
					queryDistrictSJLDTwo(node.cityCode);
					$('#hometwos').combobox('setValue','');
					$('#homethrees').combobox('setValue','');
					$('#homefours').combobox('setValue','');
		        },onHidePanel:function(none){
		            var data = $(this).combobox('getData');
		            var val = $(this).combobox('getValue');
		            var result = true;
		            for (var i = 0; i < data.length; i++) {
		                if (val == data[i].cityCode) {
		                    result = false;
		                }
		            }
		            if (result) {
		                $(this).combobox("clear");
		            }else{
		                $(this).combobox('unselect',val);
		                $(this).combobox('select',val);
		            }
		        },
		        filter: function(q, row){
		            var keys = new Array();
		            keys[keys.length] = 'cityCode';
		            keys[keys.length] = 'cityName';
		            keys[keys.length] = 'pinyin';
		            keys[keys.length] = 'wb';
		            keys[keys.length] = 'inputCode';
		            return filterLocalCombobox(q, row, keys);
		        }
			});
			bindEnterEvent('homeones',popWinToDistrict,'easyui');//绑定回车事件
		}
	function queryDistrictSJLDTwo(id) {
		if(id===''){
			$('#hometwos').combobox({
				data:[]
			})
			bindEnterEvent('hometwos',popWinToDistrictTwos,'easyui');//绑定回车事件
			return;
		}
		$('#hometwos').combobox({  
			url: "<c:url value='/baseinfo/district/queryDistrictTreeTwo.action'/>?parId="+id,    
				valueField:'cityCode',    
			    textField:'cityName',
			    required:true,
			    multiple:false,
			    editable:true,
			    onSelect:function(node) {
		        	queryDistrictSJLDThree(node.cityCode);
		        	$('#homethrees').combobox('setValue','');
		        	$('#homefours').combobox('setValue','');
		        },onHidePanel:function(none){
		            var data = $(this).combobox('getData');
		            var val = $(this).combobox('getValue');
		            var result = true;
		            for (var i = 0; i < data.length; i++) {
		                if (val == data[i].cityCode) {
		                    result = false;
		                }
		            }
		            if (result) {
		                $(this).combobox("clear");
		            }else{
		                $(this).combobox('unselect',val);
		                $(this).combobox('select',val);
		            }
		        },
		        filter: function(q, row){
		            var keys = new Array();
		            keys[keys.length] = 'cityCode';
		            keys[keys.length] = 'cityName';
		            keys[keys.length] = 'pinyin';
		            keys[keys.length] = 'wb';
		            keys[keys.length] = 'inputCode';
		            return filterLocalCombobox(q, row, keys);
		        }
		});
		bindEnterEvent('hometwos',popWinToDistrictTwos,'easyui');//绑定回车事件
	}
	function queryDistrictSJLDThree(id) {
		if(id===''){
			$('#homethrees').combobox({
				data:[]
			})
			bindEnterEvent('homethrees',popWinToDistrictThrees,'easyui');
			return;
		}
			$('#homethrees').combobox({  
				url: "<c:url value='/baseinfo/district/queryDistrictTreeThree.action'/>?parId="+id,    
					valueField:'cityCode',    
				    textField:'cityName',
				    required:true,
				    multiple:false,
				    editable:true,
				     onSelect:function(node) {
				     	var bool = false;
				     	if(node.cityName=="市辖区"){
				     		bool=true;
				     		$('#homefours').combobox({
								required: true,
								value:'',
								disabled:false
							});
				     	}else{
				     		$('#patientCitys').val(node.cityCode);
				     		$('#homefours').combobox({
								required: false,
								value:'',
								disabled:true
							});
				     	}				     	
			        	queryDistrictSJLDFour(node.cityCode,bool);
			        },onHidePanel:function(none){
			            var data = $(this).combobox('getData');
			            var val = $(this).combobox('getValue');
			            var result = true;
			            for (var i = 0; i < data.length; i++) {
			                if (val == data[i].cityCode) {
			                    result = false;
			                }
			            }
			            if (result) {
			                $(this).combobox("clear");
			            }else{
			                $(this).combobox('unselect',val);
			                $(this).combobox('select',val);
			            }
			        },
			        filter: function(q, row){
			            var keys = new Array();
			            keys[keys.length] = 'cityCode';
			            keys[keys.length] = 'cityName';
			            keys[keys.length] = 'pinyin';
			            keys[keys.length] = 'wb';
			            keys[keys.length] = 'inputCode';
			            return filterLocalCombobox(q, row, keys);
			        }
			});
			bindEnterEvent('homethrees',popWinToDistrictThrees,'easyui');
	}
	function queryDistrictSJLDFour(id,bool) {
		if(id===''){
			$('#homefours').combobox({
				data:[]
			})
			bindEnterEvent('homefours',popWinToDistrictFours,'easyui');
			return;
		}
			$('#homefours').combobox({  
				url: "<c:url value='/baseinfo/district/queryDistrictTreeFour.action'/>?parId="+id,    
				valueField:'cityCode',    
				    textField:'cityName',
				    required:bool,   
				    multiple:false,
				    editable:true,
				    onSelect:function(node) {
				    	$('#patientCitys').val(node.cityCode);
				    },onHidePanel:function(none){
			            var data = $(this).combobox('getData');
			            var val = $(this).combobox('getValue');
			            var result = true;
			            for (var i = 0; i < data.length; i++) {
			                if (val == data[i].cityCode) {
			                    result = false;
			                }
			            }
			            if (result) {
			                $(this).combobox("clear");
			            }else{
			                $(this).combobox('unselect',val);
			                $(this).combobox('select',val);
			            }
			        },
			        filter: function(q, row){
			            var keys = new Array();
			            keys[keys.length] = 'cityCode';
			            keys[keys.length] = 'cityName';
			            keys[keys.length] = 'pinyin';
			            keys[keys.length] = 'wb';
			            keys[keys.length] = 'inputCode';
			            return filterLocalCombobox(q, row, keys);
			        }
			});
	bindEnterEvent('homefours',popWinToDistrictFours,'easyui');
	}
	/**
	* 回车弹出地址一级选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToDistrict(){
		$('#hometwos').textbox('setValue','');
		$('#homethrees').textbox('setValue','');
		$('#homefours').textbox('setValue','');
		var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homeones&level=1&parentId=1";
		window.open (tempWinPath,'newwindowSJLD',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 

	+',scrollbars,resizable=yes,toolbar=yes')
	}
	/**
	* 回车弹出地址二级选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToDistrictTwos(){
		$('#homethrees').textbox('setValue','');
		$('#homefours').textbox('setValue','');
		var parentId=$('#homeones').textbox('getValue');
		if(!parentId){
			$.messager.alert('提示','请选择省/直辖市');  
			close_alert();
			
		}else{
			var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=hometwos&level=2&parentId="+parentId;
			window.open (tempWinPath,'newwindowSJLD',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
			+',scrollbars,resizable=yes,toolbar=yes')
		}
	}
	/**
	* 回车弹出地址三级选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToDistrictThrees(){
		$('#homefours').textbox('setValue','');
		var parentId=$('#hometwos').textbox('getValue');
		if(!parentId){
			$.messager.alert('提示','请选择市');  
			close_alert();
			
		}else{
			var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homethrees&level=3&parentId="+parentId;
			window.open (tempWinPath,'newwindowSJLD',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
			+',scrollbars,resizable=yes,toolbar=yes')
		}
	}
	/**
	* 回车弹出地址四级选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToDistrictFours(){
		var parentId=$('#homethrees').textbox('getValue');
		if(!parentId){
			$.messager.alert('提示','请选择县');  
			close_alert();
			
		}else{
			var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homefours&level=4&parentId="+parentId;
			window.open (tempWinPath,'newwindowSJLD',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
			+',scrollbars,resizable=yes,toolbar=yes')
		}
	}
	
	function changeAge(){
			 var dateStr=$('#reportBirthday').val();
			 var date=getDate(dateStr);
	    	 var now = new Date();
		     var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
		     var agess=now.getFullYear()-date.getFullYear();
		     selsctage=agess;
		     if (agess<14) {
		    	 $('#patientParents').textbox({    
		    		    required: true    
		    		});
			}else {
				$('#patientParents').textbox({    
	    		    required: false    
	    		});
			}
	}
	//字符串转日期格式，strDate要转为日期格式的字符串
	function getDate(strDate){
		  var date = eval('new Date(' + strDate.replace(/\d+(?=-[^-]+$)/, 
		   function (a) { return parseInt(a, 10) - 1; }).match(/\d+/g) + ')');
		  return date;
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>