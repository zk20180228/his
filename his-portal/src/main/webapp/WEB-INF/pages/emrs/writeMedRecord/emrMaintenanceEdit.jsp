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
<title>护理信息维护编辑界面</title>
<style type="text/css">
	.tableCss{
		border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:70%;
	}
	.tableCss .TDlabel{
		text-align: left;
		font-size:14px;
		width:150px;
	}
	.tableCss td{
		border: 1px solid #95b8e7;
		padding: 5px 5px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.easyuiInput{
		width:100px;
	}
	.Input{
		width:65px;
	}
</style>
<script type="text/javascript">
/**
 *保存方法
 */
function submit(){ 
	$('#editForm').form('submit',{  
    	url:"<%=basePath %>emrs/maintenance/save.action",  
    	onSubmit:function(){
			if (!$('#editForm').form('validate')) {
				$.messager.show({
					title : '提示信息',
					msg : '验证没有通过,不能提交表单!'
				});
				return false;
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});
    	},  
        success:function(){ 
        	$.messager.progress('close');
        	$.messager.alert('提示','保存成功');
        	window.location.href="<%=basePath%>emrs/maintenance/toViewMaintenanceList.action?menuAlias=${menuAlias}";
        },
		error : function(data) {
			$.messager.progress('close');
			$.messager.alert('提示','保存失败！');	
		}							         
	}); 
}
/**
 *关闭
 */
function clear(){
	window.location.href="<%=basePath%>emrs/maintenance/toViewMaintenanceList.action?menuAlias=${menuAlias}";
}
</script>
</head>
<body>
	<div class="easyui-panel" id = "panelEast" style="width:100%" border = "false">
		<form method="post" id="editForm">
		<div style="padding:5px 5px 5px 5px;">
			<input id="flag"  value="emrMaintenance" type="hidden">
			<input id="nurPatid" name="maintenance.nurPatid" value="${maintenance.nurPatid }" type="hidden">
			<input id="nurInpatientNo" name="maintenance.nurInpatientNo" value="${maintenance.nurInpatientNo }" type="hidden">
			<input id="nurName" name="maintenance.nurName" value="${maintenance.nurName }" type="hidden">
			<input id="nurOpDay" name="maintenance.nurOpDay" value="${maintenance.nurOpDay }" type="hidden">
			<input id="nurInDay" name="maintenance.nurInDay" value="${maintenance.nurInDay }" type="hidden">
			<input id="id" name="maintenance.id" value="${maintenance.id }" type="hidden">
			<input id="id2" name="maintenanceVo2.id2" value="${maintenanceVo2.id2 }" type="hidden">
			<input id="id3" name="maintenanceVo3.id3" value="${maintenanceVo3.id3 }" type="hidden">
			<input id="id4" name="maintenanceVo4.id4" value="${maintenanceVo4.id4 }" type="hidden">
			<input id="id5" name="maintenanceVo5.id5" value="${maintenanceVo5.id5 }" type="hidden">
			<input id="id6" name="maintenanceVo6.id6" value="${maintenanceVo6.id6 }" type="hidden">
			<input id="nurMeasurEtime" name="maintenance.nurMeasurEtime" value="${maintenance.nurMeasurEtime }" type="hidden">
			<input id="nurMeasurEtime2" name="maintenanceVo2.nurMeasurEtime2" value="${maintenanceVo2.nurMeasurEtime2 }" type="hidden">
			<input id="nurMeasurEtime3" name="maintenanceVo3.nurMeasurEtime3" value="${maintenanceVo3.nurMeasurEtime3 }" type="hidden">
			<input id="nurMeasurEtime4" name="maintenanceVo4.nurMeasurEtime4" value="${maintenanceVo4.nurMeasurEtime4 }" type="hidden">
			<input id="nurMeasurEtime5" name="maintenanceVo5.nurMeasurEtime5" value="${maintenanceVo5.nurMeasurEtime5 }" type="hidden">
			<input id="nurMeasurEtime6" name="maintenanceVo6.nurMeasurEtime6" value="${maintenanceVo6.nurMeasurEtime6 }" type="hidden">
			<table style="width: 70%;margin-left:auto;margin-right:auto;">
				<tr>
					<td style="font-size:14px" >住院号：
					${maintenance.nurInpatientNo }&nbsp;姓名：${maintenance.nurName }&nbsp;日期：
					<input type="text" id="dateboxs" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',onpicked:pickedFunc})" value="${maintenance.dates}" class="Wdate" style="width: 150px"/>
					<a href="javascript:void(0)" onclick="set1()" class="easyui-linkbutton">※/E</a>
						<a href="javascript:void(0)" onclick="set2()" class="easyui-linkbutton">※</a>
						<a href="javascript:void(0)" onclick="set3()" class="easyui-linkbutton">✳</a>
						<a href="javascript:void(0)" onclick="set4()" class="easyui-linkbutton">|</a>
						<a href="javascript:void(0)" onclick="set5()" class="easyui-linkbutton">/C</a>
						<a href="javascript:void(0)" onclick="set6()" class="easyui-linkbutton">Ⅰ</a>
						<a href="javascript:void(0)" onclick="set7()" class="easyui-linkbutton">Ⅱ</a>
						<a href="javascript:void(0)" onclick="set8()" class="easyui-linkbutton">Ⅲ</a>
						<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		    			<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">关闭</a>
	    			</td>
				</tr>
			</table>   
		</div>
		<div style="padding:5px 5px 5px 5px;" border = "false">
			<table class="tableCss" style="width: 70%;margin-left:auto;margin-right:auto;">
				<tr>
					<td colspan="7" align="center">上午</td>
					<td colspan="6" align="center">下午</td>
				</tr>
				<tr>
					<td></td>
					<td colspan="2" align="center">2</td>
					<td colspan="2" align="center">6</td>
					<td colspan="2" align="center">10</td>
					<td colspan="2" align="center">14</td>
					<td colspan="2" align="center">18</td>
					<td colspan="2" align="center">22</td>
				</tr>
				<tr>
					<td class="TDlabel">体温（℃）</td>
					<td class="Input" align="center"><input name="maintenance.nurTemperature" id="nurTemperature" class="easyui-numberbox" data-options="min:0,precision:1" style="width:50px" value="${maintenance.nurTemperature }"></td>
					<td class="Input"><input name="maintenance.nurMeasurMode" id="nurMeasurMode" class="easyui-combobox" data-options="valueField:'id',textField: 'value',data: [{id: '1',value: '口腔'},{id: '2',value: '腋下'},{id: '3',value: '肛门'}]" style="width:65px" value="${maintenance.nurMeasurMode }"></td>
					<td class="Input" align="center"><input name="maintenanceVo2.nurTemperature2"  id="nurTemperature2" class="easyui-numberbox" data-options="min:0,precision:1" style="width:50px" value="${maintenanceVo2.nurTemperature2 }"></td>
					<td class="Input"><input name="maintenanceVo2.nurMeasurMode2" id="nurMeasurMode2" class="easyui-combobox" data-options="valueField:'id',textField: 'value',data: [{id: '1',value: '口腔'},{id: '2',value: '腋下'},{id: '3',value: '肛门'}]" style="width:65px" value="${maintenanceVo2.nurMeasurMode2 }"></td>
					<td class="Input" align="center"><input name="maintenanceVo3.nurTemperature3" id="nurTemperature3" class="easyui-numberbox" data-options="min:0,precision:1" style="width:50px" value="${maintenanceVo3.nurTemperature3 }"></td>
					<td class="Input"><input name="maintenanceVo3.nurMeasurMode3" id="nurMeasurMode3" class="easyui-combobox" data-options="valueField:'id',textField: 'value',data: [{id: '1',value: '口腔'},{id: '2',value: '腋下'},{id: '3',value: '肛门'}]" style="width:65px" value="${maintenanceVo3.nurMeasurMode3 }"></td>
					<td class="Input" align="center"><input name="maintenanceVo4.nurTemperature4" id="nurTemperature4" class="easyui-numberbox" data-options="min:0,precision:1" style="width:50px" value="${maintenanceVo4.nurTemperature4 }"></td>
					<td class="Input"><input name="maintenanceVo4.nurMeasurMode4" id="nurMeasurMode4" class="easyui-combobox" data-options="valueField:'id',textField: 'value',data: [{id: '1',value: '口腔'},{id: '2',value: '腋下'},{id: '3',value: '肛门'}]" style="width:65px" value="${maintenanceVo4.nurMeasurMode4 }"></td>
					<td class="Input" align="center"><input name="maintenanceVo5.nurTemperature5" id="nurTemperature5" class="easyui-numberbox" data-options="min:0,precision:1" style="width:50px" value="${maintenanceVo5.nurTemperature5 }"></td>
					<td class="Input"><input name="maintenanceVo5.nurMeasurMode5" id="nurMeasurMode5" class="easyui-combobox" data-options="valueField:'id',textField: 'value',data: [{id: '1',value: '口腔'},{id: '2',value: '腋下'},{id: '3',value: '肛门'}]" style="width:65px" value="${maintenanceVo5.nurMeasurMode5 }"></td>
					<td class="Input" align="center"><input name="maintenanceVo6.nurTemperature6" id="nurTemperature6" class="easyui-numberbox" data-options="min:0,precision:1" style="width:50px" value="${maintenanceVo6.nurTemperature6 }"></td>
					<td class="Input"><input name="maintenanceVo6.nurMeasurMode6" id="nurMeasurMode6" class="easyui-combobox" data-options="valueField:'id',textField: 'value',data: [{id: '1',value: '口腔'},{id: '2',value: '腋下'},{id: '3',value: '肛门'}]" style="width:65px" value="${maintenanceVo6.nurMeasurMode6 }"></td>
				</tr>
				<tr>
					<td class="TDlabel">脉搏（次/分）</td>
					<td colspan="2" align="center"><input id="nurPulse" name="maintenance.nurPulse" class="easyui-numberbox"  value="${maintenance.nurPulse }"></td>
					<td colspan="2" align="center"><input id="nurPulse2" name="maintenanceVo2.nurPulse2" class="easyui-numberbox"  value="${maintenanceVo2.nurPulse2 }"></td>
					<td colspan="2" align="center"><input id="nurPulse3" name="maintenanceVo3.nurPulse3" class="easyui-numberbox"  value="${maintenanceVo3.nurPulse3 }"></td>
					<td colspan="2" align="center"><input id="nurPulse4" name="maintenanceVo4.nurPulse4" class="easyui-numberbox"  value="${maintenanceVo4.nurPulse4 }"></td>
					<td colspan="2" align="center"><input id="nurPulse5" name="maintenanceVo5.nurPulse5" class="easyui-numberbox"  value="${maintenanceVo5.nurPulse5 }"></td>
					<td colspan="2" align="center"><input id="nurPulse6" name="maintenanceVo6.nurPulse6" class="easyui-numberbox"  value="${maintenanceVo6.nurPulse6 }"></td>
				</tr>
				<tr>
					<td class="TDlabel">呼吸（次/分）</td>
					<td colspan="2" align="center"><input id="nurBreath" name="maintenance.nurBreath" class="easyui-numberbox"  value="${maintenance.nurBreath }"></td>
					<td colspan="2" align="center"><input id="nurBreath2" name="maintenanceVo2.nurBreath2" class="easyui-numberbox"  value="${maintenanceVo2.nurBreath2 }"></td>
					<td colspan="2" align="center"><input id="nurBreath3" name="maintenanceVo3.nurBreath3" class="easyui-numberbox"  value="${maintenanceVo3.nurBreath3 }"></td>
					<td colspan="2" align="center"><input id="nurBreath4" name="maintenanceVo4.nurBreath4" class="easyui-numberbox"  value="${maintenanceVo4.nurBreath4 }"></td>
					<td colspan="2" align="center"><input id="nurBreath5" name="maintenanceVo5.nurBreath5" class="easyui-numberbox"  value="${maintenanceVo5.nurBreath5 }"></td>
					<td colspan="2" align="center"><input id="nurBreath6" name="maintenanceVo6.nurBreath6" class="easyui-numberbox"  value="${maintenanceVo6.nurBreath6 }"></td>
				</tr>
				<tr>
					<td class="TDlabel" >大便次数</td>
					<td colspan="2" align="center"><input id="nurDefacation" name="maintenance.nurDefacation" value="${maintenance.nurDefacation }" class="easyui-textbox" style="width:100px"><input id="rediv" type="radio" name="rediv">※<input id="rediva" name="rediv" type="radio">☆</td>
					<td colspan="2" align="center"><input id="nurDefacation2" name="maintenanceVo2.nurDefacation2" value="${maintenanceVo2.nurDefacation2 }" class="easyui-textbox" style="width:100px"><input id="rediv2" type="radio" name="rediv2a">※<input id="rediv2a" name="rediv2a" type="radio">☆</td>
					<td colspan="2" align="center"><input id="nurDefacation3" name="maintenanceVo3.nurDefacation3" value="${maintenanceVo3.nurDefacation3 }" class="easyui-textbox" style="width:100px"><input id="rediv3" type="radio" name="rediv3a">※<input id="rediv3a" name="rediv3a" type="radio">☆</td>
					<td colspan="2" align="center"><input id="nurDefacation4" name="maintenanceVo4.nurDefacation4" value="${maintenanceVo4.nurDefacation4 }" class="easyui-textbox" style="width:100px"><input id="rediv4" type="radio" name="rediv4a">※<input id="rediv4a" name="rediv4a" type="radio">☆</td>
					<td colspan="2" align="center"><input id="nurDefacation5" name="maintenanceVo5.nurDefacation5" value="${maintenanceVo5.nurDefacation5 }" class="easyui-textbox" style="width:100px"><input id="rediv5" type="radio" name="rediv5a">※<input id="rediv5a" name="rediv5a" type="radio">☆</td>
					<td colspan="2" align="center"><input id="nurDefacation6" name="maintenanceVo6.nurDefacation6" value="${maintenanceVo6.nurDefacation6 }" class="easyui-textbox" style="width:100px"><input id="rediv6" type="radio" name="rediv6a">※<input id="rediv6a" name="rediv6a" type="radio">☆</td>
				</tr>
				<tr>
					<td class="TDlabel">血压（mmHg）</td>
					<td align="center" colspan="2"><input id="nurBloodpressured" name="maintenance.nurBloodpressured" value="${maintenance.nurBloodpressured }" class="easyui-numberbox" style="width:50px">/<input id="nurBloodpressureg" name="maintenance.nurBloodpressureg" value="${maintenance.nurBloodpressureg }"  class="easyui-numberbox" style="width:50px"></td>
					<td align="center" colspan="2"><input id="nurBloodpressure2" name="maintenanceVo2.nurBloodpressured2" value="${maintenanceVo2.nurBloodpressured2 }" class="easyui-numberbox" style="width:50px">/<input id="nurBloodpressureg2" name="maintenanceVo2.nurBloodpressureg2" value="${maintenanceVo2.nurBloodpressureg2 }" class="easyui-numberbox" style="width:50px"></td>
					<td align="center" colspan="2"><input id="nurBloodpressure3" name="maintenanceVo3.nurBloodpressured3" value="${maintenanceVo3.nurBloodpressured3 }" class="easyui-numberbox" style="width:50px">/<input id="nurBloodpressureg3" name="maintenanceVo3.nurBloodpressureg3" value="${maintenanceVo3.nurBloodpressureg3 }" class="easyui-numberbox" style="width:50px"></td>
					<td align="center" colspan="2"><input id="nurBloodpressure4" name="maintenanceVo4.nurBloodpressured4" value="${maintenanceVo4.nurBloodpressured4 }" class="easyui-numberbox" style="width:50px">/<input id="nurBloodpressureg4" name="maintenanceVo4.nurBloodpressureg4" value="${maintenanceVo4.nurBloodpressureg4 }" class="easyui-numberbox" style="width:50px"></td>
					<td align="center" colspan="2"><input id="nurBloodpressure5" name="maintenanceVo5.nurBloodpressured5" value="${maintenanceVo5.nurBloodpressured5 }" class="easyui-numberbox" style="width:50px">/<input id="nurBloodpressureg5" name="maintenanceVo5.nurBloodpressureg5" value="${maintenanceVo5.nurBloodpressureg5 }" class="easyui-numberbox" style="width:50px"></td>
					<td align="center" colspan="2"><input id="nurBloodpressure6" name="maintenanceVo6.nurBloodpressured6" value="${maintenanceVo6.nurBloodpressured6 }" class="easyui-numberbox" style="width:50px">/<input id="nurBloodpressureg6" name="maintenanceVo6.nurBloodpressureg6" value="${maintenanceVo6.nurBloodpressureg6 }" class="easyui-numberbox" style="width:50px"></td>
				</tr>
				<tr>
					<td class="TDlabel">总入量（ml）</td>
					<td colspan="2" align="center"><input id="nurSumin" name="maintenance.nurSumin" value="${maintenance.nurSumin }" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurSumin2" name="maintenanceVo2.nurSumin2" value="${maintenanceVo2.nurSumin2 }" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurSumin3" name="maintenanceVo3.nurSumin3" value="${maintenanceVo3.nurSumin3 }" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurSumin4" name="maintenanceVo4.nurSumin4" value="${maintenanceVo4.nurSumin4 }" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurSumin5" name="maintenanceVo5.nurSumin5" value="${maintenanceVo5.nurSumin5 }" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurSumin6" name="maintenanceVo6.nurSumin6" value="${maintenanceVo6.nurSumin6 }" class="easyui-numberbox" data-options="min:0,precision:2"></td>
				</tr>
				<tr>
					<td class="TDlabel">总出量（ml）</td>
					<td colspan="2" align="center"><input id="nurSumout" name="maintenance.nurSumout" value="${maintenance.nurSumout}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurSumout2" name="maintenanceVo2.nurSumout2" value="${maintenanceVo2.nurSumout2 }" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurSumout3" name="maintenanceVo3.nurSumout3" value="${maintenanceVo3.nurSumout3 }" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurSumout4" name="maintenanceVo4.nurSumout4" value="${maintenanceVo4.nurSumout4 }" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurSumout5" name="maintenanceVo5.nurSumout5" value="${maintenanceVo5.nurSumout5 }" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurSumout6" name="maintenanceVo6.nurSumout6" value="${maintenanceVo6.nurSumout6 }" class="easyui-numberbox" data-options="min:0,precision:2"></td>
				</tr>
				<tr>
					<td class="TDlabel">引流量（ml）</td>
					<td colspan="2" align="center"><input id="nurDrainage" name="maintenance.nurDrainage" value="${maintenance.nurDrainage}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurDrainage2" name="maintenanceVo2.nurDrainage2" value="${maintenanceVo2.nurDrainage2}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurDrainage3" name="maintenanceVo3.nurDrainage3" value="${maintenanceVo3.nurDrainage3}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurDrainage4" name="maintenanceVo4.nurDrainage4" value="${maintenanceVo4.nurDrainage4}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurDrainage5" name="maintenanceVo5.nurDrainage5" value="${maintenanceVo5.nurDrainage5}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurDrainage6" name="maintenanceVo6.nurDrainage6" value="${maintenanceVo6.nurDrainage6}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
				</tr>
				<tr>
					<td class="TDlabel">身高（cm）</td>
					<td colspan="2" align="center"><input id="nurHeight" name="maintenance.nurHeight" value="${maintenance.nurHeight}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurHeight2" name="maintenanceVo2.nurHeight2" value="${maintenanceVo2.nurHeight2}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurHeight3" name="maintenanceVo3.nurHeight3" value="${maintenanceVo3.nurHeight3}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurHeight4" name="maintenanceVo4.nurHeight4" value="${maintenanceVo4.nurHeight4}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurHeight5" name="maintenanceVo5.nurHeight5" value="${maintenanceVo5.nurHeight5}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurHeight6" name="maintenanceVo6.nurHeight6" value="${maintenanceVo6.nurHeight6}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
				</tr>
				<tr>
					<td class="TDlabel">体重（kg）</td>
					<td colspan="2" align="center"><input id="nurWeight" name="maintenance.nurWeight"  value="${maintenance.nurWeight}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurWeight2" name="maintenanceVo2.nurWeight2" value="${maintenanceVo2.nurWeight2}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurWeight3" name="maintenanceVo3.nurWeight3" value="${maintenanceVo3.nurWeight3}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurWeight4" name="maintenanceVo4.nurWeight4" value="${maintenanceVo4.nurWeight4}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurWeight5" name="maintenanceVo5.nurWeight5" value="${maintenanceVo5.nurWeight5}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
					<td colspan="2" align="center"><input id="nurWeight6" name="maintenanceVo6.nurWeight6" value="${maintenanceVo6.nurWeight6}" class="easyui-numberbox" data-options="min:0,precision:2"></td>
				</tr>
				<tr>
					<td class="TDlabel">过敏药物</td>
					<td colspan="2" align="center"><input id="nurAllergicdrug" name="maintenance.nurAllergicdrug" value="${maintenance.nurAllergicdrug}" class="easyui-textbox"></td>
					<td colspan="2" align="center"><input id="nurAllergicdrug2" name="maintenanceVo2.nurAllergicdrug2" value="${maintenanceVo2.nurAllergicdrug2}" class="easyui-textbox"></td>
					<td colspan="2" align="center"><input id="nurAllergicdrug3" name="maintenanceVo3.nurAllergicdrug3" value="${maintenanceVo3.nurAllergicdrug3}" class="easyui-textbox"></td>
					<td colspan="2" align="center"><input id="nurAllergicdrug4" name="maintenanceVo4.nurAllergicdrug4" value="${maintenanceVo4.nurAllergicdrug4}" class="easyui-textbox"></td>
					<td colspan="2" align="center"><input id="nurAllergicdrug5" name="maintenanceVo5.nurAllergicdrug5" value="${maintenanceVo5.nurAllergicdrug5}" class="easyui-textbox"></td>
					<td colspan="2" align="center"><input id="nurAllergicdrug6" name="maintenanceVo6.nurAllergicdrug6" value="${maintenanceVo6.nurAllergicdrug6}" class="easyui-textbox"></td>
				</tr>
				<tr>
					<td class="TDlabel">心率（次/分）</td>
					<td colspan="2" align="center"><input id="nurHeartrate" name="maintenance.nurHeartrate" value="${maintenance.nurHeartrate}" class="easyui-numberbox"></td>
					<td colspan="2" align="center"><input id="nurHeartrate2" name="maintenanceVo2.nurHeartrate2" value="${maintenanceVo2.nurHeartrate2}" class="easyui-numberbox"></td>
					<td colspan="2" align="center"><input id="nurHeartrate3" name="maintenanceVo3.nurHeartrate3" value="${maintenanceVo3.nurHeartrate3}" class="easyui-numberbox"></td>
					<td colspan="2" align="center"><input id="nurHeartrate4" name="maintenanceVo4.nurHeartrate4" value="${maintenanceVo4.nurHeartrate4}" class="easyui-numberbox"></td>
					<td colspan="2" align="center"><input id="nurHeartrate5" name="maintenanceVo5.nurHeartrate5" value="${maintenanceVo5.nurHeartrate5}" class="easyui-numberbox"></td>
					<td colspan="2" align="center"><input id="nurHeartrate6" name="maintenanceVo6.nurHeartrate6" value="${maintenanceVo6.nurHeartrate6}" class="easyui-numberbox"></td>
				</tr>
				<tr>
					<td class="TDlabel">物理降温</td>
					<td colspan="2" align="center"><input id="nurPhysicalcooling" name="maintenance.nurPhysicalcooling" value="${maintenance.nurPhysicalcooling}" class="easyui-textbox"></td>
					<td colspan="2" align="center"><input id="nurPhysicalcooling2" name="maintenanceVo2.nurPhysicalcooling2" value="${maintenanceVo2.nurPhysicalcooling2}" class="easyui-textbox"></td>
					<td colspan="2" align="center"><input id="nurPhysicalcooling3" name="maintenanceVo3.nurPhysicalcooling3" value="${maintenanceVo3.nurPhysicalcooling3}" class="easyui-textbox"></td>
					<td colspan="2" align="center"><input id="nurPhysicalcooling4" name="maintenanceVo4.nurPhysicalcooling4" value="${maintenanceVo4.nurPhysicalcooling4}" class="easyui-textbox"></td>
					<td colspan="2" align="center"><input id="nurPhysicalcooling5" name="maintenanceVo5.nurPhysicalcooling5" value="${maintenanceVo5.nurPhysicalcooling5}" class="easyui-textbox"></td>
					<td colspan="2" align="center"><input id="nurPhysicalcooling6" name="maintenanceVo6.nurPhysicalcooling6" value="${maintenanceVo6.nurPhysicalcooling6}" class="easyui-textbox"></td>
				</tr>
			</table>
		</div>
		</form>
	</div>

</body>