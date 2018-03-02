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
	<div id="listeidt" style="height:100%;border: none;">
		<div style="padding: 5px 5px 0px 5px;border: none;">
			<form id="editForm" method="post">
				<div>
					<h5 style="padding-left: 45%;font-size: 28px">患者病案首页</h5>
				</div>
				<input type="hidden" id="id" name="emrFirst.id" value="${emrFirst.id }">
				<input type="hidden" id="patId" name="emrFirst.patId" value="${emrFirst.patId }">
				<input type="hidden" id="inpatientNo" name="emrFirst.inpatientNo" value="${emrFirst.inpatientNo }">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="honry-lable"  style="font-size: 14;width:150px">
						患者姓名：
						</td>
						<td style="font-size: 14;width:150px">
							<input class="easyui-textbox" name="emrFirst.name" id="name" style="width:200px" value="${emrFirst.name}"/>
						</td>
						<td  class="honry-lable" style="font-size: 14">
							医疗保险号：
						</td>
						<td style="font-size: 14">
							<input class="easyui-textbox" name="emrFirst.siNo" id="siNo" style="width:200px" value="${emrFirst.siNo}"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable"  style="font-size: 14">
							出生日期：
						</td>
						<td style="font-size: 14">
							<input id="birth" class="Wdate" name="emrFirst.birth" type="text" value="${emrFirst.birth}" onClick="WdatePicker()" style="width:200px"/>
<%-- 							<input class="easyui-datebox" name="emrFirst.birth" id="birth" style="width:200px"value="${emrFirst.birth}"/> --%>
						</td>
						<td  class="honry-lable"  style="font-size: 14">
							身份证号：
						</td>
						<td style="font-size: 14" >
							<input class="easyui-textbox" name="emrFirst.idNo"  id="idNo"
								style="width:200px"value="${emrFirst.idNo}" data-options="validType:'idEntityCard'"/>
						</td>
					</tr>
					<tr>
						<td  class="honry-lable"  style="font-size: 14">
							性&nbsp;别：
						</td>
						<td style="font-size: 14">
							<input class="easyui-combobox" style="width:200px" id="sex" name="emrFirst.sex" value="${emrFirst.sex}"/>
						</td>
						<td class="honry-lable" style="font-size: 14">
							婚姻状况：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" id="marry" name="emrFirst.marriage" value="${emrFirst.marriage}"/>
						</td>
					</tr>
					<tr>
						<td  class="honry-lable"  style="font-size: 14">
							民&nbsp;族：
						</td>
						<td style="font-size: 14">
							<input id="folk" name="emrFirst.folk" value="${emrFirst.folk}" />
							<a href="javascript:delSelectedData('folk');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable" style="font-size: 14">
							国&nbsp;籍：
						</td>
						<td style="font-size: 14">
							<input id="nation" name="emrFirst.nation" value="${emrFirst.nation}" />
							<a href="javascript:delSelectedData('nation');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td  class="honry-lable"  style="font-size: 14">
							患者职业：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" id="profeesion" name="emrFirst.profeesion" value="${emrFirst.profeesion}"/>
							<a href="javascript:delSelectedData('profeesion');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable" style="font-size: 14">
							出生地：
						</td>
						<td style="font-size: 14">
							<input class="easyui-textbox" style="width:400px" id="birthPlace" name="emrFirst.birthPlace" value="${emrFirst.birthPlace}"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							工作电话：
						</td>
						<td style="font-size: 14">
							<input class="easyui-textbox" style="width:200px" id="jobPhone" name="emrFirst.jobPhone" data-options="validType:'phoneRex'" value="${emrFirst.jobPhone}"/>
						</td>
						<td class="honry-lable" style="font-size: 14">
							工作单位和地址：
						</td>
						<td style="font-size: 14">
							<input class="easyui-textbox" style="width:400px" id="jobAddress" name="emrFirst.jobAddress" value="${emrFirst.jobAddress}"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							工作地点邮编：
						</td>
						<td style="font-size: 14">
							<input class="easyui-textbox" style="width:200px" id="jobPost" name="emrFirst.jobPost" data-options="validType:'zip'" value="${emrFirst.jobPost}"/>
						</td>
						<td class="honry-lable" style="font-size: 14">
							户口地址：
						</td>
						<td style="font-size: 14">
							<input class="easyui-textbox" style="width:400px" id="houseHold" name="emrFirst.houseHold" value="${emrFirst.houseHold}"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							家庭电话：
						</td>
						<td style="font-size: 14">
							<input class="easyui-textbox" style="width:200px" id="homePhone" name="emrFirst.homePhone" data-options="validType:'phoneRex'" value="${emrFirst.homePhone}"/>
						</td>
						<td class="honry-lable" style="font-size: 14">
							户口所在地邮编：
						</td>
						<td style="font-size: 14">
							<input class="easyui-textbox" style="width:200px" id="homePost" name="emrFirst.homePost" data-options="validType:'zip'" value="${emrFirst.homePost}"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							联系人：
						</td>
						<td style="font-size: 14">
							<input class="easyui-textbox" style="width:200px" id="link" name="emrFirst.link" value="${emrFirst.link}"/>
						</td>
						<td class="honry-lable" style="font-size: 14">
							联系人关系：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" id="linkRelation" name="emrFirst.linkRelation" value="${emrFirst.linkRelation}"/>
							<a href="javascript:delSelectedData('linkRelation');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							联系人电话：
						</td>
						<td style="font-size: 14">
							<input class="easyui-textbox" style="width:200px" id="linkPhone" name="emrFirst.linkPhone" data-options="validType:'phoneRex'" value="${emrFirst.linkPhone}"/>
						</td>
						<td class="honry-lable" style="font-size: 14">
							联系人地址：
						</td>
						<td style="font-size: 14">
							<input class="easyui-textbox" style="width:400px" id="linkAddress" name="emrFirst.linkAddress" value="${emrFirst.linkAddress}"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							入院时间：
						</td>
						<td style="font-size: 14">
							<input id="inTime" class="Wdate" name="emrFirst.inTime" type="text" value="${emrFirst.inTime}" onClick="WdatePicker()" style="width:200px"/>
<%-- 							<input class="easyui-datebox" style="width:200px" id="inTime" name="emrFirst.inTime"  value="${emrFirst.inTime}"/> --%>
						</td>
						<td class="honry-lable" style="font-size: 14">
							入院科别：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" class="easyui-combobox" data-options="editable:false,prompt:'下拉或回车查询',iconCls:'icon-user_gray_cool'"
									id="inDept" name="emrFirst.inDept" value="${emrFirst.inDept}" />
							<a href="javascript:delSelectedData('inDept');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							所在病区：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" class="easyui-combobox" data-options="editable:false,prompt:'下拉或回车查询',iconCls:'icon-user_home',required:true" 
									id="inNation" name="emrFirst.inNation"value="${emrFirst.inNation}"/>
							<a href="javascript:delSelectedData('inNation');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable" style="font-size: 14">
							术前住院天数：
						</td>
						<td style="font-size: 14">
							<input class="easyui-numberbox" data-options="min:0" style="width:200px" id="opDay" name="emrFirst.opDay" value="${emrFirst.opDay}"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							转科日期：
						</td>
						<td style="font-size: 14">
							<input id="tranTime" class="Wdate" name="emrFirst.tranTime" type="text" value="${emrFirst.tranTime}" onClick="WdatePicker()" style="width:200px"/>
<%-- 							<input class="easyui-datebox" style="width:200px" id="tranTime" name="emrFirst.tranTime" value="${emrFirst.tranTime}"/> --%>
						</td>
						<td class="honry-lable" style="font-size: 14">
							转科科别：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" class="easyui-combobox" data-options="editable:false,prompt:'下拉或回车查询',iconCls:'icon-user_gray_cool'"
									id="tranDept" name="emrFirst.tranDept" value="${emrFirst.tranDept}" />
							<a href="javascript:delSelectedData('tranDept');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							转科病区：
						</td>
						<td style="font-size: 14">
							<input style="width:200px"  class="easyui-combobox" data-options="editable:false,prompt:'下拉或回车查询',iconCls:'icon-user_home'" 
									id="tranNation" name="emrFirst.tranNation" value="${emrFirst.tranNation}"/>
							<a href="javascript:delSelectedData('tranNation');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable" style="font-size: 14">
							再转科别：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" class="easyui-combobox" data-options="editable:false,prompt:'下拉或回车查询',iconCls:'icon-user_gray_cool'"
									id="againTranDept" name="emrFirst.againTranDept" value="${emrFirst.againTranDept}" />
							<a href="javascript:delSelectedData('againTranDept');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							出院日期：
						</td>
						<td style="font-size: 14">
							<input id="outTime" class="Wdate" name="emrFirst.outTime" type="text" value="${emrFirst.outTime}" onClick="WdatePicker()" style="width:200px"/>
<%-- 							<input class="easyui-datebox" style="width:200px" id="outTime" name="emrFirst.outTime" value="${emrFirst.outTime}"/> --%>
						</td>
						<td class="honry-lable" style="font-size: 14">
							出院科别：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" class="easyui-combobox" data-options="editable:false,prompt:'下拉或回车查询',iconCls:'icon-user_gray_cool'"
									id="outDept" name="emrFirst.outDept" value="${emrFirst.outDept}" />
							<a href="javascript:delSelectedData('outDept');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							出院病区：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" class="easyui-combobox" data-options="editable:false,prompt:'下拉或回车查询',iconCls:'icon-user_home',required:true" 
									id="outNation" name="emrFirst.outNation"value="${emrFirst.outNation}"/>
							<a href="javascript:delSelectedData('outNation');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable" style="font-size: 14">
							实际住院天数：
						</td>
						<td style="font-size: 14">
							<input class="easyui-numberbox" data-options="min:0" style="width:200px" id="inpatientDay" name="emrFirst.inpatientDay" value="${emrFirst.inpatientDay}"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							死亡时间：
						</td>
						<td style="font-size: 14">
							<input id="deathTime" class="Wdate" name="emrFirst.deathTime" type="text" value="${emrFirst.deathTime}" onClick="WdatePicker()" style="width:200px"/>
<%-- 							<input class="easyui-datebox" style="width:200px" id="deathTime" name="emrFirst.deathTime" value="${emrFirst.deathTime}"/> --%>
						</td>
						<td class="honry-lable" style="font-size: 14">
							死亡原因：
						</td>
						<td style="font-size: 14">
							<input class="easyui-textbox" style="width:200px" id="deathReson" name="emrFirst.deathReson" value="${emrFirst.deathReson}"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							科主任：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" class="easyui-combobox" data-options="editable:false,prompt:'下拉或回车查询'" id="deptHead" name="emrFirst.deptHead" value="${emrFirst.deptHead}"/>
							<a href="javascript:delSelectedData('deptHead');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable" style="font-size: 14">
							（副）主任医师：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" id="chiefDoc" class="easyui-combobox" data-options="editable:false,prompt:'下拉或回车查询'" name="emrFirst.chiefDoc" value="${emrFirst.chiefDoc }" />
							<a href="javascript:delSelectedData('chiefDoc');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							主治医师：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" class="easyui-combobox" data-options="editable:false,prompt:'下拉或回车查询'" id="attendingDoc" name="emrFirst.attendingDoc" value="${emrFirst.attendingDoc}"/>
							<a href="javascript:delSelectedData('attendingDoc');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable" style="font-size: 14">
							住院医师：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" class="easyui-combobox" data-options="editable:false,prompt:'下拉或回车查询'" id="inpatientDoc" name="emrFirst.inpatientDoc" value="${emrFirst.inpatientDoc}"/>
							<a href="javascript:delSelectedData('inpatientDoc');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							进修医生：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" id="refresherDoc" class="easyui-combobox" data-options="editable:false,prompt:'下拉或回车查询'" name="emrFirst.refresherDoc" value="${emrFirst.refresherDoc }"/>
							<a href="javascript:delSelectedData('refresherDoc');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td class="honry-lable" style="font-size: 14">
							研究生实习医师：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" id="graduateIntern" class="easyui-combobox" data-options="editable:false,prompt:'下拉或回车查询'" name="emrFirst.graduateIntern" value="${emrFirst.graduateIntern }"/>
							<a href="javascript:delSelectedData('graduateIntern');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							门急诊诊断：
						</td>
						<td style="font-size: 14"colspan="6">
							<textarea class="easyui-validatebox" id="outPatientDiag" style="width: 100%;height: 50px;font-size: 14px"  
								name="emrFirst.outPatientDiag">${emrFirst.outPatientDiag }</textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							入院状态：
						</td>
						<td style="font-size: 14">
							<input style="width:200px" id="inState" name="emrFirst.inState" value="${emrFirst.inState}" />
						</td>
						<td class="honry-lable" style="font-size: 14">
							入院后确诊日期：
						</td>
						<td style="font-size: 14">
							<input id="diagTime" class="Wdate" name="emrFirst.diagTime" type="text" value="${emrFirst.diagTime}" onClick="WdatePicker()" style="width:200px"/>
<%-- 							<input class="easyui-datebox" style="width:200px" id="diagTime" name="emrFirst.diagTime" value="${emrFirst.diagTime}" /> --%>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							出院诊断1：
						</td>
						<td style="font-size: 14"colspan="6">
							<textarea class="easyui-validatebox" id="diag1" style="width: 100%;height: 50px;font-size: 14px"  
								name="emrFirst.diag1">${emrFirst.diag1 }</textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							出院诊断2：
						</td>
						<td style="font-size: 14"colspan="6">
							<textarea class="easyui-validatebox" id="diag2" style="width: 100%;height: 50px;font-size: 14px"  
								name="emrFirst.diag2">${emrFirst.diag2 }</textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							出院诊断3：
						</td>
						<td style="font-size: 14"colspan="6">
							<textarea class="easyui-validatebox" id="diag3" style="width: 100%;height: 50px;font-size: 14px"  
								name="emrFirst.diag3">${emrFirst.diag3 }</textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							入院诊断：
						</td>
						<td style="font-size: 14"colspan="6">
							<textarea class="easyui-validatebox" id="inDiag" style="width: 100%;height: 50px;font-size: 14px"  
								name="emrFirst.inDiag">${emrFirst.inDiag }</textarea>
						</td>
					</tr>
				</table>
			</form>
    	</div>  
		<div style="text-align: center; padding: 5px">
			<a href="javascript:submit();" class="easyui-linkbutton"
				data-options="iconCls:'icon-save'">保存</a>
			<a href="javascript:clear();" class="easyui-linkbutton"
				data-options="iconCls:'icon-clear'">清除</a>
		</div>		
	</div>
	<script type="text/javascript">
	$(function(){
		
		$.ajax({
			url: "<%=basePath %>emrs/emrFirst/getEmployee.action",
				success: function(data) {
					var deptHeadId = $('#deptHead').val();
					var attendingDocId = $('#attendingDoc').val();
					var inpatientDocId = $('#inpatientDoc').val();
					var flag1=0;
					var flag2=0;
					var flag3=0;
					for(var i = 0; i < data.length; i++){
						if(data[i].id == deptHeadId){
							$('#strDeptHead').textbox('setValue',data[i].name);
							flag1 = 1;
						}
						if(data[i].id == attendingDocId){
							$('#strAttendingDoc').textbox('setText',data[i].name);
							flag2 = 1;
						}
						if(data[i].id == inpatientDocId){
							$('#strInpatientDoc').textbox('setValue',data[i].name);
							flag3 = 1;
						}
						if(flag1 == 1 && flag2 == 1 && flag3 == 1){
							break;
						}
					}
			}
		});
		//性别
		$('#sex').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
			    valueField:'encode',    
			    textField:'name',
			    editable:false
			});
		//入院状态
		$('#inState').combobox({ 
			data: [{
				id: '1',
				name: '病危'
			},{
				id: '2',
				name: '病重'
			},{
				id: '3',
				name: '手术'
			},{
				id: '4',
				name: '常规'
			}],      
		    valueField:'id',    
		    textField:'name',
		    editable:false
		});
		//婚姻情况
		$('#marry') .combobox({ 
		    url : "<%=basePath%>emrs/emrFirst/getCombox.action?codeType=marry",      
		    valueField:'encode',    
		    textField:'name',
		    editable:false
		});
		//患者职业
		$('#profeesion') .combobox({ 
		    url : "<%=basePath%>emrs/emrFirst/getCombox.action?codeType=occupation",      
		    valueField:'encode',    
		    textField:'name',
		    editable:false,
		    keyHandler: function(){
		    	toSelectWin("<%=basePath%>popWin/popWinCode/toCodePopWinXML.action?classNameTmp=CodeOccupation&textId=profeesion");
		    }
		});
		//民族
		$('#folk') .combobox({ 
		    url : "<%=basePath%>emrs/emrFirst/getCombox.action?codeType=nationality",      
		    valueField:'encode',    
		    textField:'name',
		    editable:false,
		    keyHandler: function(){
		    	toSelectWin("<%=basePath%>popWin/popWinCode/toCodePopWinXML.action?classNameTmp=CodeNationality&textId=folk");
		    }
		});
		//国籍
		$('#nation') .combobox({ 
		    url : "<%=basePath%>emrs/emrFirst/getCombox.action?codeType=country",      
		    valueField:'encode',    
		    textField:'name',
		    editable:false,
		    keyHandler: function(){
		    	toSelectWin("<%=basePath%>popWin/popWinCode/toCodePopWinXML.action?classNameTmp=CodeCountry&textId=nation");
		    }
		});
		//联系人关系
		$('#linkRelation') .combobox({ 
		    url : "<%=basePath%>emrs/emrFirst/getCombox.action?codeType=relation",      
		    valueField:'encode',    
		    textField:'name',
		    editable:false,
		    keyHandler: function(){
		    	toSelectWin("<%=basePath%>popWin/popWinCode/toCodePopWinXML.action?classNameTmp=CodeRelation&textId=linkRelation");
		    }
		});
		
		
		//所在病区
		$('#inNation').combobox({
				url:'<%=basePath%>emrs/emrFirst/getSysDeptment.action?flg=1',
				valueField:'deptCode',
				textField:'deptName',
			    editable:false,
			    keyHandler:{
					enter:function(){
						toWinDepartment('N','inNation');
					}
				}
			});
		//转科病区
		$('#tranNation').combobox({
				url:'<%=basePath%>emrs/emrFirst/getSysDeptment.action?flg=1',
				valueField:'deptCode',
				textField:'deptName',
			    editable:false,
			    keyHandler:{
					enter:function(){
						toWinDepartment('N','tranNation');
					}
				}
			});
		//出院病区
		$('#outNation').combobox({
				url:'<%=basePath%>emrs/emrFirst/getSysDeptment.action?flg=2',
				valueField:'deptCode',
				textField:'deptName',
			    editable:false,
			    keyHandler:{
					enter:function(){
						toWinDepartment('C,I,U','outNation');
					}
				}
			});
		//入院科室
		$('#inDept').combobox({
				url:'<%=basePath%>emrs/emrFirst/getSysDeptment.action?flg=2',
				valueField:'deptCode',
				textField:'deptName',
			    editable:false,
			    keyHandler:{
					enter:function(){
						toWinDepartment('C,I,U,N','inDept');
					}
				}
			});
		//转科科别
		$('#tranDept').combobox({
				url:'<%=basePath%>emrs/emrFirst/getSysDeptment.action?flg=2',
				valueField:'deptCode',
				textField:'deptName',
			    editable:false,
			    keyHandler:{
					enter:function(){
						toWinDepartment('C,I,U,N','tranDept');
					}
				}
			});
		//再转科别
		$('#againTranDept').combobox({
				url:'<%=basePath%>emrs/emrFirst/getSysDeptment.action?flg=2',
				valueField:'deptCode',
				textField:'deptName',
			    editable:false,
			    keyHandler:{
					enter:function(){
						toWinDepartment('C,I,U,N','againTranDept');
					}
				}
			});
		//出院科别
		$('#outDept').combobox({
				url:'<%=basePath%>emrs/emrFirst/getSysDeptment.action?flg=2',
				valueField:'deptCode',
				textField:'deptName',
			    editable:false,
			    keyHandler:{
					enter:function(){
						toWinDepartment('C,I,U,N','outDept');
					}
				}
			});
		//科主任
		$('#deptHead').combobox({
				url:'<%=basePath%>emrs/emrFirst/getEmployee.action',
				valueField:'jobNo',
				textField:'name',
			    editable:false,
			    keyHandler:{
					enter:function(){
						toSelectWin("<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=deptHead&employeeType=1");
					}
				}
			});
		//主治医师
		$('#chiefDoc').combobox({
				url:'<%=basePath%>emrs/emrFirst/getEmployee.action',
				valueField:'jobNo',
				textField:'name',
			    editable:false,
			    keyHandler:{
					enter:function(){
						toSelectWin("<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=chiefDoc&employeeType=1");
					}
				}
			});
		//住院医师
		$('#attendingDoc').combobox({
				url:'<%=basePath%>emrs/emrFirst/getEmployee.action',
				valueField:'jobNo',
				textField:'name',
			    editable:false,
			    keyHandler:{
					enter:function(){
						toSelectWin("<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=attendingDoc&employeeType=1");
					}
				}
			});
		//（副）主任医师
		$('#inpatientDoc').combobox({
				url:'<%=basePath%>emrs/emrFirst/getEmployee.action',
				valueField:'jobNo',
				textField:'name',
			    editable:false,
			    keyHandler:{
					enter:function(){
						toSelectWin("<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=inpatientDoc&employeeType=1");
					}
				}
			});
		//进修医生
		$('#refresherDoc').combobox({
				url:'<%=basePath%>emrs/emrFirst/getEmployee.action',
				valueField:'jobNo',
				textField:'name',
			    editable:false,
			    keyHandler:{
					enter:function(){
						toSelectWin("<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=refresherDoc&employeeType=1");
					}
				}
			});
		//研究生实习医师
		$('#graduateIntern').combobox({
				url:'<%=basePath%>emrs/emrFirst/getEmployee.action',
				valueField:'jobNo',
				textField:'name',
			    editable:false,
			    keyHandler:{
					enter:function(){
						toSelectWin("<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=graduateIntern&employeeType=1");
					}
				}
			});
		
	});
	/**
	 * 还原表单信息
	 */
	function clear(){
		$('#editForm').form('reset');
	}
	/**
	 * 提交表单
	 */
	function submit(){
		$('#editForm').form('submit', {    
			url:'<%=basePath%>emrs/emrFirst/save.action',
			onSubmit: function(){
				if($("#inpatientNo").val() == undefined){
					$.messager.alert('操作提示','请从左侧患者树双击选择患者！！！');
					setTimeout(function(){$(".messager-body").window('close')},3500);
					return false;
				}else if(!$(this).form('validate')){
					$.messager.alert('操作提示','尚有数据不符合规定，请修改后保存！！');
					setTimeout(function(){$(".messager-body").window('close')},3500);
					return false;
				}
			},
		    success: function(data){    
		        if (data == 'success'){    
		        	$.messager.alert('提示','保存成功！！');   
		        	$('#editForm').form('clear');
		        }    
		    },    
		    error: function(data){    

		    }    
		});  

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
		      // alert('t'+value);
		      return true;
		    }else
		    {
		     //alert('false '+value);
		       return false;
		    }
		      
		    },
		    message: '请输入正确电话或手机格式'
		  },
		  idEntityCard: {
		    validator: function(value){
		    // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
	   		var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
		    if(reg.test(value))
		    {
		      // alert('t'+value);
		      return true;
		    }else
		    {
		     //alert('false '+value);
		       return false;
		    }
		      
		    },
		    message: '请输入正确身份证格式'
		  },
		  zip: {// 验证邮政编码
              validator: function (value) {
                  return /^[0-9]\d{5}$/i.test(value);
              },
              message: '请输入正确邮政编码格式'
          },

		});
	
	/**
	 * 回车弹框
	 */
	 function toSelectWin(url){
		var tempWinPath = url;
		window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='+ (screen.availHeight-
		370)+',scrollbars,resizable=yes,toolbar=yes');
	}
	/**
	 * 回车弹出病区/科室弹框
	 */
	 function toWinDepartment(deptType,textId){
		var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptType="+deptType+"&textId="+textId;
		window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='+ (screen.availHeight-
		370)+',scrollbars,resizable=yes,toolbar=yes');
	}
	</script>
</body>