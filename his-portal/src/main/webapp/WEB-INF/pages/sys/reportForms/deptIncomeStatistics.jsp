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
<script
	src="${pageContext.request.contextPath}/javascript/echarts/echarts.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
</head>
<script type="text/javascript">
	var pieDayChart;
	var pieDayByAreaChart;
	var barDayChart;
	
	var pieColor = [ 
	                '#ff7f50', '#da70d6', '#32cd32', '#6495ed', 
	                '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0', 
	                '#1e90ff', '#ff6347', '#7b68ee', '#00fa9a', '#ffd700', 
	                '#6b8e23', '#ff00ff', '#3cb371', '#b8860b', '#30e0e0' 
	            ];
	var pieAreaColor = ['#ca8622', '#125906','#234497'];
	
	var pieDayTime;
	var pieDayValue = [
		                {value:5000, name:'西药费'},
		                {value:8000, name:'中成药费'},
		                {value:7600, name:'中草药费'}
		            ];
	var pieDayByAreaValue = [
		                {value:550, name:'郑东院区'},
		                {value:380, name:'河医院区'},
		                {value:766, name:'惠济院区'}
		            ];
	
	var dayBarTime = [];
	var dayBarName = [];
	var dayBarValue = [];
	var dayBarValueMom = [];
	var dayBarValueYoy = [];
	var dayBarTimeMom;
	var dayBarTimeYoy;
	var seriesDayMom = [];
	var seriesDayYoy = [];
	var seriesDay = [];
	
	var pieMonthChart;
	var pieMonthByAreaChart;
	var barMonthChart;
	
	var pieMonthTime;
	
	var monthBarTime = [];
	var monthBarName = [];
	var monthBarValue = [];
	var monthBarValueMom = [];
	var monthBarValueYoy = [];
	var monthBarTimeMom;
	var monthBarTimeYoy;
	var seriesMonthMom = [];
	var seriesMonthYoy = [];
	var seriesMonth = [];
	
	var pieYearChart;
	var pieYearByAreaChart;
	var barYearChart;
	
	var pieYearTime;
	
	var yearBarTime = [];
	var yearBarName = [];
	var yearBarValue = [];
	var yearBarValueMom = [];
	var yearBarTimeMom;
	var seriesYearMom = [];
	var seriesYear = [];
	
	var pieSelfChart;
	var pieSelfByAreaChart;
	var barSelfChart;
	
	var pieSelfTime;
	
	var menuAlias = '${menuAlias}';
	
	var flag;
	$(function(){
		//选择科室
		$(".deptInput").MenuList({
			width :530, //设置宽度，不写默认为530，不要加单位
			height :400, //设置高度，不写默认为400，不要加单位
			dropmenu:"#m2",//弹出层id，必须要写
			isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
			deptTypes:'I',	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
			firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias, //获取列表的url，必须要写
			relativeInput:".doctorInput",	//与其级联的文本框，必须要写
			relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
		});
		$('#m2 .addList h2 input').click();
		$('a[name=\'menu-confirm\']').click();
		flag=$('#ksnew').getMenuIds();
		$('a[name=\'menu-confirm-clear\']').click();
		if(flag==''){
			$("body").setLoading({
				id:"body",
				isImg:false,
				text:"无数据权限"
			});
		}else{
		//科室
		$.ajax({
			url: "<%=basePath%>baseinfo/department/getDeptMap.action",
			success: function(date) {
				deptMap = date;
					//科室下拉框
					 $.extend($.fn.datagrid.methods, {
					        autoMergeCells: function (jq, fields) {
					            return jq.each(function () {
					                var target = $(this);
					                if (!fields) {
					                    fields = target.datagrid("getColumnFields");
					                }
					                var rows = target.datagrid("getRows");
					                var i = 0,
					                j = 0,
					                temp = {};
					                for (i; i < rows.length; i++) {
					                    var row = rows[i];
					                    j = 0;
					                    for (j; j < fields.length; j++) {
					                        var field = fields[j];
					                        var tf = temp[field];
					                        if (!tf) {
					                            tf = temp[field] = {};
					                            tf[row[field]] = [i];
					                        } else {
					                            var tfv = tf[row[field]];
					                            if (tfv) {
					                                tfv.push(i);
					                            } else {
					                                tfv = tf[row[field]] = [i];
					                            }
					                        }
					                    }
					                }
					                $.each(temp, function (field, colunm) {
					                    $.each(colunm, function () {
					                        var group = this;
					                        if (group.length > 1) {
					                            var before,
					                            after,
					                            megerIndex = group[0];
					                            for (var i = 0; i < group.length; i++) {
					                                before = group[i];
					                                after = group[i + 1];
					                                if (after && (after - before) == 1) {
					                                    continue;
					                                }
					                                var rowspan = before - megerIndex + 1;
					                                if (rowspan > 1) {
					                                    target.datagrid('mergeCells', {
					                                        index: megerIndex,
					                                        field: field,
					                                        rowspan: rowspan
					                                    });
					                                }
					                                if (after && (after - before) != 1) {
					                                    megerIndex = after;
					                                }
					                            }
					                        }
					                    });
					                });
					            });
					        }
					    });
			}
		
		});
		

		//初始化时间
		 $("#timeDay").val('${sTime}');
		 $("#timeMonth").val('${sTime}'.split("-")[0]+"-"+'${sTime}'.split("-")[1]);
		 $("#timeYear").val('${sTime}'.split("-")[0]);
		 $("#timeSelfE").val('${sTime}');
		 $("#timeSelfS").val('${eTime}');
		 
		//初始化图例对象
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
		
		
		$("#pieDay").setLoading({
				id: "pieDayLoad",
				text: "无数据",
				opacity: 1,
				backgroudColor: "#ffffff",
				isImg: false
		 });
		$("#pieDayByArea").setLoading({
			id: "pieDayByAreaLoad",
			text: "无数据",
			opacity: 1,
			backgroudColor: "#ffffff",
			isImg: false
		 })
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
		$("#pieMonth").setLoading({
				id: "pieMonthLoad",
				text: "无数据",
				opacity: 1,
				backgroudColor: "#ffffff",
				isImg: false
		 })
			$("#pieMonthByArea").setLoading({
				id: "pieMonthByAreaLoad",
				text: "无数据",
				opacity: 1,
				backgroudColor: "#ffffff",
				isImg: false
		 })
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
		 $("#pieYear").setLoading({
			id: "pieYearLoad",
			text: "无数据",
			opacity: 1,
			backgroudColor: "#ffffff",
			isImg: false
		 })
		$("#pieYearByArea").setLoading({
			id: "pieYearByAreaLoad",
			text: "无数据",
			opacity: 1,
			backgroudColor: "#ffffff",
			isImg: false
		 })
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
		$("#pieSelf").setLoading({
			id: "pieSelfLoad",
			text: "无数据",
			opacity: 1,
			backgroudColor: "#ffffff",
			isImg: false
		 })
		$("#pieSelfByArea").setLoading({
			id: "pieSelfByAreaLoad",
			text: "无数据",
			opacity: 1,
			backgroudColor: "#ffffff",
			isImg: false
		 })
		/* pickedByDay();
		pickedByMonth();
		pickedByYear();
		pickedBySelf(); */
		$("#daySelect").combobox({
			onSelect: function(rec){
				if('yoy'==rec.value){//同比
					dayBarName[dayBarName.length-1] = '科室总收入同比';
					seriesDay = seriesDayYoy;
					dayBarTime = dayBarTimeYoy;
					setBar(dayBarName, dayBarTime, seriesDay, barDayByArea);
				}else{//环比
					dayBarName[dayBarName.length-1] = '科室总收入环比';
					seriesDay = seriesDayMom;
					dayBarTime = dayBarTimeMom;
					setBar(dayBarName, dayBarTime, seriesDay, barDayByArea);
				}
		      }
		});
		$("#monthSelect").combobox({
			onSelect: function(rec){
				if('yoy'==rec.value){//同比
					monthBarName[monthBarName.length-1] = '科室总收入同比';
					seriesMonth = seriesMonthYoy;
					monthBarTime = monthBarTimeYoy;
					setBar(monthBarName, monthBarTime, seriesMonth, barMonthByArea);
				}else{//环比
					monthBarName[monthBarName.length-1] = '科室总收入环比';
					seriesMonth = seriesMonthMom;
					monthBarTime = monthBarTimeMom;
					setBar(monthBarName, monthBarTime, seriesMonth, barMonthByArea);
		      }
			}
		});
		}
	});
	
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
   			    /* toolbox : {
				    	show:true,
				    	y : '50',
				    	feature:{
				    		restore:{show:true}
				    	}
				    }, */
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
	function setBar(barName, barTime, seriesValue, chartBar){
		var option = {
			    tooltip : {
			    	formatter: function (params, ticket, callback) {
			        	var i = "--";
			        	var nextNum = "0.01";
			        	if("0.00"!=seriesValue[params.seriesIndex].data[(params.dataIndex-1)]){
			        		nextNum = seriesValue[params.seriesIndex].data[(params.dataIndex-1)];
			        	}
			        	if((params.dataIndex-1)<0){
			        	}else{
						    i = ((Number(params.value)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
			        	}
					    var rc = (""+params.seriesName).substring(0,4);
					    return params.seriesName+"<br/>"+params.data+"万元<br/>增减量"+"<br/>"+i;
					},
					trigger: 'item',
			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			        },
			    },
			     legend: {
			    	selectedMode : false,
			        data: barName,
			    },
			    xAxis : [
			        {
// 			            type : 'category',
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
			            
			        }
			    ],
			    series : seriesValue
			};
		  // 为echarts对象加载数据 
		  chartBar.setOption(option); 
	}
	
	function pickedByDay(){
		seriesDayMom = [];
		seriesDayYoy = [];
		barDayByArea.clear(); 
		
		var dept = $('#ksnew').getMenuIds();
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
		
		//$("#DayTable").rmoveLoading("DayTableLoad")
		
		$.ajax({
			url:"<%=basePath%>statistics/deptIncome/deptIncomeCharts.action",
			data:{date:dayTime,dateSign:1,deptCodes:dept,menuAlias:menuAlias},
			success:function(data){
				$("#DayBox").rmoveLoading("DayBoxLoad") 
				pieDayValue = data.feeOfDay;
				pieDayByAreaValue = data.areaOfDay;
				
// 				$(".addList").children("h2").click();
				
				 if(pieDayValue.length==0){
					 $("#pieDay").setLoading({
							id: "pieDayLoad",
							text: "无数据",
							opacity: 1,
							backgroudColor: "#ffffff",
							isImg: false
					 })
					 /* $("#DayTable").setLoading({
							id: "DayTableLoad",
							text: "无数据",
							opacity: 1,
							backgroudColor: "#ffffff",
							isImg: false
					 }) */
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
				
				
				dayBarName = data.deptsName;
// 				var ksNew = $("#ksnew").val(dayBarName);
				
				dayBarName[dayBarName.length] = '科室总收入环比';
				var totalMom = [0,0,0,0,0,0];
				for(var i = 0;i<data.huanbiByDay.length;i++){
					var eachName = dayBarName[i];
					var dayBarValue1 = [];
					for(var j = 0; j<data.huanbiByDay[i].length ; j++){
						totalMom[j] = Number(totalMom[j])+Number(data.huanbiByDay[i][j]);
						dayBarValue1[j] = (Number(data.huanbiByDay[i][j])/10000).toFixed(2);
					}
					seriesDayMom[i] = {
			     		    name:dayBarName[i],
			     		    stack: dayBarName[0],
			     		    type:'bar',
			     		    data: dayBarValue1,
			     		    barWidth : 20
			     		};
				}
				for(var t = 0; t<totalMom.length; t++){
					totalMom[t] = (Number(totalMom[t])/10000).toFixed(2);
				}
				seriesDayMom[data.huanbiByDay.length] = {
		     		    name:'科室总收入环比',
		     		    type:'line',
		     		    data: totalMom,
		     		    barWidth : 20
		     		};
				
				var totalYoy = [0,0,0,0,0,0];
				for(var i = 0;i<data.tongbiByDay.length;i++){
					var eachName = dayBarName[i];
					var dayBarValue2 = [];
					for(var j = 0; j<data.tongbiByDay[i].length ; j++){
						totalYoy[j] = Number(totalYoy[j])+Number(data.tongbiByDay[i][j]);
						dayBarValue2[j] = (Number(data.tongbiByDay[i][j])/10000).toFixed(2);
					}
					seriesDayYoy[i] = {
			     		    name:dayBarName[i],
			     		    stack: dayBarName[0],
			     		    type:'bar',
			     		    data: dayBarValue2,
			     		    barWidth : 20
			     		};
				}
				for(var t = 0; t<totalYoy.length; t++){
					totalYoy[t] = (Number(totalYoy[t])/10000).toFixed(2);
				}
				seriesDayYoy[data.tongbiByDay.length] = {
		     		    name:'科室总收入同比',
		     		    type:'line',
		     		    data: totalYoy,
		     		    barWidth : 20
		     		};
				
				seriesDay = seriesDayMom;
				dayBarTimeMom = data.xAxisByHuanbiByDay;
				dayBarTimeYoy = data.xAxisByTongbiByDay;
				
				dayBarTime = dayBarTimeMom;
				dayTotCost = (Number(data.dayTotCost)/10000).toFixed(2);
				dayTitle = [{text: "(日)合计:" + dayTotCost + "万元",x:'center',y:'10'},{subtext : '(日)各科室收入',x:'left',y:'15',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
				
				if(dayBarName.length>5){
					dayBarName[5] = dayBarName[dayBarName.length-1];
					dayBarName.length = 6;
				}
				pieAreaColor = chooseAreaColor(pieDayByAreaValue);
				setPie(dayTitle,pieColor,pieDayValue,pieDayChart);
				setPie(dayAreaTitle,pieAreaColor,pieDayByAreaValue,pieDayByArea);
				setBar(dayBarName, dayBarTime, seriesDay, barDayByArea);
				}
			});
	}
	
	function pickedByMonth(){
		seriesMonthMom = [];
		seriesMonthYoy = [];
		seriesMonth = [];
		barMonthByArea.clear(); 
		
		var dept = $('#ksnew').getMenuIds();
		var MonthTime = $("#timeMonth").val().split("-")[0]+"-"+$("#timeMonth").val().split("-")[1];
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
		//$("#MonthTable").rmoveLoading("MonthTableLoad")
		
		$.ajax({
			url:"<%=basePath%>statistics/deptIncome/deptIncomeCharts.action",
			data:{date:MonthTime,dateSign:2,deptCodes:dept,menuAlias:menuAlias},
			success:function(data){
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
					 /*  $("#MonthTable").setLoading({
							id: "MonthTableLoad",
							text: "无数据",
							opacity: 1,
							backgroudColor: "#ffffff",
							isImg: false
					 }) */
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
				monthBarName = data.deptsName;
				monthBarName[monthBarName.length] = '科室总收入环比';
				var totalMom = [0,0,0,0,0,0];
				for(var i = 0;i<data.huanbiByMonth.length;i++){
					var eachName = monthBarName[i];
					var monthBarValue1 = [];
					for(var j = 0; j<data.huanbiByMonth[i].length ; j++){
						totalMom[j] = Number(totalMom[j])+Number(data.huanbiByMonth[i][j]);
						monthBarValue1[j] = (Number(data.huanbiByMonth[i][j])/10000).toFixed(2);
					}
					seriesMonthMom[i] = {
			     		    name:monthBarName[i],
			     		    stack: monthBarName[0],
			     		    type:'bar',
			     		    data: monthBarValue1,
			     		    barWidth : 20
			     		};
				}
				for(var t = 0; t<totalMom.length; t++){
					totalMom[t] = (Number(totalMom[t])/10000).toFixed(2);
				}
				seriesMonthMom[data.huanbiByMonth.length] = {
		     		    name:'科室总收入环比',
		     		    type:'line',
		     		    data: totalMom,
		     		    barWidth : 20
		     		};
				
				var totalYoy = [0,0,0,0,0,0];
				for(var i = 0;i<data.tongbiByMonth.length;i++){
					var eachName = monthBarName[i];
					var monthBarValue2 = [];
					for(var j = 0; j<data.tongbiByMonth[i].length ; j++){
						totalYoy[j] = Number(totalYoy[j])+Number(data.tongbiByMonth[i][j]);
						monthBarValue2[j] = (Number(data.tongbiByMonth[i][j])/10000).toFixed(2);
					}
					seriesMonthYoy[i] = {
			     		    name:monthBarName[i],
			     		    stack: monthBarName[0],
			     		    type:'bar',
			     		    data: monthBarValue2,
			     		    barWidth : 20
			     		};
				}
				for(var t = 0; t<totalYoy.length; t++){
					totalYoy[t] = (Number(totalYoy[t])/10000).toFixed(2);
				}
				seriesMonthYoy[data.tongbiByMonth.length] = {
		     		    name:'科室总收入同比',
		     		    type:'line',
		     		    data: totalYoy,
		     		    barWidth : 20
		     		};
				
				seriesMonth = seriesMonthMom;
				monthBarTimeMom = data.xAxisByHuanbiByMonth;
				monthBarTimeYoy = data.xAxisByTongbiByMonth;
				
				monthBarTime = monthBarTimeMom;
				monthTotCost = (Number(data.monthTotCost)/10000).toFixed(2);
				monthTitle = [{text: "(月)合计:" + monthTotCost + "万元",x:'center',y:'10'},{subtext : '(月)各科室收入',x:'left',y:'15',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
				
				
				if(monthBarName.length>5){
					monthBarName[5] = monthBarName[monthBarName.length-1];
					monthBarName.length = 6;
				}
				pieAreaColor = chooseAreaColor(pieMonthByAreaValue);
				setPie(monthTitle,pieColor,pieMonthValue,pieMonthChart);
				setPie(monthAreaTitle,pieAreaColor,pieMonthByAreaValue,pieMonthByArea);
				setBar(monthBarName, monthBarTime, seriesMonth, barMonthByArea);
				}
			});
	}
	
	
	function pickedByYear(){
		seriesYearMom = [];
		seriesYearYoy = [];
		seriesYear = [];
		barYearByArea.clear();
		
		var dept = $('#ksnew').getMenuIds();
		var YearTime = $("#timeYear").val().split("-")[0];
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
		//$("#YearTable").rmoveLoading("YearTableLoad")
		$.ajax({
			url:"<%=basePath%>statistics/deptIncome/deptIncomeCharts.action",
			data:{date:YearTime,dateSign:3,deptCodes:dept,menuAlias:menuAlias},
			success:function(data){
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
					/*  $("#YearTable").setLoading({
						id: "YearTableLoad",
						text: "无数据",
						opacity: 1,
						backgroudColor: "#ffffff",
						isImg: false
					 }) */
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
				yearBarName = data.deptsName;
				yearBarName[yearBarName.length] = '科室总收入环比';
				var totalMom = [0,0,0,0,0,0];
				for(var i = 0;i<data.huanbiByYear.length;i++){
					var eachName = yearBarName[i];
					var yearBarValue1 = [];
					for(var j = 0; j<data.huanbiByYear[i].length ; j++){
						totalMom[j] = Number(totalMom[j])+Number(data.huanbiByYear[i][j]);
						yearBarValue1[j] = (Number(data.huanbiByYear[i][j])/10000).toFixed(2);
					}
					seriesYearMom[i] = {
			     		    name:yearBarName[i],
			     		    stack: yearBarName[0],
			     		    type:'bar',
			     		    data: yearBarValue1,
			     		    barWidth : 20
			     		};
				}
				for(var t = 0; t<totalMom.length; t++){
					totalMom[t] = (Number(totalMom[t])/10000).toFixed(2);
				}
				seriesYearMom[data.huanbiByYear.length] = {
		     		    name:'科室总收入环比',
		     		    type:'line',
		     		    data: totalMom,
		     		    barWidth : 20
		     		};
				seriesYear = seriesYearMom;
				
				yearBarTime = data.xAxisByHuanbiByYear;
				YearTotCost = (Number(data.yearTotCost)/10000).toFixed(2);
				yearTitle = [{text: "(年)合计:" + YearTotCost + "万元",x:'center',y:'10'},{subtext : '(年)各科室收入',x:'left',y:'15',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
				
				
				if(yearBarName.length>5){
					yearBarName[5] = yearBarName[yearBarName.length-1];
					yearBarName.length = 6;
				}
				pieAreaColor = chooseAreaColor(pieYearByAreaValue);
				setPie(yearTitle,pieColor,pieYearValue,pieYearChart);
				setPie(yearAreaTitle,pieAreaColor,pieYearByAreaValue,pieYearByArea);
				setBar(yearBarName, yearBarTime, seriesYear, barYearByArea);
				}
			});
	}
	
	function pickedBySelf(){
		
		var dept = $('#ksnew').getMenuIds();
		var SelfTime = $("#timeSelfS").val()+","+$("#timeSelfE").val();
		pieSelfTime = SelfTime;
		pieSelfByAreaTime = SelfTime;
		$("#DivBox").setLoading({
			id: "DivBoxLoad",
			text: "计算中...",
		})
		$("#pieSelf").rmoveLoading("pieSelfLoad") 
		$("#pieSelfByArea").rmoveLoading("pieSelfByAreaLoad") 
		//$("#DivTable").rmoveLoading("DivTableLoad") 
		$.ajax({
			url:"<%=basePath%>statistics/deptIncome/deptIncomeCharts.action",
			data:{date:SelfTime,dateSign:4,deptCodes:dept,menuAlias:menuAlias},
			success:function(data){
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
					 /* $("#DivTable").setLoading({
						id: "DivTableLoad",
						text: "无数据",
						opacity: 1,
						backgroudColor: "#ffffff",
						isImg: false
					 }) */
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
				pieAreaColor = chooseAreaColor(pieSelfByAreaValue);
				selfTotCost = (Number(data.customTotCost)/10000).toFixed(2);
				selfTitle = [{text: "(年)合计:" + selfTotCost + "万元",x:'center',y:'10'},{subtext : '(年)各科室收入',x:'left',y:'15',subtextStyle:{color:'#000000',fontWeight:'bold',fontSize:'16'}}];
				setPie(selfTitle,pieColor,pieSelfValue,pieSelfChart);
				setPie(selfAreaTitle,pieAreaColor,pieSelfByAreaValue,pieSelfByArea);
				}
			});
	}
	
	$(function(){
		 $(".textbox").off("click").on("click",function(even){
			  var width = $("body").width()/4
			  var num = $(this).parent().attr("index")-0
			  $(".combo-p").css("display","none")
 			  if(num == 0){
				  $(".combo-p").eq(num).css({
					  "left": width*num+10+"px"
				  })
			  }
			  if(num == 1){
				  $(".combo-p").eq(num).css({
					  "left": width*num+5+"px"
				  })
			  }
			  if(num == 2){
				  $(".combo-p").eq(num).css({
					  "left": width*num+5+"px"
				  })
			  } 
				$(".combo-p").eq(num).css({
					"top":this.offsetTop+20+"px",
					"display":"block",
 					"position": "absolute",
					"zIndex": "110000",
					"width": "151px"
				})
			
			even.stopPropagation()
			return false
		})
		$(".textbox-text").off("click")
		$(".textbox-addon").off("click") 
	})
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
	

	function query(){
		pickedByDay();
		pickedByMonth();
		pickedByYear();
		pickedBySelf();
	}
	
	</script>
<body style="overflow-y: auto;position: relative;">
	<div  class="changeskin"
		style="height: 30px; width: 100%; padding: 5px 5px 5px 5px;">
		<table>
			<tr>
				<td style="width: 55px;" align="center">科室:</td>
				<td style="width: 120px;" class="newMenu">
					<div class="deptInput menuInput" style="width: 120px">
						<input style="width: 95px" class="ksnew" id="ksnew" 
							readonly="readonly" /><span></span>
					</div>
					<div id="m2" class="xmenu" style="display: none;">
						<div class="searchDept">
							<input type="text" name="searchByDeptName" placeholder="回车查询" />
							<span class="searchMenu"><i></i>查询</span> <a
								name="menu-confirm-cancel" href="javascript:void(0);"
								class="a-btn"> <span class="a-btn-text">取消</span>
							</a> <a name="menu-confirm-clear" href="javascript:void(0);"
								class="a-btn"> <span class="a-btn-text">清空</span>
							</a> <a name="menu-confirm" href="javascript:void(0);" class="a-btn">
								<span class="a-btn-text">确定</span>
							</a>
						</div>
						<div class="select-info" style="display: none">
							<label class="top-label">已选部门：</label>
							<ul class="addDept">

							</ul>
						</div>
						<div class="depts-dl">
							<div class="addList"></div>
							<div class="tip" style="display: none">没有检索到数据</div>
						</div>
					</div>
				</td>
				<td><a href="javascript:void(0)" class="easyui-linkbutton" onclick="query()" iconCls="icon-search">查询</a></td>
			</tr>
			
		</table>
		
	</div>
	<div id = "DayBox"  style="width: calc(25% - 3px); float: left;padding: 0 1px">
		<div style="height: 35px; width: 100%;">
			<table style="width: 100%; border: 1; padding: 5px 5px 5px 5px;">
				<tr>
					<td index = "0" style="width: 220px;">(日)统计: <input id="timeDay"
						class="Wdate" type="text"
						onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:pickedByDay,maxDate:'%y-%M-%d'})" />
					</td>
				</tr>
			</table>
		</div>
		<div style="height: 40%; width: 100%;">
			<div id="pieDay" style="height: 100%; width: 100%;"></div>
		</div>
		<div id = "pieDayByAreaBox" style="height: 40%; width: 100%;">
			<div id="pieDayByArea" style="height: 100%; width: 100%;"></div>
		</div>
		<div id = "barDayTitle" index = 0 style="height: 30px; width: calc(100% - 10px); margin-left: 10px">
			<input editable="false" class="easyui-combobox" id="daySelect"
				data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
					                   label: '科室总收入环比',  
					                   value: 'mom',  
					                   selected:true},  
					                   {label: '科室总收入同比',  
					                   value: 'yoy'}]" />
		</div>
		<div id="barDay" style="height: 40%; width: 100%"></div>
	</div>
	<div id = "MonthBox" class="changeskin"
		style="width: calc(25% - 3px); float: left; border-top: 0 !important; border-bottom: 0 !important;padding: 0 1px">
		<div style="height: 35px; width: 100%;">
			<table style="width: 100%; border: 1; padding: 5px 5px 5px 5px;">
				<tr>
					<td index = "1" >(月)统计: <input id="timeMonth" class="Wdate" type="text"
						onClick="WdatePicker({dateFmt:'yyyy-MM',onpicked:pickedByMonth,maxDate:'%y-%M-%d'})" />
					</td>
				</tr>
			</table>
		</div>
		<div style="height: 40%; width: 100%;">
			<div id="pieMonth" style="height: 100%; width: 100%;"></div>
		</div>
		<div id = "pieMonthByAreaBox" style="height: 40%; width: 100%;">
			<div id="pieMonthByArea" style="height: 100%; width: 100%;"></div>
		</div>
		<div id = "barMonthTitle" index = 1 style="height: 30px; width: calc(100% - 10px); margin-left: 10px">
			<input editable="false" class="easyui-combobox" id="monthSelect"
				data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
					                   label: '科室总收入环比',  
					                   value: 'mom',  
					                   selected:true},  
					                   {label: '科室总收入同比',  
					                   value: 'yoy'}]" />
		</div>
		<div id="barMonth" style="height: 40%; width: 100%"></div>
	</div>
	<div  id = "YearBox"  style="width: calc(25% - 4px); float: left;padding: 0 2px 0 1px">
		<div style="height: 35px; width: 100%;">
			<table style="width: 100%; border: 1; padding: 5px 5px 5px 5px;">
				<tr>
					<td index = "2"> (年)统计: <input id="timeYear" class="Wdate" type="text"
						onClick="WdatePicker({dateFmt:'yyyy',onpicked:pickedByYear,maxDate:'%y-%M-%d'})" />
					</td>
				</tr>
			</table>
		</div>
		<div style="height: 40%; width: 100%;">
			<div id="pieYear" style="height: 100%; width: 100%;"></div>
		</div>
		<div style="height: 40%; width: 100%;">
			<div id="pieYearByArea" style="height: 100%; width: 100%;"></div>
		</div>
		<div id = "barYearTitle" index = 2 style="height: 30px; width: calc(100% - 10px); margin-left: 10px">
			<input editable="false" class="easyui-combobox" id="YearSelect"
				data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
					                   label: '科室总收入环比',  
					                   value: 'mom',  
					                   selected:true}]" />
		</div>
		<div id="barYear" style="height: 40%; width: 100%;"></div>
	</div>
	<div id = "DivBox" class="changeskin"
		 style="width: calc(25% - 3px); float: left; border-bottom: 0 !important; border-top: 0 !important;padding: 0 1px">
		<div style="height: 35px; width: 100%;">
			<table style="width: 100%; border: false; padding: 5px 5px 5px 5px;">
				<tr>
					<td>自定义: <input style="width: 100px" id="timeSelfS" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',onpicked:pickedBySelf,maxDate:'#F{$dp.$D(\'timeSelfE\')}'})"  
						class="Wdate" type="text"/>
						至 <input style="width: 100px" id="timeSelfE" class="Wdate"
						type="text"
						onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:pickedBySelf,maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'timeSelfS\')}'})" />
					</td>
				</tr>

			</table>
		</div>
		<div style="height: 40%; width: 100%;">
			<div id="pieSelf" style="height: 100%; width: 100%;"></div>
		</div>
		<div style="height: 40%; width: 100%;">
			<div id="pieSelfByArea" style="height: 100%; width: 100%;"></div>
		</div>
		<div style="height: calc(40% + 32px); width: 100%"></div>
	</div>
</body>
</html>