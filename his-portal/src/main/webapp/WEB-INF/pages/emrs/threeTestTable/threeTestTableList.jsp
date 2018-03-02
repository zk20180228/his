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
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript">
var sexMap=new Map();
$(function(){
	//患者树
	$("#treePatientInfo").tree({
		url:'<%=basePath%>emrs/maintenance/getInpatientTree.action',
		lines:true,
		onClick:function(node){
			if(node.attributes.pid!='1'){
				$.messager.alert('提示','请操作本区患者下的的患者');
				setTimeout(function(){$(".messager-body").window('close')},3500);
			}else{
				clear();
				var weekState = 1;// weekState 1 是本周 2 是上周 3 是下周
				$('#nurPatid').val(node.id);
				$('#mid').val(node.attributes.medicalrecordId);
				queryPrient(node.id);
				queryDate(node.attributes.medicalrecordId,weekState);
			}
		}
	});
	
	$.ajax({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
			type:'post',
			success: function(data) {
				var v = data;
				for(var i=0;i<v.length;i++){
					sexMap.put(v[i].encode,v[i].name);
				}
			}
		});
});

function inntView(medicalrecordId,weekState,dates){
	 require.config({
           paths: {
               echarts: '${pageContext.request.contextPath}/javascript/echarts'
           }
       });
       var option=null;
       var arrayDate = new Array();//时间
       var arrayNurTemperature = new Array();//体温
       var arrayNurPulse = new Array();//脉搏
       var arrayNurBreath = new Array();//呼吸
       // 使用
		     require(
           [
               'echarts',
               'echarts/chart/line' // 使用折线图就加载bar模块，按需加载
           ],
           function (ecdom) {
               // 基于准备好的dom，初始化echarts图表
               var myChart = ecdom.init(document.getElementById('main')); 
               $.ajax({
           	       url: "<%=basePath%>emrs/threeTestTable/queryMaintenanceWeedState.action",
           		   type:'post',
           		   sysnc:true,
           		   data:{'maintenance.nurPatid':medicalrecordId,'maintenance.weekState':weekState,'maintenance.dates':dates},
           		   success: function(objData) {
           		       if(objData.maintenanceList.length>0){
           		    	   for(var i=0;i<objData.maintenanceList.length;i++){
           		    		   arrayNurTemperature[i] = objData.maintenanceList[i].nurTemperature;
           		    		   arrayNurPulse[i] = objData.maintenanceList[i].nurPulse;
           		    		   arrayNurBreath[i] =  objData.maintenanceList[i].nurBreath;
           		    	   }
           		       }
           		  	    arrayDate[0] = objData.datesList[6].date+" 2";
        				arrayDate[1] = "6";
        				arrayDate[2] = "10";
        				arrayDate[3] = "14";
        				arrayDate[4] = "18";
        				arrayDate[5] = "22";
        				arrayDate[6] = objData.datesList[0].date+" 2";
        				arrayDate[7] = "6";
        				arrayDate[8] = "10";
        				arrayDate[9] = "14";
        				arrayDate[10] = "18";
        				arrayDate[11] = "22";
        				arrayDate[12] = objData.datesList[1].date+" 2";
        				arrayDate[13] = "6";
        				arrayDate[14] = "10";
        				arrayDate[15] = "14";
        				arrayDate[16] = "18";
        				arrayDate[17] = "22";
        				arrayDate[18] = objData.datesList[2].date+" 2";
        				arrayDate[19] = "6";
        				arrayDate[20] = "10";
        				arrayDate[21] = "14";
        				arrayDate[22] = "18";
        				arrayDate[23] = "22";
        				arrayDate[24] = objData.datesList[3].date+" 2";
        				arrayDate[25] = "6";
        				arrayDate[26] = "10";
        				arrayDate[27] = "14";
        				arrayDate[28] = "18";
        				arrayDate[29] = "22";
        				arrayDate[30] = objData.datesList[4].date+" 2";
        				arrayDate[31] = "6";
        				arrayDate[32] = "10";
        				arrayDate[33] = "14";
        				arrayDate[34] = "18";
        				arrayDate[35] = "22";
        				arrayDate[36] = objData.datesList[5].date+" 2";
        				arrayDate[37] = "6";
        				arrayDate[38] = "10";
        				arrayDate[39] = "14";
        				arrayDate[40] = "18";
        				arrayDate[41] = "22";
	   					option = {
	   						    title : {
	   						        text: '体温，呼吸，脉搏三测图',
	   						    },
	   						    tooltip : {
	   						        trigger: 'axis'
	   						    },
	   						    legend: {
	   						        data:['口温','腋温','肛温','呼吸','脉搏']
	   						    },
	   						 	grid: {
	   						 		borderWidth : 0
   					  		    },
	   						    toolbox: {
	   						        show : true,
	   						        feature : {
	   						            mark : {show: true},
	   						            dataView : {show: true, readOnly: false},
	   						            restore : {show: true},
	   						            saveAsImage : {show: true}
	   						        }
	   						    },
	   						    calculable : false,
	   						    xAxis : [
	   						        {
	   						            type : 'category',
	   						            boundaryGap : false,
	   						            data : arrayDate,
	   						            name : '时间（小时）',
		   						        axisLabel:{
		   					                 //X轴刻度配置
		   					                 interval:0, //0：表示全部显示不间隔；auto:表示自动根据刻度个数和宽度自动设置间隔个数
		   					              	 rotate: 60
		   					            }
	   						        }
	   						    ],
	   						    yAxis : [
	   						        {
	   						            type : 'value', 
	   						            name:'体温（℃），呼吸（次/分），（次/分）脉搏'
	   						        }
	   						    ],
	   						    series : [
	   						        {
	   						            name:'口温',
	   						            type:'line',
	   						            data:arrayNurTemperature,
	   						        },
	   						        {
	   						            name:'腋温',
	   						            type:'line',
	   						            data:arrayNurTemperature,
	   						        },
	   						        {
	   						            name:'肛温',
	   						            type:'line',
	   						            data:arrayNurTemperature,
	   						        },
	   						        {
	   						            name:'呼吸',
	   						            type:'line',
	   						            data:arrayNurBreath,
	   						        },
	   						        {
	   						            name:'脉搏',
	   						            type:'line',
	   						            data:arrayNurPulse,
	   						        }
	   						    ]
	   						};
   						    // 为echarts对象加载数据 
   							 myChart.setOption(option,true,false); 
   				}
   			});	
           }
       );
}

function queryDate(medicalrecordId,weekState){
	$.ajax({
		url: "<%=basePath%>emrs/threeTestTable/queryDateRoMaintenance.action",
		type:'post',
		data:{'maintenance.weekState':weekState},
		success: function(data) {
			var dates ="";
			if(data.length>0){
				for(var i=0;i<data.length;i++){
					if(dates!=""){
						dates = dates + "','";
					}
					dates = dates + data[i].date
				}
				queryMaintenance(medicalrecordId,weekState,dates);
				inntView(medicalrecordId,weekState,dates);
			}
		}
	});
}
function queryMaintenance(medicalrecordId,weekState,dates){
	$.ajax({
		url: "<%=basePath%>emrs/threeTestTable/queryMaintenance.action",
		type:'post',
		data:{'maintenance.nurPatid':medicalrecordId,'maintenance.weekState':weekState,'maintenance.dates':dates},
		success: function(data) {
			for(var i=0;i<data.length;i++){
				if(data[i].nurMeasurEtime!='undefind'&&data[i].nurMeasurEtime!=null){
					$('#dateDay'+(i+1)).text(data[i].nurMeasurEtime);
				}
				if(data[i].nurInDay!='undefind'&&data[i].nurInDay!=null){
					$('#inDate'+(i+1)).text(data[i].nurInDay);
				}
				if(data[i].nurOpDay!='undefind'&&data[i].nurOpDay!=null){
					$('#opDate'+(i+1)).text(data[i].nurOpDay);
				}
			}
		}
	});
}

function queryTopWeek(){
	var mid = $('#mid').val();
	var nurPatid = $('#nurPatid').val();
	if(nurPatid==null||nurPatid==""){
		$.messager.alert('提示','请先选择患者，在进行操作');
		setTimeout(function(){$(".messager-body").window('close')},3500);
		return;
	}
	clear();
	var weekState = 2;
	queryDate(mid,weekState);
	queryPrient(nurPatid);
}

function queryBottenWeek(){
	var mid = $('#mid').val();
	var nurPatid = $('#nurPatid').val();
	if(nurPatid==null||nurPatid==""){
		$.messager.alert('提示','请先选择患者，在进行操作');
		setTimeout(function(){$(".messager-body").window('close')},3500);
		return;
	}
	clear();
	var weekState = 3;
	queryDate(mid,weekState);
	queryPrient(nurPatid);
}

function queryBenWeek(){
	var mid = $('#mid').val();
	var nurPatid = $('#nurPatid').val();
	if(nurPatid==null||nurPatid==""){
		$.messager.alert('提示','请先选择患者，在进行操作');
		setTimeout(function(){$(".messager-body").window('close')},3500);
		return;
	}
	clear();
	var weekState = 1;
	queryDate(mid,weekState);
	queryPrient(nurPatid);
}

function queryPrient(nurPatid){
	$.ajax({
		url: "<%=basePath%>emrs/threeTestTable/queryPrient.action",
		type:'post',
		data:{'maintenance.nurPatid':nurPatid},
		success: function(data) {
			$('#name').text(data.name);
			$('#sex').text(sexMap.get(data.sex));
			$('#age').text(data.age);
			$('#dept').text(data.dept);
			$('#medicalrecordId').text(data.medicalrecordId);
			$('#bedno').text(data.bedno);
			$('#dates').text(data.dates);
			var age = data.dates;
			var ages=DateOfBirth(age);
		    $('#age').text(ages.get("nianling")+ages.get("ageUnits"));
		}
	});
}

function clear(){
	for(var i=0;i<7;i++){
		$('#dateDay'+i).text('');
		$('#inDate'+i).text('');
		$('#opDate'+i).text('');
	}
}
</script>
<style type="text/css">
.panel-header{
	border-top:0
}
</style>
</head>
<body>
<div id="divLayout" class="easyui-layout" fit=true style="width: 100%;height: 100%;">
	<div data-options="region:'west',title:'护理患者树',split:true" style="width:300px;">
		<ul id="treePatientInfo"></ul>
	</div>
	<div data-options="region:'center',title:'护理信息列表'" style="padding:5px;width:100%">
		<div style="padding:5px 5px 5px 5px;">	
			<input id="nurPatid" type="hidden">
			<input id="mid" type="hidden">
			<table style="width:100%;border:1px solid #95b8e7;padding:5px;" class="changeskin">
				<tr>
					<td><a href="javascript:void(0)" onclick="queryBenWeek()" class="easyui-linkbutton threeTestFontColor" data-options="iconCls:'icon-add',plain:true">本周</a></td>
					<td><a href="javascript:void(0)" onclick="queryTopWeek()" class="easyui-linkbutton threeTestFontColor" data-options="iconCls:'icon-add',plain:true">上一周</a></td>
					<td><a href="javascript:void(0)" onclick="queryBottenWeek()" class="easyui-linkbutton threeTestFontColor" data-options="iconCls:'icon-add',plain:true">下一周</a></td>
				</tr>
			</table>   
		</div>
		<div style="padding:0px 5px 5px 5px;">
			<table style="width:100%">
				<tr>
					<td align="center"><font size="5" class="threeTestFont">郑州大学第一附属医院</font></td>
				</tr>
			</table>
		</div>
		<div style="padding:0px 5px 5px 5px;">
			<table style="width:100%">
				<tr>
					<td align="center"><font size="5" class="threeTestFont">体温单</font></td>
				</tr>
			</table>
		</div>
		<div style="padding:0px 5px 5px 5px;">
			<table style="width:100%">
				<tr>
					<td>姓名：&nbsp;<span id="name"></span></td>
					<td>年龄：&nbsp;<span id="age"></span></td>
					<td>性别：&nbsp;<span id="sex"></span></td>
					<td>科室：&nbsp;<span id="dept"></span></td>
					<td>床号：&nbsp;<span id="bedno"></span></td>
					<td>入院日期：&nbsp;<span id="dates"></span></td>
					<td>病历号：&nbsp;<span id="medicalrecordId"></span></td>
				</tr>
			</table>
		</div>
		<div style="padding:0px 5px 5px 5px;">
			<table style="width:100%"  class="tableCss">
				<tr>
					<td class="Input">日期</td>
					<td class="Input" id="dateDay1"></td>
					<td class="Input" id="dateDay2"></td>
					<td class="Input" id="dateDay3"></td>
					<td class="Input" id="dateDay4"></td>
					<td class="Input" id="dateDay5"></td>
					<td class="Input" id="dateDay6"></td>
					<td class="Input" id="dateDay7"></td>
				</tr>
				<tr>
					<td class="Input">住院天数</td>
					<td class="Input" id="inDate1"></td>
					<td class="Input" id="inDate2"></td>
					<td class="Input" id="inDate3"></td>
					<td class="Input" id="inDate4"></td>
					<td class="Input" id="inDate5"></td>
					<td class="Input" id="inDate6"></td>
					<td class="Input" id="inDate7"></td>
				</tr>
				<tr>
					<td class="Input">手术后天数</td>
					<td class="Input" id="opDate1"></td>
					<td class="Input" id="opDate2"></td>
					<td class="Input" id="opDate3"></td>
					<td class="Input" id="opDate4"></td>
					<td class="Input" id="opDate5"></td>
					<td class="Input" id="opDate6"></td>
					<td class="Input" id="opDate7"></td>
				</tr>
			</table>
		</div>
		<div id="main" style="height:500px"></div>
	</div>   
</div>
</body>
</html>