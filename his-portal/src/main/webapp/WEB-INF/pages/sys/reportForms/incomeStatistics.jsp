﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.min.js"></script>

<style type="text/css">
.tableInfo {
	width: 100%;
	height: 200px;
	padding-top: 1px; overflow-y : auto; border-bottom : 1px solid;
	border-color: #bfe8e8 !important;
	border-bottom: 1px solid;
	overflow-y: auto;
}

.TDlabel td {
	line-height: 27px;
	padding: 0 !important;
	text-align: center;
}

.tabletitle {
	position: absolute;
	width: 25%;
	top: 35px;
}

table tr td {
	width: 33.33%;
	text-indent: 10px;
	line-height: 25px;
	padding: 0 !important;
}
</style>
</head>
<body style="overflow-y: auto; position: relative; overflow-x: hidden">
	<div id="showBox">
		<div id="DayBox"
			style="width: calc(25% - 3px); float: left; padding: 0 1px;">
			<div style="height: 35px; width: 100%;">
				<table style="width: 100%; border: 1; padding: 5px 5px 5px 5px;">
					<tr>
						<td>(日)统计: <input id="timeDay" class="Wdate" type="text"
							onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:pickedByDay,maxDate:'%y-%M-%d'})" />
						</td>
					</tr>
				</table>
			</div>
			<div id="DayTable" class="tableInfo">
				<table class="tableCss tabletitle"
					style="border-collapse: collapse; border-spacing: 0;">
					<tr class="TDlabel">
						<td>费别</td>
						<td>金额(万元)</td>
						<td>比例</td>
					</tr>
				</table>
				<table id="dayDetai" class="tableCss"
					style="margin-top: 27px; width: 100%; border-collapse: collapse; border-spacing: 0;">

				</table>
			</div>
			<div style="height: 50%; width: 100%;">
				<div id="pieDay" style="height: 100%; width: 100%;"></div>
			</div>
			<div style="height: 50%; width: 100%;">
				<div id="pieDayByArea" style="height: 100%; width: 100%;"></div>
			</div>
			<div id=barDayTitle index="0"
				style="height: 30px; width: calc(100% - 10px); margin-left: 10px">
				<input editable="false" class="easyui-combobox" id="daySelect"
					data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
					                   label: '门诊总收入环比',  
					                   value: 'mom',  
					                   selected:true},  
					                   {label: '门诊总收入同比',  
					                   value: 'yoy'}]" />
			</div>
			<div id="barDay" style="height: 40%; width: 100%"></div>
		</div>
		<div id="MonthBox" class="changeskin"
			style="padding: 0 1px; width: calc(25% - 3px); float: left; border-top: 0 !important; border-bottom: 0 !important;">
			<div style="height: 35px; width: 100%;">
				<table style="width: 100%; border: 1; padding: 5px 5px 5px 5px;">
					<tr>
						<td>(月)统计: <input id="timeMonth" class="Wdate" type="text"
							onClick="WdatePicker({dateFmt:'yyyy-MM',onpicked:pickedByMonth,maxDate:'%y-%M-%d'})" />
						</td>
					</tr>
				</table>
			</div>
			<div id="MonthTable" class="tableInfo">
				<table class="tableCss tabletitle"
					style="border-collapse: collapse; border-spacing: 0;">
					<tr class="TDlabel">
						<td>费别</td>
						<td>金额(万元)</td>
						<td>比例</td>
					</tr>
				</table>
				<table id="MonthDetai" class="tableCss"
					style="margin-top: 27px; width: 100%; border-collapse: collapse; border-spacing: 0;">

				</table>
			</div>
			<div style="height: 50%; width: 100%;">
				<div id="pieMonth" style="height: 100%; width: 100%;"></div>
			</div>
			<div style="height: 50%; width: 100%;">
				<div id="pieMonthByArea" style="height: 100%; width: 100%;"></div>
			</div>
			<div id="barMonthTitle" index=1
				style="height: 30px; width: calc(100% - 10px); margin-left: 10px">
				<input editable="false" class="easyui-combobox" id="monthSelect"
					data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
					                   label: '门诊总收入环比',  
					                   value: 'mom',  
					                   selected:true},  
					                   {label: '门诊总收入同比',  
					                   value: 'yoy'}]" />
			</div>
			<div id="barMonth" style="height: 40%; width: 100%"></div>
		</div>
		<div id="YearBox"
			style=" padding: 0 2px 0  1px; width: calc(25% - 4px); float: left;">
			<div style="height: 35px; width: 100%;">
				<table style="width: 100%; border: 1; padding: 5px 5px 5px 5px;">
					<tr>
						<td>(年)统计: <input id="timeYear" class="Wdate" type="text"
							onClick="WdatePicker({dateFmt:'yyyy',onpicked:pickedByYear,maxDate:'%y-%M-%d'})" />
						</td>
					</tr>
				</table>
			</div>
			<div id="YearTable" class="tableInfo">
				<table class="tableCss tabletitle"
					style="border-collapse: collapse; border-spacing: 0;">
					<tr class="TDlabel">
						<td>费别</td>
						<td>金额(万元)</td>
						<td>比例</td>
					</tr>
				</table>
				<table id="YeayDetai" class="tableCss"
					style="margin-top: 27px; width: 100%; border-collapse: collapse; border-spacing: 0;">

				</table>
			</div>
			<div style="height: 50%; width: 100%;">
				<div id="pieYear" style="height: 100%; width: 100%;"></div>
			</div>
			<div style="height: 50%; width: 100%;">
				<div id="pieYearByArea" style="height: 100%; width: 100%;"></div>
			</div>
			<div id="barYearTitle" index=2
				style="height: 30px; width: calc(100% - 10px); margin-left: 10px">
				<input editable="false" class="easyui-combobox"
					data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
					                   label: '门诊总收入环比',  
					                   value: 'mom',  
					                   selected:true}]" />
				<!-- 									<input readonly="readonly" value="门急诊环比" style="border: 0;font-size: 14px"/>  -->
			</div>
			<div id="barYear" style="height: 40%; width: 100%;"></div>
		</div>
		<div id="DivBox" class="changeskin"
			style="padding: 0 1px; width: calc(25% - 3px); float: left; border-bottom: 0 !important; border-top: 0 !important;">
			<div style="height: 35px; width: 100%">
				<table style="width: 100%; border: false; padding: 5px 5px 5px 5px;">
					<tr>
						<td>自定义: <input style="width: 100px" id="timeSelfS"
							class="Wdate" type="text"
							onClick="WdatePicker({onpicked:pickedBySelf,maxDate:'#F{$dp.$D(\'timeSelfE\')}'})" />
							至 <input style="width: 100px" id="timeSelfE" class="Wdate"
							type="text"
							onClick="WdatePicker({onpicked:pickedBySelf,minDate:'#F{$dp.$D(\'timeSelfS\')}'})" />
						</td>
					</tr>
				</table>
			</div>
			<div id="DivTable" class="tableInfo">
				<table class="tableCss tabletitle"
					style="border-collapse: collapse; border-spacing: 0;">
					<tr class="TDlabel">
						<td>费别</td>
						<td>金额(万元)</td>
						<td>比例</td>
					</tr>
				</table>
				<table id="divDetai" class="tableCss"
					style="margin-top: 27px; width: 100%; border-collapse: collapse; border-spacing: 0;">

				</table>
			</div>
			<div style="height: 50%; width: 100%;">
				<div id="pieSelf" style="height: 100%; width: 100%;"></div>
			</div>
			<div style="height: 50%; width: 100%;">
				<div id="pieSelfByArea" style="height: 100%; width: 100%;"></div>
			</div>
			<div id="selfBar"
				style="height: 40%; width: 100%; margin-bottom: 30px"></div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var pieDayChart;
	var pieDayByArea; 
	var barDayByArea; 
	var dayBarName = ['门诊总收入','门诊总收入环比'];
	var pieDayValue = [];
	var pieDayByAreaValue = [];
	var dayBarValue = [];
	var dayBarValueMom = [];
	var dayBarValueYoy = [];
	var dayLineValueMom=[];//折线图数据
	var dayLineValueYoy=[];//折线图数据
	var dayBarTimeMom;
	var dayBarTimeYoy;
	var dayBarTime;
	var dayTitle;
	var dayAreaTitle;
	
	var pieMonthChart;
	var pieMonthByArea;
	var barMonthByArea;
	var MonthBarName = ['门诊总收入','门诊总收入环比'];
	var pieMonthValue = [];
	var pieMonthByAreaValue = [];
	var MonthBarValue = [];
	var MonthBarValueMom = [];
	var MonthBarValueYoy = [];
	var MonthLineValueMom=[];//折线图数据
	var MonthLineValueYoy=[];//折线图数据
	var MonthBarTimeMom;
	var MonthBarTimeYoy;
	var MonthBarTime;
	var monthTitle;
	var monthAreaTitle;

	
	var pieYearChart;
	var pieYearByArea;
	var barYearByArea;
	var YearBarName = ['门诊总收入','门诊总收入环比'];
	var pieYearValue = [];
	var pieYearByAreaValue = [];
	var YearBarValue = [];
	var YearLineValue=[];//折线图数据
	var YearBarTime;
	var yearTitle;
	var yearAreaTitle;	
	
	var pieSelfChart;
	var pieSelfByArea;
	var SelfBarName = ['门诊总收入','门诊总收入环比'];
	var pieSelfValue = [];
	var pieSelfByAreaValue = [];
	
	var pieColor = [ 
	                '#ff7f50', '#da70d6', '#32cd32', '#6495ed', 
	                '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0', 
	                '#1e90ff', '#ff6347', '#7b68ee', '#00fa9a', '#ffd700', 
	                '#6b8e23', '#ff00ff', '#3cb371', '#b8860b', '#30e0e0' 
	            ];
	var pieAreaColor = ['#653957','#61a0a8','#d48265'];
	$(function(){
		<!--遮罩-->
		/* $("body").setLoading({
			id:"body"
		}); */
		$("#daySelect").combobox('select', 'mom');
		$("#monthSelect").combobox('select', 'mom');
		
		//初始化时间
		 $("#timeDay").val('${sTime}');
		 $("#timeMonth").val('${sTime}'.split("-")[0]+"-"+'${sTime}'.split("-")[1]);
		 $("#timeYear").val('${sTime}'.split("-")[0]);
		 $("#timeSelfE").val('${sTime}');
		 $("#timeSelfS").val('${eTime}');
		<%--  $.ajax({
				url:"<%=basePath%>statistics/ReportForms/queryOutpatientCharts.action",
				data:{date:$("#timeSelfS").val()+","+$("#timeSelfE").val(),dateSign:0},
				success:function(data){
					dayTotCost =    (Number(data.dayTotCost)/10000).toFixed(2);
					monthTotCost =  (Number(data.monthTotCost)/10000).toFixed(2);
					yearTotCost  =  (Number(data.yearTotCost)/10000).toFixed(2);
					customTotCost = (Number(data.customTotCost)/10000).toFixed(2);
					/*
					 * 日
					 */
					pieDayValue = data.feeOfDay;
					for(var i = 0;i<data.huanbiByDay.length;i++){
						dayBarValueMom[i] = (Number(data.huanbiByDay[i])/10000).toFixed(2);
					}
					dayBarTimeMom = data.xAxisByHuanbiByDay;
					for(var i = 0;i<data.tongbiByDay.length;i++){
						dayBarValueYoy[i] = (Number(data.tongbiByDay[i])/10000).toFixed(2);
					}
					dayBarTimeYoy = data.xAxisByTongbiByDay;
					
					dayBarValue = dayBarValueMom;
					dayBarTime = dayBarTimeMom;
					 
					pieDayChart = echarts.init(document.getElementById('pieDay'));
					pieDayByArea = echarts.init(document.getElementById('pieDayByArea'));
					barDayByArea = echarts.init(document.getElementById('barDay'));
					dayTitle = [{text: "(日)合计:" + dayTotCost + "万元",x:'center',y:'25'},{subtext : '(日)各费别收入',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
					dayAreaTitle = {subtext : '(日)各院区收入',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}};
					
					setPie(dayTitle,pieColor,pieDayValue,pieDayChart);
					setPie(dayAreaTitle,pieAreaColor,pieDayByAreaValue,pieDayByArea);
					setBar(dayBarName, dayBarTime, dayBarValue, barDayByArea);
					
					
					/*
					 * 月
					 */		
					pieMonthValue = data.feeOfMonth;
					/* if(pieMonthValue.length==0){
						pieMonthValue = [{value:0, name:''}];
					} */
					pieMonthByAreaValue = data.areaOfMonth;
					
					for(var i = 0;i<data.huanbiByMonth.length;i++){
						MonthBarValueMom[i] = (Number(data.huanbiByMonth[i])/10000).toFixed(2);
					}
					MonthBarTimeMom = data.xAxisByHuanbiByMonth;
					for(var i = 0;i<data.tongbiByMonth.length;i++){
						MonthBarValueYoy[i] = (Number(data.tongbiByMonth[i])/10000).toFixed(2);
					}
					MonthBarTimeYoy = data.xAxisByTongbiByMonth;
					
					MonthBarValue = MonthBarValueMom;
					MonthBarTime = MonthBarTimeMom;

					pieMonthChart = echarts.init(document.getElementById('pieMonth'));
					pieMonthByArea = echarts.init(document.getElementById('pieMonthByArea'));
					barMonthByArea = echarts.init(document.getElementById('barMonth'));
					monthTitle = [{text: "(月)合计:" + monthTotCost + "万元",x:'center',y:'25'},{subtext : '(月)各费别收入',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
					monthAreaTitle = {subtext : '(月)各院区收入',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}};
					
					setPie(monthTitle,pieColor,pieMonthValue,pieMonthChart);
					setPie(monthAreaTitle,pieAreaColor,pieMonthByAreaValue,pieMonthByArea);
					setBar(MonthBarName, MonthBarTime, MonthBarValue, barMonthByArea);
					
					/*
					 * 年
					 */	
					pieYearValue = data.feeOfYear;
					pieYearByAreaValue = data.areaOfYear;
					pieAreaColor = chooseAreaColor(pieYearByAreaValue);
					for(var i = 0;i<data.huanbiByYear.length;i++){
						YearBarValue[i] = (Number(data.huanbiByYear[i])/10000).toFixed(2);
					}
					YearBarTime = data.xAxisByHuanbiByYear;
					
					pieYearChart = echarts.init(document.getElementById('pieYear'));
					pieYearByArea = echarts.init(document.getElementById('pieYearByArea'));
					barYearByArea = echarts.init(document.getElementById('barYear'));
					yearTitle = [{text: "(年)合计:" + yearTotCost + "万元",x:'center',y:'25'},{subtext : '(年)各费别收入',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
					yearAreaTitle = {subtext : '(年)各院区收入',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}};
					
					setPie(yearTitle,pieColor,pieYearValue,pieYearChart);
					setPie(yearAreaTitle,pieAreaColor,pieYearByAreaValue,pieYearByArea);
					setBar(YearBarName, YearBarTime, YearBarValue, barYearByArea);
					/*
					 * 自定义
					 */	
					pieSelfValue = data.feeOfCustom;
					pieSelfByAreaValue = data.areaOfCustom;
					pieSelfChart = echarts.init(document.getElementById('pieSelf'));
					pieSelfByArea = echarts.init(document.getElementById('pieSelfByArea'));
					selfTitle = [{text: "(自定义)合计:" + customTotCost + "万元",x:'center',y:'25'},{subtext : '(自定义)各费别收入',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
					selfAreaTitle = {subtext : '(自定义)各院区收入',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}};
					
					setPie(selfTitle,pieColor,pieSelfValue,pieSelfChart);
					setPie(selfAreaTitle,pieAreaColor,pieSelfByAreaValue,pieSelfByArea);
					<!--关闭遮罩-->
					$("body").rmoveLoading ("body")
					
					Loadres()
				}
		 }); --%>
			pieDayChart = echarts.init(document.getElementById('pieDay'));
			pieDayByArea = echarts.init(document.getElementById('pieDayByArea'));
			barDayByArea = echarts.init(document.getElementById('barDay'));
			pieMonthChart = echarts.init(document.getElementById('pieMonth'));
			pieMonthByArea = echarts.init(document.getElementById('pieMonthByArea'));
			barMonthByArea = echarts.init(document.getElementById('barMonth'));
			pieYearChart = echarts.init(document.getElementById('pieYear'));
			pieYearByArea = echarts.init(document.getElementById('pieYearByArea'));
			barYearByArea = echarts.init(document.getElementById('barYear'));
			pieSelfChart = echarts.init(document.getElementById('pieSelf'));
			pieSelfByArea = echarts.init(document.getElementById('pieSelfByArea'));
			dayAreaTitle = {subtext : '(日)各院区收入',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}};
			monthAreaTitle = {subtext : '(月)各院区收入',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}};
			yearAreaTitle = {subtext : '(年)各院区收入',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}};
			selfAreaTitle = {subtext : '(自定义)各院区收入',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}};
		 
			pickedByDay();
			pickedByMonth();
			pickedByYear();
			pickedBySelf();

		$("#daySelect").combobox({
			onSelect: function(rec){
				if('yoy'==rec.value){//同比
					dayBarName = ['门诊总收入','门诊总收入同比'];
					dayBarValue = dayBarValueYoy;
					dayBarTime = dayBarTimeYoy;
					setBar(dayBarName, dayBarTime, dayBarValue, barDayByArea,dayLineValueYoy);
				}else{//环比
					dayBarName = ['门诊总收入','门诊总收入环比'];
					dayBarValue = dayBarValueMom;
					dayBarTime = dayBarTimeMom;
					setBar(dayBarName, dayBarTime, dayBarValue, barDayByArea,dayLineValueMom);
				}
		      }
		});
		$("#monthSelect").combobox({
			onSelect: function(rec){
				if('yoy'==rec.value){//同比
					MonthBarName = ['门诊总收入','门诊总收入同比'];
					MonthBarValue = MonthBarValueYoy;
					MonthBarTime = MonthBarTimeYoy;
					setBar(MonthBarName, MonthBarTime, MonthBarValue, barMonthByArea,MonthLineValueYoy);
				}else{//环比
					MonthBarName = ['门诊总收入','门诊总收入环比'];
					MonthBarValue = MonthBarValueMom;
					MonthBarTime = MonthBarTimeMom;
					setBar(MonthBarName, MonthBarTime, MonthBarValue, barMonthByArea,MonthLineValueMom);
				}
		      }
		});
	});
	//设置table
	function setTable(database) {
		var htmlDay = ""
		for(var i = 0; i < database.data.length; i++) {
			htmlDay += "<tr><td>" + database.data[i].name + "</td>" +
				"<td style = 'text-align:right'>" + changeCost(database.data[i].value) + "</td>" +
				"<td style = 'text-align:right'>" + ((Number(database.data[i].value) / Number(database.TotCost)) * 100).toFixed(2) + "%</td></tr>";
		}
		return htmlDay;
	}
	//金额格式
	function changeCost(manny) {
		if(manny != null) {
			var value = manny/10000;
			value = parseFloat((value + "").replace(/[^\d\.-]/g, "")).toFixed(2) + "";
			var l = value.split(".")[0].split("").reverse(),
				r = value.split(".")[1],
				t = "";
			for(i = 0; i < l.length; i++) {
				t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
			}
			return t.split("").reverse().join("") + "." + r;
		} else {
			return "0.00";
		}
	}
	/* 
	 * 饼图
	 * 放置饼图共有参数
	 * 传echarts对象
	 */
	function setPie(pieTitle, pieColor, pieValue, chartPie){
		 var optionInit = {
   			  title : pieTitle,
   			    tooltip : {
   			        trigger: 'item',
   			        formatter: 
   			        	function (params, ticket, callback) {
								    return params.name+"("+params.percent+"%)<br/>"+(Number(params.value)/10000).toFixed(2)+"万元";
							},
   			    },
   			    toolbox : {
				    	show:true,
				    	y : '50',
				    	feature:{
				    		restore:{show:true}
				    	}
				    },
   			    calculable : true,//显示边框
   			    color : pieColor,
   			    series : [
   			        {
   			            name: '万元',
   			            type: 'pie',
   			            radius : '45%',
   			            center: ['50%', '55%'],
   			            data: pieValue,
   			            itemStyle : {
     			                normal : {
     			                    label : {
     			                        show : true,
     			                      	formatter: "{b}({d}%)",
     			                        textStyle:{
     			                        	 fontSize:'10'
     			                        }
     			                    }
     			                }
	            	          }
   			        }
   			    ]
   	    };
   	  // 为echarts对象加载数据 
   	  chartPie.setOption(optionInit); 
	}
	/* 
	 * 柱形图
	 * 放置柱形图共有参数
	 * 传echarts对象
	 */
	function setBar(barName, barTime, barValue, chartBar,lineValue){
		var option = {
			    tooltip : {
			    	formatter: function (params, ticket, callback) {
			    		if(params.componentSubType=='bar'){
			    			return '门诊总收入<br/>'+params.data+'万元';
			    		}else{
			    			return '增减率<br/>'+params.data+'%';
			    		}
// 			        	var i = "--";
// 			        	var nextNum = "1";
// 			        	if("0.00"!=barValue[(params.dataIndex-1)]){
// 			        		nextNum = barValue[(params.dataIndex-1)];
// 			        	}
// 			        	if((params.dataIndex-1)<0){
// 			        	}else{
// 						    i = ((Number(params.value)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
// 			        	}
// 					    var rc = (""+params.seriesName).substring(0,4);
// 					    return rc+"<br/>"+params.data+"万元<br/>增减量"+"<br/>"+i;
					},
					trigger: 'item',
			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			        },
			    },
			     legend: {
			        data: barName,
			    },
			    xAxis : [
			        {
			            type : 'category',
			            data : barTime,
			            axisLabel:{
			            	interval : 0 ,
				        	rotate : 30		
				        }
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value',
			            name:"(万元)"
			        }, {
			            type : 'value',
			            name:"(百分比)",
			            axisLabel: {
			                formatter: '{value} %'
			            }
			        }
			    ],
			    grid:{
			    	containLabel :true
			    },
			    color : ['#7B68EE','#E87529'],
			    series : [
			        {
			            name:barName[0],
			            type:'bar',
			            data: barValue,
			            barWidth : 20
			        },
			        {
			            name:barName[1],
			            type:'line',
			            yAxisIndex: 1,
			            data: lineValue
			        }
			    ]
			};
		  // 为echarts对象加载数据 
		  chartBar.setOption(option); 
	}
	
	/*
	 * 日----日期事件
	 */
	function pickedByDay(){
		$("#daySelect").combobox('select', 'mom');
		var dayTime = $("#timeDay").val();
		pieDayTime = dayTime;
		pieDayByAreaTime = dayTime;
		
		$("#DayBox").setLoading({
			id: "DayBoxLoad",
			text: "计算中...",
		}) 
		$("#pieDay").rmoveLoading("pieDayLoad") 
		$("#pieDayByArea").rmoveLoading("pieDayByAreaLoad") 
		$("#barDay").rmoveLoading("barDayLoad")
		$("#barDayTitle").rmoveLoading("barDayLoadTitleLoad")
		$("#DayTable").rmoveLoading("DayTableLoad")
		
		$.ajax({
			url:"<%=basePath%>statistics/ReportForms/queryOutpatientCharts.action",
			data:{date:dayTime,dateSign:1},
			success:function(data){
					dayTotCost = (Number(data.dayTotCost)/10000).toFixed(2);
					pieDayValue = data.feeOfDay;
					pieDayByAreaValue = data.areaOfDay;
					$("#DayBox").rmoveLoading("DayBoxLoad") 
					 if(pieDayValue.length==0){
						 $("#pieDay").setLoading({
								id: "pieDayLoad",
								text: "无数据",
								opacity: 1,
								backgroudColor: "#ffffff",
								isImg: false
						 })
						 $("#DayTable").setLoading({
								id: "DayTableLoad",
								text: "无数据",
								opacity: 1,
								backgroudColor: "#ffffff",
								isImg: false
						 })
					} 
					if(pieDayByAreaValue.length==0){
						 $("#pieDayByArea").setLoading({
							id: "pieDayByAreaLoad",
							text: "无数据",
							opacity: 1,
							backgroudColor: "#ffffff",
							isImg: false
						 })
					} 
					
					if(pieDayByAreaValue.length==0 && pieDayValue.length==0 ){
						$("#barDay").setLoading({
							id: "barDayLoad",
							text: "无数据",
							opacity: 1,
							backgroudColor: "#ffffff",
							isImg: false
						 })
						 $("#barDayTitle").setLoading({
								id: "barDayLoadTitleLoad",
								opacity: 1,
								backgroudColor: "#ffffff",
								isSmbox: false
							 })
					}
					$("#dayDetai").html(null).append(setTable({
						data:data.feeOfDay,
						TotCost:data.dayTotCost
					}))
					if($("#dayDetai").height() > 200) {
						$("#dayDetai").parent().children().eq(0).css("width", "calc(25% - 20px)")
					}
					dayLineValueMom=[];
					for(var i = 0;i<data.huanbiByDay.length;i++){
						var tempHuanbi=Number(data.huanbiByDay[i])/10000;
						dayBarValueMom[i] = (tempHuanbi).toFixed(2);
						lineValueEquesl(i,tempHuanbi,dayLineValueMom,dayBarValueMom);
					}
					dayBarTimeMom = data.xAxisByHuanbiByDay;
					dayLineValueYoy=[];
					for(var i = 0;i<data.tongbiByDay.length;i++){
						var tempTongbi=Number(data.tongbiByDay[i])/10000;
						dayBarValueYoy[i] = (tempTongbi).toFixed(2);
						lineValueEquesl(i,tempTongbi,dayLineValueYoy,dayBarValueYoy);
					}
					dayBarTimeYoy = data.xAxisByTongbiByDay;
					
					dayBarValue = dayBarValueMom;
					dayBarTime = dayBarTimeMom;
					dayTitle = [{text: "(日)合计:" + dayTotCost + "万元",x:'center',y:'10'},{subtext : '(日)各费别收入',x:'left',y:'15',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
					pieAreaColor = chooseAreaColor(pieDayByAreaValue);
					
					setPie(dayTitle,pieColor,pieDayValue,pieDayChart);
					setPie(dayAreaTitle,pieAreaColor,pieDayByAreaValue,pieDayByArea);
					setBar(dayBarName, dayBarTime, dayBarValue, barDayByArea,dayLineValueYoy);
					
				}
			
			});
	}
	/*
	 * 月----日期事件
	 */
	function pickedByMonth(){
		var MonthTime = $("#timeMonth").val();
		pieMonthTime = MonthTime;
		pieMonthByAreaTime = MonthTime;
		
		$("#MonthBox").setLoading({
			id: "MonthBoxLoad",
			text: "计算中...",
		}) 
		$("#pieMonth").rmoveLoading("pieMonthLoad") 
		$("#pieMonthByArea").rmoveLoading("pieMonthByAreaLoad") 
		$("#barMonth").rmoveLoading("barMonthLoad")
		$("#barMonthTitle").rmoveLoading("barMonthTitleLoad")
		$("#MonthTable").rmoveLoading("MonthTableLoad")
		
		$.ajax({
			url:"<%=basePath%>statistics/ReportForms/queryOutpatientCharts.action",
			data:{date:MonthTime,dateSign:2},
			success:function(data){
				$("#monthSelect").combobox('select', 'mom');
				MonthBarName = ['门诊总收入','门诊总收入环比'];
				monthTotCost =  (Number(data.monthTotCost)/10000).toFixed(2);
				pieMonthValue = data.feeOfMonth;
				pieMonthByAreaValue = data.areaOfMonth;
				$("#MonthBox").rmoveLoading("MonthBoxLoad") 
				if(pieMonthValue.length==0){
					 $("#pieMonth").setLoading({
							id: "pieMonthLoad",
							text: "无数据",
							opacity: 1,
							backgroudColor: "#ffffff",
							isImg: false
					 })
					  $("#MonthTable").setLoading({
							id: "MonthTableLoad",
							text: "无数据",
							opacity: 1,
							backgroudColor: "#ffffff",
							isImg: false
					 })
				}
				if(pieMonthByAreaValue.length==0){
					$("#pieMonthByArea").setLoading({
						id: "pieMonthByAreaLoad",
						text: "无数据",
						opacity: 1,
						backgroudColor: "#ffffff",
						isImg: false
				 })
				}
				 if(pieMonthByAreaValue.length==0 && pieMonthValue.length==0 ){
						$("#barMonth").setLoading({
							id: "barMonthLoad",
							text: "无数据",
							opacity: 1,
							backgroudColor: "#ffffff",
							isImg: false
					 })
					 $("#barMonthTitle").setLoading({
							id: "barMonthTitleLoad",
							opacity: 1,
							backgroudColor: "#ffffff",
							isSmbox: false
					 })
				 }
				 $("#MonthDetai").html(null).append(setTable({
						data:data.feeOfMonth,
						TotCost:data.monthTotCost
					}))
					if($("#MonthDetai").height() > 200) {
						$("#MonthDetai").parent().children().eq(0).css("width", "calc(25% - 20px)")
					}
				 MonthLineValueMom=[]
				for(var i = 0;i<data.huanbiByMonth.length;i++){
					var tempHuanbi=Number(data.huanbiByMonth[i])/10000;
					MonthBarValueMom[i] = (tempHuanbi).toFixed(2);
					lineValueEquesl(i,tempHuanbi,MonthLineValueMom,MonthBarValueMom);
				}
				MonthBarTimeMom = data.xAxisByHuanbiByMonth;
				MonthLineValueYoy=[];
				for(var i = 0;i<data.tongbiByMonth.length;i++){
					var tempTongbiValue=Number(data.tongbiByMonth[i])/10000;
					MonthBarValueYoy[i] = (tempTongbiValue).toFixed(2);
					lineValueEquesl(i,tempTongbiValue,MonthLineValueYoy,MonthBarValueYoy);
				}
				MonthBarTimeYoy = data.xAxisByTongbiByMonth;
				MonthBarValue = MonthBarValueMom;
				MonthBarTime = MonthBarTimeMom;
				monthTitle = [{text: "(月)合计:" + monthTotCost + "万元",x:'center',y:'10'},{subtext : '(月)各费别收入',x:'left',y:'15',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
				pieAreaColor = chooseAreaColor(pieMonthByAreaValue);
				
				setPie(monthTitle,pieColor,pieMonthValue,pieMonthChart);
				setPie(monthAreaTitle,pieAreaColor,pieMonthByAreaValue,pieMonthByArea);
				setBar(MonthBarName, MonthBarTime, MonthBarValue, barMonthByArea,MonthLineValueMom);
				
				}
			});
	}
	/*
	 * 年----日期事件
	 */
	function pickedByYear(){
		var YearTime = $("#timeYear").val();
		pieYearTime = YearTime;
		pieYearByAreaTime = YearTime;
		
		$("#YearBox").setLoading({
			id: "YearBoxLoad",
			text: "计算中...",
		}) 
		$("#pieYear").rmoveLoading("pieYearLoad") 
		$("#pieYearByArea").rmoveLoading("pieYearByAreaLoad") 
		$("#barYear").rmoveLoading("barYearLoad")
		$("#barYearTitle").rmoveLoading("barYearTitleLoad")
		$("#YearTable").rmoveLoading("YearTableLoad")
		$.ajax({
			url:"<%=basePath%>statistics/ReportForms/queryOutpatientCharts.action",
			data:{date:YearTime,dateSign:3},
			success:function(data){
					yearTotCost  =  (Number(data.yearTotCost)/10000).toFixed(2);
					pieYearValue = data.feeOfYear;
					pieYearByAreaValue = data.areaOfYear;
					$("#YearBox").rmoveLoading("YearBoxLoad") 
					if(pieYearValue.length==0){
						$("#pieYear").setLoading({
							id: "pieYearLoad",
							text: "无数据",
							opacity: 1,
							backgroudColor: "#ffffff",
							isImg: false
						 })
						 $("#YearTable").setLoading({
							id: "YearTableLoad",
							text: "无数据",
							opacity: 1,
							backgroudColor: "#ffffff",
							isImg: false
						 })
					}
					if(pieYearByAreaValue.length==0){
						$("#pieYearByArea").setLoading({
							id: "pieYearByAreaLoad",
							text: "无数据",
							opacity: 1,
							backgroudColor: "#ffffff",
							isImg: false
						 })
					}
					if(pieYearByAreaValue.length==0 && pieYearValue.length==0){
						$("#barYear").setLoading({
							id: "barYearLoad",
							text: "无数据",
							opacity: 1,
							backgroudColor: "#ffffff",
							isImg: false
						 })
						 $("#barYearTitle").setLoading({
							id: "barYearTitleLoad",
							opacity: 1,
							backgroudColor: "#ffffff",
							isSmbox: false
						 })
					}
				
					$("#YeayDetai").html(null).append(setTable({
						data:data.feeOfYear,
						TotCost:data.yearTotCost
					}))
					if($("#YeayDetai").height() > 200) {
						$("#YeayDetai").parent().children().eq(0).css("width", "calc(25% - 20px)")
					}
					YearLineValue=[];
					for(var i = 0;i<data.huanbiByYear.length;i++){
						var tempHuanbiValue=Number(data.huanbiByYear[i])/10000;
						YearBarValue[i] = Number(tempHuanbiValue).toFixed(2);
						lineValueEquesl(i,tempHuanbiValue,YearLineValue,YearBarValue);
					}
					YearBarTime = data.xAxisByHuanbiByYear;
					
					yearTitle = [{text: "(年)合计:" + yearTotCost + "万元",x:'center',y:'10'},{subtext : '(年)各费别收入',x:'left',y:'15',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
					pieAreaColor = chooseAreaColor(pieYearByAreaValue);
					
					setPie(yearTitle,pieColor,pieYearValue,pieYearChart);
					setPie(yearAreaTitle,pieAreaColor,pieYearByAreaValue,pieYearByArea);
					setBar(YearBarName, YearBarTime, YearBarValue, barYearByArea,YearLineValue);
					
					
				}
			});
	}
	/*
	 * 自定义----日期事件
	 */
	function pickedBySelf(){
		if($("#timeSelfS").val()>$("#timeSelfE").val()){
			$.messager.alert("提示","开始时间不能大于结束时间！");
			close_alert();
			return;
		}
		
		var SelfTime = $("#timeSelfS").val()+","+$("#timeSelfE").val();
		pieSelfTime = SelfTime;
		pieSelfByAreaTime = SelfTime;
		
		$("#DivBox").setLoading({
			id: "DivBoxLoad",
			text: "计算中...",
		})
		$("#pieSelf").rmoveLoading("pieSelfLoad") 
		$("#pieSelfByArea").rmoveLoading("pieSelfByAreaLoad") 
		$("#DivTable").rmoveLoading("DivTableLoad") 
		$.ajax({
			url:"<%=basePath%>statistics/ReportForms/queryOutpatientCharts.action",
			data:{date:SelfTime,dateSign:4},
			success:function(data){
				customTotCost = (Number(data.customTotCost)/10000).toFixed(2);
				pieSelfValue = data.feeOfCustom;
				pieSelfByAreaValue = data.areaOfCustom;
				$("#DivBox").rmoveLoading("DivBoxLoad")
				if(pieSelfValue.length==0){
					$("#pieSelf").setLoading({
						id: "pieSelfLoad",
						text: "无数据",
						opacity: 1,
						backgroudColor: "#ffffff",
						isImg: false
					 })
					 $("#DivTable").setLoading({
						id: "DivTableLoad",
						text: "无数据",
						opacity: 1,
						backgroudColor: "#ffffff",
						isImg: false
					 })
				}
				if(pieSelfByAreaValue.length==0){
					$("#pieSelfByArea").setLoading({
						id: "pieSelfByAreaLoad",
						text: "无数据",
						opacity: 1,
						backgroudColor: "#ffffff",
						isImg: false
					 })
				}
				$("#divDetai").html(null).append(setTable({
					data:data.feeOfCustom,
					TotCost:data.customTotCost
				}))
				if($("#divDetai").height() > 200) {
					$("#divDetai").parent().children().eq(0).css("width", "calc(25% - 20px)")
				}
				
				selfTitle = [{text: "(自定义)合计:" + customTotCost + "万元",x:'center',y:'10'},{subtext : '(自定义)各费别收入',x:'left',y:'15',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
				pieAreaColor = chooseAreaColor(pieSelfByAreaValue);
				
				setPie(selfTitle,pieColor,pieSelfValue,pieSelfChart);
				setPie(selfAreaTitle,pieAreaColor,pieSelfByAreaValue,pieSelfByArea);
				
			}
		});
	}
// 	var pieAreaColor = ['#653957','#61a0a8','#d48265'];
	function chooseAreaColor(pieValue){
		var colorList = [];
		for(var i = 0;i<pieValue.length;i++){
			if("河医院区"==pieValue[i].name){
    			colorList[i]='#61a0a8';
    		}else if("郑东院区"==pieValue[i].name){
    			colorList[i]='#653957';
    		}else if("惠济院区"==pieValue[i].name){
    			colorList[i]= '#d48265';
    		}
		}
		return colorList;
	}
	
// 	$(function(){
// 		$(".textbox").off("click").on("click",function(even){
// 			$(".combo-p").css({
// 				"display":"none",
// 			})
// 			  var width = $("body").width()/4
// 			  var num = $(this).parent().attr("index")-0
//  			  if(num == 0){
// 				  $(".combo-p").eq(num).css({
// 					  "left": width*num+10+"px"
// 				  })
// 			  }
// 			  if(num == 1){
// 				  $(".combo-p").eq(num).css({
// 					  "left": width*num+5+"px"
// 				  })
// 			  }
// 			  if(num == 2){
// 				  $(".combo-p").eq(num).css({
// 					  "left": width*num+5+"px"
// 				  })
// 			  } 
// 				$(".combo-p").eq(num).css({
// 					"top":this.offsetTop+20+"px",
// 					"display":"block",
//  					"position": "absolute",
// 					"zIndex": "110000",
// 					"width": "151px"
// 				})
			
// 			even.stopPropagation()
// 			return false
// 		})
// 		$(".textbox-text").off("click")
// 		$(".textbox-addon").off("click")
// 	})
function lineValueEquesl(i,tempValue,tempLineValue,tempBarValue){
		if(i==0){
			tempLineValue[i]='0.00'
		}else{
			var tempLine=Number(tempBarValue[i-1]);
			if(tempValue==0){
				tempLineValue[i]=-100.00;
			}else if(tempLine==0){
				tempLineValue[i]=100.00;
			}else{
				tempLineValue[i]=parseFloat((tempValue-tempLine)/tempLine*100).toFixed(2);
			}
			
		}
	}
	</script>
</html>