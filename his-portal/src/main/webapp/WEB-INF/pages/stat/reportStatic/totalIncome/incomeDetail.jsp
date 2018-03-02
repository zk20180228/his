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
<title>总收入情况明细</title>
</head>
<style>
.center {
  padding: 10px;
  margin: 20px auto;
  display: -webkit-box;
  -webkit-box-orient: horizontal;
  -webkit-box-pack: center;
  -webkit-box-align: center;
  display: -moz-box;
  -moz-box-orient: horizontal;
  -moz-box-pack: center;
  -moz-box-align: center;
  display: -o-box;
  -o-box-orient: horizontal;
  -o-box-pack: center;
  -o-box-align: center;
  display: -ms-box;
  -ms-box-orient: horizontal;
  -ms-box-pack: center;
  -ms-box-align: center;
  display: box;
  box-orient: horizontal;
  box-pack: center;
  box-align: center;
}

</style>
<body class="easyui-layout" data-options="fit:true" style="padding:5px"> 
	<div data-options="region:'north',title:'',border:false" style="width: 100%;height: 15%;">
		<table style="width:100%;height:80%;">
			<caption><font size="5" id="show"></font></caption>
			<tr align="center">
				<td>
					统计(年)<input id="time1" class="Wdate" type="text" value="" onClick="WdatePicker({dateFmt:'yyyy',onpicked:pickedFunc1,maxDate:'%y'})" />
				</td>
				<td>
					统计(月)<input id="time2" class="Wdate" type="text" value="" onClick="WdatePicker({dateFmt:'yyyy-MM',onpicked:pickedFunc2,maxDate:'%y-%M'})" />
				</td>
				<td>
					统计(日)<input id="time3" class="Wdate" type="text" value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:pickedFunc3,maxDate:'%y-%M-%d'})" />
				</td>				
			</tr>
		</table>
	</div> 
	<div data-options="region:'center',title:'',border:false" style="width:50%;height:85%;">
		<div class="easyui-layout" data-options="fit:true">   
				<div style="width: 100%;height:100%;float: left;">
					<div id="main1" class="center" style="width:100%;height:100%;"></div>
				</div>
		</div> 
	</div>
	  <div data-options="region:'east',title:''" style="width:50%;height:85%;">
	  	<div class="easyui-layout" data-options="fit:true">   
				<div style="width: 100%;height:50%;">
					<div id="main3" class="center" style="width:100%;height:80%;"></div>
					<div id="main33" class="center" style="width:100%;height:100%;">暂无年同比数据</div>
					<table>
						
					</table>
				</div>
				<div style="width: 100%;height:50%;">
					<div id="main4" class="center" style="width:100%;height:90%;"></div>
				</div>
		</div> 
	  </div>
</body>
</html>
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript">
    var barByMonthName =  ['总收入金额','总收入环比'];
	var dateSign;//1 年  2月  3日
	var searhDate;//查询时间
	var searchTime="${Etime}";//当前时间段
	var dateArr=searchTime.split('-');
	$('#time1').val(dateArr[0]);
	$('#time2').val(dateArr[0]+'-'+dateArr[1]);
	$('#time3').val(dateArr[0]+'-'+dateArr[1]+'-'+dateArr[2]);
		$(function(){
			pickedFunc3();
		});
		function setData1(dateValue,dateName,count){
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
	            	var  myChart = ec.init(document.getElementById('main1')); 
	           var option = {
	        		   title : {
	        		        text: '门诊/住院',
	        		        subtext: count,
	        		        x:'center'
	        		    },
	        		    tooltip : {
	        		        trigger: 'item',
	        		        formatter: "{a} <br/>{b} : {c} ({d}%)"
	        		    },
	        		    legend: {
	        		        orient : 'vertical',
	        		        x : 'left',
	        		        data:eval(dateName)
	        		    },
	        		    toolbox: {
	        		        show : true,
	        		        feature : {
	        		            mark : {show: true},
	        		            dataView : {show: true, readOnly: false},
	        		            magicType : {
	        		                show: true, 
	        		                type: ['pie', 'funnel'],
	        		                option: {
	        		                    funnel: {
	        		                        x: '25%',
	        		                        width: '50%',
	        		                        funnelAlign: 'center',
	        		                        max: 1548
	        		                    }
	        		                }
	        		            },
	        		            restore : {show: true},
	        		            saveAsImage : {show: true}
	        		        }
	        		    },
	        		    calculable : true,
	        		    series : [
	        		        {
	        		            name:'门诊住院环状图',
	        		            type:'pie',
	        		            radius : ['50%', '70%'],
	        		            itemStyle : {
	        		                normal : {
	        		                    label : {
	        		                        show : false
	        		                    },
	        		                    labelLine : {
	        		                        show : false
	        		                    }
	        		                },
	        		                emphasis : {
	        		                    label : {
	        		                        show : true,
	        		                        position : 'center',
	        		                        textStyle : {
	        		                            fontSize : '30',
	        		                            fontWeight : 'bold'
	        		                        }
	        		                    }
	        		                }
	        		            },
	        		            data:eval(dateValue)
	        		        }
	        		    ]
	        		};
	            myChart.setOption(option); 
	              }
	            );
	    }
		function setData2(dateValue,dateName,dateCount,id,barByMonthName){
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
			    	 var myChart = ec.init(document.getElementById(id)); 
				      var option = {
							    tooltip : {
							    	formatter: function (params, ticket, callback) {
							    		
							    		var i = "--";
							        	var nextNum = "1";
							        	var ind=params.dataIndex-1;
							        	if(ind>=0&&"0"!=dateValue[ind].value){
							        		nextNum = (dateValue[params.dataIndex-1].value);
							        	}
							        	if((params.dataIndex-1)<0){
							        		
							        	}else{
										    i = ((Number(params[2])-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
							        	}
									    var rc = (""+params.seriesName).substring(0,3)+"金额";
									    var bz = "增减(%)";
									    return rc+"<br/>"+(parseFloat(params[2])).toLocaleString()+"<br/>"+bz+"<br/>"+i;
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
							            data : dateName,
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
							        {
							            name:barByMonthName[0],
							            type:'bar',
							            data:  dateValue,
							            barWidth : 20
							        },
							        {
							            name:barByMonthName[1],
							            type:'line',
							            data: dateValue
							        }
							    ]
							};
			    	  // 为echarts对象加载数据 
			    	  myChart.setOption(option); 
			    	}
			);
	    }
		//门诊住院环状图请求
		function main1Date1(){
			$.ajax({ 
		 		   url:"<%=basePath%>/statistics/listTotalIncomeStatic/queryMZZY.action",
		 		   type:"post",
		 		   data:{startTime:searhDate,dateSign:dateSign},
		 		   async:true,
		 		   success:function(data){
		 			  var dateValue= "[";
		 			  dateValue+="{ value:'"+data.sum[0].douValue+"',name:'"+data.sum[0].classType+"'},";
		 			 dateValue+="{ value:'"+data.sum[1].douValue+"',name:'"+data.sum[1].classType+"'}]";
		 			 dateName="['"+data.sum[0].classType+"','"+data.sum[1].classType+"']";
		 			  setData1(dateValue,dateName,(data.count).toLocaleString());
		 		   }
			});
		}
		//门诊住院同比
		function main1Date2(){
			if(dateSign!='1'){
				$('#main3').show();
				$('#main33').hide();
			$.ajax({ 
		 		   url:"<%=basePath%>/statistics/listTotalIncomeStatic/queryTotalSame.action",
		 		   type:"post",
		 		   data:{startTime:searhDate,dateSign:dateSign},
		 		   async:true,
		 		   success:function(data){
		 			   var len=data.sum.length;
		 			   var dateValue="[";
		 			   var dateName="[";
		 			   var total=0.00;
		 			   for(var i=0;i<len;i++){
		 				  dateValue+="{'value':"+(data.sum[i].douValue).toFixed(2)+",'name':'"+data.sum[i].name+"'},";
		 			   	  dateName+="'"+data.sum[i].name+"',";
		 			   }
		 			   total+=parseFloat(data.count);
		 			   dateName=dateName.substr(0,dateName.length-1);
		 			   dateValue=dateValue.substr(0,dateValue.length-1);
		 			   dateValue+="]";
		 			   dateName+="]";
		 			  setData2(eval(dateValue),eval(dateName),parseFloat(total).toLocaleString(),'main3',['总收入金额','总收入同比']);
		 		   }
			});
			}else{
				$('#main3').hide();
				$('#main33').show();
				
			}
		}
		//总收入环比
		function main1Date3(){
			$.ajax({ 
		 		   url:"<%=basePath%>/statistics/listTotalIncomeStatic/queryTotalSque.action",
		 		   type:"post",
		 		   data:{startTime:searhDate,dateSign:dateSign},
		 		   success:function(data){
		 			   var len=data.sum.length;
		 			   var dateValue="[";
		 			   var dateName="[";
		 			   var total=0.00;
		 			   for(var i=0;i<len;i++){
		 				  dateValue+="{value:"+(data.sum[i].douValue).toFixed(2)+",name:'"+data.sum[i].name+"'},";
		 			   	  dateName+="'"+data.sum[i].name+"',";
		 			   }
		 			   total+=parseFloat(data.count);
		 			   dateName=dateName.substr(0,dateName.length-1);
		 			   dateValue=dateValue.substr(0,dateValue.length-1);
		 			   dateValue+="]";
		 			   dateName+="]";
		 			   setData2(eval(dateValue),eval(dateName),parseFloat(total).toLocaleString(),'main4',['总收入金额','总收入同比']);
		 		   }
			});
		}
	    function pickedFunc1(){
	    	searhDate=$('#time1').val();
	    	dateSign='1'
			main1Date1();
			main1Date2();
			main1Date3();
			$('#show').html('年统计');
		}
		function pickedFunc2(){
			searhDate=$('#time2').val();
	    	dateSign='2'
			main1Date1();
			main1Date2();
			main1Date3();
			$('#show').html('月统计');
		}
		function pickedFunc3(){
			searhDate=$('#time3').val();
	    	dateSign='3'
			main1Date1();
			main1Date2();
			main1Date3();
			$('#show').html('日统计');
		}
</script>
