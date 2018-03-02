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
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript">
var colorYQ = [ 
               '#ff7f50', '#da70d6', '#6495ed', 
               '#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0', 
               '#1e90ff', '#ff6347', '#7b68ee',  
               '#6b8e23', '#ff00ff', '#3cb371', '#b8860b', '#30e0e0' 
           ];
var colorArea = ['#653957','#61a0a8','#d48265'];            

var showBarByDay;
var BarByMonth;
var showBarByYear;
var show;
var show1;
var show2;
var show3;
var value1=0;
var value2=0;
var value3=0;
var value4=0;
var value5=0;
var value6=0;
var value7=0;
var value8=0;
var name;
var name1;
var name2;
var name3;
var initDay = '${Etime}';
var initMonth = initDay.split("-")[0]+"-"+initDay.split("-")[1];
var initYear = initDay.split("-")[0];



var dayBarTime = [];
var dayBarTime1 = [];
var dayBarTime2 = [];
var dayBarValue = [];
var dayBarValue1 = [];
var dayBarValue2 = [];
var monthBarTime = [];
var monthBarTime1 = [];
var monthBarTime2 = [];
var monthBarValue = [];
var monthBarValue1 = [];
var monthBarValue2 = [];
var yearBarTime = [];
var yearBarTime1 = [];
var yearBarTime2 = [];
var yearBarValue = [];
var yearBarValue1 = [];
var yearBarValue2 = [];
var selfBarValue1 = [];
var selfBarValue2 = [];

var barByMonthName =  ['门急诊人次','门急诊环比'];

momYoyFlag=true;

function pickedFuncByDay(){
	show.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
	value1=0;
	value2=0;
	//按天统计--------------staType:"1"
	$.ajax({
		url:"<%=basePath%>statistics/toListView/queryToListView.action",
		type:'post',
		data:{date:$("#timeDay").val(),staType:1},
		success:function(data){
			$("#dySelect").combobox('setValue', 'mom');
			$("#dySelect").combobox('setText', '门急诊环比');
			barByMonthName =  ['门急诊人次','门急诊环比'];
			$("#list").datagrid('loadData', data.toView);
			value1=data.toView[0].passengers;
			value2=data.toView[1].passengers;
			name=$("#timeDay").val();
			functione();
			dayMomYoy();
			var pieValue = [
			                {value:'', name:'郑东院区'},
			                {value:'', name:'惠济院区'},
			                {value:'', name:'河医院区'}
			            ];
			if(""!=data.areaJson){
				if(data.areaJson.length>2){
					pieValue = JSON.parse(data.areaJson);
					colorArea = chooseAreaColor(pieValue);
				}
			}
			pieArea("areaByDay", "日", pieValue, colorArea);
		}
	});
	<%-- $("#list").datagrid({
		url:"<%=basePath%>statistics/toListView/queryToListView.action",
		queryParams:{date:$("#timeDay").val(),staType:1},
		onLoadSuccess:function(data){
			$("#dySelect").combobox('setValue', 'mom');
			$("#dySelect").combobox('setText', '门急诊环比');
			barByMonthName =  ['门急诊人次','门急诊环比'];
			value1=data.rows[0].passengers;
			value2=data.rows[1].passengers;
			name=$("#timeDay").val();
			functione();
			dayMomYoy();
		}
	}); --%>
// 	start();
}

function pickedFuncByMonth(){
	value3=0;
	value4=0;
	show1.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
	$.ajax({
		url:"<%=basePath%>statistics/toListView/queryToListView.action",
		type:'post',
		data:{date:$("#timeMonth").val(),staType:2},
		success:function(data){
			$("#mySelect").combobox('setValue', 'mom');
			$("#mySelect").combobox('setText', '门急诊环比');
			barByMonthName =  ['门急诊人次','门急诊环比'];
			$("#list1").datagrid('loadData', data.toView);
			value3=data.toView[0].passengers;
			value4=data.toView[1].passengers;
			name1=$("#timeMonth").val();
			functione1();
			monthMomYoy();
			var pieValue = [
			                {value:'', name:'郑东院区'},
			                {value:'', name:'惠济院区'},
			                {value:'', name:'河医院区'}
			            ];
			if(""!=data.areaJson){
				if(data.areaJson.length>2){
					pieValue = JSON.parse(data.areaJson);
					colorArea = chooseAreaColor(pieValue);
				}
			}
			pieArea("areaByMonth", "月", pieValue, colorArea);
		}
	});
	//按月统计--------------staType:"2"
	<%-- $("#list1").datagrid({
		url:"<%=basePath%>statistics/toListView/queryToListView.action",
		queryParams:{date:$("#timeMonth").val(),staType:2},
		onLoadSuccess:function(data){
			$("#mySelect").combobox('setValue', 'mom');
			$("#mySelect").combobox('setText', '门急诊环比');
			barByMonthName =  ['门急诊人次','门急诊环比'];
			value3=data.rows[0].passengers;
			value4=data.rows[1].passengers;
			name1=$("#timeMonth").val();
			functione1();
			monthMomYoy();
		}
	}); --%>
	
// 	funBarByMonth();
// 	start();
}
function pickedFuncByYear(){
	value5=0;
	value6=0;
	show2.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
// 	functione2();
	//按年统计--------------staType:"3"
	$.ajax({
		url:"<%=basePath%>statistics/toListView/queryToListView.action",
		type:'post',
		data:{date:$("#timeYear").val(),staType:3},
		success:function(data){
			$("#list2").datagrid('loadData', data.toView);
			value5=data.toView[0].passengers;
			value6=data.toView[1].passengers;
			name2=$("#timeYear").val();
			functione2();
			yearMomYoy();
			var pieValue = [
			                {value:'', name:'郑东院区'},
			                {value:'', name:'惠济院区'},
			                {value:'', name:'河医院区'}
			            ];
			if(""!=data.areaJson){
				if(data.areaJson.length>2){
					pieValue = JSON.parse(data.areaJson);
					colorArea = chooseAreaColor(pieValue);
				}
			}
			pieArea("areaByYear", "年", pieValue, colorArea);
		}
	});
	<%-- $("#list2").datagrid({
		url:"<%=basePath%>statistics/toListView/queryToListView.action",
		queryParams:{date:$("#timeYear").val(),staType:3},
		onLoadSuccess:function(data){
			value5=data.rows[0].passengers;
			value6=data.rows[1].passengers;
			name2=$("#timeYear").val();
			functione2();
			yearMomYoy();
		}
	}); --%>
// 	start();
}
function pickedFuncBySelf(){
	$("#timeSelfS").val();
	$("#timeSelfE").val();
	if($("#timeSelfS").val()>$("#timeSelfE").val()){
	      $.messager.alert("提示","开始时间不能大于结束时间！");
	      return ;
	    }
	value7=0;
	value8=0;
	show3.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
// 	functione3();
	//自定义统计--------------staType:"4"
	$.ajax({
		url:"<%=basePath%>statistics/toListView/queryToListView.action",
		type:'post',
		data:{date:$("#timeSelfS").val()+","+$("#timeSelfE").val(),staType:4},
		success:function(data){
			$("#list3").datagrid('loadData', data.toView);
			value7=data.toView[0].passengers;
			value8=data.toView[1].passengers;
			name3=$("#timeSelfS").val()+"至"+$("#timeSelfE").val();
			functione3();
			var pieValue = [
			                {value:'', name:'郑东院区'},
			                {value:'', name:'惠济院区'},
			                {value:'', name:'河医院区'}
			            ];
			if(""!=data.areaJson){
				if(data.areaJson.length>2){
					pieValue = JSON.parse(data.areaJson);
					colorArea = chooseAreaColor(pieValue);
				}
			}
			pieArea("areaBySelf", "自定义", pieValue, colorArea);
		}
	});
	<%-- $("#list3").datagrid({
		url:"<%=basePath%>statistics/toListView/queryToListView.action",
		queryParams:{date:$("#timeSelfS").val()+","+$("#timeSelfE").val(),staType:4},
		onLoadSuccess:function(data){
			value7=data.rows[0].passengers;
			value8=data.rows[1].passengers;
			name3=$("#timeSelfS").val()+"至"+$("#timeSelfE").val();
			functione3();
			/* selfBarValue1[0]=data.rows[0].addPerLastM;
			selfBarValue1[1]=data.rows[1].addPerLastM;
			selfBarValue1[2]=data.rows[2].addPerLastM;
			selfBarValue2[0]=data.rows[0].addPerLastY;
			selfBarValue2[1]=data.rows[1].addPerLastY;
			selfBarValue2[2]=data.rows[2].addPerLastY;
			funBarBySelf(); */
		}
	}); --%>
// 	start();
}

$(function(){
	$('#mySelect').combobox({
		onSelect: function(rec){
			if('yoy'==rec.value){//同比
				monthBarValue = monthBarValue2;
				monthBarTime = monthBarTime2;
				barByMonthName = ['门急诊人次','门急诊同比'];
				funBarByMonth();
			}else{//环比
				monthBarValue = monthBarValue1;
				barByMonthName = ['门急诊人次','门急诊环比'];
				monthBarTime = monthBarTime1;
				funBarByMonth();
			}
	      }
	});
	$('#dySelect').combobox({
		onSelect: function(rec){
			if('yoy'==rec.value){//同比
				dayBarValue = dayBarValue2;
				dayBarTime = dayBarTime2;
				barByMonthName = ['门急诊人次','门急诊同比'];
				funBarByDay();
			}else{//环比
				dayBarValue = dayBarValue1;
				barByMonthName = ['门急诊人次','门急诊环比'];
				dayBarTime = dayBarTime1;
				funBarByDay();
			}
	      }
	});
	start();
});	

function start(){
	//按天统计--------------staType:"1"
	$.ajax({
		url:"<%=basePath%>statistics/toListView/queryToListView.action",
		type:'post',
		async:false,
		data:{date:$("#timeDay").val(),staType:1},
		success:function(data){
			$("#list").datagrid('loadData', data.toView);
			value1=data.toView[0].passengers;
			value2=data.toView[1].passengers;
			$("#timeDay").val('${Etime}');
			name=$("#timeDay").val();
			functione();
			dayMomYoy();
			var pieValue = [
			                {value:'', name:'郑东院区'},
			                {value:'', name:'惠济院区'},
			                {value:'', name:'河医院区'}
			            ];
			if(""!=data.areaJson){
				if(data.areaJson.length>2){
					pieValue = JSON.parse(data.areaJson);
					colorArea = chooseAreaColor(pieValue);
				}
			}
			pieArea("areaByDay", "日", pieValue, colorArea);
		}
	});
	//按月统计
	$.ajax({
		url:"<%=basePath%>statistics/toListView/queryToListView.action",
		type:'post',
		async:false,
		data:{date:$("#timeDay").val(),staType:2},
		success:function(data){
			$("#list1").datagrid('loadData', data.toView);
			value3=data.toView[0].passengers;
			value4=data.toView[1].passengers;
			$("#timeDay").val('${Etime}');
			initDay = '${Etime}';
			initMonth = initDay.split("-")[0]+"-"+initDay.split("-")[1];
			$("#timeMonth").val(initMonth);
			name1=$("#timeMonth").val();
			functione1();
			var pieValue =  [
				                {value:0, name:'郑东院区'},
				                {value:0, name:'惠济院区'},
				                {value:0, name:'河医院区'}
				            ];
			if(""!=data.areaJson){
				if(data.areaJson.length>2){
					pieValue = JSON.parse(data.areaJson);
					colorArea = chooseAreaColor(pieValue);
				}
			}
			pieArea("areaByMonth", "月", pieValue, colorArea);
		}
	});
	//按年统计
	$.ajax({
		url:"<%=basePath%>statistics/toListView/queryToListView.action",
		type:'post',
		async:false,
		data:{date:$("#timeDay").val(),staType:3},
		success:function(data){
			$("#list2").datagrid('loadData', data.toView);
			value5=data.toView[0].passengers;
			value6=data.toView[1].passengers;
			initDay = '${Etime}';
			initMonth = initDay.split("-")[0]+"-"+initDay.split("-")[1];
			initYear = initDay.split("-")[0];
			$("#timeYear").val(initYear);
			name2=$("#timeYear").val();
			functione2();
			var pieValue = [
			                {value:'0', name:'郑东院区'},
			                {value:'0', name:'惠济院区'},
			                {value:'0', name:'河医院区'}
			            ];
			if(""!=data.areaJson){
				if(data.areaJson.length>2){
					pieValue = JSON.parse(data.areaJson);
					colorArea = chooseAreaColor(pieValue);
				}
			}
			pieArea("areaByYear", "年", pieValue, colorArea);
		}
	});
	//自定义时间统计
	$.ajax({
		url:"<%=basePath%>statistics/toListView/queryToListView.action",
		type:'post',
		async:false,
		data:{date:$("#timeSelfS").val()+","+$("#timeSelfE").val(),staType:4},
		success:function(data){
			$("#list3").datagrid('loadData', data.toView);
			value7=data.toView[0].passengers;
			value8=data.toView[1].passengers;
			name3=$("#timeSelfS").val()+"至"+$("#timeSelfE").val();
			functione3();
			var pieValue = [
			                {value:0, name:'郑东院区'},
			                {value:0, name:'惠济院区'},
			                {value:0, name:'河医院区'}
			            ];
			if(""!=data.areaJson){
				if(data.areaJson.length>2){
					pieValue = JSON.parse(data.areaJson);
					colorArea = chooseAreaColor(pieValue);
				}
			}
			pieArea("areaBySelf", "自定义", pieValue, colorArea);
		}
	});
}
//计算月统计同环比
function monthMomYoy(){
	//六天同环比等待图层
// 	BarByMonth.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
	$.ajax({
		url:"<%=basePath%>statistics/toListView/querySixMomYoy.action",
		data:{date:$("#timeMonth").val(),staType:2},
		async:true,
		success:function(data){
			monthBarValue1[5] = data[0].nowMJNum
			monthBarValue1[4] = data[0].nowMJNumB1
			monthBarValue1[3] = data[0].nowMJNumB2
			monthBarValue1[2] = data[0].nowMJNumB3
			monthBarValue1[1] = data[0].nowMJNumB4
			monthBarValue1[0] = data[0].nowMJNumB5
			monthBarValue2[5] = data[1].nowMJNum
			monthBarValue2[4] = data[1].nowMJNumB1
			monthBarValue2[3] = data[1].nowMJNumB2
			monthBarValue2[2] = data[1].nowMJNumB3
			monthBarValue2[1] = data[1].nowMJNumB4
			monthBarValue2[0] = data[1].nowMJNumB5
			monthBarTime1[5] = data[0].nowTime
			monthBarTime1[4] = data[0].nowTimeB1
			monthBarTime1[3] = data[0].nowTimeB2
			monthBarTime1[2] = data[0].nowTimeB3
			monthBarTime1[1] = data[0].nowTimeB4
			monthBarTime1[0] = data[0].nowTimeB5
			monthBarTime2[5] = data[1].nowTime
			monthBarTime2[4] = data[1].nowTimeB1
			monthBarTime2[3] = data[1].nowTimeB2
			monthBarTime2[2] = data[1].nowTimeB3
			monthBarTime2[1] = data[1].nowTimeB4
			monthBarTime2[0] = data[1].nowTimeB5
			monthBarValue = monthBarValue1;
			monthBarTime = monthBarTime1;
			funBarByMonth();
			if(momYoyFlag){
				yearMomYoy();
				momYoyFlag=false;
			}
		}
	});
}
function dayMomYoy(){
	//六天同环比等待图层
// 	showBarByDay.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
	$.ajax({
		url:"<%=basePath%>statistics/toListView/querySixMomYoy.action",
		data:{date:$("#timeDay").val(),staType:1},
		async:true,
		success:function(data){
			dayBarValue1[0] = data[0].nowMJNum
			dayBarValue1[1] = data[0].nowMJNumB1
			dayBarValue1[2] = data[0].nowMJNumB2
			dayBarValue1[3] = data[0].nowMJNumB3
			dayBarValue1[4] = data[0].nowMJNumB4
			dayBarValue1[5] = data[0].nowMJNumB5
			dayBarValue2[0] = data[1].nowMJNum
			dayBarValue2[1] = data[1].nowMJNumB1
			dayBarValue2[2] = data[1].nowMJNumB2
			dayBarValue2[3] = data[1].nowMJNumB3
			dayBarValue2[4] = data[1].nowMJNumB4
			dayBarValue2[5] = data[1].nowMJNumB5
			dayBarTime1[5] = data[0].nowTime
			dayBarTime1[4] = data[0].nowTimeB1
			dayBarTime1[3] = data[0].nowTimeB2
			dayBarTime1[2] = data[0].nowTimeB3
			dayBarTime1[1] = data[0].nowTimeB4
			dayBarTime1[0] = data[0].nowTimeB5
			dayBarTime2[5] = data[1].nowTime
			dayBarTime2[4] = data[1].nowTimeB1
			dayBarTime2[3] = data[1].nowTimeB2
			dayBarTime2[2] = data[1].nowTimeB3
			dayBarTime2[1] = data[1].nowTimeB4
			dayBarTime2[0] = data[1].nowTimeB5
			dayBarValue = dayBarValue1;
			dayBarTime = dayBarTime1;
			funBarByDay();
			if(momYoyFlag){
				monthMomYoy();
			}
		}
	});
}
function yearMomYoy(){
	//六天同环比等待图层
// 	showBarByYear.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
	$.ajax({
		url:"<%=basePath%>statistics/toListView/querySixMomYoy.action",
		data:{date:$("#timeYear").val(),staType:3},
		async:true,
		success:function(data){
			yearBarValue1[5] = data[0].nowMJNum
			yearBarValue1[4] = data[0].nowMJNumB1
			yearBarValue1[3] = data[0].nowMJNumB2
			yearBarValue1[2] = data[0].nowMJNumB3
			yearBarValue1[1] = data[0].nowMJNumB4
			yearBarValue1[0] = data[0].nowMJNumB5
			yearBarTime1[5] = data[0].nowTime
			yearBarTime1[4] = data[0].nowTimeB1
			yearBarTime1[3] = data[0].nowTimeB2
			yearBarTime1[2] = data[0].nowTimeB3
			yearBarTime1[1] = data[0].nowTimeB4
			yearBarTime1[0] = data[0].nowTimeB5
			yearBarValue = yearBarValue1;
			yearBarTime = yearBarTime1;
			funBarByYear();
		}
	});
}
function functione(){
	// 路径配置
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	        
	// 使用
	require(
	    [
	        'echarts',
	        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
	    ],
	    function (ec) {
	    	  // 基于准备好的dom，初始化echarts图表
	    	  myChart = ec.init(document.getElementById('main')); 
	    	  show=myChart;
		      myChart.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
	    	  var option = {
	    			  title : {
	    			        text: '(日)门急诊人次',
	    			        subtext: name,
	    			        x:'left'
	    			    },
	    			    tooltip : {
	    			        trigger: 'item',
	    			        formatter: "{b} : <br/>{c} ({d}%)"
	    			    },
	    			    toolbox : {
					    	show:true,
					    	y : '50',
					    	feature:{
					    		restore:{show:true}
					    	}
					    },
	    			    legend: {
	    			        x: 'right',
	    			        data: ['门诊','急诊']
	    			    },
	    			    calculable : true,//显示边框
	    			    color : colorYQ,
	    			    series : [
	    			        {
	    			            name: '人次',
	    			            type: 'pie',
	    			            radius : '50%',
	    			            center: ['50%', '55%'],
	    			            data:[
	    			                {value:value2, name:'急诊'},
	    			                {value:value1, name:'门诊'}
	    			            ],
	    			            itemStyle:{ 
	    		                    normal:{ 
	    		                        label:{ 
	    		                           show: true, 
	    		                           position:'top',
	    		                           formatter: "{b}:{c}({d}%)" 
	    		                        }, 
	    		                        labelLine :{show:true}
	    		                    } 
	    		                }
	    			        }
	    			    ]
	    	    };
	    	  // 为echarts对象加载数据 
	    	  myChart.setOption(option); 
	    	  myChart.hideLoading();
	    	}
	);
}
function pieArea(pieAreaId, pieAreaName, pieAreaValue, colorArea){
	// 路径配置
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	        
	// 使用
	require(
	    [
	        'echarts',
	        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
	    ],
	    function (ec) {
	    	  // 基于准备好的dom，初始化echarts图表
	    	  myChartp = ec.init(document.getElementById(pieAreaId)); 
		      myChartp.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
	    	  var option = {
	    			  title : {
	    			        text: '('+pieAreaName+')各院区门急诊人次',
// 	    			        subtext: name,
	    			        x:'left'
	    			    },
	    			    tooltip : {
	    			        trigger: 'item',
	    			        formatter: "{b} : <br/>{c} ({d}%)"
	    			    },
	    			    toolbox : {
					    	show:true,
					    	y : '50',
					    	feature:{
					    		restore:{show:true}
					    	}
					    },
	    			    /* legend: {
	    			        x: 'right',
	    			        data: ['门诊','急诊']
	    			    }, */
	    			    calculable : true,//显示边框
	    			    color : colorArea,
	    			    series : [
	    			        {
	    			            name: '人次',
	    			            type: 'pie',
	    			            radius : '50%',
	    			            center: ['50%', '55%'],
	    			            data:pieAreaValue,
	    			            itemStyle:{ 
	    		                    normal:{ 
	    		                        label:{ 
	    		                           show: true, 
	    		                           position:'top',
	    		                           formatter: "{b}:{c}({d}%)" 
	    		                        }, 
	    		                        labelLine :{show:true}
	    		                    } 
	    		                }
	    			        }
	    			    ]
	    	    };
	    	  // 为echarts对象加载数据 
	    	  myChartp.setOption(option); 
	    	  myChartp.hideLoading();
	    	}
	);
}
function funBarByDay(){
	// 路径配置
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	        
	// 使用
	require(
	    [
	        'echarts',
	        'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
	        'echarts/chart/line'
	    ],
	    function (ec) {
	    	  // 基于准备好的dom，初始化echarts图表
	    	  myChart = ec.init(document.getElementById('dayBar')); 
	    	  showBarByDay=myChart;
		      myChart.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
		      var option = {
					    tooltip : {
					    	formatter: function (params, ticket, callback) {
					        	var i = "--";
					        	var nextNum = "1";
					        	if("0"!=dayBarValue[(params.dataIndex-1)]){
					        		nextNum = dayBarValue[(params.dataIndex-1)];
					        	}
					        	if((params.dataIndex-1)<0){
					        	}else{
								    i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
					        	}
							    var rc = (""+params.seriesName).substring(0,3)+"人次";
							    var bz = "增减(%)";
							    
							    return rc+"<br/>"+params.data+"<br/>"+bz+"<br/>"+i;
							},
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
					        },
					    },
					     legend: {
					        data:barByMonthName,
					    },
					    xAxis : [
					        {
					            type : 'category',
					            data : dayBarTime,
					            axisLabel:{
						        	rotate : 30		
						        }
					        }
					    ],
					    yAxis : [
					        {
					            type : 'value'
					        }
					    ],
					    series : [
//	 						barGap :'20',
					        {
					            name:barByMonthName[0],
					            type:'bar',
					            data: dayBarValue,
					            barWidth : 20
					        },
					        {
					            name:barByMonthName[1],
					            type:'line',
					            data: dayBarValue
					        }
					    ]
					};
	    	  // 为echarts对象加载数据 
	    	  myChart.setOption(option); 
	    	  myChart.hideLoading();
	    	}
	);
}
function functione1(){
	// 路径配置
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	        
	// 使用
	require(
	    [
	        'echarts',
	        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
	    ],
	    function (ec) {
	    	  // 基于准备好的dom，初始化echarts图表
	    	  myChart = ec.init(document.getElementById('main1')); 
	    	  show1 = myChart;
		      myChart.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
	    	  var option = {
	    			  title : {
	    			        text: '(月)门急诊人次',
	    			        subtext: name1,
	    			        x:'left'
	    			    },
	    			    tooltip : {
	    			        trigger: 'item',
	    			        formatter: "{b} : <br/>{c} ({d}%)"
	    			    },
	    			    toolbox : {
					    	show:true,
					    	y : '50',
					    	feature:{
					    		restore:{show:true}
					    	}
					    },
	    			    legend: {
	    			        x: 'right',
	    			        data: ['门诊','急诊']
	    			    },
	    			    calculable : true,//显示边框
	    			    color : colorYQ,
	    			    series : [
	    			        {
	    			            name: '人次',
	    			            type: 'pie',
	    			            radius : '50%',
	    			            center: ['50%', '55%'],
	    			            data:[
	    			                {value:value4, name:'急诊'},
	    			                {value:value3, name:'门诊'}
	    			            ],
	    			            itemStyle:{ 
	    		                    normal:{ 
	    		                        label:{ 
	    		                           show: true, 
	    		                           position:'top',
	    		                           formatter: "{b}:{c} ({d}%)" 
	    		                        }, 
	    		                        labelLine :{show:true}
	    		                    } 
	    		                }
	    			        }
	    			    ]
	    	    };
	    	  // 为echarts对象加载数据 
	    	  myChart.setOption(option); 
	    	  myChart.hideLoading();
	    	}
	);
}
function funBarByMonth(){
	// 路径配置
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	// 使用
	require(
	    [
	        'echarts',
	        'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
	        'echarts/chart/line'
	    ],
	    function (ec) {
	    	  // 基于准备好的dom，初始化echarts图表
	    	  myChart = ec.init(document.getElementById('monthBar')); 
	    	  BarByMonth = myChart; 
		      var option = {
					    tooltip : {
					    	formatter: function (params, ticket, callback) {
					        	var i = "--";
					        	var nextNum = "1";
					        	if("0"!=monthBarValue[(params.dataIndex-1)]){
					        		nextNum = monthBarValue[(params.dataIndex-1)];
					        	}
					        	if((params.dataIndex-1)<0){
					        	}else{
								    i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
					        	}
							    var rc = (""+params.seriesName).substring(0,3)+"人次";
							    var bz = "增减(%)";
							    
							    return rc+"<br/>"+params.data+"<br/>"+bz+"<br/>"+i;
							},
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
					        },
					    },
					     legend: {
					        data:barByMonthName,
					        /* selected: {
		  						'门急诊环比': true,
		  						'门急诊环比': true,
		  					} */
					    },
					    /* grid: {
					        left: '3%',
					        right: '4%',
					        bottom: '3%',
					        containLabel: true
					    },
					    /* dataZoom : {
					        show : true,
					        realtime : true,
					        zoomLock : true,
					        start : 0,
					        end : persent
					    }, */
					    xAxis : [
					        {
					            type : 'category',
					            data : monthBarTime,
					            axisLabel:{
						        	rotate : 30		
						        }
					        }
					    ],
					    yAxis : [
					        {
					            type : 'value'
					        }
					    ],
					    series : [
//	 						barGap :'20',
					        {
					            name:barByMonthName[0],
					            type:'bar',
					            data: monthBarValue,
					            barWidth : 20
					        },
					        {
					            name:barByMonthName[1],
					            type:'line',
					            data: monthBarValue
					        }
					    ]
					};
	    	  // 为echarts对象加载数据 
	    	  myChart.setOption(option); 
	    	  myChart.hideLoading();
	    	}
	);
}
function functione2(){
	// 路径配置
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	        
	// 使用
	require(
	    [
	        'echarts',
	        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
	    ],
	    function (ec) {
	    	  // 基于准备好的dom，初始化echarts图表
	    	  myChart = ec.init(document.getElementById('main2')); 
	    	  show2=myChart;
		      myChart.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
	    	  var option = {
	    			  title : {
	    			        text: '(年)门急诊人次',
	    			        subtext: name2,
	    			        x:'left'
	    			    },
	    			    tooltip : {
	    			        trigger: 'item',
	    			        formatter: "{b} : <br/>{c} ({d}%)"
	    			    },
	    			    toolbox : {
					    	show:true,
					    	y : '50',
					    	feature:{
					    		restore:{show:true}
					    	}
					    },
	    			    legend: {
	    			        x: 'right',
	    			        data: ['门诊','急诊']
	    			    },
	    			    calculable : true,//显示边框
	    			    color : colorYQ,
	    			    series : [
	    			        {
	    			            name: '人次',
	    			            type: 'pie',
	    			            radius : '50%',
	    			            center: ['50%', '55%'],
	    			            data:[
	    			                {value:value6, name:'急诊'},
	    			                {value:value5, name:'门诊'}
	    			            ],
	    			            itemStyle:{ 
	    		                    normal:{ 
	    		                        label:{ 
	    		                           show: true, 
	    		                           position:'top',
	    		                           formatter: "{b}:{c} ({d}%)" 
	    		                        }, 
	    		                        labelLine :{show:true}
	    		                    } 
	    		                }
	    			        }
	    			    ]
	    	    };
	    	  // 为echarts对象加载数据 
	    	  myChart.setOption(option); 
	    	  myChart.hideLoading();
	    	}
	);
}
function funBarByYear(){
	// 路径配置
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	        
	// 使用
	require(
	    [
	        'echarts',
	        'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
	        'echarts/chart/line'
	    ],
	    function (ec) {
	    	  // 基于准备好的dom，初始化echarts图表
	    	  myChart = ec.init(document.getElementById('yearBar')); 
	    	  showBarByYear=myChart;
		      myChart.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
		      var option = {
					    tooltip : {
// 					        trigger: 'axis',
					        formatter: function (params, ticket, callback) {
					        	var i = "--";
					        	var nextNum = "1";
					        	if("0"!=yearBarValue[(params.dataIndex-1)]){
					        		nextNum = yearBarValue[(params.dataIndex-1)];
					        	}
					        	if((params.dataIndex-1)<0){
					        	}else{
								    i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
					        	}
							    var rc = (""+params.seriesName).substring(0,3)+"人次";
							    var bz = "增减(%)";
							    
							    return rc+"<br/>"+params.data+"<br/>"+bz+"<br/>"+i;
							},
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
					        },
					    },
					     legend: {
					        data:barByMonthName,
					        /* selected: {
		  						'门急诊环比': true,
		  						'门急诊环比': true,
		  					} */
					    },
					    /* grid: {
					        left: '3%',
					        right: '4%',
					        bottom: '3%',
					        containLabel: true
					    },
					    /* dataZoom : {
					        show : true,
					        realtime : true,
					        zoomLock : true,
					        start : 0,
					        end : persent
					    }, */
					    xAxis : [
					        {
					            type : 'category',
					            data : yearBarTime,
					            axisLabel:{
						        	rotate : 30		
						        }
					        }
					    ],
					    yAxis : [
					        {
					            type : 'value'
					        }
					    ],
					    series : [
//	 						barGap :'20',
					        {
					            name:barByMonthName[0],
					            type:'bar',
					            data: yearBarValue,
					            barWidth : 20
					        },
					        {
					            name:barByMonthName[1],
					            type:'line',
					            data: yearBarValue
					        }
					    ]
					};
	    	  // 为echarts对象加载数据 
	    	  myChart.setOption(option); 
	    	  myChart.hideLoading();
	    	}
	);
}
function functione3(){
	// 路径配置
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	        
	// 使用
	require(
	    [
	        'echarts',
	        'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
	    ],
	    function (ec) {
	    	  // 基于准备好的dom，初始化echarts图表
	    	  myChart = ec.init(document.getElementById('main3')); 
	    	  show3=myChart;
		      myChart.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
	    	  var option = {
	    			  title : {
	    			        text: '自定义门急诊人次',
	    			        subtext: name3,
	    			        x:'left'
	    			    },
	    			    tooltip : {
	    			        trigger: 'item',
	    			        formatter: "{b} : <br/>{c} ({d}%)"
	    			    },
	    			    toolbox : {
					    	show:true,
					    	y : '50',
					    	feature:{
					    		restore:{show:true}
					    	}
					    },
	    			    legend: {
	    			        x: 'right',
	    			        data: ['门诊','急诊']
	    			    },
	    			    calculable : true,//显示边框
	    			    color : colorYQ,
	    			    series : [
	    			        {
	    			            name: '人次',
	    			            type: 'pie',
	    			            radius : '50%',
	    			            center: ['50%', '55%'],
	    			            data:[
	    			                {value:value8, name:'急诊'},
	    			                {value:value7, name:'门诊'}
	    			            ],
	    			            itemStyle:{ 
	    		                    normal:{ 
	    		                        label:{ 
	    		                           show: true, 
	    		                           position:'top',
	    		                           formatter: "{b}:{c}人次 ({d}%)" 
	    		                        }, 
	    		                        labelLine :{show:true}
	    		                    } 
	    		                }
	    			        }
	    			    ]
	    	    };
	    	  // 为echarts对象加载数据 
	    	  myChart.setOption(option); 
	    	  myChart.hideLoading();
	    	}
	);
}
function funBarBySelf(){
	// 路径配置
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	        
	// 使用
	require(
	    [
	        'echarts',
	        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	    ],
	    function (ec) {
	    	  // 基于准备好的dom，初始化echarts图表
	    	  myChart = ec.init(document.getElementById('selfBar')); 
// 	    	  showBarByDay=myChart;
		      myChart.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
		      var option = {
					    tooltip : {
					        trigger: 'axis',
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
					        },
					    },
					    legend: {
					        data:['门诊','急诊'],
					        selected: {
		  						'门诊': true,
		  						'急诊': true,
		  					}
					    },
					    grid: {
					        left: '3%',
					        right: '4%',
					        bottom: '3%',
					        containLabel: true
					    },
					    /* dataZoom : {
					        show : true,
					        realtime : true,
					        zoomLock : true,
					        start : 0,
					        end : persent
					    }, */
					    xAxis : [
					        {
					            type : 'category',
					            data : ['环比','同比']
					        }
					    ],
					    yAxis : [
					        {
					            type : 'value'
					        }
					    ],
					    series : [
//	 						barGap :'20',
					        {
					            name:'门急诊',
					            type:'bar',
					            data: selfBarValue1,
// 					            barWidth : 20
					        }
					    ]
					};
	    	  // 为echarts对象加载数据 
	    	  myChart.setOption(option); 
	    	  myChart.hideLoading();
	    	}
	);
}

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
</script>
<style type="text/css">
	.datagrid-htable .datagrid-sort-icon{
		display: none;
	}
</style>
</head>
<body style="overflow-y: auto">
<div style="width:calc(25% - 1px);float: left;">
								<!-- 按日统计表 -->
					  	  		<div style="height:35px;width:100%;">
					  	  			<table style="width:100%;border:1;padding:5px 5px 5px 5px;">
										<tr>
											<!-- <td>
												<a href="javascript:void(0)" onclick="queryMidday1()" class="easyui-linkbutton" iconCls="icon-date">年</a>&nbsp;&nbsp;
												<a href="javascript:void(0)" onclick="queryMidday2()" class="easyui-linkbutton" iconCls="icon-date">月</a>&nbsp;&nbsp;
												<a href="javascript:void(0)" onclick="queryMidday3()" class="easyui-linkbutton" iconCls="icon-date">日</a>&nbsp;&nbsp;
											</td> -->
											<td>
									 		(日)统计:
											<input id="timeDay" class="Wdate" type="text" value="${Etime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:pickedFuncByDay,maxDate:'%y-%M-%d'})" />
											</td>
										</tr>
									</table>
					  	  		</div> 
							    <div style="height:150px;width:100%;border-right:0;">
							    	<table id="list" class="easyui-datagrid" data-options="fitColumns:true,singleSelect:true,fit:true,fitColumns:true,border:false,scrollbarSize:'0'">   
									    <thead>   
									        <tr>   
									            <th data-options="field:'index',rowspan:2,width:'12%'" align="center">指标</th>   
									            <th data-options="field:'passengers',rowspan:2,width:'18%'" align="center">人次</th>   
									            <th data-options="field:'c',colspan:2" align="center">与上月比较</th>
									            <th data-options="field:'d',colspan:2" align="center">与上年同期比较</th> 
<!-- 									            <th data-options="field:'ratio',rowspan:2" width="20%">完成月目标比值</th>    -->
									        </tr>   
									         <tr>   
									            <th data-options="field:'addsLastM',width:'18%'" align="center">增减量</th>
									            <th data-options="field:'addPerLastM',width:'17%'" align="center">增减(%)</th>
<!-- 									            <th data-options="field:'mom'" width="14%">同比（%）</th> -->
									            <th data-options="field:'addsLastY',width:'18%'" align="center">增减量</th>
									            <th data-options="field:'addPerLastY',width:'17%'" align="center">增减(%)</th> 
<!-- 									            <th data-options="field:'yoy'" width="14%">环比（%）</th>  -->
									        </tr> 
									    </thead>   
									</table> 
							    </div> 
							    <!-- 门急诊饼状图 -->
							    <div data-options="region:'center',border:false" style="height:50%;width: 100%;">
							    	<div id="main" style="height:100%;width:100%;" ></div>
							    </div>   
							    <!-- 院区饼状图 -->
							    <div data-options="region:'center',border:false" style="height:50%;width: 100%;">
							    	<div id="areaByDay" style="height:100%;width:100%;" ></div>
							    </div>   
							    <!-- 柱状图 -->
							    <div style="height:30px;width:100%;margin-left: 10px">
									<input editable="false" class="easyui-combobox" id="dySelect"
					                   data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
					                   label: '门急诊环比',  
					                   value: 'mom',  
					                   selected:true},  
					                   {label: '门急诊同比',  
					                   value: 'yoy'}]"                    
					                 /> 
								</div>
							    <div id="dayBar" style="height:40%;width:100%" ></div>
</div>
<div  class="changeskin" style="width:calc(25% - 1px);float: left;border-top: 0 !important;border-bottom: 0 !important; ">
<!-- 按月统计表 -->
					  	  		<div style="height:35px;width:100%">
					  	  			<table style="width:100%;border:false;padding:5px 5px 5px 5px;">
										<tr>
											<!-- <td>
												<a href="javascript:void(0)" onclick="queryMidday1()" class="easyui-linkbutton" iconCls="icon-date">年</a>&nbsp;&nbsp;
												<a href="javascript:void(0)" onclick="queryMidday2()" class="easyui-linkbutton" iconCls="icon-date">月</a>&nbsp;&nbsp;
												<a href="javascript:void(0)" onclick="queryMidday3()" class="easyui-linkbutton" iconCls="icon-date">日</a>&nbsp;&nbsp;
											</td> -->
											<td>
									 		(月)统计:
											<input id="timeMonth" class="Wdate" type="text" value="${Etime}" onClick="WdatePicker({dateFmt:'yyyy-MM',onpicked:pickedFuncByMonth,maxDate:'%y-%M-%d'})" />
											</td>
										</tr>
									</table>
					  	  		</div>   
							    <div style="height:150px;width:100%;border-right:0;">
							    	<table id="list1" class="easyui-datagrid" data-options="fitColumns:true,singleSelect:true,fit:true,fitColumns:true,border:false,scrollbarSize:'0'">   
									    <thead>   
									        <tr>   
									            <th data-options="field:'index',rowspan:2,width:'12%'" align="center">指标</th>   
									            <th data-options="field:'passengers',rowspan:2,width:'18%'" align="center">人次</th>   
									            <th data-options="field:'c',colspan:2" align="center">与上月比较</th>
									            <th data-options="field:'d',colspan:2" align="center">与上年同期比较</th> 
<!-- 									            <th data-options="field:'ratio',rowspan:2" width="20%">完成月目标比值</th>    -->
									        </tr>   
									         <tr>   
									            <th data-options="field:'addsLastM',width:'18%'" align="center">增减量</th>
									            <th data-options="field:'addPerLastM',width:'17%'"align="center">增减(%)）</th>
									            <th data-options="field:'addsLastY',width:'18%'"align="center">增减量</th>
									            <th data-options="field:'addPerLastY',width:'17%'"align="center">增减(%)</th> 
									        </tr> 
									    </thead>  
									</table> 
							    </div> 
							    <div data-options="region:'center'" style="height:50%;width: 100%;border-top:0">
							    	<div id="main1" style="height:100%;width:100%" ></div>
							    </div>  
							     <!-- 院区饼状图 -->
							    <div data-options="region:'center',border:false" style="height:50%;width: 100%;">
							    	<div id="areaByMonth" style="height:100%;width:100%;" ></div>
							    </div>  
								<div style="height:30px;width:100%;margin-left: 10px">
									<input editable="false"  class="easyui-combobox" id="mySelect"
					                   data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
					                   label: '门急诊环比',  
					                   value: 'mom',  
					                   selected:true},  
					                   {label: '门急诊同比',  
					                   value: 'yoy'}]"                    
					                 /> 
								</div>
							    <div id="monthBar" style="height:40%;width:100%;" ></div> 	
</div>
<div style="width:calc(25% - 1px);float: left;">
<!-- 按年统计表 -->
					  	  		<div style="height:35px;width:100%">
					  	  			<table style="width:100%;border:false;padding:5px 5px 5px 5px;">
										<tr>
											<!-- <td>
												<a href="javascript:void(0)" onclick="queryMidday1()" class="easyui-linkbutton" iconCls="icon-date">年</a>&nbsp;&nbsp;
												<a href="javascript:void(0)" onclick="queryMidday2()" class="easyui-linkbutton" iconCls="icon-date">月</a>&nbsp;&nbsp;
												<a href="javascript:void(0)" onclick="queryMidday3()" class="easyui-linkbutton" iconCls="icon-date">日</a>&nbsp;&nbsp;
											</td> -->
											<td>
									 		(年)统计:
											<input id="timeYear" class="Wdate" type="text" value="${Etime}" onClick="WdatePicker({dateFmt:'yyyy',onpicked:pickedFuncByYear,maxDate:'%y-%M-%d'})" />
											</td>
										</tr>
									</table>
					  	  		</div>   
							    <div style="height:150px;width:100%;border-right:0;">
							    	<table id="list2" class="easyui-datagrid" data-options="fitColumns:true,singleSelect:true,fit:true,border:false,scrollbarSize:'3'">   
									    <thead>   
									        <tr>   
									            <th data-options="field:'index',rowspan:2,width:28" align="center">指标</th>   
									            <th data-options="field:'passengers',rowspan:2,width:28" align="center">人次</th>   
									            <th data-options="field:'c',colspan:2,width:50" align="center">与上年比较</th>
<!-- 									            <th data-options="field:'d',colspan:2" width="28%">与上年同期比较</th>  -->
<!-- 									            <th data-options="field:'ratio',rowspan:2" width="20%">完成月目标比值</th>    -->
									        </tr>   
									         <tr>   
									            <th data-options="field:'addsLastM',width:25" align="center">增减量</th>
									            <th data-options="field:'addPerLastM',width:25" align="center">增减（%）</th>
<!-- 									            <th data-options="field:'addsLastY'" width="14%">增减量</th> -->
<!-- 									            <th data-options="field:'addPerLastY'" width="14%">增减（%）</th>  -->
									        </tr> 
									    </thead>   
									</table> 
							    </div>  
							    <div data-options="region:'center'" style="height:50%;width: 100%;border-top:0">
							    	<div id="main2" style="height:100%;width:100%" ></div>
							    </div>   
							    <!-- 院区饼状图 -->
							    <div data-options="region:'center',border:false" style="height:50%;width: 100%;">
							    	<div id="areaByYear" style="height:100%;width:100%;" ></div>
							    </div> 
							     <div style="height:30px;width:100%;margin-left: 10px">
									<input editable="false" class="easyui-combobox"
					                   data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
					                   label: '门急诊环比',  
					                   value: 'mom',  
					                   selected:true}]"                    
					                 /> 
								</div>
							    <div id="yearBar" style="height:40%;width:100%;" ></div> 
</div>
<div class="changeskin" style="width:calc(25% - 1px);float: left;border-bottom: 0 !important;border-top: 0 !important;">
<!-- 自定义统计门急诊人次   -->
					  	  		<div style="height:35px;width:100%">
					  	  			<table style="width:100%;border:false;padding:5px 5px 5px 5px;">
										<tr>
											<!-- <td>
												<a href="javascript:void(0)" onclick="queryMidday1()" class="easyui-linkbutton" iconCls="icon-date">年</a>&nbsp;&nbsp;
												<a href="javascript:void(0)" onclick="queryMidday2()" class="easyui-linkbutton" iconCls="icon-date">月</a>&nbsp;&nbsp;
												<a href="javascript:void(0)" onclick="queryMidday3()" class="easyui-linkbutton" iconCls="icon-date">日</a>&nbsp;&nbsp;
											</td> -->
											<td>
									 		自定义:
											<input style="width: 100px" id="timeSelfS" class="Wdate" type="text" value="${Stime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:pickedFuncBySelf,maxDate:'#F{$dp.$D(\'timeSelfE\')}'})" />
											至
											<input style="width: 100px" id="timeSelfE" class="Wdate" type="text" value="${Etime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:pickedFuncBySelf,minDate:'#F{$dp.$D(\'timeSelfS\')}'})" />
										</td></tr>
										
									</table>
					  	  		</div>   
							    <div style="height:150px;width:100%;border-right:0;">
							    	<table id="list3" class="easyui-datagrid" style="height:150px;width:100%;border-right:0;margin-top: 35px" data-options="fitColumns:true,singleSelect:true,fit:true,border:false,scrollbarSize:'0'">   
									    <thead>   
									       <tr >   
									            <th data-options="field:'index',width:50,rowspan:2" align="center">指标</th>   
									            <th data-options="field:'passengers',width:50,rowspan:2" align="center">人次</th>   
<!-- 									            <th data-options="field:'c',colspan:2" width="48%">与上期比较</th> -->
<!-- 									            <th data-options="field:'ratio',rowspan:2" width="20%">完成月目标比值</th>    -->
									        </tr>   
									         <tr>   
									           
									        </tr>   
									    </thead>   
									</table> 
							    </div>  
							    <div data-options="region:'center'" style="height:50%;width: 100%;">
							    	<div id="main3" style="height:100%;width:100%" ></div>
							    </div>   
							    <!-- 院区饼状图 -->
							    <div data-options="region:'center',border:false" style="height:50%;width: 100%;">
							    	<div id="areaBySelf" style="height:100%;width:100%;" ></div>
							    </div> 
							    <div id="selfBar" style="height:40%;width:100%" ></div>	
</div>
</body>
</html>