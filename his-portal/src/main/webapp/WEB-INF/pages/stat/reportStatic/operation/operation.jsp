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
</head>
<body class="easyui-layout" data-options="fit:true"> 
<div data-options="region:'north',title:''" style="width: 100%;height: 17%;border: none;">
	<div class="easyui-layout" data-options="fit:true">
		<div style="width:100%;height:110px;padding: 5px 5px 2px 5px;" data-options="region:'north',border:false">
			<table  style="width:100%;border: none;"  class="honry-table" cellspacing="0" cellpadding="0" border="0" data-options="border:false">
				<tr>
					<td style="padding: 10px 15px;text-align: center; border: none;">
					<input id="time2" value="${Etime}" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',onpicked:query})" />
					
						<input id="startTime"  type="hidden"  />
						<input id="endTime"  type="hidden"  />
					</td>
					<td style="padding: 10px 15px;text-align: center; border: none;">
					<input id="time3" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM',maxDate:'%y-%M',onpicked:queryMonth})" />
					</td>
					<td style="padding: 10px 15px;text-align: center; border: none;">
					<input id="time4" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy',onpicked:queryYear})"  type="text" />
					
					</td>
				</tr>
				<tr>
					<td style="padding: 10px 15px;text-align: center;border: none;font-weight:bold;" id="sumTdDay">
					   合计：计算中..
					</td>
					<td style="padding: 10px 15px;text-align: center;border: none;font-weight:bold;" id="sumTdMonth">
					   合计：计算中..
					</td>
					<td style="padding: 10px 15px;text-align: center;border: none;font-weight:bold;" id="sumTdYear">
					   合计：计算中..
					</td>
				</tr>
				
			</table>
		</div>
	</div>
</div>
<div data-options="region:'center'" id="mainWest" style="width:33%;height:82%;overflow-x: hidden; overflow-y: auto;"></div>
<div data-options="region:'west'" id="main" style="width:33%;height:82%;overflow-x: hidden; overflow-y: auto;"></div>
<div data-options="region:'east'" id="mainEast" style="width:33%;height:82%;overflow-x: hidden; overflow-y: auto;"></div>

</body>
	 <script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
		<script type="text/javascript">
		function fmoney(s, n)  
		{  
		   n = n > 0 && n <= 20 ? n : 2;  
		   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";  
		   var l = s.split(".")[0].split("").reverse(),  
		   r = s.split(".")[1];  
		   t = "";  
		   for(i = 0; i < l.length; i ++ )  
		   {  
		      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");  
		   }  
		   return t.split("").reverse().join("") + "." + r;  
		}
		
		function setData(){
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
	            	  option = {
	            			    tooltip : {
	            			        trigger: 'item',
	            			        formatter: "{b} : {c}人,{d}%"
	            			    },
	            			    color:['#48cda6','#fd87ab','#11abff'], 
	            			    legend: {
	            			        orient : 'vertical',
	            			        x : '20px',
	    	            	        y:'50px',
	            			        itemWidth:30,
	    	            	        itemHeight:24,
	    	            	        textStyle:{
			                        	 fontSize:'14'
			                        },
	            			        data:['住院手术例数','门诊手术例数','介入治疗例数']
	            			    },
	            			    toolbox: {
	            			        show : true,
	            			        feature : {
	            			            restore : {show: true},
	            			        },
	            			        x: 1270,
	            			        y: 50, 
	            			    },
	            			    calculable : true,
	            			    series : [
	            			        {
	            			            name:'手术情况统计',
	            			            type:'pie',
	            			            radius : ['40%', '50%'],
	            			            center: ['50%', '57%'],
	            			            itemStyle : {
	            			                normal : {
	            			                    label : {
	            			                        show : true,
	            			                        textStyle:{
	            			                        	 fontSize:'14'
	            			                        }
	            			                    },
	            			                    labelLine : {
	            			                        show : true
	            			                    }
	            			                },
	            			                emphasis : {
	            			                    label : {
	            			                        show : true,
	            			                        position : 'center',
	            			                        textStyle : {
	            			                            fontSize : '25',
	            			                            fontWeight : 'bold'
	            			                        }
	            			                    }
	            			                }
	            			            },
	            			            data:[
	      			    	                {value:num1, name:'住院手术例数'},
	      			    	                {value:num2 , name:'门诊手术例数'},
	      			    	                {value:num3, name:'介入治疗例数'},
	      			    	            ],
	      			    	            itemStyle:{ 
		    	                            normal:{ 
		    	                                label:{ 
		    	                                   show: true, 
		    	                                   formatter: '{b} : {c}人 ({d}%)' 
		    	                                }, 
		    	                                labelLine :{show:true}
		    	                            } 
		    	                        },

	            			        }
	            			    ]
	            			};
	            	  
	            	  // 为echarts对象加载数据 
	            	  myChart.setOption(option); 
	            	}
	        );
			
		}
		
		function setData1(){
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
	            	  myChart = ec.init(document.getElementById('mainWest')); 
	            	  show=myChart;
	    		      myChart.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
	            	  option1 = {
	            			    tooltip : {
	            			        trigger: 'item',
	            			        formatter: "{b} : {c}人,{d}%"
	            			    },
	            			    color:['#48cda6','#fd87ab','#11abff'], 
	            			    legend: {
	            			        orient : 'vertical',
	            			        x : '20px',
	    	            	        y:'50px',
	            			        itemWidth:30,
	    	            	        itemHeight:24,
	    	            	        textStyle:{
			                        	 fontSize:'14'
			                        },
	            			        data:['住院手术例数','门诊手术例数','介入治疗例数']
	            			    },
	            			    toolbox: {
	            			        show : true,
	            			        feature : {
	            			            restore : {show: true},
	            			        },
	            			        x: 1270,
	            			        y: 50, 
	            			    },
	            			    calculable : true,
	            			    series : [
	            			        {
	            			            name:'手术情况统计',
	            			            type:'pie',
	            			            radius : ['40%', '50%'],
	            			            center: ['50%', '57%'],
	            			            itemStyle : {
	            			                normal : {
	            			                    label : {
	            			                        show : true,
	            			                        textStyle:{
	            			                        	 fontSize:'14'
	            			                        }
	            			                    },
	            			                    labelLine : {
	            			                        show : true
	            			                    }
	            			                },
	            			                emphasis : {
	            			                    label : {
	            			                        show : true,
	            			                        position : 'center',
	            			                        textStyle : {
	            			                            fontSize : '25',
	            			                            fontWeight : 'bold'
	            			                        }
	            			                    }
	            			                }
	            			            },
	            			            data:[
	      			    	                {value:num1, name:'住院手术例数'},
	      			    	                {value:num2 , name:'门诊手术例数'},
	      			    	                {value:num3, name:'介入治疗例数'},
	      			    	            ],
	      			    	            itemStyle:{ 
		    	                            normal:{ 
		    	                                label:{ 
		    	                                   show: true, 
		    	                                   formatter: '{b} : {c}人 ({d}%)' 
		    	                                }, 
		    	                                labelLine :{show:true}
		    	                            } 
		    	                        },

	            			        }
	            			    ]
	            			};
	            	  
	            	  // 为echarts对象加载数据 
	            	  myChart.setOption(option1); 
	            	}
	        );
			
		}
		
		function setData2(){
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
	            	  myChart = ec.init(document.getElementById('mainEast')); 
	            	  show=myChart;
	    		      myChart.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
	            	  option2 = {
	            			    tooltip : {
	            			        trigger: 'item',
	            			        formatter: "{b} : {c}人,{d}%"
	            			    },
	            			    color:['#48cda6','#fd87ab','#11abff'], 
	            			    legend: {
	            			        orient : 'vertical',
	            			        x : '20px',
	    	            	        y:'50px',
	            			        itemWidth:30,
	    	            	        itemHeight:24,
	    	            	        textStyle:{
			                        	 fontSize:'14'
			                        },
	            			        data:['住院手术例数','门诊手术例数','介入治疗例数']
	            			    },
	            			    toolbox: {
	            			        show : true,
	            			        feature : {
	            			            restore : {show: true},
	            			        },
	            			        x: 1270,
	            			        y: 50, 
	            			    },
	            			    calculable : true,
	            			    series : [
	            			        {
	            			            name:'手术情况统计',
	            			            type:'pie',
	            			            radius : ['40%', '50%'],
	            			            center: ['50%', '57%'],
	            			            itemStyle : {
	            			                normal : {
	            			                    label : {
	            			                        show : true,
	            			                        textStyle:{
	            			                        	 fontSize:'14'
	            			                        }
	            			                    },
	            			                    labelLine : {
	            			                        show : true
	            			                    }
	            			                },
	            			                emphasis : {
	            			                    label : {
	            			                        show : true,
	            			                        position : 'center',
	            			                        textStyle : {
	            			                            fontSize : '25',
	            			                            fontWeight : 'bold'
	            			                        }
	            			                    }
	            			                }
	            			            },
	            			            data:[
	      			    	                {value:num1, name:'住院手术例数'},
	      			    	                {value:num2 , name:'门诊手术例数'},
	      			    	                {value:num3, name:'介入治疗例数'},
	      			    	            ],
	      			    	            itemStyle:{ 
		    	                            normal:{ 
		    	                                label:{ 
		    	                                   show: true, 
		    	                                   formatter: '{b} : {c}人 ({d}%)' 
		    	                                }, 
		    	                                labelLine :{show:true}
		    	                            } 
		    	                        },

	            			        }
	            			    ]
	            			};
	            	  
	            	  // 为echarts对象加载数据 
	            	  myChart.setOption(option2); 
	            	}
	        );
			
		}
		function pickedFunc(){
			num1=0;
			num2=0;
			num3=0;
			setData();
			query();
		}
		$(function(){
			var todayTime="${Etime}";
			$("#time3").val(/\d{4}-\d{1,2}/g.exec(todayTime));
			$("#time4").val(/\d{4}/g.exec(todayTime));
			setData();
			setData1();
			setData2();
			query(1);
			queryMonth(2);
			queryYear(3);
	    });
		var show;
		var num1=0;
		var num2=0;
		var num3=0;
		var nums=0;
		function query(){
			var startTime=$("#time2").val();
			$.ajax({ 
		 		   url:"<%=basePath%>statistics/listOperationStatic/queryListOperationStatic.action",
		 		   type:"post",
		 		   data: {startTime:startTime,type:"3"},
		           dataType: "json",
		 		   success:function(data){
		 			   num1=data.num1;
		 			   num2=data.num2;
		 			   num3=data.num3;
		 			   nums=(data.num1+data.num2+data.num3);
		 			   var html1='<span>'+"合计："+nums+"人"+'</span>';
		 			   $("#sumTdDay").html(html1);
		 			   setData();
		 			  show.hideLoading();
		 		   }
		    	});
		}
		
		function queryMonth(){
			var startTime=$("#time3").val();
			$.ajax({ 
		 		   url:"<%=basePath%>statistics/listOperationStatic/queryListOperationStatic.action",
		 		   type:"post",
		 		   data: {startTime:startTime,type:"2"},
		           dataType: "json",
		 		   success:function(data){
		 			   num1=data.num1;
		 			   num2=data.num2;
		 			   num3=data.num3;
		 			   nums=(data.num1+data.num2+data.num3);
		 			   var html1='<span>'+"合计："+nums+"人"+'</span>';
		 			   $("#sumTdMonth").html(html1);
		 			   setData1();
		 			  show.hideLoading();
		 		   }
		    	});
		}
		
		function queryYear(){
			var startTime=$("#time4").val();
			$.ajax({ 
		 		   url:"<%=basePath%>statistics/listOperationStatic/queryListOperationStatic.action",
		 		   type:"post",
		 		   data: {startTime:startTime,type:"1"},
		           dataType: "json",
		 		   success:function(data){
		 			   num1=data.num1;
		 			   num2=data.num2;
		 			   num3=data.num3;
		 			   nums=(data.num1+data.num2+data.num3);
		 			   var html1='<span>'+"合计："+nums+"人"+'</span>';
		 			   $("#sumTdYear").html(html1);
		 			   setData2();
		 			  show.hideLoading();
		 		   }
		    	});
		}
		
		
		
		function onclickDay(flg){
			if(flg=='1'){
				var st=beforeDay(0);
				var et=beforeDay(-1);
				$('#startTime').val(st)
				$("#endTime").val(et);
				query();
			}else if(flg=='2'){
				var myDate  = new Date();
				var month=(myDate.getMonth()+1)>9?(myDate.getMonth()+1).toString():'0' + (myDate.getMonth()+1);
				 var year = myDate.getFullYear();
				 var start= year+"-"+month+"-01";
				 var et=beforeDay(-1);
				 $('#startTime').val(start)
				$("#endTime").val(et);
				 query2();
			}else{
				var myDate  = new Date();
				var start= myDate.getFullYear()+"-01-31";
				var et=beforeDay(-1);
				$('#startTime').val(start)
				$("#endTime").val(et);
				query3();
			}
		}
		
		function beforeDay(beforeDayNum) {
			var d = new Date();
			var endDate = dateToString(d);
			d = d.valueOf();
			d = d - beforeDayNum * 24 * 60 * 60 * 1000;
			d = new Date(d);
			var startDate = dateToString(d);
			return startDate;
		}
		function dateToString(d) {
			var y = d.getFullYear();
			var m = d.getMonth() + 1;
			var d = d.getDate();
			if (m.toString().length == 1) m = "0" + m;
			if (d.toString().length == 1) d = "0" + d;
			return y + "-" + m + "-" + d;
		}
		
		</script>
</html>
