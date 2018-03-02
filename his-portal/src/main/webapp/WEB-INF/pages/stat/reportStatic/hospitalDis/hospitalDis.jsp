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
var show3;
var initDay = '${Etime}';
var initMonth = initDay.split("-")[0]+"-"+initDay.split("-")[1];
var initYear = initDay.split("-")[0];
var barByMonthName =  ['入，出院人次','入，出院环比'];
//日查询
function pickedFuncByDay(){
	$("#dySelect").combobox('select', 'HB');
	$("#list").datagrid({
		url:"<%=basePath%>/statistics/HospitalDischarge/newHospDisc.action",
		queryParams:{date:$("#timeDay").val(),dateSign:3},
		onLoadSuccess:function(data){
			var date= returnPieDate(data.rows);
			queryPieDate(date,'main','（日）入，出院人次',$("#timeDay").val());
			queryContinue($("#timeDay").val(),'3',$('#dySelect').combobox('getValue'),'dayBar');
		}
	});
}
//月查询
function pickedFuncByMonth(){
	$("#mySelect").combobox('select', 'HB');
	$("#list1").datagrid({
		url:"<%=basePath%>/statistics/HospitalDischarge/newHospDisc.action",
		queryParams:{date:$("#timeMonth").val(),dateSign:2},
		onLoadSuccess:function(data){
			var date= returnPieDate(data.rows);
			queryPieDate(date,'main1','(月)入，出院人次',$("#timeMonth").val());
			queryContinue($("#timeMonth").val(),'2',$('#mySelect').combobox('getValue'),'monthBar');
		}
	});
}
//年查询
function pickedFuncByYear(){
	$("#list2").datagrid({
		url:"<%=basePath%>/statistics/HospitalDischarge/newHospDisc.action",
		queryParams:{date:$("#timeYear").val(),dateSign:1},
		onLoadSuccess:function(data){
			var date= returnPieDate(data.rows);
			queryPieDate(date,'main2','（年）入，出院人次',$("#timeYear").val());
			queryContinue($("#timeYear").val(),'1','HB','yearBar');
		}
	});
}
//自定义区段查询
function pickedFuncBySelf(){
	$("#timeSelfS").val();
	$("#timeSelfE").val();
	if($("#timeSelfS").val()==''||$("#timeSelfE").val()==''){
	      $.messager.alert("提示","时间不能为空");
	      return ;
	  }
	$("#list3").datagrid({
		url:"<%=basePath%>/statistics/HospitalDischarge/newHospDisc.action",
		queryParams:{date:$("#timeSelfS").val(),Etime:$("#timeSelfE").val()},
		onLoadSuccess:function(data){
			var date=returnPieDate(data.rows);
			queryPieDate(date,'main3','(自定义)入，出院人次',$("#timeSelfS").val()+'至'+$("#timeSelfE").val());
		}
	});
}
$(function(){
	$('#mySelect').combobox({
		onSelect: function(rec){
			if("HB"==rec.value){
				barByMonthName =  ['入，出院人次','入，出院环比'];
			}else{
				barByMonthName =  ['入，出院人次','入，出院同比'];
			}
			queryContinue($("#timeMonth").val(),'2',rec.value,'monthBar');
			
	      }
	});
	$('#dySelect').combobox({
		onSelect: function(rec){
			if("HB"==rec.value){
				barByMonthName =  ['入，出院人次','入，出院环比'];
			}else{
				barByMonthName =  ['入，出院人次','入，出院同比'];
			}
			queryContinue($("#timeDay").val(),'3',rec.value,'dayBar');
	      }
	});
	$("#timeMonth").val(initMonth);
	$("#timeYear").val(initYear);
	
	pickedFuncByDay();//日查询
	pickedFuncByMonth();//月查询
	pickedFuncByYear();//年查询
	pickedFuncBySelf();//自定义查询
});
//返回数组
function returnPieDate(date){
	var dateArr="[";
	dateArr+="{'value':"+date[0].passengers+",'name':'"+date[0].index+"'},";
	dateArr+="{'value':"+date[1].passengers+",'name':'"+date[1].index+"'}]";
	return eval(dateArr);
}
//饼图数据
function queryPieDate(data,id,tital,tip){//数据
	echarBing(id,data,tital,tip);
}
//柱状图数据
function queryContinue(date,dateSign,type,id){
	$.ajax({
		url:"<%=basePath%>statistics/HospitalDischarge/queryContinue.action",
		data:{date:date,dateSign:dateSign,type:type},
		async:true,
		success:function(data){
			var dateName=[];
			var dateList=[];
			for(var i=0,len=data.length;i<len;i++){
				dateName.push(data[i].date);
				dateList.push(+data[i].num)
			}
			echarZhu(id,dateList,dateName);//柱状图
		}
	});
}
//饼图
 function echarBing(id,dateList,tital,tip){
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
		    	  var myChart = ec.init(document.getElementById(id)); 
		    	  show3=myChart;
			      myChart.showLoading({textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
		    	  var option = {
		    			  title : {
		    			        text: tital,
		    			        subtext: tip,
		    			        x:'left'
		    			    },
		    			    toolbox:{
		    			    	show:true,
		    			    	feature:{
		    			    		restore:{show:true}
		    			    	},
		    			    	y:'bottom'
		    			    },
		    			    tooltip : {
		    			        trigger: 'item',
		    			        formatter: "{b} : <br/>{c} ({d}%)"
		    			    },
		    			    legend: {
		    			        x: 'right',
		    			        data: ['入院','出院'],
		    			    },
		    			    calculable : true,//显示边框
		    			    series : [
		    			        {
		    			            name: '人次',
		    			            type: 'pie',
		    			            radius : '50%',
		    			            center: ['50%', '55%'],
		    			            data:dateList,
		    			            itemStyle:{ 
		    		                    normal:{ 
		    		                        label:{ 
		    		                           show: true, 
		    		                           position:'outter',
		    		                           formatter: "{b}人次"
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
 //柱状图
 function echarZhu(id,dateList,dateName){
	 
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
						        	if("0"!=dateList[params.dataIndex-1]){
						        		nextNum = dateList[params.dataIndex-1];
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
						            data: dateList,
						            barWidth : 20
						        },
						        {
						            name:barByMonthName[1],
						            type:'line',
						            data: dateList
						        }
						    ]
						};
		    	  // 为echarts对象加载数据 
		    	  myChart.setOption(option); 
		    	}
		);
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
							    <!-- 饼状图 -->
							    <div data-options="region:'center',border:false" style="height:50%;width: 100%;">
							    	<div id="main" style="height:100%;width:100%;" ></div>
							    </div>   
							    <!-- 柱状图 -->
							    <div style="height:30px;width:100%;margin-left: 10px">
									<input editable="false" class="easyui-combobox" id="dySelect"
					                   data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
					                   label: '入，出院环比',  
					                   value: 'HB',  
					                   selected:true},  
					                   {label: '入，出院同比',  
					                   value: 'TB'}]"                    
					                 /> 
								</div>
							    <div id="dayBar" style="height:40%;width:100%" ></div>
</div>
<div  class="changeskin" style="width:calc(25% - 1px);float: left;border-top: 0 !important;border-bottom: 0 !important; ">
<!-- 按月统计表 -->
					  	  		<div style="height:35px;width:100%">
					  	  			<table style="width:100%;border:false;padding:5px 5px 5px 5px;">
										<tr>
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
									        </tr>   
									         <tr>   
									            <th data-options="field:'addsLastM',width:'18%'" align="center">增减量</th>
									            <th data-options="field:'addPerLastM',width:'17%'"align="center">增减(%)</th>
									            <th data-options="field:'addsLastY',width:'18%'"align="center">增减量</th>
									            <th data-options="field:'addPerLastY',width:'17%'"align="center">增减(%)</th> 
									        </tr> 
									    </thead>  
									</table> 
							    </div> 
							    <div data-options="region:'center'" style="height:50%;width: 100%;border-top:0">
							    	<div id="main1" style="height:100%;width:100%" ></div>
							    </div>  
								<div style="height:30px;width:100%;margin-left: 10px">
									<input editable="false"  class="easyui-combobox" id="mySelect"
					                   data-options="panelHeight:'80px',valueField: 'value',textField: 'label',data: [{  
					                   label: '入，出院环比',  
					                   value: 'HB',  
					                   selected:true},  
					                   {label: '入，出院同比',  
					                   value: 'TB'}]"                      
					                 /> 
								</div>
							    <div id="monthBar" style="height:40%;width:100%;" ></div> 	
</div>
<div style="width:calc(25% - 1px);float: left;">
<!-- 按年统计表 -->
					  	  		<div style="height:35px;width:100%">
					  	  			<table style="width:100%;border:false;padding:5px 5px 5px 5px;">
										<tr>
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
									        </tr>   
									         <tr>   
									            <th data-options="field:'addsLastM',width:25" align="center">增减量</th>
									            <th data-options="field:'addPerLastM',width:25" align="center">增减（%）</th>
									        </tr> 
									    </thead>   
									</table> 
							    </div>  
							    <div data-options="region:'center'" style="height:50%;width: 100%;border-top:0">
							    	<div id="main2" style="height:100%;width:100%" ></div>
							    </div>   
							    <div id="yearBar" style="height:40%;width:100%;margin-top: 30px" ></div> 
</div>
<div class="changeskin" style="width:calc(25% - 1px);float: left;border-bottom: 0 !important;border-top: 0 !important;">
									<!-- 自定义统计门急诊人次   -->
					  	  		<div style="height:35px;width:100%">
					  	  			<table style="width:100%;border:false;padding:5px 5px 5px 5px;">
										<tr>
											<td>
									 		自定义:
											<input style="width: 100px" id="timeSelfS" class="Wdate" type="text" value="${Stime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:pickedFuncBySelf,maxDate:'#F{$dp.$D(\'timeSelfE\')}'})" />
											至
											<input style="width: 100px" id="timeSelfE" class="Wdate" type="text" value="${Etime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:pickedFuncBySelf,minDate:'#F{$dp.$D(\'timeSelfS\')}',maxDate:'%y-%M-%d'})" />
										</td></tr>
									</table>
					  	  		</div>   
							    <div style="height:150px;width:100%;border-right:0;">
							    	<table id="list3" class="easyui-datagrid" style="height:150px;width:100%;border-right:0;margin-top: 35px" data-options="fitColumns:true,singleSelect:true,fit:true,border:false,scrollbarSize:'0'">   
									    <thead>   
									       <tr >   
									            <th data-options="field:'index',width:50,rowspan:2" align="center">指标</th>   
									            <th data-options="field:'passengers',width:50,rowspan:2" align="center">人次</th>   
									        </tr>   
									         <tr>   
									           
									        </tr>   
									    </thead>   
									</table> 
							    </div>  
							    <div data-options="region:'center'" style="height:50%;width: 100%;border-top:0;margin-bottom : 30px">
							    	<div id="main3" style="height:100%;width:100%" ></div>
							    </div>   
							    <div id="selfBar" style="height:40%;width:100%" ></div>	
</div>
</body>
</html>