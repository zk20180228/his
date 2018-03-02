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
<title>护理信息维护查看界面</title>
<script type="text/javascript">
$(function (){
	var nurMeasurModeList = new Array(6);   [{id: '1',value: '口腔'},{id: '2',value: '腋下'},{id: '3',value: '肛门'}]
	nurMeasurModeList[0] = ('${maintenanceVo.nurMeasurMode }');
	nurMeasurModeList[1] = '${maintenanceVo2.nurMeasurMode2 }';
	nurMeasurModeList[2] = '${maintenanceVo3.nurMeasurMode3 }';
	nurMeasurModeList[3] = '${maintenanceVo4.nurMeasurMode4 }';
	nurMeasurModeList[4] = '${maintenanceVo5.nurMeasurMode5 }';
	nurMeasurModeList[5] = '${maintenanceVo6.nurMeasurMode6 }';
	for(var i = 0; i < nurMeasurModeList.length; i ++){
		nurMeasur = nurMeasurModeList[i];
		if(nurMeasur == 1){
			document.getElementById("nurMeasurMode" + i).innerHTML='口腔';
		}else if(nurMeasur == 2){
			document.getElementById("nurMeasurMode" + i).innerHTML='腋下';
		}else if(nurMeasur == 3){
			document.getElementById("nurMeasurMode" + i).innerHTML='肛门';
		}
		
	}
});
/**
 *关闭
 */
function clear(){
	window.location.href="<%=basePath%>emrs/maintenance/toViewMaintenanceList.action?menuAlias=${menuAlias}";
}
</script>
</head>
<body>
<div class="easyui-panel" id = "panelEast" style="width:100%">
	<div style="padding:5px 5px 5px 5px;">	
		<table style="width:100%;border:1px solid #95b8e7;padding:5px;">
			<tr>
				<td style="padding: 5px 15px;font-size:14px" >
	    			<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">关闭</a>
				</td>
			</tr>
		</table>   
	</div>
	<div style="padding:5px 5px 5px 5px;">
		<table style="width:100%;border:1px solid #95b8e7;padding:5px;">
			<input type="hidden" id="flag" value="emrMaintenanceView"/> 
			<tr>
				<td style="padding: 5px 15px;font-size:14px" >住院号：</td>
				<td style="padding: 5px 15px;font-size:14px" >${maintenance.nurInpatientNo }</td>
				<td style="padding: 5px 15px;font-size:14px" >姓名：</td>
				<td style="padding: 5px 15px;font-size:14px" >${maintenance.nurName }</td>
				<td style="padding: 5px 15px;font-size:14px" >日期：</td>
				<td style="padding: 5px 15px;font-size:14px" >${maintenance.nurMeasurEtime}</td>
			</tr>
		</table>   
	</div>
	<div style="padding:5px 5px 5px 5px;">
		<table class="tableCss" style="height:600px">
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
				<td class="Input" align="center">${maintenance.nurTemperature }</td>
				<td class="Input" id="nurMeasurMode1"></td>
				<td class="Input" align="center">${maintenanceVo2.nurTemperature2 }</td>
				<td class="Input" id="nurMeasurMode2"></td>
				<td class="Input" align="center">${maintenanceVo3.nurTemperature3 }</td>
				<td class="Input" id="nurMeasurMode3"></td>
				<td class="Input" align="center">${maintenanceVo4.nurTemperature4 }</td>
				<td class="Input" id="nurMeasurMode4"></td>
				<td class="Input" align="center">${maintenanceVo5.nurTemperature5 }</td>
				<td class="Input" id="nurMeasurMode5"></td>
				<td class="Input" align="center">${maintenanceVo6.nurTemperature6 }</td>
				<td class="Input" id="nurMeasurMode6"></td>
			</tr>
			<tr>
				<td class="TDlabel">脉搏（次/分）</td>
				<td colspan="2" align="center">${maintenance.nurPulse }</td>
				<td colspan="2" align="center">${maintenanceVo2.nurPulse2 }</td>
				<td colspan="2" align="center">${maintenanceVo3.nurPulse3 }</td>
				<td colspan="2" align="center">${maintenanceVo4.nurPulse4 }</td>
				<td colspan="2" align="center">${maintenanceVo5.nurPulse5 }</td>
				<td colspan="2" align="center">${maintenanceVo6.nurPulse6 }</td>
			</tr>
			<tr>
				<td class="TDlabel">呼吸（次/分）</td>
				<td colspan="2" align="center">${maintenance.nurBreath }</td>
				<td colspan="2" align="center">${maintenanceVo2.nurBreath2 }</td>
				<td colspan="2" align="center">${maintenanceVo3.nurBreath3 }</td>
				<td colspan="2" align="center">${maintenanceVo4.nurBreath4 }</td>
				<td colspan="2" align="center">${maintenanceVo5.nurBreath5 }</td>
				<td colspan="2" align="center">${maintenanceVo6.nurBreath6 }</td>
			</tr>
			<tr>
				<td class="TDlabel" >大便次数</td>
				<td colspan="2" align="center">${maintenance.nurDefacation }</td>
				<td colspan="2" align="center">${maintenanceVo2.nurDefacation2 }</td>
				<td colspan="2" align="center">${maintenanceVo3.nurDefacation3 }</td>
				<td colspan="2" align="center">${maintenanceVo4.nurDefacation4 }</td>
				<td colspan="2" align="center">${maintenanceVo5.nurDefacation5 }</td>
				<td colspan="2" align="center">${maintenanceVo6.nurDefacation6 }</td>
			</tr>
			<tr>
				<td class="TDlabel">血压（mmHg）</td>
				<td align="center" colspan="2">${maintenance.nurBloodpressured }/${maintenance.nurBloodpressureg }</td>
				<td align="center" colspan="2">${maintenanceVo2.nurBloodpressured2 }/${maintenanceVo2.nurBloodpressureg2 }</td>
				<td align="center" colspan="2">${maintenanceVo3.nurBloodpressured3 }/${maintenanceVo3.nurBloodpressureg3 }</td>
				<td align="center" colspan="2">${maintenanceVo4.nurBloodpressured4 }/${maintenanceVo4.nurBloodpressureg4 }</td>
				<td align="center" colspan="2">${maintenanceVo5.nurBloodpressured5 }/${maintenanceVo5.nurBloodpressureg5 }</td>
				<td align="center" colspan="2">${maintenanceVo6.nurBloodpressured6 }/${maintenanceVo6.nurBloodpressureg6 }</td>
			</tr>
			<tr>
				<td class="TDlabel">总入量（ml）</td>
				<td colspan="2" align="center">${maintenance.nurSumin }</td>
				<td colspan="2" align="center">${maintenanceVo2.nurSumin2 }</td>
				<td colspan="2" align="center">${maintenanceVo3.nurSumin3 }</td>
				<td colspan="2" align="center">${maintenanceVo4.nurSumin4 }</td>
				<td colspan="2" align="center">${maintenanceVo5.nurSumin5 }</td>
				<td colspan="2" align="center">${maintenanceVo6.nurSumin6 }</td>
			</tr>
			<tr>
				<td class="TDlabel">总出量（ml）</td>
				<td colspan="2" align="center">${maintenance.nurSumout}</td>
				<td colspan="2" align="center">${maintenanceVo2.nurSumout2 }</td>
				<td colspan="2" align="center">${maintenanceVo3.nurSumout3 }</td>
				<td colspan="2" align="center">${maintenanceVo4.nurSumout4 }</td>
				<td colspan="2" align="center">${maintenanceVo5.nurSumout5 }</td>
				<td colspan="2" align="center">${maintenanceVo6.nurSumout6 }</td>
			</tr>
			<tr>
				<td class="TDlabel">引流量（ml）</td>
				<td colspan="2" align="center">${maintenance.nurDrainage}</td>
				<td colspan="2" align="center">${maintenanceVo2.nurDrainage2}</td>
				<td colspan="2" align="center">${maintenanceVo3.nurDrainage3}</td>
				<td colspan="2" align="center">${maintenanceVo4.nurDrainage4}</td>
				<td colspan="2" align="center">${maintenanceVo5.nurDrainage5}</td>
				<td colspan="2" align="center">${maintenanceVo6.nurDrainage6}</td>
			</tr>
			<tr>
				<td class="TDlabel">身高（cm）</td>
				<td colspan="2" align="center">${maintenance.nurHeight}</td>
				<td colspan="2" align="center">${maintenanceVo2.nurHeight2}</td>
				<td colspan="2" align="center">${maintenanceVo3.nurHeight3}</td>
				<td colspan="2" align="center">${maintenanceVo4.nurHeight4}</td>
				<td colspan="2" align="center">${maintenanceVo5.nurHeight5}</td>
				<td colspan="2" align="center">${maintenanceVo6.nurHeight6}</td>
			</tr>
			<tr>
				<td class="TDlabel">体重（kg）</td>
				<td colspan="2" align="center">${maintenance.nurWeight}</td>
				<td colspan="2" align="center">${maintenanceVo2.nurWeight2}</td>
				<td colspan="2" align="center">${maintenanceVo3.nurWeight3}</td>
				<td colspan="2" align="center">${maintenanceVo4.nurWeight4}</td>
				<td colspan="2" align="center">${maintenanceVo5.nurWeight5}</td>
				<td colspan="2" align="center">${maintenanceVo6.nurWeight6}</td>
			</tr>
			<tr>
				<td class="TDlabel">过敏药物</td>
				<td colspan="2" align="center">${maintenance.nurAllergicdrug}</td>
				<td colspan="2" align="center">${maintenanceVo2.nurAllergicdrug2}</td>
				<td colspan="2" align="center">${maintenanceVo3.nurAllergicdrug3}</td>
				<td colspan="2" align="center">${maintenanceVo4.nurAllergicdrug4}</td>
				<td colspan="2" align="center">${maintenanceVo5.nurAllergicdrug5}</td>
				<td colspan="2" align="center">${maintenanceVo6.nurAllergicdrug6}</td>
			</tr>
			<tr>
				<td class="TDlabel">心率（次/分）</td>
				<td colspan="2" align="center">${maintenance.nurHeartrate}</td>
				<td colspan="2" align="center">${maintenanceVo2.nurHeartrate2}</td>
				<td colspan="2" align="center">${maintenanceVo3.nurHeartrate3}</td>
				<td colspan="2" align="center">${maintenanceVo4.nurHeartrate4}</td>
				<td colspan="2" align="center">${maintenanceVo5.nurHeartrate5}</td>
				<td colspan="2" align="center">${maintenanceVo6.nurHeartrate6}</td>
			</tr>
			<tr>
				<td class="TDlabel">物理降温</td>
				<td colspan="2" align="center">${maintenance.nurPhysicalcooling}</td>
				<td colspan="2" align="center">${maintenanceVo2.nurPhysicalcooling2}</td>
				<td colspan="2" align="center">${maintenanceVo3.nurPhysicalcooling3}</td>
				<td colspan="2" align="center">${maintenanceVo4.nurPhysicalcooling4}</td>
				<td colspan="2" align="center">${maintenanceVo5.nurPhysicalcooling5}</td>
				<td colspan="2" align="center">${maintenanceVo6.nurPhysicalcooling6}</td>
			</tr>
		</table>
	</div>
</div>
</body>
</html>