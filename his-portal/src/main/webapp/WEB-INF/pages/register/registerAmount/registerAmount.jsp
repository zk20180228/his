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
canvas {
	margin: 0
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
						<td>挂号分类</td>
						<td>例数</td>
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
					                   label: '挂号量环比',  
					                   value: 'mom',  
					                   selected:true},  
					                   {label: '挂号量同比',  
					                   value: 'yoy'}]" />
			</div>
			<div id="barDay" style="height: 40%; width: 100%;"></div>
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
						<td>挂号分类</td>
						<td>例数</td>
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
					                   label: '挂号量环比',  
					                   value: 'mom',  
					                   selected:true},  
					                   {label: '挂号量同比',  
					                   value: 'yoy'}]" />
			</div>
			<div id="barMonth" style="height: 40%; width: 100%;"></div>
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
						<td>挂号分类</td>
						<td>例数</td>
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
					                   label: '挂号量环比',  
					                   value: 'mom',  
					                   selected:true}]" />
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
						<td>挂号分类</td>
						<td>例数</td>
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
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.min.js"></script>
<script type="text/javascript">
	var pieDayChart;
	var pieDayByArea; 
	var barDayByArea; 
	var dayBarName = ['挂号量','挂号量环比'];
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
	var MonthBarName = ['挂号量','挂号量环比'];
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
	var YearBarName = ['挂号量','挂号量环比'];
	var pieYearValue = [];
	var pieYearByAreaValue = [];
	var YearBarValue = [];
	var YearLineValue=[];//折线图数据
	var YearBarTime;
	
	var yearTitle;
	var yearAreaTitle;	
	
	var pieSelfChart;
	var pieSelfByArea;
	var SelfBarName = ['挂号量','挂号量环比'];
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
		$("#daySelect").combobox('select', 'mom');
		$("#monthSelect").combobox('select', 'mom');
		
		//初始化时间
		 $("#timeDay").val('${sTime}');
		 $("#timeMonth").val('${sTime}'.split("-")[0]+"-"+'${sTime}'.split("-")[1]);
		 $("#timeYear").val('${sTime}'.split("-")[0]);
		 $("#timeSelfE").val('${eTime}');
		 $("#timeSelfS").val('${sTime}');
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
			dayAreaTitle = {subtext : '(日)挂号级别',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}};
			monthAreaTitle = {subtext : '(月)挂号级别',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}};
			yearAreaTitle = {subtext : '(年)挂号级别',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}};
			selfAreaTitle = {subtext : '(自定义)挂号级别',x:'left',y:'30',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}};
		 
			pickedByDay();
			pickedByMonth();
			pickedByYear();
			pickedBySelf();

		$("#daySelect").combobox({
			onSelect: function(rec){
				if('yoy'==rec.value){//同比
					dayBarName = ['挂号量','挂号量同比'];
					dayBarValue = dayBarValueYoy;
					dayBarTime = dayBarTimeYoy;
					setBar(dayBarName, dayBarTime, dayBarValue, barDayByArea,dayLineValueYoy);
				}else{//环比
					dayBarName = ['挂号量','挂号量环比'];
					dayBarValue = dayBarValueMom;
					dayBarTime = dayBarTimeMom;
					setBar(dayBarName, dayBarTime, dayBarValue, barDayByArea,dayLineValueMom);
				}
		      }
		});
		$("#monthSelect").combobox({
			onSelect: function(rec){
				if('yoy'==rec.value){//同比
					MonthBarName = ['挂号量','挂号量同比'];
					MonthBarValue = MonthBarValueYoy;
					MonthBarTime = MonthBarTimeYoy;
					setBar(MonthBarName, MonthBarTime, MonthBarValue, barMonthByArea,MonthLineValueYoy);
				}else{//环比
					MonthBarName = ['挂号量','挂号量环比'];
					MonthBarValue = MonthBarValueMom;
					MonthBarTime = MonthBarTimeMom;
					setBar(MonthBarName, MonthBarTime, MonthBarValue, barMonthByArea,MonthLineValueMom);
				}
		      }
		});
	});
	//设置table
	function setTable(database) {
		var htmlDay = "";
		var totalNum=database.TotCost;
		for(var i = 0; i < database.data.length; i++) {
			var htm='--';
			if(Number(totalNum)!=0){
				htm=((Number(database.data[i].value) / Number(totalNum)) * 100).toFixed(2)+'%';
			}
			htmlDay += "<tr><td>" + database.data[i].name + "</td>" +
				"<td style = 'text-align:right'>" + database.data[i].value+ "</td>" +
				"<td style = 'text-align:right'>" + htm + "</td></tr>";
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
								    return params.name+"("+params.percent+"%)<br/>"+(Number(params.value))+"例";
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
   			            name: '例',
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
			    			return '挂号量'+params.data+'例';
			    		}else{
			    			return '增减率'+params.data+'%';
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
// 					    return rc+"<br/>"+params.data+"例<br/>增减率"+"<br/>"+i;
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
			            name:"(例)",
			           
			        }
			        , {
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
			url:"<%=basePath%>statistics/RegisterInfoGzltj/queryRegisterCharts.action",
			data:{date:dayTime,dateSign:1},
			success:function(data){
					dayTotCost = (Number(data.total));
					pieDayValue = data.fee;
					pieDayByAreaValue = data.area;
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
						data:data.fee,
						TotCost:data.total
					}))
					if($("#dayDetai").height() > 200) {
						$("#dayDetai").parent().children().eq(0).css("width", "calc(25% - 20px)")
					}
					dayBarTimeMom=[];
					dayLineValueMom=[];
					for(var i = 0;i<data.Huanbi.length;i++){
						var tempHuanbiValue=(Number(data.Huanbi[i].value));
						dayBarValueMom[i] = tempHuanbiValue;
						lineValueEquesl(i,tempHuanbiValue,dayLineValueMom,dayBarValueMom);
						dayBarTimeMom.push(data.Huanbi[i].name)
					}
					dayBarTimeYoy=[];
					dayLineValueYoy=[];
					for(var i = 0;i<data.Tongbi.length;i++){
						var tempTongbi= (Number(data.Tongbi[i].value));
						dayBarValueYoy[i] =tempTongbi;
						lineValueEquesl(i,tempTongbi,dayLineValueYoy,dayBarValueYoy);
						dayBarTimeYoy.push(data.Tongbi[i].name)
					}
					dayBarValue = dayBarValueMom;
					dayBarTime = dayBarTimeMom;
					dayTitle = [{text: "(日)合计:" + dayTotCost + "例",x:'center',y:'10'},{subtext : '(日)各挂号分类例数',x:'left',y:'15',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
					pieAreaColor = chooseAreaColor(pieDayByAreaValue);
					
					setPie(dayTitle,pieColor,pieDayValue,pieDayChart);
					setPie(dayAreaTitle,pieAreaColor,pieDayByAreaValue,pieDayByArea);
					setBar(dayBarName, dayBarTime, dayBarValue, barDayByArea,dayLineValueMom);
					
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
			url:"<%=basePath%>statistics/RegisterInfoGzltj/queryRegisterCharts.action",
			data:{date:MonthTime,dateSign:2},
			success:function(data){
				$("#monthSelect").combobox('select', 'mom');
				MonthBarName = ['挂号量','挂号量环比'];
				monthTotCost =  (Number(data.total));
				pieMonthValue = data.fee;
				pieMonthByAreaValue = data.area;
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
						data:pieMonthValue,
						TotCost:data.total
					}))
				if($("#MonthDetai").height() > 200) {
					$("#MonthDetai").parent().children().eq(0).css("width", "calc(25% - 20px)")
				}
				 //环比
				 MonthBarTimeMom=[];
				 MonthLineValueMom=[];
				for(var i = 0;i<data.Huanbi.length;i++){
					var tempHuanbi=(Number(data.Huanbi[i].value));
					MonthBarValueMom[i] = tempHuanbi;
					lineValueEquesl(i,tempHuanbi,MonthLineValueMom,MonthBarValueMom);
					MonthBarTimeMom.push(data.Huanbi[i].name)
				}
				MonthBarTimeYoy=[];
				MonthLineValueYoy=[];
				for(var i = 0;i<data.Tongbi.length;i++){
					var tempTongbiValue=(Number(data.Tongbi[i].value));
					MonthBarValueYoy[i] = tempTongbiValue;
					lineValueEquesl(i,tempTongbiValue,MonthLineValueYoy,MonthBarValueYoy);
					MonthBarTimeYoy.push(data.Tongbi[i].name)
				}
				
				MonthBarValue = MonthBarValueMom;
				MonthBarTime = MonthBarTimeMom;
				
				monthTitle = [{text: "(月)合计:" + monthTotCost + "例",x:'center',y:'10'},{subtext : '(月)各挂号分类例数',x:'left',y:'15',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
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
			url:"<%=basePath%>statistics/RegisterInfoGzltj/queryRegisterCharts.action",
			data:{date:YearTime,dateSign:3},
			success:function(data){
					yearTotCost  =  (Number(data.total));
					pieYearValue = data.fee;
					pieYearByAreaValue = data.area;
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
						data:data.fee,
						TotCost:data.total
					}))
					if($("#YeayDetai").height() > 200) {
						$("#YeayDetai").parent().children().eq(0).css("width", "calc(25% - 20px)")
					}
					YearBarTime=[];
					YearLineValue=[];
					for(var i = 0;i<data.Huanbi.length;i++){
						var tempHuanbiValue=(Number(data.Huanbi[i].value));
						YearBarValue[i] = tempHuanbiValue;
						lineValueEquesl(i,tempHuanbiValue,YearLineValue,YearBarValue);
						YearBarTime.push(data.Huanbi[i].name);
					}
					yearTitle = [{text: "(年)合计:" + yearTotCost + "例",x:'center',y:'10'},{subtext : '(年)各挂号分类例数',x:'left',y:'15',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
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
			url:"<%=basePath%>statistics/RegisterInfoGzltj/queryRegisterCharts.action",
			data:{date:SelfTime,dateSign:4},
			success:function(data){
				customTotCost = (Number(data.total));
				pieSelfValue = data.fee;
				pieSelfByAreaValue = data.area;
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
					data:data.fee,
					TotCost:data.total
				}))
				if($("#divDetai").height() > 200) {
					$("#divDetai").parent().children().eq(0).css("width", "calc(25% - 20px)")
				}
				
				selfTitle = [{text: "(自定义)合计:" + customTotCost + "例",x:'center',y:'10'},{subtext : '(自定义)各挂号分类收入',x:'left',y:'15',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
				pieAreaColor = chooseAreaColor(pieSelfByAreaValue);
				setPie(selfTitle,pieColor,pieSelfValue,pieSelfChart);
				setPie(selfAreaTitle,pieAreaColor,pieSelfByAreaValue,pieSelfByArea);
				
			}
		});
	}
	function chooseAreaColor(pieValue){
		var colorList = [];
		for(var i = 0;i<pieValue.length;i++){
			if("国家级知名专家"==pieValue[i].name){
    			colorList[i]='#61a0a8';
    		}else if("省级知名专家"==pieValue[i].name){
    			colorList[i]='#653957';
    		}else if("知名专家"==pieValue[i].name){
    			colorList[i]= '#d48265';
    		}else if("教授"==pieValue[i].name){
    			colorList[i]= '#6CA6CD';
    		}else if("副教授"==pieValue[i].name){
    			colorList[i]= '#6959CD';
    		}else if("简易门诊"==pieValue[i].name){
    			colorList[i]= '#556B2F';
    		}else if("一般医生"==pieValue[i].name){
    			colorList[i]= '#388E8E';
    		}else if("主治医生"==pieValue[i].name){
    			colorList[i]= '#1C86EE';
    		}else if("老年优诊"==pieValue[i].name){
    			colorList[i]= '#00EEEE';
    		}else if("视力诊查费"==pieValue[i].name){
    			colorList[i]= '#7A7A7A';
    		}else if("居民健身卡"==pieValue[i].name){
    			colorList[i]= '#7171C6';
    		}else if("其他"==pieValue[i].name){
    			colorList[i]= '#8B5F65';
    		}
		}
		return colorList;
	}
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