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
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
    <script type="text/javascript">
    /*
     * 时间控件的点击事件
     */
     
     /* 
        $("body").setLoading("body")
		setTimeout(function (){
			$("body").rmoveLoading ("body")
		},5000) */
     
     
    	function pickedFunc(){
    		start();
    	}
       	//时间数组
	   	var arr=[];
	   	
	    /* top1 */
	    var costPer="00.00";
	    var lastCostPer="计算中...";
	    /*top2_1*/
	    var mDays=0;
	    /*top2_2*/
	    var avgCost=0.0;
	    /* top3 */
	    var totCost=[0,0,0,0,0,0,0,0,0,0,0,0];
	    var avgDays=[0,0,0,0,0,0,0,0,0,0,0,0];
	    /* mid1 */
	    var totCosts=[0,0,0,0,0,0,0,0,0,0,0,0];
	    var nums=[0,0,0,0,0,0,0,0,0,0,0,0];
	    var typeName=[];
	    var seriesmd=[];
	    /* mid2 */
	    var doctNameList=[];
	    var doctCostList=[];
	    var doctNumList=[];
	    /* bottom1 */
	    var deptNameList=[];
	    var deptCostList=[];
	    var deptNumList=[];
    $(function(){
    	top1();
     	top2_1();
     	top2_2();
     	top3();
     	mid1();
     	mid2();
     	bottom1();
     	bottom2();
    	start();
    });
    function start(){
    	
    	var  midTwo = $("#midTwo"),
    		 bottomOne = $("#bottomOne");
   		 midTwo.rmoveLoading ("midTwoload")
 	     bottomOne.rmoveLoading ("bottomOneload")
    	
    	var html1='';
    	var html2='';
    	/*
    	 * 获取选择月份 的前12个月的时间段
    	 */
    	 arr.splice(0,arr.length);//清空数组
    	 var str =$("#time").val();
  	     var myDate = new Date(str.replace(/-/,"/"))
    	 var month=myDate.getMonth()+1;//今年月份
    	 var year=myDate.getFullYear();//今年
    	 var yearl=year-1;//上一年
    	 var monthl=month+1;//上一年月份
    	 var time="";
    	 for(var i=0;i<12;i++){
    	 	for(var i=monthl;i<13;i++){
    	 		time=yearl+"-"+i;
    	 		arr.push(time);
    	 	}
    	 	for(var j=1;j<month+1;j++){
    	 		time=year+"-"+j;
    	 		arr.push(time);
    	 	}	 
    	 }
    	
    	$.ajax({ 
 		   url:"<%=basePath%>statistics/outpatientUseMedic/queryStatisticsCost.action",
 		   type:"post",
 		   data: {dateList:arr, date:$("#time").val()},
           dataType: "json",
 		   success:function(data){
 			  seriesmd.splice(0,seriesmd.length);//清空数组
 			  var map=data.maps;
 			  totCost= data.avgTotCosts;
 			  avgCost=totCost[11];
 			  typeName=data.typeName;
 			  for (var i = 0; i < typeName.length; i++) {
 				  if(typeName[i]=="总药费"){
 		 			  seriesmd.push({name:typeName[i],yAxisIndex:0,type:'line',smooth: true,data: (map[0][typeName[i]]),animation: true});
 				  }else{
	 			 	 seriesmd.push({name:typeName[i],yAxisIndex:1,type:'line',smooth: true,data: (map[0][typeName[i]]),animation: true});
 				  }
				}
 			  top3();
 			  mid1();
 			  top2_2();
 		   }
  	 	});
    	$.ajax({ 
  		   url:"<%=basePath%>statistics/outpatientUseMedic/queryMedicationDays.action",
  		   type:"post",
  		   data: {dateList:arr, date:$("#time").val()},
           dataType: "json",
  		   success:function(data){
  			  avgDays= data.avgDays;
  			  mDays=data.mDays;
  			  top3();
  			  top2_1();
  		   }
   	 	});
    	$.ajax({ 
  		   url:"<%=basePath%>statistics/outpatientUseMedic/queryCost.action",
  		   type:"post",
  		   data: {date:$("#time").val()},
           dataType: "json",
  		   success:function(data){
  			 costPer= data.costPer.toFixed(2);
  			 lastCostPer=data.lastCostPer;
  			 top1();
  		   }
   	   });
    	$.ajax({ 
   		   url:"<%=basePath%>statistics/outpatientUseMedic/queryDoctCost.action",
   		   type:"post",
   		   data: {date:$("#time").val()},
           dataType: "json",
   		   success:function(data){
   			 if(data.doctNameList.length==0){
   				 $("#midTwo").setLoading({
   					 id:"midTwoload",
   					 isImg:false,
   					 text:"暂无数据",
   					 opacity:1,
   				 	 backgroudColor:"#ffffff"
   				 })
   			 }else{
	   			doctNameList= data.doctNameList;
	   		 	doctCostList=data.doctCostList;
	   			doctNumList=data.doctNumList;
	   			mid2();
   			 }
   		   }
    	});
    	$.ajax({ 
    	   url:"<%=basePath%>statistics/outpatientUseMedic/queryDeptCost.action",
    	   type:"post",
    	   data: {date:$("#time").val()},
           dataType: "json",
    		  success:function(data){
	   			 if(data.deptNameList.length==0){
	   				 $("#bottomOne").setLoading({
	   					 id:"bottomOneload",
	   					 isImg:false,
	   					 text:"暂无数据",
	   					 opacity:1,
	   				 	 backgroudColor:"#ffffff"
	   				 })
	   			 }else{
		    		  deptNameList= data.deptNameList;
		    		  deptCostList=data.deptCostList;
		    		  deptNumList=data.deptNumList;
		    		  bottom1();
	   			 }
    		 }
     	});
    };
    
	// 药占比，仪表盘
	function top1(){
		require.config({
		    paths: {
		        echarts: '${pageContext.request.contextPath}/javascript/echarts'
		    }
		});
		
		require(
			    [
			        'echarts',
			        'echarts/chart/gauge' // 使用柱状图就加载bar模块，按需加载
			    ],
			    function (ec) {
		    var myChart2 = ec.init(document.getElementById('topOne'));
		    option1 = {
		    		title : {
				        text: '药占比',
				    },
		    	    series : [
		    	        {
		    	            name:'药占比',
		    	            type:'gauge',
		    	            center : ['50%', '60%'],    // 默认全局居中
		    	            radius : [0, '100%'],
		    	            startAngle: 160,
		    	            endAngle : 20,
		    	            min: 0,                     // 最小值
		    	            max: 100,                   // 最大值
		    	            precision: 0,               // 小数精度，默认为0，无小数点
		    	            splitNumber: 2,             // 分割段数，默认为5
		    	            axisLine: {            // 坐标轴线
		    	                show: true,        // 默认显示，属性show控制显示与否
		    	                lineStyle: {       // 属性lineStyle控制线条样式
		    	                    color: [[0.5, '#00ff00'],[1, '#ff4500']], 
		    	                    width: 36
		    	                }
		    	            },
		    	            axisTick: {            // 坐标轴小标记
		    	                show: 0,        // 属性show控制显示与否，默认不显示
		    	                splitNumber: 5,    // 每份split细分多少段
		    	                length :8,         // 属性length控制线长
		    	                lineStyle: {       // 属性lineStyle控制线条样式
		    	                    color: '#eee',
		    	                    width: 1,
		    	                    type: 'solid'
		    	                }
		    	            },
		    	            axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
		    	                show: true,
		    	                formatter: function(v){
		    	                    switch (v+''){
		    	                        case '0': return '0%';
		    	                        case '50': return '50%';
		    	                        case '100': return '100%';
		    	                        default: return '';
		    	                    }
		    	                },
		    	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
		    	                    color: '#333'
		    	                }
		    	            },
		    	            splitLine: {           // 分隔线
		    	                show: 0,        // 默认显示，属性show控制显示与否
		    	                length :30,         // 属性length控制线长
		    	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
		    	                    color: 'red',
		    	                    width: 2,
		    	                    type: 'solid'
		    	                }
		    	            },
		    	            pointer : {
		    	                length : '90%',
		    	                width : 2,
		    	                color : '#333'
		    	            },
		    	            title : {
		    	                show : true,
		    	                offsetCenter: [0, '45%'],       // x, y，单位px
		    	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
		    	                    color: '#333',
		    	                    fontSize : 8
		    	                }
		    	            },
		    	            detail : {
		    	                show : true,
		    	                backgroundColor: 'rgba(0,0,0,0)',
		    	                borderWidth: 0,
		    	                borderColor: '#ccc',
		    	                width: 100,
		    	                height: 40,
		    	                offsetCenter: [0, '10%'],       // x, y，单位px
		    	                formatter:'{value}%',
		    	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
		    	                    color: 'auto',
		    	                    fontSize : 8
		    	                }
		    	            },
		    	            data:[{value: costPer,name: "环比："+lastCostPer}]
		    	        }
		    	    ]
		    	};
		    setInterval(function () {
		        option1.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
		      
		    },2000); 
		      myChart2.setOption(option1, true);
			    }
		    );
	}

function top2_1(){
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	
	require(
		    [
		        'echarts',
		        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		   		var myChart2 = ec.init(document.getElementById('topTwo1'));
				option2 = {
					    title : {
					        text: '人均用药天数(天)',
					    },
					    tooltip : {
					        trigger: 'item',
					        formatter:function(a){ 
						        var relVal = ''; 
						        relVal = '人均用药天数：'; 
						        relVal += a[1]+'天'; 
						        return relVal; 
					        } 
					    },
					    calculable : true,
					    xAxis : [
					        {	
					        	splitLine : 0,
					            type : 'value',
					            boundaryGap : [0, 0.01]
					        }
					    ],
					    yAxis : [
					        {
					        	splitLine : 0,
					            axisLabel : 0,
					          	axisLine : 0,
					            type : 'category',
					            data : [mDays]
					        }
					    ],
					    series : [
					        {
					            name:$("#time").val(),
					            type:'bar',
					            data:[mDays]
					        }
					    ]
					};
				myChart2.setOption(option2);
		    });
}
function top2_2(){
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	
	require(
		    [
		        'echarts',
		        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		    	var myChart2_2 = ec.init(document.getElementById('topTwo2'));
				option2_2 = {
					    title : {
					        text: '人均药费(元)',
					    },
					    tooltip : {
					        trigger: 'item',
					        formatter:function(a){ 
						        var relVal = ''; 
						        relVal = '人均药费：'; 
						        relVal += a[1]+'元'; 
						        return relVal; 
					        } 
					    },
					    calculable : true,
					    xAxis : [
					        {	
					        	splitLine : 0,
					            type : 'value',
					            boundaryGap : [0, 0.01]
					        }
					    ],
					    yAxis : [
					        {
					        	splitLine : 0,
					            axisLabel : 0,
					          	axisLine : 0,
					            type : 'category',
					            data : [avgCost]
					        }
					    ],
					    series : [
					        {
					            name:$("#time").val(),
					            type:'bar',
					            data:[avgCost]
					        }
					    ]
					};
				myChart2_2.setOption(option2_2);
		    });
}
function top3(){
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	
	require(
		    [
		        'echarts',
		        'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
		        'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		   		var myChart3 = ec.init(document.getElementById('topThr'));
		   		option3 = {
		   				title: {
			                 text : '最近12个月的人均药费、人均用药天数',
			             },
		   			    tooltip : {
		   			        trigger: 'axis'
		   			    },

		   			    calculable : true,
		   			    legend: {
		   			    	x : 'right',
		   			        data:['(左)人均药费(元)','(右)人均用药天数(天)'],
		   			        
		   			    },
		   			    xAxis : [
		   			        {
		   			        	splitLine : 0,
		   			            type : 'category',
		   			            data :arr
		   			        }
		   			    ],
		   			    yAxis : [
		   			        {
		   			        	splitLine : 0,
		   			            type : 'value',
		   			            name : '费用',
		   			            min: 'dataMin',
			   			        axisLabel: {
					   		        formatter: '{value} 元'
					   		    },
		   			            splitNumber : 2
		   			        },
		   			        {
		   			            type : 'value',
		   			            name : '天数',
		   			            min: 'dataMin',
			   			        axisLabel: {
					   		        formatter: '{value} 天'
					   		    },
		   			            splitNumber : 2
		   			        }
		   			    ],
		   			    series : [

		   			        {
		   			            name:'(左)人均药费(元)',
		   			            type:'bar',
		   			            data:totCost
		   			        },
		   			        {
		   			            name:'(右)人均用药天数(天)',
		   			            type:'line',
		   			            yAxisIndex: 1,
		   			            data:avgDays
		   			        }
		   			    ]
		   			};
				myChart3.setOption(option3);
		    });
}
function mid1(){
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	
	require(
		    [
		        'echarts',
		        'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		   		var myChartM1 = ec.init(document.getElementById('midOne'));
		   		var colors = ['#5793f3', '#d14a61', '#675bba'];


		   		optionM1 = {
	   				title: {
		                 text : '最近12个月的药品金额、用药数量',
		             },
		   		    tooltip: {
		   		        trigger: 'axis'
		   		    },
		   		    legend: {
		   		    	x : 'right',
		   		        data: typeName
		   		    },
		   		    xAxis: {
		   		        type: 'category',
		   		        boundaryGap: false,
		   		        data: arr,
		   		        splitLine: {
		   		            show: false,
		   		            interval: 'auto'
		   		        }
		   		    },
		   		    yAxis: [{
		   		        name: '总药费用(万元)',
		   		        position: 'left',
		   		        axisLabel: {
		   		            formatter: '{value} 万元'
		   		        },
		   		        axisLine: {
		   		            lineStyle: {
		   		                color: colors[0]
		   		            }
		   		        },
		   		        splitLine: {
		   		            show: false
		   		        },
		   		        min: 'dataMin',
		   		     	splitNumber : 2
		   		    }, {
		   		        name: '用药数量(最小发药单位)',
		   		        position: 'right',
		   		        axisLabel: {
		   		            formatter: '{value}'
		   		        },
		   		        axisLine: {
		   		            lineStyle: {
		   		                color: colors[1]
		   		            }
		   		        },
		   		        splitLine: {
		   		            show: false
		   		        },
		   		        min: 'dataMin',
		   		     splitNumber : 2
		   		    }],
		   		    series: seriesmd
		   		};
				myChartM1.setOption(optionM1);
		    });
}
function mid2(){
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	
	require(
		    [
		        'echarts',
		        'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		   		var myChartM2 = ec.init(document.getElementById('midTwo'));
		   		var colors = ['#5793f3', '#d14a61', '#675bba'];

		   		optionM2 = {
	   				title: {
		                 text : '医生用药金额前5名',
		             },
		             tooltip : {
					    	trigger: 'item',
			    	        formatter: "{a} : {c}"
					    },

		   		    toolbox: {
		   		        show: true,
		   		        feature: {
		   		            saveAsImage: {}
		   		        }
		   		    },
		   		    legend: {
		   		        data: ['(左)总药费用(万元)', '(右)用药数量(最小发药单位)']
		   		    },
		   		    xAxis: {
		   		        type: 'category',
		   		        data: doctNameList,
		   		    },
		   		    yAxis: [{
		   		        name: '总药费用',
		   		        position: 'left',
		   		        min: 'dataMin',
		   		     	splitNumber: 2,
		   		        axisLabel: {
		   		            formatter: '{value} 万元'
		   		        },
		   		        axisLine: {
		   		            lineStyle: {
		   		                color: colors[0]
		   		            }
		   		        },
		   		        splitLine: {
		   		            show: false
		   		        }
		   		    }, {
		   		        name: '用药数量',
		   		        position: 'right',
		   		        min: 'dataMin',
		   		     	splitNumber: 2,
		   		        axisLabel: {
		   		            formatter: '{value} '
		   		        },
		   		        axisLine: {
		   		            lineStyle: {
		   		                color: colors[1]
		   		            }
		   		        },
		   		        splitLine: {
		   		            show: false
		   		        },
		   		    }],
		   		    series: [{
		   		        name: '(左)总药费用(万元)',
		   		        yAxisIndex: 0,
		   		       	type: 'bar',
		   		        smooth: true,
		   		        data: doctCostList,
		   		        animation: true

		   		    }, {
		   		        name: '(右)用药数量(最小发药单位)',
		   		        yAxisIndex: 1,
		   		        type: 'bar',
		   		        smooth: true,
		   		        data: doctNumList,
		   		        animation: true

		   		    }]
		   		};
				myChartM2.setOption(optionM2);
		    });
}
function bottom1(){
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	
	require(
		    [
		        'echarts',
		        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		   		var myChartB1 = ec.init(document.getElementById('bottomOne'));
		   		var colors = ['#5793f3', '#d14a61', '#675bba'];


		   		optionB1 = {
	   				title: {
		                 text : '科室用药金额前5名',
		             },
		   		    tooltip: {
		   		        trigger: 'axis'
		   		    },

		   		    toolbox: {
		   		        show: true,
		   		        feature: {
		   		            saveAsImage: {}
		   		        }
		   		    },
		   		    legend: {
		   		        data: ['(左)总药费用(万元)', '(右)用药数量(最小发药单位)']
		   		    },
		   		    xAxis: {
		   		        type: 'category',
		   		        data: deptNameList,
		   		    },
		   		    yAxis: [{
		   		        name: '总药费用',
		   		        position: 'left',
		   		        type: 'value',
		   		        min: 'dataMin',
		   		     	splitNumber: 2,
		   		        axisLabel: {
		   		            formatter: '{value} 万元'
		   		        },
		   		        axisLine: {
		   		            lineStyle: {
		   		                color: colors[0]
		   		            }
		   		        },
		   		        splitLine: {
		   		            show: false
		   		        },
		   		    }, {
		   		        name: '用药数量',
		   		        position: 'right',
		   		        type: 'value',
		   		        min: 'dataMin',
		   		     	splitNumber: 2,
		   		        axisLabel: {
		   		            formatter: '{value}'
		   		        },
		   		        axisLine: {
		   		            lineStyle: {
		   		                color: colors[1]
		   		            }
		   		        },
		   		        splitLine: {
		   		            show: false
		   		        },
		   		    }],
		   		    series: [{
		   		        name: '(左)总药费用(万元)',
		   		        yAxisIndex: 0,
		   		        type: 'bar',
		   		        smooth: true,
		   		        data: deptCostList,
		   		        animation: true

		   		    }, {
		   		        name: '(右)用药数量(最小发药单位)',
		   		        yAxisIndex: 1,
		   		        type: 'bar',
		   		        smooth: true,
		   		        data: deptNumList,
		   		        animation: true

		   		    }]
		   		};
				myChartB1.setOption(optionB1);
		    });
}
function bottom2(){
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	
	require(
		    [
		        'echarts',
		        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
	    var myChartB2 = ec.init(document.getElementById('bottomTwo'));
	    optionB2 = {
	    	    tooltip : {
	    	        trigger: 'item',
	    	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    	    },
	    	    legend: {
	    	        orient : 'horizontal',
	    	        x : 'center',
	    	        data:['大肠和小肠克罗恩','肾移植','额下淋巴上皮瘤','右肺癌术后化疗','肺癌晚期']
	    	    },

	    	    calculable : true,
	    	    series : [
	    	        {
	    	            name:'治疗效果',
	    	            type:'pie',
	    	            radius : '55%',
	    	            center: ['50%', '60%'],
	    	            data:[
	    	                {value:6012, name:'大肠和小肠克罗恩'},
	    	                {value:4914, name:'肾移植'},
	    	                {value:4754, name:'额下淋巴上皮瘤'},
	    	                {value:4747, name:'右肺癌术后化疗'},
	    	                {value:4745, name:'肺癌晚期'},
	    	            ]
	    	        }
	    	    ]
	    	};
	      myChartB2.setOption(optionB2, true);
		    }
	    );
}
</script>
</head>
<body>
	<div id="topTiltle" class="bottomLine" style=" width:100%; height:4%">
		<table style="width:100%;height:100%;">
	    		<tr>
<!-- 	    			<th style="font-size:16px;" align="left" class="outpatientFont"> -->
<!-- 	    				门诊用药监控 -->
<!-- 	    			</th> -->
	    			<th align="right" class="outpatientFont1">
	    				年月
		                 <input id="time" class="Wdate" type="text" value="${Etime}" onClick="WdatePicker({dateFmt:'yyyy-MM',onpicked:pickedFunc,maxDate:'%y-%M'})" />
	    			</th>
	    			<th style="width:22%">同比：与去年同月对比；环比：与上个月对比</th>
	    		</tr>
	    </table>
	</div>
	<div id="fir" class="bottomLine" style=" width:100%; height:33%">
	    <div id="topOne" style="width:25%;height:100%; float: left;"></div>
	    <div style="width:25%;height:100%;float: left;" >
	    	<div id="topTwo1" style="width:100%;height:50%;float: top;"></div>
	    	<div id="topTwo2" style="width:100%;height:50%;float: top;"></div>
	    </div>
	    <div id="topThr" style="width:50%;height:100%;float: left;" ></div>
	</div>
	
	<div id="sec" class="bottomLine" style="width:100%;height:31%">
		<div id="midOne" style="width:50%;height:100%; float: left;"></div>
	    <div id="midTwo" style="width:50%;height:100%;float: left;" ></div>
	</div>
	<div  id="thr" class="bottomLine" style="width:100%;height:31%;">
		<div id="bottomOne" style="width:50%;height:100%; float: left;"></div>
	    <div id="bottomThr" style="width:20%;height:100%; float: left;">
	    	<table style="width:100%;height:90%; float: left;">
	    		<tr style="font-size:16px;" align="left" class="outpatientFont">
	    			<th class="outpatientFont">人均费用前五单病种</th>
	    		</tr>
	    		<tr style="font-size:16px;" align="left" class="outpatientFont">
	    			<th class="outpatientFont">诊断</th>
	    			<th class="outpatientFont">人均药费</th>
	    		</tr>
	    		<tr>
	    			<td>大肠和小肠克罗恩</td>
	    			<td align="left">6012元</td>
	    		</tr>
	    		<tr bgcolor="#f5f5f5">
	    			<td>肾移植</td>
	    			<td align="left">4914元</td>
	    		</tr>
	    		<tr>
	    			<td>额下淋巴上皮瘤</td>
	    			<td align="left">4754元</td>
	    		</tr>
	    		<tr bgcolor="#f5f5f5">
	    			<td>右肺癌术后化疗</td>
	    			<td align="left">4747元</td>
	    		</tr>
	    		<tr>
	    			<td>肺癌晚期</td>
	    			<td align="left">4745元</td>
	    		</tr>
	    	</table>
	    </div>
	    <div id="bottomTwo" style="width:30%;height:100%; float: left;"></div>
	</div>
</body>
</html>